package com.zhuyun.jingxi.android.activty;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mob.tools.utils.UIHandler;
import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.WishDetailsHoriListAdapter;
import com.zhuyun.jingxi.android.adapter.WishDetailsListAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.Friends;
import com.zhuyun.jingxi.android.bean.WishDetailsCommentBean;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.ShareConfig;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.CirclrDegreeView;
import com.zhuyun.jingxi.android.view.HorizontalListView;
import com.zhuyun.jingxi.android.view.RoundProgressBar;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/17. 许愿详情
 */
public class WishDetailsActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener,Handler.Callback {
    private View head,head2;
    private RoundedImageView wishdetails_head_img;
    private CirclrDegreeView wishdetails_head_url;
    private ImageView wishdetails_headsex;
    private TextView wishdetails_headname,wishdetails_head_time,wishdetails_head_content,wishdetails_head_giftname,wishdetails_likenum,wishdetails_commnum;
    private ListView listview;
    private List<WishDetailsCommentBean> commentLists=new ArrayList();
    private List<Friends> likedPersonLists=new ArrayList();
    private HorizontalListView horiListView;
    private WishDetailsListAdapter  commentAdapter;
    private WishDetailsHoriListAdapter likeAdapter;
    private EditText wishdetails_comment_edit;
    private RoundProgressBar progress_crowdfunding;
    private TextView progress_text;
    private String nickName,portraitUrl,googdsName,content,imgUrl,tag;//intent传值
    private int gender,type;
    private long wishUserId,wishID,utpTime,likeNum,commNum,isFriend;
    private double price;


    private HashMap<String, Object> map_wxcircle;    //朋友圈注册数据
    private HashMap<String, Object> map_wxFriend;    //微信好友注册数据
    private String share_image = "http://sta.273.com.cn/app/mbs/img/mobile_default.png";//分享图片
    private String share_url = "http://sta.273.com.cn/app/mbs/img/mobile_default.png";//分享web连接
    private String share_text;//分享的内容
    private String share_title;//分享的标题

    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入许愿详情");
        //头
        head = View.inflate(this, R.layout.layout_wishdetails_head,null);
        head2 = View.inflate(this, R.layout.layout_wishdetails_head2,null);
        wishdetails_head_img= (RoundedImageView) head.findViewById(R.id.wishdetails_head_img);
        wishdetails_head_url= (CirclrDegreeView) head.findViewById(R.id.wishdetails_head_url);
        wishdetails_headname= (TextView) head.findViewById(R.id.wishdetails_headname);
        wishdetails_headsex= (ImageView) head.findViewById(R.id.wishdetails_headsex);
        wishdetails_head_time=(TextView) head.findViewById(R.id.wishdetails_head_time);
        wishdetails_head_content=(TextView) head.findViewById(R.id.wishdetails_head_content);
        wishdetails_head_giftname=(TextView) head.findViewById(R.id.wishdetails_head_giftname);
        wishdetails_likenum=(TextView) head.findViewById(R.id.wishdetails_likenum);
        wishdetails_commnum=(TextView) head2.findViewById(R.id.wishdetails_commnum);

        horiListView= (HorizontalListView) head.findViewById(R.id.wishdetails_horilistview);
        progress_crowdfunding= (RoundProgressBar) head.findViewById(R.id.progress_crowdfunding);
        progress_text= (TextView) head.findViewById(R.id.progress_text);
        wishdetails_comment_edit= (EditText)findViewById(R.id.wishdetails_comment_edit);
        listview = (ListView)findViewById(R.id.wishdetails_listview);
        listview.addHeaderView(head);
        listview.addHeaderView(head2);
        listview.setSelector(R.color.transparency);
        initData();
        loadData();
        setViewListener();
    }
    private void setViewListener() {
      wishdetails_comment_edit.setOnClickListener(this);
      head.findViewById(R.id.wishdetails_head_see_giftdetails).setOnClickListener(this);//加好友
      head.findViewById(R.id.wishdetails_head_img).setOnClickListener(this);
      head.findViewById(R.id.go_crowdfunding).setOnClickListener(this);//帮Ta众筹
      findViewById(R.id.wishdetails_back).setOnClickListener(this);
      findViewById(R.id.wishdetails_share).setOnClickListener(this);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    MyLogs.e("haifeng", "点击弹出评论");
                    wishdetails_comment_edit.requestFocus();
                    InputMethodManager imm = (InputMethodManager) wishdetails_comment_edit.getContext().getSystemService(App.getAppContext().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                }
            }
        });
        horiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLogs.e("haifeng", "id="+likedPersonLists.get(position).id);
                Intent intent=  new Intent(WishDetailsActivity.this,WishHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("FriendsId",likedPersonLists.get(position).id);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void initData() {
        share_text = "分享的内容............";
        Bundle bundle= getIntent().getExtras();
        tag=bundle.getString("tag");
        wishID=bundle.getLong("wishID");//许愿id


//        nickName=bundle.getString("nickName");//收礼人
//        portraitUrl=bundle.getString("portraitUrl");
//        gender=bundle.getInt("gender");
//        content=bundle.getString("content");
//        utpTime=bundle.getLong("utpTime");
//        imgUrl=bundle.getString("imgUrl");
//        googdsName=bundle.getString("giftName");//礼物名
//        type=bundle.getInt("type");//（1：普通许愿； 2：众筹许愿）
//        price= bundle.getDouble("price");//众筹单价
//        likeNum=bundle.getInt("likeNum");
//        commNum=bundle.getInt("commNum");
//        isFriend=bundle.getInt("isFriend");
//
//
//        //填充数据
//        wishdetails_headname.setText(nickName);
//        wishdetails_head_giftname.setText(googdsName);
//        wishdetails_head_time.setText(CommanUtil.transhms(utpTime,"MM.dd hh:mm"));
//        wishdetails_head_content.setText(content);
//        wishdetails_likenum.setText(likeNum+"");
//        wishdetails_commnum.setText(commNum+"");
////        Picasso.with(App.getAppContext()).load(portraitUrl).placeholder(R.drawable.kongtu)
////                .error(R.drawable.kongtu).into(wishdetails_head_img);
////        Picasso.with(App.getAppContext()).load(imgUrl).placeholder(R.drawable.kongtu)
////                .error(R.drawable.kongtu).into(wishdetails_head_url);
//        if (gender==0){
//            wishdetails_headsex.setImageResource(R.drawable.nv);
//        }else {
//            wishdetails_headsex.setImageResource(R.drawable.nan);
//        }
//
//
//        MyLogs.e("haifeng","进入许愿详情3="+price);
//
//        //众筹进度
//        crowdFund(1000,555.5);


        commentAdapter =new WishDetailsListAdapter(this,commentLists);
        listview.setAdapter(commentAdapter);
        likeAdapter= new WishDetailsHoriListAdapter(this,likedPersonLists);
        horiListView.setAdapter(likeAdapter);
        MyLogs.e("haifeng", "3");

    }


     @Override
     public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
             if (tag.equals("评论")){
                listview.setSelection(1);
             }
         MyLogs.e("haifeng", "4");
         }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_wishdetails_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.wishdetails_back:
                WishDetailsActivity.this.finish();
                break;
            case R.id.wishdetails_comment_edit:
                MyLogs.e("hanzhen","如果点击完成1");
                wishdetails_comment_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        MyLogs.e("hanzhen","如果点击完成2");
                        if (actionId == EditorInfo.IME_ACTION_DONE){
                            //让软键盘消失
                            ((InputMethodManager) wishdetails_comment_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(WishDetailsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            final String content=wishdetails_comment_edit.getText().toString().trim();
                            if (content.equals("")){
                                TempSingleToast.showToast(App.getAppContext(),"内容不能为空");
                            }else{
                                //请求网络
                                if(CommanUtil.isNetworkAvailable()) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MyLogs.e("hanzhen","loadData  开子线程");
                                            Map<String,Object> params = new HashMap<String,Object>();
                                            params.put("id", wishID); // 许愿id
                                            params.put("toId", wishUserId);//被评论用户id
                                            params.put("content", content);
                                            publicCommentData(params);
                                        }
                                    }).start();
                                }else {
                                    TempSingleToast.showToast(App.getAppContext(),"你的网络不给力");
                                }

                            }

                        }
                        return true;
                    }
                });

                break;
            case R.id.go_crowdfunding:

                Intent intent2=  new Intent(WishDetailsActivity.this,CrowdFundingActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("nickName",nickName);
                bundle2.putString("giftName",googdsName);
                bundle2.putString("price",String.valueOf(price));
                intent2.putExtras(bundle2);
                startActivity(intent2);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.wishdetails_share:
                MyLogs.e("haifeng", "分享到微信，朋友圈");
                //如果要用shareSDK自带的UI，分享界面是不能修改的，只能更改分享平台的小图标和小图标下面的文字，如果非要更改分享界面，只能自己画UI，然后调用shareSDK的无UI分享方法。
                // 初始化sdk分享资源
                ShareSDK.initSDK(this, ShareConfig.APPKEY);
                initRegistInfo();
                share_image = share_image.replace("110-110", "600-800");//??????????????

                final AlertDialog dialog=  new AlertDialog.Builder(WishDetailsActivity.this).create();
                dialog.show();
                dialog.setCancelable(true);
                Window window=dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//透明背景
                View dialogView=View .inflate(WishDetailsActivity.this, R.layout.layout_webshare_dialog, null);
                window.setContentView(dialogView);

                dialogView.findViewById(R.id.share_dialog_friends).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        share_WxFriend();
                        dialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.share_dialog_room).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        share_CircleFriend();
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.wishdetails_head_img:
                Intent intent3=  new Intent(WishDetailsActivity.this,WishHomeActivity.class);
                Bundle bundle3 = new Bundle();
                /**假数据*/
                bundle3.putInt("FriendsId",68);
                intent3.putExtras(bundle3);
                startActivity(intent3);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.wishdetails_head_see_giftdetails:
                Intent intent=  new Intent(WishDetailsActivity.this,GiftDetailsActivity.class);
                Bundle bundle = new Bundle();
                /**假数据*/
                bundle.putLong("giftId",1);
                bundle.putString("from","WishDetailsActivity");
                bundle.putString("person",wishdetails_headname.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;

        }
    }

    private void loadData(){
        //请求网络
        if(CommanUtil.isNetworkAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyLogs.e("hanzhen","loadData  开子线程");
                    loadHead();
                    getLikedPerson();
                    getCommentPerson();
                }
            }).start();
        }else {
            TempSingleToast.showToast(App.getAppContext(),"你的网络不给力");
        }
    }
    //头部基本信息
    private void loadHead() {
        HttpClient.requestGetAsyncHeader(Url.HOME_ITEM_DETAILS+"?id="+wishID, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "详情,头，接口，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONObject jsonObject2 = jsonObject.optJSONObject("datas");
                            MyLogs.e("haifeng", "1");
                            wishID=jsonObject2.optLong("id");
                            wishUserId=jsonObject2.optLong("wishUserId");
                            nickName=jsonObject2.optString("nickName");
                            portraitUrl=jsonObject2.optString("portraitUrl");
                            gender=jsonObject2.optInt("gender");
                            content=jsonObject2.optString("content");
                            utpTime=jsonObject2.optLong("utpTime");
                            imgUrl=jsonObject2.optString("imgUrl");
                            googdsName=jsonObject2.optString("googdsName");
                            final double cfPercent=jsonObject2.optDouble("cfPercent");
                            final double cfPrice=jsonObject2.optDouble("cfPrice");
                            double cfGap=jsonObject2.optDouble("cfGap");
                            int type=jsonObject2.optInt("type");//许愿类别（1：普通许愿； 2：众筹许愿）
                            likeNum=jsonObject2.optInt("likeNum");
                            commNum=jsonObject2.optInt("commNum");
                            int isFriend=jsonObject2.optInt("isFriend");
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    wishdetails_headname.setText(nickName);
                                    wishdetails_head_giftname.setText(googdsName);
                                    wishdetails_head_time.setText(CommanUtil.transhms(utpTime,"MM.dd hh:mm"));
                                    wishdetails_head_content.setText(content);
                                    wishdetails_likenum.setText(likeNum+"");
                                    wishdetails_commnum.setText(commNum+"");
                                    Picasso.with(App.getAppContext()).load(portraitUrl).placeholder(R.drawable.touxiang_yuan)
                                            .error(R.drawable.touxiang_yuan).into(wishdetails_head_img);
                                    Picasso.with(App.getAppContext()).load(imgUrl).placeholder(R.drawable.shouyeliwu)
                                            .error(R.drawable.shouyeliwu).into(wishdetails_head_url);
                                    if (gender==0){
                                        wishdetails_headsex.setImageResource(R.drawable.nv);
                                    }else {
                                        wishdetails_headsex.setImageResource(R.drawable.nan);
                                    }
                                    /**假数据*/
                                    //众筹进度
                                    crowdFund(1000,555.5);
//                                    crowdFund(cfPercent,cfPrice);
                                }
                            });
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
   //点赞人列表
    private void getLikedPerson() {
        MyLogs.e("haifeng","点赞人列表");
        HttpClient.requestGetAsyncHeader(Url.ADD_LIKE_LIST+"?id="+wishID, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        TempSingleToast.showToast(App.getAppContext(),"服务连接失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.code()==200) {
                    final String json = response.body().string();
                    MyLogs.e("haifeng", "点赞人列表，json=" + json);
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            MyLogs.e("haifeng","runOnUIThread");
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                final String errCode = jsonObject.optString("errCode");
                                if (errCode.equals("0")) {
                                    JSONArray jsonArray = jsonObject.optJSONArray("datas");
                                    if (jsonArray != null) {
                                        likedPersonLists.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            final Friends bean = new Friends();
                                            JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                            bean.id=jsonObject2.optLong("id");
                                            bean.portrait=jsonObject2.optString("portrait");
                                            CommanUtil.runOnUIThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    likedPersonLists.add(bean);
                                                }
                                            });

                                        }
                                        MyLogs.e("haifeng","点赞集合大小="+likedPersonLists.size());
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                likeAdapter.notifyDataSetChanged();
                                            }
                                        });

                                    }else {
                                        MyLogs.e("haifeng","集合为null");
                                    }
                                }else {
                                    MyLogs.e("haifeng","errCode不对");
                                }
                            } catch (Exception e) {
                                MyLogs.e("haifeng","走了异常 :"+e);
                                e.printStackTrace();
                            }


                        }
                    });
                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");
                }
            }
        });
    }
    //评论列表
    private void getCommentPerson() {
        MyLogs.e("haifeng","走评论列表接口");
        HttpClient.requestGetAsyncHeader(Url.COMMENT_LIST+"?id="+wishID, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        TempSingleToast.showToast(App.getAppContext(),"服务连接失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.code()==200) {
                    final String json = response.body().string();
                    MyLogs.e("haifeng", "评论列表接口，json=" + json);
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            MyLogs.e("haifeng","runOnUIThread");
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                final String errCode = jsonObject.optString("errCode");
                                if (errCode.equals("0")) {
                                    JSONArray jsonArray = jsonObject.optJSONArray("datas");
                                    if (jsonArray != null) {
                                        commentLists.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            final WishDetailsCommentBean bean = new WishDetailsCommentBean();
                                            JSONObject jsonObject2 = jsonArray.optJSONObject(i);
//                                            bean.id=jsonObject2.optLong("id");
//                                            bean.portrait=jsonObject2.optString("portrait");
                                            commentLists.add(bean);
                                        }
                                        MyLogs.e("haifeng","评论集合大小="+commentLists.size());
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                commentAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }else {
                                        MyLogs.e("haifeng","集合为null");
                                    }
                                }else {
                                    MyLogs.e("haifeng","errCode不对");
                                }
                            } catch (Exception e) {
                                MyLogs.e("haifeng","走了异常 :"+e);
                                e.printStackTrace();
                            }


                        }
                    });
                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");
                }
            }
        });
    }
    //新增评论接口
    private void publicCommentData(Map<String, Object> params) {
        MyLogs.e("haifeng","新增评论接口 params="+params);
        try {
            HttpClient.requestPostAsyncHeader(Url.PERSON_ADD_LIKE, params, new Callback() {
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
                        MyLogs.e("haifeng","新增评论接口 :json="+json);
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
                                            TempSingleToast.showToast(App.getAppContext(),"成功");
                                        }
                                    });
                                }else {
                                    MyLogs.e("haifeng", "失败");
                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TempSingleToast.showToast(App.getAppContext(),"失败");
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

    private void crowdFund(double zong,double you) {
        /**假数据*/
//        double zong=price;
//       zong=99.9;
//       you=10.5;
        //众筹进度
        progress_crowdfunding.setMax(100);
//        int percent = (int)(((float)you / (float)price) * 100);
        int percent = (int)(((float)you / (float)zong) * 100);
        progress_crowdfunding.setProgress(percent);
        double cha=zong-you;
        progress_text.setText("￥"+cha);

    }

    /**初始化各个平台注册信息*/
    private void initRegistInfo() {
        // TODO Auto-generated method stub
        MyLogs.e("haifeng","进入UI分享，初始化各个平台注册信息");
        map_wxcircle = new HashMap<String, Object>();
        map_wxFriend = new HashMap<String, Object>();

        map_wxcircle.put("AppId", ShareConfig.APPID_CIRCLE_FRIEND);
        map_wxcircle.put("AppSecret", ShareConfig.APPSECRET_CIRCLE_FRIEND);
        map_wxcircle.put("Enable", ShareConfig.ENABLE_CIRCLE_FRIEND);
        map_wxcircle.put("BypassApproval", ShareConfig.BYPASSAPPROVAL_CIRCLE_FRIEND);
        map_wxcircle.put("ShortLinkConversationEnable", "true");
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, map_wxcircle);

        map_wxFriend.put("AppId", ShareConfig.APPID_WXFRIEND);
        map_wxFriend.put("Enable", ShareConfig.ENABLE_WXFRIEND);
        map_wxFriend.put("BypassApproval", ShareConfig.BYPASSAPPROVAL_WXFRIEND);
        map_wxFriend.put("ShortLinkConversationEnable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, map_wxFriend);
        MyLogs.e("haifeng","进入UI分享，初始化各个平台注册信息,结束");
    }

    /**分享到微信好友 */
    private void share_WxFriend() {
        if (!CommanUtil.isWXAppInstalledAndSupported()) {
            TempSingleToast.showToast(App.getAppContext(),"请先安装微信");
            return;
        }
        Platform platform_wxFriend = ShareSDK.getPlatform(this, Wechat.NAME);//微信好友分享对象
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(share_title);
        sp.setText(share_text);
        sp.setUrl(share_url);
        sp.setImageData(null);
        sp.setImageUrl(share_image);
        sp.setImagePath(null);

        platform_wxFriend.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        platform_wxFriend.share(sp);
    }

    /**分享到朋友圈*/
    private void share_CircleFriend() {
        if (!CommanUtil.isWXAppInstalledAndSupported()) {
            TempSingleToast.showToast(App.getAppContext(),"请先安装微信");
            return;
        }
        Platform platform_circle = ShareSDK.getPlatform(this, WechatMoments.NAME);
        cn.sharesdk.wechat.moments.WechatMoments.ShareParams sp = new cn.sharesdk.wechat.moments.WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(share_title);
        sp.setText(share_text);
        sp.setImageUrl(share_image);
        sp.setImagePath(null);
        sp.setUrl(share_url);

        platform_circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        platform_circle.share(sp);
    }

    /**shareSDK---UI可控 实现的接口回调*/
    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        Message msg = new Message();
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    /**分享结果*/
    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            // 1代表分享成功，2代表分享失败，3代表分享取消
            case 1:
                // 成功
                TempSingleToast.showToast(App.getAppContext(),"分享成功");
                break;
            case 2:
                TempSingleToast.showToast(App.getAppContext(),"分享失败");
                break;
            case 3:
                TempSingleToast.showToast(App.getAppContext(),"分享取消");
                break;

        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLogs.e("haifeng","许愿详情onDestroy()");
        // 释放资源空间
        ShareSDK.stopSDK(this.getBaseContext());
//        //关闭软键盘
//        if(getCurrentFocus()!=null)
//        {
//            MyLogs.e("haifeng","关闭键盘");
//            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//                    .hideSoftInputFromWindow(getCurrentFocus()
//                                    .getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
//        }

    }

}
