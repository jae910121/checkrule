package com.guohualife.ebiz.bpm.credit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.asset.constants.ConstantsForAsset;
import com.guohualife.ebiz.bpm.asset.dto.AssetListQueryDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetModifyDTO;
import com.guohualife.ebiz.bpm.asset.service.AssetModifyService;
import com.guohualife.ebiz.bpm.credit.bo.CreditBo;
import com.guohualife.ebiz.bpm.credit.service.CreditAssetService;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.product.constant.ConstantsForProduct;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizAssetBalance;
import com.guohualife.edb.bpm.model.EbizAssetPrincipal;
import com.guohualife.edb.product.model.EbizProductProperty;
import com.guohualife.platform.common.api.util.DateUtil;
import com.guohualife.platform.common.api.util.StringUtil;

@Service
public class CreditAssetServiceImpl implements CreditAssetService {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	AssetBo assetBo;
	
	@Resource
	private ProductService productService;
	
	@Resource
	private OrderBo orderBo;

	@Resource
	private AssetModifyService assetModifyService;
	
	@Resource
	private CreditBo creditBo;
	
	/**
	 * 计算资产
	 * 
	 * @param balanceDate 结算日期
	 */
	public void dealAsset(Date balanceDate) {
		
		// 成功笔数
		Integer successNum = 0;
		// 异常笔数
		Integer exceptionNum = 0;

		// 收益日期
		Date incomeDate = DateUtil.parseDate(
				DateUtil.formatDate(DateUtil.addDays(balanceDate, -1), DateUtil.ZH_CN_DATE_PATTERN),
				DateUtil.ZH_CN_DATE_PATTERN);
		
		logger.info("批处理结算日期："
				+ DateUtil.formatDate(balanceDate, DateUtil.ZH_CN_DATE_PATTERN)
				+ "，计算收益日期："
				+ DateUtil.formatDate(incomeDate, DateUtil.ZH_CN_DATE_PATTERN));

		EbizAssetBalance currAssetBalance = initBalance(balanceDate);
		
		String checkResult = checkAssetBalance(balanceDate);
		if (StringUtil.isNotEmpty(checkResult)) {
			currAssetBalance.setStatus(
					ConstantsForAsset.ENUM_ASSET_BALANCE_STATUS.FAILED.getValue());
			currAssetBalance.setRemark(checkResult);
			assetBo.saveOrUpdateAssetBalance(currAssetBalance);
			logger.info("债权产品计算收益批处理结束");
			return;
		}
		
		AssetListQueryDTO assetListQueryDTO = new AssetListQueryDTO();
		assetListQueryDTO.setProductType(
				ConstantsForProduct.ENUM_PRODUCT_TYPE.CREDITOR.getValue());
		assetListQueryDTO.setValueDate(balanceDate);
		assetListQueryDTO.setExpiryDate(balanceDate);
		assetListQueryDTO.setStatus(ConstantsForAsset.ENUM_ASSET_STATUS.EFFECTIVE.getValue());
		List<EbizAsset> ebizAssets = 
				assetBo.getAssetList(assetListQueryDTO);
		logger.info("本次待计算收益数据：" + ebizAssets.size() + "条");
		if(ebizAssets != null && ebizAssets.size() > 0){
			
			for(EbizAsset ebizAsset : ebizAssets){
				String orderNo = ebizAsset.getOrderNo();
				BigDecimal daliyIncome = BigDecimal.ZERO;
				logger.info("本次计算收益订单：" + orderNo + "，上次收益日期："
						+ ebizAsset.getLastIncomeDate());
				
				try{
					
					if(ebizAsset.getLastIncomeDate() != null){
						if (balanceDate.compareTo(ebizAsset.getLastIncomeDate()) == 0) {
							logger.info("订单" + orderNo + "最近收益日为结算日，不予处理");
							successNum++;
							continue;
						}
						
						if (DateUtil.addDays(balanceDate, -1).compareTo(
								ebizAsset.getLastIncomeDate()) != 0) {
							logger.info("订单" + orderNo + "最近收益日不为结算日前一天，不予处理");
							exceptionNum++;
							continue;
						}
					}
					
					EbizAssetPrincipal ebizAssetPrincipal = 
							assetBo.getLastAssetPrincipal(orderNo, balanceDate);
					if(ebizAssetPrincipal == null){
						logger.info("订单" + orderNo + "没有小于" + balanceDate + "的本金结算记录");
						exceptionNum++;
						continue;
					}
					
					BigDecimal principalAmount = ebizAssetPrincipal.getPrincipalAmount();
					Date principalDate = ebizAssetPrincipal.getPrincipalDate();
					logger.info("订单" + orderNo + "上次本金结算金额" + principalAmount
							+ "，上次本金结算日期" + principalDate);
					
					EbizProductProperty ebizProductProperty = 
						productService.getSingleProductPropertyByOrderType(
									ebizAsset.getProductCode(),
									ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.INTEREST_RATE.getValue(),
									orderBo.getOrder(orderNo).getOrderType());
					if (ebizProductProperty == null) {
						throw new Exception("年化收益配置不存在");
					}
					BigDecimal rate = new BigDecimal(
							ebizProductProperty.getPropertyValue())
							.divide(new BigDecimal("100"));

					daliyIncome = calculationIncome(rate, principalAmount,
							principalDate, incomeDate);
					logger.info("订单"
							+ orderNo
							+ "计算"
							+ DateUtil.formatDate(incomeDate,
									DateUtil.ZH_CN_DATE_PATTERN) + "收益结果："
							+ daliyIncome);

					AssetModifyDTO assetModifyDTO = new AssetModifyDTO();
					assetModifyDTO.setModifyIncome(daliyIncome);
					assetModifyDTO.setModifyValue(daliyIncome); // 资产变更量 = 收益变更量
					assetModifyDTO.setModifyDate(balanceDate);
					assetModifyDTO.setOperCode(
							DateUtil.formatDate(balanceDate, "yyyyMMdd"));
					assetModifyDTO.setModifyType(
							ConstantsForAsset.ENUM_ASSET_MODIFY_TYPE.ASSET_SYNC.getValue());
					assetModifyDTO.setOrderNo(ebizAsset.getOrderNo());
					ebizAsset = assetModifyService.dealAssetBalance(assetModifyDTO);
					
					if(ConstantsForAsset.ENUM_ASSET_STATUS.SURRENDER_INVEST_ALL.getValue().equals(
							ebizAsset.getStatus())){
						orderBo.updateOrderStatus(
								assetModifyDTO.getOrderNo(), 
								ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMALL.getValue());
					}
					
					successNum++;
				}catch(Exception e){
					logger.info("订单" + orderNo + "计算收益发生异常", e);
					exceptionNum++ ;
					continue;
				}
			}
		}
		
		afterAssetBalance(currAssetBalance, successNum, exceptionNum);
		logger.info("债权产品计算收益批处理结束");
		
	}
	
	private EbizAssetBalance initBalance(Date balanceDate){
		EbizAssetBalance currAssetBalance = 
				assetBo.getAssetBalance(
						ConstantsForAsset.ENUM_ASSET_BALANCE_TYPE.CREDIT.getValue(), 
						balanceDate,
						ConstantsForAsset.ENUM_ASSET_BALANCE_STATUS.PROCESSING.getValue());
		if(currAssetBalance != null){
			return currAssetBalance;
		}
		
		currAssetBalance = new EbizAssetBalance();
		currAssetBalance.setBalanceType(
				ConstantsForAsset.ENUM_ASSET_BALANCE_TYPE.CREDIT.getValue());
		currAssetBalance.setBalanceDate(balanceDate);
		currAssetBalance.setStatus(
				ConstantsForAsset.ENUM_ASSET_BALANCE_STATUS.PROCESSING.getValue());
		currAssetBalance.setCreatedDate(new Date());
		currAssetBalance.setCreatedUser("债权收益资产计算批处理");
		currAssetBalance.setModifiedUser("债权收益资产计算批处理");
		Long balanceId = assetBo.saveOrUpdateAssetBalance(currAssetBalance);
		currAssetBalance.setBalanceId(balanceId);
		return currAssetBalance;
	}
	
	private String checkAssetBalance(Date balanceDate) {
		EbizAssetBalance ebizAssetBalance = assetBo.getAssetBalance(
				ConstantsForAsset.ENUM_ASSET_BALANCE_TYPE.CREDIT.getValue(),
				DateUtil.addDays(balanceDate, -1), 
				ConstantsForAsset.ENUM_ASSET_BALANCE_STATUS.SUCCESS.getValue());
		
		if(ebizAssetBalance == null){
			logger.info(DateUtil.formatDate(DateUtil.addDays(balanceDate, -1),
					DateUtil.ZH_CN_DATE_PATTERN) + "收益结算未成功");
			return "结算日前一日结算未完成";
		}
		
		if (!ConstantsForAsset.ENUM_ASSET_BALANCE_STATUS.SUCCESS.getValue()
				.equals(ebizAssetBalance.getStatus())) {
			logger.info(DateUtil.formatDate(DateUtil.addDays(balanceDate, -1),
					DateUtil.ZH_CN_DATE_PATTERN) + "收益结算未成功");
			return "结算日前一日结算未完成";
		}
		return "";
	}
	
	@SuppressWarnings("unused")
	private String checkAssetIncomeDate(Date balanceDate, EbizAsset ebizAsset){
		String orderNo = ebizAsset.getOrderNo();
		if (ebizAsset.getLastIncomeDate().compareTo(balanceDate) == 0) {
			logger.info("订单" + orderNo + "最近收益日为结算日，不予处理");
			return "1";
		}
		
		if (ebizAsset.getLastIncomeDate().compareTo(
				DateUtil.addDays(balanceDate, -1)) != 0) {
			logger.info("订单" + orderNo + "最近收益日不为结算日前一天，不予处理");
			return "0";
		}
		return "";
	}
	
	private void afterAssetBalance(EbizAssetBalance ebizAssetBalance,
			Integer successNum, Integer exceptionNum) {
		logger.info("本次处理成功" + successNum + "条，异常" + exceptionNum + "条");
		ebizAssetBalance.setStatus(
				ConstantsForAsset.ENUM_ASSET_BALANCE_STATUS.HAS_EXCEPTION.getValue());
		
		if(exceptionNum == 0){
			ebizAssetBalance.setStatus(
					ConstantsForAsset.ENUM_ASSET_BALANCE_STATUS.SUCCESS.getValue());
		}
		ebizAssetBalance.setRemark("本次批处理处理成功" + successNum + "条，异常" + exceptionNum + "条");
		assetBo.saveOrUpdateAssetBalance(ebizAssetBalance);
	}
	
	/**
	 * 计算某日收益
	 * 
	 * @param rate
	 * @param principalAmount
	 * @param principalDate
	 * @param incomeDate
	 * @return
	 */
	private BigDecimal calculationIncome(BigDecimal rate,
			BigDecimal principalAmount, Date principalDate, Date incomeDate) {
		
		Integer incomeDays = DateUtil.getDateDistance(incomeDate, principalDate) + 1;
		logger.info("当日" + DateUtil.formatDate(incomeDate, "yyyy-MM-dd") + "至"
				+ DateUtil.formatDate(principalDate, "yyyy-MM-dd") + "累计收益天数："
				+ incomeDays + "天");
		BigDecimal todayTotalIncome = 
				principalAmount.multiply(rate).multiply(new BigDecimal(incomeDays))
					.divide(new BigDecimal("365"), 2, 4);
		logger.info("当日累计收益:本金*年化收益率*收益天数/365=" + principalAmount + "*" + rate
				+ "*" + incomeDays + "/365=" + todayTotalIncome);
		
		BigDecimal dayBeforeToatalIncome = BigDecimal.ZERO;
		if(incomeDays > 1){
			logger.info("前一日"
					+ DateUtil.formatDate(DateUtil.addDays(incomeDate, -1),
							"yyyy-MM-dd") + "至"
					+ DateUtil.formatDate(principalDate, "yyyy-MM-dd")
					+ "累计收益天数：" + incomeDays + "天");
			dayBeforeToatalIncome = 
					principalAmount.multiply(rate).multiply(new BigDecimal(incomeDays - 1))
						.divide(new BigDecimal("365"), 2, 4);
			logger.info("前一日累计收益:年化收益*前一日收益天数/365=" + principalAmount + "*"
					+ rate + "*" + (incomeDays - 1) + "/365="
					+ todayTotalIncome);
		}
		
		BigDecimal todayIncome = todayTotalIncome.subtract(dayBeforeToatalIncome);
		logger.info("当日收益：" + todayIncome);
		return todayIncome;
	}

}
