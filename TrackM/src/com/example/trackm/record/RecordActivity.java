package com.example.trackm.record;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.trackm.R;

public class RecordActivity extends Activity {
	
	ViewPager viewPager;
    PagerAdapter adapter;
    String[] rank;
    String[] country;
    String[] population;
    int[] title;
	private GestureDetector tapGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_record);

    	rank = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

    	// Locate the ViewPager in viewpager_main.xml
    	viewPager = (ViewPager) findViewById(R.id.pager);
    	// Pass results to ViewPagerAdapter Class
    	adapter = new ViewPagerAdapter(RecordActivity.this, rank);
    	// Binds the Adapter to the ViewPager
    	viewPager.setAdapter(adapter);
    	
    	tapGestureDetector = new GestureDetector(this, new TapGestureListener());
    	
    	viewPager.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					 tapGestureDetector.onTouchEvent(event);
					return false;
				}
    	});
    }
    
    class TapGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
        	Log.v("Tap", String.valueOf(e.getAction()));
			return false;
        }
       }

}
