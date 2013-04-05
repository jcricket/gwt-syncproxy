package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.EnumsTestService;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestServiceAsync;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class AsyncEnumsTest extends GWTTestCase {

	public AsyncEnumsTest() {
	}

	public void testBasicEnums() throws Throwable {
		EnumsTestServiceAsync service = GWT.create(EnumsTestService.class);
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/rpcsuite/enums");
		service.echo(Basic.A, new AsyncCallback<Basic>() {
			@Override
			public void onFailure(Throwable caught) {
				fail(caught.getMessage());
			}

			@Override
			public void onSuccess(Basic result) {
				assertEquals(Basic.A, result);
				finishTest();
			}
		});
		delayTestFinish(2000);
	}

	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}
}
