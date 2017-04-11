package com.zhuyun.jingxi.android.activty;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.zhuyun.jingxi.android.MainActivity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.utils.MyLogs;

/**
 * Created by jiangxiangfei on 2016/5/17.
 * notice：此activity因为播放视频的原因需要保持屏幕常亮：不再继承baseactivity
 * 此类为启动页：app入口需要全屏：在清单中设置了
 * 此类因为只是启动页：不需要设置启动模式和对内存回收情况的考虑
 */
public class SplashActivity extends Activity implements View.OnClickListener{
    //handler用于发送延迟消息等待显示登录和注册的
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    MyLogs.e("jxf","splash页handler收到延迟消息");
                    activity_splash_loginandregist_group.setVisibility(View.VISIBLE);
                    break;
            }

        }
    };


    private ImageView activity_splash_im;
    private VideoView activty_splash_vv;
    private LinearLayout activity_splash_loginandregist_group;
    private TextView activity_splash_login_tv;
    private TextView activity_splash_regist_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_splash);
        activity_splash_im= (ImageView) findViewById(R.id.activity_splash_im);
        activty_splash_vv= (VideoView) findViewById(R.id.activty_splash_vv);
        activity_splash_loginandregist_group= (LinearLayout) findViewById(R.id.activity_splash_loginandregist_group);
        activity_splash_login_tv= (TextView) findViewById(R.id.activity_splash_login_tv);
        activity_splash_regist_tv= (TextView) findViewById(R.id.activity_splash_regist_tv);
        initState();
    }

    private void initState() {
        Message message=new Message();
        message.what=0;
        MyLogs.e("jxf","splash页handler发送延迟消息5000");
        handler.sendMessageDelayed(message,5000);
        activity_splash_im.setVisibility(View.GONE);
        activity_splash_im.setOnClickListener(this);

        activity_splash_loginandregist_group.setVisibility(View.GONE);
        activity_splash_login_tv.setOnClickListener(this);
        activity_splash_regist_tv.setOnClickListener(this);

//        activty_splash_vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_vedio2));
        activty_splash_vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                MyLogs.e("jxf","splash视频播放完成，按钮显示");
                    activity_splash_im.setVisibility(View.VISIBLE);
            }
        });
        MyLogs.e("jxf","splash视频开始准备");
        activty_splash_vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                MyLogs.e("jxf","splash视频准备完成，开始播放");
                activty_splash_vv.start();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_splash_im:
                MyLogs.e("jxf","splash页播放按钮显示点击，隐藏按钮+从头播放");
                activity_splash_im.setVisibility(View.GONE);
                activty_splash_vv.start();
                break;
            case R.id.activity_splash_login_tv:
                MyLogs.e("jxf","splash页点击登录按钮，进行跳转");
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_splash_regist_tv:
                MyLogs.e("jxf","splash页点击注册按钮，进行跳转");
                Intent intent1=new Intent(this,MainActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activty_splash_vv!=null){
            activty_splash_vv.stopPlayback();
            activty_splash_vv=null;
            MyLogs.e("jxf","splash页destroy:videoview释放");
        }
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
            MyLogs.e("jxf","splash页destroy:移除所有信息");
        }
    }
}
