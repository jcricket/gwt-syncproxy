package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.TestSetValidator;
import com.google.gwt.user.client.rpc.XsrfTestServiceAsync;

public class XsrfProtectionTest extends TestCase {
	private static XsrfTestServiceAsync service = (XsrfTestServiceAsync) SyncProxy
			.newProxyInstance(XsrfTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "xsrftestservice",
					true);

	public XsrfProtectionTest() {
	}

	private void checkServerState(String drink, final boolean stateShouldChange)
			throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		service.checkIfDrankDrink(drink, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(Boolean result) {
				assertTrue(stateShouldChange == result);
				signal.countDown();
			}

		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	@Override
	protected void setUp() {

	}

	public void testRpcWithoutXsrfTokenFails() throws Exception {
		final CountDownLatch signal = new CountDownLatch(1);
		service.drink("kumys", new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				RpcTokenException e = (RpcTokenException) caught;
				assertTrue(e.getMessage().contains("XSRF token missing"));
				try {
					checkServerState("kumys", false);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				signal.countDown();
			}

			@Override
			public void onSuccess(Void result) {
				fail("Should've failed without XSRF token");
			}

		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
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
