/**
 * 
 */
package com.lic.epgs.common.integration.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionBulkResponseDto;
import com.lic.epgs.claim.dto.ClaimMbrBulkResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.integration.dto.AnnuityModeResponse;
import com.lic.epgs.common.integration.dto.AnnuityOptionalResponse;
import com.lic.epgs.common.integration.dto.AnnuityRealTimeData;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.payout.restapi.dto.PayoutAddressRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutAnnuityRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutAnnuityRestApiRes;
import com.lic.epgs.payout.restapi.dto.PayoutBankRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutFinancialDetailRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutJointDetailRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutMbrPrslDtRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutNomineeDtlRestApiRequest;
import com.lic.epgs.quotation.dto.QuotationMemberBulkResponseDto;
import com.lic.epgs.regularadjustmentcontribution.dto.RegularAdjustmentContributionBulkResponseDto;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.common.integration.dto.DepositReceiptGenerationDto;
import org.springframework.beans.factory.annotation.Value;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.common.integration.dto.StateDetailsDto;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.repository.PolicyMasterRepository;

import com.lic.epgs.common.integration.dto.DepositCollectBulkUploadResponseDto;

/**
 * @author Muruganandam
 *
 */
@Service
public class IntegrationServiceImpl implements IntegrationService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private Environment environment;

	@Autowired
	@Qualifier(value = "restTemplateService")
	private RestTemplate restTemplateService;
	
	@Value("${DEPOSITRECEIPTGENERATION}")
	private String depositReceiptGenaration;
	
	@Autowired
	MphMasterRepository mphMasterRepository;
	
	@Autowired
	PolicyMasterRepository policyMasterRepository;


	public HttpHeaders getAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CommonConstants.AUTHORIZATION, "Bearer ");
		headers.add(CommonConstants.CORELATIONID, UUID.randomUUID().toString());
		headers.add(CommonConstants.BUSINESSCORELATIONID, UUID.randomUUID().toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

//	@Override
//	public Object getMphAndIcodeDetail(MphAndIcodeRequestDto requestDto) {
//		try {
//			String url = environment.getProperty("SA_GETMPHANDICODEDETAIL");
//			if (StringUtils.isNotBlank(url)) {
//				HttpHeaders headers = getAuthHeaders();
//				url = url + requestDto.getPolicyNo();
//				return restTemplateService.exchange(url, HttpMethod.GET, null, String.class, headers).getBody();
//				// return
//				// "{\"responseData\":{\"portfolioData\":{\"icodeForLob\":101,\"icodeForProductLine\":10,\"icodeForVariant\":\"Group
//				// Superannuation Cash Accumulation (DC)
//				// Scheme-V1\",\"icodeForBusinessType\":1,\"icodeForParticipatingType\":2,\"icodeForBusinessSegment\":13,\"icodeForInvestmentPortfolio\":12},\"servicingUnitDetail\":{\"servicingUnitName\":\"MUMBAI\",\"servicingUnitAddress1\":\"1st
//				// floor Yogakshema\",\"servicingUnitAddress2\":\"Jeevan Bima
//				// Marg\",\"servicingUnitAddress3\":\"PandGS
//				// Deptt\",\"servicingUnitAddress4\":\"Mumbai\",\"servicingUnitCity\":\"Mumbai\",\"servicingUnitPincode\":\"400021\",\"servicingUnitEmail\":null,\"servicingUnitPhoneNo\":null,\"operatingUnitType\":\"MUMBAI\",\"unitCode\":\"G706\"},\"mphdata\":{\"schemeName\":\"Superannuation\",\"mphCode\":\"C00000363\",\"mphName\":\"Akila\",\"mphNo\":\"P00000379\",\"mphMobileNo\":7856341211,\"mphEmail\":\"log@gmail.com\",\"mphAddress1\":\"Chennai\",\"mphAddress2\":null,\"district\":\"119\",\"state\":\"8\",\"pinCode\":600119}},\"transactionMessage\":\"Fetched
//				// successfully\",\"transactionStatus\":\"success\"}";
//			}
//		} catch (IllegalArgumentException e) {
//			logger.info("CommonServiceImpl-address-Error:", e);
//		} 
//		return "{\"responseData\":{},\"transactionMessage\":\"No Record Found for the given Policy No.\",\"transactionStatus\":\"error\"}";
//	}

	@Override
	public QuotationMemberBulkResponseDto bulkMemberUpload(Integer quotationId, MultipartFile file) {
		QuotationMemberBulkResponseDto bulkResponseDto = new QuotationMemberBulkResponseDto();
		try {
			String sparkUrl = environment.getProperty("SA_BULK_MEMBER_UPLOAD_SPARK_FOR_QUOTATION");
			bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
			logger.info("IntegrationServiceImpl-bulkMemberUpload-Start");

			byte[] fileContent = null;
			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {
				logger.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
				bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
				bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
				return bulkResponseDto;
			}

			String filename = file.getOriginalFilename();

			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();

			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);
			body.add("quotationId", quotationId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			ResponseEntity<QuotationMemberBulkResponseDto> responseEntity = restTemplateService.postForEntity(sparkUrl,
					requestEntity, QuotationMemberBulkResponseDto.class);

			if (responseEntity.getBody() != null) {
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
		}
		return bulkResponseDto;
	}

	@Override
	public QuotationMemberBulkResponseDto removeBatchForQuotation(Long batchId, Integer quotationId) {
		QuotationMemberBulkResponseDto bulkResponseDto = new QuotationMemberBulkResponseDto();
		try {

			logger.info("IntegrationServiceImpl-removeBatch-Start");
			String sparkUrl = environment.getProperty("SA_REMOVE_BATCH_FOR_QUOTATION") + "?batchId=" + batchId
					+ "&quotationId=" + quotationId;
			bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
			ResponseEntity<QuotationMemberBulkResponseDto> responseEntity = restTemplateService.getForEntity(sparkUrl,
					QuotationMemberBulkResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-removeBatch-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-removeBatch-Error:", e);
		}
		logger.info("IntegrationServiceImpl-removeBatch-End");
		return bulkResponseDto;
	}

	@Override
	public QuotationMemberBulkResponseDto saveBulkForAom(Long serviceId, String serviceNumber, String unitCode, String policyNumber,String createdBy,
														 Long memberAdditionId,MultipartFile file)throws IllegalStateException, IOException {

		QuotationMemberBulkResponseDto bulkResponseDto = new QuotationMemberBulkResponseDto();
		try {
			String sparkUrl = environment.getProperty("SA_BULK_MEMBER_UPLOAD_SPARK_FOR_AOM");
			bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
			logger.info("IntegrationServiceImpl-saveBulkForAom-Start");

			byte[] fileContent = null;
			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {
				logger.info("IntegrationServiceImpl-saveBulkForAom-Error:", e);
				bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
				bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
				return bulkResponseDto;
			}

			String filename = file.getOriginalFilename();

			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();

			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("serviceId", serviceId);
			body.add("serviceNumber", serviceNumber);
			body.add("unitCode", unitCode);
			body.add("policyNumber", policyNumber);
			body.add("createdBy", createdBy);
			body.add("memberAdditionId", memberAdditionId);
			body.add("file", fileEntity);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			ResponseEntity<QuotationMemberBulkResponseDto> responseEntity = restTemplateService.postForEntity(sparkUrl,
					requestEntity, QuotationMemberBulkResponseDto.class);

			if (responseEntity.getBody() != null) {
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-saveBulkForAom-Error:", e);
		}
		return bulkResponseDto;
	}

	@Override
	public QuotationMemberBulkResponseDto removeBatchForAom(Long batchId, Long policyId) {
		QuotationMemberBulkResponseDto response = new QuotationMemberBulkResponseDto();
		try {

			logger.info("IntegrationServiceImpl-removeBatch-Start");
			String sparkUrl = environment.getProperty("SA_REMOVE_BATCH_FOR_AOM") + "?batchId=" + batchId + "&policyId="
					+ policyId;
			response.setTransactionStatus(CommonConstants.ERROR);
			ResponseEntity<QuotationMemberBulkResponseDto> responseEntity = restTemplateService.getForEntity(sparkUrl,
					QuotationMemberBulkResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-removeBatch-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-removeBatch-Error:", e);
		}
		logger.info("IntegrationServiceImpl-removeBatch-End");
		return response;
	}
	
	
	
	@Override
	public void downloadForAom(Long batchId, String fileType, HttpServletResponse response) {
		try {
			logger.info("IntegrationServiceImpl-removeBatch-Start");
			String sparkUrl = environment.getProperty("DOWNLOAD_BATCH_FOR_AOM") + "?batchId=" + batchId + "&fileType="+ fileType+"&response"+response;

			restTemplateService.getForEntity(sparkUrl,QuotationMemberBulkResponseDto.class);

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-removeBatch-Error:", e);
		}
		logger.info("IntegrationServiceImpl-removeBatch-End");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public ClaimMbrBulkResponseDto saveBulkMemberForClaim(Long policyId, String createdBy, String unitCode, MultipartFile file) {
		ClaimMbrBulkResponseDto bulkResponseDto = new ClaimMbrBulkResponseDto();
		try {
			logger.info("IntegrationServiceImpl-saveBulkMemberForClaim-Start");

			String sparkUrl = environment.getProperty("SA_BULK_MEMBER_UPLOAD_FOR_CLAIM");
			bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);

			byte[] fileContent = null;
			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {
				logger.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
				bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
				bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
				return bulkResponseDto;
			}

			String filename = file.getOriginalFilename();

			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();

			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("policyId", policyId);
			body.add("createdBy", createdBy);
			body.add("unitCode", unitCode);
			body.add("file", fileEntity);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			ResponseEntity<ClaimMbrBulkResponseDto> responseEntity = restTemplateService.postForEntity(sparkUrl,
					requestEntity, ClaimMbrBulkResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-saveBulkMemberForClaim-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-saveBulkMemberForClaim-Error:", e);
		}

		logger.info("IntegrationServiceImpl-saveBulkMemberForClaim-End");
		return bulkResponseDto;
	}

	@Override
	public CommonResponseDto getAllBatches() {
		CommonResponseDto response = new CommonResponseDto();
		try {

			logger.info("IntegrationServiceImpl-getAllBatches-Start");
			String sparkUrl = environment.getProperty("SA_BULK_GET_ALL_BATCHES_FOR_CLAIM");
			response.setTransactionStatus(CommonConstants.ERROR);
			ResponseEntity<CommonResponseDto> responseEntity = restTemplateService.getForEntity(sparkUrl,
					CommonResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-getAllBatches-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-getAllBathes-Error:", e);
		}
		logger.info("IntegrationServiceImpl-getAllBatches-End");
		return response;
	}

	@Override
	public CommonResponseDto getAllBathMembers() {
		CommonResponseDto response = new CommonResponseDto();
		try {

			logger.info("IntegrationServiceImpl-getAllBathMembers-Start");
			String sparkUrl = environment.getProperty("SA_BULK_GET_ALL_BATCH_MEMBERS_FOR_CLAIM");
			response.setTransactionStatus(CommonConstants.ERROR);
			ResponseEntity<CommonResponseDto> responseEntity = restTemplateService.getForEntity(sparkUrl,
					CommonResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-getAllBathMembers-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-getAllBathMembers-Error:", e);
		}
		logger.info("IntegrationServiceImpl-getAllBathMembers-End");
		return response;
	}

	@Override
	public CommonResponseDto removeClaimMembers(Long batchId) {
		CommonResponseDto response = new CommonResponseDto();
		try {

			logger.info("IntegrationServiceImpl-removeClaimMembers-Start");
			String sparkUrl = environment.getProperty("SA_BULK_REMOVE_MEMBERS_FOR_CLAIM") + "?batchId=" + batchId;
			response.setTransactionStatus(CommonConstants.ERROR);
			ResponseEntity<CommonResponseDto> responseEntity = restTemplateService.getForEntity(sparkUrl,
					CommonResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-removeClaimMembers-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-removeClaimMembers-Error:", e);
		}
		logger.info("IntegrationServiceImpl-removeClaimMembers-End");
		return response;
	}

	@Override
	public CommonResponseDto getBatchAssociateData(Long batchId) {
		CommonResponseDto response = new CommonResponseDto();
		try {

			logger.info("IntegrationServiceImpl-removeClaimMembers-Start");
			String sparkUrl = environment.getProperty("SA_BULK_GET_ALL_MEMBERS_BY_BATCHID_FOR_CLAIM") + "?batchId="
					+ batchId;
			response.setTransactionStatus(CommonConstants.ERROR);
			ResponseEntity<CommonResponseDto> responseEntity = restTemplateService.getForEntity(sparkUrl,
					CommonResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-removeClaimMembers-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-removeClaimMembers-Error:", e);
		}
		logger.info("IntegrationServiceImpl-removeClaimMembers-End");
		return response;
	}
	
	
	
	
	@Override
	public CommonResponseDto getAllDataByPolicyNo(String masterPolicyNo) {
		CommonResponseDto response = new CommonResponseDto();
		try {

			logger.info("IntegrationServiceImpl-getAllDataByPolicyNo-Start");
			String sparkUrl = environment.getProperty("SA_BULK_GET_ALL_DATA_BY_POLICY_NO_FOR_CLAIM") + "?masterPolicyNo="+ masterPolicyNo;
			response.setTransactionStatus(CommonConstants.ERROR);
			ResponseEntity<CommonResponseDto> responseEntity = restTemplateService.getForEntity(sparkUrl,CommonResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-getAllDataByPolicyNo-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-getAllDataByPolicyNo-Error:", e);
		}
		logger.info("IntegrationServiceImpl-getAllDataByPolicyNo-End");
		return response;
	}
	

	@Override
	public AdjustmentContributionBulkResponseDto bulkUploadForAdjustmentContribution(Long masterPolicyId, Long tempPolicyId, String createdBy, 
			String adjustmentContributionId, String unitCode,MultipartFile file) {
		AdjustmentContributionBulkResponseDto bulkResponseDto = new AdjustmentContributionBulkResponseDto();
		try {
			logger.info("IntegrationServiceImpl-bulkUploadForAdjustmentContribution-Start");
			
			String sparkUrl = environment.getProperty("SA_BULK_MEMBER_UPLOAD_FOR_ADJUSTMENT_CONTRIBUTION");
			bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
			

			byte[] fileContent = null;
			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {				
				logger.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
				bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
				bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
				return bulkResponseDto;
			}

			String filename = file.getOriginalFilename();

			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();

			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("masterPolicyId", masterPolicyId);
			body.add("tempPolicyId", tempPolicyId);
			body.add("file", fileEntity);
			body.add("createdBy", createdBy);
			body.add("adjustmentContributionId", adjustmentContributionId);
			body.add("unitCode", unitCode);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			ResponseEntity<AdjustmentContributionBulkResponseDto> responseEntity = restTemplateService.postForEntity(sparkUrl,
					requestEntity, AdjustmentContributionBulkResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-bulkUploadForAdjustmentContribution-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-bulkUploadForAdjustmentContribution-Error:", e);
		}
		
		logger.info("IntegrationServiceImpl-bulkUploadForAdjustmentContribution-End");
		return bulkResponseDto;
	}

	@Override
	public AdjustmentContributionBulkResponseDto removeBatchForAdjustmentContribution(Long batchId,Long adjustmentContributionId,Long policyId,String unitCode,
			String modifiedBy) {
		AdjustmentContributionBulkResponseDto response = new AdjustmentContributionBulkResponseDto();
		try {

			logger.info("IntegrationServiceImpl-removeBatchForAdjustmentContribution-Start");
			String sparkUrl = environment.getProperty("SA_REMOVE_BATCH_FOR_ADJUSTMENT_CONTRIBUTION") + "?batchId="
					+ batchId+"&adjustmentContributionId="+adjustmentContributionId+"&policyId="+policyId+"&unitCode="+unitCode+"&modifiedBy="+modifiedBy;
			
			response.setTransactionStatus(CommonConstants.ERROR);
			ResponseEntity<AdjustmentContributionBulkResponseDto> responseEntity = restTemplateService.getForEntity(sparkUrl, AdjustmentContributionBulkResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-removeBatchForAdjustmentContribution-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-removeBatchForAdjustmentContribution-Error:", e);
		}
		logger.info("IntegrationServiceImpl-removeBatchForAdjustmentContribution-End");
		return response;
	}

	@Override
	public RegularAdjustmentContributionBulkResponseDto bulkUploadForRegularAdjustmentContribution(Long masterPolicyId, Long tempPolicyId,
			String createdBy, String regularContributionId, String unitCode, MultipartFile file) {
		RegularAdjustmentContributionBulkResponseDto bulkResponseDto = new RegularAdjustmentContributionBulkResponseDto();
		try {
			logger.info("IntegrationServiceImpl-bulkUploadForRegularAdjustmentContribution-Start");
			
			String sparkUrl = environment.getProperty("SA_BULK_MEMBER_UPLOAD_FOR_REGULAR_ADJUSTMENT_CONTRIBUTION");
			bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
			

			byte[] fileContent = null;
			
			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {				
				logger.info("IntegrationServiceImpl-bulkMemberUpload-Error:", e);
				bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
				bulkResponseDto.setTransactionStatus(CommonConstants.ERROR);
				return bulkResponseDto;
			}

			String filename = file.getOriginalFilename();

			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();

			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);
			body.add("masterPolicyId", masterPolicyId);
			body.add("tempPolicyId", tempPolicyId);
			body.add("createdBy", createdBy);
			body.add("regularContributionId", regularContributionId);
			body.add("unitCode", unitCode);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			ResponseEntity<RegularAdjustmentContributionBulkResponseDto> responseEntity = restTemplateService.postForEntity(sparkUrl,
					requestEntity, RegularAdjustmentContributionBulkResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-bulkUploadForRegularAdjustmentContribution-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-bulkUploadForRegularAdjustmentContribution-Error:", e);
		}
		
		logger.info("IntegrationServiceImpl-bulkUploadForRegularAdjustmentContribution-End");
		return bulkResponseDto;
	}

	@Override
	public RegularAdjustmentContributionBulkResponseDto removeBatchForRegularAdjustmentContribution(Long batchId,Long regularContributionId,Long policyId,String unitCode,
			String modifiedBy) {
		RegularAdjustmentContributionBulkResponseDto response = new RegularAdjustmentContributionBulkResponseDto();
		try {

			logger.info("IntegrationServiceImpl-removeBatchForRegularAdjustmentContribution-Start");
			String sparkUrl = environment.getProperty("SA_REMOVE_BATCH_FOR_REGULAR_ADJUSTMENT_CONTRIBUTION")
					+ "?batchId=" + batchId+"&regularContributionId="+regularContributionId+"&policyId="+policyId+"&unitCode="+unitCode+"&modifiedBy="+modifiedBy;
			response.setTransactionStatus(CommonConstants.ERROR);
			ResponseEntity<RegularAdjustmentContributionBulkResponseDto> responseEntity = restTemplateService
					.getForEntity(sparkUrl, RegularAdjustmentContributionBulkResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-removeBatchForRegularAdjustmentContribution-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-removeBatchForRegularAdjustmentContribution-Error:", e);
		}
		logger.info("IntegrationServiceImpl-removeBatchForRegularAdjustmentContribution-End");
		return response;
	}

//	private static PayoutAnnuityRestApiRes annutityRestApi() {
//		PayoutAnnuityRestApiRes payoutAnnuityRestApiRes = new PayoutAnnuityRestApiRes();
//		try {
//
//			Set<PayoutAddressRestApiRequest> addressRestApiRequests = new HashSet<>();
//			PayoutBankRestApiRequest bankRestApiRequest = new PayoutBankRestApiRequest();
//			PayoutFinancialDetailRestApiRequest financialDetailRestApiRequest = new PayoutFinancialDetailRestApiRequest();
//			PayoutJointDetailRestApiRequest payoutJointDetailRestApiRequest = new PayoutJointDetailRestApiRequest();
//			Set<PayoutNomineeDtlRestApiRequest> payoutNomineeDtlRestApiRequests = new HashSet<>();
//			PayoutMbrPrslDtRestApiRequest payoutMbrPrslDtRestApiRequest = new PayoutMbrPrslDtRestApiRequest();
//
//			PayoutAnnuityRestApiRequest payoutAnnuityRestApiReq = new PayoutAnnuityRestApiRequest();
//			payoutAnnuityRestApiReq.setPolicyNumber("101000001043");
//			payoutAnnuityRestApiReq.setSourceCode("Super_Annuation_Claims_Annuities");
//			payoutAnnuityRestApiReq.setUploadedBy("SA");
//			payoutAnnuityRestApiReq.setUploadedOn("31/01/2023");
//			payoutAnnuityRestApiReq.setAnAddressDtlReqList(addressRestApiRequests);
//			payoutAnnuityRestApiReq.setAnBankDetailRequest(bankRestApiRequest);
//			payoutAnnuityRestApiReq.setAnFinancialDetailRequest(financialDetailRestApiRequest);
//			payoutAnnuityRestApiReq.setAnJointDetailRequest(payoutJointDetailRestApiRequest);
//			payoutAnnuityRestApiReq.setAnNomineeDtlRequestList(payoutNomineeDtlRestApiRequests);
//			payoutAnnuityRestApiReq.setAntPrslDtlRequest(payoutMbrPrslDtRestApiRequest);
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			String annuityUrl = "https://d1stvrrpgca01.licindia.com:8443/annuitynbservice/LIC_ePGS/saveComAnnuityCreationService";
//			HttpEntity<PayoutAnnuityRestApiRequest> requestEntity = new HttpEntity<>(payoutAnnuityRestApiReq, headers);
//			payoutAnnuityRestApiRes.setMessage("Skip Annuity API call");
//			if (payoutAnnuityRestApiReq != null) {
//				ObjectMapper mapper = new ObjectMapper();
//				mapper.writerWithDefaultPrettyPrinter();
//
//				RestTemplate restTemplate = new RestTemplate();
//				System.out.println("AnnuityRequest: jsontoString  ============"
//						+ mapper.writeValueAsString(payoutAnnuityRestApiReq));
//				ResponseEntity<PayoutAnnuityRestApiRes> responseEntity = restTemplate.postForEntity(annuityUrl,
//						requestEntity, PayoutAnnuityRestApiRes.class);
//
//				if (responseEntity.getBody() != null) {
//					System.out.println("AnnuityCal-PayoutAnnuityRestApiRes-End");
//					payoutAnnuityRestApiRes = responseEntity.getBody();
//					System.out.println("AnnuityCal-Response==>" + mapper.writeValueAsString(payoutAnnuityRestApiRes));
//					return payoutAnnuityRestApiRes;
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return payoutAnnuityRestApiRes;
//	}
//
//	public static void main(String[] args) {
//		annutityRestApi();
//	}

	/****Narayanan****/
	@Override
	public AnnuityOptionalResponse getAnnuityOptionReponse() {
		AnnuityOptionalResponse response = new AnnuityOptionalResponse();
			try {
				
				logger.info("IntegrationServiceImpl-getAnnuityReponse-Start");
				String annuityOptionalUrl = environment.getProperty("SA_ANNUITY_OPTION_FOR_CLAIM");
				
				ResponseEntity<AnnuityOptionalResponse> responseEntity = restTemplateService.getForEntity(annuityOptionalUrl, AnnuityOptionalResponse.class);						
				if (responseEntity.getBody() != null) {
					logger.info("IntegrationServiceImpl-getAnnuityReponse-End");
					return responseEntity.getBody();
				}

			} catch (IllegalArgumentException e) {
				logger.info("IntegrationServiceImpl-getAllBathes-Error:", e);
			}
			logger.info("IntegrationServiceImpl-getAllBatches-End");
			return response;
		}
	
	/****Narayanan****/
	@Override
	public AnnuityModeResponse getAnnuityModeReponse() {
		AnnuityModeResponse response = new AnnuityModeResponse();
			try {
				
				logger.info("IntegrationServiceImpl-getAnnuityReponse-Start");
				String annuityOptionalUrl = environment.getProperty("SA_ANNUITY_MODE_FOR_CLAIM");
				
				ResponseEntity<AnnuityModeResponse> responseEntity = restTemplateService.getForEntity(annuityOptionalUrl, AnnuityModeResponse.class);						
				if (responseEntity.getBody() != null) {
					logger.info("IntegrationServiceImpl-getAnnuityReponse-End");
					return responseEntity.getBody();
				}

			} catch (IllegalArgumentException e) {
				logger.info("IntegrationServiceImpl-getAllBathes-Error:", e);
			}
			logger.info("IntegrationServiceImpl-getAllBatches-End");
			return response;
		}
	
	
	/****Narayanan****/
	@Override
	public JsonNode getProposalDetailsByProposalNumber(String proposalNumber) throws MalformedURLException, ProtocolException, IOException{
		JsonNode actualObj=null;
		try {
			URL url = new URL(environment.getProperty("PROPOSAL_DETAILS_BY_PROPOSAL_NUMER") + proposalNumber);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			//read response 
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			//convert response as json 
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			 actualObj = mapper.readTree(parser);
		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-getAllBathes-Error:", e);
		}
		logger.info("IntegrationServiceImpl-getAllBatches-End");
		return actualObj;
	}
	
	
	/****Narayanan
	 * @throws IOException ****/
	@Override
	public JsonNode getProductDetailsByProductId(Long productId) throws IOException {
		JsonNode actualObj=null;
			try {
				
				//define and call rest api
				URL url = new URL(environment.getProperty("GETPRODUCTBYPRODUCTID") + productId);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				//read response 
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output = br.readLine();
				conn.disconnect();

				//convert response as json 
				ObjectMapper mapper = new ObjectMapper();
				JsonFactory factory = mapper.getFactory();
				JsonParser parser = factory.createParser(output);
				 actualObj = mapper.readTree(parser);
				 System.out.println("ActualObject:--"+actualObj);

			} catch (IllegalArgumentException e) {
				logger.info("IntegrationServiceImpl-getAllBathes-Error:", e);
			}
			logger.info("IntegrationServiceImpl-getAllBatches-End");
			return actualObj;
		}
  
	
	@Override
	public JsonNode getVariantDetailsByProductVariantId(Long productVariantId) throws IOException {
		JsonNode actualObj=null;
			try {
				
				//define and call rest api
				URL url = new URL(environment.getProperty("GETVARIANTBYPRODUCTVARIANTID") + productVariantId);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				//read response 
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output = br.readLine();
				conn.disconnect();

				//convert response as json 
				ObjectMapper mapper = new ObjectMapper();
				JsonFactory factory = mapper.getFactory();
				JsonParser parser = factory.createParser(output);
				 actualObj = mapper.readTree(parser);
				 System.out.println("ActualObject:--"+actualObj);

			} catch (IllegalArgumentException e) {
				logger.info("IntegrationServiceImpl-getAllBathes-Error:", e);
			}
			logger.info("IntegrationServiceImpl-getAllBatches-End");
			return actualObj;
		}
	
	
	@Override
	public AnnuityRealTimeData callAnnuityRealtimeApi(String policyNumber) throws JsonMappingException, JsonProcessingException {

		AnnuityRealTimeData annuityRealTimeData = new AnnuityRealTimeData();
		logger.info("IntegrationServiceImpl :: callAnnuityRealtimeApi::Start::{},", policyNumber);
		try {
			String url = environment.getProperty("SA_ANNUITY_REAL_TIME_FOR_CLAIM");
			logger.info("===================" + url);
			if (StringUtils.isNotBlank(url)) {
				url = url + policyNumber;
				logger.info("URL:- " + url);
				String strResponse = restTemplateService.exchange(url, HttpMethod.GET, null, String.class).getBody();
				logger.info(":::strResponse:::\n" + strResponse + "\n");
				ObjectMapper mapper = new ObjectMapper();
				annuityRealTimeData  = mapper.readValue(strResponse, AnnuityRealTimeData.class);
				logger.info(":::strResponse:::\n" + annuityRealTimeData + "\n");

			}
		} catch (IllegalArgumentException e) {
			annuityRealTimeData.setStatus(0);
			annuityRealTimeData.setMessage("SA_ANNUITY_REAL_TIME_FOR_CLAIM" + e.getMessage());
			logger.error("Exception IntegrationServiceImpl :: callAnnuityRealtimeApi::End::{},", policyNumber, e);
		}
		logger.info("IntegrationServiceImpl :: callAnnuityRealtimeApi::End::{},", policyNumber);
		return annuityRealTimeData;
	}


	public JsonNode getGstStateDetails(String unitCode) throws IOException {
		JsonNode actualObj=null;
			try {
				
				//define and call rest api
				URL url = new URL(environment.getProperty("GETGSTSTATEDETAILS") + unitCode);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				//read response 
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output = br.readLine();
				conn.disconnect();

				//convert response as json 
				ObjectMapper mapper = new ObjectMapper();
				JsonFactory factory = mapper.getFactory();
				JsonParser parser = factory.createParser(output);
				 actualObj = mapper.readTree(parser);
				 System.out.println("ActualObject:--"+actualObj);

			} catch (IllegalArgumentException e) {
				logger.info("IntegrationServiceImpl-getAllBathes-Error:", e);
			}
			logger.info("IntegrationServiceImpl-getAllBatches-End");
			return actualObj;
		}
   
	
	public DepositReceiptGenerationDto getDepositReceiptGenaration(String receiptNumber,Boolean isProposalDeposit,String policyNumber) {
		DepositReceiptGenerationDto response = null;
			try {
				logger.info("IntegrationServiceImpl-getAnnuityReponse-Start");
				String url = depositReceiptGenaration;
				
				Long mphId = policyMasterRepository.getMphId(policyNumber, Boolean.TRUE);
				if(mphId != null) {
				
				MphMasterEntity masterEntity = mphMasterRepository.findByMphIdAndIsActiveTrue(mphId);
				if(masterEntity!=null) {
				String requestEmailId = masterEntity.getEmailId();
				String schemeName = "SuperAnnuation";
				if(StringUtils.isNotBlank(url)) {
				response = restTemplateService.exchange(url+"?receiptNumber="+receiptNumber+"&schemeName="+schemeName+"&isProposalDeposit="+isProposalDeposit+"&requestEmailId="+requestEmailId, HttpMethod.GET, null, DepositReceiptGenerationDto.class).getBody();					
				if (response == null) {
					response = new DepositReceiptGenerationDto();
				}
				}
				}
				}
			} catch (IllegalArgumentException e) {
				logger.info("IntegrationServiceImpl-getAllBathes-Error:", e);
			}
			logger.info("IntegrationServiceImpl-getAllBatches-End");
			return response;
		}

	@Override
	public StateDetailsDto getStatedetailsBystateId(Integer stateId) {
		StateDetailsDto response = null;
		try {
			logger.info("IntegrationServiceImpl-getStatedetailsBystateId-Start");
			String url = environment.getProperty("GETSTATEDETAILSBYSTATEID");
		
			
			if(StringUtils.isNotBlank(url)) {
			response = restTemplateService.exchange(url+stateId, HttpMethod.GET, null, StateDetailsDto.class).getBody();					
		    if (response == null) {
				response = new StateDetailsDto();
			}
			
			}
		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-getStatedetailsBystateId-Error:", e);
		}
		logger.info("IntegrationServiceImpl-getStatedetailsBystateId-End");
		return response;
	}

	@Override
	public JsonNode getStateCodeAndStateName(String unitCode) throws IOException {
		JsonNode actualObj=null;
		try {
			
			//define and call rest api
			String urll = environment.getProperty("GETSTATEDETAILSBYUNITCODE");
//			URL url = new URL(environment.getProperty("https://d1utvrrpgca01.licindia.com:8443/commonmasterservice/getAllStatesByunitCode/") + unitCode);
			logger.info(urll);
			URL url = new URL (urll + unitCode);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
		
			if (conn.getResponseCode() != 202) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			//read response 
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.readLine();
			conn.disconnect();

			//convert response as json 
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory();
			JsonParser parser = factory.createParser(output);
			 actualObj = mapper.readTree(parser);
			 System.out.println("ActualObject:--"+actualObj);

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-getAllBathes-Error:", e);
		}
		logger.info("IntegrationServiceImpl-getAllBatches-End");
		return actualObj;
	}
	
	
	
	@Override
	public DepositCollectBulkUploadResponseDto depositCollectBulk(MultipartFile file) {

		DepositCollectBulkUploadResponseDto responseDto = new DepositCollectBulkUploadResponseDto();
		
		try {
			logger.info("IntegrationServiceImpl-depositCollectBulk-Start");
			
			String sparkUrl = environment.getProperty("SA_BULK_UPLOAD_FOR_DEPOSIT_COLLECT");
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			
			byte[] fileContent = null;
			try {
				fileContent = IOUtils.toByteArray(file.getInputStream());
			} catch (IOException e) {				
				logger.info("IntegrationServiceImpl-depositCollectBulk-Error:", e);
				responseDto.setTransactionStatus(CommonConstants.ERROR);
				responseDto.setTransactionStatus(CommonConstants.ERROR);
				return responseDto;
			}
		
			
			String filename = file.getOriginalFilename();

			MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
			ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("file")
					.filename(filename).build();

			fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			
			body.add("file", fileEntity);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			ResponseEntity<DepositCollectBulkUploadResponseDto> responseEntity = restTemplateService.postForEntity(sparkUrl,
					requestEntity, DepositCollectBulkUploadResponseDto.class);

			if (responseEntity.getBody() != null) {
				logger.info("IntegrationServiceImpl-depositCollectBulk-End");
				return responseEntity.getBody();
			}

		} catch (IllegalArgumentException e) {
			logger.info("IntegrationServiceImpl-depositCollectBulk-Error:", e);
		}
		
		logger.info("IntegrationServiceImpl-depositCollectBulk-End");
		return responseDto;
	}

	
	
	
}
