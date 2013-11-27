package com.example.trackm.record;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trackm.R;

public class RecordActivity extends Activity {

	ViewPager viewPager;
	PagerAdapter adapter;
	String[] rank;
	String[] country;
	String[] population;
	int[] title;
	private ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
	private GestureDetector tapGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);

		rank = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		
		ImageView imageView = (ImageView)findViewById(R.id.record_image);
		
		TextView text = (TextView) findViewById(R.id.scrolling_text);
		
		text.setText("pojfskljfsdklfdjfkklfjsfkljlfjfkljsfkljsf");
		text.setSelected(true);
		
		initImageViews();
		
		// Locate the ViewPager in viewpager_main.xml
		viewPager = (ViewPager) findViewById(R.id.pager);
		// Pass results to ViewPagerAdapter Class
		adapter = new ViewPagerAdapter(RecordActivity.this, rank);
		// Binds the Adapter to the ViewPager
		viewPager.setAdapter(adapter);

		tapGestureDetector = new GestureDetector(this, new TapGestureListener());

//		imageView.setOnDragListener(new ChoiceDragListener());
		
		viewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				tapGestureDetector.onTouchEvent(event);
				return false;
			}
		});
	}

	private void initImageViews() {
		imageViewList.add((ImageView)findViewById(R.id.slice1));
		imageViewList.add((ImageView)findViewById(R.id.slice2));
		imageViewList.add((ImageView)findViewById(R.id.slice3));
		
		for(ImageView image : imageViewList){
			image.setAlpha(0f);
			image.setOnDragListener(new ChoiceDragListener());
		}
	}

	private class TapGestureListener extends GestureDetector.SimpleOnGestureListener{
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Log.v("Tap", String.valueOf(e.getAction()));
			return false;
		}
	}

	@SuppressLint("NewApi")
	private class ChoiceDragListener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			ImageView dropTarget = (ImageView) v;
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				Log.v("drag", "start");
				//no action necessary
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				dropTarget.setAlpha(0.2f);
				break;
			case DragEvent.ACTION_DRAG_EXITED:        
				dropTarget.setAlpha(0f);
				break;
			case DragEvent.ACTION_DROP:

				//handle the dragged view being dropped over a drop view
				View view = (View) event.getLocalState();
				//view dragged item is being dropped on
				//view being dragged and dropped
				TextView dropped = (TextView) view;
			
				Log.v("changed", dropped.getText().toString());
				dropTarget.setBackgroundColor(Color.BLUE);
				dropTarget.setAlpha(1.0f);
				
				Log.v("action drop", "drop");
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				//no action necessary
				break;
			default:
				break;
			}
			return true;
		}
	} 

}
