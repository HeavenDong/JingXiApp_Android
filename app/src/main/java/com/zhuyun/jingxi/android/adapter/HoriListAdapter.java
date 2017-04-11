package com.zhuyun.jingxi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.GiftRecommentEntity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.view.CirclrDegreeView;

import java.util.List;

/**
 *   礼品屋__推荐__横向Listview适配器
 */
public class HoriListAdapter extends BaseAdapter {
    private final Context context;
    private final List<GiftRecommentEntity> list;

    public HoriListAdapter(Context context, List<GiftRecommentEntity> strategyList) {
        this.context=context;
        this.list=strategyList;
    }

    @Override
    public int getCount() {
        return list.size();
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
        CirclrDegreeView hori_item_img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_recommend_horilist_item,null);
            holder=new ViewHolder();
            holder.hori_item_img= (CirclrDegreeView) convertView.findViewById(R.id.hori_item_img);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        if (list.size()>0) {
            Picasso.with(App.getAppContext()).load(list.get(position).getImgIconUrl()).placeholder(R.drawable.fenlei)
                    .error(R.drawable.fenlei).into(holder.hori_item_img);
        }
        return convertView;
    }
}
