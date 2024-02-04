package com.lic.epgs.integration.serviceImpl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.lic.epgs.common.entity.IcodeMasterEntity;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.common.repository.IcodeMasterRepository;
import com.lic.epgs.integration.constants.AccountingIntegrationConstants;
import com.lic.epgs.integration.dto.AccountingIntegrationRequestDto;
import com.lic.epgs.integration.dto.ResponseDto;
import com.lic.epgs.integration.dto.SuperAnnuationResponseModel;
import com.lic.epgs.integration.service.AccountingIntegrationService;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.entity.MphAddressEntity;
import com.lic.epgs.policy.repository.MphAddressRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.utils.NumericUtils;

@Service
public class AccountingIntegrationServiceImpl implements AccountingIntegrationService {
	
	
	@Autowired
	@Qualifier(value = "restTemplateService")
	private RestTemplate restTemplateService;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	PolicyMasterRepository policyMasterRepository;
	
	@Autowired
	MphMasterRepository mphMasterRepository;
	
	@Autowired
	MphAddressRepository mphAddressRepository;
	
	@Autowired
	IntegrationService integrationService;
	
	@Autowired
	IcodeMasterRepository icodeMasterRepository;
	


	
	private final Logger logger = LogManager.getLogger(AccountingIntegrationServiceImpl.class);
	
	
//	public SuperAnnuationResponseModel getMphAndIcodeDetail(AccountingIntegrationRequestDto accountingIntegrationRequestDto) {
//		logger.info("ProposalMakerServiceImpl------getMphAndIcodeDetail-------Started");
//		SuperAnnuationResponseModel superAnnuationResponseModel = new SuperAnnuationResponseModel();
//		try {
//			ProposalAnnuityDto proposalAnnuityDto = getDetails(accountingIntegrationRequestDto.getProposalNumber());
//			if(proposalAnnuityDto != null) {
//				superAnnuationResponseModel.setSchemeName(AccountingIntegrationConstants.SCHEME_NAME);
//				superAnnuationResponseModel.setMphCode(proposalAnnuityDto.getCustomerBasicDetails().getCustomerCode());
//				superAnnuationResponseModel.setMphName(proposalAnnuityDto.getCustomerBasicDetails().getCustomerName());
//				superAnnuationResponseModel.setMphNo(proposalAnnuityDto.getProposalNumber());
//			List<MPHContactPersonDetailsDto> mPHContactPersonDetailsDtoList = proposalAnnuityDto.getMphContactDetails();
//            if(!mPHContactPersonDetailsDtoList.isEmpty()) {
//			for(MPHContactPersonDetailsDto mPHContactPersonDetailsDto : mPHContactPersonDetailsDtoList) {
//				superAnnuationResponseModel.setMphMobileNo(NumericUtils.convertStringToLong(mPHContactPersonDetailsDto.getMobileNumber()));
//				superAnnuationResponseModel.setMphEmail(mPHContactPersonDetailsDto.getEmailID());
//            }
//            }
//            List<MPHAddressDetailsDto> mPHAddressDetailsDtoList = proposalAnnuityDto.getMphAddressDetails();
//			if(!mPHAddressDetailsDtoList.isEmpty()) {
//            for(MPHAddressDetailsDto mPHAddressDetailsDto : mPHAddressDetailsDtoList) {
//            	superAnnuationResponseModel.setMphAddress1(mPHAddressDetailsDto.getAddress1());
//            	superAnnuationResponseModel.setMphAddress2(mPHAddressDetailsDto.getAddress2());
//            	superAnnuationResponseModel.setDistrict(mPHAddressDetailsDto.getDistrict());
//            	superAnnuationResponseModel.setState(mPHAddressDetailsDto.getState());
//            	superAnnuationResponseModel.setPinCode(NumericUtils.convertStringToLong(mPHAddressDetailsDto.getPinCode()));
//			}
//			}
//			
//			ResponseDto responseDto=commonmasterserviceUnitByCode(proposalAnnuityDto.getProposalBasicDetails().getUnitCode());
//			if(responseDto != null) {
//				superAnnuationResponseModel.setServicingUnitName(responseDto.getDescription());
//				superAnnuationResponseModel.setServicingUnitAddress1(responseDto.getAddress1());
//				superAnnuationResponseModel.setServicingUnitAddress2(responseDto.getAddress2());
//				superAnnuationResponseModel.setServicingUnitAddress3(responseDto.getAddress3());
//				superAnnuationResponseModel.setServicingUnitAddress4(responseDto.getAddress4());
//				superAnnuationResponseModel.setServicingUnitCity(responseDto.getCityName());
//				superAnnuationResponseModel.setServicingUnitPincode(responseDto.getPincode());
//				superAnnuationResponseModel.setServicingUnitEmail(responseDto.getEmailId());
//				superAnnuationResponseModel.setServicingUnitPhoneNo(responseDto.getTelephoneNo());
//				superAnnuationResponseModel.setOperatingUnitType(responseDto.getDescription());
//				superAnnuationResponseModel.setUnitCode(responseDto.getUnitCode());
//			}
//			superAnnuationResponseModel.setICodeForLob(AccountingIntegrationConstants.icodeforlob);
//			superAnnuationResponseModel.setICodeForProductLine(AccountingIntegrationConstants.iCodeForProductLine);
//			superAnnuationResponseModel.setICodeForVariant(AccountingIntegrationConstants.iCodeForVariant);
//			superAnnuationResponseModel.setICodeForBusinessType(AccountingIntegrationConstants.iCodeForBusinessType);
//			superAnnuationResponseModel.setICodeForParticipatingType(AccountingIntegrationConstants.iCodeForParticipatingType);
//			superAnnuationResponseModel.setICodeForBusinessSegment(AccountingIntegrationConstants.iCodeForBusinessSegment);
//			superAnnuationResponseModel.setICodeForInvestmentPortfolio(AccountingIntegrationConstants.iCodeForInvestmentPortfolio);
//			}
//		}
//		catch (Exception ue) {
//			logger.error(PolicyConstants.EXCEPTION, "getMphAndIcodeDetail", ue);
//		}
//		logger.info("ProposalMakerServiceImpl ------ getMphAndIcodeDetail----------- Ended");
//		return superAnnuationResponseModel;
//	}
//	
//	public ProposalAnnuityDto getDetails(String proposalNumber) {
//		ProposalAnnuityDto proposalAnnuityDto = null;
//		try {
//			String url = environment.getProperty("PROPOSAL_DETAILSBY_PROPOSALNUMBER");
//		 
//			if (StringUtils.isNotBlank(url)) {
//				proposalAnnuityDto = restTemplateService.exchange(url + proposalNumber, HttpMethod.GET, null, ProposalAnnuityDto.class)
//						.getBody();
//				if (proposalAnnuityDto == null) {
//					proposalAnnuityDto = new ProposalAnnuityDto();
//				}
//				
//			}
//			
//		} catch (HttpStatusCodeException e) {
//			logger.info("CommonServiceImpl-getDetails-Error:");
//		}
//		logger.info("CommonServiceImpl-getDetails-End");
//		 
//		 return proposalAnnuityDto;
//	}
//	
	
	
	
	
	
	
		public ResponseDto commonmasterserviceUnitByCode(String unitCode) {
			ResponseDto responseDto=null;
			logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-Start");

			try {
				String url = environment.getProperty("COMMON_MASTER_UNITBY_CODE");

				if (StringUtils.isNotBlank(url)) {

					responseDto = restTemplateService.exchange(url + unitCode, HttpMethod.GET, null, ResponseDto.class)
							.getBody();
					if (responseDto == null) {
						responseDto = new ResponseDto();
					}
				}
			} catch (HttpStatusCodeException e) {

				logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-Error:");
			}
			logger.info("CommonServiceImpl-commonmasterserviceUnitByCode-End");

			return responseDto;
		}


@Override
public SuperAnnuationResponseModel getMphAndIcodeDetail(
		AccountingIntegrationRequestDto accountingIntegrationRequestDto) {
	logger.info("ProposalMakerServiceImpl------getMphAndIcodeDetail-------Started");
	SuperAnnuationResponseModel superAnnuationResponseModel = new SuperAnnuationResponseModel();
	 try {
		 
		 Object object = policyMasterRepository.findPolicyDetail(accountingIntegrationRequestDto.getPolicyNumber());
		 
		 if(object != null) {
		 
		 Object[] obj = (Object[]) object;
		 
		 JsonNode newResponse = integrationService
					.getVariantDetailsByProductVariantId(NumericUtils.stringToLong(String.valueOf(obj[6])));
			if (newResponse != null) {
				
				JsonNode proposeDetails = newResponse.path("responseData");
				String variantCode = proposeDetails.path("subCategory").textValue();
				
		     String emailId =  mphAddressRepository.getEmailId(String.valueOf(obj[5]));
		 
			 superAnnuationResponseModel.setSchemeName(variantCode);
			 superAnnuationResponseModel.setMphCode(String.valueOf(obj[1]));
			 superAnnuationResponseModel.setMphName(String.valueOf(obj[2]));
			 superAnnuationResponseModel.setMphNo(String.valueOf(obj[4]));
			 superAnnuationResponseModel.setMphEmail(emailId);
			 superAnnuationResponseModel.setMphMobileNo(NumericUtils.convertStringToLong(String.valueOf(obj[8])));
			 MphAddressEntity mphAddressEntity = mphAddressRepository.findByMphIdAndIsActiveTrueAndIsDefaultTrue(NumericUtils.convertStringToLong(String.valueOf(obj[0])));
		     
			 if(mphAddressEntity != null) {
				    superAnnuationResponseModel.setMphAddress1(mphAddressEntity.getAddressLine1());
	            	superAnnuationResponseModel.setMphAddress2(mphAddressEntity.getAddressLine2());
	            	superAnnuationResponseModel.setDistrict(mphAddressEntity.getDistrict());
	            	superAnnuationResponseModel.setState(NumericUtils.convertIntegerToString(mphAddressEntity.getStateId()));
	            	superAnnuationResponseModel.setPinCode(NumericUtils.convertIntegerToLong(mphAddressEntity.getPincode()));
			 }
			 
			 ResponseDto responseDto=commonmasterserviceUnitByCode(String.valueOf(obj[5]));
				if(responseDto != null) {
					superAnnuationResponseModel.setServicingUnitName(responseDto.getDescription());
					superAnnuationResponseModel.setServicingUnitAddress1(responseDto.getAddress1());
					superAnnuationResponseModel.setServicingUnitAddress2(responseDto.getAddress2());
					superAnnuationResponseModel.setServicingUnitAddress3(responseDto.getAddress3());
					superAnnuationResponseModel.setServicingUnitAddress4(responseDto.getAddress4());
					superAnnuationResponseModel.setServicingUnitCity(responseDto.getCityName());
					superAnnuationResponseModel.setServicingUnitPincode(responseDto.getPincode());
					superAnnuationResponseModel.setServicingUnitEmail(responseDto.getEmailId());
					superAnnuationResponseModel.setServicingUnitPhoneNo(responseDto.getTelephoneNo());
					superAnnuationResponseModel.setOperatingUnitType(responseDto.getDescription());
					superAnnuationResponseModel.setUnitCode(responseDto.getUnitCode());
				}
				
					
					String variantVersion = proposeDetails.path("variantVersion").textValue();
					
					IcodeMasterEntity icodeMasterEntity = icodeMasterRepository.findByVariant(variantVersion);
					
					if(icodeMasterEntity != null) {
					
					superAnnuationResponseModel.setICodeForLob(icodeMasterEntity.getIcodeForLob());
					superAnnuationResponseModel.setICodeForProductLine(icodeMasterEntity.getIcodeForProductLine());
					superAnnuationResponseModel.setICodeForVariant(icodeMasterEntity.getIcodeForVariant());
					superAnnuationResponseModel.setICodeForBusinessType(icodeMasterEntity.getIcodeForBusinessType());
					superAnnuationResponseModel.setICodeForParticipatingType(icodeMasterEntity.getIcodeForParticipating());
					superAnnuationResponseModel.setICodeForBusinessSegment(icodeMasterEntity.getIcodeForBusinessSegment());
					superAnnuationResponseModel.setICodeForInvestmentPortfolio(icodeMasterEntity.getIcodeForInvestmentPortFolio());
               
					}
				}		 
		 }
	 }
	 catch (Exception ue) {
			logger.error(PolicyConstants.EXCEPTION, "getMphAndIcodeDetail", ue);
		}
		logger.info("ProposalMakerServiceImpl ------ getMphAndIcodeDetail----------- Ended");
	return superAnnuationResponseModel;
}
		
		
		
		
		

	

}
