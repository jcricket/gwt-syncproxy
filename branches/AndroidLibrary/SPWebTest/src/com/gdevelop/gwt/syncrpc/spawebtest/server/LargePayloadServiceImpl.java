package com.gdevelop.gwt.syncrpc.spawebtest.server;

import java.util.ArrayList;
import java.util.List;

import com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadService;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.user.server.rpc.HybridServiceServlet;

@SuppressWarnings("serial")
public class LargePayloadServiceImpl extends HybridServiceServlet implements
		LargePayloadService {
	public LargePayloadServiceImpl() {
	}

	@Override
	public List<UserInfo> testLargeResponsePayload() {
		List<UserInfo> result = new ArrayList<UserInfo>();
		for (int i = 0; i < 1000; i++) {
			UserInfo userInfo = new UserInfo();
			userInfo.setId("user_id_" + i);
			userInfo.setEmail("user" + i + "@example.com");
			result.add(userInfo);
		}
		return result;
	}

	@Override
	public int[] testLargeResponseArray() {
		int[] result = new int[70000];
		for (int i = 0; i < result.length; i++) {
			result[i] = i;
		}
		return result;
	}
}
