package com.guohualife.ebiz.bpm.order.bo.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.insurance.bo.InsuranceBo;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.bo.OrderSurrenderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder.ENUM_ORDER_STATUS;
import com.guohualife.ebiz.bpm.order.dao.EbizSurrenderSPECDAO;
import com.guohualife.ebiz.bpm.order.dto.SurrenderApplyDTO;
import com.guohualife.ebiz.gateway.ins.service.InsTradeService;
import com.guohualife.ebiz.message.dto.MessageInfoDTO;
import com.guohualife.ebiz.message.service.MessageService;
import com.guohualife.ebiz.product.constant.ConstantsForProduct;
import com.guohualife.ebiz.product.constant.ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE;
import com.guohualife.ebiz.product.dto.EbizProductPropertyDTO;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.edb.bpm.dao.EbizSurrenderDAO;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizOrderAccount;
import com.guohualife.edb.bpm.model.EbizSurrender;
import com.guohualife.edb.bpm.model.EbizSurrenderExample;
import com.guohualife.edb.product.model.EbizProductProperty;
import com.guohualife.platform.base.api.bo.implement.BaseBOImpl;
import com.guohualife.platform.common.api.util.DateUtil;
import com.guohualife.platform.common.api.util.JsonUtil;
import com.guohualife.platform.common.api.util.StringUtil;

/**
 * 订单赎回Bo实现
 * 
 * @author wangxulu
 *
 */
@Component
public class OrderSurrenderBoImpl extends BaseBOImpl implements OrderSurrenderBo {

	@Resource(name = "ebizSurrenderDAOImpl")
	private EbizSurrenderDAO ebizSurrenderDAO;
	
	@Resource(name = "ebizSurrenderSPECDAOImpl")
	private EbizSurrenderSPECDAO ebizSurrenderSPECDAO;
	
	@Resource
	private InsTradeService insTradeService;
	
	@Resource
	private InsuranceBo insuranceBo;
	
	@Resource
	private OrderBo orderBo;
	
	@Resource
	private AssetBo assetBo;
	
	@Resource
	private ProductService productService;
	
	@Resource
	private MessageService messageService;
	
	/**
	 * 保存/更新赎回表
	 * 
	 * @param ebizSurrender
	 * @return
	 */
	public Long saveOrUpdateSurrender(EbizSurrender ebizSurrender){
		ebizSurrender.setModifiedDate(new Date());
		if("".equals(StringUtil.getString(ebizSurrender.getSurrenderId()))){
			ebizSurrender.setIsDelete((short)0);
			ebizSurrenderDAO.insertSelective(ebizSurrender);
		}else{
			ebizSurrenderDAO.updateByPrimaryKeySelective(ebizSurrender);
		}
		
		return ebizSurrender.getSurrenderId();
	}

	/**
	 * 根据ID查询赎回信息
	 * 
	 * @param accetpNo
	 * @return
	 */
	public EbizSurrender getSurrenderById(Long surrenderId) {
		return ebizSurrenderDAO.selectByPrimaryKey(surrenderId);
	}

	/**
	 * 根据状态查询赎回列表
	 * 
	 * @param statusList
	 * @return
	 */
	public List<EbizSurrender> getSurrenderByStatus(List<String> statusList){
		EbizSurrenderExample ebizSurrenderExample = new EbizSurrenderExample();
		ebizSurrenderExample.createCriteria().andStatusIn(statusList);
		return ebizSurrenderDAO.selectByExample(ebizSurrenderExample);
	}

	/**
	 * 根据订单号询赎回列表
	 * 
	 * @param statusList
	 * @return
	 */
	public List<EbizSurrender> getSurrenderByOrderNo(String orderNo) {
		EbizSurrenderExample ebizSurrenderExample = new EbizSurrenderExample();
		ebizSurrenderExample.createCriteria().andOrderNoEqualTo(orderNo);
		return ebizSurrenderDAO.selectByExample(ebizSurrenderExample);
	}
	
	/**
	 * 根据资金划拨状态查询赎回列表
	 * 
	 * @param statusList
	 * @return
	 */
	public List<EbizSurrender> getSurrenderByTransferStatus(
			List<String> statusList){
		EbizSurrenderExample ebizSurrenderExample = new EbizSurrenderExample();
		ebizSurrenderExample.createCriteria().andTransferStatusIn(statusList);
		return ebizSurrenderDAO.selectByExample(ebizSurrenderExample);
	}

	/**
	 * 申请赎回
	 * @param surrenderApplyDTO
	 */
	public void applySurrender(SurrenderApplyDTO surrenderApplyDTO) {
		String orderNo = surrenderApplyDTO.getOrderNo();
		
		EbizOrder ebizOrder = orderBo.getOrder(orderNo);
		String result = surrenderApplyCheck(surrenderApplyDTO, ebizOrder);
		if(StringUtil.isNotEmpty(result)){
			logger.info("赎回提交失败原因：" + result);
			surrenderApplyDTO.setErrorMessage(result);
		}
		
		EbizAsset ebizAsset = assetBo.getAssetByOrderNo(orderNo);
		
		//保存领取表
		EbizSurrender ebizSurrender = new EbizSurrender();
		//订单号
		ebizSurrender.setOrderNo(orderNo);
		//领取类型
		ebizSurrender.setSurrenderType(surrenderApplyDTO.getSurrenderType());
		//领取原因
		ebizSurrender.setSurrenderReason("1");
		//退保说明
		ebizSurrender.setSurrenderRemark("客户急需用钱");
		//账户价值
		ebizSurrender.setAccountValue(ebizAsset.getTotalValue());
		//投资本金
		ebizSurrender.setInvestAmount(ebizAsset.getInvestAmount());
		//申请金额
		ebizSurrender.setApplyAmount(surrenderApplyDTO.getApplyMoney());
		//手续费金额
		ebizSurrender.setSurrenderFee(surrenderApplyDTO.getSurrenderFee());
		//实际金额
		ebizSurrender.setSurrenderAmount(surrenderApplyDTO.getAvailableMoney());
		//赎回本金
		ebizSurrender.setSurrenderPrincipal(surrenderApplyDTO.getSurrenderPrincipal());
		//领取日期
		ebizSurrender.setSurrenderDate(new Date());
		//领取状态
		ebizSurrender.setStatus(ConstantsForOrder.ENUM_SURRENDER_STATUS.INFORM.getValue());
		//申请平台
		ebizSurrender.setPlatformType(surrenderApplyDTO.getPlatformType());
		//创建时间
		ebizSurrender.setCreatedDate(new Date());
		//最后更新时间
		ebizSurrender.setModifiedDate(new Date());
		//创建用户
		ebizSurrender.setCreatedUser("交易中心");
		//修改用户
		ebizSurrender.setModifiedUser("交易中心");
		//删除标志
		ebizSurrender.setIsDelete((short)0);
		
		Long surrenderId = ebizSurrenderDAO.insertSelective(ebizSurrender);
		
		//更新订单状态
        orderBo.updateOrderStatus(orderNo, ENUM_ORDER_STATUS.REDEEMING.getValue());
        
        surrenderApplyDTO.setSurrenderId(surrenderId);
        return;
	}
	
	/**
	 * 订单赎回校验
	 * 
	 * @param orderNo
	 * @return
	 */
	public String checkSurrenderInfo(EbizOrder ebizOrder) {
		if (ebizOrder.getOrderStatus().equals(
				ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMALL.getValue())) {
			logger.info("订单" + ebizOrder.getOrderNo() + "已全部赎回");
			return "订单" + ebizOrder.getOrderNo() + "已全部赎回";
		}

		if (ebizOrder.getOrderStatus().equals(
				ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMING.getValue())) {
			logger.info("订单" + ebizOrder.getOrderNo() + "赎回处理中，暂不允许操作");
			return "订单" + ebizOrder.getOrderNo() + "赎回处理中，暂不允许操作";
		}
		return "";
	}
	
	/**
	 * 根据产品配置校验
	 * 
	 * @param ebizOrder
	 * @param applyMoney
	 * @return
	 */
	public String checkByProduct(EbizOrder ebizOrder, BigDecimal applyMoney) {
		
		String isSurrender = "";
		EbizProductProperty ebizProductProperty = productService
				.getSingleProductPropertyByOrderType(
						ebizOrder.getProductCode(),
						ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.IS_REDEEM
								.getValue(), ebizOrder.getOrderType());
		if (ebizProductProperty != null) {
			isSurrender = ebizProductProperty.getPropertyValue();
		}
		
		if ("0".equals(isSurrender)) {
			return "该订单不支持赎回";
		}
		
		if ("1".equals(isSurrender)) {
			return "";
		} 
		
		if ("2".equals(isSurrender)) {
			Date endCloseDate = ebizOrder.getEndCloseDate();
			if (new Date().compareTo(endCloseDate) < 0) {
				return "该订单不支持赎回";
			}
		} 
		
		if(applyMoney == null){
			return "";
		}
		
		String productCode = ebizOrder.getProductCode();
		String orderType = ebizOrder.getOrderType();
		
		ebizProductProperty = productService.getSingleProductPropertyByOrderType(
				productCode,
				ENUM_PRODUCT_PROPERTY_TYPE.MIN_GET_MONEY.getValue(), orderType);
		if (ebizProductProperty != null) {
			if (applyMoney.compareTo(new BigDecimal(ebizProductProperty.getPropertyValue())) < 0){
				logger.info("最低赎回金额为" + ebizProductProperty.getPropertyValue());
				return "最低赎回金额为" + ebizProductProperty.getPropertyValue();
			}
		}
		
		ebizProductProperty = productService.getSingleProductPropertyByOrderType(
				productCode,
				ENUM_PRODUCT_PROPERTY_TYPE.MUL_GET_MONEY.getValue(), orderType);
		if (ebizProductProperty != null) {
			if (applyMoney.divideAndRemainder(
					new BigDecimal(ebizProductProperty
							.getPropertyValue()))[1].compareTo(BigDecimal.ZERO) != 0) {
				logger.info("赎回金额必须为" + ebizProductProperty.getPropertyValue() + "的倍数");
				return "赎回金额必须为" + ebizProductProperty.getPropertyValue() + "的倍数";
				
			}
		}
		
		ebizProductProperty = productService.getSingleProductPropertyByOrderType(
				productCode,
				ENUM_PRODUCT_PROPERTY_TYPE.MIN_SURPLUS_VALUE.getValue(),
				orderType);
		EbizAsset asset = assetBo.getAssetByOrderNo(ebizOrder.getOrderNo());
		if (ebizProductProperty != null) {
			if (asset.getTotalValue().subtract(applyMoney).compareTo(
					new BigDecimal(ebizProductProperty.getPropertyValue())) == -1) {
				logger.info("赎回后账户剩余最低金额必须大于等于" + ebizProductProperty.getPropertyValue());
				return "赎回后账户剩余最低金额必须大于等于" + ebizProductProperty.getPropertyValue();
			}
		}
		
		return "";
	}
	
	/**
	 * 检查赎回提交信息.
	 * 
	 * @param surrenderApplyDTO
	 * @param ebizOrder
	 * @return
	 */
	private String surrenderApplyCheck(
			SurrenderApplyDTO surrenderApplyDTO, EbizOrder ebizOrder){
		
		if (surrenderApplyDTO == null
				|| surrenderApplyDTO.getCustomerId() == null
				|| StringUtil.isEmpty(surrenderApplyDTO.getSurrenderType())
				|| surrenderApplyDTO.getApplyMoney() == null
				|| surrenderApplyDTO.getSurrenderPrincipal() == null
				|| StringUtil.isEmpty(surrenderApplyDTO.getOrderNo())
				|| surrenderApplyDTO.getAvailableMoney() == null
				|| surrenderApplyDTO.getSurrenderFee() == null
				|| StringUtil.isEmpty(surrenderApplyDTO.getPlatformType())) {
			return "传入参数有误";
		}
		
		// 检查 订单状态是否有效
		String message = checkSurrenderInfo(ebizOrder);
		if (StringUtil.isNotBlank(message)) {
			return message;
		}
		
		message = checkByProduct(ebizOrder, surrenderApplyDTO.getApplyMoney());
				
		return message;
	}
	
	/**
	 * 计算最大领取金额
	 * 
	 * @param totalValue
	 * @param surrenderRestMin
	 * @param surrenderUnit
	 * @return
	 */
	public BigDecimal getMaxForAmount(BigDecimal totalValue,
			BigDecimal surrenderRestMin, BigDecimal surrenderUnit) {
		// 剩余账户价值
		BigDecimal maxForAmount = totalValue;
		if (maxForAmount.compareTo(surrenderRestMin) >= 0) {
			// 如果账户价值大于 领取后账户最低剩余金额
			maxForAmount = maxForAmount.subtract(surrenderRestMin);
		}
		logger.info("最大金额：" + maxForAmount);

		// 金额倍数
		if (surrenderUnit != null && !((BigDecimal.ZERO).compareTo(surrenderUnit) == 0)) {
			return maxForAmount;
		}
		BigDecimal[] divideAndRemainder = 
				maxForAmount.divideAndRemainder(surrenderUnit);

		maxForAmount = maxForAmount.subtract(divideAndRemainder[1]);
		logger.info("能被" + surrenderUnit + "整除的最大金额:" + maxForAmount);
		return maxForAmount;
	}

	@Override
	public void sendSurrenderSuccessMsg(EbizSurrender ebizSurrender,
			EbizOrder ebizOrder) {
		EbizOrderAccount ebizOrderAccount = orderBo.getOrderAccount(ebizSurrender.getOrderNo());
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("applyAmount", ebizSurrender.getSurrenderAmount().toString());
		paramMap.put("productName", ebizOrder.getProductName());
		paramMap.put("customerName", ebizOrder.getProductName());
		paramMap.put("bankName", ebizOrderAccount.getBankName());
		paramMap.put("cardBookCode", 
				StringUtils.substring(ebizOrderAccount.getBankName(), ebizOrderAccount.getBankName().length() - 4, ebizOrderAccount.getBankName().length()));
		paramMap.put("surrenderDate", DateUtil.formatDate(ebizSurrender.getSurrenderDate(), "yyyy-MM-dd"));
		paramMap.put("applyAmount", ebizSurrender.getApplyAmount().toString());
		paramMap.put("surrenderPrincipal", ebizSurrender.getSurrenderPrincipal().toString());
		paramMap.put("surrenderIncome", ebizSurrender.getApplyAmount().subtract(ebizSurrender.getSurrenderPrincipal()).toString());
		paramMap.put("surrenderStatus", ConstantsForOrder.ENUM_SURRENDER_STATUS.getNameByValue(ebizSurrender.getStatus()));
		
		EbizProductPropertyDTO ebizProductPropertyDTO = productService.getProductProperty(
				ebizOrder.getProductCode(), 
				ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.REDEEM_SUCC_TEMPLATE.getValue(), 
				ebizSurrender.getSurrenderType(), 
				"surrenderType");
		
		if(ebizProductPropertyDTO.getEbizProductPropertyList() != null &&
				ebizProductPropertyDTO.getEbizProductPropertyList().size() > 0){
			
			MessageInfoDTO messageInfoDTO = new MessageInfoDTO();
			messageInfoDTO.setCustomerId(StringUtil.getString(ebizOrder.getCustomerId()));
			messageInfoDTO.setParamMap(paramMap);
			
			for(EbizProductProperty ebizProductProperty : ebizProductPropertyDTO.getEbizProductPropertyList()){
				messageInfoDTO.setTemplateCode(ebizProductProperty.getPropertyValue());
				try{
					messageService.sendMessageByCustomerId(messageInfoDTO);
				}catch(Exception e){
					logger.info("订单" + ebizSurrender.getOrderNo() + "赎回成功发送消息异常", e);
				}
			}
		}
	}
}
