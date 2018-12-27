package com.guohualife.ebiz.bpm.credit.dto;

import java.util.Date;

public class CreditSuccessDTO {

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 成交日期
	 */
	private Date successDate;
	
	/**
	 * 起息日期
	 * 使用时不需要set，可在调用后get
	 */
	private Date valueDate;
	
	/**
	 * 到期日期
	 * 使用时不需要set，可在调用后get
	 */
	private Date expiryDate;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}
	
	
}
