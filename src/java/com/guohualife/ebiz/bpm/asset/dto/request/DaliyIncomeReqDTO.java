package com.guohualife.ebiz.bpm.asset.dto.request;

import java.util.Date;

import com.guohualife.platform.base.api.model.PageQueryDTO;

/**
 * 每日收益查询请求DTO
 * 
 * @author wangxulu
 *
 */
public class DaliyIncomeReqDTO {

	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 查询起始日期
	 */
	private Date queryBeginDate;
	
	/**
	 * 查询结束日期
	 */
	private Date queryEndDate;
	
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

	public PageQueryDTO getPageQueryDTO() {
		return pageQueryDTO;
	}

	public void setPageQueryDTO(PageQueryDTO pageQueryDTO) {
		this.pageQueryDTO = pageQueryDTO;
	}
	
}
