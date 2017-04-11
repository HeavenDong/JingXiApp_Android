package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

/**
 * Created by user0081 on 2016/7/4.
 */
public class MyMobileEditActivity extends BaseActivity{
    private TextView my_mobile_edit;
    @Override
    protected void initView() {
        my_mobile_edit= (TextView) findViewById(R.id.my_mobile_edit);
        findViewById(R.id.edit_mobile_back).setOnClickListener(this);
        findViewById(R.id.edit_mobile_save).setOnClickListener(this);

        String name=getIntent().getExtras().getString("oldeMobile");//获取旧数据
        my_mobile_edit.setText(name);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_myedit_mobile_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.edit_mobile_back:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.edit_mobile_save:
                if (TextUtils.isEmpty(my_mobile_edit.getText().toString())) {
                    TempSingleToast.showToast(App.getAppContext(), "内容不能为空");
                    return;
                }else if (CommanUtil.isMobilePhone(my_mobile_edit.getText().toString())){
                    TempSingleToast.showToast(App.getAppContext(), "手机号输入有误");
                }else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("mobile", my_mobile_edit.getText().toString());
                    intent.putExtras(bundle);
                    setResult(21, intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;

        }
    }
}
