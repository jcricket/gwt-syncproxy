package com.gdevelop.gwt.syncrpc.android.auth;

import android.content.Context;

/**
 * Defines access of the Server's OAuth 2.0 Client Id.
 *
 * @author Preethum
 * @since 0.6
 */
public interface GoogleOAuthIdManager {
	public String getServerClientId(Context context);
}
