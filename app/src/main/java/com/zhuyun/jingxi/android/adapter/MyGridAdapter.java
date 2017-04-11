package com.zhuyun.jingxi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.GiftClassifyEntity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by user0081 on 2016/6/14.
 */
public class MyGridAdapter extends BaseAdapter{

    private final Context context;
    private final List<GiftClassifyEntity> list;

    public MyGridAdapter(Context context, List<GiftClassifyEntity> gridList) {
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
            convertView=View.inflate(context, R.layout.layout_gift_classify_item,null);
            holder.gift_class_img= (RoundedImageView) convertView.findViewById(R.id.gift_class_img);
            holder.gift_class_tv= (TextView) convertView.findViewById(R.id.gift_class_tv);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.gift_class_tv.setText(list.get(position).getName().toString());
        if (list.get(position).getImgIconUrl()!=null&&!list.get(position).getImgIconUrl().equals("")) {
            Picasso.with(App.getAppContext()).load(list.get(position).getImgIconUrl()).placeholder(R.drawable.gongluetuijian)
                    .error(R.drawable.gongluetuijian).into(holder.gift_class_img);

//            Picasso.with(App.getAppContext()).load("http://sta.273.com.cn/app/mbs/img/mobile_default.png").into(holder.gift_class_img);
        }
        return convertView;
    }

    private class ViewHolder {
        RoundedImageView gift_class_img;
        TextView  gift_class_tv;
    }
}
