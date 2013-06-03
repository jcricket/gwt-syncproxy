package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CollectionsTestService;
import com.google.gwt.user.client.rpc.CollectionsTestServiceAsync;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeArrayList;
import com.google.gwt.user.client.rpc.TestSetValidator;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class CollectionsCallbackTest extends GWTTestCase {

	public void testArrayList() {
		CollectionsTestServiceAsync service = (CollectionsTestServiceAsync) GWT
				.create(CollectionsTestService.class);
		service.echo(TestSetFactory.createArrayList(),
				new AsyncCallback<ArrayList<MarkerTypeArrayList>>() {
					@Override
					public void onFailure(Throwable caught) {
						fail(caught.getMessage());
					}

					@Override
					public void onSuccess(ArrayList<MarkerTypeArrayList> result) {
						assertNotNull(result);
						assertTrue(TestSetValidator.isValid(result));
						finishTest();
					}
				});
		delayTestFinish(2000);
	}

	public void testBooleanArray() {
		CollectionsTestServiceAsync service = (CollectionsTestServiceAsync) GWT
				.create(CollectionsTestService.class);
		final Boolean[] expected = TestSetFactory.createBooleanArray();
		service.echo(expected, new AsyncCallback<Boolean[]>() {
			@Override
			public void onFailure(Throwable caught) {
				fail(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean[] result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.equals(expected, result));
				finishTest();
			}
		});
		delayTestFinish(1000);
	}

	@Override
	public String getModuleName() {
		// ServiceDefTarget serTarget = (ServiceDefTarget) service;
		// serTarget.setServiceEntryPoint("/spawebtest/rpcsuite/enums");
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}
}
