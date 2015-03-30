/*
 * Copyright 2013 Blue Esoteric Web Development, LLC (http://www.blueesoteric.com/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  See Android wiki (https://code.google.com/p/gwt-syncproxy/wiki/Android) for
 *  coding details. This android interface was created from reviewing and integrating
 *  ideas found from: http://blog.notdot.net/2010/05/Authenticating-against-App-Engine-from-an-Android-app.
 */
package com.gdevelop.gwt.syncrpc.spaapptest;

import java.net.CookieManager;
import java.util.ArrayList;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gdevelop.gwt.syncrpc.LoginUtils;
import com.gdevelop.gwt.syncrpc.ProxySettings;
import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.gdevelop.gwt.syncrpc.android.CookieManagerAvailableListener;
import com.gdevelop.gwt.syncrpc.spawebtest.client.GreetingServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

class ActivateButton extends AsyncTask<MainActivity, Void, MainActivity> {
	GreetingServiceAsync rpcService;
	CookieManager cm;

	public ActivateButton(CookieManager cm) {
		this.cm = cm;
	}

	/**
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected MainActivity doInBackground(MainActivity... act) {
		// Use 10.0.2.2 for Hosted Emulator Loopback interface
		SyncProxy.setBaseURL(MainActivity.BASE_URL + "/spawebtest/");
		this.rpcService = SyncProxy.createProxy(GreetingServiceAsync.class,
				new ProxySettings().setCookieManager(this.cm));
		return act[0];
	}

	@Override
	protected void onPostExecute(final MainActivity act) {
		// Greet Server Test for Random T1 object
		// rpcService.greetServer(((EditText) act.findViewById(R.id.input))
		// .getText().toString(), new AsyncCallback<T1>() {
		// @Override
		// public void onFailure(Throwable caught) {
		// throw new RuntimeException(e);
		// }
		//
		// @Override
		// public void onSuccess(final T1 result) {
		// act.runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// final TextView tv = (TextView) act
		// .findViewById(R.id.result);
		// tv.setText(Html.fromHtml(result.getText()));
		// }
		// });
		// }
		// });

		// Greet Server Test for Regular String
		// rpcService.greetServer2(((EditText) act.findViewById(R.id.input))
		// .getText().toString(), new AsyncCallback<String>() {
		// @Override
		// public void onFailure(Throwable caught) {
		// throw new RuntimeException(e);
		// }
		//
		// @Override
		// public void onSuccess(final String result) {
		// act.runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// final TextView tv = (TextView) act
		// .findViewById(R.id.result);
		// tv.setText(Html.fromHtml(result));
		// }
		// });
		// }
		// });

		// Greet Server Test for ArrayList contain the String
		this.rpcService.greetServerArr(
				((EditText) act.findViewById(R.id.input)).getText().toString(),
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

public class MainActivity extends Activity {
	public static int MY_REQUEST_ID = 2222;
	public static final String BASE_URL = "http://192.168.1.125:8888";
	// Use this for Hosted Emulator Loopback Interface
	// public static final String BASE_URL = "http://10.0.2.2:8888";
	CookieManager cm;

	CookieManagerAvailableListener listener;

	boolean waitForCM = false;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_REQUEST_ID) {
			if (resultCode == RESULT_OK) {
				Account account = (Account) data.getExtras().get(
						LoginUtils.ACCOUNT_KEY);
				try {
					LoginUtils.loginAppEngine(this, this.listener, account);
					this.waitForCM = true;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		Button activate = (Button) this.findViewById(R.id.activate);
		activate.setEnabled(false);
		activate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivateButton ab = new ActivateButton(MainActivity.this.cm);
				ab.execute(MainActivity.this);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.listener = new CookieManagerAvailableListener() {
			@Override
			public void onAuthFailure() {
				throw new RuntimeException("Authentication Failed");
			}

			@Override
			public void onCMAvailable(CookieManager cm) {
				MainActivity.this.cm = cm;
				Button activate = (Button) MainActivity.this
						.findViewById(R.id.activate);
				activate.setEnabled(true);
			}
		};
		if (this.cm == null && !this.waitForCM) {
			try {
				LoginUtils.useAccountSelector(true);
				// Test mode
				LoginUtils.setLoginUrl(BASE_URL, true);
				LoginUtils.loginAppEngine(this, this.listener, null,
						MY_REQUEST_ID);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}