package com.guohualife.ebiz.bpm.asset.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.guohualife.platform.base.api.model.BaseModel;

/**
 * 每日收益信息DTO
 * 
 * @author wangxulu
 *
 */
public class DaliyIncomeDTO extends BaseModel{
	
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
	private String produtCode;

	/**
	 * 每天收益
	 */
	private BigDecimal income;
	
	/**
	 * 每天收益时间
	 */
	private Date incomeDate;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public Date getIncomeDate() {
		return incomeDate;
	}

	public void setIncomeDate(Date incomeDate) {
		this.incomeDate = incomeDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProdutCode() {
		return produtCode;
	}

	public void setProdutCode(String produtCode) {
		this.produtCode = produtCode;
	}
	
}
