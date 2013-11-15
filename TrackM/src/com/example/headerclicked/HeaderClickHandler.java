package com.example.headerclicked;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class HeaderClickHandler implements OnClickListener {

	private Activity activity;
	
	public HeaderClickHandler(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onClick(View arg0) {
		sendIntent();
	}

	public void sendIntent(){
		activity.finish();
	}

}
