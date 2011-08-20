package com.gdevelop.gwt.syncrpc.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
  ValueTypesTest.class
  , EnumsTest.class
  , InheritanceTest.class
  , CollectionsTest.class
  , CoreJavaTest.class
  , CustomFieldSerializerTest.class
  , ExceptionTest.class
  , ObjectGraphTest.class
  // , RemoteServiceServletTest.class
  // , RpcTokenTest.class
  , UnicodeEscapingTest.class
  , RunTimeSerializationErrorsTest.class
  , RecursiveClassTest.class
  , TypeCheckedObjectsTest.class
  , XsrfProtectionTest.class
})
public class SyncProxyTestSuite {
  public SyncProxyTestSuite() {
  }
}
