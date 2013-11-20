package com.example.trackm.playlist;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackm.R;
import com.example.trackm.playlist.customAdapter.CustomAdapter;

public class ThirdActivity extends Activity{

	private ThirdActivity activity;
	private LayoutInflater inflater;
	private ListView listView;
	public int viewPosition;
	private View view;
	private ArrayList<String> playlist = new ArrayList<String>();
	private Uri mUri;
	private CustomAdapter<String> customAdapter;
	private boolean remove = false;
	private ActionMode.Callback mActionModeCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.ActionBar);
		super.onCreate(savedInstanceState);
		activity = this;
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_third);
		Log.v("ThirdAct", "Start");
		setView(findViewById(R.layout.activity_third));
		
		listView = (ListView)findViewById(R.id.playlist_listview);

		customAdapter = new CustomAdapter<String>(this, R.layout.playlist_list_view_row, playlist);
		
		loadPlayList();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setAdapter(customAdapter);
		listView.setOnItemClickListener(new ListClickHandler());
		listView.setOnItemLongClickListener(new ListLongClickHandler());
		mActionModeCallback = new MultiChoiceListener(); 
		listView.setMultiChoiceModeListener(new MultiChoiceListener());

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
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.action_new: newPlaylistDialog();
			break;
		case R.id.action_remove: remove = true;
		activity.startActionMode(mActionModeCallback);
			break;
		default:
			break;
		}
		return true;
	}

	public void newPlaylistDialog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Create Playlist");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		input.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                input.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        input.requestFocus();
		
		input.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				for(int i = s.length(); i > 0; i--){
					if(s.subSequence(i-1, i).toString().equals("\n"))
						s.replace(i-1, i, "");
				}
			}
		});
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				if(!value.trim().equals("")){
					createPlaylist(value);
				} else{
					dialog.cancel();
					warningDialog();
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		
		alert.show();
	}
	
	public void warningDialog(){
		AlertDialog.Builder warning = new AlertDialog.Builder(this);
		warning.setTitle("Alert");
		warning.setMessage("Playlist name cannot be empty");
		
		warning.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		
		warning.show();
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
						removePlaylists(selecteditem);
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
