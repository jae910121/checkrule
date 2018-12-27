package com.guohualife.ebiz.bpm.asset.dto.response;

import com.guohualife.ebiz.bpm.asset.dto.DaliyIncomeDTO;
import com.guohualife.platform.base.api.model.PageGrid;


/**
 * 每日收益查询返回DTO
 * 
 * @author wangxulu
 *
 */
public class DaliyIncomeResDTO {
	
	/**
	 * 收益明细分页列表
	 */
	private PageGrid<DaliyIncomeDTO> daliyIncomeGrid;

	public PageGrid<DaliyIncomeDTO> getDaliyIncomeGrid() {
		return daliyIncomeGrid;
	}

	public void setDaliyIncomeGrid(PageGrid<DaliyIncomeDTO> daliyIncomeGrid) {
		this.daliyIncomeGrid = daliyIncomeGrid;
	}
	
}
