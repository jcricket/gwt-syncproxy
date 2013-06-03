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

import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;

import com.gdevelop.gwt.syncrpc.Utils;
import com.google.gwt.user.client.rpc.StatusCodeException;

/**
 * @author Preethum
 * @since 0.4
 */
public class GetCookieRunnable implements Runnable {
	String request;
	URL loginUrl;

	CookieManagerAvailableListener listener;
	Activity parent;

	/**
	 * @param parent
	 * @param loginUrl
	 * @param request
	 * @param listener
	 */
	public GetCookieRunnable(Activity parent, URL loginUrl, String request,
			CookieManagerAvailableListener listener) {
		super();
		this.parent = parent;
		this.loginUrl = loginUrl;
		this.request = request;
		this.listener = listener;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) loginUrl.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length",
					"" + request.length());
		} catch (Exception e) {
			e.printStackTrace();
			if (connection != null) {
				connection.disconnect();
			}
			return;
		}

		CookieHandler oldCookieHandler = CookieHandler.getDefault();
		final CookieManager cookieManager = new CookieManager(null,
				CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cookieManager);
		try {
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(request);
			writer.flush();
			writer.close();

			int statusCode = connection.getResponseCode();
			if ((statusCode != HttpURLConnection.HTTP_OK)
					&& (statusCode != HttpURLConnection.HTTP_MOVED_TEMP)) {
				String responseText = Utils.getResposeText(connection);
				throw new StatusCodeException(statusCode, responseText);
			}
			if (connection.getHeaderField("Set-Cookie").length() == 0) {
				parent.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						listener.onAuthFailure();
					}
				});
			} else {
				if (parent != null) {
					parent.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							listener.onCMAvailable(cookieManager);
						}
					});
				} else {
					listener.onCMAvailable(cookieManager);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failure in attempt to get cookie: ", e);
		} finally {
			CookieHandler.setDefault(oldCookieHandler);
			connection.disconnect();
		}
	}
}
