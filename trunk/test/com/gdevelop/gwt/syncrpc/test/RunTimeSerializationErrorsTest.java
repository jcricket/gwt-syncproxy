package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.MixedSerializable;
import com.google.gwt.user.client.rpc.MixedSerializableEchoService;

import junit.framework.TestCase;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class RunTimeSerializationErrorsTest extends TestCase{
  private static MixedSerializableEchoService service = 
    (MixedSerializableEchoService)SyncProxy.newProxyInstance(
        MixedSerializableEchoService.class, RPCSyncTestSuite.BASE_URL, 
        "echo");
  
  public RunTimeSerializationErrorsTest() {
  }
  
  public void testBadSerialization1() {
    try{
      service.echoVoid(new MixedSerializable.NonSerializableSub());
    }catch(Exception e){
      return; // OK
    }
    fail("RPC request should have failed");
  }
  
  public void testBadSerialization2() {
    // empty
  }
  
  public void testBadSerialization3() throws RequestException {
    // empty
  }
  
  public void testGoodSerialization1() {
    service.echoVoid(new MixedSerializable.SerializableSub());
  }
}
