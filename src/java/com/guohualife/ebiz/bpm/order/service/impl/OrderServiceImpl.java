/**
 * 
 */
package com.guohualife.ebiz.bpm.order.service.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.dto.request.OrderReqDTO;
import com.guohualife.ebiz.bpm.order.dto.response.OrderResDTO;
import com.guohualife.ebiz.bpm.order.service.OrderService;
import com.guohualife.platform.common.api.util.XmlUtil;

/**
 * 订单Service实现
 * 
 * @author wangxulu
 *
 */
@Service
public class OrderServiceImpl implements OrderService {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private OrderBo orderBo;

	/**
	 * 查询订单信息
	 * 
	 * @param orderReqDTO
	 */
	public OrderResDTO getOrderDetail(OrderReqDTO orderReqDTO) {
		logger.info("查询订单信息接口getOrderInfo入参OrderReqDTO：\n" + XmlUtil.toXml(orderReqDTO, new Class[]{}));
		logger.info("查询" + orderReqDTO.getOrderNo() + "订单详细信息");
		OrderResDTO orderResDTO = new OrderResDTO();
		orderBo.getOrderInfo(orderReqDTO, orderResDTO);
		logger.info("查询订单信息接口getOrderInfo出参OrderResDTO：\n" + XmlUtil.toXml(orderResDTO, new Class[]{}));
		return orderResDTO;
	}
	
}
