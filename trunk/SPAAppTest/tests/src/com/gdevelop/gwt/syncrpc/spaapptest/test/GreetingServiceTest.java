/**
 * Jan 1, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.spaapptest.test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingServiceAsync;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.T1;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class GreetingServiceTest extends
AndroidGWTTestCase<GreetingServiceAsync> {
	GreetingServiceAsync greetingService;

	public GreetingServiceTest() {
		super(MainActivity.class);
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				GreetingServiceTest.this.greetingService = SyncProxy
						.create(GreetingService.class);
				arg0[0].countDown();
				return null;
			}
		});
	}

	protected GreetingServiceAsync getAsyncService() {

		return this.greetingService;
	}

	/**
	 * This test will send a request to the server using the greetServer method
	 * in GreetingService and verify the response.
	 */
	public void testGreetingService() {
		// Create the service that we will test.
		final GreetingServiceAsync greetingService = getAsyncService();

		// Since RPC calls are asynchronous, we will need to wait for a response
		// after this test method returns. This line tells the test runner to
		// wait
		// up to 10 seconds before timing out.
		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
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
						assertTrue(result
								.contains(GreetingService.NAME));

						// Now that we have received a response, we need
								// to tell
						// the
						// test runner
						// that the test is complete. You must call
								// finishTest()
						// after
						// an
						// asynchronous test finishes successfully, or
								// the test
						// will
						// time out.
						finishTest();
					}
				});
				return null;
			}

		});
	}

	public void testGreetingService2() {
		final GreetingServiceAsync greetingService = getAsyncService();
		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
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
						assertTrue(result.getText().equals(
								GreetingService.NAME));
						finishTest();
					}
				});
				return null;
			}

		});
	}

	public void testGreetingServiceArray() {
		final GreetingServiceAsync greetingService = getAsyncService();
		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
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
						assertTrue(result.get(0).equals(
								GreetingService.NAME));
						finishTest();
					}
				});
				return null;
			}

		});
	}
}
