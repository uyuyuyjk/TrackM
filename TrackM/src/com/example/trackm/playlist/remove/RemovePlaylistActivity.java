package com.example.trackm.playlist.remove;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.headerclicked.HeaderClickHandler;
import com.example.trackm.R;

public class RemovePlaylistActivity extends Activity{

	private RemovePlaylistActivity activity;
	private LayoutInflater inflater;
	private ListView listView;
	private ArrayAdapter<String> sAdapter;
	private ArrayList<String> playlist = new ArrayList<String>();
	private CheckBox checkBox;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.ActionBar);
		super.onCreate(savedInstanceState);
		activity = this;
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.remove_playlist_activity);
		
		listView = (ListView)findViewById(R.id.playlist_listview);
		
		sAdapter = new ArrayAdapter<String>(this, R.layout.remove_playlist_row, R.id.removeListView1, playlist); 

		loadPlayList();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setAdapter(sAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_remove:
			removePlaylists();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void removePlaylists(){
		ContentResolver resolver = this.getContentResolver();
		
		for(int index = 0; index < sAdapter.getCount(); index ++){
			//if the item is checked remove it from the mediastore db
		}
		
	    Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
	    Cursor c = resolver.query(playlists, new String[] {"*"}, null, null, null);
	    if(c.moveToFirst()){
	    	do {
	    		String plname = c.getString(c.getColumnIndex(MediaStore.Audio.Playlists.NAME));
	    		long playlistId = c.getLong(c.getColumnIndex(MediaStore.Audio.Playlists._ID));
	    		//change required
	    		notifyDataChanged();
	    		
	    		Log.v("Adapter", String.valueOf(sAdapter.hasStableIds()));
	    		SparseBooleanArray array = listView.getCheckedItemPositions();

//	    		playlist.remove(plname);
//    			notifyDataChanged();
//    			removeFromMediaStore(resolver, playlists, playlistId);
	    	} while (c.moveToNext());
	    }c.close();
	}
	
	public void removeFromMediaStore(ContentResolver resolver, Uri playlists, long playlistId){
		 Uri deleteUri = ContentUris.withAppendedId(playlists, playlistId);
	        Log.d("Tag", "REMOVING Existing Playlist: " + playlistId);
	        // delete the playlist
	        resolver.delete(deleteUri, null, null);
	}

	public void loadPlayList(){
		ContentResolver resolver = this.getContentResolver();

	    Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
	    Cursor c = resolver.query(playlists, new String[] {"*"}, null, null, null);
	    if(c.moveToFirst()){
	    	do {
	    		String plname = c.getString(c.getColumnIndex(MediaStore.Audio.Playlists.NAME));
	    		playlist.add(plname);
	    		notifyDataChanged();
	    	} while (c.moveToNext());
	    }c.close();
	}
	
	public void notifyDataChanged(){
		sAdapter.notifyDataSetChanged();
	}
}
