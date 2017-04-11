package com.zhuyun.jingxi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.GiftRecommentEntity;
import com.zhuyun.jingxi.android.R;

import java.util.List;

/**
 * Created by user0081 on 2016/6/14.
 */
public class HotRecommendGridAdapter extends BaseAdapter{

    private final Context context;
    private final List<GiftRecommentEntity> list;

    public HotRecommendGridAdapter(Context context, List<GiftRecommentEntity> gridList) {
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
            holder.grid_item_img= (ImageView) convertView.findViewById(R.id.grid_item_img);
            holder.grid_item_name= (TextView) convertView.findViewById(R.id.grid_item_name);
            holder.grid_item_price= (TextView) convertView.findViewById(R.id.grid_item_price);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        if (list.size()>0) {
            if (list.get(position).getImgIconUrl() != null && !list.get(position).getImgIconUrl().equals("")) {
                Picasso.with(App.getAppContext()).load(list.get(position).getImgIconUrl()).placeholder(R.drawable.darentuijian)
                        .error(R.drawable.darentuijian).into(holder.grid_item_img);
            } else {
                Picasso.with(App.getAppContext()).load("http://sta.273.com.cn/app/mbs/img/mobile_default.png").into(holder.grid_item_img);
            }
            holder.grid_item_name.setText(list.get(position).getName().toString());
            holder.grid_item_price.setText("￥" + list.get(position).getPrice());
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView grid_item_img;
        TextView  grid_item_name,grid_item_price;
    }
}
