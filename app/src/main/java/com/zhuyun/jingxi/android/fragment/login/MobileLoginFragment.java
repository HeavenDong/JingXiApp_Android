package com.zhuyun.jingxi.android.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.RegisterNickNameActivity;
import com.zhuyun.jingxi.android.activty.ForgotPSWActivity;
import com.zhuyun.jingxi.android.MainActivity;
import com.zhuyun.jingxi.android.activty.RegisterActivity;
import com.zhuyun.jingxi.android.base.BaseFragment;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
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
 * Created by user0081 on 2016/6/21.  手机快速登录
 */
public class MobileLoginFragment extends BaseFragment {
    private EditText login_mobile,login_mobile_code;
    private TextView login_mobile_getmessage,login_mobile_butt;
    @Override
    protected void initView(View view, Bundle bundle) {
        login_mobile= (EditText) view.findViewById(R.id.login_mobile);
        login_mobile_code= (EditText) view.findViewById(R.id.login_mobile_code);

        login_mobile_getmessage= (TextView) view.findViewById(R.id.login_mobile_getmessage);
        login_mobile.addTextChangedListener(new EditChangedListener() );
        login_mobile_butt= (TextView) view.findViewById(R.id.login_mobile_butt);
        login_mobile_butt.setOnClickListener(this);
        login_mobile_getmessage.setOnClickListener(this);
        view.findViewById(R.id.toregist_mobile_butt).setOnClickListener(this);
        view.findViewById(R.id.login_mobile_forgot).setOnClickListener(this);
        view.findViewById(R.id.login_mobile_byweixin).setOnClickListener(this);
    }


    @Override
    protected int setLayout() {
        return R.layout.layout_mobile_login_fragment;
    }

    @Override
    protected void onClickEvent(View view) {
         switch (view.getId()){
             case R.id.login_mobile_butt:
                 startActivity(new Intent(context, MainActivity.class));
                 if(CommanUtil.isNetworkAvailable()) {
                     if (TextUtils.isEmpty(login_mobile.getText().toString())) {
                         TempSingleToast.showToast(App.getAppContext(),"忘记输入手机号");
                         return;
                     } else if (!CommanUtil.isMobilePhone(login_mobile.getText().toString())) {
                         TempSingleToast.showToast(App.getAppContext(),"手机号输入有误");
                         return;
                     } else if (TextUtils.isEmpty(login_mobile_code.getText().toString())) {
                         TempSingleToast.showToast(App.getAppContext(),"忘记输入验证码码");
                         return;
                     } else {
                         login_mobile_butt.setEnabled(false);
//                         NetLogin();
                     }
                 }else {
                     TempSingleToast.showToast(App.getAppContext(),"网络不可用");
                     login_mobile_butt.setEnabled(true);
                 }
                 break;
             case R.id.login_mobile_getmessage:
                 TempSingleToast.showToast(App.getAppContext(),"获取验证码");
                 break;
             case R.id.toregist_mobile_butt:
                 startActivityForResult(new Intent(context, RegisterActivity.class), 101);
                 break;
             case R.id.login_mobile_forgot:
                 startActivity(new Intent(context, ForgotPSWActivity.class));
                 break;

         }
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String currentText= login_mobile.getText().toString();
            if (CommanUtil.isMobilePhone(currentText)){
                login_mobile_getmessage.setEnabled(true);
                login_mobile_getmessage.setBackgroundResource(R.drawable.img_red_fillet_background2);
            }else {
                login_mobile_getmessage.setEnabled(false);
                login_mobile_getmessage.setBackgroundResource(R.drawable.img_gray_fillet_background3);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    private void NetLogin() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userLoginName", login_mobile.getText().toString());
        params.put("password", login_mobile_code.getText().toString());
        MyLogs.e("haifeng", "accoints登录:params=" + params);
        HttpClient.requestPostAsyncNoHeader(Url.LOGIN, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng", "走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        login_mobile_butt.setEnabled(true);
                        TempSingleToast.showToast(App.getAppContext(), "网络连接失败");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "登录:json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONObject jsonObject2 = jsonObject.optJSONObject("datas");


                        } else {
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    TempSingleToast.showToast(App.getAppContext(), "服务器返回数据问题");
                                    login_mobile_butt.setEnabled(true);
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            TempSingleToast.showToast(App.getAppContext(), "服务器连接失败");
                            login_mobile_butt.setEnabled(true);
                        }
                    });
                }

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册成功，关闭登录页，跳转首页
        if (requestCode==101&&resultCode==99){
            Intent intent = new Intent(context, RegisterNickNameActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
