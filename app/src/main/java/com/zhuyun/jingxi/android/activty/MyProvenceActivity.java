package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.ProvenceActivityAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.ProvenceBean;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user0081 on 2016/7/5.   省级
 */
public class MyProvenceActivity extends BaseActivity{
    private ListView address_lv;
    private  ProvenceActivityAdapter adapter;
    private ArrayList<ProvenceBean> provences=new ArrayList();
    //保存跳转需要传递的值
    private int provenceId;
    private String provenceName;
    private int countryId;

    @Override
    protected void initView() {
        findViewById(R.id.address_back).setOnClickListener(this);
        address_lv = (ListView) findViewById(R.id.address_lv);
        initData();
    }

    private void initData() {
        relationDb();
        adapter = new ProvenceActivityAdapter(this,provences);
        address_lv.setAdapter(adapter);
        address_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provenceId = provences.get(position).provenceId;
                provenceName = provences.get(position).provenceName;
                countryId = provences.get(position).countryId;
                //携带省的id
                Intent intent = new Intent(MyProvenceActivity.this, MyCityActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("provenceId", provenceId);
                intent.putExtras(bundle);
                startActivityForResult(intent, 203);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });
    }
    //关联本地数据库
    private void relationDb() {
        File databaseFilename=new File(App.getAppContext().getFilesDir().getPath(),App.LOC_DBNAME);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        Cursor cs=db.query("province", new String[]{"id","name","country_id"}, null,null, null, null, null);
        //db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
        ProvenceBean provenceBean=null;
        if(cs!=null&&cs.getCount()>0){
            while(cs.moveToNext()){
                provenceBean=new ProvenceBean();
                provenceBean.provenceId=cs.getInt(0);
                provenceBean.provenceName=cs.getString(1);
                provenceBean.countryId=cs.getInt(2);
                provences.add(provenceBean);
            }
        }
        cs.close();
        db.close();
//        adapter.notifyDataSetChanged();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==203&&resultCode==23){
            String cityName=data.getStringExtra("cityName");
            int cityId=data.getIntExtra("cityId", 0);
            Intent intent = new Intent();
            intent.putExtra("provenceName", provenceName);
            intent.putExtra("cityName", cityName);
            intent.putExtra("countryId", countryId);
            intent.putExtra("provenceId", provenceId);
            intent.putExtra("cityId", cityId);
            MyProvenceActivity.this.setResult(22, intent);
            MyProvenceActivity.this.finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }
}
