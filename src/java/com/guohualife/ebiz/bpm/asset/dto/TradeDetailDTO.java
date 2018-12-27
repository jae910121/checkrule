package com.guohualife.ebiz.bpm.asset.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.guohualife.platform.base.api.model.BaseModel;

/**
 * 资金交易明细信息DTO
 * 
 * @author wangxulu
 *
 */
public class TradeDetailDTO extends BaseModel{

	/**
	 * 交易日期
	 */
	private Date tradeDate;
	
	/**
	 * 交易类型
	 * 1-订单成交 2-订单赎回 
	 */
	private String tradeType;
	
	/**
	 * 金额
	 */
	private BigDecimal amount;
	
	/**
	 * 交易流水号
	 * 对于订单成交是订单号，对于订单赎回是赎回受理号
	 * 
	 */
	private String businessNo;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 产品名称
	 */
	private String productName;

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

}
