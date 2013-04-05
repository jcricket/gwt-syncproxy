package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.TestSetValidator;
import com.google.gwt.user.client.rpc.XsrfTestService;
import com.google.gwt.user.client.rpc.XsrfTestServiceAsync;

public class XsrfProtectionTest extends GWTTestCase {
	private static XsrfTestServiceAsync service;

	public XsrfProtectionTest() {
	}

	private void checkServerState(String drink, final boolean stateShouldChange) {
		service.checkIfDrankDrink(drink, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(Boolean result) {
				assertTrue(stateShouldChange == result);
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

	@Override
	protected void gwtSetUp() {
		service = GWT.create(XsrfTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/xsrftestservice");
	}

	public void testRpcWithoutXsrfTokenFails() throws Exception {
		service.drink("kumys", new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				RpcTokenException e = (RpcTokenException) caught;
				assertTrue(e.getMessage().contains("XSRF token missing"));
				checkServerState("kumys", false);
				finishTest();
			}

			@Override
			public void onSuccess(Void result) {
				fail("Should've failed without XSRF token");
			}

		});
		delayTestFinish(2000);
	}

	public void xtestRpcWithBadXsrfTokenFails() throws Exception {
		assertTrue("Not Yet Implemented", false);
	}

	public void xtestRpcWithXsrfToken() throws Exception {
		assertTrue("Not Yet Implemented", false);
	}

	public void xtestXsrfTokenService() throws Exception {
		assertTrue("Not Yet Implemented", false);
	}

	public void xtestXsrfTokenWithDifferentSessionCookieFails()
			throws Exception {
		assertTrue("Not Yet Implemented", false);
	}
}
