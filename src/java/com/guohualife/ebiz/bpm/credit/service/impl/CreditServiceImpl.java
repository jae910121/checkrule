/**
 * 
 */
package com.guohualife.ebiz.bpm.credit.service.impl;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.base.constants.Constants;
import com.guohualife.ebiz.bpm.credit.bo.CreditBo;
import com.guohualife.ebiz.bpm.credit.check.bo.CheckBo;
import com.guohualife.ebiz.bpm.credit.check.constant.ConstantsForCheckRule;
import com.guohualife.ebiz.bpm.credit.check.dto.CheckRuleInputDTO;
import com.guohualife.ebiz.bpm.credit.check.dto.CheckRuleResultDTO;
import com.guohualife.ebiz.bpm.credit.constants.ConstantsForCredit;
import com.guohualife.ebiz.bpm.credit.constants.ConstantsForCredit.ENUM_PAY_CUSTOMER_CARD_TYPE;
import com.guohualife.ebiz.bpm.credit.dto.CreditDTO;
import com.guohualife.ebiz.bpm.credit.dto.CreditSuccessDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditApplyPurchaseReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderApplyReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderChargeReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderInfoReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditApplyPurchaseResDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderApplyResDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderChargeResDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderInfoResDTO;
import com.guohualife.ebiz.bpm.credit.service.CreditService;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.bo.OrderSurrenderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.bpm.order.dto.OrderAccountDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderSuccessDTO;
import com.guohualife.ebiz.bpm.order.dto.SurrenderApplyDTO;
import com.guohualife.ebiz.common.config.constant.ConstantsForConfig;
import com.guohualife.ebiz.common.pay.constant.ENUM_PAY_ACCOUNT_TYPE;
import com.guohualife.ebiz.common.pay.constant.ENUM_PAY_BUSINESS_TYPE;
import com.guohualife.ebiz.common.pay.constant.ENUM_PAY_CARD_TYPE;
import com.guohualife.ebiz.common.pay.constant.ENUM_PAY_CHANNEL_TYPE;
import com.guohualife.ebiz.common.pay.constant.ENUM_PAY_PRIVATE_TYPE;
import com.guohualife.ebiz.common.pay.constant.ENUM_PAY_TRADE_STATUS_TYPE;
import com.guohualife.ebiz.common.pay.dto.request.WithholdingReqDTO;
import com.guohualife.ebiz.common.pay.dto.response.WithholdingResDTO;
import com.guohualife.ebiz.common.pay.service.PayService;
import com.guohualife.ebiz.customer.account.dto.CustomerQryResultDTO;
import com.guohualife.ebiz.customer.account.dto.base.CustomerInfoDTO;
import com.guohualife.ebiz.trace.dto.EbizOperHisDTO;
import com.guohualife.ebiz.trace.service.TraceService;
import com.guohualife.platform.common.api.exception.ServiceException;
import com.guohualife.platform.common.api.util.ReflectionUtil;
import com.guohualife.platform.common.api.util.StringUtil;
import com.guohualife.platform.common.api.util.XmlUtil;

/**
 * 债权Service接口实现
 * 
 * @author wangxulu
 *
 */
@Service
public class CreditServiceImpl implements CreditService {

	private static final Log logger = LogFactory
			.getLog(CreditServiceImpl.class);

	@Resource
	private OrderBo orderBo;
	
	@Resource
	private OrderSurrenderBo orderSurrenderBo;

	@Resource
	private CreditBo creditBo;

	@Resource
	private PayService payService;

	@Resource
	private TraceService traceService;
	
	@Resource
	private CheckBo checkBo;

	/**
	 * 申购
	 * 
	 * @param creditApplyPurchaseReqDTO
	 * @return
	 * @throws ServiceException
	 */
	public CreditApplyPurchaseResDTO applyPurchase(
			CreditApplyPurchaseReqDTO creditApplyPurchaseReqDTO)
			throws ServiceException {

		logger.info("申购接口applyPurchase入参CreditApplyPurchaseReqDTO：\n"
				+ XmlUtil.toXml(creditApplyPurchaseReqDTO, new Class[] {}));

		CreditApplyPurchaseResDTO creditApplyPurchaseResDTO = new CreditApplyPurchaseResDTO();
		
		// 必录字段校验
		String errorMessage = checkCreditApplyInfo(creditApplyPurchaseReqDTO);
		if (!"".equals(errorMessage)){
			creditApplyPurchaseResDTO.setFailReason(errorMessage);
			creditApplyPurchaseResDTO.setStatus(ConstantsForCredit.FAILED);
			return creditApplyPurchaseResDTO;
		}
		
		// 检查渠道是否正确
		if (!orderBo.checkOrderType(creditApplyPurchaseReqDTO.getProductCode(),
				creditApplyPurchaseReqDTO.getOrderType())) {
			logger.info("productCode = "
					+ creditApplyPurchaseReqDTO.getProductCode() + ",refValue="
					+ creditApplyPurchaseReqDTO.getOrderType()
					+ ",propertyType=order_type.该渠道暂不支持购买");
			creditApplyPurchaseResDTO.setFailReason("该渠道暂不支持购买");
			creditApplyPurchaseResDTO.setStatus(ConstantsForCredit.FAILED);
			return creditApplyPurchaseResDTO;
		}
		
		/*
		 * 1.保存Order(thirdOrder)、Credit
		 */
		Long customerId = creditApplyPurchaseReqDTO.getCustomerId();
		CustomerQryResultDTO customerQryResultDTO = orderBo
				.getCustomerInfo(customerId);
		// 购买校验
		if (customerQryResultDTO == null
				|| CollectionUtils.isEmpty(customerQryResultDTO
						.getCustomerDTOList())) {
			logger.info("查询不到客户" + customerId + "信息");
			creditApplyPurchaseResDTO.setStatus(Constants.FAILED);
			creditApplyPurchaseResDTO.setFailReason("查询不到客户" + customerId
					+ "信息");
			return creditApplyPurchaseResDTO;
		}
		CustomerInfoDTO customerInfoDTO = customerQryResultDTO
				.getCustomerDTOList().get(0);
		String customerName = customerInfoDTO.getName();

		OrderAccountDTO orderAccountDTO = new OrderAccountDTO();
		orderAccountDTO.setPayMoney(creditApplyPurchaseReqDTO.getOrderAmount());
		orderAccountDTO.setBankCode(creditApplyPurchaseReqDTO.getBankCode());
		orderAccountDTO.setCardBookType(creditApplyPurchaseReqDTO
				.getCardBookType());
		orderAccountDTO.setCardBookCode(creditApplyPurchaseReqDTO
				.getCardBookCode());
		

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setCustomerId(creditApplyPurchaseReqDTO.getCustomerId());
		orderDTO.setOrderAmount(creditApplyPurchaseReqDTO.getOrderAmount());
		orderDTO.setOrderType(creditApplyPurchaseReqDTO.getOrderType());
		orderDTO.setProductCode(creditApplyPurchaseReqDTO.getProductCode());
		if (creditApplyPurchaseReqDTO.getActivityDTO() != null) {
			orderDTO.setActivityDTO(creditApplyPurchaseReqDTO.getActivityDTO());
		}
		// 保存订单
		String orderNo = orderBo.saveOrder(orderDTO);
		
		creditApplyPurchaseResDTO.setOrderNo(orderNo);
		
		CreditDTO creditDTO = new CreditDTO();
		creditDTO.setInvestAmount(creditApplyPurchaseReqDTO.getOrderAmount());
		creditDTO.setMult(creditApplyPurchaseReqDTO.getMult());
		creditDTO.setOrderNo(orderNo);
		// 保存债权
		creditBo.saveCredit(creditDTO);
		// 更新订单状态.
		orderAccountDTO.setOrderType(creditApplyPurchaseReqDTO.getOrderType());
		orderAccountDTO.setOrderNo(creditApplyPurchaseResDTO.getOrderNo());
		orderBo.applyOrderPay(orderAccountDTO);
		/*
		 * 2.调用支付
		 */
		WithholdingReqDTO withholdingReqDTO = new WithholdingReqDTO();
		withholdingReqDTO.setMoney(orderAccountDTO.getPayMoney());
		withholdingReqDTO.setCertificateType(ENUM_PAY_CARD_TYPE
				.getEnumByValue(ENUM_PAY_CUSTOMER_CARD_TYPE
						.getPayByCustomer(customerInfoDTO.getIdtype()
								.toString())));
		withholdingReqDTO.setCertificateCode(customerInfoDTO.getIdno());
		withholdingReqDTO.setBusinessType(ENUM_PAY_BUSINESS_TYPE.YIQIAN);
		withholdingReqDTO.setBankCode(orderAccountDTO.getBankCode());
		withholdingReqDTO.setCardBookType(ENUM_PAY_ACCOUNT_TYPE
				.getEnumByValue(orderAccountDTO.getCardBookType()));
		withholdingReqDTO.setCardBookCode(orderAccountDTO.getCardBookCode());
		withholdingReqDTO.setCardHolder(customerInfoDTO.getRealName());
		withholdingReqDTO.setThirdPartType(ENUM_PAY_CHANNEL_TYPE.DEFAULT);
		withholdingReqDTO.setBusinessDocumentCode(orderNo);
		withholdingReqDTO.setIsPrivate(ENUM_PAY_PRIVATE_TYPE.IS_PRIVATE);
		WithholdingResDTO withholdingResDTO = payService
				.withholding(withholdingReqDTO);
		
		
		String operDesc = "订单" + orderNo + "申购";
		if (ENUM_PAY_TRADE_STATUS_TYPE.SUCCESS.getValue().equals(
				withholdingResDTO.getResponseCode())) {
			creditApplyPurchaseResDTO.setStatus(ConstantsForCredit.SUCCESS);
			operDesc += "成功";
			
			CreditSuccessDTO creditSuccessDTO = new CreditSuccessDTO();
			creditSuccessDTO.setSuccessDate(new Date());
			creditSuccessDTO.setOrderNo(orderNo);
			creditBo.dealCreditSuccess(creditSuccessDTO);
			
			OrderSuccessDTO orderSuccessDTO = new OrderSuccessDTO();
			orderSuccessDTO.setValueDate(creditSuccessDTO.getValueDate());
			orderSuccessDTO.setExpiryDate(creditSuccessDTO.getExpiryDate());
			orderSuccessDTO.setOrderType(orderDTO.getOrderType());
			orderSuccessDTO.setOrderNo(orderNo);
			orderSuccessDTO.setSuccessDate(new Date());
			orderBo.dealOrderSuccess(orderSuccessDTO);
			
			creditApplyPurchaseResDTO.setCloseDate(orderSuccessDTO.getCloseDate());
			creditApplyPurchaseResDTO.setEndDate(orderSuccessDTO.getExpiryDate());
			creditApplyPurchaseResDTO.setStartDate(orderSuccessDTO.getValueDate());
			creditApplyPurchaseResDTO.setSuccessDate(orderSuccessDTO.getSuccessDate());
			
		}
		
		if (ENUM_PAY_TRADE_STATUS_TYPE.FAIL.getValue().equals(
				withholdingResDTO.getResponseCode())) {
			creditApplyPurchaseResDTO.setStatus(ConstantsForCredit.FAILED);
			creditApplyPurchaseResDTO.setFailReason(withholdingResDTO
					.getErrorMessage());
			operDesc += "支付失败,原因:" + withholdingResDTO.getErrorMessage();	
			orderBo.dealOrderFail(orderNo);
		}
		
		if (ENUM_PAY_TRADE_STATUS_TYPE.PROCESSING.getValue().equals(
				withholdingResDTO.getResponseCode())) {
			creditApplyPurchaseResDTO.setStatus(ConstantsForCredit.PROCESSING);
			operDesc += "正在处理中";
		} 
		
		if (ENUM_PAY_TRADE_STATUS_TYPE.REPEAT_REQU.getValue().equals(
				withholdingResDTO.getResponseCode())) {
			creditApplyPurchaseResDTO.setStatus(ConstantsForCredit.PROCESSING);// TODO 交易重复
			operDesc += "交易请求重复";
		}
		
		//处理后续状态的更新和操作(订单,债权).
		//creditBo.dealPurchaseResult(withholdingResDTO.getResponseCode(), orderNo, creditApplyPurchaseReqDTO.getOrderType());
		
		EbizOperHisDTO ebizOperHis = new EbizOperHisDTO();
		ebizOperHis.setCreatedUser("交易中心");
		ebizOperHis.setModifiedUser("交易中心");
		ebizOperHis.setOperDate(new Date());
		ebizOperHis.setOperUser(customerName);
		ebizOperHis
				.setOperType(ConstantsForConfig.ENUM_OPER_TYPE.ORDER_PURCHASE
						.getValue());
		ebizOperHis.setOperDesc(operDesc);
		ebizOperHis.setOrderNo(orderNo);
		traceService.saveOperHis(ebizOperHis);
		
		logger.info("申购接口applyPurchase出参CreditApplyPurchaseResDTO：\n"
				+ XmlUtil.toXml(creditApplyPurchaseResDTO, new Class[] {}));

		return creditApplyPurchaseResDTO;
	}

	

	/**
	 * 查询赎回信息
	 * 
	 * @param creditSurrenderInfoReqDTO
	 * @return
	 */
	public CreditSurrenderInfoResDTO getSurrenderInfo(
			CreditSurrenderInfoReqDTO creditSurrenderInfoReqDTO)
			throws ServiceException {
		logger.info("查询赎回信息接口入参：\n" + XmlUtil.toXml(creditSurrenderInfoReqDTO, new Class[]{}));
		CreditSurrenderInfoResDTO creditSurrenderInfoResDTO = new CreditSurrenderInfoResDTO();
		creditBo.getSurrenderInfo(creditSurrenderInfoReqDTO, creditSurrenderInfoResDTO);
		logger.info("查询赎回信息接口出参：\n" + XmlUtil.toXml(creditSurrenderInfoResDTO, new Class[]{}));

		return creditSurrenderInfoResDTO;
	}

	/**
	 * 赎回核算
	 * 
	 * @param creditSurrenderChargeReqDTO
	 * @return
	 * @throws ServiceException
	 */
	public CreditSurrenderChargeResDTO surrenderCharge(
			CreditSurrenderChargeReqDTO creditSurrenderChargeReqDTO)
			throws ServiceException {
		logger.info("赎回核算接口入参：\n" + XmlUtil.toXml(creditSurrenderChargeReqDTO, new Class[]{}));
		CreditSurrenderChargeResDTO creditSurrenderChargeResDTO = new CreditSurrenderChargeResDTO();
		creditBo.surrenderCharge(creditSurrenderChargeReqDTO, creditSurrenderChargeResDTO);
		logger.info("赎回核算接口出参：\n" + XmlUtil.toXml(creditSurrenderChargeResDTO, new Class[]{}));
		return creditSurrenderChargeResDTO;
	}

	/**
	 * 赎回提交
	 * 
	 * @param creditSurrenderApplyReqDTO
	 * @return
	 * @throws ServiceException
	 */
	public CreditSurrenderApplyResDTO applySurrender(
			CreditSurrenderApplyReqDTO creditSurrenderApplyReqDTO)
			throws ServiceException {
		logger.info("赎回提交接口入参：\n" + XmlUtil.toXml(creditSurrenderApplyReqDTO, new Class[]{}));
		CreditSurrenderApplyResDTO creditSurrenderApplyResDTO = new CreditSurrenderApplyResDTO();
		SurrenderApplyDTO surrenderApplyDTO = new SurrenderApplyDTO();
		ReflectionUtil.copyProperties(creditSurrenderApplyReqDTO, surrenderApplyDTO);
		surrenderApplyDTO.setSurrenderPrincipal(creditSurrenderApplyReqDTO.getSurrenderInvest());
		surrenderApplyDTO.setSurrenderType(ConstantsForOrder.ENUM_SURRENDER_TYPE.REDEEM.getValue());
		orderSurrenderBo.applySurrender(surrenderApplyDTO);
		if(surrenderApplyDTO.getSurrenderId() == null){
			creditSurrenderApplyResDTO.failure(surrenderApplyDTO.getErrorMessage());
		}
		
		logger.info("赎回提交接口出参：\n" + XmlUtil.toXml(creditSurrenderApplyResDTO, new Class[]{}));
		
		return creditSurrenderApplyResDTO;
	}
	
	/**
	 * 1. 必录字段校验 2. 单笔限额是否超限、累计购买金额是否超限
	 * @param creditApplyPurchaseReqDTO
	 * @return errorMessage
	 */
	private String checkCreditApplyInfo(CreditApplyPurchaseReqDTO creditApplyPurchaseReqDTO) {
		
		if (creditApplyPurchaseReqDTO.getCustomerId() == null){
			return "客户Id不能为空";
		}
		if (StringUtil.isBlank(creditApplyPurchaseReqDTO.getProductCode())){
			return "产品编码不能为空";
		}
		if (StringUtil.isBlank(creditApplyPurchaseReqDTO.getOrderType())){
			return "订单渠道不能为空";
		}
		if (creditApplyPurchaseReqDTO.getOrderAmount() == null){
			return "订单金额不能为空";
		}
		if (creditApplyPurchaseReqDTO.getMult() == null){
			return "份数不能为空";
		}
		if (StringUtil.isBlank(creditApplyPurchaseReqDTO.getBankCode())){
			return "银行编码不能为空";
		}
		if (StringUtil.isBlank(creditApplyPurchaseReqDTO.getCardBookCode())){
			return "卡折标志不能为空";
		}
		if (StringUtil.isBlank(creditApplyPurchaseReqDTO.getCardBookType())) {
			return "支付卡号不能为空";
		}
		
		// 开始进行单笔金额和累计金额校验.
		logger.info("开始进行单笔金额和累计金额校验.");
		CheckRuleInputDTO checkInputDTO = new CheckRuleInputDTO();
		checkInputDTO.setBusinessSubType(ConstantsForCheckRule.ENUM_SUB_BUSSINESS_TYPE.BUY.getValue());
		checkInputDTO.setBusinessType(ConstantsForCheckRule.ENUM_BUSSINESS_TYPE.CREDITAPPLY.getValue());
		checkInputDTO.setPlatformType(ConstantsForCheckRule.ENUM_PLATFORM_TYPE.WEBSITE.getValue());
		checkInputDTO.setProductCode(creditApplyPurchaseReqDTO.getProductCode());
		checkInputDTO.setOrderType(creditApplyPurchaseReqDTO.getOrderType());
		HashMap<String, Object> paramObject = new HashMap<String, Object>();
		paramObject.put("orderAmount", creditApplyPurchaseReqDTO.getOrderAmount());
		paramObject.put("customerId", creditApplyPurchaseReqDTO.getCustomerId());
		paramObject.put("productCode", creditApplyPurchaseReqDTO.getProductCode());
		checkInputDTO.setParamObject(paramObject);
		CheckRuleResultDTO checkRuleResultDTO = new CheckRuleResultDTO();
		try {
			logger.info("校验开始,校验输入参数:" + XmlUtil.toXml(checkInputDTO, new Class[] {}));
			checkRuleResultDTO = checkBo.check(checkInputDTO);
			if (checkRuleResultDTO.getResultCode().equals(ConstantsForCredit.FAILED)) {
				// 校验失败
				logger.info("债权申购校验失败" + checkRuleResultDTO.getResultMessage());
				return checkRuleResultDTO.getResultMessage();
			}
			logger.info(creditApplyPurchaseReqDTO.getCustomerId() + "债权申购,校验通过");
		} catch (Exception e) {
			logger.warn("债权申购校验异常" + e);
			return "债权申购校验异常";
		}
		
		return "";
	}
	
}
