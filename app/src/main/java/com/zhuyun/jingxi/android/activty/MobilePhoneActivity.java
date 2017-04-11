package com.zhuyun.jingxi.android.activty;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.adapter.FriendsPhoneAdapter;
import com.zhuyun.jingxi.android.base.BaseActivity;
import com.zhuyun.jingxi.android.bean.PhoneBean;
import com.zhuyun.jingxi.android.utils.CommanUtil;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;
import com.zhuyun.jingxi.android.view.QuickIndexBar;

import java.util.ArrayList;

/**
 * Created by user0081 on 2016/6/26.  手机联系人
 */
public class MobilePhoneActivity extends BaseActivity {
    private ListView friends_phone_listview;
    private  String from;
    private QuickIndexBar quickIndexBar;//字母显示
    private ArrayList<PhoneBean> lists = new ArrayList<PhoneBean>();//存储本地手机联系人
    private PhoneBean bean;//手机联系人姓名
    private FriendsPhoneAdapter adapter;
    private boolean flag = true;
    private boolean onlyOne = true;
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    private BroadcastReceiver sendMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("haifeng", "send。。" + intent.getAction() + "---" + getResultCode() + "---" + getResultData());
            //判断短信是否发送成功
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    onlyOne=false;
//                    TempSingleToast.showToast(App.getAppContext(),"短信发送成功");
                    MyLogs.e("haifeng","短信发送成功");
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    onlyOne=true;
                    TempSingleToast.showToast(App.getAppContext(),"发送失败");
                    MyLogs.e("haifeng","发送失败");
                    break;
            }
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("haifeng", "receiver。。" + intent.getAction() + "---" + getResultCode() + "---" + getResultData());
            //表示对方成功收到短信
            TempSingleToast.showToast(App.getAppContext(),"对方接收成功");
            MyLogs.e("haifeng","对方接收成功");
        }
    };

    @Override
    protected void initView() {
        MyLogs.e("haifeng","进入手机通讯录");

        from = getIntent().getExtras().getString("from");//上一页
        friends_phone_listview= (ListView) findViewById(R.id.friends_phone_listview);
        quickIndexBar= (QuickIndexBar) findViewById(R.id.phone_quickIndexBar);
        initData();
        setViewListener();
    }

    private void setViewListener() {
       findViewById(R.id.phonefriend_back).setOnClickListener(this);

    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.layout_friends_phone_activity);

    }

    @Override
    protected void onClickEvent(View v) {
      switch (v.getId()){
          case R.id.phonefriend_back:
              MobilePhoneActivity.this.finish();
              break;
      }
    }

    private void initData() {
        //注册广播
        IntentFilter filter=new IntentFilter();
        filter.addAction(SENT_SMS_ACTION);//
        registerReceiver(sendMessage, filter);
        IntentFilter filter2=new IntentFilter();
        filter2.addAction(DELIVERED_SMS_ACTION);
        registerReceiver(receiver, filter2);


        adapter= new FriendsPhoneAdapter(this,lists,from);
        friends_phone_listview.setAdapter(adapter);
        if (from.equals("ReceiverMessaage")){
            friends_phone_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent();
                    intent.putExtra("mobile", lists.get(position).phoneNumber);
                    MobilePhoneActivity.this.setResult(27,intent);
                    MobilePhoneActivity.this.finish();
                }
            });
        }



        quickIndexBar.setVisibility(View.GONE);
        // 获得所有的联系人
        Cursor cur = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC");
        if (cur.getCount() < 1) {
            Log.e("haifeng", "读取联系人权限被禁止" );
            TempSingleToast.showToast(App.getAppContext(), "亲，读取联系人权限被禁止，请修改权限管理");
            if (flag) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");

                Uri uri = Uri.fromParts("package", "com.zhuyun.jingxi.android", null);
                intent.setData(uri);
                MobilePhoneActivity.this.startActivityForResult(intent, 0);
            }

            return;
        }
        // 循环遍历
        if (cur.moveToFirst()) {
            int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
            int displayNameColumn = cur
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            do {
                // 获得联系人的ID号
                String contactId = cur.getString(idColumn);
                // 获得联系人姓名
                String phoneName = cur.getString(displayNameColumn);
                String phoneNumber = null;

                // 查看该联系人有多少个电话号码。如果没有这返回值为0
                int phoneCount = cur
                        .getInt(cur
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (phoneCount > 0) {
                    // 获得联系人的电话号码
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = " + contactId, null, null);
                    if (phones.moveToFirst()) {
                        phoneNumber = phones
                                .getString(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String phoneType = phones
                                .getString(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    }
                    if (phones != null) {
                        phones.close();
                    }
                }

                //将联系人对象放进集合
                bean = new PhoneBean();
                bean.phoneName=phoneName;
                bean.phoneNumber=phoneNumber;
                bean.ischecked=false;
                lists.add(bean);
            } while (cur.moveToNext());

        }
        if (cur != null) {
            cur.close();
        }
        CommanUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * 参数说明
     * destinationAddress:收信人的手机号码
     * scAddress:发信人的手机号码
     * text:发送信息的内容
     * sentIntent:发送是否成功的回执，用于监听短信是否发送成功。
     * DeliveryIntent:接收是否成功的回执，用于监听短信对方是否接收成功。
     */


    public void sendMessage(int position){
        if (onlyOne){
            onlyOne=false;
            lists.get(position).ischecked=true;
            String phoneNumber=lists.get(position).phoneNumber;
            String message="成为我的好友吧，我再爱惊喜等你哦。";
//            SpannableStringBuilder builder=new SpannableStringBuilder(message);
//            builder.setSpan(new URLSpan("tel:18210097124"), message.length(), message.length(),
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//

//       //调系统发短信功能
//       Uri smsToUri = Uri.parse("smsto:"+lists.get(position).phoneNumber);
//       Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
//       intent.putExtra("sms_body", "");
//       MobilePhoneActivity.this.startActivityForResult(intent, 1);

            //直接发送短信
            SmsManager sms = SmsManager.getDefault();
            // 监听发送短信
            Intent sentIntent = new Intent(SENT_SMS_ACTION);
            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent, 0);
            // 监听对方接收短信
            Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
            PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);
            //如果短信内容超过70个字符 将这条短信拆成多条短信发送出去
            if (message.length() > 70) {
                ArrayList<String> msgs = sms.divideMessage(message);
                for (String msg : msgs) {
                    sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
                }
            } else {
                sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
            }
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
            MyLogs.e("haifeng","修改访问手机通讯录权限");
            flag = false;
            initView();
        }
//       if (requestCode==1){
//           MyLogs.e("haifeng","调系统发短信，notifyDataSetChanged");
//           adapter.notifyDataSetChanged();
//       }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        if (sendMessage!=null){
            unregisterReceiver(sendMessage);
        }
        MyLogs.e("haifeng","通讯录界面被销毁，执行ondestory");
    }
}
