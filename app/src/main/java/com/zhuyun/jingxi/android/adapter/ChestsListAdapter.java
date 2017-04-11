package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.ChestsActivity;
import com.zhuyun.jingxi.android.bean.StarBean;
import com.zhuyun.jingxi.android.view.CirclrDegreeView;

import java.util.List;

/**
 * Created by user0081 on 2016/6/16.
 */
public class ChestsListAdapter extends BaseAdapter {
    private final ChestsActivity context;
    private final List<StarBean> lists;

    public ChestsListAdapter(ChestsActivity context, List<StarBean> lists) {
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

    class ViewHolder{
       CirclrDegreeView chests_item_img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_chestslist_item,null);
            holder=new ViewHolder();
            holder.chests_item_img= (CirclrDegreeView) convertView.findViewById(R.id.chests_item_img);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
            Picasso.with(App.getAppContext()).load("http://sta.273.com.cn/app/mbs/img/mobile_default.png").placeholder(R.drawable.baoxiangtupian)
                    .error(R.drawable.baoxiangtupian).into(holder.chests_item_img);

        return convertView;
    }
}
