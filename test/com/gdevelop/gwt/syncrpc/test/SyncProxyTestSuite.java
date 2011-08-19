package com.gdevelop.gwt.syncrpc.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
  CollectionsTest.class
  , CustomFieldSerializerTest.class
  , EnumsTest.class
  , ExceptionTest.class
  , InheritanceTest.class
  , ObjectGraphTest.class
  , RunTimeSerializationErrorsTest.class
  , UnicodeEscapingTest.class
  , ValueTypesTest.class
})
public class SyncProxyTestSuite {
  public SyncProxyTestSuite() {
  }
}
