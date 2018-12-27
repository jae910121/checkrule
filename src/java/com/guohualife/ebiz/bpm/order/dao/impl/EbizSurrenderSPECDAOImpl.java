package com.guohualife.ebiz.bpm.order.dao.impl;

import java.util.HashMap;
import java.util.List;

import com.guohualife.ebiz.bpm.order.dao.EbizSurrenderSPECDAO;
import com.guohualife.edb.bpm.dao.impl.EbizSurrenderDAOImpl;
import com.guohualife.edb.bpm.model.EbizSurrender;

public class EbizSurrenderSPECDAOImpl extends EbizSurrenderDAOImpl implements EbizSurrenderSPECDAO {

	@SuppressWarnings("unchecked")
	public List<EbizSurrender> getSurrenderList(List<String> statusList,
			String productType) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("productType", productType);
		paramMap.put("statusList", statusList);
		return this.queryForList("EbizSurrenderSPECDAOImpl.getSurrenderList", paramMap);
	}

}
