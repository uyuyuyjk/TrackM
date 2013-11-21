package com.example.trackm.playlist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.trackm.R;
import com.example.trackm.playlist.customAdapter.CustomAdapter;

public class OpenPlaylist extends Activity{

	private ListView listView;
	private ArrayList<String> tracklist = new ArrayList<String>();
	private OpenPlaylist activity;
	private CustomAdapter<String> customAdapter;
	private LayoutInflater inflater;
	private String playlistName;
	private long playlistId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.ActionBar);
		super.onCreate(savedInstanceState);
		activity = this;
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_open_playlist_);
		
		playlistName = activity.getIntent().getStringExtra("playlistName");
		playlistId = searchForPlayListID(playlistName);
		
		listView = (ListView)findViewById(R.id.open_playlist_listview);

		customAdapter = new CustomAdapter<String>(this, R.layout.list_view_row, tracklist);
		
		loadSongFromCurrentPlaylist(playlistId);
		
		listView.setAdapter(customAdapter);		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.playlist_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_add : 
				Intent intent = new Intent(this, AddToPlaylist.class);
				intent.putExtra("playlistId", playlistId);
				intent.putExtra("playlist", tracklist);
				startActivityForResult(intent, 1);
				break;
			case R.id.action_delete : 
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public long searchForPlayListID(String plname){
		ContentResolver resolver = this.getContentResolver();
		long playlistId = 0;
		String[] projection1 = { MediaStore.Audio.Playlists._ID,
		        MediaStore.Audio.Playlists.NAME };
		Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
		Cursor cursor = resolver.query(playlists, projection1, null, null, null);
		if(cursor != null){
			if(cursor.moveToFirst()){
				do{
					String playlistName =  cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME));
					long matchId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID));
					if(plname.equals(playlistName)){
						playlistId = matchId;
					}
				} while(cursor.moveToNext());
				cursor.close();
			}
		}
		return playlistId;	
	}

	public void removeFromPlaylist(int audioId) {
		ContentResolver resolver = this.getContentResolver();
		Log.v("made it to add",""+audioId);
		String[] cols = new String[] {
				"count(*)"
		};
		Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
		Cursor cur = resolver.query(uri, cols, null, null, null);
		cur.moveToFirst();
		final int base = cur.getInt(0);
		cur.close();
		ContentValues values = new ContentValues();

		resolver.delete(uri, MediaStore.Audio.Playlists.Members.AUDIO_ID +" = "+audioId, null);
	}

	public void loadSongFromCurrentPlaylist(long playlistId){	
		ContentResolver resolver = this.getContentResolver();
		String[] proj =  new String[]{ MediaStore.Audio.Playlists.Members.PLAY_ORDER,
				MediaStore.Audio.Playlists.Members.AUDIO_ID };
		Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
		
		Cursor cursor = resolver.query(uri, proj, null, null, null);
		if(cursor != null){
			if(cursor.moveToFirst()){
				do{
					String trackName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.TITLE));
					String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST));
					if(!tracklist.contains(trackName)){
						tracklist.add(String.valueOf(trackName));
						notifyDataChanged();
					}
				}while (cursor.moveToNext());
				cursor.close();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    Log.d("CheckStartActivity","onActivityResult and resultCode = " + resultCode);
	    if(data.hasExtra("newTrack")){
	    	for(String nTrack : data.getStringArrayListExtra("newTrack")){
	    		tracklist.add(nTrack);
	    	}
	    	notifyDataChanged();
	    }
	}
	
	public void notifyDataChanged(){
		customAdapter.notifyDataSetChanged();
	}
}
