package com.gdevelop.gwt.syncrpc.spaapptest;

import java.net.CookieManager;
import java.util.ArrayList;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gdevelop.gwt.syncrpc.LoginUtils;
import com.gdevelop.gwt.syncrpc.ProxySettings;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.android.CookieManagerAvailableListener;
import com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingServiceAsync;
import com.gdevelop.gwt.syncrpc.spawebtest.shared.T1;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GreetFragment extends Fragment {
	class ActivateButton extends AsyncTask<Activity, Void, Activity> {
		GreetingServiceAsync rpcService;
		CookieManager cm;

		public ActivateButton(CookieManager cm) {
			this.cm = cm;
		}

		/**
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Activity doInBackground(Activity... act) {
			// Use 10.0.2.2 for Hosted Emulator Loopback interface
			SyncProxy.setBaseURL(MainActivity.BASE_URL + "/spawebtest/");
			this.rpcService = SyncProxy.createProxy(GreetingServiceAsync.class,
					new ProxySettings().setCookieManager(this.cm));
			return act[0];
		}

		private void greetServer(final Activity act) {
			// Greet Server Test for Random T1 object
			rpcService.greetServer(((EditText) act.findViewById(R.id.input))
					.getText().toString(), new AsyncCallback<String>() {
				@Override
				public void onFailure(Throwable e) {
					throw new RuntimeException(e);
				}

				@Override
				public void onSuccess(final String result) {
					act.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							final TextView tv = (TextView) act
									.findViewById(R.id.result);
							tv.setText(Html.fromHtml(result));
						}
					});
				}
			});

		}

		@Override
		protected void onPostExecute(final Activity act) {
			greetServerArr(act);
		}

		private void greetServer2(final Activity act) {
			// Greet Server Test for Regular String
			rpcService.greetServer2(((EditText) act.findViewById(R.id.input))
					.getText().toString(), new AsyncCallback<T1>() {
				@Override
				public void onFailure(Throwable e) {
					throw new RuntimeException(e);
				}

				@Override
				public void onSuccess(final T1 result) {
					act.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							final TextView tv = (TextView) act
									.findViewById(R.id.result);
							tv.setText(Html.fromHtml(result.getText()));
						}
					});
				}
			});
		}

		private void greetServerArr(final Activity act) {
			// Greet Server Test for ArrayList contain the String
			this.rpcService.greetServerArr(((EditText) act
					.findViewById(R.id.input)).getText().toString(),
					new AsyncCallback<ArrayList<String>>() {
						@Override
						public void onFailure(Throwable caught) {
							throw new RuntimeException(caught);
						}

						@Override
						public void onSuccess(final ArrayList<String> result) {
							act.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									final TextView tv = (TextView) act
											.findViewById(R.id.result);
									tv.setText(Html.fromHtml(result.get(0)));
								}
							});
						}
					});
		}
	}

	public static int MY_REQUEST_ID = 2222;
	public static final String BASE_URL = "http://192.168.1.125:8888";
	// Use this for Hosted Emulator Loopback Interface
	// public static final String BASE_URL = "http://10.0.2.2:8888";
	CookieManager cm;

	CookieManagerAvailableListener listener;

	boolean waitForCM = false;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_REQUEST_ID) {
			if (resultCode == Activity.RESULT_OK) {
				Account account = (Account) data.getExtras().get(
						LoginUtils.ACCOUNT_KEY);
				try {
					LoginUtils.loginAppEngine(getActivity(), this.listener,
							account);
					this.waitForCM = true;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_gspgreet, null);
		Button activate = (Button) root.findViewById(R.id.activate);
		Button chooseAcc = (Button) root.findViewById(R.id.acc_choose);
		chooseAcc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cm == null && !waitForCM) {
					try {
						LoginUtils.useAccountSelector(true);
						// Test mode
						LoginUtils.setLoginUrl(BASE_URL, true);
						LoginUtils.loginAppEngine(getActivity(), listener,
								null, MY_REQUEST_ID);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
		activate.setEnabled(false);
		activate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivateButton ab = new ActivateButton(cm);
				ab.execute(getActivity());
			}
		});
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		this.listener = new CookieManagerAvailableListener() {
			@Override
			public void onAuthFailure() {
				throw new RuntimeException("Authentication Failed");
			}

			@Override
			public void onCMAvailable(CookieManager cm2) {
				cm = cm2;
				Button activate = (Button) getActivity().findViewById(
						R.id.activate);
				activate.setEnabled(true);
			}
		};

	}
}
