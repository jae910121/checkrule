package com.guohualife.ebiz.bpm.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.base.bo.BpmBaseBo;
import com.guohualife.ebiz.bpm.base.constants.Constants;
import com.guohualife.ebiz.bpm.credit.bo.CreditBo;
import com.guohualife.ebiz.bpm.credit.dto.CreditSuccessDTO;
import com.guohualife.ebiz.bpm.insurance.bo.InsuranceBo;
import com.guohualife.ebiz.bpm.insurance.constants.ConstantsForInsurance;
import com.guohualife.ebiz.bpm.insurance.dto.PolicySuccessDTO;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.bo.OrderSurrenderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.bpm.order.dto.OrderListQueryDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderSuccessDTO;
import com.guohualife.ebiz.bpm.order.service.OrderBatchService;
import com.guohualife.ebiz.bpm.order.service.OrderSurrenderService;
import com.guohualife.ebiz.common.config.constant.ConstantsForConfig;
import com.guohualife.ebiz.common.pay.constant.ENUM_PAY_TRADE_STATUS_TYPE;
import com.guohualife.ebiz.common.pay.dto.request.BusinessQueryReqDTO;
import com.guohualife.ebiz.common.pay.dto.response.BusinessQueryResDTO;
import com.guohualife.ebiz.common.pay.service.PayService;
import com.guohualife.ebiz.gateway.ins.constants.ENUM_CONTSTATUS_INS_TYPE;
import com.guohualife.ebiz.gateway.ins.dto.request.ContStatusReqDTO;
import com.guohualife.ebiz.gateway.ins.dto.response.ContStatusResDTO;
import com.guohualife.ebiz.gateway.ins.service.InsTradeService;
import com.guohualife.ebiz.product.constant.ConstantsForProduct;
import com.guohualife.ebiz.trace.dto.EbizOperHisDTO;
import com.guohualife.ebiz.trace.service.TraceService;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizPolicy;
import com.guohualife.edb.bpm.model.EbizSurrender;
import com.guohualife.platform.common.api.exception.ServiceException;

@Service
public class OrderBatchServiceImpl implements OrderBatchService {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private OrderBo orderBo;
	
	@Resource
	private OrderSurrenderBo orderSurrenderBo;
	
	@Resource
	private InsuranceBo insuranceBo;
	
	@Resource
	private TraceService traceService;
	
	@Resource
	private CreditBo creditBo;
	
	@Resource
	private BpmBaseBo bpmBaseBo;

	@Resource
	private InsTradeService insTradeService;
	
	@Resource
	private PayService payService;
	
	@Resource
	private OrderSurrenderService orderSurrenderService;
	
	/**
	 * 处理支付中订单
	 * 
	 * @param ebizOrder
	 */
	public void dealPayingOrder(){
		
		// 成功笔数
		Integer successNum = 0;
		// 异常笔数
		Integer exceptionNum = 0;
		
		List<String> statusList = new ArrayList<String>();
		statusList.add(ConstantsForOrder.ENUM_ORDER_STATUS.PAYING.getValue());
		OrderListQueryDTO orderListQueryDTO = new OrderListQueryDTO();
		orderListQueryDTO.setStatusList(statusList);
		List<EbizOrder> ebizOrderList = orderBo.getOrderList(orderListQueryDTO);
		logger.info("本次共扫描到数据" + ebizOrderList.size() + "条");
		
		if(ebizOrderList != null && ebizOrderList.size() > 0){
			for(EbizOrder ebizOrder : ebizOrderList){
				try{
					String orderNo = ebizOrder.getOrderNo();
					if((new Date().getTime() - ebizOrder.getCommitDate().getTime()) / (60 * 1000) < 2){
						continue;
					}
					
					if(ConstantsForProduct.ENUM_PRODUCT_TYPE.INSURANCE.getValue().equals(
							ebizOrder.getProductType())){
						
						ContStatusResDTO contStatusResDTO = queryOrderWithGateway(orderNo);
						dealQueryResultWithGetway(contStatusResDTO, ebizOrder);
					}
					
					if(ConstantsForProduct.ENUM_PRODUCT_TYPE.CREDITOR.getValue().equals(
							ebizOrder.getProductType())){
						BusinessQueryResDTO businessQueryResDTO = queryOrderWithPayment(orderNo);
						dealQueryResultWithPayment(businessQueryResDTO, ebizOrder);
					}
					successNum++;
				}catch(Exception e){
					logger.warn("订单" + ebizOrder.getOrderNo() + "支付中处理异常", e);
	            	exceptionNum++;
	            	continue;
				}
			}
		}
		logger.info("处理成功" + successNum + "条，异常：" + exceptionNum + "条");
	}
	
	/**
	 * 处理赎回
	 * 
	 * @param ebizSurrender
	 */
	public void dealSurrender() {
		
		// 成功笔数
		Integer successNum = 0;
		// 异常笔数
		Integer exceptionNum = 0;

		List<String> statusList = new ArrayList<String>();
		statusList.add(ConstantsForOrder.ENUM_SURRENDER_STATUS.INFORM.getValue());

		List<EbizSurrender> surrenderList = 
				orderSurrenderBo.getSurrenderByStatus(statusList);
		logger.info("本次共扫描到数据" + surrenderList.size() + "条");

		if (surrenderList != null && surrenderList.size() > 0) {
			for (EbizSurrender ebizSurrender : surrenderList) {
				try {
					orderSurrenderService.dealSurrender(ebizSurrender);
					successNum++;
				} catch (ServiceException e) {
					logger.warn("订单" + ebizSurrender.getOrderNo() + "赎回处理异常", e);
					exceptionNum++;
					continue;
				}
			}
		}

		logger.info("本次批处理成功" + successNum + "条，异常：" + exceptionNum + "条");
				
	}
	
	private ContStatusResDTO queryOrderWithGateway(String orderNo){
		ContStatusReqDTO contStatusReqDTO = new ContStatusReqDTO();
		contStatusReqDTO.setOrderId(orderNo);
		contStatusReqDTO.setThirdType(bpmBaseBo.getThirdType(orderNo));
		
		long start = System.currentTimeMillis();
		logger.info("订单" + orderNo + "调用gateway查询状态接口开始");
		ContStatusResDTO contStatusResDTO = insTradeService.doQueryContStatus(contStatusReqDTO);
		logger.info("订单" + orderNo + "调用gateway查询状态接口结束，耗时："
				+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
		return contStatusResDTO;
	}
	
	private void dealQueryResultWithGetway(ContStatusResDTO contStatusResDTO,
			EbizOrder ebizOrder) {
		String orderNo = ebizOrder.getOrderNo();
		
		String responseCode = contStatusResDTO.getResponseCode();
		String errorMessage = contStatusResDTO.getErrorMessage();
		
		String queryResult = "订单" + orderNo + "查询状态：";
		
		if(Constants.FAILED.equals(responseCode)){
			queryResult = queryResult + "失败：" + errorMessage;
			logger.info(queryResult);
			return;
		}
		
		ENUM_CONTSTATUS_INS_TYPE status = contStatusResDTO.getContStatus();
		queryResult = queryResult + status.getName();
		logger.info(queryResult);
		if(ENUM_CONTSTATUS_INS_TYPE.PAYMENT_PROCESSING.equals(status)){
			return;
		}
		
		if(ENUM_CONTSTATUS_INS_TYPE.UNDERWRIT_SUCCESS.equals(status)){
			ebizOrder.setOrderStatus(ConstantsForOrder.ENUM_ORDER_STATUS.WAITPAY.getValue());
			orderBo.saveOrUpdateOrder(ebizOrder);
			
			EbizPolicy ebizPolicy = insuranceBo.getPolicyByOrderNo(orderNo);
			ebizPolicy.setProcessStatus(
					ConstantsForInsurance.ENUM_POLICY_STATUS.UNDERWRITESUCCESS.getValue());
			insuranceBo.saveOrUpdatePolicy(ebizPolicy);
			return;
		}
		
		if(ENUM_CONTSTATUS_INS_TYPE.INSURED_SUCCESS.equals(status)){
			String policyNo = contStatusResDTO.getPolicyNo();
			
			PolicySuccessDTO policySuccessDTO = new PolicySuccessDTO();
			policySuccessDTO.setOrderNo(orderNo);
			policySuccessDTO.setPolicyNo(policyNo);
			policySuccessDTO.setSuccessDate(contStatusResDTO.getIssuedTime());
			policySuccessDTO.setProductCode(ebizOrder.getProductCode());
			policySuccessDTO.setOrderType(ebizOrder.getOrderType());
			insuranceBo.dealPolicySuccess(policySuccessDTO);
			
			OrderSuccessDTO orderSuccessDTO = new OrderSuccessDTO();
			orderSuccessDTO.setBusinessNo(policyNo);
			orderSuccessDTO.setOrderNo(orderNo);
			orderSuccessDTO.setSuccessDate(contStatusResDTO.getIssuedTime());
			orderSuccessDTO.setValueDate(contStatusResDTO.getInsBeginDate());
			orderSuccessDTO.setProductCode(ebizOrder.getProductCode());
			orderSuccessDTO.setOrderType(ebizOrder.getOrderType());
			orderBo.dealOrderSuccess(orderSuccessDTO);
			
			EbizOperHisDTO ebizOperHisDTO = new EbizOperHisDTO();
			ebizOperHisDTO.setCreatedUser("支付中查询状态批处理");
			ebizOperHisDTO.setModifiedUser("支付中查询状态批处理");
			ebizOperHisDTO.setOperUser("支付中查询状态批处理");
			ebizOperHisDTO.setOperDesc(queryResult);
			ebizOperHisDTO.setOperType(ConstantsForConfig.ENUM_OPER_TYPE.ORDER_PURCHASE.getValue());
			ebizOperHisDTO.setOrderNo(orderNo);
			traceService.saveOperHis(ebizOperHisDTO);
		}
	}
	
	private BusinessQueryResDTO queryOrderWithPayment(String orderNo){
		BusinessQueryReqDTO businessQueryReqDTO = new BusinessQueryReqDTO();
		businessQueryReqDTO.setBusinessDocumentCode(orderNo);
		
		long start = System.currentTimeMillis();
		logger.info("订单" + orderNo + "调用支付平台交易查询接口开始");
		BusinessQueryResDTO businessQueryResDTO =
				payService.queryBusiness(businessQueryReqDTO);
		logger.info("订单" + orderNo + "调用支付平台交易查询接口结束，耗时："
				+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
		
		return businessQueryResDTO;
	}
	
	private void dealQueryResultWithPayment(
			BusinessQueryResDTO businessQueryResDTO, EbizOrder ebizOrder) {
		String orderNo = ebizOrder.getOrderNo();
		
		String resultCode = businessQueryResDTO.getResponseCode();
		logger.info("订单" + orderNo + "查询状态：" + ENUM_PAY_TRADE_STATUS_TYPE.getNameByValue(resultCode));
		
		if (ENUM_PAY_TRADE_STATUS_TYPE.SUCCESS.getValue().equals(resultCode)) {
			
			CreditSuccessDTO creditSuccessDTO = new CreditSuccessDTO();
			creditSuccessDTO.setOrderNo(orderNo);
			creditSuccessDTO.setSuccessDate(new Date());
			creditBo.dealCreditSuccess(creditSuccessDTO);
			
			OrderSuccessDTO orderSuccessDTO = new OrderSuccessDTO();
			orderSuccessDTO.setValueDate(creditSuccessDTO.getValueDate());
			orderSuccessDTO.setExpiryDate(creditSuccessDTO.getExpiryDate());
			orderSuccessDTO.setOrderType(ebizOrder.getOrderType());
			orderSuccessDTO.setOrderNo(orderNo);
			orderSuccessDTO.setSuccessDate(new Date());
			orderBo.dealOrderSuccess(orderSuccessDTO);
		}
		
		if (ENUM_PAY_TRADE_STATUS_TYPE.FAIL.getValue().equals(resultCode)) {
			orderBo.dealOrderFail(orderNo);
		}
		
		if (ENUM_PAY_TRADE_STATUS_TYPE.PROCESSING.getValue().equals(resultCode)) {
			return;
		} 
		
		if (ENUM_PAY_TRADE_STATUS_TYPE.REPEAT_REQU.getValue().equals(resultCode)) {
			return; // TODO
		}
	}
}
