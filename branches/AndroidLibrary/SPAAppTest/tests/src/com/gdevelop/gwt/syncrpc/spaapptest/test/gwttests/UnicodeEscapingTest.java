package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.UnicodeEscapingService.InvalidCharacterException;
import com.google.gwt.user.client.rpc.UnicodeEscapingServiceAsync;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class UnicodeEscapingTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	/** the size of a block of characters to test */
	private static final int CHARACTER_BLOCK_SIZE = 64;
	/**
	 * When doing the non-BMP test, we don't test every block of characters
	 * because it takes too long - this is the increment to use. It is not a
	 * power of two so we alter the alignment of the block of characters we
	 * skip.
	 */
	private static final int NON_BMP_TEST_INCREMENT = 8192 + 64;
	UnicodeEscapingServiceAsync service;

	/** start of current block being tested */
	private int current;

	public UnicodeEscapingTest() throws InterruptedException {
		super(MainActivity.class);
		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (UnicodeEscapingServiceAsync) SyncProxy
						.newProxyInstance(UnicodeEscapingServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/",
								"unicodeEscape", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(20, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
	}

	private void clientToServerVerifyRange(final int start, final int end,
			final int size, final int step) throws InvalidCharacterException,
			InterruptedException {
		current = start;
		final CountDownLatch signal = new CountDownLatch(1);

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
							signal.countDown();
						}

					});
			assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
			current += step;
		}
	}

	private void serverToClientVerify(final int start, final int end,
			final int size, final int step) throws InvalidCharacterException,
			InterruptedException {
		current = start;
		final CountDownLatch signal = new CountDownLatch(1);

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
							signal.countDown();
						}

					});
			assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
			current += step;
		}
	}

	public void testClientToServerBMP() throws InvalidCharacterException,
			InterruptedException {
		clientToServerVerifyRange(Character.MIN_CODE_POINT,
				Character.MIN_SURROGATE, 1, CHARACTER_BLOCK_SIZE);
		clientToServerVerifyRange(Character.MAX_SURROGATE + 1,
				Character.MIN_SUPPLEMENTARY_CODE_POINT, CHARACTER_BLOCK_SIZE,
				CHARACTER_BLOCK_SIZE);
	}

	public void testClientToServerBMPHigh() throws InvalidCharacterException,
			InterruptedException {
		clientToServerVerifyRange(Character.MAX_SURROGATE + 1,
				Character.MIN_SUPPLEMENTARY_CODE_POINT, CHARACTER_BLOCK_SIZE,
				CHARACTER_BLOCK_SIZE);
	}

	public void testClientToServerNonBMP() throws InvalidCharacterException,
			InterruptedException {
		clientToServerVerifyRange(Character.MIN_SUPPLEMENTARY_CODE_POINT,
				Character.MAX_CODE_POINT + 1, CHARACTER_BLOCK_SIZE,
				NON_BMP_TEST_INCREMENT);
	}

	public void testServerToClientBMP() throws InvalidCharacterException,
			InterruptedException {
		serverToClientVerify(Character.MIN_CODE_POINT,
				Character.MIN_SUPPLEMENTARY_CODE_POINT, CHARACTER_BLOCK_SIZE,
				CHARACTER_BLOCK_SIZE);
	}

	public void testServerToClientNonBMP() throws InvalidCharacterException,
			InterruptedException {
		serverToClientVerify(Character.MIN_SUPPLEMENTARY_CODE_POINT,
				Character.MAX_CODE_POINT + 1, CHARACTER_BLOCK_SIZE,
				NON_BMP_TEST_INCREMENT);
	}
}
