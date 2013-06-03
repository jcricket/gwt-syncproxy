package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestServiceAsync;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestSetFactory;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestSetValidator;
import com.google.gwt.user.client.rpc.ManuallySerializedClass;
import com.google.gwt.user.client.rpc.ManuallySerializedImmutableClass;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class CustomFieldSerializerTest extends TestCase {

	public CustomFieldSerializerTest() {
	}

	private static CustomFieldSerializerTestServiceAsync service = (CustomFieldSerializerTestServiceAsync) SyncProxy
			.newProxyInstance(CustomFieldSerializerTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/",
					"customfieldserializers", true);

	public void testCustomFieldSerializabilityInheritance()
			throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testCustomFieldSerialization() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		service.echo(
				CustomFieldSerializerTestSetFactory.createUnserializableClass(),
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testSerializableSubclasses() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testSerializableImmutables() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		service.echo(CustomFieldSerializerTestSetFactory
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

}
