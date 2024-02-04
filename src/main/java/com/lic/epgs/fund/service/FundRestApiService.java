package com.lic.epgs.fund.service;

import java.util.Map;

import com.lic.epgs.common.dto.FundChangeDto;
import com.lic.epgs.common.dto.FundRequestDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.fund.dto.VouchereffectiveDto;
import com.lic.epgs.fund.dto.VouchereffectiveRequest;
import com.lic.epgs.integration.dto.CommonResponseDto;
import com.lic.epgs.integration.dto.InterestFundDto;
import com.lic.epgs.integration.dto.InterestFundResponseDto;
import com.lic.epgs.integration.dto.InterestRateResponseDto;
import com.lic.epgs.payout.dto.PayoutCheckerActionRequestDto;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterTempEntity;
import com.lic.epgs.policy.old.dto.PolicyDto;

/***
 * 
 * @author Muruganandam
 *
 */
public interface FundRestApiService {
	InterestRateResponseDto creditPolicy(InterestFundDto dto) throws ApplicationException;

	InterestRateResponseDto creditPolicyMembers(InterestFundDto dto) throws ApplicationException;

	InterestRateResponseDto viewByPolicyNo(String policyNo);

	InterestRateResponseDto viewMembersByPolicyNo(String policyNo);

	InterestRateResponseDto viewByMemberId(String memberId);

	InterestRateResponseDto viewHistoryPolicyNo(String policyNo);

	InterestRateResponseDto viewHistoryByMemberId(String memberId);

	InterestRateResponseDto viewMembersHistoryByPolicyNo(String policyNo);

	InterestRateResponseDto updatePolicyFund(FundChangeDto dto) throws ApplicationException;

	void setPolicyDetails(PolicyMasterTempEntity entity, InterestFundDto dto);

	void setPolicyDetails(PolicyMasterEntity sourceEntity, PolicyMasterEntity destinationEntity, FundChangeDto dto);

	InterestFundDto setContributionRequest(PolicyMasterTempEntity entity, String txnSubType);

	/***
	 * @throws ApplicationException
	 * @notes to invoke the fund API to process the claim for the given member
	 */
	InterestRateResponseDto processClaim(PayoutCheckerActionRequestDto request, TempPayoutEntity payoutEntity)
			throws ApplicationException;

	InterestRateResponseDto contributionAdjustmentForFund(PolicyDto policyDto, PolicyMasterTempEntity tempEntity,
			String contributionType) throws ApplicationException;

	InterestFundResponseDto viewMemberFundDetails(FundRequestDto requestDto) throws ApplicationException;

	CommonResponseDto<Map<String, Object>> contirbutionToTransEntries(FundRequestDto requestDto)
			throws ApplicationException;
	
	InterestFundResponseDto viewPolicyFundDetails(FundRequestDto requestDto) throws ApplicationException;

//	VouchereffectiveDto getCollectionData(String collectionId, String voucherEffective)throws ApplicationException;

	VouchereffectiveDto getCollectionData(VouchereffectiveRequest dto);

}
