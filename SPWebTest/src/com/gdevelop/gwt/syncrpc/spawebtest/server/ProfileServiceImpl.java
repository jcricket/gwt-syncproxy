package com.gdevelop.gwt.syncrpc.spawebtest.server;

import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileService;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UnauthenticateException;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ProfileServiceImpl extends RemoteServiceServlet implements
		ProfileService {
	public ProfileServiceImpl() {
	}

	@Override
	public UserInfo getMyProfile() throws UnauthenticateException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new UnauthenticateException();
		}

		UserInfo userInfo = new UserInfo();
		userInfo.setId(user.getUserId());
		userInfo.setEmail(user.getEmail());
		return userInfo;
	}
}
