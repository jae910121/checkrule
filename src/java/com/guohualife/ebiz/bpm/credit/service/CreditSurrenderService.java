package com.guohualife.ebiz.bpm.credit.service;

import com.guohualife.edb.bpm.model.EbizSurrender;

/**
 * 债权赎回Service接口
 * 
 * @author wangxulu
 * 
 */
public interface CreditSurrenderService {

	/**
	 * 处理赎回
	 * 
	 * @param ebizSurrender
	 */
	public void dealSurrender(EbizSurrender ebizSurrender);	
	
	/**
	 * 处理赎回资金划拨
	 * 
	 */
	public void dealSurrenderTransfer();
	
	/**
	 * 处理资金赎回划拨状态
	 */
	public void dealSurrenderTransferResult();
	
	/**
	 * 到期自动赎回
	 */
	public void autoSurrender();
	
}
