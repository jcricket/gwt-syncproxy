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

import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticationListener;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides alias's for Account object based on the account name instead of Account instance since
 * multiple account instances may exist for the same account
 *
 * @author Preethum
 * @version 0.6.1
 * @since 0.6 - 7/29/2015
 */
public class AuthenticatorManager extends HashMap<String, ServiceAuthenticator> {
	/**
	 * Trackers listeners waiting for an authenticator of a particular account to be made available
	 */
	HashMap<String, ArrayList<ServiceAuthenticationListener>> pendingListeners;
	ArrayList<String> callingListenersFor;
	String name;

	public AuthenticatorManager(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * @see #listenFor(String, ServiceAuthenticationListener)
	 * @since 0.6.1
	 */
	public boolean listenFor(Account account, ServiceAuthenticationListener listener) {
		return this.listenFor(account.name, listener);
	}

	/**
	 * Used to manage asynchronous authenticator usage. If authenticator is not available, listener
	 * is place in a queue and will be called once an authenticator for the specified account is
	 * available
	 *
	 * @param listener to be called when a prepared authenticator has been made available
	 * @return <code>true</code> if authenticator was available, otherwise <code>false</code> if it
	 * the listener was put into waiting
	 * @since 0.6.1
	 */
	public boolean listenFor(String accName, ServiceAuthenticationListener listener) {
		ServiceAuthenticator auth = get(accName);
		if (auth == null) {
			if (pendingListeners == null) {
				pendingListeners = new HashMap<>();
			}
			ArrayList<ServiceAuthenticationListener> listeners = pendingListeners.get(accName);
			if (listeners == null) {
				listeners = new ArrayList<>();
				pendingListeners.put(accName, listeners);
			}
			listeners.add(listener);
			return false;
		} else {
			listener.onAuthenticatorPrepared(auth);
			return true;
		}
	}

	/**
	 * Uses ServiceAuthenticator#accountName() as the key
	 *
	 * @since 0.6.1
	 */
	public void put(ServiceAuthenticator authenticator) {
		put(authenticator.accountName(), authenticator);
	}

	/**
	 * @since 0.6.1
	 */
	public ServiceAuthenticator put(String accName, ServiceAuthenticator authenticator) {
		if (!authenticator.isPrepared()) {
			throw new RuntimeException("Service Authenticator should have been prepared prior to installing to manager");
		}
		ServiceAuthenticator old = super.put(accName, authenticator);
		callListeners(accName, authenticator);
		return old;
	}

	/**
	 * @since 0.6.1
	 */
	private void callListeners(String accName, ServiceAuthenticator authenticator) {
		// Call listeners if they were waiting
		if (pendingListeners != null) {
			if (callingListenersFor == null) {
				callingListenersFor = new ArrayList<>();
			}
			// Check if we're already calling listeners for this account to prevent embedded loops
			if (pendingListeners.get(accName) != null && !callingListenersFor.contains(accName)) {
				callingListenersFor.add(accName);
				for (ServiceAuthenticationListener sal : pendingListeners.get(accName)) {
					sal.onAuthenticatorPrepared(authenticator);
				}
				// Clear the pending listeners for this account
				pendingListeners.get(accName).clear();
				callingListenersFor.remove(accName);
			}
		}
	}

	public ServiceAuthenticator get(Account account) {
		return super.get(account.name);
	}

	/**
	 * Ensures the authenticator has been prepared and calls any listeners that were waiting for
	 * this authenticator
	 *
	 * @version 0.6.1
	 * @see HashMap#put(Object, Object)
	 */
	public ServiceAuthenticator put(Account account, ServiceAuthenticator authenticator) {
		return this.put(account.name, authenticator);
	}

	/**
	 * Quick use Listener that will automatically add an authenticator to this provided manager once
	 * it is prepared
	 *
	 * @since 0.6.1
	 */
	public static class AMAdder implements ServiceAuthenticationListener {
		AuthenticatorManager manager;

		public AMAdder(AuthenticatorManager manager) {
			this.manager = manager;
		}

		@Override
		public void onAuthenticatorPrepared(ServiceAuthenticator authenticator) {
			manager.put(authenticator.accountName(), authenticator);
		}
	}
}
