package com.lic.epgs.claim.service;

import java.util.Set;

import com.lic.epgs.claim.dto.ClaimNotesDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface ClaimNotesService {

	ApiResponseDto<String> add(ClaimNotesDto claimNotesDto);

	ApiResponseDto<Set<ClaimNotesDto>> getNotesByClaimNo(String claimNo);

}