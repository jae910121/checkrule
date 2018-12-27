package com.guohualife.ebiz.bpm.credit.dto.response;

import java.math.BigDecimal;

import com.guohualife.platform.base.api.dto.BaseResultDTO;

/**
 * 查询赎回信息返回DTO
 * 
 * @author wangxulu
 *
 */
public class CreditSurrenderInfoResDTO extends BaseResultDTO {

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 是否可以赎回
	 * 1-是 0-否
	 */
	private String isSurrender;
	
	/**
	 * 投资本金
	 */
	private BigDecimal investAmount;
	
	/**
	 * 订单资产
	 */
	private BigDecimal totalValue;
	
	/**
	 * 赎回金额倍数
	 */
	private BigDecimal surrenderUnit;
	
	/**
	 * 赎回最小金额
	 */
	private BigDecimal surrenderMin;
	
	/**
	 * 赎回剩余最小金额
	 */
	private BigDecimal surrenderRestMin;
	
	/**
	 * 赎回最大金额
	 */
	private BigDecimal surrenderMax;
	
	/**
	 * 赎回账户号
	 */
	private String recAccountNo;
	
	/**
	 * 赎回账户名
	 */
	private String recAccountName;
	
	/**
	 * 赎回账户类型
	 */
	private String recAccountType;
	
	/**
	 * 银行名称
	 */
	private String bankName;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getIsSurrender() {
		return isSurrender;
	}

	public void setIsSurrender(String isSurrender) {
		this.isSurrender = isSurrender;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public BigDecimal getSurrenderUnit() {
		return surrenderUnit;
	}

	public void setSurrenderUnit(BigDecimal surrenderUnit) {
		this.surrenderUnit = surrenderUnit;
	}

	public BigDecimal getSurrenderMin() {
		return surrenderMin;
	}

	public void setSurrenderMin(BigDecimal surrenderMin) {
		this.surrenderMin = surrenderMin;
	}

	public BigDecimal getSurrenderRestMin() {
		return surrenderRestMin;
	}

	public void setSurrenderRestMin(BigDecimal surrenderRestMin) {
		this.surrenderRestMin = surrenderRestMin;
	}

	public BigDecimal getSurrenderMax() {
		return surrenderMax;
	}

	public void setSurrenderMax(BigDecimal surrenderMax) {
		this.surrenderMax = surrenderMax;
	}

	public String getRecAccountNo() {
		return recAccountNo;
	}

	public void setRecAccountNo(String recAccountNo) {
		this.recAccountNo = recAccountNo;
	}

	public String getRecAccountName() {
		return recAccountName;
	}

	public void setRecAccountName(String recAccountName) {
		this.recAccountName = recAccountName;
	}

	public String getRecAccountType() {
		return recAccountType;
	}

	public void setRecAccountType(String recAccountType) {
		this.recAccountType = recAccountType;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
}
