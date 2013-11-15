package com.example.trackm.playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headerclicked.HeaderClickHandler;
import com.example.trackm.R;

public class ThirdActivity extends Activity{

	private ThirdActivity activity;
	private LayoutInflater inflater;
	private ListView listView;
	public int viewPosition;
	private View view;
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_third);

		setView(findViewById(R.layout.activity_third));

		listView = (ListView)findViewById(R.id.playlist_listview);


		SimpleAdapter sAdapter = new SimpleAdapter(this, data,  R.layout.list_view_row, 
						new String[] {"title", "subtitle"}, new int[]{R.id.listText1, R.id.listText2}); 
		View header = inflater.inflate(R.layout.main_menu_header, null);

		header.setOnClickListener(new HeaderClickHandler(activity));

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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	

}
