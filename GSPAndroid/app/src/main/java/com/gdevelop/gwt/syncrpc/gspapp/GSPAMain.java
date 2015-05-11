package com.gdevelop.gwt.syncrpc.gspapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class GSPAMain extends FragmentActivity {
	GSPFragmentStateAdapter pager;
	ViewPager viewPager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_gspamain);
		pager = new GSPFragmentStateAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(pager);
	}
}
