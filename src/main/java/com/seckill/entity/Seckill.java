package com.seckill.entity;

import java.util.Date;

public class Seckill {
	private long seckillId;

	private String name;

	private int seckillNo;

	private Date startTime;

	private Date endTime;

	private Date createTime;


	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override public String toString() {
		final StringBuffer sb = new StringBuffer("Seckill{");
		sb.append("seckillId=").append(seckillId);
		sb.append(", name='").append(name).append('\'');
		sb.append(", seckill_no=").append(seckillNo);
		sb.append(", startTime=").append(startTime);
		sb.append(", endTime=").append(endTime);
		sb.append(", createTime=").append(createTime);
		sb.append('}');
		return sb.toString();
	}

	public int getSeckillNo() {
		return seckillNo;
	}

	public void setSeckillNo(int seckillNo) {
		this.seckillNo = seckillNo;
	}
}