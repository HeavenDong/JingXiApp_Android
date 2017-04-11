package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.HotActivity;
import com.zhuyun.jingxi.android.bean.GiftBean;

import java.util.List;

/**
 * Created by user0081 on 2016/6/14.
 */
public class HotGridAdapter extends BaseAdapter{

    private final HotActivity context;
    private final List<GiftBean> list;

    public HotGridAdapter(HotActivity context, List<GiftBean> gridList) {
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
            holder.grid_item_name= (TextView) convertView.findViewById(R.id.grid_item_name);
            holder.grid_item_price= (TextView) convertView.findViewById(R.id.grid_item_price);
            holder.grid_item_wish= (TextView) convertView.findViewById(R.id.grid_item_wish);
            holder.grid_item_img=(ImageView) convertView.findViewById(R.id.grid_item_img);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.grid_item_name.setText(list.get(position).name.toString());
        holder.grid_item_price.setText("￥"+list.get(position).price);
        Picasso.with(App.getAppContext()).load(list.get(position).imgIconUrl).placeholder(R.drawable.darentuijian)
                .error(R.drawable.darentuijian).into(holder.grid_item_img);
        return convertView;
    }

    private class ViewHolder {
        ImageView grid_item_img;
        TextView  grid_item_name,grid_item_price,grid_item_wish;
    }
}
