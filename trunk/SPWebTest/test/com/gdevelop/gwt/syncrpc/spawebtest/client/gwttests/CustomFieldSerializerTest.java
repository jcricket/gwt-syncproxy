package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestService;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestServiceAsync;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestSetFactory;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestSetValidator;
import com.google.gwt.user.client.rpc.ManuallySerializedClass;
import com.google.gwt.user.client.rpc.ManuallySerializedImmutableClass;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class CustomFieldSerializerTest extends GWTTestCase {

	public CustomFieldSerializerTest() {
	}

	public void testCustomFieldSerializabilityInheritance() {
		CustomFieldSerializerTestServiceAsync service = GWT
				.create(CustomFieldSerializerTestService.class);
		delayTestFinish(2000);
		service.echo(CustomFieldSerializerTestSetFactory
				.createUnserializableSubclass(),
				new AsyncCallback<ManuallySerializedClass>() {

					@Override
					public void onFailure(Throwable caught) {
						finishTest();
					}

					@Override
					public void onSuccess(ManuallySerializedClass result) {
						assertTrue(
								"Class Unserializable Subclass should not be serializable",
								false);
					}
				});

	}

	public void testCustomFieldSerialization() {
		CustomFieldSerializerTestServiceAsync service = GWT
				.create(CustomFieldSerializerTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/customfieldserializers");
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
						finishTest();
					}
				});
		delayTestFinish(2000);
	}

	public void testSerializableSubclasses() {
		CustomFieldSerializerTestServiceAsync service = GWT
				.create(CustomFieldSerializerTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/customfieldserializers");
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
						finishTest();
					}
				});
		delayTestFinish(2000);
	}

	public void testSerializableImmutables() {
		CustomFieldSerializerTestServiceAsync service = GWT
				.create(CustomFieldSerializerTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/customfieldserializers");
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
						finishTest();
					}
				});
		delayTestFinish(2000);
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}
}
