package com.gdevelop.gwt.syncrpc.spawebtest.server;

import java.util.ArrayList;

import com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingService;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.FieldVerifier;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	// /**
	// * @see
	// com.blueesoteric.testbed.client.GreetingService#greetServerWithUser(java.lang.String)
	// */
	// @Override
	// public T1 greetServer(String name) throws IllegalArgumentException {
	// UserService userService = UserServiceFactory.getUserService();
	// User user = userService.getCurrentUser();
	// String serverInfo = getServletContext().getServerInfo();
	// String userAgent = getThreadLocalRequest().getHeader("User-Agent");
	//
	// // Escape data from the client to avoid cross-site script
	// // vulnerabilities.
	// name = escapeHtml(name);
	// userAgent = escapeHtml(userAgent);
	// T1 t1 = new T1();
	//
	// t1.setText("Hello, " + name + "(" + user.getEmail()
	// + ")!<br><br>I am running " + serverInfo
	// + ".<br><br>It looks like you are using:<br>" + userAgent);
	// return t1;
	// }

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public String greetServer2(String input) throws IllegalArgumentException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back
			// to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script
		// vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + user.getEmail() + ". You said: " + input
				+ "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	@Override
	public ArrayList<String> greetServerArr(String input)
			throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back
			// to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script
		// vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		ArrayList<String> list = new ArrayList<String>();
		list.add("List says Hello, " + input + "!<br><br>I am running "
				+ serverInfo + ".<br><br>It looks like you are using:<br>"
				+ userAgent);
		return list;
	}

}
