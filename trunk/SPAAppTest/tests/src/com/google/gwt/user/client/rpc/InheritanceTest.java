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

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.AndroidGWTTestCase;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.AnonymousClassInterface;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.Circle;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableClass;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableClassWithTransientField;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableSubclass;

/**
 * Tests RPC serialization of classes without custom serializers.
 *
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class InheritanceTest extends
AndroidGWTTestCase<InheritanceTestServiceAsync> {

	private InheritanceTestServiceAsync inheritanceTestService;

	public InheritanceTest() throws InterruptedException {
		super(MainActivity.class);
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				InheritanceTest.this.inheritanceTestService = SyncProxy
						.create(InheritanceTestService.class);
				((ServiceDefTarget) InheritanceTest.this.inheritanceTestService)
				.setServiceEntryPoint(getModuleBaseURL()
						+ "inheritance");
				arg0[0].countDown();
				return null;
			}
		});
	}

	private InheritanceTestServiceAsync getServiceAsync() {

		return this.inheritanceTestService;
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		SyncProxy.suppressRelativePathWarning(true);
		super.setUp();
	}

	/**
	 * Test that anonymous classes are not serializable.
	 */
	public void testAnonymousClasses() {
		final InheritanceTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(new AnonymousClassInterface() {
					@Override
					public void foo() {
						// purposely empty
					}
				}, new AsyncCallback<Object>() {
					@Override
					public void onFailure(Throwable caught) {
						finishTest();
					}

					@Override
					public void onSuccess(Object result) {
						fail("Anonymous inner classes should not be serializable");
					}
				});
				return null;
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
		final InheritanceTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(InheritanceTestSetFactory.createCircle(),
						new AsyncCallback<Object>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Object result) {
						Circle circle = (Circle) result;
						assertNotNull(circle.getName());
						finishTest();
					}
				});
				return null;
			}
		});
	}

	/**
	 * Tests that transient fields do not prevent serializability.
	 */
	public void testJavaSerializableClass() {
		final InheritanceTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						new InheritanceTestSetFactory.JavaSerializableClass(3),
						new AsyncCallback<Object>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(Object result) {
								assertNotNull(result);
								finishTest();
							}
						});
				return null;
			}
		});
	}

	/**
	 * Test that non-static inner classes are not serializable.
	 */
	public void testNonStaticInnerClass() {
		final InheritanceTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						InheritanceTestSetFactory.createNonStaticInnerClass(),
						new AsyncCallback<Object>() {
							@Override
							public void onFailure(Throwable caught) {
								finishTest();
							}

							@Override
							public void onSuccess(Object result) {
								fail("Non-static inner classes should not be serializable");
							}
						});
				return null;
			}
		});
	}

	/**
	 * Tests that a serialized type can be sent again on the wire.
	 */
	public void testResendJavaSerializableClass() {
		final InheritanceTestServiceAsync service = getServiceAsync();
		final InheritanceTestSetFactory.JavaSerializableClass first = new InheritanceTestSetFactory.JavaSerializableClass(
				3);
		final AsyncCallback<Object> resendCallback = new AsyncCallback<Object>() {
			private boolean resend = true;

			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(Object result) {
				assertEquals(first, result);
				if (this.resend) {
					this.resend = false;
					service.echo(
							(InheritanceTestSetFactory.JavaSerializableClass) result,
							this);
				} else {
					finishTest();
				}
			}
		};
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(first, resendCallback);
				return null;
			}
		});
	}

	public void testReturnOfUnserializableClassFromServer() {
		final InheritanceTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.getUnserializableClass(new AsyncCallback<Object>() {
					@Override
					public void onFailure(Throwable caught) {
						finishTest();
					}

					@Override
					public void onSuccess(Object result) {
						fail("Returning an unserializable class from the server should fail");
					}
				});
				return null;
			}
		});
	}

	/**
	 * Test that a valid serializable class can be serialized.
	 */
	public void testSerializableClass() {
		final InheritanceTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						InheritanceTestSetFactory.createSerializableClass(),
						new AsyncCallback<Object>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(Object result) {
								assertNotNull(result);
								assertTrue(InheritanceTestSetValidator
										.isValid((SerializableClass) result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	/**
	 * Test that IsSerializable is inherited, also test static inner classes.
	 */
	public void testSerializableSubclass() {
		final InheritanceTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(
						InheritanceTestSetFactory.createSerializableSubclass(),
						new AsyncCallback<Object>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(Object result) {
								assertNotNull(result);
								assertTrue(InheritanceTestSetValidator
										.isValid((SerializableSubclass) result));
								finishTest();
							}
						});
				return null;
			}
		});
	}

	/**
	 * Modified by P.Prith to remove dependency on GWT Timer object
	 */
	public void testSerializationExceptionPreventsCall() {
		delayTestFinishForRpc();
		// final boolean serializationExceptionCaught[] = new boolean[1];
		// new Timer() {
		// @Override
		// public void run() {
		// assertTrue("serializationExceptionCaught was not true",
		// serializationExceptionCaught[0]);
		// finishTest();
		// }
		// }.schedule(RPC_TIMEOUT / 2);

		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				InheritanceTestServiceAsync service = getServiceAsync();
				service.echo(new AnonymousClassInterface() {
					@Override
					public void foo() {
						// purposely empty
					}
				}, new AsyncCallback<Object>() {
					@Override
					public void onFailure(Throwable caught) {
						assertTrue(
								"onFailure: got something other than a SerializationException ("
										+ caught.getClass().getName() + ")",
										caught instanceof SerializationException);
						finishTest();
					}

					@Override
					public void onSuccess(Object result) {
						fail("onSuccess: call should not have succeeded");
					}
				});
				return null;
			}
		});
	}

	/**
	 * Tests that transient fields do not prevent serializability.
	 */
	public void testTransientFieldExclusion() {
		final InheritanceTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echo(InheritanceTestSetFactory
						.createSerializableClassWithTransientField(),
						new AsyncCallback<Object>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Object result) {
						assertNotNull(result);
						assertTrue(InheritanceTestSetValidator
								.isValid((SerializableClassWithTransientField) result));
						finishTest();
					}
				});
				return null;
			}
		});
	}
}
