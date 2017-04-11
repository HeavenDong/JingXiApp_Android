package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.StarActivity;
import com.zhuyun.jingxi.android.bean.StarBean;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by user0081 on 2016/6/17.
 */
public class StarListAdapter extends BaseAdapter {
    private final StarActivity context;
    private final List<StarBean> lists;

    public StarListAdapter(StarActivity context, List<StarBean> lists) {
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
        TextView star_item_nick,star_item_addfriend,star_item_wishnumber,star_item_sendnumber;
        RoundedImageView star_item_head;
        ImageView star_item_sex;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_starlist_item,null);
            holder=new ViewHolder();
            holder.star_item_nick= (TextView) convertView.findViewById(R.id.star_item_nick);
            holder.star_item_addfriend= (TextView) convertView.findViewById(R.id.star_item_addfriend);
            holder.star_item_wishnumber= (TextView) convertView.findViewById(R.id.star_item_wishnumber);
            holder.star_item_sendnumber= (TextView) convertView.findViewById(R.id.star_item_sendnumber);
            holder.star_item_head= (RoundedImageView) convertView.findViewById(R.id.star_item_head);
            holder.star_item_sex= (ImageView) convertView.findViewById(R.id.star_item_sex);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.star_item_nick.setText(lists.get(position).nickName);
        holder.star_item_wishnumber.setText(lists.get(position).wishNumber+"");
        holder.star_item_sendnumber.setText(lists.get(position).sendNumber+"");
        if (lists.get(position).gender==0){
            holder.star_item_sex.setImageResource(R.drawable.nv);
        }else {
            holder.star_item_sex.setImageResource(R.drawable.nan);
        }
        Picasso.with(App.getAppContext()).load(lists.get(position).portrait).placeholder(R.drawable.touxiang_yuan)
                .error(R.drawable.touxiang_yuan).into(holder.star_item_head);

        if (lists.get(position).isApplayed){
            holder.star_item_addfriend.setEnabled(false);
            holder.star_item_addfriend.setText("已邀请");
            holder.star_item_addfriend.setTextColor(context.getResources().getColor(R.color.grey));
            holder.star_item_addfriend.setBackgroundResource(R.drawable.img_white_fillet_background);
        }else {
            holder.star_item_addfriend.setEnabled(true);
            holder.star_item_addfriend.setText("加好友");
            holder.star_item_addfriend.setTextColor(context.getResources().getColor(R.color.red));
            holder.star_item_addfriend.setBackgroundResource(R.drawable.stroke_corner_transparent_background);
        }

        holder.star_item_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLogs.e("haifeng","点击申请好友按钮");
                context.requestAddFriends(lists.get(position).id,position);
            }
        });
        return convertView;
    }
}
