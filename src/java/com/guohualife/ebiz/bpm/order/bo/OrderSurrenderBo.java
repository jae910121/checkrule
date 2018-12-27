package com.guohualife.ebiz.bpm.order.bo;

import java.math.BigDecimal;
import java.util.List;

import com.guohualife.ebiz.bpm.order.dto.SurrenderApplyDTO;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizSurrender;

/**
 * 订单赎回Bo接口
 * 
 * @author wangxulu
 *
 */
public interface OrderSurrenderBo {
	
	/**
	 * 根据ID查询赎回信息
	 * 
	 * @param accetpNo
	 * @return
	 */
	public EbizSurrender getSurrenderById(Long surrenderId);
	
	/**
	 * 根据订单号询赎回列表
	 * 
	 * @param statusList
	 * @return
	 */
	public List<EbizSurrender> getSurrenderByOrderNo(String orderNo);
	
	/**
	 * 根据状态查询赎回列表
	 * 
	 * @param statusList
	 * @return
	 */
	public List<EbizSurrender> getSurrenderByStatus(List<String> statusList);
	
	/**
	 * 保存/更新赎回表
	 * 
	 * @param ebizSurrender
	 * @return
	 */
	public Long saveOrUpdateSurrender(EbizSurrender ebizSurrender);
	
	/**
	 * 根据资金划拨状态查询赎回列表
	 * 
	 * @param statusList
	 * @return
	 */
	public List<EbizSurrender> getSurrenderByTransferStatus(
			List<String> statusList);
	
	/**
	 * 申请赎回
	 * 
	 * @param surrenderApplyDTO
	 * @return
	 */
	public void applySurrender(SurrenderApplyDTO surrenderApplyDTO);
	
	/**
	 * 订单赎回校验
	 * 
	 * @param orderNo
	 * @return
	 */
	public String checkSurrenderInfo(EbizOrder ebizOrder);
	
	/**
	 * 根据产品配置校验
	 * 
	 * @param ebizOrder
	 * @param applyMoney
	 * @return
	 */
	public String checkByProduct(EbizOrder ebizOrder, BigDecimal applyMoney);
	
	/**
	 * 计算最大领取金额
	 * 
	 * @param totalValue
	 * @param surrenderRestMin
	 * @param surrenderUnit
	 * @return
	 */
	public BigDecimal getMaxForAmount(BigDecimal totalValue,
			BigDecimal surrenderRestMin, BigDecimal surrenderUnit);
	/**
	 * 发送赎回成功短信
	 * 
	 * @param ebizSurrender
	 * @param ebizOrder
	 */
	public void sendSurrenderSuccessMsg(EbizSurrender ebizSurrender, EbizOrder ebizOrder);
	
}
