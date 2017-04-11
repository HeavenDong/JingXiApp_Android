package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.WishDetailsActivity;
import com.zhuyun.jingxi.android.bean.Friends;

import java.util.List;

/**
 * Created by user0081 on 2016/6/17.
 */
public class WishDetailsHoriListAdapter extends BaseAdapter {
    private final WishDetailsActivity context;
    private final List<Friends> likedPersonLists;

    public WishDetailsHoriListAdapter(WishDetailsActivity context, List<Friends> likedPersonLists) {
        this.context=context;
        this.likedPersonLists=likedPersonLists;
    }

    @Override
    public int getCount() {
        return likedPersonLists.size();
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

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_wishdetails_horilist_item,null);
            holder=new ViewHolder();

            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
