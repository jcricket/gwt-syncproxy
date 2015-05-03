package com.gdevelop.gwt.syncrpc.server.auth;

public interface ClientIdManager {
	String[] getAllClients();

	String getServerAudience();
}
