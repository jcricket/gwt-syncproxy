package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.MixedSerializable;
import com.google.gwt.user.client.rpc.MixedSerializableEchoService;
import com.google.gwt.user.client.rpc.MixedSerializableEchoServiceAsync;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Base on com.google.gwt.user.client.rpc.*Test
 */
public class RunTimeSerializationErrorsTest extends GWTTestCase {
	private static MixedSerializableEchoServiceAsync service;

	public RunTimeSerializationErrorsTest() {
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	public void testBadSerialization1() {
		service = GWT.create(MixedSerializableEchoService.class);
		delayTestFinish(2000);
		service.echoVoid(new MixedSerializable.NonSerializableSub(),
				new AsyncCallback<MixedSerializable>() {

					@Override
					public void onFailure(Throwable caught) {
						finishTest();
					}

					@Override
					public void onSuccess(MixedSerializable result) {
						fail("RPC request should have failed");
					}

				});

	}

	public void testBadSerialization2() {
		service = GWT.create(MixedSerializableEchoService.class);
		delayTestFinish(2000);
		service.echoRequest(new MixedSerializable.NonSerializableSub(),
				new AsyncCallback<MixedSerializable>() {

					@Override
					public void onFailure(Throwable caught) {
						finishTest();
					}

					@Override
					public void onSuccess(MixedSerializable result) {
						fail("RPC request should have failed");
					}

				});

	}

	public void testGoodSerialization1() {
		service = GWT.create(MixedSerializableEchoService.class);
		final MixedSerializable.SerializableSub expected = new MixedSerializable.SerializableSub();
		ServiceDefTarget serTarget = (ServiceDefTarget) service;
		serTarget.setServiceEntryPoint("/spawebtest/echo");
		service.echoVoid(expected, new AsyncCallback<MixedSerializable>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(MixedSerializable result) {
				assertNotNull(result);
				finishTest();
			}

		});
		delayTestFinish(2000);
	}

	public void xtestBadSerialization3() throws RequestException {
		// empty
	}
}
