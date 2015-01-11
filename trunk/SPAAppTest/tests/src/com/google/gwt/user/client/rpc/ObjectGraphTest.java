/*
 * Copyright 2008 Google Inc.
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

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.AndroidGWTTestCase;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializableDoublyLinkedNode;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializableGraphWithCFS;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializablePrivateNoArg;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializableWithTwoArrays;
import com.google.gwt.user.client.rpc.impl.AbstractSerializationStream;

/**
 * TODO: document me.
 *
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class ObjectGraphTest extends
		AndroidGWTTestCase<ObjectGraphTestServiceAsync> {

	private ObjectGraphTestServiceAsync objectGraphTestService;

	public ObjectGraphTest() throws InterruptedException {
		super(MainActivity.class);
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				ObjectGraphTest.this.objectGraphTestService = SyncProxy
						.create(ObjectGraphTestService.class);
				((ServiceDefTarget) ObjectGraphTest.this.objectGraphTestService)
						.setServiceEntryPoint(getModuleBaseURL()
								+ "objectgraphs");
				arg0[0].countDown();
				return null;
			}
		});
	}

	protected boolean expectedObfuscationState() {
		return false;
	}

	private ObjectGraphTestServiceAsync getServiceAsync() {

		return this.objectGraphTestService;
	}

	@Override
	protected void setUp() throws Exception {
		SyncProxy.suppressRelativePathWarning(true);
		super.setUp();
	}

	public void testAcyclicGraph() {
		final ObjectGraphTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_AcyclicGraph(TestSetFactory.createAcyclicGraph(),
						new AsyncCallback() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Object result) {
						assertNotNull(result);
						assertTrue(TestSetValidator
								.isValidAcyclicGraph((SerializableDoublyLinkedNode) result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testComplexCyclicGraph() {
		final ObjectGraphTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_ComplexCyclicGraph(
						TestSetFactory.createComplexCyclicGraph(),
						new AsyncCallback() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(Object result) {
								assertNotNull(result);
								assertTrue(TestSetValidator
										.isValidComplexCyclicGraph((SerializableDoublyLinkedNode) result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testComplexCyclicGraph2() {
		final ObjectGraphTestServiceAsync service = getServiceAsync();
		final SerializableDoublyLinkedNode node = TestSetFactory
				.createComplexCyclicGraph();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_ComplexCyclicGraph(node, node,
						new AsyncCallback() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Object result) {
						assertNotNull(result);
						assertTrue(TestSetValidator
								.isValidComplexCyclicGraph((SerializableDoublyLinkedNode) result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testComplexCyclicGraphWithCFS() {
		final ObjectGraphTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_ComplexCyclicGraphWithCFS(
						TestSetFactory.createComplexCyclicGraphWithCFS(),
						new AsyncCallback<SerializableGraphWithCFS>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(
									SerializableGraphWithCFS result) {
								assertNotNull(result);
								assertTrue(TestSetValidator
										.isValidComplexCyclicGraphWithCFS(result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	public void testDoublyReferencedArray() {
		final ObjectGraphTestServiceAsync service = getServiceAsync();
		final SerializableWithTwoArrays node = TestSetFactory
				.createDoublyReferencedArray();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_SerializableWithTwoArrays(node,
						new AsyncCallback() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Object result) {
						assertNotNull(result);
						assertTrue(TestSetValidator
								.isValid((SerializableWithTwoArrays) result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testElision() throws SerializationException {
		ObjectGraphTestServiceAsync async = getServiceAsync();

		SerializationStreamWriter writer = ((SerializationStreamFactory) async)
				.createStreamWriter();
		AbstractSerializationStream stream = (AbstractSerializationStream) writer;
		assertEquals(
				"Missing flag",
				expectedObfuscationState(),
				stream.hasFlags(AbstractSerializationStream.FLAG_ELIDE_TYPE_NAMES));

		SerializableDoublyLinkedNode node = new SerializableDoublyLinkedNode();
		writer.writeObject(node);
		String s = writer.toString();

		// Don't use class.getName() due to conflict with removal of type names
		assertEquals("Checking for SerializableDoublyLinkedNode",
				expectedObfuscationState(),
				!s.contains("SerializableDoublyLinkedNode"));
	}

	public void testPrivateNoArg() {
		final ObjectGraphTestServiceAsync service = getServiceAsync();
		final SerializablePrivateNoArg node = TestSetFactory
				.createPrivateNoArg();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_PrivateNoArg(node, new AsyncCallback() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Object result) {
						assertNotNull(result);
						assertTrue(TestSetValidator
								.isValid((SerializablePrivateNoArg) result));
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testTrivialCyclicGraph() {
		final ObjectGraphTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo_TrivialCyclicGraph(
						TestSetFactory.createTrivialCyclicGraph(),
						new AsyncCallback() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(Object result) {
								assertNotNull(result);
								assertTrue(TestSetValidator
										.isValidTrivialCyclicGraph((SerializableDoublyLinkedNode) result));
								finishTest();
							}
						});
				return null;
			}
		});
	}
}
