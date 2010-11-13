package com.gdevelop.gwt.syncrpc;

import java.net.Authenticator;
import java.net.URL;

/**
 * A service provider interface for logging into a GWT server. This is
 * separate from java.net's {@link Authenticator} in that it is used
 * to establish an http session rather than regular web server authentication.
 * 
 * @author cmacnaug
 */
public interface LoginProvider {

  /**
   * Performs login for the given session. The login provider generally will set
   * cookies in the {@link SessionManager}'s {@link CookieManager}
   * 
   * @param url The url that is being logged into. 
   * @param credentials the credentials to use
   * @param sessionManager The manager for the session. 
   * @throws Exception If there is an error logging in
   */
  public void login(URL url, LoginCredentials credentials, SessionManager sessionManager) throws Exception;
}
