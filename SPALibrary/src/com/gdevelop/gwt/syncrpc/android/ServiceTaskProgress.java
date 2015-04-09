/**
 * Copyright 2015 Blue Esoteric Web Development, LLC
 * <http://www.blueesoteric.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc.android;

import com.gdevelop.gwt.syncrpc.SyncProxy;

/**
 * Progress indicators for {@link ServiceAsyncTask}
 *
 * @author Preethum
 * @since 0.6
 */
public enum ServiceTaskProgress {
	/**
	 * Indicates task has been created and initialized, pending execution
	 */
	INIT,
	/**
	 * Indicates {@link SyncProxy#setBaseURL(String)} has completed
	 */
	BASE_SET,
	/**
	 * Indicates the service has been created
	 */
	SERVICE_CREATED,
	/**
	 * Indicates the service RPC, as defined by
	 * {@link ServiceAsyncTask#serviceCall()}, has been initiated
	 */
	SERVICE_CALLED,
	/**
	 * Indicates the service RPC was interrupted or timed out
	 */
	RPC_INTERRUPTED,
	/**
	 * Indicates the service RPC has returned, with either failure or success
	 */
	RPC_COMPLETE,
	/**
	 * Indicates the task has ended and the appropriate AsyncCallback methods
	 * have been called
	 */
	TASK_COMPLETE
}