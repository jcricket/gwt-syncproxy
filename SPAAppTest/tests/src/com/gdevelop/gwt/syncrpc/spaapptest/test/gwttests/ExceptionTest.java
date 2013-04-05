package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ExceptionsTestServiceAsync;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetValidator;

public class ExceptionTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	ExceptionsTestServiceAsync service;

	public ExceptionTest() throws InterruptedException {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (ExceptionsTestServiceAsync) SyncProxy
						.newProxyInstance(ExceptionsTestServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/",
								"exceptions", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(20, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	public void testException() throws Throwable {
		assertTrue(
				"Failed due to UmbrellaException not IsSerializable on server",
				false);
		final UmbrellaException expected = TestSetFactory
				.createUmbrellaException();

		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(expected, new AsyncCallback<UmbrellaException>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(UmbrellaException result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(expected, result));
						signal.countDown();
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}
}
