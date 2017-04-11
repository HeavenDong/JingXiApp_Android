package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.RecordForSendGiftListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.RecordGiftBean;
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
 * Created by user0081 on 2016/7/1. 送礼记录
 */
public class RecordForSendGiftActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ListView record_sendgift_listview;
    private SwipeRefreshLayout giftsend_swip_refresh;
    private RecordForSendGiftListAdapter adapter;
    private List<RecordGiftBean> lists=new ArrayList<>();

    @Override
    protected void initView() {
        MyLogs.e("haifeng", "进入送礼记录");
        findViewById(R.id.record_sendgift_back).setOnClickListener(this);
        record_sendgift_listview = (ListView) findViewById(R.id.record_sendgift_listview);
        giftsend_swip_refresh = (SwipeRefreshLayout) findViewById(R.id.giftsend_swip_refresh);
        initData();
        loadData();
    }

    private void initData() {
        adapter = new RecordForSendGiftListAdapter(this,lists);
        record_sendgift_listview.setAdapter(adapter);

        record_sendgift_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=  new Intent(RecordForSendGiftActivity.this,DetailsForSendGift.class);
                Bundle bundle = new Bundle();
                bundle.putLong("itemId",lists.get(position).id);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        giftsend_swip_refresh.setOnRefreshListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_record_sendgift_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.record_sendgift_back:
                RecordForSendGiftActivity.this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }

    }

    private void loadData() {
        MyLogs.e("haifeng","送礼记录接口");
        HttpClient.requestGetAsyncHeader(Url.RECORD_SEND, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "送礼记录接口 :json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                lists.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final RecordGiftBean bean = new RecordGiftBean();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.id = jsonObject2.optLong("id");
                                    bean.utpTime = jsonObject2.optLong("utpTime");
                                    bean.recipientNickName = jsonObject2.optString("recipientNickName");
                                    bean.imgUrl = jsonObject2.optString("imgUrl");
                                    bean.giftsPrice = jsonObject2.optDouble("giftsPrice");
                                    bean.goodsName = jsonObject2.optString("goodsName");
                                    lists.add(bean);
                                }
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        giftsend_swip_refresh.setRefreshing(false);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }else {
                                MyLogs.e("haifeng","errCode="+errCode);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","response.code()！=200");
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}