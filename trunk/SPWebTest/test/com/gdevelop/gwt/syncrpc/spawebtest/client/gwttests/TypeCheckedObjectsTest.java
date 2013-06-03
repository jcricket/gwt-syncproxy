package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.TypeCheckedGenericClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestService;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestServiceAsync;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedFieldClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedSuperClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetValidator;
import com.google.gwt.user.client.rpc.TypeUncheckedGenericClass;

public class TypeCheckedObjectsTest extends GWTTestCase {
	private static TypeCheckedObjectsTestServiceAsync service;

	public TypeCheckedObjectsTest() {
	}

	private void expectedException(Throwable caught, Class<?> expectedClass) {
		assertTrue((caught instanceof SerializationException));
		// || ((caught instanceof UndeclaredThrowableException) && (caught
		// .getCause() instanceof SerializationException)));
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	public void testInvalidCheckedFieldSerializer() {
		service = GWT.create(TypeCheckedObjectsTestService.class);
		@SuppressWarnings("rawtypes")
		TypeCheckedFieldClass arg1 = TypeCheckedObjectsTestSetFactory
				.createInvalidCheckedFieldClass();
		delayTestFinish(2000);
		service.echo(arg1,
				new AsyncCallback<TypeCheckedFieldClass<Integer, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						expectedException(caught, SerializationException.class);
						finishTest();
					}

					@Override
					public void onSuccess(
							TypeCheckedFieldClass<Integer, String> result) {
						fail("testInvalidCheckedFieldSerializer is expected to throw an assertion");
					}

				});

	}

	public void testInvalidCheckedSerializer() {
		service = GWT.create(TypeCheckedObjectsTestService.class);
		@SuppressWarnings("rawtypes")
		TypeCheckedGenericClass arg = TypeCheckedObjectsTestSetFactory
				.createInvalidCheckedGenericClass();
		delayTestFinish(2000);
		service.echo(arg,
				new AsyncCallback<TypeCheckedGenericClass<Integer, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						expectedException(caught, SerializationException.class);
						finishTest();
					}

					@Override
					public void onSuccess(
							TypeCheckedGenericClass<Integer, String> result) {
						fail("testInvalidCheckedSerializer is expected to throw an assertion");
					}

				});
	}

	public void testInvalidCheckedSuperSerializer() {
		service = GWT.create(TypeCheckedObjectsTestService.class);
		@SuppressWarnings("rawtypes")
		TypeCheckedSuperClass arg = TypeCheckedObjectsTestSetFactory
				.createInvalidCheckedSuperClass();
		delayTestFinish(2000);
		service.echo(arg,
				new AsyncCallback<TypeCheckedSuperClass<Integer, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						expectedException(caught, SerializationException.class);
						finishTest();
					}

					@Override
					public void onSuccess(
							TypeCheckedSuperClass<Integer, String> result) {
						fail("testInvalidCheckedSuperSerializer is expected to throw an assertion");
					}

				});

	}

	public void testInvalidUncheckedSerializer() {
		service = GWT.create(TypeCheckedObjectsTestService.class);
		@SuppressWarnings("rawtypes")
		TypeUncheckedGenericClass arg = TypeCheckedObjectsTestSetFactory
				.createInvalidUncheckedGenericClass();
		delayTestFinish(2000);
		service.echo(
				arg,
				new AsyncCallback<TypeUncheckedGenericClass<Integer, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						expectedException(caught, SerializationException.class);
						finishTest();
					}

					@Override
					public void onSuccess(
							TypeUncheckedGenericClass<Integer, String> result) {
						fail("testInvalidUncheckedSerializer is expected to throw an assertion");
					}

				});

	}

	public void testTypeCheckedFieldSerializer() {
		service = GWT.create(TypeCheckedObjectsTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/typecheckedobjects");
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testTypeCheckedSerializer() {
		service = GWT.create(TypeCheckedObjectsTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/typecheckedobjects");
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	public void testTypeCheckedSuperSerializer() {
		service = GWT.create(TypeCheckedObjectsTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/typecheckedobjects");
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
						finishTest();
					}

				});
		delayTestFinish(1000);
	}

	public void testTypeUncheckedSerializer() {
		service = GWT.create(TypeCheckedObjectsTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/typecheckedobjects");
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}
}
