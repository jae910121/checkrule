package com.guohualife.ebiz.bpm.order.service;

import java.util.List;

import javax.annotation.Resource;

import com.guohualife.ebiz.bpm.base.BaseTester;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyInfoDTO;
import com.guohualife.ebiz.bpm.order.dto.response.OrderResDTO;
import com.guohualife.edb.bpm.model.EbizPolicy;
import com.guohualife.edb.bpm.model.EbizPolicyBnf;
import com.guohualife.edb.bpm.model.EbizPolicyInsured;

public class OrderServiceTest extends BaseTester{
	
	@SuppressWarnings("rawtypes")
	private Class[] clazz = {String.class, List.class, OrderResDTO.class, 
		PolicyInfoDTO.class, EbizPolicy.class, EbizPolicyInsured.class, EbizPolicyBnf.class};
	
	@Resource
	private OrderService orderService;
	
//	@Resource
//	private PayAndSignQueryBatch payAndSignQueryBatch;
	
//	@Test
//	public void testGetOrderBasicInfo(){
//		OrderQueryReqDTO orderQueryReqDTO = new OrderQueryReqDTO();
//		orderQueryReqDTO.setOrderNo("20150425010100873831");
//		orderQueryReqDTO.setCustomerId(new BigDecimal("96798").longValue());
//		OrderQueryResDTO orderQueryResDTO = orderService.getOrderInfo(orderQueryReqDTO);
//		System.out.println("查询订单基本信息：\n" + XmlUtil.toXml(orderQueryResDTO, clazz));
//	}
//	
//	@Test
//	public void testGetOrderDetailInfo(){
//		OrderReqDTO orderQueryReqDTO = new OrderReqDTO();
//		orderQueryReqDTO.setOrderNo("20150425010100873831");
//		orderQueryReqDTO.setCustomerId(new BigDecimal("96798").longValue());
//		orderQueryReqDTO.setQueryDetailType("2");
//		OrderResDTO orderQueryResDTO = orderService.getOrderInfo(orderQueryReqDTO);
//		System.out.println("查询订单详细信息：\n" + XmlUtil.toXml(orderQueryResDTO, clazz));
//	}
	
//	@Test
//	public void testQueryPayingOrder() throws Exception{
//		payAndSignQueryBatch.execute();
//	}

}
