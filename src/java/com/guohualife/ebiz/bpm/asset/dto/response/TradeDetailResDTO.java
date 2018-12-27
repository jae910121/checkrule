package com.guohualife.ebiz.bpm.asset.dto.response;

import com.guohualife.ebiz.bpm.asset.dto.TradeDetailDTO;
import com.guohualife.platform.base.api.model.PageGrid;


/**
 * 资金流水返回结果DTO
 * 
 * @author wangxulu
 *
 */
public class TradeDetailResDTO {
	
	/**
	 * 资金流水分页列表
	 */
	private PageGrid<TradeDetailDTO> tradeDetailGrid;

	public PageGrid<TradeDetailDTO> getTradeDetailGrid() {
		return tradeDetailGrid;
	}

	public void setTradeDetailGrid(PageGrid<TradeDetailDTO> tradeDetailGrid) {
		this.tradeDetailGrid = tradeDetailGrid;
	}
	
}
