package com.gdevelop.gwt.syncrpc.spawebtest.server;

import com.gdevelop.gwt.syncrpc.spawebtest.server.Checker.ClientId;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class CrossClientAuthRSS extends RemoteServiceServlet {

	public final static String OAUTH_HEADER = "X-GSP-OAUTH-ID";
	User user;

	@Override
	protected void onAfterRequestDeserialized(RPCRequest rpcRequest) {
		super.onAfterRequestDeserialized(rpcRequest);

		GoogleIdToken.Payload payload = verify();
		if (payload == null) {
			user = null;
			return;
		}
		user = new User(payload.getEmail(), "gmail.com", payload.getUserId());
	}

	protected User getCurrentUser() {
		return user;
	}

	/**
	 * May be overridden to localized testing scenarios
	 *
	 */
	protected GoogleIdToken.Payload verify() {
		String idToken = getThreadLocalRequest().getHeader(OAUTH_HEADER);
		if (idToken == null || idToken.equals("")) {
			return null;
		}
		Checker checker = new Checker(new String[] {
				Checker.getClientId(ClientId.GAE, getServletContext()),
				Checker.getClientId(ClientId.ANDROID, getServletContext()) },
				Checker.getClientId(ClientId.GAE, getServletContext()));
		GoogleIdToken.Payload payload = checker.check(idToken);
		return payload;
	}
}
