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
 */
package com.gdevelop.gwt.syncrpc.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.gdevelop.gwt.syncrpc.LoginUtils;
import com.gdevelop.gwt.syncrpc.R;

/**
 * @author Preethum
 * @since 0.4
 */
public class AccountList extends ListActivity {
	protected AccountManager accountManager;
	protected Intent intent;

	private boolean devMode = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		devMode = this.getIntent().getBooleanExtra(
				LoginUtils.LOCAL_DEV_MODE_FLAG, devMode);

		accountManager = AccountManager.get(getApplicationContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.account_list_menu, menu);

		if (devMode) {
			MenuItem customItem = menu.findItem(R.id.add_custom);
			customItem.setVisible(true);
		}
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Account account = (Account) getListView().getItemAtPosition(position);
		Intent rData = new Intent();
		rData.putExtra(LoginUtils.ACCOUNT_KEY, account);
		setResult(Activity.RESULT_OK, rData);
		finish();
	}

	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.add_account) {
			startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));
		} else if (itemId == R.id.add_custom) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Enter Email");
			alert.setMessage("Enter an Email to use as a mock account");
			final EditText input = new EditText(this);
			alert.setView(input);
			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Account account = new Account(input.getText()
									.toString(), LoginUtils.TEST_ACCOUNT_TYPE);
							Intent rData = new Intent();
							rData.putExtra(LoginUtils.ACCOUNT_KEY, account);
							setResult(Activity.RESULT_OK, rData);
							finish();
						}
					});
			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alert.show();
		} else {
			return false;
		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		Account[] accounts = accountManager.getAccountsByType("com.google");
		this.setListAdapter(new ArrayAdapter<Account>(this,
				android.R.layout.simple_list_item_1, accounts));
	}
}
