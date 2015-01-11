/**
 * Jan 10, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.test.poj;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;

import com.gdevelop.gwt.syncrpc.HasProxySettings;
import com.gdevelop.gwt.syncrpc.ProxySettings;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spawebtest.client.CookieService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.CookieServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcTestBase;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class CookieServiceTest extends RpcTestBase {

	protected CookieServiceAsync getAsyncService() {
		CookieManager cm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
		CookieServiceAsync cookieService = SyncProxy.createProxy(
				CookieServiceAsync.class,
				new ProxySettings().setCookieManager(cm));
		return cookieService;
	}

	/**
	 * Must refer to a valid module that sources this class.
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	protected CookieService getSyncService() {
		CookieManager cm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
		CookieService cookieService = SyncProxy.createProxy(
				CookieService.class, new ProxySettings().setCookieManager(cm));
		return cookieService;
	}

	private boolean searchCookieStoreForValue(String value, CookieStore store) {
		for (HttpCookie c : store.getCookies()) {
			if (c.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// setRpcTimeout(2);
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		CookieService cookieService = getSyncService();
		cookieService.invalidateAllSessions();
	}

	public void testEchoCookiesFromClient() {
		final CookieServiceAsync serviceAsync = getAsyncService();
		// Make sure Cookie Store is empty to begin with
		assertEquals("Cookie Store should be empty", 0,
				((HasProxySettings) serviceAsync).getCookieManager()
						.getCookieStore().getCookies().size());
		for (String val : CookieService.COOKIE_VALS) {
			String domain = URI.create(getModuleBaseURL()).getHost();
			HttpCookie cookie = new HttpCookie(val, val);
			((HasProxySettings) serviceAsync).getCookieManager()
					.getCookieStore()
			.add(URI.create("http://" + domain), cookie);
		}
		delayTestFinishForRpc();
		serviceAsync
		.echoCookiesFromClient(new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				assertEquals("Missing Cookies",
						CookieService.COOKIE_VALS.length, result.size());
				for (String val : CookieService.COOKIE_VALS) {
					assertTrue(
							"Missing Cookie Value in Store",
							searchCookieStoreForValue(val,
									((HasProxySettings) serviceAsync)
													.getCookieManager()
									.getCookieStore()));
				}
				for (String val : CookieService.COOKIE_VALS) {
					assertTrue("Missing Cookie Value Server Response",
							result.contains(val));
				}
				finishTest();
			}
		});
	}

	public void testEchoCookiesFromClientForDifferentURL() {
		CookieServiceAsync serviceAsync = getAsyncService();
		// Make sure Cookie Store is empty to begin with
		assertEquals("Cookie Store should be empty", 0,
				((HasProxySettings) serviceAsync).getCookieManager()
						.getCookieStore().getCookies().size());
		for (String val : CookieService.COOKIE_VALS) {
			((HasProxySettings) serviceAsync)
					.getCookieManager()
					.getCookieStore()
					.add(URI.create("http://another.url"),
							new HttpCookie(val, val));
		}
		delayTestFinishForRpc();
		serviceAsync
				.echoCookiesFromClient(new AsyncCallback<ArrayList<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException(caught);
					}

					@Override
					public void onSuccess(ArrayList<String> result) {
						assertEquals("Should be no cookies", 0, result.size());
						finishTest();
					}
				});
	}

	public void testGenerateCookiesOnServer() {
		final CookieServiceAsync serviceAsync = getAsyncService();
		delayTestFinishForRpc();
		// Make sure Cookie Store is empty to begin with
		assertEquals("Cookie Store should be empty", 0,
				((HasProxySettings) serviceAsync).getCookieManager()
						.getCookieStore().getCookies().size());
		serviceAsync.generateCookiesOnServer(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(Void result) {
				assertEquals("Missing Cookies",
						CookieService.COOKIE_VALS.length,
						((HasProxySettings) serviceAsync).getCookieManager()
								.getCookieStore().getCookies().size());
				for (String val : CookieService.COOKIE_VALS) {
					assertTrue(
							"Missing Cookie Value",
							searchCookieStoreForValue(val,
									((HasProxySettings) serviceAsync)
											.getCookieManager()
											.getCookieStore()));
				}
				finishTest();
			}
		});
	}

	public void testMultipleSessions() {
		final CookieService syncService1 = getSyncService();
		final CookieService syncService2 = getSyncService();

		final String test = "TEST";
		final String test2 = "TEST2";

		syncService1.setSessionAttrib(test);
		assertEquals("Wrong session 1 attribute", test,
				syncService1.getSessionAttrib());
		syncService2.setSessionAttrib(test2);
		assertEquals("Wrong session 1 attribute", test,
				syncService1.getSessionAttrib());
		assertEquals("Wrong session 2 attribute", test2,
				syncService2.getSessionAttrib());
	}

	public void testOnTheFlyCookieManagerChange() {
		CookieService serviceSync = getSyncService();
		// Make sure Cookie Store is empty to begin with
		assertEquals("Cookie Store should be empty", 0,
				((HasProxySettings) serviceSync).getCookieManager()
						.getCookieStore().getCookies().size());
		for (String val : CookieService.COOKIE_VALS) {
			String domain = URI.create(getModuleBaseURL()).getHost();
			HttpCookie cookie = new HttpCookie(val, val);
			((HasProxySettings) serviceSync).getCookieManager()
			.getCookieStore()
			.add(URI.create("http://" + domain), cookie);
		}
		ArrayList<String> result = serviceSync.echoCookiesFromClient();
		assertEquals("Missing Cookies", CookieService.COOKIE_VALS.length,
				result.size());
		for (String val : CookieService.COOKIE_VALS) {
			assertTrue(
					"Missing Cookie Value in Store",
					searchCookieStoreForValue(val,
							((HasProxySettings) serviceSync).getCookieManager()
							.getCookieStore()));
		}
		for (String val : CookieService.COOKIE_VALS) {
			assertTrue("Missing Cookie Value Server Response",
					result.contains(val));
		}
		// Change cookiemanager with new cookies and check again
		String postFix = "-T2";
		CookieManager cm2 = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
		for (String val : CookieService.COOKIE_VALS) {
			String domain = URI.create(getModuleBaseURL()).getHost();
			HttpCookie cookie = new HttpCookie(val + postFix, val + postFix);
			cm2.getCookieStore().add(URI.create("http://" + domain), cookie);
		}
		((HasProxySettings) serviceSync).setCookieManager(cm2);
		result = serviceSync.echoCookiesFromClient();
		assertEquals("Missing Cookies", CookieService.COOKIE_VALS.length,
				result.size());
		for (String val : CookieService.COOKIE_VALS) {
			assertTrue(
					"Missing Cookie Value in Store",
					searchCookieStoreForValue(val + postFix,
							((HasProxySettings) serviceSync).getCookieManager()
							.getCookieStore()));
		}
		for (String val : CookieService.COOKIE_VALS) {
			assertTrue("Missing Cookie Value Server Response",
					result.contains(val + postFix));
		}

	}

	public void testSessionGood() {
		final CookieServiceAsync serviceAsync = getAsyncService();
		final String test = "TEST";
		delayTestFinishForRpc();
		serviceAsync.setSessionAttrib(test, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(Void result) {
				serviceAsync.getSessionAttrib(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException(caught);
					}

					@Override
					public void onSuccess(String result) {
						assertEquals("Wrong session attribute value", test,
								result);
						finishTest();
					}
				});
			}
		});
	}

	public void testVerifyServerGeneratedIsEchoed() {
		final CookieServiceAsync serviceAsync = getAsyncService();
		// Make sure Cookie Store is empty to begin with
		assertEquals("Cookie Store should be empty", 0,
				((HasProxySettings) serviceAsync).getCookieManager()
						.getCookieStore().getCookies().size());
		delayTestFinishForRpc();
		serviceAsync.generateCookiesOnServer(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(Void result) {
				// Make sure Cookie Store is empty to begin with
				assertEquals("Cookie Store should not be empty",
						CookieService.COOKIE_VALS.length,
						((HasProxySettings) serviceAsync).getCookieManager()
								.getCookieStore().getCookies().size());
				serviceAsync
				.echoCookiesFromClient(new AsyncCallback<ArrayList<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException(caught);
					}

					@Override
					public void onSuccess(ArrayList<String> result2) {
						assertEquals("Missing Cookies",
								CookieService.COOKIE_VALS.length,
								result2.size());
						for (String val : CookieService.COOKIE_VALS) {
							assertTrue(
									"Missing Cookie Value in Store",
									searchCookieStoreForValue(
													val,
											((HasProxySettings) serviceAsync)
															.getCookieManager()
											.getCookieStore()));
						}
						for (String val : CookieService.COOKIE_VALS) {
							assertTrue(
									"Missing Cookie Value Server Response",
									result2.contains(val));
						}
						finishTest();
					}
				});

			}
		});
	}

	public void testVerifyServerGeneratedIsNotEchoed() {
		final CookieServiceAsync serviceAsync = getAsyncService();
		// Make sure Cookie Store is empty to begin with
		assertEquals("Cookie Store should be empty", 0,
				((HasProxySettings) serviceAsync).getCookieManager()
						.getCookieStore().getCookies().size());
		delayTestFinishForRpc();
		serviceAsync.generateCookiesOnServer(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(Void result) {
				CookieServiceAsync serviceAsync2 = getAsyncService();
				// Make sure Cookie Store is empty to begin with
				assertEquals("Cookie Store should be empty", 0,
						((HasProxySettings) serviceAsync2).getCookieManager()
								.getCookieStore().getCookies().size());
				serviceAsync2
				.echoCookiesFromClient(new AsyncCallback<ArrayList<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException(caught);
					}

					@Override
					public void onSuccess(ArrayList<String> result) {
						assertEquals("Should have no Cookies", 0,
								result.size());

								finishTest();
					}
				});
				finishTest();
			}
		});
	}
}
