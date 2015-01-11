package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ValueTypesTestServiceAsync;

public class HttpsTest extends TestCase {
	private static ValueTypesTestServiceAsync service = SyncProxy
			.newProxyInstance(ValueTypesTestServiceAsync.class,
					"https://127.0.0.1:8888/spawebtest/", "valuetypes", true);

	public HttpsTest() {

	}

	public void testBoolean_FALSE() throws InterruptedException {
		fail("Fails with SSL Error Plain Text Issue");
		final CountDownLatch signal = new CountDownLatch(1);
		service.echo_FALSE(false, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(Boolean result) {
				assertNotNull(result);
				assertFalse(result.booleanValue());
			}

		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}
}
