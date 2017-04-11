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
import com.zhuyun.jingxi.android.adapter.FriendsListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.Friends;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/16.  好友
 */
public class FriendsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ListView friend_listview;
    private SwipeRefreshLayout friends_swip_refresh;
    private LinearLayout friend_kongtu,friend_nonet;
    private TextView reload;
    private FriendsListAdapter adapter;
    private List<Friends> lists=new ArrayList<>();
    private View head;
    private Thread thread;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入好友界面");
        friends_swip_refresh= (SwipeRefreshLayout) findViewById(R.id.friends_swip_refresh);
        //头
        head = View.inflate(this, R.layout.layout_friends_head,null);
        friend_listview= (ListView) findViewById(R.id.friend_listview);
        friend_kongtu= (LinearLayout) findViewById(R.id.friend_kongtu);
        friend_nonet= (LinearLayout) findViewById(R.id.friend_nonet);
        reload= (TextView) findViewById(R.id.reload);
        friend_listview.addHeaderView(head);
        initData();
        loadData();
        setViewListener();
    }

    private void initData() {
        adapter=new FriendsListAdapter(this,lists);
        friend_listview.setAdapter(adapter);
        friend_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index=position-1;
                MyLogs.e("haifeng","点击了 FriendsId="+lists.get(position-1).id);
                Intent intent=  new Intent(FriendsActivity.this,WishHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("FriendsId",lists.get(position-1).id);
                bundle.putInt("isFriend",1);
                intent.putExtras(bundle);
                startActivityForResult(intent,103);

            }
        });


    }

    private void setViewListener() {
        friends_swip_refresh.setOnRefreshListener(this);
        reload.setOnClickListener(this);
        findViewById(R.id.friends_back).setOnClickListener(this);
        findViewById(R.id.friend_add).setOnClickListener(this);
        head.findViewById(R.id.friend_news).setOnClickListener(this);
        head.findViewById(R.id.friend_search).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_friends_activity);
    }
    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.friends_back:
              FriendsActivity.this.finish();
              break;
          case R.id.friend_add:
              startActivity(new Intent(FriendsActivity.this,AddFriendsActivity.class));
              break;
          case R.id.friend_news://新的好友
              startActivityForResult(new Intent(FriendsActivity.this,NewFriendsActivity.class),102);
              break;
          case R.id.friend_search://好友  收索

              break;
          case R.id.reload: //断网刷新
              if(thread!=null) {
                  thread.interrupt();
                  thread = null;
              }
              loadData();
              break;
      }
    }

    private void loadData() {
        friend_kongtu.setVisibility(View.GONE);
        friend_nonet.setVisibility(View.GONE);
        if (CommanUtil.isNetworkAvailable()){
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
                friends_swip_refresh.setRefreshing(false);
                friend_nonet.setVisibility(View.VISIBLE);
                TempSingleToast.showToast(App.getAppContext(), "网络连接失败");
            }else {
                TempSingleToast.showToast(App.getAppContext(), "你的网络不给力");
                friends_swip_refresh.setRefreshing(false);
            }
            MyLogs.e("haifeng","断网");
        }

    }

    private void netData() {
        MyLogs.e("haifeng","走获取好友列表接口");
        HttpClient.requestGetAsyncHeader(Url.GET_FRIENDS_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lists.size()<=0){
                            friend_kongtu.setVisibility(View.VISIBLE);
                        }else {
                            friend_kongtu.setVisibility(View.GONE);
                        }
                        friends_swip_refresh.setRefreshing(false);
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
                                lists.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final Friends bean = new Friends();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.id = jsonObject2.optLong("id");
                                    bean.mobile = jsonObject2.optString("mobile");
                                    bean.nickName = jsonObject2.optString("nickName");
                                    bean.rName = jsonObject2.optString("rName");
                                    bean.portrait = jsonObject2.optString("portrait");
                                    bean.gender = jsonObject2.optInt("gender");
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
                                        friends_swip_refresh.setRefreshing(false);
                                        adapter.notifyDataSetChanged();
                                        if (lists.size()<=0){
                                            friend_kongtu.setVisibility(View.VISIBLE);
                                        }else {
                                            friend_kongtu.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                        }else {
                            MyLogs.e("haifeng","errCode不对");
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    friends_swip_refresh.setRefreshing(false);
                                    TempSingleToast.showToast(App.getAppContext(),"服务返回异常");
                                    if (lists.size()<=0){
                                        friend_kongtu.setVisibility(View.VISIBLE);
                                    }else {
                                        friend_kongtu.setVisibility(View.GONE);
                                    }

                                }
                            });
                        }
                    } catch (JSONException e) {
                        MyLogs.e("haifeng","走了异常 :"+e);
                        CommanUtil.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                friends_swip_refresh.setRefreshing(false);
                            }
                        });
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","response.code()！=200");
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            friends_swip_refresh.setRefreshing(false);
                            TempSingleToast.showToast(App.getAppContext(),"服务响应异常");
                            if (lists.size()<=0){
                                friend_kongtu.setVisibility(View.VISIBLE);
                            }else {
                                friend_kongtu.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //新的好友---接受
        if (requestCode==102&&resultCode==88){
            MyLogs.e("haifeng","新的好友---接受了---重新获取好友列表");
            if(thread!=null) {
                thread.interrupt();
                thread = null;
            }
            loadData();
        }
        //许愿主页----好友可能修改关系
        if (requestCode==103&&resultCode==77){
            MyLogs.e("haifeng","许愿主页----好友修改关系---重新获取好友列表");
            if(thread!=null) {
                thread.interrupt();
                thread = null;
            }
            loadData();
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
