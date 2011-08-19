package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.user.client.rpc.UnicodeEscapingService;
import com.google.gwt.user.client.rpc.UnicodeEscapingService.InvalidCharacterException;

import junit.framework.TestCase;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class UnicodeEscapingTest extends TestCase{
  static{
    RPCSyncTestSuite.login();
  }
  
  /** the size of a block of characters to test */
  private static final int CHARACTER_BLOCK_SIZE = 64;

  /**
   * When doing the non-BMP test, we don't test every block of characters
   * because it takes too long - this is the increment to use.  It is not a
   * power of two so we alter the alignment of the block of characters we skip.
   */
  private static final int NON_BMP_TEST_INCREMENT = 8192 + 64;

  private static UnicodeEscapingService service = 
    (UnicodeEscapingService)SyncProxy.newProxyInstance(
        UnicodeEscapingService.class, RPCSyncTestSuite.BASE_URL, 
        "unicodeEscape");

  /** start of current block being tested */
  private int current;

  public UnicodeEscapingTest() {
  }
  
  public void testClientToServerBMP() throws InvalidCharacterException {
    clientToServerVerifyRange(Character.MIN_CODE_POINT,
        Character.MIN_SURROGATE, 1,
        CHARACTER_BLOCK_SIZE);
    clientToServerVerifyRange(Character.MAX_SURROGATE + 1,
        Character.MIN_SUPPLEMENTARY_CODE_POINT, CHARACTER_BLOCK_SIZE,
        CHARACTER_BLOCK_SIZE);
  }
  public void testClientToServerNonBMP() throws InvalidCharacterException {
    clientToServerVerifyRange(Character.MIN_SUPPLEMENTARY_CODE_POINT,
        Character.MAX_CODE_POINT + 1, CHARACTER_BLOCK_SIZE,
        NON_BMP_TEST_INCREMENT);
  }
  public void testServerToClientBMP() throws InvalidCharacterException {
    serverToClientVerify(Character.MIN_CODE_POINT,
        Character.MIN_SUPPLEMENTARY_CODE_POINT, CHARACTER_BLOCK_SIZE,
        CHARACTER_BLOCK_SIZE);
  }
  public void testServerToClientNonBMP() throws InvalidCharacterException{
    serverToClientVerify(Character.MIN_SUPPLEMENTARY_CODE_POINT,
        Character.MAX_CODE_POINT + 1, CHARACTER_BLOCK_SIZE,
        NON_BMP_TEST_INCREMENT);
  }
  
  private void clientToServerVerifyRange(final int start, final int end,
      final int size, final int step) throws InvalidCharacterException {
    current = start;
    
    while (current < end) {
      int blockEnd = Math.min(end, current + size);

      service.verifyStringContainingCharacterRange(current, blockEnd,
                                                   com.google.gwt.user.client.rpc.UnicodeEscapingTest.getStringContainingCharacterRange(current, blockEnd));
      
      current += step;
    }
  }
  
  private void serverToClientVerify(final int start, final int end,
      final int size, final int step) throws InvalidCharacterException {
    current = start;
    
    while (current < end) {
      int blockEnd = Math.min(end, current + size);

      String str = service.getStringContainingCharacterRange(current, blockEnd);
      com.google.gwt.user.client.rpc.UnicodeEscapingTest.verifyStringContainingCharacterRange(current, blockEnd, str);
      
      current += step;
    }
  }
}
