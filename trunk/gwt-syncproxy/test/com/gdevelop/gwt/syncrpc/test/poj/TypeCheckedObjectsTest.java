package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.TypeCheckedGenericClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestServiceAsync;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedFieldClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedSuperClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetValidator;
import com.google.gwt.user.client.rpc.TypeUncheckedGenericClass;

public class TypeCheckedObjectsTest extends TestCase {
	private static TypeCheckedObjectsTestServiceAsync service = (TypeCheckedObjectsTestServiceAsync) SyncProxy
			.newProxyInstance(TypeCheckedObjectsTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "typecheckedobjects",
					true);

	public TypeCheckedObjectsTest() {
	}

	private void expectedException(Throwable caught, Class<?> expectedClass) {
		assertTrue((caught instanceof SerializationException));
		// || ((caught instanceof UndeclaredThrowableException) && (caught
		// .getCause() instanceof SerializationException)));
	}

	public void testInvalidCheckedFieldSerializer() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		@SuppressWarnings("rawtypes")
		TypeCheckedFieldClass arg1 = TypeCheckedObjectsTestSetFactory
				.createInvalidCheckedFieldClass();

		service.echo(arg1,
				new AsyncCallback<TypeCheckedFieldClass<Integer, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						expectedException(caught, SerializationException.class);
						signal.countDown();
					}

					@Override
					public void onSuccess(
							TypeCheckedFieldClass<Integer, String> result) {
						fail("testInvalidCheckedFieldSerializer is expected to throw an assertion");
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testInvalidCheckedSerializer() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		@SuppressWarnings("rawtypes")
		TypeCheckedGenericClass arg = TypeCheckedObjectsTestSetFactory
				.createInvalidCheckedGenericClass();

		service.echo(arg,
				new AsyncCallback<TypeCheckedGenericClass<Integer, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						expectedException(caught, SerializationException.class);
						signal.countDown();
					}

					@Override
					public void onSuccess(
							TypeCheckedGenericClass<Integer, String> result) {
						fail("testInvalidCheckedSerializer is expected to throw an assertion");
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testInvalidCheckedSuperSerializer() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		@SuppressWarnings("rawtypes")
		TypeCheckedSuperClass arg = TypeCheckedObjectsTestSetFactory
				.createInvalidCheckedSuperClass();

		service.echo(arg,
				new AsyncCallback<TypeCheckedSuperClass<Integer, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						expectedException(caught, SerializationException.class);
						signal.countDown();
					}

					@Override
					public void onSuccess(
							TypeCheckedSuperClass<Integer, String> result) {
						fail("testInvalidCheckedSuperSerializer is expected to throw an assertion");
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testInvalidUncheckedSerializer() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		@SuppressWarnings("rawtypes")
		TypeUncheckedGenericClass arg = TypeCheckedObjectsTestSetFactory
				.createInvalidUncheckedGenericClass();

		service.echo(
				arg,
				new AsyncCallback<TypeUncheckedGenericClass<Integer, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						expectedException(caught, SerializationException.class);
						signal.countDown();
					}

					@Override
					public void onSuccess(
							TypeUncheckedGenericClass<Integer, String> result) {
						fail("testInvalidUncheckedSerializer is expected to throw an assertion");
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testTypeCheckedFieldSerializer() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		TypeCheckedFieldClass<Integer, String> arg = TypeCheckedObjectsTestSetFactory
				.createTypeCheckedFieldClass();
		service.echo(arg,
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testTypeCheckedSerializer() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		TypeCheckedGenericClass<Integer, String> arg = TypeCheckedObjectsTestSetFactory
				.createTypeCheckedGenericClass();
		service.echo(arg,
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testTypeCheckedSuperSerializer() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		TypeCheckedSuperClass<Integer, String> arg = TypeCheckedObjectsTestSetFactory
				.createTypeCheckedSuperClass();
		service.echo(arg,
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testTypeUncheckedSerializer() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		TypeUncheckedGenericClass<Integer, String> arg = TypeCheckedObjectsTestSetFactory
				.createTypeUncheckedGenericClass();
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}
}
