package com.example.trackm;

import com.example.trackm.playlist.ThirdActivity;
import com.example.trackm.tracklibrary.SecondActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView textView;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendIntent(View view){
		try {
			switch(view.getId()){
				case R.id.button1: intent = new Intent(this, SecondActivity.class);
				break;
				case R.id.button3: intent = new Intent(this, ThirdActivity.class);
				break;
				default :
				break;
			}
		} catch (IllegalStateException exception) {
			Log.v("Exception", "Activity does not exist");
			return;
		}
		startActivity(intent);
	}
}
