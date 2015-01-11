package com.gdevelop.gwt.syncrpc.spaapptest.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadServiceAsync;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LargePayloadServiceTest extends
		AndroidGWTTestCase<LargePayloadServiceAsync> {
	LargePayloadServiceAsync payloadService;

	public LargePayloadServiceTest() {
		super(MainActivity.class);
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				LargePayloadServiceTest.this.payloadService = SyncProxy
						.create(LargePayloadService.class);
				arg0[0].countDown();
				return null;
			}
		});
	}

	protected LargePayloadServiceAsync getAsyncService() {
		return this.payloadService;
	}

	public void testLargeResponseArray() throws Throwable {
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				getAsyncService().testLargeResponseArray(
						new AsyncCallback<int[]>() {

							@Override
							public void onFailure(Throwable caught) {
								throw new RuntimeException(caught);
							}

							@Override
							public void onSuccess(int[] result) {
								assertEquals("Wrong array size",
										LargePayloadService.ARRAY_SIZE,
										result.length);
								finishTest();
							}
						});
				return null;
			}

		});

	}

	public void testLargeResponsePayload() {
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				LargePayloadServiceAsync service = getAsyncService();

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
				return null;
			}

		});
	}
}
