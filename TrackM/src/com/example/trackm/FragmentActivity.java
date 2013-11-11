package com.example.trackm;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentActivity extends Fragment{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onCreateView");
		
		View view = inflater.inflate(R.layout.fragment_layout, container, false);
		
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onDetach");
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onResume");
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onStop");
		super.onStop();
	}
	
}
