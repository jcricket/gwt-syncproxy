package com.gdevelop.gwt.syncrpc;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.gdevelop.gwt.syncrpc.auth.DeviceServiceAuthenticationListener;
import com.gdevelop.gwt.syncrpc.auth.GoogleOAuthClientIdManager;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;
import com.gdevelop.gwt.syncrpc.auth.gae.JavaGAEOAuthBearerAuthenticator;
import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileServiceAsync;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.gdevelop.gwt.syncrpc.test.ui.BasicFrame;
import com.gdevelop.gwt.syncrpc.test.ui.OpenActionListener;
import com.google.gwt.user.client.rpc.AsyncCallback;

@SuppressWarnings("serial")
public class ManualAuthTest extends BasicFrame {
	public static void main(final String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		Logger.getLogger(JavaGAEOAuthBearerAuthenticator.class.getName()).setLevel(Level.FINE);
		logger.setLevel(Level.FINE);
		setLoggingLevel(Level.FINE);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ManualAuthTest ui = new ManualAuthTest();
				ui.createAndShowGUI();
				ui.init();
			}
		});
	}

	@Override
	protected JMenuBar createMenuBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAppTitle() {
		return "GSP Manual Auth Testing App";
	}

	@Override
	protected String getSummary() {
		return "";
	}

	@Override
	public void init() {
		initIdManager();
		initUI();
	}

	private void initIdManager() {
		idManager = new GoogleOAuthClientIdManagerImpl();
	}

	static Logger logger = Logger.getLogger(ManualAuthTest.class.getName());
	JavaGAEOAuthBearerAuthenticator authenticator;
	GoogleOAuthClientIdManager idManager;

	private void sendRPC() {
		/***************************************************************************/
		// This section is strictly to handle the stunnel local dev mode for GAE
		logger.info("Initializaing HTTPS STunnel Settings");
		SSLSocketFactory sslFactory = null;
		try {
			KeyStore localTrustStore = KeyStore.getInstance("JKS");
			// InputStream in = getClass().getClassLoader().getResourceAsStream(
			// "store.jks");
			File file = new File("store.jks");
			FileInputStream in = new FileInputStream(file);
			localTrustStore.load(in, "login1".toCharArray());
			// localTrustStore.getCertificate("server").
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(localTrustStore);
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, tmf.getTrustManagers(), null);
			sslFactory = ctx.getSocketFactory();
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			HttpsURLConnection.setDefaultSSLSocketFactory(sslFactory);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		/***************************************************************************/
		logger.info("Initializting Profile Service RPC");
		SyncProxy.setLoggingLevel(Level.FINER);
		SyncProxy.setBaseURL("http://192.168.1.107:8888/spawebtest/");
		// Send RPC to Profile Service
		ProfileServiceAsync service = SyncProxy.create(ProfileService.class);
		((HasProxySettings) service).setServiceAuthenticator(authenticator);
		logger.info("Sending Profile RPC");
		service.getOAuthProfile(new AsyncCallback<UserInfo>() {

			@Override
			public void onSuccess(UserInfo result) {
				logger.info("RPC Returned");
				returnedUser.setText(result.getEmail());
			}

			@Override
			public void onFailure(Throwable caught) {
				returnedUser.setText("Failed: " + caught.getMessage());
				throw new RuntimeException(caught);
			}
		});
	}

	private void initOAuth() {
		logger.info("Initializing OAUth Process");
		returnedUser.setText("");
		DeviceServiceAuthenticationListener listener = new DeviceServiceAuthenticationListener() {

			@Override
			public void onAuthenticatorPrepared(ServiceAuthenticator auth) {
				logger.info("Authenticator Prepared: " + auth.accountName());
				setStatus("Authenticator Prepared. Click verify to talk to App Engine");
				verify.setEnabled(true);
			}

			@Override
			public void onUserCodeAvailable(String userCode, String verificationUrl) {
				logger.info("User Code Available: " + userCode + "- " + verificationUrl);
				setStatus("Polling initiated, click URL to login");
				authorize.setText("Authorize: " + userCode);
				try {
					authorize.addActionListener(new OpenActionListener(new URI(verificationUrl),
							OpenActionListener.Type.WEB));
				} catch (URISyntaxException e) {
					throw new RuntimeException();
				}
				authorize.setEnabled(true);
			}
		};
		authenticator = new JavaGAEOAuthBearerAuthenticator(idManager, listener);
		ArrayList<String> testModeHosts = new ArrayList<>();
		testModeHosts.add("192.168.1.107");
		authenticator.setTestModeHosts(testModeHosts);
		logger.info("Preparing Authenticator");
		authenticator.prepareAuthentication();
	}

	JTextField returnedUser;
	JButton verify;
	JButton authorize;

	public static void setLoggingLevel(Level level) {
		// get the top Logger:
		Logger topLogger = java.util.logging.Logger.getLogger("");

		// Handler for console (reuse it if it already exists)
		Handler consoleHandler = null;
		// see if there is already a console handler
		for (Handler handler : topLogger.getHandlers()) {
			if (handler instanceof ConsoleHandler) {
				// found the console handler
				consoleHandler = handler;
				break;
			}
		}

		if (consoleHandler == null) {
			// there was no console handler found, create a new one
			consoleHandler = new ConsoleHandler();
			topLogger.addHandler(consoleHandler);
		}
		// set the console handler to fine:
		consoleHandler.setLevel(level);
	}

	protected void initUI() {
		this.setTitle(getAppTitle());
		JPanel main = new JPanel(new BorderLayout());
		getContentPane().add(main, BorderLayout.CENTER);

		JPanel oAuthPanel = new JPanel(new FlowLayout());
		JButton initOAuth = new JButton("Init OAuth");
		initOAuth.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				verify.setEnabled(false);
				initOAuth();
			}
		});
		verify = new JButton("Verify");
		verify.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendRPC();
			}
		});
		authorize = new JButton("Authorize");
		authorize.setEnabled(false);
		verify.setEnabled(false);
		oAuthPanel.add(initOAuth);
		oAuthPanel.add(verify);
		oAuthPanel.add(authorize);
		JLabel userLabel = new JLabel("Returned User");
		returnedUser = new JTextField(20);
		JPanel userSet = new JPanel(new FlowLayout());
		userSet.add(userLabel);
		userSet.add(returnedUser);
		oAuthPanel.add(userSet);
		main.add(oAuthPanel, BorderLayout.CENTER);

		validate();
	}

	@Override
	public void onWindowClose() {
		System.exit(0);
	}

}
