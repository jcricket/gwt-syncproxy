package dev;

import com.google.gwt.dev.DevMode;

public class StartDevAppServer {
  public static void main(String[] args) throws Exception {
    System.setProperty("gwt.rpc.dumpPayload", "true");
    
    args = new String[]{"-war", ".", 
      "-gen", "../x-src-gen",
      "-startupUrl", "/questions/ask/", 
      "com.gdevelop.gwt.syncrpc.test.SyncRPCSuite"
    };
    DevMode.main(args);
  }

  public static void gae_main(String[] args) throws Exception {
    StartGAEDevServer.main(args);
    
    args = new String[]{"-war", ".", 
      "-gen", "../x-src-gen",
      "-noserver",
      "-startupUrl", "/questions/ask/", 
      "com.google.gwt.user.RPCSuite"
    };
    DevMode.main(args);
  }
}
