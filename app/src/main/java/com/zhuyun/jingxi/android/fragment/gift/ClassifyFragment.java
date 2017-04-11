package com.zhuyun.jingxi.android.fragment.gift;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.GiftClassifyEntity;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.HotActivity;
import com.zhuyun.jingxi.android.adapter.MyGridAdapter;
import com.zhuyun.jingxi.android.base.BaseFragment;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.HeaderGridView;

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
 * Created by user0081 on 2016/6/13.   礼品屋_专区
 */
public class ClassifyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener  {
    private List<GiftClassifyEntity> lists= new ArrayList();
    private SwipeRefreshLayout classify_swip_refresh;
    private HeaderGridView classify_grid;
    private MyGridAdapter adapter;
    private Thread thread;
    @Override
    protected void initView(View view, Bundle bundle) {
        View head = View.inflate(context, R.layout.layout_classify_head,null);
        classify_grid= (HeaderGridView) view.findViewById(R.id.classify_grid);
        classify_grid.addHeaderView(head);
        classify_swip_refresh= (SwipeRefreshLayout) view.findViewById(R.id.classify_swip_refresh);
        lists= App.getDBManager().selectGiftClassifyList();//查数据库缓存
        MyLogs.e("haifeng","查数据库完成 大小="+lists.size());
        initData();
//        selectDao();
        loadData();
    }

    @Override
    protected int setLayout() {
        return R.layout.layout_gift_classify;
    }

    @Override
    protected void onClickEvent(View view) {

    }

    private void initData() {
        adapter = new MyGridAdapter(context, lists);
        classify_grid.setAdapter(adapter);
        classify_grid.setSelector(R.color.white);//去点击状态原生背景色
        classify_swip_refresh.setOnRefreshListener(this);
        classify_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLogs.e("haifeng", "点击坐标="+(position-3));
                Intent intent=  new Intent(context,HotActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("catId",lists.get(position-3).getClassiftyId());
                bundle.putString("title",lists.get(position-3).getName());
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
            classify_swip_refresh.setRefreshing(false);
            MyLogs.e("haifeng","断网");
        }

    }

    private void netData() {

        MyLogs.e("haifeng","走礼品屋_分类接口");
        HttpClient.requestGetAsyncHeader(Url.GIFT_CLASSIFY_INFO, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");
                CommanUtil.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        classify_swip_refresh.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "分类接口，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("datas");
                            if (jsonArray != null) {
                                lists.clear();
                                App.getDBManager().clearGiftClassifyList();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final GiftClassifyEntity bean = new GiftClassifyEntity();
                                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                                    bean.setClassiftyId(jsonObject2.optLong("id"));
                                    bean.setName(jsonObject2.optString("name"));
                                    bean.setImgIconUrl(jsonObject2.optString("imgIconUrl"));
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
                                        adapter.notifyDataSetChanged();
                                        classify_swip_refresh.setRefreshing(false);
                                        insertDao(lists);
                                    }
                                });
                            }
                        }else {
                            MyLogs.e("haifeng","errCode不对");
                            classify_swip_refresh.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        MyLogs.e("haifeng","走了异常 :"+e);
                         classify_swip_refresh.setRefreshing(false);
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");
                    classify_swip_refresh.setRefreshing(false);

                }
            }
        });
    }

    //查数据库
    private void selectDao() {

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                lists= App.getDBManager().selectGiftClassifyList();//查数据库缓存
                MyLogs.e("haifeng","查数据库完成 大小="+lists.size());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                adapter = new MyGridAdapter(context, lists);
                classify_grid.setAdapter(adapter);
                classify_grid.setSelector(R.color.white);//去点击状态原生背景色
                MyLogs.e("haifeng","查数据库    完成 notifyDataSetChanged");
            }
        }.execute();
    }

    //插入数据库
    private void insertDao(final List<GiftClassifyEntity> lists) {
        MyLogs.e("haifeng","数据库操作");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < lists.size(); i++) {
                    App.getDBManager().insertGiftClassifyList(lists.get(i));
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
        MyLogs.e("haifeng","classifyFragment销毁");
    }

}
