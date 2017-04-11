package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.FriendsActivity;
import com.zhuyun.jingxi.android.bean.Friends;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by user0081 on 2016/6/23.
 */
public class FriendsListAdapter extends BaseAdapter{
    private final List<Friends> lists;
    private FriendsActivity context;

    public FriendsListAdapter(FriendsActivity context, List<Friends> lists) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=  View.inflate(context, R.layout.layout_friendlist_item,null);
            holder=  new ViewHolder();
            holder.friends_item_img= (RoundedImageView) convertView.findViewById(R.id.friends_item_img);
            holder.friends_item_name= (TextView) convertView.findViewById(R.id.friends_item_name);
            holder.friends_item_sex= (ImageView) convertView.findViewById(R.id.friends_item_sex);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.friends_item_name.setText(lists.get(position).nickName);
        if (!lists.get(position).portrait.equals("")) {
            Picasso.with(App.getAppContext()).load(lists.get(position).portrait).placeholder(R.drawable.touxiang_yuan)
                    .error(R.drawable.touxiang_yuan).into(holder.friends_item_img);
        }
        if (lists.get(position).gender==0){
            holder.friends_item_sex.setImageResource(R.drawable.nv);
        }else {
            holder.friends_item_sex.setImageResource(R.drawable.nan);
        }

        return convertView;
    }

    private class ViewHolder {
        RoundedImageView friends_item_img;
        ImageView friends_item_sex;
        TextView friends_item_name;
    }
}
