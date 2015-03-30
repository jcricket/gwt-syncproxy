package com.gdevelop.gwt.syncrpc.android.auth;

import com.gdevelop.gwt.syncrpc.HasProxySettings;

public interface ServiceAuthenticator {
	public void prepareAuthentication();

	public void applyAuthenticationToService(HasProxySettings service);
}
