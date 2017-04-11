package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.NewFriendsActivity;
import com.zhuyun.jingxi.android.bean.NewFriends;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by user0081 on 2016/6/24.
 */
public class NewFriendsListAdapter extends BaseAdapter{
    private final List<NewFriends> lists;
    private NewFriendsActivity context;

    public NewFriendsListAdapter(NewFriendsActivity context,List<NewFriends> lists) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView==null){
            convertView=  View.inflate(context, R.layout.layout_newfriendslist_item,null);
            holder=  new ViewHolder();
            holder.newfriends_item_receiver= (TextView) convertView.findViewById(R.id.newfriends_item_receiver);
            holder.newfriends_item_name= (TextView) convertView.findViewById(R.id.newfriends_item_name);
            holder.newfriends_item_hint= (TextView) convertView.findViewById(R.id.newfriends_item_hint);
            holder.newfriends_item_head= (RoundedImageView) convertView.findViewById(R.id.newfriends_item_head);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.newfriends_item_name.setText(lists.get(position).nickName);
        holder.newfriends_item_hint.setText("HI我是"+lists.get(position).nickName+"，请求添加你为好友");
//        if (lists.get(position).portrait!=null&&!lists.get(position).portrait.equals("")){
            Picasso.with(App.getAppContext()).load("http://sta.273.com.cn/app/mbs/img/mobile_default.png").placeholder(R.drawable.touxiang_yuan)
                    .error(R.drawable.touxiang_yuan).into(holder.newfriends_item_head);
//        }
        if (lists.get(position).Receivered){
            holder.newfriends_item_receiver.setEnabled(false);
            holder.newfriends_item_receiver.setText("已接受");
            holder.newfriends_item_receiver.setTextColor(context.getResources().getColor(R.color.grey));
            holder.newfriends_item_receiver.setBackgroundResource(R.drawable.img_white_fillet_background);
        }else {
            holder.newfriends_item_receiver.setTextColor(context.getResources().getColor(R.color.red));
            holder.newfriends_item_receiver.setBackgroundResource(R.drawable.stroke_corner_transparent_background);
        }

        holder.newfriends_item_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.responseAddFriends(lists.get(position).fAId,position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        RoundedImageView newfriends_item_head;
        TextView newfriends_item_receiver,newfriends_item_name,newfriends_item_hint;
    }

}
