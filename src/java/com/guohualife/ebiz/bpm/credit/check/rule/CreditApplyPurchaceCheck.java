package com.guohualife.ebiz.bpm.credit.check.rule;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.guohualife.ebiz.bpm.credit.check.constant.ConstantsForCheckRule.CHECK_RESULT;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.bpm.order.dto.OrderListQueryDTO;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.platform.common.api.context.SpringContext;

/**
 * 
 * 债权购买校验
 * 
 * @history add by hezg_cyd
 * @version 1.0
 */
public class CreditApplyPurchaceCheck {
	private static final Log logger = LogFactory.getLog(CreditApplyPurchaceCheck.class);


	/**
	 * 校验单笔交易额度
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static final String checkOrderAmount(List<Object> params,List<Object> limits) {
		String result = CHECK_RESULT.SUCCESS.getValue();
		//处理得到的传参
		String minAmnt = (String) limits.get(0);
		String maxAmnt = (String) limits.get(1);
		HashMap<String, Object> paramMap = (HashMap<String, Object>) params.get(0);
		BigDecimal orderAmount = new BigDecimal(paramMap.get("orderAmount")+"");
		if (orderAmount.compareTo(new BigDecimal(minAmnt)) < 0) {
			logger.info("单笔交易最小额度有误!");
			result = CHECK_RESULT.FAIL.getValue();
		} else {
			logger.info("单笔交易最小额度成功！");
		}
		// 传入数值大于限制值时判断有没有上传
		if (orderAmount.compareTo(new BigDecimal(maxAmnt)) > 0) {
			logger.info("单笔交易最大额度有误!");
			result = CHECK_RESULT.FAIL.getValue();
		} else {
			logger.info("单笔交易最大额度成功！");
		}
		return result;
	}
	
	/**
	 * 校验客户累计金额(包含本单) 
	 *
	 * @throws ParseException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static final String checkSumMoney(List<Object> params,
			List<Object> limits) throws ParseException {
		String result = CHECK_RESULT.SUCCESS.getValue();
		// 处理得到的传参
		BigDecimal limAmnt = new BigDecimal((String)limits.get(0));
		HashMap<String, Object> paramMap = (HashMap<String, Object>) params.get(0);
		OrderListQueryDTO orderListQueryDTO = new OrderListQueryDTO();
		Long customerId= new Long(paramMap.get("customerId") + "");
		BigDecimal orderAmount = new BigDecimal(paramMap.get("orderAmount") + "");
		String productCode = new String(paramMap.get("productCode")+"");
		orderListQueryDTO.setCustomerId(customerId);
		List<String> productCodeList = new ArrayList<String>();
		productCodeList.add(productCode);
		orderListQueryDTO.setProductCodeList(productCodeList);

		List<String> statusList = new ArrayList<String>();
		statusList.add(ConstantsForOrder.ENUM_ORDER_STATUS.PAYING.getValue());
		statusList.add(ConstantsForOrder.ENUM_ORDER_STATUS.DEAL.getValue());
		orderListQueryDTO.setStatusList(statusList);
		OrderBo orderBo = (OrderBo)SpringContext.getBean("orderBoImpl");
		List<EbizOrder> ebizOrders = orderBo.getOrderList(orderListQueryDTO);
		if(ebizOrders != null && ebizOrders.size() > 0){
			for(EbizOrder ebizOrder : ebizOrders){
				orderAmount = orderAmount.add(ebizOrder.getOrderAmount());
			}
		}
		
		if(orderAmount.compareTo(limAmnt) > 0 ){
			logger.info(customerId + "的累计购买金额为:" + orderAmount + ",累计购买金额不得超过" + limAmnt + "万");
			return CHECK_RESULT.FAIL.getValue();
		}
		return result;
	}
}
