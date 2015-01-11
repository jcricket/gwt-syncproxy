/**
 * Jan 1, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.google.gwt.user.client.rpc;

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.AndroidGWTTestCase;

//import com.google.gwt.user.client.Cookies;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class XsrfProtectionTest extends
		AndroidGWTTestCase<XsrfTestServiceAsync> {

	private XsrfTestServiceAsync xsrfTestService;

	public static final String SESSION_COOKIE_NAME = "MYSESSIONCOOKIE";

	XsrfTokenServiceAsync xsrfTokenService;

	public XsrfProtectionTest() throws InterruptedException {
		super(MainActivity.class);
		// TODO XSRF Not Yet Supported
		// setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
		// @Override
		// protected Void doInBackground(CountDownLatch... arg0) {
		// XsrfProtectionTest.this.xsrfTestService = SyncProxy
		// .create(XsrfTestService.class);
		// ((ServiceDefTarget) XsrfProtectionTest.this.xsrfTestService)
		// .setServiceEntryPoint(getModuleBaseURL()
		// + "xsrftestservice");
		// XsrfProtectionTest.this.xsrfTokenService = SyncProxy
		// .create(XsrfTokenService.class);
		// ((ServiceDefTarget) XsrfProtectionTest.this.xsrfTokenService)
		// .setServiceEntryPoint(getModuleBaseURL() + "xsrfmock");
		// arg0[0].countDown();
		// return null;
		// }
		// });
	}

	private void checkServerState(final String drink,
			final boolean stateShouldChange) {
		final XsrfTestServiceAsync service = getAsyncService();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
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
				return null;
			}

		});
	}

	protected XsrfTestServiceAsync getAsyncService() {
		return this.xsrfTestService;
	}

	protected XsrfTokenServiceAsync getAsyncXsrfService() {
		return this.xsrfTokenService;
	}

	@Override
	protected void setUp() throws Exception {
		SyncProxy.suppressRelativePathWarning(true);
		super.setUp();
		// MD5 test vector
		// Cookies.setCookie(SESSION_COOKIE_NAME, "abc");
	}

	@Override
	protected void tearDown() throws Exception {
		// Cookies.removeCookie(SESSION_COOKIE_NAME);
		super.tearDown();
	}

	public void testRpcWithBadXsrfTokenFails() throws Exception {
		fail("XSRF Not Yet Supported");
		XsrfToken badToken = new XsrfToken("Invalid Token");
		final XsrfTestServiceAsync service = getAsyncService();
		((HasRpcToken) service).setRpcToken(badToken);
		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				service.drink("maksym", new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						checkServerState("maksym", false);
					}

					@Override
					public void onSuccess(Void result) {
						fail("Should've failed with bad XSRF token");
					}
				});
				return null;
			}

		});
	}

	public void testRpcWithoutXsrfTokenFails() throws Exception {
		fail("XSRF Not Yet Supported");
		final XsrfTestServiceAsync service = getAsyncService();

		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				service.drink("kumys", new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						RpcTokenException e = (RpcTokenException) caught;
						assertTrue(e.getMessage()
								.contains("XSRF token missing"));
						checkServerState("kumys", false);
					}

					@Override
					public void onSuccess(Void result) {
						fail("Should've failed without XSRF token");
					}
				});
				return null;
			}

		});
	}

	public void testRpcWithXsrfToken() throws Exception {
		fail("XSRF Not Yet Supported");
		final XsrfTokenServiceAsync xsrfService = getAsyncXsrfService();

		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				xsrfService.getNewXsrfToken(new AsyncCallback<XsrfToken>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(XsrfToken result) {
						XsrfTestServiceAsync service = getAsyncService();

						((HasRpcToken) service).setRpcToken(result);
						service.drink("airan", new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(Void result) {
								checkServerState("airan", true);
							}
						});
					}
				});
				return null;
			}

		});
	}

	public void testXsrfTokenService() throws Exception {
		fail("XSRF Not Yet Supported");
		final XsrfTokenServiceAsync xsrfService = getAsyncXsrfService();

		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				xsrfService.getNewXsrfToken(new AsyncCallback<XsrfToken>() {
					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(XsrfToken result) {
						assertNotNull(result);
						assertNotNull(result.getToken());
						// MD5("abc")
						assertEquals("900150983CD24FB0D6963F7D28E17F72",
								result.getToken());
						finishTest();
					}
				});
				return null;
			}

		});
	}

	public void testXsrfTokenWithDifferentSessionCookieFails() throws Exception {
		fail("XSRF Not Yet Supported");
		final XsrfTokenServiceAsync xsrfService = getAsyncXsrfService();

		final XsrfTestServiceAsync service = getAsyncService();

		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {

				xsrfService.getNewXsrfToken(new AsyncCallback<XsrfToken>() {

					@Override
					public void onFailure(Throwable caught) {
						TestSetValidator.rethrowException(caught);
					}

					@Override
					public void onSuccess(XsrfToken result) {
						// Ensure it's MD5
						assertEquals(32, result.getToken().length());

						((HasRpcToken) service).setRpcToken(result);

						// change cookie to ensure verification fails since
						// XSRF token was derived from previous cookie value
						// Cookies.setCookie(SESSION_COOKIE_NAME,
						// "sometingrandom");

						service.drink("bozo", new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								RpcTokenException e = (RpcTokenException) caught;
								assertTrue(e.getMessage().contains(
										"Invalid XSRF token"));
								checkServerState("bozo", false);
							}

							@Override
							public void onSuccess(Void result) {
								fail("Should've failed since session cookie has changed");
							}
						});
					}
				});

				return null;
			}
		});
	}
}
