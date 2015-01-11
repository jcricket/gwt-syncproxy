/**
 * Jan 9, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.spawebtest.server;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gdevelop.gwt.syncrpc.spawebtest.client.CookieService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class CookieServiceImpl extends RemoteServiceServlet implements
CookieService {

	private static final long serialVersionUID = 1L;

	/**
	 * @see com.gdevelop.gwt.syncrpc.spawebtest.client.CookieService#echoCookiesFromClient()
	 */
	@Override
	public ArrayList<String> echoCookiesFromClient() {
		Cookie[] cookies = this.getThreadLocalRequest().getCookies();
		ArrayList<String> vals = new ArrayList<String>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				vals.add(cookie.getValue());
			}
		}
		return vals;
	}

	/**
	 * @see com.gdevelop.gwt.syncrpc.spawebtest.client.CookieService#generateCookiesOnServer()
	 */
	@Override
	public void generateCookiesOnServer() {
		for (String cVal : CookieService.COOKIE_VALS) {
			Cookie userCookie = new Cookie(cVal, cVal);
			getThreadLocalResponse().addCookie(userCookie);
		}
	}

	/**
	 * @see com.gdevelop.gwt.syncrpc.spawebtest.client.CookieService#getSessionAttrib()
	 */
	@Override
	public String getSessionAttrib() {
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		return (String) session.getAttribute(SESSION_ATTRIB);
	}

	@Override
	public void invalidateAllSessions() {
		SessionsTracker tracker = SessionsTracker
				.getInstance(getServletContext());
		tracker.destroyAllSessions();
	}

	/**
	 * @see com.gdevelop.gwt.syncrpc.spawebtest.client.CookieService#setSessionAttrib(java.lang.String)
	 */
	@Override
	public void setSessionAttrib(String extra) {
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		session.setAttribute(SESSION_ATTRIB, extra);
	}
}
