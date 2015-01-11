package com.gdevelop.gwt.syncrpc.test.poj;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadServiceAsync;

/**
 * POJ Test of the LargePayload Service. Tests are run from heirarchy with
 * modified RpcTestBase and Service
 *
 * @author Preethum
 * @since 0.5
 *
 */
public class LargePayloadTest extends
com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadTest {

	/**
	 * @see com.gdevelop.gwt.syncrpc.spawebtest.client.LargePayloadTest#getAsyncService()
	 */
	@Override
	protected LargePayloadServiceAsync getAsyncService() {
		LargePayloadServiceAsync service = SyncProxy
				.create(LargePayloadService.class);
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
