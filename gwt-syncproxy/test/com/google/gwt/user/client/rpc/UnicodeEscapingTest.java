/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.user.client.rpc;

import java.util.ArrayList;
import java.util.List;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.user.client.rpc.UnicodeEscapingService.InvalidCharacterException;

/**
 * Test that any valid string can be sent via RPC in both directions.
 *
 * TODO(jat): make unpaired surrogates work properly if it is possible to do so
 * on all browsers, then add them to this test.
 *
 * Modified by P.Prith in 0.5 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0. Also modified class to extend
 * from modified RpcTestBase instead of GWTTestCase along with dependent methods
 */
public class UnicodeEscapingTest extends RpcTestBase {

	/**
	 * Generates a string containing a sequence of code points.
	 *
	 * @param start
	 *            first code point to include in the string
	 * @param end
	 *            one past the last code point to include in the string
	 * @return a string containing all the requested code points
	 */
	public static String getStringContainingCharacterRange(int start, int end) {
		StringBuffer buf = new StringBuffer();
		for (int codePoint = start; codePoint < end; ++codePoint) {
			if (Character.isSupplementaryCodePoint(codePoint)) {
				buf.append(Character.toChars(codePoint));
			} else {
				buf.append((char) codePoint);
			}
		}

		return buf.toString();
	}

	/**
	 * Verifies that the supplied string includes the requested code points.
	 *
	 * @param start
	 *            first code point to include in the string
	 * @param end
	 *            one past the last code point to include in the string
	 * @param str
	 *            the string to test
	 * @throws InvalidCharacterException
	 *             if a character doesn't match
	 * @throws RuntimeException
	 *             if the string is too long
	 */
	public static void verifyStringContainingCharacterRange(int start, int end,
			String str) throws InvalidCharacterException {
		if (str == null) {
			throw new NullPointerException("String is null");
		}
		int expectedLen = end - start;
		int strLen = str.codePointCount(0, str.length());
		for (int i = 0, codePoint = start; i < strLen; i = Character
				.offsetByCodePoints(str, i, 1)) {
			int strCodePoint = str.codePointAt(i);
			if (strCodePoint != codePoint) {
				throw new InvalidCharacterException(i, codePoint, strCodePoint);
			}
			++codePoint;
		}
		if (strLen < expectedLen) {
			throw new InvalidCharacterException(strLen, start + strLen, -1);
		} else if (expectedLen != strLen) {
			throw new RuntimeException(
					"Too many characters returned on block from U+"
							+ Integer.toHexString(start) + " to U+"
							+ Integer.toHexString(end) + ": expected="
							+ expectedLen + ", actual=" + strLen);
		}
	}

	/** the size of a block of characters to test. */
	private static final int CHARACTER_BLOCK_SIZE = 64;

	/**
	 * When doing the non-BMP test, we don't test every block of characters
	 * because it takes too long - this is the increment to use. It is not a
	 * power of two so we alter the alignment of the block of characters we
	 * skip.
	 */
	private static final int NON_BMP_TEST_INCREMENT = 8192 + 64;

	/** the time to wait for the test of a block of characters. */
	private static final int TEST_FINISH_DELAY_MS = 500000;

	/** start of current block being tested. */
	protected int current;

	final static int BMPTimeout = 10;

	protected void clientToServerVerifyRange(final int start, final int end,
			final int size, final int step) throws InvalidCharacterException {
		this.current = start;
		int blockEnd = Math.min(end, this.current + size);
		getService().verifyStringContainingCharacterRange(this.current,
				blockEnd, getStringContainingCharacterRange(start, blockEnd),
				new AsyncCallback<Boolean>() {
			List<Throwable> fails = new ArrayList<Throwable>();

			@Override
			public void onFailure(Throwable caught) {
				this.fails.add(caught);
				onSuccess(false);
			}

			@Override
			public void onSuccess(Boolean ignored) {
				UnicodeEscapingTest.this.current += step;
				if (UnicodeEscapingTest.this.current < end) {
					// setRpcTimeout(BMPTimeout);
					// delayTestFinishForRpc();
					int blockEnd = Math.min(end,
							UnicodeEscapingTest.this.current + size);
					try {
						getService()
						.verifyStringContainingCharacterRange(
								UnicodeEscapingTest.this.current,
								blockEnd,
								getStringContainingCharacterRange(
										UnicodeEscapingTest.this.current,
										blockEnd), this);
					} catch (InvalidCharacterException e) {
						this.fails.add(e);
					}
				} else if (!this.fails.isEmpty()) {
					StringBuilder msg = new StringBuilder();
					for (Throwable t : this.fails) {
						msg.append(t.getMessage()).append("\n");
					}
					TestSetValidator
					.rethrowException(new RuntimeException(msg
							.toString()));
				} else {
					finishTest();
				}
			}
		});
	}

	/**
	 * Requests strings of CHARACTER_RANGE_SIZE from the server and validates
	 * that the returned string length matches CHARACTER_RANGE_SIZE and that all
	 * of the characters remain intact. This test checks the range of surrogate
	 * characters, which are used to encode non-BMP characters as pairs of UTF16
	 * characters.
	 *
	 * Note that this does not test all possible combinations.
	 */
	@DoNotRunWith(Platform.HtmlUnitBug)
	// TODO(jat): decide if we really want to specify this behavior since some
	// browsers and OOPHM plugins have issues with it -- disabled for now
	public void disabled_testServerToClientBMPSurrogates() {
		setRpcTimeout(BMPTimeout);
		delayTestFinishForRpc();
		serverToClientVerify(Character.MIN_SURROGATE,
				Character.MIN_SUPPLEMENTARY_CODE_POINT, CHARACTER_BLOCK_SIZE,
				CHARACTER_BLOCK_SIZE);
	}

	/**
	 * Verify that string encoding/decoding is lossless.
	 */
	private void echoVerify(final String str) {
		setRpcTimeout(BMPTimeout);
		delayTestFinishForRpc();
		getService().echo(str, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(String result) {
				assertEquals(str, result);
				finishTest();
			}
		});
	}

	@Override
	public String getModuleName() {
		return "com.google.gwt.user.RPCSuite";
	}

	private UnicodeEscapingServiceAsync getService() {
		UnicodeEscapingServiceAsync service = SyncProxy
				.create(UnicodeEscapingService.class);
		((ServiceDefTarget) service).setServiceEntryPoint(getModuleBaseURL()
				+ "unicodeEscape");
		return service;
	}

	protected void serverToClientVerify(final int start, final int end,
			final int size, final int step) {
		this.current = start;
		getService().getStringContainingCharacterRange(start,
				Math.min(end, this.current + size),
				new AsyncCallback<String>() {
			List<Throwable> fails = new ArrayList<Throwable>();

			private void nextBatch() {
				UnicodeEscapingTest.this.current += step;
				System.out.println("Unicode Progress: "
								+ UnicodeEscapingTest.this.current + "/" + end);
				if (UnicodeEscapingTest.this.current < end) {
					// setRpcTimeout(BMPTimeout);
					// delayTestFinishForRpc();
					getService().getStringContainingCharacterRange(
							UnicodeEscapingTest.this.current,
							Math.min(end,
									UnicodeEscapingTest.this.current
									+ size), this);
				} else if (!this.fails.isEmpty()) {
					StringBuilder msg = new StringBuilder();
					for (Throwable t : this.fails) {
						msg.append(t.getMessage()).append("\n");
					}
					TestSetValidator
					.rethrowException(new RuntimeException(msg
							.toString()));
				} else {
					finishTest();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				this.fails.add(caught);
				nextBatch();
			}

			@Override
			public void onSuccess(String str) {
				try {
					verifyStringContainingCharacterRange(
							UnicodeEscapingTest.this.current, Math.min(
									end,
									UnicodeEscapingTest.this.current
									+ size), str);
				} catch (InvalidCharacterException e) {
					this.fails.add(e);
				}
				nextBatch();
			}
		});
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		SyncProxy.suppressRelativePathWarning(true);
	}

	/**
	 * Generate strings containing ranges of characters and sends them to the
	 * server for verification. This ensures that client->server string escaping
	 * properly handles all BMP characters.
	 *
	 * Unpaired or improperly paired surrogates are not tested here, as some
	 * browsers refuse to accept them. Properly paired surrogates are tested in
	 * the non-BMP test.
	 *
	 * Note that this does not test all possible combinations, which might be an
	 * issue, particularly with combining marks, though they should be logically
	 * equivalent in that case.
	 *
	 * @throws InvalidCharacterException
	 */
	public void testClientToServerBMPHigh() throws InvalidCharacterException {
		setRpcTimeout(BMPTimeout);
		delayTestFinishForRpc();
		clientToServerVerifyRange(Character.MAX_SURROGATE + 1,
				Character.MIN_SUPPLEMENTARY_CODE_POINT, CHARACTER_BLOCK_SIZE,
				CHARACTER_BLOCK_SIZE);
	}

	/**
	 * Generate strings containing ranges of characters and sends them to the
	 * server for verification. This ensures that client->server string escaping
	 * properly handles all BMP characters.
	 *
	 * Unpaired or improperly paired surrogates are not tested here, as some
	 * browsers refuse to accept them. Properly paired surrogates are tested in
	 * the non-BMP test.
	 *
	 * Note that this does not test all possible combinations, which might be an
	 * issue, particularly with combining marks, though they should be logically
	 * equivalent in that case.
	 *
	 * @throws InvalidCharacterException
	 */
	public void testClientToServerBMPLow() throws InvalidCharacterException {
		setRpcTimeout(BMPTimeout);
		delayTestFinishForRpc();
		clientToServerVerifyRange(Character.MIN_CODE_POINT,
				Character.MIN_SURROGATE, CHARACTER_BLOCK_SIZE,
				CHARACTER_BLOCK_SIZE);
	}

	/**
	 * Generate strings containing ranges of characters and sends them to the
	 * server for verification. This ensures that client->server string escaping
	 * properly handles all non-BMP characters.
	 *
	 * Note that this does not test all possible combinations, which might be an
	 * issue, particularly with combining marks, though they should be logically
	 * equivalent in that case.
	 *
	 * @throws InvalidCharacterException
	 */
	public void testClientToServerNonBMP() throws InvalidCharacterException {
		setRpcTimeout(BMPTimeout);
		delayTestFinishForRpc();
		clientToServerVerifyRange(Character.MIN_SUPPLEMENTARY_CODE_POINT,
				Character.MAX_CODE_POINT + 1, CHARACTER_BLOCK_SIZE,
				NON_BMP_TEST_INCREMENT);
	}

	/**
	 * Test that HTML special characters are encoded correctly.
	 */
	public void testEscapeHtml() {
		echoVerify("<img src=x onerror=alert(1)>");
	}

	/**
	 * Test that a NUL character followed by an octal character is encoded
	 * correctly. Encoding the NUL character simply as "\0" in this case would
	 * cause the recipient to see "\07" as a single octal escape sequence,
	 * rather than two separate characters.
	 */
	public void testEscapeNull() {
		echoVerify("\u0000" + "7"); // split to emphasize two characters
	}

	/**
	 * Requests strings of CHARACTER_RANGE_SIZE from the server and validates
	 * that the returned string length matches CHARACTER_RANGE_SIZE and that all
	 * of the characters remain intact.
	 *
	 * Note that this does not test all possible combinations, which might be an
	 * issue, particularly with combining marks, though they should be logically
	 * equivalent in that case. Surrogate characters are also not tested here,
	 * see {@link #disabled_testServerToClientBMPSurrogates()}.
	 */
	public void testServerToClientBMP() {
		setRpcTimeout(BMPTimeout);
		delayTestFinishForRpc();
		serverToClientVerify(Character.MIN_CODE_POINT, Character.MIN_SURROGATE,
				CHARACTER_BLOCK_SIZE, CHARACTER_BLOCK_SIZE);
	}

	/**
	 * Requests strings of CHARACTER_RANGE_SIZE from the server and validates
	 * that the returned string length matches CHARACTER_RANGE_SIZE and that all
	 * of the characters remain intact. Note that this test verifies non-BMP
	 * characters (ie, those which are represented as pairs of surrogates).
	 *
	 * Note that this does not test all possible combinations, which might be an
	 * issue, particularly with combining marks, though they should be logically
	 * equivalent in that case.
	 */
	public void testServerToClientNonBMP() {
		setRpcTimeout(BMPTimeout);
		delayTestFinishForRpc();
		serverToClientVerify(Character.MIN_SUPPLEMENTARY_CODE_POINT,
				Character.MAX_CODE_POINT + 1, CHARACTER_BLOCK_SIZE,
				NON_BMP_TEST_INCREMENT);
	}
}
