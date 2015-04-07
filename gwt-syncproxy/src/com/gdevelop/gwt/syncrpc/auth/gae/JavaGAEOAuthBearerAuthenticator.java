package com.gdevelop.gwt.syncrpc.auth.gae;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.gdevelop.gwt.syncrpc.HasProxySettings;
import com.gdevelop.gwt.syncrpc.auth.DeviceServiceAuthenticationListener;
import com.gdevelop.gwt.syncrpc.auth.GoogleOAuthClientIdManager;
import com.gdevelop.gwt.syncrpc.auth.HasOAuthBearerToken;
import com.gdevelop.gwt.syncrpc.auth.HasOAuthTokens;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;
import com.google.gson.Gson;

/**
 * TODO Future option to get JWT Id Token. Once access/refresh token is
 * available, make sure scope is at least "openid email", then refresh the
 * token. The response should include an id_token field.
 *
 * https://developers.google.com/accounts/docs/OAuth2ForDevices used a base for
 * code outlines
 *
 * @author Preethum
 * @since 0.5.1
 *
 */
public class JavaGAEOAuthBearerAuthenticator implements ServiceAuthenticator,
HasOAuthBearerToken, HasOAuthTokens {
	DeviceServiceAuthenticationListener listener;
	boolean autoStartPolling = true;
	GoogleOAuthClientIdManager idManager;
	public static final String OAUTH_DEVICE_CODE_URL = "https://accounts.google.com/o/oauth2/device/code";
	public static final String OAUTH_TOKEN_URL = "https://www.googleapis.com/oauth2/v3/token";
	public static final String OAUTH_TOKEN_GRANT_TYPE_DEVICE = "http://oauth.net/grant_type/device/1.0";
	public static final String OAUTH_TOKEN_GRANT_TYPE_REFRESH = "refresh_token";
	public static final String OAUTH_SCOPE = "email profile";

	public JavaGAEOAuthBearerAuthenticator(
			GoogleOAuthClientIdManager idManager,
			DeviceServiceAuthenticationListener listener) {
		this.idManager = idManager;
		this.listener = listener;
	}

	/**
	 * Amount of time before access code expires that this class will attempt to
	 * get a new access code
	 */
	public static final int DEFAULT_REFRESH_LEAD = 30;
	int refreshLeadTime = DEFAULT_REFRESH_LEAD;

	/**
	 *
	 * @param refreshLeadTime
	 *            number of seconds before an access code expires that the
	 *            system should attempt to get a new access code
	 */
	public void setRefreshLeadTime(int refreshLeadTime) {
		this.refreshLeadTime = refreshLeadTime;
	}

	/**
	 * Initiates the device authentication process as described here:
	 * https://developers.google.com/accounts/docs/OAuth2ForDevices. This
	 * process will call the
	 * {@link DeviceServiceAuthenticationListener#onUserCodeAvailable(String, String)}
	 * when the client should prompt the user to authorize this service. If the
	 * {@link #autoStartPolling} value is true, then this will initiate polling
	 * right away. Once polling verify's authentication, it will retrieve the
	 * access and refresh tokens.
	 *
	 * Once these tokens (the access token is the Bearer token) are available,
	 * the
	 * {@link DeviceServiceAuthenticationListener#onAuthenticatorPrepared(String)}
	 * method will be called.
	 */
	@Override
	public void prepareAuthentication() {
		try {
			URL url = new URL(OAUTH_DEVICE_CODE_URL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			String requestBody = String
					.format("client_id=%s&scope=%s",
							idManager.getClientId(),
							URLEncoder.encode(OAUTH_SCOPE, "UTF-8").replace(
									"+", "%20"));
			logger.config("Request Body: " + requestBody);
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(requestBody);
			writer.flush();
			writer.close();

			int statusCode = connection.getResponseCode();
			logger.config("Response code: " + statusCode);
			InputStream is = connection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) > 0) {
				baos.write(buffer, 0, len);
			}
			String encodedResponse = baos.toString("UTF8");
			logger.fine("Response payload: " + encodedResponse);

			// Map<String, Object> retMap = new Gson().fromJson(encodedResponse,
			// new TypeToken<HashMap<String, Object>>() {}.getType());
			deviceCodeResponse = new Gson().fromJson(encodedResponse,
					OAuth2DeviceCodeResponse.class);
			listener.onUserCodeAvailable(deviceCodeResponse.getUser_code(),
					deviceCodeResponse.getVerification_url());
			if (autoStartPolling) {
				logger.config("Auto-initiating polling");
				initiatePolling();
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	OAuth2DeviceCodeResponse deviceCodeResponse;
	static Logger logger = Logger
			.getLogger(JavaGAEOAuthBearerAuthenticator.class.getName());

	/**
	 * Initiates polling for the authorization for this app to access.
	 */
	public void initiatePolling() {
		logger.info("Initializing Polling");
		continuePolling = true;
		scheduler.schedule(pollingTask, deviceCodeResponse.getInterval(),
				TimeUnit.SECONDS);
		scheduler.schedule(new Runnable() {

			@Override
			public void run() {
				logger.info("Discontinuing polling due to timeout");
				continuePolling = false;
			}
		}, deviceCodeResponse.getExpires_in(), TimeUnit.SECONDS);
	}

	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);
	Runnable pollingTask = new Runnable() {

		@Override
		public void run() {
			logger.fine("Task to Call poller");
			pollOAuthService();
		}
	};
	Runnable refreshTask = new Runnable() {

		@Override
		public void run() {
			logger.fine("Task to call refresh token");
			refreshAccessToken();
		}
	};

	boolean continuePolling = false;

	protected void pollOAuthService() {
		logger.info("Polling auth service");
		if (!continuePolling) {
			logger.fine("Breaking out of polling cycle");
			// Break out of polling cycle if device code expired
			return;
		}
		try {
			URL url = new URL(OAUTH_TOKEN_URL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			String requestBody = String.format(
					"client_id=%s&client_secret=%s&code=%s&grant_type=%s",
					idManager.getClientId(), idManager.getClientSecret(),
					deviceCodeResponse.getDevice_code(),
					OAUTH_TOKEN_GRANT_TYPE_DEVICE);
			logger.fine("Polling Request Body: " + requestBody);
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(requestBody);
			writer.flush();
			writer.close();

			int statusCode = connection.getResponseCode();
			logger.config("Response code: " + statusCode);
			// TODO if bad response code use #getErrorStream for response Body
			if (statusCode != 200 && statusCode != 400) {
				InputStream es = connection.getErrorStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len;
				while ((len = es.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				String encodedResponse = baos.toString("UTF8");
				logger.fine("Error payload: " + encodedResponse);
				return;
			}

			String encodedResponse = null;
			InputStream is = null;
			if (statusCode == 200) {
				is = connection.getInputStream();
			} else if (statusCode == 400) {
				is = connection.getErrorStream();
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) > 0) {
				baos.write(buffer, 0, len);
			}
			encodedResponse = baos.toString("UTF8");

			logger.fine("Response payload: " + encodedResponse);
			tokenResponse = new Gson().fromJson(encodedResponse,
					OAuth2TokenResponse.class);
			if (tokenResponse.hasError()) {
				logger.warning("Token Response error");
				if (tokenResponse.getError().equals(
						OAuth2TokenResponse.ERROR_AUTH_PENDING)) {
					logger.warning("Token Authorization Pending, re-scheduling polling task: "
							+ deviceCodeResponse.getInterval());
					scheduler.schedule(pollingTask,
							deviceCodeResponse.getInterval(), TimeUnit.SECONDS);
					return;
				} else if (tokenResponse.getError().equals(
						OAuth2TokenResponse.ERROR_SLOW_DOWN)) {
					logger.warning("Rapid Polling, adjusting poll rate");
					// As a slow down request, just double the future polling
					// intervals
					// TODO Catch if interval becomes too large and advise
					// client
					deviceCodeResponse.interval = deviceCodeResponse.interval * 2;
					logger.warning("Re-scheduling polling task: "
							+ deviceCodeResponse.interval);
					scheduler.schedule(pollingTask,
							deviceCodeResponse.getInterval(), TimeUnit.SECONDS);
					return;
				} else {
					// Unexpected
					continuePolling = false;
					throw new RuntimeException("Unexpected polling error: "
							+ tokenResponse.getError());
				}
			} else {
				continuePolling = false;
				logger.info("Polling authorized");
				if (!tokenResponse.getToken_type().equals("Bearer")) {
					throw new RuntimeException("Unexpected token type: "
							+ tokenResponse.getToken_type());
				}
				listener.onAuthenticatorPrepared(null);
				// Schedule the ability to automatically refresh the access
				// token so that updated bearer codes will be applied to service
				// calls automatically
				logger.info("scheduling refresh handler: "
						+ (tokenResponse.getExpires_in() - refreshLeadTime));
				refreshHandle = scheduler.schedule(refreshTask,
						tokenResponse.getExpires_in() - refreshLeadTime,
						TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	protected void refreshAccessToken() {
		if (!refreshEnabled) {
			return;
		}
		try {
			logger.info("Refreshing access token");
			URL url = new URL(OAUTH_TOKEN_URL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			String requestBody = String
					.format("client_id=%s&client_secret=%s&refresh_token=%s&grant_type=%s",
							idManager.getClientId(),
							idManager.getClientSecret(),
							tokenResponse.getRefresh_token(),
							OAUTH_TOKEN_GRANT_TYPE_REFRESH);

			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(requestBody);
			writer.flush();
			writer.close();

			int statusCode = connection.getResponseCode();
			logger.config("Response code: " + statusCode);
			InputStream is = connection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) > 0) {
				baos.write(buffer, 0, len);
			}
			String encodedResponse = baos.toString("UTF8");

			logger.fine("Response payload: " + encodedResponse);
			tokenResponse = new Gson().fromJson(encodedResponse,
					OAuth2TokenResponse.class);

			if (tokenResponse.hasError()) {
				throw new RuntimeException("Unexpected token refresh error: "
						+ tokenResponse.getError());
			}
			// Schedule the ability to automatically refresh the access
			// token so that updated bearer codes will be applied to service
			// calls automatically
			logger.info("Scheduling refresh handler: "
					+ (tokenResponse.getExpires_in() - refreshLeadTime));
			refreshHandle = scheduler.schedule(refreshTask,
					tokenResponse.getExpires_in() - refreshLeadTime,
					TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	boolean refreshEnabled = true;

	public void disableRefresh() {
		logger.info("Disabling auto-refresh");
		refreshEnabled = false;
		refreshHandle.cancel(true);
	}

	ScheduledFuture<?> refreshHandle;
	OAuth2TokenResponse tokenResponse;

	/**
	 * Applies the Bearer
	 */
	@Override
	public void applyAuthenticationToService(HasProxySettings service) {
		logger.info("Applying Bearer token to service: "
				+ service.getClass().getName());
		service.setOAuthBearerToken(tokenResponse.getAccess_token());
	}

	@Override
	public String getBearerToken() {
		return tokenResponse.getAccess_token();
	}

	@Override
	public String getAccessToken() {
		return tokenResponse.getAccess_token();
	}

	@Override
	public String getRefreshToken() {
		return tokenResponse.getRefresh_token();
	}

}
