package com.guohualife.ebiz.bpm.insurance.bo;

import java.util.List;

import com.guohualife.ebiz.bpm.insurance.dto.PolicyDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyInfoDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyListQueryDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicySuccessDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.PolicySurrenderChargeReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.PolicySurrenderInfoReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.PolicySurrenderChargeResDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.PolicySurrenderInfoResDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderDTO;
import com.guohualife.edb.bpm.model.EbizPolicy;
import com.guohualife.platform.common.api.exception.ServiceException;

/**
 * @author wangxulu
 * 
 */
public interface InsuranceBo {

	/**
	 * 保存保单
	 * 
	 * @param orderDTO
	 * @param policyDTO
	 * @return
	 * @throws ServiceException
	 */
	public Long savePolicy(OrderDTO orderDTO, PolicyDTO policyDTO)
			throws ServiceException;

	/**
	 * 根据订单号查询保单详细信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public PolicyInfoDTO getPolicyDetailByOrderNo(String orderNo);
	
	/**
	 * 根据订单号查询保单表
	 * 
	 * @param orderNo
	 * @return
	 */
	public EbizPolicy getPolicyByOrderNo(String orderNo);
	
	/**
	 * 保存/更新保单表
	 * 
	 * @param ebizPolicy
	 * @return
	 */
	public Long saveOrUpdatePolicy(EbizPolicy ebizPolicy);
	
	/**
	 * 出单成功处理
	 * 
	 * @param policySuccessDTO
	 */
	public void dealPolicySuccess(PolicySuccessDTO policySuccessDTO);
	
	/**
	 * 查询保单列表
	 * 
	 * @param policyListQueryDTO
	 * @return
	 */
	public List<EbizPolicy> getPolicyList(PolicyListQueryDTO policyListQueryDTO);
	
	/**
	 * 查询赎回信息
	 * 
	 * @param policySurrenderInfoReqDTO
	 * @param policySurrenderInfoResDTO
	 * @return
	 */
	public void getSurrenderInfo(
			PolicySurrenderInfoReqDTO policySurrenderInfoReqDTO,
			PolicySurrenderInfoResDTO policySurrenderInfoResDTO);

	/**
	 * 赎回核算
	 * 
	 * @param policySurrenderChargeReqDTO
	 * @param policySurrenderChargeResDTO
	 * @return
	 */
	public void surrenderCharge(
			PolicySurrenderChargeReqDTO policySurrenderChargeReqDTO,
			PolicySurrenderChargeResDTO policySurrenderChargeResDTO);
	
}
