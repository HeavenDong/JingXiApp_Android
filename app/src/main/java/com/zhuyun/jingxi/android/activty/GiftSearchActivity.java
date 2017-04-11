package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.utils.MyLogs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user0081 on 2016/7/8.  搜索
 */
public class GiftSearchActivity extends BaseActivity{
    private TextView search_first_tv,search_second_tv,search_three_tv,search_four_tv,search_five_tv,search_six_tv,search_seven_tv,search_eight_tv;
    private List<String> lists=new ArrayList<>();
    List<String> lists0=new ArrayList<>();
    List<String> lists1=new ArrayList<>();
    List<String> lists2=new ArrayList<>();
    private int index=0;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入搜索");
        search_first_tv= (TextView) findViewById(R.id.search_first_tv);//小标题
        search_second_tv= (TextView) findViewById(R.id.search_second_tv);
        search_three_tv= (TextView) findViewById(R.id.search_three_tv);
        search_four_tv= (TextView) findViewById(R.id.search_four_tv);
        search_five_tv= (TextView) findViewById(R.id.search_five_tv);
        search_six_tv= (TextView) findViewById(R.id.search_six_tv);
        search_seven_tv= (TextView) findViewById(R.id.search_seven_tv);
        search_eight_tv= (TextView) findViewById(R.id.search_eight_tv);

        initData();
        initShow();
        setViewListener();
    }

    private void initData() {
        lists0.add("对象");
        for (int i = 1; i < 8; i++) {
            lists0.add("送"+i+"号");
        }
        lists1.add("节日");
        for (int i = 1; i < 8; i++) {
            lists1.add(i+"号节日");
        }
        lists2.add("场景");
        for (int i = 1; i < 8; i++) {
            lists2.add( i + "号博物馆");
        }
        lists.addAll(lists0);
    }

    private void initShow() {
        if (index>2){
            index=0;
        }
        search_first_tv.setText(lists.get(0));
        search_second_tv.setText(lists.get(1));
        search_three_tv.setText(lists.get(2));
        search_four_tv.setText(lists.get(3));
        search_five_tv.setText(lists.get(4));
        search_six_tv.setText(lists.get(5));
        search_seven_tv.setText(lists.get(6));
        search_eight_tv.setText(lists.get(7));
    }

    private void setViewListener() {
        search_first_tv.setOnClickListener(this);
        search_second_tv.setOnClickListener(this);
        search_three_tv.setOnClickListener(this);
        search_four_tv.setOnClickListener(this);
        search_five_tv.setOnClickListener(this);
        search_six_tv.setOnClickListener(this);
        search_seven_tv.setOnClickListener(this);
        search_eight_tv.setOnClickListener(this);
        findViewById(R.id.search_cancle).setOnClickListener(this);
        findViewById(R.id.search_updata).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_gift_search_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        Intent intent=  new Intent(GiftSearchActivity.this,GiftSearchResultActivity.class);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.search_cancle://返回
                finish();
                break;
            case R.id.search_updata://更新
                if (index==0) {
                    lists.clear();
                    lists.addAll(lists1);

                }else if (index==1){
                    lists.clear();
                    lists.addAll(lists2);

                }else if (index==2){
                    lists.clear();
                    lists.addAll(lists0);
                }
                index++;
                initShow();
                break;
            case R.id.search_second_tv:
                bundle.putString("searchText",lists.get(1));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.search_three_tv:
                bundle.putString("searchText",lists.get(2));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.search_four_tv:
                bundle.putString("searchText",lists.get(3));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.search_five_tv:
                bundle.putString("searchText",lists.get(4));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.search_six_tv:
                bundle.putString("searchText",lists.get(5));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.search_seven_tv:
                bundle.putString("searchText",lists.get(6));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.search_eight_tv:
                bundle.putString("searchText",lists.get(7));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;

        }
    }
}
