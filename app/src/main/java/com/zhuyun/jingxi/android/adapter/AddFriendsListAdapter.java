package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.AddFriendsActivity;
import com.zhuyun.jingxi.android.bean.RecommendFriends;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by user0081 on 2016/6/24.
 */
public class AddFriendsListAdapter extends BaseAdapter{
    private final List<RecommendFriends> lists;
    private AddFriendsActivity context;

    public AddFriendsListAdapter(AddFriendsActivity context, List<RecommendFriends> lists) {
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
            convertView=  View.inflate(context, R.layout.layout_addfriendslist_item,null);
            holder=  new ViewHolder();
            holder.addfriends_item_head= (RoundedImageView) convertView.findViewById(R.id.addfriends_item_head);
            holder.addfriends_item_name= (TextView) convertView.findViewById(R.id.addfriends_item_name);
            holder.addfriends_item_butt= (TextView) convertView.findViewById(R.id.addfriends_item_butt);
            holder.addfriends_item_mobile= (TextView) convertView.findViewById(R.id.addfriends_item_mobile);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.addfriends_item_name.setText(lists.get(position).nickName);
        holder.addfriends_item_mobile.setText(lists.get(position).mobile);
        if (!lists.get(position).portrait.equals("")) {
            Picasso.with(App.getAppContext()).load(lists.get(position).portrait).placeholder(R.drawable.touxiang_yuan)
                    .error(R.drawable.touxiang_yuan).into(holder.addfriends_item_head);
        }

//        MyLogs.e("haifeng","按钮 :"+position+""+lists.get(position).isApplayed);
        if (lists.get(position).isApplayed){
            holder.addfriends_item_butt.setEnabled(false);
            holder.addfriends_item_butt.setText("已添加");
            holder.addfriends_item_butt.setTextColor(context.getResources().getColor(R.color.grey));
            holder.addfriends_item_butt.setBackgroundResource(R.drawable.img_white_fillet_background);
        }else {
            holder.addfriends_item_butt.setText("添加");
            holder.addfriends_item_butt.setTextColor(context.getResources().getColor(R.color.red));
            holder.addfriends_item_butt.setBackgroundResource(R.drawable.stroke_corner_transparent_background);
        }

        holder.addfriends_item_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLogs.e("haifeng","点击申请好友按钮");
                context.requestAddFriends(lists.get(position).id,position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        RoundedImageView addfriends_item_head;
        TextView addfriends_item_name,addfriends_item_mobile,addfriends_item_butt;
    }
}
