package com.lic.epgs.surrender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;
import com.lic.epgs.surrender.dto.CommonResponseDto;
import com.lic.epgs.surrender.dto.PolicySurrenderApiResponse;
import com.lic.epgs.surrender.dto.PolicySurrenderDto;
import com.lic.epgs.surrender.dto.PolicySurrenderPayOutDto;
import com.lic.epgs.surrender.service.PolicySurrenderService;
import com.lic.epgs.utils.CommonConstants;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policySurrender")
public class PolicySurrenderController {

	@Autowired
	private PolicySurrenderService policySurrenderService;

	@PostMapping("/savePolicyFullSurrender")
	public PolicySurrenderApiResponse saveAndUpdatePolicySurrender(
			@RequestBody PolicySurrenderDto policySurrenderDto) {
		return policySurrenderService.saveAndUpdatePolicySurrender(policySurrenderDto);
	}

	@PostMapping("/sendToChecker")
	public PolicySurrenderApiResponse sendToChecker(@RequestBody PolicySurrenderDto policySurrenderDto) {
		return policySurrenderService.sendToMakerChecker(policySurrenderDto);
	}
	
	@PostMapping("/sendToMaker")
	public PolicySurrenderApiResponse sendToMaker(@RequestBody PolicySurrenderDto policySurrenderDto) {
		return policySurrenderService.sendToMakerChecker(policySurrenderDto);
	}

	@PostMapping("/approvePolicySurrender")
	public PolicySurrenderApiResponse approvePolicySurrender(
			@RequestBody PolicySurrenderDto policySurrenderDto) {
		return policySurrenderService.approvePolicySurrender(policySurrenderDto);
	}

	@PostMapping("/rejectPolicySurrender")
	public PolicySurrenderApiResponse rejectPolicySurrender(
			@RequestBody PolicySurrenderDto policySurrenderDto) {
		return policySurrenderService.rejectPolicySurrender(policySurrenderDto);
	}

	@PostMapping("/uploadDocument")
	public PolicySurrenderApiResponse uploadDocument(@RequestBody CommonDocsDto docsDto) {
		return policySurrenderService.uploadDocument(docsDto);

	}
	
	@PostMapping("/savePayoutDetails")
	public PolicySurrenderApiResponse savePayoutDetails(@RequestBody PolicySurrenderPayOutDto policySurrenderPayOutDto) {
	return policySurrenderService.savePayoutDetails(policySurrenderPayOutDto);
	}

	@PostMapping("/editPayoutDetails")
	public PolicySurrenderApiResponse editPayoutDetails(@RequestBody PolicySurrenderPayOutDto policySurrenderPayOutDto) {
	return policySurrenderService.editPayoutDetails(policySurrenderPayOutDto);
	}
	
	@PostMapping("/saveNotesDetails")
	public PolicySurrenderApiResponse saveNotesDetails(@RequestBody CommonNotesDto commonNotesDto) {
	return policySurrenderService.saveNotesDetails(commonNotesDto);
	}
	
	@GetMapping("/inprogressSurrender")
	public PolicySurrenderApiResponse inprogressSurrender(@RequestParam String roleType) {
		return policySurrenderService.inprogresOrExitingSurrender( roleType, CommonConstants.INPROGRESS);
	}

	@GetMapping("/exitingSurrender")
	public PolicySurrenderApiResponse exitingSurrender(@RequestParam String roleType) {
		return policySurrenderService.inprogresOrExitingSurrender( roleType, CommonConstants.EXISTING);
	}
	
	@GetMapping("/getPolicySurrenderbyId")
	public PolicySurrenderApiResponse getPolicySurrenderbySurrenderId(@RequestParam Long surrenderId) {
		return policySurrenderService.getPolicySurrenderbySurrenderId(surrenderId);
	}
	
	@GetMapping("/getPolicySurrenderBasicDetailsById")
	public PolicySurrenderApiResponse getPolicySurrenderBasicDetailsById(@RequestParam Long surrenderId) {
		return policySurrenderService.getPolicySurrenderBasicDetailsById(surrenderId);
	}
	
	@PostMapping("/removeDocumentDetails")
	public CommonResponseDto removeDocumentDetails(@RequestParam Long surrenderId,Integer docId,String modifiedBy) {
	return policySurrenderService.removeDocumentDetails(surrenderId,docId,modifiedBy);
	}
	
	
	@GetMapping("/getSurrenderNotesList")
	public CommonResponseDto getSurrenderNotesList(@RequestParam Long surrenderId) {
		return policySurrenderService.getSurrenderNotesList(surrenderId);
	}
	

}
