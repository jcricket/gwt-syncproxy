package com.gdevelop.gwt.syncrpc.test.client;

import java.io.Serializable;

public class UserInfo implements Serializable{
  private String id;
  private String email;
  
  public UserInfo() {
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }
}
