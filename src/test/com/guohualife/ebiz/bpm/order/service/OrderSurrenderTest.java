package com.guohualife.ebiz.bpm.order.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.guohualife.ebiz.bpm.base.BaseTester;
import com.guohualife.ebiz.bpm.order.dto.request.OrderSurrenderReqDTO;
import com.guohualife.ebiz.bpm.order.dto.response.OrderSurrenderResDTO;
import com.guohualife.platform.common.api.util.XmlUtil;

public class OrderSurrenderTest extends BaseTester{

	@SuppressWarnings("rawtypes")
	private Class[] clazz = {String.class, List.class};
	
	@Resource
	private OrderSurrenderService orderSurrenderService;
	
	@Test
	public void testGetOrderBasicInfo(){
		OrderSurrenderReqDTO orderSurrenderReqDTO = new OrderSurrenderReqDTO();
		orderSurrenderReqDTO.setOrderNo("20150425010100873831");
		orderSurrenderReqDTO.setSurrenderId(new BigDecimal("873801").longValue());
		OrderSurrenderResDTO orderSurrenderResDTO = orderSurrenderService.getSurrenderDetail(orderSurrenderReqDTO);
		System.out.println("查询订单赎回信息：\n" + XmlUtil.toXml(orderSurrenderResDTO, clazz));
	}
	
}
