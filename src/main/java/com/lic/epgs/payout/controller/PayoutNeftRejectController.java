package com.lic.epgs.payout.controller;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.payout.dto.PayoutDto;
import com.lic.epgs.payout.dto.PayoutNotesDto;
import com.lic.epgs.payout.dto.PayoutPayeeBankDetailsDto;
import com.lic.epgs.payout.dto.PayoutSearchRequestDto;
import com.lic.epgs.payout.dto.PayoutSearchResponseDto;
import com.lic.epgs.payout.service.PayoutNeftRejectService;
import com.lic.epgs.payout.temp.entity.PayoutPayeeBankDetailsTempEntity;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payout/neft/reject")
public class PayoutNeftRejectController {

	@Autowired
	PayoutNeftRejectService payoutNeftRejectService;

	protected final Logger logger = LogManager.getLogger(getClass());

	@PostMapping("/note/add")
	public ApiResponseDto<Set<PayoutNotesDto>> add(@RequestBody PayoutNotesDto request) {
		logger.info("PayoutNeftRejectController -- add --started");
		ApiResponseDto<Set<PayoutNotesDto>> responseDto = payoutNeftRejectService.addTempNotes(request);
		logger.info("PayoutNeftRejectController -- add --ended");
		return responseDto;
	}

	@GetMapping("/note/get/{payoutNo}")
	public ApiResponseDto<Set<PayoutNotesDto>> getTempNotesByPayoutNo(@PathVariable String payoutNo) {
		logger.info("PayoutNeftRejectController -- getTempNotesByPayoutNo --started");
		ApiResponseDto<Set<PayoutNotesDto>> responseDto = payoutNeftRejectService.getTempNotesByPayoutNo(payoutNo);
		logger.info("PayoutNeftRejectController -- getTempNotesByPayoutNo --ended");
		return responseDto;
	}

	@GetMapping("/note/get/existing/{payoutNo}")
	public ApiResponseDto<Set<PayoutNotesDto>> getNotesByPayoutNo(@PathVariable String payoutNo) {
		logger.info("PayoutNeftRejectController -- getNotesByPayoutNo --started");
		ApiResponseDto<Set<PayoutNotesDto>> responseDto = payoutNeftRejectService.getNotesByPayoutNo(payoutNo);
		logger.info("PayoutNeftRejectController -- getNotesByPayoutNo --ended");
		return responseDto;
	}

	@PostMapping("/existing/payout")
	public ApiResponseDto<List<PayoutSearchResponseDto>> existingfindPayoutNeftDetails(
			@RequestBody PayoutSearchRequestDto request) {
		logger.info("PayoutNeftRejectController :: existingfindPayoutNeftDetails :: Start");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = payoutNeftRejectService
				.existingfindPayoutNeftDetails(request);
		logger.info("PayoutNeftRejectController :: existingfindPayoutNeftDetails :: Ends");
		return responseDto;
	}

	@GetMapping("/fetch/temp")
	public ApiResponseDto<PayoutDto> findTempByPayoutNo(@RequestParam(defaultValue = "") String payoutNo) {
		logger.info("PayoutNeftRejectController :: findTempByPayoutNo :: Start -- PayOut No: {},", payoutNo);
		ApiResponseDto<PayoutDto> responseDto = payoutNeftRejectService.findTempPayoutDetails(payoutNo);
		logger.info("PayoutNeftRejectController :: findTempByPayoutNo :: Ends -- PayOut No: {},", payoutNo);
		return responseDto;
	}

	@GetMapping("/fetch")
	public ApiResponseDto<PayoutDto> findByPayoutNo(@RequestParam(defaultValue = "") String payoutNo) {
		logger.info("PayoutNeftRejectController :: findByPayoutNo :: Start -- PayOut No: {},", payoutNo);
		ApiResponseDto<PayoutDto> responseDto = payoutNeftRejectService.findPayoutDetails(payoutNo);
		logger.info("PayoutNeftRejectController :: findByPayoutNo :: Ends -- PayOut No: {},", payoutNo);
		return responseDto;
	}

	@PostMapping("/edit/PayeeBank")
	public ApiResponseDto<PayoutPayeeBankDetailsDto> editTempPayoutPayeeBankValueDetails(
			@RequestParam(defaultValue = "") String payoutNo, @RequestBody PayoutPayeeBankDetailsDto request) {
		logger.info("PayoutNeftRejectController :: editTempPayoutPayeeBankValueDetails :: Start -- PayOut No: {},",
				payoutNo);
		PayoutPayeeBankDetailsTempEntity newEntity = new PayoutPayeeBankDetailsTempEntity();
		BeanUtils.copyProperties(request, newEntity);
		ApiResponseDto<PayoutPayeeBankDetailsDto> responseDto = payoutNeftRejectService
				.editTempPayoutPayeeBankValueDetails(payoutNo, newEntity);
		logger.info("PayoutNeftRejectController :: editTempPayoutPayeeBankValueDetails :: Ends -- PayOut No: {},",
				payoutNo);
		return responseDto;
	}

	@PostMapping("/senttoapprove")
	public ApiResponseDto<PayoutDto> sentToApprove(@RequestParam(defaultValue = "") String payoutNo) {
		logger.info("PayoutNeftRejectController :: sentToApprove :: Start -- PayOut No: {},", payoutNo);
		ApiResponseDto<PayoutDto> responseDto = payoutNeftRejectService.sentToApprove(payoutNo);
		logger.info("PayoutNeftRejectController :: sentToApprove :: Ends -- PayOut No: {},", payoutNo);
		return responseDto;
	}

	@PostMapping("/senttochecker")
	public ApiResponseDto<PayoutDto> sentToChecker(@RequestParam(defaultValue = "") String payoutNo) {
		logger.info("PayoutNeftRejectController :: sentToChecker :: Start -- PayOut No: {},", payoutNo);
		ApiResponseDto<PayoutDto> responseDto = payoutNeftRejectService.sentToChecker(payoutNo);
		logger.info("PayoutNeftRejectController :: sentToChecker :: Ends -- PayOut No: {},", payoutNo);
		return responseDto;
	}

	@PostMapping("/senttomaker")
	public ApiResponseDto<PayoutDto> sentToMaker(@RequestParam(defaultValue = "") String payoutNo) {
		logger.info("PayoutNeftRejectController :: sentToMaker :: Start -- PayOut No: {},", payoutNo);
		ApiResponseDto<PayoutDto> responseDto = payoutNeftRejectService.sentToMaker(payoutNo);
		logger.info("PayoutNeftRejectController :: sentToMaker :: Ends -- PayOut No: {},", payoutNo);
		return responseDto;
	}

	@PostMapping("/senttoreject")
	public ApiResponseDto<PayoutDto> sentToReject(@RequestParam(defaultValue = "") String payoutNo) {
		logger.info("PayoutNeftRejectController :: sentToReject :: Start -- PayOut No: {},", payoutNo);
		ApiResponseDto<PayoutDto> responseDto = payoutNeftRejectService.sentToReject(payoutNo);
		logger.info("PayoutNeftRejectController :: sentToReject :: Ends -- PayOut No: {},", payoutNo);
		return responseDto;
	}

	@PostMapping("/inprogress")
	public ApiResponseDto<List<PayoutSearchResponseDto>> inprogress(@RequestBody PayoutSearchRequestDto request) {
		logger.info("PayoutNeftRejectController :: inprogress :: Start ");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = payoutNeftRejectService.inprogress(request);
		logger.info("PayoutNeftRejectController :: inprogress :: Ends");
		return responseDto;
	}

	@GetMapping("/inprogress/{payoutNo}")
	public ApiResponseDto<List<PayoutSearchResponseDto>> inprogressByPayoutNo(@PathVariable String payoutNo) {
		logger.info("PayoutNeftRejectController :: inprogressByPayoutNo :: Start -- PayOut No: {},", payoutNo);
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = payoutNeftRejectService
				.inprogressByPayoutNo(payoutNo);
		logger.info("PayoutNeftRejectController :: inprogressByPayoutNo :: Ends -- PayOut No: {},", payoutNo);
		return responseDto;
	}

	@PostMapping("/existing")
	public ApiResponseDto<List<PayoutSearchResponseDto>> existing(@RequestBody PayoutSearchRequestDto request) {
		logger.info("PayoutNeftRejectController :: existing :: Start");
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = payoutNeftRejectService.existing(request);
		logger.info("PayoutNeftRejectController :: existing :: Ends");
		return responseDto;
	}

	@GetMapping("/existing/{payoutNo}")
	public ApiResponseDto<List<PayoutSearchResponseDto>> existingByPayoutNo(@PathVariable String payoutNo) {
		logger.info("PayoutNeftRejectController :: existingByPayoutNo :: Start -- PayOut No: {},", payoutNo);
		ApiResponseDto<List<PayoutSearchResponseDto>> responseDto = payoutNeftRejectService
				.existingByPayoutNo(payoutNo);
		logger.info("PayoutNeftRejectController :: existingByPayoutNo :: Ends -- PayOut No: {},", payoutNo);
		return responseDto;
	}

}