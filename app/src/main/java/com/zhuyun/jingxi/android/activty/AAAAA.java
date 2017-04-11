package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.utils.MyLogs;


/**
 * Created by user0081 on 2016/6/30.
 */
public class AAAAA extends BaseActivity {
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入测试");
        ImageView text_img= (ImageView) findViewById(R.id.text_img);
//        Picasso.with(App.getAppContext()).load("http://sta.273.com.cn/app/mbs/img/mobile_default.png").resize(90,90).centerCrop().placeholder(R.drawable.kongtu)
//                .error(R.drawable.kongtu).into(text_img);

        findViewById(R.id.butt1).setOnClickListener(this);
        findViewById(R.id.butt21).setOnClickListener(this);
        findViewById(R.id.butt22).setOnClickListener(this);
        findViewById(R.id.butt23).setOnClickListener(this);
        findViewById(R.id.butt24).setOnClickListener(this);

        findViewById(R.id.butt3).setOnClickListener(this);
        findViewById(R.id.butt4).setOnClickListener(this);
        findViewById(R.id.butt5).setOnClickListener(this);
        findViewById(R.id.butt6).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
         setContentView(R.layout.aaaaaa);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.butt1:
                //实验跳转页面回收的JumpActivity
                Intent intent=new Intent(AAAAA.this,JumpActivity.class);
                startActivity(intent);
                break;
            case R.id.butt21:

            case R.id.butt22:
                //一键分享sahre
//                TextwechatShare(0);

            case R.id.butt23:
                //一键分享sahre
//                WebwechatShare(1);//分享到微信朋友圈

            case R.id.butt24:
                //一键分享sahre
//                TextwechatShare(1);

                break;
            case R.id.butt3:
////                //高德地图
//                Intent intent3=new Intent(AAAAA.this,GaoDeMapActivity.class);
//                 startActivity(intent3);
                //环信
//                Intent intent=new Intent(getActivity(),HuanXinActivity.class);
//                getActivity().startActivity(intent);
                break;
            case R.id.butt4:
//                showShare();
                break;
            case R.id.butt5:
                Intent intent2=new Intent(AAAAA.this,MySharedSDKActivity.class);
                startActivity(intent2);
                break;
            case R.id.butt6:


                break;
        }
    }
    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
//    private void WebwechatShare(int flag){
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = "http://apk.hiapk.com/appinfo/com.zhuyun.jingxi.android";//这里填写链接url;
//        WXMediaMessage msg = new WXMediaMessage(webpage);
////        msg.title = "爱惊喜";//这里填写标题;
//        msg.description = "我再爱惊喜发布了许愿（这只是测试，哥几个别当真）_web但是看了发哈舒服就快乐哈手机发来看发大水看见了放假克拉斯发手机看了对方尽快啦圣诞节快乐飞爱抚困了就睡的风景克拉斯啊伺服电机卡就索科洛夫";//这里填写内容;
//        //这里替换一张自己工程里的图片资源
//        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//        msg.setThumbImage(thumb);
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = String.valueOf(System.currentTimeMillis());
//        req.message = msg;
//        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
//        App.getIWXAPI().sendReq(req);
//    }
//
//    private void TextwechatShare(int flag){
//        String text="我测试一下分享文字功能";
//        // 初始化一个WXTextObject对象
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = text;
//        // 用WXTextObject对象初始化一个WXMediaMessage对象
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObj;
//        // 发送文本类型的消息时，title字段不起作用
//        // msg.title = "Will be ignored";
//        msg.description = text;
//
//        // 构造一个Req
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction(text); // transaction字段用于唯一标识一个请求
//        req.message = msg;
//        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
//
//        // 调用api接口发送数据到微信
//        App.getIWXAPI().sendReq(req);
//    }
//    private String buildTransaction(final String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
//    }


//    /**shareSDK*/
//    private void showShare() {
//        MyLogs.e("haifeng","进入一键分享了");
//        ShareSDK.initSDK(this);
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
////        oks.setTitle(getString(R.string.share));
//        oks.setTitle("分享爱惊喜测试");
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
//
//// 启动分享GUI
//        oks.show(this);
//    }
}
