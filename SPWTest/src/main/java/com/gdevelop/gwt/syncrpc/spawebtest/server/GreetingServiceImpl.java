package com.gdevelop.gwt.syncrpc.spawebtest.server;

import java.util.ArrayList;

import com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingService;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.FieldVerifier;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.T1;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
GreetingService {

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
	public String greetServer(String input) throws IllegalArgumentException {
		// UserService userService = UserServiceFactory.getUserService();
		// User user = userService.getCurrentUser();
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

		return "Hello, " /* + user.getEmail() */+ ". You said: " + input
				+ "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * @see com.blueesoteric.testbed.client.GreetingService#greetServerWithUser(java.lang.String)
	 */
	@Override
	public T1 greetServer2(String name) throws IllegalArgumentException {
		T1 t1 = new T1();

		t1.setText(name);
		t1.setCount(COUNT);
		return t1;
	}

	@Override
	public ArrayList<String> greetServerArr(String input)
			throws IllegalArgumentException {

		ArrayList<String> list = new ArrayList<String>();
		list.add(input);
		return list;
	}

}
