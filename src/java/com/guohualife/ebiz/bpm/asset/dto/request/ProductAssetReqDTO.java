package com.guohualife.ebiz.bpm.asset.dto.request;

import com.guohualife.platform.base.api.model.PageQueryDTO;


/**
 * 收益查询请求DTO
 * 
 * @author wangxulu
 *
 */
public class ProductAssetReqDTO {

	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 订单号
	 */
	private String productCode;
	
	/**
	 * 分页查询参数
	 */
	private PageQueryDTO pageQueryDTO;	

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

	public PageQueryDTO getPageQueryDTO() {
		return pageQueryDTO;
	}

	public void setPageQueryDTO(PageQueryDTO pageQueryDTO) {
		this.pageQueryDTO = pageQueryDTO;
	}
	
}
