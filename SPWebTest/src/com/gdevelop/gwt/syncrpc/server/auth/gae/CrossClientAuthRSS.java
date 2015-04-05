package com.gdevelop.gwt.syncrpc.server.auth.gae;

import java.util.logging.Logger;

import com.gdevelop.gwt.syncrpc.server.auth.GoogleOAuth2Checker;
import com.gdevelop.gwt.syncrpc.server.auth.GoogleOAuth2CheckerImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.oauth.OAuthService;
import com.google.appengine.api.oauth.OAuthServiceFactory;
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
		 * Will preferentially return the GAE OAuth User Service User if it
		 * exists
		 */
		OAUTH_USER_SERVICE,
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

	private User getUserByPriority(User oUser, User usUser, User ccUser) {
		UserConflictMode[] modesOrder = usersPriority();
		for (UserConflictMode element : modesOrder) {
			switch (element) {
			case OAUTH2:
				if (user != null) {
					return user;
				}
				break;
			case USER_SERVICE:
				if (usUser != null) {
					return usUser;
				}
				break;
			case OAUTH_USER_SERVICE:
				if (oUser != null) {
					return oUser;
				}
				break;
			default:
				throw new RuntimeException("Handled mode type: " + element);
			}
		}
		return null;
	}

	protected User getCurrentUser() {
		UserService service = UserServiceFactory.getUserService();
		OAuthService oService = OAuthServiceFactory.getOAuthService();
		User usUser = null;
		User oUser = null;
		try {
			oUser = oService.getCurrentUser();
		} catch (OAuthRequestException e) {
			throw new RuntimeException(e);
		}
		if (service.isUserLoggedIn()) {
			usUser = service.getCurrentUser();
		}

		if (mode == UserConflictMode.EXCEPTION
				&& !userConflicts(oUser, usUser, user)) {
			return getUserByPriority(oUser, usUser, user);

		} else if (mode == UserConflictMode.EXCEPTION) {
			throw new RuntimeException("User Id's Conflict: OAuth - "
					+ (user != null ? user.getEmail() : "()")
					+ " and GAE UserService - "
					+ (usUser != null ? usUser.getEmail() : "()")
					+ " and GAE OAuthUserService - "
					+ (oUser != null ? oUser.getEmail() : "()"));
		}
		return getUserByPriority(oUser, usUser, user);
	}

	private static boolean equalUsers(User userA, User userB) {
		if (userA != null && userB != null
				&& !userA.getUserId().equals(userB.getUserId())) {
			return false;
		}
		return true;
	}

	private static boolean userConflicts(User oServiceUser, User usUser,
			User ccUser) {
		// TODO Removed checks against oServiceUser because on Development side,
		// it always return example@example.com regardless of the actually
		// correct user email
		return
		// equalUsers(oServiceUser, usUser) &&
		equalUsers(usUser, ccUser)
		// && equalUsers(oServiceUser, ccUser)
		;
	}

	protected UserConflictMode[] usersPriority() {
		return new UserConflictMode[] { UserConflictMode.USER_SERVICE,
				UserConflictMode.OAUTH2 };
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
