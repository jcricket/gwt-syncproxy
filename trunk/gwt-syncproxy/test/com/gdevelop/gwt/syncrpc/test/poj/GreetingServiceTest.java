/**
 * Dec 31, 2014 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.test.poj;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingServiceAsync;

/**
 * POJ Test of the Greeting Service. Tests are run from heirarchy with modified
 * RpcTestBase and Service
 *
 * @author Preethum
 * @since 0.5
 *
 */
public class GreetingServiceTest extends
		com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingServiceTest {
	/**
	 * @see com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadTest#getAsyncService()
	 */
	@Override
	protected GreetingServiceAsync getAsyncService() {
		GreetingServiceAsync service = SyncProxy.create(GreetingService.class);
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
}
