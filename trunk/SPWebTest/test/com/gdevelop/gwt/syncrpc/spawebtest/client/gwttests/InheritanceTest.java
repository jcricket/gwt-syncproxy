package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InheritanceTestService;
import com.google.gwt.user.client.rpc.InheritanceTestServiceAsync;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.AnonymousClassInterface;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.Circle;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableClass;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableClassWithTransientField;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableSubclass;
import com.google.gwt.user.client.rpc.InheritanceTestSetValidator;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class InheritanceTest extends GWTTestCase {
	private static InheritanceTestServiceAsync service;

	public InheritanceTest() {
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	/**
	 * Test that anonymous classes are not serializable.
	 */
	public void testAnonymousClasses() {
		service = GWT.create(InheritanceTestService.class);
		delayTestFinish(2000);
		service.echo(new AnonymousClassInterface() {
			@Override
			public void foo() {
				// purposely empty
			}
		}, new AsyncCallback<AnonymousClassInterface>() {

			@Override
			public void onFailure(Throwable caught) {
				finishTest();
			}

			@Override
			public void onSuccess(AnonymousClassInterface result) {
				fail("Anonymous inner classes should not be serializable");
			}
		});

	}

	/**
	 * Tests that a shadowed field is properly serialized.
	 * 
	 * Checks for <a href="bug
	 * http://code.google.com/p/google-web-toolkit/issues/detail?id=161">BUG
	 * 161</a>
	 */
	public void testFieldShadowing() {
		service = GWT.create(InheritanceTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/inheritance");
		service.echo(InheritanceTestSetFactory.createCircle(),
				new AsyncCallback<Circle>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Circle result) {
						assertNotNull(result.getName());
						finishTest();
					}
				});
		delayTestFinish(2000);
	}

	/**
	 * Tests that transient fields do not prevent serializability.
	 */
	public void testJavaSerializableClass() {
		service = GWT.create(InheritanceTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/inheritance");
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
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	/**
	 * Test that non-static inner classes are not serializable.
	 */
	public void testNonStaticInnerClass() {
		service = GWT.create(InheritanceTestService.class);
		delayTestFinish(2000);
		service.echo(InheritanceTestSetFactory.createNonStaticInnerClass(),
				new AsyncCallback<SerializableClass>() {

					@Override
					public void onFailure(Throwable caught) {
						finishTest();
					}

					@Override
					public void onSuccess(SerializableClass result) {
						fail("Non-static inner classes should not be serializable");
					}

				});

	}

	public void testReturnOfUnserializableClassFromServer() {
		service = GWT.create(InheritanceTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/inheritance");
		service.getUnserializableClass(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				finishTest();
			}

			@Override
			public void onSuccess(Void result) {
				fail("Returning an unserializable class from the server should fail");
			}

		});
		delayTestFinish(2000);
	}

	/**
	 * Test that a valid serializable class can be serialized.
	 */
	public void testSerializableClass() {
		service = GWT.create(InheritanceTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/inheritance");
		service.echo(InheritanceTestSetFactory.createSerializableClass(),
				new AsyncCallback<SerializableClass>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(SerializableClass result) {
						assertNotNull(result);
						assertTrue(InheritanceTestSetValidator.isValid(result));
						finishTest();
					}

				});
		delayTestFinish(2000);
	}

	/**
	 * Test that IsSerializable is inherited, also test static inner classes.
	 */
	public void testSerializableSubclass() {
		service = GWT.create(InheritanceTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/inheritance");
		service.echo(InheritanceTestSetFactory.createSerializableSubclass(),
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
						finishTest();
					}

				});
	}

	/**
	 * Tests that transient fields do not prevent serializability.
	 */
	public void testTransientFieldExclusion() {
		service = GWT.create(InheritanceTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/inheritance");
		service.echo(InheritanceTestSetFactory
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
						assertTrue(InheritanceTestSetValidator.isValid(result));
						finishTest();
					}

				});
		delayTestFinish(2000);
	}
}
