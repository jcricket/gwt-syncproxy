/**
 * Copyright 2015 Blue Esoteric Web Development, LLC
 * <http://www.blueesoteric.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc;

import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;

/**
 * Handles settings utilized by the SyncProxy creation. Set methods return the
 * Settings object for chaining.
 *
 * @author Preethum
 * @since 0.5
 * @version 0.6
 */
public class ProxySettings implements HasProxySettings {
	String bearerToken;
	CookieManager cookieManager;
	Map<String, String> headers;
	String moduleBaseUrl;
	String oAuth2IdToken;
	String policyName;
	String remoteServiceRelativePath;
	ServiceAuthenticator serviceAuthenticator;
	boolean waitForInvocation = false;

	public ProxySettings() {

	}

	public ProxySettings(String moduleBaseUrl, String remoteServiceRelativePath, String policyName,
			CookieManager cookieManager, boolean waitForInvocation) {
		super();
		this.moduleBaseUrl = moduleBaseUrl;
		this.remoteServiceRelativePath = remoteServiceRelativePath;
		this.policyName = policyName;
		this.cookieManager = cookieManager;
		this.waitForInvocation = waitForInvocation;
	}

	@Override
	public CookieManager getCookieManager() {
		return this.cookieManager;
	}

	@Override
	public Map<String, String> getCustomHeaders() {
		if (headers == null) {
			headers = new HashMap<>();
		}
		return headers;
	}

	@Override
	public String getModuleBaseUrl() {
		return this.moduleBaseUrl;
	}

	@Override
	public String getOAuth2IdToken() {
		return oAuth2IdToken;
	}

	@Override
	public String getOAuthBearerToken() {
		return bearerToken;
	}

	@Override
	public String getPolicyName() {
		return this.policyName;
	}

	@Override
	public String getRemoteServiceRelativePath() {
		return this.remoteServiceRelativePath;
	}

	@Override
	public ServiceAuthenticator getServiceAuthenticator() {
		return serviceAuthenticator;
	}

	@Override
	public boolean isWaitForInvocation() {
		return this.waitForInvocation;
	}

	@Override
	public ProxySettings setCookieManager(CookieManager cookieManager) {
		this.cookieManager = cookieManager;
		return this;
	}

	@Override
	public HasProxySettings setCustomHeaders(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	@Override
	public ProxySettings setModuleBaseUrl(String moduleBaseUrl) {
		this.moduleBaseUrl = moduleBaseUrl;
		return this;
	}

	@Override
	public HasProxySettings setOAuth2IdToken(String token) {
		this.oAuth2IdToken = token;
		return this;
	}

	@Override
	public HasProxySettings setOAuthBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
		return this;
	}

	@Override
	public ProxySettings setPolicyName(String policyName) {
		this.policyName = policyName;
		return this;
	}

	@Override
	public ProxySettings setRemoteServiceRelativePath(String remoteServiceRelativePath) {
		this.remoteServiceRelativePath = remoteServiceRelativePath;
		return this;
	}

	@Override
	public HasProxySettings setServiceAuthenticator(ServiceAuthenticator authenticator) {
		serviceAuthenticator = authenticator;
		return this;
	}

	@Override
	public ProxySettings setWaitForInvocation(boolean waitForInvocation) {
		this.waitForInvocation = waitForInvocation;
		return this;
	}

}