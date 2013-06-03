package com.gdevelop.gwt.syncrpc.test.poj;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Preethum
 * @since 0.4
 * 
 */
public class PojGSPTests extends TestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite("Plain Old Java GWT-SyncProxy Tests");
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
		suite.addTestSuite(ValueTypesTest.class);
		suite.addTestSuite(UnicodeEscapingTest.class);
		suite.addTestSuite(XsrfProtectionTest.class);
		return suite;
	}
}
