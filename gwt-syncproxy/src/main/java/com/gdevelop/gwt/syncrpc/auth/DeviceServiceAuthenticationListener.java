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

/**
 *
 * @author Preethum
 * @since 0.6
 *
 */
public interface DeviceServiceAuthenticationListener extends ServiceAuthenticationListener {
	/**
	 * Called when the Authenticator receives a response back from Google
	 * indicating that it is waiting for the user to login.
	 * 
	 * @param userCode
	 *            is the value the user should input when navigating to the
	 *            verificationUrl
	 * @param verificationUrl
	 *            is the address to which the user should navigate to, login,
	 *            then input the userCode
	 */
	void onUserCodeAvailable(String userCode, String verificationUrl);
}
