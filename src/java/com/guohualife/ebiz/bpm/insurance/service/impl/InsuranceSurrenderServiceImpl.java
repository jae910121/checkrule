package com.guohualife.ebiz.bpm.insurance.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.base.bo.BpmBaseBo;
import com.guohualife.ebiz.bpm.base.constants.Constants;
import com.guohualife.ebiz.bpm.insurance.bo.InsuranceBo;
import com.guohualife.ebiz.bpm.insurance.service.InsuranceSurrenderService;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.bo.OrderSurrenderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.common.config.constant.ConstantsForConfig;
import com.guohualife.ebiz.gateway.ins.dto.request.RefundReqDTO;
import com.guohualife.ebiz.gateway.ins.dto.response.RefundResDTO;
import com.guohualife.ebiz.gateway.ins.service.InsTradeService;
import com.guohualife.ebiz.trace.dto.EbizOperHisDTO;
import com.guohualife.ebiz.trace.service.TraceService;
import com.guohualife.edb.bpm.model.EbizPolicy;
import com.guohualife.edb.bpm.model.EbizSurrender;
import com.guohualife.platform.common.api.util.StringUtil;

@Service
public class InsuranceSurrenderServiceImpl implements InsuranceSurrenderService {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private InsuranceBo insuranceBo;
	
	@Resource
	private BpmBaseBo bpmBaseBo;
	
	@Resource
	private InsTradeService insTradeService;
	
	@Resource
	private TraceService traceService;
	
	@Resource
	private OrderBo orderBo;
	
	@Resource
	private OrderSurrenderBo orderSurrenderBo;
	
	/**
	 * 处理赎回
	 * 
	 * @param ebizSurrender
	 */
	public void dealSurrender(EbizSurrender ebizSurrender) {
		String orderNo = ebizSurrender.getOrderNo();
		String surrenderType = ebizSurrender.getSurrenderType();
		
		RefundReqDTO refundReqDTO = new RefundReqDTO();
		refundReqDTO.setThirdType(bpmBaseBo.getThirdType(orderNo));
		refundReqDTO.setOrderId(orderNo);
		refundReqDTO.setRefundId(StringUtil.getString(ebizSurrender.getSurrenderId()));
		refundReqDTO.setWithdrawMoney(ebizSurrender.getApplyAmount());
		refundReqDTO.setAvailableMoney(ebizSurrender.getSurrenderAmount());
		
		EbizPolicy ebizPolicy = insuranceBo.getPolicyByOrderNo(orderNo);
		refundReqDTO.setPolicyNo(ebizPolicy.getPolicyNo());
		
		if (ConstantsForOrder.ENUM_SURRENDER_TYPE.REDEEM.getValue().equals(surrenderType)) {
			refundReqDTO.setIsCancelPolicy(Constants.FAILED);
			refundReqDTO.setIsWithdrawAll(Constants.FAILED);
		}
		if(ConstantsForOrder.ENUM_SURRENDER_TYPE.REFUND.getValue().equals(surrenderType)
				|| ConstantsForOrder.ENUM_SURRENDER_TYPE.SPECIALREDEEM.getValue().equals(surrenderType)){
			refundReqDTO.setIsCancelPolicy(Constants.SUCCESS);
			refundReqDTO.setIsWithdrawAll(Constants.SUCCESS);
		}
		
		EbizOperHisDTO ebizOperHisDTO = new EbizOperHisDTO();
		ebizOperHisDTO.setCreatedDate(new Date());
		ebizOperHisDTO.setCreatedUser("交易中心");
		ebizOperHisDTO.setModifiedUser("交易中心");
		ebizOperHisDTO.setOperUser("交易中心");
		ebizOperHisDTO.setOperType(ConstantsForConfig.ENUM_OPER_TYPE.ORDER_SURRENDER.getValue());
		ebizOperHisDTO.setOrderNo(orderNo);
		
		long start = System.currentTimeMillis();
		logger.info("订单" + orderNo + "调用gateway赎回提交接口开始");
		RefundResDTO refundResDTO = insTradeService.doRefund(refundReqDTO);
		logger.info("订单" + orderNo + "调用gateway赎回提交接口结束，耗时："
				+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
		
		if(Constants.FAILED.equals(refundResDTO.getResponseCode())){
			logger.info("订单" + orderNo + "赎回提交失败，" + refundResDTO.getErrorMessage());
			
			ebizOperHisDTO.setOperDesc("订单" + orderNo + "赎回提交失败，" + refundResDTO.getErrorMessage());
			traceService.saveOperHis(ebizOperHisDTO);
			return;
		}
		
		logger.info("订单" + orderNo + "赎回提交成功");
		ebizSurrender.setStatus(ConstantsForOrder.ENUM_SURRENDER_STATUS.ACCEPTAUDIT.getValue());
		orderSurrenderBo.saveOrUpdateSurrender(ebizSurrender);
		
		orderBo.updateOrderStatus(orderNo, ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMING.getValue());
		
		ebizOperHisDTO.setOperDesc("订单" + orderNo + "赎回提交成功");
		traceService.saveOperHis(ebizOperHisDTO);
		return;
	}

}
