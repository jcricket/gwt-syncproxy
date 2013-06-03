package com.gdevelop.gwt.syncrpc.test.poj;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CoreJavaTestService.CoreJavaTestServiceException;
import com.google.gwt.user.client.rpc.CoreJavaTestServiceAsync;

public class CoreJavaTest extends TestCase {
	private static CoreJavaTestServiceAsync service = (CoreJavaTestServiceAsync) SyncProxy
			.newProxyInstance(CoreJavaTestServiceAsync.class,
					"http://127.0.0.1:8888/spawebtest/", "corejava", true);

	private static MathContext createMathContext() {
		return new MathContext(5, RoundingMode.CEILING);
	}

	public static boolean isValid(MathContext value) {
		return createMathContext().equals(value);
	}

	public CoreJavaTest() {
	}

	public void testMathContext() throws CoreJavaTestServiceException,
			InterruptedException {
		final MathContext expected = createMathContext();
		final CountDownLatch signal = new CountDownLatch(1);
		service.echoMathContext(expected, new AsyncCallback<MathContext>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(MathContext result) {
				assertNotNull(result);
				assertTrue(isValid(result));
				signal.countDown();
			}

		});
		assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS));
	}

}
