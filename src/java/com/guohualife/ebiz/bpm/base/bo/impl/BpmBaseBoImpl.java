package com.guohualife.ebiz.bpm.base.bo.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.guohualife.common.util.mq.client.MQProducer;
import com.guohualife.common.util.mq.dto.MQMessageDTO;
import com.guohualife.ebiz.bpm.base.bo.BpmBaseBo;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.gateway.common.constants.ENUM_PARTNER_TYPE;
import com.guohualife.ebiz.product.dto.EbizProductDTO;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.platform.base.api.bo.implement.BaseBOImpl;
import com.guohualife.platform.common.api.exception.ServiceException;
import com.guohualife.platform.common.api.util.JsonUtil;

@Component
public class BpmBaseBoImpl extends BaseBOImpl implements BpmBaseBo{

	@Resource
	private ProductService productService;
	
	@Resource
	OrderBo orderBo;
	
	@Resource(name = "MQProducer")
	private MQProducer producer;
	
	/**
	 * 根据订单号查询第三方类型
	 * 
	 * @param orderNo
	 * @return
	 */
	public ENUM_PARTNER_TYPE getThirdType(String orderNo){
		EbizOrder ebizOrder = orderBo.getOrder(orderNo);
		EbizProductDTO ebizProductDTO = productService.getProductByProductCode(ebizOrder.getProductCode());
		if(ENUM_PARTNER_TYPE.GUOHUA.getValue().equals(ebizProductDTO.getProductChannel())){
			return ENUM_PARTNER_TYPE.GUOHUA;
		}
		return null;
		
	}

	/**
	 * 发送MQ消息
	 * 
	 * @param topic
	 * @param key
	 * @param msgBody
	 */
	public void sendMessage(String topic, String key, String tag, Object msgObj) {
		MQMessageDTO message = new MQMessageDTO();
        message.setTopic(topic);
        message.setTag(tag);
        message.setKey(key);
		try {
			message.setMessage(JsonUtil.writeValue(msgObj));
			logger.info("开始发送消息topic：" + topic + " key：" + key + "，消息内容：" + JsonUtil.writeValue(msgObj));
			producer.send(message);
		} catch (Exception e) {
			logger.error("消息topic：" + topic + " key：" + key + "发送出现异常", e);
			throw new ServiceException("消息发送出现异常");
		}	
	}
	
	public static void main(String args[]) {
        System.out.println((System.currentTimeMillis() - new Date().getTime()) / (60 * 1000));
    }
	
}
