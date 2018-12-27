package com.guohualife.ebiz.bpm.credit.dto.response;

import java.util.Date;

/**
 * 债权申购返回信息DTO
 * 
 * @author wangxulu
 *
 */
public class CreditApplyPurchaseResDTO {
	
	/**
	 * 出单状态(1-出单成功 0-出单失败，2-处理中)
	 */
	private String status;
	
	/**
	 * 失败原因
	 */
	private String failReason;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 起息日（计算收益日期）
	 */
	private Date startDate;
	
	/**
	 * 到期日
	 */
	private Date endDate;
	
	/**
	 * 成交时间
	 */
	private Date successDate;
	
	/**
	 * 封闭期结束时间
	 */
	private Date closeDate;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	
}
