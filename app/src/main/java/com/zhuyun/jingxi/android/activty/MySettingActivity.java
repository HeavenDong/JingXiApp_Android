package com.zhuyun.jingxi.android.activty;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

/**
 * Created by user0081 on 2016/7/1.  设置
 */
public class MySettingActivity extends BaseActivity{
    private TextView version_code;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入 设置");
        version_code= (TextView) findViewById(R.id.version_code);
        initData();
        setViewListener();
    }

    private void initData() {
        String pkName = App.getAppContext().getPackageName();
        String versionName=null;
        int versionCode=0;
        try {
            versionName =  App.getAppContext().getPackageManager().getPackageInfo(pkName,0).versionName;
            versionCode =  App.getAppContext().getPackageManager().getPackageInfo(pkName,0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        String versionCode= SharedPreUtils.get(App.getAppContext(),"versionCode","").toString();
        MyLogs.e("haifeng","versionName="+versionName+";  versionCode="+versionCode);
        version_code.setText("V"+versionName);
    }

    private void setViewListener() {
        findViewById(R.id.setting_back).setOnClickListener(this);
        findViewById(R.id.set_change_psw).setOnClickListener(this);
        findViewById(R.id.log_out).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
      setContentView(R.layout.layout_my_setting_activity);
    }

    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.setting_back:
              MySettingActivity.this.finish();
              overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
              break;
          case R.id.set_change_psw:
              startActivity(new Intent(MySettingActivity.this,ChangePSWActivity.class));
              overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;

          case R.id.log_out:
              final AlertDialog dialog=  new AlertDialog.Builder(MySettingActivity.this).create();
              dialog.show();
              dialog.setCancelable(true);
              Window window=dialog.getWindow();
              View dialogView=View .inflate(MySettingActivity.this, R.layout.layout_my_finish_dialog, null);
              window.setContentView(dialogView);

              dialogView.findViewById(R.id.finish_dialog_cancel).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      dialog.dismiss();
                  }
              });
              dialogView.findViewById(R.id.finish_dialog_sure).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      DemoHelper.getInstance().logout(true,new EMCallBack() {//里面的参数 true 表示退出登录时解绑 GCM 或者小米推送的 token

                          @Override
                          public void onSuccess() {
                              runOnUiThread(new Runnable() {
                                  public void run() {
                                      MyLogs.e("haifeng","注销环信");
                                      setResult(44);
                                      SharedPreUtils.clear(App.getAppContext());
                                      startActivity(new Intent(MySettingActivity.this,LoginActivity.class));
                                      dialog.dismiss();
                                      MySettingActivity.this.finish();

                                  }
                              });
                          }

                          @Override
                          public void onProgress(int progress, String status) {

                          }

                          @Override
                          public void onError(int code, String message) {
                              runOnUiThread(new Runnable() {

                                  @Override
                                  public void run() {
                                      dialog.dismiss();
                                      MyLogs.e("haifeng","注销环信失败");
                                      TempSingleToast.showToast(App.getAppContext(),"注销环信失败");
                                  }
                              });
                          }
                      });

                  }
              });
              break;
      }
    }
}
