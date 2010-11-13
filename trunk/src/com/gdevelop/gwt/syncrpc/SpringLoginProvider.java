package com.gdevelop.gwt.syncrpc;

import java.net.HttpURLConnection;
import java.net.URL;

public class SpringLoginProvider implements LoginProvider{

  @Override
  public void login(URL url, LoginCredentials credentials, SessionManager sessionManager) throws Exception {
    
    //Make the inital connection to the server (should assign us a cookie):
    HttpURLConnection springCheck = (HttpURLConnection) url.openConnection();
    springCheck.setRequestMethod("POST");
    springCheck.setInstanceFollowRedirects(false);
    springCheck.connect();
    System.out.println("Auth response: " + springCheck.getResponseCode() + "-" + springCheck.getHeaderFields() + Utils.getResposeText(springCheck));
    sessionManager.getCookieManager().storeCookies(springCheck);
    
    //We'll expect a redirect to the login page:
    if(springCheck.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP)
    {
      URL redirect = new URL(credentials.getLoginUrl());
      springCheck = (HttpURLConnection) redirect.openConnection();
      springCheck.setDoOutput(true);
      springCheck.setDoInput(true);
      springCheck.setRequestMethod("POST");
      springCheck.setInstanceFollowRedirects(false);
      springCheck.addRequestProperty("Referer", url.toString());
      springCheck.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      sessionManager.getCookieManager().setCookies(springCheck);
      System.out.println("Auth request: " + springCheck.getRequestMethod() + "-" + springCheck.getRequestProperties());
      springCheck.connect();
      
      String login = new String("j_username=" + credentials.getUsername() + "&j_password=" + credentials.getPassword() + "&login=Login");
      springCheck.getOutputStream().write(login.getBytes("utf-8"));
      springCheck.getOutputStream().close();
      
      System.out.println("Auth response: " + springCheck.getResponseCode() + "-" + springCheck.getHeaderFields());
      if(springCheck.getResponseCode() != HttpURLConnection.HTTP_MOVED_TEMP)
      {
        throw new Exception("Login failed: " + springCheck.getResponseCode() + "-" + springCheck.getResponseMessage() + ":" + Utils.getResposeText(springCheck)); 
      }
      sessionManager.getCookieManager().clearCookies(springCheck);
      sessionManager.getCookieManager().storeCookies(springCheck);
    }
    else
    {
      
    }
  }
}
