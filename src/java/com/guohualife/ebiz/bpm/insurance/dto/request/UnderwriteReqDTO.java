package com.guohualife.ebiz.bpm.insurance.dto.request;

import java.math.BigDecimal;

import com.guohualife.ebiz.bpm.insurance.dto.ActivityDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyDTO;
import com.guohualife.ebiz.bpm.order.dto.ThirdOrderDTO;


/**
 * 核保请求DTO
 * 
 * @author wangxulu
 *
 */
public class UnderwriteReqDTO {
	
	/**
	 * 第三方订单信息DTO
	 */
	private ThirdOrderDTO thirdOrderDTO;
	
	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 订单渠道
	 */
	private String orderType;
	
	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;
	
	/**
	 * 保单(投保人、被保人、受益人)信息
	 */
	private PolicyDTO policyDTO;

	/**
	 * 活动信息
	 */
	private ActivityDTO activityDTO;
	
	public ThirdOrderDTO getThirdOrderDTO() {
		return thirdOrderDTO;
	}

	public void setThirdOrderDTO(ThirdOrderDTO thirdOrderDTO) {
		this.thirdOrderDTO = thirdOrderDTO;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public PolicyDTO getPolicyDTO() {
		return policyDTO;
	}

	public void setPolicyDTO(PolicyDTO policyDTO) {
		this.policyDTO = policyDTO;
	}

	public ActivityDTO getActivityDTO() {
		return activityDTO;
	}

	public void setActivityDTO(ActivityDTO activityDTO) {
		this.activityDTO = activityDTO;
	}
	
}
