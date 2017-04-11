package com.zhuyun.jingxi.android.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by jiangxiangfei on 2016/5/17.
 * notice：android的浸入式状态栏有半透明：达不到ios的效果，所以选择放弃，暂时封装需要的部分
 * splash需要泉品支持的：其他页面只需要有没有titlebar就可以了
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener{
    /*
    * base中的点击事件抽取公共方法
    * */
    @Override
    public void onClick(View v) {
        onClickEvent(v);
    }



    /*
    * 复写oncreat生命周期方法：抽取公共方法和进行全屏和状态栏的浸入式代码处理
    * */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //抽取公共的方法抽象：待子类实现
        setContentLayout();
        initView();
    }

    protected abstract void initView();
    protected abstract void setContentLayout();
    /*
  * 抽象受保护需要子类继承的点击事件方法
  * */
    protected abstract void onClickEvent(View v);


}
