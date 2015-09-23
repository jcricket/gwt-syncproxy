/**
 * Copyright 2015 Blue Esoteric Web Development, LLC <http://www.blueesoteric.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc.android.auth;

import android.accounts.Account;

import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;

import java.util.HashMap;

/**
 * @author Preethum
 * @version 0.6
 * @since 0.6 - 7/29/2015
 */
public class AuthenticatorManager extends HashMap<String, ServiceAuthenticator> {

	public ServiceAuthenticator get(Account account) {
		return super.get(account.name);
	}

	public ServiceAuthenticator put(Account account, ServiceAuthenticator authenticator) {
		if (!authenticator.isPrepared()) {
			throw new RuntimeException("Service Authenticator should have been prepared prior to installing to manager");
		}
		return super.put(account.name, authenticator);
	}
}
