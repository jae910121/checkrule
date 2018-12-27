package com.guohualife.ebiz.bpm.insurance.dto.request;

import java.math.BigDecimal;



public class PolicySurrenderApplyReqDTO {
	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 保单号
	 */
	private String policyNo;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 赎回类型
	 */
	private String surrenderType;
	
	/**
	 * 赎回申请金额
	 */
	private BigDecimal applyMoney;
	
	/**
	 * 实际赎回金额
	 */
	private BigDecimal availableMoney;
	
	/**
	 * 扣除手续费
	 */
	private BigDecimal surrenderFee;
	
	/**
	 * 赎回申请平台
	 */
	private String platformType;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSurrenderType() {
		return surrenderType;
	}

	public void setSurrenderType(String surrenderType) {
		this.surrenderType = surrenderType;
	}

	public BigDecimal getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}

	public BigDecimal getAvailableMoney() {
		return availableMoney;
	}

	public void setAvailableMoney(BigDecimal availableMoney) {
		this.availableMoney = availableMoney;
	}

	public BigDecimal getSurrenderFee() {
		return surrenderFee;
	}

	public void setSurrenderFee(BigDecimal surrenderFee) {
		this.surrenderFee = surrenderFee;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	
}
