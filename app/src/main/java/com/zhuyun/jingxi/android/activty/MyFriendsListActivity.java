package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.MyFriendsGridAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.Friends;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.HeaderGridView;

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
 * Created by user0081 on 2016/7/8.  我的好友
 */
public class MyFriendsListActivity extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener{
    private HeaderGridView myfriends_grid;
    private MyFriendsGridAdapter adapter;
    private SwipeRefreshLayout myfriends_swip_refresh;
    private List<Friends> gridList;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入我的好友");
        View head = View.inflate(this, R.layout.layout_hot_head,null);
        myfriends_grid= (HeaderGridView) findViewById(R.id.myfriends_grid);
        myfriends_grid.addHeaderView(head);
        myfriends_swip_refresh= (SwipeRefreshLayout) findViewById(R.id.myfriends_swip_refresh);
        initData();
        loadData();
        setViewListener();

    }

    private void initData() {
        gridList= new ArrayList<Friends>();
        adapter=new MyFriendsGridAdapter(this,gridList);
        myfriends_grid.setOverScrollMode(View.OVER_SCROLL_NEVER);
        myfriends_grid.setAdapter(adapter);
        myfriends_grid.setSelector(R.color.transparency);//去点击状态原生背景色

    }

    private void setViewListener() {
        myfriends_swip_refresh.setOnRefreshListener(this);
        findViewById(R.id.myfriends_back).setOnClickListener(this);
        myfriends_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLogs.e("haifeng","点击"+position);
                Intent intent=new Intent();
                intent.putExtra("friendName",gridList.get(position-3).nickName);
                setResult(31,intent);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

    }

    @Override
    protected void setContentLayout() {
       setContentView(R.layout.layout_myfriendslist_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.myfriends_back:
                finish();
                break;
        }
    }
    private void loadData() {
        MyLogs.e("haifeng","走获取好友列表接口");
        HttpClient.requestGetAsyncHeader(Url.GET_FRIENDS_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        myfriends_swip_refresh.setRefreshing(false);
                        TempSingleToast.showToast(App.getAppContext(),"服务连接失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "好友列表，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                gridList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final Friends bean = new Friends();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.id = jsonObject2.optLong("id");
                                    bean.mobile = jsonObject2.optString("mobile");
                                    bean.nickName = jsonObject2.optString("nickName");
                                    bean.rName = jsonObject2.optString("rName");
                                    bean.portrait = jsonObject2.optString("portrait");
                                    bean.gender = jsonObject2.optInt("gender");
                                    MyLogs.e("haifeng", "好友列表,解析完成");
                                    gridList.add(bean);
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            myfriends_swip_refresh.setRefreshing(false);
                                            adapter.notifyDataSetChanged();
                                            MyLogs.e("haifeng", "好友列表,适配");
                                        }
                                    });
                                }
                            }
                        }else {
                            MyLogs.e("haifeng","errCode不对");
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    myfriends_swip_refresh.setRefreshing(false);
                                    TempSingleToast.showToast(App.getAppContext(),"服务返回异常");
                                }
                            });
                        }
                    } catch (JSONException e) {
                        MyLogs.e("haifeng","走了异常 :"+e);
                        CommanUtil.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                myfriends_swip_refresh.setRefreshing(false);
                            }
                        });
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","response.code()！=200");
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            myfriends_swip_refresh.setRefreshing(false);
                            TempSingleToast.showToast(App.getAppContext(),"服务响应异常");
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
