package com.zhuyun.jingxi.android.activty;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.EaseUser;
import com.squareup.picasso.Picasso;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.http.HttpClient;
import com.zhuyun.jingxi.android.http.Url;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.SharedPreUtils;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.CycleWheelView;
import com.zhuyun.jingxi.android.view.SelectPicPopupWindow;
import com.zhuyun.jingxi.android.view.myroundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user0081 on 2016/7/1.  个人信息
 */
public class MyPersonActivity extends BaseActivity{

    private RoundedImageView my_head;
    private TextView my_nickname,my_brithday,my_constellation,my_address,my_mobile;
    private TextView person_sex;
    private AlertDialog dialog,dialog2;
    private CycleWheelView my_constellation_wheelview;
    private ImageView user_change_iv;
    private RotateAnimation rotateAnimation;
    List<String> labels = new ArrayList<>();//星座集合
    //自定义的弹出框类
    SelectPicPopupWindow menuWindow;
    private File outPutPhoto;
    private Uri photoUri;
    private File outPutPhotos;
    private File imagePath;
    private Uri uri;
    private ImageView man_box,woman_box;
    private long userId;
    private String smallHeadImgPath;
    private String nickName;
    private int gender=0;
    private long brithday;
    private String address;
    private String user_login_name,mobile;
    private String constellation;
    private int constellationPosition;
    private static final int DATE_PICKER_ID=1;//该常量用于标识DatePickerDialog
    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入 个人信息");
        my_head= (RoundedImageView) findViewById(R.id.my_head);
        my_nickname = (TextView) findViewById(R.id.my_nickname);
        person_sex= (TextView) findViewById(R.id.person_sex);
        my_brithday = (TextView) findViewById(R.id.my_brithday);
        my_constellation= (TextView) findViewById(R.id.my_constellation);
        my_address= (TextView) findViewById(R.id.my_address);
        my_mobile = (TextView) findViewById(R.id.my_mobile);
        initData();
        setViewListener();
    }

    private void initData() {
        /**正在加载图片*/
        user_change_iv= (ImageView)findViewById(R.id.user_change_iv);
        /**旋转动画*/
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(App.getAppContext(), R.anim.rotate_refresh_drawable_default);
        /**星座*/
        for (int i = 0; i < 12; i++) {
            labels.add(CommanUtil.getConstellation(i));
        }
        userId = (long) SharedPreUtils.get(App.getAppContext(), "user_id", (long)0);
        if (userId!=0) {
            smallHeadImgPath = SharedPreUtils.get(App.getAppContext(), "smallHeadImgPath", "").toString();
            nickName = SharedPreUtils.get(App.getAppContext(), "nick_name", "未填写").toString();
            gender = (int) SharedPreUtils.get(App.getAppContext(), "gender", 0);
            brithday = (long) SharedPreUtils.get(App.getAppContext(), "birthday",(long)0);
            constellationPosition = (int) SharedPreUtils.get(App.getAppContext(), "constellation", 0);
            address = SharedPreUtils.get(App.getAppContext(), "address", "未填写").toString();
            mobile = SharedPreUtils.get(App.getAppContext(), "mobile", "未填写").toString();

            MyLogs.e("haifeng","进入 个人信息<< smallHeadImgPath="+smallHeadImgPath+"  brithday="+brithday);
            if (!smallHeadImgPath.equals("")) {
                /**测试用*/
                Picasso.with(App.getAppContext()).load(new File(smallHeadImgPath)).into(my_head);
//            Picasso.with(App.getAppContext()).load(smallHeadImgPath).into(my_head);
            }
            if (nickName.equals("")) {
                my_nickname.setText("未填写");
            } else {
                my_nickname.setText(nickName);
            }
            if (gender==0) {
                person_sex.setText("女");
            } else if (gender==1) {
                person_sex.setText("男");
            }
            if (brithday==0) {
                my_brithday.setText("未填写");
            } else {
                my_brithday.setText(CommanUtil.transhms(brithday,"yyyy.MM.dd"));
            }
            if (address.equals("")) {
                my_address.setText("未填写");
            } else {
                my_address.setText(address);
            }
            my_constellation.setText(CommanUtil.getConstellation(constellationPosition));
            my_mobile.setText(mobile.substring(0, 3) + "*****" + mobile.substring(8, mobile.length()));
        }
    }

    private void setViewListener() {
        findViewById(R.id.myperson_back).setOnClickListener(this);
        findViewById(R.id.edit_head).setOnClickListener(this);
        findViewById(R.id.edit_nick).setOnClickListener(this);
        findViewById(R.id.edit_sex).setOnClickListener(this);
        findViewById(R.id.edit_brithday).setOnClickListener(this);
        findViewById(R.id.edit_constellation).setOnClickListener(this);
        findViewById(R.id.edit_address).setOnClickListener(this);
        findViewById(R.id.edit_mobile).setOnClickListener(this);

    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_my_person_activity);
    }

    @Override
    protected void onClickEvent(View v) {
        switch (v.getId()){
            case R.id.myperson_back:
                MyPersonActivity.this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.edit_head://头像
                //弹出底部PopupWindow
                menuWindow = new SelectPicPopupWindow(this, itemsOnClick, R.layout.layout_my_addpic_pop);
                menuWindow.showAtLocation(this.findViewById(R.id.my_rl_main_edit), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在

                break;
            case R.id.edit_nick://昵称
                Intent intent3 = new Intent(MyPersonActivity.this, MyNickNameEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("oldeName",my_nickname.getText().toString());
                intent3.putExtras(bundle);
                startActivityForResult(intent3, 200);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.edit_sex://性别
                gender= (int) SharedPreUtils.get(App.getAppContext(),  "gender",0);
                dialog = new AlertDialog.Builder(MyPersonActivity.this).create();
                dialog.show();
                dialog.setCancelable(true);
                Window window=dialog.getWindow();
                View dialogView=View .inflate(MyPersonActivity.this, R.layout.layout_select_sex_dialog, null);
                window.setContentView(dialogView);
                man_box = (ImageView) dialogView.findViewById(R.id.man_box);
                woman_box= (ImageView)dialogView.findViewById(R.id.woman_box);
                if (gender==0){
                    woman_box.setBackgroundResource(R.drawable.shixin);
                }else if (gender==1){
                    man_box.setBackgroundResource(R.drawable.shixin);
                }
                dialogView.findViewById(R.id.select_sex_man).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String,Object> params = new HashMap<String,Object>();
                        params.put("gender", "1");
                        changUserInfo(params,2);
                        dialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.select_sex_women).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> params = new HashMap<String,Object>();
                        params.put("gender", "0");
                        changUserInfo(params,3);
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.edit_brithday://生日
                MyLogs.e("haifeng","点击生日一次");
                showDialog(DATE_PICKER_ID);//此方法会继续调用下面的onCreateDialog（）方法将id传入（id是前面定义的一个常量）
                break;
            case R.id.edit_constellation://星座
                dialog2= new AlertDialog.Builder(MyPersonActivity.this).create();
                dialog2.show();
                dialog2.setCancelable(true);
                Window window2=dialog2.getWindow();
                View dialogView2=View .inflate(MyPersonActivity.this, R.layout.layout_select_constellation_dialog, null);
                window2.setContentView(dialogView2);
                my_constellation_wheelview= (CycleWheelView) dialogView2.findViewById(R.id.my_constellation_wheelview);
                try {
                    my_constellation_wheelview.setWheelSize(4);
                } catch (CycleWheelView.CycleWheelViewException e) {
                    e.printStackTrace();
                }
                my_constellation_wheelview.setLabels(labels);
                my_constellation_wheelview.setAlphaGradual(0.5f);
                my_constellation_wheelview.setCycleEnable(true);
                my_constellation_wheelview.setDivider(Color.parseColor("#abcdef"), 2);
                my_constellation_wheelview.setSolid(Color.WHITE,Color.WHITE);
                my_constellation_wheelview.setLabelColor(getResources().getColor(R.color.im_head_black));
                my_constellation_wheelview.setLabelSelectColor(Color.RED);
                my_constellation_wheelview.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
                    @Override
                    public void onItemSelected(int position, String label) {
                        constellation=label;
                        constellationPosition=position;

                    }
                });
                dialogView2.findViewById(R.id.select_constellation_finish).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyLogs.e("haifeng","constellation1="+constellation);
                        MyLogs.e("haifeng","constellation2="+CommanUtil.getConstellation(constellationPosition));
                        Map<String,Object> params = new HashMap<String,Object>();
                        params.put("constellation", constellationPosition);
                        changUserInfo(params,5);
                        dialog2.dismiss();
                    }
                });

                break;
            case R.id.edit_address://地址
                Intent intent2 = new Intent(MyPersonActivity.this, MyProvenceActivity.class);
                startActivityForResult(intent2, 202);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.edit_mobile://手机号
                Intent intent7 = new Intent(MyPersonActivity.this, MyMobileEditActivity.class);
                Bundle bundle7 = new Bundle();
                bundle7.putString("oldeMobile",mobile);
                intent7.putExtras(bundle7);
                startActivityForResult(intent7, 201);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    //生日Dialog
    protected Dialog onCreateDialog(int id){
        switch (id) {
            case DATE_PICKER_ID:
                //onDateSetListener为用户点击设置时执行的回调函数，数字是默认显示的日期，注意月份设置11而实际显示12，会自动加1
                MyLogs.e("haifeng","生日一次");
                DatePickerDialog datePickerDialog= new DatePickerDialog(this,onDateSetListener,1990,9,11);
                datePickerDialog.setCancelable(true);
                return datePickerDialog;
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
            try {
                brithday=sdf.parse(year+"."+(monthOfYear+1)+"."+dayOfMonth).getTime();
                MyLogs.e("haifeng","birthday1="+brithday);
                MyLogs.e("haifeng","birthday2="+CommanUtil.transhms(brithday,"yyyy.MM.dd"));

                Map<String,Object> params = new HashMap<String,Object>();
                params.put("birthday", brithday);
                changUserInfo(params,4);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };


    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick =  new View.OnClickListener() {

        public void onClick(View v) {

            switch (v.getId()) {
                /**开启相机*/
                case R.id.btn_take_photo:
                    //防止内存溢出
                    MyLogs.e("jxf", "ImageLoader清理内存缓存");
//                    ImageLoader.getInstance().clearMemoryCache();
                    Intent intent5 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent5.putExtra(MediaStore.EXTRA_OUTPUT, getOutputPhoto());
                    startActivityForResult(intent5, 204);
                    menuWindow.dismiss();
                    break;
                /**开启相册*/
                case R.id.btn_pick_photo:
                    imagePath = null;
                    Intent intent4 = new Intent(Intent.ACTION_PICK);
                    intent4.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(intent4, 205);
                    menuWindow.dismiss();
                    break;
                /**关闭PopupWindow*/
                case R.id.cancel_addhead:
                    menuWindow.dismiss();
                    break;
                default:
                    break;
            }


        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 200:
                if (resultCode==20){//编辑昵称

                    nickName = data.getExtras().getString("nickName");
                    Map<String,Object> params = new HashMap<String,Object>();
                    params.put("nickName", nickName);
                    changUserInfo(params,1);
                }
                break;
            case 201:
                if (resultCode==21){//编辑手机号
                    mobile = data.getExtras().getString("mobile");
                    Map<String,Object> params = new HashMap<String,Object>();
                    params.put("mobile", mobile);
                    changUserInfo(params,7);
                }
                break;
            case 202:
                if (resultCode==22){//编辑地址
                    int countryId=data.getIntExtra("countryId",0);
                    int provenceId=data.getIntExtra("provenceId", 0);
                    int cityId=data.getIntExtra("cityId", 0);

                    address = data.getStringExtra("provenceName")+" "+data.getStringExtra("cityName");
                    Map<String,Object> params = new HashMap<String,Object>();
                    params.put("address",address);
                    changUserInfo(params,6);

                }
                break;
            case 204:
                if (resultCode==RESULT_OK){//拍照  ---防止拍照旋转问题（1.获取旋转角度；2.根据旋转角度处理图片）
                    MyLogs.e("jxf","打印保存照片地址path"+outPutPhoto.getAbsolutePath());
                    int degree=getBitmapDegree(outPutPhoto.getAbsolutePath());
                    MyLogs.e("jxf","打印旋转尺寸"+degree);
                    //使用大图做尺寸压缩
                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    // 从解码器中获取原始图片的宽高，这样避免了直接申请内存空间
//                    options.inJustDecodeBounds = true;
                    // Calculate inSampleSize
                    options.inSampleSize = 5;
//                    // 压缩完后便可以将inJustDecodeBounds设置为false了。
//                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    //做尺寸压缩
                    MyLogs.e("jxf","做尺寸压缩");
                    Bitmap bitmap = BitmapFactory.decodeFile(outPutPhoto.getAbsolutePath(), options);
                    MyLogs.e("jxf","bitmap大小打印"+(bitmap.getRowBytes() * bitmap.getHeight()));
                    // 根据旋转角度，生成旋转矩阵
                    Matrix matrix = new Matrix();
                    matrix.postRotate(degree);
                    MyLogs.e("jxf", "做角度变换了");
                    Bitmap returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    MyLogs.e("jxf","returnBm大小打印"+(returnBm.getRowBytes() * returnBm.getHeight()));
                    //压缩进原来的文件中
                    File imagePath = new File(getCacheDir(), "" + Calendar.getInstance().getTimeInMillis());// /sdcard/aijingxi/cropImage
                    File parentFile = imagePath.getParentFile();
                    if (!parentFile.exists()) {// 判断上级目录是否存在，不存在就需要创建
                        parentFile.mkdirs();
                    }
                    // 把Bitmap对象持久化到本地
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(imagePath);
                        returnBm.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        MyLogs.e("jxf", "做质量压缩压缩进硬盘");
                        os.flush();
                        os.close();
                    } catch (FileNotFoundException e) {
                        MyLogs.e("jxf","切图类1--bitmap图片压缩时，报FileNotFoundException异常");
                        e.printStackTrace();
                    } catch (IOException e) {
                        MyLogs.e("jxf","切图类1--bitmap图片压缩时，报IOException异常");
                        e.printStackTrace();
                    }
                    //释放
                    if (bitmap != null && !bitmap.isRecycled()) {
                        // 回收并且置为null
                        bitmap.recycle();
                        bitmap = null;
                        MyLogs.e("jxf", "bitmap回收");
                    }
                    System.gc();
                    //释放
                    if (returnBm != null && !returnBm.isRecycled()) {
                        // 回收并且置为null
                        returnBm.recycle();
                        returnBm = null;
                        MyLogs.e("jxf", "returnBm回收");
                    }
                    System.gc();
                    uri =saveImageToDICM(getApplicationContext(),
                            imagePath);
                    Intent intent = new Intent(this, CropActivity.class);
                    intent.putExtra("uri", uri.toString());
                    MyLogs.e("jxf","传递的uri=="+ uri.toString());
                    startActivityForResult(intent, 206);
                }
                break;
            case 205:
                if (resultCode==RESULT_OK){//相册
                    Uri uri= data.getData();
                    if (data == null) {
                        return;
                    }
                    photoUri = uri;
                    Intent intent = new Intent(this, CropActivity.class);
                    intent.putExtra("uri", uri.toString());
                    startActivityForResult(intent, 206);
                }
                break;
            case 206: //切图
                if (resultCode==RESULT_OK){
                    if (data == null) {
                        return;
                    }
                    String imageCropuri = data.getStringExtra("Cropuri");
                    MyLogs.e("jxf","打印切图返回的imageCropuri"+imageCropuri);
                    if (imageCropuri != null) {
                        outPutPhotos = new File(imageCropuri);
                        /**测试用*/
                        TempSingleToast.showToast(App.getAppContext(),"头像上传还没有，这只是本地的");
                        Picasso.with(App.getAppContext()).load(outPutPhotos).into(my_head);
                        SharedPreUtils.put(App.getAppContext(), "smallHeadImgPath",outPutPhotos);
//                        Map<String,Object> params = new HashMap<String,Object>();
//                        params.put("portrait", outPutPhotos);
//                        changUserInfo(params,0);

                    }
                }
                break;

        }
    }
    /**
     * 获取磁盘缓存路径
     * @param name 文件夹名称
     * @return 缓存路径
     */
    private   File getDiskCacheDir(Context context, String name) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File file = new File(context.getExternalCacheDir(), name);
            if (!file.exists())
                file.mkdirs();
            return file;
        } else {
            File file = new File(context.getCacheDir(), name);
            if (!file.exists())
                file.mkdirs();
            return file;
        }
    }

    public Uri getOutputPhoto() {
        if (photoUri == null || outPutPhoto == null) {
            String fileName = ""+ Calendar.getInstance().getTimeInMillis() +".JPG";
            outPutPhoto = new File(getDiskCacheDir(App.getAppContext(), "data"),
                    fileName);
            photoUri = Uri.fromFile(outPutPhoto);
        }
        return photoUri;
    }
    /**
     * 保存图片到系统相册
     * @param image
     */
    private Uri saveImageToDICM(Context context, File image) {
        Uri uri = null;
        // 保存到相册
        try {
            String uriString = MediaStore.Images.Media.insertImage(
                    context.getContentResolver(), image.getAbsolutePath(),
                    image.getName(), image.getName());

            uri = Uri.parse(uriString);
//            // 通知系统相册刷新
//            context.sendBroadcast(new Intent(
//                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(image)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            MyLogs.e("jxf","orientation"+orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    //修改个人信息接口
    private void changUserInfo(Map<String, Object> params, final int type) {

        MyLogs.e("haifeng","修改个人信息 params="+params);
        //请求网络
        if(CommanUtil.isNetworkAvailable()) {
            // 开始动画
            user_change_iv.setAnimation(rotateAnimation);
            user_change_iv.setVisibility(View.VISIBLE);
            try {
                HttpClient.requestPostAsyncHeader(Url.CHANG_USER_INFO, params, new Callback() {
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
                            MyLogs.e("haifeng","修改个人信息 :json="+json);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json);
                                final String errCode = jsonObject.optString("errCode");
                                if (errCode.equals("0")) {
                                    JSONObject  jsonObject2 = jsonObject.optJSONObject("datas");
                                    int flag = jsonObject2.optInt("flag");
                                    if (flag==1) {
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                user_change_iv.clearAnimation();
                                                user_change_iv.setVisibility(View.GONE);
                                                switch (type){
                                                    case 0://头像
                                                        MyLogs.e("haifeng", "修改头像成功");
                                                        Picasso.with(App.getAppContext()).load(outPutPhotos).into(my_head);
                                                        SharedPreUtils.put(App.getAppContext(), "smallHeadImgPath", "   ");
                                                        EaseUser user = new EaseUser(CommanUtil.md5Hex(userId+"",true)+userId);
                                                        user.setAvatar("http://www.99aijingxi.com:8080/aijingxiimage/wish/friend.png");
                                                        DemoHelper.getInstance().saveContact(user);
                                                        break;
                                                    case 1://昵称
                                                        MyLogs.e("haifeng", "修改昵称成功");
                                                        my_nickname.setText(nickName);
                                                        SharedPreUtils.put(App.getAppContext(), "nick_name", nickName);
                                                        EaseUser user2 = new EaseUser(CommanUtil.md5Hex(userId+"",true)+userId);
                                                        user2.setNick(nickName);
                                                        DemoHelper.getInstance().saveContact(user2);

                                                        break;
                                                    case 2://性别  男
                                                        MyLogs.e("haifeng", "修改性别1成功");
                                                        person_sex.setText("男");
                                                        man_box.setBackgroundResource(R.drawable.shixin);
                                                        SharedPreUtils.put(App.getAppContext(), "gender", 1);

                                                        break;
                                                    case 3://性别  女
                                                        MyLogs.e("haifeng", "修改性别2成功");
                                                        person_sex.setText("女");
                                                        woman_box.setBackgroundResource(R.drawable.shixin);
                                                        SharedPreUtils.put(App.getAppContext(), "gender", 0);

                                                        break;
                                                    case 4://生日
                                                        MyLogs.e("haifeng", "修改生日成功");
                                                        my_brithday.setText(CommanUtil.transhms(brithday,"yyyy.MM.dd"));
                                                        SharedPreUtils.put(App.getAppContext(), "birthday", brithday);
                                                        break;
                                                    case 5://星座
                                                        MyLogs.e("haifeng", "修改星座成功");
                                                        my_constellation.setText(CommanUtil.getConstellation(constellationPosition));
                                                        SharedPreUtils.put(App.getAppContext(), "constellation", constellationPosition);
                                                        break;
                                                    case 6://地区
                                                        MyLogs.e("haifeng", "修改地区成功");
                                                        my_address.setText(address);
                                                        SharedPreUtils.put(App.getAppContext(), "address", address);
                                                        break;
                                                    case 7://手机号
                                                        MyLogs.e("haifeng", "修改手机号成功");
                                                        my_mobile.setText(mobile.substring(0,3)+"*****"+mobile.substring(8,mobile.length()));
                                                        SharedPreUtils.put(App.getAppContext(), "mobile", mobile);
                                                        break;
                                                }

                                            }
                                        });
                                    }else if (flag==0){
                                        MyLogs.e("haifeng", "修改失败");
                                        CommanUtil.runOnUIThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                TempSingleToast.showToast(App.getAppContext(),"修改失败");
                                                user_change_iv.clearAnimation();
                                                user_change_iv.setVisibility(View.GONE);
                                            }
                                        });
                                    }



                                }else {
                                    MyLogs.e("haifeng", "修改失败 errorCode="+errCode);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            MyLogs.e("haifeng","response.code()！=200");

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

}
