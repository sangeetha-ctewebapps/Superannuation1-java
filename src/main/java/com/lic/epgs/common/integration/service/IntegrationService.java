/**
 * 
 */
package com.lic.epgs.common.integration.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionBulkResponseDto;
import com.lic.epgs.claim.dto.ClaimMbrBulkResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.integration.dto.AnnuityModeResponse;
import com.lic.epgs.common.integration.dto.AnnuityOptionalResponse;
import com.lic.epgs.common.integration.dto.AnnuityRealTimeData;
import com.lic.epgs.quotation.dto.QuotationMemberBulkResponseDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionBulkResponseDto;
import com.lic.epgs.common.integration.dto.DepositReceiptGenerationDto;
import com.lic.epgs.common.integration.dto.StateDetailsDto;

import com.lic.epgs.common.integration.dto.DepositCollectBulkUploadResponseDto;

/**
 * @author Muruganandam
 *
 */
public interface IntegrationService {
//	Object getMphAndIcodeDetail(MphAndIcodeRequestDto requestDto);
	QuotationMemberBulkResponseDto bulkMemberUpload(Integer quotationId, MultipartFile file);
	QuotationMemberBulkResponseDto removeBatchForQuotation(Long batchId, Integer quotationId);

	
	
	QuotationMemberBulkResponseDto saveBulkForAom(Long serviceId, String serviceNumber, String unitCode, String policyNumber,String createdBy,
			 									  Long memberAdditionId,MultipartFile file)throws IllegalStateException, IOException;
	QuotationMemberBulkResponseDto removeBatchForAom(Long batchId, Long policyId);
	void downloadForAom(Long batchId, String fileType, HttpServletResponse response);
	
	
	
	ClaimMbrBulkResponseDto saveBulkMemberForClaim(Long policyId,String createdBy,String unitCode, MultipartFile file);
	CommonResponseDto getAllBatches();
	CommonResponseDto getAllBathMembers();
	CommonResponseDto removeClaimMembers(Long batchId);
	CommonResponseDto getBatchAssociateData(Long batchId);
	CommonResponseDto getAllDataByPolicyNo(String masterPolicyNo);
	
	
	
	AdjustmentContributionBulkResponseDto bulkUploadForAdjustmentContribution(Long masterPolicyId, Long tempPolicyId, String createdBy,
			String adjustmentContributionId,String unitCode, MultipartFile file);
	AdjustmentContributionBulkResponseDto removeBatchForAdjustmentContribution(Long batchId,Long adjustmentContributionId,Long policyId,String unitCode,
			String modifiedBy);
	
	
	
	
//	RegularAdjustmentContributionBulkResponseDto bulkUploadForRegularAdjustmentContribution(Long masterPolicyId, Long tempPolicyId, String createdBy, String regularContributionId,String unitCode, MultipartFile file);
	RegularAdjustmentContributionBulkResponseDto removeBatchForRegularAdjustmentContribution(Long batchId,Long regularContributionId,Long policyId,String unitCode,
			String modifiedBy);
	
	RegularAdjustmentContributionBulkResponseDto bulkUploadForRegularAdjustmentContribution(Long masterPolicyId,
			Long tempPolicyId, String createdBy, String regularContributionId, String unitCode, MultipartFile file);


	AnnuityOptionalResponse getAnnuityOptionReponse();
	
	
	AnnuityModeResponse getAnnuityModeReponse();
	
	JsonNode getProposalDetailsByProposalNumber(String proposalNumber) throws MalformedURLException, ProtocolException, IOException;	
	JsonNode getProductDetailsByProductId(Long productId) throws MalformedURLException, ProtocolException, IOException;
	JsonNode getVariantDetailsByProductVariantId(Long productVariantId) throws IOException;
	
	
	
	AnnuityRealTimeData callAnnuityRealtimeApi(String policyNumber) throws JsonMappingException, JsonProcessingException;
	
	JsonNode getGstStateDetails(String unitCode) throws IOException;

	DepositReceiptGenerationDto getDepositReceiptGenaration(String receiptNumber,Boolean isProposalDeposit,String policyNumber) throws IOException;

	StateDetailsDto getStatedetailsBystateId(Integer stateId);
	JsonNode getStateCodeAndStateName(String unitCode) throws IOException;
	
	
	DepositCollectBulkUploadResponseDto depositCollectBulk(MultipartFile file);

}
