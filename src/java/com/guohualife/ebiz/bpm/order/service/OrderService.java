package com.guohualife.ebiz.bpm.order.service;

import javax.jws.WebService;

import com.guohualife.ebiz.bpm.order.dto.request.OrderReqDTO;
import com.guohualife.ebiz.bpm.order.dto.response.OrderResDTO;


/**
 * 订单Service接口
 * 
 * @author wangxulu
 *
 */
@WebService
public interface OrderService {
	
	/**
	 * 查询订单信息
	 * 
	 * @param orderReqDTO
	 */
	public OrderResDTO getOrderDetail(OrderReqDTO orderReqDTO);
}
