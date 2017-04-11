package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.WishHomeBean;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.huanxin.ui.ChatActivity;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.view.CirclrDegreeView;
import com.zhuyun.jingxi.android.view.FastBlur;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/17.  许愿主页
 */
public class WishHomeActivity extends BaseActivity{
    private TextView wishhome_name,add_friends_butt,wishhome_number,wish_home_constellation,wish_home_address;
    private ImageView wishhome_more,wishhome_sex,wish_home_backimg;
    private RoundedImageView wishhome_head_url;
    private CirclrDegreeView wish_home_img1,wish_home_img2,wish_home_img3,wish_home_img4;
    private long friendId;
    private int gender;
    private String portrait,nickName,mobile;
    private  Bitmap blurBitmap,textImg;
  private String text1="http://sta.273.com.cn/app/mbs/img/mobile_default.png";
    private String text="http://file.youboy.com/a/150/23/76/0/177860.jpg";
    private List<WishHomeBean> lists=new ArrayList<>();
    protected void initView() {
       MyLogs.e("haifeng","进入许愿主页");
        wishhome_head_url= (RoundedImageView) findViewById(R.id.wishhome_head_url);
        wishhome_name= (TextView) findViewById(R.id.wishhome_name);
        wishhome_sex= (ImageView) findViewById(R.id.wishhome_sex);
        add_friends_butt= (TextView) findViewById(R.id.add_friends_butt);
        wishhome_more= (ImageView) findViewById(R.id.wishhome_more);
        wish_home_backimg= (ImageView) findViewById(R.id.wish_home_backimg);
        wishhome_number= (TextView) findViewById(R.id.wishhome_number);
        wish_home_constellation= (TextView) findViewById(R.id.wish_home_constellation);
        wish_home_address= (TextView) findViewById(R.id.wish_home_address);
        wish_home_img1= (CirclrDegreeView) findViewById(R.id.wish_home_img1);
        wish_home_img2= (CirclrDegreeView) findViewById(R.id.wish_home_img2);
        wish_home_img3= (CirclrDegreeView) findViewById(R.id.wish_home_img3);
        wish_home_img4= (CirclrDegreeView) findViewById(R.id.wish_home_img4);
        initData();
        loadInfoData();
        setViewListener();
    }
    private void initData() {
        friendId = getIntent().getExtras().getLong("FriendsId");
        nickName=getIntent().getExtras().getString("FriendsName");
           portrait=getIntent().getExtras().getString("portraitUrl");
//        mobile="18210097124";
           gender=getIntent().getExtras().getInt("gender");
           wishhome_name.setText(nickName);
//         if (gender==0){
//            wishhome_sex.setImageResource(R.drawable.nv);
//           }else {
//            wishhome_sex.setImageResource(R.drawable.nan);
//           }
//           if (FriendsId==(long) SharedPreUtils.get(App.getAppContext(),"user_id",(long)0)) {
//               Picasso.with(App.getAppContext()).load(text).placeholder(R.drawable.touxiang_yuan)
//                       .error(R.drawable.touxiang_yuan).into(wishhome_head_url);
//               gaoshi(text);
//           }
    }

    private void setViewListener() {
        add_friends_butt.setOnClickListener(this);
        wishhome_more.setOnClickListener(this);
        findViewById(R.id.wish_home_wishlist).setOnClickListener(this);
        findViewById(R.id.wishhome_back).setOnClickListener(this);

    }

    @Override
    protected void setContentLayout() {
       setContentView(R.layout.layout_wishhome_activity);
    }

    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.wishhome_back:
              WishHomeActivity.this.finish();
              break;
          case R.id.wishhome_more://更多
              Intent intent=  new Intent(WishHomeActivity.this,FriendSettingActivity.class);
              Bundle bundle = new Bundle();
              bundle.putLong("FriendsId",friendId);
              bundle.putString("FriendsName",wishhome_name.getText().toString());
              intent.putExtras(bundle);
              startActivityForResult(intent,104);
              overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;
          case R.id.wish_home_wishlist:
              Intent intent2=  new Intent(WishHomeActivity.this,WishActivity.class);
              Bundle bundle2 = new Bundle();
              bundle2.putLong("FriendsId",friendId);
              intent2.putExtras(bundle2);
              startActivity(intent2);
              overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;
          case R.id.add_friends_butt://添加好友
              //设置好友的昵称 、头像
              EaseUser user = new EaseUser(CommanUtil.md5Hex(friendId+"",true)+friendId);
              user.setAvatar(text1);
              user.setNick(nickName);
              DemoHelper.getInstance().saveContact(user);
              boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                      App.currentUserNick.trim());
              if (!updatenick) {
                  Log.e("haifeng", "update current user nick fail");
              }
              DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
//              if (isFriend==1){
                  MyLogs.e("haifeng","走 即时通讯updatenick="+updatenick+";;;getCurrentUser="+ EMClient.getInstance().getCurrentUser());
              // demo中直接进入聊天页面，实际一般是进入用户详情页
              startActivity(new Intent(WishHomeActivity.this, ChatActivity.class).putExtra("userId", CommanUtil.md5Hex(friendId+"",true)+friendId));
//              }else {
//                  Intent intent2 = new Intent(WishHomeActivity.this, ApplyFriendActivity.class);
//                  Bundle bundle2 = new Bundle();
//                  bundle2.putLong("itsId", FriendsId);
//                  intent2.putExtras(bundle2);
//                  startActivity(intent2);
//                  overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//              }
              break;
      }
    }

    //查询好友信息
    private void loadInfoData() {
        String url=Url.GET_FRIENDS_MSG+"?fId="+friendId;
        MyLogs.e("haifeng","查好友信息 url="+url);
        HttpClient.requestGetAsyncHeader(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "查好友信息 :json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONObject  jsonObject2 = jsonObject.optJSONObject("datas");
                            friendId = jsonObject2.optLong("id");
                            portrait=jsonObject2.optString("portrait");
                            nickName=jsonObject2.optString("nickName");
                            mobile=jsonObject2.optString("mobile");
                            String rName=jsonObject2.optString("rName");
                            final int constellation = jsonObject2.optInt("constellation");
                            final int wishNum = jsonObject2.optInt("wishNum");
                            final String address=jsonObject2.optString("address");
                            final int gender=jsonObject2.optInt("gender");
                            JSONArray jsonArray = jsonObject2.optJSONArray("wishesProtocolList");
                            if (jsonArray != null) {
                                lists.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.optJSONObject(i);
                                    WishHomeBean bean=new WishHomeBean();
                                    bean.id=jsonObject3.optLong("id");
                                    bean.imgIconUrl=jsonObject3.optString("imgIconUrl");
                                    lists.add(bean);
                                }
                            }
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {

                                    wishhome_number.setText(wishNum+"");
                                    if (friendId==0){
                                        add_friends_butt.setVisibility(View.GONE);
                                        wish_home_address.setText("未填写");
                                        wish_home_address.setText("未填写");
                                    }else {
                                        wishhome_name.setText(nickName);
                                        wish_home_address.setText(address);
                                    }
                                    wish_home_constellation.setText(CommanUtil.getConstellation(constellation));
                                    if (gender==0){
                                        wishhome_sex.setImageResource(R.drawable.nv);
                                    }else {
                                        wishhome_sex.setImageResource(R.drawable.nan);
                                    }
                                    //许愿列表
                                    if (lists.size()>0) {
                                        wish_home_img1.setVisibility(View.VISIBLE);
                                        Picasso.with(App.getAppContext()).load(lists.get(0).imgIconUrl).into(wish_home_img1);
                                    }
                                    if (lists.size()>1) {
                                        wish_home_img2.setVisibility(View.VISIBLE);
                                        Picasso.with(App.getAppContext()).load(lists.get(1).imgIconUrl).into(wish_home_img2);
                                    }
                                    if (lists.size()>2) {
                                        wish_home_img3.setVisibility(View.VISIBLE);
                                        Picasso.with(App.getAppContext()).load(lists.get(2).imgIconUrl).into(wish_home_img3);
                                    }
                                    if (lists.size()>3) {
                                        wish_home_img4.setVisibility(View.VISIBLE);
                                        Picasso.with(App.getAppContext()).load(lists.get(3).imgIconUrl).into(wish_home_img4);
                                    }
//                                    gaoshi(portrait);

                                    if(!portrait.equals("")) {
                                        gaoshi(portrait);
                                        Picasso.with(App.getAppContext()).load(portrait).placeholder(R.drawable.touxiang_yuan)
                                                .error(R.drawable.touxiang_yuan).into(wishhome_head_url);
                                    }
                                }
                            });

                        }else {
                            if (errCode.equals("61001")){
                                MyLogs.e("haifeng","好友关系不存在");
                            }else {
                                MyLogs.e("haifeng","查好友信息errCode="+errCode);
                            }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //许愿主页---好友设置
        if (requestCode==104&&resultCode==66) {//修改了备注或删除或拉黑
            setResult(77);//通知好友页刷新列表
        }
//        if (requestCode==104&&resultCode==62) {//删除
//            setResult(77);//通知好友页刷新列表
//        }
//        if (requestCode==104&&resultCode==63) {//拉黑
//            setResult(77);//通知好友页刷新列表
//        }
    }

    private void gaoshi(final String portraitUrl){
        //高斯模糊
        Log.e("haifeng","高斯模糊 portraitUrl="+portraitUrl);
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                if (portraitUrl!=null||!portraitUrl.equals("")) {

                    URL url = null;
                    try {
                        url = new URL(portraitUrl);
                        Log.e("haifeng", url + "/高斯模糊");
                        InputStream is = url.openStream();

                        textImg = BitmapFactory.decodeStream(is);
                        // blur(textImg, wish_home_backimg);
                        blurBitmap = FastBlur.doBlur(textImg, 120, false);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("haifeng","不空/高斯模糊"+portraitUrl);
                }else {

                    textImg = BitmapFactory.decodeResource(getResources(), R.drawable.touxiang_yuan);
                    // blur(textImg, wish_home_backimg);
                    blurBitmap = FastBlur.doBlur(textImg, 120, false);
                    Log.e("haifeng","空/高斯模糊"+portraitUrl);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                wish_home_backimg.setImageBitmap(blurBitmap);
                Log.e("haifeng","成功/高斯模糊");
            }
        }.execute();
        //释放
        if (textImg != null && !textImg.isRecycled()) {
            // 回收并且置为null
            textImg.recycle();
            textImg = null;
            System.gc();
            MyLogs.e("haifeng","回收textImg");
        }
        if (blurBitmap != null && !blurBitmap.isRecycled()) {
            // 回收并且置为null
            blurBitmap.recycle();
            blurBitmap = null;
            System.gc();
            MyLogs.e("haifeng","回收bitmap");
        }
    }

    //没走通
    private void blur(Bitmap bitmap, ImageView view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;//图片缩放比例；
        float radius = 20;//模糊程度

        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()/ scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setImageBitmap(overlay);
        //释放
        if (overlay != null && !overlay.isRecycled()) {
            // 回收并且置为null
            overlay.recycle();
            overlay = null;
            System.gc();
        }
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
        Log.e("haifeng", "blur time:" + (System.currentTimeMillis() - startMs));
    }

}
