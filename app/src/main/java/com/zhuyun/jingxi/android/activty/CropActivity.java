package com.zhuyun.jingxi.android.activty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.cropimg.ClipImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by user0081 on 2016/7/5.  切图
 */
public class CropActivity extends BaseActivity {
    private ClipImageView imageView;
    @Override
    protected void initView() {
//        ImageLoader.getInstance().clearMemoryCache();
//        Picasso.
        imageView = (ClipImageView) findViewById(R.id.src_pic);
        final String uri = getIntent().getStringExtra("uri");
        MyLogs.e("jxf","要切的uri=="+ uri.toString());
//        ImageLoader.getInstance().displayImage(uri, imageView, ImageLoaderOptions.optionsCrop);
        Picasso.with(App.getAppContext()).load(uri).into(imageView);

        findViewById(R.id.bt_cancle).setOnClickListener(this);
        findViewById(R.id.bt_complete).setOnClickListener(this);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_cropimg_activity);
    }

    @Override
    protected void onClickEvent(View v) {
       switch (v.getId()){
           case R.id.bt_cancle://取消
               CropActivity.this.finish();
               break;
           case R.id.bt_complete://选取  注意OOM
               MyLogs.e("jxf","点击--切图");
               Bitmap bitmap = null;
               try {
                   bitmap = imageView.clip();//切图
                   MyLogs.e("jxf", "切图后，bitmap大小打印" + (bitmap.getRowBytes() * bitmap.getHeight()));
               }catch (OutOfMemoryError e){
                   MyLogs.e("jxf","切图类--切图完成时，报OOM异常");
                   TempSingleToast.showToast(App.getAppContext(),"内存不够，请重新加载");
                   e.printStackTrace();
               }

               //String CACHE_DIR = "/CacheDir/aijingxi";// 本地缓存目录
               File imagePath = new File(getCacheDir(), "" + Calendar.getInstance().getTimeInMillis()+".jpg");// /sdcard/aijingxi/cropImage
               File parentFile = imagePath.getParentFile();
               if (!parentFile.exists()) {// 判断上级目录是否存在，不存在就需要创建
                   parentFile.mkdirs();
               }

               // 把Bitmap对象持久化到本地
               OutputStream os = null;
               try {
                   os = new FileOutputStream(imagePath);
                   bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                   os.flush();
                   os.close();
               } catch (FileNotFoundException e) {
                   MyLogs.e("jxf","切图类--bitmap图片压缩时，报FileNotFoundException异常");
                   e.printStackTrace();
               } catch (IOException e) {
                   MyLogs.e("jxf","切图类--bitmap图片压缩时，报IOException异常");
                   e.printStackTrace();
               }

               //释放
               if (bitmap != null && !bitmap.isRecycled()) {
                   // 回收并且置为null
                   bitmap.recycle();
                   bitmap = null;
                   MyLogs.e("jxf", "CropActivity: bitmap回收");
               }
               System.gc();

               Intent intent = new Intent();
               intent.putExtra("Cropuri", imagePath.getAbsolutePath());
               CropActivity.this.setResult(RESULT_OK, intent);
               CropActivity.this.finish();

               break;
       }
    }
}
