package com.guohualife.ebiz.bpm.insurance.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PolicyAssetSyncDTO {

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 保单号
	 */
	private String policyNo;
	
	/**
	 * 保单价值
	 */
	private BigDecimal contValue;
	
	/**
	 * 当日收益
	 */
	private BigDecimal recentIncome;
	
	/**
	 * 累积收益
	 */
	private BigDecimal totalIncome;

	/**
	 * 更新日期
	 */
	private Date modifyDate;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public BigDecimal getContValue() {
		return contValue;
	}

	public void setContValue(BigDecimal contValue) {
		this.contValue = contValue;
	}

	public BigDecimal getRecentIncome() {
		return recentIncome;
	}

	public void setRecentIncome(BigDecimal recentIncome) {
		this.recentIncome = recentIncome;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}
}
