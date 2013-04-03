package com.gdevelop.gwt.syncrpc.test.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("profile")
public interface ProfileService extends RemoteService{
  public UserInfo getMyProfile() throws UnauthenticateException;
}
