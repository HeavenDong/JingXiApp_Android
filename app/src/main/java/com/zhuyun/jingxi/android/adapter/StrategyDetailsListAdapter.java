package com.zhuyun.jingxi.android.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.GiftDetailsActivity;
import com.zhuyun.jingxi.android.activty.StrategyDetailsActivity;
import com.zhuyun.jingxi.android.bean.StrategyDetailsBean;
import com.zhuyun.jingxi.android.utils.MyLogs;

import java.util.List;

/**
 * Created by user0081 on 2016/6/27.
 */
public class StrategyDetailsListAdapter extends BaseAdapter{
    private final StrategyDetailsActivity context;
    private final List<StrategyDetailsBean> lists;

    public StrategyDetailsListAdapter(StrategyDetailsActivity context, List<StrategyDetailsBean> lists) {
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

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=  View.inflate(context, R.layout.layout_strategydetails_listitem,null);
            holder=  new ViewHolder();
            holder.to_seemore= (TextView) convertView.findViewById(R.id.to_seemore);
            holder.strategy_details_item_name=(TextView) convertView.findViewById(R.id.strategy_details_item_name);
            holder.strategy_details_item_des=(TextView) convertView.findViewById(R.id.strategy_details_item_des);
            holder.strategy_details_item_price=(TextView) convertView.findViewById(R.id.strategy_details_item_price);
            holder.strategy_details_item_img=(ImageView) convertView.findViewById(R.id.strategy_details_item_img);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.strategy_details_item_name.setText(lists.get(position).name.toString());
        holder.strategy_details_item_des.setText(lists.get(position).des.toString());
        holder.strategy_details_item_price.setText("￥"+lists.get(position).price);
        Picasso.with(App.getAppContext()).load(lists.get(position).imgIconUrl).placeholder(R.drawable.shouyeliwu)
                .error(R.drawable.shouyeliwu).into(holder.strategy_details_item_img);

        holder.to_seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLogs.e("haifeng","点击"+position);
                Intent intent=  new Intent(context,GiftDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("giftId",lists.get(position).id);
                bundle.putString("from","StrategyDetailsActivity");
                intent.putExtras(bundle);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView strategy_details_item_img;
        TextView strategy_details_item_name,strategy_details_item_des,strategy_details_item_price,to_seemore;
    }
}
