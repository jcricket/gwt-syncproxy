package com.gdevelop.gwt.syncrpc.test;


import com.google.gwt.junit.tools.GWTTestSuite;

import junit.framework.Test;

import junit.textui.TestRunner;


public class RPCSyncTestSuite {
//  public static final String BASE_URL = "https://localhost:8443/rpcsuite/rpcsuite/";
  public static final String BASE_URL = "http://localhost:8888/rpcsuite/";

  public RPCSyncTestSuite() {
  }
  
  public static Test suite() {
    GWTTestSuite suite = new GWTTestSuite(
        "Sync Test for com.google.gwt.user.client.rpc");

    suite.addTestSuite(CollectionsTest.class);
    suite.addTestSuite(CustomFieldSerializerTest.class);
    suite.addTestSuite(EnumsTest.class);
    suite.addTestSuite(InheritanceTest.class);
    suite.addTestSuite(ObjectGraphTest.class);
    suite.addTestSuite(RunTimeSerializationErrorsTest.class);
    suite.addTestSuite(UnicodeEscapingTest.class);
    suite.addTestSuite(ValueTypesTest.class);
    return suite;
  }
  
  public static void main(String[] args) throws Throwable{
    args = new String[]{RPCSyncTestSuite.class.getName()};
    TestRunner.main(args);
  }
}
