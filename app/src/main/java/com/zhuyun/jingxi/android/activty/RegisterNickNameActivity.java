package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/7/1.
 */
public class RegisterNickNameActivity extends BaseActivity{
    private EditText nick_edit;
    @Override
    protected void initView() {
        nick_edit= (EditText) findViewById(R.id.nick_edit);
        findViewById(R.id.nick_next).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_edit_nickname_activity);
    }

    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.nick_next:
              if (!TextUtils.isEmpty(nick_edit.getText().toString())) {
                  Map<String,Object> params = new HashMap<String,Object>();
                  params.put("nickName",  nick_edit.getText().toString());
                  changUserInfo(params);

              }else {
                  TempSingleToast.showToast(App.getAppContext(),"昵称不能为空");
              }
              break;
      }
    }

    //修改个人信息接口
    private void changUserInfo(Map<String, Object> params) {

        MyLogs.e("haifeng","注册昵称 params="+params);
        //请求网络
        if(CommanUtil.isNetworkAvailable()) {
            try {
                HttpClient.requestPostAsyncHeader(Url.CHANG_USER_INFO, params, new Callback() {
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
                            MyLogs.e("haifeng","注册昵称 :json="+json);
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
                                                SharedPreUtils.put(App.getAppContext(), "nick_name", nick_edit.getText().toString());
                                                startActivityForResult(new Intent(RegisterNickNameActivity.this, RegisterSexActivity.class), 107);
                                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                            }
                                        });
                                    }else if (flag==0){
                                        MyLogs.e("haifeng", "修改失败");
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                TempSingleToast.showToast(App.getAppContext(),"修改失败");
                                            }
                                        });
                                    }



                                }else {
                                    MyLogs.e("haifeng", "修改失败 errorCode="+errCode);
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
        }else {
            TempSingleToast.showToast(App.getAppContext(),"你的网络不给力");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return  true;
        }
        return  super.onKeyDown(keyCode, event);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLogs.e("haifeng","EditNickNameActivity界面被销毁，执行ondestory");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==107&&resultCode==33){
            RegisterNickNameActivity.this.finish();
        }
    }
}
