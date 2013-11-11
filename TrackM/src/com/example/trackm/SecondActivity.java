package com.example.trackm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {

	private ListView listView;
	private String[] track_array;
	private Activity activity;
	private LayoutInflater inflater;
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_seconday);

		setView(findViewById(R.layout.activity_seconday));
		listView = (ListView)findViewById(R.id.listView);
		track_array = this.getResources().getStringArray(R.array.track_array);

		ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, R.layout.list_view_row, R.id.listText, track_array);

		View header = inflater.inflate(R.layout.track_manager_header, null);

		header.setOnClickListener(new HeaderClickHandler());

		listView.addHeaderView(header);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ListClickHandler());

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
		element.putInt("position", listView.getPositionForView(getView()));
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
