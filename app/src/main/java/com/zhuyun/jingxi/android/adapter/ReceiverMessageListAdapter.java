package com.zhuyun.jingxi.android.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.EditReceiverMessaageActivity;
import com.zhuyun.jingxi.android.activty.ReceiverMessaageActivity;
import com.zhuyun.jingxi.android.bean.ReceiverMessage;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user0081 on 2016/7/6.
 */
public class ReceiverMessageListAdapter extends BaseAdapter{
    private final ReceiverMessaageActivity context;
    private final List<ReceiverMessage> lists;
    public  int lastPosition = -1;
    private int currentPosition=0;
    private boolean isTow=true;
    public ReceiverMessageListAdapter(ReceiverMessaageActivity context, List<ReceiverMessage> lists) {
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

    private class ViewHolder {
        ImageView receive_msg_item_check;
        RelativeLayout receive_msg_item_moren;
        LinearLayout receive_msg_item_edit,receive_msg_item_delect;
        TextView receive_msg_item_name,receive_msg_item_mobile,receive_msg_item_address;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView==null){
            convertView=  View.inflate(context, R.layout.layout_receiver_msg_item,null);
            holder=  new ViewHolder();
            holder.receive_msg_item_moren= (RelativeLayout) convertView.findViewById(R.id.receive_msg_item_moren);
            holder.receive_msg_item_delect= (LinearLayout) convertView.findViewById(R.id.receive_msg_item_delect);
            holder.receive_msg_item_edit= (LinearLayout) convertView.findViewById(R.id.receive_msg_item_edit);
            holder.receive_msg_item_check= (ImageView) convertView.findViewById(R.id.receive_msg_item_check);

            holder.receive_msg_item_name= (TextView) convertView.findViewById(R.id.receive_msg_item_name);
            holder.receive_msg_item_mobile= (TextView) convertView.findViewById(R.id.receive_msg_item_mobile);
            holder.receive_msg_item_address= (TextView) convertView.findViewById(R.id.receive_msg_item_address);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.receive_msg_item_name.setText(lists.get(position).name.toString());
        holder.receive_msg_item_mobile.setText(lists.get(position).mobile.toString());
        holder.receive_msg_item_address.setText(lists.get(position).address.toString());
        if(lists.get(position).isDefault==1){
            lists.get(position).isDefault=1;
            holder.receive_msg_item_check.setBackgroundResource(R.drawable.moren);

        }else {
            lists.get(position).isDefault=0;
            holder.receive_msg_item_check.setBackgroundResource(R.drawable.shxx);
        }


        //监听点击，如果是选中（isSelected==true）,遍历集合将其他item变成未选中状态（isSelected==false）,notifyDataSetChanged。
        holder.receive_msg_item_moren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lists.get(position).isDefault==0) {//点击的是非默认，修改
                    if (CommanUtil.isNetworkAvailable()) {
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("id", lists.get(position).id);//	条目id
                        MyLogs.e("haifeng", "修改默认地址 id=" + lists.get(position).id);
                        context.changeDefaultAddress(params, position);
                    } else {
                        TempSingleToast.showToast(App.getAppContext(), "你的网络不给力");
                    }
                }else {
                    MyLogs.e("haifeng", "点击的是默认，不修改");
                }

//                if (lists.get(position).isDefault==0) {
//                    for (int i = 0; i < lists.size(); i++) {
//                        if (i != position) {
//                            lists.get(i).isDefault = 0;
//                        }
//                    }
//                    lists.get(position).isDefault=1;
//                }
//                lastPosition=currentPosition;
//                currentPosition=position;
////
//                Log.e("haifeng","点击 lastPosition="+lastPosition);
//                Log.e("haifeng","点击 currentPosition="+currentPosition);
//                Log.e("haifeng","点击 position="+position);
//                Log.e("haifeng","点击 ---------------------------");
//
//                //二次点击，这个item改变
//                if (lastPosition == currentPosition) {
//                    if (isTow){
//                        isTow=false;
//                        lists.get(currentPosition).isSelected=false;
//                    }else {
//                        isTow=true;
//                        lists.get(currentPosition).isSelected=true;
//                    }
//                }

//                ReceiverMessageListAdapter.this.notifyDataSetChanged();
            }
        });


        holder.receive_msg_item_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommanUtil.isNetworkAvailable()) {
                    context.deleteMessageDialog(position);
                }else {
                    TempSingleToast.showToast(App.getAppContext(),"你的网络不给力");
                }
            }
        });


        holder.receive_msg_item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=  new Intent(context,EditReceiverMessaageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("itemId",lists.get(position).id);
                bundle.putString("name",lists.get(position).name);
                bundle.putString("mobile",lists.get(position).mobile);
                /**假数据*/
                bundle.putString("address","北京市海淀区");
                bundle.putString("detailsAddress",lists.get(position).address);
                intent.putExtras(bundle);
                context.startActivityForResult(intent,402);
                context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        return convertView;
    }

    public int getCurrentPosition(){
        return currentPosition;
    }
}
