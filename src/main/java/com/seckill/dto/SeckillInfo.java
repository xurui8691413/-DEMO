package com.seckill.dto;

public class SeckillInfo {
	
	private String name;
	
	private int storeNum;
	
	private long seckillId;

	private long userPhone;

	public long getSeckillId() {
		return seckillId;
	}

	

	public SeckillInfo(String name, int storeNum, long seckillId, long userPhone) {
		super();
		this.name = name;
		this.storeNum = storeNum;
		this.seckillId = seckillId;
		this.userPhone = userPhone;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public int getStoreNum() {
		return storeNum;
	}



	public void setStoreNum(int storeNum) {
		this.storeNum = storeNum;
	}



	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public long getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(long userPhone) {
		this.userPhone = userPhone;
	}
}
