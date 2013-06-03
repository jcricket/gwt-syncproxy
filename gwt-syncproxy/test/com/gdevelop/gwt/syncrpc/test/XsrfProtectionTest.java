package com.gdevelop.gwt.syncrpc.test;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.TestSetValidator;
import com.google.gwt.user.client.rpc.XsrfTestService;

public class XsrfProtectionTest extends TestCase {
	private static XsrfTestService service = (XsrfTestService) SyncProxy
			.newProxyInstance(XsrfTestService.class, RPCSyncTestSuite.BASE_URL,
					"xsrftestservice");

	public XsrfProtectionTest() {
	}

	public void testRpcWithoutXsrfTokenFails() throws Exception {
		try {
			service.drink("kumys");
			fail("Should've failed without XSRF token");
		} catch (Exception caught) {
			RpcTokenException e = (RpcTokenException) caught;
			assertTrue(e.getMessage().contains("XSRF token missing"));
			checkServerState("kumys", false);
		}
	}

	public void testRpcWithBadXsrfTokenFails() throws Exception {
		// TODO
	}

	public void testXsrfTokenService() throws Exception {
		// TODO
	}

	public void testRpcWithXsrfToken() throws Exception {
		// TODO
	}

	public void testXsrfTokenWithDifferentSessionCookieFails() throws Exception {
		// TODO
	}

	private void checkServerState(String drink, final boolean stateShouldChange) {
		try {
			Boolean result = service.checkIfDrankDrink(drink);
			assertTrue(stateShouldChange == result);
		} catch (Exception caught) {
			TestSetValidator.rethrowException(caught);
		}
	}
}
