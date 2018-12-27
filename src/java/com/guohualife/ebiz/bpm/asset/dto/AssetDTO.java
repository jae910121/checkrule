package com.guohualife.ebiz.bpm.asset.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.guohualife.platform.base.api.model.BaseModel;

/**
 * 资产信息DTO
 * 
 * @author wangxulu
 *
 */
public class AssetDTO extends BaseModel{
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 订单状态
	 */
	private String orderStatus;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 产品类型
	 */
	private String productType;
	
	/**
	 * 银行编码
	 */
	private String bankCode;
	
	/**
	 * 银行名称
	 */
	private String bankName;
	
	/**
	 * 银行卡号
	 */
	private String cardBookCode;
	
	/**
	 * 购买时间
	 */
	private Date successDate;
	
	/**
	 * 起息时间
	 */
	private Date valueDate;
	
	/**
	 * 到期时间
	 */
	private Date expiryDate;
	
	/**
	 * 总本金
	 */
	private BigDecimal totalInvestAmount;
	
	/**
	 * 总收益
	 */
	private BigDecimal totalIncome;
	
	/**
	 * 总资产
	 */
	private BigDecimal totalAsset;
	
	/**
	 * 最近收益
	 */
	private BigDecimal recentIncome;
	
	/**
	 * 是否可以赎回
	 */
	private String isSurrender;
	
	/**
	 * 封闭期结束时间
	 */
	private Date endCloseDate;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public BigDecimal getTotalInvestAmount() {
		return totalInvestAmount;
	}

	public void setTotalInvestAmount(BigDecimal totalInvestAmount) {
		this.totalInvestAmount = totalInvestAmount;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(BigDecimal totalAsset) {
		this.totalAsset = totalAsset;
	}

	public BigDecimal getRecentIncome() {
		return recentIncome;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setRecentIncome(BigDecimal recentIncome) {
		this.recentIncome = recentIncome;
	}

	public String getCardBookCode() {
		return cardBookCode;
	}

	public void setCardBookCode(String cardBookCode) {
		this.cardBookCode = cardBookCode;
	}

	public Date getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getIsSurrender() {
		return isSurrender;
	}

	public void setIsSurrender(String isSurrender) {
		this.isSurrender = isSurrender;
	}

	public Date getEndCloseDate() {
		return endCloseDate;
	}

	public void setEndCloseDate(Date endCloseDate) {
		this.endCloseDate = endCloseDate;
	}
	
}
