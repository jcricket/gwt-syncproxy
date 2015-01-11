/*
 * Copyright 2014 Google Inc.
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

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.FinalFieldsTestService.FinalFieldsNode;

/**
 * Test serializing final fields rpc.serializeFinalFields=false (default).
 * Modified by P.Prith in 0.5 to utilize Local App Engine server for service
 * through SyncProxy against Test in GWT 2.7.0
 */
public class FinalFieldsFalseTest extends RpcTestBase {
	private FinalFieldsTestServiceAsync finalFieldsTestService;

	private FinalFieldsTestServiceAsync getServiceAsync() {
		if (this.finalFieldsTestService == null) {
			this.finalFieldsTestService = SyncProxy
					.create(FinalFieldsTestService.class);
			((ServiceDefTarget) this.finalFieldsTestService)
			.setServiceEntryPoint(getModuleBaseURL() + "finalfields");
		}
		return this.finalFieldsTestService;
	}

	/**
	 * @see com.google.gwt.user.client.rpc.RpcTestBase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		SyncProxy.suppressRelativePathWarning(true);
	}

	public void testFinalFields() {
		FinalFieldsTestServiceAsync service = getServiceAsync();
		delayTestFinishForRpc();

		FinalFieldsNode node = new FinalFieldsNode(4, "C", 9);

		service.transferObject(node, new AsyncCallback<FinalFieldsNode>() {
			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(FinalFieldsNode result) {
				assertNotNull(result);
				assertTrue(TestSetValidator
						.isValidFinalFieldsObjectDefault(result));
			}
		});

		service.returnI(node, new AsyncCallback<Integer>() {
			@Override
			public void onFailure(Throwable caught) {
				TestSetValidator.rethrowException(caught);
			}

			@Override
			public void onSuccess(Integer i) {
				// since finalize is false, i should be 5
				assertEquals(new Integer(5), i);
				finishTest();
			}
		});
	}
}
