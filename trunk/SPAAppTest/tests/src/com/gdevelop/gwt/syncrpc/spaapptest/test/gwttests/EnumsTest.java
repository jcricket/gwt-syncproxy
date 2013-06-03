package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestService.Complex;
import com.google.gwt.user.client.rpc.EnumsTestService.Subclassing;
import com.google.gwt.user.client.rpc.EnumsTestServiceAsync;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class EnumsTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private static EnumsTestServiceAsync service;

	public EnumsTest() throws InterruptedException {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (EnumsTestServiceAsync) SyncProxy.newProxyInstance(
						EnumsTestServiceAsync.class,
						"http://10.0.2.2:8888/spawebtest/", "enums", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(20, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	public void testBasicEnums() throws Throwable {
		assertTrue("Server Fails for Enum IsSerializable", false);
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Basic.A, new AsyncCallback<Basic>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Basic result) {
						assertEquals(Basic.A, result);
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

	public void testComplexEnums() throws Throwable {
		assertTrue("Server Fails for Enum IsSerializable", false);
		final CountDownLatch signal = new CountDownLatch(1);

		Complex a = Complex.A;
		a.value = "client";

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Complex.A, new AsyncCallback<Complex>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Complex result) {
						assertEquals(Complex.A, result);
						assertEquals("client", result.value);
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

	public void testNull() throws Throwable {
		fail("Throws IncompatibleRemoteServiceException on server");
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo((Basic) null, new AsyncCallback<Basic>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Basic result) {
						assertNull(result);
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
		assertTrue("Failed to Complete", signal.await(20, TimeUnit.SECONDS));
	}

	public void testSubclassingEnums() throws Throwable {
		assertTrue("Server Fails for Enum IsSerializable", false);
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(Subclassing.A, new AsyncCallback<Subclassing>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Subclassing result) {
						assertEquals(Subclassing.A, result);
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
