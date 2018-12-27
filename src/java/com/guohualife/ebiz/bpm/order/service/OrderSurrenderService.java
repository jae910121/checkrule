package com.guohualife.ebiz.bpm.order.service;

import javax.jws.WebService;

import com.guohualife.ebiz.bpm.order.dto.request.OrderSurrenderReqDTO;
import com.guohualife.ebiz.bpm.order.dto.response.OrderSurrenderResDTO;
import com.guohualife.edb.bpm.model.EbizSurrender;

/**
 * 订单赎回Service接口
 * 
 * @author wangxulu
 * 
 */
@WebService
public interface OrderSurrenderService {
	
	/**
	 * 订单赎回信息查询
	 * 
	 * @param orderSurrenderReqDTO
	 * @return
	 */
	public OrderSurrenderResDTO getSurrenderDetail(
			OrderSurrenderReqDTO orderSurrenderReqDTO);
	
	/**
	 * 处理赎回
	 * 
	 * @param ebizSurrender
	 */
	public void dealSurrender(EbizSurrender ebizSurrender);
}
