package com.guohualife.ebiz.bpm.order.dto;

import java.math.BigDecimal;

import com.guohualife.ebiz.bpm.insurance.dto.ActivityDTO;
import com.guohualife.ebiz.product.dto.EbizProductDTO;

/**
 * 订单信息DTO
 * 
 * @author wangxulu
 *
 */
public class OrderDTO {
	
	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 客户名
	 */
	private String customerName;
	
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
	 * 产品编码(对应于保险公司)
	 */
	private String skuCode;
	
	/**
	 * 订单状态
	 */
	private String orderStatus;
	
	/**
	 * 活动信息
	 */
	private ActivityDTO activityDTO;
	
	/**
	 * 产品信息
	 */
	private EbizProductDTO ebizProductDTO;
	
	/**
	 * 第三方订单信息
	 */
	private ThirdOrderDTO thirdOrderDTO;

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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public ActivityDTO getActivityDTO() {
		return activityDTO;
	}

	public void setActivityDTO(ActivityDTO activityDTO) {
		this.activityDTO = activityDTO;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public EbizProductDTO getEbizProductDTO() {
		return ebizProductDTO;
	}

	public void setEbizProductDTO(EbizProductDTO ebizProductDTO) {
		this.ebizProductDTO = ebizProductDTO;
	}

	public ThirdOrderDTO getThirdOrderDTO() {
		return thirdOrderDTO;
	}

	public void setThirdOrderDTO(ThirdOrderDTO thirdOrderDTO) {
		this.thirdOrderDTO = thirdOrderDTO;
	}
		
}
