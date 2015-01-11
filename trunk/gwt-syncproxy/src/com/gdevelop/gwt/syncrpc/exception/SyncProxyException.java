/**
 * Dec 29, 2014 Copyright Blue Esoteric Web Development, LLC
 * Contact: P.Prith@BlueEsoteric.com
 */
package com.gdevelop.gwt.syncrpc.exception;

import java.util.Arrays;

import com.gdevelop.gwt.syncrpc.RemoteServiceSyncProxy;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author Preethum
 * @since 0.5
 *
 */
public class SyncProxyException extends InvocationException {
	public enum InfoType {
		/**
		 * Used to indicate that the relative path for the service request was
		 * not provided manually and not able to be determined from the
		 * Synchronous interface's annotation value of
		 * {@link RemoteServiceRelativePath}
		 */
		REMOTE_SERVICE_RELATIVE_PATH(
				"Check RemoteServiceRelativePath annotation on service"), MODULE_BASE_URL(
						"Set SyncProxy Base Url"), POLICY_NAME_POPULATION(
				"Unable to populate policy names, see below exception."), POLICY_NAME_MISSING(
				"Unable to locate policy name. See FAQ"),
		/**
		 * Used to indicate that an attempt to find the synchronous service
		 * interface based on a provided Asynchronous service interface failed.
		 */
		SERVICE_BASE("Make sure your service classes are on the classpath"),

		/**
										 * Used to indicate the use of the
										 * {@link ServiceDefTarget#setServiceEntryPoint(String)} where the
										 * moduleBaseUrl is different. This occurs because with a different base
										 * url, we have no good way of separating the provided EntryPoint into
										 * moduleBase and remoteServiceRelativePath. The moduleBase is needed
										 * separately in
										 * {@link RemoteServiceSyncProxy#doInvoke(com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader, String)}
										 *
										 */
										SERVICE_BASE_DELTA(
												"Unable to determine new module base url from provided service entry point.");
		String help;

		InfoType(String help) {
			this.help = help;
		}
	}

	private static final long serialVersionUID = 1L;
	InfoType type;

	public SyncProxyException(Class<?> service, InfoType type) {
		super("Missing " + type + " for service " + service.getName() + ". "
				+ type.help);
		this.type = type;
	}

	public SyncProxyException(Class<?> service, InfoType type, Exception e) {
		super("Missing " + type + " for service " + service.getName() + ". "
				+ type.help, e);
		this.type = type;
	}

	public SyncProxyException(InfoType type, Exception e) {
		super("Error " + type + ". " + type.help, e);
		this.type = type;
	}

	public InfoType getType() {
		return this.type;
	}

	/**
	 * Quickly verify's if this exceptions error type is in the provided set. If
	 * not, this exception is re-thrown.
	 *
	 * @param type
	 */
	public void verify(InfoType... types) {
		if (!Arrays.asList(types).contains(this.type)) {
			throw this;
		}
	}
}
