package com.zhuyun.jingxi.android.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.GiftSearchActivity;
import com.zhuyun.jingxi.android.base.BaseFragment;
import com.zhuyun.jingxi.android.fragment.gift.ClassifyFragment;
import com.zhuyun.jingxi.android.fragment.gift.RecommendFragment;
import com.zhuyun.jingxi.android.fragment.gift.StrategyFragment;
import com.zhuyun.jingxi.android.utils.MyLogs;

/**
 *  礼品屋
 */
public class GiftFragment extends BaseFragment {
    private View[] line;
    private TextView[] text;
    private View gonglue_line,fenlei_line,tuijian_line;
    private TextView tuijian_tv,fenlei_tv,gonglue_tv;
    private Fragment recommendFragment,classifyFragment,strategyFragment;
    private String[] tas;
    private Fragment[] fragments;
    private int currentPosition;//用来记录点击位置的
    private int fragmentPosition=-1;//fragment切换的位置(-1表示没有选择)
    @Override
    protected void initView(View view, Bundle bundle) {
        MyLogs.e("haifeng","进入GiftFragment");
        currentPosition=0;
        text= new TextView[3];
        line=new View[3];
        tuijian_line= view.findViewById(R.id.tuijian_line);
        fenlei_line=  view.findViewById(R.id.fenlei_line);
        gonglue_line= view.findViewById(R.id.gonglue_line);
        line[0]=tuijian_line;
        line[1]=fenlei_line;
        line[2]=gonglue_line;
        tuijian_tv= (TextView) view.findViewById(R.id.tuijian_tv);
        fenlei_tv= (TextView) view.findViewById(R.id.fenlei_tv);
        gonglue_tv= (TextView) view.findViewById(R.id.gonglue_tv);
        text[0]=tuijian_tv;
        text[1]=fenlei_tv;
        text[2]=gonglue_tv;
        view.findViewById(R.id.tuijian).setOnClickListener(this);
        view.findViewById(R.id.fenlei).setOnClickListener(this);
        view.findViewById(R.id.gonglue).setOnClickListener(this);
        view.findViewById(R.id.gift_search).setOnClickListener(this);

        initFragment();

    }
    @Override
    protected int setLayout() {
        return R.layout.layout_fragment_gift;
    }

    @Override
    protected void onClickEvent(View view) {
        switch (view.getId()){
            case R.id.tuijian:
                currentPosition=0;
                selectItem(currentPosition);
                break;
            case R.id.fenlei:
                currentPosition=1;
                selectItem(currentPosition);
                break;
            case R.id.gonglue:
                currentPosition=2;
                selectItem(currentPosition);
                break;
            case R.id.gift_search:
                MyLogs.e("haifeng","跳收索页");
                context.startActivity(new Intent(context,GiftSearchActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    private void initFragment() {
        tas=new String[]{"recommend","classify","strategy"};
        fragments=new Fragment[3];
        //推荐
        recommendFragment=getChildFragmentManager().findFragmentByTag("recommend");
        if (recommendFragment==null){
            recommendFragment=new RecommendFragment();
            MyLogs.e("haifeng","通过Tag查找recommendFragment，不存在，创建");
        }
        fragments[0]=recommendFragment;
        MyLogs.e("haifeng","通过Tag查找recommendFragment，存在，放到数组0位");

        //分类
        classifyFragment=getChildFragmentManager().findFragmentByTag("classify");
        if (classifyFragment==null){
            classifyFragment=new ClassifyFragment();
            MyLogs.e("haifeng","通过Tag查找classifyFragment，不存在，创建");
        }
        fragments[1]=classifyFragment;
        MyLogs.e("haifeng","通过Tag查找classifyFragment，存在，放到数组1位");

        //攻略
        strategyFragment=getChildFragmentManager().findFragmentByTag("strategy");
        if (strategyFragment==null){
            strategyFragment=new StrategyFragment();
            MyLogs.e("haifeng","通过Tag查找strategyFragment，不存在，创建");
        }
        fragments[2]=strategyFragment;
        MyLogs.e("haifeng","通过Tag查找strategyFragment，存在，放到数组2位");

        selectItem(currentPosition);
    }

    private void selectItem(int currentPosition) {
        MyLogs.e("haifeng","selectItem()，currentPosition="+currentPosition);
        //切换标题
        for (int i=0;i<3;i++){
            if (i==currentPosition){
                line[i].setVisibility(View.VISIBLE);
                text[i].setTextColor(getResources().getColor(R.color.red));
            }else {
                line[i].setVisibility(View.GONE);
                text[i].setTextColor(getResources().getColor(R.color.im_head_black));
            }
        }
        /**切换fragment (点击坐标,根据该坐标展示fragment，如果该坐标fragment没有添加过，就添加再显示；
         *                                            如果该坐标fragment添加过但隐藏了，就直接显示；
         *                   其他非点击坐标的fragment，如果添加了，就隐藏；如果没添加，不做处理。)
         * */
        FragmentTransaction transaction=getChildFragmentManager().beginTransaction();
        if (fragmentPosition!=currentPosition){
            fragmentPosition=currentPosition;
            //1.显示当前点击坐标对应的fragment
            if (!fragments[fragmentPosition].isAdded()){//没有添加过
                MyLogs.e("haifeng","fragment"+currentPosition+"添加了");
                transaction.add(R.id.gift_framelayout,fragments[fragmentPosition],tas[currentPosition]);
                transaction.show(fragments[fragmentPosition]);

            }else{//添加了
                if (fragments[fragmentPosition].isHidden()){//隐藏了
                    transaction.show(fragments[fragmentPosition]);
                    MyLogs.e("haifeng","fragment"+currentPosition+"显示了");
                }

            }
            //2.隐藏其他fragment
            for (int i = 0; i <fragments.length ; i++) {
                if (i!=currentPosition){//非点击坐标
                    if (fragments[i].isAdded()){//已添加的
                        transaction.hide(fragments[i]);
                        MyLogs.e("haifeng","fragment"+i+"隐藏了");
                    }
                }
            }

            transaction.commit();
        }


    }
}
