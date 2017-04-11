package com.zhuyun.jingxi.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by user0081 on 2016/6/21.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragmentList;

    public FragmentAdapter(FragmentManager supportFragmentManager, ArrayList<Fragment> fragmentList) {
        super(supportFragmentManager);
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragmentList!=null){
            return fragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (fragmentList!=null){
            return fragmentList.size();
        }
        return 0;
    }
}
