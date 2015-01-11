/*
 * Copyright 2011 Google Inc.
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
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedFieldClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedNestedLists;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedSuperClass;

/**
 * Test for RPC serialization of type checked objects.
 *
 * Type checked objects are those that are verified as being of the correct type
 * before they are deserialized, thus catching certain attacks that occur
 * through deserialization.
 *
 * Test Cases: - Type checked generic class with a server-side custom serializer
 * that is NOT derived from ServerCustomFieldSerializer but which does define
 * instantiateChecked and deserializeChecked, to verify that such methods are
 * found and called. - Generic class that has no custom field serializer but
 * which does include fields that do have type checked serializers, to verify
 * that such serializers are still used. - Generic class that has no custom
 * field serializer but which does extend a class with type checked serializers,
 * to verify that such serializers are still used.
 *
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0. Also changed to catch
 * {@link IncompatibleRemoteServiceException} since these are server calls
 * instead of just {@link SerializableException}. See code modified with Tag
 * GSP-0.4.4.
 */
public class TypeCheckedObjectsTest extends
AndroidGWTTestCase<TypeCheckedObjectsTestServiceAsync> {

	private TypeCheckedObjectsTestServiceAsync typeCheckedObjectsTestService;

	public TypeCheckedObjectsTest() throws InterruptedException {
		super(MainActivity.class);

		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				TypeCheckedObjectsTest.this.typeCheckedObjectsTestService = SyncProxy
						.create(TypeCheckedObjectsTestService.class);
				((ServiceDefTarget) TypeCheckedObjectsTest.this.typeCheckedObjectsTestService)
				.setServiceEntryPoint(getModuleBaseURL()
						+ "typecheckedobjects");
				arg0[0].countDown();
				return null;
			}
		});
	}

	private TypeCheckedObjectsTestServiceAsync getServiceAsync() {

		return this.typeCheckedObjectsTestService;
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		SyncProxy.suppressRelativePathWarning(true);
		super.setUp();
	}

	public void testInvalidCheckedFieldSerializer() {
		final TypeCheckedObjectsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						TypeCheckedObjectsTestSetFactory
						.createInvalidCheckedFieldClass(),
						new AsyncCallback<TypeCheckedFieldClass<Integer, String>>() {
							@Override
							public void onFailure(Throwable caught) {
								// Expected in this case
								assertTrue(caught instanceof SerializationException);
								finishTest();
							}

							@Override
							public void onSuccess(
									TypeCheckedFieldClass<Integer, String> result) {
								fail("testInvalidCheckedFieldSerializer is expected to throw an assertion");
							}
						});
				return null;
			}
		});
	}

	public void testInvalidCheckedSerializer() {
		final TypeCheckedObjectsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						TypeCheckedObjectsTestSetFactory
						.createInvalidCheckedGenericClass(),
						new AsyncCallback<TypeCheckedGenericClass<Integer, String>>() {
							@Override
							public void onFailure(Throwable caught) {
								// Expected in this case
								assertTrue(caught instanceof SerializationException);
								finishTest();
							}

							@Override
							public void onSuccess(
									TypeCheckedGenericClass<Integer, String> result) {
								fail("testInvalidCheckedSerializer is expected to throw an assertion");
							}
						});
				return null;
			}
		});
	}

	public void testInvalidCheckedSuperSerializer() {
		final TypeCheckedObjectsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						TypeCheckedObjectsTestSetFactory
						.createInvalidCheckedSuperClass(),
						new AsyncCallback<TypeCheckedSuperClass<Integer, String>>() {
							@Override
							public void onFailure(Throwable caught) {
								// Expected in this case
								assertTrue(caught instanceof SerializationException);
								finishTest();
							}

							@Override
							public void onSuccess(
									TypeCheckedSuperClass<Integer, String> result) {
								fail("testInvalidCheckedSerializer is expected to throw an assertion");
							}
						});
				return null;
			}
		});
	}

	public void testInvalidUncheckedSerializer() {
		final TypeCheckedObjectsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						TypeCheckedObjectsTestSetFactory
						.createInvalidUncheckedGenericClass(),
						new AsyncCallback<TypeUncheckedGenericClass<Integer, String>>() {
							@Override
							public void onFailure(Throwable caught) {
								// Expected in this case
								assertTrue(caught instanceof SerializationException);
								finishTest();
							}

							@Override
							public void onSuccess(
									TypeUncheckedGenericClass<Integer, String> result) {
								fail("testInvalidUncheckedSerializer is expected to throw an assertion");
							}
						});
				return null;
			}
		});
	}

	public void testTypeCheckedFieldSerializer() {
		final TypeCheckedObjectsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						TypeCheckedObjectsTestSetFactory
						.createTypeCheckedFieldClass(),
						new AsyncCallback<TypeCheckedFieldClass<Integer, String>>() {
							@Override
							public void onFailure(Throwable caught) {
								AssertionFailedError er = new AssertionFailedError(
										"Could not serialize/deserialize TypeCheckedFieldClass");
								er.initCause(caught);
								throw er;
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
				return null;
			}
		});
	}

	public void testTypeCheckedNestedLists() {
		final TypeCheckedObjectsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(TypeCheckedObjectsTestSetFactory
						.createTypeCheckedNestedLists(),
						new AsyncCallback<TypeCheckedNestedLists>() {
					@Override
					public void onFailure(Throwable caught) {
						AssertionFailedError er = new AssertionFailedError(
								"Could not serialize/deserialize TypeCheckedNestedLists");
						er.initCause(caught);
						throw er;
					}

					@Override
					public void onSuccess(TypeCheckedNestedLists result) {
						assertNotNull(result);
						assertTrue(TypeCheckedObjectsTestSetValidator
								.isValid(result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testTypeCheckedSerializer() {
		final TypeCheckedObjectsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						TypeCheckedObjectsTestSetFactory
						.createTypeCheckedGenericClass(),
						new AsyncCallback<TypeCheckedGenericClass<Integer, String>>() {
							@Override
							public void onFailure(Throwable caught) {
								AssertionFailedError er = new AssertionFailedError(
										"Could not serialize/deserialize TypeCheckedGenericClass");
								er.initCause(caught);
								throw er;
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
				return null;
			}
		});
	}

	public void testTypeCheckedSuperSerializer() {
		final TypeCheckedObjectsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						TypeCheckedObjectsTestSetFactory
						.createTypeCheckedSuperClass(),
						new AsyncCallback<TypeCheckedSuperClass<Integer, String>>() {
							@Override
							public void onFailure(Throwable caught) {
								AssertionFailedError er = new AssertionFailedError(
										"Could not serialize/deserialize TypeCheckedGenericClass");
								er.initCause(caught);
								throw er;
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
				return null;
			}
		});
	}

	public void testTypeUncheckedSerializer() {
		final TypeCheckedObjectsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						TypeCheckedObjectsTestSetFactory
						.createTypeUncheckedGenericClass(),
						new AsyncCallback<TypeUncheckedGenericClass<Integer, String>>() {
							@Override
							public void onFailure(Throwable caught) {
								AssertionFailedError er = new AssertionFailedError(
										"Could not serialize/deserialize TypeUncheckedGenericClass");
								er.initCause(caught);
								throw er;
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
				return null;
			}
		});
	}
}
