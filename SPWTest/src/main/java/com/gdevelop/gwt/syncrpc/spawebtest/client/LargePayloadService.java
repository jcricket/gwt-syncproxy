package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.util.List;

import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpcsuite/largePayload")
public interface LargePayloadService extends RemoteService {
	final static int ARRAY_SIZE = 70000;
	final static int PAYLOAD_SIZE = 1000;

	public int[] testLargeResponseArray();

	public List<UserInfo> testLargeResponsePayload();
}
