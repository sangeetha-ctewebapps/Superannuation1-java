package com.lic.epgs.common.gi.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.common.gi.dto.DepositTransferDto;
import com.lic.epgs.common.gi.dto.GiServiceRequestDto;
import com.lic.epgs.common.gi.service.DepositTransferService;

/**
 * @author Ramprasad
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/gic/service/")
public class DepositTransferController {

	@Autowired
	private DepositTransferService transferService;
	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("depositTransfersearch")
	public DepositTransferDto giPolicySearch(@RequestBody GiServiceRequestDto dto) {
		logger.info("DepositTransferController {}::giPolicySearch {}::{} started");
		DepositTransferDto transferDto = transferService.giPolicySearch(dto);
		logger.info("DepositTransferController {}::giPolicySearch {}::{} ended");
		return transferDto;
	}

}
