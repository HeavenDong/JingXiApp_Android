package com.zhuyun.jingxi.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.AAAAA;
import com.zhuyun.jingxi.android.activty.ChestsActivity;
import com.zhuyun.jingxi.android.activty.MyPersonActivity;
import com.zhuyun.jingxi.android.activty.MySettingActivity;
import com.zhuyun.jingxi.android.activty.ReceiverMessaageActivity;
import com.zhuyun.jingxi.android.activty.RecordForReceivedGiftActivity;
import com.zhuyun.jingxi.android.activty.RecordForSendGiftActivity;
import com.zhuyun.jingxi.android.activty.WishActivity;
import com.zhuyun.jingxi.android.base.BaseFragment;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import java.io.File;

/**
 *  我
 */
public class MeFragment extends BaseFragment {
    private RoundedImageView me_fragment_head;
    private TextView me_fragment_name;
    private String nick_name,smallHeadImgPath;
    @Override
    protected void initView(View view, Bundle bundle) {
        MyLogs.e("haifeng","进入MeFragment");
        me_fragment_head= (RoundedImageView) view.findViewById(R.id.me_fragment_head);
        me_fragment_name= (TextView) view.findViewById(R.id.me_fragment_name);

        view.findViewById(R.id.my_message).setOnClickListener(this);
        view.findViewById(R.id.sendgift_history).setOnClickListener(this);
        view.findViewById(R.id.receivergift_history).setOnClickListener(this);
        view.findViewById(R.id.my_box).setOnClickListener(this);
        view.findViewById(R.id.message_receiver).setOnClickListener(this);
        view.findViewById(R.id.my_setting).setOnClickListener(this);
        view.findViewById(R.id.sharemaney_code).setOnClickListener(this);
        view.findViewById(R.id.my_wish).setOnClickListener(this);
        initData();
    }

    private void initData() {
        nick_name= SharedPreUtils.get(App.getAppContext(),  "nick_name","未填写").toString();
        smallHeadImgPath= SharedPreUtils.get(App.getAppContext(),  "smallHeadImgPath","").toString();
        if (!smallHeadImgPath.equals("")) {
            Picasso.with(App.getAppContext()).load(new File(smallHeadImgPath)).into(me_fragment_head);
        }
         me_fragment_name.setText(nick_name);
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_fragment_me;
    }

    @Override
    protected void onClickEvent(View view) {
      switch (view.getId()){
          //个人信息
          case R.id.my_message:
              startActivity(new Intent(context, MyPersonActivity.class));
              getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;
          //设置
          case R.id.my_setting:
              startActivityForResult(new Intent(context, MySettingActivity.class),106);
              getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;
          //我的许愿
          case R.id.my_wish:
              Intent intent=  new Intent(context,WishActivity.class);
              Bundle bundle = new Bundle();
              bundle.putLong("FriendsId", (long) SharedPreUtils.get(App.getAppContext(), "user_id", (long)0));
              intent.putExtras(bundle);
              startActivity(intent);
              getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;
          //送礼记录
          case R.id.sendgift_history:
              startActivity(new Intent(context, RecordForSendGiftActivity.class));
              getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;
          //收礼记录
          case R.id.receivergift_history:
              startActivity(new Intent(context, RecordForReceivedGiftActivity.class));
              getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;

          //我的宝箱
          case R.id.my_box:
              startActivity(new Intent(context, ChestsActivity.class));
              getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;

          //收货信息
          case R.id.message_receiver:
              startActivity(new Intent(context, ReceiverMessaageActivity.class));
              getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;

          case R.id.sharemaney_code:
              startActivity(new Intent(context, AAAAA.class));
              getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
              break;

      }
    }
    @Override
    public void onResume() {
        super.onResume();
        MyLogs.e("haifeng","MeFragment 数据刷新");
        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==106&&resultCode==44){
            MyLogs.e("haifeng","点击退出登录l");
            getActivity().finish();
        }
    }
}
