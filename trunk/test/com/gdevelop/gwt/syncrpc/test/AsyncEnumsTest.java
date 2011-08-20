package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestServiceAsync;

import junit.framework.TestCase;

public class AsyncEnumsTest extends TestCase{
  private static EnumsTestServiceAsync service = 
    (EnumsTestServiceAsync)SyncProxy.newProxyInstance(
        EnumsTestServiceAsync.class, RPCSyncTestSuite.BASE_URL, true);

  public AsyncEnumsTest() {
  }

  public void testBasicEnums() throws Throwable {
    final Throwable[] exceptions = new Throwable[1];
    service.echo(Basic.A, new AsyncCallback<Basic>(){
      public void onFailure(Throwable caught) {
        fail(caught.getMessage());
        exceptions[0] = caught;
      }

      public void onSuccess(Basic result) {
        System.out.println("OK");
        assertEquals(Basic.A, result);
      }
    });
    
    if (exceptions[0] != null){
      throw exceptions[0];
    }
  }
}
