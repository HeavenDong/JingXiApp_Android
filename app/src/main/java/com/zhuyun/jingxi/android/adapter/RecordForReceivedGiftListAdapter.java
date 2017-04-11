package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.RecordForReceivedGiftActivity;
import com.zhuyun.jingxi.android.bean.RecordGiftBean;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.view.CirclrDegreeView;

import java.util.List;

/**
 * Created by user0081 on 2016/6/16.
 */
public class RecordForReceivedGiftListAdapter extends BaseAdapter {
    private final RecordForReceivedGiftActivity context
            ;
    private final List<RecordGiftBean> lists;

    public RecordForReceivedGiftListAdapter(RecordForReceivedGiftActivity context, List<RecordGiftBean> lists) {
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
        CirclrDegreeView receive_giftrecord_item_img;
        TextView receive_giftname,giftrecord_receiver_name,receive_item_times,receive_price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_receivedgiftrecord_list_item,null);
            holder=new ViewHolder();
            holder.receive_giftrecord_item_img= (CirclrDegreeView) convertView.findViewById(R.id.receive_giftrecord_item_img);
            holder.receive_giftname= (TextView) convertView.findViewById(R.id.receive_giftname);
            holder.giftrecord_receiver_name= (TextView) convertView.findViewById(R.id.giftrecord_receiver_name);
            holder.receive_item_times= (TextView) convertView.findViewById(R.id.receive_item_times);
            holder.receive_price= (TextView) convertView.findViewById(R.id.receive_price);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.receive_giftname.setText(lists.get(position).goodsName);
        holder.giftrecord_receiver_name.setText(lists.get(position).recipientNickName);
        holder.receive_item_times.setText(CommanUtil.transhms(lists.get(position).utpTime,"MM.dd hh:mm"));
        holder.receive_price.setText(lists.get(position).giftsPrice+"");
        Picasso.with(App.getAppContext()).load(lists.get(position).imgUrl).placeholder(R.drawable.baoxiangtupian)
                .error(R.drawable.baoxiangtupian).into(holder.receive_giftrecord_item_img);
        return convertView;
    }
}
