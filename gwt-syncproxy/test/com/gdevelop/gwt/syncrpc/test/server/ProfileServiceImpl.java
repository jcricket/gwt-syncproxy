package com.gdevelop.gwt.syncrpc.test.server;

import com.gdevelop.gwt.syncrpc.test.client.ProfileService;

import com.gdevelop.gwt.syncrpc.test.client.UnauthenticateException;
import com.gdevelop.gwt.syncrpc.test.client.UserInfo;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService{
  public ProfileServiceImpl() {
  }

  public UserInfo getMyProfile() throws UnauthenticateException{
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == null){
      throw new UnauthenticateException();
    }
    
    UserInfo userInfo = new UserInfo();
    userInfo.setId(user.getUserId());
    userInfo.setEmail(user.getEmail());
    return userInfo;
  }
}
