package com.lic.epgs.topupda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.topupda.dto.AnnuityRequestDto;
import com.lic.epgs.topupda.dto.AnnutityResponseDto;
import com.lic.epgs.topupda.dto.TopupdaApiResponseDto;
import com.lic.epgs.topupda.dto.TopupdaDto;
import com.lic.epgs.topupda.dto.TopupdaNotesDto;
import com.lic.epgs.topupda.dto.TopupdaRquestDto;
import com.lic.epgs.topupda.service.TopupdaService;
import com.lic.epgs.utils.CommonConstants;

/**
*
* @author Dhanush
*
*/

@RestController
@CrossOrigin("*")
@RequestMapping("/api/topupDa")

public class TopupdaController {
	
	@Autowired	TopupdaService topUpDaService;
	
	
	@PostMapping("/saveTopupda")
	public TopupdaApiResponseDto saveTopUpDa(@RequestBody TopupdaDto topUpDaDto ) {
		return topUpDaService.saveTopUpDa(topUpDaDto);
	}
	@PostMapping("/saveNotes")
	public TopupdaApiResponseDto saveNotes(@RequestBody TopupdaNotesDto topUpDaNotesDto ) {
		return topUpDaService.saveNotes(topUpDaNotesDto);
	}
	@GetMapping("/getNotesList")
	public TopupdaApiResponseDto getNotesList(@RequestParam Long topupId) {
		return topUpDaService.getNotesList(topupId);
	}
	@PutMapping("/sendToChecker")
	public TopupdaApiResponseDto sendToCheker(@RequestParam Long topupId) {
		return topUpDaService.changeStatus(topupId, PolicyConstants.CHECKER_NO);
	}
	
	@PutMapping("/sendToMaker")
	public TopupdaApiResponseDto sendToMaker(@RequestParam Long topupId) {
		return topUpDaService.changeStatus(topupId, PolicyConstants.MAKER_NO);
	}
	
	@PutMapping("/sendToApprove")
	public TopupdaApiResponseDto sendToApprove(@RequestParam Long topupId) {
		return topUpDaService.sendToApprove(topupId);
	}
	@PostMapping("/sendToReject")
	public TopupdaApiResponseDto sendToReject(@RequestBody TopupdaDto topUpDaDto) {
		return topUpDaService.sendToReject(topUpDaDto);
	}
	
	@PostMapping("/inprogressCitrieaLoad")
	public TopupdaApiResponseDto inprogressCitrieaLoad(@RequestBody TopupdaRquestDto topUpDaDto) {
		return topUpDaService.inprogressCitrieaLoad(topUpDaDto);
	}
	@PostMapping("/existingCitrieaLoad")
	public TopupdaApiResponseDto existingCitrieaLoad(@RequestBody TopupdaRquestDto topUpDaDto) {
		return topUpDaService.existingCitrieaLoad(topUpDaDto);
	}
	@GetMapping("/inprogress/{id}")
	public TopupdaApiResponseDto getInprogressById(@PathVariable("id") Long topupId) {
		return topUpDaService.getTopupById(CommonConstants.INPROGRESS,topupId);
	}
	@GetMapping("/existing/{id}")
	public TopupdaApiResponseDto getExistingById(@PathVariable("id") Long topupId) {
		return  topUpDaService.getTopupById(CommonConstants.EXISTING,topupId);
	}
	@PostMapping("/notification")
	public AnnutityResponseDto annutityApi(@RequestBody AnnuityRequestDto annuityRequestDto ) {
		return topUpDaService.annutityApi(annuityRequestDto);
	}

}
