package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.MixedSerializable;
import com.google.gwt.user.client.rpc.MixedSerializableEchoServiceAsync;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class RunTimeSerializationErrorsTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	MixedSerializableEchoServiceAsync service;

	public RunTimeSerializationErrorsTest() throws Throwable {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (MixedSerializableEchoServiceAsync) SyncProxy
						.newProxyInstance(
								MixedSerializableEchoServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/", "echo",
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

	public void testBadSerialization1() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoVoid(new MixedSerializable.NonSerializableSub(),
						new AsyncCallback<MixedSerializable>() {

							@Override
							public void onFailure(Throwable caught) {
								signal.countDown();
							}

							@Override
							public void onSuccess(MixedSerializable result) {
								fail("RPC request should have failed");
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

	public void testBadSerialization2() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoRequest(new MixedSerializable.NonSerializableSub(),
						new AsyncCallback<MixedSerializable>() {

							@Override
							public void onFailure(Throwable caught) {
								signal.countDown();
							}

							@Override
							public void onSuccess(MixedSerializable result) {
								fail("RPC request should have failed");
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

	public void testGoodSerialization1() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final MixedSerializable.SerializableSub expected = new MixedSerializable.SerializableSub();

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoVoid(expected,
						new AsyncCallback<MixedSerializable>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(MixedSerializable result) {
								assertNotNull(result);
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

	public void xtestBadSerialization3() throws RequestException {
		// empty
	}
}
