package com.example.trackm.customAdapter;
 
import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trackm.R;
 
public class CustomAdapter<String> extends ArrayAdapter<String> {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<String> playlist;
    private SparseBooleanArray mSelectedItemsIds;
 
    public CustomAdapter(Context context, int resourceId,
            List<String> platlist) {
        super(context, resourceId, platlist);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.playlist = platlist;
        inflater = LayoutInflater.from(context);
    }
 
    private class ViewHolder {
        TextView name;
//        CheckBox selection;
    }
 
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.playlist_list_view_row, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.playlistText1);
//            holder.selection = (CheckBox) view.findViewById(R.id.checkBox1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Capture position and set to the TextViews
        holder.name.setText(playlist.get(position).toString());
//        holder.selection.setChecked(false);
        return view;
    }
 
    @Override
    public void remove(String object) {
        playlist.remove(object);
        notifyDataSetChanged();
    }
 
    public List<String> getPlaylists() {
        return playlist;
    }
 
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }
 
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
 
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }
 
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }
 
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}