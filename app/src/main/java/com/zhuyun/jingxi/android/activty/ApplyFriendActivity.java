package com.zhuyun.jingxi.android.activty;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

/**
 * Created by user0081 on 2016/6/28. 好友验证
 */
public class ApplyFriendActivity extends BaseActivity{
    private long itsId;
    private EditText apply_edit;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入好友验证");
        itsId = getIntent().getExtras().getLong("itsId");
        apply_edit= (EditText) findViewById(R.id.apply_edit);
        setViewListener();
    }
    private void setViewListener() {
        findViewById(R.id.apply_back).setOnClickListener(this);
        findViewById(R.id.send_apply).setOnClickListener(this);
    }
    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_applyfriends_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.apply_back:
                ApplyFriendActivity.this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.send_apply:
                if (TextUtils.isEmpty(apply_edit.getText().toString())){
                    TempSingleToast.showToast(App.getAppContext(),"备注名不能为空");
                    break;
                }else {
                    sendApply(apply_edit.getText().toString());
                }

                break;
        }
    }

    private void sendApply(String msg) {
        MyLogs.e("haifeng","走好友验证的接口 ");
        TempSingleToast.showToast(App.getAppContext(),"已发送");
        ApplyFriendActivity.this.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//        Map<String,Object> params = new HashMap<String,Object>();
//        params.put("fId", FriendsId);
//        params.put("", msg);
//
//        MyLogs.e("haifeng","好友验证 params="+params);
//        try {
//            HttpClient.requestPostAsyncHeader(Url.APPLY_FRIEND, params, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    MyLogs.e("haifeng","走onFailure");
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if(response.code()==200){
//                        String json=response.body().string();
//                        MyLogs.e("haifeng","申请加好友 :json="+json);
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(json);
//                            final String errCode = jsonObject.optString("errCode");
//                            if (errCode.equals("0")) {
//                                MyLogs.e("haifeng","申请了");
//
//                            }else {
//                                MyLogs.e("haifeng","申请errCode="+errCode);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }else {
//                        MyLogs.e("haifeng","response.code()！=200");
//
//                    }
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
