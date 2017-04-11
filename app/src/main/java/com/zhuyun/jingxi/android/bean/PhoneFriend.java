package com.zhuyun.jingxi.android.bean;

public class PhoneFriend{

	public int type;//类型
	public String headImg;
	public String mobile;
	public String nickName;


//	private String name;
//	private String pinyin;

	public PhoneFriend(String nickName) {
		super();
		this.nickName = nickName;
//		//心得：在数据创建的时候去处理，而不是每次需要的时候去处理，前提是数据不变
//		setPinyin(PinYinUtil.getPinYin(name));
	}
//
//	public String getName() {
//		return nickName;
//	}
//
//	public void setName(String name) {
//		this.nickName = name;
//	}
//

	public String getPhone() {
		return mobile;
	}

	public void setPhone(String mobile) {
		this.mobile = mobile;
	}


//	@Override
//	public int compareTo(PhoneFriend another) {
////		String pinYin = PinYinUtil.getPinYin(name);
////		String anotherPinYin = PinYinUtil.getPinYin(another.getName());
//		return getPinyin().compareTo(another.getPinyin());
//	}

//	public String getPinyin() {
//		return pinyin;
//	}

//	public void setPinyin(String pinyin) {
//		this.pinyin = pinyin;
//	}
//
}
