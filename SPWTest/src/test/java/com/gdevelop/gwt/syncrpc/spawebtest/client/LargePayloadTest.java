package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.util.List;

import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcTestBase;

public class LargePayloadTest extends RpcTestBase {
	public LargePayloadTest() {
	}

	protected LargePayloadServiceAsync getAsyncService() {
		LargePayloadServiceAsync service = GWT
				.create(LargePayloadService.class);
		return service;
	}

	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	public void testLargeResponseArray() {
		LargePayloadServiceAsync service = getAsyncService();
		delayTestFinishForRpc();
		service.testLargeResponseArray(new AsyncCallback<int[]>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(int[] result) {
				assertEquals("Wrong array size",
						LargePayloadService.ARRAY_SIZE, result.length);
				finishTest();
			}
		});
	}

	public void testLargeResponsePayload() {
		LargePayloadServiceAsync service = getAsyncService();
		delayTestFinishForRpc();
		service.testLargeResponsePayload(new AsyncCallback<List<UserInfo>>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(List<UserInfo> result) {
				assertEquals("Wrong list size",
						LargePayloadService.PAYLOAD_SIZE, result.size());
				finishTest();
			}
		});
	}
}
