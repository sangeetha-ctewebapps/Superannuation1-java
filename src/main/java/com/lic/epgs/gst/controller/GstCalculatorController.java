package com.lic.epgs.gst.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.gst.dto.GstCalculationRequest;
import com.lic.epgs.gst.dto.GstCalculationResponse;
import com.lic.epgs.gst.service.GstCalculatorService;

@CrossOrigin("*")
@RestController
@RequestMapping("/gstCalculator")
public class GstCalculatorController {
	private static final Logger log = LoggerFactory.getLogger(GstCalculatorController.class);

	@Autowired
	private GstCalculatorService gstCalculatorService;

	@PostMapping("/calculate/gst")
	public ResponseEntity<GstCalculationResponse> getGstCalculation(
			@RequestBody GstCalculationRequest gstCalculationRequest) {
		log.info("Entering into GstCalculatorController : getGstCalculation ");

		GstCalculationResponse gstCalculationResponse = gstCalculatorService.callGstCalculation(gstCalculationRequest);
		log.info("Exit into GstCalculatorController : getGstCalculation ");

		return new ResponseEntity<>(gstCalculationResponse, HttpStatus.OK);

	}

}
