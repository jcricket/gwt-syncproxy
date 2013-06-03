package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import java.math.MathContext;
import java.math.RoundingMode;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CoreJavaTestService;
import com.google.gwt.user.client.rpc.CoreJavaTestService.CoreJavaTestServiceException;
import com.google.gwt.user.client.rpc.CoreJavaTestServiceAsync;

public class CoreJavaTest extends GWTTestCase {
	private static CoreJavaTestServiceAsync service;

	private static MathContext createMathContext() {
		return new MathContext(5, RoundingMode.CEILING);
	}

	public static boolean isValid(MathContext value) {
		return createMathContext().equals(value);
	}

	public CoreJavaTest() {
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	public void testMathContext() throws CoreJavaTestServiceException {
		final MathContext expected = createMathContext();
		service = GWT.create(CoreJavaTestService.class);
		service.echoMathContext(expected, new AsyncCallback<MathContext>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(MathContext result) {
				assertNotNull(result);
				assertTrue(isValid(result));
				finishTest();
			}

		});
		delayTestFinish(2000);
	}
}
