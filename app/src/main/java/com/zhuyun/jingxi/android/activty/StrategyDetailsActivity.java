package com.zhuyun.jingxi.android.activty;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.StrategyDetailsListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.StrategyDetailsBean;
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
 * Created by user0081 on 2016/6/27.
 *   攻略详情
 */
public class StrategyDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ListView strategydetails_listview;
    private SwipeRefreshLayout strategy_details_swip_refresh;
    private List<StrategyDetailsBean> lists=new ArrayList();
    private StrategyDetailsListAdapter adapter;
    private TextView strategy_details_head_des;
    private ImageView strategy_details_head_img;
    private long sId;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入攻略详情");
        View head = View.inflate(this, R.layout.layout_strategydetails_head,null);
        strategy_details_head_img= (ImageView) head.findViewById(R.id.strategy_details_head_img);
        strategy_details_head_des= (TextView) head.findViewById(R.id.strategy_details_head_des);
        strategydetails_listview= (ListView) findViewById(R.id.strategydetails_listview);
        strategy_details_swip_refresh= (SwipeRefreshLayout)findViewById(R.id.strategy_details_swip_refresh);
        strategydetails_listview.addHeaderView(head);
        initData();
        loadData();
        setViewListener();
    }

    private void initData() {
        sId=getIntent().getExtras().getLong("sId");
       adapter= new StrategyDetailsListAdapter(this,lists);
        strategydetails_listview.setAdapter(adapter);
//        strategydetails_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }

    private void setViewListener() {
        strategy_details_swip_refresh.setOnRefreshListener(this);
        findViewById(R.id.strategydetails_back).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
       setContentView(R.layout.layout_strategydetails_activity);
    }

    @Override
    protected void onClickEvent(View v) {
       switch (v.getId()){
           case R.id.strategydetails_back:
               StrategyDetailsActivity.this.finish();
               overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
               break;
       }
    }

    private void loadData() {
        String url= Url.GIFT_STRATEGY_DETAILS_INFO+"?sId="+sId;
        MyLogs.e("haifeng","走攻略详情接口"+url);
        HttpClient.requestGetAsyncHeader(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "攻略详情接口，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray2 = jsonObject.optJSONArray("datas");
                            JSONObject jsonObject2 = jsonArray2.optJSONObject(0);
                                     int id= jsonObject2.optInt("id");
                                     String name=jsonObject2.optString("name");
                                     int wishCount=jsonObject2.optInt("wishCount");
                                     final String des=jsonObject2.optString("des");
                                    JSONArray jsonArray = jsonObject2.getJSONArray("goodsList");
                                    if (jsonArray != null) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            final StrategyDetailsBean bean = new StrategyDetailsBean();
                                            JSONObject jsonObject3 = jsonArray.optJSONObject(i);
                                            bean.id = jsonObject3.optInt("id");
                                            bean.name = jsonObject3.optString("name");
                                            bean.imgIconUrl=jsonObject3.optString("imgIconUrl");
                                            bean.utpTime=jsonObject3.optLong("utpTime");
                                            bean.wishCount=jsonObject3.optInt("wishCount");
                                            bean.price=jsonObject3.optDouble("price");
                                            bean.des=jsonObject3.optString("des");
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
//                                                Picasso.with(App.getAppContext()).load("http://sta.273.com.cn/app/mbs/img/mobile_default.png").into(strategy_details_head_img);
                                                strategy_details_head_des.setText(des);
                                                strategy_details_swip_refresh.setRefreshing(false);
                                                adapter.notifyDataSetChanged();

                                            }
                                        });
                            }
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
}
