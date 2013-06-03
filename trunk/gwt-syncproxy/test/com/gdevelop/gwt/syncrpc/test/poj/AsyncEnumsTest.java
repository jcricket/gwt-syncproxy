package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestServiceAsync;

public class AsyncEnumsTest extends TestCase {

	private static EnumsTestServiceAsync service = (EnumsTestServiceAsync) SyncProxy
			.newProxyInstance(EnumsTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "rpcsuite/enums", true);

	public AsyncEnumsTest() {
	}

	public void testBasicEnums() throws Throwable {
		assertTrue("Server Fails for Enum IsSerializable", false);
		final CountDownLatch signal = new CountDownLatch(1);
		service.echo(Basic.A, new AsyncCallback<Basic>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Basic result) {
				assertEquals(Basic.A, result);
				signal.countDown();
			}
		});
		assertTrue("Failed to complete", signal.await(2, TimeUnit.SECONDS));
	}
}
