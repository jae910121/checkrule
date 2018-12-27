package com.guohualife.ebiz.bpm.asset.dto;

import java.util.Date;

import com.guohualife.platform.base.api.model.PageQueryDTO;

/**
 * 资产分页查询DTO
 * 
 * @author wangxulu
 *
 */
public class AssetPageDTO extends PageQueryDTO{
	
	/**
	 * 客户Id
	 */
	private Long customerId;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 查询起始日期
	 */
	public Date queryBeginDate;
	
	/**
	 * 查询结束日期
	 */
	public Date queryEndDate;

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
