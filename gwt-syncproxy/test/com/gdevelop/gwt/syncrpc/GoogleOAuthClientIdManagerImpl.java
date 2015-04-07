package com.gdevelop.gwt.syncrpc;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.gdevelop.gwt.syncrpc.auth.GoogleOAuthClientIdManager;

public class GoogleOAuthClientIdManagerImpl implements
GoogleOAuthClientIdManager {
	public enum ClientId {
		JAVA_ID("gsp.clientid.java"), JAVA_SECRET("gsp.clientsecret.java");
		String propName;

		private ClientId(String propName) {
			this.propName = propName;
		}

		public String getPropName() {
			return propName;
		}

	}

	@Override
	public String getClientId() {
		return getClientValue(ClientId.JAVA_ID);
	}

	@Override
	public String getClientSecret() {
		return getClientValue(ClientId.JAVA_SECRET);
	}

	protected String getClientValue(ClientId idType) {
		Properties props = new Properties();
		try {
			File file = new File("client_ids.properties");
			FileInputStream fileInput = new FileInputStream(file);

			// InputStream is = getClass().getClassLoader().getResourceAsStream(
			// "/client_ids.properties");
			props.load(fileInput);
			fileInput.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return props.getProperty(idType.getPropName()).trim();
	}
}
