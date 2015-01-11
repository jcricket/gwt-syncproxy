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

import java.util.concurrent.CountDownLatch;

import junit.framework.AssertionFailedError;
import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.AndroidGWTTestCase;
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
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class CustomFieldSerializerTest extends
		AndroidGWTTestCase<CustomFieldSerializerTestServiceAsync> {

	private CustomFieldSerializerTestServiceAsync customFieldSerializerTestService;

	public CustomFieldSerializerTest() throws InterruptedException {
		super(MainActivity.class);
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				CustomFieldSerializerTest.this.customFieldSerializerTestService = SyncProxy
						.create(CustomFieldSerializerTestService.class);
				((ServiceDefTarget) CustomFieldSerializerTest.this.customFieldSerializerTestService)
				.setServiceEntryPoint(getModuleBaseURL()
						+ "customfieldserializers");
				arg0[0].countDown();
				return null;
			}
		});
	}

	private CustomFieldSerializerTestServiceAsync getServiceAsync() {

		return this.customFieldSerializerTestService;
	}

	/**
	 * @see com.gdevelop.gwt.syncrpc.spaapptest.test.AndroidGWTTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		SyncProxy.suppressRelativePathWarning(true);
		super.setUp();
	}

	/**
	 * Test that custom field serializers do not make their subclasses
	 * serializable.
	 */
	public void testCustomFieldSerializabilityInheritance() {
		final CustomFieldSerializerTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
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
				return null;
			}
		});
	}

	/**
	 * Tests that the custom field serializers are actually called when the
	 * custom field serializer does not derive from
	 * {@link CustomFieldSerializer}
	 */
	public void testCustomFieldSerialization() {
		final CustomFieldSerializerTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(CustomFieldSerializerTestSetFactory
						.createUnserializableClass(), new AsyncCallback() {
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
				return null;
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
		final CustomFieldSerializerTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(CustomFieldSerializerTestSetFactory
						.createSerializableImmutablesArray(),
						new AsyncCallback() {
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
				return null;
			}
		});
	}

	/**
	 * Test that serializable subclasses of classes that have custom field
	 * serializers serialize and deserialize correctly.
	 */
	public void testSerializableSubclasses() {
		final CustomFieldSerializerTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
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
				return null;
			}
		});
	}
}
