package com.guohualife.ebiz.bpm.credit.check.bo;

import com.guohualife.ebiz.bpm.credit.check.dto.CheckRuleInputDTO;
import com.guohualife.ebiz.bpm.credit.check.dto.CheckRuleResultDTO;

public interface CheckBo {

	/**
     * 执行校验
     * 
     * @param checkInputDTO
     * @return
     * @throws Exception
     */
    public CheckRuleResultDTO check(CheckRuleInputDTO checkInputDTO) throws Exception;
	
}
