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

/**
 * This class provides an extension to the {@link RemoteServiceServlet} for GWT
 * servlets which adds recognition capabilities for Cross Client Id Tokens (with
 * verification). It also provides a unified {@link #getCurrentUser()} method
 * which will select the User object available or authenticated by the client,
 * regardless of which method was used (Cross Client ID, UserService
 * (ClientLogin), OAuth Bearer Token).
 *
 * @author Preethum
 * @since 0.6
 */
@SuppressWarnings("serial")
public class CrossClientAuthRSS extends RemoteServiceServlet {
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
		 * Default: Will thrown an exception if the possible users ,OAuth2 by Id
		 * Token, OAuth 2 by Bearer Token, and User Service user exist and are
		 * not the same
		 */
		EXCEPTION,
		/**
		 * Returns the UserOption based on Priority of {@link #usersPriority()}
		 */
		PRIORITY;
	}

	public enum UserOption {
		/**
		 * Will preferentially return the OAuth2 Cross Client user if it exists
		 */
		CROSS_CLIENT,
		/**
		 * Will preferentially return the GAE OAuth User Service User if it
		 * exists
		 */
		OAUTH_USER_SERVICE,
		/**
		 * Will preferentially return the GAE User Service user if it exists
		 */
		USER_SERVICE
	}

	public final static String OAUTH_HEADER = "X-GSP-OAUTH-ID";

	private static boolean equalUsers(User userA, User userB) {
		// Check's against id 0 to eliminate psedo-user example@example.com
		// return by OAuthService
		// This may fail improperly for dev-mode UserService users if those
		// also return 0 instead of unique ID. A local fix is implemented in
		// #getCurrentUser which sets the Id to the e-mail for User's with an Id
		// of "0"
		if (userA != null && userB != null && !userA.getUserId().equals("0") && !userB.getUserId().equals("0")
				&& !userA.getUserId().equals(userB.getUserId())) {
			return false;
		} else if (userA == null && userB == null) {
			return true;
		}
		return true;
	}

	private static boolean userConflicts(User oServiceUser, User usUser, User ccUser) {
		return !(equalUsers(oServiceUser, usUser) && equalUsers(usUser, ccUser) && equalUsers(oServiceUser, ccUser));
	}

	Logger logger = Logger.getLogger(CrossClientAuthRSS.class.getName());

	UserConflictMode mode = UserConflictMode.EXCEPTION;

	boolean oAuthBearerToken = false;

	User user;

	private User getUserByPriority(User oUser, User usUser, User ccUser) {
		UserOption[] modesOrder = usersPriority();
		for (UserOption element : modesOrder) {
			switch (element) {
			case CROSS_CLIENT:
				if (user != null && !user.getUserId().equals("0")) {
					return user;
				}
				break;
			case USER_SERVICE:
				if (usUser != null && !usUser.getUserId().equals("0")) {
					return usUser;
				}
				break;
			case OAUTH_USER_SERVICE:
				if (oUser != null && !oUser.getUserId().equals("0")) {
					return oUser;
				}
				break;
			default:
				throw new RuntimeException("Handled mode type: " + element);
			}
		}
		return null;
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
	 * Retrieves the current logged in user. The user returned comes from one of
	 * the following:
	 * <ul>
	 * <li>Dynamically created based on an OpenId Connect Id Token</li>
	 * <li>User available from {@link UserService}</li>
	 * <li>User available from {@link OAuthService}</li>
	 * <ul>
	 *
	 * Which one is returned is dependent on the following factors. If
	 * {@link #mode} is {@link UserConflictMode#EXCEPTION}, then it will return
	 * the first user matched according to {@link #usersPriority()}. If there is
	 * a mismatch amongst the available users, an exception is thrown.If
	 * {@link #mode} is {@link UserConflictMode#PRIORITY}, then the user is
	 * returned according to the defined {@link #usersPriority()} for the first
	 * one that is not null.
	 *
	 * In local Dev mode, the user's ids for UserService and OAuthUserService
	 * are set to the e-mail address in order to provide unique identifiers.
	 * Otherwise in Dev Mode, the user id's are set to 0.
	 *
	 */
	protected User getCurrentUser() {
		UserService service = UserServiceFactory.getUserService();
		OAuthService oService = OAuthServiceFactory.getOAuthService();
		User usUser = null;
		User oUser = null;
		try {
			oUser = oService.getCurrentUser();
			// Local Dev Mode
			if (oUser != null && oUser.getUserId().equals("0") && oAuthBearerToken) {
				User lUser = new User(oUser.getEmail(), oUser.getAuthDomain(), oUser.getEmail());
				oUser = lUser;
			}
		} catch (OAuthRequestException e) {
			throw new RuntimeException(e);
		}
		if (service.isUserLoggedIn()) {
			usUser = service.getCurrentUser();
			// Local Dev Mode
			if (usUser != null && (usUser.getUserId() == null || usUser.getUserId().equals("0"))) {
				User lUser = new User(usUser.getEmail(), usUser.getAuthDomain(), usUser.getEmail());
				usUser = lUser;
			}

		}

		if (mode == UserConflictMode.EXCEPTION && !userConflicts(oUser, usUser, user)) {
			return getUserByPriority(oUser, usUser, user);

		} else if (mode == UserConflictMode.EXCEPTION) {
			throw new RuntimeException("User Id's Conflict: OAuth - " + (user != null ? user.getEmail() : "()")
					+ " and GAE UserService - " + (usUser != null ? usUser.getEmail() : "()")
					+ " and GAE OAuthUserService - " + (oUser != null ? oUser.getEmail() : "()"));
		}
		return getUserByPriority(oUser, usUser, user);
	}

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

	protected void setUserPriorityMode(UserConflictMode mode) {
		this.mode = mode;
	}

	/**
	 * May be overridden to provide an ordered set of {@link UserOption}s that
	 * should be returned by {@link #getCurrentUser()} if available
	 */
	protected UserOption[] usersPriority() {
		return new UserOption[] { UserOption.USER_SERVICE, UserOption.OAUTH_USER_SERVICE, UserOption.CROSS_CLIENT };
	}

	/**
	 * May be overridden to localized testing scenarios
	 *
	 */
	protected GoogleIdToken.Payload verify() {
		logger.fine("Verifying if OAuth Header for CC Auth is available");
		oAuthBearerToken = getThreadLocalRequest().getHeader("Authorization") != null;
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
