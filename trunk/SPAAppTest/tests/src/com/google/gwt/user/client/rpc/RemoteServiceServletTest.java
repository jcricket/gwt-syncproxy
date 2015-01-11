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
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

/**
 * This test case is used to check that the RemoteServiceServlet walks the class
 * hierarchy looking for the service interface. Prior to this test the servlet
 * would only look into the concrete class but not in any of its super classes.
 *
 * See <a href=
 * "http://code.google.com/p/google-web-toolkit/issues/detail?id=50&can=3&q="
 * >Bug 50</a> for more details.
 * <p>
 * This test works in conjunction with
 * {@link com.google.gwt.user.server.rpc.RemoteServiceServletTestServiceImpl}.
 * </p>
 *
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class RemoteServiceServletTest extends
		AndroidGWTTestCase<RemoteServiceServletTestServiceAsync> {
	private static class MyRpcRequestBuilder extends RpcRequestBuilder {
		private boolean doCreate;
		private boolean doFinish;
		private boolean doSetCallback;
		private boolean doSetContentType;
		private boolean doSetRequestData;
		private boolean doSetRequestId;

		public void check() {
			assertTrue("doCreate", this.doCreate);
			assertTrue("doFinish", this.doFinish);
			assertTrue("doSetCallback", this.doSetCallback);
			assertTrue("doSetContentType", this.doSetContentType);
			assertTrue("doSetRequestData", this.doSetRequestData);
			assertTrue("doSetRequestId", this.doSetRequestId);
		}

		@Override
		protected RequestBuilder doCreate(String serviceEntryPoint) {
			this.doCreate = true;
			return super.doCreate(serviceEntryPoint);
		}

		@Override
		protected void doFinish(RequestBuilder rb) {
			this.doFinish = true;
			super.doFinish(rb);
		}

		@Override
		protected void doSetCallback(RequestBuilder rb, RequestCallback callback) {
			this.doSetCallback = true;
			super.doSetCallback(rb, callback);
		}

		@Override
		protected void doSetContentType(RequestBuilder rb, String contentType) {
			this.doSetContentType = true;
			super.doSetContentType(rb, contentType);
		}

		@Override
		protected void doSetRequestData(RequestBuilder rb, String data) {
			this.doSetRequestData = true;
			super.doSetRequestData(rb, data);
		}

		@Override
		protected void doSetRequestId(RequestBuilder rb, int id) {
			this.doSetRequestId = true;
			super.doSetRequestId(rb, id);
		}
	}

	protected static RemoteServiceServletTestServiceAsync getAsyncService() {

		return servletTestService;
	}

	protected static RemoteServiceServletTestServiceAsync servletTestService;

	private Request req;

	public RemoteServiceServletTest() throws InterruptedException {
		super(MainActivity.class);
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				servletTestService = SyncProxy
						.create(RemoteServiceServletTestService.class);
				((ServiceDefTarget) servletTestService)
				.setServiceEntryPoint(getModuleBaseURL()
						+ "servlettest");
				arg0[0].countDown();
				return null;
			}
		});
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
	 * Modified by P.Prith to handle the {@link GWT#getModuleBaseURL()}
	 * dependency
	 */
	public void testAlternateStatusCode() {
		final RemoteServiceServletTestServiceAsync service = getAsyncService();
		((ServiceDefTarget) service).setServiceEntryPoint(getModuleBaseURL()
				+ "servlettest/404");

		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.test(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						if (caught instanceof StatusCodeException) {
							assertEquals(Response.SC_NOT_FOUND,
									((StatusCodeException) caught)
											.getStatusCode());
							assertEquals("Not Found",
									((StatusCodeException) caught)
											.getStatusText());
							finishTest();
						} else {
							TestSetValidator.rethrowException(caught);
						}
					}

					@Override
					public void onSuccess(Void result) {
						fail("Should not have succeeded");
					}
				});
				return null;
			}
		});
	}

	/**
	 * Verify behavior when the RPC method throws a RuntimeException declared on
	 * the RemoteService interface.
	 */
	public void testDeclaredRuntimeException() {
		final RemoteServiceServletTestServiceAsync service = getAsyncService();

		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.throwDeclaredRuntimeException(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						assertTrue(
								"Wrong exception type: " + caught.getClass(),
								caught instanceof NullPointerException);
						assertEquals("expected", caught.getMessage());
						finishTest();
					}

					@Override
					public void onSuccess(Void result) {
						fail();
					}
				});
				return null;
			}
		});
	}

	public void testManualSend() throws RequestException {
		fail("Request Builder Not Supported");
		final RemoteServiceServletTestServiceAsync service = getAsyncService();

		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				RequestBuilder builder = service
						.testExpectCustomHeader(new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(Void result) {
								assertTrue(!RemoteServiceServletTest.this.req
										.isPending());
								finishTest();
							}
						});
				builder.setHeader("X-Custom-Header", "true");
				try {
					RemoteServiceServletTest.this.req = builder.send();
				} catch (RequestException e) {
					throw new RuntimeException(e);
				}
				assertTrue(RemoteServiceServletTest.this.req.isPending());
				return null;
			}
		});
	}

	/**
	 * Modified by P.prith to remove dependence on GWT
	 */
	public void testPermutationStrongName() {
		// fail("IOException");
		final RemoteServiceServletTestServiceAsync service = getAsyncService();

		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				// assertNotNull(GWT.getPermutationStrongName());
				assertNotNull(((ServiceDefTarget) service)
						.getSerializationPolicyName());
				// service.testExpectPermutationStrongName(GWT.getPermutationStrongName(),
				service.testExpectPermutationStrongName(
						((ServiceDefTarget) service)
								.getSerializationPolicyName(),
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(Void result) {
								finishTest();
							}
						});
				return null;
			}
		});
	}

	/**
	 * Test that the policy strong name is available from browser-side Java
	 * code.
	 */
	public void testPolicyStrongName() {
		String policy = ((ServiceDefTarget) getAsyncService())
				.getSerializationPolicyName();
		assertNotNull(policy);
		assertTrue(policy.length() != 0);
	}

	/**
	 * Send request without the permutation strong name and expect a
	 * SecurityException. This tests
	 * RemoteServiceServlet#checkPermutationStrongName.
	 */
	public void testRequestWithoutStrongNameHeader() {
		final RemoteServiceServletTestServiceAsync service = getAsyncService();
		((ServiceDefTarget) service)
				.setRpcRequestBuilder(new RpcRequestBuilder() {
					/**
					 * Copied from base class.
					 */
					@Override
					protected void doFinish(RequestBuilder rb) {
						// Don't set permutation strong name
						rb.setHeader(MODULE_BASE_HEADER, GWT.getModuleBaseURL());
					}

				});

		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.test(new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						assertTrue(caught instanceof StatusCodeException);
						assertEquals(500,
								((StatusCodeException) caught).getStatusCode());
						finishTest();
					}

					@Override
					public void onSuccess(Void result) {
						fail();
					}
				});
				return null;
			}
		});
	}

	/**
	 * Ensure that each doFoo method is called.
	 */
	public void testRpcRequestBuilder() {
		final MyRpcRequestBuilder builder = new MyRpcRequestBuilder();
		final RemoteServiceServletTestServiceAsync service = getAsyncService();
		((ServiceDefTarget) service).setRpcRequestBuilder(builder);

		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.test(new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(Void result) {
						builder.check();
						finishTest();
					}
				});
				return null;
			}
		});
	}

	public void testServiceInterfaceLocation() {
		fail("Request no support");
		final RemoteServiceServletTestServiceAsync service = getAsyncService();

		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				RemoteServiceServletTest.this.req = service
						.test(new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(Void result) {
								assertTrue(!RemoteServiceServletTest.this.req
										.isPending());
								finishTest();
							}
						});
				assertTrue(RemoteServiceServletTest.this.req.isPending());
				return null;
			}
		});
	}

	/**
	 * Verify behavior when the RPC method throws an unknown RuntimeException
	 * (possibly one unknown to the client).
	 */
	public void testUnknownRuntimeException() {
		final RemoteServiceServletTestServiceAsync service = getAsyncService();

		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.throwUnknownRuntimeException(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						assertTrue(caught instanceof InvocationException);
						finishTest();
					}

					@Override
					public void onSuccess(Void result) {
						fail();
					}
				});
				return null;
			}
		});
	}
}
