package com.zhuyun.jingxi.android.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.WishDynamicActivity;
import com.zhuyun.jingxi.android.activty.WishHomeActivity;
import com.zhuyun.jingxi.android.bean.WishDynamicBean;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by user0081 on 2016/7/1.
 */
public class WishDynamicListAdapter extends BaseAdapter {
    private final WishDynamicActivity context;
    private final List<WishDynamicBean> lists;

    public WishDynamicListAdapter(WishDynamicActivity context, List<WishDynamicBean> lists) {
        this.context = context;
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

    class ViewHolder {
        RoundedImageView wish_dynamic_head;
        TextView wish_dynamic_number,wish_dynamic_name,wish_dynamic_content,wish_dynamic_time;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.layout_wishdynamic_list_item, null);
            holder = new ViewHolder();
            holder.wish_dynamic_head= (RoundedImageView) convertView.findViewById(R.id.wish_dynamic_head);
            holder.wish_dynamic_name= (TextView) convertView.findViewById(R.id.wish_dynamic_name);
            holder.wish_dynamic_content= (TextView) convertView.findViewById(R.id.wish_dynamic_content);
            holder.wish_dynamic_time= (TextView) convertView.findViewById(R.id.wish_dynamic_time);
            holder.wish_dynamic_number= (TextView) convertView.findViewById(R.id.wish_dynamic_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.wish_dynamic_name.setText(lists.get(position).nickName);
        holder.wish_dynamic_name.setText(lists.get(position).nickName);
        holder.wish_dynamic_time.setText(CommanUtil.transhms(lists.get(position).newWishTime,"MM-dd"));
        if (lists.get(position).number>0) {
            holder.wish_dynamic_number.setVisibility(View.VISIBLE);
            holder.wish_dynamic_number.setText(lists.get(position).number + "");
        }
        Picasso.with(App.getAppContext()).load(lists.get(position).imgIconUrl).placeholder(R.drawable.touxiang_yuan)
                .error(R.drawable.touxiang_yuan).into(holder.wish_dynamic_head);

        holder.wish_dynamic_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=  new Intent(context,WishHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("FriendsId",lists.get(position).fid);
                intent.putExtras(bundle);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

        return convertView;
    }
}