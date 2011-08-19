package com.gdevelop.gwt.syncrpc;

import java.util.HashMap;
import java.util.Map;

public class LoginProviderRegistry {
  public static final String GAE_LOGIN_PROVIDER = "gae";
  public static final String SPRING_LOGIN_PROVIDER = "spring";

  private static final Map<String, LoginProvider> LOGIN_PROVIDERS = new HashMap<String, LoginProvider>();
  
  static{
    LOGIN_PROVIDERS.put(GAE_LOGIN_PROVIDER, new GAELoginProvider());
    LOGIN_PROVIDERS.put(SPRING_LOGIN_PROVIDER, new SpringLoginProvider());
  }
  
  public static void registerLoginProvider(String scheme, LoginProvider loginProvider) {
    LOGIN_PROVIDERS.put(scheme, loginProvider);
  }

  public static final LoginProvider getLoginProvider(String loginScheme){
    return LOGIN_PROVIDERS.get(loginScheme);
  }
}
