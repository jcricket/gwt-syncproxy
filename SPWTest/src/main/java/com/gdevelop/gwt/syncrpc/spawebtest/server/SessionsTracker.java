package com.gdevelop.gwt.syncrpc.spawebtest.server;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Modified from
 * http://stackoverflow.com/questions/3679465/find-number-of-active
 * -sessions-created-from-a-given-client-ip/3679783#3679783
 *
 * @author Preethum
 * @since 0.5
 *
 */
public class SessionsTracker implements ServletContextListener,
HttpSessionListener, ServletRequestListener {

	public static SessionsTracker getInstance(ServletContext context) {
		return (SessionsTracker) context.getAttribute(ATTRIBUTE_NAME);
	}

	private static final String ATTRIBUTE_NAME = "com.example.SessionCounter";

	private List<HttpSession> sessions = new CopyOnWriteArrayList<HttpSession>();

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// NOOP. No logic needed. Maybe some future cleanup?
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		event.getServletContext().setAttribute(ATTRIBUTE_NAME, this);
	}

	public void destroyAllSessions() {
		for (HttpSession session : this.sessions) {
			session.invalidate();
		}
	}

	public int getCount(String remoteAddr) {
		return Collections.frequency(this.sessions, remoteAddr);
	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		// NOOP. No logic needed.
	}

	@Override
	public void requestInitialized(ServletRequestEvent event) {

	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		if (session.isNew()) {
			this.sessions.add(session);
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		this.sessions.remove(event.getSession());
	}

}