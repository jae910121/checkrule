/**
 * 
 */
package com.guohualife.ebiz.bpm.credit.bo.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.base.constants.Constants;
import com.guohualife.ebiz.bpm.credit.bo.CreditBo;
import com.guohualife.ebiz.bpm.credit.constants.ConstantsForCredit.ENUM_CREDIT_STATUS;
import com.guohualife.ebiz.bpm.credit.dto.CreditDTO;
import com.guohualife.ebiz.bpm.credit.dto.CreditListQueryDTO;
import com.guohualife.ebiz.bpm.credit.dto.CreditSuccessDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderChargeReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderInfoReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderChargeResDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderInfoResDTO;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.bo.OrderSurrenderBo;
import com.guohualife.ebiz.customer.account.dto.CustomerQryResultDTO;
import com.guohualife.ebiz.product.constant.ConstantsForProduct;
import com.guohualife.ebiz.product.constant.ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.ebiz.trace.service.TraceService;
import com.guohualife.edb.bpm.dao.EbizCreditDAO;
import com.guohualife.edb.bpm.dao.EbizSurrenderDAO;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizCredit;
import com.guohualife.edb.bpm.model.EbizCreditExample;
import com.guohualife.edb.bpm.model.EbizCreditExample.Criteria;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizOrderAccount;
import com.guohualife.edb.payment.dao.PayEbizTradeMiddleDAO;
import com.guohualife.edb.product.model.EbizProductProperty;
import com.guohualife.platform.base.api.bo.implement.BaseBOImpl;
import com.guohualife.platform.common.api.util.CollectionUtil;
import com.guohualife.platform.common.api.util.StringUtil;

/**
 * 债权Bo实现
 * 
 * @author wangxulu
 *
 */
@Component
public class CreditBoImpl extends BaseBOImpl implements CreditBo {

	@Resource
	private EbizCreditDAO ebizCreditDAO;
	
	@Resource
	private OrderBo orderBo;
	
	@Resource
	private OrderSurrenderBo orderSurrenderBo;
	
	@Resource
	private ProductService productService;
	
	@Resource
	private AssetBo assetBo;
	
	@Resource(name = "ebizSurrenderDAOImpl")
	private EbizSurrenderDAO ebizSurrenderDAO;
	
	@Resource
	private TraceService traceService;

	@Resource
	private PayEbizTradeMiddleDAO payEbizTradeMiddleDAO;

	/**
	 * 根据订单查询债权表信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public EbizCredit getCreditByOrderNo(String orderNo){
		EbizCreditExample ebizCreditExample = new EbizCreditExample();
		ebizCreditExample.createCriteria().andOrderNoEqualTo(orderNo);
		List<EbizCredit> credits = ebizCreditDAO.selectByExample(ebizCreditExample);
		if(credits != null && credits.size() > 0){
			return credits.get(0);
		}
		return null;
	}
	
	/**
	 * 保存债权信息
	 * 
	 * @param creditDTO
	 */
	public void saveCredit(CreditDTO creditDTO) {
		EbizCredit ebizCredit = new EbizCredit();
		ebizCredit.setCreatedDate(new Date());
		ebizCredit.setCreatedUser("交易中心");
		ebizCredit.setInvestAmount(creditDTO.getInvestAmount());
		ebizCredit.setModifiedUser("交易中心");
		ebizCredit.setMult(creditDTO.getMult());
		ebizCredit.setOrderNo(creditDTO.getOrderNo());
		ebizCredit.setProcessStatus(ENUM_CREDIT_STATUS.WAITCONFIRM.getValue());
		saveOrUpdateCredit(ebizCredit);
	}

	/**
	 * 更新债权状态
	 * 
	 * @param orderNo
	 * @param status
	 */
	public void updateCreditStatus(String orderNo, String status) {
		EbizCreditExample ebizCreditExample = new EbizCreditExample();
		ebizCreditExample.createCriteria().andOrderNoEqualTo(orderNo);
		EbizCredit updEbizCredit = new EbizCredit();
		updEbizCredit.setModifiedDate(new Date());
		updEbizCredit.setProcessStatus(status);
		ebizCreditDAO
				.updateByExampleSelective(updEbizCredit, ebizCreditExample);
	}

	/**
	 * 查询债权列表
	 * 
	 * @param creditListQueryDTO
	 * @return
	 */
	public List<EbizCredit> getCreditList(CreditListQueryDTO creditListQueryDTO){
		EbizCreditExample ebizCreditExample = new EbizCreditExample();
		Criteria criteria = ebizCreditExample.createCriteria();
		criteria.andIsDeleteEqualTo((short)0);
		
		if (creditListQueryDTO.getProcessStatusList() != null
				&& creditListQueryDTO.getProcessStatusList().size() > 0) {
			criteria.andProcessStatusIn(creditListQueryDTO
					.getProcessStatusList());
		}
		
		if(creditListQueryDTO.getValueDate() != null){
			criteria.andValueDateLessThan(creditListQueryDTO.getValueDate());
		}
		
		if(creditListQueryDTO.getExpiryDate() != null){
			criteria.andExpiryDateLessThanOrEqualTo(creditListQueryDTO.getExpiryDate());
		}
		
		if(creditListQueryDTO.getRaiseDate() != null){
			criteria.andRaiseDateLessThan(creditListQueryDTO.getRaiseDate());
		}
		
		List<EbizCredit> ebizCreditList = ebizCreditDAO
				.selectByExample(ebizCreditExample);
		return ebizCreditList;
	}

	/**
	 * 查询赎回信息
	 * 
	 * @param creditSurrenderInfoReqDTO
	 * @param creditSurrenderInfoResDTO
	 */
	public void getSurrenderInfo(
			CreditSurrenderInfoReqDTO creditSurrenderInfoReqDTO,
			CreditSurrenderInfoResDTO creditSurrenderInfoResDTO) {
		
		String orderNo = creditSurrenderInfoReqDTO.getOrderNo();
		creditSurrenderInfoResDTO.setOrderNo(orderNo);
		
		EbizOrder ebizOrder = orderBo.getOrder(orderNo);
		String result = checkSurrender(creditSurrenderInfoReqDTO, ebizOrder);
		if(StringUtil.isNotEmpty(result)){
			creditSurrenderInfoResDTO.failure(result);
			return;
		}
		
		EbizAsset ebizAsset = assetBo.getAssetByOrderNo(orderNo);
		initSurrenderAccount(creditSurrenderInfoResDTO, orderNo, ebizOrder.getCustomerId());
		initSurrenderBasic(creditSurrenderInfoResDTO, ebizOrder, ebizAsset);
		initSurrenderByProduct(creditSurrenderInfoResDTO, ebizOrder, ebizAsset);
		
	}
	
	/**
	 * 赎回核算
	 * 
	 * @param creditSurrenderChargeReqDTO
	 * @param creditSurrenderChargeResDTO
	 */
	public void surrenderCharge(
			CreditSurrenderChargeReqDTO creditSurrenderChargeReqDTO,
			CreditSurrenderChargeResDTO creditSurrenderChargeResDTO) {
		String orderNo = creditSurrenderChargeReqDTO.getOrderNo();
		creditSurrenderChargeResDTO.setOrderNo(orderNo);
		
		EbizOrder ebizOrder = orderBo.getOrder(orderNo);

		// 检查必录项,订单状态是否有效,是否支持赎回
		String errorMessage = checkSurrenderCharge(creditSurrenderChargeReqDTO, ebizOrder);
		if (StringUtil.isNotEmpty(errorMessage)) {
			logger.info(errorMessage);
			creditSurrenderChargeResDTO.failure(errorMessage);
			return;
		}
		
		EbizAsset ebizAsset = assetBo.getAssetByOrderNo(orderNo);
		// 申请赎回金额
		BigDecimal applyMoney = creditSurrenderChargeReqDTO.getApplyMoney();
		
		// 投资本金
		BigDecimal investAmount = ebizAsset.getInvestAmount();

		// 赎回收益与本金比例  = 本金 / 总资产
		BigDecimal surrenderFeeRate = new BigDecimal(0);
		List<EbizProductProperty> ebizProductProperty = productService
				.getProductPropertyByOrderType(
						ebizOrder.getProductCode(),
						ENUM_PRODUCT_PROPERTY_TYPE.SURRENDER_FEE_RATE
								.getValue(), ebizOrder.getOrderType())
				.getEbizProductPropertyList();
		if (CollectionUtil.isNotEmpty(ebizProductProperty)){
			surrenderFeeRate = new BigDecimal(ebizProductProperty.get(0).getPropertyValue());
		}
		// 扣除手续费 = surrenderFeeRate * 申请赎回金额
		BigDecimal surrenderFee = surrenderFeeRate.multiply(applyMoney).setScale(2,BigDecimal.ROUND_HALF_UP);
		// 实际到账金额 = 申请赎回金额 - 扣除手续费	
		BigDecimal availableMoney = applyMoney.subtract(surrenderFee).setScale(2, BigDecimal.ROUND_HALF_UP);
		// 赎回本金金额 = 申请赎回金额 * 本金 / 总资产
		BigDecimal surrenderInvest = applyMoney.multiply(investAmount).divide(ebizAsset.getTotalValue(), 2, 4);
		// 赎回收益金额 = 申请赎回金额 - 赎回本金金额
		BigDecimal surrenderIncome = applyMoney.subtract(surrenderInvest).setScale(2, BigDecimal.ROUND_HALF_UP);

		// 剩余本金 = 投资本金 - 赎回本金金额
		BigDecimal surplusMoney = investAmount.subtract(surrenderInvest).setScale(2,BigDecimal.ROUND_HALF_UP);
		// 剩余收益 = 帐户总资产 - 投资本金 - 赎回收益金额
		BigDecimal surplusIncome = ebizAsset.getTotalValue().subtract(investAmount).subtract(surrenderIncome).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		String confirmMessage = generateResultMessage(ebizOrder,
				surrenderIncome, surrenderInvest, surplusMoney, surplusIncome,
				surrenderFee);
		creditSurrenderChargeResDTO.setApplyMoney(applyMoney);
		creditSurrenderChargeResDTO.setSurrenderFee(surrenderFee);
		creditSurrenderChargeResDTO.setAvailableMoney(availableMoney);
		creditSurrenderChargeResDTO.setSurrenderIncome(surrenderIncome);
		creditSurrenderChargeResDTO.setSurrenderInvest(surrenderInvest);
		creditSurrenderChargeResDTO.setConfirmMessage(confirmMessage);
		logger.info("申请赎回金额:"+applyMoney+",比例:"+surrenderFeeRate+",实际到账金额"+availableMoney+",赎回收益金额"+surrenderIncome+",赎回本金金额"+surrenderInvest);
	}	

	public void dealCreditSuccess(CreditSuccessDTO creditSuccessDTO) {
		EbizOrder ebizOrder = orderBo.getOrder(creditSuccessDTO.getOrderNo());
		String productCode = ebizOrder.getProductCode();
		String orderType = ebizOrder.getOrderType();

		EbizCredit ebizCredit = getCreditByOrderNo(creditSuccessDTO.getOrderNo());
		ebizCredit.setOrderNo(creditSuccessDTO.getOrderNo());
		
		ebizCredit.setValueDate(
				orderBo.getValueDate(productCode, orderType, creditSuccessDTO.getSuccessDate()));
		creditSuccessDTO.setValueDate(ebizCredit.getValueDate());
		ebizCredit.setExpiryDate(
				orderBo.getExpiryDate(productCode, orderType, ebizCredit.getValueDate()));
		creditSuccessDTO.setExpiryDate(ebizCredit.getExpiryDate());
		ebizCredit.setProcessStatus(ENUM_CREDIT_STATUS.RAISESUCCESS.getValue());
		saveOrUpdateCredit(ebizCredit);

	}
	
	/**
	 * 获取赎回信息校验
	 * @param creditSurrenderInfoReqDTO
	 * @param ebizOrder
	 * @return
	 */
	private String checkSurrender(CreditSurrenderInfoReqDTO creditSurrenderInfoReqDTO, EbizOrder ebizOrder){ 

		// 必填字段校验
		if (creditSurrenderInfoReqDTO == null
				|| StringUtil.isEmpty(creditSurrenderInfoReqDTO.getOrderNo())
				|| creditSurrenderInfoReqDTO.getCustomerId() == null) {
			return "传入参数有误";
		}
		// 检查 订单状态是否有效
		String result = orderSurrenderBo.checkSurrenderInfo(ebizOrder);
		if (StringUtil.isNotEmpty(result)) {
			return result;
		}
		
		result = orderSurrenderBo.checkByProduct(ebizOrder,
				null);

		return result;
	}

	/**
	 * 债权赎回核算检查
	 * 
	 * @param creditSurrenderChargeReqDTO
	 * @param creditSurrenderChargeResDTO
	 * @param ebizOrder
	 * @return
	 */
	private String checkSurrenderCharge(
			CreditSurrenderChargeReqDTO creditSurrenderChargeReqDTO,
			EbizOrder ebizOrder) {
		
		// 检查必填字段
		if (creditSurrenderChargeReqDTO.getCustomerId() == null){
			return "客户ID不能为空";
		}
		if (StringUtil.isEmpty(creditSurrenderChargeReqDTO.getOrderNo())){
			return "订单号不能为空";
		}
		if (creditSurrenderChargeReqDTO.getApplyMoney() == null){
			return "申请金额不能为空";
		}
		
		// 检查 订单状态是否有效
		String result = orderSurrenderBo.checkSurrenderInfo(ebizOrder);
		if(StringUtil.isNotEmpty(result)){
			return result;
		}
		
		result = orderSurrenderBo.checkByProduct(ebizOrder,
				creditSurrenderChargeReqDTO.getApplyMoney());
		
		return result;
		
	}
	
	private void saveOrUpdateCredit(EbizCredit ebizCredit) {
		ebizCredit.setModifiedDate(new Date());
		if ("".equals(StringUtil.getString(ebizCredit.getCreditId()))) {
			ebizCredit.setIsDelete((short) 0);
			ebizCreditDAO.insertSelective(ebizCredit);
		} else {
			ebizCreditDAO.updateByPrimaryKeySelective(ebizCredit);
		}
	}
	
	/**
	 * 生成返回信息
	 * 
	 * @param surrenderIncome
	 * @param surrenderInvest
	 * @param surplusMoney
	 * @param surplusIncome
	 * @return
	 */
	private String generateResultMessage(EbizOrder ebizOrder, BigDecimal surrenderIncome,
			BigDecimal surrenderInvest, BigDecimal surplusMoney,
			BigDecimal surplusIncome, BigDecimal surrenderFee) {
		String message = "";
		EbizProductProperty ebizProductProperty = productService
				.getSingleProductPropertyByOrderType(
						ebizOrder.getProductCode(),
						ENUM_PRODUCT_PROPERTY_TYPE.SURRENDER_MESSAGE
								.getValue(), ebizOrder.getOrderType());
		if (ebizProductProperty != null){
			message = ebizProductProperty.getPropertyValue();
			message = message.replaceAll("[?]", "").replaceAll("surrenderIncome", surrenderIncome+"")
					.replaceAll("surrenderInvest", surrenderInvest+"")
					.replaceAll("surplusMoney", surplusMoney+"")
					.replaceAll("surrenderFee", surrenderFee+"")
					.replaceAll("surplusIncome", surplusIncome+"");
		}
		
		return message;
	}

	private void initSurrenderAccount(
			CreditSurrenderInfoResDTO creditSurrenderInfoResDTO,
			String orderNo, Long customerId) {
		EbizOrderAccount ebizOrderAccount = orderBo.getOrderAccount(orderNo);
		creditSurrenderInfoResDTO.setRecAccountNo(
				StringUtil.substring(ebizOrderAccount.getCardBookCode(), ebizOrderAccount.getCardBookCode().length() - 4));
		CustomerQryResultDTO customerQryResultDTO = orderBo.getCustomerInfo(customerId);
		creditSurrenderInfoResDTO.setRecAccountName(
				customerQryResultDTO.getCustomerDTOList().get(0).getRealName());
		creditSurrenderInfoResDTO.setRecAccountType(ebizOrderAccount.getBankCode());
		creditSurrenderInfoResDTO.setBankName(ebizOrderAccount.getBankName());
	}
	
	private void initSurrenderByProduct(
			CreditSurrenderInfoResDTO creditSurrenderInfoResDTO,
			EbizOrder ebizOrder, EbizAsset ebizAsset) {
		String isSurrender = Constants.FAILED;
		
		String productCode = ebizOrder.getProductCode();
		String orderType = ebizOrder.getOrderType();
		
		//赎回(领取)
		EbizProductProperty ebizProductProperty = productService
				.getSingleProductPropertyByOrderType(
						productCode,
						ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.IS_REDEEM
								.getValue(), orderType);
		if (ebizProductProperty != null) {
			isSurrender = ebizProductProperty.getPropertyValue();
		}
		
		if (("2".equals(isSurrender) && new Date().compareTo(ebizOrder
						.getEndCloseDate()) > -1)) {
			isSurrender = Constants.SUCCESS;
		}
		
		creditSurrenderInfoResDTO.setIsSurrender(isSurrender);
		
		// 判断isSurrender=1 允许赎回 或者 isSurrender=2 且 已经过了封闭日期
		if (isSurrender.equals(Constants.SUCCESS)) {
			BigDecimal surrenderUnit = BigDecimal.ZERO;
			BigDecimal surrenderMin = BigDecimal.ZERO;
			BigDecimal surrenderRestMin = BigDecimal.ZERO;
			BigDecimal surrenderMax = BigDecimal.ZERO;
	
			//赎回金额倍数
			ebizProductProperty = productService.getSingleProductPropertyByOrderType(
					productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.MUL_GET_MONEY.getValue(),
					orderType);
			if(ebizProductProperty != null){
				surrenderUnit = new BigDecimal(ebizProductProperty.getPropertyValue());
			}
			
			//赎回最小金额
			ebizProductProperty = productService.getSingleProductPropertyByOrderType(
					productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.MIN_GET_MONEY.getValue(),
					orderType);
			if(ebizProductProperty != null){
				surrenderMin = new BigDecimal(ebizProductProperty.getPropertyValue());
			}
			
			//赎回账户剩余最低金额
			ebizProductProperty = productService.getSingleProductPropertyByOrderType(
					productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.MIN_SURPLUS_VALUE.getValue(),
					orderType);
			if(ebizProductProperty != null){
				surrenderRestMin = new BigDecimal(ebizProductProperty.getPropertyValue());
			}
			
			//赎回最大金额 
			surrenderMax = orderSurrenderBo.getMaxForAmount(
					ebizAsset.getTotalValue(), surrenderRestMin, surrenderUnit);
			
			creditSurrenderInfoResDTO.setSurrenderRestMin(surrenderRestMin);
			creditSurrenderInfoResDTO.setSurrenderMax(surrenderMax);
			creditSurrenderInfoResDTO.setSurrenderMin(surrenderMin);
			creditSurrenderInfoResDTO.setSurrenderUnit(surrenderUnit);
		}

		
		
		
	}
	
	private void initSurrenderBasic(
			CreditSurrenderInfoResDTO creditSurrenderInfoResDTO,
			EbizOrder ebizOrder, EbizAsset ebizAsset) {

		creditSurrenderInfoResDTO.setProductName(ebizOrder.getProductName());
		creditSurrenderInfoResDTO.setProductCode(ebizOrder.getProductCode());
		creditSurrenderInfoResDTO.setTotalValue(ebizAsset.getTotalValue());
		creditSurrenderInfoResDTO.setInvestAmount(ebizAsset.getInvestAmount());

	}
	
}
