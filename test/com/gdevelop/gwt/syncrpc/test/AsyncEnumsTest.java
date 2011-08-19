package com.gdevelop.gwt.syncrpc.test;

import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.EnumsTestService;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestService.Complex;
import com.google.gwt.user.client.rpc.EnumsTestService.Subclassing;
import com.google.gwt.user.client.rpc.EnumsTestServiceAsync;

import junit.framework.TestCase;

public class AsyncEnumsTest extends TestCase{
  private static EnumsTestServiceAsync service = 
    (EnumsTestServiceAsync)SyncProxy.newProxyInstance(
        EnumsTestServiceAsync.class, RPCSyncTestSuite.BASE_URL, 
        "enums");

  public AsyncEnumsTest() {
  }

  public void testBasicEnums() throws Throwable {
    final Throwable[] exceptions = new Throwable[1];
    service.echo(Basic.A, new AsyncCallback<Basic>(){
      public void onFailure(Throwable caught) {
        exceptions[0] = caught;
      }

      public void onSuccess(Basic result) {
        System.out.println("OK");
        assertEquals(Basic.A, result);
      }
    });
    
    Thread.sleep(2000);   // TODO: Wait for Invocations
    if (exceptions[0] != null){
      throw exceptions[0];
    }
  }
}
