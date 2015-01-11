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

import java.util.concurrent.CountDownLatch;

import android.os.AsyncTask;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.spaapptest.MainActivity;
import com.gdevelop.gwt.syncrpc.spaapptest.test.AndroidGWTTestCase;
import com.google.gwt.core.shared.SerializableThrowable;
import com.google.gwt.event.shared.UmbrellaException;

/**
 * Tests serialization of various GWT Exception classes for RPC.
 *
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class ExceptionsTest extends
AndroidGWTTestCase<ExceptionsTestServiceAsync> {

	private ExceptionsTestServiceAsync exceptionsTestService;

	public ExceptionsTest() throws InterruptedException {
		super(MainActivity.class);

		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				ExceptionsTest.this.exceptionsTestService = SyncProxy
						.create(ExceptionsTestService.class);
				arg0[0].countDown();
				return null;
			}
		});
	}

	private <T extends Throwable> void checkException(T expected,
			AsyncCallback<T> callback) {
		delayTestFinishForRpc();
		getServiceAsync().echo(expected, callback);
	}

	private ExceptionsTestServiceAsync getServiceAsync() {

		return this.exceptionsTestService;
	}

	public void testSerializableThrowable() {
		// fail("IRSE TypeSignature for SeraializableThrowable");
		SerializableThrowable expected = new SerializableThrowable(null, "msg");
		expected.setDesignatedType("x", true);
		expected.setStackTrace(new StackTraceElement[] { new StackTraceElement(
				"c", "m", "f", 42) });
		expected.initCause(new SerializableThrowable(null, "cause"));

		checkException(expected, new AsyncCallback<SerializableThrowable>() {
			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(SerializableThrowable result) {
				assertNotNull(result);
				assertEquals("msg", result.getMessage());
				assertEquals("x", result.getDesignatedType());
				assertTrue(result.isExactDesignatedTypeKnown());
				assertEquals("c.m(f:42)", result.getStackTrace()[0].toString());
				assertEquals("cause", ((SerializableThrowable) result
						.getCause()).getMessage());
				finishTest();
			}
		});
	}

	public void testUmbrellaException() {
		fail("IRSE Illegal Access Exception for JRE Fields");
		final UmbrellaException expected = TestSetFactory
				.createUmbrellaException();
		checkException(expected, new AsyncCallback<UmbrellaException>() {
			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(UmbrellaException result) {
				assertNotNull(result);
				assertTrue(TestSetValidator.isValid(expected, result));
				finishTest();
			}
		});
	}
}
