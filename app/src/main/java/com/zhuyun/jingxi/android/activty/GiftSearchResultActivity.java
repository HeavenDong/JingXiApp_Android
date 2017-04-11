package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.GiftSearchResultGridAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.GiftBean;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.HeaderGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/17.  搜索结果
 */
public class GiftSearchResultActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private HeaderGridView search_result_grid;
    private SwipeRefreshLayout search_reset_swiprefresh;
    private List<GiftBean> lists= new ArrayList<GiftBean>();
    private GiftSearchResultGridAdapter adapter;
    private boolean isPositive;
    private String content,searchText;
    private long sceneId,holidayId,objectId,maxPrice,minPrice;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入搜索结果");
        searchText=getIntent().getExtras().getString("searchText");//接收到的话
        MyLogs.e("haifeng","传进来的话="+searchText);
        TempSingleToast.showToast(App.getAppContext(),"搜索条件： "+searchText);

        View  head = View.inflate(this, R.layout.layout_hot_head,null);
        search_result_grid= (HeaderGridView) findViewById(R.id.search_result_grid);
        search_reset_swiprefresh= (SwipeRefreshLayout) findViewById(R.id.search_reset_swiprefresh);
        search_result_grid.addHeaderView(head);
        initData();
        loadData();
        setViewListener();
    }

    private void initData() {
        isPositive=true;
        adapter = new GiftSearchResultGridAdapter(this, lists);
        search_result_grid.setOverScrollMode(View.OVER_SCROLL_NEVER);
        search_result_grid.setAdapter(adapter);
        search_result_grid.setSelector(R.color.transparency);//去点击状态原生背景色
    }
    private void loadData(){
        //请求网络
        if(CommanUtil.isNetworkAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyLogs.e("hanzhen","loadData  开子线程");
                    netData();
                }
            }).start();
        }else {
            TempSingleToast.showToast(App.getAppContext(),"你的网络不给力");
        }
    }

    private void netData() {
        String url =Url.SEARCH_RESULT+"?sceneId="+sceneId+"holidayId="+holidayId+"objectId="+objectId+
                "maxPrice="+maxPrice+"minPrice="+minPrice+"content="+content;
        MyLogs.e("haifeng","搜索结果"+url);
        HttpClient.requestGetAsyncHeader(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        TempSingleToast.showToast(App.getAppContext(),"服务连接失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.code()==200) {
                    final String json = response.body().string();
                    MyLogs.e("haifeng", "搜索结果，json=" + json);
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
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            final GiftBean bean = new GiftBean();
                                            JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                            bean.id=jsonObject2.optLong("id");
                                            CommanUtil.runOnUIThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    lists.add(bean);
                                                }
                                            });

                                        }
                                        MyLogs.e("haifeng","集合大小="+lists.size());
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });

                                    }else {
                                        MyLogs.e("haifeng","集合为null");
                                    }
                                }else {
                                    MyLogs.e("haifeng","errCode不对");
                                }
                            } catch (Exception e) {
                                MyLogs.e("haifeng","走了异常 :"+e);
                                e.printStackTrace();
                            }


                        }
                    });
                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");
                }
            }
        });
    }

    private void setViewListener() {
        search_reset_swiprefresh.setOnRefreshListener(this);
        findViewById(R.id.hot_back).setOnClickListener(this);
        findViewById(R.id.hot_price_sequence).setOnClickListener(this);
        findViewById(R.id.hot_go_giftsearch).setOnClickListener(this);

//        search_result_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(GiftSearchResultActivity.this, GiftDetailsActivity.class));
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
//        });
    }

    @Override
    protected void setContentLayout() {
       setContentView(R.layout.layout_search_result_activity);
    }

    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.hot_back:
              GiftSearchResultActivity.this.finish();
              break;
          case R.id.hot_price_sequence:
              if (isPositive){
                  isPositive=false;
                  TempSingleToast.showToast(App.getAppContext(), "正序刷新");
              }else {
                  isPositive=true;
                  TempSingleToast.showToast(App.getAppContext(), "倒序刷新");
              }
              break;
          case R.id.hot_go_giftsearch:
              startActivity(new Intent(GiftSearchResultActivity.this,GiftFreeSelectActivity.class));
              overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;
      }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MyLogs.e("haifeng", "下拉刷新完成");
                search_reset_swiprefresh.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        }, 500);
    }
}
