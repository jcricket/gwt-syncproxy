package com.gdevelop.gwt.syncrpc.server.test;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class GAEAuthenticationFilter implements Filter{
  public GAEAuthenticationFilter() {
    super();
  }

  public void init(FilterConfig filterConfig) {
  }
  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException{
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    httpRequest.getSession(true);
    
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == null){
      throw new ServletException("Not logged in.");
    }
    
    chain.doFilter(request, response);
  }
}
