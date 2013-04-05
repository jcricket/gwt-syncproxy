/*
 * Copyright 2013 Blue Esoteric Web Development, LLC (http://www.blueesoteric.com/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */
package com.gdevelop.gwt.syncrpc.spawebtest.client;

import junit.framework.Test;

import com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests.GwtTests;
import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * @author Preethum
 * @since
 * 
 */
public class StandardTests extends GWTTestSuite {
	public static Test suite() {
		GWTTestSuite suite = new GWTTestSuite("All Standard Tests");
		suite.addTest(GwtTests.suite());
		suite.addTestSuite(GreetingServiceTest.class);
		suite.addTestSuite(LargePayloadTest.class);
		return suite;
	}
}
