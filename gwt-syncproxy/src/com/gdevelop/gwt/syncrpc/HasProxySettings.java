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
import java.util.Map;

import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;

/**
 * Interface to specify an object that will provide for requested Proxy
 * Settings. Custom ProxySettings objects may be utilized as of version 0.5 to
 * create reusable settings objects that are customized.
 *
 * @author Preethum
 * @since 0.5
 * @version 0.6
 */
public interface HasProxySettings {
	CookieManager getCookieManager();

	/**
	 * @since 0.6
	 */
	Map<String, String> getCustomHeaders();

	String getModuleBaseUrl();

	/**
	 * @since 0.6
	 */
	String getOAuth2IdToken();

	/**
	 * @since 0.6
	 */
	String getOAuthBearerToken();

	String getPolicyName();

	String getRemoteServiceRelativePath();

	ServiceAuthenticator getServiceAuthenticator();

	boolean isWaitForInvocation();

	HasProxySettings setCookieManager(CookieManager cookieManager);

	/**
	 * @since 0.6
	 */
	HasProxySettings setCustomHeaders(Map<String, String> headers);

	HasProxySettings setModuleBaseUrl(String serverBaseUrl);

	/**
	 * @since 0.6
	 */
	HasProxySettings setOAuth2IdToken(String token);

	/**
	 * @since 0.6
	 */
	HasProxySettings setOAuthBearerToken(String bearerToken);

	HasProxySettings setPolicyName(String policyName);

	HasProxySettings setRemoteServiceRelativePath(String remoteServiceRelativePath);

	HasProxySettings setServiceAuthenticator(ServiceAuthenticator authenticator);

	HasProxySettings setWaitForInvocation(boolean waitForInvocation);
}
