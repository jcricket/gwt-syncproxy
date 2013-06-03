package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
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
public class InheritanceTest extends TestCase {
	private static InheritanceTestServiceAsync service = (InheritanceTestServiceAsync) SyncProxy
			.newProxyInstance(InheritanceTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "inheritance", true);

	public InheritanceTest() {
	}

	/**
	 * Test that anonymous classes are not serializable.
	 * 
	 * @throws InterruptedException
	 */
	public void testAnonymousClasses() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	/**
	 * Tests that a shadowed field is properly serialized.
	 * 
	 * Checks for <a href="bug
	 * http://code.google.com/p/google-web-toolkit/issues/detail?id=161">BUG
	 * 161</a>
	 * 
	 * @throws InterruptedException
	 */
	public void testFieldShadowing() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	/**
	 * Tests that transient fields do not prevent serializability.
	 * 
	 * @throws InterruptedException
	 */
	public void testJavaSerializableClass() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	/**
	 * Test that non-static inner classes are not serializable.
	 * 
	 * @throws InterruptedException
	 */
	public void testNonStaticInnerClass() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		service.echo(InheritanceTestSetFactory.createNonStaticInnerClass(),
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testReturnOfUnserializableClassFromServer()
			throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	/**
	 * Test that a valid serializable class can be serialized.
	 * 
	 * @throws InterruptedException
	 */
	public void testSerializableClass() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

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
						signal.countDown();
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	/**
	 * Test that IsSerializable is inherited, also test static inner classes.
	 * 
	 * @throws InterruptedException
	 */
	public void testSerializableSubclass() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

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
						signal.countDown();
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	/**
	 * Tests that transient fields do not prevent serializability.
	 * 
	 * @throws InterruptedException
	 */
	public void testTransientFieldExclusion() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

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
						signal.countDown();
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}
}
