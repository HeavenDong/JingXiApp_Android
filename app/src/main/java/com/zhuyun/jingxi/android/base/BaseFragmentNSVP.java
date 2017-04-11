package com.zhuyun.jingxi.android.base;

import android.support.v4.app.Fragment;

import com.zhuyun.jingxi.android.utils.MyLogs;

/**
 * Created by jiangxiangfei on 2016/5/23.
 */
public abstract class BaseFragmentNSVP extends Fragment {
    /*
    * 当前界面是否呈现给用户的状态标识
    * */
    protected boolean isVisible;
    /*
    * 重写Fragment父类生命周期方法，在OnCreate之前调用该方法，实现Fragment数据的缓加载。
    * isVisibleToUser:当前是否已将界面显示给用户的状态
    * */

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLogs.e("jxf","fragment走setUserVisibleHint()方法");
        if (getUserVisibleHint()){
            isVisible=true;
            onVisible();
        }
        else{
            isVisible=false;
            onInVisible();
        }
    }

    /*
    * 当前界面还没呈现给用户，即设置不可见时执行
    * */
    private void onInVisible() {
    }

    /*
    * 当界面呈现给用户，即设置可见时执行，进行加载数据的方法
    * 在用户可见时加载数据，而不在用户不可见的时候加载数据，是为了防止控件对象出现空指针异
    * */
    protected void onVisible() {
        setlazyLoad();
    }

    /*
    * 该方法：子类实现  需要双指针控制+代码调用：setUserVisibleHint在oncreatview之前调用：但是不符合双指针 所以具体的方法没有执行，但是接着往下 走到oncreateview方法：指针改变，手动调用该方法：指针符合就会执行该方法
    * */
    protected  abstract void setlazyLoad();
}
