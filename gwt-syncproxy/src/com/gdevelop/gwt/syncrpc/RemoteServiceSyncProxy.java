/*
 * Copyright www.gdevelop.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.gwt.user.client.rpc.RpcToken;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.RpcTokenExceptionHandler;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * Base on {@link com.google.gwt.user.client.rpc.impl.RemoteServiceProxy}
 */
public class RemoteServiceSyncProxy implements SerializationStreamFactory {
	public static class DummySerializationPolicy extends SerializationPolicy {
		@Override
		public boolean shouldDeserializeFields(Class<?> clazz) {
			return clazz != null;
		}

		@Override
		public boolean shouldSerializeFields(Class<?> clazz) {
			return clazz != null;
		}

		@Override
		public void validateDeserialize(Class<?> clazz)
				throws SerializationException {
		}

		@Override
		public void validateSerialize(Class<?> clazz)
				throws SerializationException {
		}
	}

	public static boolean isReturnValue(String encodedResponse) {
		return encodedResponse.startsWith("//OK");
	}

	public static boolean isThrownException(String encodedResponse) {
		return encodedResponse.startsWith("//EX");
	}

	private final String moduleBaseURL;
	private final String remoteServiceURL;
	private final String serializationPolicyName;
	private SerializationPolicy serializationPolicy;

	private final CookieManager cookieManager;

	private final RpcToken rpcToken;

	RpcTokenExceptionHandler rpcTokenExceptionHandler;

	boolean ignoreResponse = false;
	static Logger logger = Logger.getLogger(RemoteServiceSyncProxy.class
			.getName());

	public RemoteServiceSyncProxy(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			CookieManager cookieManager, RpcToken rpcToken,
			RpcTokenExceptionHandler rpcTokenExceptionHandler) {
		this.moduleBaseURL = moduleBaseURL;
		this.remoteServiceURL = moduleBaseURL + remoteServiceRelativePath;
		this.serializationPolicyName = serializationPolicyName;
		this.cookieManager = cookieManager;
		this.rpcToken = rpcToken;
		this.rpcTokenExceptionHandler = rpcTokenExceptionHandler;
		if (serializationPolicyName == null) {
			this.serializationPolicy = new DummySerializationPolicy();
		} else {
			String policyFileName = SerializationPolicyLoader
					.getSerializationPolicyFileName(serializationPolicyName);
			InputStream is = getClass().getResourceAsStream(
					"/" + policyFileName);
			try {
				if (is == null) {
					logger.warning("Unable to get policy file from stream, attempting cache: "
							+ policyFileName + " at base: " + moduleBaseURL);
					// Try to get from cache
					String text = RpcPolicyFinder
							.getCachedPolicyFile(moduleBaseURL + policyFileName);
					if (text != null) {
						is = new ByteArrayInputStream(text.getBytes("UTF8"));
					}
				}
				this.serializationPolicy = SerializationPolicyLoader
						.loadFromStream(is, null);
			} catch (Exception e) {
				throw new InvocationException(
						"Error while loading serialization policy "
								+ serializationPolicyName, e);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// Ignore this error
					}
				}
			}
		}
	}

	@Override
	public SyncClientSerializationStreamReader createStreamReader(String encoded)
			throws SerializationException {
		SyncClientSerializationStreamReader reader = new SyncClientSerializationStreamReader(
				this.serializationPolicy);
		logger.finer("Preparing Stream Reader");
		reader.prepareToRead(encoded);
		logger.finer("Stream Reader Prepared");
		return reader;
	}

	@Override
	public SyncClientSerializationStreamWriter createStreamWriter() {
		SyncClientSerializationStreamWriter streamWriter = new SyncClientSerializationStreamWriter(
				null, this.moduleBaseURL, this.serializationPolicyName,
				this.serializationPolicy, this.rpcToken);
		streamWriter.prepareToWrite();

		return streamWriter;
	}

	public Object doInvoke(
			RequestCallbackAdapter.ResponseReader responseReader,
			String requestData) throws Throwable {
		// Workaround for unknown reset of the logger
		logger.setLevel(SyncProxy.getLoggingLevel());
		HttpURLConnection connection = null;
		InputStream is = null;
		int statusCode;
		logger.info("Send request to " + this.remoteServiceURL);
		logger.fine("Request payload: " + requestData);

		// Send request
		CookieHandler oldCookieHandler = CookieHandler.getDefault();
		try {
			CookieHandler.setDefault(this.cookieManager);
			logger.config("Starting Request sending to "
					+ this.remoteServiceURL);
			URL url = new URL(this.remoteServiceURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty(RpcRequestBuilder.STRONG_NAME_HEADER,
					this.serializationPolicyName);
			connection.setRequestProperty(RpcRequestBuilder.MODULE_BASE_HEADER,
					this.moduleBaseURL);
			connection.setRequestProperty("Content-Type",
					"text/x-gwt-rpc; charset=utf-8");
			connection.setRequestProperty("Content-Length",
					"" + requestData.getBytes("UTF-8").length);
			// Patch for Issue 21 - Modified to only send cookies for
			// moduleBaseURL host and sets the domain/path for the cookie in the
			// event
			// it is a user-added cookie without those values specified
			CookieStore store = this.cookieManager.getCookieStore();
			// Create the URI with port if specified
			URI uri = URI.create("http://"
					+ URI.create(this.moduleBaseURL).getHost());
			String domain = URI.create(this.moduleBaseURL).getHost();
			String path = URI.create(this.remoteServiceURL).getPath();
			logger.fine("Cookie target uri: " + uri);
			logger.config("Setting cookies:"
					+ this.cookieManager.getCookieStore().get(uri));
			for (HttpCookie cookie : store.get(uri)) {
				// Domain must be specified on Cookie to be passed along in
				// Android
				if (cookie.getDomain() == null) {
					logger.finer("Setting domain for Cookie: "
							+ cookie.getName() + " to " + domain);
					cookie.setDomain(domain);
				}
				// Path must be specified on Cookie to be passed along in POJ
				// and Android
				if (cookie.getPath() == null) {
					logger.finer("Setting path for Cookie: " + cookie.getName()
							+ " to " + path);
					cookie.setPath(path);
				}
			}
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(requestData);
			writer.flush();
			writer.close();
			// get all headers
			logger.fine("Checking Response");
			Map<String, List<String>> map2 = connection.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : map2.entrySet()) {
				logger.finer(entry.getKey() + " : " + entry.getValue());
			}

		} catch (IOException e) {
			throw new InvocationException(
					"IOException while sending RPC request", e);
		} finally {
			CookieHandler.setDefault(oldCookieHandler);
		}

		// Receive and process response
		try {
			statusCode = connection.getResponseCode();
			is = connection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) > 0) {
				baos.write(buffer, 0, len);
			}
			String encodedResponse = baos.toString("UTF8");
			logger.config("Response code: " + statusCode);
			logger.fine("Response payload: " + encodedResponse);
			logger.config("Post-Response cookies:"
					+ this.cookieManager
							.getCookieStore()
							.get(URI.create("http://"
									+ URI.create(this.moduleBaseURL).getHost())));
			if (statusCode != HttpURLConnection.HTTP_OK) {
				throw new StatusCodeException(statusCode, encodedResponse);
			} else if (encodedResponse == null) {
				// This can happen if the XHR is interrupted by the server dying
				throw new InvocationException("No response payload");
			} else if (isReturnValue(encodedResponse)) {
				logger.info("Reading return value");
				encodedResponse = encodedResponse.substring(4);
				return responseReader.read(createStreamReader(encodedResponse));
			} else if (isThrownException(encodedResponse)) {
				logger.info("Handling Thrown exception");
				encodedResponse = encodedResponse.substring(4);
				Throwable throwable = (Throwable) createStreamReader(
						encodedResponse).readObject();
				// Handle specific instance of RpcTokenException which may have
				// a specified handler
				if (throwable instanceof RpcTokenException
						&& this.rpcTokenExceptionHandler != null) {
					this.rpcTokenExceptionHandler
							.onRpcTokenException((RpcTokenException) throwable);
					this.ignoreResponse = true;
					return null;
				}
				throw throwable;
			} else {
				throw new InvocationException("Unknown response "
						+ encodedResponse);
			}
		} catch (IOException e) {
			// Handle Status Code 404 not found exception - Does not provide
			// full response data
			if (e.getCause() instanceof FileNotFoundException
					|| e instanceof FileNotFoundException) {
				throw new StatusCodeException(Response.SC_NOT_FOUND,
						"Not Found", null);
			}
			throw new InvocationException(
					"IOException while receiving RPC response", e);
		} catch (SerializationException e) {
			throw new InvocationException(
					"Error while deserialization response", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignore) {
				}
			}
			if (connection != null) {
				// connection.disconnect();
			}
		}
	}

	/**
	 * Specifically utilized if an RpcTokenException is returned and handled by
	 * a separate handler
	 *
	 * @return
	 */
	public boolean shouldIgnoreResponse() {
		return this.ignoreResponse;
	}
}
