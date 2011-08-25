package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.ValueTypesTestService;

import junit.framework.TestCase;


public class HttpsTest extends TestCase{
  private static ValueTypesTestService service = 
    (ValueTypesTestService)SyncProxy.newProxyInstance(
        ValueTypesTestService.class, "https://localhost:8443/rpcsuite/rpcsuite/");

  public HttpsTest() {
  }

  public void testBoolean_FALSE() {
    Object result = service.echo_FALSE(false);
    assertNotNull(result);
    assertFalse(((Boolean) result).booleanValue());
  }
}
