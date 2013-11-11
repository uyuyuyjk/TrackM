package com.example.trackm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
	private Context context;
	private LayoutInflater inflater;
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_seconday);

		setView(findViewById(R.layout.activity_seconday));
		listView = (ListView)findViewById(R.id.listView);

		track_array = this.getResources().getStringArray(R.array.track_array);

		ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, R.layout.list_view_row, R.id.listText, track_array);

		View header = inflater.inflate(R.layout.track_manager_header, null);

		header.setOnClickListener(new HeaderClickHandler());
		
		listView.setAdapter(adapter);
		listView.addHeaderView(header);
		listView.setOnItemClickListener(new ListClickHandler());
	}
	
	private class HeaderClickHandler implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			sendIntent();
		}
	
		public void sendIntent(){
			Intent intent = new Intent(context, MainActivity.class);
			startActivity(intent);
		}
		
	}

	private class ListClickHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long value) {
			// TODO Auto-generated method stub
			TextView textView = (TextView)view.findViewById(R.id.listText);

			String text = textView.getText().toString();

			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
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
