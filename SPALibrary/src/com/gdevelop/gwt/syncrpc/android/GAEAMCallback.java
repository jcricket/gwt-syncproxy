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
 */
package com.gdevelop.gwt.syncrpc.android;

import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gdevelop.gwt.syncrpc.LoginUtils;

/**
 * Call back from request for authorization token.
 *
 * @author Preethum
 * @since 0.4
 */
public class GAEAMCallback implements AccountManagerCallback<Bundle> {
	Activity parent;
	String loginUrl;
	CookieManagerAvailableListener primaryListener;
	CookieManagerAvailableListener localListener;
	Account account;

	String auth_token;

	public GAEAMCallback(Activity parent, Account account, String loginUrl,
			CookieManagerAvailableListener listener) {
		this.parent = parent;
		this.account = account;
		this.loginUrl = loginUrl;
		this.primaryListener = listener;
		this.localListener = new CookieManagerAvailableListener() {
			@Override
			public void onAuthFailure() {
				AccountManager accountManager = AccountManager
						.get(GAEAMCallback.this.parent);
				accountManager.invalidateAuthToken(
						LoginUtils.GOOGLE_ACCOUNT_TYPE,
						GAEAMCallback.this.auth_token);
				accountManager.getAuthToken(GAEAMCallback.this.account,
						LoginUtils.GAE_SERVICE_NAME, null, false,
						new GAEAMCallback(GAEAMCallback.this.parent,
								GAEAMCallback.this.account,

								GAEAMCallback.this.loginUrl,
								GAEAMCallback.this.primaryListener), null);
			}

			@Override
			public void onCMAvailable(CookieManager cm) {
				GAEAMCallback.this.primaryListener.onCMAvailable(cm);
			}
		};
	}

	@Override
	public void run(AccountManagerFuture<Bundle> result) {
		try {
			Bundle bundle;

			bundle = result.getResult();
			Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
			if (intent != null) {
				this.parent.startActivity(intent);
			} else {
				this.auth_token = bundle
						.getString(AccountManager.KEY_AUTHTOKEN);
				this.loginUrl = this.loginUrl + "/_ah/login";
				String request = "?continue="
						+ URLEncoder.encode("nowhere", "UTF-8") + "&auth="
						+ this.auth_token;
				URL url = new URL(this.loginUrl);
				Runnable runnable = new GetCookieRunnable(this.parent, url,
						request, this.localListener);
				new Thread(runnable).start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}