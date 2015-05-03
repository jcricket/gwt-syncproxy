package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.util.ArrayList;

import com.gdevelop.gwt.syncrpc.spawebtest.shared.T1;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void greetServer2(String input, AsyncCallback<T1> callback)
			throws IllegalArgumentException;

	void greetServerArr(String input, AsyncCallback<ArrayList<String>> callback)
			throws IllegalArgumentException;
}
