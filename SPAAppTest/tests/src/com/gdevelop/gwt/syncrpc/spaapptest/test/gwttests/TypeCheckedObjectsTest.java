package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.TypeCheckedGenericClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestServiceAsync;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedFieldClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedSuperClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetValidator;
import com.google.gwt.user.client.rpc.TypeUncheckedGenericClass;

public class TypeCheckedObjectsTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	TypeCheckedObjectsTestServiceAsync service;

	public TypeCheckedObjectsTest() throws Throwable {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (TypeCheckedObjectsTestServiceAsync) SyncProxy
						.newProxyInstance(
								TypeCheckedObjectsTestServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/",
								"typecheckedobjects", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(20, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	private void expectedException(Throwable caught, Class<?> expectedClass) {
		assertTrue((caught instanceof SerializationException));
		// || ((caught instanceof UndeclaredThrowableException) && (caught
		// .getCause() instanceof SerializationException)));
	}

	public void testInvalidCheckedFieldSerializer() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		@SuppressWarnings("rawtypes")
		final TypeCheckedFieldClass arg1 = TypeCheckedObjectsTestSetFactory
				.createInvalidCheckedFieldClass();

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						arg1,
						new AsyncCallback<TypeCheckedFieldClass<Integer, String>>() {

							@Override
							public void onFailure(Throwable caught) {
								expectedException(caught,
										SerializationException.class);
								signal.countDown();
							}

							@Override
							public void onSuccess(
									TypeCheckedFieldClass<Integer, String> result) {
								fail("testInvalidCheckedFieldSerializer is expected to throw an assertion");
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

	public void testInvalidCheckedSerializer() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		@SuppressWarnings("rawtypes")
		final TypeCheckedGenericClass arg = TypeCheckedObjectsTestSetFactory
				.createInvalidCheckedGenericClass();

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						arg,
						new AsyncCallback<TypeCheckedGenericClass<Integer, String>>() {

							@Override
							public void onFailure(Throwable caught) {
								expectedException(caught,
										SerializationException.class);
								signal.countDown();
							}

							@Override
							public void onSuccess(
									TypeCheckedGenericClass<Integer, String> result) {
								fail("testInvalidCheckedSerializer is expected to throw an assertion");
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

	public void testInvalidCheckedSuperSerializer() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		@SuppressWarnings("rawtypes")
		final TypeCheckedSuperClass arg = TypeCheckedObjectsTestSetFactory
				.createInvalidCheckedSuperClass();

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						arg,
						new AsyncCallback<TypeCheckedSuperClass<Integer, String>>() {

							@Override
							public void onFailure(Throwable caught) {
								expectedException(caught,
										SerializationException.class);
								signal.countDown();
							}

							@Override
							public void onSuccess(
									TypeCheckedSuperClass<Integer, String> result) {
								fail("testInvalidCheckedSuperSerializer is expected to throw an assertion");
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

	public void testInvalidUncheckedSerializer() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		@SuppressWarnings("rawtypes")
		final TypeUncheckedGenericClass arg = TypeCheckedObjectsTestSetFactory
				.createInvalidUncheckedGenericClass();

		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						arg,
						new AsyncCallback<TypeUncheckedGenericClass<Integer, String>>() {

							@Override
							public void onFailure(Throwable caught) {
								expectedException(caught,
										SerializationException.class);
								signal.countDown();
							}

							@Override
							public void onSuccess(
									TypeUncheckedGenericClass<Integer, String> result) {
								fail("testInvalidUncheckedSerializer is expected to throw an assertion");
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

	public void testTypeCheckedFieldSerializer() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final TypeCheckedFieldClass<Integer, String> arg = TypeCheckedObjectsTestSetFactory
				.createTypeCheckedFieldClass();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						arg,
						new AsyncCallback<TypeCheckedFieldClass<Integer, String>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									TypeCheckedFieldClass<Integer, String> result) {
								assertNotNull(result);
								assertTrue(TypeCheckedObjectsTestSetValidator
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testTypeCheckedSerializer() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final TypeCheckedGenericClass<Integer, String> arg = TypeCheckedObjectsTestSetFactory
				.createTypeCheckedGenericClass();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						arg,
						new AsyncCallback<TypeCheckedGenericClass<Integer, String>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									TypeCheckedGenericClass<Integer, String> result) {
								assertNotNull(result);
								assertTrue(TypeCheckedObjectsTestSetValidator
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testTypeCheckedSuperSerializer() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final TypeCheckedSuperClass<Integer, String> arg = TypeCheckedObjectsTestSetFactory
				.createTypeCheckedSuperClass();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						arg,
						new AsyncCallback<TypeCheckedSuperClass<Integer, String>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									TypeCheckedSuperClass<Integer, String> result) {
								assertNotNull(result);
								assertTrue(TypeCheckedObjectsTestSetValidator
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testTypeUncheckedSerializer() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);

		final TypeUncheckedGenericClass<Integer, String> arg = TypeCheckedObjectsTestSetFactory
				.createTypeUncheckedGenericClass();
		final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						arg,
						new AsyncCallback<TypeUncheckedGenericClass<Integer, String>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									TypeUncheckedGenericClass<Integer, String> result) {
								assertNotNull(result);
								assertTrue(TypeCheckedObjectsTestSetValidator
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}
}
