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
package com.gdevelop.gwt.syncrpc.android.auth.gae;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.gdevelop.gwt.syncrpc.HasProxySettings;
import com.gdevelop.gwt.syncrpc.android.R;
import com.gdevelop.gwt.syncrpc.android.auth.GoogleOAuthIdManager;
import com.gdevelop.gwt.syncrpc.auth.HasOAuthIDToken;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticationListener;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;
import com.gdevelop.gwt.syncrpc.auth.TestModeHostVerifier;
import com.gdevelop.gwt.syncrpc.exception.TokenNotAvailableException;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;
import java.net.URL;

/**
 * Uses OAuth 2 Data from a provided Android account to attach ID Tokens to a Google App Engine GWT
 * RPC endpoint services.
 * <p/>
 * Resources: <ol> <li>https://developers.google.com/accounts/docs/CrossClientAuth</li>
 * <li>https://developers.google.com/identity-toolkit/quickstart/android - getting debug key for dev
 * console</li> <li>https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/
 * master/GcmEndpoints</li> <li>https://developers.google.com/console/help/new/#apikeybestpractices</li>
 * </ol>
 *
 * @author Preethum
 * @since 0.6
 */
public class AndroidGAECrossClientAuthenticator extends AsyncTask<Void, Void, String> implements ServiceAuthenticator,
																										 HasOAuthIDToken, TestModeHostVerifier {
	public static final String OAUTH_ID_SCOPE_PREFIX = "audience:server:client_id:";

	public final static int RC_ACCOUNT_CHOOSER_REQUEST = 3032;
	public final static int RC_RECOVER_AUTH_ERROR = 3031;
	public final static int RC_RECOVER_PLAY_SERVICES_ERROR = 3030;
	public static final String LOG_TAG = "AGAECCAuth";
	String accountName;
	boolean actionComplete = false;
	Activity activity;
	Context context;
	GoogleOAuthIdManager idManager;
	String idToken = null;
	Exception pendingException;
	boolean scheduled = false;
	private Fragment actResultDelegate;
	private ServiceAuthenticationListener listener;
	private int rcAccountChooser = RC_ACCOUNT_CHOOSER_REQUEST;
	private int rcRecoverAuth = RC_RECOVER_AUTH_ERROR;
	private int rcRecoverPlayServices = RC_RECOVER_PLAY_SERVICES_ERROR;

	/**
	 * Use case for performing authentication within a {@link Fragment}, which expects to handle the
	 * {@link #onActivityResult(int, int, Intent)}
	 */
	public AndroidGAECrossClientAuthenticator(Activity activity, Fragment actResultDelegate,											  GoogleOAuthIdManager idManager, ServiceAuthenticationListener listener) {
		this(activity, idManager, listener);
		this.actResultDelegate = actResultDelegate;
	}

	/**
	 * Use case for authentication and this sub-system will query for the user's selected account.
	 * The listener will be called when authentication has been prepared regarding which account was
	 * chosen.
	 */
	public AndroidGAECrossClientAuthenticator(Activity activity, GoogleOAuthIdManager idManager,											  ServiceAuthenticationListener listener) {
		this.context = activity;
		this.activity = activity;
		this.idManager = idManager;
		this.listener = listener;
	}

	/**
	 * Use case non-Activity, non-Fragment authentication preparation for the specified account
	 *
	 * @param account   to gain authentication details about
	 * @param idManager containing the server's client id to retrieve and Id Token instead of other
	 *                  credentials
	 * @param listener  to be called once this authenticator is prepared with the details necessary
	 *                  to apply to any target service
	 */
	public AndroidGAECrossClientAuthenticator(Context context, Account account, GoogleOAuthIdManager idManager,
											  ServiceAuthenticationListener listener) {
		this.context = context;
		this.accountName = account.name;
		this.idManager = idManager;
		this.listener = listener;
	}

	/**
	 * Handles activity results relating to the {@link GoogleAuthUtil} service.
	 *
	 * @return true if this class was able to handle the result
	 * @throws IOException
	 * @throws GoogleAuthException
	 */
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) throws IOException,																								 GoogleAuthException {
		if ((requestCode == rcRecoverPlayServices || requestCode == rcRecoverAuth) && resultCode == Activity.RESULT_OK) {
			// Receiving a result that follows a GoogleAuthException, try auth
			// again
			prepareAuthenticationL();
			return true;
		} else if (requestCode == rcAccountChooser && resultCode == Activity.RESULT_OK) {
			accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			prepareAuthenticationL();
			return true;
		}
		return false;
	}

	/**
	 * Makes sure details necessary for authentication are available before undergoing preparation
	 * measures. Specifically, an account name must be specified, and if not an activity must be
	 * available to query the user for which account to utilize
	 * <p/>
	 * TODO Check network connectivity to handle IOException errors
	 * <p/>
	 * TODO Customize the newChooseAccountIntent parameters
	 *
	 * @throws IOException
	 * @throws GoogleAuthException for Unrecoverable errors or if no activity was provided to
	 *                             auto-handle these errors
	 */
	protected void prepareAuthenticationL() throws IOException, GoogleAuthException {
		if (scheduled) {
			return;
		}
		if (actionComplete) {
			listener.onAuthenticatorPrepared(accountName);
			return;
		}

		if (accountName == null && activity != null) {
			Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
			if (actResultDelegate != null) {
				actResultDelegate.startActivityForResult(googlePicker, rcAccountChooser);
			} else {
				activity.startActivityForResult(googlePicker, rcAccountChooser);
			}
			return;
		} else if (accountName == null) {
			throw new RuntimeException("Must provide an activity for account choosing or provide a valid account");
		}
		scheduled = true;
		execute();
	}

	@Override
	public void prepareAuthentication() {
		try {
			prepareAuthenticationL();
		} catch (GoogleAuthException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Applies the OAuth 2 Id Token (OpenId Connect)
	 */
	@Override
	public void applyAuthenticationToService(HasProxySettings service) {
		try {
			service.setOAuth2IdToken(getOAuthIDToken());
		} catch (TokenNotAvailableException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getOAuthIDToken() throws TokenNotAvailableException {
		if (!actionComplete) {
			throw new TokenNotAvailableException();
		}
		return idToken;
	}

	public void setRcAccountChooser(int rcAccountChooser) {
		this.rcAccountChooser = rcAccountChooser;
	}

	public void setRcRecoverAuth(int rcRecoverAuth) {
		this.rcRecoverAuth = rcRecoverAuth;
	}

	public void setRcRecoverPlayServices(int rcRecoverPlayServices) {
		this.rcRecoverPlayServices = rcRecoverPlayServices;
	}

	/**
	 * Performs the preparation measures, specifically retrieving the server-usable OAuth 2.0 Id
	 * Token which must be sent to the server
	 *
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected String doInBackground(Void... params) {
		try {
			idToken = GoogleAuthUtil.getToken(context, accountName, OAUTH_ID_SCOPE_PREFIX + idManager.getServerClientId(context));
			return idToken;
		} catch (GooglePlayServicesAvailabilityException gpsae) {
			// The Google Play services APK is old, disabled, or not present.
			// Show a dialog created by Google Play services that allows
			// the user to update the APK
			if (activity != null) {
				int statusCode = gpsae.getConnectionStatusCode();
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, activity, rcRecoverPlayServices);
				dialog.show();
				return null;
			}
			pendingException = gpsae;
		} catch (UserRecoverableAuthException urae) {
			if (activity != null) {
				if (actResultDelegate != null) {
					actResultDelegate.startActivityForResult(urae.getIntent(), rcRecoverAuth);
				} else {
					activity.startActivityForResult(urae.getIntent(), rcRecoverAuth);
				}
				return null;
			}
			pendingException = urae;
		} catch (Exception e) {
			pendingException = e;
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		pendingException = null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result == null) {
			if (pendingException != null) {
				throw new RuntimeException(pendingException);
			}
			// If no pendingException, then system was able to recover
			// automatically to gain further information
			return;
		}
		actionComplete = true;
		if (listener != null) {
			listener.onAuthenticatorPrepared(accountName);
		}
	}

	protected int getTestModeHostArrayResource(){
		return R.array.gsp_no_ssl_whitelist;
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
}
