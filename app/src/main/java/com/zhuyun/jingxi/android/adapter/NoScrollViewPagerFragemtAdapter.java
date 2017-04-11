package com.zhuyun.jingxi.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by jiangxiangfei on 2016/5/17.
 * notice:viewpager的预加载会造成内存不好，复写base方法形成懒加载：第一层不适用viewpager：为了形成的第一层：为了防止内存回收要不要在第一层使用
 * 第一层使用记录添加的fragmentmaneger
 */
public class NoScrollViewPagerFragemtAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragments;
    public NoScrollViewPagerFragemtAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 5;
    }
}
