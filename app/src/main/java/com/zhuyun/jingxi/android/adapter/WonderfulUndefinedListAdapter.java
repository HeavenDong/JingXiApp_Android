package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.WonderfulUndefinedActivity;

/**
 * Created by user0081 on 2016/7/1.
 */
public class WonderfulUndefinedListAdapter extends BaseAdapter {
    private final WonderfulUndefinedActivity context;

    public WonderfulUndefinedListAdapter(WonderfulUndefinedActivity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.layout_wonderfulundefined_listitem, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}