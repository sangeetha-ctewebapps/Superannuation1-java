/**
 * 
 */
package com.lic.epgs.fund.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.common.dto.InterestRequestDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.fund.service.PolicyInterestFundService;

/**
 * @author Muruganandam
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy/interest/")
public class PolicyInterestRateFundController {

	@Autowired
	private PolicyInterestFundService serviceImpl;

	@GetMapping(value = { "credit/{policyNo}" })
	public ResponseEntity<Object> creditPolicy(@PathVariable String policyNo) throws ApplicationException {
		return ResponseEntity.ok().body(serviceImpl.creditPolicy(policyNo));
	}

	@GetMapping(value = { "debit/{policyNo}" })
	public ResponseEntity<Object> debitPolicy(@PathVariable String policyNo) throws ApplicationException {
		return ResponseEntity.ok().body(serviceImpl.debitPolicy(policyNo));
	}

	@GetMapping(value = { "member/credit/{policyNo}" })
	public ResponseEntity<Object> creditPolicyMember(@PathVariable String policyNo) {
		return ResponseEntity.ok().body(serviceImpl.creditPolicyMembers(policyNo));
	}

	@GetMapping(value = { "member/credit/{policyNo}/{memberId}" })
	public ResponseEntity<Object> creditPolicyMember(@PathVariable String policyNo, @PathVariable String memberId) {
		return ResponseEntity.ok().body(serviceImpl.creditPolicyMember(policyNo, memberId));
	}

	@GetMapping(value = { "member/credit/bulk" })
	public ResponseEntity<Object> creditBulkPolicyMember(@RequestBody InterestRequestDto requestDto)
			throws ApplicationException {
		return ResponseEntity.ok().body(serviceImpl.creditBulkPolicyMember(requestDto));
	}

	@GetMapping(value = { "member/debit/{policyNo}" })
	public ResponseEntity<Object> debitMembersByPolicy(@PathVariable String policyNo) {
		return ResponseEntity.ok().body(serviceImpl.debitMembersByPolicy(policyNo));
	}

	@GetMapping(value = { "member/debit/{policyNo}/{memberId}" })
	public ResponseEntity<Object> debitPolicyMemberById(@PathVariable String policyNo, @PathVariable String memberId)
			throws ApplicationException {
		return ResponseEntity.ok().body(serviceImpl.debitPolicyMemberById(policyNo, memberId));
	}
	
	@GetMapping(value = { "member/debit/bulk" })
	public ResponseEntity<Object> debitBulkPolicyMember(@RequestBody InterestRequestDto requestDto)
			throws ApplicationException {
		return ResponseEntity.ok().body(serviceImpl.debitBulkPolicyMember(requestDto));
	}

}
