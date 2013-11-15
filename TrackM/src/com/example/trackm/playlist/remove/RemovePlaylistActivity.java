package com.example.trackm.playlist.remove;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.trackm.R;

public class RemovePlaylistActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.ActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remove_playlist_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
}
