package com.lic.epgs.payout.service;

import java.util.Set;

import com.lic.epgs.payout.dto.PayoutNotesDto;
import com.lic.epgs.common.dto.ApiResponseDto;

public interface PayoutNotesService {

	ApiResponseDto<Set<PayoutNotesDto>> add(PayoutNotesDto payoutNotesDto);

	ApiResponseDto<Set<PayoutNotesDto>> getNotesByPayoutNo(String payoutNo);

}