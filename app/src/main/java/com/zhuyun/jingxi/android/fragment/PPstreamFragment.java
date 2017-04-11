package com.zhuyun.jingxi.android.fragment;

import android.os.Bundle;
import android.view.View;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseFragment;
import com.zhuyun.jingxi.android.utils.MyLogs;

/**
 *   直播
 */
public class PPstreamFragment extends BaseFragment {


    @Override
    protected void initView(View view, Bundle bundle) {
        MyLogs.e("haifeng","进入PPstreamFragment");

    }

    @Override
    protected int setLayout() {
            return R.layout.layout_fragment_ppstream;
    }

    @Override
    protected void onClickEvent(View view) {

    }
}
