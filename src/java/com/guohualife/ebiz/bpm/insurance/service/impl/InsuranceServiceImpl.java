package com.guohualife.ebiz.bpm.insurance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.asset.constants.ConstantsForAsset;
import com.guohualife.ebiz.bpm.asset.dto.AssetModifyDTO;
import com.guohualife.ebiz.bpm.asset.service.AssetModifyService;
import com.guohualife.ebiz.bpm.base.bo.BpmBaseBo;
import com.guohualife.ebiz.bpm.base.constants.Constants;
import com.guohualife.ebiz.bpm.insurance.bo.InsuranceBo;
import com.guohualife.ebiz.bpm.insurance.constants.ConstantsForInsurance;
import com.guohualife.ebiz.bpm.insurance.dto.AppntDTO;
import com.guohualife.ebiz.bpm.insurance.dto.BnfDTO;
import com.guohualife.ebiz.bpm.insurance.dto.InsuredDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicySuccessDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.AcceptInsuranceReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.PolicySurrenderApplyReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.PolicySurrenderChargeReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.PolicySurrenderInfoReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.UnderwriteReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.AcceptInsuranceResDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.PolicySurrenderApplyResDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.PolicySurrenderChargeResDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.PolicySurrenderInfoResDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.UnderwriteResDTO;
import com.guohualife.ebiz.bpm.insurance.service.InsuranceService;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.bo.OrderSurrenderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.bpm.order.dto.OrderAccountDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderSuccessDTO;
import com.guohualife.ebiz.bpm.order.dto.SurrenderApplyDTO;
import com.guohualife.ebiz.bpm.order.dto.request.PolicySurrenderCallbackReqDTO;
import com.guohualife.ebiz.bpm.order.dto.response.PolicySurrenderCallbackResDTO;
import com.guohualife.ebiz.common.config.constant.ConstantsForConfig;
import com.guohualife.ebiz.common.config.service.ConfigService;
import com.guohualife.ebiz.customer.account.dto.CustomerQryResultDTO;
import com.guohualife.ebiz.customer.account.service.CustomerAccountService;
import com.guohualife.ebiz.gateway.common.constants.ENUM_PARTNER_TYPE;
import com.guohualife.ebiz.gateway.ins.constants.ENUM_BENEFI_RELATION_INS_TYPE;
import com.guohualife.ebiz.gateway.ins.constants.ENUM_CARDTYPE_INS_TYPE;
import com.guohualife.ebiz.gateway.ins.constants.ENUM_INSUR_RELATION_INS_TYPE;
import com.guohualife.ebiz.gateway.ins.constants.ENUM_PAYSTATUS_INS_TYPE;
import com.guohualife.ebiz.gateway.ins.constants.ENUM_SEX_INS_TYPE;
import com.guohualife.ebiz.gateway.ins.dto.base.request.ReqBenefitDTO;
import com.guohualife.ebiz.gateway.ins.dto.base.request.ReqBenefitInfoDTO;
import com.guohualife.ebiz.gateway.ins.dto.base.request.ReqHolderDTO;
import com.guohualife.ebiz.gateway.ins.dto.base.request.ReqInsuredDTO;
import com.guohualife.ebiz.gateway.ins.dto.base.request.ReqInsuredInfoDTO;
import com.guohualife.ebiz.gateway.ins.dto.base.request.ReqOrderDTO;
import com.guohualife.ebiz.gateway.ins.dto.request.PayAndSignReqDTO;
import com.guohualife.ebiz.gateway.ins.dto.response.PayAndSignResDTO;
import com.guohualife.ebiz.gateway.ins.service.InsTradeService;
import com.guohualife.ebiz.product.dto.EbizProductDTO;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.ebiz.trace.dto.EbizOperHisDTO;
import com.guohualife.ebiz.trace.service.TraceService;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizPolicy;
import com.guohualife.edb.bpm.model.EbizSurrender;
import com.guohualife.platform.common.api.exception.ServiceException;
import com.guohualife.platform.common.api.util.DateUtil;
import com.guohualife.platform.common.api.util.ReflectionUtil;
import com.guohualife.platform.common.api.util.StringUtil;
import com.guohualife.platform.common.api.util.XmlUtil;

/**
 * 保险Service实现
 * 
 * @author wangxulu
 *
 */
@Service
public class InsuranceServiceImpl implements InsuranceService {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private InsuranceBo insuranceBo;
	
	@Resource
	private OrderBo orderBo;
	
	@Resource
	private AssetBo assetBo;
	
	@Resource
	private TraceService traceService;
	
	@Resource
	private ConfigService configService;
	
	@Resource
	private ProductService productService;
	
	@Resource
	private InsTradeService insTradeService;
	
	@Resource
	private OrderSurrenderBo orderSurrenderBo;
	
	@Resource
	private BpmBaseBo bpmBaseBo;
	
	@Resource
	private CustomerAccountService customerAccountService;
	
	@Resource
	private AssetModifyService assetModifyService;
	
	/**
	 * 核保
	 * 
	 * @param underwriteReqDTO
	 * @return
	 * @throws Exception
	 */
	public UnderwriteResDTO underwrite(UnderwriteReqDTO underwriteReqDTO)
			throws ServiceException {
		/*
		 * 1.保存Order(ThirdOrder)
		 * 2.保存保单(投保人)、被保人、受益人
		 * 3.调用gateway核保接口 
		 * 4.更新Policy,Order
		 * 5.记录轨迹
		 * 6.组装返回DTO
		 */
		logger.info("核保接口underwrite入参UnderwriteReqDTO：\n"
				+ XmlUtil.toXml(underwriteReqDTO, new Class[] {}));
		long start = System.currentTimeMillis();
		
		UnderwriteResDTO underwriteResDTO = new UnderwriteResDTO();
		
		Long customerId = underwriteReqDTO.getCustomerId();
		CustomerQryResultDTO customerQryResultDTO = 
				orderBo.getCustomerInfo(customerId);
		if(customerQryResultDTO == null){
			logger.info("查询不到客户" + customerId + "信息");
			underwriteResDTO.setUnderwriteFlag(Constants.FAILED);
			underwriteResDTO.setFailReason("查询不到客户" + customerId + "信息");
			return underwriteResDTO;
		}
		String customerName = customerQryResultDTO.getCustomerDTOList().get(0).getName();
		
		OrderDTO orderDTO = new OrderDTO();
		initNewOrder(underwriteReqDTO, orderDTO);
		orderDTO.setCustomerName(customerName);
		
		String orderNo = orderBo.saveOrder(orderDTO);
		orderDTO.setOrderNo(orderNo);
		
		String underWriteResult = "订单" + orderNo + "核保";
		
		Long policyId = 
				insuranceBo.savePolicy(orderDTO, underwriteReqDTO.getPolicyDTO());
		if(policyId == null){
			underWriteResult = underWriteResult + "失败：保存保单信息失败";
			underwriteResDTO.setUnderwriteFlag(Constants.FAILED);
			underwriteResDTO.setFailReason(underWriteResult);
			return underwriteResDTO;
		}
		
		com.guohualife.ebiz.gateway.ins.dto.request.UnderwriteReqDTO gatewayUwDTO = 
				new com.guohualife.ebiz.gateway.ins.dto.request.UnderwriteReqDTO();
		
		initOrder(gatewayUwDTO, underwriteReqDTO, orderDTO);
		
		long start1 = System.currentTimeMillis();
		logger.info("订单" + orderNo + "调用gateway核保接口开始");
		com.guohualife.ebiz.gateway.ins.dto.response.UnderwriteResDTO uwResDTO = 
				insTradeService.doUnderwrite(gatewayUwDTO);
		logger.info("订单" + orderNo + "调用gateway核保接口结束，耗时："
				+ ((System.currentTimeMillis() - start1) / 1000f) + "秒");
		
		String reponseCode = uwResDTO.getResponseCode();
		
		String failReason = "";
		String orderStatus = "";
		String policyStatus = "";
		
		EbizPolicy ebizPolicy = new EbizPolicy();
		ebizPolicy.setPolicyId(policyId);
		
		if(Constants.FAILED.equals(reponseCode)){
			failReason = uwResDTO.getErrorMessage();
			underWriteResult = underWriteResult + "失败，失败原因：" + failReason;
			logger.info(underWriteResult);
			
			underwriteResDTO.setUnderwriteFlag(Constants.FAILED);
			underwriteResDTO.setFailReason(failReason);
			
			orderStatus = ConstantsForOrder.ENUM_ORDER_STATUS.CLOSE.getValue();
			policyStatus = ConstantsForInsurance.ENUM_POLICY_STATUS.UNDERWRITEFAILED.getValue();
			
		}else{
			String underwriteFlag = 
					uwResDTO.getUnderwriteFlag().equals(Constants.SUCCESS) ? 
							Constants.SUCCESS : Constants.FAILED;
			failReason = uwResDTO.getFailReason();
			
			underwriteResDTO.setUnderwriteFlag(underwriteFlag);
			
			if(Constants.FAILED.equals(underwriteFlag)){
				underWriteResult = underWriteResult + "失败，失败原因：" + failReason;
				logger.info(underWriteResult);
				underwriteResDTO.setFailReason(failReason);
				
				orderStatus = ConstantsForOrder.ENUM_ORDER_STATUS.CLOSE.getValue();
				policyStatus = ConstantsForInsurance.ENUM_POLICY_STATUS.UNDERWRITEFAILED.getValue();
			}
			
			if(Constants.SUCCESS.equals(underwriteFlag)){
				String prtNo = uwResDTO.getProposalNo();
				underWriteResult = underWriteResult + "成功，投保单号:" + prtNo;
				
				ebizPolicy.setPrtNo(prtNo);
				
				orderStatus = ConstantsForOrder.ENUM_ORDER_STATUS.WAITPAY.getValue();
				policyStatus = ConstantsForInsurance.ENUM_POLICY_STATUS.UNDERWRITESUCCESS.getValue();
				
				underwriteResDTO.setProposalNo(uwResDTO.getProposalNo());
				underwriteResDTO.setOrderNo(orderNo);
				logger.info(underWriteResult + "耗时：" + ((System.currentTimeMillis() - start) / 1000f) + "秒");
			}
		}
		
		ebizPolicy.setProcessStatus(policyStatus);
		insuranceBo.saveOrUpdatePolicy(ebizPolicy);
		
		orderBo.updateOrderStatus(orderNo, orderStatus);
		
		EbizOperHisDTO ebizOperHisDTO = new EbizOperHisDTO();
		ebizOperHisDTO.setCreatedDate(new Date());
		ebizOperHisDTO.setCreatedUser("交易中心");
		ebizOperHisDTO.setModifiedUser("交易中心");
		ebizOperHisDTO.setOperUser(customerName);
		ebizOperHisDTO.setOperDesc(underWriteResult);
		ebizOperHisDTO.setOperType(ConstantsForConfig.ENUM_OPER_TYPE.ORDER_PURCHASE.getValue());
		ebizOperHisDTO.setOrderNo(orderNo);
		traceService.saveOperHis(ebizOperHisDTO);
		logger.info("核保接口underwrite出参UnderwriteResDTO：\n" + XmlUtil.toXml(underwriteResDTO, new Class[]{}));
		return underwriteResDTO;
	}

	/**
	 * 支付承保
	 * 
	 * @param acceptInsuranceReqDTO
	 * @return
	 * @throws Exception
	 */
	public AcceptInsuranceResDTO acceptInsurance(
			AcceptInsuranceReqDTO acceptInsuranceReqDTO)
			throws ServiceException {
		/*
		 * 1.保存OrderAccunt(更新第三方订单表账户信息)
		 * 2.更新订单状态支付中
		 * 3.调用gateway承保接口 
		 * 4.更新Order，保单，资产信息
		 * 5.记录轨迹
		 * 6.组装返回DTO
		 */
		logger.info("承保接口acceptInsurance入参AcceptInsuranceReqDTO：\n"
				+ XmlUtil.toXml(acceptInsuranceReqDTO, new Class[] {}));
		long start = System.currentTimeMillis();
		
		String orderNo = acceptInsuranceReqDTO.getOrderNo();
		
		AcceptInsuranceResDTO acceptInsuranceResDTO = new AcceptInsuranceResDTO();
		acceptInsuranceResDTO.setOrderNo(acceptInsuranceReqDTO.getOrderNo());
		Long customerId = acceptInsuranceReqDTO.getCustomerId();
		CustomerQryResultDTO customerQryResultDTO = 
				orderBo.getCustomerInfo(customerId);
		if(customerQryResultDTO == null){
			logger.info("查询不到客户" + customerId + "信息");
			acceptInsuranceResDTO.setStatus("2");
			acceptInsuranceResDTO.setFailReason("查询不到客户" + customerId + "信息");
			return acceptInsuranceResDTO;
		}
		String customerName = customerQryResultDTO.getCustomerDTOList().get(0).getName();
		
		EbizOrder ebizOrder = orderBo.getOrder(orderNo);
		if(ConstantsForOrder.ENUM_ORDER_STATUS.DEAL.getValue().equals(ebizOrder.getOrderStatus())){
			acceptInsuranceResDTO.setResultCode(Constants.SUCCESS);
			acceptInsuranceResDTO.setStatus("0");
			acceptInsuranceResDTO.setResultMessage("订单已承保");
			return acceptInsuranceResDTO;
		}
		
		if(ConstantsForOrder.ENUM_ORDER_STATUS.PAYING.getValue().equals(ebizOrder.getOrderStatus())){
			acceptInsuranceResDTO.setResultCode(Constants.SUCCESS);
			acceptInsuranceResDTO.setStatus("1");
			acceptInsuranceResDTO.setResultMessage("订单支付承保中");
			return acceptInsuranceResDTO;
		}
		
		OrderAccountDTO orderAccountDTO = new OrderAccountDTO();
		orderAccountDTO.setOrderNo(orderNo);
		orderAccountDTO.setBankCode(acceptInsuranceReqDTO.getBankCode());
		orderAccountDTO.setCardBookCode(acceptInsuranceReqDTO.getCardBookCode());
		orderAccountDTO.setCardBookType(acceptInsuranceReqDTO.getCardBookType());
		orderAccountDTO.setOrderNo(orderNo);
		orderBo.applyOrderPay(orderAccountDTO);
		
		EbizPolicy ebizPolicy = insuranceBo.getPolicyByOrderNo(acceptInsuranceReqDTO.getOrderNo());
		
		PayAndSignReqDTO payAndSignReqDTO = new PayAndSignReqDTO();
		initPayAndSign(payAndSignReqDTO, acceptInsuranceReqDTO);
		payAndSignReqDTO.setPayMoney(ebizPolicy.getPrem());
		payAndSignReqDTO.setIdNo(customerQryResultDTO.getCustomerDTOList().get(0).getIdno());
		payAndSignReqDTO.setHolderName(customerQryResultDTO.getCustomerDTOList().get(0).getRealName());
		
		long start1 = System.currentTimeMillis();
		logger.info("订单" + orderNo + "调用gateway支付承保开始");
		PayAndSignResDTO payAndSignResDTO = new PayAndSignResDTO();
		payAndSignResDTO = insTradeService.doPayAndSign(payAndSignReqDTO);
		logger.info("订单" + orderNo + "调用gateway支付承保结束，耗时："
				+ ((System.currentTimeMillis() - start1) / 1000f) + "秒");
		
		String reponseCode = payAndSignResDTO.getResponseCode();
		ENUM_PAYSTATUS_INS_TYPE statusEnum = payAndSignResDTO.getPayStatus();
		
		String failReason = "";
		
		String accInsResult = "订单" + orderNo + "支付承保";
		
		if(Constants.FAILED.equals(reponseCode)){
			failReason = payAndSignResDTO.getErrorMessage();;
			accInsResult = accInsResult + "失败，失败原因：" + failReason;
			logger.info(accInsResult);
			acceptInsuranceResDTO.setStatus(ENUM_PAYSTATUS_INS_TYPE.FAIL.getValue());
			acceptInsuranceResDTO.setFailReason(failReason);
		}else{
			acceptInsuranceResDTO.setStatus(statusEnum.getValue());
			
			if(ENUM_PAYSTATUS_INS_TYPE.SUCCESS.equals(statusEnum)){
				String policyNo = payAndSignResDTO.getPolicyNo();
				accInsResult = accInsResult + "成功，保单号：" + policyNo;
				
				PolicySuccessDTO policySuccessDTO = new PolicySuccessDTO();
				policySuccessDTO.setOrderNo(orderNo);
				policySuccessDTO.setPolicyNo(policyNo);
				policySuccessDTO.setSuccessDate(new Date());
				policySuccessDTO.setProductCode(ebizOrder.getProductCode());
				policySuccessDTO.setOrderType(ebizOrder.getOrderType());
				insuranceBo.dealPolicySuccess(policySuccessDTO);
				
				OrderSuccessDTO orderSuccessDTO = new OrderSuccessDTO();
				orderSuccessDTO.setOrderNo(orderNo);
				orderSuccessDTO.setBusinessNo(policyNo);
				orderSuccessDTO.setSuccessDate(new Date());
				orderSuccessDTO.setValueDate(policySuccessDTO.getCvalidate());
				orderSuccessDTO.setProductCode(ebizOrder.getProductCode());
				orderSuccessDTO.setOrderType(ebizOrder.getOrderType());
				orderBo.dealOrderSuccess(orderSuccessDTO);
				
				acceptInsuranceResDTO.setPolicyNo(policyNo);
				acceptInsuranceResDTO.setCvalidate(policySuccessDTO.getCvalidate());
				acceptInsuranceResDTO.setIssuedTime(new Date());
				
				logger.info(accInsResult + "，耗时："
						+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
			}
			
			if(ENUM_PAYSTATUS_INS_TYPE.FAIL.equals(statusEnum)){
				failReason = payAndSignResDTO.getPayMessage();
				accInsResult = accInsResult + "失败，失败原因：" + failReason;
				logger.info(accInsResult);
				orderBo.dealOrderFail(orderNo);
				acceptInsuranceResDTO.setFailReason(failReason);
			}
			
			if(ENUM_PAYSTATUS_INS_TYPE.PROCESSING.equals(statusEnum)){
				accInsResult = accInsResult + "处理中";
				logger.info(accInsResult);
			}
		}
		
		EbizOperHisDTO ebizOperHisDTO = new EbizOperHisDTO();
		ebizOperHisDTO.setCreatedUser("交易中心");
		ebizOperHisDTO.setModifiedUser("交易中心");
		ebizOperHisDTO.setOperUser(customerName);
		ebizOperHisDTO.setOperDesc(accInsResult);
		ebizOperHisDTO.setOperType(ConstantsForConfig.ENUM_OPER_TYPE.ORDER_PURCHASE.getValue());
		ebizOperHisDTO.setOrderNo(orderNo);
		traceService.saveOperHis(ebizOperHisDTO);
		
		logger.info("承保接口acceptInsurance出参AcceptInsuranceResDTO：\n"
				+ XmlUtil.toXml(acceptInsuranceResDTO, new Class[] {}));
		return acceptInsuranceResDTO;
	}
	
	/**
	 * 查询赎回信息
	 * 
	 * @param policySurrenderInfoReqDTO
	 * @return
	 */
	public PolicySurrenderInfoResDTO getSurrenderInfo(
			PolicySurrenderInfoReqDTO policySurrenderInfoReqDTO) {
		logger.info("查询赎回信息接口getSurrenderInfo入参PolicySurrenderInfoReqDTO：\n"
				+ XmlUtil.toXml(policySurrenderInfoReqDTO, new Class[] {}));
		PolicySurrenderInfoResDTO policySurrenderInfoResDTO = new PolicySurrenderInfoResDTO();
		insuranceBo.getSurrenderInfo(policySurrenderInfoReqDTO, policySurrenderInfoResDTO);
		logger.info("查询赎回信息接口getSurrenderInfo出参PolicySurrenderInfoResDTO：\n"
				+ XmlUtil.toXml(policySurrenderInfoResDTO, new Class[] {}));
		return policySurrenderInfoResDTO;
	}
	
	/**
	 * 赎回核算
	 * 
	 * @param policySurrenderChargeReqDTO
	 * @return
	 */
	public PolicySurrenderChargeResDTO surrenderCharge(
			PolicySurrenderChargeReqDTO policySurrenderChargeReqDTO) {
		logger.info("赎回核算接口surrenderCharge入参PolicySurrenderChargeReqDTO：\n"
				+ XmlUtil.toXml(policySurrenderChargeReqDTO, new Class[] {}));
		PolicySurrenderChargeResDTO policySurrenderChargeResDTO = new PolicySurrenderChargeResDTO();
		insuranceBo.surrenderCharge(policySurrenderChargeReqDTO , policySurrenderChargeResDTO);
		logger.info("赎回核算接口surrenderCharge出参PolicySurrenderChargeResDTO：\n"
				+ XmlUtil.toXml(policySurrenderChargeResDTO, new Class[] {}));
		return policySurrenderChargeResDTO;
	}
	
	/**
	 * 赎回申请
	 * 
	 * @param policySurrenderApplyReqDTO
	 * @return
	 */
	public PolicySurrenderApplyResDTO applySurrender(
			PolicySurrenderApplyReqDTO policySurrenderApplyReqDTO) {
		logger.info("赎回申请接口applySurrender入参PolicySurrenderApplyReqDTO：\n"
				+ XmlUtil.toXml(policySurrenderApplyReqDTO, new Class[] {}));

		PolicySurrenderApplyResDTO policySurrenderApplyResDTO = new PolicySurrenderApplyResDTO();		
		SurrenderApplyDTO surrenderApplyDTO = new SurrenderApplyDTO();
		ReflectionUtil.copyProperties(policySurrenderApplyReqDTO, surrenderApplyDTO);
		surrenderApplyDTO.setSurrenderPrincipal(policySurrenderApplyReqDTO.getAvailableMoney());
		orderSurrenderBo.applySurrender(surrenderApplyDTO);
		if(surrenderApplyDTO.getSurrenderId() == null){
			policySurrenderApplyResDTO.failure(surrenderApplyDTO.getErrorMessage());
		}
		logger.info("赎回申请接口applySurrender出参PolicySurrenderApplyResDTO：\n"
				+ XmlUtil.toXml(policySurrenderApplyResDTO, new Class[] {}));
		return policySurrenderApplyResDTO;
	}

	/**
	 * 赎回回调入口
	 * 
	 * @param policySurrenderCallbackReqDTO
	 * @return
	 */
	public PolicySurrenderCallbackResDTO surrenderCallback(
			PolicySurrenderCallbackReqDTO policySurrenderCallbackReqDTO) {
		logger.info("赎回回调入口接口surrenderCallback入参PolicySurrenderCallbackReqDTO：\n"
				+ XmlUtil.toXml(policySurrenderCallbackReqDTO, new Class[] {}));
		
		String orderNo = policySurrenderCallbackReqDTO.getOrderNo();
		logger.info("订单" + orderNo + "接收赎回回调");
		
		String isSuccess = policySurrenderCallbackReqDTO.getIsSuccess();
		
		Long surrenderId = policySurrenderCallbackReqDTO.getSurrenderId();
		EbizSurrender ebizSurrender = orderSurrenderBo.getSurrenderById(surrenderId);
		ebizSurrender.setSurrenderId(surrenderId);
		
		EbizOperHisDTO ebizOperHisDTO = new EbizOperHisDTO();
		ebizOperHisDTO.setCreatedUser("交易中心");
		ebizOperHisDTO.setModifiedUser("交易中心");
		ebizOperHisDTO.setOperUser("交易中心");
		ebizOperHisDTO.setOperType(ConstantsForConfig.ENUM_OPER_TYPE.ORDER_PURCHASE.getValue());
		ebizOperHisDTO.setOrderNo(orderNo);
		
		if(Constants.SUCCESS.equals(isSuccess)){
			String acceptNo = policySurrenderCallbackReqDTO.getAcceptNo();
			logger.info("订单" + orderNo + "赎回成功，受理号" + acceptNo);
			
			ebizSurrender.setAcceptNo(acceptNo);
			ebizSurrender.setStatus(ConstantsForOrder.ENUM_SURRENDER_STATUS.ACCEPTSUCCESS.getValue());
			orderSurrenderBo.saveOrUpdateSurrender(ebizSurrender);
			
			if(ConstantsForOrder.ENUM_SURRENDER_TYPE.REDEEM.getValue().equals(
					ebizSurrender.getSurrenderType())){
				orderBo.updateOrderStatus(orderNo,
						ConstantsForOrder.ENUM_ORDER_STATUS.DEAL.getValue());
			}
			
			AssetModifyDTO assetModifyDTO = new AssetModifyDTO();
			assetModifyDTO.setModifyValue(
					ebizSurrender.getApplyAmount().multiply(new BigDecimal("-1")));
			assetModifyDTO.setModifyDate(
					DateUtil.parseDate(
							DateUtil.formatDate(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"));
			assetModifyDTO.setModifyType(
					ConstantsForAsset.ENUM_ASSET_MODIFY_TYPE.SURRENDER.getValue());
			assetModifyDTO.setOperCode(StringUtil.getString(ebizSurrender.getSurrenderId()));
			assetModifyDTO.setOrderNo(orderNo);
			EbizAsset ebizAsset = assetModifyService.dealAssetSurrender(assetModifyDTO);
			
			//如果是犹豫期退保将剩余资产也清零
			if(ConstantsForOrder.ENUM_SURRENDER_TYPE.REFUND.getValue().equals(
					ebizSurrender.getSurrenderType())){
				assetModifyDTO.setModifyValue(
						ebizAsset.getTotalValue().multiply(new BigDecimal("-1")));
				assetModifyDTO.setModifyType(
						ConstantsForAsset.ENUM_ASSET_MODIFY_TYPE.POLICY_HESITATE_SURRRENDER.getValue());
				ebizAsset = assetModifyService.dealAssetSurrender(assetModifyDTO);
			}
			
			if(ConstantsForAsset.ENUM_ASSET_STATUS.SURRENDER_INVEST_ALL.getValue().equals(
					ebizAsset.getStatus())){
				orderBo.updateOrderStatus(
						assetModifyDTO.getOrderNo(), 
						ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMALL.getValue());
			}
			
			if (ConstantsForOrder.ENUM_SURRENDER_TYPE.REFUND.getValue().equals(ebizSurrender.getSurrenderType())
					|| ConstantsForOrder.ENUM_SURRENDER_TYPE.SPECIALREDEEM.getValue().equals(ebizSurrender.getSurrenderType())) {

				orderBo.updateOrderStatus(orderNo,
						ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMALL.getValue());

				EbizPolicy ebizPolicy = insuranceBo.getPolicyByOrderNo(orderNo);
				ebizPolicy.setProcessStatus(ConstantsForInsurance.ENUM_POLICY_STATUS.SURRENDERSUCCESS.getValue());
				insuranceBo.saveOrUpdatePolicy(ebizPolicy);
			}
			
			ebizOperHisDTO.setOperDesc("订单" + orderNo + "赎回成功，受理号" + acceptNo);
			
			if(ConstantsForOrder.ENUM_SURRENDER_TYPE.REFUND.getValue().equals(
					ebizSurrender.getSurrenderType())){
				EbizOrder ebizOrder = orderBo.getOrder(orderNo);
				logger.info("订单" + orderNo + "赎回成功发送消息");
				orderBo.sendMsgForSurrenderSuccess(ebizOrder, ebizSurrender);
			}
			
		}
		
		if(Constants.FAILED.equals(isSuccess)){
			logger.info("订单" + orderNo + "赎回失败：" + policySurrenderCallbackReqDTO.getFailReason());
			
			ebizSurrender.setStatus(ConstantsForOrder.ENUM_SURRENDER_STATUS.ACCEPTFAIL.getValue());
			ebizSurrender.setSurrenderResult(policySurrenderCallbackReqDTO.getFailReason());
			orderSurrenderBo.saveOrUpdateSurrender(ebizSurrender);
			
			orderBo.updateOrderStatus(orderNo, ConstantsForOrder.ENUM_ORDER_STATUS.DEAL.getValue());
			
			ebizOperHisDTO.setOperDesc("订单" + orderNo + "赎回失败：" + policySurrenderCallbackReqDTO.getFailReason());
			
		}
		
		traceService.saveOperHis(ebizOperHisDTO);
		
		EbizOrder ebizOrder = orderBo.getOrder(ebizSurrender.getOrderNo());
		orderSurrenderBo.sendSurrenderSuccessMsg(ebizSurrender, ebizOrder);

		return new PolicySurrenderCallbackResDTO();
	}
	
	private void initOrder(
			com.guohualife.ebiz.gateway.ins.dto.request.UnderwriteReqDTO uwReqDTO,
			UnderwriteReqDTO underwriteReqDTO, OrderDTO orderDTO) {

		ReqOrderDTO reqOrderDTO = new ReqOrderDTO();
		reqOrderDTO.setPremium(underwriteReqDTO.getOrderAmount());
		reqOrderDTO.setTotalPremium(underwriteReqDTO.getOrderAmount());
		reqOrderDTO.setOrderId(orderDTO.getOrderNo());
		reqOrderDTO.setApplyNum(underwriteReqDTO.getPolicyDTO().getMult().intValue());
		reqOrderDTO.setSkuCode(orderDTO.getSkuCode());
		reqOrderDTO.setThirdType(bpmBaseBo.getThirdType(orderDTO.getOrderNo()));
		uwReqDTO.setOrderDTO(reqOrderDTO);
		
		initHolder(uwReqDTO, underwriteReqDTO.getPolicyDTO().getAppntDTO());
		initInsured(uwReqDTO, underwriteReqDTO);
	}
	
	private void initHolder(
			com.guohualife.ebiz.gateway.ins.dto.request.UnderwriteReqDTO uwReqDTO,
			AppntDTO appntDTO) {
		ReqHolderDTO reqHolderDTO = new ReqHolderDTO();
		reqHolderDTO.setHolderAddress(appntDTO.getAddress());
		reqHolderDTO.setHolderBirthday(appntDTO.getBirthday());
		reqHolderDTO.setHolderCardNo(appntDTO.getIdNo());
		reqHolderDTO.setHolderCardType(
				ENUM_CARDTYPE_INS_TYPE.getEnumByValue(appntDTO.getIdType())); 
		reqHolderDTO.setHolderEmail(appntDTO.getEmail());
		reqHolderDTO.setHolderMobile(appntDTO.getMobile());
		reqHolderDTO.setHolderName(appntDTO.getName());
		reqHolderDTO.setHolderPhone(appntDTO.getPhone());
		reqHolderDTO.setHolderResidentCity(appntDTO.getCity());
		reqHolderDTO.setHolderResidentProvince(appntDTO.getProvince());
		reqHolderDTO.setHolderZip(appntDTO.getZip());
		reqHolderDTO.setHolderSex(ENUM_SEX_INS_TYPE.getEnumByValue(appntDTO.getSex()));
		uwReqDTO.setHolderDTO(reqHolderDTO);
	}
	
	private void initInsured(
			com.guohualife.ebiz.gateway.ins.dto.request.UnderwriteReqDTO uwReqDTO,
			UnderwriteReqDTO underwriteReqDTO) {

		ReqInsuredInfoDTO reqInsuredInfoDTO = new ReqInsuredInfoDTO();
		List<ReqInsuredDTO> reqInsuredList = new ArrayList<ReqInsuredDTO>();
		
		if(ConstantsForInsurance.ENUM_INSURED_TYPE.SELF.getValue().equals(
				underwriteReqDTO.getPolicyDTO().getInsuredType())){
			AppntDTO appntDTO = underwriteReqDTO.getPolicyDTO().getAppntDTO();
			ReqInsuredDTO reqInsuredDTO = new ReqInsuredDTO();
			reqInsuredDTO.setInsuredAddress(appntDTO.getAddress());
			reqInsuredDTO.setInsuredBirthday(appntDTO.getBirthday());
			reqInsuredDTO.setInsuredCardNo(appntDTO.getIdNo());
			reqInsuredDTO.setInsuredCardType(ENUM_CARDTYPE_INS_TYPE.getEnumByValue(appntDTO.getIdType()));
			reqInsuredDTO.setInsuredEmail(appntDTO.getEmail());
			reqInsuredDTO.setInsuredMobile(appntDTO.getMobile());
			reqInsuredDTO.setInsuredName(appntDTO.getName());
			reqInsuredDTO.setInsuredPhone(appntDTO.getPhone());
			reqInsuredDTO.setInsuredResidentCity(appntDTO.getCity());
			reqInsuredDTO.setInsuredResidentProvince(appntDTO.getProvince());
			reqInsuredDTO.setInsuredSex(ENUM_SEX_INS_TYPE.getEnumByValue(appntDTO.getSex()));
			reqInsuredDTO.setInsuredZip(appntDTO.getZip());
			reqInsuredDTO.setInsuredRelation(ENUM_INSUR_RELATION_INS_TYPE.ONESELF);
			
			ReqBenefitInfoDTO reqBenefitInfoDTO = new ReqBenefitInfoDTO();
			initBnf(reqBenefitInfoDTO, 
					underwriteReqDTO.getPolicyDTO().getBnfDTOList(), 
					underwriteReqDTO.getPolicyDTO().getBnfType());
			
			reqInsuredDTO.setBenefitInfoDTO(reqBenefitInfoDTO);
			reqInsuredList.add(reqInsuredDTO);
			
			reqInsuredInfoDTO.setInsuredList(reqInsuredList);
			reqInsuredInfoDTO.setIsHolder(underwriteReqDTO.getPolicyDTO().getInsuredType());
			uwReqDTO.setInsuredInfoDTO(reqInsuredInfoDTO);
			return;
		}
		
		for(InsuredDTO insuredDTO : underwriteReqDTO.getPolicyDTO().getInsuredDTOList()){
			ReqInsuredDTO reqInsuredDTO = new ReqInsuredDTO();
			reqInsuredDTO.setInsuredEmail(insuredDTO.getEmail());
			reqInsuredDTO.setInsuredCardNo(insuredDTO.getIdNo());
			reqInsuredDTO.setInsuredCardType(
					ENUM_CARDTYPE_INS_TYPE.getEnumByValue(insuredDTO.getIdType()));
			reqInsuredDTO.setInsuredMobile(insuredDTO.getMobile());
			reqInsuredDTO.setInsuredName(insuredDTO.getName());
			reqInsuredDTO.setInsuredRelation(
					ENUM_INSUR_RELATION_INS_TYPE.getEnumByValue(insuredDTO.getRelationToAppnt()));
			reqInsuredDTO.setInsuredSex(ENUM_SEX_INS_TYPE.getEnumByValue(insuredDTO.getSex()));
			reqInsuredDTO.setInsuredBirthday(insuredDTO.getBirthday());
			reqInsuredDTO.setInsuredPhone(insuredDTO.getTelephone());
			reqInsuredDTO.setInsuredZip(insuredDTO.getZip());
			reqInsuredDTO.setInsuredAddress(insuredDTO.getAddress());
			
			ReqBenefitInfoDTO reqBenefitInfoDTO = new ReqBenefitInfoDTO();
			initBnf(reqBenefitInfoDTO, 
					underwriteReqDTO.getPolicyDTO().getBnfDTOList(), 
					underwriteReqDTO.getPolicyDTO().getBnfType());
			
			reqInsuredDTO.setBenefitInfoDTO(reqBenefitInfoDTO);
			reqInsuredList.add(reqInsuredDTO);
		}
		reqInsuredInfoDTO.setInsuredList(reqInsuredList);
		reqInsuredInfoDTO.setIsHolder(underwriteReqDTO.getPolicyDTO().getInsuredType());
		uwReqDTO.setInsuredInfoDTO(reqInsuredInfoDTO);
		
	}
	
	private void initBnf(ReqBenefitInfoDTO reqBenefitInfoDTO,
			List<BnfDTO> bnfDTOsList, String bnfType) {
		List<ReqBenefitDTO> reqBenefitList = new ArrayList<ReqBenefitDTO>();
		if(bnfDTOsList != null && bnfDTOsList.size() > 0){
			for (int i = 0; i < bnfDTOsList.size(); i++) {
				BnfDTO bnfDTO = bnfDTOsList.get(i);
				ReqBenefitDTO reqBenefitDTO = new ReqBenefitDTO();
				reqBenefitDTO.setBenefitScale(bnfDTO.getBnfLot());
				reqBenefitDTO.setBenefitOrder(bnfDTO.getBnfOrder().intValue());
				reqBenefitDTO.setBenefitCardNo(bnfDTO.getIdNo());
				reqBenefitDTO.setBenefitCardType(
						ENUM_CARDTYPE_INS_TYPE.getEnumByValue(bnfDTO.getIdType())); 
				reqBenefitDTO.setBenefitName(bnfDTO.getName()); 
				reqBenefitDTO.setBenefitRelation(
						ENUM_BENEFI_RELATION_INS_TYPE.getEnumByValue(bnfDTO.getRelationToInsured()));
			}
			reqBenefitInfoDTO.setBenefitList(reqBenefitList);
		}
		reqBenefitInfoDTO.setIsLegal(bnfType);
	}
	
	private void initPayAndSign(PayAndSignReqDTO payAndSignReqDTO,
			AcceptInsuranceReqDTO acceptInsuranceReqDTO) {
		payAndSignReqDTO.setBankCode(acceptInsuranceReqDTO.getBankCode());
		payAndSignReqDTO.setCardCode(acceptInsuranceReqDTO.getCardBookCode());
		payAndSignReqDTO.setCardType(acceptInsuranceReqDTO.getCardBookType());
		payAndSignReqDTO.setOrderId(acceptInsuranceReqDTO.getOrderNo());
		payAndSignReqDTO.setProposalNo(acceptInsuranceReqDTO.getProposalNo());
		payAndSignReqDTO.setUserId(StringUtil.getString(acceptInsuranceReqDTO.getCustomerId()));
		payAndSignReqDTO.setThirdType(ENUM_PARTNER_TYPE.GUOHUA);
	}
	
	private void initNewOrder(UnderwriteReqDTO underwriteReqDTO, OrderDTO orderDTO) {
		orderDTO.setCustomerId(underwriteReqDTO.getCustomerId());
		orderDTO.setOrderAmount(underwriteReqDTO.getOrderAmount());
		orderDTO.setOrderType(underwriteReqDTO.getOrderType());
		orderDTO.setProductCode(underwriteReqDTO.getProductCode());
		orderDTO.setActivityDTO(underwriteReqDTO.getActivityDTO());
		EbizProductDTO ebizProductDTO = 
				productService.getProductByProductCode(orderDTO.getProductCode());
		orderDTO.setSkuCode(ebizProductDTO.getPlanCode());
		orderDTO.setEbizProductDTO(ebizProductDTO);
		orderDTO.setThirdOrderDTO(underwriteReqDTO.getThirdOrderDTO());
		orderDTO.setOrderStatus(
				ConstantsForOrder.ENUM_ORDER_STATUS.WAITCONFIRM.getValue());
	}

}
