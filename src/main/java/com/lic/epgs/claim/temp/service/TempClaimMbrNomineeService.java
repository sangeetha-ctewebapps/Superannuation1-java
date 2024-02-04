package com.lic.epgs.claim.temp.service;

import java.util.List;

import com.lic.epgs.claim.dto.ClaimAppointeeDto;
import com.lic.epgs.claim.dto.ClaimMbrNomineeDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempClaimMbrNomineeService {

	ApiResponseDto<ClaimMbrNomineeDto> save(ClaimMbrNomineeDto docsDto);

	ApiResponseDto<List<ClaimMbrNomineeDto>> findNomineeByClaimNo(String claimNo);

	ApiResponseDto<List<ClaimAppointeeDto>> getAppointeeNameList(String claimNo);

}