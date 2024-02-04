package com.lic.epgs.topupda.service;

import com.lic.epgs.topupda.dto.AnnuityRequestDto;
import com.lic.epgs.topupda.dto.AnnutityResponseDto;
import com.lic.epgs.topupda.dto.TopupdaApiResponseDto;
import com.lic.epgs.topupda.dto.TopupdaDto;
import com.lic.epgs.topupda.dto.TopupdaNotesDto;
import com.lic.epgs.topupda.dto.TopupdaRquestDto;

/**
*
* @author Dhanush
*
*/

public interface TopupdaService {

	

        TopupdaApiResponseDto saveTopUpDa(TopupdaDto topUpDaDto);

		TopupdaApiResponseDto changeStatus(Long topupId, String checkerNo);

		TopupdaApiResponseDto sendToApprove(Long topupId);

		TopupdaApiResponseDto sendToReject(TopupdaDto topUpDaDto);

		TopupdaApiResponseDto inprogressCitrieaLoad(TopupdaRquestDto topUpDaDto);

		TopupdaApiResponseDto existingCitrieaLoad(TopupdaRquestDto topUpDaDto);


		TopupdaApiResponseDto getTopupById(String inprogress, Long topupId);

		TopupdaApiResponseDto saveNotes(TopupdaNotesDto topUpDaNotesDto);

		TopupdaApiResponseDto getNotesList(Long topupId);

		AnnutityResponseDto annutityApi(AnnuityRequestDto annuityRequestDto);

}
