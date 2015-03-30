/**
 * Jan 10, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc;

import java.net.CookieManager;

/**
 * Interface to specify an object that will provide for requested Proxy
 * Settings. Custom ProxySettings objects may be utilized as of version 0.6 to
 * create reusable settings objects that are customized
 *
 * @author Preethum
 * @since 0.5
 * @version 0.5.5
 */
public interface HasProxySettings {

	/**
	 * @return the cookieManager
	 */
	public CookieManager getCookieManager();

	/**
	 * @return the policyName
	 */
	public String getPolicyName();

	/**
	 * @return the remoteServiceRelativePath
	 */
	public String getRemoteServiceRelativePath();

	/**
	 * @return the serverBaseUrl
	 */
	public String getModuleBaseUrl();

	/**
	 * @return the waitForInvocation
	 */
	public boolean isWaitForInvocation();

	/**
	 * @param cookieManager
	 *            the cookieManager to set
	 */
	public HasProxySettings setCookieManager(CookieManager cookieManager);

	/**
	 * @param policyName
	 *            the policyName to set
	 */
	public HasProxySettings setPolicyName(String policyName);

	public HasProxySettings setOAuth2IdToken(String token);

	public String getOAuth2IdToken();

	/**
	 * @param remoteServiceRelativePath
	 *            the remoteServiceRelativePath to set
	 */
	public HasProxySettings setRemoteServiceRelativePath(
			String remoteServiceRelativePath);

	/**
	 * @param serverBaseUrl
	 *            the serverBaseUrl to set
	 */
	public HasProxySettings setModuleBaseUrl(String serverBaseUrl);

	/**
	 * @param waitForInvocation
	 *            the waitForInvocation to set
	 */
	public HasProxySettings setWaitForInvocation(boolean waitForInvocation);
}
