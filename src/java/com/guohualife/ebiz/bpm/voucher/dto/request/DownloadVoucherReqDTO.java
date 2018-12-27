package com.guohualife.ebiz.bpm.voucher.dto.request;

public class DownloadVoucherReqDTO {
	/**
	 * 发送邮箱
	 */
	private String toUser;
	/**
	 * 订单号
	 */
	private String orderNo;

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
