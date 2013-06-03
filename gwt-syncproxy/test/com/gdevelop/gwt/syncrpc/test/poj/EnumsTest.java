package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestService.Complex;
import com.google.gwt.user.client.rpc.EnumsTestService.Subclassing;
import com.google.gwt.user.client.rpc.EnumsTestServiceAsync;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class EnumsTest extends TestCase {
	private static EnumsTestServiceAsync service = (EnumsTestServiceAsync) SyncProxy
			.newProxyInstance(EnumsTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "enums", true);

	public EnumsTest() {
	}

	public void testBasicEnums() throws InterruptedException {
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
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testComplexEnums() throws InterruptedException {
		assertTrue("Server Fails for Enum IsSerializable", false);
		final CountDownLatch signal = new CountDownLatch(1);

		Complex a = Complex.A;
		a.value = "client";

		service.echo(Complex.A, new AsyncCallback<Complex>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Complex result) {
				assertEquals(Complex.A, result);
				assertEquals("client", result.value);
				signal.countDown();
			}
		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testNull() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);

		service.echo((Basic) null, new AsyncCallback<Basic>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Basic result) {
				assertNull(result);
				signal.countDown();
			}
		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

	public void testSubclassingEnums() throws InterruptedException {
		assertTrue("Server Fails for Enum IsSerializable", false);
		final CountDownLatch signal = new CountDownLatch(1);

		service.echo(Subclassing.A, new AsyncCallback<Subclassing>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Subclassing result) {
				assertEquals(Subclassing.A, result);
				signal.countDown();
			}
		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

}
