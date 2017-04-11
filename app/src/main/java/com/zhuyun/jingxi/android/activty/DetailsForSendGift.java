package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/28.送礼详情
 */
public class DetailsForSendGift extends BaseActivity{
    private long itemId,id,gId;
    private ImageView send_details_url;
    private RoundedImageView send_details_head;
    private TextView receive_person_name,send_time,send_details_giftname,send_content;
    @Override
    protected void initView() {
        itemId=getIntent().getExtras().getLong("itemId");
        MyLogs.e("haifeng","进入送礼详情,接收到 id="+itemId);

        send_details_url= (ImageView) findViewById(R.id.send_details_url);
        send_details_head= (RoundedImageView) findViewById(R.id.send_details_head);
        receive_person_name= (TextView) findViewById(R.id.receive_person_name);
        send_time= (TextView) findViewById(R.id.send_time);
        send_details_giftname= (TextView) findViewById(R.id.send_details_giftname);
        send_content= (TextView) findViewById(R.id.send_content);

        loadData();
        setViewListener();
    }

    private void setViewListener() {
        findViewById(R.id.detailsrecord_sendgift_back).setOnClickListener(this);
        findViewById(R.id.details_receivedgift_call).setOnClickListener(this);
        send_details_head.setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_detailsrecord_sendgift_activity);

    }

    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.detailsrecord_sendgift_back:
              DetailsForSendGift.this.finish();
              overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
              break;
          case R.id.details_receivedgift_call:
              MyLogs.e("haifeng","调系统打电话");
              Intent intent = new Intent();
              intent.setAction(Intent.ACTION_CALL);
              intent.setData(Uri.parse("tel:18753127533"));
              startActivity(intent);

              break;
          case R.id.send_details_head:
              Intent intent2=  new Intent(DetailsForSendGift.this,WishHomeActivity.class);
              Bundle bundle2 = new Bundle();
              bundle2.putLong("FriendsId",gId);
              intent2.putExtras(bundle2);
              startActivity(intent2);
              overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;
      }
    }
    //送礼详情
    private void loadData() {
        String url= Url.RECORD_SEND_DETAILS+"?id="+itemId;
        MyLogs.e("haifeng","送礼详情 url="+url);
        HttpClient.requestGetAsyncHeader(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "送礼详情 :json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                     id = jsonObject2.optLong("id");
                                     gId= jsonObject2.optLong("gId");
                                    final long utpTime = jsonObject2.optLong("utpTime");
                                    final String imgUrl = jsonObject2.optString("imgUrl");
                                    final String goodsName = jsonObject2.optString("goodsName");
                                    final String recipientNickName = jsonObject2.optString("recipientNickName");
                                    final int gender =jsonObject2.optInt("gender");
                                    final double giftsPrice = jsonObject2.optDouble("giftsPrice");
                                    final String content = jsonObject2.optString("content");
                                    MyLogs.e("haifeng", "解析完成");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            receive_person_name.setText(recipientNickName);
                                            send_time.setText(CommanUtil.transhms(utpTime,"MM/dd hh:mm"));
                                            send_details_giftname.setText(goodsName);
                                            send_content.setText(content);
                                            Picasso.with(App.getAppContext()).load(imgUrl).placeholder(R.drawable.touxiang_yuan)
                                                    .error(R.drawable.touxiang_yuan).into(send_details_url);
                                            Picasso.with(App.getAppContext()).load(imgUrl).placeholder(R.drawable.touxiang_yuan)
                                                    .error(R.drawable.touxiang_yuan).into(send_details_head);
                                        }
                                    });
                                }
                            }
                        }else {
                                MyLogs.e("haifeng","送礼详情errCode="+errCode);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","response.code()！=200");
                }
            }
        });
    }

}