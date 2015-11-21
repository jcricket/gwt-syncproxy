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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

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
	GoogleOAuthIdManager idManager;
	ServiceAuthenticationListener listener;
	String accountName;
	GoogleSignInAccount account;
	boolean prepared = false;
	GoogleApiClient mGoogleApiClient = null;
	/**
	 * Use case for authentication and this sub-system will query for the user's selected account.
	 * The listener will be called when authentication has been prepared regarding which account was
	 * chosen.
	 */
	private AndroidGSIAuthenticator(Context context, FragmentActivity activity, GoogleOAuthIdManager idManager, ServiceAuthenticationListener listener, GoogleSignInAccount account, String accountName) {
		this.context = context;
		this.activity = activity;
		this.idManager = idManager;
		this.listener = listener;
		this.account = account;
		this.accountName = accountName;
		if (account != null) {
			Log.i(LOG_TAG, "Account provided, prepared");
			prepared = true;
			listener.onAuthenticatorPrepared(this);
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
		GoogleSignInOptions.Builder gsoBuilder;

		if (accountName != null) {
			Log.v(LOG_TAG, "Setting accounting name to GSO");
			gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).setAccountName(accountName);
		} else {
			gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN);
		}
		//  Setup activity and failed listener
		if (mGoogleApiClient == null) {
			GoogleSignInOptions gso = gsoBuilder.requestEmail().requestIdToken(idManager.getServerClientId(context)).build();
			mGoogleApiClient = new GoogleApiClient.Builder(context).enableAutoManage(activity /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
				@Override
				public void onConnectionFailed(ConnectionResult connectionResult) {
					throw new RuntimeException(connectionResult.getErrorMessage());
				}
			} /* OnConnectionFailedListener */).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
			Log.d(LOG_TAG, "API Client Connected: " + mGoogleApiClient.isConnected());
			Log.d(LOG_TAG, "Launching account selection intent");
			Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
			activity.startActivityForResult(signInIntent, RC_GSI);
		} else {
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
				listener.onAuthenticatorPrepared(this);
			} else {
				// If the user has not previously signed in on this device or the sign-in has expired,
				// this asynchronous branch will attempt to sign in the user silently.  Cross-device
				// single sign-on will occur in this branch.
				Log.v(LOG_TAG, "Awaiting OPR Callacbk");
				opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
					@Override
					public void onResult(GoogleSignInResult googleSignInResult) {
						Log.d(LOG_TAG, "OPR Result, authenticator prepared");
						account = googleSignInResult.getSignInAccount();
						prepared = true;
						listener.onAuthenticatorPrepared(AndroidGSIAuthenticator.this);
					}
				});
			}
		}
	}

	@Override
	public void applyAuthenticationToService(HasProxySettings service) {
		if (isPrepared()) {
			service.setOAuth2IdToken(account.getIdToken());
		}
	}

	public static class Builder {
		FragmentActivity activity;
		Context context;
		ServiceAuthenticationListener listener;
		GoogleOAuthIdManager idManager;
		AuthenticatorManager manager;
		GoogleSignInResult gsiResult;
		GoogleSignInAccount acct;
		String accountName;

		/**
		 * Standard use for building an authenticator
		 *
		 * @param listener  to be called once this authenticator is prepared with the details
		 *                  necessary to apply to any target service
		 * @param idManager containing the server's client id to retrieve and Id Token instead of
		 *                  other credentials
		 */
		public Builder(FragmentActivity activity, ServiceAuthenticationListener listener, GoogleOAuthIdManager idManager) {
			this.context =activity;
			this.activity = activity;
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
				}
			}
			return this;
		}

		/**
		 * Use when setting up an inital account selection request
		 */
		public Builder signIn() {
			return this;
		}

		/**
		 * For use case when users has logged in before, and app is re-establishing credneitals to
		 * retrieve token for backend authentication
		 */
		public Builder forAccount(String accountName) {
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
			return new AndroidGSIAuthenticator(context, activity, idManager, listener, acct, accountName);
		}
	}
}
