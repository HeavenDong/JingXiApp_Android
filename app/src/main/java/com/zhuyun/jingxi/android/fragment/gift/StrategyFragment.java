package com.zhuyun.jingxi.android.fragment.gift;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.GiftStrategyEntity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.StrategyDetailsActivity;
import com.zhuyun.jingxi.android.adapter.StrategyListAdapter;
import com.zhuyun.jingxi.android.base.BaseFragment;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

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
 * Created by user0081 on 2016/6/13.   礼品屋_攻略
 */
public class StrategyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView gift_strategy_listview;
    private List<GiftStrategyEntity> lists=new ArrayList<>();
    private SwipeRefreshLayout strategy_swip_refresh;
    private StrategyListAdapter adapter;
    private Thread thread;
    @Override
    protected void initView(View view, Bundle bundle) {
        Log.e("haifeng","进入StrategyFragment");
        gift_strategy_listview= (ListView) view.findViewById(R.id.gift_strategy_listview);
        strategy_swip_refresh= (SwipeRefreshLayout) view.findViewById(R.id.strategy_swip_refresh);
        lists= App.getDBManager().selectGiftStrategyList();//查数据库缓存
        MyLogs.e("haifeng","查数据库完成 大小="+lists.size());
        initData();
//        selectDao();
        loadData();
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_gift_strategy;
    }

    @Override
    protected void onClickEvent(View view) {

    }
    private void initData() {
        adapter= new StrategyListAdapter(context,lists);
        gift_strategy_listview.setAdapter(adapter);
        gift_strategy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=  new Intent(context,StrategyDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("sId",lists.get(position).getStrategyId());
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        strategy_swip_refresh.setOnRefreshListener(this);
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
            strategy_swip_refresh.setRefreshing(false);
            MyLogs.e("haifeng","断网");
        }


    }

    private void netData() {

        MyLogs.e("haifeng","走礼品屋_攻略接口");
        HttpClient.requestGetAsyncHeader(Url.GIFT_STRATEGY_INFO, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        strategy_swip_refresh.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "攻略接口，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                lists.clear();
                                App.getDBManager().clearGiftStrategyList();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final GiftStrategyEntity bean = new GiftStrategyEntity();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.setStrategyId(jsonObject2.optLong("id"));
                                    bean.setName(jsonObject2.optString("name"));
                                    bean.setImgIconUrl(jsonObject2.optString("imgIconUrl"));
                                    bean.setWishCount(jsonObject2.optInt("wishCount"));
                                    bean.setSort(jsonObject2.optInt("sort"));

                                    CommanUtil.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            lists.add(bean);
                                        }
                                    });
                                }
                                CommanUtil.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        strategy_swip_refresh.setRefreshing(false);
                                        adapter.notifyDataSetChanged();
                                        insertDao(lists);
                                    }
                                });
                            }
                        }else {
                            MyLogs.e("haifeng","errCode不对");
                             strategy_swip_refresh.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        MyLogs.e("haifeng","走了异常 :"+e);
                         strategy_swip_refresh.setRefreshing(false);
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");
                    strategy_swip_refresh.setRefreshing(false);
                }
            }
        });

    }

    //查数据库
    private void selectDao() {

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                lists= App.getDBManager().selectGiftStrategyList();//查数据库缓存
                MyLogs.e("haifeng","查数据库完成 大小="+lists.size());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                adapter= new StrategyListAdapter(context,lists);
                gift_strategy_listview.setAdapter(adapter);
                MyLogs.e("haifeng","查数据库    完成 notifyDataSetChanged");
            }
        }.execute();
    }

    //插入数据库
    private void insertDao(final List<GiftStrategyEntity> lists) {
        MyLogs.e("haifeng","数据库操作");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < lists.size(); i++) {
                    App.getDBManager().insertGiftStrategyList(lists.get(i));
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
        MyLogs.e("haifeng","strategyFragment销毁");
    }
}
