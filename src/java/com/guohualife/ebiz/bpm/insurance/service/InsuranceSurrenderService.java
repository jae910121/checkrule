package com.guohualife.ebiz.bpm.insurance.service;

import com.guohualife.edb.bpm.model.EbizSurrender;

/**
 * 保险赎回Service接口
 * 
 * @author wangxulu
 * 
 */
public interface InsuranceSurrenderService {

	/**
	 * 处理赎回
	 * 
	 * @param ebizSurrender
	 */
	public void dealSurrender(EbizSurrender ebizSurrender);
	
}
