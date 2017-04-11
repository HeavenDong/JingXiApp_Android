package com.zhuyun.jingxi.android.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.zhuyun.jingxi.android.App;
import com.zhuyun.jingxi.android.R;
import com.zhuyun.jingxi.android.activty.BirthdayReminderActivity;
import com.zhuyun.jingxi.android.activty.WishDynamicActivity;
import com.zhuyun.jingxi.android.activty.WonderfulUndefinedActivity;
import com.zhuyun.jingxi.android.huanxin.Constant;
import com.zhuyun.jingxi.android.huanxin.DemoHelper;
import com.zhuyun.jingxi.android.huanxin.db.InviteMessgeDao;
import com.zhuyun.jingxi.android.huanxin.ui.ChatActivity;
import com.zhuyun.jingxi.android.utils.MyLogs;
import com.zhuyun.jingxi.android.utils.TempSingleToast;

/**
 *   消息
 */
public class IMFragment2 extends EaseConversationListFragment implements View.OnClickListener {
    private TextView huodong_tv,dongtai_tv;
    private TextView huodong_number,dongtai_number,shengri_number;

    @Override
    protected void initView() {
        super.initView();
        MyLogs.e("haifeng","进入IMFragment2");
        MyLogs.e("haifeng","环信登录状态="+ DemoHelper.getInstance().isLoggedIn());
        View head= View.inflate(getActivity(),R.layout.layout_im_head,null);
        head.findViewById(R.id.huodong_linear).setOnClickListener(this);
        head.findViewById(R.id.dongtai_linear).setOnClickListener(this);
        head.findViewById(R.id.shengri_linear).setOnClickListener(this);

        huodong_tv= (TextView) head.findViewById(R.id.huodong_tv);
        dongtai_tv= (TextView) head.findViewById(R.id.dongtai_tv);

        huodong_number= (TextView) head.findViewById(R.id.huodong_number);
        dongtai_number= (TextView) head.findViewById(R.id.dongtai_number);
        shengri_number= (TextView) head.findViewById(R.id.shengri_number);
        errorItemContainer.addView(head);

        initData();
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    TempSingleToast.showToast(App.getAppContext(),"不能和自己聊天");
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if(conversation.isGroup()){
                        if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        }else{
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });
//        //red packet code : 红包回执消息在会话列表最后一条消息的展示
//        conversationListView.setConversationListHelper(new EaseConversationList.EaseConversationListHelper() {
//            @Override
//            public String onSetItemSecondaryText(EMMessage lastMessage) {
//                if (lastMessage.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    String sendNick = lastMessage.getStringAttribute(RedPacketConstant.EXTRA_RED_PACKET_SENDER_NAME, "");
//                    String receiveNick = lastMessage.getStringAttribute(RedPacketConstant.EXTRA_RED_PACKET_RECEIVER_NAME, "");
//                    String msg;
//                    if (lastMessage.direct() == EMMessage.Direct.RECEIVE) {
//                        msg = String.format(getResources().getString(R.string.msg_someone_take_red_packet), receiveNick);
//                    } else {
//                        if (sendNick.equals(receiveNick)) {
//                            msg = getResources().getString(R.string.msg_take_red_packet);
//                        } else {
//                            msg = String.format(getResources().getString(R.string.msg_take_someone_red_packet), sendNick);
//                        }
//                    }
//                    return msg;
//                }
//                return null;
//            }
//        });
//        super.setUpView();
//        //end of red packet code
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
            MyLogs.e("haifeng","连接环信成功");
        } else {
            MyLogs.e("haifeng","连接环信失败");
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if(tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat){
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.getUserName());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.getUserName(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // TODO: 2016/8/2
        // update unread count
//        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }

    private void initData() {
        /*假数据    4,3,10*/
        String str="你有"+4+"条活动消息";
        String str2="你的好友有"+3+"条许愿动态";

        huodong_number.setText("4");
        dongtai_number.setText("3");
        shengri_number.setText("10");
         // 设置颜色改变
        SpannableStringBuilder builder=new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(Color.RED), str.length() - 6,str.length() - 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        huodong_tv.setText(builder);

        SpannableStringBuilder builder2=new SpannableStringBuilder(str2);
        builder2.setSpan(new ForegroundColorSpan(Color.RED), str2.length() - 6,str2.length() - 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        dongtai_tv.setText(builder2);




    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.huodong_linear:
                MyLogs.e("haifeng","活动");
                startActivity(new Intent(getActivity(), WonderfulUndefinedActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.dongtai_linear:
                MyLogs.e("haifeng","动态");
                startActivity(new Intent(getActivity(), WishDynamicActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.shengri_linear:
                MyLogs.e("haifeng","生日");
                startActivity(new Intent(getActivity(), BirthdayReminderActivity.class));
                break;
        }
    }
}
