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
package com.gdevelop.gwt.syncrpc.spawebtest.client.gwttests;

import junit.framework.Test;

import com.google.gwt.junit.tools.GWTTestSuite;
import com.google.gwt.safehtml.shared.GwtSafeHtmlBuilderTest;
import com.google.gwt.safehtml.shared.GwtSafeHtmlStringTest;
import com.google.gwt.user.client.rpc.CollectionsTest;
import com.google.gwt.user.client.rpc.CoreJavaTest;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTest;
import com.google.gwt.user.client.rpc.EnumsTest;
import com.google.gwt.user.client.rpc.ExceptionsTest;
import com.google.gwt.user.client.rpc.FailedRequestTest;
import com.google.gwt.user.client.rpc.FinalFieldsFalseTest;
import com.google.gwt.user.client.rpc.FinalFieldsTest;
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
 * Draws directly from tests in the GWT src for verification against PojTests
 * and SPATests
 *
 * @author Preethum
 * @since 0.4.0
 *
 */
public class GwtTests extends GWTTestSuite {
	public static Test suite() {
		GWTTestSuite suite = new GWTTestSuite("GWT Tests");
		suite.addTestSuite(CollectionsTest.class);
		suite.addTestSuite(CoreJavaTest.class);
		suite.addTestSuite(CustomFieldSerializerTest.class);
		suite.addTestSuite(EnumsTest.class);
		suite.addTestSuite(ExceptionsTest.class);
		suite.addTestSuite(FailedRequestTest.class);
		suite.addTestSuite(FinalFieldsFalseTest.class);
		suite.addTestSuite(FinalFieldsTest.class);
		suite.addTestSuite(InheritanceTest.class);
		suite.addTestSuite(LoggingRPCTest.class);
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
		suite.addTestSuite(GwtSafeHtmlStringTest.class);
		suite.addTestSuite(GwtSafeHtmlBuilderTest.class);
		return suite;
	}
}
