package com.lic.epgs.policyservicing.memberlvl.transferout.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutReqDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutResponseDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.service.MemberLevelTransferOutService;
import com.lic.epgs.utils.CommonConstants;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/memberTransferInOut")
public class MemberLevelTransferOutController {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private MemberLevelTransferOutService memberLevelTransferOutService;

	
	@PostMapping("/saveTransferInOut")
	public TransferInAndOutResponseDto saveTransferInOut(@RequestBody TransferInAndOutDto transferInAndOutDto) {
		logger.info("MemberLevelTransferOutController:saveTransferInOut:started");
		TransferInAndOutResponseDto commonDto = memberLevelTransferOutService.saveAndUpdateMemberTransferOut(transferInAndOutDto);
		logger.info("MemberLevelTransferOutController:saveTransferInOut:ended");
		return commonDto;
	}
	
	
	@PostMapping("/getOverallDetails")
	public TransferInAndOutResponseDto getOverallDetails(@RequestBody TransferInAndOutReqDto transferInAndOutReqDto) {
		logger.info("MemberLevelTransferOutController:getOverallDetails:started");
		TransferInAndOutResponseDto commonDto = memberLevelTransferOutService.getOverallDetails(transferInAndOutReqDto);
		logger.info("MemberLevelTransferOutController:getOverallDetails:ended");
		return commonDto;
	}
	
	
	@PostMapping("/sendToChecker")
	public TransferInAndOutResponseDto sendToChecker(@RequestBody TransferInAndOutDto transferInAndOutDto) {
		logger.info("MemberLevelTransferOutController:sendToChecker:started");
		TransferInAndOutResponseDto commonDto = memberLevelTransferOutService.sendToChecker(transferInAndOutDto);
		logger.info("MemberLevelTransferOutController:sendToChecker:ended");
		return commonDto;
	}
	
	
	@PostMapping("/merberSearchCriteria")
	public TransferInAndOutResponseDto getMemberSearchByCriteria(@RequestBody TransferInAndOutReqDto memberSearchDto) {
		logger.info("MemberLevelTransferOutController:getMemberSearchByCriteria:started");
		TransferInAndOutResponseDto commonDto = memberLevelTransferOutService.memberSearch(memberSearchDto);
		logger.info("MemberLevelTransferOutController:getMemberSearchByCriteria:ended");
		return commonDto;
	}
	
	
	@PostMapping("/sendToMaker")
	public TransferInAndOutResponseDto sendToMaker(@RequestBody TransferInAndOutReqDto reqDto) {
		logger.info("MemberLevelTransferOutController:sendToMaker:started");
		TransferInAndOutResponseDto commonDto = memberLevelTransferOutService.sendToMaker(reqDto);
		logger.info("MemberLevelTransferOutController:sendToMaker:ended");
		return commonDto;
	}

	
	@PostMapping("/approve")
	public TransferInAndOutResponseDto approve(@RequestBody TransferInAndOutReqDto reqDto) {
		logger.info("MemberLevelTransferOutController:approve:started");
		TransferInAndOutResponseDto commonDto = memberLevelTransferOutService.approve(reqDto);
		logger.info("MemberLevelTransferOutController:approve:ended");
		return commonDto;
	}
	
	

	@PostMapping("/getPolicyDetails")
	public TransferInAndOutResponseDto getPolicyDetails(@RequestBody TransferInAndOutReqDto dto) {
		logger.info("PolicyRestController:getPolicyDetails:started");
		TransferInAndOutResponseDto commonDto = memberLevelTransferOutService.getPolicyDetails(dto);
		logger.info("PolicyRestController:getPolicyDetails:ended");
		return commonDto;
	}
	
	
	@PostMapping("/reject")
	public TransferInAndOutResponseDto reject(@RequestBody TransferInAndOutReqDto reqDto) {
		logger.info("MemberLevelTransferOutController:reject:started");
		TransferInAndOutResponseDto commonDto = memberLevelTransferOutService.reject(reqDto);
		logger.info("MemberLevelTransferOutController:reject:ended");
		return commonDto;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	@PostMapping("/saveNotesDetails")
	public TransferInAndOutResponseDto saveNotesDetails(@RequestBody PolicyServiceNotesDto commonNotesDto) {
	return memberLevelTransferOutService.saveNotesDetails(commonNotesDto);
	}
	
	@GetMapping("/inprogressTransferOut")
	public TransferInAndOutResponseDto inprogressTransferOut(@RequestParam String roleType) {
		return memberLevelTransferOutService.inprogresOrExitingTransferOut( roleType, CommonConstants.INPROGRESS);
	}

	@GetMapping("/exitingTransferOut")
	public TransferInAndOutResponseDto exitingTransferOut(@RequestParam String roleType) {
		return memberLevelTransferOutService.inprogresOrExitingTransferOut( roleType, CommonConstants.EXISTING);
	}
	
	@PostMapping("/inprogressTransferOut/citrieaSearch")
	public TransferInAndOutResponseDto inprogressTransferOut(@RequestBody TransferInAndOutReqDto memberSearchDto) {
		TransferInAndOutResponseDto commonDto = memberLevelTransferOutService.inprogressCitrieaSearch(memberSearchDto);
		return commonDto;
	}

	@GetMapping(value = "/getInprogressLoad")
	public ResponseEntity<Object> getInprogressLoad(@RequestParam String role, @RequestParam String unitCode) {
		return ResponseEntity.ok().body(memberLevelTransferOutService.getInprogressLoad(role, unitCode));
	}

	@PostMapping("/existing/citrieaSearch1")
	public TransferInAndOutResponseDto getExistingCitriea(@RequestBody TransferInAndOutReqDto policySearchDto) {
		TransferInAndOutResponseDto commonDto = memberLevelTransferOutService.existingCitrieaSearch(policySearchDto);
		return commonDto;
	}

	@GetMapping(value = "/getExistingLoad")
	public ResponseEntity<Object> getExistingLoad(@RequestParam String role, @RequestParam String unitCode) {
		return ResponseEntity.ok().body(memberLevelTransferOutService.getExistingLoad(role, unitCode));
	}

	
}
