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
 */package com.gdevelop.gwt.syncrpc.auth;

import java.net.URL;

public interface TestModeHostVerifier {
	/**
	 * Use this method to verify if a given that GSP will not force HTTPS
	 * connections to. Essentially this is your white-list for testing servers.
	 *
	 */
	boolean isTestModeHost(URL serviceUrl);
}
