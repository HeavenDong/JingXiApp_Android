package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.WishDiscussActivity;
import com.zhuyun.jingxi.android.bean.WishDiscussBean;

import java.util.List;

/**
 * Created by user0081 on 2016/7/1.
 */
public class WishDiscussListAdapter extends BaseAdapter {
    private final WishDiscussActivity context;
    private final List<WishDiscussBean> lists;

    public WishDiscussListAdapter(WishDiscussActivity context, List<WishDiscussBean> lists) {
        this.context=context;
        this.lists=lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        TextView wishdiscuss_item_name;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_wishdiscuss_list_item,null);
            holder=new ViewHolder();
            holder.wishdiscuss_item_name= (TextView) convertView.findViewById(R.id.wishdiscuss_item_name);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.wishdiscuss_item_name.setText(lists.get(position).name.toString());
        return convertView;
    }
}
