package com.example.trackm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {

	private ListView listView;
	private Activity activity;
	private LayoutInflater inflater;
	private View view;
	private int viewPosition;
	private Cursor cursor;
	private ProgressDialog progressDialog;
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private static ArrayList<String> listDetail = new ArrayList<String>();
	private long MEGABYTE = 1024L * 1024L;
	private Map<String, String> datum = new HashMap<String, String>(2);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_seconday);

		setView(findViewById(R.layout.activity_seconday));
		
		listView = (ListView)findViewById(R.id.listView);

		getAllSongsFromSDCARD();
		
		SimpleAdapter sAdapter = new SimpleAdapter(this, data,  R.layout.list_view_row, 
				new String[] {"title", "subtitle"}, new int[]{R.id.listText1, R.id.listText2}); 
		View header = inflater.inflate(R.layout.track_manager_header, null);

		header.setOnClickListener(new HeaderClickHandler());

		listView.addHeaderView(header);
		listView.setAdapter(sAdapter);
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
			TextView textView = (TextView)view.findViewById(R.id.listText1);

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
		
		final Handler mHandler = new Handler();
		showProgressDialog();
		
		new Thread(new Runnable() {  
			@Override  
             public void run() {           	 
            	 if (cursor != null) {
         			if (cursor.moveToFirst()) {
         				do {
         					Log.i(this.getClass().getSimpleName(), "load");
         					String song_name = cursor
         							.getString(cursor
         									.getColumnIndex(MediaStore.Audio.Media.TITLE));         			
//         					int song_id = cursor.getInt(cursor
//         							.getColumnIndex(MediaStore.Audio.Media._ID));

         					String fullpath = cursor.getString(cursor
         							.getColumnIndex(MediaStore.Audio.Media.DATA));

         					String album_name = cursor.getString(cursor
         							.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//         					int album_id = cursor.getInt(cursor
//         							.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

         					String artist_name = cursor.getString(cursor
         							.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//         					int artist_id = cursor.getInt(cursor
//         							.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
         					
         					float sizeNumber = cursor.getInt(cursor
         							.getColumnIndex(MediaStore.Audio.Media.SIZE));
//         					String size = cursor.getString(cursor
//         							.getColumnIndex(MediaStore.Audio.Media.SIZE));
         					
         					float duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
         					Log.i(this.getClass().getSimpleName(), "load");
         					if(trackFilterSuccess(sizeNumber, duration)){
         						datum = new HashMap<String, String>(2);
         						datum.put("title", song_name);
             					datum.put("subtitle", artist_name);
             					data.add(datum);
             					
             					listDetail.add("Name: " + song_name  + "\n\n" 
             							+ "Album: " + album_name + "\n\n" + "Artist: " + artist_name + "\n\n" 
             							+ "Size: " + String.format("%.2f Mb", sizeNumber / MEGABYTE) + "\n\n" + "Path: \n" + fullpath + "\n");
         					}					
         					Log.i(this.getClass().getSimpleName(), "load");
         				} while (cursor.moveToNext());
         				
         			}
         			cursor.close();
         		}
            	 dismissProgressDialog(mHandler);
             }  
           
         }).start();
		
	}
	
	private boolean trackFilterSuccess(float size, float duration) {
		boolean isValid = false;
		
		size = size / MEGABYTE;
		
		if(duration >= 60000 && size >= 1){
			isValid = true;
		} 
		
		return isValid;
	}
	
	private void showProgressDialog() { 
	    progressDialog = new ProgressDialog(SecondActivity.this);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setMessage("Loading...");
	    progressDialog.getContext().setTheme(R.style.MyTransparentTheme);
	    progressDialog.show();
	}

	private void dismissProgressDialog(Handler handler) {
		handler.postDelayed(new Runnable() {
   		    public void run() {
   		    	if(progressDialog != null)
   			        progressDialog.dismiss();
   		    }}, 3000);
	}

	public static ArrayList<String> getListDetail(){
		return listDetail;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	@Override
	protected void onDestroy() {
		Log.i(this.getClass().getSimpleName(), "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.i(this.getClass().getSimpleName(), "onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.i(this.getClass().getSimpleName(), "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.i(this.getClass().getSimpleName(), "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i(this.getClass().getSimpleName(), "onStop");
		super.onStop();
	}

}
