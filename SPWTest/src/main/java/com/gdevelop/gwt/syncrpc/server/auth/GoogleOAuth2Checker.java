package com.gdevelop.gwt.syncrpc.server.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

/**
 *
 * General interface for verifying an Id Token String
 *
 * @author Preethum
 * @since 0.6
 */
public interface GoogleOAuth2Checker {
	/**
	 *
	 * @param tokenString
	 *            Id Token provide from client for cross-client authentication
	 * @return null if token is not valid
	 */
	GoogleIdToken.Payload check(String tokenString);
	
	/**
	 * 
	 * @return a string indicate a problem, if one occurred
	 */
	String problem();
}
