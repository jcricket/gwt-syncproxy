package com.gdevelop.gwt.syncrpc.spawebtest.server;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

/**
 * http://android-developers.blogspot.co.il/2013/01/verifying-back-end-calls-
 * from-android.html
 *
 */
public class Checker {
	public enum ClientId {
		GAE("gsp.clientid.gae"), ANDROID("gsp.clientid.android");
		String propName;

		private ClientId(String propName) {
			this.propName = propName;
		}

		public String getPropName() {
			return propName;
		}

	}

	private final List<String> mClientIDs;
	private final String mAudience;
	private final GoogleIdTokenVerifier mVerifier;
	private final JsonFactory mJFactory;
	private String mProblem = "Verification failed. (Time-out?)";

	public Checker(String[] clientIDs, String audience) {
		mClientIDs = Arrays.asList(clientIDs);
		mAudience = audience;
		NetHttpTransport transport = new NetHttpTransport();
		mJFactory = new GsonFactory();
		mVerifier = new GoogleIdTokenVerifier(transport, mJFactory);
	}

	public static String getClientId(ClientId idType, ServletContext context) {
		InputStream is = context
				.getResourceAsStream("/WEB-INF/client_ids.properties");
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return props.getProperty(idType.getPropName()).trim();
	}

	public GoogleIdToken.Payload check(String tokenString) {
		GoogleIdToken.Payload payload = null;
		try {
			GoogleIdToken token = GoogleIdToken.parse(mJFactory, tokenString);
			if (mVerifier.verify(token)) {
				GoogleIdToken.Payload tempPayload = token.getPayload();
				if (!tempPayload.getAudience().equals(mAudience)) {
					mProblem = "Audience mismatch";
				} else if (!mClientIDs.contains(tempPayload
						.getAuthorizedParty())) {
					mProblem = "Client ID mismatch";
				} else {
					payload = tempPayload;
				}
			}
		} catch (GeneralSecurityException e) {
			mProblem = "Security issue: " + e.getLocalizedMessage();
		} catch (IOException e) {
			mProblem = "Network problem: " + e.getLocalizedMessage();
		}
		return payload;
	}

	public String problem() {
		return mProblem;
	}
}