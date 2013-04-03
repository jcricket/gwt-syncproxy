package com.gdevelop.gwt.syncrpc.test.server;

import com.gdevelop.gwt.syncrpc.test.client.LargePayloadService;
import com.gdevelop.gwt.syncrpc.test.client.UserInfo;

import com.google.gwt.user.server.rpc.HybridServiceServlet;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LargePayloadServiceImpl extends HybridServiceServlet implements LargePayloadService{
  public LargePayloadServiceImpl() {
  }

  public List<UserInfo> testLargeResponsePayload() {
    List<UserInfo> result = new ArrayList<UserInfo>();
    for (int i=0; i<1000; i++){
      UserInfo userInfo = new UserInfo();
      userInfo.setId("user_id_" + i);
      userInfo.setEmail("user" + i + "@example.com");
      result.add(userInfo);
    }
    return result;
  }

  public int[] testLargeResponseArray() {
    int[] result = new int[70000];
    for (int i=0; i<result.length; i++){
      result[i] = i;
    }
    return result;
  }
}
