package com.guohualife.ebiz.bpm.order.dao;

import java.util.List;

import com.guohualife.edb.bpm.model.EbizSurrender;

public interface EbizSurrenderSPECDAO {

	/**
	 * 根据产品类型和状态查询赎回列表
	 * 
	 * @param statusList
	 * @param productType
	 * @return
	 */
	public List<EbizSurrender> getSurrenderList(List<String> statusList, String productType);
	
}
