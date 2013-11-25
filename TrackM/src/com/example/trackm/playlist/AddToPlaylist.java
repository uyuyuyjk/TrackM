package com.example.trackm.playlist;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackm.R;
import com.example.trackm.customAdapter.CustomAdapter;

public class AddToPlaylist extends Activity{

	private ArrayList<String> fulltrackList = new ArrayList<String>();
	private ArrayList<Integer> audioIdList = new ArrayList<Integer>();
	private ArrayList<Integer> newAudioId = new ArrayList<Integer>();
	private ArrayList<String> trackList = new ArrayList<String>();
	private ArrayList<String> artistList = new ArrayList<String>();
	private ArrayList<String> newTrackElement = new ArrayList<String>();
	private long playlistId;
	private ListView listView;
	private CustomAdapter<String> customAdapter;
	private LayoutInflater inflater;
	private AddToPlaylist activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		activity = this;

		setTheme(R.style.ActionBar);
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_add_track_to_playlist);
		
		trackList = getIntent().getStringArrayListExtra("playlist");
		playlistId = getIntent().getLongExtra("playlistId", 0);
		
		listView = (ListView)findViewById(R.id.add_playlist_listview);
		
		customAdapter = new CustomAdapter<String>(this, R.layout.list_view_row, fulltrackList);
		
		loadAllSongs();
		
		listView.setOnItemClickListener(new ListClickHandler());
		listView.setAdapter(customAdapter);	
	}

	private class ListClickHandler implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long value) {
			TextView textView = (TextView)view.findViewById(R.id.playlistText1);

			String text = textView.getText().toString();

			Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
			
			customAdapter.toggleSelection(position);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.confirm_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_confirm: finishIntent();
				break;
			default:
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void finishIntent() {
		SparseBooleanArray selected = customAdapter.getSelectedIds();
		// Captures all selected ids with a loop
		for (int i = (selected.size() - 1); i >= 0; i--) {
			if (selected.valueAt(i)) {
				String selecteditem = customAdapter.getItem(selected.keyAt(i));
				if(!trackList.contains(selecteditem)){
					newTrackElement.add(selecteditem);
					newAudioId.add(audioIdList.get(i));
					addToPlaylist(artistList.get(i), fulltrackList.get(i), audioIdList.get(i));
				}
			}
		}
		
		Intent intent = new Intent(this, OpenPlaylist.class);
		intent.putExtra("newTrack", newTrackElement);
		intent.putExtra("audioId", newAudioId);
		setResult(1,intent);
		finish();
	}

	public void addToPlaylist(String artistName, String title, int audioId) {
		//do not add repetitive items
		ContentResolver resolver = this.getContentResolver();
		String[] cols = new String[] {
				"count(*)" };
		Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
		Cursor cursor = resolver.query(uri, cols, null, null, null);
		cursor.moveToFirst();
		final int base = cursor.getInt(0);
		cursor.close();
		ContentValues values = new ContentValues();
		values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, Integer.valueOf(base + audioId));
		values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audioId);

		resolver.insert(uri, values);
		ContentValues values2 = new ContentValues();
		values2.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, Integer.valueOf(base + audioId));
		values2.put(MediaStore.Audio.Playlists.Members.TITLE, title);
		resolver.insert(uri, values2);
		
	}
	
	private void loadAllSongs() {
		String[] STAR = { "*" };        
		Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
		ContentResolver cr = activity.getContentResolver();

		Cursor cursor = cr.query(allsongsuri, STAR, selection, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					String song_name = cursor
							.getString(cursor
									.getColumnIndex(MediaStore.Audio.Media.TITLE));         			
					int song_id = cursor.getInt(cursor
							.getColumnIndex(MediaStore.Audio.Media._ID));

					String artist_name = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST));

					//call remove here to remove repetitive items
					audioIdList.add(song_id);
					artistList.add(artist_name);
					fulltrackList.add(song_name);
					notifyDataSetChanged();

				} while (cursor.moveToNext());
				
			}
			cursor.close();
		}
	}
	
	public void notifyDataSetChanged(){
		customAdapter.notifyDataSetChanged();
	}

}
