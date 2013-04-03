package com.gdevelop.gwt.syncrpc.test.client;

import com.google.gwt.user.client.rpc.RemoteService;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("largePayload")
public interface LargePayloadService extends RemoteService{
  public List<UserInfo> testLargeResponsePayload();
  public int[] testLargeResponseArray();
}
