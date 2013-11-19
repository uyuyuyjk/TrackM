package com.example.trackm.playlist.remove;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trackm.R;

public class RemovePlaylistActivity extends Activity{

	private RemovePlaylistActivity activity;
	private LayoutInflater inflater;
	private ListView listView;
	private ArrayList<String> playlist = new ArrayList<String>();
	private CustomAdapter<String> customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.ActionBar);
		super.onCreate(savedInstanceState);
		activity = this;
		
		setContentView(R.layout.remove_playlist_activity);
		
		customAdapter = new CustomAdapter<String>(this, R.layout.playlist_list_view_row, playlist);
		
		listView = (ListView)findViewById(R.id.playlist_listview);
		
		loadPlayList();
		
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setAdapter(customAdapter);
		listView.setMultiChoiceModeListener(new MultiChoiceListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public void removePlaylists(String name){
		ContentResolver resolver = this.getContentResolver();

	    Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
	    Cursor c = resolver.query(playlists, new String[] {"*"}, null, null, null);
	    if(c.moveToFirst()){
	    	do {
	    		String plname = c.getString(c.getColumnIndex(MediaStore.Audio.Playlists.NAME));
	    		long playlistId = c.getLong(c.getColumnIndex(MediaStore.Audio.Playlists._ID));

	    		if(name.equals(plname)){
	    			playlist.remove(plname);
	    			notifyDataChanged();
	    			removeFromMediaStore(resolver, playlists, playlistId);
	    		}
	    		
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
		customAdapter.notifyDataSetChanged();
	}
	
	class MultiChoiceListener implements MultiChoiceModeListener{

		@Override
		public void onItemCheckedStateChanged(ActionMode mode,
				int position, long id, boolean checked) {
			// Capture total checked items
			final int checkedCount = listView.getCheckedItemCount();
			// Set the CAB title according to total checked items
			mode.setTitle(checkedCount + " Selected");
			// Calls toggleSelection method from ListViewAdapter Class
			customAdapter.toggleSelection(position);
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_remove:
				// Calls getSelectedIds method from ListViewAdapter Class
				SparseBooleanArray selected = customAdapter
				.getSelectedIds();
				// Captures all selected ids with a loop
				for (int i = (selected.size() - 1); i >= 0; i--) {
					if (selected.valueAt(i)) {
						String selecteditem = customAdapter
								.getItem(selected.keyAt(i));
						// Remove selected items following the ids
						customAdapter.remove(selecteditem);
						removePlaylists(selecteditem);
					}
				}
				// Close CAB
				mode.finish();
				finish();
				return true;
			default:
				return false;
			}
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			 mode.getMenuInflater().inflate(R.menu.action_menu, menu);
                return true;
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
