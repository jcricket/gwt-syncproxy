/**
 * Jan 1, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.spaapptest.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadServiceAsync;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.T1;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class SyncedServicesTest extends
		AndroidGWTTestCase<LargePayloadServiceAsync> {
	GreetingService greetingService;
	LargePayloadService payloadService;

	public SyncedServicesTest() {
		super(MainActivity.class);
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				SyncedServicesTest.this.payloadService = SyncProxy
						.createSync(LargePayloadService.class);
				SyncedServicesTest.this.greetingService = SyncProxy
						.createSync(GreetingService.class);
				arg0[0].countDown();
				return null;
			}
		});
	}

	protected GreetingService getGreetingSyncService() {
		return this.greetingService;
	}

	protected LargePayloadService getLargePayloadSyncService() {
		return this.payloadService;
	}

	public void testGreetingService() {
		// Create the service that we will test.
		final GreetingService greetingService = getGreetingSyncService();
		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				// Send a request to the server.
				String result = greetingService
						.greetServer(GreetingService.NAME);
				assertTrue(result.contains(GreetingService.NAME));
				return null;
			}

		});

	}

	public void testGreetingService2() {
		final GreetingService greetingService = getGreetingSyncService();
		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				// Send a request to the server.
				T1 result = greetingService.greetServer2(GreetingService.NAME);
				// Verify that the response is correct.
				assertTrue(result.getText().equals(GreetingService.NAME));
				return null;
			}

		});

	}

	public void testGreetingServiceArray() {
		final GreetingService greetingService = getGreetingSyncService();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				ArrayList<String> result = greetingService
						.greetServerArr(GreetingService.NAME);
				// Verify that the response is correct.
				assertTrue(result.get(0).equals(GreetingService.NAME));
				return null;
			}

		});
	}

	public void testLargeResponseArray() {
		final LargePayloadService service = getLargePayloadSyncService();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				int[] result = service.testLargeResponseArray();
				assertEquals("Wrong array size",
						LargePayloadService.ARRAY_SIZE, result.length);
				return null;
			}

		});
	}

	public void testLargeResponsePayload() {
		final LargePayloadService service = getLargePayloadSyncService();

		setTask(new AsyncTask<Void, Void, Void>() {
			@Override
			public Void doInBackground(Void... v) {
				List<UserInfo> result = service.testLargeResponsePayload();
				assertEquals("Wrong list size",
						LargePayloadService.PAYLOAD_SIZE, result.size());
				return null;
			}

		});
	}
}
