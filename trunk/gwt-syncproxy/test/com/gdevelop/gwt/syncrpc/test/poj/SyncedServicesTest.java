/**
 * Dec 31, 2014 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.test.poj;

import java.util.ArrayList;
import java.util.List;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadService;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.T1;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.user.client.rpc.RpcTestBase;

/**
 *
 * Tests the custom POJ services in a synchronized format
 *
 * @author Preethum
 * @since 0.5
 *
 */
public class SyncedServicesTest extends RpcTestBase {
	protected GreetingService getGreetingSyncService() {
		GreetingService service = SyncProxy.createSync(GreetingService.class);
		return service;
	}

	protected LargePayloadService getLargePayloadSyncService() {
		LargePayloadService service = SyncProxy
				.createSync(LargePayloadService.class);
		return service;
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		SyncProxy.setBaseURL(getModuleBaseURL());
	}

	public void testGreetingService() {
		// Create the service that we will test.
		GreetingService greetingService = getGreetingSyncService();
		// Send a request to the server.
		String result = greetingService.greetServer(GreetingService.NAME);

		assertTrue(result.contains(GreetingService.NAME));

	}

	public void testGreetingService2() {
		GreetingService greetingService = getGreetingSyncService();
		// Send a request to the server.
		T1 result = greetingService.greetServer2(GreetingService.NAME);
		// Verify that the response is correct.
		assertTrue(result.getText().equals(GreetingService.NAME));

	}

	public void testGreetingServiceArray() {
		GreetingService greetingService = getGreetingSyncService();

		ArrayList<String> result = greetingService
				.greetServerArr(GreetingService.NAME);
		// Verify that the response is correct.
		assertTrue(result.get(0).equals(GreetingService.NAME));
	}

	public void testLargeResponseArray() {
		LargePayloadService service = getLargePayloadSyncService();

		int[] result = service.testLargeResponseArray();
		assertEquals("Wrong array size", LargePayloadService.ARRAY_SIZE,
				result.length);
	}

	public void testLargeResponsePayload() {
		LargePayloadService service = getLargePayloadSyncService();

		List<UserInfo> result = service.testLargeResponsePayload();
		assertEquals("Wrong list size", LargePayloadService.PAYLOAD_SIZE,
				result.size());
	}
}
