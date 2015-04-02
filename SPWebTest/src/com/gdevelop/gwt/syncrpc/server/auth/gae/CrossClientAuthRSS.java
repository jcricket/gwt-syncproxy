package com.gdevelop.gwt.syncrpc.server.auth.gae;

import java.util.logging.Logger;

import com.gdevelop.gwt.syncrpc.server.auth.GoogleOAuth2Checker;
import com.gdevelop.gwt.syncrpc.server.auth.GoogleOAuth2CheckerImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class CrossClientAuthRSS extends RemoteServiceServlet {
	Logger logger = Logger.getLogger(CrossClientAuthRSS.class.getName());

	/**
	 * This sets the mode for what should happen in the event that the OAuth2
	 * user verified by this process is different than the use available through
	 * GAE's userService (if any).
	 *
	 * @author Preethum
	 *
	 */
	public enum UserConflictMode {
		/**
		 * Will preferentially return the OAuth2 user if it exists
		 */
		OAUTH2,
		/**
		 * Will preferentially return the GAE User Service user if it exists
		 */
		USER_SERVICE,
		/**
		 * Default: Will thrown an exception if both an OAuth2 user and User
		 * Service user exist and are not the same
		 */
		EXCEPTION;
	}

	public final static String OAUTH_HEADER = "X-GSP-OAUTH-ID";
	User user;
	UserConflictMode mode = UserConflictMode.EXCEPTION;

	@Override
	protected void onAfterRequestDeserialized(RPCRequest rpcRequest) {
		super.onAfterRequestDeserialized(rpcRequest);

		GoogleIdToken.Payload payload = verify();
		if (payload == null) {
			logger.fine("No usable payload available for OAuth CC");
			user = null;
			return;
		}
		logger.config("OAuth CC User defined");

		logger.finest("OAuth User Available: " + payload.getEmail());
		user = new User(payload.getEmail(), "gmail.com", payload.getSubject());
	}

	protected User getCurrentUser() {
		UserService service = UserServiceFactory.getUserService();
		if (service.isUserLoggedIn()) {
			User usUser = service.getCurrentUser();
			if (mode == UserConflictMode.EXCEPTION && user != null
					&& usUser != null) {
				if (user.getUserId().equals(usUser.getUserId())) {
					return usUser;
				}
				throw new RuntimeException("User Id's Conflict: OAuth - "
						+ user.getEmail() + " and GAE UserService - "
						+ usUser.getEmail());
			}
			if (user != null && usUser != null) {
				switch (mode) {
				case OAUTH2:
					return user;
				case USER_SERVICE:
					return usUser;
				}
			}
			return usUser;
		}
		return user;
	}

	/**
	 * May be overridden to provide a custom auth checker
	 *
	 * @return
	 */
	protected GoogleOAuth2Checker getAuthChecker() {
		return new GoogleOAuth2CheckerImpl(this.getServletContext());
	}

	/**
	 * May be overridden to localized testing scenarios
	 *
	 */
	protected GoogleIdToken.Payload verify() {
		logger.fine("Verifying if OAuth Header for CC Auth is available");
		String idToken = getThreadLocalRequest().getHeader(OAUTH_HEADER);
		if (idToken == null || idToken.equals("")) {
			logger.fine("OAuth Header not found");
			return null;
		}
		logger.config("OAuth Header found, check id token");
		GoogleOAuth2Checker checker = getAuthChecker();
		GoogleIdToken.Payload payload = checker.check(idToken);
		return payload;
	}
}
