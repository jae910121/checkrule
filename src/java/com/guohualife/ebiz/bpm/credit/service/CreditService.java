package com.guohualife.ebiz.bpm.credit.service;

import javax.jws.WebService;

import com.guohualife.ebiz.bpm.credit.dto.request.CreditApplyPurchaseReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderApplyReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderChargeReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderInfoReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditApplyPurchaseResDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderApplyResDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderChargeResDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderInfoResDTO;
import com.guohualife.platform.common.api.exception.ServiceException;

/**
 * 债权Service接口
 * 
 * @author wangxulu
 *
 */
@WebService
public interface CreditService {

	/**
	 * 申购
	 * 
	 * @param creditApplyPurchaseReqDTO
	 * @return
	 * @throws Exception
	 */
	public CreditApplyPurchaseResDTO applyPurchase(
			CreditApplyPurchaseReqDTO creditApplyPurchaseReqDTO)
			throws ServiceException;
	
	/**
	 * 查询赎回信息
	 * 
	 * @param creditSurrenderInfoReqDTO
	 * @return
	 */
	public CreditSurrenderInfoResDTO getSurrenderInfo(
			CreditSurrenderInfoReqDTO creditSurrenderInfoReqDTO)
			throws ServiceException;

	/**
	 * 赎回核算
	 * 
	 * @param creditSurrenderChargeReqDTO
	 * @return
	 * @throws ServiceException
	 */
	public CreditSurrenderChargeResDTO surrenderCharge(
			CreditSurrenderChargeReqDTO creditSurrenderChargeReqDTO)
			throws ServiceException;

	/**
	 * 赎回提交
	 * 
	 * @param creditSurrenderApplyReqDTO
	 * @return
	 * @throws ServiceException
	 */
	public CreditSurrenderApplyResDTO applySurrender(
			CreditSurrenderApplyReqDTO creditSurrenderApplyReqDTO)
			throws ServiceException;
	
}
