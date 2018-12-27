package com.guohualife.ebiz.bpm.order.dto;

import java.util.Date;
import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;


/**
 * 订单消息DTO
 * 
 * @author wangxulu
 *
 */
public class OrderMsgDTO {

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 订单渠道
	 */
	private String orderType;
	
	/**
	 * 订单成交时间
	 */
	private Date successDate;
	
	/**
	 * 活动编码
	 */
	private String activityCode;
	
	/**
	 * 活动推荐码
	 */
	private String recommendCode;
	
	/**
	 * 订单活动备注信息
	 */
	private String actRemark;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 产品类型
	 */
	private String productType;
	
	/**
	 * 购买客户ID
	 */
	private Long customerId;
	
	/**
	 * 客户姓名
	 */
	private String customerName;
	
	/**
	 * 订单备注信息
	 */
	private String remark;
	
	/**
	 * 订单备注信息集合
	 */
	private HashMap<String, String> remarkMap;
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getRecommendCode() {
		return recommendCode;
	}

	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getActRemark() {
		return actRemark;
	}

	public void setActRemark(String actRemark) {
		this.actRemark = actRemark;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Date getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public HashMap<String, String> getRemarkMap() {
		return remarkMap;
	}

	@SuppressWarnings("unchecked")
	public void setRemarkMap() throws Exception {
		this.remarkMap = JSONObject.parseObject(getRemark(), HashMap.class);
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setRemarkMap(HashMap<String, String> remarkMap) {
		this.remarkMap = remarkMap;
	}
	
}
