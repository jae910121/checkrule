package com.guohualife.ebiz.bpm.order.service.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.credit.service.CreditSurrenderService;
import com.guohualife.ebiz.bpm.insurance.service.InsuranceSurrenderService;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.bo.OrderSurrenderBo;
import com.guohualife.ebiz.bpm.order.dto.request.OrderSurrenderReqDTO;
import com.guohualife.ebiz.bpm.order.dto.response.OrderSurrenderResDTO;
import com.guohualife.ebiz.bpm.order.service.OrderSurrenderService;
import com.guohualife.ebiz.product.constant.ConstantsForProduct;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizOrderAccount;
import com.guohualife.edb.bpm.model.EbizSurrender;
import com.guohualife.platform.common.api.util.StringUtil;
import com.guohualife.platform.common.api.util.XmlUtil;

/**
 * 订单赎回Service实现
 * 
 * @author wangxulu
 *
 */
@Service
public class OrderSurrenderServiceImpl implements OrderSurrenderService {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private OrderBo orderBo;
	
	@Resource
	private OrderSurrenderBo orderSurrenderBo;
	
	@Resource
	private InsuranceSurrenderService insuranceSurrenderService;
	
	@Resource
	private CreditSurrenderService creditSurrenderService;
	
	/**
	 * 订单赎回详细信息查询
	 * 
	 * @param orderSurrenderReqDTO
	 * @return
	 */
	public OrderSurrenderResDTO getSurrenderDetail(
			OrderSurrenderReqDTO orderSurrenderReqDTO) {
		OrderSurrenderResDTO orderSurrenderResDTO = new OrderSurrenderResDTO();
		// 必录字段校验
		String errorMessage = checkOrderSurrenderResInfo(orderSurrenderReqDTO);
		if (StringUtil.isNotEmpty(errorMessage)){
			logger.info("订单赎回详细信息查询必填字段校验不通过，错误信息：" + errorMessage);
			return orderSurrenderResDTO;
		}
		logger.info("订单赎回信息查询接口getSurrenderDetail入参OrderSurrenderReqDTO：\n"
				+ XmlUtil.toXml(orderSurrenderReqDTO, new Class[] {}));
		Long surrenderId = orderSurrenderReqDTO.getSurrenderId();
		logger.info("赎回ID" + surrenderId + "详细信息");
		EbizSurrender ebizSurrender = orderSurrenderBo.getSurrenderById(surrenderId);
		
		if (ebizSurrender != null) {
			// 订单号
			String orderNo = ebizSurrender.getOrderNo();
			EbizOrder ebizOrder = orderBo.getOrder(orderNo);
			EbizOrderAccount ebizOrderAccount = orderBo.getOrderAccount(orderNo);
			orderSurrenderResDTO.setBankCode(ebizOrderAccount.getBankCode());
			orderSurrenderResDTO.setBankName(ebizOrderAccount.getBankName());
			//截取后四位
			orderSurrenderResDTO.setRecAccountNo(StringUtil.substring(ebizOrderAccount.getCardBookCode(), ebizOrderAccount.getCardBookCode().length() - 4));
			orderSurrenderResDTO.setOrderNo(ebizOrder.getOrderNo());
			orderSurrenderResDTO.setProductCode(ebizOrder.getProductCode());
			orderSurrenderResDTO.setProductName(ebizOrder.getProductName());
			orderSurrenderResDTO.setPlatfotmType(ebizSurrender.getPlatformType());
			orderSurrenderResDTO.setStatus(ebizSurrender.getStatus());
			orderSurrenderResDTO.setSurrenderDate(ebizSurrender.getSurrenderDate());
			orderSurrenderResDTO.setSurrenderType(ebizSurrender.getSurrenderType());
			orderSurrenderResDTO.setActualMoney(ebizSurrender.getSurrenderAmount());
			orderSurrenderResDTO.setApplyMoney(ebizSurrender.getApplyAmount());
			// 赎回本金
			orderSurrenderResDTO.setSurrenderPrincipal(ebizSurrender.getSurrenderPrincipal());
			// 手续费
			orderSurrenderResDTO.setSurrenderFee(ebizSurrender.getSurrenderFee());
			
		}
		logger.info("订单赎回信息查询接口getSurrenderDetail出参OrderSurrenderResDTO：\n"
				+ XmlUtil.toXml(orderSurrenderResDTO, new Class[] {}));
		return orderSurrenderResDTO;
		
	}

	/**
	 * 处理赎回
	 * 
	 * @param ebizSurrender
	 */
	public void dealSurrender(EbizSurrender ebizSurrender) {
		EbizOrder ebizOrder = orderBo.getOrder(ebizSurrender.getOrderNo());

		if (ConstantsForProduct.ENUM_PRODUCT_TYPE.INSURANCE.getValue().equals(
				ebizOrder.getProductType())) {
			insuranceSurrenderService.dealSurrender(ebizSurrender);
		}

		if (ConstantsForProduct.ENUM_PRODUCT_TYPE.CREDITOR.getValue().equals(
				ebizOrder.getProductType())) {
			creditSurrenderService.dealSurrender(ebizSurrender);
			
			orderSurrenderBo.sendSurrenderSuccessMsg(ebizSurrender, ebizOrder);
		}
		
		
	}

	/**
	 * 1. 必录字段校验
	 * @param orderSurrenderReqDTO
	 * @return errorMessage
	 */
	private String checkOrderSurrenderResInfo(OrderSurrenderReqDTO orderSurrenderReqDTO) {
		
		if (orderSurrenderReqDTO.getCustomerId() == null){
			return "客户Id不能为空";
		}
		if (orderSurrenderReqDTO.getSurrenderId() == null){
			return "赎回Id不能为空";
		}
		return "";
	}
	
}
