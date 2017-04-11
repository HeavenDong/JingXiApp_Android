package com.zhuyun.jingxi.android.activty;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.WishListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.WishBean;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;

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
 * Created by user0081 on 2016/6/16.  许愿
 */
public class WishActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ListView wish_listview;
    private SwipeRefreshLayout wish_swip_refresh;
    private WishListAdapter adapter;
    private List<WishBean> lists=new ArrayList<WishBean>();
    private long FriendsId;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入许愿");
        findViewById(R.id.wish_back).setOnClickListener(this);
        wish_listview= (ListView) findViewById(R.id.wish_listview);
        wish_swip_refresh= (SwipeRefreshLayout)findViewById(R.id.wish_swip_refresh);
        MyLogs.e("haifeng","进入许愿2");
        initData();
        loadData();
    }

    private void initData() {
        FriendsId = getIntent().getExtras().getLong("FriendsId");
        MyLogs.e("haifeng","进入许愿3");
        adapter=new WishListAdapter(this,lists);
        wish_listview.setAdapter(adapter);
//        wish_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(WishActivity.this,DetailsForReceivedGift.class));
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
//        });

        wish_swip_refresh.setOnRefreshListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_wish_activity);
    }
    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.wish_back:
              WishActivity.this.finish();
              break;
      }

    }

    //查询许愿
    private void loadData() {
        String url;
        MyLogs.e("haifeng","id 1");
        if (FriendsId==(long) SharedPreUtils.get(App.getAppContext(), "user_id", (long)0)){
            url = Url.GET_MY_WISH;
            MyLogs.e("haifeng","我的许愿列表 ");
        }else {
           url= Url.GET_FRIENDS_WISH+"?fid="+FriendsId;
            MyLogs.e("haifeng","Ta的许愿列表 ");
        }
        MyLogs.e("haifeng","许愿列表 url="+url);
        HttpClient.requestGetAsyncHeader(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "许愿列表 :json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                lists.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final WishBean bean = new WishBean();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.id=jsonObject2.optLong("id");
                                    bean.wishUserId=jsonObject2.optLong("wishUserId");
                                    bean.nickName=jsonObject2.optString("nickName");
                                    bean.gender=jsonObject2.optInt("gender");
                                    bean.portraitUrl=jsonObject2.optString("portraitUrl");
                                    bean.imgUrl=jsonObject2.optString("imgUrl");
                                    bean.utpTime=jsonObject2.optLong("utpTime");
                                    bean.content=jsonObject2.optString("content");
                                    bean.goodsName=jsonObject2.optString("goodsName");
                                    bean.type=jsonObject2.optInt("type");
                                    bean.likeNum=jsonObject2.optInt("likeNum");
                                    bean.commNum=jsonObject2.optInt("commNum");
                                    lists.add(bean);
                                }
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wish_swip_refresh.setRefreshing(false);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }

                        }else {
                                MyLogs.e("haifeng","许愿列表errCode="+errCode);
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
