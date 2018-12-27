package com.guohualife.ebiz.bpm.order.bo;

import java.util.Date;
import java.util.List;

import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.bpm.order.dto.OrderAccountDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderListQueryDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderSuccessDTO;
import com.guohualife.ebiz.bpm.order.dto.request.OrderReqDTO;
import com.guohualife.ebiz.bpm.order.dto.response.OrderResDTO;
import com.guohualife.ebiz.customer.account.dto.CustomerQryResultDTO;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizOrderAccount;
import com.guohualife.edb.bpm.model.EbizSurrender;
import com.guohualife.platform.common.api.exception.ServiceException;

/**
 * 订单Bo接口
 * 
 * @author wangxulu
 *
 */
public interface OrderBo {

	/**
	 * 保存订单信息
	 * 
	 * @param orderDTO
	 * @return
	 * @throws ServiceException
	 */
	public String saveOrder(OrderDTO orderDTO) throws ServiceException;
	
	/**
	 * 申请订单支付
	 * 
	 * @param orderAccountDTO
	 * @throws ServiceException
	 */
	public void applyOrderPay(OrderAccountDTO orderAccountDTO)
			throws ServiceException;
	
	/**
	 * 根据订单号查询订单表信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public EbizOrder getOrder(String orderNo);
	
	/**
	 * 根据订单号查询订单账户表信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public EbizOrderAccount getOrderAccount(String orderNo);
	
	/**
	 * 查询订单信息
	 * 
	 * @param orderReqDTO
	 * @param orderResDTO
	 * @return
	 */
	public void getOrderInfo(OrderReqDTO orderReqDTO,
			OrderResDTO orderResDTO);
	
	/**
	 * 查询订单列表
	 * 
	 * @param orderListQueryDTO
	 * @return
	 */
	public List<EbizOrder> getOrderList(OrderListQueryDTO orderListQueryDTO);
	
	/**
	 * 更新订单状态
	 * 
	 * @param orderNo
	 * @param status
	 */
	public void updateOrderStatus(String orderNo, String status);
	
	/**
	 * 校验购买客户是否存在
	 * 
	 * @param customerId
	 * @return
	 */
	public CustomerQryResultDTO getCustomerInfo(Long customerId);
	
	/**
	 * 根据客户ID和查询类型查询订单列表
	 * 
	 * @param customerId
	 * @param queryType
	 * @return
	 */
	public List<EbizOrder> getOrderByCustomerId(Long customerId,
			ConstantsForOrder.ENUM_ORDER_QUERY_TYPE queryType);
	
	/**
	 * 保存/更新订单表
	 * 
	 * @param ebizOrder
	 * @return
	 */
	public String saveOrUpdateOrder(EbizOrder ebizOrder);
	
	/**
	 * 订单成交处理
	 * 
	 * @param orderSuccessDTO
	 */
	public void dealOrderSuccess(OrderSuccessDTO orderSuccessDTO);
	
	/**
	 * 订单支付失败处理
	 * 
	 * @param orderNo
	 */
	public void dealOrderFail(String orderNo);
	
	/**
	 * 订单赎回成功发送消息
	 * 
	 * @param ebizOrder
	 * @param firstBuy
	 */
	public void sendMsgForSurrenderSuccess(EbizOrder ebizOrder, EbizSurrender ebizSurrender);
	
	/**
	 * 获取起息日
	 * 
	 * @param productCode
	 * @param orderType
	 * @param successDate
	 * @return
	 */
	public Date getValueDate(String productCode, String orderType,
			Date successDate);
	
	/**
	 * 获取订单到期时间
	 * 
	 * @param productCode
	 * @param orderType
	 * @param valueDate
	 * @return
	 */
	public Date getExpiryDate(String productCode, String orderType,
			Date valueDate);
	
	/**
	 * 渠道检查
	 * 
	 * @param creditApplyPurchaseReqDTO
	 * @return
	 */
	public boolean checkOrderType(String productCode, String orderType);
	
	/**
	 * 订单成交发送消息
	 * 
	 * @param ebizOrder
	 * @param firstBuy
	 */
	public void sendMsgForOrderSuccess(EbizOrder ebizOrder);
}
