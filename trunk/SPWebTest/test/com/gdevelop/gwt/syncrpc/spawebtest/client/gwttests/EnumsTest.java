package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.EnumsTestService;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestService.Complex;
import com.google.gwt.user.client.rpc.EnumsTestService.Subclassing;
import com.google.gwt.user.client.rpc.EnumsTestServiceAsync;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class EnumsTest extends GWTTestCase {
	private static EnumsTestServiceAsync service;

	public EnumsTest() {
	}

	public void testBasicEnums() {
		service = GWT.create(EnumsTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/rpcsuite/enums");
		service.echo(Basic.A, new AsyncCallback<Basic>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Basic result) {
				assertEquals(Basic.A, result);
				finishTest();
			}
		});
		delayTestFinish(2000);
	}

	public void testComplexEnums() {
		service = GWT.create(EnumsTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/rpcsuite/enums");
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
				finishTest();
			}
		});
		delayTestFinish(2000);
	}

	public void testNull() {
		service = GWT.create(EnumsTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/rpcsuite/enums");
		service.echo((Basic) null, new AsyncCallback<Basic>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Basic result) {
				assertNull(result);
				finishTest();
			}
		});
		delayTestFinish(2000);
	}

	public void testSubclassingEnums() {
		service = GWT.create(EnumsTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/rpcsuite/enums");
		service.echo(Subclassing.A, new AsyncCallback<Subclassing>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Subclassing result) {
				assertEquals(Subclassing.A, result);
				finishTest();
			}
		});
		delayTestFinish(2000);
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}
}
