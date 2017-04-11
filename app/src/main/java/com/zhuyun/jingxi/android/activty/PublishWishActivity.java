package com.zhuyun.jingxi.android.activty;

import android.view.View;
import android.widget.EditText;
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
 * Created by user0081 on 2016/6/17.   发布许愿
 */
public class PublishWishActivity extends BaseActivity{
    private EditText publishwish_edit;
    private TextView wish_giftname;
    private ImageView publishwish_img;
    private long gId;
    private String giftName,imgIconUrl;

    protected void initView() {
        MyLogs.e("haifeng","进入发布许愿");
        publishwish_img= (ImageView) findViewById(R.id.publishwish_img);
        wish_giftname= (TextView) findViewById(R.id.wish_giftname);
        publishwish_edit= (EditText) findViewById(R.id.publishwish_edit);
        findViewById(R.id.publishwish_back).setOnClickListener(this);
        findViewById(R.id.publishwish_butt).setOnClickListener(this);
        findViewById(R.id.publishwish_text_butt).setOnClickListener(this);
        initData();
    }

    @Override
    protected void setContentLayout() {
         setContentView(R.layout.layout_publishwish_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.publishwish_back:
                finish();
                break;
            case R.id.publishwish_butt:
                publishWish();
                break;
        }
    }

    private void initData() {
        gId=getIntent().getExtras().getLong("gId");
        giftName=getIntent().getExtras().getString("giftName");//礼物名
        imgIconUrl=getIntent().getExtras().getString("imgIconUrl");
        wish_giftname.setText(giftName);
        Picasso.with(App.getAppContext()).load(imgIconUrl).placeholder(R.drawable.gongluetuijian)
                .error(R.drawable.gongluetuijian).into(publishwish_img);
    }
    private void publishWish() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("gId", gId);// long    许愿产品id
        params.put("content", publishwish_edit.getText().toString());// string  许愿内容
        params.put("quantity", 1);//long    数量
        params.put("type", 1);//int     许愿类型（1：普通许愿，2：众筹许愿）
        MyLogs.e("haifeng","发布许愿 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.WISH_PUBLISH, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MyLogs.e("haifeng","走onFailure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        String json=response.body().string();
                        MyLogs.e("haifeng","发布许愿 :json="+json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            String errCode = jsonObject.optString("errCode");
                            if (errCode.equals("0")) {
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TempSingleToast.showToast(App.getAppContext(), "许愿成功");
                                        MyLogs.e("haifeng", "许愿成功");
                                        finish();
                                    }
                                });
                            }else {
                                MyLogs.e("haifeng","许愿失败，errorCode="+errCode);
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                   TempSingleToast.showToast(App.getAppContext(), "许愿失败");
                                    }
                                });
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
