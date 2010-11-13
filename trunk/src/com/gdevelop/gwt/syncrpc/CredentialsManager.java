package com.gdevelop.gwt.syncrpc;

import java.net.URL;

/**
 * This interface can be used in conjuction with a {@link SessionManager}
 * to provide login information. 
 */
public interface CredentialsManager {

  /**
   * Gets login information for the specified url. If non null
   * {@link LoginCredentials} are return the {@link SessionManager} will 
   * attempt to lookup the corresponding login provider via
   * {@link LoginProviderRegistry#getLoginProvider(String)} using the value returned
   * by {@link LoginCredentials#getLoginScheme()}.
   * 
   * @param url The url being loggen into. 
   * @return Credentials for authentication or null if no authentication is required. 
   */
  public LoginCredentials getLoginCredentials(URL url);
}
