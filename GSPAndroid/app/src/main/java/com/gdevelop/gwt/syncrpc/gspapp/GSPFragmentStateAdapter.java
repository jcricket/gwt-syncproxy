package com.gdevelop.gwt.syncrpc.gspapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class GSPFragmentStateAdapter extends FragmentStatePagerAdapter {
	public GSPFragmentStateAdapter(FragmentManager fm) {
		super(fm);

	}

	public enum GSPFrag {
		GREET("Greeting"), OAUTH("A-GAE CC");

		String title;

		private GSPFrag(String title) {
			this.title = title;
		}

		public Fragment getFragment() {
			switch (this) {
			case GREET:
				return new GreetFragment();
			case OAUTH:
				return new AndroidGAECrossClientFragment();
			default:
				throw new RuntimeException("Unhandled Fragment Type: " + this);
			}
		}

		public String getTitle() {
			return title;
		}
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = GSPFrag.values()[i].getFragment();
		Bundle args = new Bundle();
		// // Our object is just an integer :-P
		// args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return GSPFrag.values().length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return GSPFrag.values()[position].getTitle();
	}
}
