package com.zhuyun.jingxi.android.view;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by user0081 on 2016/7/27.
 * 目前在MyIndicator里面其实就是一个LinearLayout，独立出来以后方便扩展
 */
public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }
}
