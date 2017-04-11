package com.zhuyun.jingxi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.bean.ProvenceBean;

import java.util.ArrayList;

/**
 * Created by user0081 on 2016/7/5.
 */
public class ProvenceActivityAdapter extends BaseAdapter{
    private Context context;
    //需要集合 list
    private ArrayList<ProvenceBean> provences;
    public ProvenceActivityAdapter(Context context,ArrayList<ProvenceBean> provences){
        this.context=context;
        this.provences=provences;
    }

    @Override
    public int getCount() {
        return provences.size();
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
        TextView provence_lv_item_tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_address_lv_item,null);
            holder=new ViewHolder();

            holder.provence_lv_item_tv= (TextView) convertView.findViewById(R.id.provence_lv_item_tv);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }

        //以下赋值
        holder.provence_lv_item_tv.setText(provences.get(position).provenceName);

        return convertView;
    }
}
