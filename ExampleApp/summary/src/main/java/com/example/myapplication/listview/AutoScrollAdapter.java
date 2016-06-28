package com.example.myapplication.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * @author LIUYAN
 * @date 2016/6/3 0003
 * @time 15:09
 */
public class AutoScrollAdapter extends ArrayAdapter<String> {

    private int mListCount;

    public AutoScrollAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        mListCount = objects.size();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        position = position % mListCount;
        return super.getView(position, convertView, parent);
    }
}
