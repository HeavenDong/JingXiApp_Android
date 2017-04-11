package com.zhuyun.jingxi.android.adapter;

import android.content.Context;
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
import com.zhuyun.jingxi.android.HomeListEntity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.PayGiftActivity;
import com.zhuyun.jingxi.android.activty.WishDetailsActivity;
import com.zhuyun.jingxi.android.activty.WishHomeActivity;
import com.zhuyun.jingxi.android.fragment.HomeFragment;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;
import com.zhuyun.jingxi.android.view.CirclrDegreeView;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by user0081 on 2016/6/13.
 */
public class HomeListAdapter extends BaseAdapter {
    private final Context context;
    private final List<HomeListEntity> lists;
    private final HomeFragment homeFragment;
    private long myId;
    private String url;
    public HomeListAdapter(Context context, HomeFragment homeFragment, List<HomeListEntity> lists) {
        this.context=context;
        this.homeFragment=homeFragment;
        this.lists=lists;
        myId= (long) SharedPreUtils.get(App.getAppContext(), "user_id", (long)0);

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
        TextView item_name,shuoming,gift_name,homelist_item_update;
        ImageView home_item_sex,item_good;
        CirclrDegreeView home_img_item;
        RelativeLayout home_item_crowd,home_item_good,home_item_discuss;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.layout_homelist_item,null);

            holder.home_item_head = (RoundedImageView) convertView.findViewById(R.id.home_item_head);
            holder.item_name= (TextView) convertView.findViewById(R.id.item_name);
            holder.homelist_item_update= (TextView) convertView.findViewById(R.id.homelist_item_update);
            holder.shuoming= (TextView) convertView.findViewById(R.id.shuoming);
            holder.gift_name=(TextView) convertView.findViewById(R.id.gift_name);

            holder.home_item_sex= (ImageView) convertView.findViewById(R.id.home_item_sex);
            holder.home_img_item= (CirclrDegreeView) convertView.findViewById(R.id.home_img_item);
            holder.item_good= (ImageView) convertView.findViewById(R.id.item_good);
            holder.home_item_crowd= (RelativeLayout) convertView.findViewById(R.id.home_item_crowd);
            holder.home_item_good= (RelativeLayout) convertView.findViewById(R.id.home_item_good);
            holder.home_item_discuss= (RelativeLayout) convertView.findViewById(R.id.home_item_discuss);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        if (lists.get(position).getWishUserId()==myId){
            holder.home_item_head.setEnabled(false);
        }else {
            holder.home_item_head.setEnabled(true);
        }
        if (lists.get(position).getGender()==0){//女
            holder.home_item_sex.setImageResource(R.drawable.nv);
        }else {
            holder.home_item_sex.setImageResource(R.drawable.nan);
        }
        if(lists.get(position).getIsLiked()==1){
            holder.item_good.setImageResource(R.drawable.yizan);
        }else {
            holder.item_good.setImageResource(R.drawable.zan);
        }
        holder.shuoming.setText(lists.get(position).getContent().toString());
        holder.item_name.setText(lists.get(position).getNickName().toString());
        holder.gift_name.setText(lists.get(position).getGoodsName().toString());
        holder.homelist_item_update.setText(CommanUtil.transhms(lists.get(position).getUtpTime(),"MM.dd hh:mm"));
        Picasso.with(App.getAppContext()).load(lists.get(position).getPortraitUrl()).placeholder(R.drawable.shouyeliwu)
                .error(R.drawable.shouyeliwu).into(holder.home_img_item);
        Picasso.with(App.getAppContext()).load(lists.get(position).getPortraitUrl()).placeholder(R.drawable.touxiang_yuan)
                .error(R.drawable.touxiang_yuan).into(holder.home_item_head);
       //头像---许愿主页
            holder.home_item_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyLogs.e("haifeng","许愿listview点击 "+lists.get(position).getNickName());
                    Intent intent = new Intent(context, WishHomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("isFriend", lists.get(position).getIsFriend());
                    bundle.putLong("FriendsId", lists.get(position).getWishUserId());
                    bundle.putString("FriendsName", lists.get(position).getNickName());
                    bundle.putString("portraitUrl", lists.get(position).getPortraitUrl());
                    bundle.putInt("gender", lists.get(position).getGender());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        //送礼
        holder.home_item_crowd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PayGiftActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",lists.get(position).getType());
                bundle.putLong("wishId",lists.get(position).getWishId());
                bundle.putString("from","爱惊喜");
                bundle.putString("person",lists.get(position).getNickName().toString());
                bundle.putString("giftName",lists.get(position).getGoodsName().toString());
                bundle.putDouble("price",lists.get(position).getCfPrice());
                bundle.putString("number","1");
                bundle.putString("giftUrl","http://sta.273.com.cn/app/mbs/img/mobile_default.png");  /**假数据*/
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        //点赞
        holder.home_item_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLogs.e("haifeng","赞  isLike="+lists.get(position).getIsLiked());
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("id", lists.get(position).getWishId());
                if (lists.get(position).getIsLiked()==1){
                    url= Url.CANCEL_LIKE;
                }else {
                    url= Url.ADD_LIKE;
                }
                homeFragment.changlikeInfo(params,url,position);
            }
        });
        //评论
        holder.home_item_discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=  new Intent(context,WishDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("wishID",lists.get(position).getWishId());
                bundle.putString("nickName",lists.get(position).getNickName());
                bundle.putString("portraitUrl",lists.get(position).getPortraitUrl());
                bundle.putInt("gender",lists.get(position).getGender());
                bundle.putString("content",lists.get(position).getContent());
                bundle.putLong("utpTime",lists.get(position).getUtpTime());
                bundle.putString("imgUrl",lists.get(position).getImgUrl());
                bundle.putString("giftName",lists.get(position).getGoodsName());
                bundle.putInt("type",lists.get(position).getType());//（1：普通许愿； 2：众筹许愿）
                bundle.putDouble("price",lists.get(position).getCfPrice());
                bundle.putInt("likeNum",lists.get(position).getLikeNum());
                bundle.putInt("commNum",lists.get(position).getCommNum());
                bundle.putInt("isFriend",lists.get(position).getIsFriend());
                bundle.putString("tag","评论");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}