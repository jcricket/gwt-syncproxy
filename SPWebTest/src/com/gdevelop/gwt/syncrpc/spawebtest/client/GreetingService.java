package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	// T1 greetServer(String name) throws IllegalArgumentException;

	String greetServer2(String name) throws IllegalArgumentException;

	ArrayList<String> greetServerArr(String name)
			throws IllegalArgumentException;

}
