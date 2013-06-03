package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.ObjectGraphTestService;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializableDoublyLinkedNode;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializablePrivateNoArg;
import com.google.gwt.user.client.rpc.TestSetFactory.SerializableWithTwoArrays;
import com.google.gwt.user.client.rpc.TestSetValidator;

import junit.framework.TestCase;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class ObjectGraphTest extends TestCase{
  private static ObjectGraphTestService service = 
    (ObjectGraphTestService)SyncProxy.newProxyInstance(
        ObjectGraphTestService.class, RPCSyncTestSuite.BASE_URL, 
        "objectgraphs");
  
  public ObjectGraphTest() {
  }
  
  public void testAcyclicGraph() {
    Object result = service.echo_AcyclicGraph(TestSetFactory.createAcyclicGraph());
    assertNotNull(result);
    assertTrue(TestSetValidator.isValidAcyclicGraph((SerializableDoublyLinkedNode) result));
  }
  
  public void testComplexCyclicGraph() {
    Object result = service.echo_ComplexCyclicGraph(TestSetFactory.createComplexCyclicGraph());
    assertNotNull(result);
    assertTrue(TestSetValidator.isValidComplexCyclicGraph((SerializableDoublyLinkedNode) result));
  }
  
  public void testComplexCyclicGraph2() {
    SerializableDoublyLinkedNode node = TestSetFactory.createComplexCyclicGraph();
    Object result = service.echo_ComplexCyclicGraph(node, node);
    assertNotNull(result);
    assertTrue(TestSetValidator.isValidComplexCyclicGraph((SerializableDoublyLinkedNode) result));
  }
  
  public void testDoublyReferencedArray() {
    SerializableWithTwoArrays node = TestSetFactory.createDoublyReferencedArray();
    Object result = service.echo_SerializableWithTwoArrays(node);
    assertNotNull(result);
    assertTrue(TestSetValidator.isValid((SerializableWithTwoArrays) result));
  }
  
  public void testPrivateNoArg() {
    SerializablePrivateNoArg node = TestSetFactory.createPrivateNoArg();
    Object result = service.echo_PrivateNoArg(node);
    assertNotNull(result);
    assertTrue(TestSetValidator.isValid((SerializablePrivateNoArg) result));
  }
  
  public void testTrivialCyclicGraph() {
    Object result = service.echo_TrivialCyclicGraph(TestSetFactory.createTrivialCyclicGraph());
    assertNotNull(result);
    assertTrue(TestSetValidator.isValidTrivialCyclicGraph((SerializableDoublyLinkedNode) result));
  }
}
