package com.lic.epgs.policy.contribution.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.policy.contribution.dto.CustomResponseEntity;
import com.lic.epgs.policy.contribution.dto.PolicyContributionResponseDto;
import com.lic.epgs.policy.contribution.service.PolicyContributionService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/policy_contribution")
public class PolicyContributionRestController {
	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	PolicyContributionService policyContributionService;

	@GetMapping("getPolicyContributionsDetails")
	public ResponseEntity<CustomResponseEntity<List<PolicyContributionResponseDto>>> getPolicyContributionsDetails(
			@RequestParam Integer policyId, @RequestParam String financialYear, @RequestParam String quarter)
			throws Exception {
		logger.info("PolicyContributionRestController : getPolicyContributionsDetails : start ");
		CustomResponseEntity<List<PolicyContributionResponseDto>> responseEntity = new CustomResponseEntity<>();

		List<PolicyContributionResponseDto> policyContributionList = policyContributionService
				.getPolcyContributionsDetails(policyId, financialYear, quarter);

		if (policyContributionList.isEmpty()) {
			responseEntity.setStatusCode(HttpStatus.NOT_FOUND.value());
			responseEntity.setStatusMessage("Fail : No data found against policy id :" + policyId);
		} else {
			responseEntity.setStatusCode(HttpStatus.OK.value());
			responseEntity.setStatusMessage("Success");
			responseEntity.setData(policyContributionList);
		}
		logger.info("PolicyContributionRestController : getPolicyContributionsDetails : ended");
		return ResponseEntity.status(HttpStatus.OK).body(responseEntity);
	}

}
