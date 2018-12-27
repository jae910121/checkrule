package com.guohualife.ebiz.bpm.order.dto.response;

import java.math.BigDecimal;
import java.util.Date;

import com.guohualife.ebiz.bpm.asset.dto.AssetDTO;

/**
 * 订单查询返回DTO
 * 
 * @author wangxulu
 *
 */
public class OrderResDTO {
	
	/**
	 * 客户ID
	 */
	private Long customerId;
	
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
	 * 订单类型
	 */
	private String orderType;
	
	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;
	
	/**
	 * 成交时间
	 */
	private Date successDate;
	
	/**
	 * 起息日
	 */
	private Date valueDate;
	
	/**
	 * 到期日
	 */
	private Date expiryDate;
	
	/**
	 * 银行编码
	 */
	private String bankCode;
	
	/**
	 * 银行名称
	 */
	private String bankName;
	
	/**
	 * 支付卡号
	 */
	private String cardBookCode;
	
	/**
	 * 过封闭期到期日期
	 */
	private Date endCloseDate;
	
	/**
	 * 资产信息
	 */
	private AssetDTO assetDTO;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
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

	public String getCardBookCode() {
		return cardBookCode;
	}

	public void setCardBookCode(String cardBookCode) {
		this.cardBookCode = cardBookCode;
	}

	public AssetDTO getAssetDTO() {
		return assetDTO;
	}

	public void setAssetDTO(AssetDTO assetDTO) {
		this.assetDTO = assetDTO;
	}

	public Date getEndCloseDate() {
		return endCloseDate;
	}

	public void setEndCloseDate(Date endCloseDate) {
		this.endCloseDate = endCloseDate;
	}
	
}
