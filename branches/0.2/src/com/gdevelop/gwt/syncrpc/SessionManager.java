package com.gdevelop.gwt.syncrpc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface SessionManager {
  
  /**
   * Creates a url connection setting any appropriate headers including cookies
   * @param url The url
   * @return an opened HttpURLConnection
   */
  public HttpURLConnection openConnection(URL url) throws Exception;
  
  
  /**
   * Called after a request is made to handle any response headers 
   * including cookies. The response data from the connection shouldn't
   * be read. 
   * @param connection The connection
   */
  public void handleResponseHeaders(HttpURLConnection connection) throws IOException;

  /**
   * @return The {@link CookieManager} for this session.
   */
  public CookieManager getCookieManager();
  
  /**
   * @param credentialsManager The {@link CredentialsManager} for this session:
   */
  public void setCredentialsManager(CredentialsManager credentialsManager);
}
