package com.zhuyun.jingxi.android.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.PublishWishActivity;
import com.zhuyun.jingxi.android.bean.PublishWishBean;

import java.util.List;

/**
 * Created by user0081 on 2016/6/18.
 */
public class PublishWishGridAdapter extends BaseAdapter {
    public  int lastPosition = -1;
    private int currentPosition=0;
    private final PublishWishActivity context;
    private final List<PublishWishBean> lists;

    public PublishWishGridAdapter(PublishWishActivity context, List<PublishWishBean> lists) {
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
            holder.publishwish_item_tv= (TextView) convertView.findViewById(R.id.publishwish_item_standard);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        //notifyDataSetChanged的时候，根据标记布尔值isSelected显示不同颜色
        if(lists.get(position).isSelected){
            lists.get(position).isSelected=true;
            holder.publishwish_item_tv.setTextColor(context.getResources().getColor(R.color.white));
            holder.publishwish_item_tv.setBackgroundResource(R.drawable.img_red_fillet_background2);

        }else {
            lists.get(position).isSelected=false;
            holder.publishwish_item_tv.setTextColor(context.getResources().getColor(R.color.gray_content));
            holder.publishwish_item_tv.setBackgroundResource(R.drawable.img_gray_fillet_background2);
        }
        //监听点击，如果是选中（isSelected==true）,遍历集合将其他item变成未选中状态（isSelected==false）,notifyDataSetChanged。
        holder.publishwish_item_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lists.get(position).isSelected=true) {
                    for (int i = 0; i < lists.size(); i++) {
                        if (i != position) {
                            lists.get(i).isSelected = false;
                        }
                    }
                }
                currentPosition=position;
                PublishWishGridAdapter.this.notifyDataSetChanged();
            }
        });

        //二次点击，这个item改变
      if (lastPosition == position) {
           if(lists.get(position).isSelected){
               Log.e("haifeng","二次点击，红变灰");
               lists.get(position).isSelected=false;
               holder.publishwish_item_tv.setTextColor(context.getResources().getColor(R.color.gray_content));
               holder.publishwish_item_tv.setBackgroundResource(R.drawable.img_gray_fillet_background2);


         }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView publishwish_item_tv;
    }
    public int getCurrentPosition(){
        return currentPosition;
    }

}
