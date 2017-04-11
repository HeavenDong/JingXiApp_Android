package com.zhuyun.jingxi.android;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table GIFT_RECOMMENT_ENTITY.
 */
public class GiftRecommentEntity {

    private Long id;
    private Long recommenId;
    private String name;
    private String imgIconUrl;
    private Long utpTime;
    private Integer sort;
    private Integer type;
    private Integer wishCount;
    private Double price;

    public GiftRecommentEntity() {
    }

    public GiftRecommentEntity(Long id) {
        this.id = id;
    }

    public GiftRecommentEntity(Long id, Long recommenId, String name, String imgIconUrl, Long utpTime, Integer sort, Integer type, Integer wishCount, Double price) {
        this.id = id;
        this.recommenId = recommenId;
        this.name = name;
        this.imgIconUrl = imgIconUrl;
        this.utpTime = utpTime;
        this.sort = sort;
        this.type = type;
        this.wishCount = wishCount;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecommenId() {
        return recommenId;
    }

    public void setRecommenId(Long recommenId) {
        this.recommenId = recommenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgIconUrl() {
        return imgIconUrl;
    }

    public void setImgIconUrl(String imgIconUrl) {
        this.imgIconUrl = imgIconUrl;
    }

    public Long getUtpTime() {
        return utpTime;
    }

    public void setUtpTime(Long utpTime) {
        this.utpTime = utpTime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWishCount() {
        return wishCount;
    }

    public void setWishCount(Integer wishCount) {
        this.wishCount = wishCount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}