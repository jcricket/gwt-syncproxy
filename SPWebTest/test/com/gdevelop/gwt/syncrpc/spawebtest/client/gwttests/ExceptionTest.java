package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ExceptionsTestService;
import com.google.gwt.user.client.rpc.ExceptionsTestServiceAsync;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetValidator;

public class ExceptionTest extends GWTTestCase {
	private static ExceptionsTestServiceAsync service;

	public ExceptionTest() {
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	public void testException() {
		final UmbrellaException expected = TestSetFactory
				.createUmbrellaException();

		service = GWT.create(ExceptionsTestService.class);
		service.echo(expected, new AsyncCallback<UmbrellaException>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(UmbrellaException result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.isValid(expected, result));
				finishTest();
			}
		});
		delayTestFinish(2000);
	}
}
