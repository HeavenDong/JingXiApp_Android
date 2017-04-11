package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.NewFriendsListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.NewFriends;
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
 * Created by user0081 on 2016/6/24.  新的好友
 */
public class NewFriendsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ListView newfriends_listview;
    private List<NewFriends> lists=new ArrayList<>();
    private NewFriendsListAdapter adapter;
    private View head;
    private SwipeRefreshLayout newfriend_swip_refresh;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入新的好友");
        newfriend_swip_refresh= (SwipeRefreshLayout) findViewById(R.id.newfriend_swip_refresh);
        head = View.inflate(this, R.layout.layout_newfriend_head,null);
        newfriends_listview= (ListView) findViewById(R.id.newfriends_listview);
        newfriends_listview.addHeaderView(head);
        initData();
        loadData();
        setViewListener();
    }

    private void initData() {
        adapter= new NewFriendsListAdapter(this,lists);
        newfriends_listview.setAdapter(adapter);
        newfriends_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    int index = position - 1;
                    MyLogs.e("haifeng", "点击了 FriendsId=" + lists.get(position - 1).id);
                    Intent intent = new Intent(NewFriendsActivity.this, WishHomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("FriendsId", lists.get(position - 1).id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void setViewListener() {
        newfriend_swip_refresh.setOnRefreshListener(this);
        findViewById(R.id.new_friends_back).setOnClickListener(this);
        head.findViewById(R.id.newfriend_phone).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
      setContentView(R.layout.layout_newfriends_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.new_friends_back:
                NewFriendsActivity.this.finish();
                break;
            case R.id.newfriend_phone:
                Intent intent = new Intent(NewFriendsActivity.this, MobilePhoneActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from","NewFriendsActivity");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }
    //获取好友申请列表
    private void loadData() {
        HttpClient.requestGetAsyncHeader(Url.GET_APPLY_FRIENDS_LIST, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "好友申请列表，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            lists.clear();
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final NewFriends bean=new NewFriends();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.fAId=jsonObject2.getInt("fAId");
                                    bean.id=jsonObject2.getInt("id");
                                    bean.mobile=jsonObject2.getString("mobile");
                                    bean.portrait=jsonObject2.getString("portrait");
                                    bean.nickName=jsonObject2.getString("nickName");
                                    bean.gender=jsonObject2.getInt("gender");
                                    bean.msg=jsonObject2.getString("msg");
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
                                        newfriend_swip_refresh.setRefreshing(false);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }else {
                            MyLogs.e("haifeng","errCode不对");
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

    //6.好友申请答复
    public void responseAddFriends(int fAId, final int position ) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id", fAId);//条目id
        params.put("flag", 1);//（1同意，2拒绝）反馈
        MyLogs.e("haifeng","申请加好友 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.REPLY_FRIEND, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MyLogs.e("haifeng","走onFailure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        String json=response.body().string();
                        MyLogs.e("haifeng","好友申请答复 :json="+json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            final String errCode = jsonObject.optString("errCode");
                            if (errCode.equals("0")) {
                                MyLogs.e("haifeng","接受了");
                                setResult(88);
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        ReceiverFriends(position);
                                    }
                                });

                            }else {
                                MyLogs.e("haifeng","接受errCode="+errCode);
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TempSingleToast.showToast(App.getAppContext(),"服务返回异常");

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                        MyLogs.e("haifeng","response.code()！=200");
                        CommanUtil.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                TempSingleToast.showToast(App.getAppContext(),"连接服务失败");

                            }
                        });

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ReceiverFriends(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(lists.get(position).mobile);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            lists.get(position).Receivered=true;
                            adapter.notifyDataSetChanged();
                            MyLogs.e("haifeng", "环信 已同意");
                        }
                    });

                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            MyLogs.e("haifeng", "huanxin接受失败");
                            TempSingleToast.showToast(App.getAppContext(),"同意添加好友失败:"+e);

                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onRefresh() {
       loadData();
    }
}
