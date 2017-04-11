package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.MobilePhoneActivity;
import com.zhuyun.jingxi.android.bean.PhoneBean;

import java.util.ArrayList;

/**
 * Created by user0081 on 2016/6/18.
 */
public class FriendsPhoneAdapter extends BaseAdapter {

    private final ArrayList<PhoneBean> phoneNameList;
    private final MobilePhoneActivity context;
    private final String from;

    public FriendsPhoneAdapter(MobilePhoneActivity context, ArrayList<PhoneBean> phoneNameList, String from) {
        this.phoneNameList=phoneNameList;
        this.context=context;
        this.from=from;
    }

    @Override
    public int getCount() {
        return phoneNameList.size();
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
            convertView=View.inflate(context, R.layout.layout_friends_phone_item,null);
            holder= new ViewHolder();
            holder.friends_phone_name = (TextView) convertView.findViewById(R.id.friends_phone_name);
            holder.friends_phone_call = (TextView) convertView.findViewById(R.id.friends_phone_call);
            holder.phone_listitem_butt= (TextView) convertView.findViewById(R.id.phone_listitem_butt);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        if (from.equals("ReceiverMessaage")){
            holder.phone_listitem_butt.setVisibility(View.GONE);
        }

        holder.friends_phone_name.setText(phoneNameList.get(position).phoneName.toString());
        if (phoneNameList.get(position).ischecked){
            holder.phone_listitem_butt.setEnabled(false);
            holder.phone_listitem_butt.setText("已邀请");
            holder.phone_listitem_butt.setTextColor(context.getResources().getColor(R.color.grey));
            holder.phone_listitem_butt.setBackgroundResource(R.drawable.img_white_fillet_background);
        }else {
            holder.phone_listitem_butt.setEnabled(true);
            holder.phone_listitem_butt.setText("邀请");
            holder.phone_listitem_butt.setTextColor(context.getResources().getColor(R.color.red));
            holder.phone_listitem_butt.setBackgroundResource(R.drawable.img_red_transparency_fillet_background);
        }
        holder.phone_listitem_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.sendMessage(position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView friends_phone_name, friends_phone_call,phone_listitem_butt;
    }
}
