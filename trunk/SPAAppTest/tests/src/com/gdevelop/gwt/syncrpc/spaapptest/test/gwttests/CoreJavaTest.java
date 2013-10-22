package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.SPATests;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CoreJavaTestServiceAsync;

public class CoreJavaTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	CoreJavaTestServiceAsync service;

	public CoreJavaTest() throws InterruptedException {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (CoreJavaTestServiceAsync) SyncProxy
						.newProxyInstance(CoreJavaTestServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/", "corejava",
								true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(SPATests.WAIT_TIME_MEDIUM, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	private static MathContext createMathContext() {
		return new MathContext(5, RoundingMode.CEILING);
	}

	public static boolean isValid(MathContext value) {
		return createMathContext().equals(value);
	}

	public void testMathContext() throws Throwable {
		final MathContext expected = createMathContext();
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoMathContext(expected,
						new AsyncCallback<MathContext>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(MathContext result) {
								assertNotNull(result);
								assertTrue(isValid(result));
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

}
