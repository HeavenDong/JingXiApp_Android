package com.zhuyun.jingxi.android.activty;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
 * Created by user0081 on 2016/6/17.  礼品支付
 */
public class PayGiftActivity extends BaseActivity{
    private CheckBox pay_weixin_box,pay_zhifubao_box,pay_jingxibi_box;
    private int way=0;//0代表微信支付；1代表支付宝支付；2代表惊喜币支付;
    private int type;//许愿类型（1：普通许愿，2：众筹许愿）
    private long wishId;
    private String from,person,giftname,price,number,giftUrl;
    private TextView gift_name,receiver_name,pay_standard,pay_number,pay_price;
    private ImageView gift_pay_head;
    private RelativeLayout pay_from;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入礼品支付");
        pay_from= (RelativeLayout) findViewById(R.id.pay_from);
        gift_name= (TextView) findViewById(R.id.gift_name);
        pay_number= (TextView) findViewById(R.id.pay_number);
        pay_price= (TextView) findViewById(R.id.pay_price);
        receiver_name= (TextView) findViewById(R.id.receiver_name);
        pay_weixin_box= (CheckBox) findViewById(R.id.pay_weixin_box);
        pay_zhifubao_box= (CheckBox) findViewById(R.id.pay_zhifubao_box);
        pay_jingxibi_box= (CheckBox) findViewById(R.id.pay_jingxibi_box);
        gift_pay_head= (ImageView) findViewById(R.id.gift_pay_head);
        pay_weixin_box.setChecked(true);
        initData();
        setViewListener();
    }

    private void initData() {
        wishId=getIntent().getExtras().getLong("wishId");
        type=getIntent().getExtras().getInt("type");
        from=getIntent().getExtras().getString("from");//来源
        giftname=getIntent().getExtras().getString("giftName");//礼品
        person=getIntent().getExtras().getString("person");//收礼人
        price= String.valueOf(getIntent().getExtras().getDouble("price"));//价格
        number=getIntent().getExtras().getString("number");//数量
        giftUrl=getIntent().getExtras().getString("giftUrl");
        MyLogs.e("haifeng","type="+type+"wishId="+wishId+"number="+number+"giftName="+giftname);
        gift_name.setText(giftname);
        receiver_name.setText(person);
        pay_price.setText("￥"+price);
        if (from.equals("爱惊喜")){
            pay_from.setVisibility(View.GONE);
        }else if (from.equals("确认订单")){
            pay_from.setVisibility(View.VISIBLE);
            pay_number.setText(number);
        }
        Picasso.with(App.getAppContext()).load(giftUrl).placeholder(R.drawable.baoxiangtupian)
                .error(R.drawable.baoxiangtupian).into(gift_pay_head);

    }

    @Override
    protected void setContentLayout() {
       setContentView(R.layout.layout_paygift_activity);
    }

    @Override
    protected void onClickEvent(View v) {
     switch (v.getId()){
         case R.id.paygift_back:
             PayGiftActivity.this.finish();

             break;
         case R.id.pay_weixin_relat:
             pay_weixin_box.setChecked(true);
             pay_zhifubao_box.setChecked(false);
             pay_jingxibi_box.setChecked(false);
             if (pay_weixin_box.isChecked()) {
                 way=0;
             }else{
                 way=-1;
             }
             break;
         case R.id.pay_zhifubao_relat:
             pay_zhifubao_box.setChecked(true);
             pay_weixin_box.setChecked(false);
             pay_jingxibi_box.setChecked(false);
             if (pay_zhifubao_box.isChecked()) {
                 way=1;
             }else{
                 way=-1;
             }
             break;
         case R.id.pay_jingxibi_relat:
             pay_jingxibi_box.setChecked(true);
             pay_weixin_box.setChecked(false);
             pay_zhifubao_box.setChecked(false);
             if (pay_jingxibi_box.isChecked()) {
                 way=2;
             }else{
                 way=-1;
             }
             break;
         case R.id.pay_sure:
             if(CommanUtil.isNetworkAvailable()) {
                 if(way==0){
//                     if(isWXAppInstalledAndSupported()) {
                         TempSingleToast.showToast(App.getAppContext(), "开启微信支付");
//                     }
                 }
                 if(way==1){
                     TempSingleToast.showToast(App.getAppContext(), "开启支付宝支付");
                 }
                 if(way==2){
                     TempSingleToast.showToast(App.getAppContext(), "开启惊喜币支付");
                     if (type!=0) {
                         MyLogs.e("haifeng","送礼，测试支付成功调送礼接口");
                         Map<String, Object> params = new HashMap<String, Object>();
                         params.put("id", wishId);
                         params.put("type", type);
                         paySuccessed(params);
                     }else {
                         MyLogs.e("haifeng","自己买");
                     }
                 }
             }else {
                 TempSingleToast.showToast(App.getAppContext(), "网络不可用");
             }
             break;
     }
    }

    private void setViewListener() {
        findViewById(R.id.paygift_back).setOnClickListener(this);
        findViewById(R.id.pay_weixin_relat).setOnClickListener(this);
        findViewById(R.id.pay_zhifubao_relat).setOnClickListener(this);
        findViewById(R.id.pay_jingxibi_relat).setOnClickListener(this);
        findViewById(R.id.pay_sure).setOnClickListener(this);
    }

//    //检出微信app是否安装
//    private boolean isWXAppInstalledAndSupported() {
//        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
//        msgApi.registerApp(Constants.APP_ID);
//
//        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
//                && msgApi.isWXAppSupportAPI();
//        return sIsWXAppInstalledAndSupported;
//    }

    private void paySuccessed(Map<String, Object> params) {
        MyLogs.e("haifeng","送礼接口（支付后） params="+params);
        //请求网络
        try {
            HttpClient.requestPostAsyncHeader(Url.CHANGE_PSW, params, new Callback() {
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

                        MyLogs.e("haifeng","送礼 :json="+json);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(json);
                            final String errCode = jsonObject.optString("errCode");
                            if (errCode.equals("0")) {
                                String datas =  jsonObject.optString("datas");
                                if (datas.equals("1")) {
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TempSingleToast.showToast(App.getAppContext(),"支付成功");
                                        }
                                    });
                                }else {
                                    MyLogs.e("haifeng", "支付失败");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TempSingleToast.showToast(App.getAppContext(),"支付失败");

                                        }
                                    });
                                }

                            }else {
                                MyLogs.e("haifeng", "支付失败 errorCode="+errCode);
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
