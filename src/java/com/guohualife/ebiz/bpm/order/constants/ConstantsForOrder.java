package com.guohualife.ebiz.bpm.order.constants;



public class ConstantsForOrder {

	/**
	 * 订单产品类型类别
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_ORDER_PRODUCT_TYPE{
		
	    INSURANCE("01","保险产品"),
	    CREDIT("02","债券产品");
	    
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_ORDER_PRODUCT_TYPE(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	    public static ENUM_ORDER_PRODUCT_TYPE getEnum(String value) {
            for (ENUM_ORDER_PRODUCT_TYPE e : ENUM_ORDER_PRODUCT_TYPE.values()) {
                if (value.equals(e.getValue())) {
                    return e;
                }
            }
            return null;
        }
	    
	}

	/**
	 * 业务类型
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_BUSINESS_TYPE{
		UNDERWRITE("1","核保"),
		ACCEPTINSURANCE("2","承保"),
		APPLYFORPURCHASE("3","申购");
		
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_BUSINESS_TYPE(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}
	

	/**
	 * 订单状态
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_ORDER_STATUS{
		
	    WAITCONFIRM("01","待确认"),
	    WAITPAY("02","待付款"),
	    PAYING("03","支付中"),
	    DEAL("04","订单成交"),
	    REDEEMING("05","赎回中"),
	    REDEEMALL("06","订单全部赎回"),
	    REFUNDING("07","退款中"),
	    REFUND("08","订单退款"),
	    CLOSE("09","订单关闭"),
	    CANCEL("10", "订单撤销");
	    
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_ORDER_STATUS(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}
	
	/**
	 * 保单列表查询类型
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_ORDER_QUERY_TYPE{
		
	    EFFECTIVE("effective","查询有效订单");
	    
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_ORDER_QUERY_TYPE(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	    public static String getNameByValue(String value){
	    	String name = "";
	    	for(ENUM_ORDER_QUERY_TYPE e : ENUM_ORDER_QUERY_TYPE.values()){
	    		if(e.getValue().equals(value)){
	    			name = e.getName();
	    			break;
	    		}
	    	}
	    	return name;
	    }
	    
	}
	
	/**
	 * 保单详细信息查询类型
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_ORDER_DETAIL_QUERY_TYPE{
		
	    QUERY_BASIC_INFO("1","查询基本信息"),
	    QUERY_CONTAINBUSINESS_INFO("2","查询基本信息和业务信息");
	    
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_ORDER_DETAIL_QUERY_TYPE(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	    public static String getNameByValue(String value){
	    	String name = "";
	    	for(ENUM_ORDER_DETAIL_QUERY_TYPE e : ENUM_ORDER_DETAIL_QUERY_TYPE.values()){
	    		if(e.getValue().equals(value)){
	    			name = e.getName();
	    			break;
	    		}
	    	}
	    	return name;
	    }
	    
	}
	
	/**
	 * 订单赎回状态
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_SURRENDER_STATUS{
		
		ACCEPTAUDIT("0","受理审核中"),
		ACCEPTSUCCESS("1","受理成功"),
		ACCEPTFAIL("2","受理失败"),
		ACCEPTCANCEL("3","受理申请作废"),
		INFORM("4","待通知产品商户");
	    
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_SURRENDER_STATUS(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	    public static String getNameByValue(String value){
	    	String name = "";
	    	for(ENUM_SURRENDER_STATUS e : ENUM_SURRENDER_STATUS.values()){
	    		if(e.getValue().equals(value)){
	    			name = e.getName();
	    			break;
	    		}
	    	}
	    	return name;
	    }
	    
	}
	
	/**
	 * 赎回类型
	 * 
	 * @author zhangyl
	 *
	 */
	public static enum ENUM_SURRENDER_TYPE{
		REDEEM("1","赎回"),
		REFUND("2","退款（保险产品指犹豫期退保）"),
		SPECIALREDEEM("3","特殊赎回（保险产品指退保）"),
		AUTOREDEEM("4", "到期自动赎回(债权产品)");
		
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_SURRENDER_TYPE(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}
	
	/**
	 * 资金划拨状态
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_SURRENDER_TRANSFER_STATUS{
		WAIT_TRNASFER("01","待划拨"),
		TRNASFERING("02","划拨中"),
		TRNASFER_SUCCESS("03","划拨成功"),
		TRNASFER_FAILED("04","划拨失败");
		
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_SURRENDER_TRANSFER_STATUS(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}
}
