package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.util.ArrayList;

import com.gdevelop.gwt.syncrpc.spawebtest.shared.T1;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	final static int COUNT = 2;
	final static String NAME = "GWT User";

	String greetServer(String name) throws IllegalArgumentException;

	T1 greetServer2(String name) throws IllegalArgumentException;

	ArrayList<String> greetServerArr(String name)
			throws IllegalArgumentException;

}
