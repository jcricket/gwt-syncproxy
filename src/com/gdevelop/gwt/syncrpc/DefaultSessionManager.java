package com.gdevelop.gwt.syncrpc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

/**
 * Implements a default session manager that handles storing its own
 * private sesssion state such as cookies and and login information. 
 */
public class DefaultSessionManager implements SessionManager {

  private CookieManager cookieManager = new CookieManager();
  private CredentialsManager credentialsManager;
  private HashSet<String> loggedIn = new HashSet<String>();


  @Override
  public HttpURLConnection openConnection(URL url) throws Exception {
    login(url);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    cookieManager.setCookies(connection);
    return connection;
  }

  @Override
  public void handleResponseHeaders(HttpURLConnection connection) throws IOException {
    cookieManager.storeCookies(connection);
  }
  
  public void login(URL url) throws Exception {
    if(credentialsManager == null) {
      return; 
    }
    
    LoginCredentials credentials = credentialsManager.getLoginCredentials(url);
    if(credentials == null)
    {
      return;
    }
    
    //If we've already logged in return:
    if(loggedIn.contains(credentials.getLoginUrl()))
    {
      return;
    }
    
    LoginProvider provider = LoginProviderRegistry.getLoginProvider(credentials.getLoginScheme());
    if(provider == null)
    {
      throw new IOException(credentials.getLoginScheme() + " is not a registered login scheme");
    }
    provider.login(url, credentials, this);
    loggedIn.add(credentials.getLoginUrl());
  }

  public CookieManager getCookieManager() {
    return cookieManager;
  }

  @Override
  public void setCredentialsManager(CredentialsManager credentialsManager) {
    this.credentialsManager = credentialsManager;
  }

}
