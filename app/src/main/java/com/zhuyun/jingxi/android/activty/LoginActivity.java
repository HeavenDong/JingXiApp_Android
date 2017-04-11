package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zhuyun.jingxi.android.MainActivity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.FragmentAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.fragment.login.AccountsLoginFragment;
import com.zhuyun.jingxi.android.fragment.login.MobileLoginFragment;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.utils.MyLogs;

import java.util.ArrayList;


public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private ViewPager login_pager;
    private View accounts_login_line,mobile_login_line;
    private TextView mobile_login_title,accounts_login_title;
    private ArrayList<Fragment> fragmentList;
    private boolean autoLogin = false;
    @Override
    protected  void initView() {
        MyLogs.e("haifeng","进入登录");
        login_pager= (ViewPager) findViewById(R.id.login_pager);
        accounts_login_title= (TextView) findViewById(R.id.accounts_login_title);
        mobile_login_title= (TextView) findViewById(R.id.mobile_login_title);
        accounts_login_line=findViewById(R.id.accounts_login_line);
        mobile_login_line=findViewById(R.id.mobile_login_line);
        initFragment();
        initData();
        setViewListener();

        if (DemoHelper.getInstance().getCurrentUsernName() != null) {
            MyLogs.e("haifeng","当前用户="+ DemoHelper.getInstance().getCurrentUsernName());
        }
    }
    //实例化Fragment
    private void initFragment() {
        fragmentList = new ArrayList<Fragment>();
        Fragment f1 = new AccountsLoginFragment();
        Fragment f2 = new MobileLoginFragment();
        fragmentList.add(f1);
        fragmentList.add(f2);
    }
    private void initData() {
        login_pager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragmentList));
        login_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        accounts_login_title.setTextColor(getResources().getColor(R.color.im_head_black));
                        mobile_login_title.setTextColor(getResources().getColor(R.color.im_head_gray));
                        accounts_login_line.setVisibility(View.VISIBLE);
                        mobile_login_line.setVisibility(View.GONE);
                        break;
                    case 1:
                        accounts_login_title.setTextColor(getResources().getColor(R.color.im_head_gray));
                        mobile_login_title.setTextColor(getResources().getColor(R.color.im_head_black));
                        accounts_login_line.setVisibility(View.GONE);
                        mobile_login_line.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setViewListener() {
        findViewById(R.id.login_back).setOnClickListener(this);
        findViewById(R.id.accounts_login_title).setOnClickListener(this);
        findViewById(R.id.mobile_login_title).setOnClickListener(this);

    }
    @Override
    protected void setContentLayout() {
        if (DemoHelper.getInstance().isLoggedIn()) {
            autoLogin = true;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

            return;
        }
        setContentView(R.layout.layout_login_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.login_back:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.accounts_login_title:
                accounts_login_title.setTextColor(getResources().getColor(R.color.im_head_black));
                mobile_login_title.setTextColor(getResources().getColor(R.color.im_head_gray));
                accounts_login_line.setVisibility(View.VISIBLE);
                mobile_login_line.setVisibility(View.GONE);
                login_pager.setCurrentItem(0,true);
                break;
            case R.id.mobile_login_title:
                accounts_login_title.setTextColor(getResources().getColor(R.color.im_head_gray));
                mobile_login_title.setTextColor(getResources().getColor(R.color.im_head_black));
                accounts_login_line.setVisibility(View.GONE);
                mobile_login_line.setVisibility(View.VISIBLE);
                login_pager.setCurrentItem(1,true);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLogs.e("haifeng","登录界面被销毁");
    }

}
