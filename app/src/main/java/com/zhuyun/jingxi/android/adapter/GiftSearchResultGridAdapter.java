package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.GiftSearchResultActivity;
import com.zhuyun.jingxi.android.bean.GiftBean;

import java.util.List;

/**
 * Created by user0081 on 2016/6/14.
 */
public class GiftSearchResultGridAdapter extends BaseAdapter{

    private final GiftSearchResultActivity context;
    private final List<GiftBean> list;

    public GiftSearchResultGridAdapter(GiftSearchResultActivity context, List<GiftBean> gridList) {
        this.context=context;
        this.list=gridList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
           holder= new ViewHolder();
            convertView=View.inflate(context, R.layout.layout_recommend_grid_item,null);
            convertView.findViewById(R.id.gift_class_img);
            convertView.findViewById(R.id.gift_class_tv);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewHolder {
         ImageView gift_class_img;
         TextView  gift_class_tv;
    }
}
