package com.gdevelop.gwt.syncrpc;

import com.google.gdata.client.GoogleAuthTokenFactory;
import com.google.gwt.user.client.rpc.StatusCodeException;

import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class GAELoginProvider implements LoginProvider {

  private static final String GAE_SERVICE_NAME = "ah";

  @Override
  public void login(URL loginURL, LoginCredentials credentials, SessionManager sessionManager) throws Exception {
    String loginUrl = credentials.getLoginUrl();
    String serviceUrl = credentials.getServiceUrl();
    String email = credentials.getUsername();
    String password = credentials.getPassword();
    
    boolean localDevMode = false;
    if (credentials.getLoginUrl().startsWith("http://localhost")) {
      localDevMode = true;
    }

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
      sessionManager.getCookieManager().setCookies(connection);

      OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
      writer.write(requestData);
      writer.flush();
      writer.close();
      sessionManager.getCookieManager().storeCookies(connection);
      int statusCode = connection.getResponseCode();

      if ((statusCode != HttpURLConnection.HTTP_OK)
          && (statusCode != HttpURLConnection.HTTP_MOVED_TEMP)) {
        String responseText = Utils.getResposeText(connection);
        throw new StatusCodeException(statusCode, responseText);
      }
    } else {
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
      // Get cookie returned from login service
      sessionManager.getCookieManager().storeCookies(connection);
      int statusCode = connection.getResponseCode();
      if ((statusCode != HttpURLConnection.HTTP_OK)
          && (statusCode != HttpURLConnection.HTTP_MOVED_TEMP)) {
        String responseText = Utils.getResposeText(connection);
        throw new StatusCodeException(statusCode, responseText);
      }
    }
  }
}
