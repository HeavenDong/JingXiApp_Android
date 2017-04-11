package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.RecordForSendGiftActivity;
import com.zhuyun.jingxi.android.bean.RecordGiftBean;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.view.CirclrDegreeView;

import java.util.List;

/**
 * Created by user0081 on 2016/6/16.
 */
public class RecordForSendGiftListAdapter extends BaseAdapter {
    private final RecordForSendGiftActivity context
            ;
    private final List<RecordGiftBean> lists;

    public RecordForSendGiftListAdapter(RecordForSendGiftActivity context, List<RecordGiftBean> lists) {
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
        CirclrDegreeView sendgiftrecord_item_img;
        TextView send_giftname,giftrecord_send_name,sendgiftrecord_item_times,send_price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_sendgiftrecord_list_item,null);
            holder=new ViewHolder();
            holder.sendgiftrecord_item_img= (CirclrDegreeView) convertView.findViewById(R.id.sendgiftrecord_item_img);
            holder.send_giftname= (TextView) convertView.findViewById(R.id.send_giftname);
            holder.giftrecord_send_name= (TextView) convertView.findViewById(R.id.giftrecord_send_name);
            holder.sendgiftrecord_item_times= (TextView) convertView.findViewById(R.id.sendgiftrecord_item_times);
            holder.send_price= (TextView) convertView.findViewById(R.id.send_price);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.send_giftname.setText(lists.get(position).goodsName);
        holder.giftrecord_send_name.setText(lists.get(position).recipientNickName);
        holder.sendgiftrecord_item_times.setText(CommanUtil.transhms(lists.get(position).utpTime,"MM.dd hh:mm"));
        holder.send_price.setText(lists.get(position).giftsPrice+"");
        Picasso.with(App.getAppContext()).load(lists.get(position).imgUrl).placeholder(R.drawable.baoxiangtupian)
                .error(R.drawable.baoxiangtupian).into(holder.sendgiftrecord_item_img);
        return convertView;
    }
}
