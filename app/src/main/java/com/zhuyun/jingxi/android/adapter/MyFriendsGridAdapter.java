package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.MyFriendsListActivity;
import com.zhuyun.jingxi.android.bean.Friends;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by user0081 on 2016/7/8.
 */
public class MyFriendsGridAdapter extends BaseAdapter {
    private final List<Friends> lists;
    private MyFriendsListActivity context;

    public MyFriendsGridAdapter(MyFriendsListActivity context, List<Friends> lists) {
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



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=  View.inflate(context, R.layout.layout_myfriends_grid_item,null);
            holder=  new ViewHolder();
            holder.select_friends_item_img= (RoundedImageView) convertView.findViewById(R.id.select_friends_item_img);
            holder.select_friends_item_name= (TextView) convertView.findViewById(R.id.select_friends_item_name);
            holder.select_friends_item_sex= (ImageView) convertView.findViewById(R.id.select_friends_item_sex);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.select_friends_item_name.setText(lists.get(position).nickName);
//        Picasso.with(App.getAppContext()).load(lists.get(position).portrait).into( holder.select_friends_item_img);
//        Picasso.with(App.getAppContext()).load("http://sta.273.com.cn/app/mbs/img/mobile_default.png").into(holder.select_friends_item_img);
        if (lists.get(position).gender==0){
            holder.select_friends_item_sex.setImageResource(R.drawable.nv);
        }else {
            holder.select_friends_item_sex.setImageResource(R.drawable.nan);
        }
        return convertView;
    }

    private class ViewHolder {
        RoundedImageView select_friends_item_img;
        TextView select_friends_item_name;
        ImageView select_friends_item_sex;
    }
}
