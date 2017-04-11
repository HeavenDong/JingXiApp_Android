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
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.huanxin.db.DemoDBManager;
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
 * Created by user0081 on 2016/5/23.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private TextView regist_getmessage,regist_butt;
    private CheckBox regist_seepw;
    private EditText regist_mobile,rigist_password,regist_message;
    private long user_id;
    private Thread thread;
    int i = 90;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (i == 1) {
                        regist_getmessage.setText("重新获取");
                        regist_getmessage.setEnabled(true);
                        regist_getmessage.setBackgroundResource(R.drawable.img_red_fillet_background2);
                        i = 90;
                        mHandler.removeMessages(0);
                    } else {
                        i--;
                        regist_getmessage.setText(i + "s");
                        regist_getmessage.setTextSize(11);
                        regist_getmessage.setEnabled(false);
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                        regist_getmessage.setBackgroundResource(R.drawable.img_gray_fillet_background3);
                    }

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入快速注册");
        regist_getmessage= (TextView) findViewById(R.id.regist_getmessage);
        regist_mobile=(EditText)findViewById(R.id.regist_mobile);
        rigist_password=(EditText)findViewById(R.id.rigist_password);
        regist_message=(EditText)findViewById(R.id.regist_message);
        regist_seepw=(CheckBox)findViewById(R.id.regist_seepw);
        regist_butt= (TextView) findViewById(R.id.regist_butt);
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
        rigist_password.setHint(builder);
    }

    private void setViewListener() {
        findViewById(R.id.regist_back).setOnClickListener(this);
        findViewById(R.id.regist_getmessage).setOnClickListener(this);
        findViewById(R.id.regist_butt).setOnClickListener(this);
        findViewById(R.id.protocol).setOnClickListener(this);
        regist_seepw.setOnClickListener(this);
        regist_mobile.addTextChangedListener(new EditChangedListener() );

    }
    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_register);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.regist_back:
                finish();
                break;
            case R.id.regist_getmessage:
                if(CommanUtil.isNetworkAvailable()) {
                    regist_getmessage.setEnabled(false);
                    getMessageCode();
                }else {
                    TempSingleToast.showToast(App.getAppContext(),"网络不可用");
                    regist_getmessage.setEnabled(true);
                }
                break;
            case R.id.regist_butt:
//                huanxinRegister(regist_mobile.getText().toString().trim(), rigist_password.getText().toString().trim());

              if(CommanUtil.isNetworkAvailable()) {
                    if (TextUtils.isEmpty(regist_mobile.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(),"忘记输入手机号");
                        return;
                    } else if (!CommanUtil.isMobilePhone(regist_mobile.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(),"手机号输入有误");
                        return;
                    } else if (TextUtils.isEmpty(regist_message.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(),"忘记输入验证码");
                        return;
                    } else if (TextUtils.isEmpty(rigist_password.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(),"忘记输入密码");
                        return;
                    } else if (!CommanUtil.isPSW(rigist_password.getText().toString())) {
                        TempSingleToast.showToast(App.getAppContext(),"密码输入有误");
                        return;
                    } else {
                        regist_butt.setEnabled(false);
//                        //请求网络
                            if(thread==null){
                                thread=  new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyLogs.e("hanzhen","loadData  开子线程");
                                        NetRegister();
                                    }
                                });
                                thread.start();
                            }
                    }
                }else {
                    TempSingleToast.showToast(App.getAppContext(),"网络不可用");
                    regist_butt.setEnabled(true);
                }
                break;
            case R.id.protocol:
                TempSingleToast.showToast(App.getAppContext(),"查看协议");
                break;
            case R.id.regist_seepw:
                if (regist_seepw.isChecked()) {
                    //密码可见
                    rigist_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Editable etable = rigist_password.getText();
                    Selection.setSelection(etable, etable.length());
                }else {
                    //密码隐藏
                    rigist_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Editable etable = rigist_password.getText();
                    Selection.setSelection(etable, etable.length());
                }
                break;

        }
    }


    //获取短息验证码
    public void getMessageCode() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userLoginName", regist_mobile.getText().toString());
        params.put("type",1);
        MyLogs.e("haifeng","注册短信 params="+params);
        HttpClient.requestPostAsyncNoHeader(Url.SEND_CODE, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        TempSingleToast.showToast(App.getAppContext(),"连接服务器失败");
                        regist_getmessage.setEnabled(true);
                    }
                });

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200){
                    String json=response.body().string();
                    MyLogs.e("haifeng","注册短信，json="+json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")){

                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    TempSingleToast.showToast(App.getAppContext(),"发送成功");
                                     mHandler.sendEmptyMessage(0);
                                }
                            });

                        }else {
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    TempSingleToast.showToast(App.getAppContext(),"服务器返回数据问题");
                                    regist_getmessage.setEnabled(true);
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
                            regist_getmessage.setEnabled(true);
                        }
                    });
                }

            }
        });
    }
    //注册
    private void NetRegister() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userLoginName", regist_mobile.getText().toString());
        params.put("password", CommanUtil.md5Hex(rigist_password.getText().toString(),true));
        params.put("code", regist_message.getText().toString());
        MyLogs.e("haifeng","注册:params="+params);
        HttpClient.requestPostAsyncNoHeader(Url.REGIST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        TempSingleToast.showToast(App.getAppContext(),"网络连接失败");
                        regist_butt.setEnabled(true);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200){
                    String json=response.body().string();
                    MyLogs.e("haifeng","注册:json="+json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")){
                            login();
                        }else {
                              if(errCode.equals("21001")){
                                  MyLogs.e("haifeng",":errCode="+errCode);
                                  CommanUtil.runOnUIThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          TempSingleToast.showToast(App.getAppContext(),"验证码无效");
                                          regist_butt.setEnabled(true);
                                      }
                                  });
                              }else{
                                  MyLogs.e("haifeng",":errCode="+errCode);
                                  CommanUtil.runOnUIThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          TempSingleToast.showToast(App.getAppContext(),"服务返回异常");
                                          regist_butt.setEnabled(true);
                                      }
                                  });
                              }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        MyLogs.e("haifeng","走了异常");
                    }


                }else {
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            TempSingleToast.showToast(App.getAppContext(),"服务器连接失败");
                            regist_butt.setEnabled(true);
                        }
                    });
                    MyLogs.e("haifeng","response.code()！=200");

                }

            }
        });

    }
    //注册后立即登录
    private void login() {
        new Thread(new Runnable() {
            public void run() {
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("userLoginName", regist_mobile.getText().toString());
                params.put("password", CommanUtil.md5Hex(rigist_password.getText().toString(),true));
                MyLogs.e("haifeng","登录:params="+params);
                HttpClient.requestPostAsyncNoHeader(Url.LOGIN, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        MyLogs.e("haifeng","走onFailure");
                        CommanUtil.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                TempSingleToast.showToast(App.getAppContext(),"网络连接失败");
                                regist_butt.setEnabled(true);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code()==200){
                            String json=response.body().string();
                            MyLogs.e("haifeng","登录:json="+json);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                String errCode = jsonObject.optString("errCode");
                                if (errCode.equals("0")){
                                    JSONObject  jsonObject2 = jsonObject.optJSONObject("datas");
                                    //本地存储
                                    user_id= jsonObject2.optLong("id");
                                    SharedPreUtils.put(App.getAppContext(), "user_id", user_id);
                                    SharedPreUtils.put(App.getAppContext(), "user_password",CommanUtil.md5Hex(rigist_password.getText().toString(),true) );
                                    SharedPreUtils.put(App.getAppContext(), "user_login_name", jsonObject2.optString("userLoginName"));
                                    SharedPreUtils.put(App.getAppContext(), "user_name", jsonObject2.optString("userName"));
                                    SharedPreUtils.put(App.getAppContext(), "nick_name", jsonObject2.optString("nickName"));
                                    SharedPreUtils.put(App.getAppContext(), "mobile", jsonObject2.optString("mobile"));
                                    SharedPreUtils.put(App.getAppContext(), "gender", jsonObject2.optString("gender"));
                                    SharedPreUtils.put(App.getAppContext(), "mediumHeadImgPath", jsonObject2.optString("mediumHeadImgPath"));
                                    SharedPreUtils.put(App.getAppContext(), "smallHeadImgPath", jsonObject2.optString("smallHeadImgPath"));
                                    SharedPreUtils.put(App.getAppContext(), "remark", jsonObject2.optString("remark"));
                                    SharedPreUtils.put(App.getAppContext(), "birthday", jsonObject2.optLong("birthday"));
                                    SharedPreUtils.put(App.getAppContext(), "address", jsonObject2.optString("address"));
                                    SharedPreUtils.put(App.getAppContext(), "integral", jsonObject2.optInt("integral"));
                                    SharedPreUtils.put(App.getAppContext(), "invitationCode", jsonObject2.optString("invitationCode"));
                                    SharedPreUtils.put(App.getAppContext(), "dnaStatus", jsonObject2.optInt("dnaStatus"));
                                    SharedPreUtils.put(App.getAppContext(), "status", jsonObject2.optString("status"));
                                    SharedPreUtils.put(App.getAppContext(), "wishNum", jsonObject2.optInt("wishNum"));
                                    SharedPreUtils.put(App.getAppContext(), "provId", jsonObject2.optInt("provId"));
                                    SharedPreUtils.put(App.getAppContext(), "cityId", jsonObject2.optInt("cityId"));
                                    SharedPreUtils.put(App.getAppContext(), "areaId", jsonObject2.optInt("areaId"));
                                    SharedPreUtils.put(App.getAppContext(), "constellation", jsonObject2.optInt("constellation"));

                                    /**环信注册*/
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            huanxinRegister(CommanUtil.md5Hex(user_id+"",true)+user_id, CommanUtil.md5Hex(rigist_password.getText().toString(),true));
                                        }
                                    });
                                }else {
                                    MyLogs.e("haifeng",":errCode="+errCode);
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TempSingleToast.showToast(App.getAppContext(),"服务器连接失败");
                                            regist_butt.setEnabled(true);
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
                                    regist_butt.setEnabled(true);
                                }
                            });
                        }

                    }
                });
            }
        }).start();
    }

    private void huanxinRegister(final String username, final String pwd) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(username, pwd);
                    MyLogs.e("haifeng","环信注册");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                             // save current user
                             DemoHelper.getInstance().setCurrentUserName(username);

                            setResult(99);
                            RegisterActivity.this.finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


//                            huanxinLogin(username, rigist_password.getText().toString().trim());
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            int errorCode=e.getErrorCode();
                            if(errorCode== EMError.NETWORK_ERROR){
//                                TempSingleToast.showToast(App.getAppContext(),"网络异常，请检查网络！");
                                MyLogs.e("haifeng","网络异常，请检查网络！");
                            }else if(errorCode == EMError.USER_ALREADY_EXIST){
//                                TempSingleToast.showToast(App.getAppContext(),"用户已存在！");
                                MyLogs.e("haifeng","用户已存在！");
                            }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
//                                TempSingleToast.showToast(App.getAppContext(),"注册失败，无权限！");
                                MyLogs.e("haifeng","注册失败，无权限！");
                            }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
//                                TempSingleToast.showToast(App.getAppContext(),"用户名不合法");
                                MyLogs.e("haifeng","用户名不合法");
                            }else{
//                                TempSingleToast.showToast(App.getAppContext(),"注册失败");
                                MyLogs.e("haifeng","注册失败");
                            }


                            setResult(99);
                            RegisterActivity.this.finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    });
                }
            }
        }).start();
    }

    private void huanxinLogin(final String currentUsername, String currentPassword) {
        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();
        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);
        MyLogs.e("haifeng","setCurrentUserName="+currentUsername);
        final long start = System.currentTimeMillis();
        // call login method
        Log.d("haifeng", "EMClient.getInstance().login");
        EMClient.getInstance().login(currentUsername,currentPassword , new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.e("haifeng", "IMlogin: onSuccess");
//                Log.d("haifeng", "设置参数1");
//                EaseUser user = new EaseUser(currentUsername);
//                user.setAvatar("http://www.99aijingxi.com:8080/aijingxiimage/wish/friend.png");
//                user.setNick("大风1");
//                DemoHelper.getInstance().saveContact(user);
//                Log.d("haifeng", "设置参数2");

                // 注册群组和联系人监听
                DemoHelper.getInstance().registerGroupAndContactListener();
                //  第一次登录或者之前logout后再登录，加载所有本地群和回话
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        App.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                setResult(99);
                RegisterActivity.this.finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        setResult(99);
//                        RegisterActivity.this.finish();
//                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                    }
//                });

            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e("haifeng", "IMlogin: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.e("haifeng", "IMlogin: onError: " + code);

            }
        });
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String currentText= regist_mobile.getText().toString();
            if (CommanUtil.isMobilePhone(currentText)){
                regist_getmessage.setEnabled(true);
                regist_getmessage.setBackgroundResource(R.drawable.img_red_fillet_background2);
            }else {
                regist_getmessage.setEnabled(false);
                regist_getmessage.setBackgroundResource(R.drawable.img_gray_fillet_background3);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler!=null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler=null;
        }
        if(thread!=null) {
            thread.interrupt();
            thread = null;
        }
    }
}
