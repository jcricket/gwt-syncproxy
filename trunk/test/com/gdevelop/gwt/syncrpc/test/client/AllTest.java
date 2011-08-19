package com.gdevelop.gwt.syncrpc.test.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CollectionsTestService;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestService;
import com.google.gwt.user.client.rpc.EnumsTestService;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestServiceAsync;
import com.google.gwt.user.client.rpc.ExceptionsTestService;
import com.google.gwt.user.client.rpc.ExceptionsTestServiceAsync;
import com.google.gwt.user.client.rpc.InheritanceTestService;
import com.google.gwt.user.client.rpc.MixedSerializableEchoService;
import com.google.gwt.user.client.rpc.ObjectGraphTestService;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.UnicodeEscapingService;
import com.google.gwt.user.client.rpc.ValueTypesTestService;

public class AllTest implements EntryPoint{
  public AllTest() {
  }

  public void onModuleLoad() {
    GWT.create(CollectionsTestService.class);
    GWT.create(CustomFieldSerializerTestService.class);
    GWT.create(EnumsTestService.class);
    GWT.create(ExceptionsTestService.class);
    GWT.create(InheritanceTestService.class);
    GWT.create(ObjectGraphTestService.class);
    GWT.create(MixedSerializableEchoService.class);
    GWT.create(UnicodeEscapingService.class);
    GWT.create(ValueTypesTestService.class);
    
    testEnums();
  }
  
  private void testEnums(){
    EnumsTestServiceAsync service = (EnumsTestServiceAsync)GWT.create(EnumsTestService.class);
    service.echo(Basic.A, new AsyncCallback<Basic>(){
      public void onFailure(Throwable caught) {
        caught.printStackTrace();
      }

      public void onSuccess(EnumsTestService.Basic result) {
        System.out.println("result: " + result);
      }
    });
  }

  private void testExceptions(){
    ExceptionsTestServiceAsync service = (ExceptionsTestServiceAsync)GWT.create(ExceptionsTestService.class);
    service.echo(TestSetFactory.createUmbrellaException(), new AsyncCallback<UmbrellaException>(){
      public void onFailure(Throwable caught) {
        caught.printStackTrace();
      }

      public void onSuccess(UmbrellaException result) {
        System.out.println("result: " + result);
      }
    });
  }
}
