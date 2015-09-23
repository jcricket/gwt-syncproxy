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
package com.gdevelop.gwt.syncrpc.auth.gae;

/**
 * Gson object for receiving data from Google's Token Response flow
 *
 * @author Preethum
 * @since 0.6
 *
 */
public class OAuth2TokenResponse {
	public static final String ERROR_AUTH_PENDING = "authorization_pending";
	public static final String ERROR_SLOW_DOWN = "slow_down";

	String error;
	String access_token;
	String refresh_token;
	String token_type;
	int expires_in;

	public String getError() {
		return error;
	}

	public String getAccess_token() {
		return access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public boolean hasError() {
		return error != null;
	}
}
