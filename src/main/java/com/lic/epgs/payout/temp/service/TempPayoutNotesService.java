package com.lic.epgs.payout.temp.service;

import java.util.Set;

import com.lic.epgs.payout.dto.PayoutNotesDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface TempPayoutNotesService {

	ApiResponseDto<Set<PayoutNotesDto>> add(PayoutNotesDto payoutNotesDto);

	ApiResponseDto<Set<PayoutNotesDto>> getNotesByPayoutNo(String payoutNo);

}