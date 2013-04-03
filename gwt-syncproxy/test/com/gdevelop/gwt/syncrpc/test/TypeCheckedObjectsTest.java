package com.gdevelop.gwt.syncrpc.test;

import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.EnumsTestService;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.TypeCheckedGenericClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestService;

import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedFieldClass;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetFactory.TypeCheckedSuperClass;

import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestSetValidator;
import com.google.gwt.user.client.rpc.TypeUncheckedGenericClass;

import java.lang.reflect.UndeclaredThrowableException;

import java.util.HashSet;

import junit.framework.TestCase;

public class TypeCheckedObjectsTest extends TestCase{
  private static TypeCheckedObjectsTestService service = 
    (TypeCheckedObjectsTestService)SyncProxy.newProxyInstance(
        TypeCheckedObjectsTestService.class, RPCSyncTestSuite.BASE_URL, 
        "typecheckedobjects");

  public TypeCheckedObjectsTest() {
  }
  
  public void testInvalidCheckedFieldSerializer() {
    TypeCheckedFieldClass arg1 = 
      TypeCheckedObjectsTestSetFactory.createInvalidCheckedFieldClass();
    try{
      service.echo(arg1);
      fail("testInvalidCheckedFieldSerializer is expected to throw an assertion");
    }catch(Exception caught){
      expectedException(caught, SerializationException.class);
    }
  }
  
  public void testInvalidCheckedSerializer() {
    TypeCheckedGenericClass arg = TypeCheckedObjectsTestSetFactory.createInvalidCheckedGenericClass();
    try{
      service.echo(arg);
      fail("testInvalidCheckedSerializer is expected to throw an assertion");
    }catch(Exception caught){
      expectedException(caught, SerializationException.class);
    }
  }
  
  public void testInvalidCheckedSuperSerializer() {
    TypeCheckedSuperClass arg = 
      TypeCheckedObjectsTestSetFactory.createInvalidCheckedSuperClass();
    try{
      service.echo(arg);
      fail("testInvalidCheckedSuperSerializer is expected to throw an assertion");
    }catch(Exception caught){
      expectedException(caught, SerializationException.class);
    }
  }
  
  public void testInvalidUncheckedSerializer() {
    TypeUncheckedGenericClass arg = 
      TypeCheckedObjectsTestSetFactory.createInvalidUncheckedGenericClass();
    try{
      service.echo(arg);
      fail("testInvalidUncheckedSerializer is expected to throw an assertion");
    }catch(Exception caught){
      expectedException(caught, SerializationException.class);
    }
  }
  
  public void testTypeCheckedFieldSerializer() {
    TypeCheckedFieldClass arg = 
      TypeCheckedObjectsTestSetFactory.createTypeCheckedFieldClass();
    
    TypeCheckedFieldClass result = service.echo(arg);
    assertNotNull(result);
    assertTrue(TypeCheckedObjectsTestSetValidator.isValid(result));
  }
  
  public void testTypeCheckedSerializer() {
    TypeCheckedGenericClass arg = 
      TypeCheckedObjectsTestSetFactory.createTypeCheckedGenericClass();
    
    TypeCheckedGenericClass result = service.echo(arg);
    assertNotNull(result);
    assertTrue(TypeCheckedObjectsTestSetValidator.isValid(result));
  }
  
  public void testTypeCheckedSuperSerializer() {
    TypeCheckedSuperClass arg = 
      TypeCheckedObjectsTestSetFactory.createTypeCheckedSuperClass();
    
    TypeCheckedSuperClass result = service.echo(arg);
    assertNotNull(result);
    assertTrue(TypeCheckedObjectsTestSetValidator.isValid(result));
  }
  
  public void testTypeUncheckedSerializer() {
    TypeUncheckedGenericClass arg = 
      TypeCheckedObjectsTestSetFactory.createTypeUncheckedGenericClass();
    
    TypeUncheckedGenericClass result = service.echo(arg);
    assertNotNull(result);
    assertTrue(TypeCheckedObjectsTestSetValidator.isValid(result));
  }
  
  private void expectedException(Exception caught, Class expectedClass){
    assertTrue((expectedClass.isAssignableFrom(caught.getClass())) || 
        ((caught instanceof UndeclaredThrowableException) 
          && (expectedClass.isAssignableFrom(caught.getCause().getClass()))));
  }
}
