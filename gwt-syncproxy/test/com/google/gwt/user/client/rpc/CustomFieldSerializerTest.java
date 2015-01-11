/*
 * Copyright 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.user.client.rpc;

import junit.framework.AssertionFailedError;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestSetFactory.SerializableSubclass;

/**
 * Tests the following scenarios.
 * <ul>
 * <li>Manually serializable types use their custom field serializer</li>
 * <li>Subtypes of manually serializable types that are not auto-serializable
 * fail to be serialized</li>
 * <li>Automatically serializable subtypes of manually serialized types can be
 * serialized</li>
 * </ul>
 *
 * Modified by P.Prith in 0.5 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class CustomFieldSerializerTest extends RpcTestBase {

	private CustomFieldSerializerTestServiceAsync customFieldSerializerTestService;

	private CustomFieldSerializerTestServiceAsync getServiceAsync() {
		if (this.customFieldSerializerTestService == null) {
			this.customFieldSerializerTestService = SyncProxy
					.create(CustomFieldSerializerTestService.class);
			((ServiceDefTarget) this.customFieldSerializerTestService)
			.setServiceEntryPoint(getModuleBaseURL()
					+ "customfieldserializers");
		}
		return this.customFieldSerializerTestService;
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		SyncProxy.suppressRelativePathWarning(true);
	}

	/**
	 * Test that custom field serializers do not make their subclasses
	 * serializable.
	 */
	public void testCustomFieldSerializabilityInheritance() {
		CustomFieldSerializerTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		service.echo(CustomFieldSerializerTestSetFactory
				.createUnserializableSubclass(), new AsyncCallback() {
			@Override
			public void onFailure(Throwable caught) {
				assertTrue("Should be a SerializationException",
						caught instanceof SerializationException);
				finishTest();
			}

			@Override
			public void onSuccess(Object result) {
				fail("Class UnserializableSubclass should not be serializable");
			}
		});
	}

	/**
	 * Tests that the custom field serializers are actually called when the
	 * custom field serializer does not derive from
	 * {@link CustomFieldSerializer}
	 */
	public void testCustomFieldSerialization() {
		CustomFieldSerializerTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		service.echo(
				CustomFieldSerializerTestSetFactory.createUnserializableClass(),
				new AsyncCallback() {
					@Override
					public void onFailure(Throwable caught) {
						AssertionFailedError er = new AssertionFailedError(
								"Class UnserializableClass should be serializable because it has a custom field serializer");
						er.initCause(caught);
						throw er;
					}

					@Override
					public void onSuccess(Object result) {
						assertNotNull(result);
						assertTrue(CustomFieldSerializerTestSetValidator
								.isValid((ManuallySerializedClass) result));
						finishTest();
					}
				});
	}

	/**
	 * Test that custom serializers that call readObject() inside instantiate
	 * (as is required for most immutable classes) work.
	 *
	 * This also checks that custom <code>instantiate</code> works when the
	 * custom serializer does not implement {@link CustomFieldSerializer}.
	 */
	public void testSerializableImmutables() {
		CustomFieldSerializerTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		service.echo(CustomFieldSerializerTestSetFactory
				.createSerializableImmutablesArray(), new AsyncCallback() {
			@Override
			public void onFailure(Throwable caught) {
				AssertionFailedError er = new AssertionFailedError(
						"Could not serialize/deserialize immutable classes");
				er.initCause(caught);
				throw er;
			}

			@Override
			public void onSuccess(Object result) {
				assertNotNull(result);
				assertTrue(CustomFieldSerializerTestSetValidator
						.isValid((ManuallySerializedImmutableClass[]) result));
				finishTest();
			}
		});
	}

	/**
	 * Test that serializable subclasses of classes that have custom field
	 * serializers serialize and deserialize correctly.
	 */
	public void testSerializableSubclasses() {
		CustomFieldSerializerTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		service.echo(CustomFieldSerializerTestSetFactory
				.createSerializableSubclass(), new AsyncCallback() {
			@Override
			public void onFailure(Throwable caught) {
				AssertionFailedError er = new AssertionFailedError(
						"Class SerializableSubclass should be serializable automatically");
				er.initCause(caught);
				throw er;
			}

			@Override
			public void onSuccess(Object result) {
				assertNotNull(result);
				assertTrue(CustomFieldSerializerTestSetValidator
						.isValid((SerializableSubclass) result));
				finishTest();
			}
		});
	}
}
