package com.lic.epgs.quotation.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.quotation.dto.QuotationFinancialResponseDto;
import com.lic.epgs.quotation.dto.QuotationMemberApiResponseDto;
import com.lic.epgs.quotation.dto.QuotationMemberBulkResponseDto;
import com.lic.epgs.quotation.dto.QuotationMemberDto;

public interface QuotationMemberService {

	QuotationMemberApiResponseDto saveQuotationMember(QuotationMemberDto quotationMemberDto);

	QuotationMemberApiResponseDto getQuotationMember(Long quotationMemberId, Integer quotationId);

	QuotationMemberApiResponseDto getQuotationMembers(Integer quotationId, String pageName);

	QuotationMemberBulkResponseDto saveBulkQuotationMember(Integer quotationId, MultipartFile file)
			throws IllegalStateException, IOException;
	
	void download(Long batchId, String fileType, HttpServletResponse response);
	QuotationMemberApiResponseDto getBatches(Long batchId);
	QuotationMemberApiResponseDto getBatchIdList();
	

	QuotationFinancialResponseDto getFinancialCalculation(Integer quotationId);
	
	
	
//	ram prasad codes
	QuotationMemberApiResponseDto deleteAddressesDetails(Long addressId, Long memberId);
	QuotationMemberApiResponseDto deleteNomineeDetails(Long nomineeId,Long memberId);

	


	

}
