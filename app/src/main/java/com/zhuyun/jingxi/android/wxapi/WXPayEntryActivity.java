package com.zhuyun.jingxi.android.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

/**
 * Created by user0081 on 2016/7/11.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
  // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onReq(BaseReq arg0) { }

    @Override
    public void onResp(BaseResp resp) {
        MyLogs.e("haifeng", resp.errCode + resp.errCode + resp.errStr
        + resp.errStr);
        TempSingleToast.showToast(App.getAppContext(),"微信登录返回2"+ resp.errCode);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                MyLogs.e("haifeng","分享成功");

                TempSingleToast.showToast(App.getAppContext(),"分享成功");
                //分享成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                MyLogs.e("haifeng","分享取消");
                TempSingleToast.showToast(App.getAppContext(),"分享取消");
                //分享取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                MyLogs.e("haifeng","分享拒绝");
                TempSingleToast.showToast(App.getAppContext(),"分享拒绝");
                //分享拒绝
                break;
        }
    }
}