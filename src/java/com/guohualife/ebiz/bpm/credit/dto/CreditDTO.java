package com.guohualife.ebiz.bpm.credit.dto;

import java.math.BigDecimal;

/**
 * 债权信息DTO
 * 
 * @author wangxulu
 *
 */
public class CreditDTO {
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 投资本金
	 */
	private BigDecimal investAmount;
	
	/**
	 * 份数
	 */
	private BigDecimal mult;

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public BigDecimal getMult() {
		return mult;
	}

	public void setMult(BigDecimal mult) {
		this.mult = mult;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
