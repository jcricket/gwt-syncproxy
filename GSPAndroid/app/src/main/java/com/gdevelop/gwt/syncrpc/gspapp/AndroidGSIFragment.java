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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		AndroidGSIAuthenticator.Builder builder = new AndroidGSIAuthenticator.Builder(new ServiceAuthenticationListener() {

			@Override
			public void onAuthenticatorPrepared(ServiceAuthenticator authenticator) {
				EditText selected = (EditText) getActivity().findViewById(R.id.choosen_account);
				selected.setText(authenticator.accountName());
				reapply();
			}
		}, manager).onActivityResult(requestCode, resultCode, data);
		authenticator = builder.build();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidGSIAuthenticator.Builder builder = new AndroidGSIAuthenticator.Builder(new ServiceAuthenticationListener() {

			@Override
			public void onAuthenticatorPrepared(ServiceAuthenticator authenticator) {
				EditText selected = (EditText) getActivity().findViewById(R.id.choosen_account);
				selected.setText(authenticator.accountName());
				verify.setEnabled(true);
			}
		}, manager).signIn(getActivity());
		authenticator = builder.build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_gspagsi, null);
		verify = (Button) root.findViewById(R.id.verify_button);
		prepare = (SignInButton) root.findViewById(R.id.prepare_auth);
		serviceProgress = (TextView) root.findViewById(R.id.serviceProgerss);
		reapply = (Button) root.findViewById(R.id.reapply);
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();

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

	private void reapplyNewAuth() {
		AndroidGSIAuthenticator.Builder builder = new AndroidGSIAuthenticator.Builder(new ServiceAuthenticationListener() {

			@Override
			public void onAuthenticatorPrepared(ServiceAuthenticator authenticator) {
				EditText selected = (EditText) getActivity().findViewById(R.id.choosen_account);
				selected.setText(authenticator.accountName());
				reapply();
			}
		}, manager).forAccount(verifiedAccount);
		authenticator = builder.build();
	}

	private void reapply() {
		EditText returned = (EditText) getActivity().findViewById(R.id.returned_account);
		returned.setText("");
		serviceProgress.setText("");
		/***************************************************************************/
		// This section is strictly to handle the stunnel local dev mode for
		// GAE. The alternative to the STunnel option is provide the whitelist
		// server hosts in the string-array resource gsp_no_ssl_whitelist

		// TODO Create Proguard Config that will remove the STunnel class
		// if (BuildConfig.DEBUG) {
		// STunnel.reconfig(getActivity(), R.raw.server, "login1");
		// }
		/***************************************************************************/
		ServiceAsyncTask<ProfileServiceAsync, UserInfo> serviceTask = new ServiceAsyncTask<ProfileServiceAsync, UserInfo>(ProfileService.class, getActivity(), R.string.gsp_base, authenticator, new AsyncCallback<UserInfo>() {

			@Override
			public void onFailure(Throwable arg0) {
				EditText returned = (EditText) getActivity().findViewById(R.id.returned_account);
				returned.setText("Exception: " + arg0.getMessage());
				throw new RuntimeException(arg0);
			}

			@Override
			public void onSuccess(UserInfo arg0) {
				EditText returned = (EditText) getActivity().findViewById(R.id.returned_account);
				verifiedAccount = arg0.getEmail();
				returned.setText(arg0.getEmail());
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

	private void verify() {
		EditText returned = (EditText) getActivity().findViewById(R.id.returned_account);
		returned.setText("");
		serviceProgress.setText("");
		/***************************************************************************/
		// This section is strictly to handle the stunnel local dev mode for
		// GAE. The alternative to the STunnel option is provide the whitelist
		// server hosts in the string-array resource gsp_no_ssl_whitelist

		// TODO Create Proguard Config that will remove the STunnel class
		// if (BuildConfig.DEBUG) {
		// STunnel.reconfig(getActivity(), R.raw.server, "login1");
		// }
		/***************************************************************************/
		ServiceAsyncTask<ProfileServiceAsync, UserInfo> serviceTask = new ServiceAsyncTask<ProfileServiceAsync, UserInfo>(ProfileService.class, getActivity(), R.string.gsp_base, authenticator, new AsyncCallback<UserInfo>() {

			@Override
			public void onFailure(Throwable arg0) {
				EditText returned = (EditText) getActivity().findViewById(R.id.returned_account);
				returned.setText("Exception: " + arg0.getMessage());
				throw new RuntimeException(arg0);
			}

			@Override
			public void onSuccess(UserInfo arg0) {
				EditText returned = (EditText) getActivity().findViewById(R.id.returned_account);
				verifiedAccount = arg0.getEmail();
				returned.setText(arg0.getEmail());
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
}
