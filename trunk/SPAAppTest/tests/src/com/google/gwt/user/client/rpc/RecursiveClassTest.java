/*
 * Copyright 2010 Google Inc.
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

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.AndroidGWTTestCase;
import com.google.gwt.user.client.rpc.RecursiveClassTestService.ResultNode;

/**
 * Class used to test generics with wild cards and recursive references.
 *
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class RecursiveClassTest extends
AndroidGWTTestCase<RecursiveClassTestServiceAsync> {

	private RecursiveClassTestServiceAsync recursiveClassTestService;

	public RecursiveClassTest() throws InterruptedException {
		super(MainActivity.class);

		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				RecursiveClassTest.this.recursiveClassTestService = SyncProxy
						.create(RecursiveClassTestService.class);
				((ServiceDefTarget) RecursiveClassTest.this.recursiveClassTestService)
						.setServiceEntryPoint(getModuleBaseURL()
								+ "recursiveclass");
				arg0[0].countDown();
				return null;
			}
		});
	}

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private RecursiveClassTestServiceAsync getServiceAsync() {

		return this.recursiveClassTestService;
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		SyncProxy.suppressRelativePathWarning(true);
		super.setUp();
	}

	/**
	 * This method is used to test generics with wild cards and recursive
	 * references.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testRecursiveClass() {
		RecursiveClassTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();

		service.greetServer("Hello", new AsyncCallback() {
			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(Object result) {
				assertNotNull(result);
				assertTrue(result instanceof ResultNode);
				finishTest();
			}
		});
	}

}
