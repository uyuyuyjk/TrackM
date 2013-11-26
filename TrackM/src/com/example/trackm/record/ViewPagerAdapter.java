package com.example.trackm.record;

import android.content.ClipData;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.trackm.R;

public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    String[] title;
//    int[] flag;
    LayoutInflater inflater;
 
    public ViewPagerAdapter(Context context, String[] title){//  int[] flag) {
        this.context = context;
        this.title = title;
//        this.flag = flag;
    }
 
    @Override
    public int getCount() {
        return title.length;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
 
        // Declare Variables
        TextView titleView;
 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_content, container,
                false);
 
        // Locate the TextViews in viewpager_item.xml
        titleView = (TextView) itemView.findViewById(R.id.image_label);
 
        // Capture position and set to the TextViews
        titleView.setText(title[position]);
        
//        titleView.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View view) {
//				Log.v("View", ((TextView)view).getText().toString());
//			}
//        });
        
        titleView.setOnLongClickListener(new ChoiceTouchListener());
        
//         Locate the ImageView in viewpager_item.xml
//        imgflag = (ImageView) itemView.findViewById(R.id.flag);
//         Capture position and set to the ImageView
//        imgflag.setImageResource(flag[position]);
// 
        //Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);
        return itemView;
    }
    
    @Override
	public float getPageWidth(int position) {
		return super.getPageWidth(position) / 3;
	}

	@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
 
    }
	
	private final class ChoiceTouchListener implements OnLongClickListener {
		@Override
		public boolean onLongClick(View view) {
				Log.v("touch", "touch");
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				//start dragging the item touched
				view.startDrag(data, shadowBuilder, view, 0);
				return true;
		}
	} 
}