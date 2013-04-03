package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.CoreJavaTestService;
import com.google.gwt.user.client.rpc.CoreJavaTestService.CoreJavaTestServiceException;

import java.math.MathContext;
import java.math.RoundingMode;

import junit.framework.TestCase;


public class CoreJavaTest extends TestCase{
  private static CoreJavaTestService service = 
    (CoreJavaTestService)SyncProxy.newProxyInstance(
        CoreJavaTestService.class, RPCSyncTestSuite.BASE_URL, 
        "corejava");

  private static MathContext createMathContext() {
    return new MathContext(5, RoundingMode.CEILING);
  }

  public static boolean isValid(MathContext value) {
    return createMathContext().equals(value);
  }

  public CoreJavaTest() {
  }

  public void testMathContext() throws CoreJavaTestServiceException {
    final MathContext expected = createMathContext();

    MathContext result = service.echoMathContext(expected);
    assertNotNull(result);
    assertTrue(isValid(result));
  }
}
