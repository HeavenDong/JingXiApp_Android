package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.HotGridAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.GiftBean;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/17.   分类后的商品列表
 */
public class HotActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private TextView gift_class_title;
    private HeaderGridView hot_grid;
    private SwipeRefreshLayout hot_swip_refresh;
    private List<GiftBean> lists= new ArrayList<GiftBean>();
    private HotGridAdapter adapter;
    private boolean isPositive;
    private long catId;
    private String title;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","分类后的商品列表");
        View head = View.inflate(this, R.layout.layout_hot_head,null);
        hot_grid= (HeaderGridView) findViewById(R.id.hot_grid);
        gift_class_title= (TextView) findViewById(R.id.gift_class_title);
        hot_swip_refresh= (SwipeRefreshLayout) findViewById(R.id.hot_swip_refresh);
        hot_grid.addHeaderView(head);
        initData();
        setViewListener();
        loadData();
    }

    private void initData() {
        catId=  getIntent().getExtras().getLong("catId");
        title=  getIntent().getExtras().getString("title");
        gift_class_title.setText(title);

        hot_swip_refresh.setOnRefreshListener(this);
        isPositive=true;
        adapter = new HotGridAdapter(this, lists);
        hot_grid.setOverScrollMode(View.OVER_SCROLL_NEVER);
        hot_grid.setAdapter(adapter);
        hot_grid.setSelector(R.color.transparency);//去点击状态原生背景色

    }

    private void setViewListener() {
        findViewById(R.id.hot_back).setOnClickListener(this);
        findViewById(R.id.hot_price_sequence).setOnClickListener(this);
        findViewById(R.id.hot_go_giftsearch).setOnClickListener(this);

        hot_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLogs.e("haifeng","分类后的商品  position=="+(position-2));
                Intent intent=  new Intent(HotActivity.this, GiftDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("giftId",lists.get(position-2).id);
                    bundle.putString("from","HotActivity");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    protected void setContentLayout() {
       setContentView(R.layout.layout_hot_activity);
    }

    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.hot_back:
              HotActivity.this.finish();
              break;
          case R.id.hot_price_sequence:
              if (isPositive){
                  isPositive=false;
                  Collections.sort(lists, unordercomp);
                  TempSingleToast.showToast(App.getAppContext(), "正序刷新");


              }else {
                  isPositive=true;
//                  Collections.sort(lists);
                  TempSingleToast.showToast(App.getAppContext(), "倒序刷新");
              }
              break;
          case R.id.hot_go_giftsearch:
              startActivity(new Intent(HotActivity.this,GiftFreeSelectActivity.class));
              overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;
      }
    }

    private void loadData() {
        String url= Url.GIFT_INFO_BY_CLASSIFY+"?catId="+catId;
        MyLogs.e("haifeng","分类查商品接口");
        HttpClient.requestGetAsyncHeader(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "分类查商品接口，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                lists.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                  JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    GiftBean bean=new GiftBean();
                                    bean.id=jsonObject2.optInt("id");
                                    bean.cId=jsonObject2.optInt("cId");
                                    bean.sort=jsonObject2.optInt("sort");
                                    bean.name=jsonObject2.optString("name");
                                    bean.imgIconUrl=jsonObject2.optString("imgIconUrl");
                                    bean.utpTime=jsonObject2.optLong("utpTime");
                                    bean.price=jsonObject2.optDouble("price");
                                    lists.add(bean);
                                }
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hot_swip_refresh.setRefreshing(false);
                                        adapter.notifyDataSetChanged();

                                    }
                                });
                            }
                        }else {
                            MyLogs.e("haifeng","errCode不对");
                        }
                    } catch (JSONException e) {
                        MyLogs.e("haifeng","走了异常 :"+e);
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");

                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }



    Comparator unordercomp = new Comparator() {
        public int compare(Object o1, Object o2) {
          MyLogs.e("haifeng","排序比较");
            GiftBean p1 = (GiftBean) o1;
            GiftBean p2 = (GiftBean) o2;
            if (p1.price < p2.price)
                return 1;
            else if (p1.price == p2.price)
                return 0;
            else if (p1.price > p2.price)
                return -1;
            return 0;
        }
    };

}
