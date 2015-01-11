package com.gdevelop.gwt.syncrpc.test.poj;

import java.net.CookieManager;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.LoginUtils;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.ValueTypesTestService;

public class SessionTest extends TestCase {
	private static CookieManager cookieManager;
	static {
		try {
			cookieManager = LoginUtils.loginFormBasedJ2EE(
					"https://localhost:8443/rpcsuite/", "tomcat", "tomcat");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static ValueTypesTestService service = SyncProxy.newProxyInstance(
			ValueTypesTestService.class,
			"https://localhost:8443/rpcsuite/rpcsuite/", cookieManager);

	public SessionTest() {
	}

	public void testBoolean_FALSE() {
		Object result = service.echo_FALSE(false);
		assertNotNull(result);
		assertFalse(((Boolean) result).booleanValue());
	}
}
