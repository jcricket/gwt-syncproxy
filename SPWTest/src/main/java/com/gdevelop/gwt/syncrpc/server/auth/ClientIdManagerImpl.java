package com.gdevelop.gwt.syncrpc.server.auth;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

public class ClientIdManagerImpl implements ClientIdManager {
	public enum ClientId {
		GAE("gsp.clientid.gae"), ANDROID("gsp.clientid.android"), ANDROID_NEW("gsp.clientid.androidnew");
		String propName;

		private ClientId(String propName) {
			this.propName = propName;
		}

		public String getPropName() {
			return propName;
		}

	}

	ServletContext context;

	public ClientIdManagerImpl(ServletContext context) {
		this.context = context;
	}

	@Override
	public String[] getAllClients() {
		return new String[] { getClientId(ClientId.GAE, context), getClientId(ClientId.ANDROID, context),
				getClientId(ClientId.ANDROID_NEW, context) };
	}

	@Override
	public String getServerAudience() {
		return getClientId(ClientId.GAE, context);
	}

	protected String getClientId(ClientId idType, ServletContext context) {
		InputStream is = context.getResourceAsStream("/WEB-INF/client_ids.properties");
		Properties props = new Properties();
		try {
			props.load(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return props.getProperty(idType.getPropName()).trim();
	}
}
