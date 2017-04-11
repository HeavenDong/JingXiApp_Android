package com.zhuyun.jingxi.android.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuyun.jingxi.android.R;

/**
 * Created by user0081 on 2016/6/14.
 */
public class TempSingleToast {
    private static Toast toast;
    public static void showToast(Context context, String talking) {
        if (toast == null) {
            toast = new Toast(context);
        }
        View view = View.inflate(context, R.layout.layout_toast, null);
        TextView text = (TextView) view.findViewById(R.id.talks);
        text.setText(talking);
        // toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();

    }
    public  static void cancleToast(){
        if (toast!=null){
            toast.cancel();
        }
    }
}
