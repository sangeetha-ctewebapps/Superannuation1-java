package com.lic.epgs.policyservicing.policylvl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDocumentDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyConversionSearchDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyLevelConversionDto;
import com.lic.epgs.policyservicing.policylvl.service.PolicyConversionService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/policyLevelConversion/")
public class PolicyLevelConversionController {
	
	@Autowired
	PolicyConversionService policyLevelConversionService;
	
	
	@PostMapping(value = "/savePolicyConversionDetails")
	ResponseEntity<Object> savePolicyConversionDetails(@RequestBody PolicyLevelConversionDto policyLevelConversionDto) {
		return ResponseEntity.ok(policyLevelConversionService.savePolicyConversionDetails(policyLevelConversionDto));
	}
	
	@GetMapping(value = "/getInprogressPolicyConversionDetailsList")
	ResponseEntity<Object> getInprogressPolicyConversionDetailsList(@RequestParam String role,String unitCode) {
		return ResponseEntity.ok(policyLevelConversionService.getInprogressPolicyConversionDetailsList(role, unitCode));
	}
	
	
	@GetMapping(value = "/getExistingPolicyConversionDetailsList")
	ResponseEntity<Object> getExistingPolicyConversionDetailsList(@RequestParam String role,String unitCode) {
		return ResponseEntity.ok(policyLevelConversionService.getExistingPolicyConversionDetailsList(role, unitCode));
	}
	
	
	@PostMapping(value = "/sendToChecker")
	ResponseEntity<Object> sendToChecker(@RequestParam String conversionId,String modifiedBy) {
		return ResponseEntity.ok(policyLevelConversionService.sendToChecker(conversionId, modifiedBy));
	}
	
	
	@PostMapping(value = "/saveBenefitTypes")
	ResponseEntity<Object> saveBenefitTypes(@RequestBody List<PolicyRulesDto> policyRulesDto) {
		return ResponseEntity.ok(policyLevelConversionService.saveBenefitTypes(policyRulesDto));
	}
	
	
	@PostMapping(value = "/savePolicyConversionNotes")
	ResponseEntity<Object> savePolicyConversionNotes(@RequestBody PolicyServiceNotesDto policyNotesDto) {
		return ResponseEntity.ok(policyLevelConversionService.savePolicyConversionNotes(policyNotesDto));
	}
	
	@GetMapping(value = "/getInprogressOverallDetails")
	ResponseEntity<Object> getInprogressOverallDetails(@RequestParam Long serviceId) {
		return ResponseEntity.ok(policyLevelConversionService.getInprogressOverallDetails(serviceId));
	}
	
	
	@GetMapping(value = "/getExistingOverallDetails")
	ResponseEntity<Object> getExistingOverallDetails(@RequestParam Long serviceId) {
		return ResponseEntity.ok(policyLevelConversionService.getExistingOverallDetails(serviceId));
	}
	
	
	@GetMapping(value = "/getNotesDetailsList")
	ResponseEntity<Object> getNotesDetailsList(@RequestParam Long serviceId) {
		return ResponseEntity.ok(policyLevelConversionService.getNotesDetailsList(serviceId));
	}
	
	@GetMapping(value = "/getPolicyConversionDetailsBypolicyNo")
	public ResponseEntity<Object> getPolicyConversionDetailsBypolicyNo(@RequestParam String prevPolicyNo,String role) {
	return ResponseEntity.ok().body(policyLevelConversionService.getPolicyConversionDetailsBypolicyNo(prevPolicyNo,role));
	}
	
	@PostMapping(value = "/InProgressCommonSearch")
	public ResponseEntity<Object> InProgressCommonSearch(@RequestBody PolicyConversionSearchDto policyConversionSearchDto) {
	return ResponseEntity.ok().body(policyLevelConversionService.InProgressCommonSearch(policyConversionSearchDto));
	}
	
	
//	@PostMapping(value = "/getMemberDetailsInConversion")
//	public ResponseEntity<Object> getMemberDetailsInConversion(@RequestParam String conversionId,String memberId) {
//	return ResponseEntity.ok().body(policyLevelConversionService.getMemberDetailsInConversion(conversionId,memberId));
//	}
//	
//	@PostMapping(value = "/getMemberDetailsInConversionMaster")
//	public ResponseEntity<Object> getMemberDetailsInConversionMaster(@RequestParam String conversionId,String memberId) {
//	return ResponseEntity.ok().body(policyLevelConversionService.getMemberDetailsInConversionMaster(conversionId,memberId));
//	}
//	
//	@PostMapping(value = "/getMemberDetailsbyConversionIdInprocess")
//	public ResponseEntity<Object> getMemberDetailsbyConversionId(@RequestParam String conversionId) {
//	return ResponseEntity.ok().body(policyLevelConversionService.getMemberDetailsbyConversionIdInprocess(conversionId));
//	}
//	@PostMapping(value = "/getMemberDetailsbyConversionIdExisting")
//	public ResponseEntity<Object> getMemberDetailsbyConversionIdExisting(@RequestParam String conversionId) {
//	return ResponseEntity.ok().body(policyLevelConversionService.getMemberDetailsbyConversionIdExisting(conversionId));
//	}
//	
//	@PostMapping(value = "/removeMemberBankDetails")
//	public ResponseEntity<Object> removeMemberBankDetails(@RequestParam Integer bankId,Long memberId,String modifiedBy) {
//	return ResponseEntity.ok().body(policyLevelConversionService.removeMemberBankDetails(bankId,memberId,modifiedBy));
//	}
//	
//	@PostMapping(value = "/removeMemberAddressDetails")
//	public ResponseEntity<Object> removeMemberAddressDetails(@RequestParam Long addressId,Long memberId,String modifiedBy) {
//	return ResponseEntity.ok().body(policyLevelConversionService.removeMemberAddressDetails(addressId, memberId, modifiedBy));
//	}
	
	@PostMapping(value = "/removeDocumentDetails")
	public ResponseEntity<Object> removeDocumentDetails(@RequestParam Long docId,Long conversionId,String modifiedBy) {
	return ResponseEntity.ok().body(policyLevelConversionService.removeDocumentDetails(docId, conversionId,modifiedBy));
	}
	
	
	@GetMapping(value = "/getDocumentList")
	public ResponseEntity<Object> getDocumentList(@RequestParam Long conversionId) {
	return ResponseEntity.ok().body(policyLevelConversionService.getDocumentList(conversionId));
	}
	
	@PostMapping(value = "/saveDocumentDetails")
	public ResponseEntity<Object> saveDocumentDetails(@RequestBody PolicyServiceDocumentDto policyServiceDocumentDto) {
	return ResponseEntity.ok().body(policyLevelConversionService.saveDocumentDetails(policyServiceDocumentDto));
	}
	
	@GetMapping(value = "/policyCriteriaSearch")
	ResponseEntity<Object> policyCriteriaSearch(@RequestParam String policyNo) {
		return ResponseEntity.ok(policyLevelConversionService.policyCriteriaSearch(policyNo));
	}
	
   
}
