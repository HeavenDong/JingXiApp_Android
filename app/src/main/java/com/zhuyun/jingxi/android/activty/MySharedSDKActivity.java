package com.zhuyun.jingxi.android.activty;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.tools.utils.UIHandler;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.ShareConfig;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by user0081 on 2016/7/13.
 */
public class MySharedSDKActivity extends Activity implements View.OnClickListener, PlatformActionListener,
        Callback {

    public static String  EXTRA_BIG_TITLE = "big_title";

    public static String  EXTRA_LITTLE_TITLE = "little_title";

    public static String EXTRA_CAR_ID="id";

    public static String EXTRA_CAR_IMAGE="image";


    /** 分享的图片显示 */
    private ImageView imageView_share_car;

    /** 分享的文字显示 */
    private TextView textView_share_car;

    /** 退出分享按钮 */
    private Button share_cancel;

    /** 分享到朋友圈按钮 */
    private LinearLayout share_circleFriend;

    /** 分享到微信好友按钮 */
    private LinearLayout share_wxFriend;

//    /** 分享到QQ空间按钮 */
//    private LinearLayout share_qzone;
//
//    /** 分享到QQ好友按钮 */
//    private LinearLayout share_qqFriend;
//
//    /** 分享到短信按钮 */
//    private LinearLayout share_shortMessage;
//
//    /** 分享到新浪微博按钮 */
//    private LinearLayout share_sinaWeibo;

    /** 分享的标题部分 */
    private String share_title;

    /** 分享的文字内容部分 */
    private String share_text;

    /** 分享的图片部分 */
    private String share_image;

    /** 分享的网址部分 */
    private String share_url;

    /** 分享无图标记 */
    public static final String NOIMAGE = "noimage";

    /** 记录当前分享点击状态 */
    private boolean isClick = false;

//    /** 短信分享按钮是否被点击 */
//    private boolean isSina = false;

    /** 新浪分享等待dilog */
    private ProgressDialog dialog;

    /** 朋友圈注册数据 */
    private HashMap<String, Object> map_circle;

    /** 微信好友注册数据 */
    private HashMap<String, Object> map_wxFriend;

//    /** QQ空间注册数据 */
//    private HashMap<String, Object> map_qzone;
//
//    /** QQ好友注册数据 */
//    private HashMap<String, Object> map_qqFriend;
//
//    /** 短信注册数据 */
//    private HashMap<String, Object> map_shortMessage;
//
//    /** 新浪微博注册数据 */
//    private HashMap<String, Object> map_sina;

    /** 朋友圈分享对象 */
    private Platform platform_circle;

    /** 微信好友分享对象 */
    private Platform platform_wxFriend;

//    /** QQ空间分享对象 */
//    private Platform platform_qzone;
//
//    /** QQ好友分享对象 */
//    private Platform platform_qqFriend;
//
//    /** 短信分享对象 */
//    private Platform platform_shortMessage;
//
//    /** 新浪分享对象 */
//    private Platform platform_sina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.share_item_dialog);
        MyLogs.e("haifeng","进入UI分享");
        // 组件初始化
        initView();
        // 数据初始化
        initData();
        // 组件添加监听
        initAddOnClickListener();
    }

    /**
     * 定义一个函数将dp转换为像素
     *
     * @param dp
     * @return
     */
    public int Dp2Px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(dp * scale / 2);
    }

    /**
     * @数据初始化
     */
    private void initData() {
        // 初始化sdk分享资源
        ShareSDK.initSDK(this, ShareConfig.APPKEY);
        MyLogs.e("haifeng","进入UI分享，初始化SDK");
        initRegistInfo();
        // 初始化要属相的内容
        share_title = "标题";

//        share_text = getIntent().getStringExtra(EXTRA_LITTLE_TITLE);
//        share_title = getIntent().getStringExtra(EXTRA_BIG_TITLE);
        System.out.println("share_text---->"+share_text);
        System.out.println("share_title---->"+share_title);
//        if (TextUtils.isEmpty(getIntent().getStringExtra(EXTRA_CAR_IMAGE))) {
            // 没有默认图片吗，先用这张图片替代
            share_image = "http://sta.273.com.cn/app/mbs/img/mobile_default.png";
//        } else {
//            share_image = imageUrl(getIntent().getStringExtra(EXTRA_CAR_IMAGE));
//        }
//        System.out.println("share_image---->"+share_image);
//        http://m.273.cn/car/14923159.html?source=zzshare
        share_url ="http://apk.hiapk.com/appinfo/com.zhuyun.jingxi.android";

//        mOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.no_car)// 在ImageView加载过程中显示图片
//                .showImageForEmptyUri(R.drawable.no_car) // image连接地址为空时
//                .showImageOnFail(R.drawable.no_car) // image加载失败
//                .cacheInMemory(true)// 加载图片时会在内存中加载缓存
//                .cacheOnDisc(true).build();
//        ImageLoader.getInstance().displayImage(share_image, imageView_share_car, mOptions, null);

//        Picasso.with(App.getAppContext()).load(share_image).into(imageView_share_car);
        share_image = share_image.replace("110-110", "600-800");
        MyLogs.e("haifeng","进入UI分享，初始化数据");
    }

    private String imageUrl(String imageUrl){

//        int index = 0;
//        if (!TextUtils.isEmpty(imageUrl)) {
//            index = imageUrl.lastIndexOf(".");
//            if (index != -1) {
//                String extendName = imageUrl.substring(index);
//                StringBuilder sb = new StringBuilder();
//                sb.append(imageUrl.substring(0, index));
//                sb.append("_");
//                sb.append("_6_0_0").append(extendName);
//                imageUrl = sb.toString();
//                if (!imageUrl.startsWith("http://")) {
//                    imageUrl = RequestUrl.IMAGE_URL_API + imageUrl;
//                }
//            }
//        } else {
//            imageUrl = "";
//        }
//        return imageUrl;

//        return Utils.formatImgUrl(imageUrl, 512, 0, false);
        return "http://sta.273.com.cn/app/mbs/img/mobile_default.png";
    }

    /**
     * 初始化各个平台注册信息
     */
    private void initRegistInfo() {
        // TODO Auto-generated method stub
        MyLogs.e("haifeng","进入UI分享，初始化各个平台注册信息");
        map_circle = new HashMap<String, Object>();
        map_wxFriend = new HashMap<String, Object>();
//        map_qzone = new HashMap<String, Object>();
//        map_qqFriend = new HashMap<String, Object>();
//        map_shortMessage = new HashMap<String, Object>();
//        map_sina = new HashMap<String, Object>();

        map_circle.put("AppId", ShareConfig.APPID_CIRCLE_FRIEND);
        map_circle.put("AppSecret", ShareConfig.APPSECRET_CIRCLE_FRIEND);
        map_circle.put("Enable", ShareConfig.ENABLE_CIRCLE_FRIEND);
        map_circle.put("BypassApproval", ShareConfig.BYPASSAPPROVAL_CIRCLE_FRIEND);
        map_circle.put("ShortLinkConversationEnable", "true");
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, map_circle);

        map_wxFriend.put("AppId", ShareConfig.APPID_WXFRIEND);
        map_wxFriend.put("Enable", ShareConfig.ENABLE_WXFRIEND);
        map_wxFriend.put("BypassApproval", ShareConfig.BYPASSAPPROVAL_WXFRIEND);
        map_wxFriend.put("ShortLinkConversationEnable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, map_wxFriend);
        MyLogs.e("haifeng","进入UI分享，初始化各个平台注册信息,结束");
//        map_qzone.put("AppId", ShareConfig.APPID_QZONE);
//        map_qzone.put("AppKey", ShareConfig.APPKEY_QZONE);
//        map_qzone.put("ShareByAppClient", ShareConfig.SHAREBYAPPCLIENT_QZONE);
//        map_qzone.put("Enable", ShareConfig.ENABLE_QZONE);
//        map_qzone.put("ShortLinkConversationEnable", "true");
//        ShareSDK.setPlatformDevInfo(QZone.NAME, map_qzone);
//
//        map_qqFriend.put("AppId", ShareConfig.APPID_QQFRIEND);
//        map_qqFriend.put("AppKey", ShareConfig.APPKEY_QQFRIEND);
//        map_qqFriend.put("Enable", ShareConfig.ENABLE_QQFRIEND);
//        map_qqFriend.put("ShortLinkConversationEnable", "true");
//        ShareSDK.setPlatformDevInfo(QQ.NAME, map_qqFriend);
//
//        map_shortMessage.put("ShortLinkConversationEnable", "true");
//        ShareSDK.setPlatformDevInfo(ShortMessage.NAME, map_shortMessage);
//
//        map_sina.put("AppKey", ShareConfig.APPKEY_SINA_WEIBO);
//        map_sina.put("AppSecret", ShareConfig.APPSECRET_SINA_WEIBO);
//        map_sina.put("RedirectUrl", ShareConfig.REDIRECTURL_SINA_WEIBO);
//        map_sina.put("ShareByAppClient", ShareConfig.SHAREBYAPPCLIENT_SINA_WEIBO);//true
//        map_sina.put("Enable", ShareConfig.ENABLE_SINA_WEIBO);//true
//        map_sina.put("ShortLinkConversationEnable", "true");
//        ShareSDK.setPlatformDevInfo(SinaWeibo.NAME, map_sina);

    }

    /**
     * @组件初始化
     */
    private void initView() {
//        imageView_share_car = (ImageView)findViewById(R.id.imageView_share_car);
//        textView_share_car = (TextView)findViewById(R.id.textView_share_car);
        share_cancel = (Button)findViewById(R.id.share_cancel);
        share_cancel = (Button)findViewById(R.id.share_cancel);
        share_circleFriend = (LinearLayout)findViewById(R.id.linearLayout_ciclefriend);
//        share_qqFriend = (LinearLayout)findViewById(R.id.LinearLayout_qqfriend);
//        share_qzone = (LinearLayout)findViewById(R.id.linearLayout_qzone);
//        share_shortMessage = (LinearLayout)findViewById(R.id.LinearLayout_shortmessage);
//        share_sinaWeibo = (LinearLayout)findViewById(R.id.LinearLayout_sinaweibo);
        share_wxFriend = (LinearLayout)findViewById(R.id.linearLayout_weixin);
        MyLogs.e("haifeng","进入UI分享，初始化控件");
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载。。。。");
        dialog.setCancelable(false);
    }

    /**
     * @组件添加监听
     */
    private void initAddOnClickListener() {
        share_cancel.setOnClickListener(this);
        share_circleFriend.setOnClickListener(this);
//        share_qqFriend.setOnClickListener(this);
//        share_qzone.setOnClickListener(this);
//        share_shortMessage.setOnClickListener(this);
//        share_sinaWeibo.setOnClickListener(this);
        share_wxFriend.setOnClickListener(this);
    }

    /*
     * (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.share_cancel) {
            // 取消
            MySharedSDKActivity.this.finish();
        } else {
            if (!isClick) {// 没点击才能点击
                isClick = true;
                switch (v.getId()) {
                    // 分享到微信朋友圈
                    case R.id.linearLayout_ciclefriend:
                        share_CircleFriend();
                        break;
                    // 分享到微信好友
                    case R.id.linearLayout_weixin:
                        share_WxFriend();
                        break;
//                    // 分享到QQ好友
//                    case R.id.LinearLayout_qqfriend:
//                        share_QQFriend();
//                        break;
//                    //分享到QQ空间
//                    case R.id.linearLayout_qzone:
//                        share_Qzone();
//                        break;
//                    // 分享到短信
//                    case R.id.LinearLayout_shortmessage:
//                        share_ShortMessage();
//                        break;
//                    // 分享到新浪微博
//                    case R.id.LinearLayout_sinaweibo:
//                        share_SinaWeibo();
//                        break;
                }
            }
        }
    }

    /**
     * 分享到朋友圈
     */
    private void share_CircleFriend() {
        if (!CommanUtil.isWXAppInstalledAndSupported()) {
            TempSingleToast.showToast(App.getAppContext(),"请先安装微信");
            isClick = false;
            return;
        }

        platform_circle = ShareSDK.getPlatform(this, WechatMoments.NAME);
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

    /**
     * 分享到微信好友
     */
    private void share_WxFriend() {
        if (!CommanUtil.isWXAppInstalledAndSupported()) {
            TempSingleToast.showToast(App.getAppContext(),"请先安装微信");
            isClick = false;
            return;
        }
        platform_wxFriend = ShareSDK.getPlatform(this, Wechat.NAME);
        ShareParams sp = new ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(share_title);
        sp.setText(share_text);
        sp.setUrl(share_url);
        sp.setImageData(null);
        sp.setImageUrl(share_image);
        sp.setImagePath(null);

        platform_wxFriend.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        platform_wxFriend.share(sp);
    }

//    /**
//     * 分享到QQ空间
//     */
//    private void share_Qzone() {
//        platform_qzone = ShareSDK.getPlatform(this, QZone.NAME);
//        cn.sharesdk.tencent.qzone.QZone.ShareParams sp = new cn.sharesdk.tencent.qzone.QZone.ShareParams();
//        sp.setTitle(share_title);
//        sp.setText(share_text);
//        sp.setTitleUrl(share_url);
//        sp.setImageUrl(share_image);// imageUrl存在的时候，原来的imagePath将被忽略
//        sp.setSite("273二手车");
//        sp.setSiteUrl(share_url);
//
//        platform_qzone.setPlatformActionListener(this); // 设置分享事件回调
//        // 执行图文分享
//        platform_qzone.share(sp);
//    }
//
//    /**
//     * 分享到QQ好友
//     */
//    private void share_QQFriend() {
//        platform_qqFriend = ShareSDK.getPlatform(this, QQ.NAME);
//        cn.sharesdk.tencent.qq.QQ.ShareParams sp = new cn.sharesdk.tencent.qq.QQ.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//        sp.setTitle(share_title);
//        sp.setTitleUrl(share_url);
//        sp.setText(share_text);
//        sp.setImageUrl(share_image);
//        sp.setImagePath(null);
//
//        platform_qqFriend.setPlatformActionListener(this); // 设置分享事件回调
//        // 执行图文分享
//        platform_qqFriend.share(sp);
//    }
//
//    /**
//     * 分享到短信
//     */
//    private void share_ShortMessage() {
//        platform_shortMessage = ShareSDK.getPlatform(this, ShortMessage.NAME);
//        cn.sharesdk.system.text.ShortMessage.ShareParams sp = new cn.sharesdk.system.text.ShortMessage.ShareParams();
//        sp.setAddress("");
//        sp.setText(share_text + share_url);
////        platform_shortMessage.setPlatformActionListener(this); // 设置分享事件回调
//        // 执行图文分享
//        platform_shortMessage.share(sp);
//    }
//
//    /**
//     * 分享到新浪微博
//     */
//    private void share_SinaWeibo() {
//        platform_sina = ShareSDK.getPlatform(SinaWeibo.NAME);
//        System.out.println("platform_sina.isValid()----"+platform_sina.isValid());
//        if (!platform_sina.isValid()) {// 如果有新浪微博客户端，每次都可以重新选择或添加分享账号
//            platform_sina.removeAccount();
//        }
//
//        cn.sharesdk.sina.weibo.SinaWeibo.ShareParams sp = new cn.sharesdk.sina.weibo.SinaWeibo.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);
//        sp.setTitle(share_title);
//        sp.setText(share_text + share_url);
//        sp.setUrl(share_url);
//        sp.setImageUrl(share_image);
//        sp.setImagePath(null);
//        platform_sina.setPlatformActionListener(this); // 设置分享事件回调
//        // 执行图文分享
//        platform_sina.share(sp);
//        isSina = true;
//        dialog.show();

//        一键快分享
//            OnekeyShare oks = new OnekeyShare();
//            //关闭sso授权
//            oks.disableSSOWhenAuthorize();
//
//           // 分享时Notification的图标和文字
//            oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//            oks.setTitle(getString(R.string.share));
//            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//            oks.setTitleUrl("http://sharesdk.cn");
//            // text是分享文本，所有平台都需要这个字段
//            oks.setText("我是分享文本");
//            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//            oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//            // url仅在微信（包括好友和朋友圈）中使用
//            oks.setUrl("http://sharesdk.cn");
//            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//            oks.setComment("我是测试评论文本");
//            // site是分享此内容的网站名称，仅在QQ空间使用
//            oks.setSite(getString(R.string.app_name));
//            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//            oks.setSiteUrl("http://sharesdk.cn");
//
//           // 启动分享GUI
//            oks.show(this);
//
//    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        // 释放资源空间
        ShareSDK.stopSDK(this.getBaseContext());
        super.onDestroy();
    }

    public void onComplete(Platform plat, int action, HashMap<String, Object> res) {

        // 如果是通过微博客户端进行分享，则直接结束此页面
//        if (isSina) {
//            dialog.dismiss();
//        }
// System.out.println("plat.getName()--->"+plat.getName()+"    :"+SinaWeibo.NAME);
//        if (plat.getName().equals(SinaWeibo.NAME) && !platform_sina.isValid()) {
//            finish();
//        } else {
            Message msg = new Message();
            msg.arg1 = 1;
            msg.arg2 = action;
            msg.obj = plat;
            UIHandler.sendMessage(msg, this);
//        }
    }

    public void onCancel(Platform plat, int action) {

//        if (isSina) {
//            dialog.dismiss();
//        }
//        isSina = false;
        Message msg = new Message();
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    public void onError(Platform plat, int action, Throwable t) {

        Message msg = new Message();
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        isClick = false;// 恢复未点击状态
        switch (msg.arg1) {
            // 1代表分享成功，2代表分享失败，3代表分享取消
            case 1:
                // 成功
                TempSingleToast.showToast(App.getAppContext(),"分享成功");
//                if (isSina) {
//                    isSina = false;
//                    MySharedSDKActivity.this.finish();
//                }
                break;
            case 2:
                TempSingleToast.showToast(App.getAppContext(),"分享失败");
                // 失败
//                if (isSina) {
//                    isSina = false;
//                    MySharedSDKActivity.this.finish();
//                }
                break;

        }
        return false;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        if (!isSina) {
//            finish();
//        }
    }

}