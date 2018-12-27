package com.guohualife.ebiz.bpm.credit.check.dto;


/**
 * 校验规则结果DTO
 * 
 * @author wangxulu
 *
 */
public class CheckRuleResultDTO {

	/**
	 * 校验结果编码
	 * 0-失败 1-成功 2-需其它处理
	 */
	private String resultCode;
	
	/**
	 * 校验结果信息
	 */
	private String resultMessage;
	

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
}
