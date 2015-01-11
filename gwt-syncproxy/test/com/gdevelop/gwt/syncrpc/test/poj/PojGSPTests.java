package com.gdevelop.gwt.syncrpc.test.poj;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.gdevelop.gwt.syncrpc.test.safehtml.POJGwtSafeHtmlBuilderTest;
import com.gdevelop.gwt.syncrpc.test.safehtml.POJGwtSafeHtmlStringTest;
import com.google.gwt.user.client.rpc.CollectionsTest;
import com.google.gwt.user.client.rpc.CoreJavaTest;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTest;
import com.google.gwt.user.client.rpc.EnumsTest;
import com.google.gwt.user.client.rpc.ExceptionsTest;
import com.google.gwt.user.client.rpc.FailedRequestTest;
import com.google.gwt.user.client.rpc.FinalFieldsFalseTest;
import com.google.gwt.user.client.rpc.InheritanceTest;
import com.google.gwt.user.client.rpc.LoggingRPCTest;
import com.google.gwt.user.client.rpc.ObjectGraphTest;
import com.google.gwt.user.client.rpc.RecursiveClassTest;
import com.google.gwt.user.client.rpc.RemoteServiceServletTest;
import com.google.gwt.user.client.rpc.RpcTokenTest;
import com.google.gwt.user.client.rpc.RunTimeSerializationErrorsTest;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTest;
import com.google.gwt.user.client.rpc.UnicodeEscapingTest;
import com.google.gwt.user.client.rpc.ValueTypesTest;
import com.google.gwt.user.client.rpc.XsrfProtectionTest;

/**
 *
 * All Test are drawn from GWT source 2.7, modified to use SyncProxy as RPC
 * instead of GWT.create
 *
 * @author Preethum
 * @since 0.4
 *
 */
public class PojGSPTests extends TestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite("Plain Old Java GWT-SyncProxy Tests");
		suite.addTestSuite(CollectionsTest.class);
		suite.addTestSuite(CoreJavaTest.class);
		suite.addTestSuite(CustomFieldSerializerTest.class);
		suite.addTestSuite(EnumsTest.class);
		suite.addTestSuite(ExceptionsTest.class);
		suite.addTestSuite(FinalFieldsFalseTest.class);
		suite.addTestSuite(FailedRequestTest.class);
		suite.addTestSuite(LoggingRPCTest.class);
		suite.addTestSuite(InheritanceTest.class);
		suite.addTestSuite(ObjectGraphTest.class);
		suite.addTestSuite(RecursiveClassTest.class);
		suite.addTestSuite(RemoteServiceServletTest.class);
		suite.addTestSuite(RpcTokenTest.class);
		suite.addTestSuite(RunTimeSerializationErrorsTest.class);
		suite.addTestSuite(TypeCheckedObjectsTest.class);
		suite.addTestSuite(UnicodeEscapingTest.class);
		suite.addTestSuite(ValueTypesTest.class);
		suite.addTestSuite(XsrfProtectionTest.class);
		// For support of SafeHtml
		suite.addTestSuite(POJGwtSafeHtmlBuilderTest.class);
		suite.addTestSuite(POJGwtSafeHtmlStringTest.class);
		return suite;
	}
}
