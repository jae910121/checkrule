package com.guohualife.ebiz.bpm.base.bo;

import com.guohualife.ebiz.gateway.common.constants.ENUM_PARTNER_TYPE;

public interface BpmBaseBo{
	
	/**
	 * 根据订单号查询第三方类型
	 * 
	 * @param orderNo
	 * @return
	 */
	public ENUM_PARTNER_TYPE getThirdType(String orderNo);
	
	/**
	 * 发送MQ消息
	 * 
	 * @param topic
	 * @param key
	 * @param tag
	 * @param msgObj
	 */
	public void sendMessage(String topic, String key, String tag, Object msgObj);
	
}
