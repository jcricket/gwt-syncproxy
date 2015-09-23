package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.util.ArrayList;

import com.gdevelop.gwt.syncrpc.spawebtest.shared.T1;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcTestBase;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class GreetingServiceTest extends RpcTestBase {

	protected GreetingServiceAsync getAsyncService() {
		GreetingServiceAsync greetingService = GWT
				.create(GreetingService.class);
		ServiceDefTarget target = (ServiceDefTarget) greetingService;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "spawebtest/greet");
		return greetingService;
	}

	/**
	 * Must refer to a valid module that sources this class.
	 */
	@Override
	public String getModuleName() {
		return "com.gdevelop.gwt.syncrpc.spawebtest.SPAGWTTest";
	}

	/**
	 * This test will send a request to the server using the greetServer method
	 * in GreetingService and verify the response.
	 */
	public void testGreetingService() {
		// Create the service that we will test.
		GreetingServiceAsync greetingService = getAsyncService();

		// Since RPC calls are asynchronous, we will need to wait for a response
		// after this test method returns. This line tells the test runner to
		// wait
		// up to 10 seconds before timing out.
		delayTestFinishForRpc();

		// Send a request to the server.
		greetingService.greetServer(GreetingService.NAME,
				new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// The request resulted in an unexpected error.
				fail("Request failure: " + caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				// Verify that the response is correct.
				assertTrue(result.contains(GreetingService.NAME));

				// Now that we have received a response, we need to tell
				// the
				// test runner
				// that the test is complete. You must call finishTest()
				// after
				// an
				// asynchronous test finishes successfully, or the test
				// will
				// time out.
				finishTest();
			}
		});
	}

	public void testGreetingService2() {
		GreetingServiceAsync greetingService = getAsyncService();
		delayTestFinishForRpc();

		// Send a request to the server.
		greetingService.greetServer2(GreetingService.NAME,
				new AsyncCallback<T1>() {
					@Override
					public void onFailure(Throwable caught) {
						// The request resulted in an unexpected error.
						fail("Request failure: " + caught.getMessage());
					}

					@Override
					public void onSuccess(T1 result) {
						// Verify that the response is correct.
						assertTrue(result.getText()
								.equals(GreetingService.NAME));
						finishTest();
					}
				});
	}

	public void testGreetingServiceArray() {
		GreetingServiceAsync greetingService = getAsyncService();
		delayTestFinishForRpc();

		// Send a request to the server.
		greetingService.greetServerArr(GreetingService.NAME,
				new AsyncCallback<ArrayList<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						// The request resulted in an unexpected error.
						fail("Request failure: " + caught.getMessage());
					}

					@Override
					public void onSuccess(ArrayList<String> result) {
						// Verify that the response is correct.
						assertTrue(result.get(0).equals(GreetingService.NAME));
						finishTest();
					}
				});
	}
}