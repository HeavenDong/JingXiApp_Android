package com.zhuyun.jingxi.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.wxapi.Constants;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jiangxiangfei on 2016/05/18.
 */
public class CommanUtil {
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String[] constellation=
            {"白羊座（3.21-4.19)","金牛座（4.20-5.20)","双子座（5.21-6.21）","巨蟹座（6.22-7.22）","狮子座（7.23-8.22）","处女座（8.23-9.22）",
                    "天枰座（9.23-10.23）","天蝎座（10.24-11.22）","射手座（11.23-12.21）","摩羯座（12.22-1.19）","水平座（1.20-2.18）","双鱼座（2.19-3.20）"};

    /**
     *   MD5加密
     */
    public static String md5Hex(String planText, boolean toLowerCase) {
        try {
            byte[] data = MessageDigest.getInstance("MD5").digest(planText.getBytes("UTF-8"));
            final int l = data.length;
            final char[] out = new char[l << 1];

            char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
            // two characters form the hex value.
            for (int i = 0, j = 0; i < l; i++) {
                out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
                out[j++] = toDigits[0x0F & data[i]];
            }
            return new String(out);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ignored) {

        }
        return null;
    }

    /**
     * 验证输入手机号码---正则验证
     */
    public static boolean isMobilePhone(String str) {
        String regex = "^(((1[3,4,5,6,7,8][0-9]{1})|170)+\\d{8})$";
        return match(regex, str);
    }
    /**
     * 验证输入密码---正则验证
     */
    public static boolean isPSW(String str) {
        String regex = "^[0-9A-Za-z]{6,20}$";
        return match(regex, str);
    }
    /**
     * 验证输入数字---正则验证
     */
    public static boolean isCount(String str) {
        String regex = "^[0-9]*$";
        return match(regex, str);
    }
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     *  星座
     * */
    public static String getConstellation(int index){
        return constellation[index];
    }


    /**
     * 检查当前网络是否可用：
     * return  boolean
     */
    public static boolean isNetworkAvailable()
    {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
            }

//            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
//
//            if (networkInfo != null && networkInfo.length > 0)
//            {
//                for (int i = 0; i < networkInfo.length; i++)
//                {
//                    System.out.println(i + "===状态===" + networkInfo[i].getState());
//                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
//                    // 判断当前网络状态是否为连接状态
//                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
//                    {
//                        return true;
//                    }
//                }
//            }
        }
        return false;
    }


    /**
    * 转化时间
    * long l = Long.parseLong(String);
    * */
    public static String transhms(long progress, String rule){
        SimpleDateFormat formatter = new SimpleDateFormat(rule);//初始化Formatter的转换格式。

        String ms = formatter.format(progress);
        return ms;
    }


    /**
     * 在主线程执行任务
     *
     * @param r
     */
    public static void runOnUIThread(Runnable r) {

        App.getAppInsatnce().getHandler().post(r);
    }
   /**
    *  检测微信是否安装
    * */
    public static boolean isWXAppInstalledAndSupported() {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(App.getAppContext(), null);
        msgApi.registerApp(Constants.APP_ID);
        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();
        return sIsWXAppInstalledAndSupported;
    }

}
