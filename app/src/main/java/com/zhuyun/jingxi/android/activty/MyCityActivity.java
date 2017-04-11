package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.CityActivityAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.CityBean;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user0081 on 2016/7/5. 市级
 */
public class MyCityActivity extends BaseActivity{
    private ListView address_lv;
    private int provenceId;
    private ArrayList<CityBean> citys=new ArrayList();

    @Override
    protected void initView() {
        provenceId=getIntent().getExtras().getInt("provenceId");
        findViewById(R.id.address_back).setOnClickListener(this);
        address_lv = (ListView) findViewById(R.id.address_lv);
        initData();
    }

    private void initData() {
        relationDb();
        CityActivityAdapter adapter=new CityActivityAdapter(this,citys);
        address_lv.setAdapter(adapter);
        address_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("cityName", citys.get(position).cityName);
                intent.putExtra("cityId", citys.get(position).cityId);
                MyCityActivity.this.setResult(23,intent);
                MyCityActivity.this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    private void relationDb() {
        File databaseFilename=new File(App.getAppContext().getFilesDir().getPath(), App.LOC_DBNAME);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        Cursor cs=db.query("city", new String[]{"id","name"}, "province_id=?",new String[]{String.valueOf(provenceId)}, null, null, null);
        //db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
        CityBean cityBean=null;
        if(cs!=null&&cs.getCount()>0){
            while(cs.moveToNext()){
                cityBean=new CityBean();
                cityBean.cityId=cs.getInt(0);
                cityBean.cityName=cs.getString(1);
                citys.add(cityBean);
            }
        }
        cs.close();
        db.close();

    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_address_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.address_back:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
    }
}
