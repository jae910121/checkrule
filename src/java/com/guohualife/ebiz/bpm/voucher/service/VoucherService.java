package com.guohualife.ebiz.bpm.voucher.service;

import javax.jws.WebService;

import com.guohualife.ebiz.bpm.voucher.dto.request.DownloadVoucherReqDTO;
import com.guohualife.ebiz.bpm.voucher.dto.response.DownloadVoucherResDTO;
import com.guohualife.platform.common.api.exception.ServiceException;

/**
 * 债权产品电子凭证下载Service接口
 * 
 * @author zhoudd
 * 
 */
@WebService
public interface VoucherService {

	/**
	 * 发送电子凭证数据，保存发送数据至数据库
	 * 
	 * @param ebizSurrender
	 */
	public DownloadVoucherResDTO downloadVoucher(
			DownloadVoucherReqDTO elecVoucherReqDTO) throws ServiceException;

}
