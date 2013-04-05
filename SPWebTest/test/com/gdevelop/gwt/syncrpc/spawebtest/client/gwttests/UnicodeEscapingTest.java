package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.UnicodeEscapingService;
import com.google.gwt.user.client.rpc.UnicodeEscapingService.InvalidCharacterException;
import com.google.gwt.user.client.rpc.UnicodeEscapingServiceAsync;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class UnicodeEscapingTest extends GWTTestCase {
	/** the size of a block of characters to test */
	private static final int CHARACTER_BLOCK_SIZE = 64;
	/**
	 * When doing the non-BMP test, we don't test every block of characters
	 * because it takes too long - this is the increment to use. It is not a
	 * power of two so we alter the alignment of the block of characters we
	 * skip.
	 */
	private static final int NON_BMP_TEST_INCREMENT = 8192 + 64;
	private static UnicodeEscapingServiceAsync service;

	/** start of current block being tested */
	private int current;

	public UnicodeEscapingTest() {
	}

	private void clientToServerVerifyRange(final int start, final int end,
			final int size, final int step) throws InvalidCharacterException {
		current = start;
		service = GWT.create(UnicodeEscapingService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/unicodeEscape");
		while (current < end) {
			int blockEnd = Math.min(end, current + size);

			String data = com.google.gwt.user.client.rpc.UnicodeEscapingTest
					.getStringContainingCharacterRange(current, blockEnd);
			service.verifyStringContainingCharacterRange(current, blockEnd,
					data, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(Boolean result) {
							assertTrue(result);
							finishTest();
						}

					});
			delayTestFinish(2000);
			current += step;
		}
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	private void serverToClientVerify(final int start, final int end,
			final int size, final int step) throws InvalidCharacterException {
		current = start;
		service = GWT.create(UnicodeEscapingService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/unicodeEscape");
		while (current < end) {
			final int blockEnd = Math.min(end, current + size);
			service.getStringContainingCharacterRange(current, blockEnd,
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(String result) {
							try {
								com.google.gwt.user.client.rpc.UnicodeEscapingTest
										.verifyStringContainingCharacterRange(
												current, blockEnd, result);
							} catch (InvalidCharacterException e) {
								e.printStackTrace();
							}
							finishTest();
						}

					});
			delayTestFinish(2000);
			current += step;
		}
	}

	public void testClientToServerBMP() throws InvalidCharacterException {
		clientToServerVerifyRange(Character.MIN_CODE_POINT,
				Character.MIN_SURROGATE, 1, CHARACTER_BLOCK_SIZE);
		clientToServerVerifyRange(Character.MAX_SURROGATE + 1,
				Character.MIN_SUPPLEMENTARY_CODE_POINT, CHARACTER_BLOCK_SIZE,
				CHARACTER_BLOCK_SIZE);
	}

	public void testClientToServerBMPHigh() throws InvalidCharacterException {
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

	public void testServerToClientNonBMP() throws InvalidCharacterException {
		serverToClientVerify(Character.MIN_SUPPLEMENTARY_CODE_POINT,
				Character.MAX_CODE_POINT + 1, CHARACTER_BLOCK_SIZE,
				NON_BMP_TEST_INCREMENT);
	}
}
