package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.MainActivity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user0081 on 2016/6/20.
 */
public class EnterActivity extends BaseActivity {
    private ImageView imageView;
    private Timer mTimer;
    private long userId;
    private int time =3;
    private TextView tvTime;
    public static String ONEKEY = "";
    public static String ONEVALUE = "";
    private static final int sleepTime = 2000;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (time == 0) {
                        mTimer.cancel();
                        if (userId!=0){
                            if (DemoHelper.getInstance().isLoggedIn()) {
                                MyLogs.e("haifeng","环信已登录状态");
                                long start = System.currentTimeMillis();
                                startActivity(new Intent(EnterActivity.this, MainActivity.class));
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                finish();
                            }else {
                                MyLogs.e("haifeng","环信未登录状态");
                                startActivity(new Intent(EnterActivity.this, MainActivity.class));
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                finish();
                            }
                        }else {
                            Intent intent = new Intent(EnterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        tvTime.setText(+time + " s");
                        time--;
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void initView() {
          userId = (long) SharedPreUtils.get(App.getAppContext(), "user_id", (long)0);
          MyLogs.e("haifeng","本地存user_id="+userId);
          MyLogs.e("haifeng","环信登录状态="+DemoHelper.getInstance().isLoggedIn());
            tvTime = (TextView) findViewById(R.id.time);
            imageView = (ImageView) findViewById(R.id.enter_img_tow);
//            initTime();
            new Thread(new Runnable() {
            public void run() {
                if (DemoHelper.getInstance().isLoggedIn()&&userId!=0) {
                    MyLogs.e("haifeng","环信已登录状态");
                    // auto login mode, make sure all group and conversation is loaed before enter the main screen
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    long costTime = System.currentTimeMillis() - start;
                    //wait
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //enter main screen
                    startActivity(new Intent(EnterActivity.this, MainActivity.class));
                    finish();
                }else {
                    MyLogs.e("haifeng","环信未登录状态");
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(EnterActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).start();





//        RequestParams params = new RequestParams();
//        NetClient.post("http://192.168.0.81:8081/jingxi_server_2.3/user/requestCheck", params, new NetResponseHandler() {
//            @Override
//            public void onResponse(String json) {
//                try {
//                    JSONObject jsonObject=new JSONObject(json);
//                    String key=jsonObject.getString("key");
//                    String value=jsonObject.getString("value");
//                    ONEKEY=key;
//                    ONEVALUE=value;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private void initTime() {
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        };
        mTimer.schedule(task, 0, 1000);

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.layout_enter_activity);
    }

    @Override
    protected void onClickEvent(View view) {

    }
}
