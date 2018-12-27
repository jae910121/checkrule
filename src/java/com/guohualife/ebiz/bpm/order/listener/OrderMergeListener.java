package com.guohualife.ebiz.bpm.order.listener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.guohualife.common.util.mq.constant.ConsumerResult;
import com.guohualife.common.util.mq.context.MQConsumerContext;
import com.guohualife.common.util.mq.dto.MQMessageDTO;
import com.guohualife.common.util.mq.listener.MQMessageListener;
import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.bo.OrderSurrenderBo;
import com.guohualife.ebiz.customer.account.dto.CustomerMergeDetailDTO;
import com.guohualife.ebiz.customer.account.dto.CustomerMergeDetailDataDTO;
import com.guohualife.ebiz.customer.account.dto.CustomerMergeUserDTO;
import com.guohualife.ebiz.customer.account.service.CustomerAccountService;
import com.guohualife.ebiz.customer.common.constant.ConstantForCustomer.ENUM_CUS_MERGEDETAIL_TYPE;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.platform.common.api.util.JsonUtil;

@Component
public class OrderMergeListener implements MQMessageListener {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private OrderBo orderBo;
	
	@Resource
	private AssetBo assetBo;
	
	@Resource
	private OrderSurrenderBo orderSurrenderBo;
	
	@Resource
	private CustomerAccountService customerAccountService;
	
	public ConsumerResult consumeMessage(MQMessageDTO message,
			MQConsumerContext arg1) {

		try{
			logger.info("接收消息，Key:" + message.getKey() + "，topic:"
					+ message.getTopic() + "，MsgId:" + message.getMsgId() + "开始处理");  
			
			String body = (String)message.getMessage();
			logger.info("消息内容：" + body);
			CustomerMergeDetailDTO customerMergeDetailDTO = 
					(CustomerMergeDetailDTO) JsonUtil.readValue(body, CustomerMergeDetailDTO.class);
			
			Long mainId = customerMergeDetailDTO.getMainId();
			List<CustomerMergeUserDTO> customerMergeUsers = customerMergeDetailDTO.getMergeDetailUserLst();
			
			if(customerMergeUsers.size() == 0){
				logger.info("待合并客户数为0");
			}
			
			for(CustomerMergeUserDTO customerMergeUserDTO : customerMergeDetailDTO.getMergeDetailUserLst()){
				Long customerId = customerMergeUserDTO.getSubId();
				
				List<EbizOrder> ebizOrders = orderBo.getOrderByCustomerId(customerId, null);
				if(ebizOrders.size() == 0){
					logger.info("待合并订单数为0");
				}
				
				List<CustomerMergeDetailDataDTO> customerMergeDetailDatas = new ArrayList<CustomerMergeDetailDataDTO>();
				
				for(EbizOrder ebizOrder : ebizOrders){
					String orderNo = ebizOrder.getOrderNo();
					logger.info("合并订单" + orderNo + "从"
							+ ebizOrder.getCustomerId() + "至" + mainId);
					ebizOrder.setCustomerId(mainId);
					orderBo.saveOrUpdateOrder(ebizOrder);
					
					EbizAsset ebizAsset = assetBo.getAssetByOrderNo(orderNo);
					ebizAsset.setCustomerId(mainId);
					assetBo.saveOrUpdateAsset(ebizAsset);
					
					CustomerMergeDetailDataDTO customerMergeDetailDataDTO = new CustomerMergeDetailDataDTO();
					customerMergeDetailDataDTO.setDataType(ENUM_CUS_MERGEDETAIL_TYPE.MERGEORDER.getValue());
					customerMergeDetailDataDTO.setDataNo(orderNo);
					customerMergeDetailDatas.add(customerMergeDetailDataDTO);
				}
				
				customerMergeUserDTO.setMergeDataDTOLst(customerMergeDetailDatas);
			}
			
			long start = System.currentTimeMillis();
			logger.info("调用customer模块mergeYiqianJinfuData接口开始");
			customerAccountService.saveCustomerMergeDetail(customerMergeDetailDTO);
			logger.info("调用customer模块mergeYiqianJinfuData接口结束，耗时："
					+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
			
			logger.info("合并订单消息处理结束");
			
		}catch(Exception e){
			logger.info("合并订单消息处理异常", e);
			return ConsumerResult.RECONSUME_LATER;
		}
		
		return ConsumerResult.CONSUMER_SUCCESS;
	}

}
