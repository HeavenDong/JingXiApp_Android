package com.zhuyun.jingxi.android.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;

/**
 * Created by user0081 on 2016/6/28.
 */
public class AddBlackListPopupWindow extends PopupWindow {
    private View mMenuView;
    private TextView add_balcklist_cancel,add_balcklist_sure;
    public AddBlackListPopupWindow(Activity context, View.OnClickListener itemsOnClick){
        super();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_add_blacklist_popu, null);
        add_balcklist_cancel = (TextView) mMenuView.findViewById(R.id.add_balcklist_cancel);
        add_balcklist_sure = (TextView) mMenuView.findViewById(R.id.add_balcklist_sure);
        //设置按钮监听
        add_balcklist_sure.setOnClickListener(itemsOnClick);
        add_balcklist_cancel.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.dialog_relt).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return false;
            }
        });

     }
}
