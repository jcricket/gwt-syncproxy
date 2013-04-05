/*
 * Copyright 2013 Blue Esoteric Web Development, LLC (http://www.blueesoteric.com/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 *  See Android wiki (https://code.google.com/p/gwt-syncproxy/wiki/Android) for 
 *  coding details. This android interface was created from reviewing and integrating
 *  ideas found from: http://blog.notdot.net/2010/05/Authenticating-against-App-Engine-from-an-Android-app.
 */
package com.gdevelop.gwt.syncrpc.android;

import java.net.CookieManager;

public interface CookieManagerAvailableListener {
	public void onAuthFailure();

	public void onCMAvailable(CookieManager cm);
}