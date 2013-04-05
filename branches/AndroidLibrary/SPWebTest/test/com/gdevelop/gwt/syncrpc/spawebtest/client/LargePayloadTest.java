package com.gdevelop.gwt.syncrpc.spawebtest.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LargePayloadTest extends GWTTestCase {
	// private static LargePayloadService service = (LargePayloadService)
	// SyncProxy
	// .newProxyInstance(LargePayloadService.class,
	// RPCSyncTestSuite.BASE_URL);

	public LargePayloadTest() {
	}

	public void xx_testLargeResponsePayload() {
		// LargePayloadService service = GWT.create(LargePayloadService.class);
		// List<UserInfo> userList = service.testLargeResponsePayload();
		// System.out.println(userList);
	}

	public void testLargeResponseArray() {
		LargePayloadServiceAsync service = GWT
				.create(LargePayloadService.class);

		service.testLargeResponseArray(new AsyncCallback<int[]>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(int[] result) {
				assertEquals("Wrong array size", 70000, result.length);
				finishTest();
			}
		});
		delayTestFinish(15000);
	}

	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}
}
