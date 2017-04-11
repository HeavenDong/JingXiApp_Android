package com.zhuyun.jingxi.android.fragment.gift;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.GiftRecommentEntity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.GiftDetailsActivity;
import com.zhuyun.jingxi.android.activty.StrategyDetailsActivity;
import com.zhuyun.jingxi.android.adapter.HoriListAdapter;
import com.zhuyun.jingxi.android.adapter.HotRecommendGridAdapter;
import com.zhuyun.jingxi.android.adapter.StarRecommendGridAdapter;
import com.zhuyun.jingxi.android.base.BaseFragment;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.HeaderGridView;
import com.zhuyun.jingxi.android.view.HorizontalListView;
import com.zhuyun.jingxi.android.view.NoScrollGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/6/13.    礼品屋__推荐
 */

public class RecommendFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private HeaderGridView hot_recommend_grid;
    private SwipeRefreshLayout recommend_swip_refresh;
    private List<GiftRecommentEntity> lists=new ArrayList<>();
    private List<GiftRecommentEntity> strategyList= new ArrayList();
    private List<GiftRecommentEntity> starList= new ArrayList();
    private List<GiftRecommentEntity> hotList= new ArrayList();
    private HoriListAdapter startegy_adapter;
    private StarRecommendGridAdapter star_adapter;
    private HotRecommendGridAdapter hot_adapter;
    private HorizontalListView horiListView;
    private NoScrollGridView recommend_grid;
    private boolean isTop;
    private Thread thread;
    @Override
    protected void initView(View view, Bundle bundle) {
        Log.e("haifeng","进入礼品__推荐fragment");

        View head= View.inflate(context,R.layout.layout_giftrecommend_head,null);
        hot_recommend_grid= (HeaderGridView) view.findViewById(R.id.recommend_grid);
        hot_recommend_grid.addHeaderView(head);
        recommend_swip_refresh= (SwipeRefreshLayout) view.findViewById(R.id.recommend_swip_refresh);
        horiListView= (HorizontalListView) head.findViewById(R.id.recommend_hori_liatview);
        recommend_grid= (NoScrollGridView) head.findViewById(R.id.hot_recommend_grid);
        horiListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        lists= App.getDBManager().selectGiftRecommendList();//查数据库缓存
        if (lists.size()>0){
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).getType()==1){//攻略
                    strategyList.add(lists.get(i));
                }else if (lists.get(i).getType()==2){//达人
                    starList.add(lists.get(i));
                }else if (lists.get(i).getType()==3){//热门
                    hotList.add(lists.get(i));
                }
            }
        }
        MyLogs.e("haifeng","查数据库完成 大小="+lists.size());
        initData();
//        selectDao();
        loadData();
        setViewListener();
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_gift_recommend;
    }

    @Override
    protected void onClickEvent(View view) {

    }

    private void initData() {
        startegy_adapter =new HoriListAdapter(context,strategyList);
        star_adapter = new StarRecommendGridAdapter(context, starList);//达人
        hot_adapter = new HotRecommendGridAdapter(context, hotList);//热门
        //攻略推荐
        horiListView.setAdapter(startegy_adapter);
        //达人推荐
        recommend_grid.setAdapter(star_adapter);
        recommend_grid.setSelector(R.color.transparency);//去点击状态原生背景色
        //热门推荐
        hot_recommend_grid.setAdapter(hot_adapter);
        hot_recommend_grid.setSelector(R.color.transparency);//去点击状态原生背景色
    }


    private void setViewListener() {
        recommend_swip_refresh.setOnRefreshListener(this);

        horiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(context, StrategyDetailsActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });
        recommend_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLogs.e("haifeng","达人推荐  position=="+position);
                Intent intent=  new Intent(context,GiftDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("giftId",lists.get(position).getRecommenId());
                bundle.putString("from","RecommendFragment");
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });
        hot_recommend_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLogs.e("haifeng","热门推荐  position=="+position);
                if (position>1) {
                    Intent intent=  new Intent(context,GiftDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("giftId",lists.get(position-2).getRecommenId());
                    bundle.putString("from","RecommendFragment");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
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
            recommend_swip_refresh.setRefreshing(false);
            MyLogs.e("haifeng","断网");
        }



    }

    private void netData() {

        MyLogs.e("haifeng","走礼品屋_推荐接口");
        HttpClient.requestGetAsyncHeader(Url.GIFT_RECOMMEND_INFO, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        recommend_swip_refresh.setRefreshing(false);
                    }
                });

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "推荐接口，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                lists.clear();
                                strategyList.clear();
                                starList.clear();
                                hotList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final GiftRecommentEntity bean = new GiftRecommentEntity();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.setRecommenId(jsonObject2.optLong("id"));
                                    bean.setName(jsonObject2.optString("name"));
                                    bean.setImgIconUrl(jsonObject2.optString("imgIconUrl"));
                                    bean.setUtpTime(jsonObject2.optLong("utpTime"));
                                    bean.setSort(jsonObject2.optInt("sort"));
                                    bean.setType(jsonObject2.optInt("type"));
                                    bean.setWishCount(jsonObject2.optInt("wishCount"));
                                    bean.setPrice(jsonObject2.optDouble("price"));

//                                    lists.add(bean);
//                                    if (bean.getType()==1){//攻略
//                                        strategyList.add(bean);
//                                    }else if (bean.getType()==2){//达人
//                                        starList.add(bean);
//                                    }else if (bean.getType()==3){//热门
//                                        hotList.add(bean);
//                                    }

                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            lists.add(bean);
                                            if (bean.getType()==1){//攻略
                                                strategyList.add(bean);
                                            }else if (bean.getType()==2){//达人
                                                starList.add(bean);
                                            }else if (bean.getType()==3){//热门
                                                hotList.add(bean);
                                            }
                                        }
                                    });
                                }
                                MyLogs.e("haifeng","集合大小=="+lists.size());
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startegy_adapter.notifyDataSetChanged();
                                        star_adapter.notifyDataSetChanged();
                                        hot_adapter.notifyDataSetChanged();
                                        recommend_swip_refresh.setRefreshing(false);
                                        insertDao(lists);
                                    }
                                });

                            }
                        }else {
                            MyLogs.e("haifeng","errCode不对");
                            recommend_swip_refresh.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        MyLogs.e("haifeng","走了异常 :"+e);
                        recommend_swip_refresh.setRefreshing(false);
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","推荐接口返回失败：response.code()！=200");
                    recommend_swip_refresh.setRefreshing(false);
                }
            }
        });

    }

    //查数据库
    private void selectDao() {

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                lists= App.getDBManager().selectGiftRecommendList();//查数据库缓存
                MyLogs.e("haifeng","查数据库完成 大小="+lists.size());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                if (lists.size()>0){
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getType()==1){//攻略
                            strategyList.add(lists.get(i));
                        }else if (lists.get(i).getType()==2){//达人
                            starList.add(lists.get(i));
                        }else if (lists.get(i).getType()==3){//热门
                            hotList.add(lists.get(i));
                        }
                    }
                }
                if (startegy_adapter==null) {
                startegy_adapter =new HoriListAdapter(context,strategyList);
                }
                if (star_adapter==null) {
                star_adapter = new StarRecommendGridAdapter(context, starList);//达人
                }
                if (hot_adapter==null) {
                    hot_adapter = new HotRecommendGridAdapter(context, hotList);//热门
                }
                //攻略推荐
                horiListView.setAdapter(startegy_adapter);
                //达人推荐
                recommend_grid.setAdapter(star_adapter);
                recommend_grid.setSelector(R.color.transparency);//去点击状态原生背景色
                //热门推荐
                hot_recommend_grid.setAdapter(hot_adapter);
                hot_recommend_grid.setSelector(R.color.transparency);//去点击状态原生背景色
                MyLogs.e("haifeng","查数据库    完成 notifyDataSetChanged");
            }
        }.execute();
    }
    //插入数据库
    private void insertDao(final List<GiftRecommentEntity> lists) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                App.getDBManager().clearGiftRecommentList();
                for (int i = 0; i < lists.size(); i++) {
                    App.getDBManager().insertGiftRecommentList(lists.get(i));
                }
                MyLogs.e("haifeng","插入数据库完成");
            }
        }).start();
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
        if (lists!=null){
            lists.clear();
            lists=null;
        }
        System.gc();
        MyLogs.e("haifeng","recommendFragment销毁");
    }
}
