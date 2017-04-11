package com.zhuyun.jingxi.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.NetUtils;
import com.zhuyun.jingxi.android.fragment.GiftFragment;
import com.zhuyun.jingxi.android.fragment.HomeFragment;
import com.zhuyun.jingxi.android.fragment.IMFragment2;
import com.zhuyun.jingxi.android.fragment.MeFragment;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.huanxin.ui.BaseActivity;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;


/**
 * Created by jiangxiangfei on 2016/5/23.
 * 主activity 不继承baseActivity
 *
 * 但是注意，系统销毁前不一定能保存所有用户希望保存的数据
 系统提供onSaveInstanceState()方法给用户保存额外的数据
 提供onRestoreInstanceState()方法用于重建时恢复
 但是上述方法仅仅保存少量键值对数据，无法保留大量数据
 系统提供onRetainNonConfigurationInstance()用于保留大量的数据
 提供getLastNonConfigurationInstance()用户恢复数据
 注意这个方法不能传递和context有关的东西，比如view、adapter等等，否则会泄露内存
 只能保存自定义的MyDataObject
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final long mBackPressThreshold = 2000;
    private long mLastPress;
    //记录fragment切换的位置-1表示没有选择
    private int position=-1;
    //用来记录点击位置的
    private int currentPosition;
    private String[] tags;
    private LinearLayout[] linearLayouts;
    private ImageView[] imageViews;
    private TextView[] textViews;
    private Fragment[] fragments;
    private Fragment homeFragment;
    private Fragment contentFragment;
    private Fragment connectFragment;
    private Fragment giftFragment;
    private Fragment mallFragment;

    //点击前的图片
    private int imageNormal[] = new int[]{
            R.drawable.aijingxi_2,R.drawable.lipin_2,R.drawable.xiaoxi_2,R.drawable.wo_2
    };
    //点击以后图片
    private int imagePressed[] = new int[]{
            R.drawable.aijingxi, R.drawable.lipin, R.drawable.xiaoxi,R.drawable.wo
    };

    private BroadcastReceiver TransactionFragmentBroadcastReceiver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int index=intent.getIntExtra("index",0);
            Log.i("haifeng","广播接到，index="+index);
            switch (index){
                case 0:

                    break;
                case 1:
                    currentPosition=index;
                    selectItem(currentPosition);
                    break;
            }
        }
    };

    private boolean isCurrentAccountRemoved = false;
    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLogs.e("haifeng","mainactivity执行oncreat方法");
        MyLogs.e("haifeng","环信登录状态="+ DemoHelper.getInstance().isLoggedIn());
       long userId = (long) SharedPreUtils.get(App.getAppContext(), "user_id", (long)0);

//        EaseUser user = new EaseUser(CommanUtil.md5Hex(userId+"",true)+userId);
//        user.setAvatar("http://www.99aijingxi.com:8080/aijingxiimage/wish/friend.png");
//        user.setNick(SharedPreUtils.get(App.getAppContext(), "nick_name","未填写").toString());
//        DemoHelper.getInstance().saveContact(user);
        DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
        MyLogs.e("haifeng","环信登录,我的信息****************************");
        Log.e("haifeng","nick="+ EaseUserUtils.getUserInfo(CommanUtil.md5Hex(userId+"",true)+userId).getNick());
        Log.e("haifeng","Avatar="+EaseUserUtils.getUserInfo(CommanUtil.md5Hex(userId+"",true)+userId).getAvatar());
        Log.e("haifeng","Username="+EaseUserUtils.getUserInfo(CommanUtil.md5Hex(userId+"",true)+userId).getUsername());

        //显示碎片的初始化
        currentPosition=0;
//        currentPosition=1;
        setContentLayout();
        initView();
        initIM();
    }

    private void initIM() {
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    private void setContentLayout() {
        setContentView(R.layout.activity_main);
    }

    private void initView() {
        //注册广播
        IntentFilter filter=new IntentFilter();
        filter.addAction("transactionMainFragment");
        registerReceiver(TransactionFragmentBroadcastReceiver, filter);


        linearLayouts=new LinearLayout[4];
        imageViews=new ImageView[4];
        textViews=new TextView[4];

        //爱惊喜
        LinearLayout main_bottom_home_ll= (LinearLayout) findViewById(R.id.main_bottom_home_ll);
        main_bottom_home_ll.setOnClickListener(this);
        ImageView main_bottom_home_iv= (ImageView) findViewById(R.id.main_bottom_home_iv);
        TextView main_bottom_home_tv= (TextView) findViewById(R.id.main_bottom_home_tv);
        linearLayouts[0]=main_bottom_home_ll;
        imageViews[0]=main_bottom_home_iv;
        textViews[0]=main_bottom_home_tv;
        //直播
//        LinearLayout main_bottom_content_ll= (LinearLayout) findViewById(R.id.main_bottom_content_ll);
//        main_bottom_content_ll.setOnClickListener(this);
//        ImageView main_bottom_content_iv= (ImageView) findViewById(R.id.main_bottom_content_iv);
//        TextView main_bottom_content_tv= (TextView) findViewById(R.id.main_bottom_content_tv);
//        linearLayouts[1]=main_bottom_content_ll;
//        imageViews[1]=main_bottom_content_iv;
//        textViews[1]=main_bottom_content_tv;
        //礼品屋
        LinearLayout main_bottom_connect_ll= (LinearLayout) findViewById(R.id.main_bottom_connect_ll);
        main_bottom_connect_ll.setOnClickListener(this);
        ImageView main_bottom_connect_iv= (ImageView) findViewById(R.id.main_bottom_connect_iv);
        TextView main_bottom_connect_tv= (TextView) findViewById(R.id.main_bottom_connect_tv);
        linearLayouts[1]=main_bottom_connect_ll;
        imageViews[1]=main_bottom_connect_iv;
        textViews[1]=main_bottom_connect_tv;
        //消息
        LinearLayout main_bottom_schedule_ll= (LinearLayout) findViewById(R.id.main_bottom_schedule_ll);
        main_bottom_schedule_ll.setOnClickListener(this);
        ImageView main_bottom_schedule_iv= (ImageView) findViewById(R.id.main_bottom_schedule_iv);
        TextView main_bottom_schedule_tv= (TextView) findViewById(R.id.main_bottom_schedule_tv);
        linearLayouts[2]=main_bottom_schedule_ll;
        imageViews[2]=main_bottom_schedule_iv;
        textViews[2]=main_bottom_schedule_tv;
        //我
        LinearLayout main_bottom_mall_ll= (LinearLayout) findViewById(R.id.main_bottom_mall_ll);
        main_bottom_mall_ll.setOnClickListener(this);
        ImageView main_bottom_mall_iv= (ImageView) findViewById(R.id.main_bottom_mall_iv);
        TextView main_bottom_mall_tv= (TextView) findViewById(R.id.main_bottom_mall_tv);
        linearLayouts[3]=main_bottom_mall_ll;
        imageViews[3]=main_bottom_mall_iv;
        textViews[3]=main_bottom_mall_tv;


        //标记fragment
        tags=new String[]{"home","connect","schedule","mall"};
        fragments=new Fragment[4];
        homeFragment=getSupportFragmentManager().findFragmentByTag("home");
        if (homeFragment==null){
            MyLogs.e("jxf","homeFragment为null，new");
            homeFragment=new HomeFragment();
        }
        fragments[0]=homeFragment;

//        contentFragment=getSupportFragmentManager().findFragmentByTag("content");
//        if (contentFragment==null){
//            MyLogs.e("jxf","contentFragment为null，new");
//            contentFragment=new PPstreamFragment();
//        }
//        fragments[1]=contentFragment;

        connectFragment=getSupportFragmentManager().findFragmentByTag("connect");
        if (connectFragment==null){
            MyLogs.e("jxf","connectFragment为null，new");
            connectFragment=new GiftFragment();
        }
        fragments[1]=connectFragment;

        giftFragment=getSupportFragmentManager().findFragmentByTag("schedule");
        if (giftFragment==null){
            MyLogs.e("jxf","scheduleFragment为null，new");
            // TODO: 2016/8/2  
            giftFragment=new IMFragment2();
        }
        fragments[2]=giftFragment;

        mallFragment=getSupportFragmentManager().findFragmentByTag("mall");
        if (mallFragment==null){
            MyLogs.e("jxf","mallFragment为null，new");
            mallFragment=new MeFragment();
        }
        fragments[3]=mallFragment;
        selectItem(currentPosition);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_bottom_home_ll:
                currentPosition=0;
                selectItem(currentPosition);
                break;
//            case R.id.main_bottom_content_ll:
//                currentPosition=1;
//                selectItem(currentPosition);
//                break;
            case R.id.main_bottom_connect_ll:
                currentPosition=1;
                selectItem(currentPosition);
                break;
            case R.id.main_bottom_schedule_ll:
                currentPosition=2;
                selectItem(currentPosition);
                break;
            case R.id.main_bottom_mall_ll:
                currentPosition=3;
                selectItem(currentPosition);
                break;
        }
    }

    private void selectItem(int currentPosition){
        MyLogs.e("haifeng","当前坐标"+currentPosition);
        for (int i = 0; i < 4; i++) {
            if (i == currentPosition) {
                textViews[i].setTextColor(getResources().getColor(R.color.title_color));
                imageViews[i].setBackgroundResource(imagePressed[i]);
            } else {
                textViews[i].setTextColor(getResources().getColor(R.color.grey));
                imageViews[i].setBackgroundResource(imageNormal[i]);
            }
        }
        MyLogs.e("jxf","selectItem()方法执行：传进来的currentpostion==="+currentPosition);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (position!=currentPosition){
            position=currentPosition;
            if (!fragments[position].isAdded()){
                MyLogs.e("jxf","此fragment没有被添加");
                transaction.add(R.id.main_frame_fl,fragments[position],tags[currentPosition]);
                transaction.show(fragments[position]);
            }
            else {
                MyLogs.e("jxf","此fragment被添加过");
                if (fragments[position].isHidden()){
                    MyLogs.e("jxf","此fragment被添加过,但是被隐藏了");
                    transaction.show(fragments[position]);
                }
            }
            MyLogs.e("jxf","其他fragment隐藏");
            for (int i=0;i<fragments.length;i++){
                MyLogs.e("jxf","隐藏执行打印：i"+i);
                if (i!=currentPosition){
                    if (fragments[i].isAdded()){
                        MyLogs.e("jxf","此i"+i+"::::fragment被添加过,隐藏其他的时候需要隐藏他");
                        transaction.hide(fragments[i]);
                    }
                }
            }
            transaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        Toast pressBackToast = Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT);

        if (Math.abs(currentTime - mLastPress) > mBackPressThreshold) {
            pressBackToast.show();
            mLastPress = currentTime;
        } else {
            pressBackToast.cancel();

            finish();
        }
    }

    //内存回收的时候的数据保存:fragment也会被保存,注释掉super就是不保存fragment实例
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (TransactionFragmentBroadcastReceiver!=null){
            unregisterReceiver(TransactionFragmentBroadcastReceiver);
        }
//        if (App.getAppInsatnce().getHandler()!=null){
//            App.getAppInsatnce().getHandler().removeCallbacksAndMessages(null);
//            MyLogs.e("haifeng","关闭mHandler");
//        }
        MyLogs.e("haifeng","mainactivity界面被销毁，执行ondestory");
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        MyLogs.e("haifeng","显示帐号已经被移除");
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        MyLogs.e("haifeng","显示帐号在其他设备登录");
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器
                            MyLogs.e("haifeng","连接不到聊天服务器");
                        }else {
                            //当前网络不可用，请检查网络设置
                            MyLogs.e("haifeng","当前网络不可用，请检查网络设置");
                        }
                    }
                }
            });
        }
    }
}
