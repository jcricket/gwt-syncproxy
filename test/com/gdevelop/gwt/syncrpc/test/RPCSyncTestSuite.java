package com.gdevelop.gwt.syncrpc.test;


import com.gdevelop.gwt.syncrpc.CredentialsManager;
import com.gdevelop.gwt.syncrpc.LoginCredentials;
import com.gdevelop.gwt.syncrpc.SyncProxy;

import com.google.gwt.junit.tools.GWTTestSuite;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;

import junit.framework.Test;

import junit.textui.TestRunner;


public class RPCSyncTestSuite {
  public static final String BASE_URL = "http://localhost:8888/rpcsuite/";
//  public static final String BASE_URL = "http://gdevelop-demo.appspot.com/rpcsuite/";
  
  static void login(){
    // if (true)return;     // Used for testing without user login
    try {
      if (RPCSyncTestSuite.BASE_URL.indexOf("appspot.com") > 0){
        System.out.print("Enter your email: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final String email = br.readLine();
        System.out.print("Enter your password: ");
        final String password = br.readLine();
        
        // SyncProxy.loginGAE("https://gdevelop-demo.appspot.com", "http://gdevelop-demo.appspot.com", email, password);
        SyncProxy.getDefaultSessionManager().setCredentialsManager(new CredentialsManager(){
          public LoginCredentials getLoginCredentials(URL url) {
            return new LoginCredentials("gae", "https://gdevelop-demo.appspot.com", "https://gdevelop-demo.appspot.com", email, password);
          }
        });
      }else{
        // SyncProxy.loginGAE("http://localhost:8888", "http://localhost:8888", "a@b.com", "");
        SyncProxy.getDefaultSessionManager().setCredentialsManager(new CredentialsManager(){
          public LoginCredentials getLoginCredentials(URL url) {
            return new LoginCredentials("gae", "http://localhost:8888", "http://localhost:8888", "a@b.com", "");
          }
        });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  static{
    login();
  }

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
