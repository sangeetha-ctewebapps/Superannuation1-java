/**
 * 
 */
package com.lic.epgs.fund.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.fund.dto.VouchereffectiveDto;
import com.lic.epgs.fund.dto.VouchereffectiveRequest;
import com.lic.epgs.fund.service.FundRestApiService;
import com.lic.epgs.integration.dto.InterestFundDto;

/**
 * @author Muruganandam
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/external/interest/")
public class InterestFundController {
	@Autowired
	private FundRestApiService fundRestApiService;

	@PostMapping(value = "policy/credit", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> creditPolicy(@RequestBody InterestFundDto requestDto) throws ApplicationException {
		return ResponseEntity.ok().body(fundRestApiService.creditPolicy(requestDto));
	}

	/**
	 * @throws ApplicationException
	 * @PostMapping(value = "policy/debit", produces = {
	 *                    MediaType.APPLICATION_JSON_VALUE }) public
	 *                    ResponseEntity<Object> debitPolicy(@RequestBody
	 *                    DebitRequestDto requestDto) { return
	 *                    ResponseEntity.ok().body(fundRestApiService.debitPolicy(requestDto));
	 *                    }
	 */

	@PostMapping(value = "policy-member/credit", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> creditPolicyMember(@RequestBody InterestFundDto requestDto)
			throws ApplicationException {
		return ResponseEntity.ok().body(fundRestApiService.creditPolicyMembers(requestDto));
	}

	/**
	 * @PostMapping(value = "policy-member/debit", produces = {
	 *                    MediaType.APPLICATION_JSON_VALUE }) public
	 *                    ResponseEntity<Object> debitPolicyMember(@RequestBody
	 *                    DebitRequestDto requestDto) { return
	 *                    ResponseEntity.ok().body(fundRestApiService.debitPolicyMember(requestDto));
	 *                    }
	 */

	@GetMapping(value = "policy/viewLatestByPolicyNo/{policyNo}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> viewByPolicyNo(@PathVariable String policyNo) {
		return ResponseEntity.ok().body(fundRestApiService.viewByPolicyNo(policyNo));
	}

	/***
	 * @GetMapping(value = "policy-member/viewMembersByPolicyNo/{policyNo}",
	 *                   produces = { MediaType.APPLICATION_JSON_VALUE }) public
	 *                   ResponseEntity<Object> viewMembersByPolicyNo(@PathVariable
	 *                   String policyNo) { return
	 *                   ResponseEntity.ok().body(fundRestApiService.viewMembersByPolicyNo(policyNo))
	 *                   ; }
	 */

	@GetMapping(value = "policy-member/viewLatestByMemberId/{memberId}", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> viewByMemberId(@PathVariable String memberId) {
		return ResponseEntity.ok().body(fundRestApiService.viewByMemberId(memberId));
	}

	@GetMapping(value = { "viewHistoryPolicyNo/{policyNo}" })
	public ResponseEntity<Object> viewHistoryPolicyNo(@PathVariable String policyNo) {
		return ResponseEntity.ok().body(fundRestApiService.viewHistoryPolicyNo(policyNo));
	}

	@GetMapping(value = { "viewHistoryByMemberId/{memberId}" })
	public ResponseEntity<Object> viewHistoryByMemberId(@PathVariable String memberId) {
		return ResponseEntity.ok().body(fundRestApiService.viewHistoryByMemberId(memberId));
	}

	@GetMapping(value = { "viewMembersHistoryByPolicyNo/{policyNo}" })
	public ResponseEntity<Object> viewMembersHistoryByPolicyNo(@PathVariable String policyNo) {
		return ResponseEntity.ok().body(fundRestApiService.viewMembersHistoryByPolicyNo(policyNo));
	}
	
	@PostMapping("/updateEffectiveDate")
	public ResponseEntity<Object> getCollectionData(@RequestBody  VouchereffectiveRequest dto )throws ApplicationException {
		return ResponseEntity.ok().body(fundRestApiService.getCollectionData(dto));
	}
	
	
}
