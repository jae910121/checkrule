package com.guohualife.ebiz.bpm.order.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单成交消息DTO
 * 
 * @author wangxulu
 *
 */
public class OrderSuccessMsgDTO extends OrderMsgDTO{

	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;
	
	/**
	 * 起息日
	 */
	private Date valueDate;
	
	/**
	 * 业务编号
	 * 对于保险来说是保单号
	 */
	private String businessNo;

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	
}
