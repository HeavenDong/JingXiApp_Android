package com.zhuyun.jingxi.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.BirthdayReminderActivity;
import com.zhuyun.jingxi.android.bean.BirthdayBean;
import com.zhuyun.jingxi.android.utils.CommanUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user0081 on 2016/6/18.
 */
public class BirthdayGridAdapter extends BaseAdapter {
    public static int lastPosition = -1;
    private final BirthdayReminderActivity context;
    private final List<BirthdayBean> birthdayList;
    private final HashMap<Integer,Boolean> isSelect;

    public BirthdayGridAdapter(BirthdayReminderActivity context, List<BirthdayBean> birthdayList, HashMap isSelectTy) {
        this.context=context;
        this.birthdayList=birthdayList;
        this.isSelect=isSelectTy;
    }

    @Override
    public int getCount() {
        return birthdayList.size();
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
        CheckBox birthday_item_check;
        TextView birthday_item_name,birthday_item_time;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView==null){
            holder= new ViewHolder();
            convertView=View.inflate(context, R.layout.layout_birthday_grid_item,null);
            holder.birthday_item_check= (CheckBox) convertView.findViewById(R.id.birthday_item_check);
            holder.birthday_item_name= (TextView) convertView.findViewById(R.id.birthday_item_name);
            holder.birthday_item_time= (TextView) convertView.findViewById(R.id.birthday_item_time);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.birthday_item_name.setText(birthdayList.get(position).nickName.toString());
        holder.birthday_item_time.setText(CommanUtil.transhms(birthdayList.get(position).birthday,"MM月dd日"));

        holder.birthday_item_check.setChecked(isSelect.get(position));
        holder.birthday_item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSelect.put(position, isChecked);
                if(buttonView.isChecked())
                {
                    for(int i=0;i<birthdayList.size();i++)
                    {
                        //把其他的checkbox设置为false
                        if(i!=position){
                            isSelect.put(i, false);
                        }
                    }
                }
                //通知适配器更改
                BirthdayGridAdapter.this.notifyDataSetChanged();
            }
        });
        if (lastPosition == position) {
            holder.birthday_item_check.setChecked(true);
        }

        return convertView;
    }


}
