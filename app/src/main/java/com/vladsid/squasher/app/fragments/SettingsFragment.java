package com.vladsid.squasher.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vladsid.squasher.app.R;

import static com.vladsid.squasher.app.FontOverrider.applyFont;

public class SettingsFragment extends Fragment {

	public SettingsFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

		//overriding system font for whole layout
		applyFont(this.getActivity(), rootView, "fonts/MyriadPro-Light.ttf");

		return rootView;
	}
}