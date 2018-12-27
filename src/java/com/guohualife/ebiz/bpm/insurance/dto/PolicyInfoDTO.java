package com.guohualife.ebiz.bpm.insurance.dto;

import java.util.List;

import com.guohualife.edb.bpm.model.EbizPolicy;
import com.guohualife.edb.bpm.model.EbizPolicyBnf;
import com.guohualife.edb.bpm.model.EbizPolicyInsured;

/**
 * 保单信息DTO(查询返回)
 * 
 * @author wangxulu
 *
 */
public class PolicyInfoDTO {

	/**
	 * 保单信息
	 */
	private EbizPolicy ebizPolicy;
	
	/**
	 * 被保人列表信息
	 */
	private List<EbizPolicyInsured> ebizPolicyInsuredList;
	
	/**
	 * 受益人列表信息
	 */
	private List<EbizPolicyBnf> ebizPolicyBnfList;

	public EbizPolicy getEbizPolicy() {
		return ebizPolicy;
	}

	public void setEbizPolicy(EbizPolicy ebizPolicy) {
		this.ebizPolicy = ebizPolicy;
	}

	public List<EbizPolicyInsured> getEbizPolicyInsuredList() {
		return ebizPolicyInsuredList;
	}

	public void setEbizPolicyInsuredList(
			List<EbizPolicyInsured> ebizPolicyInsuredList) {
		this.ebizPolicyInsuredList = ebizPolicyInsuredList;
	}

	public List<EbizPolicyBnf> getEbizPolicyBnfList() {
		return ebizPolicyBnfList;
	}

	public void setEbizPolicyBnfList(List<EbizPolicyBnf> ebizPolicyBnfList) {
		this.ebizPolicyBnfList = ebizPolicyBnfList;
	}
	
}
