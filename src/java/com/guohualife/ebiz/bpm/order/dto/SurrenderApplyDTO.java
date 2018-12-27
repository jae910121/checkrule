package com.guohualife.ebiz.bpm.order.dto;

import java.math.BigDecimal;

public class SurrenderApplyDTO {

	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 赎回类型
	 */
	private String surrenderType;
	
	/**
	 * 申请金额
	 */
	private BigDecimal applyMoney;
	
	/**
	 * 手续费
	 */
	private BigDecimal surrenderFee;
	
	/**
	 * 实际金额
	 */
	private BigDecimal availableMoney;
	
	/**
	 * 赎回本金
	 */
	private BigDecimal surrenderPrincipal;
	
	/**
	 * 申请平台
	 */
	private String platformType;
	
	/**
	 * 赎回提交失败信息
	 */
	private String errorMessage;
	
	/**
	 * 赎回Id
	 */
	private Long surrenderId;

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

	public BigDecimal getSurrenderFee() {
		return surrenderFee;
	}

	public void setSurrenderFee(BigDecimal surrenderFee) {
		this.surrenderFee = surrenderFee;
	}

	public BigDecimal getAvailableMoney() {
		return availableMoney;
	}

	public void setAvailableMoney(BigDecimal availableMoney) {
		this.availableMoney = availableMoney;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Long getSurrenderId() {
		return surrenderId;
	}

	public void setSurrenderId(Long surrenderId) {
		this.surrenderId = surrenderId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getSurrenderPrincipal() {
		return surrenderPrincipal;
	}

	public void setSurrenderPrincipal(BigDecimal surrenderPrincipal) {
		this.surrenderPrincipal = surrenderPrincipal;
	}
	
}
