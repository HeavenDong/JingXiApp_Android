package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.BirthdayGridAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.BirthdayBean;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/18.  生日提醒
 */
public class BirthdayReminderActivity extends BaseActivity{
    private GridView birthday_grid;
    private  BirthdayGridAdapter adapter;
    private List<BirthdayBean> birthdayList=new ArrayList();
    private HashMap<Integer,Boolean> isSelectTy = new HashMap();
    @Override
    protected void initView() {
        birthday_grid= (GridView) findViewById(R.id.birthday_grid);
        findViewById(R.id.birthday_sendgift).setOnClickListener(this);
        findViewById(R.id.birthday_back).setOnClickListener(this);
        initData();
        loadData();
    }


    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_birthdayremind_activity);
    }

    @Override
    protected void onClickEvent(View v) {
       switch (v.getId()){
           case R.id.birthday_back:
               finish();
               break;
           case R.id.birthday_sendgift:
               int pos = getkey(isSelectTy,true);
               if (pos>=0) {
                   MyLogs.e("haifeng", "给" + birthdayList.get(pos).nickName + "送上礼物");
                   TempSingleToast.showToast(App.getAppContext(), "给" + birthdayList.get(pos).nickName + "送上礼物");
                   SharedPreUtils.put(App.getAppContext(), "birthday_gift_fId", birthdayList.get(pos).fId);
                   Intent intent = new Intent();
                   intent.setAction("transactionMainFragment");
                   intent.putExtra("index", 1);
                   sendBroadcast(intent);
                   BirthdayReminderActivity.this.finish();
               }else{
                   TempSingleToast.showToast(App.getAppContext(), "没有送礼对象");
               }

               break;
       }
    }

    private void initData() {
        adapter = new BirthdayGridAdapter(this,birthdayList,isSelectTy);
        birthday_grid.setAdapter(adapter);
    }

    private void loadData() {
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
            MyLogs.e("haifeng","断网");
        }
    }

    private void netData() {
        MyLogs.e("haifeng","走生日提醒接口");
        HttpClient.requestGetAsyncHeader(Url.BIRTHDAY_REMIND, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    final String json = response.body().string();
                    MyLogs.e("haifeng", "生日提醒接口，json=" + json);
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                birthdayList.clear();
                                App.getDBManager().clearGiftClassifyList();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final BirthdayBean bean = new BirthdayBean();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.fId=jsonObject2.optLong("fId");
                                    bean.rName=jsonObject2.optString("rName");
                                    bean.nickName=jsonObject2.optString("nickName");
                                    bean.birthday=jsonObject2.optLong("birthday");
                                    bean.portrait=jsonObject2.optString("portrait");
                                    isSelectTy.put(i, false);
                                    birthdayList.add(bean);
                                }
                               adapter.notifyDataSetChanged();
                            }
                        }else {
                            MyLogs.e("haifeng","errCode不对");
                        }
                    } catch (JSONException e) {
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

    private int getkey(HashMap<Integer,Boolean> map,Boolean value)
    {
        Iterator<Integer> it= map.keySet().iterator();
        while(it.hasNext())
        {
            Integer keyString=it.next();
            if(map.get(keyString).equals(value))
                return keyString;
        }
        return -1;
    }
}
