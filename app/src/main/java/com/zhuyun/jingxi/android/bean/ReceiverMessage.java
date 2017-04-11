package com.zhuyun.jingxi.android.bean;

/**
 * Created by jiangxiangfei on 2015/12/24.
 */
public class ReceiverMessage {
    public long id;       //条目id
    public String name;   //收货人姓名
    public String mobile; //收货人电话
    public long country;  //国家id
    public long province; //省份id
    public long city;     //城市id
    public long area;     //县区id
    public String address;//详细收货地址
    public int isDefault;  //是否默认地址  1  默认，0 正常
}
