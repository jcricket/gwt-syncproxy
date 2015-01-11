/*
 * Copyright 2007 Google Inc.
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
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestService.Complex;
import com.google.gwt.user.client.rpc.EnumsTestService.FieldEnum;
import com.google.gwt.user.client.rpc.EnumsTestService.FieldEnumWrapper;
import com.google.gwt.user.client.rpc.EnumsTestService.Subclassing;

/**
 * Tests enums over RPC.
 *
 * Modified by P.Prith in 0.4.4 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class EnumsTest extends AndroidGWTTestCase<EnumsTestServiceAsync> {
	/**
	 * @since 0.4.4
	 * @return
	 */
	private static EnumsTestServiceAsync getService() {
		return enumsTestService;
	}

	private static void rethrowException(Throwable caught) {
		if (caught instanceof RuntimeException) {
			throw (RuntimeException) caught;
		} else {
			throw new RuntimeException(caught);
		}
	}

	private static EnumsTestServiceAsync enumsTestService;

	public EnumsTest() throws InterruptedException {
		super(MainActivity.class);
		setServiceInitTask(new AsyncTask<CountDownLatch, Void, Void>() {
			@Override
			protected Void doInBackground(CountDownLatch... arg0) {
				EnumsTest.enumsTestService = SyncProxy
						.create(EnumsTestService.class);
				((ServiceDefTarget) enumsTestService)
						.setServiceEntryPoint(getModuleBaseURL() + "enums");
				arg0[0].countDown();
				return null;
			}
		});
	}

	@Override
	protected void setUp() throws Exception {
		SyncProxy.suppressRelativePathWarning(true);
		super.setUp();
	}

	/**
	 * Test that basic enums can be used over RPC.
	 */
	public void testBasicEnums() {
		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				getService().echo(Basic.A, new AsyncCallback<Basic>() {
					@Override
					public void onFailure(Throwable caught) {
						rethrowException(caught);
					}

					@Override
					public void onSuccess(Basic result) {
						assertNotNull("Was null", result);
						assertEquals(Basic.A, result);
						finishTest();
					}
				});
				return null;
			}
		});
	}

	/**
	 * Test that complex enums with state and non-default constructors can be
	 * used over RPC and that the client state does not change.
	 */
	public void testComplexEnums() {
		Complex a = Complex.A;
		a.value = "client";

		delayTestFinishForRpc();
		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				getService().echo(Complex.A, new AsyncCallback<Complex>() {
					@Override
					public void onFailure(Throwable caught) {
						rethrowException(caught);
					}

					@Override
					public void onSuccess(Complex result) {
						assertNotNull("Was null", result);
						assertEquals(Complex.A, result);

						// Ensure that the server's changes did not impact us.
						assertEquals("client", result.value);

						finishTest();
					}
				});
				return null;
			}
		});
	}

	/**
	 * Test that enums as fields in a wrapper class can be passed over RPC.
	 */
	public void testFieldEnumWrapperClass() {
		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				FieldEnumWrapper wrapper = new FieldEnumWrapper();
				wrapper.setFieldEnum(FieldEnum.X);
				getService().echo(wrapper,
						new AsyncCallback<FieldEnumWrapper>() {
					@Override
					public void onFailure(Throwable caught) {
						rethrowException(caught);
					}

					@Override
					public void onSuccess(FieldEnumWrapper result) {
						assertNotNull("Was null", result);
						FieldEnum fieldEnum = result.getFieldEnum();
						/*
								 * Don't want to do assertEquals(FieldEnum.X,
								 * fieldEnum) here, since it will force an
								 * implicit upcast on FieldEnum -> Object, which
								 * will bias the test. We want to assert that
								 * the EnumOrdinalizer properly prevents
								 * ordinalization of FieldEnum.
								 */
						assertTrue(FieldEnum.X == fieldEnum);
						finishTest();
					}
				});
				return null;
			}
		});
	}

	/**
	 * Test that null can be used as an enumeration.
	 */
	public void testNull() {
		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				getService().echo((Basic) null, new AsyncCallback<Basic>() {
					@Override
					public void onFailure(Throwable caught) {
						rethrowException(caught);
					}

					@Override
					public void onSuccess(Basic result) {
						assertNull(result);
						finishTest();
					}
				});
				return null;
			}
		});
	}

	/**
	 * Test that enums with subclasses can be passed over RPC.
	 */
	public void testSubclassingEnums() {
		delayTestFinishForRpc();

		setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				getService().echo(Subclassing.A,
						new AsyncCallback<Subclassing>() {
					@Override
					public void onFailure(Throwable caught) {
						rethrowException(caught);
					}

					@Override
					public void onSuccess(Subclassing result) {
						assertNotNull("Was null", result);
						assertEquals(Subclassing.A, result);
						finishTest();
					}
				});
				return null;
			}
		});
	}

}
