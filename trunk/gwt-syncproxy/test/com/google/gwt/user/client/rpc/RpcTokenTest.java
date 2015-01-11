/*
 * Copyright 2010 Google Inc.
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

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.core.client.GWT;

/**
 * Tests RpcToken functionality.
 *
 * Modified by P.Prith in 0.5 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class RpcTokenTest extends RpcTestBase {

	/**
	 * Second RpcToken implementation.
	 */
	public static class AnotherTestRpcToken implements RpcToken {
		int token;
	}

	/**
	 * First RpcToken implementation.
	 */
	public static class TestRpcToken implements RpcToken {
		String tokenValue;
	}

	protected AnnotatedRpcTokenTestServiceAsync getAnnotatedAsyncService() {
		AnnotatedRpcTokenTestServiceAsync service = SyncProxy
				.create(AnnotatedRpcTokenTestService.class);
		((ServiceDefTarget) service).setServiceEntryPoint(getModuleBaseURL()
				+ "rpctokentest-annotation");
		return service;
	}

	protected RpcTokenTestServiceAsync getAsyncService() {
		RpcTokenTestServiceAsync service = SyncProxy
				.create(RpcTokenTestService.class);
		((ServiceDefTarget) service).setServiceEntryPoint(getModuleBaseURL()
				+ "rpctokentest");
		return service;
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		SyncProxy.suppressRelativePathWarning(true);
	}

	public void testRpcTokenAnnotation() {
		AnnotatedRpcTokenTestServiceAsync service = getAnnotatedAsyncService();

		final AnotherTestRpcToken token = new AnotherTestRpcToken();
		token.token = 1337;
		((HasRpcToken) service).setRpcToken(token);

		service.getRpcTokenFromRequest(new AsyncCallback<RpcToken>() {

			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(RpcToken result) {
				assertNotNull(result);
				assertTrue(result instanceof AnotherTestRpcToken);
				assertEquals(token.token, ((AnotherTestRpcToken) result).token);
				finishTest();
			}
		});
	}

	public void testRpcTokenAnnotationDifferentFromActualType() {
		AnnotatedRpcTokenTestServiceAsync service = getAnnotatedAsyncService();

		// service is annotated to use AnotherTestRpcToken and not TestRpcToken,
		// generated proxy should catch this error
		final TestRpcToken token = new TestRpcToken();
		token.tokenValue = "Drink kumys!";
		try {
			((HasRpcToken) service).setRpcToken(token);
			fail("Should have thrown an RpcTokenException");
		} catch (RpcTokenException e) {
			// Expected
		}
	}

	/**
	 * Modified by P.Prith to remove dependence on
	 * {@link GWT#getModuleBaseURL()}
	 */
	public void testRpcTokenExceptionHandler() {
		RpcTokenTestServiceAsync service = getAsyncService();
		((ServiceDefTarget) service)
				.setServiceEntryPoint("http://127.0.0.1:8888/spawebtest/"
						+ "rpctokentest?throw=true");
		((HasRpcToken) service)
		.setRpcTokenExceptionHandler(new RpcTokenExceptionHandler() {
			@Override
			public void onRpcTokenException(RpcTokenException exception) {
				assertNotNull(exception);
				finishTest();
			}
		});

		delayTestFinishForRpc();

		service.getRpcTokenFromRequest(new AsyncCallback<RpcToken>() {

			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(RpcToken rpcToken) {
				fail("Should've called RpcTokenExceptionHandler");
			}
		});
	}

	public void testRpcTokenMissing() {
		RpcTokenTestServiceAsync service = getAsyncService();

		delayTestFinishForRpc();

		service.getRpcTokenFromRequest(new AsyncCallback<RpcToken>() {

			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(RpcToken token) {
				assertNull(token);
				finishTest();
			}
		});
	}

	public void testRpcTokenPresent() {
		RpcTokenTestServiceAsync service = getAsyncService();

		final TestRpcToken token = new TestRpcToken();
		token.tokenValue = "Drink kumys!";
		((HasRpcToken) service).setRpcToken(token);

		delayTestFinishForRpc();

		service.getRpcTokenFromRequest(new AsyncCallback<RpcToken>() {

			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(RpcToken rpcToken) {
				assertNotNull(rpcToken);
				assertTrue(rpcToken instanceof TestRpcToken);
				assertEquals(token.tokenValue,
						((TestRpcToken) rpcToken).tokenValue);
				finishTest();
			}
		});
	}
}
