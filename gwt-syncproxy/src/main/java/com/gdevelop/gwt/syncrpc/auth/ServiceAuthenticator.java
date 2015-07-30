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
	 * Helper method to verify if this authenticator has been prepared. Should
	 * return true IFF the {@link #prepareAuthentication()} method has completed
	 * successfully
	 */
	boolean isPrepared();

	/**
	 * Performs whatever actions are necessary to retrieve and prepare
	 * authentication details and data prior to application to the service
	 */
	void prepareAuthentication();

	/**
	 * Applies authentication details to the provided service. Typically, this
	 * should be done by calling the set methods on the {@link HasProxySettings}
	 * service.
	 *
	 * @param service
	 *            which will be enhanced with authentication details according
	 *            to the implementing class
	 */
	void applyAuthenticationToService(HasProxySettings service);

}
