package com.zhuyun.jingxi.android.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.PayGiftActivity;
import com.zhuyun.jingxi.android.activty.WishActivity;
import com.zhuyun.jingxi.android.activty.WishHomeActivity;
import com.zhuyun.jingxi.android.bean.WishBean;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by user0081 on 2016/7/6.
 */
public class WishListAdapter  extends BaseAdapter {
    private final WishActivity context;
    private final List<WishBean> lists;
    private long myId;
//    private int [] colors;
    public WishListAdapter(WishActivity context, List<WishBean> lists) {
        this.context=context;
        this.lists=lists;
//        colors=new int[]{context.getResources().getColor(R.color.imback_1),
//                context.getResources().getColor(R.color.imback_2),
//                context.getResources().getColor(R.color.imback_3),
//                context.getResources().getColor(R.color.imback_4)};
        myId= (long) SharedPreUtils.get(App.getAppContext(), "user_id", (long)0);
        MyLogs.e("haifeng","进入许愿56"+myId);
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
       RoundedImageView home_item_head;
       TextView item_name,homelist_item_update,shuoming,gift_name,item_good_number,home_item_discuss_number;
        ImageView home_img_item;
        RelativeLayout home_item_crowd;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.layout_homelist_item,null);
            holder=new ViewHolder();
            holder.home_item_head= (RoundedImageView) convertView.findViewById(R.id.home_item_head);
            holder.item_name= (TextView) convertView.findViewById(R.id.item_name);
            holder.homelist_item_update= (TextView) convertView.findViewById(R.id.homelist_item_update);
            holder.shuoming= (TextView) convertView.findViewById(R.id.shuoming);
            holder.gift_name= (TextView) convertView.findViewById(R.id.gift_name);
            holder.item_good_number= (TextView) convertView.findViewById(R.id.item_good_number);
            holder.home_item_discuss_number= (TextView) convertView.findViewById(R.id.home_item_discuss_number);
            holder.home_img_item= (ImageView) convertView.findViewById(R.id.home_img_item);
            holder.home_item_crowd= (RelativeLayout) convertView.findViewById(R.id.home_item_crowd);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        MyLogs.e("haifeng","进入许愿7"+lists.get(position).wishUserId);
        MyLogs.e("haifeng","进入许愿7"+myId);
        if (lists.get(position).wishUserId==myId){
            holder.home_item_head.setEnabled(false);
            MyLogs.e("haifeng","进入许愿  不可点");
        }else {
            holder.home_item_head.setEnabled(true);
            MyLogs.e("haifeng","进入许愿7可点");
        }
        holder.item_name.setText(lists.get(position).nickName);
        holder.homelist_item_update.setText(CommanUtil.transhms(lists.get(position).utpTime,"MM.dd hh:mm"));
        holder.shuoming.setText(lists.get(position).content);
        holder.gift_name.setText(lists.get(position).goodsName);
        holder.item_good_number.setText(lists.get(position).likeNum+"");
        holder.home_item_discuss_number.setText(lists.get(position).commNum+"");

//        holder.home_img_item.setBackgroundColor(colors[position%(colors.length)]);
        Picasso.with(App.getAppContext()).load(lists.get(position).imgUrl).placeholder(R.drawable.shouyeliwu)
                .error(R.drawable.shouyeliwu).into(holder.home_img_item);
        Picasso.with(App.getAppContext()).load(lists.get(position).portraitUrl).placeholder(R.drawable.touxiang_yuan)
                .error(R.drawable.touxiang_yuan).into(holder.home_item_head);
        if (lists.get(position).wishUserId!=(long) SharedPreUtils.get(App.getAppContext(), "user_id", (long)0)) {
            holder.home_item_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WishHomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("FriendsId", lists.get(position).wishUserId);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                }
            });
        }
       // 送礼
        holder.home_item_crowd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PayGiftActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putInt("type",lists.get(position).getType());
//                bundle.putLong("wishId",lists.get(position));
                bundle.putString("from","爱惊喜");
                bundle.putString("person",lists.get(position).nickName.toString());
                bundle.putString("giftName",lists.get(position).goodsName.toString());
                bundle.putDouble("price",100.0);/**假数据*/
                bundle.putString("number","1");
                bundle.putString("giftUrl",lists.get(position).imgUrl);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
