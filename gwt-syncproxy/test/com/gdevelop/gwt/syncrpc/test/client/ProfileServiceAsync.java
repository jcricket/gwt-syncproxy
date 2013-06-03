package com.gdevelop.gwt.syncrpc.test.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {
  public void getMyProfile(AsyncCallback<UserInfo> callback);
}
