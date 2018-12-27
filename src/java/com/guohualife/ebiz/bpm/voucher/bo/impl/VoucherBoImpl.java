package com.guohualife.ebiz.bpm.voucher.bo.impl;

import java.util.Date;

import javax.annotation.Resource;

import com.guohualife.ebiz.bpm.voucher.bo.VoucherBo;
import com.guohualife.ebiz.bpm.voucher.dto.VoucherDTO;
import com.guohualife.ebiz.bpm.voucher.dto.response.DownloadVoucherResDTO;
import com.guohualife.edb.bpm.dao.impl.EbizElecVoucherDAOImpl;
import com.guohualife.edb.bpm.model.EbizElecVoucher;
import com.guohualife.platform.base.api.bo.implement.BaseBOImpl;

public class VoucherBoImpl extends BaseBOImpl implements VoucherBo {
	@Resource
	private EbizElecVoucherDAOImpl ebizElecVoucherDAOImpl;
	
	@Override
	public DownloadVoucherResDTO saveVoucher(VoucherDTO voucherDTO) {
		DownloadVoucherResDTO downloadVoucherResDTO = new DownloadVoucherResDTO();
		EbizElecVoucher ebizElecVoucher = new EbizElecVoucher();
		try{
			ebizElecVoucher.setOrderNo(voucherDTO.getOrderNo());
			ebizElecVoucher.setVoucherStatus(voucherDTO.getVoucherStatus());
			ebizElecVoucher.setToMail(voucherDTO.getToMail());
			ebizElecVoucher.setCreatedDate(new Date());
			ebizElecVoucher.setModifiedDate(new Date());
			ebizElecVoucher.setCreatedUser("交易中心");
			ebizElecVoucher.setModifiedUser("交易中心");
			ebizElecVoucher.setIsDelete((short)0);
		
			// save EbizVoucher
			logger.info("保存债权产品电子凭证ebizElecVoucherDAOImpl.insertSelective开始");
			Long voucherId = ebizElecVoucherDAOImpl.insertSelective(ebizElecVoucher);
			logger.info("保存债权产品电子凭证ebizElecVoucherDAOImpl.insertSelective，voucherId:" +voucherId+ ",voucherIdorderNO:"+voucherDTO.getOrderNo()+",ToMail:"+voucherDTO.getToMail());
		}catch (Exception e) {
			logger.info("保存债权产品电子凭证失败,,Exception:"+e);
		}
		return downloadVoucherResDTO;
		
	}
	
}
