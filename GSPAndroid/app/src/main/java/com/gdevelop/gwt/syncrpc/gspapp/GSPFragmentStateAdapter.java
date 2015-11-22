package com.gdevelop.gwt.syncrpc.gspapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class GSPFragmentStateAdapter extends FragmentStatePagerAdapter {
	ArrayList<Fragment> frags;

	public GSPFragmentStateAdapter(FragmentManager fm) {
		super(fm);
		frags = new ArrayList<>();
	}

	@Override
	public Fragment getItem(int i) {
		if (frags.size() - 1 >= i) {
			return frags.get(i);
		}
		Fragment fragment = GSPFrag.values()[i].getFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		frags.add(i, fragment);
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
					return new GreetFragment();
				case OAUTH:
					return new AndroidGAECrossClientFragment();
				case GSI:
					return new AndroidGSIFragment();
				default:
					throw new RuntimeException("Unhandled Fragment Type: " + this);
			}
		}

		public String getTitle() {
			return title;
		}
	}
}
