package com.gdevelop.gwt.syncrpc.server.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

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
public class GoogleOAuth2CheckerImpl implements GoogleOAuth2Checker {

	private final List<String> mClientIDs;
	private final String mAudience;
	private GoogleIdTokenVerifier mVerifier;
	private JsonFactory mJFactory;
	private String mProblem = "Verification failed. (Time-out?)";

	public GoogleOAuth2CheckerImpl(ServletContext context) {
		ClientIdManager manager = new ClientIdManagerImpl(context);
		mClientIDs = Arrays.asList(manager.getAllClients());
		mAudience = manager.getServerAudience();
		init();
	}

	public GoogleOAuth2CheckerImpl(ServletContext context,
			ClientIdManager manager) {
		this(manager.getAllClients(), manager.getServerAudience());
	}

	protected void init() {
		NetHttpTransport transport = new NetHttpTransport();
		mJFactory = new GsonFactory();
		mVerifier = new GoogleIdTokenVerifier(transport, mJFactory);
	}

	public GoogleOAuth2CheckerImpl(String[] clientIDs, String audience) {
		mClientIDs = Arrays.asList(clientIDs);
		mAudience = audience;
		init();
	}

	@Override
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