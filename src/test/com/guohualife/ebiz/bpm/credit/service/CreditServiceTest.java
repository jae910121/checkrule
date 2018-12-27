package com.guohualife.ebiz.bpm.credit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.guohualife.ebiz.bpm.base.BaseTester;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditApplyPurchaseReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderApplyReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderChargeReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.request.CreditSurrenderInfoReqDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditApplyPurchaseResDTO;
import com.guohualife.ebiz.bpm.credit.dto.response.CreditSurrenderInfoResDTO;
import com.guohualife.ebiz.bpm.order.dto.ThirdOrderDTO;
import com.guohualife.ebiz.product.constant.ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.edb.product.model.EbizProductProperty;
import com.guohualife.platform.common.api.util.XmlUtil;

public class CreditServiceTest extends BaseTester {

	private static final Log logger = LogFactory
			.getLog(CreditServiceTest.class);

	@Resource
	private CreditService creditService;
	
	@Resource
	private ProductService productService;
	
	@Resource
	private CreditSurrenderService creditSurrenderService;

	/**
	 * 测试赎回申请
	 */
	@Test
	public void testApplyPurchase() {
		try {
			 ThirdOrderDTO thirdOrderDTO = new ThirdOrderDTO();
			 thirdOrderDTO.setThirdOrderNo("344");
			 thirdOrderDTO.setThirdType("08");
			 thirdOrderDTO.setThirdUserId("13488");

			CreditApplyPurchaseReqDTO creditApplyPurchaseReqDTO = new CreditApplyPurchaseReqDTO();
			creditApplyPurchaseReqDTO.setThirdOrderDTO(thirdOrderDTO);
			creditApplyPurchaseReqDTO.setMult(new BigDecimal(50));
			creditApplyPurchaseReqDTO.setOrderAmount(new BigDecimal("5000"));
			creditApplyPurchaseReqDTO.setCustomerId(13412l);
			creditApplyPurchaseReqDTO.setOrderType("02");
			creditApplyPurchaseReqDTO.setProductCode("2");
			creditApplyPurchaseReqDTO.setCardBookCode("20150429020100874078");
			creditApplyPurchaseReqDTO.setCardBookType("01");
			creditApplyPurchaseReqDTO.setBankCode("0100");

			CreditApplyPurchaseResDTO creditApplyPurchaseResDTO = creditService
					.applyPurchase(creditApplyPurchaseReqDTO);
			logger.info(XmlUtil.toXml(creditApplyPurchaseResDTO,
					new Class<?>[] {}));
		} catch (Exception e) {
			logger.error("债权申购出错!", e);
		}
	}
	@Test
	public void testApplyPurchase3() {
		try {

			CreditApplyPurchaseReqDTO creditApplyPurchaseReqDTO = new CreditApplyPurchaseReqDTO();
			creditApplyPurchaseReqDTO.setMult(new BigDecimal(2));
			creditApplyPurchaseReqDTO.setOrderAmount(new BigDecimal("2000.00"));
			creditApplyPurchaseReqDTO.setCustomerId(98179l);
			creditApplyPurchaseReqDTO.setOrderType("02");
			creditApplyPurchaseReqDTO.setProductCode("2");
			creditApplyPurchaseReqDTO.setCardBookCode("6217711300173307");
			creditApplyPurchaseReqDTO.setCardBookType("01");
			creditApplyPurchaseReqDTO.setBankCode("302");

			CreditApplyPurchaseResDTO creditApplyPurchaseResDTO = creditService
					.applyPurchase(creditApplyPurchaseReqDTO);
			logger.info(XmlUtil.toXml(creditApplyPurchaseResDTO,
					new Class<?>[] {}));
		} catch (Exception e) {
			logger.error("债权申购出错!", e);
		}
	}
	/**
	 * 测试赎回核算
	 */
	@Test
	public void testSurrenderCharge(){
		CreditSurrenderChargeReqDTO credit = new CreditSurrenderChargeReqDTO();
		credit.setApplyDate(new Date());
		credit.setApplyMoney(BigDecimal.valueOf(1000));
		credit.setCustomerId(new Long(98179));
		credit.setOrderNo("20150616020200878600");
		creditService.surrenderCharge(credit);
	}
	/**
	 * 测试赎回提交
	 */
	@Test
	public void testApplySurrender(){
		CreditSurrenderApplyReqDTO creditSurrenderApplyReqDTO = new CreditSurrenderApplyReqDTO();
		creditSurrenderApplyReqDTO.setApplyDate(new Date());
		creditSurrenderApplyReqDTO.setApplyMoney(new BigDecimal(200));
		creditSurrenderApplyReqDTO.setAvailableMoney(new BigDecimal(100));
		creditSurrenderApplyReqDTO.setCustomerId(13412l);
		creditSurrenderApplyReqDTO.setOrderNo("20150615140200878445");
		creditSurrenderApplyReqDTO.setPlatformType("03");
		creditSurrenderApplyReqDTO.setSurrenderFee(BigDecimal.ZERO);
		creditService.applySurrender(creditSurrenderApplyReqDTO);
	}
	
	/**
	 * 查询债权信息
	 */
	@Test
	public void testSurrenderInfo(){
		CreditSurrenderInfoReqDTO creditSurrenderInfoReqDTO = new CreditSurrenderInfoReqDTO();
		creditSurrenderInfoReqDTO.setCustomerId(new Long(98179));
		creditSurrenderInfoReqDTO.setOrderNo("20150616020200878575");
		CreditSurrenderInfoResDTO res = new CreditSurrenderInfoResDTO();
		res = creditService.getSurrenderInfo(creditSurrenderInfoReqDTO);
		System.out.println(res);
	}
	@Test
	public void productPropertiesTest(){
		String productCode = "1";
		String orderType =ENUM_PRODUCT_PROPERTY_TYPE.MIN_GET_MONEY.getValue();
		System.out.println(productService);
		List<EbizProductProperty> ebizProductPropertys = productService.getProductPropertyByOrderType(productCode,"",orderType).getEbizProductPropertyList();
		System.out.println(ebizProductPropertys.size());
	}

	public static void main(String[] args) {
		BigDecimal bg = BigDecimal.valueOf(11);
		BigDecimal bg2 = BigDecimal.valueOf(25);
		BigDecimal[] result = bg2.divideAndRemainder(bg);
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
		BigDecimal bg3 = BigDecimal.valueOf(25);
		System.out.println("-----------------------------");
		// System.out.println(bg3.setScale(2));
		System.out.println(bg3.setScale(3, BigDecimal.ROUND_HALF_UP));
		System.out.println(bg3.setScale(2, BigDecimal.ROUND_HALF_UP));
		String message = "您本次赎回本金?surrenderInvest?元，收益?surrenderIncome?元，资产剩余本金?surplusMoney?元，收益?surplusIncome?元，资金将在T+1个工作日内转入您的账户，请注意查收。";
		String surrenderIncome = "100";
		String surrenderInvest = "200";
		String surplusMoney = "300";
		String surplusIncome = "400";
		message = message.replaceAll("[?]", "").replaceAll("surrenderIncome", surrenderIncome+"")
				.replaceAll("surrenderInvest", surrenderInvest+"")
				.replaceAll("surplusMoney", surplusMoney+"")
				.replaceAll("surplusIncome", surplusIncome+"");
		System.out.println(message);
		bg3 = null;
		System.out.println(bg3);
	}
	
	@Test
	public void testPublic() {
		logger.info("债权赎回资金划拨批处理开始");
		creditSurrenderService.dealSurrenderTransfer();
		creditSurrenderService.dealSurrenderTransferResult();
		logger.info("债权赎回资金划拨批处理结束");
	}

}
