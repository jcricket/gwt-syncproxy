package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.CustomFieldSerializerTestService;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestSetFactory;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestSetValidator;
import com.google.gwt.user.client.rpc.ManuallySerializedClass;
import com.google.gwt.user.client.rpc.ManuallySerializedImmutableClass;

import junit.framework.TestCase;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class CustomFieldSerializerTest extends TestCase{
  private static CustomFieldSerializerTestService service = 
    (CustomFieldSerializerTestService)SyncProxy.newProxyInstance(
        CustomFieldSerializerTestService.class, RPCSyncTestSuite.BASE_URL, 
        "customfieldserializers");
  
  public CustomFieldSerializerTest() {
  }
  
  public void testCustomFieldSerializabilityInheritance() {
    try{
      service.echo(CustomFieldSerializerTestSetFactory.createUnserializableSubclass());
    }catch(Exception e){
      return; // OK
    }
    fail("Class UnserializableSubclass should not be serializable");
  }
  
  public void testCustomFieldSerialization() {
    Object result = service.echo(CustomFieldSerializerTestSetFactory.createUnserializableClass());
    assertNotNull(result);
    assertTrue(CustomFieldSerializerTestSetValidator.isValid((ManuallySerializedClass) result));
  }
  
  public void testSerializableSubclasses() {
    Object result = service.echo(CustomFieldSerializerTestSetFactory.createSerializableSubclass());
    assertNotNull(result);
    assertTrue(CustomFieldSerializerTestSetValidator.isValid((CustomFieldSerializerTestSetFactory.SerializableSubclass) result));
  }
  
  public void testSerializableImmutables() {
    Object result = service.echo(CustomFieldSerializerTestSetFactory.createSerializableImmutablesArray());
    assertNotNull(result);
    assertTrue(CustomFieldSerializerTestSetValidator.isValid(
        (ManuallySerializedImmutableClass[]) result));
  }
}
