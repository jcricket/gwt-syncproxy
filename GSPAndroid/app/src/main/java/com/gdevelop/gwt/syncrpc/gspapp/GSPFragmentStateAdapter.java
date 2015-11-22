package com.gdevelop.gwt.syncrpc.gspapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class GSPFragmentStateAdapter extends FragmentStatePagerAdapter {

	static GreetFragment greet = new GreetFragment();
	static AndroidGAECrossClientFragment ageaccFrag = new AndroidGAECrossClientFragment();
	static AndroidGSIFragment agsiFrag = new AndroidGSIFragment();

	public GSPFragmentStateAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = GSPFrag.values()[i].getFragment();
//		Bundle args = new Bundle();
//		fragment.setArguments(args);
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

	public enum GSPFrag {
		GREET("Greeting"), OAUTH("A-GAE CC"), GSI("A-GSI");

		String title;

		private GSPFrag(String title) {
			this.title = title;
		}

		public Fragment getFragment() {
			switch (this) {
				case GREET:
					return greet;
				case OAUTH:
					return ageaccFrag;
				case GSI:
					return agsiFrag;
				default:
					throw new RuntimeException("Unhandled Fragment Type: " + this);
			}
		}

		public String getTitle() {
			return title;
		}
	}
}
