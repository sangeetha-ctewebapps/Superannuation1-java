package com.lic.epgs.policyservicing.common.service;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceCommonResponseDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceStatusResponseDto;

/**
 * @author Muruganandam
 *
 */

public interface PolicyServicingCommonService {
	
	public String getSequence(String type);
	
	public PolicyServiceStatusResponseDto policyServicingStatus();

	public PolicyServiceCommonResponseDto checkService(Long policyId,String serviceType);
	
	public PolicyServiceCommonResponseDto initiateService(PolicyServiceDto policyServiceDto,String serviceType);
	

	
	public PolicyServiceCommonResponseDto startService(PolicyServiceDto policyServiceDto,String serviceType);

	public PolicyServiceCommonResponseDto endService1(Long policyId,Long serviceId);
	
	public PolicyServiceCommonResponseDto getServiceDetailsByServiceId(Long serviceId);
	
	public PolicyServiceCommonResponseDto getServiceDetailsByPolicyId(Long policyId);

	public PolicyServiceCommonResponseDto endService(PolicyServiceDto policyServiceDto, String serviceType);
			
//	public PolicyServiceCommonResponseDto generateServiceId(PolicyServiceDto policyServiceDto);

//	public PolicyServiceDto startServicecheck(PolicyServiceDto policyServiceDto,String serviceType);
	
	PolicyServiceCommonResponseDto generateServiceId(PolicyServiceDto policyLevelServiceDto);
	

	
	
}
