package com.gdevelop.gwt.syncrpc;

/**
 * This class stores information used to log into a webapp using a given
 * authentication scheme. 
 * @author cmacnaug
 */
public class LoginCredentials {

  private final String loginScheme;
  private final String loginUrl;
  private final String serviceUrl;
  private final String username;
  private final String password;

  public LoginCredentials(String loginScheme, String loginUrl, String serviceUrl, String username, String password) {
    this.loginScheme = loginScheme;
    this.loginUrl = loginUrl;
    this.serviceUrl = serviceUrl;
    this.username = username;
    this.password = password;
  }

  public String getLoginScheme() {
    return loginScheme;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public String getServiceUrl() {
    return serviceUrl;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
