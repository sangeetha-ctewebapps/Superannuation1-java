//package com.lic.epgs.claim.controller;
//
//import java.util.List;
//
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.lic.epgs.claim.dto.PolicyMemberSearchRequestDto;
//import com.lic.epgs.claim.dto.ClaimMemberSearchResponseDto;
//import com.lic.epgs.claim.temp.service.TempClaimMemberSearchService;
//import com.lic.epgs.common.dto.ApiResponseDto;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/api/claim/member")
//public class ClaimMemberSearchController {
//
//	protected final Logger logger = LogManager.getLogger(getClass());
//
//	@Autowired
//	TempClaimMemberSearchService claimMemberSearchService;
//
//	@PostMapping("/search")
//	public ApiResponseDto<List<ClaimMemberSearchResponseDto>> memberSearch(
//			@RequestBody PolicyMemberSearchRequestDto claimMemberSearchRequestDto) {
//		return claimMemberSearchService.memberSearch(claimMemberSearchRequestDto);
//	}
//
//	@GetMapping("/search/{memberShipNo}")
//	public ApiResponseDto<List<ClaimMemberSearchResponseDto>> memberShipNoSearch(@PathVariable String memberShipNo) {
//		return claimMemberSearchService.memberShipNoSearch(memberShipNo);
//	}
//
//}
