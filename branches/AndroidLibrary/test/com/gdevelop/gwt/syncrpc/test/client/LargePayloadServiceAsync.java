package com.gdevelop.gwt.syncrpc.test.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface LargePayloadServiceAsync {
  public void testLargeResponsePayload(AsyncCallback<List<UserInfo>> callback);
  public void testLargeResponseArray(AsyncCallback<int[]> callback);
}
