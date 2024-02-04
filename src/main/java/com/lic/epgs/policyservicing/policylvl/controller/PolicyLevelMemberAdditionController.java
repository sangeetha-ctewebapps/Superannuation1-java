package com.lic.epgs.policyservicing.policylvl.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.policyservicing.policylvl.service.PolicyMemberAdditionService;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/aom/")
	public class PolicyLevelMemberAdditionController {

	protected final Logger logger = LogManager.getLogger(getClass());
	
	
	@Autowired
	private PolicyMemberAdditionService additionOfMemberService;
	
//	@PostMapping("/save")
//	public CommonResponseDto saveQuotation(@RequestBody PolicyServiceMbrDto policyMbrDto) {
//		logger.info("AdditionOfMemberController:saveQuotation:started");
//		CommonResponseDto commonDto = additionOfMemberService.save(policyMbrDto);
//		logger.info("AdditionOfMemberController:saveQuotation:ended");
//		return commonDto;
//	}
//	
//	
//	@PostMapping("/saveNotes")
//	public CommonResponseDto saveNotes(@RequestBody PolicyServiceNotesDto policyMbrNotesDto) {
//		logger.info("AdditionOfMemberController:saveNotes:started");
//		CommonResponseDto commonDto = additionOfMemberService.saveNotes(policyMbrNotesDto);
//		logger.info("AdditionOfMemberController:saveNotes:ended");
//		return commonDto;
//	}
//	
//	
//	@GetMapping("/inprogress")
//	public CommonResponseDto inprogressLoad(@RequestParam("payload") String payload) throws IOException {
//		logger.info("AdditionOfMemberController:inprogressLoad:started");
//		CommonResponseDto commonDto = additionOfMemberService.getByData(CommonUtils.jsonToSearchPolicySearchDto(payload));
//		logger.info("AdditionOfMemberController:inprogressLoad:ended");
//		return commonDto;
//	}
//	
//	@GetMapping("/existing")
//	public CommonResponseDto existingLoad(@RequestParam("payload") String payload) throws IOException {
//		logger.info("AdditionOfMemberController:existingLoad:started");
//		CommonResponseDto commonDto = additionOfMemberService.getByData(CommonUtils.jsonToSearchPolicySearchDto(payload));
//		logger.info("AdditionOfMemberController:existingLoad:ended");
//		return commonDto;
//	}
//	
//	@GetMapping("/getByPolicyNumber")
//	public CommonResponseDto getByPolicyNumber(@RequestParam("payload") String payload) throws IOException {
//		logger.info("AdditionOfMemberController:getByPolicyNumber:started");
//		CommonResponseDto commonDto = additionOfMemberService.getByData(CommonUtils.jsonToSearchPolicySearchDto(payload));
//		logger.info("AdditionOfMemberController:getByPolicyNumber:ended");
//		return commonDto;
//	}
//	
//	
//	@PostMapping("/citrieaSearch")
//	public CommonResponseDto citrieaSearch(@RequestBody PolicyServiceMemberAdditionDto dto) {
//		logger.info("QuotationController:citrieaSearch:started");
//		CommonResponseDto commonDto = additionOfMemberService.citrieaSearch(dto);
//		logger.info("QuotationController:citrieaSearch:ended");
//		return commonDto;
//	}
//	
//	
//	
//	
//	@GetMapping("/getByPolicyNo")
//	public CommonResponseDto getByPolicyNo(@RequestParam("payload") String payload) throws IOException {
//		logger.info("AdditionOfMemberController:getByPolicyNo:started");
//		CommonResponseDto commonDto = additionOfMemberService.getByPolicyNo(CommonUtils.jsonToSearchPolicySearchDto(payload));
//		logger.info("AdditionOfMemberController:getByPolicyNo:ended");
//		return commonDto;
//	}
//	
//	@PutMapping("sendToCheker")
//	public CommonResponseDto sendToCheker(@RequestParam("payload") String payload) throws IOException {
//		logger.info("AdditionOfMemberController:sendToCheker:started");
//		CommonResponseDto commonDto = additionOfMemberService.sendToCheker(CommonUtils.jsonToSearchPolicySearchDto(payload));
//		logger.info("AdditionOfMemberController:sendToCheker:ended");
//		return commonDto;
//	}
//	
//	@PutMapping("approveOrReject")
//	public CommonResponseDto approveOrReject(@RequestParam("payload") String payload) throws IOException {
//		logger.info("AdditionOfMemberController:approveOrReject:started");
//		CommonResponseDto commonDto = additionOfMemberService.approveOrReject(CommonUtils.jsonToSearchPolicySearchDto(payload));
//		logger.info("AdditionOfMemberController:approveOrReject:ended");
//		return commonDto;
//	}
//	
//	@PutMapping("sendToMaker")
//	public CommonResponseDto sendToMaker(@RequestParam("payload") String payload) throws IOException {
//		logger.info("AdditionOfMemberController:sendToMaker:started");
//		CommonResponseDto commonResponseDto = additionOfMemberService.sendToMaker(CommonUtils.jsonToSearchPolicySearchDto(payload));
//		logger.info("AdditionOfMemberController:sendToMaker:ended");
//		return commonResponseDto;
//	}
//	
//	
//	
//	@PostMapping("/uploadDocument")
//	public CommonResponseDto uploadDocument(@RequestBody PolicyServiceDocumentDto docsDto) {
//		logger.info("AdditionOfMemberController:uploadDocument:started");
//		CommonResponseDto commonDto = additionOfMemberService.uploadDocument(docsDto);
//		logger.info("AdditionOfMemberController:uploadDocument:ended");
//		return commonDto;
//	}
//	
//	
//	@PostMapping("/removeDocument")
//	public CommonResponseDto removeDocument(@RequestBody PolicyServiceDocumentDto docsDto) {
//		logger.info("AdditionOfMemberController:removeDocument:started");
//		CommonResponseDto commonDto = additionOfMemberService.removeDocument(docsDto);
//		logger.info("AdditionOfMemberController:removeDocument:ended");
//		return commonDto;
//	}
//	
//	
//	
//	
//	
//	
	
	
	
	
	
	
	
	@GetMapping("bulk/download")
	public void download(@RequestParam Long batchId, @RequestParam String fileType, HttpServletResponse response){
		logger.info("PolicyLevelMemberAdditionController -- download --started");
		additionOfMemberService.bulkDownload(batchId,fileType,response);
		logger.info("PolicyLevelMemberAdditionController -- download --ended");
	}
	
	
	
	
	
	
	
	
	


}

