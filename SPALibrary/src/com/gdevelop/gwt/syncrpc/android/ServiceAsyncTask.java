package com.gdevelop.gwt.syncrpc.android;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.HasProxySettings;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.android.auth.ServiceAuthenticator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public abstract class ServiceAsyncTask<AsyncService, ReturnType> extends
AsyncTask<Void, Void, Void> {

	private AsyncCallback<ReturnType> primaryCallback;
	private ServiceAuthenticator authenticator;

	public <ServiceClass extends RemoteService> ServiceAsyncTask(
			Class<ServiceClass> clazz,
			AsyncCallback<ReturnType> primaryCallback,
			ServiceAuthenticator authenticator) {
		this.serviceClass = (Class<RemoteService>) clazz;
		this.primaryCallback = primaryCallback;
		this.authenticator = authenticator;
	}

	private Class<RemoteService> serviceClass;
	private AsyncService asyncService;
	private BridgeCallback<ReturnType> callback = new BridgeCallback<ReturnType>();

	protected AsyncService getAsyncService() {
		return asyncService;
	}

	protected AsyncCallback<ReturnType> getCallback() {
		return callback;
	}

	@Override
	protected Void doInBackground(Void... params) {
		for (Class<?> clazz : SyncProxy.getLoggerClasses()) {
			FixedAndroidHandler.setupLogger(Logger.getLogger(clazz.getName()));
		}
		SyncProxy.setLoggingLevel(Level.FINE);
		SyncProxy.setBaseURL("https://192.168.1.107:8080/spawebtest/");

		asyncService = SyncProxy.create(serviceClass);

		authenticator
		.applyAuthenticationToService((HasProxySettings) asyncService);
		serviceCall();
		try {
			((BridgeCallback) getCallback()).await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (callback.wasSuccessful()) {
			primaryCallback.onSuccess(callback.getResult());
		} else {
			callback.getCaught().printStackTrace();
			primaryCallback.onFailure(callback.getCaught());
		}
	}

	public abstract void serviceCall();

}
