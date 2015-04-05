package com.gdevelop.gwt.syncrpc.auth.gae;

public class OAuth2TokenResponse {
	public static final String ERROR_AUTH_PENDING = "authorization_pending";
	public static final String ERROR_SLOW_DOWN = "slow_down";
	String error;
	String access_token;
	String refresh_token;
	String token_type;
	int expires_in;

	public String getError() {
		return error;
	}

	public String getAccess_token() {
		return access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public boolean hasError() {
		return error != null;
	}
}
