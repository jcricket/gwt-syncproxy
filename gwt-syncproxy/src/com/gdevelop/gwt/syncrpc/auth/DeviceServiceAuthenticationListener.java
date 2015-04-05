package com.gdevelop.gwt.syncrpc.auth;

public interface DeviceServiceAuthenticationListener extends
ServiceAuthenticationListener {
	void onUserCodeAvailable(String userCode, String verificationUrl);
}
