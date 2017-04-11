package com.zhuyun.jingxi.android.http;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.iml.CustomGetSyncCallBack;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jiangxiangfei on 2016/5/18.
 *
 *
 * 网络请求必须要在子线程运行：同步：要回归ui线程执行  异步：就是在ui线程执行
 *
 * execute  是同步步执行  子线程开启 结果需要回到ui线程，在ui线程刷新界面的
 *
 *
 *enqueue主线程开启 但结果不再ui线程
 *使用enqueue方法进行网络访问时，OkHttp会在线程池中调用异步任务进行网络访问和回调
 *
 *
 *
 * OkHttp 3.0开始，默认不保存Cookie，要自己使用CookieJar来保存Cookie
 *
 //记录下正确姿势
 OkHttpClient mHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
 private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
 //Tip：這裡key必須是String
 @Override
 public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
 cookieStore.put(url.host(), cookies);
 }
 @Override
 public List<Cookie> loadForRequest(HttpUrl url) {
 List<Cookie> cookies = cookieStore.get(url.host());
 return cookies != null ? cookies : new ArrayList<Cookie>();
 }
 }).build();
 *
 */
public class HttpClient {

    private static OkHttpClient client=new OkHttpClient();

    /*
    * 同步get请求：无头。
    * param：如果有参数：使用本方法的url拼装
    * */
    public static void requestGetSyncNoHeader(String url, CustomGetSyncCallBack callBack)throws IOException{

        Request request=new Request.Builder().url(url).build();
        Call call=client.newCall(request);
        Response response=call.execute();
        callBack.result(response);
    }


    /*
    * 异步get请求：无头。
    * param：如果有参数：使用本方法的url拼装
    * */
    public static void requestGetAsyncNoHeader(String url, final Callback callback){
        Request request=new Request.Builder().url(url).build();
        Call call=client.newCall(request);
        call.enqueue(callback);
    }


    /*
    * 同步get请求：无头。
    * param：如果有参数：使用本方法的url拼装
    * */
    public static void requestGetSyncHeader(String url,CustomGetSyncCallBack callBack)throws IOException{
        String userLoginName = (String) SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_login_name","");
        String password = (String)SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_password","");
        Request request=new Request.Builder().url(url).header("User-Agent", App.getAppInsatnce().getHttpUserAgent()).header("userLoginName", userLoginName).header("passwd",password).build();
        //call:请求封装成了任务:是通过client使用request来得到的
        Call call=client.newCall(request);
        Response response=call.execute();
        callBack.result(response);
    }


    /*
    * 异步get请求：有头。
    * param：如果有参数：使用本方法的url拼装
    * */
    public static void requestGetAsyncHeader(String url,Callback callback){
        String userLoginName = (String) SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_login_name","");
        String password = (String)SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_password","");
        Request request=new Request.Builder().url(url).header("User-Agent", App.getAppInsatnce().getHttpUserAgent()).header("userLoginName", userLoginName).header("passwd",password).build();
        Call call=client.newCall(request);
        call.enqueue(callback);

    }



    /*
    * 以下是为 get请求 拼装url的
    * */
//    private static final String CHARSET_NAME = "UTF-8";
//
//    public static String formatParams(List<BasicNameValuePair> params){
//        return URLEncodedUtils.format(params, CHARSET_NAME);
//    }
//    public static String attachHttpGetParams(String url, List<BasicNameValuePair> params){
//        return url + "?" + formatParams(params);
//    }

    public static String attachHttpGetParam(String url, String name, String value){
        return url + "?" + name + "=" + value;
    }




    /*
    * 同步post提交，无头。
    * param:Map<String,Object> params 为null时表示没有参数
    * param:CustomGetSyncCallBack callBack:自己定义的回调接口，需要实现
    * */
    public static void requestPostSyncNoHeader(String url,Map<String,Object> params,CustomGetSyncCallBack callBack)throws IOException{
        if (params==null){
            params = new HashMap<String,Object>();
        }


        FormBody.Builder bodyBuilder = new FormBody.Builder();
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            bodyBuilder.add(entry.getKey(),entry.getValue().toString());
        }
        FormBody formBody=bodyBuilder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call=client.newCall(request);
        Response response=call.execute();
        callBack.result(response);
    }



    /*
    * 异步post提交，无头。
    * param:Map<String,Object> params 为null时表示没有参数
    * */
    public static void requestPostAsyncNoHeader(String url, Map<String,Object> params, final Callback callback){
        if (params==null){
            params = new HashMap<String,Object>();
        }


        FormBody.Builder bodyBuilder = new FormBody.Builder();
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            bodyBuilder.add(entry.getKey(),entry.getValue().toString());
        }
        FormBody formBody=bodyBuilder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call=client.newCall(request);
        call.enqueue(callback);

    }


    /*
    * 同步post提交，有头。
    * param:Map<String,Object> params 为null时表示没有参数
    * param:CustomGetSyncCallBack callBack:自己定义的回调接口，需要实现
    * */
    public static void requestPostSyncHeader(String url,Map<String,Object> params,CustomGetSyncCallBack callBack)throws IOException{
        if (params==null){
            params = new HashMap<String,Object>();
        }


        FormBody.Builder bodyBuilder = new FormBody.Builder();
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            bodyBuilder.add(entry.getKey(),entry.getValue().toString());
        }
        FormBody formBody=bodyBuilder.build();

        long uerId = (long) SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_id",(long)0);
        String userLoginName = (String) SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_login_name","");
        String password = (String)SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_password","");

        Request request = new Request.Builder().url(url).header("User-Agent", App.getAppInsatnce().getHttpUserAgent()).header("id", String.valueOf(uerId)).header("userLoginName", userLoginName).header("passwd",password).post(formBody).build();
        Call call=client.newCall(request);
        Response response=call.execute();
        callBack.result(response);
    }



    /*
    * 异步post提交，有头:注意获得请求体消息被替代:
    * param:Map<String,Object> params 为null时表示没有参数
    * */
    public static void requestPostAsyncHeader(String url,Map<String,Object> params,Callback callBack)throws IOException{
        if (params==null){
            params = new HashMap<String,Object>();
        }


        FormBody.Builder bodyBuilder = new FormBody.Builder();
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            bodyBuilder.add(entry.getKey(),entry.getValue().toString());
        }
        FormBody formBody=bodyBuilder.build();
        MyLogs.e("haifeng","formBody="+formBody.value(0));
        long uerId = (long) SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_id",(long)0);
        String userLoginName = (String) SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_login_name","");
        String password = (String)SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_password","");
        Request request = new Request.Builder().url(url).header("User-Agent", App.getAppInsatnce().getHttpUserAgent()).header("id", String.valueOf(uerId)).header("userLoginName", userLoginName).header("passwd",password).post(formBody).build();
        Call call=client.newCall(request);
        call.enqueue(callBack);

    }

    /*
    * post上传文件：带头  ：重新设计超时时间
    * notice：可能会出现问题：要注意
    * */
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("image/JPG");

    public static void upLoadFilePostHead(String url, File file){
        /*client.newBuilder().connectTimeout(10000).readTimeout(10000).build();*/
        OkHttpClient copy=client.newBuilder().connectTimeout(20, TimeUnit.SECONDS).writeTimeout(20,TimeUnit.SECONDS).readTimeout(10,TimeUnit.SECONDS).build();
        //超时时间可能不够
        String userLoginName = (String) SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_login_name","");
        String password = (String)SharedPreUtils.get(App.getAppInsatnce().getAppContext(),"user_password","");
        Request request=new Request.Builder().url(url).header("User-Agent", App.getAppInsatnce().getHttpUserAgent()).header("userLoginName", userLoginName).header("passwd",password).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file)).build();
        Call call=copy.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }




    /*
    * 大文件下载:get: 同步get请求：无头。
    * */

    public static void downFile(String url,CustomGetSyncCallBack callBack)throws IOException{
        OkHttpClient copy=client.newBuilder().connectTimeout(20,TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS).writeTimeout(20,TimeUnit.SECONDS).build();
        Request request= new Request.Builder().url(url).build();
        Call call=copy.newCall(request);
        Response response=call.execute();
        callBack.result(response);


    }


}
