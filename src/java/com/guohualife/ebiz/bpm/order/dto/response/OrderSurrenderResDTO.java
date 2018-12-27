package com.guohualife.ebiz.bpm.order.dto.response;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单赎回详细信息查询返回DTO
 * 
 * @author wangxulu
 *
 */
public class OrderSurrenderResDTO {

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 银行名称
	 */
	private String bankName;
	
	/**
	 * 银行编码
	 */
	private String bankCode;
	
	/**
	 * 退款账户号
	 */
	private String recAccountNo;
	
	/**
	 * 赎回时间
	 */
	private Date surrenderDate;
	
	/**
	 * 赎回类型
	 */
	private String surrenderType;
	
	/**
	 * 赎回申请金额
	 */
	private BigDecimal applyMoney;
	
	/**
	 * 实际退款金额
	 */
	private BigDecimal actualMoney;
	
	/**
	 * 赎回状态
	 */
	private String status;
	
	/**
	 * 赎回申请平台
	 */
	private String platfotmType;
	
	/**
	 * 赎回本金
	 */
	private BigDecimal surrenderPrincipal;
	
	/**
	 * 手续费
	 */
	private BigDecimal surrenderFee;
	

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getRecAccountNo() {
		return recAccountNo;
	}

	public void setRecAccountNo(String recAccountNo) {
		this.recAccountNo = recAccountNo;
	}

	public Date getSurrenderDate() {
		return surrenderDate;
	}

	public void setSurrenderDate(Date surrenderDate) {
		this.surrenderDate = surrenderDate;
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

	public BigDecimal getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(BigDecimal actualMoney) {
		this.actualMoney = actualMoney;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPlatfotmType() {
		return platfotmType;
	}

	public void setPlatfotmType(String platfotmType) {
		this.platfotmType = platfotmType;
	}

	public BigDecimal getSurrenderPrincipal() {
		return surrenderPrincipal;
	}

	public void setSurrenderPrincipal(BigDecimal surrenderPrincipal) {
		this.surrenderPrincipal = surrenderPrincipal;
	}

	public BigDecimal getSurrenderFee() {
		return surrenderFee;
	}

	public void setSurrenderFee(BigDecimal surrenderFee) {
		this.surrenderFee = surrenderFee;
	}

	
}
