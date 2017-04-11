package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.ClearEditText;

/**
 * Created by user0081 on 2016/7/4. 修改
 */
public class MyNickNameEditActivity extends BaseActivity {
    private ClearEditText my_nick_edit;
    @Override
    protected void initView() {
        my_nick_edit= (ClearEditText) findViewById(R.id.my_nick_edit);
        findViewById(R.id.edit_nick_back).setOnClickListener(this);
        findViewById(R.id.edit_save).setOnClickListener(this);

        String name=getIntent().getExtras().getString("oldeName");//获取旧数据
        my_nick_edit.setText(name);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_myedit_nick_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.edit_nick_back:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.edit_save:
                if (TextUtils.isEmpty(my_nick_edit.getText().toString())) {
                    TempSingleToast.showToast(App.getAppContext(),"内容不能为空");
                    return;
                }else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("nickName", my_nick_edit.getText().toString());
                    intent.putExtras(bundle);
                    setResult(20, intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;

        }
    }
}
