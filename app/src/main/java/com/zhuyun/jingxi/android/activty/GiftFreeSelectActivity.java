package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.GiftSearch1GridAdapter;
import com.zhuyun.jingxi.android.adapter.GiftSearch2GridAdapter;
import com.zhuyun.jingxi.android.adapter.GiftSearch3GridAdapter;
import com.zhuyun.jingxi.android.adapter.GiftSearch4GridAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.GiftSearchBean;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.view.MyGridView;

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
 * Created by user0081 on 2016/7/7.   随心选
 */
public class GiftFreeSelectActivity extends BaseActivity{
    private MyGridView search_price_grid,search_scene_grid,search_festival_grid,search_object_grid;
    private GiftSearch1GridAdapter adapter1;
    private GiftSearch2GridAdapter adapter2;
    private GiftSearch3GridAdapter adapter3;
    private GiftSearch4GridAdapter adapter4;
    private List<GiftSearchBean> lists1=new ArrayList<GiftSearchBean>();
    private List<GiftSearchBean> lists2=new ArrayList<GiftSearchBean>();
    private List<GiftSearchBean> lists3=new ArrayList<GiftSearchBean>();
    private List<GiftSearchBean> lists4=new ArrayList<GiftSearchBean>();
    private EditText search_lowest_price,search_highest_price;
    private String price,scene,festival,object;
    private int selectType=1;
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入随心选");
        search_price_grid= (MyGridView) findViewById(R.id.search_price_grid);//价格
        search_scene_grid= (MyGridView) findViewById(R.id.search_scene_grid);//场景
        search_festival_grid= (MyGridView) findViewById(R.id.search_festival_grid);//节日
        search_object_grid= (MyGridView) findViewById(R.id.search_object_grid);//对象

        search_lowest_price= (EditText) findViewById(R.id.search_lowest_price);
        search_highest_price= (EditText) findViewById(R.id.search_highest_price);
        initData();
        loadData();
        setViewListener();
    }

    private void initData() {

        /**假数据*/
        //价格
        for (int i = 1; i <9 ; i++) {
            GiftSearchBean bean=new GiftSearchBean();
            bean.isSelected=false;
            bean.name="￥"+i+"00-"+(i+1)+"00";
            lists1.add(bean);
        }
        adapter1 = new GiftSearch1GridAdapter(this,lists1);
        search_price_grid.setAdapter(adapter1);
        search_price_grid.setSelector(R.color.white);
        //场景
//        for (int i = 1; i <7 ; i++) {
//            GiftSearchBean bean=new GiftSearchBean();
//            bean.isSelected=false;
//            bean.text="在旅游"+i;
//            lists2.add(bean);
//        }
        adapter2 = new GiftSearch2GridAdapter(this,lists2);
        search_scene_grid.setAdapter(adapter2);
        search_scene_grid.setSelector(R.color.white);

        //节日
//        for (int i = 1; i <9 ; i++) {
//            GiftSearchBean bean=new GiftSearchBean();
//            bean.isSelected=false;
//            bean.text="情人节"+i;
//            lists3.add(bean);
//        }
        adapter3 = new GiftSearch3GridAdapter(this,lists3);
        search_festival_grid.setAdapter(adapter3);
        search_festival_grid.setSelector(R.color.white);

        //对象
//        for (int i = 1; i <9 ; i++) {
//            GiftSearchBean bean=new GiftSearchBean();
//            bean.isSelected=false;
//            bean.text="女友"+i+"号";
//            lists4.add(bean);
//        }
        adapter4 = new GiftSearch4GridAdapter(this,lists4);
        search_object_grid.setAdapter(adapter4);
        search_object_grid.setSelector(R.color.white);
    }

    private void setViewListener() {
        findViewById(R.id.free_select_back).setOnClickListener(this);
        findViewById(R.id.search_reset).setOnClickListener(this);
        findViewById(R.id.search_sure).setOnClickListener(this);

        search_highest_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){//获取焦点
                    MyLogs.e("haifeng","EditText获取焦点");
                    for (int i = 0; i < lists1.size(); i++) {
                        lists1.get(i).isSelected=false;
                        price="未选";
                        adapter1.setCurrentPosition(-1);
                    }
                    adapter1.setCurrentPosition(-1);
                    adapter1.notifyDataSetChanged();
                }else {
                    MyLogs.e("haifeng","EditText失去焦点");
                }
            }
        });
        search_lowest_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){//获取焦点
                    MyLogs.e("haifeng","EditText获取焦点");
                    for (int i = 0; i < lists1.size(); i++) {
                        lists1.get(i).isSelected=false;
                    }
                    adapter1.notifyDataSetChanged();
                }else {
                    MyLogs.e("haifeng","EditText失去焦点");
                }
            }
        });
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_gift_freeselect_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.free_select_back:
                finish();
                break;
            case R.id.search_reset:
                MyLogs.e("haifeng","重置");
                search_highest_price.setText("");
                search_lowest_price.setText("");
                for (int i = 0; i < lists1.size(); i++) {
                    lists1.get(i).isSelected=false;
                }
                for (int i = 0; i < lists2.size(); i++) {
                    lists2.get(i).isSelected=false;
                }
                for (int i = 0; i < lists3.size(); i++) {
                    lists3.get(i).isSelected=false;
                }
                for (int i = 0; i < lists4.size(); i++) {
                    lists4.get(i).isSelected=false;
                }
                adapter1.setCurrentPosition(-1);
                adapter2.setCurrentPosition(-1);
                adapter3.setCurrentPosition(-1);
                adapter4.setCurrentPosition(-1);
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                adapter3.notifyDataSetChanged();
                adapter4.notifyDataSetChanged();
                break;
            case R.id.search_sure:
                MyLogs.e("haifeng", "selectType=="+selectType);//1:价格  2：场景  3：节日   4：对象

                if (!search_highest_price.getText().toString().equals("") && !search_lowest_price.getText().toString().equals("")) {
                    if ( Integer.parseInt(search_lowest_price.getText().toString())<= Integer.parseInt(search_highest_price.getText().toString())){
                        MyLogs.e("haifeng", "输入框不空");
                        price = search_lowest_price.getText().toString() + "-" + search_highest_price.getText().toString();
                    }else {
                        price="价格输入错误";
                    }

                } else if (adapter1.getCurrentPosition() != -1) {
                    MyLogs.e("haifeng", "输入狂空，选中不空"+adapter1.getCurrentPosition());
                    price = lists1.get(adapter1.getCurrentPosition()).name;
                }

                MyLogs.e("haifeng", "选中selectType=="+selectType);
                if (selectType==2){
                    if (adapter2.getCurrentPosition()!=-1){
                        object= lists2.get(adapter2.getCurrentPosition()).name;
                    }
                }
                if (selectType==3){
                    if (adapter3.getCurrentPosition()!=-1){
                        object= lists3.get(adapter3.getCurrentPosition()).name;
                    }
                }
                if (selectType==4){
                    MyLogs.e("haifeng", "selectType==4  选中位置"+adapter4.getCurrentPosition());
                    if (adapter4.getCurrentPosition()!=-1){
                        object= lists4.get(adapter4.getCurrentPosition()).name;
                    }

                }



//                if (adapter2.getCurrentPosition()!=-1){
//                    object= lists2.get(adapter2.getCurrentPosition()).text;
//                }else if (adapter3.getCurrentPosition()!=-1){
//                    object= lists3.get(adapter3.getCurrentPosition()).text;
//                }else if (adapter4.getCurrentPosition()!=-1){
//                    object= lists4.get(adapter4.getCurrentPosition()).text;
//                }else {
//                    object="未选";
//                }
                MyLogs.e("haifeng"," 价格："+price+"  条件："+object);
                Intent intent=  new Intent(GiftFreeSelectActivity.this,GiftSearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("searchText"," 价格："+price+"  条件："+object);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                finish();
                break;
        }
    }

    private void loadData() {
        MyLogs.e("haifeng","走获取节日、场景和对象列表接口");
        HttpClient.requestGetAsyncHeader(Url.GIFT_SELECT_INFO, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLogs.e("haifeng","走onFailure");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MyLogs.e("haifeng", "response.code()=" + response.code());
                if (response.code()==200) {
                    String json = response.body().string();
                    MyLogs.e("haifeng", "走获取节日、场景和对象列表接口，json=" + json);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        final String errCode = jsonObject.optString("errCode");
                        if (errCode.equals("0")) {
                            JSONObject jsonObject2 = jsonObject.optJSONObject("datas");
                            JSONArray jsonArray1=jsonObject2.getJSONArray("festivals");//节日
                            JSONArray jsonArray2=jsonObject2.getJSONArray("scenes");//场景
                            JSONArray jsonArray3=jsonObject2.getJSONArray("partneres");//对象
                            //节日
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                GiftSearchBean bean=new GiftSearchBean();
                                JSONObject jsonObject3 = jsonArray1.optJSONObject(i);
                                 bean.id=jsonObject3.optLong("id");
                                 bean.name=jsonObject3.optString("name");
                                 bean.isSelected=false;
                                lists3.add(bean);
                            }
                            //场景
                            for (int i = 0; i < jsonArray2.length(); i++) {
                                GiftSearchBean bean=new GiftSearchBean();
                                JSONObject jsonObject4 = jsonArray2.optJSONObject(i);
                                bean.id=jsonObject4.optLong("id");
                                bean.name=jsonObject4.optString("name");
                                bean.isSelected=false;
                                lists2.add(bean);
                            }
                            //对象
                            for (int i = 0; i < jsonArray3.length(); i++) {
                                GiftSearchBean bean=new GiftSearchBean();
                                JSONObject jsonObject5 = jsonArray3.optJSONObject(i);
                                bean.id=jsonObject5.optLong("id");
                                bean.name=jsonObject5.optString("name");
                                bean.isSelected=false;
                                lists4.add(bean);
                            }
                            MyLogs.e("haifeng","解析完成 ");
                            CommanUtil.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter2.notifyDataSetChanged();
                                    adapter3.notifyDataSetChanged();
                                    adapter4.notifyDataSetChanged();
                                    MyLogs.e("haifeng","更新完成完成 ");
                                }
                            });

                        }

                    } catch (JSONException e) {
                        MyLogs.e("haifeng","走了异常 :"+e);
                        e.printStackTrace();
                    }

                }else {
                    MyLogs.e("haifeng","返回失败：response.code()！=200");

                }
            }
        });
    }

    public void setEditUnFocus(int type) {
        MyLogs.e("haifeng", "type=="+type);
        selectType=type;
        //1:价格  2：场景  3：节日   4：对象
        if (type==1) {
            search_highest_price.setText("");
            search_lowest_price.setText("");
//            search_highest_price.setFocusable(false);
//            search_highest_price.setFocusableInTouchMode(false);
//            search_highest_price.clearFocus();
//
//            search_lowest_price.setFocusable(false);
//            search_lowest_price.setFocusableInTouchMode(false);
//            search_lowest_price.clearFocus();
        } else if (type==2) {
            for (int i = 0; i < lists3.size(); i++) {
                lists3.get(i).isSelected=false;
            }
            for (int i = 0; i < lists4.size(); i++) {
                lists4.get(i).isSelected=false;
            }
            adapter3.setCurrentPosition(-1);
            adapter4.setCurrentPosition(-1);
            adapter3.notifyDataSetChanged();
            adapter4.notifyDataSetChanged();
        } else if (type==3) {
            for (int i = 0; i < lists2.size(); i++) {
                lists2.get(i).isSelected=false;
            }
            for (int i = 0; i < lists4.size(); i++) {
                lists4.get(i).isSelected=false;
            }
            adapter2.setCurrentPosition(-1);
            adapter4.setCurrentPosition(-1);
            adapter2.notifyDataSetChanged();
            adapter4.notifyDataSetChanged();
        } else if (type==4) {
            for (int i = 0; i < lists2.size(); i++) {
                lists2.get(i).isSelected=false;
            }
            for (int i = 0; i < lists3.size(); i++) {
                lists3.get(i).isSelected=false;
            }
            adapter2.setCurrentPosition(-1);
            adapter3.setCurrentPosition(-1);
            adapter2.notifyDataSetChanged();
            adapter3.notifyDataSetChanged();
        }
    }

}
