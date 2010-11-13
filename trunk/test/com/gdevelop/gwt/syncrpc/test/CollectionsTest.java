package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.CollectionsTestService;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeArraysAsList;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeHashMap;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeHashSet;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeLinkedHashMap;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeTreeMap;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeTreeSet;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeVector;
import com.google.gwt.user.client.rpc.TestSetValidator;

import java.sql.Time;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import junit.framework.TestCase;


/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class CollectionsTest extends TestCase{
  private static CollectionsTestService service = 
    (CollectionsTestService)SyncProxy.newProxyInstance(
        CollectionsTestService.class, RPCSyncTestSuite.BASE_URL, 
        "collections");
  
  public CollectionsTest() {
  }
  
  public void testArrayList() {
    try {
      ArrayList<TestSetFactory.MarkerTypeArrayList> result = service.echo(TestSetFactory.createArrayList());
      assertNotNull(result);
      assertTrue(TestSetValidator.isValid(result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  // TODO: Test failed
  public void _testArraysAsList() {
    try{
      List<MarkerTypeArraysAsList> expected = TestSetFactory.createArraysAsList();
      List<MarkerTypeArraysAsList> result = service.echoArraysAsList(expected);
      assertNotNull(result);
      assertEquals(expected, result);
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testBooleanArray() {
    try{
      Boolean[] expected = TestSetFactory.createBooleanArray();
      Boolean[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testByteArray() {
    try{
      Byte[] expected = TestSetFactory.createByteArray();
  
      Byte[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testCharArray() {
    try{
      Character[] expected = TestSetFactory.createCharArray();
  
      Character[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testDoubleArray() {
    try{
      Double[] expected = TestSetFactory.createDoubleArray();

      Double[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testFloatArray() {
    try{
      Float[] expected = TestSetFactory.createFloatArray();

      Float[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testHashMap() {
    try{
      HashMap<String, MarkerTypeHashMap> expected = TestSetFactory.createHashMap();
      HashMap<String, MarkerTypeHashMap> result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.isValid(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testHashSet() {
    try{
      HashSet<MarkerTypeHashSet> expected = TestSetFactory.createHashSet();
      HashSet<MarkerTypeHashSet> result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.isValid(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testIntegerArray() {
    try{
      Integer[] expected = TestSetFactory.createIntegerArray();
      Integer[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testLinkedHashMap() {
    try{
      LinkedHashMap<String, MarkerTypeLinkedHashMap> expected = TestSetFactory.createLinkedHashMap();
      LinkedHashMap<String, MarkerTypeLinkedHashMap> result = service.echo(expected);
      assertNotNull(result);
      expected.get("SerializableSet");
      result.get("SerializableSet");
      assertTrue(TestSetValidator.isValid(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testLinkedHashMapLRU() {
    try{
      LinkedHashMap<String, MarkerTypeLinkedHashMap> expected = TestSetFactory.createLRULinkedHashMap();
      LinkedHashMap<String, MarkerTypeLinkedHashMap> actual = service.echo(expected);
      assertNotNull(actual);
      expected.get("SerializableSet");
      actual.get("SerializableSet");
      assertTrue(TestSetValidator.isValid(expected, actual));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testLongArray() {
    try{
      Long[] expected = TestSetFactory.createLongArray();
      Long[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }
  
  public void testPrimitiveBooleanArray() {
    try{
      boolean[] expected = TestSetFactory.createPrimitiveBooleanArray();
      boolean[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testPrimitiveByteArray() {
    try{
      byte[] expected = TestSetFactory.createPrimitiveByteArray();
      byte[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testPrimitiveCharArray() {
    try{
      char[] expected = TestSetFactory.createPrimitiveCharArray();
      char[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testPrimitiveDoubleArray() {
    try{
      double[] expected = TestSetFactory.createPrimitiveDoubleArray();
      double[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testPrimitiveFloatArray() {
    try{
      float[] expected = TestSetFactory.createPrimitiveFloatArray();
      float[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testPrimitiveIntegerArray() {
    try{
      int[] expected = TestSetFactory.createPrimitiveIntegerArray();
      int[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testPrimitiveLongArray() {
    try{
      long[] expected = TestSetFactory.createPrimitiveLongArray();
      long[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testPrimitiveShortArray() {
    try{
      short[] expected = TestSetFactory.createPrimitiveShortArray();
      short[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testShortArray() {
    try{
      Short[] expected = TestSetFactory.createShortArray();
      Short[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testSqlDateArray() {
    try{
      java.sql.Date[] expected = TestSetFactory.createSqlDateArray();
      java.sql.Date[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testSqlTimeArray() {
    try{
      Time[] expected = TestSetFactory.createSqlTimeArray();
      Time[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testSqlTimestampArray() {
    try{
      Timestamp[] expected = TestSetFactory.createSqlTimestampArray();
      Timestamp[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }

  public void testStringArray() {
    try{
      String[] expected = TestSetFactory.createStringArray();
      String[] result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.equals(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testStringArrayArray() {
    try{
      String[][] expected = new String[][] {
              new String[] {"hello"}, new String[] {"bye"}};
      String[][] result = service.echo(expected);
      assertNotNull(result);
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testTreeMap() {
    try{
      for (boolean option : new boolean[] {true, false}) {
        TreeMap<String, MarkerTypeTreeMap> expected = TestSetFactory.createTreeMap(option);
        TreeMap<String, MarkerTypeTreeMap> result = service.echo(expected, option);
        assertNotNull(result);
        assertTrue(TestSetValidator.isValid(expected, result));
      }
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testTreeSet() {
    try{
      for (boolean option : new boolean[] {true, false}) {
        TreeSet<MarkerTypeTreeSet> expected = TestSetFactory.createTreeSet(option);
        TreeSet<MarkerTypeTreeSet> result = service.echo(expected, option);
        assertNotNull(result);
        assertTrue(TestSetValidator.isValid(expected, result));
      }
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
  
  public void testVector() {
    try{
      Vector<MarkerTypeVector> expected = TestSetFactory.createVector();
      Vector<MarkerTypeVector> result = service.echo(expected);
      assertNotNull(result);
      assertTrue(TestSetValidator.isValid(expected, result));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
}
