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
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.AbsListView.MultiChoiceModeListener;

import com.example.trackm.R;
import com.example.trackm.customAdapter.CustomAdapter;
import com.example.trackm.playlist.PlaylistActivity.MultiChoiceListener;

public class OpenPlaylist extends Activity{

	private ListView listView;
	private ArrayList<String> tracklist = new ArrayList<String>();
	private OpenPlaylist activity;
	private CustomAdapter<String> customAdapter;
	private LayoutInflater inflater;
	private String playlistName;
	private long playlistId;
	private boolean remove = false;
	private MultiChoiceListener mActionModeCallback;
	private HashMap<String, Integer> trackMap = new HashMap<String, Integer>();

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
		
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setAdapter(customAdapter);		
		mActionModeCallback = new MultiChoiceListener(); 
		listView.setMultiChoiceModeListener(new MultiChoiceListener());
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
			case R.id.action_delete : remove = true;
			activity.startActionMode(mActionModeCallback);
//				removeFromPlaylist(11);
//				removeFromPlaylist(12);
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
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
				MediaStore.Audio.Playlists.Members.TITLE,
				MediaStore.Audio.Playlists.Members.AUDIO_ID};
		
		Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);

		Cursor cursor = resolver.query(uri, proj, null, null, null);
		if(cursor != null){
			if(cursor.moveToFirst()){
				do{
					String trackName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.TITLE));
					int audioId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
					if(!tracklist.contains(trackName)){
						tracklist.add(String.valueOf(trackName));
						trackMap.put(trackName, audioId);
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
		if(resultCode != 0){
			if(data.hasExtra("newTrack")){
				for(int i = 0; i < data.getStringArrayListExtra("newTrack").size(); i++){
					//call removal here to remove repetitve items					
					tracklist.add(data.getStringArrayListExtra("newTrack").get(i));
					trackMap.put(data.getStringArrayListExtra("newTrack").get(i), 
							data.getIntegerArrayListExtra("audioId").get(i));
				} 
				notifyDataChanged();
			
			}
		}
	}
	
	public void notifyDataChanged(){
		customAdapter.notifyDataSetChanged();
	}
	
	class MultiChoiceListener implements MultiChoiceModeListener{

		@Override
		public void onItemCheckedStateChanged(ActionMode mode,
				int position, long id, boolean checked) {
			// Capture total checked items
			if(remove){
			final int checkedCount = listView.getCheckedItemCount();
			// Set the CAB title according to total checked items
			mode.setTitle(checkedCount + " Selected");
			// Calls toggleSelection method from ListViewAdapter Class
			customAdapter.toggleSelection(position);
			}
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_remove:
				// Calls getSelectedIds method from ListViewAdapter Class
				if(remove){
				SparseBooleanArray selected = customAdapter
				.getSelectedIds();
				// Captures all selected ids with a loop
				for (int i = (selected.size() - 1); i >= 0; i--) {
					if (selected.valueAt(i)) {
						String selecteditem = customAdapter
								.getItem(selected.keyAt(i));
						// Remove selected items following the ids
						customAdapter.remove(selecteditem);
						removeFromPlaylist(trackMap.get(selecteditem));
					}
				}
				// Close CAB
				mode.finish();
				}
				remove = false;
				return true;
			default:
				remove = false;
				return false;
			}
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			if(remove){
			MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.contexual_menu, menu);
                return true;
			} else{
				return false;
			}
		}

		@Override
		public void onDestroyActionMode(ActionMode arg0) {
			customAdapter.removeSelection();
		}

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			return false;
		}
	}

}
