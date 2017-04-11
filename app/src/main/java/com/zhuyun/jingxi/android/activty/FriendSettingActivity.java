package com.zhuyun.jingxi.android.activty;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.view.AddBlackListPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/28. 好友设置
 */
public class FriendSettingActivity extends BaseActivity{
    private long FriendsId;
    private String FriendsName;
    private TextView set_friend_name;
    private AddBlackListPopupWindow popupWindow;
    private AlertDialog dialog;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入好友设置");
        FriendsId = getIntent().getExtras().getLong("FriendsId");
        FriendsName=getIntent().getExtras().getString("FriendsName");

        set_friend_name= (TextView) findViewById(R.id.set_friend_name);
        set_friend_name.setText(FriendsName);
        setViewListener();
    }

    private void setViewListener() {
        findViewById(R.id.settingfriend_back).setOnClickListener(this);
        findViewById(R.id.delet_friend_butt).setOnClickListener(this);
        findViewById(R.id.add_blacklist).setOnClickListener(this);
        findViewById(R.id.change_note).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
      setContentView(R.layout.layout_settingfriends_activity);
    }

    @Override
    protected void onClickEvent(View v) {
       switch (v.getId()){
           case R.id.settingfriend_back:
               FriendSettingActivity.this.finish();
               break;
           case R.id.delet_friend_butt: //删除好友
               dialog = new AlertDialog.Builder(FriendSettingActivity.this).create();
               dialog.show();
               dialog.setCancelable(true);
               Window window=dialog.getWindow();
               View dialogView=View .inflate(FriendSettingActivity.this, R.layout.layout_friends_delect_dialog, null);
               window.setContentView(dialogView);

               dialogView.findViewById(R.id.delect_dialog_cancel).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                   }
               });
               dialogView.findViewById(R.id.delect_dialog_sure).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       delectFriends();
                   }
               });
               break;
           case R.id.change_note: //修改备注
               Intent intent=  new Intent(FriendSettingActivity.this,ChangeRemarks.class);
               Bundle bundle = new Bundle();
               bundle.putLong("FriendsId",FriendsId);
               bundle.putString("FriendsName",set_friend_name.getText().toString());
               intent.putExtras(bundle);
               startActivityForResult(intent,105);
               overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
               break;
           case R.id.add_blacklist://黑名单
               showPopup();
               break;
       }
    }
    private void showPopup() {
        popupWindow = new AddBlackListPopupWindow(this, itemsOnClick);
        popupWindow.showAtLocation(this.findViewById(R.id.setting_friends), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }
    //加入黑名单
    private void addBlackList() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("fid", FriendsId);
        MyLogs.e("haifeng","添加黑名单 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.ADD_BLACK_LIST, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MyLogs.e("haifeng","走onFailure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        String json=response.body().string();
                        MyLogs.e("haifeng","添加黑名单 :json="+json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            String errCode = jsonObject.optString("errCode");
                            if (errCode.equals("0")) {
                                int datas = jsonObject.optInt("datas");
                                if (datas==1) {
                                    MyLogs.e("haifeng", "添加黑名单成功");
                                    setResult(66);///通知许愿主页，拉黑好友
                                }

                            }else {
                                MyLogs.e("haifeng","添加黑名单errCode="+errCode);
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
    //删除好友
    private void delectFriends() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("fid", FriendsId);
        MyLogs.e("haifeng","删除好友 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.DELET_FRIEND, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MyLogs.e("haifeng","走onFailure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        String json=response.body().string();
                        MyLogs.e("haifeng","删除好友 :json="+json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            String errCode = jsonObject.optString("errCode");
                            if (errCode.equals("0")) {
                                int datas = jsonObject.optInt("datas");
                                if (datas==1) {
                                    MyLogs.e("haifeng", "删除成功");
                                    setResult(66);////通知许愿主页，删除了好友关系
                                    dialog.dismiss();
                                }

                            }else {
                                MyLogs.e("haifeng","删除好友errCode="+errCode);
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

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.add_balcklist_cancel:
                    popupWindow.dismiss();
                    break;
                case R.id.add_balcklist_sure:
                   addBlackList();
                    break;
            }
        }


    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //许愿主页---好友设置
        if (requestCode==105&&resultCode==55) {
            setResult(66);//通知许愿主页，修改了备注
        }
    }
}
