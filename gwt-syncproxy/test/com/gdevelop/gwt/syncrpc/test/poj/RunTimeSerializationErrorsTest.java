package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.MixedSerializable;
import com.google.gwt.user.client.rpc.MixedSerializableEchoServiceAsync;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class RunTimeSerializationErrorsTest extends TestCase {
	private static MixedSerializableEchoServiceAsync service = (MixedSerializableEchoServiceAsync) SyncProxy
			.newProxyInstance(MixedSerializableEchoServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "echo", true);

	public RunTimeSerializationErrorsTest() {
	}

	public void testBadSerialization1() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		service.echoVoid(new MixedSerializable.NonSerializableSub(),
				new AsyncCallback<MixedSerializable>() {

					@Override
					public void onFailure(Throwable caught) {
						signal.countDown();
					}

					@Override
					public void onSuccess(MixedSerializable result) {
						fail("RPC request should have failed");
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testBadSerialization2() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		service.echoRequest(new MixedSerializable.NonSerializableSub(),
				new AsyncCallback<MixedSerializable>() {

					@Override
					public void onFailure(Throwable caught) {
						signal.countDown();
					}

					@Override
					public void onSuccess(MixedSerializable result) {
						fail("RPC request should have failed");
					}

				});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testGoodSerialization1() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		final MixedSerializable.SerializableSub expected = new MixedSerializable.SerializableSub();

		service.echoVoid(expected, new AsyncCallback<MixedSerializable>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(MixedSerializable result) {
				assertNotNull(result);
				signal.countDown();
			}

		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void xtestBadSerialization3() throws RequestException {
		// empty
	}
}
