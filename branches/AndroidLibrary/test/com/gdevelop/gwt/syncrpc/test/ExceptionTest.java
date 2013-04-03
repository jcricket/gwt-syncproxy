package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.rpc.ExceptionsTestService;

import com.google.gwt.user.client.rpc.ExceptionsTestService.ExceptionsTestServiceException;
import com.google.gwt.user.client.rpc.TestSetFactory;

import com.google.gwt.user.client.rpc.TestSetValidator;

import junit.framework.TestCase;

public class ExceptionTest extends TestCase{
  private static ExceptionsTestService service = 
    (ExceptionsTestService)SyncProxy.newProxyInstance(
        ExceptionsTestService.class, RPCSyncTestSuite.BASE_URL, 
        "exceptions");

  public ExceptionTest() {
  }
  
  public void testException() throws ExceptionsTestServiceException {
    final UmbrellaException expected = TestSetFactory.createUmbrellaException();
    UmbrellaException result = service.echo(expected);
    assertNotNull(result);
    assertTrue(TestSetValidator.isValid(expected, result));
  }
}
