package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.SPATests;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ValueTypesTestServiceAsync;

public class HttpsTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private static ValueTypesTestServiceAsync service;

	public HttpsTest() throws InterruptedException {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (ValueTypesTestServiceAsync) SyncProxy
						.newProxyInstance(ValueTypesTestServiceAsync.class,
								"https://10.0.2.2:8888/spawebtest/",
								"valuetypes", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(SPATests.WAIT_TIME_MEDIUM, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	public void testBoolean_FALSE() throws Throwable {
		fail("Fails with SSL ErrorL Plain Text Issue");
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_FALSE(false, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Boolean result) {
						assertNotNull(result);
						assertFalse(result.booleanValue());
					}

				});
				return null;
			}
		};
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				myTask.execute();
			}
		});
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}
}
