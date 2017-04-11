package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.MainActivity;
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
 * Created by user0081 on 2016/7/1.
 */
public class RegisterSexActivity extends BaseActivity{
    private String regist_mobile,rigist_password,nick_name;
    private long userId;
    @Override
    protected void initView() {
       findViewById(R.id.sex_man).setOnClickListener(this);
        findViewById(R.id.sex_woman).setOnClickListener(this);
        rigist_password=SharedPreUtils.get(App.getAppContext(), "user_password","未填写").toString();
        regist_mobile= SharedPreUtils.get(App.getAppContext(), "user_login_name", "未填写").toString();
        nick_name=SharedPreUtils.get(App.getAppContext(), "nick_name","未填写").toString();
        userId = (long) SharedPreUtils.get(App.getAppContext(), "user_id", (long)0);
    }

    @Override
    protected void setContentLayout() {
      setContentView(R.layout.layout_edit_sex_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        Map<String,Object> params = new HashMap<String,Object>();
      switch (v.getId()){
          case R.id.sex_man:
              params.put("gender", "1");
              changUserInfo(params,1);
              break;
          case R.id.sex_woman:
              params.put("gender", "0");
              changUserInfo(params,0);
              break;
      }
    }

    //修改个人信息接口
    private void changUserInfo(Map<String, Object> params, final int type ) {

        MyLogs.e("haifeng", "注册性别： params=" + params);
        //请求网络
        if (CommanUtil.isNetworkAvailable()) {
            try {
                HttpClient.requestPostAsyncHeader(Url.CHANG_USER_INFO, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        MyLogs.e("haifeng", "走onFailure");
                        CommanUtil.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                TempSingleToast.showToast(App.getAppContext(), "服务器连接失败");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200) {
                            String json = response.body().string();
                            MyLogs.e("haifeng", "注册性别 :json=" + json);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                final String errCode = jsonObject.optString("errCode");
                                if (errCode.equals("0")) {
                                    JSONObject jsonObject2 = jsonObject.optJSONObject("datas");
                                    int flag = jsonObject2.optInt("flag");
                                    if (flag == 1) {
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (type==1) {
                                                    SharedPreUtils.put(App.getAppContext(), "gender", 1);
                                                }else if (type==0) {
                                                    SharedPreUtils.put(App.getAppContext(), "gender", 0);
                                                }
                                                huanxinLogin(CommanUtil.md5Hex(userId+"",true)+userId, rigist_password);

                                            }
                                        });
                                    } else if (flag == 0) {
                                        MyLogs.e("haifeng", "修改失败");
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                TempSingleToast.showToast(App.getAppContext(), "修改失败");
                                            }
                                        });
                                    }


                                } else {
                                    MyLogs.e("haifeng", "修改失败 errorCode=" + errCode);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            MyLogs.e("haifeng", "response.code()！=200");

                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            TempSingleToast.showToast(App.getAppContext(), "你的网络不给力");
        }
    }


    private void huanxinLogin(final String currentUsername, String currentPassword) {
        MyLogs.e("haifeng","环信登录 ：currentUsername="+currentUsername+"currentPassword="+currentPassword);
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
                Log.d("haifeng", "设置参数1");
                EaseUser user = new EaseUser(currentUsername);
                user.setAvatar("http://www.99aijingxi.com:8080/aijingxiimage/wish/friend.png");
                user.setNick(nick_name);
                DemoHelper.getInstance().saveContact(user);
                Log.d("haifeng", "设置参数2");
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

                startActivity(new Intent(RegisterSexActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                setResult(33);
                RegisterSexActivity.this.finish();

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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLogs.e("haifeng","EditSexActivity界面被销毁，执行ondestory");
    }
}
