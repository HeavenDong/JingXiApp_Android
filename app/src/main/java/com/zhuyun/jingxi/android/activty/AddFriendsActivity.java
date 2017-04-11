package com.zhuyun.jingxi.android.activty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.AddFriendsListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.RecommendFriends;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
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
 * Created by user0081 on 2016/6/24. 添加好友
 */
public class AddFriendsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ListView addfriend_listview;
    private List<RecommendFriends>  lists=new ArrayList<>();
    private View head;
    private AddFriendsListAdapter adapter;
    private SwipeRefreshLayout addfriend_swip_refresh;
    private ProgressDialog progressDialog;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入添加好友");
        addfriend_swip_refresh= (SwipeRefreshLayout) findViewById(R.id.addfriend_swip_refresh);
        //头
        head = View.inflate(this, R.layout.layout_addfriend_head,null);
        addfriend_listview= (ListView) findViewById(R.id.addfriend_listview);
        addfriend_listview.addHeaderView(head);
        initData();
        loadData();
        setViewListener();
    }

    private void setViewListener() {
        addfriend_swip_refresh.setOnRefreshListener(this);
        findViewById(R.id.add_friends_back).setOnClickListener(this);
        head.findViewById(R.id.addfriend_phone).setOnClickListener(this);
        head.findViewById(R.id.addfriend_search).setOnClickListener(this);
    }

    private void initData() {
        adapter=new AddFriendsListAdapter(this,lists);
        addfriend_listview.setAdapter(adapter);
        addfriend_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    int index = position - 1;
                    MyLogs.e("haifeng", "点击了 FriendsId=" + lists.get(position - 1).id);
                    Intent intent = new Intent(AddFriendsActivity.this, WishHomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("FriendsId", lists.get(position - 1).id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void setContentLayout() {
      setContentView(R.layout.layout_addfriend_activity);
    }

    @Override
    protected void onClickEvent(View v) {
       switch (v.getId()){
           case R.id.add_friends_back:
               AddFriendsActivity.this.finish();
               break;
           case R.id.addfriend_phone:
               MyLogs.e("haifeng","点击手机联系人");
               Intent intent = new Intent(AddFriendsActivity.this, MobilePhoneActivity.class);
               Bundle bundle = new Bundle();
               bundle.putString("from","AddFriendsActivity");
               intent.putExtras(bundle);
               startActivity(intent);
               overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
               break;
           case R.id.addfriend_search:
               break;
       }
    }
    //获取推荐好友列表
    private void loadData() {
        HttpClient.requestGetAsyncHeader(Url.GET_RECOM_FRIENDS_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng","推荐好友列表 :json="+json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                lists.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final RecommendFriends bean = new RecommendFriends();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.id=jsonObject2.optLong("id");
                                    bean.mobile = jsonObject2.optString("mobile");
                                    bean.nickName = jsonObject2.optString("nickName");
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
                                        addfriend_swip_refresh.setRefreshing(false);
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
                            TempSingleToast.showToast(App.getAppContext(),"服务器连接失败");
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
                                    JSONObject  jsonObject2 = jsonObject.optJSONObject("datas");
                                    int flag = jsonObject2.optInt("flag");
                                    if (flag==1) {
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                addContact(position);
                                            }
                                        });
                                    }else {
                                        MyLogs.e("haifeng", "申请失败");
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                lists.get(position).isApplayed = false;
                                            }
                                        });
                                    }

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

    // TODO: 2016/8/2  申请加好友 
    private void addContact(final int position) {
        MyLogs.e("haifeng", "环信平台，申请好友");
        final String mobile=lists.get(position).mobile;
        if(EMClient.getInstance().getCurrentUser().equals(mobile)){
            new EaseAlertDialog(this,"不能添加自己").show();
            return;
        }

        if(DemoHelper.getInstance().getContactList().containsKey(mobile)){
            //let the user know the contact already in your contact list
            if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(mobile)){
                new EaseAlertDialog(this, "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可").show();
                return;
            }
            new EaseAlertDialog(this, "此用户已是你的好友").show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在发送请求...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    EMClient.getInstance().contactManager().addContact(mobile, "对方请求添加你为好友");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            lists.get(position).isApplayed = true;
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                            MyLogs.e("haifeng", "申请成功");
                            TempSingleToast.showToast(App.getAppContext(),"发送请求成功,等待对方验证");
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            MyLogs.e("haifeng", "申请失败");
                           TempSingleToast.showToast(App.getAppContext(),"请求添加好友失败:"+e);

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
