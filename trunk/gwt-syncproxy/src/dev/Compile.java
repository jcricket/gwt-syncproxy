package dev;

import com.google.gwt.dev.Compiler;

public class Compile {
	public static void main(String[] args) {
	  args = new String[]{"-war", ".", 
      "com.gdevelop.gwt.syncrpc.test.SyncRPCSuite"
		};
		
		Compiler.main(args);
	}
}
