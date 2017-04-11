package com.zhuyun.jingxi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhuyun.jingxi.android.R;


/**
 * Created by user0081 on 2016/6/13.
 */
public class IMListAdapter extends BaseAdapter {
    private final Context context;

    public IMListAdapter(Context context) {
        this.context=context;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 3;
    }

    class ViewHolder{

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_imlist_item,null);
            holder=new ViewHolder();

            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}