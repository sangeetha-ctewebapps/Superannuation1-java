package com.lic.epgs.quotation.service;

import com.lic.epgs.common.dto.CommonChallanDto;
import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.quotation.dto.QuotationApiResponseDto;
import com.lic.epgs.quotation.dto.QuotationDto;
import com.lic.epgs.quotation.dto.QuotationSearchDto;

public interface QuotationService {

	QuotationApiResponseDto saveQuotation(QuotationDto quotationDto);

	QuotationApiResponseDto getQuotations();

	QuotationApiResponseDto getQuotationById(QuotationSearchDto dto);

	QuotationApiResponseDto existingCitrieaSearch(QuotationSearchDto quotationSearchDto);

	QuotationApiResponseDto inprogressCitrieaSearch(QuotationSearchDto quotationSearchDto);

	QuotationApiResponseDto sendToCheker(Integer quotationId, String quotationStatus);
	
	QuotationApiResponseDto sendToMaker(Integer quotationId, String quotationStatus);
	
	QuotationApiResponseDto sendToApprover(Integer quotationId, String quotationStatus);
	
	QuotationApiResponseDto approveOrReject(QuotationDto dto);
	
	QuotationApiResponseDto searchChecker (String quotationStatus);
	
	QuotationApiResponseDto getQuotationByProposalNumber(String proposalNumber);

	QuotationApiResponseDto sendToActuary(QuotationSearchDto dto);

	QuotationApiResponseDto uploadDocument(CommonDocsDto docsDto);

	QuotationApiResponseDto getQuotationOtherCriteria(QuotationSearchDto quotationSearchDto);

	QuotationApiResponseDto generateChellanForQuotation(CommonChallanDto commonChallanDto);

	QuotationApiResponseDto getChellanNumber(CommonChallanDto dto);

	QuotationApiResponseDto removeDocumentByremoveDocumentId(String docId, String quotationId);

	QuotationApiResponseDto getDocumentDetails(Integer quotationId);

	QuotationApiResponseDto activateQuotation(QuotationDto dto);
	
	QuotationApiResponseDto getMatrix(Long policyId,String Servicetype);
}