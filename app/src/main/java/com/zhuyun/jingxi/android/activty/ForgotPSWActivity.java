package com.zhuyun.jingxi.android.activty;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
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
 * Created by user0081 on 2016/5/23.
 */
public class ForgotPSWActivity extends BaseActivity{
    private TextView forgot_getmessage,forgot_butt;
    private CheckBox forgot_seepw;
    private EditText forgot_mobile,forgot_password,forgot_message;
    int i = 90;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (i == 1) {
                        forgot_getmessage.setText("重新获取");
                        forgot_getmessage.setEnabled(true);
                        forgot_getmessage.setBackgroundResource(R.drawable.img_red_fillet_background2);
                        i = 90;
                        mHandler.removeMessages(0);
                    } else {
                        i--;
                        forgot_getmessage.setText(i + "s");
                        forgot_getmessage.setTextSize(11);
                        forgot_getmessage.setEnabled(false);
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                        forgot_getmessage.setBackgroundResource(R.drawable.img_gray_fillet_background3);
                    }

                    break;

                default:
                    break;
            }
        }
    };
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入忘记密码");
        forgot_getmessage= (TextView) findViewById(R.id.forgot_getmessage);
        forgot_mobile=(EditText)findViewById(R.id.forgot_mobile);
        forgot_password=(EditText)findViewById(R.id.forgot_password);
        forgot_message=(EditText)findViewById(R.id.forgot_message);
        forgot_seepw=(CheckBox)findViewById(R.id.forgot_seepw);
        forgot_butt= (TextView) findViewById(R.id.forgot_butt);
        initData();
        setViewListener();


    }

    private void initData() {
        // 设置颜色改变
        String str="请输入密码  请设置6—16位字符密码";
        SpannableStringBuilder builder=new SpannableStringBuilder(str);
        //字体大小
        builder.setSpan(new AbsoluteSizeSpan(30),str.length() - 12,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字体颜色
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.grey)), str.length() - 12,str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        forgot_password.setHint(builder);
    }

    private void setViewListener() {
        findViewById(R.id.forgot_back).setOnClickListener(this);
        findViewById(R.id.forgot_getmessage).setOnClickListener(this);
        forgot_butt.setOnClickListener(this);
        forgot_seepw.setOnClickListener(this);
        forgot_mobile.addTextChangedListener(new EditChangedListener() );
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_forgotpsw_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.forgot_back:
                finish();
                break;
            case R.id.forgot_getmessage:
                if(CommanUtil.isNetworkAvailable()) {
                    forgot_getmessage.setEnabled(false);
                    getMessageCode();
                }else {
                    TempSingleToast.showToast(App.getAppContext(),"网络不可用");
                    forgot_getmessage.setEnabled(true);
                }
                break;
            case R.id.forgot_butt:
                if(CommanUtil.isNetworkAvailable()) {
                    if (TextUtils.isEmpty(forgot_mobile.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(),"忘记输入手机号");
                        return;
                    } else if (!CommanUtil.isMobilePhone(forgot_mobile.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(),"手机号输入有误");
                        return;
                    } else if (TextUtils.isEmpty(forgot_message.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(),"忘记输入验证码");
                        return;
                    } else if (TextUtils.isEmpty(forgot_password.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(),"忘记输入密码");
                        return;
                    } else if (!CommanUtil.isPSW(forgot_password.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(),"密码输入有误");
                        return;
                    } else {
//                        forgot_butt.setEnabled(false);
                        NetForgot();
                    }
                }else {
                    MyLogs.e("haifeng","网络不可用");
                    TempSingleToast.showToast(App.getAppContext(),"网络不可用");
                    forgot_butt.setEnabled(true);
                }

                break;
            case R.id.forgot_seepw:
                if (forgot_seepw.isChecked()) {
                    //密码可见
                    forgot_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Editable etable = forgot_password.getText();
                    Selection.setSelection(etable, etable.length());
                }else {
                    //密码隐藏
                    forgot_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Editable etable = forgot_password.getText();
                    Selection.setSelection(etable, etable.length());
                }
                break;
        }
    }

    private void NetForgot() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userLoginName", forgot_mobile.getText().toString());
        params.put("password",  CommanUtil.md5Hex(forgot_password.getText().toString(),true));
        params.put("code", forgot_message.getText().toString());
        MyLogs.e("haifeng","忘记密码:params="+params);
        HttpClient.requestPostAsyncNoHeader(Url.FORGOT, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        TempSingleToast.showToast(App.getAppContext(),"网络连接失败");
                        forgot_butt.setEnabled(true);
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "忘记:json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            MyLogs.e("haifeng", "忘记:重置成功");
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    TempSingleToast.showToast(App.getAppContext(),"网络连接失败");
                                    forgot_butt.setEnabled(true);
                                }
                            });

                        }else {
                            MyLogs.e("haifeng","忘记:errCode对不上");
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    TempSingleToast.showToast(App.getAppContext()," XXX有误！");
                                    forgot_butt.setEnabled(true);
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
                            TempSingleToast.showToast(App.getAppContext(),"网络连接失败");
                            forgot_butt.setEnabled(true);
                        }
                    });
                }
            }
        });
    }

    private void getMessageCode() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userLoginName", forgot_mobile.getText().toString());
        params.put("type", 2);
        MyLogs.e("haifeng","params="+params);
        HttpClient.requestPostAsyncNoHeader(Url.SEND_CODE, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        TempSingleToast.showToast(App.getAppContext(),"网络连接失败");
                        forgot_getmessage.setEnabled(true);
                    }
                });

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200){
                    String json=response.body().string();
                    MyLogs.e("haifeng","json="+json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")){
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    mHandler.sendEmptyMessage(0);
                                    TempSingleToast.showToast(App.getAppContext(),"发送成功");
                                }
                            });

                        }else {
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    TempSingleToast.showToast(App.getAppContext(),"服务器返回数据问题");
                                    forgot_getmessage.setEnabled(true);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        MyLogs.e("haifeng","走了异常");
                    }


                }else {
                    MyLogs.e("haifeng","response.code()！=200");
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            TempSingleToast.showToast(App.getAppContext(),"服务器连接失败");
                            forgot_getmessage.setEnabled(true);
                        }
                    });
                }

            }
        });
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String currentText= forgot_mobile.getText().toString();
            if (CommanUtil.isMobilePhone(currentText)){
                forgot_getmessage.setEnabled(true);
                forgot_getmessage.setBackgroundResource(R.drawable.img_red_fillet_background2);
            }else {
                forgot_getmessage.setEnabled(false);
                forgot_getmessage.setBackgroundResource(R.drawable.img_gray_fillet_background3);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
