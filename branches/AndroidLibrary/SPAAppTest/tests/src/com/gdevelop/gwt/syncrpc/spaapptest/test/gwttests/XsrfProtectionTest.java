package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.TestSetValidator;
import com.google.gwt.user.client.rpc.XsrfTestServiceAsync;

public class XsrfProtectionTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private static XsrfTestServiceAsync service;

	public XsrfProtectionTest() throws InterruptedException {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (XsrfTestServiceAsync) SyncProxy.newProxyInstance(
						XsrfTestServiceAsync.class,
						"http://10.0.2.2:8888/spawebtest/", "xsrftestservice",
						true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(20, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	private void checkServerState(final String drink,
			final boolean stateShouldChange) throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.checkIfDrankDrink(drink, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Boolean result) {
						assertTrue(stateShouldChange == result);
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

	@Override
	protected void setUp() {

	}

	public void testRpcWithoutXsrfTokenFails() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.drink("kumys", new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						RpcTokenException e = (RpcTokenException) caught;
						assertTrue(e.getMessage()
								.contains("XSRF token missing"));
						try {
							checkServerState("kumys", false);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						} catch (Throwable e1) {
						}
						signal.countDown();
					}

					@Override
					public void onSuccess(Void result) {
						fail("Should've failed without XSRF token");
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
		assertTrue("Failed to Complete", signal.await(4, TimeUnit.SECONDS));
	}

	public void xtestRpcWithBadXsrfTokenFails() throws Exception {
		assertTrue("Not Yet Implemented", false);
	}

	public void xtestRpcWithXsrfToken() throws Exception {
		assertTrue("Not Yet Implemented", false);
	}

	public void xtestXsrfTokenService() throws Exception {
		assertTrue("Not Yet Implemented", false);
	}

	public void xtestXsrfTokenWithDifferentSessionCookieFails()
			throws Exception {
		assertTrue("Not Yet Implemented", false);
	}
}
