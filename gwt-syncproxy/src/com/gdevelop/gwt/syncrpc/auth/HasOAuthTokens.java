package com.gdevelop.gwt.syncrpc.auth;

public interface HasOAuthTokens {
	String getAccessToken();

	String getRefreshToken();
}
