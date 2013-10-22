package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.SPATests;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestServiceAsync;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestSetFactory;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestSetValidator;
import com.google.gwt.user.client.rpc.ManuallySerializedClass;
import com.google.gwt.user.client.rpc.ManuallySerializedImmutableClass;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class CustomFieldSerializerTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	CustomFieldSerializerTestServiceAsync service;

	public CustomFieldSerializerTest() throws InterruptedException {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (CustomFieldSerializerTestServiceAsync) SyncProxy
						.newProxyInstance(
								CustomFieldSerializerTestServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/",
								"customfieldserializers", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(SPATests.WAIT_TIME_MEDIUM, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	public void testCustomFieldSerializabilityInheritance() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(CustomFieldSerializerTestSetFactory
						.createUnserializableSubclass(),
						new AsyncCallback<ManuallySerializedClass>() {

							@Override
							public void onFailure(Throwable caught) {
								signal.countDown();
							}

							@Override
							public void onSuccess(ManuallySerializedClass result) {
								assertTrue(
										"Class Unserializable Subclass should not be serializable",
										false);
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

	public void testCustomFieldSerialization() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(CustomFieldSerializerTestSetFactory
						.createUnserializableClass(),
						new AsyncCallback<ManuallySerializedClass>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(ManuallySerializedClass result) {
								assertNotNull(result);
								assertTrue(CustomFieldSerializerTestSetValidator
										.isValid(result));
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

	public void testSerializableSubclasses() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(CustomFieldSerializerTestSetFactory
						.createSerializableSubclass(),
						new AsyncCallback<ManuallySerializedClass>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(ManuallySerializedClass result) {
								assertNotNull(result);
								assertTrue(CustomFieldSerializerTestSetValidator
										.isValid((CustomFieldSerializerTestSetFactory.SerializableSubclass) result));
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

	public void testSerializableImmutables() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						CustomFieldSerializerTestSetFactory
								.createSerializableImmutablesArray(),
						new AsyncCallback<ManuallySerializedImmutableClass[]>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									ManuallySerializedImmutableClass[] result) {
								assertNotNull(result);
								assertTrue(CustomFieldSerializerTestSetValidator
										.isValid(result));
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
