package com.gdevelop.gwt.syncrpc.spawebtest.server;

import java.util.logging.Logger;

import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileService;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UnauthenticateException;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ProfileServiceImpl extends CrossClientAuthRSS implements
ProfileService {
	Logger logger = Logger.getLogger(ProfileServiceImpl.class.getName());

	public ProfileServiceImpl() {
	}

	private UserInfo makeInfo(User user) throws UnauthenticateException {
		if (user == null) {
			throw new UnauthenticateException();
		}

		UserInfo userInfo = new UserInfo();
		userInfo.setId(user.getUserId());
		userInfo.setEmail(user.getEmail());
		return userInfo;
	}

	@Override
	public UserInfo getAuthProfile() throws UnauthenticateException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		return makeInfo(user);
	}

	@Override
	public UserInfo getOAuthProfile() throws UnauthenticateException {
		User user = getCurrentUser();
		return makeInfo(user);
	}

	/**
	 * Derived from:
	 * https://code.google.com/p/sauer/source/browse/src/echo/EchoServlet
	 * .java?repo=echo-headers
	 */
	// @Override
	// public EchoResponse echoRequest() {
	// HttpServletRequest request = getThreadLocalRequest();
	// EchoResponse er = new EchoResponse();
	// Enumeration<String> names = request.getHeaderNames();
	// Map<String, String> headers = new HashMap<String, String>();
	// while (names.hasMoreElements()) {
	// String name = names.nextElement();
	// String value = request.getHeader(name);
	// String msg = name + ": " + value;
	// headers.put(name, value);
	// logger.info(msg);
	// }
	//
	// // resp.getWriter().println("--------------------------------------");
	// UserService users = UserServiceFactory.getUserService();
	// boolean userLoggedIn = users.isUserLoggedIn();
	// // resp.getWriter().println("userLoggedIn: " + userLoggedIn);
	// er.setUSFLoggedIn(userLoggedIn);
	// if (userLoggedIn) {
	// User currentUser = users.getCurrentUser();
	// boolean userIsAdmin = users.isUserAdmin();
	// // resp.getWriter().println("currentUser : " + currentUser);
	// // resp.getWriter().println("currentUser.getAuthDomain() : " +
	// // currentUser.getAuthDomain());
	// er.setUSFAuthDomain(currentUser.getAuthDomain());
	// // resp.getWriter().println("currentUser.getEmail() : " +
	// // currentUser.getEmail());
	// er.setUSFEmail(currentUser.getEmail());
	// // resp.getWriter().println("currentUser.getFederatedIdentity() : "
	// // + currentUser.getFederatedIdentity());
	// er.setUSFFedIdent(currentUser.getFederatedIdentity());
	// // resp.getWriter().println("currentUser.getNickname() : " +
	// // currentUser.getNickname());
	// er.setUSFNickname(currentUser.getNickname());
	// // resp.getWriter().println("currentUser.getUserId() : " +
	// // currentUser.getUserId());
	// er.setUSFId(currentUser.getUserId());
	// // resp.getWriter().println("userIsAdmin : " + userIsAdmin);
	// er.setUSFIsAdmin(userIsAdmin);
	// } else {
	// String loginUrl = users.createLoginURL("/");
	// // resp.getWriter().println("loginUrl : " + loginUrl);
	// er.setLoginUrl(loginUrl);
	// }
	//
	// // resp.getWriter().println("--------------------------------------");
	// OAuthService oauth = OAuthServiceFactory.getOAuthService();
	// try {
	// User user = oauth
	// .getCurrentUser("https://www.googleapis.com/auth/userinfo.email");
	// // resp.getWriter().println("oauth.getCurrentUser(scope) : " +
	// // user);
	// // resp.getWriter().println("oauth.getCurrentUser(scope).getEmail() : "
	// // + user.getEmail());
	// // resp.getWriter().println(
	// // "oauth.getCurrentUser(scope).getAuthDomain() : " +
	// // user.getAuthDomain());
	// // resp.getWriter().println(
	// // "oauth.getCurrentUser(scope).getFederatedIdentity() : " +
	// // user.getFederatedIdentity());
	// //
	// resp.getWriter().println("oauth.getCurrentUser(scope).getNickname() : "
	// // + user.getNickname());
	// // resp.getWriter().println("oauth.getCurrentUser(scope).getUserId() : "
	// // + user.getUserId());
	// er.setOASAuthDomain(user.getAuthDomain());
	// er.setOASEmail(user.getEmail());
	// er.setOASFedIdent(user.getFederatedIdentity());
	// er.setOASNickname(user.getNickname());
	// er.setOASId(user.getUserId());
	// } catch (OAuthRequestException e) {
	// // resp.getWriter().println("oauth.getCurrentUser : " + e);
	// er.setOASException(e);
	// }
	//
	// // resp.getWriter().println("--------------------------------------");
	// try {
	// User user = oauth.getCurrentUser();
	// // resp.getWriter().println("oauth.getCurrentUser() : " + user);
	// // resp.getWriter().println("oauth.getCurrentUser().getEmail() : " +
	// // user.getEmail());
	// // resp.getWriter().println("oauth.getCurrentUser().getAuthDomain() : "
	// // + user.getAuthDomain());
	// // resp.getWriter().println(
	// // "oauth.getCurrentUser().getFederatedIdentity() : " +
	// // user.getFederatedIdentity());
	// // resp.getWriter().println("oauth.getCurrentUser().getNickname() : "
	// // + user.getNickname());
	// // resp.getWriter().println("oauth.getCurrentUser().getUserId() : "
	// // + user.getUserId());
	// er.setOAAuthDomain(user.getAuthDomain());
	// er.setOAEmail(user.getEmail());
	// er.setOAFedIdent(user.getFederatedIdentity());
	// er.setOANickname(user.getNickname());
	// er.setOAId(user.getUserId());
	// } catch (OAuthRequestException e) {
	// // resp.getWriter().println("oauth.getCurrentUser : " + e);
	// er.setOAException(e);
	// }
	//
	// // resp.getWriter().println("--------------------------------------");
	// return er;
	// }
}
