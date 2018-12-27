package com.guohualife.ebiz.bpm.insurance.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.base.bo.BpmBaseBo;
import com.guohualife.ebiz.bpm.base.constants.Constants;
import com.guohualife.ebiz.bpm.insurance.bo.InsuranceBo;
import com.guohualife.ebiz.bpm.insurance.constants.ConstantsForInsurance;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyListQueryDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyThawMsgDTO;
import com.guohualife.ebiz.bpm.insurance.service.InsuranceBatchService;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.customer.account.dto.CustomerQryResultDTO;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizPolicy;
import com.guohualife.platform.common.api.exception.ServiceException;

@Service
public class InsuranceBatchServiceImpl implements InsuranceBatchService {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private OrderBo orderBo;
	
	@Resource
	private InsuranceBo insuranceBo;
	
	@Resource
	private BpmBaseBo bpmBaseBo;
	
	@Resource
	private Properties bpmProperties;
	
	/**
	 * 处理犹豫期解冻
	 * 
	 * @param ebizPolicy
	 */
	public void dealPolicyThaw(){
		
		// 成功笔数
		Integer successNum = 0;
		// 异常笔数
		Integer exceptionNum = 0;

		PolicyListQueryDTO policyListQueryDTO = new PolicyListQueryDTO();
		policyListQueryDTO.setIsThaw(Constants.FAILED);
		List<String> processStatusList = new ArrayList<String>();
		processStatusList.add(ConstantsForInsurance.ENUM_POLICY_STATUS.ACCEPTINSURANCESUCCESS.getValue());
		policyListQueryDTO.setProcessStatusList(processStatusList);
		List<EbizPolicy> policyList = insuranceBo.getPolicyList(policyListQueryDTO);
		logger.info("本次共扫描到数据" + policyList.size() + "条");

		if (policyList != null && policyList.size() > 0) {
			for (EbizPolicy ebizPolicy : policyList) {
				try {
					dealPolicyThaw(ebizPolicy);
					successNum++;
				} catch (ServiceException e) {
					logger.warn("保单" + ebizPolicy.getPolicyNo() + "犹豫期解冻处理异常",
							e);
					exceptionNum++;
					continue;
				}
			}
		}

		logger.info("本次批处理成功" + successNum + "条，异常：" + exceptionNum + "条");
		
	}
	
	private void dealPolicyThaw(EbizPolicy ebizPolicy){
		EbizOrder ebizOrder = orderBo.getOrder(ebizPolicy.getOrderNo());
		
		PolicyThawMsgDTO policyThawMsgDTO = new PolicyThawMsgDTO();
		policyThawMsgDTO.setCustomerId(ebizOrder.getCustomerId());
		policyThawMsgDTO.setOrderNo(ebizOrder.getOrderNo());
		policyThawMsgDTO.setOrderType(ebizOrder.getOrderType());
		policyThawMsgDTO.setProductCode(ebizOrder.getProductCode());
		policyThawMsgDTO.setProductType(ebizOrder.getProductType());
		policyThawMsgDTO.setSuccessDate(ebizOrder.getSuccessDate());
		
		policyThawMsgDTO.setActivityCode(ebizOrder.getActivityCode());
		policyThawMsgDTO.setRecommendCode(ebizOrder.getRecommendCode());
		policyThawMsgDTO.setActRemark(ebizOrder.getActivityRemark());
		
		policyThawMsgDTO.setAmt(ebizPolicy.getAmt());
		policyThawMsgDTO.setCvalidate(ebizPolicy.getCvlidate());
		policyThawMsgDTO.setLastHesitateDate(ebizPolicy.getLastHesitateDate());
		policyThawMsgDTO.setMult(ebizPolicy.getMult());
		policyThawMsgDTO.setPolicyNo(ebizPolicy.getPolicyNo());
		policyThawMsgDTO.setPrem(ebizPolicy.getPrem());
		CustomerQryResultDTO customerQryResultDTO = 
				orderBo.getCustomerInfo(ebizOrder.getCustomerId());
		policyThawMsgDTO.setCustomerName(customerQryResultDTO.getCustomerDTOList().get(0).getRealName());
		bpmBaseBo.sendMessage(
				bpmProperties.getProperty("gh.ebiz.bpm.mq.policyThaw.topic"), 
				ebizOrder.getOrderNo(), "",
				policyThawMsgDTO);
		
		ebizPolicy.setIsThaw(Constants.SUCCESS);
		insuranceBo.saveOrUpdatePolicy(ebizPolicy);
	}
	
}
