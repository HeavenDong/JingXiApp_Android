package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.ViewPagerAdapter2;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.GiftDetailsBean;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.view.MyIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/17.  礼品详情
 */
public class GiftDetailsActivity extends BaseActivity{
    private Long giftId;
    private double price;
    private String imgIconUrl,from,person;
    private ViewPager gift_details_viewpager;
    private MyIndicator gift_myindicator;
    private ViewPagerAdapter2 mImageAdapter;
    private List<View> mListView = new ArrayList<>();
    private List<GiftDetailsBean> points = new ArrayList<>();
    private TextView details_giftname,details_giftprice,details_giftcontent,go_buy;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入礼品详情");

        details_giftname= (TextView) findViewById(R.id.details_giftname);
        details_giftprice= (TextView) findViewById(R.id.details_giftprice);
        details_giftcontent= (TextView) findViewById(R.id.details_giftcontent);
        gift_details_viewpager= (ViewPager) findViewById(R.id.gift_details_viewpager);
        gift_myindicator= (MyIndicator) findViewById(R.id.gift_myindicator);
        go_buy= (TextView) findViewById(R.id.go_buy);
        MyLogs.e("haifeng","礼品详情4");

        initData();
        loadData();
        setViewListener();
    }

    private void initData() {
        giftId= getIntent().getExtras().getLong("giftId");
        from=getIntent().getExtras().getString("from");
        person=getIntent().getExtras().getString("person");
        MyLogs.e("haifeng","接收到 come="+from+"person="+person+"giftId="+giftId);
        if (from.equals("WishDetailsActivity")){
            go_buy.setText("送给Ta");
        }

        for (int i = 0; i < 4; i++) {
            GiftDetailsBean bean=new GiftDetailsBean();
            ImageView image1 = new ImageView(this);
            image1.setBackgroundResource(R.drawable.aijingxi_banner);
            mListView.add(image1);
            bean.isCurrent=0;
            points.add(bean);
        }
        mImageAdapter = new ViewPagerAdapter2(mListView);
        gift_details_viewpager.setAdapter(mImageAdapter);
        gift_myindicator.setViewPager(gift_details_viewpager);
    }

    private void setViewListener() {
        findViewById(R.id.giftdetail_back).setOnClickListener(this);
        findViewById(R.id.go_wish).setOnClickListener(this);
        findViewById(R.id.go_buy).setOnClickListener(this);

    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_giftdetails_activity);
    }

    @Override
    protected void onClickEvent(View v) {
     switch (v.getId()){
         case R.id.giftdetail_back:
             GiftDetailsActivity.this.finish();
             break;
         case R.id.go_wish:
             Intent intent=  new Intent(GiftDetailsActivity.this,PublishWishActivity.class);
             Bundle bundle = new Bundle();
             bundle.putLong("gId",giftId);
             bundle.putString("giftName",details_giftname.getText().toString());
             bundle.putString("imgIconUrl",imgIconUrl);
             intent.putExtras(bundle);
             startActivity(intent);
             overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
             break;
         case R.id.go_buy:
             if (from.equals("WishDetailsActivity")){
                 Intent intent3 = new Intent(GiftDetailsActivity.this, PayGiftActivity.class);
                 Bundle bundle3 = new Bundle();
                 bundle3.putString("from","爱惊喜");
                 bundle3.putString("person",person);
                 bundle3.putString("giftUrl",imgIconUrl);
                 bundle3.putString("giftName",details_giftname.getText().toString());
                 bundle3.putDouble("price",price);
                 bundle3.putString("number","1");
                 intent3.putExtras(bundle3);
                 startActivity(intent3);
             }else {
                 Intent intent2 = new Intent(GiftDetailsActivity.this, ConfirmOrderActivity.class);
                 Bundle bundle2 = new Bundle();
                 bundle2.putString("giftName", details_giftname.getText().toString());
                 bundle2.putString("imgIconUrl",imgIconUrl);
                 bundle2.putDouble("price", price);
                 intent2.putExtras(bundle2);
                 startActivity(intent2);
                 overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
             }
             break;
     }
    }
    private void loadData() {
        String url= Url.GIFT_RECOMMEND_DETAILS_INFO+"?id="+giftId;
        MyLogs.e("haifeng","走礼品详情接口");
        HttpClient.requestGetAsyncHeader(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "礼品详情接口，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            final JSONObject jsonObject2 = jsonObject.optJSONObject("datas");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            wishId=jsonObject2.optLong("id");
                                            price = jsonObject2.optDouble("price");
                                            imgIconUrl = jsonObject2.optString("imgIconUrl");
                                            details_giftprice.setText("￥"+price);
                                            details_giftname.setText(jsonObject2.optString("name").toString());
                                            details_giftcontent.setText(jsonObject2.optString("des").toString());
//                                            Picasso.with(App.getAppContext()).load(imgIconUrl).placeholder(R.drawable.kongtu)
//                                                    .error(R.drawable.kongtu).into(gift_details_img);
                                        }
                                    });

//
//                             id=jsonObject2.optInt("id");
//                                    bundle.putInt("wishUserId",jsonObject2.optInt("wishUserId"));
//                                    bundle.putInt("gender",jsonObject2.optInt("gender"));
//                                    bundle.putInt("type",jsonObject2.optInt("type"));
//                                    bundle.putInt("likeNum",jsonObject2.optInt("likeNum"));
//                                    bundle.putInt("commNum",jsonObject2.optInt("commNum"));
//                                    bundle.putInt("isFriend",jsonObject2.optInt("isFriend"));
//
//                                    bundle.putString("nickName",jsonObject2.optString("nickName"));
//                                    bundle.putString("portraitUrl",jsonObject2.optString("portraitUrl"));
//                                    bundle.putString("content",jsonObject2.optString("content"));
//                                    bundle.putString("imgUrl",jsonObject2.optString("imgUrl"));
//                                    bundle.putString("goodsName",jsonObject2.optString("goodsName"));
//
//                                    bundle.putLong("utpTime",jsonObject2.optLong("utpTime"));
//                                    bundle.putDouble("cfPrice",jsonObject2.optDouble("cfPrice"));

                                }

                    } catch (JSONException e) {
                        MyLogs.e("haifeng","走了异常 :"+e);
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");

                }
            }
        });
    }

}
