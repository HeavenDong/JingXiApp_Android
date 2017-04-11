package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

/**
 * Created by user0081 on 2016/6/30.  众筹
 */
public class CrowdFundingActivity extends BaseActivity{
    private TextView crowd_giftname,crowd_person_name,gift_price;
    private String nickName,price,giftName;
    private long wishId;
    private EditText crowd_price;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入 众筹");
        crowd_giftname=(TextView) findViewById(R.id.crowd_giftname);
        gift_price= (TextView) findViewById(R.id.gift_price);
        crowd_price= (EditText) findViewById(R.id.crowd_price);
        crowd_person_name= (TextView) findViewById(R.id.crowd_person_name);
        iniData();
        setViewListener();
    }

    private void iniData() {
        wishId=getIntent().getExtras().getLong("wishId");
        nickName=getIntent().getExtras().getString("nickName");//收礼人
        giftName=getIntent().getExtras().getString("giftName");//礼物名
        price=getIntent().getExtras().getString("price");//价格
        crowd_giftname.setText(giftName);
        gift_price.setText("￥"+price);
        crowd_person_name.setText(nickName);
    }

    private void setViewListener() {
        findViewById(R.id.crowdfunding_back).setOnClickListener(this);
        findViewById(R.id.crowd_pay).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
       setContentView(R.layout.layout_crowdfunding_activity);
    }

    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.crowdfunding_back:
              CrowdFundingActivity.this.finish();
              overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
              break;
          case R.id.crowd_pay:
             String payPrice= crowd_price.getText().toString();
              if (TextUtils.isEmpty(payPrice)) {
                  TempSingleToast.showToast(App.getAppContext(), "请输入众筹金额");
              }else if(!CommanUtil.isCount(payPrice)&&Integer.parseInt(payPrice)<=0){//Integer.parseInt(payPrice)<=0
                  TempSingleToast.showToast(App.getAppContext(), "输入金额有误");
              }else {
                  Intent intent = new Intent(CrowdFundingActivity.this, PayGiftActivity.class);
                  overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                  Bundle bundle = new Bundle();
                  bundle.putString("from", "众筹");
                  bundle.putString("person", crowd_person_name.getText().toString());
                  bundle.putString("price", crowd_price.getText().toString());
                  intent.putExtras(bundle);
                  startActivity(intent);
              }
              break;
      }
    }
}
