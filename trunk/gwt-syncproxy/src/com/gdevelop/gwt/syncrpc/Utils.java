package com.gdevelop.gwt.syncrpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.InputStream;

import java.net.HttpURLConnection;

public class Utils {
  /**
   * Copied from com.google.gwt.lang.LongLib
   * @param value
   * @return
   */
  public static long longFromBase64(String value) {
    int pos = 0;
    long longVal = base64Value(value.charAt(pos++));
    int len = value.length();
    while (pos < len) {
      longVal <<= 6;
      longVal |= base64Value(value.charAt(pos++));
    }
    return longVal;
  }

  // Assume digit is one of [A-Za-z0-9$_]
  private static int base64Value(char digit) {
    if (digit >= 'A' && digit <= 'Z') {
      return digit - 'A';
    }
    // No need to check digit <= 'z'
    if (digit >= 'a') {
      return digit - 'a' + 26;
    }
    if (digit >= '0' && digit <= '9') {
      return digit - '0' + 52;
    }
    if (digit == '$') {
      return 62;
    }
    // digit == '_'
    return 63;
  }

  /**
   * Return an optionally single-quoted string containing a base-64 encoded
   * version of the given long value.
   * 
   * Keep this synchronized with the version in Base64Utils.
   */
  public static String toBase64(long value) {
    // Convert to ints early to avoid need for long ops
    int low = (int) (value & 0xffffffff);
    int high = (int) (value >> 32);

    StringBuilder sb = new StringBuilder();
    boolean haveNonZero = base64Append(sb, (high >> 28) & 0xf, false);
    haveNonZero = base64Append(sb, (high >> 22) & 0x3f, haveNonZero);
    haveNonZero = base64Append(sb, (high >> 16) & 0x3f, haveNonZero);
    haveNonZero = base64Append(sb, (high >> 10) & 0x3f, haveNonZero);
    haveNonZero = base64Append(sb, (high >> 4) & 0x3f, haveNonZero);
    int v = ((high & 0xf) << 2) | ((low >> 30) & 0x3);
    haveNonZero = base64Append(sb, v, haveNonZero);
    haveNonZero = base64Append(sb, (low >> 24) & 0x3f, haveNonZero);
    haveNonZero = base64Append(sb, (low >> 18) & 0x3f, haveNonZero);
    haveNonZero = base64Append(sb, (low >> 12) & 0x3f, haveNonZero);
    base64Append(sb, (low >> 6) & 0x3f, haveNonZero);
    base64Append(sb, low & 0x3f, true);

    return sb.toString();
  }

  private static boolean base64Append(StringBuilder sb, int digit,
      boolean haveNonZero) {
    if (digit > 0) {
      haveNonZero = true;
    }
    if (haveNonZero) {
      int c;
      if (digit < 26) {
        c = 'A' + digit;
      } else if (digit < 52) {
        c = 'a' + digit - 26;
      } else if (digit < 62) {
        c = '0' + digit - 52;
      } else if (digit == 62) {
        c = '$';
      } else {
        c = '_';
      }
      sb.append((char) c);
    }
    return haveNonZero;
  }

  public static String getResposeText(HttpURLConnection connection) throws IOException {
    InputStream is = connection.getInputStream();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int len;
    while ((len = is.read(buffer)) > 0) {
      baos.write(buffer, 0, len);
    }
    String responseText = baos.toString("UTF8");
    return responseText;
  }
}
