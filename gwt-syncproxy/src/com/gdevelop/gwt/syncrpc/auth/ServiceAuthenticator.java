package com.gdevelop.gwt.syncrpc.auth;

import com.gdevelop.gwt.syncrpc.HasProxySettings;

/**
 * Defines a system that can apply authentication details (cookies, headers,
 * etc) to be used against a back-end service
 *
 * @author Preethum
 * @since 0.6
 */
public interface ServiceAuthenticator {

	/**
	 * Performs whatever actions are necessary to retrieve and prepare
	 * authentication details and data prior to application to the service
	 */
	public void prepareAuthentication();

	/**
	 * Applies authentication details to the provided service
	 *
	 * @param service
	 *            which will be enhanced with authentication details according
	 *            to the implementing class
	 */
	public void applyAuthenticationToService(HasProxySettings service);
}
