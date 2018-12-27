package com.guohualife.ebiz.bpm.asset.constants;



public class ConstantsForAsset {
	
	/**
	 * 资产变更类型
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_ASSET_MODIFY_TYPE{
		ASSET_SYNC("1","资产同步"),
		SURRENDER("2","赎回"),
		ORDER_DEAL("3", "订单成交"),
		POLICY_HESITATE_SURRRENDER("4", "保单犹豫期赎回");
		
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_ASSET_MODIFY_TYPE(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	    public static String getNameByValue(String value) {
            for (ENUM_ASSET_MODIFY_TYPE e : ENUM_ASSET_MODIFY_TYPE.values()) {
                if (value.equals(e.getValue())) {
                    return e.getName();
                }
            }
            return null;
        }
	    
	}
	
	/**
	 * 资产变更类型
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_ASSET_STATUS{
		EFFECTIVE("01","有效"),
		SURRENDER_INVEST_ALL("02","本金全部赎回");
		
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_ASSET_STATUS(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}
	
	/**
	 * 资产结算状态
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_ASSET_BALANCE_STATUS{
		PROCESSING("01","处理中"),
		SUCCESS("02","处理成功"),
		FAILED("03","处理失败"),
		HAS_EXCEPTION("04","处理有异常");
		
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_ASSET_BALANCE_STATUS(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}
	
	/**
	 * 资产结算类型
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_ASSET_BALANCE_TYPE{
		INSURANCE("01","保险类产品"),
		CREDIT("02","债券类产品");
		
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_ASSET_BALANCE_TYPE(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}
}
