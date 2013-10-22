package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.SPATests;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InheritanceTestServiceAsync;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.AnonymousClassInterface;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.Circle;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableClass;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableClassWithTransientField;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableSubclass;
import com.google.gwt.user.client.rpc.InheritanceTestSetValidator;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class InheritanceTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	InheritanceTestServiceAsync service;

	public InheritanceTest() throws Throwable {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (InheritanceTestServiceAsync) SyncProxy
						.newProxyInstance(InheritanceTestServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/",
								"inheritance", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(SPATests.WAIT_TIME_MEDIUM, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	/**
	 * Test that anonymous classes are not serializable.
	 * 
	 * @throws Throwable
	 */
	public void testAnonymousClasses() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(new AnonymousClassInterface() {
					@Override
					public void foo() {
						// purposely empty
					}
				}, new AsyncCallback<AnonymousClassInterface>() {

					@Override
					public void onFailure(Throwable caught) {
						signal.countDown();
					}

					@Override
					public void onSuccess(AnonymousClassInterface result) {
						fail("Anonymous inner classes should not be serializable");
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

	/**
	 * Tests that a shadowed field is properly serialized.
	 * 
	 * Checks for <a href="bug
	 * http://code.google.com/p/google-web-toolkit/issues/detail?id=161">BUG
	 * 161</a>
	 * 
	 * @throws Throwable
	 */
	public void testFieldShadowing() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(InheritanceTestSetFactory.createCircle(),
						new AsyncCallback<Circle>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Circle result) {
								assertNotNull(result.getName());
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

	/**
	 * Tests that transient fields do not prevent serializability.
	 * 
	 * @throws Throwable
	 */
	public void testJavaSerializableClass() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						new InheritanceTestSetFactory.JavaSerializableClass(3),
						new AsyncCallback<InheritanceTestSetFactory.JavaSerializableClass>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									InheritanceTestSetFactory.JavaSerializableClass result) {
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
		assertTrue("Failed to Complete", signal.await(SPATests.WAIT_TIME_SHORT, TimeUnit.SECONDS));
	}

	/**
	 * Test that non-static inner classes are not serializable.
	 * 
	 * @throws Throwable
	 */
	public void testNonStaticInnerClass() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						InheritanceTestSetFactory.createNonStaticInnerClass(),
						new AsyncCallback<SerializableClass>() {

							@Override
							public void onFailure(Throwable caught) {
								signal.countDown();
							}

							@Override
							public void onSuccess(SerializableClass result) {
								fail("Non-static inner classes should not be serializable");
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

	public void testReturnOfUnserializableClassFromServer() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.getUnserializableClass(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						signal.countDown();
					}

					@Override
					public void onSuccess(Void result) {
						fail("Returning an unserializable class from the server should fail");
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

	/**
	 * Test that a valid serializable class can be serialized.
	 * 
	 * @throws Throwable
	 */
	public void testSerializableClass() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						InheritanceTestSetFactory.createSerializableClass(),
						new AsyncCallback<SerializableClass>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(SerializableClass result) {
								assertNotNull(result);
								assertTrue(InheritanceTestSetValidator
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

	/**
	 * Test that IsSerializable is inherited, also test static inner classes.
	 * 
	 * @throws Throwable
	 */
	public void testSerializableSubclass() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						InheritanceTestSetFactory.createSerializableSubclass(),
						new AsyncCallback<SerializableClass>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(SerializableClass result) {
								assertNotNull(result);
								assertTrue(InheritanceTestSetValidator
										.isValid((SerializableSubclass) result));
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

	/**
	 * Tests that transient fields do not prevent serializability.
	 * 
	 * @throws Throwable
	 */
	public void testTransientFieldExclusion() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						InheritanceTestSetFactory
								.createSerializableClassWithTransientField(),
						new AsyncCallback<SerializableClassWithTransientField>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									SerializableClassWithTransientField result) {
								assertNotNull(result);
								assertTrue(InheritanceTestSetValidator
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
