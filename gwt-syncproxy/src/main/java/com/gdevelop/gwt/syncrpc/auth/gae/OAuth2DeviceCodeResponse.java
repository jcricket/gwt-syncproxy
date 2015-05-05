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
 * Gson Object for receiving data from Google's OAuth 2.0 Device Code Flow
 *
 * @author Preethum
 * @since 0.6
 *
 */
public class OAuth2DeviceCodeResponse {
	String device_code;
	String user_code;
	String verification_url;
	int expires_in;
	int interval;

	public String getDevice_code() {
		return device_code;
	}

	public String getUser_code() {
		return user_code;
	}

	public String getVerification_url() {
		return verification_url;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public int getInterval() {
		return interval;
	}
}
