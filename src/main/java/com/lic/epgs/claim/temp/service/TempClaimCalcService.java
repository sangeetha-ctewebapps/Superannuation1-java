package com.lic.epgs.claim.temp.service;

import java.util.List;

import com.lic.epgs.claim.dto.ClaimAnnuityCalcDto;
import com.lic.epgs.claim.dto.ClaimCommutationCalcDto;
import com.lic.epgs.claim.dto.ClaimFundValueDto;
import com.lic.epgs.claim.dto.ClaimMbrFundValueDto;
import com.lic.epgs.claim.dto.ClaimMbrRequestDto;
import com.lic.epgs.claim.dto.ClaimPayeeBankDetailsDto;
import com.lic.epgs.claim.dto.NomineeTotalFundShared;
import com.lic.epgs.claim.dto.SurrenderChargeCalRequest;
import com.lic.epgs.claim.dto.SurrenderChargeCalResponse;
import com.lic.epgs.claim.temp.entity.TempClaimMbrNomineeEntity;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.FundedCommonMasterDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.surrender.dto.PolicySurrenderApiResponse;

public interface TempClaimCalcService {

	ApiResponseDto<ClaimFundValueDto> saveFundvalue(ClaimFundValueDto request) throws ApplicationException;

	ApiResponseDto<ClaimMbrFundValueDto> saveMbrFundvalue(ClaimMbrFundValueDto request);

	ApiResponseDto<ClaimCommutationCalcDto> saveCommutation(ClaimCommutationCalcDto request);

	ApiResponseDto<ClaimAnnuityCalcDto> saveAnuity(ClaimAnnuityCalcDto request);

	ApiResponseDto<ClaimPayeeBankDetailsDto> savePayeeBankDetails(ClaimPayeeBankDetailsDto claimPayeeBankDetailsDto);

	ApiResponseDto<ClaimFundValueDto> saveFundCalculationvalue(ClaimFundValueDto request);

	ApiResponseDto<ClaimFundValueDto> saveMbrFundCalculationvalue(NomineeTotalFundShared request);

	ApiResponseDto<ClaimCommutationCalcDto> saveCommutationCalculation(ClaimCommutationCalcDto request);

	ApiResponseDto<ClaimCommutationCalcDto> calAnnuityPurchase(ClaimMbrRequestDto claimMbrRequestDto);

	ApiResponseDto<TempClaimMbrNomineeEntity> calRefreshByNominee(ClaimMbrRequestDto claimMbrRequestDto);

	ApiResponseDto< List<FundedCommonMasterDto>> getFundPayableDetails(Long codeId, String modeOfExit);
	

//	ApiResponseDto<ClaimPayeeBankDetailsDto> getClaimBankDetails(ClaimFundPayableRequestDto claimFundPayableRequestDto);

	ApiResponseDto<ClaimPayeeBankDetailsDto> getClaimBankDetails(Long mphId, Long memberId, Long nomineeId, String claimNo);

	ApiResponseDto<ClaimFundValueDto> reCalculateFund(ClaimFundValueDto claimFundValue) throws ApplicationException;

	ApiResponseDto<SurrenderChargeCalResponse>  surrenderPayoutCalculation(SurrenderChargeCalRequest surrenderChargeCalRequest) throws ApplicationException;

	ApiResponseDto<SurrenderChargeCalResponse> marketValuePayoutCalculation(SurrenderChargeCalRequest surrenderChargeCalRequest) throws ApplicationException;

	ApiResponseDto<String> getGsaGnPolicyConvertToWithdrawral(Long claimId, String Type);



}