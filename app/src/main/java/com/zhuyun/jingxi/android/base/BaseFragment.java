package com.zhuyun.jingxi.android.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuyun.jingxi.android.utils.MyLogs;

/**
 * Created by jiangxiangfei on 2016/5/23.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    protected Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(setLayout(), container, false);
        if (view == null) {
            MyLogs.e("jxf", "BaseFragment: view == null!!");
            return null;
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    protected abstract void initView(View view, Bundle bundle);
    protected abstract @LayoutRes
    int setLayout();

    @Override
    public void onClick(View v) {
        onClickEvent(v);
    }

    protected abstract void onClickEvent(View view);
}
