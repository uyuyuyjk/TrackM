package com.example.trackm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TrackDetailFragment extends Fragment {
	
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_layout, container, false);
		
		TextView info = (TextView)view.findViewById(R.id.info_text);
		
		Bundle bundleInfo = this.getArguments();
		int position = bundleInfo.getInt("position");
		String trackDetails = SecondActivity.getListDetail().get(position);
		info.setText(trackDetails);
		
		return view;
	}

}
