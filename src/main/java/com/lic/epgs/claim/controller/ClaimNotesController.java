package com.lic.epgs.claim.controller;

import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.claim.dto.ClaimNotesDto;
import com.lic.epgs.claim.service.ClaimNotesService;
import com.lic.epgs.claim.service.ClaimService;
import com.lic.epgs.claim.temp.service.TempClaimNotesService;
import com.lic.epgs.common.dto.ApiResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/claim/note")
public class ClaimNotesController {

	@Autowired
	TempClaimNotesService tempClaimNotesService;

	@Autowired
	ClaimNotesService claimNotesService;

	@Autowired
	ClaimService claimService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/add")
	public ApiResponseDto<String> add(@RequestBody ClaimNotesDto request) {
		logger.info("ClaimNotesController -- add --started");
		if (claimService.checkClaimStatus(request.getClaimNo())) {
			logger.info("ClaimNotesController -- add --ended");
			return claimNotesService.add(request);
		} else {
			logger.info("ClaimNotesController -- add --ended");
			return tempClaimNotesService.add(request);
		}
	}

	@GetMapping("/get/{claimNo}")
	public ApiResponseDto<Set<ClaimNotesDto>> getNotesByClaimNo(@PathVariable String claimNo) {
		logger.info("ClaimNotesController -- add --started");
		if (claimService.checkClaimStatus(claimNo)) {
			logger.info("ClaimNotesController -- add --ended");
			return claimNotesService.getNotesByClaimNo(claimNo);
		} else {
			logger.info("ClaimNotesController -- add --ended");
			return tempClaimNotesService.getNotesByClaimNo(claimNo);
		}
	}

}
