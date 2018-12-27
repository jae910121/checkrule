package com.guohualife.ebiz.bpm.order.service;


/**
 * 订单批处理Service接口
 * 
 * @author wangxulu
 *
 */
public interface OrderBatchService {

	/**
	 * 处理支付中订单
	 */
	public void dealPayingOrder();
	
	/**
	 * 处理赎回
	 */
	public void dealSurrender();
	
}
