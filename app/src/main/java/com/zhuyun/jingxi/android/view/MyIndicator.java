package com.zhuyun.jingxi.android.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;

/**
 * Created by user0081 on 2016/7/27.
 */
public class MyIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener{
    private ViewPager mViewPager;
    private MyLinearLayout myLinearLayout;
    ViewPager.OnPageChangeListener mListener;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView)view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.index;
            setCurrentItem(newSelected);
        }
    };

    public MyIndicator(Context context) {
        super(context);
        init(context);
    }

    public MyIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context mcontext){
        setHorizontalScrollBarEnabled(false);//隐藏自带的滚动条
        //添加linearLayout
        myLinearLayout = new MyLinearLayout(mcontext);
        myLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(myLinearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setViewPager(ViewPager viewPager){
        setViewPager(viewPager,0);
    }

    public void setViewPager(ViewPager viewPager,int initPos){
        if (mViewPager == viewPager) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        viewPager.setOnPageChangeListener(this);
        notifyDataSetChanged();
        setCurrentItem(initPos);
    }

    private void notifyDataSetChanged(){
        myLinearLayout.removeAllViews();
        PagerAdapter mAdapter = mViewPager.getAdapter();
        int count = mAdapter.getCount();
        for(int i=0;i<count;i++){
            addTab(i,mAdapter.getPageTitle(i));
        }
        requestLayout();
    }

    private void addTab(int index,CharSequence text) {
//        TabView tabView = new TabView(getContext());
//        tabView.index = index;
//        tabView.setFocusable(true);
//        tabView.setOnClickListener(mTabClickListener);
//        tabView.setText(text);
//        tabView.setTextSize(30);
//        tabView.setPadding(0,5,0,5);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(getContext(), 5),dip2px(getContext(), 5));
        params.setMargins(dip2px(getContext(), 2), dip2px(getContext(), 2),dip2px(getContext(),2),dip2px(getContext(), 2));
//       params.leftMargin = dip2px(getContext(), 8);

       ImageView img=  new ImageView(getContext());
        img.setFocusable(false);
//        img.setPadding(0,5,0,5);
        img.setLayoutParams(params);
        myLinearLayout.addView(img);
    }
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**换页的动态效果
     * */
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        int mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = myLinearLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {//遍历标题，改变选中的背景
            final View child = myLinearLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
//                child.setBackgroundColor(Color.RED);
                child.setBackgroundResource(R.drawable.point2);
                animateToTab(item);//动画效果
            }else{
                child.setBackgroundResource(R.drawable.point1);
//                child.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    /**标题栏自动滑动，使标题处于正中间
     * */
    private Runnable mTabSelector;
    private void animateToTab(final int position) {
        final View tabView = myLinearLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener){
        mListener = listener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mListener!=null) mListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if(mListener!=null) mListener.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(mListener!=null) mListener.onPageScrollStateChanged(state);
    }

    /**点击标题，也会自动滑动，为了让TextView能点击，每个TextView都设置了OnClickListener
    * */
    private class TabView extends TextView {
        public int index;
        public TabView(Context context,int index){
            this(context);
            this.index = index;
        }
        public TabView(Context context) {
            super(context);
        }
    }
}