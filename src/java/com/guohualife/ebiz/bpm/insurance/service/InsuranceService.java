package com.guohualife.ebiz.bpm.insurance.service;

import javax.jws.WebService;

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
import com.guohualife.ebiz.bpm.order.dto.request.PolicySurrenderCallbackReqDTO;
import com.guohualife.ebiz.bpm.order.dto.response.PolicySurrenderCallbackResDTO;
import com.guohualife.platform.common.api.exception.ServiceException;

/**
 * 保险Service接口
 * 
 * @author wangxulu
 * 
 */
@WebService
public interface InsuranceService {

	/**
	 * 核保
	 * 
	 * @param underwriteReqDTO
	 * @return
	 * @throws Exception
	 */
	public UnderwriteResDTO underwrite(UnderwriteReqDTO underwriteReqDTO)
			throws ServiceException;

	/**
	 * 承保
	 * 
	 * @param acceptInsuranceReqDTO
	 * @return
	 * @throws Exception
	 */
	public AcceptInsuranceResDTO acceptInsurance(
			AcceptInsuranceReqDTO acceptInsuranceReqDTO)
			throws ServiceException;

	/**
	 * 查询赎回信息
	 * 
	 * @param policySurrenderInfoReqDTO
	 * @return
	 */
	public PolicySurrenderInfoResDTO getSurrenderInfo(
			PolicySurrenderInfoReqDTO policySurrenderInfoReqDTO);

	/**
	 * 赎回核算
	 * 
	 * @param policySurrenderChargeReqDTO
	 * @return
	 */
	public PolicySurrenderChargeResDTO surrenderCharge(
			PolicySurrenderChargeReqDTO policySurrenderChargeReqDTO);

	/**
	 * 赎回申请
	 * 
	 * @param policySurrenderApplyReqDTO
	 * @return
	 */
	public PolicySurrenderApplyResDTO applySurrender(
			PolicySurrenderApplyReqDTO policySurrenderApplyReqDTO);
	
	/**
	 * 赎回回调入口
	 * 
	 * @param policySurrenderCallbackReqDTO
	 * @return
	 */
	public PolicySurrenderCallbackResDTO surrenderCallback(
			PolicySurrenderCallbackReqDTO policySurrenderCallbackReqDTO);
}
