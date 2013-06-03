package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.test.client.LargePayloadService;
import com.gdevelop.gwt.syncrpc.test.client.UserInfo;

import java.util.List;

import junit.framework.TestCase;


public class LargePayloadTest extends TestCase{
  private static LargePayloadService service = 
    (LargePayloadService)SyncProxy.newProxyInstance(
        LargePayloadService.class, RPCSyncTestSuite.BASE_URL);

  public LargePayloadTest() {
  }

  public void xx_testLargeResponsePayload() {
    List<UserInfo> userList = service.testLargeResponsePayload();
    System.out.println(userList);
  }

  public void testLargeResponseArray() {
    int[] array = service.testLargeResponseArray();
    System.out.println(array);
  }
}
