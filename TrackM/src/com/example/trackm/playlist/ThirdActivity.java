package com.example.trackm.playlist;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headerclicked.HeaderClickHandler;
import com.example.trackm.R;
import com.example.trackm.playlist.remove.RemovePlaylistActivity;

public class ThirdActivity extends Activity{

	private ThirdActivity activity;
	private LayoutInflater inflater;
	private ListView listView;
	public int viewPosition;
	private View view;
	private ArrayList<String> playlist = new ArrayList<String>();
	private Uri mUri;
	private ArrayAdapter<String> sAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_third);
		Log.v("ThirdAct", "Start");
		setView(findViewById(R.layout.activity_third));

		listView = (ListView)findViewById(R.id.playlist_listview);

		sAdapter = new ArrayAdapter<String>(this, R.layout.playlist_list_view_row, R.id.playlistText1, playlist); 
		View header = inflater.inflate(R.layout.main_menu_header, null);

		header.setOnClickListener(new HeaderClickHandler(activity));
		loadPlayList();
		listView.addHeaderView(header);
		listView.setAdapter(sAdapter);
		listView.setOnItemClickListener(new ListClickHandler());
		listView.setOnItemLongClickListener(new ListLongClickHandler());

		registerForContextMenu(listView);
	}

	private class ListLongClickHandler implements OnItemLongClickListener {



		@Override
		public boolean onItemLongClick(AdapterView<?> adapter, View view,
				int position, long arg3) {

			viewPosition = position - 1;

			return false;
		}

	}

	private class ListClickHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long value) {
			TextView textView = (TextView)view.findViewById(R.id.playlistText1);

			String text = textView.getText().toString();

			Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, Menu.NONE, "New playlist").setIcon(R.drawable.new_playlist);
		menu.add(0, 1, Menu.NONE,"Remove").setIcon(R.drawable.remove);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case 0: newPlaylistDialog();
			break;
		case 1: Intent intent = new Intent(this, RemovePlaylistActivity.class);
				startActivity(intent);
				Log.v("ThirdAct", "Start");
			break;
		default:
			break;
		}
		return true;
	}

	public void newPlaylistDialog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Title");
		alert.setMessage("Message");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				createPlaylist(value);
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Log.v("dialog", "cancel");
				return;
			}
		});
		
		alert.show();
	}
	
	public void loadPlayList(){
		ContentResolver resolver = this.getContentResolver();
		playlist = new ArrayList<String>();
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

	public void createPlaylist(String pName) { 
		ContentResolver resolver = this.getContentResolver();

	    Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

	    Log.d("Tag", "Checking for existing playlist for " + pName);
	    Cursor c = resolver.query(playlists, new String[] {"*"}, null, null, null);
	    long playlistId = 0;
	    if(c.moveToFirst()){
	    	do {
	    		String plname = c.getString(c.getColumnIndex(MediaStore.Audio.Playlists.NAME));
	    		if (plname.equalsIgnoreCase(pName)) {
	    			playlistId = c.getLong(c.getColumnIndex(MediaStore.Audio.Playlists._ID));
	    			playlist.remove(plname);
	    			notifyDataChanged();
	    			break;
	    		}
	    	} while (c.moveToNext());
	    }
	    c.close();

	    if (playlistId!=0) {
	        Uri deleteUri = ContentUris.withAppendedId(playlists, playlistId);
	        Log.d("Tag", "REMOVING Existing Playlist: " + playlistId);

	        // delete the playlist
	        resolver.delete(deleteUri, null, null);
	    }

	    Log.d("Tag", "CREATING PLAYLIST: " + pName);
		
		ContentValues mInserts = new ContentValues();
        mInserts.put(MediaStore.Audio.Playlists.NAME, pName);
        mInserts.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis());
        mInserts.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());
        mUri = resolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, mInserts);       
        if (mUri != null) {
            playlistId = -1;
            c = resolver.query(mUri, PROJECTION_PLAYLIST, null, null, null);
            if (c != null) {
                // Save the newly created ID so it can be selected.  Names are allowed to be duplicated,
                // but IDs can never be.
            	c.moveToFirst();
                playlistId = c.getInt(c.getColumnIndex(MediaStore.Audio.Playlists._ID));
                playlist.add(pName);
                c.close();
            }
        }
        notifyDataChanged();
	}
	
//	public void refreshPlaylist(){
//		ContentResolver resolver = this.getContentResolver();
//	    Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
//	    Cursor c = resolver.query(playlists, new String[] {"*"}, null, null, null);
//	    if(c.moveToFirst()){
//	    	do {
//	    		String plname = c.getString(c.getColumnIndex(MediaStore.Audio.Playlists.NAME));
//	    		notifyDataChanged();
//	    	} while (c.moveToNext());
//	    }c.close();
//	}

    public static final String[] PROJECTION_PLAYLIST = new String[] {
        	MediaStore.Audio.Playlists._ID,
        	MediaStore.Audio.Playlists.NAME,
        	MediaStore.Audio.Playlists.DATA
    };

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
	public void notifyDataChanged(){
		sAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.v("ThirdAct", "Restart");
	}

	@Override
	protected void onStart() {
		Log.i(this.getClass().getSimpleName(), "onStart");
		super.onStart();
	}


}
