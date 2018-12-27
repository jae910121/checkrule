package com.guohualife.ebiz.bpm.voucher.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.base.constants.Constants;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.voucher.bo.VoucherBo;
import com.guohualife.ebiz.bpm.voucher.constants.ConstantsForVoucher.ENUM_VOUCHER_PRODUCT_TYPE;
import com.guohualife.ebiz.bpm.voucher.constants.ConstantsForVoucher.ENUM_VOUCHER_STATUS;
import com.guohualife.ebiz.bpm.voucher.dto.VoucherDTO;
import com.guohualife.ebiz.bpm.voucher.dto.request.DownloadVoucherReqDTO;
import com.guohualife.ebiz.bpm.voucher.dto.response.DownloadVoucherResDTO;
import com.guohualife.ebiz.bpm.voucher.service.VoucherService;
import com.guohualife.ebiz.customer.account.dto.base.CustomerInfoDTO;
import com.guohualife.ebiz.message.dto.MessageInfoDTO;
import com.guohualife.ebiz.message.service.MessageService;
import com.guohualife.ebiz.product.dto.EbizProductDTO;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.platform.common.api.exception.ServiceException;
import com.guohualife.platform.common.api.util.CollectionUtil;
import com.guohualife.platform.common.api.util.XmlUtil;

/**
 * 债权产品电子凭证下载Service接口实现类
 * 
 * @author zhoudd
 * 
 */
@Service
public class VoucherServiceImpl  implements VoucherService{
	private static final Log logger = LogFactory
		.getLog(VoucherServiceImpl.class);

	@Resource
	private OrderBo orderBo;
	
	@Resource
	private ProductService productService;

	@Resource
	private VoucherBo voucherBo;
	
	@Resource
	private MessageService messageService;
	
	// 邮件模板
	public static final String TEMPLATECODE = "MAIL_ELECTRONIC_YQ_TRADE_VOUCHER";
	// 购买成功标识  
	public static final String RESULTCODEFLAG = "1";
	// 
	public static final String ORDERSTATUSDESC = "支付成功";
	public static final String PRODUCTTYPE1 = "保险理财";// 01
	public static final String PRODUCTTYPE2 = "债权、债权收益权转让";//02
	@Override
	public DownloadVoucherResDTO downloadVoucher(
			DownloadVoucherReqDTO downloadVoucherReqDTO) throws ServiceException {
		logger.info("债权产品电子凭证下载接口downloadVoucher入参DownloadVoucherReqDTO：\n"+ XmlUtil.toXml(downloadVoucherReqDTO, new Class[] {}));

		DownloadVoucherResDTO downloadVoucherResDTO = new DownloadVoucherResDTO();
		String errorMessage = "";
		if (downloadVoucherReqDTO.getToUser() == null){
			errorMessage = "发送邮箱为空";
			downloadVoucherResDTO.setResultCode(Constants.FAILED);
			downloadVoucherResDTO.setResultMessage(errorMessage);
			logger.info("债权产品电子凭证下载接口必填字段校验不通过，错误信息：" + errorMessage);
			return downloadVoucherResDTO;
		}
		if (downloadVoucherReqDTO.getOrderNo() == null){
			errorMessage = "订单号为空";
			downloadVoucherResDTO.setResultCode(Constants.FAILED);
			downloadVoucherResDTO.setResultMessage(errorMessage);
			logger.info("债权产品电子凭证下载接口必填字段校验不通过，错误信息：" + errorMessage);
			return downloadVoucherResDTO;
		}
		/*
		 * 获取邮件参数
		 */
		String realName = "";
		Date successDate = null;
		String productName = "";
		String orderNo = "";
		String productDesc = "";
		BigDecimal orderAmount = null;
		Date valueDate = null;
		// 入参
		String toUser = downloadVoucherReqDTO.getToUser();
		orderNo = downloadVoucherReqDTO.getOrderNo();
		// 根据orderNo 获取 ebizOrder
		EbizOrder ebizOrder = orderBo.getOrder(orderNo);
		if (ebizOrder == null) {
			errorMessage = "订单信息为空";
			downloadVoucherResDTO.setResultCode(Constants.FAILED);
			downloadVoucherResDTO.setResultMessage(errorMessage);
			logger.info("债权产品电子凭证下载接口根据订单编号获取订单信息失败，错误信息：" + errorMessage);
			return downloadVoucherResDTO;
		}
		Long customerId = ebizOrder.getCustomerId();
		successDate = ebizOrder.getSuccessDate();
		orderAmount = ebizOrder.getOrderAmount();
		valueDate = ebizOrder.getValueDate();
		List<CustomerInfoDTO> customerInfoList = orderBo.getCustomerInfo(customerId).getCustomerDTOList();
		if (CollectionUtil.isEmpty(customerInfoList)) {
			errorMessage = "客户信息为空";
			downloadVoucherResDTO.setResultCode(Constants.FAILED);
			downloadVoucherResDTO.setResultMessage(errorMessage);
			logger.info("债权产品电子凭证下载接口根据customerId获取客户信息失败，错误信息：" + errorMessage);
			return downloadVoucherResDTO;
		}
		realName = customerInfoList.get(0).getRealName();
		// 根据查产品代码查询产品名称
		EbizProductDTO ebizProductDTO = productService.getProductByProductCode(ebizOrder.getProductCode());
		productName = ebizProductDTO.getProductName();
		productDesc = ENUM_VOUCHER_PRODUCT_TYPE.getEnum(ebizOrder.getProductType()).getName();
		
		// 调用message 发送消息
		sendMessageByTemplateCode(downloadVoucherResDTO, realName,
				successDate, productName, orderNo, productDesc, orderAmount,
				valueDate, ORDERSTATUSDESC, toUser);
		// resultCode购买成功标识  1代表成功
		if (Constants.SUCCESS.equals(downloadVoucherResDTO.getResultCode())) {
			// 保存数据
			VoucherDTO voucherDTO = new VoucherDTO();
			voucherDTO.setOrderNo(orderNo);
			voucherDTO.setVoucherStatus(ENUM_VOUCHER_STATUS.ALREADYSEND.getValue());
			voucherDTO.setToMail(downloadVoucherReqDTO.getToUser());
			voucherBo.saveVoucher(voucherDTO);
		}
		
		logger.info("债权产品电子凭证下载接口downloadVoucher出参DownloadVoucherReqDTO：\n"+ XmlUtil.toXml(downloadVoucherReqDTO, new Class[] {}));
		return downloadVoucherResDTO;
	}
	
	/**
	 * 调用message 发送消息
	 * @param downloadVoucherResDTO
	 * @param realName
	 * @param successDate
	 * @param productName
	 * @param orderNo
	 * @param productDesc
	 * @param orderAmount
	 * @param valueDate
	 * @param orderStatusDesc
	 * @param toUser
	 */
	private void sendMessageByTemplateCode(
			DownloadVoucherResDTO downloadVoucherResDTO, String realName,
			Date successDate, String productName, String orderNo,
			String productDesc, BigDecimal orderAmount, Date valueDate,
			String orderStatusDesc, String toUser) {
		MessageInfoDTO messageinfodto = new MessageInfoDTO();
		messageinfodto.setToUser(toUser);
		messageinfodto.setTemplateCode(TEMPLATECODE);
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("realName", realName);
		paramMap.put("realName", realName);
		String successDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(successDate);
		String valueDateStr = new SimpleDateFormat("yyyy-MM-dd").format(valueDate);
		paramMap.put("successDate", successDateStr);
		paramMap.put("valueDate", valueDateStr);
		paramMap.put("productName", productName);
		paramMap.put("orderNo", orderNo);
		paramMap.put("productDesc", productDesc);
		paramMap.put("orderAmount", orderAmount.setScale(2).toString());
		paramMap.put("orderStatusDesc", orderStatusDesc);
		messageinfodto.setParamMap(paramMap);
		try {
			logger.info("债权产品电子凭证下载VoucherServiceImpl调用messageService.sendMessageByTemplateCode发送邮件开始 orderNo:" + orderNo);
			messageService.sendMessageByTemplateCode(messageinfodto);
			logger.info("债权产品电子凭证下载VoucherServiceImpl调用messageService.sendMessageByTemplateCode发送邮件成功orderNo:" + orderNo);
			downloadVoucherResDTO.setResultCode("1");
		} catch (Exception e) {
			downloadVoucherResDTO.setResultCode("0");
			downloadVoucherResDTO.setResultMessage("系统繁忙，请稍后再试！");
			logger.info("债权产品电子凭证下载VoucherServiceImpl调用messageService.sendMessageByTemplateCode发送邮件失败,Exception:"+e);
		}
	}

}
