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
import com.gdevelop.gwt.syncrpc.auth.ServiceAuthenticator;
import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileService;
import com.gdevelop.gwt.syncrpc.spawebtest.client.ProfileServiceAsync;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author Preethum
 * @since 0.6
 */
public class AndroidGAECrossClientFragment extends Fragment {
	Button prepare;
	TextView serviceProgress;
	GoogleOAuthIdManager manager = new GoogleOAuthIdManager() {

		@Override
		public String getServerClientId(Context context) {
			return context.getString(R.string.gae_client_id);
		}
	};
	AndroidGAECrossClientAuthenticator authenticator;

	;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		authenticator = new AndroidGAECrossClientAuthenticator(getActivity(), this, manager,
				new ServiceAuthenticationListener() {

					@Override
					public void onAuthenticatorPrepared(ServiceAuthenticator authenticator) {
						EditText selected = (EditText) getActivity().findViewById(R.id.choosen_account);
						selected.setText(authenticator.accountName());
						verify.setEnabled(true);
					}
				});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_gspagaecc, null);
		verify = (Button) root.findViewById(R.id.verify_button);
		prepare = (Button) root.findViewById(R.id.prepare_auth);
		serviceProgress = (TextView) root.findViewById(R.id.serviceProgerss);
		return root;
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

	/**
	 */
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
		ServiceAsyncTask<ProfileServiceAsync, UserInfo> serviceTask = new ServiceAsyncTask<ProfileServiceAsync, UserInfo>(
				ProfileService.class, getActivity(), R.string.gsp_base, authenticator, new AsyncCallback<UserInfo>() {

			@Override
			public void onFailure(Throwable arg0) {
				EditText returned = (EditText) getActivity().findViewById(R.id.returned_account);
				returned.setText("Exception: " + arg0.getMessage());
				throw new RuntimeException(arg0);
			}

			@Override
			public void onSuccess(UserInfo arg0) {
				EditText returned = (EditText) getActivity().findViewById(R.id.returned_account);
				returned.setText(arg0.getEmail());
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
		serviceTask.execute();
	}
}
