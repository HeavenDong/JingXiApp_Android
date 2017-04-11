package com.zhuyun.jingxi.android.fragment.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.MainActivity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.ForgotPSWActivity;
import com.zhuyun.jingxi.android.activty.RegisterActivity;
import com.zhuyun.jingxi.android.activty.RegisterNickNameActivity;
import com.zhuyun.jingxi.android.base.BaseFragment;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.huanxin.db.DemoDBManager;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.wxapi.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/21.   账户登录
 */
public class AccountsLoginFragment extends BaseFragment {
    private EditText login_accounts_mobile,login_accounts_psw;
    private TextView login_accounts_butt;
    private CheckBox login_seepw;
    private boolean progressShow;
    private long user_id;
    private ProgressDialog pd;
    @Override
    protected void initView(View view, Bundle bundle) {
        login_accounts_mobile= (EditText) view.findViewById(R.id.login_accounts_mobile);
        login_accounts_psw= (EditText) view.findViewById(R.id.login_accounts_psw);
        login_seepw=(CheckBox)view.findViewById(R.id.login_seepw);
        login_accounts_butt= (TextView) view.findViewById(R.id.login_accounts_butt);
        login_accounts_butt.setOnClickListener(this);
        login_seepw.setOnClickListener(this);
        view.findViewById(R.id.login_accounts_toregist).setOnClickListener(this);
        view.findViewById(R.id.login_accounts_forgot).setOnClickListener(this);
        view.findViewById(R.id.login_accounts_byweixin).setOnClickListener(this);
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_accounts_login_fragment;
    }

    @Override
    protected void onClickEvent(View view) {
       switch (view.getId()){
           case R.id.login_accounts_butt:
               if(CommanUtil.isNetworkAvailable()) {
                   if (TextUtils.isEmpty(login_accounts_mobile.getText().toString().trim())) {
                       TempSingleToast.showToast(App.getAppContext(),"忘记输入手机号");
                       return;
                   } else if (!CommanUtil.isMobilePhone(login_accounts_mobile.getText().toString().trim())) {
                       TempSingleToast.showToast(App.getAppContext(),"手机号输入有误");
                       return;
                   } else if (TextUtils.isEmpty(login_accounts_psw.getText().toString().trim())) {
                       TempSingleToast.showToast(App.getAppContext(),"忘记输入密码");
                       return;
                   } else if (!CommanUtil.isPSW(login_accounts_psw.getText().toString().trim())) {
                       TempSingleToast.showToast(App.getAppContext(),"密码输入有误");
                       return;
                   } else {
                       progressShow = true;

                       pd = new ProgressDialog(context);
                       pd.setCanceledOnTouchOutside(false);
                       pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                           @Override
                           public void onCancel(DialogInterface dialog) {
                               Log.d("haifeng", "EMClient.getInstance().onCancel");
                               progressShow = false;
                           }
                       });
                       pd.setMessage("正在登录...");
                       pd.show();


                       login_accounts_butt.setEnabled(false);
                       NetLogin();
                   }
               }else {
                   TempSingleToast.showToast(App.getAppContext(),"网络不可用");
                   login_accounts_butt.setEnabled(true);
               }
               break;
           case R.id.login_seepw:
               if (login_seepw.isChecked()) {
                   //密码可见
                   login_accounts_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                   Editable etable = login_accounts_psw.getText();
                   Selection.setSelection(etable, etable.length());
               }else {
                   //密码隐藏
                   login_accounts_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                   Editable etable = login_accounts_psw.getText();
                   Selection.setSelection(etable, etable.length());
               }
               break;
           case R.id.login_accounts_toregist:
               startActivityForResult(new Intent(context, RegisterActivity.class), 100);
               break;
           case R.id.login_accounts_forgot:
               startActivity( new Intent(context, ForgotPSWActivity.class));
               break;
           case R.id.login_accounts_byweixin:
               if (!CommanUtil.isWXAppInstalledAndSupported()) {
                   TempSingleToast.showToast(App.getAppContext(),"请先安装微信");
                   return;
               }
               //移动应用微信授权登录
               SendAuth.Req req= new SendAuth.Req();
               req.scope="snsapi_userinfo";//应用授权作用域，如获取用户个人信息则填写snsapi_userinfo。（什么是授权域？）
               req.state="wechat_sdk_微信登录";//用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击，建议第三方带上该参数，可设置为简单的随机数加session进行校验。
               WXAPIFactory.createWXAPI(App.getAppContext(), Constants.APP_ID, false).sendReq(req);
               MyLogs.e("haifeng","微信登录");
               break;
       }
    }

    private void NetLogin() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userLoginName", login_accounts_mobile.getText().toString());
        params.put("password",  CommanUtil.md5Hex(login_accounts_psw.getText().toString(),true));
        MyLogs.e("haifeng","accoints登录:params="+params);
        HttpClient.requestPostAsyncNoHeader(Url.LOGIN, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!progressShow) {
                            return;
                        }
                        pd.dismiss();

                        login_accounts_butt.setEnabled(true);
                        TempSingleToast.showToast(App.getAppContext(),"连接服务失败");
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
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")){
                            JSONObject  jsonObject2 = jsonObject.optJSONObject("datas");
                            //本地存储
                            user_id= jsonObject2.optLong("id");
                            SharedPreUtils.put(App.getAppContext(), "user_id", user_id);
                            SharedPreUtils.put(App.getAppContext(), "user_password",CommanUtil.md5Hex(login_accounts_psw.getText().toString(),true) );
                            SharedPreUtils.put(App.getAppContext(), "user_login_name", jsonObject2.optString("userLoginName"));
                            SharedPreUtils.put(App.getAppContext(), "user_name", jsonObject2.optString("userName"));
                            SharedPreUtils.put(App.getAppContext(), "nick_name", jsonObject2.optString("nickName"));
                            SharedPreUtils.put(App.getAppContext(), "mobile", jsonObject2.optString("mobile"));
                            SharedPreUtils.put(App.getAppContext(), "gender", jsonObject2.optInt("gender"));
                            SharedPreUtils.put(App.getAppContext(), "mediumHeadImgPath", jsonObject2.optString("mediumHeadImgPath"));
                            SharedPreUtils.put(App.getAppContext(), "smallHeadImgPath", jsonObject2.optString("smallHeadImgPath"));
                            SharedPreUtils.put(App.getAppContext(), "remark", jsonObject2.optString("remark"));
                            SharedPreUtils.put(App.getAppContext(), "birthday", jsonObject2.optLong("birthday"));
                            SharedPreUtils.put(App.getAppContext(), "address", jsonObject2.optString("address"));
                            SharedPreUtils.put(App.getAppContext(), "point", jsonObject2.optInt("point"));
                            SharedPreUtils.put(App.getAppContext(), "invitationCode", jsonObject2.optString("invitationCode"));
                            SharedPreUtils.put(App.getAppContext(), "dnaStatus", jsonObject2.optInt("dnaStatus"));
                            SharedPreUtils.put(App.getAppContext(), "status", jsonObject2.optString("status"));
                            SharedPreUtils.put(App.getAppContext(), "wishNum", jsonObject2.optInt("wishNum"));
                            SharedPreUtils.put(App.getAppContext(), "provId", jsonObject2.optInt("provId"));
                            SharedPreUtils.put(App.getAppContext(), "cityId", jsonObject2.optInt("cityId"));
                            SharedPreUtils.put(App.getAppContext(), "areaId", jsonObject2.optInt("areaId"));
                            SharedPreUtils.put(App.getAppContext(), "constellation", jsonObject2.optInt("constellation"));

                            huanxinRegist(CommanUtil.md5Hex(user_id+"",true)+user_id,CommanUtil.md5Hex(login_accounts_psw.getText().toString(),true));
                        }else {
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!progressShow) {
                                        return;
                                    }
                                     pd.dismiss();

                                    if (errCode.equals("11007")){
                                        TempSingleToast.showToast(App.getAppContext(),"账号或密码输入有误");
                                        MyLogs.e("haifeng","输入有误");
                                        login_accounts_butt.setEnabled(true);
                                    }else {
                                        TempSingleToast.showToast(App.getAppContext(),"服务器返回数据问题");
                                        login_accounts_butt.setEnabled(true);
                                    }

                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!progressShow) {
                                return;
                            }
                            pd.dismiss();
                            TempSingleToast.showToast(App.getAppContext(),"服务器连接失败");
                            login_accounts_butt.setEnabled(true);
                        }
                    });
                }

            }
        });
    }

    private void huanxinRegist(final String currentUsername, final String currentPassword) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(currentUsername, currentPassword);
                    MyLogs.e("haifeng","环信注册");
                    CommanUtil.runOnUIThread(new Runnable() {
                        public void run() {

                                // save current user
                                DemoHelper.getInstance().setCurrentUserName(currentUsername);
                                huanxinLogin(currentUsername, currentPassword);
                        }
                    });
                } catch (final HyphenateException e) {
                    CommanUtil.runOnUIThread(new Runnable() {
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

                            huanxinLogin(currentUsername, currentPassword);
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

        final long start = System.currentTimeMillis();
        // call login method
        Log.d("haifeng", "EMClient.getInstance().login");
        EMClient.getInstance().login(currentUsername,currentPassword , new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.e("haifeng", "IMlogin: onSuccess");
                // 保存用户名
//                DemoHelper.getInstance().setCurrentUserName(CommanUtil.md5Hex(user_id+"",true)+user_id);
                EaseUser user = new EaseUser(currentUsername);
                user.setAvatar("http://www.99aijingxi.com:8080/aijingxiimage/wish/friend.png");
                user.setNick(SharedPreUtils.get(App.getAppContext(), "nick_name","未填写").toString());
                DemoHelper.getInstance().saveContact(user);

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

                //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                context.startActivity(new Intent(context, MainActivity.class));
                getActivity().finish();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d("haifeng", "IMlogin: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d("haifeng", "IMlogin: onError: " + code);
                Log.d("haifeng", "IMlogin: message: " + message);
                if (!progressShow) {
                    return;
                }
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        TempSingleToast.showToast(App.getAppContext(),"账号或密码输入有误");
                        MyLogs.e("haifeng","本地接口走通，环信登录错误");
                        login_accounts_butt.setEnabled(true);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //注册成功，关闭登录页，跳转首页
        if (requestCode==100&&resultCode==99){
            startActivity(new Intent(context, RegisterNickNameActivity.class));
            getActivity().finish();
        }
    }
}
