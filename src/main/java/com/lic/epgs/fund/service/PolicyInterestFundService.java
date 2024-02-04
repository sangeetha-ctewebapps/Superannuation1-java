/**
 * 
 */
package com.lic.epgs.fund.service;

import java.util.List;

import com.lic.epgs.common.dto.InterestRequestDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.integration.dto.InterestErrorDto;
import com.lic.epgs.integration.dto.InterestRateResponseDto;

/**
 * @author Muruganandam
 *
 */
public interface PolicyInterestFundService {
	InterestRateResponseDto creditPolicy(String policyNo) throws ApplicationException;

//	InterestRateResponseDto creditPolicy(PolicyEntity entity) throws ApplicationException;

	InterestRateResponseDto creditPolicyMembers(String policyNo);

//	InterestRateResponseDto creditPolicyMembers(PolicyEntity entity);

	InterestRateResponseDto creditPolicyMember(String policyNo, String memberId);

//	InterestRateResponseDto creditPolicyMember(PolicyEntity entity, String memberId) throws ApplicationException;

//	InterestRateResponseDto creditPolicyMembersByMembershipIds(PolicyEntity entity, List<String> memberIds)throws ApplicationException;

	InterestRateResponseDto creditBulkPolicyMember(InterestRequestDto requestDto) throws ApplicationException;

	InterestRateResponseDto debitPolicy(String policyNo) throws ApplicationException;

	InterestRateResponseDto debitPolicyMemberById(String policyNo, String memberId) throws ApplicationException;

	public InterestRateResponseDto debitMembersByPolicy(String policyNo);

	InterestRateResponseDto debitBulkPolicyMember(InterestRequestDto requestDto) throws ApplicationException;

	void saveErrorDetails(List<InterestErrorDto> errorDtos);

}
