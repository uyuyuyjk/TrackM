package com.example.trackm;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {

	private ListView listView;
	private ArrayList<String> track_array =  new ArrayList<String>();
	private Activity activity;
	private LayoutInflater inflater;
	private View view;
	private int viewPosition;
	private Cursor cursor;
	private static ArrayList<String> listDetail = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_seconday);

		setView(findViewById(R.layout.activity_seconday));
		listView = (ListView)findViewById(R.id.listView);

		getAllSongsFromSDCARD();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_row, R.id.listText, track_array);

		View header = inflater.inflate(R.layout.track_manager_header, null);

		header.setOnClickListener(new HeaderClickHandler());

		listView.addHeaderView(header);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ListClickHandler());
		listView.setOnItemLongClickListener(new ListLongClickHandler());

		registerForContextMenu(listView);
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		setView(v);
		menu.add(0, 0, Menu.NONE, "Add to play list");
		menu.add(0, 1, Menu.NONE, "Detail information");
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case 0: Toast.makeText(activity,String.valueOf(item.getItemId()), Toast.LENGTH_SHORT).show();
		break;
		case 1: openDetailInformation();				
		break;
		default: Toast.makeText(activity, "empty", Toast.LENGTH_SHORT).show();
		break;
		}

		return true;
	}

	public void openDetailInformation(){
		Bundle element = new Bundle();
		element.putInt("position", viewPosition);
		Intent intent = new Intent(activity, TrackFragmentActivity.class);
		intent.putExtra("bundle", element);
		startActivity(intent);
	}

	private class HeaderClickHandler implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			sendIntent();
		}

		public void sendIntent(){
			finish();
		}

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
			// TODO Auto-generated method stub
			TextView textView = (TextView)view.findViewById(R.id.listText);

			String text = textView.getText().toString();

			Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void getAllSongsFromSDCARD() {
		String[] STAR = { "*" };        
		Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
		ContentResolver cr = activity.getContentResolver();
		cursor = cr.query(allsongsuri, STAR, selection, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					String song_name = cursor
							.getString(cursor
									.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
					track_array.add(song_name);
					int song_id = cursor.getInt(cursor
							.getColumnIndex(MediaStore.Audio.Media._ID));

					String fullpath = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.DATA));

					String album_name = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ALBUM));
					int album_id = cursor.getInt(cursor
							.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

					String artist_name = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST));
					int artist_id = cursor.getInt(cursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
					
					listDetail.add("Name: " + song_name  + "\n\n" 
							+ "Album: " + album_name + "\n\n" + "Artist: " + artist_name + "\n\n" 
							+ "Path: \n" + fullpath + "\n");
					
				} while (cursor.moveToNext());
				setListDetail(listDetail);
			}
			
			cursor.close();
		}
	}

	public static ArrayList<String> getListDetail() {
		return listDetail;
	}


	public static void setListDetail(ArrayList<String> detail) {
		listDetail = detail;
	}


	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getSimpleName(), "onStop");
		super.onStop();
	}

}
