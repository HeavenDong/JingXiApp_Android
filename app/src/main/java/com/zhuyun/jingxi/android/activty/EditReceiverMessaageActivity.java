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
 * Created by user0081 on 2016/7/6.   编辑收货信息
 */
public class EditReceiverMessaageActivity extends BaseActivity{
    private EditText edit_receiver_name,edit_phone,edit_receivedmsg_details_address;
    private TextView edit_address;
    private String name,address,detailsAddress,mobile;
    private long itemId;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入编辑收货信息");
        edit_receiver_name= (EditText) findViewById(R.id.edit_receiver_name);
        edit_receivedmsg_details_address= (EditText) findViewById(R.id.edit_receivedmsg_details_address);
        edit_receiver_name= (EditText) findViewById(R.id.edit_receiver_name);
        edit_phone= (EditText) findViewById(R.id.edit_phone);
        edit_address= (TextView) findViewById(R.id.edit_address);
        initData();
        setViewListener();
    }

    private void initData() {
        itemId= getIntent().getExtras().getLong("itemId");
        name=getIntent().getExtras().getString("name");
        mobile=getIntent().getExtras().getString("mobile");
        address=getIntent().getExtras().getString("address");
        detailsAddress=getIntent().getExtras().getString("detailsAddress");
        edit_receiver_name.setText(name);
        edit_phone.setText(mobile);
        edit_address.setText(address);
        edit_receivedmsg_details_address.setText(detailsAddress);
    }

    private void setViewListener() {
        findViewById(R.id.edit_receive_msg_back).setOnClickListener(this);
        findViewById(R.id.edit_receivedmsg_phome).setOnClickListener(this);
        findViewById(R.id.edit_receivedmsg_address).setOnClickListener(this);
        findViewById(R.id.edit_receivedmsg_delect).setOnClickListener(this);
        findViewById(R.id.edit_receivedmsg_save).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_editreceiver_msg_activity);
    }

    @Override
    protected void onClickEvent(View v) {
         switch (v.getId()){
             case R.id.edit_receive_msg_back:
                 EditReceiverMessaageActivity.this.finish();
                 break;
             case R.id.edit_receivedmsg_phome:
                 Intent intent = new Intent(EditReceiverMessaageActivity.this, MobilePhoneActivity.class);
                 Bundle bundle = new Bundle();
                 bundle.putString("from","ReceiverMessaage");
                 intent.putExtras(bundle);
                 startActivityForResult(intent, 207);
                 overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                 break;
             case R.id.edit_receivedmsg_address:
                 Intent intent2 = new Intent(EditReceiverMessaageActivity.this, MyProvenceActivity.class);
                 startActivityForResult(intent2, 206);
                 overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                 break;
             case R.id.edit_receivedmsg_delect:
                 EditReceiverMessaageActivity.this.finish();
                 TempSingleToast.showToast(App.getAppContext(),"删除该信息");
                 break;
             case R.id.edit_receivedmsg_save:
                 Map<String,Object> params = new HashMap<String,Object>();
                 params.put("id", itemId);//	条目id
                 params.put("name", edit_receiver_name.getText().toString());
                 /**假数据*/
                 params.put("country", 1);
                 params.put("province", 1);
                 params.put("city", 1);
                 params.put("area", 1);
                 params.put("address", edit_receivedmsg_details_address.getText().toString());//详细收货地址
                 params.put("tel", edit_phone.getText().toString());
                 params.put("isDefault", 1);//是否默认地址
                 requestEditAddress(params);


                 break;

         }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 206:
                if (resultCode == 22) {//编辑地址
                    int countryId = data.getIntExtra("countryId", 0);
                    int provenceId = data.getIntExtra("provenceId", 0);
                    int cityId = data.getIntExtra("cityId", 0);

                    String address = data.getStringExtra("provenceName") + " " + data.getStringExtra("cityName");
                    edit_address.setText(address);
                }
                break;
            case 207:
                if (resultCode == 27) {//通讯录
                    String mobile = data.getStringExtra("mobile");
                    edit_phone.setText(mobile);
                }
                break;
        }
    }

    //新增收货信息地址
    public void requestEditAddress(Map<String, Object> params) {
        MyLogs.e("haifeng","编辑收货信息 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.EDIT_RECEIVE_ADDRESS, params, new Callback() {
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
                        MyLogs.e("haifeng","编辑收货信息 :json="+json);
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
                                            setResult(42);
                                            EditReceiverMessaageActivity.this.finish();
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
