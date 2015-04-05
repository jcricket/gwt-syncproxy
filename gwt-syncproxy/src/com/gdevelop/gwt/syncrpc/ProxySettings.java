/**
 * Jan 10, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc;

import java.net.CookieManager;

import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;

/**
 * Handles settings utilized by the SyncProxy creation. Set methods return the
 * Settings object for chaining.
 *
 * @author Preethum
 * @since 0.5
 *
 */
public class ProxySettings implements HasProxySettings {
	String moduleBaseUrl;
	String remoteServiceRelativePath;
	String policyName;
	CookieManager cookieManager;
	ServiceAuthenticator serviceAuthenticator;
	boolean waitForInvocation = false;
	String bearerToken;

	public ProxySettings() {

	}

	public ProxySettings(String moduleBaseUrl,
			String remoteServiceRelativePath, String policyName,
			CookieManager cookieManager, boolean waitForInvocation) {
		super();
		this.moduleBaseUrl = moduleBaseUrl;
		this.remoteServiceRelativePath = remoteServiceRelativePath;
		this.policyName = policyName;
		this.cookieManager = cookieManager;
		this.waitForInvocation = waitForInvocation;
	}

	/**
	 * @return the cookieManager
	 */
	@Override
	public CookieManager getCookieManager() {
		return this.cookieManager;
	}

	/**
	 * @return the policyName
	 */
	@Override
	public String getPolicyName() {
		return this.policyName;
	}

	/**
	 * @return the remoteServiceRelativePath
	 */
	@Override
	public String getRemoteServiceRelativePath() {
		return this.remoteServiceRelativePath;
	}

	/**
	 * @return the serverBaseUrl
	 */
	@Override
	public String getModuleBaseUrl() {
		return this.moduleBaseUrl;
	}

	/**
	 * @return the waitForInvocation
	 */
	@Override
	public boolean isWaitForInvocation() {
		return this.waitForInvocation;
	}

	/**
	 * @param cookieManager
	 *            the cookieManager to set
	 */
	@Override
	public ProxySettings setCookieManager(CookieManager cookieManager) {
		this.cookieManager = cookieManager;
		return this;
	}

	/**
	 * @param policyName
	 *            the policyName to set
	 */
	@Override
	public ProxySettings setPolicyName(String policyName) {
		this.policyName = policyName;
		return this;
	}

	/**
	 * @param remoteServiceRelativePath
	 *            the remoteServiceRelativePath to set
	 */
	@Override
	public ProxySettings setRemoteServiceRelativePath(
			String remoteServiceRelativePath) {
		this.remoteServiceRelativePath = remoteServiceRelativePath;
		return this;
	}

	/**
	 * @param moduleBaseUrl
	 *            the serverBaseUrl to set
	 */
	@Override
	public ProxySettings setModuleBaseUrl(String moduleBaseUrl) {
		this.moduleBaseUrl = moduleBaseUrl;
		return this;
	}

	/**
	 * @param waitForInvocation
	 *            the waitForInvocation to set
	 */
	@Override
	public ProxySettings setWaitForInvocation(boolean waitForInvocation) {
		this.waitForInvocation = waitForInvocation;
		return this;
	}

	String oAuth2IdToken;

	@Override
	public HasProxySettings setOAuth2IdToken(String token) {
		this.oAuth2IdToken = token;
		return this;
	}

	@Override
	public String getOAuth2IdToken() {
		return oAuth2IdToken;
	}

	@Override
	public ServiceAuthenticator getServiceAuthenticator() {
		return serviceAuthenticator;
	}

	@Override
	public void setServiceAuthenticator(ServiceAuthenticator authenticator) {
		serviceAuthenticator = authenticator;
	}

	@Override
	public String getOAuthBearerToken() {
		return bearerToken;
	}

	@Override
	public void setOAuthBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
	}

}