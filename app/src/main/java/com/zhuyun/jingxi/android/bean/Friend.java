package com.zhuyun.jingxi.android.bean;

public class Friend implements Comparable<Friend> {
	public int type;//类型
	private String pinyin;//名字的拼音
	public String address; // 地址
	public String headImg; // 头像
	public String birthday; // 生日
	public String blackStatus;//黑名单状态 0：拉黑 1：未拉黑
	public String userName; // 姓名
	public String accountId; // 账号
	public String mobile; // 手机号码
	public String nickName; // 昵称
	public String password; // MD5编码的密码
	public String remark; // 签名
	public long createTime;//账号创建时间
	public String id; // 用户Id
	public int sex; // 性别0-女,1-男
	public int status; // 状态

	public String getBlackStatus() {
		return blackStatus;
	}

	public void setBlackStatus(String blackStatus) {
		this.blackStatus = blackStatus;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Friend(String nickName) {
		super();
		this.nickName = nickName;
		//心得：在数据创建的时候去处理，而不是每次需要的时候去处理，前提是数据不变
//		setPinyin(PinYinUtil.getPinYin(nickName));
	}
	public Friend() {
		super();

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

	public void setPhone(String mobile) {
		this.mobile = mobile;
	}


	@Override
	public int compareTo(Friend another) {
//		String pinYin = PinYinUtil.getPinYin(name);
//		String anotherPinYin = PinYinUtil.getPinYin(another.getName());
		return getPinyin().compareTo(another.getPinyin());
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}




	
}
