package com.guohualife.ebiz.bpm.credit.dto.request;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 赎回核算请求DTO
 * 
 * @author wangxulu
 *
 */
public class CreditSurrenderChargeReqDTO {

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 申请金额
	 */
	private BigDecimal applyMoney;
	
	/**
	 * 申请时间
	 */
	private Date applyDate;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
}
