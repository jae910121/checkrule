package com.guohualife.ebiz.bpm.voucher.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.guohualife.ebiz.bpm.base.BaseTester;
import com.guohualife.ebiz.bpm.voucher.dto.request.DownloadVoucherReqDTO;
import com.guohualife.ebiz.bpm.voucher.dto.response.DownloadVoucherResDTO;
import com.guohualife.platform.common.api.util.XmlUtil;

public class VoucherServiceTest extends BaseTester{
	
	@SuppressWarnings("rawtypes")
	private Class[] clazz = {String.class, List.class, DownloadVoucherResDTO.class};
	
	@Resource
	private VoucherService voucherService;
	
	@Test
	public void testGetVoucher(){
		DownloadVoucherReqDTO downloadVoucherReqDTO = new DownloadVoucherReqDTO();
		downloadVoucherReqDTO.setOrderNo("20150617020100878670");
		downloadVoucherReqDTO.setToUser("123@qq.com");
		DownloadVoucherResDTO downloadVoucherResDTO = voucherService.downloadVoucher(downloadVoucherReqDTO);
		System.out.println("查询订单基本信息：\n" + XmlUtil.toXml(downloadVoucherResDTO, clazz));
	}
	

}
