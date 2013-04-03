package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CollectionsTestServiceAsync;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeArrayList;

import com.google.gwt.user.client.rpc.TestSetValidator;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class CollectionsCallbackTest extends TestCase{
  private static CollectionsTestServiceAsync service = 
    (CollectionsTestServiceAsync)SyncProxy.newProxyInstance(
        CollectionsTestServiceAsync.class, RPCSyncTestSuite.BASE_URL, 
        "collections");

  public void testArrayList() {
    service.echo(TestSetFactory.createArrayList(), new AsyncCallback<ArrayList<MarkerTypeArrayList>>(){
      public void onFailure(Throwable caught) {
        fail(caught.getMessage());
      }

      public void onSuccess(ArrayList<MarkerTypeArrayList> result) {
        assertNotNull(result);
        assertTrue(TestSetValidator.isValid(result));
      }
    });
  }
  
  public void testBooleanArray() {
    final Boolean[] expected = TestSetFactory.createBooleanArray();
    service.echo(expected, new AsyncCallback<Boolean[]>(){
      public void onFailure(Throwable caught) {
        fail(caught.getMessage());
      }

      public void onSuccess(Boolean[] result) {
        assertNotNull(result);
        assertTrue(TestSetValidator.equals(expected, result));
      }
    });
  }
}
