package com.guohualife.ebiz.bpm.voucher.constants;



/**
 * @author zhoudd_cyd
 *
 */
public class ConstantsForVoucher {
	
	/**
	 * 产品类型
	 * @author zhoudd_cyd
	 *
	 */
	public static enum ENUM_VOUCHER_PRODUCT_TYPE{
		
	    INSURANCE("01","保险理财"),
	    CREDIT("02","债权、债权收益权转让");
	    
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_VOUCHER_PRODUCT_TYPE(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	    public static ENUM_VOUCHER_PRODUCT_TYPE getEnum(String value) {
            for (ENUM_VOUCHER_PRODUCT_TYPE e : ENUM_VOUCHER_PRODUCT_TYPE.values()) {
                if (value.equals(e.getValue())) {
                    return e;
                }
            }
            return null;
        }
	    
	}
	
	
public static enum ENUM_VOUCHER_STATUS{
		
	    WAITSEND("0","待发送"),
	    ALREADYSEND("1","已发送");
	    
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_VOUCHER_STATUS(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	    public static ENUM_VOUCHER_STATUS getEnum(String value) {
            for (ENUM_VOUCHER_STATUS e : ENUM_VOUCHER_STATUS.values()) {
                if (value.equals(e.getValue())) {
                    return e;
                }
            }
            return null;
        }
	    
	}
	

}
