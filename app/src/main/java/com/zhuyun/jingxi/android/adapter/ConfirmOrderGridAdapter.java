package com.zhuyun.jingxi.android.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.ConfirmOrderActivity;
import com.zhuyun.jingxi.android.bean.PublishWishBean;

import java.util.List;

/**
 * Created by user0081 on 2016/6/18.
 */
public class ConfirmOrderGridAdapter extends BaseAdapter {
    public  int lastPosition = -1;
    private int currentPosition=0;
    private final ConfirmOrderActivity context;
    private final List<PublishWishBean> lists;
    private boolean isTow=true;

    public ConfirmOrderGridAdapter(ConfirmOrderActivity context, List<PublishWishBean> lists) {
       this.context=context;
       this.lists=lists;
       lists.get(0).isSelected=true;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView==null){
            holder= new ViewHolder();
            convertView=View.inflate(context, R.layout.layout_publishwish_grid_item,null);
            holder.publishwish_item_standard= (TextView) convertView.findViewById(R.id.publishwish_item_standard);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        //notifyDataSetChanged的时候，根据标记布尔值isSelected显示不同颜色
        if(lists.get(position).isSelected){
            holder.publishwish_item_standard.setTextColor(context.getResources().getColor(R.color.white));
            holder.publishwish_item_standard.setBackgroundResource(R.drawable.img_red_fillet_background2);

        }else {
            holder.publishwish_item_standard.setTextColor(context.getResources().getColor(R.color.gray_content));
            holder.publishwish_item_standard.setBackgroundResource(R.drawable.img_gray_fillet_background2);
        }
        //监听点击，如果是选中（isSelected==true）,遍历集合将其他item变成未选中状态（isSelected==false）,notifyDataSetChanged。
        holder.publishwish_item_standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lists.get(position).isSelected=true) {
                    for (int i = 0; i < lists.size(); i++) {
                        if (i != position) {
                            lists.get(i).isSelected = false;
                        }
                    }
                }
                lastPosition=currentPosition;
                currentPosition=position;
                Log.e("haifeng","点击 lastPosition="+lastPosition);
                Log.e("haifeng","点击 currentPosition="+currentPosition);
                Log.e("haifeng","点击 position="+position);
                Log.e("haifeng","点击 ---------------------------");

                //二次点击，这个item改变
                if (lastPosition == currentPosition) {
                    if (isTow){
                        isTow=false;
                        lists.get(currentPosition).isSelected=false;
                    }else {
                        isTow=true;
                        lists.get(currentPosition).isSelected=true;
                    }
                }

                ConfirmOrderGridAdapter.this.notifyDataSetChanged();
            }
        });



        return convertView;
    }

    private class ViewHolder {
        TextView publishwish_item_standard;
    }
    public int getCurrentPosition(){
        return currentPosition;
    }

}
