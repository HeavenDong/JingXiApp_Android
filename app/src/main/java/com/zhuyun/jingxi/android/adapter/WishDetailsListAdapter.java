package com.zhuyun.jingxi.android.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.WishDetailsActivity;
import com.zhuyun.jingxi.android.activty.WishHomeActivity;
import com.zhuyun.jingxi.android.bean.WishDetailsCommentBean;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by user0081 on 2016/6/17.
 */
public class WishDetailsListAdapter extends BaseAdapter {
    private final WishDetailsActivity context;
    private final List<WishDetailsCommentBean> wishDatailsList;

    public WishDetailsListAdapter(WishDetailsActivity context,List<WishDetailsCommentBean> wishDatailsList) {
        this.context=context;
        this.wishDatailsList=wishDatailsList;
    }

    @Override
    public int getCount() {
        return wishDatailsList.size();
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
        RoundedImageView wishdetail_item_head;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_wishdiscuss_list_item,null);
            holder=new ViewHolder();
            holder.wishdetail_item_head= (RoundedImageView) convertView.findViewById(R.id.wishdetail_item_head);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.wishdetail_item_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=  new Intent(context,WishHomeActivity.class);
                Bundle bundle = new Bundle();
                /**假数据*/
                bundle.putInt("FriendsId",68);
                intent.putExtras(bundle);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        return convertView;
    }
}
