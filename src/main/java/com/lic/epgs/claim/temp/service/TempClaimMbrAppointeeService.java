package com.lic.epgs.claim.temp.service;

import java.util.List;

import com.lic.epgs.claim.dto.ClaimMbrAppointeeDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempClaimMbrAppointeeService {

	ApiResponseDto<ClaimMbrAppointeeDto> save(ClaimMbrAppointeeDto docsDto);

	ApiResponseDto<List<ClaimMbrAppointeeDto>> findAppointeeByClaimNo(String claimNo);

}