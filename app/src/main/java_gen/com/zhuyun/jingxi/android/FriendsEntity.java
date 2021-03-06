package com.zhuyun.jingxi.android;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table FRIENDS_ENTITY.
 */
public class FriendsEntity {

    private Long id;
    private int friendsId;
    private Integer gender;
    private String nickName;
    private String mobile;
    private String portrait;
    private String rName;

    public FriendsEntity() {
    }

    public FriendsEntity(Long id) {
        this.id = id;
    }

    public FriendsEntity(Long id, int friendsId, Integer gender, String nickName, String mobile, String portrait, String rName) {
        this.id = id;
        this.friendsId = friendsId;
        this.gender = gender;
        this.nickName = nickName;
        this.mobile = mobile;
        this.portrait = portrait;
        this.rName = rName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(int friendsId) {
        this.friendsId = friendsId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getRName() {
        return rName;
    }

    public void setRName(String rName) {
        this.rName = rName;
    }

}
