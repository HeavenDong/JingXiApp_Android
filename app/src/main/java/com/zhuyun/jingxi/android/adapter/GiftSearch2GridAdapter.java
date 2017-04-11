package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.GiftFreeSelectActivity;
import com.zhuyun.jingxi.android.bean.GiftSearchBean;
import com.zhuyun.jingxi.android.utils.MyLogs;

import java.util.List;

/**
 * Created by user0081 on 2016/6/18.
 */
public class GiftSearch2GridAdapter extends BaseAdapter {
    private final GiftFreeSelectActivity context;
    private final List<GiftSearchBean> lists;
    private boolean isTow=true;
    public  int lastPosition = -1;
    private int currentPosition=-1;

    public GiftSearch2GridAdapter(GiftFreeSelectActivity context, List<GiftSearchBean> lists) {
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
        holder.publishwish_item_tv.setText(lists.get(position).name.toString());

        //notifyDataSetChanged的时候，根据标记布尔值isSelected显示不同颜色
        if(lists.get(position).isSelected){
            lists.get(position).isSelected=true;
            holder.publishwish_item_tv.setBackgroundResource(R.drawable.img_red_transparency_fillet_background);

        }else {
            lists.get(position).isSelected=false;
            holder.publishwish_item_tv.setBackgroundResource(R.drawable.img_white_fillet_background);
        }
        //监听点击，如果是选中（isSelected==true）,遍历集合将其他item变成未选中状态（isSelected==false）,notifyDataSetChanged。
        holder.publishwish_item_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setEditUnFocus(2);
                if (lists.get(position).isSelected=true) {
                    for (int i = 0; i < lists.size(); i++) {
                        if (i != position) {
                            lists.get(i).isSelected = false;
                        }
                    }
                }

                lastPosition=currentPosition;
                currentPosition=position;
//                Log.e("haifeng","2点击 lastPosition="+lastPosition);
//                Log.e("haifeng","2点击 currentPosition="+currentPosition);
//                Log.e("haifeng","2点击 position="+position);

                //二次点击，这个item改变
                if (lastPosition == currentPosition) {
                    if (isTow){
                        isTow=false;
                        lists.get(currentPosition).isSelected=false;
                         currentPosition=-1;
                    }else {
                        isTow=true;
                        lists.get(currentPosition).isSelected=true;
                    }
                }
                GiftSearch2GridAdapter.this.notifyDataSetChanged();
            }
        });


        return convertView;
    }

    private class ViewHolder {
        TextView publishwish_item_tv;
    }
    public int getCurrentPosition(){
        MyLogs.e("haifeng", "获取坐标=="+currentPosition);
        return currentPosition;
    }
    public void setCurrentPosition(int currentPosition){
        this.currentPosition= currentPosition;
    }
}
