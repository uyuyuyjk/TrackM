package com.example.trackm;

import com.example.trackm.playlist.PlaylistActivity;
import com.example.trackm.tracklibrary.TrackLibraryActivity;

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

	public void sendIntent(View view){
		try {
			switch(view.getId()){
				case R.id.button1: intent = new Intent(this, TrackLibraryActivity.class);
				break;
				case R.id.button3: intent = new Intent(this, PlaylistActivity.class);
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
