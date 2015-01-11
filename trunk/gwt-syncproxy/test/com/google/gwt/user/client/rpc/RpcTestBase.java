/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.user.client.rpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.gdevelop.gwt.syncrpc.SyncProxy;

/**
 * Base class for RPC tests.
 *
 * Modified by P.Prith to conduct Async tests using {@link CountDownLatch}
 *
 * @since gwt-syncproxy 0.5
 */
public class RpcTestBase extends TestCase {
	/**
	 * The timeout for RPC tests.
	 */
	protected static final int RPC_TIMEOUT = 15000;
	CountDownLatch signal;
	protected static final int DEFAULT_TIMEOUT = 2;
	/**
	 * Timeout for RPC inquiry
	 */
	int localizedRpcTimeout = DEFAULT_TIMEOUT;

	Logger logger = Logger.getLogger(RpcTestBase.class.getName());

	/**
	 * Delay finishing a test while we wait for an RPC response. This method
	 * should be used instead of {@link #delayTestFinish(int)} so we can adjust
	 * timeouts for all Rpc tests at once.
	 *
	 * @see #delayTestFinish(int)
	 */
	protected final void delayTestFinishForRpc() {
		this.signal = new CountDownLatch(1);
		this.logger.info("Assign Signal: " + this.signal);
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#finishTest()
	 */

	protected void finishTest() {
		this.logger.info("Finishing Test");
		if (this.signal != null) {
			this.logger.info("Countdown Signal: " + this.signal);
			this.signal.countDown();
		} else {
			this.logger.warning("Finishing Test with No Signal");
		}
	}

	public String getModuleBaseURL() {
		return "http://127.0.0.1:8888/spawebtest/";
	}

	public String getModuleName() {
		return "com.google.gwt.user.RPCSuite";
	}

	/**
	 * Used to set the timeout for a specific test. This value is reset in
	 * {@link #setUp()}
	 *
	 * @param timeout
	 */
	public void setRpcTimeout(int timeout) {
		this.localizedRpcTimeout = timeout;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.localizedRpcTimeout = DEFAULT_TIMEOUT;
		SyncProxy.setBaseURL(getModuleBaseURL());
		// SyncProxy.setLoggingLevel(Level.FINER);
		this.logger.setLevel(Level.WARNING);
	}

	/**
	 * @see com.google.gwt.junit.client.GWTTestCase#gwtTearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		try {
			waitForRpcFinish("Failed to Complete", this.localizedRpcTimeout);
		} finally {
			SyncProxy.suppressRelativePathWarning(false);
		}
		super.tearDown();
	}

	protected void waitForRpcFinish(String message, int seconds)
			throws InterruptedException {
		if (this.signal != null) {
			this.logger.info("Waiting on Signal: " + this.signal);
			assertTrue(message, this.signal.await(seconds, TimeUnit.SECONDS));
		}
	}
}
