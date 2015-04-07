package com.gdevelop.gwt.syncrpc.spaapptest;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gdevelop.gwt.syncrpc.android.ServiceAsyncTask;
import com.gdevelop.gwt.syncrpc.android.ServiceTaskProgress;
import com.gdevelop.gwt.syncrpc.android.auth.GoogleOAuthIdManager;
import com.gdevelop.gwt.syncrpc.android.auth.gae.AndroidGAECrossClientAuthenticator;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticationListener;
import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileServiceAsync;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AndroidGAECrossClientFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_gspagaecc, null);
		verify = (Button) root.findViewById(R.id.verify_button);
		prepare = (Button) root.findViewById(R.id.prepare_auth);
		serviceProgress = (TextView) root.findViewById(R.id.serviceProgerss);
		return root;
	}

	Button prepare;
	TextView serviceProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		authenticator = new AndroidGAECrossClientAuthenticator(getActivity(),
				this, manager, new ServiceAuthenticationListener() {

					@Override
					public void onAuthenticatorPrepared(String accountName) {
						EditText selected = (EditText) getActivity()
								.findViewById(R.id.choosen_account);
						selected.setText(accountName);
						verify.setEnabled(true);
					}
				});

	};

	GoogleOAuthIdManager manager = new GoogleOAuthIdManager() {

		@Override
		public String getServerClientId(Context context) {
			return context.getString(R.string.gae_client_id);
		}
	};

	/**
	 * http://stackoverflow.com/questions/13917984/accept-self-signed-ssl-
	 * certificates-where-to-set-default-trustmanager
	 *
	 * http://stackoverflow.com/questions/859111/how-do-i-accept-a-self-signed-
	 * certificate-with-a-java-httpsurlconnection
	 */
	private void verify() {
		EditText returned = (EditText) getActivity().findViewById(
				R.id.returned_account);
		returned.setText("");
		serviceProgress.setText("");
		/***************************************************************************/
		// This section is strictly to handle the stunnel local dev mode for GAE
		SSLSocketFactory sslFactory = null;
		try {
			KeyStore localTrustStore = KeyStore.getInstance("BKS");
			InputStream in = getResources().openRawResource(R.raw.server);
			localTrustStore.load(in, "login1".toCharArray());
			// localTrustStore.getCertificate("server").
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(localTrustStore);
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, tmf.getTrustManagers(), null);
			sslFactory = ctx.getSocketFactory();
			HttpsURLConnection
					.setDefaultHostnameVerifier(new HostnameVerifier() {

				@Override
				public boolean verify(String hostname,
						SSLSession session) {
					return true;
				}
			});
			HttpsURLConnection.setDefaultSSLSocketFactory(sslFactory);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		/***************************************************************************/
		ServiceAsyncTask<ProfileServiceAsync, UserInfo> serviceTask = new ServiceAsyncTask<ProfileServiceAsync, UserInfo>(
				ProfileService.class, R.string.gsp_base, authenticator,
				new AsyncCallback<UserInfo>() {

					@Override
					public void onSuccess(UserInfo arg0) {
						EditText returned = (EditText) getActivity()
								.findViewById(R.id.returned_account);
						returned.setText(arg0.getEmail());
					}

					@Override
					public void onFailure(Throwable arg0) {
						EditText returned = (EditText) getActivity()
								.findViewById(R.id.returned_account);
						returned.setText("Exception: " + arg0.getMessage());
						throw new RuntimeException(arg0);
					}
				}) {

			@Override
			public void serviceCall() {
				getAsyncService().getOAuthProfile(getCallback());

			}

			/**
			 * Not typically required, but useful to get status from the service
			 * task
			 */
			@Override
			protected void onProgressUpdate(ServiceTaskProgress... values) {
				super.onProgressUpdate(values);
				serviceProgress.setText(values[0].toString());
			}
		};
		serviceTask.execute(getActivity());
	}

	AndroidGAECrossClientAuthenticator authenticator;
	Button verify;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (!authenticator.onActivityResult(requestCode, resultCode, data)) {
				super.onActivityResult(requestCode, resultCode, data);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		verify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				verify();
			}
		});
		verify.setEnabled(false);
		prepare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				authenticator.prepareAuthentication();
			}
		});

	}
}
