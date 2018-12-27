package com.guohualife.ebiz.bpm.credit.bo;

import java.util.List;

import com.guohualife.ebiz.bpm.credit.dto.CreditDTO;
import com.guohualife.ebiz.bpm.credit.dto.CreditListQueryDTO;
import com.guohualife.ebiz.bpm.credit.dto.CreditSuccessDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderChargeReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderInfoReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderChargeResDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderInfoResDTO;
import com.guohualife.edb.bpm.model.EbizCredit;

/**
 * 债权Bo接口
 * 
 * @author wangxulu
 *
 */
public interface CreditBo {
	
	/**
	 * 根据订单查询债权表信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public EbizCredit getCreditByOrderNo(String orderNo);

	/**
	 * 查询债权列表
	 * 
	 * @param creditListQueryDTO
	 * @return
	 */
	public List<EbizCredit> getCreditList(CreditListQueryDTO creditListQueryDTO);

	/**
	 * 保存债权信息
	 * 
	 * @param creditDTO
	 */
	public void saveCredit(CreditDTO creditDTO);

	/**
	 * 更新债权状态
	 * 
	 * @param orderNo
	 * @param status
	 */
	public void updateCreditStatus(String orderNo, String status);
	
	/**
	 * 查询赎回信息
	 * 
	 * @param creditSurrenderInfoReqDTO
	 * @param creditSurrenderInfoResDTO
	 */
	public void getSurrenderInfo(
			CreditSurrenderInfoReqDTO creditSurrenderInfoReqDTO,
			CreditSurrenderInfoResDTO creditSurrenderInfoResDTO);
	
	/**
	 * 赎回核算
	 * 
	 * @param creditSurrenderChargeReqDTO
	 * @param creditSurrenderChargeResDTO
	 */
	public void surrenderCharge(
			CreditSurrenderChargeReqDTO creditSurrenderChargeReqDTO,
			CreditSurrenderChargeResDTO creditSurrenderChargeResDTO);
	

	/**
	 * 成功处理债权申购
	 * 
	 * @param creditSuccessDTO
	 */
	public void dealCreditSuccess(CreditSuccessDTO creditSuccessDTO);
	
}
