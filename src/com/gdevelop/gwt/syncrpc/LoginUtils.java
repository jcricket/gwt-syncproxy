package com.gdevelop.gwt.syncrpc;


import com.google.gdata.client.GoogleAuthTokenFactory;
import com.google.gdata.util.AuthenticationException;
import com.google.gwt.user.client.rpc.StatusCodeException;

import java.io.IOException;
import java.io.OutputStreamWriter;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class LoginUtils {
  private static final String GAE_SERVICE_NAME = "ah";
  
  /**
   *
   * @param loginUrl Should be http://localhost:8888 for local development mode 
   * or https://example.appspot.com for deployed app
   * @param serviceUrl Should be http://localhost:8888/yourApp.html
   * @param email
   * @param password
   * @return The CookieManager for subsequence call
   * @throws IOException
   * @throws AuthenticationException
   */
  public static CookieManager loginAppEngine(String loginUrl, String serviceUrl, 
                                    String email, String password) throws IOException,
                                                            AuthenticationException {
    boolean localDevMode = false;
    
    if (loginUrl.startsWith("http://localhost")){
      localDevMode = true;
    }
    
    CookieHandler oldCookieHandler = CookieHandler.getDefault();
    try{
      CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
      CookieHandler.setDefault(cookieManager);
      
      if (localDevMode) {
        loginUrl += "/_ah/login";
        URL url = new URL(loginUrl);
        email = URLEncoder.encode(email, "UTF-8");
        serviceUrl = URLEncoder.encode(serviceUrl, "UTF-8");
        String requestData = "email=" + email + "&continue=" + serviceUrl;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", "" + requestData.length());
        
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(requestData);
        writer.flush();
        writer.close();
        
        int statusCode = connection.getResponseCode();
        if ((statusCode != HttpURLConnection.HTTP_OK)
            && (statusCode != HttpURLConnection.HTTP_MOVED_TEMP)) {
          String responseText = Utils.getResposeText(connection);
          throw new StatusCodeException(statusCode, responseText);
        }
      }else{
        GoogleAuthTokenFactory factory = new GoogleAuthTokenFactory(GAE_SERVICE_NAME, "", null);
        // Obtain authentication token from Google Accounts
        String token = factory.getAuthToken(email, password, null, null, GAE_SERVICE_NAME, "");
        loginUrl = loginUrl + "/_ah/login?continue=" + URLEncoder.encode(serviceUrl, "UTF-8")
            + "&auth=" + token;
        URL url = new URL(loginUrl);
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();
        
        int statusCode = connection.getResponseCode();
        if ((statusCode != HttpURLConnection.HTTP_OK)
            && (statusCode != HttpURLConnection.HTTP_MOVED_TEMP)) {
          String responseText = Utils.getResposeText(connection);
          throw new StatusCodeException(statusCode, responseText);
        }
      }
      
      return cookieManager;
    }finally{
      CookieHandler.setDefault(oldCookieHandler);
    }
  }
}
