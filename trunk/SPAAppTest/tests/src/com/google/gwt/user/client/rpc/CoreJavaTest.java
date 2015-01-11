/*
 * Copyright 2011 Google Inc.
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

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.CountDownLatch;

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.AndroidGWTTestCase;

/**
 * Test cases for Java core emulation classes in GWT RPC.
 *
 * Logging is tested in a separate suite because it requires logging enabled in
 * gwt.xml. Otherwise, only MathContext has non-trivial content for RPC.
 *
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 *
 */
public class CoreJavaTest extends AndroidGWTTestCase<CoreJavaTestServiceAsync> {

	private static MathContext createMathContext() {
		return new MathContext(5, RoundingMode.CEILING);
	}

	public static boolean isValid(MathContext value) {
		return createMathContext().equals(value);
	}

	private CoreJavaTestServiceAsync coreJavaTestService;

	public CoreJavaTest() throws InterruptedException {
		super(MainActivity.class);
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				CoreJavaTest.this.coreJavaTestService = SyncProxy
						.create(CoreJavaTestService.class);
				arg0[0].countDown();
				return null;
			}
		});
	}

	private CoreJavaTestServiceAsync getServiceAsync() {

		return this.coreJavaTestService;
	}

	public void testMathContext() {
		final CoreJavaTestServiceAsync service = getServiceAsync();
		final MathContext expected = createMathContext();

		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				service.echoMathContext(expected,
						new AsyncCallback<MathContext>() {
							@Override
							public void onFailure(Throwable caught) {
								TestSetValidator.rethrowException(caught);
							}

							@Override
							public void onSuccess(MathContext result) {
								assertNotNull(result);
								assertTrue(isValid(result));
								finishTest();
							}
						});
				return null;
			}
		});
	}
}
