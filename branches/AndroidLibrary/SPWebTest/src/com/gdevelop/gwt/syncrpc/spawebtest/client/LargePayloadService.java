package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.util.List;

import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpcsuite/largePayload")
public interface LargePayloadService extends RemoteService {
	public List<UserInfo> testLargeResponsePayload();

	public int[] testLargeResponseArray();
}
