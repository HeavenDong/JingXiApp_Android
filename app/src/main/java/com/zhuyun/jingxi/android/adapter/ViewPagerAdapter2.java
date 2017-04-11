package com.zhuyun.jingxi.android.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by user0081 on 2016/6/13.
 */
public class ViewPagerAdapter2 extends PagerAdapter {
    private final List<View> mListViews;

    public ViewPagerAdapter2(List<View> mListViews) {
        this.mListViews=mListViews;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(mListViews.get(position),0);
        return mListViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
