package com.zhuyun.jingxi.android.activty;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.WonderfulUndefinedListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

/**
 * Created by user0081 on 2016/7/1.  精彩活动
 */
public class WonderfulUndefinedActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ListView wonderful_listview;
    private SwipeRefreshLayout wonderful_swip_refresh;
    private WonderfulUndefinedListAdapter adapter;

    @Override
    protected void initView() {
        MyLogs.e("haifeng", "进入 精彩活动");
        findViewById(R.id.wonderful_back).setOnClickListener(this);
        wonderful_listview = (ListView) findViewById(R.id.wonderful_listview);
        wonderful_swip_refresh = (SwipeRefreshLayout) findViewById(R.id.wonderful_swip_refresh);
        initData();
    }

    private void initData() {
        adapter = new WonderfulUndefinedListAdapter(this);
        wonderful_listview.setAdapter(adapter);

        wonderful_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TempSingleToast.showToast(App.getAppContext(),"查看活动详情");
            }
        });

        wonderful_swip_refresh.setOnRefreshListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_wonderfulundefined_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.wonderful_back:
                WonderfulUndefinedActivity.this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }

    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            public void run() {
                MyLogs.e("haifeng", "下拉刷新完成");
                wonderful_swip_refresh.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        }, 500);


    }
}
