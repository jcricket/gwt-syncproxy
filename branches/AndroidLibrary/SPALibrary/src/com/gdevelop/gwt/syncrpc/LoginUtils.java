/*
 * Copyright 2013 Blue Esoteric Web Development, LLC (http://www.blueesoteric.com/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 *  See Android wiki (https://code.google.com/p/gwt-syncproxy/wiki/Android) for 
 *  coding details. This android interface was created from reviewing and integrating
 *  ideas found from: http://blog.notdot.net/2010/05/Authenticating-against-App-Engine-from-an-Android-app.
 *  This is a modified LoginUtils.java file from the original SyncProxy project.
 */
package com.gdevelop.gwt.syncrpc;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.gdevelop.gwt.syncrpc.android.AccountList;
import com.gdevelop.gwt.syncrpc.android.CookieManagerAvailableListener;
import com.gdevelop.gwt.syncrpc.android.GAEAMCallback;
import com.gdevelop.gwt.syncrpc.android.GetCookieRunnable;

/**
 * This is a specially designed call to login to App Engine using the sync-proxy
 * system and android's built in account mgmt system. Activity that calls
 * {@link #loginAppEngine(Activity, CookieManagerAvailableListener, Account, String, String, String)}
 * will need to implement the following method:
 * <p>
 * <code>
 * protected void onActivityResult(int requestCode, int resultCode,
 *        Intent data) {
 *    if (requestCode == MY_REQUEST_ID) {
 *        if (resultCode == RESULT_OK) {
 *          Account account = (Account) data.getExtras().get(LoginUtils.ACCOUNT_KEY);
 *          LoginUtils.loginAppEngine(this,listener,account,null);
 *        }
 *    }
 * }
 * <code>
 * </p>
 * This method allows the system to react when the user must choose from a list
 * of accounts. MY_REQUEST_ID is the value that should be passed into
 * #loginAppEngine()<br />
 * <br />
 * In order to handle account token resets and lifecycle management, every
 * activity that will be making a call to GAE needs to call the
 * {@link #loginAppEngine(Activity, CookieManagerAvailableListener, Account, String)}
 * method from it's onResume().
 * 
 * @author Preethum
 * @since 0.4
 * 
 */
public class LoginUtils {
	public static final String GAE_SERVICE_NAME = "ah";
	public static final String LOCAL_DEV_MODE_FLAG = "DevModeFlag";
	public static final String ACCOUNT_KEY = "account";
	public static final String GOOGLE_ACCOUNT_TYPE = "com.google";
	public static final String TEST_ACCOUNT_TYPE = "test.test";
	private static String loginUrl = null;
	static boolean localDevMode = false;
	public static boolean useAccountSelector = false;

	public static void chooseAccount(Activity parent, int requestCode) {
		Intent intent = new Intent(parent, AccountList.class);
		intent.putExtra(LOCAL_DEV_MODE_FLAG, localDevMode);
		parent.startActivityForResult(intent, requestCode);
		return;
	}

	public static void loginAppEngine(Activity parent,
			CookieManagerAvailableListener listener, Account account)
			throws IOException, AccountsException {
		loginAppEngine(parent, listener, account, -1);
	}

	/**
	 * @param listener
	 *            will send back the CookieManager to be used in subsequent
	 *            calls
	 * @param account
	 *            is the account with which you want to login
	 * 
	 * @param requestCode
	 *            is the code that will be used to call back to the calling
	 *            activity if the user needed to select an account to use and
	 *            must be != -1.
	 * @return The CookieManager for subsequence call
	 * @throws IOException
	 */
	public static void loginAppEngine(final Activity parent,
			final CookieManagerAvailableListener listener,
			final Account account, int requestCode) throws IOException,
			AccountsException {
		if (account == null) {
			if (useAccountSelector && requestCode != -1) {
				chooseAccount(parent, requestCode);
				return;
			} else if (useAccountSelector && requestCode == -1) {
				throw new AccountsException(
						"Provided Request code cannot be equal to -1");
			} else {
				throw new AccountsException(
						"Account needs to be provided to login to App Engine.");
			}
		}
		if (localDevMode) {
			loginUrl += "/_ah/login";
			URL url = new URL(loginUrl);
			String email = URLEncoder.encode(account.name, "UTF-8");
			String serviceUrl = URLEncoder.encode("nowhere", "UTF-8");
			String requestData = "email=" + email + "&continue=" + serviceUrl;
			Runnable runnable = new GetCookieRunnable(parent, url, requestData,
					listener);
			new Thread(runnable).start();
		}
		// Non Dev Mode
		else {
			if (!account.type.equals(GOOGLE_ACCOUNT_TYPE)) {
				Toast.makeText(parent, R.string.nonDevToast, Toast.LENGTH_SHORT)
						.show();
				if (useAccountSelector && requestCode != -1) {
					chooseAccount(parent, requestCode);
					return;
				} else if (useAccountSelector && requestCode == -1) {
					throw new AccountsException(
							"Provided Request code cannot be equal to -1");
				} else {
					throw new AccountsException(
							"Only Google Accounts can be used for deployed App Engine Services.");
				}
			}
			final AccountManager accountManager = AccountManager.get(parent
					.getApplicationContext());

			accountManager.getAuthToken(account, GAE_SERVICE_NAME, null, false,
					new GAEAMCallback(parent, account, loginUrl, listener),
					null);
		}
	}

	/**
	 * @param loginUrl
	 *            Should be http://localhost:8888 for local development mode or
	 *            https://example.appspot.com for deployed app
	 */
	public static void setLoginUrl(String loginUrl) {
		LoginUtils.loginUrl = loginUrl;
		// if (loginUrl.startsWith("http://localhost")) {
		localDevMode = loginUrl.startsWith("http://10.0.2.2");
	}

	/**
	 * @param useAccountSelector
	 *            the useAccountSelector to set
	 */
	public static void useAccountSelector(boolean useAccountSelector) {
		LoginUtils.useAccountSelector = useAccountSelector;
	};
}