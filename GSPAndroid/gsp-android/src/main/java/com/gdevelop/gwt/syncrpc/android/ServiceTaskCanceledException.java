/**
 * Copyright 2015 Blue Esoteric Web Development, LLC <http://www.blueesoteric.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc.android;

/**
 * Thrown if the task being executed was canceled before reaching a completion status: RPC_COMPLETE
 * or RPC_INTERRUPTED
 *
 * @author Preethum
 * @version 0.6
 * @since 0.6 - 5/27/2015
 */
public class ServiceTaskCanceledException extends RuntimeException {

	public ServiceTaskCanceledException(Exception exception) {
		super(exception);
	}
}
