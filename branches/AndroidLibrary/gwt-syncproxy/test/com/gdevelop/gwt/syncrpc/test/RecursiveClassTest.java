package com.gdevelop.gwt.syncrpc.test;

import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.RecursiveClassTestService;
import com.google.gwt.user.client.rpc.RecursiveClassTestService.ResultNode;
import com.google.gwt.user.client.rpc.TestSetValidator;

import junit.framework.TestCase;

public class RecursiveClassTest extends TestCase{
  private static RecursiveClassTestService service = 
    (RecursiveClassTestService)SyncProxy.newProxyInstance(
        RecursiveClassTestService.class, RPCSyncTestSuite.BASE_URL, 
        "recursiveclass");

  public RecursiveClassTest() {
  }
  
  public void testRecursiveClass() {
    // TODO
//    ResultNode<ResultNode> result = service.greetServer("Hello");
//    assertNotNull(result);
//    assertTrue(TestSetValidator.isValidRecurisveClassObject(result));
  }
}
