package com.zhuyun.jingxi.android.activty;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.ReceiverMessageListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.ReceiverMessage;
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
 * Created by user0081 on 2016/7/6.  收货信息
 */
public class ReceiverMessaageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ListView receive_msg_listview;
    private SwipeRefreshLayout receive_msg_swip_refresh;
    private List<ReceiverMessage> lists=new ArrayList<>();
    private ReceiverMessageListAdapter adapter;
    private AlertDialog dialog;
    private Thread thread;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入收货信息");
        receive_msg_listview= (ListView) findViewById(R.id.receive_msg_listview);
        receive_msg_swip_refresh= (SwipeRefreshLayout) findViewById(R.id.receive_msg_swip_refresh);
        initData();
        loadData();
        setViewListener();
    }

    private void initData() {
        adapter=new ReceiverMessageListAdapter(this,lists);
        receive_msg_listview.setAdapter(adapter);
    }

    private void setViewListener() {
        receive_msg_swip_refresh.setOnRefreshListener(this);
        findViewById(R.id.receive_msg_back).setOnClickListener(this);
        findViewById(R.id.add_receive_msg).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_receiver_msg_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.receive_msg_back:
                ReceiverMessaageActivity.this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.add_receive_msg:
                startActivityForResult(new Intent(ReceiverMessaageActivity.this, AddReceiverMessaageActivity.class),401);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    private void loadData(){
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
            TempSingleToast.showToast(App.getAppContext(),"你的网络不给力");
            receive_msg_swip_refresh.setRefreshing(false);
            MyLogs.e("haifeng","断网");
        }

    }

    private void netData() {
        MyLogs.e("haifeng","走收货信息列表接口");
        HttpClient.requestGetAsyncHeader(Url.RECEIVE_ADDRESS_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        TempSingleToast.showToast(App.getAppContext(),"服务连接失败");
                        receive_msg_swip_refresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                MyLogs.e("haifeng","response.code()="+response.code());
                if (response.code()==200) {
                    final String json = response.body().string();
                    MyLogs.e("haifeng", "收货信息列表接口，json=" + json);
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
                                            final ReceiverMessage bean=new ReceiverMessage();
                                            JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                            bean.id=jsonObject2.optLong("id");
                                            bean.name=jsonObject2.optString("name");
                                            bean.mobile=jsonObject2.optString("tel");
                                            bean.address=jsonObject2.optString("address");
                                            bean.country=jsonObject2.optLong("country");
                                            bean.province=jsonObject2.optLong("province");
                                            bean.city=jsonObject2.optLong("city");
                                            bean.area=jsonObject2.optLong("area");
                                            bean.isDefault=jsonObject2.optInt("isDefault");
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
                                                receive_msg_swip_refresh.setRefreshing(false);
                                            }
                                        });
                                    }else {
                                        MyLogs.e("haifeng","集合为null");
                                        receive_msg_swip_refresh.setRefreshing(false);
                                    }
                                }else {
                                    MyLogs.e("haifeng","errCode不对");
                                    receive_msg_swip_refresh.setRefreshing(false);
                                }
                            } catch (Exception e) {
                                MyLogs.e("haifeng","走了异常 :"+e);
                                receive_msg_swip_refresh.setRefreshing(false);
                                e.printStackTrace();
                            }


                        }
                    });
                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");
                    receive_msg_swip_refresh.setRefreshing(false);
                }
            }
        });
    }

    public void  deleteMessageDialog(final int position){

        dialog = new AlertDialog.Builder(ReceiverMessaageActivity.this).create();
        dialog.show();
        dialog.setCancelable(true);
        Window window=dialog.getWindow();
        View dialogView=View .inflate(ReceiverMessaageActivity.this, R.layout.layout_delete_receivemassege_dialog, null);
        window.setContentView(dialogView);

        dialogView.findViewById(R.id.address_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.address_dialog_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("id", lists.get(position).id);//	条目id
                MyLogs.e("haifeng","删除收货信息 id="+lists.get(position).id);
                deleteMessage(params,position);
            }
        });
    }
    private void deleteMessage(Map<String, Object> params, final int position) {
        MyLogs.e("haifeng","删除收货信息 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.DELETE_RECEIVE_ADDRESS, params, new Callback() {
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
                        MyLogs.e("haifeng","删除收货信息 :json="+json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            final String errCode = jsonObject.optString("errCode");
                            if (errCode.equals("0")) {
                                int datas = jsonObject.optInt("datas");
                                if (datas==1) {
                                    MyLogs.e("haifeng", "成功");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            lists.remove(position);
                                            adapter.notifyDataSetChanged();
                                            TempSingleToast.showToast(App.getAppContext(),"成功");
                                        }
                                    });
                                }else {
                                    MyLogs.e("haifeng", "失败");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TempSingleToast.showToast(App.getAppContext(),"失败");
                                            dialog.dismiss();
                                        }
                                    });
                                }

                            }else {
                                MyLogs.e("haifeng","errCode="+errCode);
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TempSingleToast.showToast(App.getAppContext(),"失败");
                                        dialog.dismiss();
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
                                TempSingleToast.showToast(App.getAppContext(),"失败");
                                dialog.dismiss();
                            }
                        });

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeDefaultAddress(Map<String, Object> params, final int position) {
        MyLogs.e("haifeng","修改默认地址 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.EDIT_DEFAULT_ADDRESS, params, new Callback() {
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
                        MyLogs.e("haifeng","修改默认地址 :json="+json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            final String errCode = jsonObject.optString("errCode");
                            if (errCode.equals("0")) {
                                int datas = jsonObject.optInt("datas");
                                if (datas==1) {
                                    MyLogs.e("haifeng", "成功");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            if (lists.get(position).isDefault==0) {//点击的是非默认
                                                for (int i = 0; i < lists.size(); i++) {
                                                    if (i != position) {
                                                        lists.get(i).isDefault = 0;
                                                    }
                                                }
                                                lists.get(position).isDefault=1;
//                                            }

                                            adapter.notifyDataSetChanged();
                                            TempSingleToast.showToast(App.getAppContext(),"成功");
                                        }
                                    });
                                }else {
                                    MyLogs.e("haifeng", "失败");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TempSingleToast.showToast(App.getAppContext(),"失败");
                                            dialog.dismiss();
                                        }
                                    });
                                }

                            }else {
                                MyLogs.e("haifeng","errCode="+errCode);
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TempSingleToast.showToast(App.getAppContext(),"失败");
                                        dialog.dismiss();
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
                                TempSingleToast.showToast(App.getAppContext(),"失败");
                                dialog.dismiss();
                            }
                        });

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==401&&resultCode==41){
            if(thread!=null) {
                thread.interrupt();
                thread = null;
            }
            loadData();
        }
        if (requestCode==402&&resultCode==42){
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
