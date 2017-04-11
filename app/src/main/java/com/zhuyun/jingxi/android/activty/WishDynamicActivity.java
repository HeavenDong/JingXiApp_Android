package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.WishDynamicListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.WishDynamicBean;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/7/1.  许愿动态
 */
public class WishDynamicActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ListView wishdynamic_listview;
    private SwipeRefreshLayout wishdynamic_swip_refresh;
    private List<WishDynamicBean> lists=new ArrayList<>();
    private WishDynamicListAdapter adapter;

    @Override
    protected void initView() {
        MyLogs.e("haifeng", "进入 许愿动态");
        findViewById(R.id.wishdynamic_back).setOnClickListener(this);
        wishdynamic_listview = (ListView) findViewById(R.id.wishdynamic_listview);
        wishdynamic_swip_refresh = (SwipeRefreshLayout) findViewById(R.id.wishdynamic_swip_refresh);
        initData();
        loadData();
    }

    private void initData() {
        adapter = new WishDynamicListAdapter(this,lists);
        wishdynamic_listview.setAdapter(adapter);

        wishdynamic_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLogs.e("haifeng", position+"点击查看许愿列表 id="+lists.get(position).fid);
                Intent intent=  new Intent(WishDynamicActivity.this,WishActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("FriendsId",lists.get(position).fid);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        wishdynamic_swip_refresh.setOnRefreshListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_wishdynamic_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.wishdynamic_back:
                WishDynamicActivity.this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }

    }

    private void loadData() {
        MyLogs.e("haifeng","许愿动态接口");
        HttpClient.requestGetAsyncHeader(Url.WISH_DYNAMIC, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        wishdynamic_swip_refresh.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "许愿动态接口，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                lists.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final WishDynamicBean bean = new WishDynamicBean();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.fid=jsonObject2.optLong("fid");
                                    bean.nickName=jsonObject2.optString("nickName");
                                    bean.imgIconUrl=jsonObject2.optString("imgIconUrl");
                                    bean.number=jsonObject2.optLong("number");
                                    bean.newWishTime=jsonObject2.optLong("newWishTime");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            lists.add(bean);
                                        }
                                    });
                                }
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wishdynamic_swip_refresh.setRefreshing(false);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }else {
                            MyLogs.e("haifeng","errCode不对");
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    wishdynamic_swip_refresh.setRefreshing(false);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        MyLogs.e("haifeng","走了异常 :"+e);
                        CommanUtil.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                wishdynamic_swip_refresh.setRefreshing(false);
                            }
                        });
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            wishdynamic_swip_refresh.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRefresh() {
         loadData();
    }
}