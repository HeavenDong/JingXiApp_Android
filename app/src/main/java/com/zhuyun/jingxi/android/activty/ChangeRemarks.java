package com.zhuyun.jingxi.android.activty;

import android.text.TextUtils;
import android.view.View;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/28.    备注名
 */
public class ChangeRemarks extends BaseActivity{
    private long FriendsId;
    private String   FriendsName;
    private ClearEditText remark_edit;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入备注名");
        FriendsId = getIntent().getExtras().getLong("FriendsId");
        FriendsName=getIntent().getExtras().getString("FriendsName");
        remark_edit= (ClearEditText) findViewById(R.id.remark_edit);
        remark_edit.setText(FriendsName);
        setViewListener();
    }
    private void setViewListener() {
        findViewById(R.id.remark_back).setOnClickListener(this);
        findViewById(R.id.save_remark).setOnClickListener(this);
    }
    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_changeremarks_activity);
    }

    @Override
    protected void onClickEvent(View v) {
       switch (v.getId()){
           case R.id.remark_back:
               ChangeRemarks.this.finish();
               break;
           case R.id.save_remark:
               if (TextUtils.isEmpty(remark_edit.getText().toString())){
                   TempSingleToast.showToast(App.getAppContext(),"备注名不能为空");
                   break;
               }else {
                   changeRemark(remark_edit.getText().toString());
               }

               break;
       }
    }
    //修改备注
    private void changeRemark(String rName) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("fid", FriendsId);
        params.put("rName", rName);
        MyLogs.e("haifeng","修改备注 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.CHANGE_NOTE, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MyLogs.e("haifeng","走onFailure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        String json=response.body().string();
                        MyLogs.e("haifeng","修改备注 :json="+json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            String errCode = jsonObject.optString("errCode");
                            if (errCode.equals("0")) {
                                JSONObject  jsonObject2 = jsonObject.optJSONObject("datas");
                                int flag = jsonObject2.optInt("flag");
                                if (flag==1) {
                                    MyLogs.e("haifeng", "修改备注成功");
                                    setResult(55);
                                    ChangeRemarks.this.finish();
                                }else {
                                    MyLogs.e("haifeng", "修改备注失败");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TempSingleToast.showToast(App.getAppContext(),"修改备注失败");
                                        }
                                    });
                                }

                            }else {
                                MyLogs.e("haifeng","修改备注errCode="+errCode);
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
