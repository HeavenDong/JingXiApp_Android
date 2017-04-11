package com.zhuyun.jingxi.android.activty;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
 * Created by user0081 on 2016/7/21.
 */
public class ChangePSWActivity extends BaseActivity {
    private TextView change_psw_old,change_psw_new,change_psw_renew,change_psw_butt;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入修改密码");
        change_psw_old= (TextView) findViewById(R.id.change_psw_old);
        change_psw_new= (TextView) findViewById(R.id.change_psw_new);
        change_psw_renew= (TextView) findViewById(R.id.change_psw_renew);
        change_psw_butt= (TextView) findViewById(R.id.change_psw_butt);
        change_psw_butt.setOnClickListener(this);
        findViewById(R.id.change_psw_back).setOnClickListener(this);

    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_changepsw_activity);
    }

    @Override
    protected void onClickEvent(View v) {
            switch (v.getId()) {
                case R.id.change_psw_back:
                    ChangePSWActivity.this.finish();
                    break;
                case R.id.change_psw_butt:
                if (CommanUtil.isNetworkAvailable()) {
                    if (TextUtils.isEmpty(change_psw_old.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(), "忘记输入旧密码");
                        return;
                    } else if (!CommanUtil.isPSW(change_psw_old.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(), "旧密码输入有误");
                        return;

                    } else if (TextUtils.isEmpty(change_psw_new.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(), "忘记输入新密码");
                        return;
                    } else if (!CommanUtil.isPSW(change_psw_new.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(), "新密码输入有误");
                        return;
                    } else if (TextUtils.isEmpty(change_psw_renew.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(), "请确认密码");
                        return;
                    } else if (!change_psw_new.getText().toString().equals(change_psw_renew.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(), "两次密码不一致");
                        return;
                    } else {
//                    change_psw_butt.setEnabled(false);
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("password", CommanUtil.md5Hex(change_psw_old.getText().toString(), true));
                        params.put("newPassword", CommanUtil.md5Hex(change_psw_new.getText().toString(), true));
                        changePSW(params);
                    }
                } else {
                    TempSingleToast.showToast(App.getAppContext(), "网络不可用");
                    change_psw_butt.setEnabled(true);
                }
               break;
            }
    }

    private void changePSW(Map<String, Object> params) {

        MyLogs.e("haifeng","修改密码 params="+params);
        //请求网络
            try {
                HttpClient.requestPostAsyncHeader(Url.CHANGE_PSW, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        MyLogs.e("haifeng","走onFailure");
                        CommanUtil.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                TempSingleToast.showToast(App.getAppContext(),"服务器连接失败");
                                change_psw_butt.setEnabled(true);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            String json=response.body().string();

                            MyLogs.e("haifeng","修改密码 :json="+json);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                final String errCode = jsonObject.optString("errCode");
                                if (errCode.equals("0")) {
                                String datas =  jsonObject.optString("datas");
                                    if (datas.equals("1")) {
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                TempSingleToast.showToast(App.getAppContext(),"修改成功");
                                                SharedPreUtils.put(App.getAppContext(), "user_password",CommanUtil.md5Hex(change_psw_new.getText().toString(),true) );
                                                finish();
                                            }
                                        });
                                    }else {
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
    }

}
