/**
 * Jan 9, 2015 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public interface CookieServiceAsync {
	void echoCookiesFromClient(AsyncCallback<ArrayList<String>> callback);

	void generateCookiesOnServer(AsyncCallback<Void> callback);

	void getSessionAttrib(AsyncCallback<String> callback);

	void invalidateAllSessions(AsyncCallback<Void> callback);

	void setSessionAttrib(String extra, AsyncCallback<Void> callback);
}
