package com.gdevelop.gwt.syncrpc.android;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class BridgeCallback<ReturnType> implements AsyncCallback<ReturnType> {
	Throwable caught;
	ReturnType result;
	boolean success = false;
	CountDownLatch latch = new CountDownLatch(1);

	public boolean wasSuccessful() {
		return success;
	}

	public Throwable getCaught() {
		return caught;
	}

	public ReturnType getResult() {
		return result;
	}

	public void await() throws InterruptedException {
		latch.await(15, TimeUnit.SECONDS);
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
