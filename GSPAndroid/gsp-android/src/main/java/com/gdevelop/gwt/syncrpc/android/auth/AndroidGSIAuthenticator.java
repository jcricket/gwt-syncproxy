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
package com.gdevelop.gwt.syncrpc.android.auth;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.gdevelop.gwt.syncrpc.HasProxySettings;
import com.gdevelop.gwt.syncrpc.android.R;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticationListener;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;
import com.gdevelop.gwt.syncrpc.auth.TestModeHostVerifier;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.net.URL;

/**
 * Authenticator that uses the GoogleSignIn flow made available in Google Play Services 8.3
 *
 * @author Preethum
 * @version 0.6.2
 * @since 0.6.2 - 11/18/2015
 */
public class AndroidGSIAuthenticator implements ServiceAuthenticator, TestModeHostVerifier {
	public static final int RC_GSI = 9001;
	public static final String LOG_TAG = "AGSI_AUTH";
	Context context;
	FragmentActivity activity;
	Fragment fragment;
	GoogleOAuthIdManager idManager;
	ServiceAuthenticationListener listener;
	String accountName;
	GoogleSignInAccount account;
	boolean prepared = false;
	Mode mode;
	GoogleApiClient mGoogleApiClient = null;
	boolean revoking = false;

	/**
	 * Use case for authentication and this sub-system will query for the user's selected account.
	 * The listener will be called when authentication has been prepared regarding which account was
	 * chosen.
	 */
	private AndroidGSIAuthenticator(Context context, FragmentActivity activity, Fragment fragment, GoogleOAuthIdManager idManager, ServiceAuthenticationListener listener, GoogleSignInAccount account, String accountName, Mode mode) {
		this.context = context;
		this.activity = activity;
		this.fragment = fragment;
		this.idManager = idManager;
		this.listener = listener;
		this.account = account;
		this.accountName = accountName;
		this.mode = mode;
		if (account != null) {
			Log.i(LOG_TAG, "Account provided, prepared");
			prepared = true;
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					return null;
				}

				@Override
				protected void onPostExecute(Void aVoid) {
					super.onPostExecute(aVoid);
					prepareAuthentication();
				}
			}.execute();
		}
	}

	@Override
	public boolean isTestModeHost(URL serviceUrl) {
		String[] whitelist = context.getResources().getStringArray(getTestModeHostArrayResource());
		if (whitelist != null) {
			for (String url : whitelist) {
				if (serviceUrl.getHost().equals(url)) {
					Log.i(LOG_TAG, "Test mode host verified: " + url);
					return true;
				}
			}
		}
		return false;
	}

	protected int getTestModeHostArrayResource() {
		return R.array.gsp_no_ssl_whitelist;
	}

	@Override
	public String accountName() {
		return account == null ? null : account.getEmail();
	}

	@Override
	public boolean isPrepared() {
		return prepared;
	}

	@Override
	public void prepareAuthentication() {
		Log.i(LOG_TAG, "Prepare authenticator");
		if (prepared) {
			Log.d(LOG_TAG, "Already Prepared");
			listener.onAuthenticatorPrepared(this);
			return;
		}
		buildClient();
		mGoogleApiClient.registerConnectionCallbacks(new PreparingCCL());
		//if (mode == Mode.LOGIN_ACC) {
		Log.v(LOG_TAG, "Manually Connecting API Client");
		mGoogleApiClient.connect();
		//}

	}

	private void buildClient() {
		GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(idManager.getServerClientId(context));
		if (accountName != null) {
			Log.v(LOG_TAG, "Setting account name to GSO");
			gsoBuilder.setAccountName(accountName);
		} else if (account != null) {
			Log.v(LOG_TAG, "Setting AR account name to GSO");
			gsoBuilder.setAccountName(account.getEmail());
		}
		// Attempt Silent Sign in first, if fails requiring a sign in, launch intent
		GoogleSignInOptions gso = gsoBuilder.build();
		GoogleApiClient.Builder clientBuilder = new GoogleApiClient.Builder(context).addApi(Auth.GOOGLE_SIGN_IN_API, gso);
//		if (mode == Mode.LOGIN_NEW) {
//			Log.v(LOG_TAG, "Enable GAPI Client Auto Manage");
//			clientBuilder.enableAutoManage(activity, new GoogleApiClient.OnConnectionFailedListener() {
//				@Override
//				public void onConnectionFailed(ConnectionResult connectionResult) {
//					throw new RuntimeException(connectionResult.getErrorMessage());
//				}
//			});
//		}
		mGoogleApiClient = clientBuilder.build();
	}

	public static final String OAUTH_TYPE_HEADER = "X-GSP-OAUTH-TYPE";
	public static final String OAUTH_TYPE_GSI = "GSI";
	@Override
	public void applyAuthenticationToService(HasProxySettings service) {
		if (isPrepared()) {
			service.setOAuth2IdToken(account.getIdToken());
			service.getCustomHeaders().put(OAUTH_TYPE_HEADER, OAUTH_TYPE_GSI);
		}
	}

	private void standardLogIn() {
		Log.d(LOG_TAG, "Attempting silent log in");
		OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
		Log.d(LOG_TAG, "API Client Connected: " + mGoogleApiClient.isConnected());
		if (opr.isDone()) {
			// If the user's cached credentials are valid, the OptionalPendingResult will be "done"
			// and the GoogleSignInResult will be available instantly.
			Log.d(LOG_TAG, "Got cached sign-in");
			GoogleSignInResult result = opr.get();
			account = result.getSignInAccount();
			prepared = true;
			disconnectGoogleApiClient();
			listener.onAuthenticatorPrepared(this);
		} else {
			// If the user has not previously signed in on this device or the sign-in has expired,
			// this asynchronous branch will attempt to sign in the user silently.  Cross-device
			// single sign-on will occur in this branch.
			Log.v(LOG_TAG, "Awaiting OPR Callback");
			opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
				@Override
				public void onResult(GoogleSignInResult googleSignInResult) {
					Log.d(LOG_TAG, "OPR Result");
					if (googleSignInResult.isSuccess()) {
						Log.v(LOG_TAG, "Authenticator prepared");
						account = googleSignInResult.getSignInAccount();
						prepared = true;
						listener.onAuthenticatorPrepared(AndroidGSIAuthenticator.this);
					} else {
//						throw new RuntimeException(googleSignInResult.getStatus().getStatusMessage());
						Log.d(LOG_TAG, "Launching account selection intent");
						Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
						if (fragment != null) {
							fragment.startActivityForResult(signInIntent, RC_GSI);
						} else {
							activity.startActivityForResult(signInIntent, RC_GSI);
						}
					}
					disconnectGoogleApiClient();
				}
			});
		}
	}

	private void disconnectGoogleApiClient() {
		Log.v(LOG_TAG, "Disconnecting GAPI");
		mGoogleApiClient.disconnect();
		mGoogleApiClient = null;
	}

	public void signOut() {
		signOutA();
	}

	protected void signOutA() {
		if (!isPrepared()) {
			throw new RuntimeException("Authenticator must be prepared before it can be signed out");
		}
		Log.d(LOG_TAG, "Building client for signout");
		buildClient();
		mGoogleApiClient.registerConnectionCallbacks(new SignoutCCL());
		Log.d(LOG_TAG, "Connecting client for signout");
		mGoogleApiClient.connect();
	}

	private void signOutProcessor() {
		OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
		Log.d(LOG_TAG, "API Client Connected: " + mGoogleApiClient.isConnected());
		if (opr.isDone()) {
			// If the user's cached credentials are valid, the OptionalPendingResult will be "done"
			// and the GoogleSignInResult will be available instantly.
			Log.d(LOG_TAG, "Got cached sign-in for signout");

			prepared = true;
			if (revoking) {
				revoke();
			} else {
				secondarySignOut();
			}
		} else {
			// If the user has not previously signed in on this device or the sign-in has expired,
			// this asynchronous branch will attempt to sign in the user silently.  Cross-device
			// single sign-on will occur in this branch.
			Log.v(LOG_TAG, "Awaiting OPR Callback for signout");
			opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
				@Override
				public void onResult(GoogleSignInResult googleSignInResult) {
					Log.d(LOG_TAG, "OPR Result fpr signout");
					if (googleSignInResult.isSuccess()) {
						Log.v(LOG_TAG, "Authenticator prepared for signout");
						prepared = true;
						if (revoking) {
							revoke();
						} else {
							secondarySignOut();
						}
					} else {
						disconnectGoogleApiClient();
						throw new RuntimeException(googleSignInResult.getStatus().getStatusMessage());
//						Log.d(LOG_TAG, "Launching account selection intent");
//						Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//						activity.startActivityForResult(signInIntent, RC_GSI);
					}
				}
			});
		}
	}

	private void revoke() {
		Log.i(LOG_TAG, "Attempting revoke");
		Log.v(LOG_TAG, "GAPI Client is connected: " + mGoogleApiClient.isConnected());
		Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
			@Override
			public void onResult(Status status) {
				Log.d(LOG_TAG, "Revoke Completed: " + status.getStatusMessage());
			}
		});
		disconnectGoogleApiClient();
	}

	protected void secondarySignOut() {
		Log.i(LOG_TAG, "Attempting signout secondary");
		Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
			@Override
			public void onResult(Status status) {
				Log.d(LOG_TAG, "SIGN Out Completed: " + status.getStatusMessage());
			}
		});
		disconnectGoogleApiClient();
	}

	public void disconnect() {
		revoking = true;
		signOutA();
	}

	private enum Mode {
		ACTIVITY_RESULT, LOGIN_NEW, LOGIN_ACC, AR_FAIL_REQ, AR_FAIL_SIGNIN;
	}

	public static class Builder {
		FragmentActivity activity;
		Fragment fragment;
		Context context;
		ServiceAuthenticationListener listener;
		GoogleOAuthIdManager idManager;
		AuthenticatorManager manager;
		GoogleSignInResult gsiResult;
		GoogleSignInAccount acct;
		String accountName;
		Mode mode;

		/**
		 * Standard use for building an authenticator
		 *
		 * @param listener  to be called once this authenticator is prepared with the details
		 *                  necessary to apply to any target service
		 * @param idManager containing the server's client id to retrieve and Id Token instead of
		 *                  other credentials
		 */
		public Builder(Context context, ServiceAuthenticationListener listener, GoogleOAuthIdManager idManager) {
			this.context = context;
			this.listener = listener;
			this.idManager = idManager;
		}

		/**
		 * When set, the defined ServiceAuthenticationListener will actually be added to the
		 * manager's call-list
		 */
		public Builder setManager(AuthenticatorManager manager) {
			this.manager = manager;
			return this;
		}

		public Builder onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == RC_GSI) {
				gsiResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
				if (gsiResult.isSuccess()) {
					// Signed in successfully, show authenticated UI.
					acct = gsiResult.getSignInAccount();
					mode = Mode.ACTIVITY_RESULT;
				} else {
					mode = Mode.AR_FAIL_SIGNIN;
				}
			} else {
				mode = Mode.AR_FAIL_REQ;
			}
			return this;
		}

		/**
		 * Use when setting up an inital account selection request
		 */
		public Builder signIn(FragmentActivity activity) {
			mode = Mode.LOGIN_NEW;
			this.activity = activity;
			return this;
		}

		/**
		 * Use when setting up an inital account selection request
		 */
		public Builder signIn(Fragment frag) {
			mode = Mode.LOGIN_NEW;
			this.fragment = frag;
			return this;
		}

		/**
		 * For use case when users has logged in before, and app is re-establishing credneitals to
		 * retrieve token for backend authentication
		 */
		public Builder forAccount(String accountName) {
			mode = Mode.LOGIN_ACC;
			this.accountName = accountName;
			return this;
		}

		public AndroidGSIAuthenticator build() {
			Log.i(LOG_TAG, "Starting build of AGSI");
			if (manager != null) {
//				if (mode != Mode.NON_UI) {
//					throw new RuntimeException("Attaching the Authenticator to a manager services no purpose for a UI mode: " + mode);
//				}
				if (manager.listenFor(accountName, listener)) {
					Log.d(LOG_TAG, "Authenticator for Account: " + accountName + " already in manager, not building new authenticator");
					return (AndroidGSIAuthenticator) manager.get(accountName);
				} else {
					Log.d(LOG_TAG, "Delegating authentication listener to manager for pending preparation");
					listener = new AuthenticatorManager.AMAdder(manager);
				}
			}
			if (mode == null) {
				throw new RuntimeException("You must specify the account source (#signIn, #forAccount, or #onActivityResult)");
			}
			switch (mode) {
				case LOGIN_ACC:
					if (accountName == null) {
						throw new RuntimeException("Must specify a valid account name");
					}
					break;
				case LOGIN_NEW:
					if (activity == null && fragment == null) {
						throw new RuntimeException("Must specifiy A Fragment activity or delegator fragment for a new login to auto-manage");
					}
					break;
				case AR_FAIL_SIGNIN:
					Log.d(LOG_TAG, "Signin not processed");
				case ACTIVITY_RESULT:
					break;
				default:
					throw new RuntimeException("Invalid settings for mode: " + mode);
			}
			return new AndroidGSIAuthenticator(context, activity == null && fragment != null ? fragment.getActivity() : activity, fragment, idManager, listener, acct, accountName, mode);
		}
	}

	private class SignoutCCL implements GoogleApiClient.ConnectionCallbacks {
		@Override
		public void onConnected(Bundle bundle) {
			signOutProcessor();
		}

		@Override
		public void onConnectionSuspended(int i) {
			throw new RuntimeException("Connect suspended: " + i);
		}
	}

	private class PreparingCCL implements GoogleApiClient.ConnectionCallbacks {
		@Override
		public void onConnected(Bundle bundle) {
			Log.i(LOG_TAG, "GAPI Connection for prepartion");
			standardLogIn();
		}

		@Override
		public void onConnectionSuspended(int i) {
			throw new RuntimeException("Connect suspended: " + i);
		}
	}
}