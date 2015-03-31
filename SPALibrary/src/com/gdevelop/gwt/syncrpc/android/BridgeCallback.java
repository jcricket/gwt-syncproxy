package com.gdevelop.gwt.syncrpc.android;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * An intermediary AsyncCallback which a creator and use to hold processing
 * (using a {@link CountDownLatch}) until the service returns, with
 * {@link #onSuccess(Object)} or {@link #onFailure(Throwable)}. The default
 * timeout is 15 seconds.
 *
 * @author Preethum
 * @since 0.6
 * @param <ReturnType>
 */
public class BridgeCallback<ReturnType> implements AsyncCallback<ReturnType> {
	Throwable caught;
	ReturnType result;
	boolean success = false;
	CountDownLatch latch = new CountDownLatch(1);
	public static final int DEFAULT_TIMEOUT = 15;
	int timeout = DEFAULT_TIMEOUT;

	/**
	 * Set the timeout in seconds
	 *
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean wasSuccessful() {
		return success;
	}

	public Throwable getCaught() {
		return caught;
	}

	public ReturnType getResult() {
		return result;
	}

	/**
	 * Waits for the {@link CountDownLatch} to countdown or for the timeout to
	 * occur.
	 *
	 * @throws InterruptedException
	 */
	public void await() throws InterruptedException {
		latch.await(timeout, TimeUnit.SECONDS);
	}

	@Override
	public void onFailure(Throwable caught) {
		this.caught = caught;
		latch.countDown();
	}

	@Override
	public void onSuccess(ReturnType result) {
		this.result = result;
		success = true;
		latch.countDown();
	}
}
