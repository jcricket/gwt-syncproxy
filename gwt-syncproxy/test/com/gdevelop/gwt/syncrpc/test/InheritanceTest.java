package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.InheritanceTestService;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.AnonymousClassInterface;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.Circle;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableClass;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableClassWithTransientField;
import com.google.gwt.user.client.rpc.InheritanceTestSetFactory.SerializableSubclass;
import com.google.gwt.user.client.rpc.InheritanceTestSetValidator;

import junit.framework.TestCase;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class InheritanceTest extends TestCase{
  private static InheritanceTestService service = 
    (InheritanceTestService)SyncProxy.newProxyInstance(
        InheritanceTestService.class, RPCSyncTestSuite.BASE_URL, 
        "inheritance");
  
  public InheritanceTest() {
  }
  
  /**
   * Test that anonymous classes are not serializable.
   */
  public void testAnonymousClasses() {
    try{
      service.echo(new AnonymousClassInterface() {
        public void foo() {
          // purposely empty
        }
      });
    }catch(Exception e){
      return; // OK
    }
    fail("Anonymous inner classes should not be serializable");
  }
  
  /**
   * Tests that a shadowed field is properly serialized.
   * 
   * Checks for <a href="bug
   * http://code.google.com/p/google-web-toolkit/issues/detail?id=161">BUG 161</a>
   */
  public void testFieldShadowing() {
    Object result = service.echo(InheritanceTestSetFactory.createCircle());
    Circle circle = (Circle) result;
    assertNotNull(circle.getName());
  }
  
  /**
   * Tests that transient fields do not prevent serializability.
   */
  public void testJavaSerializableClass() {
    Object result = service.echo(new InheritanceTestSetFactory.JavaSerializableClass(3));
    assertNotNull(result);
  }
  
  /**
   * Test that non-static inner classes are not serializable.
   */
  public void testNonStaticInnerClass() {
    try{
      service.echo(InheritanceTestSetFactory.createNonStaticInnerClass());
    }catch(Exception e){
      return; // OK
    }
    fail("Non-static inner classes should not be serializable");
  }
  
  public void testReturnOfUnserializableClassFromServer() {
    try{
      service.getUnserializableClass();
    }catch(Exception e){
      return; // OK
    }
    fail("Returning an unserializable class from the server should fail");
  }
  
  /**
   * Test that a valid serializable class can be serialized.
   */
  public void testSerializableClass() {
    Object result = service.echo(InheritanceTestSetFactory.createSerializableClass());
    assertNotNull(result);
    assertTrue(InheritanceTestSetValidator.isValid((SerializableClass) result));
  }
  
  /**
   * Test that IsSerializable is inherited, also test static inner classes.
   */
  public void testSerializableSubclass() {
    Object result = service.echo(InheritanceTestSetFactory.createSerializableSubclass());
    assertNotNull(result);
    assertTrue(InheritanceTestSetValidator.isValid((SerializableSubclass) result));
  }

  /**
   * Tests that transient fields do not prevent serializability.
   */
  public void testTransientFieldExclusion() {
    Object result = service.echo(
        InheritanceTestSetFactory.createSerializableClassWithTransientField());
    assertNotNull(result);
    assertTrue(InheritanceTestSetValidator.isValid((SerializableClassWithTransientField) result));
  }
}
