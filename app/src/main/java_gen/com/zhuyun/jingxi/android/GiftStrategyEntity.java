package com.zhuyun.jingxi.android;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table GIFT_STRATEGY_ENTITY.
 */
public class GiftStrategyEntity {

    private Long id;
    private Long strategyId;
    private String name;
    private String imgIconUrl;
    private Integer wishCount;
    private Integer sort;

    public GiftStrategyEntity() {
    }

    public GiftStrategyEntity(Long id) {
        this.id = id;
    }

    public GiftStrategyEntity(Long id, Long strategyId, String name, String imgIconUrl, Integer wishCount, Integer sort) {
        this.id = id;
        this.strategyId = strategyId;
        this.name = name;
        this.imgIconUrl = imgIconUrl;
        this.wishCount = wishCount;
        this.sort = sort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
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

    public Integer getWishCount() {
        return wishCount;
    }

    public void setWishCount(Integer wishCount) {
        this.wishCount = wishCount;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}
