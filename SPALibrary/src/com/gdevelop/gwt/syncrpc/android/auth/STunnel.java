/**
 * Copyright 2015 Blue Esoteric Web Development, LLC
 * <http://www.blueesoteric.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc.android.auth;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;

/**
 * This class is purely a helper for development testing. It works if you have
 * STunnel setup on your Server Dev machine to allow HTTPS connections to pass
 * through. GSP requires HTTPS by default if Auth Settings (Bearer Token or
 * OAuthIdToken) are provided.
 *
 * http://stackoverflow.com/questions/13917984/accept-self-signed-ssl-
 * certificates-where-to-set-default-trustmanager
 *
 * http://stackoverflow.com/questions/859111/how-do-i-accept-a-self-signed-
 * certificate-with-a-java-httpsurlconnection
 *
 * @author Preethum
 * @since 0.6
 */
public class STunnel {
	public static void reconfig(Context context, int serverKeyRawResource, String pass) {
		SSLSocketFactory sslFactory = null;
		try {
			KeyStore localTrustStore = KeyStore.getInstance("BKS");
			InputStream in = context.getResources().openRawResource(serverKeyRawResource);
			localTrustStore.load(in, pass.toCharArray());
			// localTrustStore.getCertificate("server").
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(localTrustStore);
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, tmf.getTrustManagers(), null);
			sslFactory = ctx.getSocketFactory();
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			HttpsURLConnection.setDefaultSSLSocketFactory(sslFactory);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
