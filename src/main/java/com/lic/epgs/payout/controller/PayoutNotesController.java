package com.lic.epgs.payout.controller;

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

import com.lic.epgs.payout.dto.PayoutNotesDto;
import com.lic.epgs.payout.service.PayoutNotesService;
import com.lic.epgs.payout.service.PayoutService;
import com.lic.epgs.payout.temp.service.TempPayoutNotesService;
import com.lic.epgs.common.dto.ApiResponseDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payout/note")
public class PayoutNotesController {

	@Autowired
	TempPayoutNotesService tempPayoutNotesService;

	@Autowired
	PayoutNotesService payoutNotesService;

	@Autowired
	PayoutService payoutService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/add")
	public ApiResponseDto<Set<PayoutNotesDto>> add(@RequestBody PayoutNotesDto request) {
		logger.info("PayoutNotesController -- add --started");
		if (payoutService.checkPayoutStatus(request.getPayoutNo())) {
			logger.info("PayoutNotesController -- add --ended");
			return payoutNotesService.add(request);
		} else {
			logger.info("PayoutNotesController -- add --ended");
			return tempPayoutNotesService.add(request);
		}
	}

	@GetMapping("/get/{payoutNo}")
	public ApiResponseDto<Set<PayoutNotesDto>> getNotesByPayoutNo(@PathVariable String payoutNo) {
		logger.info("PayoutNotesController -- getNotesByPayoutNo --started");
		if (payoutService.checkPayoutStatus(payoutNo)) {
			logger.info("PayoutNotesController -- getNotesByPayoutNo --ended");
			return payoutNotesService.getNotesByPayoutNo(payoutNo);
		} else {
			logger.info("PayoutNotesController -- getNotesByPayoutNo --ended");
			return tempPayoutNotesService.getNotesByPayoutNo(payoutNo);
		}
	}

}
