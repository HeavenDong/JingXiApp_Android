package com.zhuyun.jingxi.android.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.HomeListEntity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.ChestsActivity;
import com.zhuyun.jingxi.android.activty.FriendsActivity;
import com.zhuyun.jingxi.android.activty.GiftSearchResultActivity;
import com.zhuyun.jingxi.android.activty.StarActivity;
import com.zhuyun.jingxi.android.activty.WishDetailsActivity;
import com.zhuyun.jingxi.android.adapter.HomeListAdapter;
import com.zhuyun.jingxi.android.adapter.ViewPagerAdapter;
import com.zhuyun.jingxi.android.base.BaseFragment;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.MyFragmentDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 爱惊喜
*/
public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private View head;
    private ListView homeListView;
    private HomeListAdapter adapter;
    private SwipeRefreshLayout home_swip_refresh;
    private List<HomeListEntity> lists=new ArrayList<HomeListEntity>();
    private ViewPager home_viewpager;
    private int curPagePos;
    private List<View> mListView = new ArrayList<>();
    private List<View> points = new ArrayList<>();
    private ImageView point1,point2;
    private ViewPagerAdapter mImageAdapter;
    private Thread thread;
    private boolean isTrue;
    private Timer mTimer;
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //广告栏小点切换
                case 1:
                    int i = curPagePos + 1;
                    if (i > 1) {
                        curPagePos = 0;
                        home_viewpager.setCurrentItem(curPagePos);
                        selectPoint(curPagePos);
                    } else {
                        curPagePos = i;
                        home_viewpager.setCurrentItem(curPagePos);
                        selectPoint(curPagePos);
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void initView(View view, Bundle bundle) {
        MyLogs.e("haifeng","进入HomeFragment");
        view.findViewById(R.id.home_togift).setOnClickListener(this);
        homeListView= (ListView) view.findViewById(R.id.home_listview);
        home_swip_refresh= (SwipeRefreshLayout) view.findViewById(R.id.home_swip_refresh);
        head = View.inflate(context, R.layout.layout_home_head,null);
        home_viewpager= (ViewPager) head.findViewById(R.id.home_viewpager);
        point1= (ImageView) head.findViewById(R.id.point1);
        point2= (ImageView) head.findViewById(R.id.point2);
        homeListView.addHeaderView(head);

        lists= App.getDBManager().selectHomeList();//查数据库缓存
        MyLogs.e("haifeng","查数据库完成 大小="+lists.size());

        initData();
//        selectDao();
        loadData();
        setViewListener();
        initViewPager();
        changeImageTimer();
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_fragment_home;
    }

    @Override
    protected void onClickEvent(View view) {
       switch (view.getId()) {
           case R.id.home_togift:
               MyLogs.e("haifeng", "去礼品屋232");
                  final MyFragmentDialog myFragmentDialog = new MyFragmentDialog();
                   myFragmentDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                   myFragmentDialog.show(getFragmentManager(), "dialog");
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           MyLogs.e("haifeng", "到时了");
                           Intent intent = new Intent();
                           intent.setAction("transactionMainFragment");
                           intent.putExtra("index", 1);
                           context.sendBroadcast(intent);
                           myFragmentDialog.dismiss();
                       }
                   },1000);
               break;
           case R.id.wish_img:
               MyLogs.e("haifeng", "点击许愿");
               final AlertDialog dialog=  new AlertDialog.Builder(context).create();
               dialog.show();
               dialog.setCancelable(false);
               Window window=dialog.getWindow();
               window.setWindowAnimations(R.style.homeDialogWindowAnim); //设置窗口弹出动画  AnimBottom
               View dialogView=View .inflate(context, R.layout.layout_wish_dialog, null);//layout_my_finish_dialog
               window.setContentView(dialogView);
               window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//透明背景
               window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//弹出软键盘
               final EditText home_wish_gosearch= (EditText) dialogView.findViewById(R.id.home_wish_gosearch);
               dialogView.findViewById(R.id.go_wish).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent=  new Intent(context,GiftSearchResultActivity.class);
                       Bundle bundle = new Bundle();
                       bundle.putString("searchText",home_wish_gosearch.getText().toString());
                       intent.putExtras(bundle);
                       startActivity(intent);
                       getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                       dialog.dismiss();
                   }
               });
               dialogView.findViewById(R.id.quit_wish).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                   }
               });



        break;
           case R.id.home_box:
               context.startActivity(new Intent(context,ChestsActivity.class));
               getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

               break;
           case R.id.home_star:
               context.startActivity(new Intent(context,StarActivity.class));
               getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

               break;
           case R.id.home_friend:
               context.startActivity(new Intent(context,FriendsActivity.class));
               getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

               break;
       }
    }

    private void initData() {
        adapter= new HomeListAdapter(context,this,lists);
        homeListView.setAdapter(adapter);
        mImageAdapter = new ViewPagerAdapter(mListView);
        home_viewpager.setAdapter(mImageAdapter);


    }
    private void setViewListener() {
        home_swip_refresh.setOnRefreshListener(this);
        head.findViewById(R.id.wish_img).setOnClickListener(this);
        head.findViewById(R.id.home_box).setOnClickListener(this);
        head.findViewById(R.id.home_star).setOnClickListener(this);
        head.findViewById(R.id.home_friend).setOnClickListener(this);

        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position-= 1;
                MyLogs.e("haifeng","许愿listview点击 "+(position));
//                if(CommanUtil.isNetworkAvailable()) {
                    Intent intent=  new Intent(context,WishDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    //许愿id
                    bundle.putLong("wishID",lists.get(position).getWishId());
                    bundle.putString("nickName",lists.get(position).getNickName());
                    bundle.putString("portraitUrl",lists.get(position).getPortraitUrl());
                    bundle.putInt("gender",lists.get(position).getGender());
                    bundle.putString("content",lists.get(position).getContent());
                    bundle.putLong("utpTime",lists.get(position).getUtpTime());
                    bundle.putString("imgUrl",lists.get(position).getImgUrl());
                    bundle.putString("giftName",lists.get(position).getGoodsName());
                    bundle.putInt("type",lists.get(position).getType());//（1：普通许愿； 2：众筹许愿）
                    bundle.putDouble("price",lists.get(position).getCfPrice());
                    bundle.putInt("likeNum",lists.get(position).getLikeNum());
                    bundle.putInt("commNum",lists.get(position).getCommNum());
                    bundle.putInt("isFriend",lists.get(position).getIsFriend());
                    bundle.putString("tag","看详情");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

//                }else {
//                    TempSingleToast.showToast(App.getAppContext(),"你的网络不给力");
//                }


            }
        });
    }

    private void loadData() {
        //请求网络
        if(CommanUtil.isNetworkAvailable()) {
            if(thread==null){
                thread=  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyLogs.e("hanzhen","loadData  开子线程");
                        netData();
                    }
                });
                thread.start();
            }
        }else {
            TempSingleToast.showToast(App.getAppContext(),"你的网络不给力");
            home_swip_refresh.setRefreshing(false);
            MyLogs.e("haifeng","断网");
        }
    }

    private void netData() {
        MyLogs.e("haifeng","走首页/大厅时间流接口");
        HttpClient.requestGetAsyncHeader(Url.HOME_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        home_swip_refresh.setRefreshing(false);
                        TempSingleToast.showToast(App.getAppContext(),"服务连接失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.code()==200) {
                    final String json = response.body().string();
                    MyLogs.e("haifeng", "首页/大厅时间流接口，json=" + json);
                    CommanUtil.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            MyLogs.e("haifeng","runOnUIThread");
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                final String errCode = jsonObject.optString("errCode");
                                if (errCode.equals("0")) {
                                    JSONArray jsonArray = jsonObject.optJSONArray("datas");
                                    if (jsonArray != null) {
                                        lists.clear();
                                        App.getDBManager().clearHomeList();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            final HomeListEntity bean = new HomeListEntity();
                                            JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                            bean.setWishId(jsonObject2.optLong("id"));
                                            bean.setNickName(jsonObject2.optString("nickName"));
                                            bean.setPortraitUrl(jsonObject2.optString("portraitUrl"));
                                            bean.setGender(jsonObject2.optInt("gender"));
                                            bean.setContent(jsonObject2.optString("content"));
                                            bean.setUtpTime(jsonObject2.optLong("utpTime"));
                                            bean.setImgUrl(jsonObject2.optString("imgUrl"));
                                            bean.setGoodsName(jsonObject2.optString("goodsName"));
                                            bean.setType(jsonObject2.optInt("type"));
                                            bean.setCfPrice(jsonObject2.optDouble("cfPrice"));
                                            bean.setLikeNum(jsonObject2.optInt("likeNum"));
                                            bean.setCommNum(jsonObject2.optInt("commNum"));
                                            bean.setIsFriend(jsonObject2.optInt("isFriend"));
                                            bean.setWishUserId(jsonObject2.optLong("wishUserId"));
                                            bean.setIsLiked(jsonObject2.optInt("isLike"));
                                            lists.add(bean);
                                        }
                                          MyLogs.e("haifeng","下载集合大小="+lists.size());
                                          adapter.notifyDataSetChanged();
                                          home_swip_refresh.setRefreshing(false);
                                          insertDao(lists);

                                    }else {
                                        MyLogs.e("haifeng","集合为null");
                                      home_swip_refresh.setRefreshing(false);
                                    }
                                }else {
                                    MyLogs.e("haifeng","errCode不对");
                                     home_swip_refresh.setRefreshing(false);
                                }
                            } catch (Exception e) {
                                MyLogs.e("haifeng","走了异常 :"+e);
                                home_swip_refresh.setRefreshing(false);
                                e.printStackTrace();
                            }


                }
            });
                }else {
                    MyLogs.e("haifeng","首页/大厅时间流接口返回失败：response.code()！=200");
                    home_swip_refresh.setRefreshing(false);
                }
            }
        });

    }

    //点赞接口
    public void changlikeInfo(Map<String, Object> params, String url, final int position) {
        MyLogs.e("haifeng",url+"赞 params="+params);

        //请求网络
        if(CommanUtil.isNetworkAvailable()) {
            try {
                HttpClient.requestPostAsyncHeader(url, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        MyLogs.e("haifeng","走onFailure");
                        CommanUtil.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                            TempSingleToast.showToast(App.getAppContext(),"服务器连接失败");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            String json=response.body().string();
                            MyLogs.e("haifeng","点赞 :json="+json);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                final String errCode = jsonObject.optString("errCode");
                                if (errCode.equals("0")) {
                                    int datas = jsonObject.optInt("datas");
                                    if (datas==1) {
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (lists.get(position).getIsLiked()==1){
                                                        MyLogs.e("haifeng", "点赞取消");
                                                        lists.get(position).setIsLiked(0);
                                                    }else if (lists.get(position).getIsLiked()==0){
                                                        MyLogs.e("haifeng", "点赞");
                                                        lists.get(position).setIsLiked(1);
                                                    }
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }else if (datas==0){
                                        MyLogs.e("haifeng","修改失败 ");
                                    }
                                }else {
                                    MyLogs.e("haifeng", "修改失败 errorCode="+errCode);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            MyLogs.e("haifeng","response.code()！=200");
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    TempSingleToast.showToast(App.getAppContext(),"服务器连接失败");
                                }
                            });
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            TempSingleToast.showToast(App.getAppContext(),"你的网络不给力");
        }

    }
//    //查数据库
//    private void selectDao() {
//
//        new AsyncTask<Void,Void,Void>(){
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                lists= App.getDBManager().selectHomeList();//查数据库缓存
//                MyLogs.e("haifeng","查数据库完成 大小="+lists.size());
//                return null;
//            }
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                adapter = new HomeListAdapter(context, this, lists);
//                homeListView.setAdapter(adapter);
//            }
//        }.execute();
//    }
    //插入数据库
    private void insertDao(final List<HomeListEntity> lists) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < lists.size(); i++) {
                    App.getDBManager().insertHomeList(lists.get(i));
                }
                MyLogs.e("haifeng","插入数据库完成");
            }
        }).start();
    }

    private void initViewPager() {
        ImageView image1 = new ImageView(getActivity());
        ImageView image2 = new ImageView(getActivity());
        image1.setBackgroundResource(R.drawable.aijingxi_banner);
        image2.setBackgroundResource(R.drawable.aijingxi_banner);
        mListView.add(image1);
        mListView.add(image2);
        mImageAdapter.notifyDataSetChanged();

        //初始化points
        points.add(point1);
        points.add(point2);
        selectPoint(0);
    }

    /**
     * 定时切换广告栏图片
     */
    private void changeImageTimer() {
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        };
        mTimer.schedule(task, 0, 1300);
    }
    /**
     * 广告栏小点切换
     *
     * @param pos
     */
    private void selectPoint(int pos) {
        for (int i = 0; i < points.size(); i++) {
            if (i == pos) {
                points.get(i).setBackgroundResource(R.drawable.point2);
            } else {
                points.get(i).setBackgroundResource(R.drawable.point1);
            }
        }
    }

    @Override
    public void onRefresh() {
        if(thread!=null) {
            thread.interrupt();
            thread = null;
        }
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer!=null){
            mTimer.cancel();
        }
        if (mHandler!=null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler=null;
        }
        if (lists!=null){
            lists.clear();
            lists=null;
        }
        System.gc();
        MyLogs.e("haifeng","homeFragment销毁");
    }
}
