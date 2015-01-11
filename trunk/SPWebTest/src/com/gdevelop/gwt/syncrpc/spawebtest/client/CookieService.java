/**
 * Jan 9, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author Preethum
 * @since 0.5
 *
 */
@RemoteServiceRelativePath("cookieservice")
public interface CookieService extends RemoteService {
	public static final String[] COOKIE_VALS = { "COOKIE_VAL1", "COOKIE_VAL2" };

	public static final String SESSION_ATTRIB = "TestSession";

	ArrayList<String> echoCookiesFromClient();

	void generateCookiesOnServer();

	String getSessionAttrib();

	void invalidateAllSessions();

	void setSessionAttrib(String extra);
}
