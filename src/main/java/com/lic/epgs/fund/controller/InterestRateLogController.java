/**
 * 
 */
package com.lic.epgs.fund.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.fund.service.InterestRateLogService;

/**
 * @author Muruganandam
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy/interest/log/error/")
public class InterestRateLogController {
	@Autowired
	private InterestRateLogService logService;

	@GetMapping(value = { "viewAll" })
	public ResponseEntity<Object> viewErrorLog() {
		return ResponseEntity.ok().body(logService.viewErrorLog());
	}

	@GetMapping(value = { "fail" })
	public ResponseEntity<Object> viewFailErrorLog() {
		return ResponseEntity.ok().body(logService.viewFailErrorLog());
	}

	@GetMapping(value = { "{policyNo}" })
	public ResponseEntity<Object> viewErrorLogByPolicy(@PathVariable String policyNo) {
		return ResponseEntity.ok().body(logService.viewErrorLogByPolicy(policyNo));
	}

	@GetMapping(value = { "member/{memberId}" })
	public ResponseEntity<Object> viewErrorLogByMemberId(@PathVariable String memberId) {
		return ResponseEntity.ok().body(logService.viewErrorLogByMemberId(memberId));
	}

	@GetMapping(value = { "referenceNo/{refNo}" })
	public ResponseEntity<Object> viewErrorLogByrefNo(@PathVariable String refNo) {
		return ResponseEntity.ok().body(logService.viewErrorLogByrefNo(refNo));
	}
}
