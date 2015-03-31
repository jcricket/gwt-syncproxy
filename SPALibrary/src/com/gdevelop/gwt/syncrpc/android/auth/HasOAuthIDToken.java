package com.gdevelop.gwt.syncrpc.android.auth;

/**
 * Indicates that when prepared, an OAuth 2.0 ID Token is available for use
 *
 * @author Preethum
 * @since 0.6
 */
public interface HasOAuthIDToken {
	/**
	 *
	 * @return the OAuth 2.0 ID Token
	 * @throws TokenNotAvailableException
	 *             if the authenticator has not completed preparing this token
	 *             data
	 */
	String getOAuthIDToken() throws TokenNotAvailableException;
}
