package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.CredentialsManager;
import com.gdevelop.gwt.syncrpc.LoginCredentials;
import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.ValueTypesTestService;

import java.net.URL;

import junit.framework.TestCase;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class ValueTypesTest extends TestCase{
  private static ValueTypesTestService service = 
    (ValueTypesTestService)SyncProxy.newProxyInstance(
        ValueTypesTestService.class, RPCSyncTestSuite.BASE_URL, 
        "valuetypes");
//  static{
//    SyncProxy.getDefaultSessionManager().setCredentialsManager(new CredentialsManager(){
//      public LoginCredentials getLoginCredentials(URL url) {
//        return new LoginCredentials("gae", "http://localhost:8888", "http://localhost:8888", "a@b.com", "");
//      }
//    });
//  }
  public ValueTypesTest() {
  }
  
  public void testBoolean_FALSE() {
    Object result = service.echo_FALSE(false);
    assertNotNull(result);
    assertFalse(((Boolean) result).booleanValue());
  }

  public void testBoolean_TRUE() {
    Object result = service.echo_TRUE(true);
    assertNotNull(result);
    assertTrue(((Boolean) result).booleanValue());
  }
  
  public void testByte() {
    Object result = service.echo((byte) (Byte.MAX_VALUE / (byte) 2));
    assertNotNull(result);
    assertEquals(Byte.MAX_VALUE / 2, ((Byte) result).byteValue());
  }
  
  public void testByte_MAX_VALUE() {
    Object result = service.echo_MAX_VALUE(Byte.MAX_VALUE);
    assertNotNull(result);
    assertEquals(Byte.MAX_VALUE, ((Byte) result).byteValue());
  }

  public void testByte_MIN_VALUE() {
    Object result = service.echo_MIN_VALUE(Byte.MIN_VALUE);
    assertNotNull(result);
    assertEquals(Byte.MIN_VALUE, ((Byte) result).byteValue());
  }

  public void testChar() {
    Object result = service.echo((char) (Character.MAX_VALUE / (char) 2));
    assertNotNull(result);
    assertEquals(Character.MAX_VALUE / 2, ((Character) result).charValue());
  }
  
  public void testChar_MAX_VALUE() {
    Object result = service.echo_MAX_VALUE(Character.MAX_VALUE);
    assertNotNull(result);
    assertEquals(Character.MAX_VALUE, ((Character) result).charValue());
  }

  public void testChar_MIN_VALUE() {
    Object result = service.echo_MIN_VALUE(Character.MIN_VALUE);
    assertNotNull(result);
    assertEquals(Character.MIN_VALUE, ((Character) result).charValue());
  }

  public void testDouble() {
    Object result = service.echo((double) (Double.MAX_VALUE / (double) 2));
    assertNotNull(result);
    assertEquals(Double.MAX_VALUE / 2, ((Double) result).doubleValue());
  }
  
  public void testDouble_MAX_VALUE() {
    Object result = service.echo_MAX_VALUE(Double.MAX_VALUE);
    assertNotNull(result);
    assertEquals(Double.MAX_VALUE, ((Double) result).doubleValue());
  }

  public void testDouble_MIN_VALUE() {
    Object result = service.echo_MIN_VALUE(Double.MIN_VALUE);
    assertNotNull(result);
    assertEquals(Double.MIN_VALUE, ((Double) result).doubleValue());
  }

  /**
   * Validate that NaNs (not-a-number, such as 0/0) propagate properly via RPC.
   */
  public void testDouble_NaN() {
    Object result = service.echo(Double.NaN);
    assertNotNull(result);
    assertTrue(Double.isNaN(((Double) result).doubleValue()));
  }

  /**
   * Validate that negative infinity propagates properly via RPC.
   */
  public void testDouble_NegInfinity() {
    Object result = service.echo(Double.NEGATIVE_INFINITY);
    assertNotNull(result);
    double doubleValue = ((Double) result).doubleValue();
    assertTrue(Double.isInfinite(doubleValue) && doubleValue < 0);
  }

  /**
   * Validate that positive infinity propagates properly via RPC.
   */
  public void testDouble_PosInfinity() {
    Object result = service.echo(Double.POSITIVE_INFINITY);
    assertNotNull(result);
    double doubleValue = ((Double) result).doubleValue();
    assertTrue(Double.isInfinite(doubleValue) && doubleValue > 0);
  }

  public void testFloat() {
    Object result = service.echo((float) (Float.MAX_VALUE / (float) 2));
    assertNotNull(result);
    assertEquals(Float.MAX_VALUE / 2, ((Float) result).floatValue());
  }
  
  public void testFloat_MAX_VALUE() {
    Object result = service.echo_MAX_VALUE(Float.MAX_VALUE);
    assertNotNull(result);
    assertEquals(Float.MAX_VALUE, ((Float) result).floatValue());
  }

  public void testFloat_MIN_VALUE() {
    Object result = service.echo_MIN_VALUE(Float.MIN_VALUE);
    assertNotNull(result);
    assertEquals(Float.MIN_VALUE, ((Float) result).floatValue());
  }

  /**
   * Validate that NaNs (not-a-number, such as 0/0) propagate properly via RPC.
   */
  public void testFloat_NaN() {
    Object result = service.echo(Float.NaN);
    assertNotNull(result);
    assertTrue(Float.isNaN(((Float) result).floatValue()));
  }

  /**
   * Validate that negative infinity propagates properly via RPC.
   */
  public void testFloat_NegInfinity() {
    Object result = service.echo(Float.NEGATIVE_INFINITY);
    assertNotNull(result);
    float floatValue = ((Float) result).floatValue();
    assertTrue(Float.isInfinite(floatValue) && floatValue < 0);
  }

  /**
   * Validate that positive infinity propagates properly via RPC.
   */
  public void testFloat_PosInfinity() {
    Object result = service.echo(Float.POSITIVE_INFINITY);
    assertNotNull(result);
    float floatValue = ((Float) result).floatValue();
    assertTrue(Float.isInfinite(floatValue) && floatValue > 0);
  }

  public void testInteger() {
    Object result = service.echo(Integer.MAX_VALUE / 2);
    assertNotNull(result);
    assertEquals(Integer.MAX_VALUE / 2, ((Integer) result).intValue());
  }
  
  public void testInteger_MAX_VALUE() {
    Object result = service.echo_MAX_VALUE(Integer.MAX_VALUE);
    assertNotNull(result);
    assertEquals(Integer.MAX_VALUE, ((Integer) result).intValue());
  }

  public void testInteger_MIN_VALUE() {
    Object result = service.echo_MIN_VALUE(Integer.MIN_VALUE);
    assertNotNull(result);
    assertEquals(Integer.MIN_VALUE, ((Integer) result).intValue());
  }

  public void testLong() {
    Object result = service.echo(Long.MAX_VALUE / 2);
    assertNotNull(result);
    assertEquals(Long.MAX_VALUE / 2, ((Long) result).longValue());
  }
  
  public void testLong_MAX_VALUE() {
    Object result = service.echo_MAX_VALUE(Long.MAX_VALUE);
    assertNotNull(result);
    assertEquals(Long.MAX_VALUE, ((Long) result).longValue());
  }

  public void testLong_MIN_VALUE() {
    Object result = service.echo_MIN_VALUE(Long.MIN_VALUE);
    assertNotNull(result);
    assertEquals(Long.MIN_VALUE, ((Long) result).longValue());
  }

  public void testShort() {
    Object result = service.echo((short)(Short.MAX_VALUE / 2));
    assertNotNull(result);
    assertEquals(Short.MAX_VALUE / 2, ((Short) result).shortValue());
  }
  
  public void testShort_MAX_VALUE() {
    Object result = service.echo_MAX_VALUE(Short.MAX_VALUE);
    assertNotNull(result);
    assertEquals(Short.MAX_VALUE, ((Short) result).shortValue());
  }

  public void testShort_MIN_VALUE() {
    Object result = service.echo_MIN_VALUE(Short.MIN_VALUE);
    assertNotNull(result);
    assertEquals(Short.MIN_VALUE, ((Short) result).shortValue());
  }
}
