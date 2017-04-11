package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
 * Created by user0081 on 2016/7/6.   新建收货信息
 */
public class AddReceiverMessaageActivity extends BaseActivity{
    private EditText new_receiver_name,new_phone,new_details_address;
    private TextView new_address;
    private long itemId;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入新建收货信息");
        new_receiver_name= (EditText) findViewById(R.id.new_receiver_name);
        new_phone= (EditText) findViewById(R.id.new_phone);
        new_address= (TextView) findViewById(R.id.new_address);
        new_details_address= (EditText) findViewById(R.id.new_details_address);

        setViewListener();
    }

    private void setViewListener() {
        findViewById(R.id.new_receive_msg_back).setOnClickListener(this);
        findViewById(R.id.new_receivedmsg_phome).setOnClickListener(this);
        findViewById(R.id.new_receivedmsg_address).setOnClickListener(this);
        findViewById(R.id.new_receivedmsg_save).setOnClickListener(this);


    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_addreceiver_msg_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.new_receive_msg_back:
                AddReceiverMessaageActivity.this.finish();
                break;
            case R.id.new_receivedmsg_phome:
                Intent intent = new Intent(AddReceiverMessaageActivity.this, MobilePhoneActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from","ReceiverMessaage");
                intent.putExtras(bundle);
                startActivityForResult(intent, 208);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.new_receivedmsg_address:
                Intent intent2 = new Intent(AddReceiverMessaageActivity.this, MyProvenceActivity.class);
                startActivityForResult(intent2, 209);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                break;
            case R.id.new_receivedmsg_save:
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("name", new_receiver_name.getText().toString());
                /**假数据*/
                params.put("country", (long)1);
                params.put("province", (long)1);
                params.put("city", (long)1);
                params.put("area", (long)1);
                params.put("address", new_details_address.getText().toString());//详细收货地址
                params.put("tel", new_phone.getText().toString());
                params.put("isDefault", 1);//是否默认地址
                requestAddAddress(params);

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 209:
                if (resultCode == 22) {//编辑地址
                    int countryId = data.getIntExtra("countryId", 0);
                    int provenceId = data.getIntExtra("provenceId", 0);
                    int cityId = data.getIntExtra("cityId", 0);

                    String address = data.getStringExtra("provenceName") + " " + data.getStringExtra("cityName");
                    new_address.setText(address);
                }
                break;
            case 208:
                if (resultCode == 27) {//通讯录
                    String mobile = data.getStringExtra("mobile");
                    new_phone.setText(mobile);
                }
                break;
        }
    }

    //新增收货信息地址
    public void requestAddAddress(Map<String, Object> params) {
        MyLogs.e("haifeng","新增收货信息地址 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.ADD_RECEIVE_ADDRESS, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MyLogs.e("haifeng","走onFailure");
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            TempSingleToast.showToast(App.getAppContext(),"服务器连接失败");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        String json=response.body().string();
                        MyLogs.e("haifeng","新增收货信息地址 :json="+json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            final String errCode = jsonObject.optString("errCode");
                            if (errCode.equals("0")) {
                                int datas = jsonObject.optInt("datas");
                                if (datas==1) {
                                    MyLogs.e("haifeng", "成功");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setResult(41);
                                            AddReceiverMessaageActivity.this.finish();
                                            TempSingleToast.showToast(App.getAppContext(),"保存成功");
                                        }
                                    });
                                }else {
                                    MyLogs.e("haifeng", "失败");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TempSingleToast.showToast(App.getAppContext(),"保存失败");
                                        }
                                    });
                                }

                            }else {
                                    MyLogs.e("haifeng","errCode="+errCode);

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
