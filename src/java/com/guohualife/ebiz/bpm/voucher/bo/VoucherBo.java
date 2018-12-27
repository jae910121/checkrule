package com.guohualife.ebiz.bpm.voucher.bo;

import com.guohualife.ebiz.bpm.voucher.dto.VoucherDTO;
import com.guohualife.ebiz.bpm.voucher.dto.response.DownloadVoucherResDTO;

public interface VoucherBo {
	/**
	 * 保存债权产品电子凭证
	 * @param voucherDTO
	 * @return
	 */
	public DownloadVoucherResDTO saveVoucher(VoucherDTO voucherDTO);
}
