package com.guohualife.ebiz.bpm.asset.dto;

import java.util.Date;

import com.guohualife.platform.base.api.model.PageQueryDTO;

/**
 * 资金明细分页查询DTO
 * 
 * @author wangxulu
 *
 */
public class TradeDetailPageDTO extends PageQueryDTO{
	
	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 查询起始日期
	 */
	private Date queryBeginDate;
	
	/**
	 * 查询结束日期
	 */
	private Date queryEndDate;

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

	public Date getQueryBeginDate() {
		return queryBeginDate;
	}

	public void setQueryBeginDate(Date queryBeginDate) {
		this.queryBeginDate = queryBeginDate;
	}

	public Date getQueryEndDate() {
		return queryEndDate;
	}

	public void setQueryEndDate(Date queryEndDate) {
		this.queryEndDate = queryEndDate;
	}

}
