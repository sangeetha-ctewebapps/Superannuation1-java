package com.lic.epgs.policyservicing.policylvl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyConversionSearchDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyLevelConversionDto;
import com.lic.epgs.policyservicing.policylvl.service.PolicyConversionService;
/**
 * @author Logesh.D Date:20-09-2022
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping({ "/api/policyLevelConversion/Checker/" })
public class PolicyLevelConversionCheckerController {
	
	@Autowired
	PolicyConversionService policyLevelConversionService;
	
	
	/*
	 * Description- PolicyConversion Details is Approved By the Checker
	 */
	@PostMapping(value = "/approvePolicyConversion")
	public ResponseEntity<Object> approvePolicyConversion(@RequestParam Long conversionId,@RequestParam String modifiedBy) {
		return ResponseEntity.ok().body(policyLevelConversionService.approvePolicyConversion(conversionId,modifiedBy));
	}
	
	@PostMapping(value = "/sendToMaker")
	ResponseEntity<Object> sendToMaker(@RequestParam String conversionId,String modifiedBy) {
		return ResponseEntity.ok(policyLevelConversionService.sendToMaker(conversionId, modifiedBy));
	}
	
	@PostMapping(value = "/policyConversionRejection")
	ResponseEntity<Object> policyConversionRejection(@RequestBody PolicyLevelConversionDto policyLevelConversionDto) {
		return ResponseEntity.ok(policyLevelConversionService.policyConversionRejection(policyLevelConversionDto));
	}
	
	
	@GetMapping(value = "/getExistingDetailsByNewPolicyNo")
	public ResponseEntity<Object> getExistingDetailsByNewPolicyNo(@RequestParam String newPolicyNo){
		return ResponseEntity.ok().body(policyLevelConversionService.getExistingDetailsByNewPolicyNo(newPolicyNo));
	}

    
	@PostMapping(value = "/existingCommonSearch")
	public ResponseEntity<Object> existingCommonSearch(@RequestBody PolicyConversionSearchDto policyConversionSearchDto){
		return ResponseEntity.ok().body(policyLevelConversionService.existingCommonSearch(policyConversionSearchDto));
	}
}
