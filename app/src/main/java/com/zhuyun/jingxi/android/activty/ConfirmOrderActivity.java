package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;

/**
 * Created by user0081 on 2016/6/17.   确认订单
 */
public class ConfirmOrderActivity extends BaseActivity{
    private TextView comfirmorder_giftname,buy_number,confirm_order_friendsname;
    private CheckBox for_me,for_friend;
    private ImageView confirm_order_img;
    private int sum;
    private double price;
    private String nickName,giftName,imgIconUrl;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入确认订单");
        buy_number= (TextView) findViewById(R.id.buy_number);
        confirm_order_img= (ImageView) findViewById(R.id.confirm_order_img);
        comfirmorder_giftname= (TextView) findViewById(R.id.comfirmorder_giftname);
        confirm_order_friendsname= (TextView) findViewById(R.id.confirm_order_friendsname);
        for_me= (CheckBox) findViewById(R.id.for_me);
        for_friend= (CheckBox) findViewById(R.id.for_friend);

        initData();
        setViewListener();
    }

    private void initData() {
        for_me.setChecked(true);
        giftName=getIntent().getExtras().getString("giftName");//礼物名
        imgIconUrl=getIntent().getExtras().getString("imgIconUrl");
        price=getIntent().getExtras().getDouble("price");//价格
        comfirmorder_giftname.setText(giftName);
        Picasso.with(App.getAppContext()).load(imgIconUrl).into(confirm_order_img);
    }
    private void setViewListener() {
        findViewById(R.id.confirm_order_back).setOnClickListener(this);
        findViewById(R.id.confirm_order_select_friends).setOnClickListener(this);
        findViewById(R.id.gopay_butt).setOnClickListener(this);
        findViewById(R.id.buy_jian).setOnClickListener(this);
        findViewById(R.id.buy_jia).setOnClickListener(this);
        findViewById(R.id.confirm_order_select_me).setOnClickListener(this);
        findViewById(R.id.confirm_order_select_friends).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
         setContentView(R.layout.layout_confirmorder_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.confirm_order_back:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.gopay_butt:
                if (for_me.isChecked()){
                    nickName = SharedPreUtils.get(App.getAppContext(), "nick_name", "未填写").toString();
                }else if (for_friend.isChecked()){
                    nickName=confirm_order_friendsname.getText().toString();
                }
                Intent intent = new Intent(ConfirmOrderActivity.this, PayGiftActivity.class);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                Bundle bundle = new Bundle();
                bundle.putString("from","确认订单");
                bundle.putString("giftName",giftName);
                bundle.putString("person",nickName);//收礼人
                bundle.putString("giftUrl",imgIconUrl);
                bundle.putDouble("price",price);
                bundle.putString("number",buy_number.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.buy_jia:
                sum= Integer.parseInt(buy_number.getText().toString());
                    int jia=sum+1;
                    buy_number.setText(jia+"");
                break;
            case R.id.buy_jian:
                sum = Integer.parseInt(buy_number.getText().toString());
                if (sum>1){
                    int jian=sum-1;
                    buy_number.setText(jian+"");
                }
                break;
            case R.id.confirm_order_select_me:
                MyLogs.e("haifeng","wo");
                for_me.setChecked(true);
                for_friend.setChecked(false);
                break;
            case R.id.confirm_order_select_friends:
                MyLogs.e("haifeng","她");
                startActivityForResult(new Intent(this,MyFriendsListActivity.class),301);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==301&&resultCode==31){
            nickName = data.getStringExtra("friendName");
            MyLogs.e("haifeng","好友选中"+nickName);
            if (!nickName.equals("")){
                confirm_order_friendsname.setText(nickName);
                for_me.setChecked(false);
                for_friend.setChecked(true);
            }else {
//                for_me.setChecked(true);
//                for_friend.setChecked(false);
            }

        }
    }
}
