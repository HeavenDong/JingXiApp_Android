package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.ChestsListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.StarBean;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/16.  宝箱
 */
public class ChestsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ListView chests;
    private List<StarBean> lists=new ArrayList<>();
    private SwipeRefreshLayout chests_swip_refresh;
    private RelativeLayout chests_kongtu,chests_nonet;
    private TextView chests_reload;
    private ChestsListAdapter adapter;
    private Thread thread;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入进入宝箱");
        findViewById(R.id.chests_back).setOnClickListener(this);
        chests= (ListView) findViewById(R.id.chests_listview);
        chests_swip_refresh= (SwipeRefreshLayout)findViewById(R.id.chests_swip_refresh);
        chests_kongtu= (RelativeLayout) findViewById(R.id.chests_kongtu);
        chests_nonet= (RelativeLayout) findViewById(R.id.chests_nonet);
        chests_reload= (TextView) findViewById(R.id.chests_reload);
        initData();
        loadData();
    }


    private void initData() {

        adapter=new ChestsListAdapter(this,lists);
        chests.setAdapter(adapter);
        chests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ChestsActivity.this,DetailsForReceivedGift.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        chests_swip_refresh.setOnRefreshListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_chests_activity);
    }
    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.chests_back:
              ChestsActivity.this.finish();
              break;
      }

    }
    private void loadData() {
        chests_kongtu.setVisibility(View.GONE);
        chests_nonet.setVisibility(View.GONE);
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
                chests_swip_refresh.setRefreshing(false);
                chests_nonet.setVisibility(View.VISIBLE);
                TempSingleToast.showToast(App.getAppContext(), "网络连接失败");
            }else {
                TempSingleToast.showToast(App.getAppContext(), "你的网络不给力");
                chests_swip_refresh.setRefreshing(false);
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
                            chests_kongtu.setVisibility(View.VISIBLE);
                        }else {
                            chests_kongtu.setVisibility(View.GONE);
                        }
                        TempSingleToast.showToast(App.getAppContext(),"服务连接失败");
                        chests_swip_refresh.setRefreshing(false);
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
                                                chests_swip_refresh.setRefreshing(false);
                                                if (lists.size()<=0){
                                                    chests_kongtu.setVisibility(View.VISIBLE);
                                                }else {
                                                    chests_kongtu.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                                    }else {
                                        MyLogs.e("haifeng","集合为null");
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                chests_swip_refresh.setRefreshing(false);
                                                TempSingleToast.showToast(App.getAppContext(),"服务返回异常");
                                                if (lists.size()<=0){
                                                    chests_kongtu.setVisibility(View.VISIBLE);
                                                }else {
                                                    chests_kongtu.setVisibility(View.GONE);
                                                }

                                            }
                                        });
                                    }
                                }else {
                                    MyLogs.e("haifeng","errCode不对");
                                    chests_swip_refresh.setRefreshing(false);
                                }
                            } catch (Exception e) {
                                MyLogs.e("haifeng","走了异常2 :"+e);
                                chests_swip_refresh.setRefreshing(false);
//                                CommanUtil.runOnUIThread(new Runnable() {
//                                    @Override
//                                    public void run() {
                                if (lists.size()<=0){
                                    chests_kongtu.setVisibility(View.VISIBLE);
                                }else {
                                    chests_kongtu.setVisibility(View.GONE);
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
                            chests_swip_refresh.setRefreshing(false);
                            if (lists.size()<=0){
                                chests_kongtu.setVisibility(View.VISIBLE);
                            }else {
                                chests_kongtu.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

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
