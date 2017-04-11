package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoGen {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.zhuyun.jingxi.android");
        addFrinds(schema);
        addHome(schema);
        addGiftRecommend(schema);
        addGiftClassify(schema);
        addGiftStrategy(schema);
        new DaoGenerator().generateAll(schema, "F:\\ASWorkSpace\\JingXiApp_Android\\app\\src\\main\\java_gen");

    }

    private static void addFrinds(Schema schema)
    {
        Entity note = schema.addEntity("FriendsEntity");
        note.addIdProperty().autoincrement();//数据库id
        note.addIntProperty("friendsId").notNull().unique();//好友id(不为空，唯一)
        note.addIntProperty("gender");//性别
        note.addStringProperty("nickName");//昵称
        note.addStringProperty("mobile");//手机号
        note.addStringProperty("portrait");//头像
        note.addStringProperty("rName");//备注
    }

    private static void addHome(Schema schema)
    {
        Entity note = schema.addEntity("HomeListEntity");
        note.addIdProperty().autoincrement();
        note.addLongProperty("wishId");//许愿id
        note.addStringProperty("nickName");//昵称
        note.addStringProperty("portraitUrl");//头像
        note.addIntProperty("gender");//性别
        note.addLongProperty("utpTime");
        note.addStringProperty("content");//内容
        note.addStringProperty("imgUrl");//图片
        note.addStringProperty("goodsName");//礼物名
        note.addIntProperty("type");
        note.addDoubleProperty("cfPrice");
        note.addIntProperty("likeNum");//赞数
        note.addIntProperty("commNum");//评论数
        note.addIntProperty("isFriend");
        note.addLongProperty("wishUserId");
        note.addIntProperty("isLiked");

    }
    private static void addGiftRecommend(Schema schema) {
        Entity note = schema.addEntity("GiftRecommentEntity");
        note.addIdProperty().autoincrement();
        note.addLongProperty("recommenId");//礼品id
        note.addStringProperty("name");//礼品名称
        note.addStringProperty("imgIconUrl");//图片
        note.addLongProperty("utpTime");
        note.addIntProperty("sort");
        note.addIntProperty("type");
        note.addIntProperty("wishCount");
        note.addDoubleProperty("price");


    }
    private static void addGiftClassify(Schema schema) {
        Entity note = schema.addEntity("GiftClassifyEntity");
        note.addIdProperty().autoincrement();
        note.addLongProperty("classiftyId");//分类id
        note.addStringProperty("name");//分类名
        note.addStringProperty("imgIconUrl");//图片

    }
    private static void addGiftStrategy(Schema schema) {
        Entity note = schema.addEntity("GiftStrategyEntity");
        note.addIdProperty().autoincrement();
        note.addLongProperty("strategyId");
        note.addStringProperty("name");//攻略名
        note.addStringProperty("imgIconUrl");//图片
        note.addIntProperty("wishCount");
        note.addIntProperty("sort");
    }

}
