package com.guohualife.ebiz.bpm.order.dto;

import java.util.Date;

public class ThirdOrderDTO {

	/**
	 * 第三方订单号
	 */
	private String thirdOrderNo;
	
	/**
	 * 第三方类型
	 */
	private String thirdType;
	
	/**
	 * 第三方产品编码
	 */
	private String thirdProductCode;
	
	/**
	 * 活动编码
	 */
	private String specialCode;
	
	/**
	 * 第三方支付号
	 */
	private String thirdPayNo;
	
	/**
	 * 第三方账号
	 */
	private String thirdAccNo;
	
	/**
	 * 第三方账户名
	 */
	private String thirdAccName;
	
	/**
	 * 第三方支付时间
	 */
	private Date thirdAccDate;
	
	/**
	 * 第三方用户Id
	 */
	private String thirdUserId;

	public String getThirdOrderNo() {
		return thirdOrderNo;
	}

	public void setThirdOrderNo(String thirdOrderNo) {
		this.thirdOrderNo = thirdOrderNo;
	}

	public String getThirdType() {
		return thirdType;
	}

	public void setThirdType(String thirdType) {
		this.thirdType = thirdType;
	}

	public String getThirdProductCode() {
		return thirdProductCode;
	}

	public void setThirdProductCode(String thirdProductCode) {
		this.thirdProductCode = thirdProductCode;
	}

	public String getThirdPayNo() {
		return thirdPayNo;
	}

	public void setThirdPayNo(String thirdPayNo) {
		this.thirdPayNo = thirdPayNo;
	}

	public String getThirdAccNo() {
		return thirdAccNo;
	}

	public void setThirdAccNo(String thirdAccNo) {
		this.thirdAccNo = thirdAccNo;
	}

	public String getThirdAccName() {
		return thirdAccName;
	}

	public void setThirdAccName(String thirdAccName) {
		this.thirdAccName = thirdAccName;
	}

	public Date getThirdAccDate() {
		return thirdAccDate;
	}

	public void setThirdAccDate(Date thirdAccDate) {
		this.thirdAccDate = thirdAccDate;
	}

	public String getThirdUserId() {
		return thirdUserId;
	}

	public void setThirdUserId(String thirdUserId) {
		this.thirdUserId = thirdUserId;
	}

	public String getSpecialCode() {
		return specialCode;
	}

	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}
	
}
