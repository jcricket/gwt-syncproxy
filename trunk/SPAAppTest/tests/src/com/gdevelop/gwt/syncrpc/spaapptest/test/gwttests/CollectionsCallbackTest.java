package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CollectionsTestServiceAsync;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeArrayList;
import com.google.gwt.user.client.rpc.TestSetValidator;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class CollectionsCallbackTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	CollectionsTestServiceAsync service;

	public CollectionsCallbackTest() throws InterruptedException {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (CollectionsTestServiceAsync) SyncProxy
						.newProxyInstance(CollectionsTestServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/",
								"collections", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(20, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	public void testArrayList() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {

				service.echo(TestSetFactory.createArrayList(),
						new AsyncCallback<ArrayList<MarkerTypeArrayList>>() {
							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									ArrayList<MarkerTypeArrayList> result) {
								assertNotNull(result);
								assertTrue(TestSetValidator.isValid(result));
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

	// public void testBooleanArray() throws Throwable {
	// final CountDownLatch signal = new CountDownLatch(1);
	// final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void,
	// Void>() {
	//
	// @Override
	// protected Void doInBackground(Void... arg0) {
	// final Boolean[] expected = TestSetFactory.createBooleanArray();
	// CollectionsTestServiceAsync service = (CollectionsTestServiceAsync)
	// SyncProxy
	// .newProxyInstance(CollectionsTestServiceAsync.class,
	// "http://10.0.2.2:8888/spawebtest/");
	// service.echo(expected, new AsyncCallback<Boolean[]>() {
	// @Override
	// public void onFailure(Throwable caught) {
	// caught.printStackTrace();
	// }
	//
	// @Override
	// public void onSuccess(Boolean[] result) {
	// assertNotNull(result);
	// assertTrue(TestSetValidator.equals(expected, result));
	// signal.countDown();
	// }
	// });
	// return null;
	// }
	//
	// };
	// // Execute the async task on the UI thread! THIS IS KEY!
	// runTestOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// myTask.execute();
	// }
	// });
	// assertTrue("Failed to complete", signal.await(15, TimeUnit.SECONDS));
	// }
}
