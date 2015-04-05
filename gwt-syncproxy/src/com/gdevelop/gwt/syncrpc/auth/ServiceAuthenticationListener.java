package com.gdevelop.gwt.syncrpc.auth;

/**
 * Helper to be notified when a {@link ServiceAuthenticator} has been prepared
 * 
 * @author Preethum
 * @since 0.6
 *
 */
public interface ServiceAuthenticationListener {
	void onAuthenticatorPrepared(String accountName);
}
