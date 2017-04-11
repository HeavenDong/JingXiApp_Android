package com.zhuyun.jingxi.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.manager.DBManager;
import com.zhuyun.jingxi.android.utils.MyLogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//
//import com.hyphenate.chat.EMOptions;
//import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by jiangxiangfei on 2016/5/17.
 * 需要做的：全局的上下文：能使用全局的位置，尽量使用全局的位置
 * 对异常的捕捉
 *  implements Thread.UncaughtExceptionHandler
 *
 */
public class App extends Application implements Thread.UncaughtExceptionHandler{
    private static App instance;
    //底层自动的关联db对象
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    public static final String DB_NAME = "donghaifeng.db";
    public static String LOC_DBNAME="locAddressDB";
    private Handler mainHandler;
    private int myPhoneScreenHeight;
    private int myPhoneScreenWidth;
    private static Context appContext;
    private static Picasso mPicasso;
    private String httpUserAgent;
    private ArrayList<Activity> activitrys;
    private static DBManager dbManager;
    public static App getAppInsatnce(){
        return instance;
    }
    public static DBManager getDBManager(){
        return dbManager;
    }
    public static Picasso getPicasso() {
        if (mPicasso == null) {
            mPicasso = Picasso.with(appContext);
        }
        return mPicasso;
    }
    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        MyLogs.e("haifeng", "App onCreate");
        instance = this;
        appContext = getApplicationContext();
        activitrys = new ArrayList<>();
        mainHandler = new Handler();

        initLocationDb();
        setupDatabase();
        dbManager = DBManager.getDBManager();
        myPhoneScreenHeight = getResources().getDisplayMetrics().heightPixels;//屏幕高
        myPhoneScreenWidth = getResources().getDisplayMetrics().widthPixels;//屏幕宽
        getHttpHeadAgent();
        Thread.currentThread().setUncaughtExceptionHandler(this);

        //init demo helper
        DemoHelper.getInstance().init(appContext);
//        //red packet code : 初始化红包上下文，开启日志输出开关
//        RedPacket.getInstance().initContext(appContext);
//        RedPacket.getInstance().setDebugMode(true);
//        //end of red packet code
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper=  new DaoMaster.DevOpenHelper(appContext,DB_NAME,null);
        daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession=  daoMaster.newSession();
        MyLogs.e("haifeng", "new daoSession");
    }
    //头信息
    private void getHttpHeadAgent() {
        String pkName = appContext.getPackageName();
        String versionName=null;
        int versionCode=0;
        try {
            versionName = appContext.getPackageManager().getPackageInfo(pkName,0).versionName;
            versionCode = appContext.getPackageManager().getPackageInfo(pkName,0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        WebView mwebView = new WebView(appContext);
        WebSettings set = mwebView.getSettings();
        String userinfo=set.getUserAgentString();
        String encoding=System.getProperty("file.encoding");
        String language=System.getProperty("user.language");
        String region=System.getProperty("user.region");
        String company=android.os.Build.MANUFACTURER;
        httpUserAgent="pkName:"+pkName+"versionName:"+versionName+"versionCode"+versionCode+"language:"+language+"region"+region+"encoding"+encoding+"company"+company+"browser"+userinfo;
        MyLogs.e("haifeng", "app中得到请求携带头部信息useragent=" + httpUserAgent);
    }

    //错误日志收集
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            PrintStream printStream=new PrintStream(appContext.getFilesDir().getAbsolutePath()+"/errorlog.txt");
            String currentTime= CommanUtil.transhms(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
            printStream.println("TIME:"+currentTime);
            printStream.println("PHONEINFOR:"+httpUserAgent);
            printStream.println("以下是错误信息===========");
            ex.printStackTrace(printStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            MyLogs.e("jxf","app中捕获全局错误日志生成文件异常了");
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //初始本地数据库
    private void initLocationDb() {
        File saveFile=new File(appContext.getFilesDir().getPath(),LOC_DBNAME);
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream is = appContext.getResources().openRawResource(R.raw.jingxi);
            FileOutputStream fos = null;
            try {
                boolean exist=saveFile.exists();
                fos = new FileOutputStream(saveFile);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {//数据库缓存本地
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  DaoSession getDaoSession(){
        return daoSession;
    }

    public Handler getHandler(){
        return mainHandler;
    }


    public int getPhoneScreenHeight(){
        return myPhoneScreenHeight;
    }

    public int getPhoneScreenWidth(){
        return myPhoneScreenWidth;
    }


    public static Context getAppContext(){
        return appContext;
    }

    public String getHttpUserAgent(){
        return httpUserAgent;
    }







//
//
//    public void addActivity(Activity activity){
//        if (activity!=null){
//            if (cheakActivityIsExist(activity)){
//                removeActivityFromList(activity);
//            }
//            activitrys.add(activity);
//        }
//    }
//
//    private boolean cheakActivityIsExist(Activity activity){
//
//        return activitrys.contains(activity);
//    }
//
//    public void removeActivityFromList(Activity activity){
//        if (activitrys!=null){
//            boolean result=activitrys.remove(activity);
//            MyLogs.e("jxf","打印移除activity的结果"+result);
//        }
//    }
//    private void removeAllActivityAndExit(){
//        if (activitrys!=null){
//            for (Activity activity:activitrys){
//                if (activity!=null){
//                    activity.finish();
//                }
//            }
//
//            myApplicationOnDestory();
//            System.exit(0);
//        }
//    }
//
//    private void myApplicationOnDestory() {
//        //一些需要回收的东西：列入数据库db对象
//    }


}
