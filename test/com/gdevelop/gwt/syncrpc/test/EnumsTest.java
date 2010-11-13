package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.EnumsTestService;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestService.Complex;
import com.google.gwt.user.client.rpc.EnumsTestService.Subclassing;

import junit.framework.TestCase;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class EnumsTest extends TestCase{
  private static EnumsTestService service = 
    (EnumsTestService)SyncProxy.newProxyInstance(
        EnumsTestService.class, RPCSyncTestSuite.BASE_URL, 
        "enums");
  
  public EnumsTest() {
  }
  
  public void testBasicEnums() {
    Basic result = service.echo(Basic.A);
    assertEquals(Basic.A, result);
  }
  
  public void testComplexEnums() {
    try{
      Complex a = Complex.A;
      a.value = "client";
      
      Complex result = service.echo(Complex.A);
      assertEquals(Complex.A, result);
      assertEquals("client", result.value);
    }catch(Throwable e){
      fail(e.getMessage());
    }
  }
  
  public void testNull() {
    Basic result = service.echo((Basic) null);
    assertNull(result);
  }
  
  public void testSubclassingEnums() {
    Subclassing result = service.echo(Subclassing.A);
    assertEquals(Subclassing.A, result);
  }
}
