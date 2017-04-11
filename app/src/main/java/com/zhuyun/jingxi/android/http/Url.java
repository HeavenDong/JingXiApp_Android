package com.zhuyun.jingxi.android.http;

/**
 * Created by user0081 on 2016/6/20.
 */
public class Url {
//    public static final String MAINURL="http://192.168.1.6:9999/api";   //彤
    public static final String MAINURL="http://192.168.1.5:9999/api";  //喜
//   public static final String MAINURL="http://192.168.1.100:8080/api";//测试服务器
// public static final String MAINURL="http://aapi.dreawin.cn";//  域名

    //登录
    public static final String LOGIN=MAINURL+"/user/login";
    //注册发送验证码
    public static final String SEND_CODE=MAINURL+"/captchaCode/captchaSendCode";
    //注册
    public static final String REGIST=MAINURL+"/user/register";
    //忘记密码
    public static final String FORGOT=MAINURL+"/user/updatePassword";
    //获取好友列表
    public static final String GET_FRIENDS_LIST=MAINURL+"/friends/friendsList";
    //删除好友
    public static final String DELET_FRIEND=MAINURL+"/friends/friendDel";
    //获取好友申请列表
    public static final String GET_APPLY_FRIENDS_LIST=MAINURL+"/friends/friendApplyList";
    //好友申请答复
    public static final String REPLY_FRIEND=MAINURL+"/friends/friendReply";
    //获取推荐好友列表
    public static final String GET_RECOM_FRIENDS_LIST=MAINURL+"/friends/friendRecomList";
    //申请加好友
    public static final String APPLY_FRIEND=MAINURL+"/friends/friendApply";
    //好友信息
    public static final String GET_FRIENDS_MSG=MAINURL+"/friends/friendInfo";
    //获取某好友许愿列表
    public static final String GET_FRIENDS_WISH=MAINURL+"/wish/FWPList";
    //获取我的许愿列表
    public static final String GET_MY_WISH=MAINURL+"/wish/MWPList";
    //修改备注
    public static final String CHANGE_NOTE=MAINURL+"/friends/uptRName";
    //黑名单
    public static final String BLACK_LIST=MAINURL+"/friends/friendBList";
    //添加黑名单
    public static final String ADD_BLACK_LIST=MAINURL+"/friends/friendBDel";
    //删除黑名单
    public static final String DELECT_BLACK_LIST=MAINURL+"/friends/friendBAdd";
    //修改用户信息
    public static final String CHANG_USER_INFO=MAINURL+"/user/userInfo";


    //获取首页/大厅时间流
    public static final String HOME_LIST=MAINURL+"/stream/list";
    //获取首页/大厅时间流中单个许愿的详情
    public static final String HOME_ITEM_DETAILS=MAINURL+"/stream/detail";
    //获取推荐商品列表
    public static final String GIFT_RECOMMEND_INFO=MAINURL+"/mall/listForRecommend";
    //获取分类列表
    public static final String GIFT_CLASSIFY_INFO=MAINURL+"/mall/FLPList";
    //获取攻略列表
    public static final String GIFT_STRATEGY_INFO=MAINURL+"/mall/listForStrategy";
    //获取商品详情
    public static final String GIFT_RECOMMEND_DETAILS_INFO=MAINURL+"/mall/goodsDetail";
    //分类查商品接口
    public static final String GIFT_INFO_BY_CLASSIFY=MAINURL+"/mall/listForCat";
    //获取攻略详情
    public static final String GIFT_STRATEGY_DETAILS_INFO=MAINURL+"/mall/strategyDetail";
    //获取节日、场景和对象列表
    public static final String GIFT_SELECT_INFO=MAINURL+"/mall/FSPList";
    //发布许愿
    public static final String WISH_PUBLISH=MAINURL+"/wish/add";
    //送礼(支付成功调)
    public static final String PAY_SUCCESS=MAINURL+"/wish/present";
    //获取许愿动态列表
    public static final String WISH_DYNAMIC=MAINURL+"/wish/WDPList";
    //获取送礼列表
    public static final String RECORD_SEND=MAINURL+"/gifts/GFList";
    //获取收礼列表
    public static final String RECORD_RECEIVED=MAINURL+"/gifts/present";
    //送礼详情
    public static final String RECORD_SEND_DETAILS=MAINURL+"/gifts/GFListDetail";
    //修改密码
    public static final String CHANGE_PSW=MAINURL+"/user/updateNewPassword";
    //取消点赞
   public static final String CANCEL_LIKE=MAINURL+"/like/cancelLike";
    //点赞
   public static final String ADD_LIKE=MAINURL+"/like/addLike";
    //点赞列表
   public static final String ADD_LIKE_LIST=MAINURL+"/like/likeList";
    //评论列表
   public static final String COMMENT_LIST=MAINURL+"/comment/commentList";
    //新增评论
   public static final String PERSON_ADD_LIKE=MAINURL+"/comment/addComment";
    //达人列表
   public static final String STAR_LIST=MAINURL+"/expert/expertList";
    //搜索
    public static final String SEARCH_RESULT=MAINURL+"/search/rSearch";
    //收货信息列表
   public static final String RECEIVE_ADDRESS_LIST=MAINURL+"/goodReceipt/goodReceiptList";
    //新增收货信息地址
    public static final String ADD_RECEIVE_ADDRESS=MAINURL+"/goodReceipt/addGoodReceipt";
    //删除收货信息地址
    public static final String DELETE_RECEIVE_ADDRESS=MAINURL+"/goodReceipt/deleteGoodReceipt";
    //更改收货信息地址
    public static final String EDIT_RECEIVE_ADDRESS=MAINURL+"/goodReceipt/updateGoodReceipt";
    //修改默认收货地址
    public static final String EDIT_DEFAULT_ADDRESS=MAINURL+"/goodReceipt/updateGRDefault";
    //生日提醒
    public static final String BIRTHDAY_REMIND=MAINURL+"/birthday/birthdayRemind";
    //送生日礼物
    public static final String BIRTHDAY_SENDGIFT=MAINURL+"/birthday/SBPresent";
}
