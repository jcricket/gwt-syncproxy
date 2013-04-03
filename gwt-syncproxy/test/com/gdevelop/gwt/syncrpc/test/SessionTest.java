package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.LoginUtils;
import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.ValueTypesTestService;

import java.net.CookieManager;

import junit.framework.TestCase;


public class SessionTest extends TestCase{
  private static CookieManager cookieManager;
  static{
    try {
      cookieManager = LoginUtils.loginFormBasedJ2EE("https://localhost:8443/rpcsuite/", 
                                                "tomcat", "tomcat");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static ValueTypesTestService service = 
    (ValueTypesTestService)SyncProxy.newProxyInstance(
        ValueTypesTestService.class, "https://localhost:8443/rpcsuite/rpcsuite/", 
        cookieManager);

  public SessionTest() {
  }

  public void testBoolean_FALSE() {
    Object result = service.echo_FALSE(false);
    assertNotNull(result);
    assertFalse(((Boolean) result).booleanValue());
  }
}
