/**
 * Jan 10, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc;

import java.net.CookieManager;

/**
 * @author Preethum
 * @since 0.5
 *
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
