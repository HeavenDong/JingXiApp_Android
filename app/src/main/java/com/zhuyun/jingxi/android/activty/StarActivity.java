package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.StarListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.StarBean;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/16.  达人
 */
public class StarActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ListView starListView;
    private SwipeRefreshLayout star_swip_refresh;
    private LinearLayout star_kongtu,star_nonet;
    private TextView star_reload;
    private List<StarBean> lists=new ArrayList<>();
    private StarListAdapter adapter;
    private Thread thread;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入达人");
        starListView= (ListView) findViewById(R.id.star_listview);
        star_swip_refresh= (SwipeRefreshLayout)findViewById(R.id.star_swip_refresh);
        star_kongtu= (LinearLayout) findViewById(R.id.star_kongtu);
        star_nonet= (LinearLayout) findViewById(R.id.star_nonet);
        star_reload= (TextView) findViewById(R.id.star_reload);
        initData();
        loadData();
        setViewListener();
    }

    private void setViewListener() {
        star_swip_refresh.setOnRefreshListener(this);
        star_reload.setOnClickListener(this);
        findViewById(R.id.star_back).setOnClickListener(this);
    }

    private void initData() {
        adapter=new StarListAdapter(this,lists);
        starListView.setAdapter(adapter);
        starListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=  new Intent(StarActivity.this,WishHomeActivity.class);
                Bundle bundle = new Bundle();
                /**假数据*/
                bundle.putLong("FriendsId",lists.get(position).id);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        star_swip_refresh.setOnRefreshListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_star_activity);
    }
    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.star_back:
              StarActivity.this.finish();
              overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
              break;
          case R.id.star_reload: //断网刷新
              if(thread!=null) {
                  thread.interrupt();
                  thread = null;
              }
              loadData();
              break;
      }

    }
    //点赞人列表
    private void loadData(){
        star_kongtu.setVisibility(View.GONE);
        star_nonet.setVisibility(View.GONE);
        //请求网络
        if(CommanUtil.isNetworkAvailable()) {
            if(thread==null){
                thread=  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyLogs.e("hanzhen","loadData  开子线程");
                        netData();
                    }
                });
                thread.start();
            }
        }else {
            if (lists.size()<=0) {
                star_swip_refresh.setRefreshing(false);
                star_nonet.setVisibility(View.VISIBLE);
                TempSingleToast.showToast(App.getAppContext(), "网络连接失败");
            }else {
                TempSingleToast.showToast(App.getAppContext(), "你的网络不给力");
                star_swip_refresh.setRefreshing(false);
            }
            MyLogs.e("haifeng","断网");
        }
    }

    private void netData() {
        MyLogs.e("haifeng","达人列表");
        HttpClient.requestGetAsyncHeader(Url.STAR_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lists.size()<=0){
                            star_kongtu.setVisibility(View.VISIBLE);
                        }else {
                            star_kongtu.setVisibility(View.GONE);
                        }
                        TempSingleToast.showToast(App.getAppContext(),"服务连接失败");
                        star_swip_refresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                MyLogs.e("haifeng", "达人列表接口，response.code()=" + response.code());
                if (response.code()==200) {
                    final String json = response.body().string();
                    MyLogs.e("haifeng", "达人列表，json=" + json);
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            MyLogs.e("haifeng","runOnUIThread");
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                final String errCode = jsonObject.optString("errCode");
                                if (errCode.equals("0")) {
                                    JSONArray jsonArray = jsonObject.optJSONArray("datas");
                                    if (jsonArray != null) {
                                        lists.clear();
                                        App.getDBManager().clearHomeList();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            final StarBean bean = new StarBean();
                                            JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                            bean.id=jsonObject2.optLong("id");
                                            bean.nickName=jsonObject2.optString("nickName");
                                            bean.portrait=jsonObject2.optString("portrait");
                                            bean.utpTime=jsonObject2.optLong("utpTime");
                                            bean.sendNumber=jsonObject2.optLong("sendNumber");
                                            bean.wishNumber=jsonObject2.optLong("wishNumber");
                                            bean.isApplayed=false;
                                            lists.add(bean);
                                        }
                                        MyLogs.e("haifeng","达人列表集合大小="+lists.size());
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                                star_swip_refresh.setRefreshing(false);
                                                if (lists.size()<=0){
                                                    star_kongtu.setVisibility(View.VISIBLE);
                                                }else {
                                                    star_kongtu.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                                    }else {
                                        MyLogs.e("haifeng","集合为null");
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                star_swip_refresh.setRefreshing(false);
                                                TempSingleToast.showToast(App.getAppContext(),"服务返回异常");
                                                if (lists.size()<=0){
                                                    star_kongtu.setVisibility(View.VISIBLE);
                                                }else {
                                                    star_kongtu.setVisibility(View.GONE);
                                                }

                                            }
                                        });
                                    }
                                }else {
                                    MyLogs.e("haifeng","errCode不对");
                                    star_swip_refresh.setRefreshing(false);
                                }
                            } catch (Exception e) {
                                MyLogs.e("haifeng","走了异常2 :"+e);
                                star_swip_refresh.setRefreshing(false);
//                                CommanUtil.runOnUIThread(new Runnable() {
//                                    @Override
//                                    public void run() {
                                        if (lists.size()<=0){
                                            star_kongtu.setVisibility(View.VISIBLE);
                                        }else {
                                            star_kongtu.setVisibility(View.GONE);
                                        }
//                                    }
//                                });
                                e.printStackTrace();
                            }


                        }
                    });
                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            TempSingleToast.showToast(App.getAppContext(),"服务连接失败");
                            star_swip_refresh.setRefreshing(false);
                            if (lists.size()<=0){
                                star_kongtu.setVisibility(View.VISIBLE);
                            }else {
                                star_kongtu.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

    }


    //申请添加好友
    public void requestAddFriends(long friendsId, final int position ) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("fId", friendsId);
        MyLogs.e("haifeng","申请加好友 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.APPLY_FRIEND, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MyLogs.e("haifeng","走onFailure");
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            TempSingleToast.showToast(App.getAppContext(),"连接服务失败");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        String json=response.body().string();
                        MyLogs.e("haifeng","申请加好友 :json="+json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            final String errCode = jsonObject.optString("errCode");
                            if (errCode.equals("0")) {
                                MyLogs.e("haifeng","申请了");
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lists.get(position).isApplayed=true;
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                            }else {
                                if (errCode.equals("61002")){
                                    MyLogs.e("haifeng","已申请状态");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            lists.get(position).isApplayed=true;
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }else {
                                    MyLogs.e("haifeng","申请errCode="+errCode);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                        MyLogs.e("haifeng","response.code()！=200");

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRefresh() {
        if(thread!=null) {
            thread.interrupt();
            thread = null;
        }
        loadData();
    }
}
