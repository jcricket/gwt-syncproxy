package com.gdevelop.gwt.syncrpc.android.auth.gae;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.gdevelop.gwt.syncrpc.HasProxySettings;
import com.gdevelop.gwt.syncrpc.android.auth.GoogleOAuthIdManager;
import com.gdevelop.gwt.syncrpc.android.auth.HasOAuthIDToken;
import com.gdevelop.gwt.syncrpc.android.auth.ServiceAuthenticationListener;
import com.gdevelop.gwt.syncrpc.android.auth.ServiceAuthenticator;
import com.gdevelop.gwt.syncrpc.android.auth.TokenNotAvailableException;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Uses OAuth 2 Data from a provided Android account to attach ID Tokens to a
 * Google App Engine GWT RPC endpoint services.
 *
 * https://developers.google.com/accounts/docs/CrossClientAuth
 *
 * https://developers.google.com/identity-toolkit/quickstart/android - getting
 * debug key for dev console
 *
 * https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master
 * /GcmEndpoints
 *
 * https://developers.google.com/console/help/new/#apikeybestpractices
 *
 * @author Preethum
 * @since 0.6
 *
 */
public class AndroidGAECrossClientAuthenticator extends
		AsyncTask<Void, Void, String> implements ServiceAuthenticator,
		HasOAuthIDToken {
	public static final String OAUTH_ID_SCOPE_PREFIX = "audience:server:client_id:";

	public final static int RC_RECOVER_PLAY_SERVICES_ERROR = 3030;
	public final static int RC_RECOVER_AUTH_ERROR = 3031;
	public final static int RC_ACCOUNT_CHOOSER_REQUEST = 3032;

	public void setRcRecoverPlayServices(int rcRecoverPlayServices) {
		this.rcRecoverPlayServices = rcRecoverPlayServices;
	}

	public void setRcRecoverAuth(int rcRecoverAuth) {
		this.rcRecoverAuth = rcRecoverAuth;
	}

	public void setRcAccountChooser(int rcAccountChooser) {
		this.rcAccountChooser = rcAccountChooser;
	}

	private int rcRecoverPlayServices = RC_RECOVER_PLAY_SERVICES_ERROR;
	private int rcRecoverAuth = RC_RECOVER_AUTH_ERROR;
	private int rcAccountChooser = RC_ACCOUNT_CHOOSER_REQUEST;

	Context context;
	GoogleOAuthIdManager idManager;
	Activity activity;
	String accountName;

	private Fragment actResultDelegate;

	/**
	 * Use case non-Activity, non-Fragment authentication preparation for the
	 * specified account
	 *
	 * @param account
	 *            to gain authentication details about
	 * @param idManager
	 *            containing the server's client id to retrieve and Id Token
	 *            instead of other credentials
	 * @param listener
	 *            to be called once this authenticator is prepared with the
	 *            details necessary to apply to any target service
	 */
	public AndroidGAECrossClientAuthenticator(Context context, Account account,
			GoogleOAuthIdManager idManager,
			ServiceAuthenticationListener listener) {
		this.context = context;
		this.accountName = account.name;
		this.idManager = idManager;
		this.listener = listener;
	}

	/**
	 * Use case for authentication and this sub-system will query for the user's
	 * selected account. The listener will be called when authentication has
	 * been prepared regarding which account was chosen.
	 */
	public AndroidGAECrossClientAuthenticator(Activity activity,
			GoogleOAuthIdManager idManager,
			ServiceAuthenticationListener listener) {
		this.context = activity;
		this.activity = activity;
		this.idManager = idManager;
		this.listener = listener;
	}

	/**
	 * Use case for performing authentication within a {@link Fragment}, which
	 * expects to handle the {@link #onActivityResult(int, int, Intent)}
	 */
	public AndroidGAECrossClientAuthenticator(Activity activity,
			Fragment actResultDelegate, GoogleOAuthIdManager idManager,
			ServiceAuthenticationListener listener) {
		this(activity, idManager, listener);
		this.actResultDelegate = actResultDelegate;
	}

	/**
	 * Handles activity results relating to the {@link GoogleAuthUtil} service.
	 *
	 * @return true if this class was able to handle the result
	 * @throws IOException
	 * @throws GoogleAuthException
	 */
	public boolean onActivityResult(int requestCode, int resultCode, Intent data)
			throws IOException, GoogleAuthException {
		if ((requestCode == rcRecoverPlayServices || requestCode == rcRecoverAuth)
				&& resultCode == Activity.RESULT_OK) {
			// Receiving a result that follows a GoogleAuthException, try auth
			// again
			prepareAuthenticationL();
			return true;
		} else if (requestCode == rcAccountChooser
				&& resultCode == Activity.RESULT_OK) {
			accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			prepareAuthenticationL();
			return true;
		}
		return false;
	}

	String idToken = null;
	boolean actionComplete = false;

	private ServiceAuthenticationListener listener;

	/**
	 * Makes sure details necessary for authentication are available before
	 * undergoing preparation measures. Specifically, an account name must be
	 * specified, and if not an activity must be available to query the user for
	 * which account to utilize
	 *
	 * TODO Check network connectivity to handle IOException errors
	 *
	 * TODO Customize the newChooseAccountIntent parameters
	 *
	 *
	 * @throws IOException
	 * @throws GoogleAuthException
	 *             for Unrecoverable errors or if no activity was provided to
	 *             auto-handle these errors
	 */
	protected void prepareAuthenticationL() throws IOException,
			GoogleAuthException {
		if (actionComplete) {
			listener.onAuthenticatorPrepared(accountName);
			return;
		}

		if (accountName == null && activity != null) {
			Intent googlePicker = AccountPicker.newChooseAccountIntent(null,
					null, new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE },
					true, null, null, null, null);
			if (actResultDelegate != null) {
				actResultDelegate.startActivityForResult(googlePicker,
						rcAccountChooser);
			} else {
				activity.startActivityForResult(googlePicker, rcAccountChooser);
			}
			return;
		} else if (accountName == null) {
			throw new RuntimeException(
					"Must provide an activity for account choosing or provide a valid account");
		}

		execute();
	}

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

	@Override
	public void prepareAuthentication() {
		try {
			prepareAuthenticationL();
		} catch (GoogleAuthException | IOException e) {
			throw new RuntimeException(e);
		}
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

	@Override
	protected void onPreExecute() {
		pendingException = null;
	}

	Exception pendingException;

	/**
	 * Performs the preparation measures, specifically retrieving the
	 * server-usable OAuth 2.0 Id Token which must be sent to the server
	 *
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected String doInBackground(Void... params) {
		try {
			idToken = GoogleAuthUtil.getToken(
					context,
					accountName,
					OAUTH_ID_SCOPE_PREFIX
							+ idManager.getServerClientId(context));
			return idToken;
		} catch (GooglePlayServicesAvailabilityException gpsae) {
			// The Google Play services APK is old, disabled, or not present.
			// Show a dialog created by Google Play services that allows
			// the user to update the APK
			if (activity != null) {
				int statusCode = gpsae.getConnectionStatusCode();
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
						statusCode, activity, rcRecoverPlayServices);
				dialog.show();
				return null;
			}
			pendingException = gpsae;
		} catch (UserRecoverableAuthException urae) {
			if (activity != null) {
				if (actResultDelegate != null) {
					actResultDelegate.startActivityForResult(urae.getIntent(),
							rcRecoverAuth);
				} else {
					activity.startActivityForResult(urae.getIntent(),
							rcRecoverAuth);
				}
				return null;
			}
			pendingException = urae;
		} catch (Exception e) {
			pendingException = e;
		}
		return null;
	}
}
