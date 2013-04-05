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
package com.gdevelop.gwt.syncrpc.spaapptest.test.gwttests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Preethum
 * @since 0.4
 * 
 */
public class SPAGWTTests extends TestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite("SyncProxy Android GWT Service Tests");
		suite.addTestSuite(AsyncEnumsTest.class);
		suite.addTestSuite(CollectionsCallbackTest.class);
		suite.addTestSuite(CollectionsTest.class);
		suite.addTestSuite(CoreJavaTest.class);
		suite.addTestSuite(CustomFieldSerializerTest.class);
		suite.addTestSuite(EnumsTest.class);
		suite.addTestSuite(ExceptionTest.class);
		suite.addTestSuite(HttpsTest.class);
		suite.addTestSuite(InheritanceTest.class);
		suite.addTestSuite(ObjectGraphTest.class);
		suite.addTestSuite(RunTimeSerializationErrorsTest.class);
		suite.addTestSuite(TypeCheckedObjectsTest.class);
		suite.addTestSuite(UnicodeEscapingTest.class);
		suite.addTestSuite(ValueTypesTest.class);
		suite.addTestSuite(XsrfProtectionTest.class);
		return suite;
	}
}
