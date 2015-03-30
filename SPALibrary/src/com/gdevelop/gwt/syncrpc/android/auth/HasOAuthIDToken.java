package com.gdevelop.gwt.syncrpc.android.auth;

public interface HasOAuthIDToken {
	String getOAuthIDToken() throws TokenNotAvailableException;
}
