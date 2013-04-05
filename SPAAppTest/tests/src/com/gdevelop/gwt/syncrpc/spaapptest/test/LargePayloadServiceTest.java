package com.gdevelop.gwt.syncrpc.spaapptest.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LargePayloadServiceTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	public LargePayloadServiceTest() {
		super(MainActivity.class);
	}

	public void testLargeResponseArray() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {

				LargePayloadServiceAsync service = (LargePayloadServiceAsync) SyncProxy
						.newProxyInstance(LargePayloadServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/");
				service.testLargeResponseArray(new AsyncCallback<int[]>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(int[] result) {
						assertEquals("Wrong array size", 70000, result.length);
						signal.countDown();
					}
				});
				return null;
			}

		};
		// Execute the async task on the UI thread! THIS IS KEY!
		runTestOnUiThread(new Runnable() {

			@Override
			public void run() {
				myTask.execute();
			}
		});
		assertTrue("Failed to complete", signal.await(15, TimeUnit.SECONDS));
	}
}
