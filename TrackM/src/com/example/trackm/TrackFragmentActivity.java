package com.example.trackm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class TrackFragmentActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);
		Intent intent = this.getIntent();
		
		Bundle element = intent.getBundleExtra("bundle");
		
		Fragment trackDetail = new TrackDetailFragment();
		trackDetail.setArguments(element);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.frame, trackDetail);
		transaction.commit();
	}

	
}
