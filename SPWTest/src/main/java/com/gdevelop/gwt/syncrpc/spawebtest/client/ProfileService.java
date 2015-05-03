package com.gdevelop.gwt.syncrpc.spawebtest.client;

import com.gdevelop.gwt.syncrpc.spawebtest.shared.UnauthenticateException;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpcsuite/profile")
public interface ProfileService extends RemoteService {

	public UserInfo getAuthProfile() throws UnauthenticateException;

	public UserInfo getOAuthProfile() throws UnauthenticateException;

}
