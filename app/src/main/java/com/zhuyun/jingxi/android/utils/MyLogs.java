package com.zhuyun.jingxi.android.utils;

import android.util.Log;

/**
 * Created by jiangxiangfei on 2016/5/17.
 */
public class MyLogs {

    public static boolean isLog=true;

    public static void e(String tag, String msg){
        if (isLog){
            Log.e(tag,msg);
        }
    }

}
