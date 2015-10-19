/**
 * Copyright 2015 Blue Esoteric Web Development, LLC <http://www.blueesoteric.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc.android;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.StringRes;
import android.util.Log;

import com.gdevelop.gwt.syncrpc.HasProxySettings;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.android.auth.AuthenticatorManager;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticationListener;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Helper class to make RPC's quicker and easier. Specifically, Android requires that all network
 * calls occur away from the main thread. This includes the {@link SyncProxy#setBaseURL(String)}
 * call as well as the actual service RPC. Additionally, if you do any UI work based on the returned
 * value, Android requires that this work occur back on the main thread. This class solves both
 * problems. It automatically takes care of {@link SyncProxy#setBaseURL(String)} and by being
 * provided with the typical {@link AsyncCallback} directly, it will return results (success or
 * failure) to your {@link AsyncCallback} on the main thread.
 * <p/>
 * To use this class, provide the specified parameters in the constructor and override the {@link
 * #serviceCall()} method. Specifically, your override should follow the pattern below, where
 * [Params ...] is the parameters to send to the RPC. The {@link #getAsyncService()} and {@link
 * #getCallback()} are critical to the described functionality.
 * <p/>
 * <pre>
 * {@code
 * getAsyncService().rpcMethodCall([Params ...,] getCallback());
 * }
 * </pre>
 * <p/>
 * You may also override {@link #onProgressUpdate(Object...)} in order to understand where in the
 * process the task is. The progress is defined by {@link ServiceTaskProgress}.
 *
 * @param <AsyncService> the AsyncService class that will be utilized for the RPC
 * @param <ReturnType>   the type expected to be returned from the RPC
 * @author Preethum
 * @version 0.6.1
 * @since 0.6
 */
public abstract class ServiceAsyncTask<AsyncService, ReturnType> extends AsyncTask<Void, ServiceTaskProgress, Void> {
	public static final String LOG_ID = "GSP_ASYNCTASK";
	AuthenticatorManager manager;
	private AsyncService asyncService;
	private ServiceAuthenticator authenticator;
	private BridgeCallback<ReturnType> callback;
	private AsyncCallback<ReturnType> primaryCallback;
	private int rpcBaseRes = -1;
	private String baseUrl;
	private Class<RemoteService> serviceClass;
	private Context context;
	private Exception exception;
	private String exceptionMsg;

	/**
	 * Although builder pattern is available, this method remains public for instances of anonymous
	 * classes
	 *
	 * @param rpcBaseRes resource with a fully formed url with module path, ex: https://mytestsite.com/mymodule or http://192.168.1.100:8888/mymodule
	 * @since 0.6.1
	 */
	public <ServiceClass extends RemoteService> ServiceAsyncTask(Class<ServiceClass> clazz, Context context, @StringRes int rpcBaseRes, ServiceAuthenticator authenticator, AsyncCallback<ReturnType> primaryCallback, AuthenticatorManager manager) {
		this.serviceClass = (Class<RemoteService>) clazz;
		this.context = context;
		this.rpcBaseRes = rpcBaseRes;
		this.authenticator = authenticator;
		this.primaryCallback = primaryCallback;
		this.manager = manager;
		if (manager != null) {
			Log.v(LOG_ID, "Manager set: " + manager.toString());
		}
		onProgressUpdate(ServiceTaskProgress.INIT);
	}

	/**
	 * @see #executeForAccount(String)
	 * @since 0.6.1
	 */
	public void executeForAccount(Account account) {
		executeForAccount(account.name);
	}

	/**
	 * Executes when an authenticator is available for the specified account in the declared
	 * AuthenticatorManager
	 *
	 * @since 0.6.1
	 */
	public void executeForAccount(String accName) {
		if (manager == null) {
			throw new RuntimeException("Cannot execute for account without an AuthenticationManager being specified");
		}
		// Wait for account authenticator to become available
		manager.listenFor(accName, new ServiceAuthenticationListener() {
			@Override
			public void onAuthenticatorPrepared(ServiceAuthenticator authenticator) {
				Log.d(LOG_ID, "Authenticator available for: " + authenticator.accountName());
				setAuthenticator(authenticator);
				execute();
			}
		});
		Log.i(LOG_ID, getClass().getSimpleName() + " is Waiting for authenticator for account: " + accName + " in manager: " + manager.toString());
	}

	public Context getContext() {
		return context;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setPrimaryCallback(AsyncCallback<ReturnType> primaryCallback) {
		this.primaryCallback = primaryCallback;
	}

	/**
	 * Overrides the Default timeout of the intermediary {@link AsyncCallback}, specified as {@link
	 * BridgeCallback#DEFAULT_TIMEOUT}.
	 */
	public void setTimeout(int seconds) {
		((BridgeCallback<ReturnType>) getCallback()).setTimeout(seconds);
	}

	/**
	 * @return an intermediate AsyncCallback which will hold continued processing until the callback
	 * has returned. This should be used in the {@link #serviceCall()} implementation to allow
	 * callback's to occur on the main thread for UI Updates
	 */
	protected AsyncCallback<ReturnType> getCallback() {
		if (callback == null) {
			callback = new BridgeCallback<>();
		}
		return callback;
	}

	/**
	 * Performs the following functions: <ol> <li>Calls {@link SyncProxy#setBaseURL(String)}</li>
	 * <li>Creates the AsyncService (assuming first call to {@link #getAsyncService()})</li>
	 * <li>Applies the {@link ServiceAuthenticator} if one was provided <li> <li>Calls {@link
	 * #serviceCall()} in order to perform the RPC</li> </ol>
	 */
	@Override
	protected Void doInBackground(Void... params) {
		try {
			if (requiresAuthenticator() && getAuthenticator() == null) {
				cancel(true);
				throw new RuntimeException("No authenticator available for required method");
			}
		} catch (Exception e) {
			exceptionMsg = "Authenticator missing. Set Authenticator or #executeForAccount(Account)";
			exception = e;
		}
		try {
			verifyBaseUrl();
		} catch (Exception e) {
			exceptionMsg = "Problem verifying the Base URL";
			exception = e;
		}
		if (isCancelled()) {
			return null;
		}
//		// Activates GSP Logging on Android
//		for (Class<?> clazz : SyncProxy.getLoggerClasses()) {
//			FixedAndroidHandler.setupLogger(Logger.getLogger(clazz.getName()));
//		}
//		SyncProxy.setLoggingLevel(Level.FINER);
		SyncProxy.setBaseURL(baseUrl);
		publishProgress(ServiceTaskProgress.BASE_SET);
		if (isCancelled()) {
			return null;
		}
		// Initiate creation of the service
		getAsyncService();
		publishProgress(ServiceTaskProgress.SERVICE_CREATED);
		if (isCancelled()) {
			return null;
		}
		// Need to handle any exceptions from the user's definition of a serviceCall
		try {
			serviceCall();
		} catch (Exception e) {
			exception = e;
			exceptionMsg = "Problem occurred during user defined #serviceCall()";
			cancel(true);
			return null;
		}
		publishProgress(ServiceTaskProgress.SERVICE_CALLED);
		if (isCancelled()) {
			return null;
		}
		// Wait for the service call to complete before passing results back to
		// primaryCallback in onPostExecute
		try {
			((BridgeCallback<ReturnType>) getCallback()).await();
			publishProgress(ServiceTaskProgress.RPC_COMPLETE);
		} catch (InterruptedException e) {
			publishProgress(ServiceTaskProgress.RPC_INTERRUPTED);
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * This method can be overridden by sub-classes to ensure that the task is not executed without
	 * an authenticator or authentication manager being available. While it defaults to false, if
	 * this task is executed with the executeForAccount method or if an authenticator is available,
	 * it will be utilized even if the method does note "require" an authenticator as indicated by
	 * this method.
	 */
	protected boolean requiresAuthenticator() {
		return false;
	}

	/**
	 * Separated as a pre-execution check for the Base url in case a descendent AsynkTask chooses to
	 * set the URL as part of the execution pattern
	 */
	protected void verifyBaseUrl() {
		if (baseUrl == null) {
			if (rpcBaseRes <= 0) {
				Log.e(LOG_ID, "No Base url defined or resolvable: " + getClass().getName());
				cancel(true);
				throw new IllegalStateException("Unable to execute asyncTask due to invalid baseUrl");
			}
			if (context == null) {
				Log.e(LOG_ID, "No Context available to resolve rpcBase resource");
				cancel(true);
				throw new IllegalArgumentException("Context not available for resource resolution");
			}
			baseUrl = context.getString(rpcBaseRes);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (primaryCallback == null) {
			Log.e(LOG_ID, "No AsyncCallback method provided. Must be defined.");
			cancel(true);
			throw new RuntimeException("Unable to execute due to missing AsyncCallback");
		}
	}

	/**
	 * Returns results or exceptions from RPC back to provided {@link AsyncCallback} on the app's
	 * main thread
	 */
	@Override
	protected void onPostExecute(Void result) {
		if (callback.wasSuccessful()) {
			primaryCallback.onSuccess(callback.getResult());
		} else {
			primaryCallback.onFailure(callback.getCaught());
		}
		onProgressUpdate(ServiceTaskProgress.TASK_COMPLETE);
	}

	/**
	 * Handles when the tasks is canceled. If the cancellation occurred due to an exception that is
	 * reported by #getCancelledException, then that exception is sent to the
	 * AsyncCallback#onFailure method
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
		onProgressUpdate(ServiceTaskProgress.TASK_CANCELED);
		Exception exc = getCancelledException();
		if (exc != null) {
			primaryCallback.onFailure(exc);
		}
	}

	/**
	 * Can be overridden by sub-classes to provided localized exception that are handled and should
	 * be sent back to the AsyncCallback#onFailure method. If the exception's resulted in a
	 * cancelation of this task, it is best handled to wrap that exception in a
	 * ServiceTaskCanceledException that can be uniformly handled by the callback
	 */
	protected Exception getCancelledException() {
		return new ServiceTaskCanceledException(exceptionMsg, exception);
	}

	/**
	 * Contains the service RPC to call. Typically this should have the form:
	 * <p/>
	 * <pre>
	 * {@code
	 * getAsyncService().rpcMethodCall([Params], getCallback());
	 * }
	 * </pre>
	 */
	public abstract void serviceCall();

	/**
	 * May be overridden if you need to customize the creation of the service. This default method
	 * implementation creates the service and applies the provided authenticator
	 */
	protected AsyncService getAsyncService() {
		if (asyncService == null) {
			asyncService = SyncProxy.create(serviceClass);
			((HasProxySettings) asyncService).setServiceAuthenticator(authenticator);
		}
		return asyncService;
	}

	public ServiceAuthenticator getAuthenticator() {
		return authenticator;
	}

	protected void setAuthenticator(ServiceAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	/**
	 * Abstract builder to help descendent classes build this hierarchy
	 *
	 * @param <AsyncService>
	 * @param <ReturnType>
	 * @since 0.6.1
	 */
	public abstract static class Builder<AsyncService, ReturnType> {
		Class<?> clazz;
		Context context;
		ServiceAuthenticator authenticator;
		AsyncCallback<ReturnType> primaryCallback;
		AuthenticatorManager manager;
		@StringRes
		int rpcBaseRes = -1;

		public <ServiceClass extends RemoteService> Builder(Class<ServiceClass> clazz, Context context) {
			this.clazz = clazz;
			this.context = context;
		}

		/**
		 * @param authenticator the authenticator that will be applied to the service before the
		 *                      RPC
		 */
		public void setAuthenticator(ServiceAuthenticator authenticator) {
			this.authenticator = authenticator;
		}

		/**
		 * @param primaryCallback the callback that will handle the success or failure returned by
		 *                        the RPC
		 */
		public void setPrimaryCallback(AsyncCallback<ReturnType> primaryCallback) {
			this.primaryCallback = primaryCallback;
		}

		/**
		 * @param rpcBaseRes the resource string that specifies the Server's Base URL, for use with
		 *                   {@link SyncProxy#setBaseURL(String)}
		 */
		public void setRpcBaseRes(int rpcBaseRes) {
			this.rpcBaseRes = rpcBaseRes;
		}

		public void setManager(AuthenticatorManager manager) {
			this.manager = manager;
		}

		public abstract ServiceAsyncTask<AsyncService, ReturnType> build();

	}
}
