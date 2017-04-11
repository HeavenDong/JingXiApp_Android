package com.zhuyun.jingxi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuyun.jingxi.android.GiftStrategyEntity;
import com.zhuyun.jingxi.android.R;

import java.util.List;


/**
 * Created by user0081 on 2016/6/13.
 */
public class StrategyListAdapter extends BaseAdapter {
    private final Context context;
    private final List<GiftStrategyEntity> lists;

    public StrategyListAdapter(Context context, List<GiftStrategyEntity> lists) {
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
        return position;
    }

    class ViewHolder{
      TextView strategy_listitem_context,strategy_listitem_wishcount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.layout_strategylist_item,null);
            holder.strategy_listitem_context= (TextView) convertView.findViewById(R.id.strategy_listitem_context);
            holder.strategy_listitem_wishcount= (TextView) convertView.findViewById(R.id.strategy_listitem_wishcount);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.strategy_listitem_wishcount.setText(lists.get(position).getWishCount()+"许愿");
        holder.strategy_listitem_context.setText(lists.get(position).getName());
        return convertView;
    }
}