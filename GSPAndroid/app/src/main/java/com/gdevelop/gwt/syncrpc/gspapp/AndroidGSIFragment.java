/**
 * Copyright 2015 Blue Esoteric Web Development, LLC <http://www.blueesoteric.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc.gspapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gdevelop.gwt.syncrpc.android.ServiceAsyncTask;
import com.gdevelop.gwt.syncrpc.android.ServiceTaskProgress;
import com.gdevelop.gwt.syncrpc.android.auth.AndroidGSIAuthenticator;
import com.gdevelop.gwt.syncrpc.android.auth.GoogleOAuthIdManager;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticationListener;
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;
import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileServiceAsync;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.android.gms.common.SignInButton;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Preethum
 * @version 0.6.2
 * @since 0.6.2 - 11/19/2015
 */
public class AndroidGSIFragment extends Fragment {
	public static final String LOG_TAG = "AGSI_FRAG";
	GoogleOAuthIdManager manager = new GoogleOAuthIdManager() {

		@Override
		public String getServerClientId(Context context) {
			return context.getString(R.string.gae_client_id);
		}
	};
	SignInButton prepare;
	Button verify;
	Button reapply;
	TextView serviceProgress;
	AndroidGSIAuthenticator authenticator;
	String verifiedAccount;
	TextView selectedAcc;
	TextView returnedAcc;
	Button signOut;
	Button disconnect;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(LOG_TAG, "Activity Result authenticator building");
		AndroidGSIAuthenticator.Builder builder = new AndroidGSIAuthenticator.Builder(getActivity(), new ServiceAuthenticationListener() {

			@Override
			public void onAuthenticatorPrepared(ServiceAuthenticator authenticator) {
				Log.v(LOG_TAG, "Activity Result authenticator prepared");
				signedIn(authenticator.accountName());
				verify.setEnabled(true);
			}
		}, manager).onActivityResult(requestCode, resultCode, data);
//		if (authenticator != null) {
//			Log.d(LOG_TAG, "Activity Result disconnecting prior auth GAPI Client");
//			authenticator.disconnectGoogleApiClient();
//		}
		authenticator = builder.build();
		//		authenticator.prepareAuthentication();

	}

	private void signedIn(String accName) {
		selectedAcc.setText(accName);
		prepare.setVisibility(View.GONE);
		signOut.setVisibility(View.VISIBLE);
		disconnect.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buildSigninAuthenticator();
	}

	private void buildSigninAuthenticator() {
//		if (authenticator != null) {
//			Log.d(LOG_TAG, "Disconnecting prior authenticator GAPI Client");
//			authenticator.disconnectGoogleApiClient();
//		}
		AndroidGSIAuthenticator.Builder builder = new AndroidGSIAuthenticator.Builder(getActivity(), new ServiceAuthenticationListener() {

			@Override
			public void onAuthenticatorPrepared(ServiceAuthenticator authenticator) {
				Log.v(LOG_TAG, "Standard Authenticator prepared");

				String accName = authenticator.accountName();
				signedIn(accName);
				verify.setEnabled(true);
			}
		}, manager).signIn(this);
		authenticator = builder.build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_gspagsi, null);
		signOut = (Button) root.findViewById(R.id.sign_out_button);
		verify = (Button) root.findViewById(R.id.verify_button);
		prepare = (SignInButton) root.findViewById(R.id.prepare_auth);
		serviceProgress = (TextView) root.findViewById(R.id.serviceProgerss);
		reapply = (Button) root.findViewById(R.id.reapply);
		selectedAcc = (TextView) root.findViewById(R.id.choosen_account);
		returnedAcc = (TextView) root.findViewById(R.id.returned_account);
		disconnect = (Button) root.findViewById(R.id.disconnect_button);
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		signOut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signOut();
			}
		});
		disconnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				disconnect();
			}
		});
		signedOut();
		verify.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				verify();
			}
		});
		verify.setEnabled(false);
		prepare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				authenticator.prepareAuthentication();
			}
		});
		reapply.setEnabled(false);
		reapply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reapplyNewAuth();
				authenticator.prepareAuthentication();
			}
		});
	}

	private void disconnect() {
		authenticator.disconnect();
		signedOut();
		buildSigninAuthenticator();
	}

	private void signOut() {
		authenticator.signOut();
		signedOut();
	}

	private void signedOut() {
		selectedAcc.setText("");
		returnedAcc.setText("");
		prepare.setVisibility(View.VISIBLE);
		signOut.setVisibility(View.GONE);
		disconnect.setVisibility(View.GONE);
		verify.setEnabled(false);
		reapply.setEnabled(false);
	}

	private void reapplyNewAuth() {
		Log.i(LOG_TAG, "Building reapply authenticator");
		selectedAcc.setText("");
		returnedAcc.setText("");
		AndroidGSIAuthenticator.Builder builder = new AndroidGSIAuthenticator.Builder(getActivity(), new ServiceAuthenticationListener() {

			@Override
			public void onAuthenticatorPrepared(ServiceAuthenticator authenticator) {
				Log.v(LOG_TAG, "Reapply authenticator prepared");

				signedIn(authenticator.accountName());
				reapply();
			}
		}, manager).forAccount(verifiedAccount);
		//authenticator.disconnectGoogleApiClient();
		authenticator = builder.build();
	}

	private void verify() {
		Log.i(LOG_TAG, "Verifying");

		returnedAcc.setText("");
		serviceProgress.setText("");
		ServiceAsyncTask<ProfileServiceAsync, UserInfo> serviceTask = new ServiceAsyncTask<ProfileServiceAsync, UserInfo>(ProfileService.class, getActivity(), R.string.gsp_base, authenticator, new AsyncCallback<UserInfo>() {

			@Override
			public void onFailure(Throwable arg0) {

				returnedAcc.setText("Exception: " + arg0.getMessage());
				throw new RuntimeException(arg0);
			}

			@Override
			public void onSuccess(UserInfo arg0) {

				verifiedAccount = arg0.getEmail();
				returnedAcc.setText(arg0.getEmail());
				reapply.setEnabled(true);
			}
		}, null) {

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
		serviceTask.execute();
	}

	private void reapply() {
		Log.d(LOG_TAG, "Reapplying");

		returnedAcc.setText("");
		serviceProgress.setText("");
		ServiceAsyncTask<ProfileServiceAsync, UserInfo> serviceTask = new ServiceAsyncTask<ProfileServiceAsync, UserInfo>(ProfileService.class, getActivity(), R.string.gsp_base, authenticator, new AsyncCallback<UserInfo>() {

			@Override
			public void onFailure(Throwable arg0) {

				returnedAcc.setText("Exception: " + arg0.getMessage());
				throw new RuntimeException(arg0);
			}

			@Override
			public void onSuccess(UserInfo arg0) {

				verifiedAccount = arg0.getEmail();
				returnedAcc.setText(arg0.getEmail());
			}
		}, null) {

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
		serviceTask.execute();
	}

}