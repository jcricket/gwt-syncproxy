package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ExceptionsTestServiceAsync;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetValidator;

public class ExceptionTest extends TestCase {
	private static ExceptionsTestServiceAsync service = (ExceptionsTestServiceAsync) SyncProxy
			.newProxyInstance(ExceptionsTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "exceptions", true);;

	public ExceptionTest() {
	}

	public void testException() throws InterruptedException {
		assertTrue(
				"Failed due to UmbrellaException not IsSerializable on server",
				false);
		final UmbrellaException expected = TestSetFactory
				.createUmbrellaException();

		final CountDownLatch signal = new CountDownLatch(1);
		service.echo(expected, new AsyncCallback<UmbrellaException>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(UmbrellaException result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.isValid(expected, result));
				signal.countDown();
			}
		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}
}
