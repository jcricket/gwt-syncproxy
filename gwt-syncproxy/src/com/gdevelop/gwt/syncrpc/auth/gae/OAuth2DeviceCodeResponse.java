package com.gdevelop.gwt.syncrpc.auth.gae;

public class OAuth2DeviceCodeResponse {
	String deviceCode;
	String user_code;
	String verification_url;
	int expires_in;
	int interval;

	public String getDeviceCode() {
		return deviceCode;
	}

	public String getUser_code() {
		return user_code;
	}

	public String getVerification_url() {
		return verification_url;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public int getInterval() {
		return interval;
	}
}
