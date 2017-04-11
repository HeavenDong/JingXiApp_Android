package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.WishDiscussListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.WishDiscussBean;
import com.zhuyun.jingxi.android.utils.MyLogs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user0081 on 2016/7/1.  许愿评论
 */
public class WishDiscussActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ListView wish_discuss_listview;
    private SwipeRefreshLayout wish_discuss_swip_refresh;
    private List<WishDiscussBean> lists=new ArrayList<>();
    private WishDiscussListAdapter adapter;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入  许愿评论列表");
        findViewById(R.id.wish_discuss_back).setOnClickListener(this);
        wish_discuss_listview= (ListView) findViewById(R.id.wish_discuss_listview);
        wish_discuss_swip_refresh= (SwipeRefreshLayout)findViewById(R.id.wish_discuss_swip_refresh);
        initData();
    }

    private void initData() {
        /**加数据*/
        for (int i = 1; i < 10; i++) {
            WishDiscussBean bean=new WishDiscussBean();
            bean.name="Robot No."+i;
            bean.id=68;
            lists.add(bean);
        }

        adapter=new WishDiscussListAdapter(this,lists);
        wish_discuss_listview.setAdapter(adapter);
        wish_discuss_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=  new Intent(WishDiscussActivity.this,WishHomeActivity.class);
                Bundle bundle = new Bundle();
                /**假数据*/
                bundle.putInt("FriendsId",lists.get(position).id);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        wish_discuss_swip_refresh.setOnRefreshListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_wish_discuss_activity);

    }

    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.wish_discuss_back:
              WishDiscussActivity.this.finish();
              overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
              break;
      }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MyLogs.e("haifeng","下拉刷新完成");
                wish_discuss_swip_refresh.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        }, 500);

    }
}
