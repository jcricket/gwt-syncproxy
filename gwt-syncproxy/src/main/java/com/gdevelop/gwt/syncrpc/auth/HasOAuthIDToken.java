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

import com.gdevelop.gwt.syncrpc.exception.TokenNotAvailableException;

/**
 * Indicates that, when prepared, an OAuth 2.0 ID Token is available for use
 *
 * @author Preethum
 * @since 0.6
 */
public interface HasOAuthIDToken {
	/**
	 *
	 * @return the OAuth 2.0 ID Token
	 * @throws TokenNotAvailableException
	 *             if the authenticator has not completed preparing this token
	 *             data
	 */
	String getOAuthIDToken() throws TokenNotAvailableException;
}
