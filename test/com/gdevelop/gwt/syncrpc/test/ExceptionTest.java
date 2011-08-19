package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.ExceptionTestService;

import junit.framework.TestCase;

public class ExceptionTest extends TestCase{
  private static ExceptionTestService service = 
    (ExceptionTestService)SyncProxy.newProxyInstance(
        ExceptionTestService.class, RPCSyncTestSuite.BASE_URL, 
        "exception");

  public ExceptionTest() {
  }
  
  public void testException() {
    System.out.println(RPCSyncTestSuite.BASE_URL);
    
//    service.doSomething01();
//    
//    try {
//      service.doSomething02();
//      fail("Must have an exception");
//    } catch (Exception e) {
//      // e.printStackTrace();
//      assertTrue(ExceptionTestService.errorMessage.equals(e.getMessage()));
//    }
    
    Integer ret = service.doSomething03(1, "A", "B");
    assertTrue(ret == 2);
  }
}
