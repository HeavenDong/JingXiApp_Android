package com.zhuyun.jingxi.android.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.BirthdayReminderActivity;
import com.zhuyun.jingxi.android.activty.WishDynamicActivity;
import com.zhuyun.jingxi.android.activty.WonderfulUndefinedActivity;
import com.zhuyun.jingxi.android.adapter.IMListAdapter;
import com.zhuyun.jingxi.android.base.BaseFragment;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

/**
 *   消息
 */
public class IMFragment extends BaseFragment {
   private ListView im_listview;
    private TextView huodong_tv,dongtai_tv;
    private TextView huodong_number,dongtai_number,shengri_number;

    @Override
    protected void initView(View view, Bundle bundle) {
        MyLogs.e("haifeng","进入IMFragment");
        MyLogs.e("haifeng","环信登录状态="+ DemoHelper.getInstance().isLoggedIn());
        im_listview= (ListView) view.findViewById(R.id.im_listview);
        View head= View.inflate(context,R.layout.layout_im_head,null);
        head.findViewById(R.id.huodong_linear).setOnClickListener(this);
        head.findViewById(R.id.dongtai_linear).setOnClickListener(this);
        head.findViewById(R.id.shengri_linear).setOnClickListener(this);

        huodong_tv= (TextView) head.findViewById(R.id.huodong_tv);
        dongtai_tv= (TextView) head.findViewById(R.id.dongtai_tv);

        huodong_number= (TextView) head.findViewById(R.id.huodong_number);
        dongtai_number= (TextView) head.findViewById(R.id.dongtai_number);
        shengri_number= (TextView) head.findViewById(R.id.shengri_number);
        im_listview.addHeaderView(head);
        initData();
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_fragment_im;
    }

    @Override
    protected void onClickEvent(View view) {
        switch (view.getId()){
            case R.id.huodong_linear:
                MyLogs.e("haifeng","活动");
                startActivity(new Intent(context, WonderfulUndefinedActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.dongtai_linear:
                MyLogs.e("haifeng","动态");
                startActivity(new Intent(context, WishDynamicActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.shengri_linear:
                MyLogs.e("haifeng","生日");
                startActivity(new Intent(context, BirthdayReminderActivity.class));
               break;
        }
    }

    private void initData() {
        im_listview.setAdapter(new IMListAdapter(context));
        im_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TempSingleToast.showToast(App.getAppContext(),"即时通讯");
                int pos=position-1;
                MyLogs.e("haifeng","点击第"+pos+"条，进即时通讯");
            }
        });

        /*假数据    4,3,10*/
        String str="你有"+4+"条活动消息";
        String str2="你的好友有"+3+"条许愿动态";

        huodong_number.setText("4");
        dongtai_number.setText("3");
        shengri_number.setText("10");
         // 设置颜色改变
        SpannableStringBuilder builder=new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(Color.RED), str.length() - 6,str.length() - 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        huodong_tv.setText(builder);

        SpannableStringBuilder builder2=new SpannableStringBuilder(str2);
        builder2.setSpan(new ForegroundColorSpan(Color.RED), str2.length() - 6,str2.length() - 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        dongtai_tv.setText(builder2);




    }


}
