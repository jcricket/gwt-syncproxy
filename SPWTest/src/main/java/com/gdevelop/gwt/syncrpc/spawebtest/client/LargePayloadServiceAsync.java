package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.util.List;

import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LargePayloadServiceAsync {
	public void testLargeResponsePayload(AsyncCallback<List<UserInfo>> callback);

	public void testLargeResponseArray(AsyncCallback<int[]> callback);
}
