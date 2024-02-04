package com.lic.epgs.adjustmentcontribution.service.impl;

/**
 * @author pradeepramesh
 *
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionNotesDto;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionEntity;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionNotesEntity;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionNotesTempEntity;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionTempEntity;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionTempRepository;
import com.lic.epgs.policy.dto.MphAddressDto;
import com.lic.epgs.policy.dto.MphBankDto;
import com.lic.epgs.policy.dto.MphMasterDto;
import com.lic.epgs.policy.dto.PolicyContributionSummaryDto;
import com.lic.epgs.policy.dto.PolicyDepositDto;
import com.lic.epgs.policy.dto.PolicyMasterDto;
import com.lic.epgs.policy.dto.PolicyNotesDto;
import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policy.dto.PolicyValuationDto;
import com.lic.epgs.policy.entity.MemberContributionEntity;
import com.lic.epgs.policy.entity.MemberContributionTempEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.PolicyContributionEntity;
import com.lic.epgs.policy.entity.PolicyContributionSummaryEntity;
import com.lic.epgs.policy.entity.PolicyContributionTempEntity;
import com.lic.epgs.policy.entity.PolicyDepositEntity;
import com.lic.epgs.policy.entity.PolicyDepositTempEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.old.dto.PolicyAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentOldDto;
import com.lic.epgs.policy.old.dto.PolicyBankOldDto;
import com.lic.epgs.policy.old.dto.PolicyDepositOldDto;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.policy.old.dto.PolicyNotesOldDto;
import com.lic.epgs.policy.old.dto.PolicyRulesOldDto;
import com.lic.epgs.policy.repository.PolicyContributionRepository;
import com.lic.epgs.policy.repository.PolicyContributionTempRepository;
import com.lic.epgs.policy.repository.PolicyDepositRepository;
import com.lic.epgs.policy.repository.PolicyDepositTempRepository;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.NumericUtils;



@Service
public class AdjustmentContributionCommonServiceImpl {
	
	@Autowired
	private AdjustmentContributionTempRepository adjustmentContributionTempRepository;
	
	@Autowired
	private PolicyContributionRepository policyContributionRepository;

	@Autowired
	private PolicyContributionTempRepository policyContributionTempRepository;
	
	@Autowired
	private PolicyDepositTempRepository policyDepositTempRepository;
	
	@Autowired
	private PolicyDepositRepository policyDepositRepository;
	
	public MphMasterDto convertAdjMphMasterEntityToMphMasterDto(MphMasterEntity masterDto) {

		MphMasterDto response = new MphMasterDto();

		response.setMphId(masterDto.getMphId());
		response.setTempMphId(masterDto.getTempMphId());
		response.setMphCode(masterDto.getMphCode());
		response.setMphName(masterDto.getMphName());
		response.setMphType(masterDto.getMphType());
		response.setProposalNumber(masterDto.getProposalNumber());
		response.setProposalId(masterDto.getProposalId());
		response.setCin(masterDto.getCin());
		response.setPan(masterDto.getPan());
		response.setAlternatePan(masterDto.getAlternatePan());
		response.setLandlineNo(masterDto.getLandlineNo());
		response.setCountryCode(masterDto.getCountryCode());
		response.setMobileNo(masterDto.getMobileNo());
		response.setEmailId(masterDto.getEmailId());
		response.setFax(masterDto.getFax());
		response.setIsActive(masterDto.getIsActive());
		response.setCreatedOn(masterDto.getCreatedOn());
		response.setCreatedBy(masterDto.getCreatedBy());
		response.setModifiedOn(masterDto.getModifiedOn());
		response.setModifiedBy(masterDto.getModifiedBy());
		Set<PolicyMasterDto> policyMaster = new HashSet<>();
		for (PolicyMasterEntity policyDto : masterDto.getPolicyMaster()) {
			PolicyMasterDto policyMasterEntity = convertPolicyMasterEntityToPolicyMasterDto(policyDto);
			policyMaster.add(policyMasterEntity);
		}
		response.setPolicyMaster(policyMaster);
		return response;
	}

	public PolicyMasterDto convertPolicyMasterEntityToPolicyMasterDto(PolicyMasterEntity policyMasterDto) {
		PolicyMasterDto response = new PolicyMasterDto();
		response.setPolicyId(policyMasterDto.getPolicyId());
		response.setTempPolicyId(policyMasterDto.getTempPolicyId());
		response.setMphId(policyMasterDto.getMphId());
		response.setPolicyNumber(policyMasterDto.getPolicyNumber());
		response.setPolicyStatus(policyMasterDto.getPolicyStatus());
		response.setPolicyType(policyMasterDto.getPolicyType());
		response.setNoOfCategory(policyMasterDto.getNoOfCategory());
		response.setContributionFrequency(policyMasterDto.getContributionFrequency());
		response.setIntermediaryOfficerCode(policyMasterDto.getIntermediaryOfficerCode());
		response.setIntermediaryOfficerName(policyMasterDto.getIntermediaryOfficerName());
		response.setMarketingOfficerCode(policyMasterDto.getMarketingOfficerCode());
		response.setMarketingOfficerName(policyMasterDto.getMarketingOfficerName());
		response.setProposalId(policyMasterDto.getProposalId());
		response.setQuotationId(policyMasterDto.getQuotationId());
		response.setLeadId(policyMasterDto.getLeadId());
		response.setLineOfBusiness(policyMasterDto.getLineOfBusiness());
		response.setVariant(policyMasterDto.getVariant());
		response.setProductId(policyMasterDto.getProductId());
		response.setTotalMember(policyMasterDto.getTotalMember());
		response.setUnitId(policyMasterDto.getUnitId());
		response.setUnitOffice(policyMasterDto.getUnitOffice());
		response.setAdjustmentDt(policyMasterDto.getAdjustmentDt());
		response.setRejectionReasonCode(policyMasterDto.getRejectionReasonCode());
		response.setRejectionRemarks(policyMasterDto.getRejectionRemarks());
		response.setWorkflowStatus(policyMasterDto.getWorkflowStatus());
		response.setArd(policyMasterDto.getArd());
		response.setIsCommencementdateOneYr(policyMasterDto.getIsCommencementdateOneYr());
		response.setPolicyCommencementDt(policyMasterDto.getPolicyCommencementDt());
		response.setPolicyDispatchDate(policyMasterDto.getPolicyDispatchDate());
		response.setPolicyRecievedDate(policyMasterDto.getPolicyRecievedDate());
		response.setConType(policyMasterDto.getConType());
		response.setAdvanceotarrears(policyMasterDto.getAdvanceotarrears());
		response.setIsActive(policyMasterDto.getIsActive());
		response.setCreatedBy(policyMasterDto.getCreatedBy());
		response.setCreatedOn(policyMasterDto.getCreatedOn());
		response.setModifiedBy(policyMasterDto.getModifiedBy());
		response.setModifiedOn(policyMasterDto.getModifiedOn());
		response.setAmountToBeAdjusted(policyMasterDto.getAmountToBeAdjusted());
		response.setFirstPremium(policyMasterDto.getFirstPremium());
		response.setSinglePremiumFirstYr(policyMasterDto.getSinglePremiumFirstYr());
		response.setRenewalPremium(policyMasterDto.getRenewalPremium());
		response.setSubsequentSinglePremium(policyMasterDto.getSubsequentSinglePremium());

		Set<PolicyDepositDto> deposits = new HashSet<>();
		for (PolicyDepositEntity policyDepositDto : policyMasterDto.getDeposits()) {
			PolicyDepositDto policyDepositEntity = new PolicyDepositDto();
			policyDepositEntity.setDepositId(policyDepositDto.getDepositId());
			policyDepositEntity.setPolicyId(policyDepositDto.getPolicyId());
			
			policyDepositEntity.setMasterPolicyId(policyDepositDto.getPolicyId());
			policyDepositEntity.setTempPolicyId(policyDepositDto.getTempPolicyId());
			policyDepositEntity.setAdjustmentContributionId(policyDepositDto.getAdjustmentContributionId());
			policyDepositEntity.setRegularContributionId(policyDepositDto.getRegularContributionId());
			policyDepositEntity.setContributionType(policyDepositDto.getContributionType());
			
			policyDepositEntity.setCollectionNo(policyDepositDto.getCollectionNo());
			policyDepositEntity.setCollectionDate(policyDepositDto.getCollectionDate());
			policyDepositEntity.setCollectionStatus(policyDepositDto.getCollectionStatus());
			policyDepositEntity.setChallanNo(policyDepositDto.getChallanNo());
			policyDepositEntity.setDepositAmount(policyDepositDto.getDepositAmount());
			policyDepositEntity.setAdjustmentAmount(policyDepositDto.getAdjustmentAmount());
			policyDepositEntity.setAvailableAmount(policyDepositDto.getAvailableAmount());
			policyDepositEntity.setTransactionMode(policyDepositDto.getTransactionMode());
			policyDepositEntity.setChequeRealisationDate(policyDepositDto.getChequeRealisationDate());
			policyDepositEntity.setRemark(policyDepositDto.getRemark());
			policyDepositEntity.setStatus(policyDepositDto.getStatus());
			policyDepositEntity.setZeroId(policyDepositDto.getZeroId());
			policyDepositEntity.setModifiedBy(policyDepositDto.getModifiedBy());
			policyDepositEntity.setModifiedOn(policyDepositDto.getModifiedOn());
			policyDepositEntity.setCreatedBy(policyDepositDto.getCreatedBy());
			policyDepositEntity.setCreatedOn(policyDepositDto.getCreatedOn());
			policyDepositEntity.setIsActive(policyDepositDto.getIsActive());
			deposits.add(policyDepositEntity);
		}
		response.setDeposits(deposits);

		Set<PolicyContributionSummaryDto> policyContributionSummary = new HashSet<>();
		for (PolicyContributionSummaryEntity policyContributionSummaryDto : policyMasterDto.getPolicyContributionSummary()) {
			PolicyContributionSummaryDto policyContributionSummaryEntity = new PolicyContributionSummaryDto();
			policyContributionSummaryEntity.setPolContSummaryId(policyContributionSummaryDto.getPolContSummaryId());
			policyContributionSummaryEntity.setPolicyId(policyContributionSummaryDto.getPolicyId());
			policyContributionSummaryEntity.setStampDuty(policyContributionSummaryDto.getStampDuty());
			policyContributionSummaryEntity.setOpeningBalance(policyContributionSummaryDto.getOpeningBalance());
			policyContributionSummaryEntity.setTotalEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
			policyContributionSummaryEntity.setTotalEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
			policyContributionSummaryEntity.setTotalVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
			policyContributionSummaryEntity.setTotalContribution(policyContributionSummaryDto.getTotalContribution());
			policyContributionSummaryEntity.setClosingBalance(policyContributionSummaryDto.getClosingBalance());
			policyContributionSummaryEntity.setFinancialYear(policyContributionSummaryDto.getFinancialYear());
			policyContributionSummaryEntity.setIsActive(policyContributionSummaryDto.getIsActive());
			policyContributionSummaryEntity.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
			policyContributionSummaryEntity.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
			policyContributionSummaryEntity.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
			policyContributionSummaryEntity.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
			policyContributionSummary.add(policyContributionSummaryEntity);
		}
		response.setPolicyContributionSummary(policyContributionSummary);
		return response;
	}
	
	public PolicyDto convertAdjNewResponseToOldResponse(MphMasterDto request) {
		PolicyDto response = new PolicyDto();

		response.setProposalName(null);
		response.setPolicyStartDate(null);
		response.setPolicyEndDt(null);
		response.setLeadNumber(null);
		response.setType(null);
		response.setServiceId(null);
		response.setServiceNo(null);
		response.setServiceStatus(null);
		response.setCancelRequestDate(null);
		response.setQuotationDate(null);
		response.setNumberOfLives(null);
		response.setQuotationPremiumAmount(null);

		response.setMphId(request.getMphId());
		response.setTempMphId(request.getTempMphId());
		response.setProposalId(NumericUtils.convertStringToInteger(request.getProposalId()));
		response.setProposalNumber(request.getProposalNumber());
		response.setMphCode(request.getMphCode());
		response.setMphName(request.getMphName());
		response.setPan(request.getPan());
		response.setApan(request.getAlternatePan());
		response.setCin(NumericUtils.convertStringToInteger(request.getCin()));
		response.setFax(request.getFax());
		response.setContactNo(NumericUtils.convertLongToString(request.getMobileNo()));
		response.setEmailId(request.getEmailId());
		response.setCustomerId(NumericUtils.convertLongToInteger(request.getMphId()));
		response.setCustomerName(request.getMphName());
		response.setCustomerCode(request.getMphCode());
		Set<PolicyAddressOldDto> policyAddressOldDtoList = new HashSet<>();
		for (MphAddressDto mphAddressDto : request.getMphAddress()) {
			PolicyAddressOldDto oldDto = new PolicyAddressOldDto();
			oldDto.setAddressId(mphAddressDto.getAddressId());
			oldDto.setAddressLine1(mphAddressDto.getAddressLine1());
			oldDto.setAddressLine2(mphAddressDto.getAddressLine2());
			oldDto.setAddressLine3(mphAddressDto.getAddressLine3());
			oldDto.setAddressType(mphAddressDto.getAddressType());
			oldDto.setCityId(mphAddressDto.getCityId());
			oldDto.setDistrictId(mphAddressDto.getDistrictId());
			oldDto.setCountryId(mphAddressDto.getCountryId());
			oldDto.setStateId(mphAddressDto.getStateId());
			oldDto.setPinCode(NumericUtils.convertIntegerToString(mphAddressDto.getPincode()));
			oldDto.setTownLocality(mphAddressDto.getCityLocality());
			oldDto.setCreatedBy(mphAddressDto.getCreatedBy());
			oldDto.setCreatedOn(mphAddressDto.getCreatedOn());
			oldDto.setModifiedBy(mphAddressDto.getModifiedBy());
			oldDto.setModifiedOn(mphAddressDto.getModifiedOn());
			oldDto.setMphId(mphAddressDto.getMphId());
			policyAddressOldDtoList.add(oldDto);
		}
		response.setAddresses(policyAddressOldDtoList);
		Set<PolicyBankOldDto> policyBankOldDtoList = new HashSet<>();
		for (MphBankDto mphBankDto : request.getMphBank()) {
			PolicyBankOldDto oldDto = new PolicyBankOldDto();
			oldDto.setMphId(mphBankDto.getMphId());
			oldDto.setBankAccountId(mphBankDto.getMphBankId());
			oldDto.setAccountNumber(mphBankDto.getAccountNumber());
			oldDto.setAccountType(mphBankDto.getAccountType());
			oldDto.setIfscCodeAvailable(mphBankDto.getIfscCodeAvailable());
			oldDto.setIfscCode(mphBankDto.getIfscCode());
			oldDto.setBankName(mphBankDto.getBankName());
			oldDto.setBankBranch(mphBankDto.getBankBranch());
			oldDto.setBankAddress(mphBankDto.getBankAddress());
			oldDto.setCountryId(mphBankDto.getCountryId());
			oldDto.setEmailId(mphBankDto.getEmailId());
			oldDto.setIsActive(mphBankDto.getIsActive());
			oldDto.setCreatedBy(mphBankDto.getCreatedBy());
			oldDto.setCreatedOn(mphBankDto.getCreatedOn());
			oldDto.setModifiedBy(mphBankDto.getModifiedBy());
			oldDto.setModifiedOn(mphBankDto.getModifiedOn());

			policyBankOldDtoList.add(oldDto);
		}
		response.setBankDetails(policyBankOldDtoList);
		for (PolicyMasterDto policyMasterdto : request.getPolicyMaster()) {
			response.setPolicyId(policyMasterdto.getPolicyId());
			response.setTempPolicyId(policyMasterdto.getTempPolicyId());
			response.setLeadId(policyMasterdto.getLeadId());
			response.setLineOfBusiness(policyMasterdto.getLineOfBusiness());
			response.setMarketingOfficerName(policyMasterdto.getMarketingOfficerName());
			response.setMarketingOfficerCode(policyMasterdto.getMarketingOfficerCode());
			response.setIntermediaryOfficerName(policyMasterdto.getIntermediaryOfficerName());
			response.setIntermediaryOfficerCode(policyMasterdto.getIntermediaryOfficerCode());
			response.setUnitCode(policyMasterdto.getUnitId());
			response.setPolicyNumber(policyMasterdto.getPolicyNumber());
			response.setPolicyStatus(policyMasterdto.getPolicyStatus());
			response.setWorkFlowStatus(policyMasterdto.getWorkflowStatus());
			response.setPolicyCommencementDate(policyMasterdto.getPolicyCommencementDt());
			response.setTotalMember(policyMasterdto.getTotalMember());
			response.setPolicyAdjustmentDate(policyMasterdto.getAdjustmentDt());
			response.setFrequency(policyMasterdto.getContributionFrequency());
			response.setCategory(policyMasterdto.getNoOfCategory());
			response.setProduct(NumericUtils.convertLongToString(policyMasterdto.getProductId()));
			response.setVariant(policyMasterdto.getVariant());
			response.setContributionType(policyMasterdto.getConType());
			response.setAdvanceOrArrears(policyMasterdto.getAdvanceotarrears());
			response.setUnitOffice(policyMasterdto.getUnitOffice());
			response.setArd(policyMasterdto.getArd());
			response.setPolicyDispatchDate(policyMasterdto.getPolicyDispatchDate());
			response.setPolicyRecievedDate(policyMasterdto.getPolicyRecievedDate());
			response.setModifiedBy(policyMasterdto.getModifiedBy());
			response.setModifiedOn(policyMasterdto.getModifiedOn());
			response.setCreatedBy(policyMasterdto.getCreatedBy());
			response.setCreatedOn(policyMasterdto.getCreatedOn());
			response.setIsActive(policyMasterdto.getIsActive());
			response.setRejectionReasonCode(policyMasterdto.getRejectionReasonCode());
			response.setRejectionRemarks(policyMasterdto.getRejectionRemarks());
			response.setAmountToBeAdjusted(policyMasterdto.getAmountToBeAdjusted());
			response.setFirstPremium(policyMasterdto.getFirstPremium());
			response.setSinglePremiumFirstYr(policyMasterdto.getSinglePremiumFirstYr());
			response.setRenewalPremium(policyMasterdto.getRenewalPremium());
			response.setSubsequentSinglePremium(policyMasterdto.getSubsequentSinglePremium());
			response.setIsCommencementdateOneYr(policyMasterdto.getIsCommencementdateOneYr());
			response.setQuotationId(policyMasterdto.getQuotationId());
			response.setQuotationType(policyMasterdto.getPolicyType());
			response.setQuotationNo(null);

			for (PolicyValuationDto policyValuationDto : policyMasterdto.getValuations()) {
				response.setValuationId(policyValuationDto.getValuationId());
				response.setValuationType(policyValuationDto.getValuationType());
				response.setAttritionRate(policyValuationDto.getAttritionRate());
				response.setSalaryEscalation(policyValuationDto.getSalaryEscalation());
				response.setDeathRate(policyValuationDto.getDeathRate());
				response.setDisRateIntrest(policyValuationDto.getDisRateIntrest());
				response.setAnnuityOption(policyValuationDto.getAnnuityOption());
				response.setAccuralRateFactor(policyValuationDto.getAccuralRateFactor());
				response.setMinPension(policyValuationDto.getMinPension());
				response.setMaxPension(policyValuationDto.getMaxPension());
				response.setWithdrawalRate(policyValuationDto.getWithdrawalRate());
				response.setDisRateSalaryEsc(policyValuationDto.getDisRateSalaryEsc());
			}

			Set<PolicyRulesOldDto> rulesList = new HashSet<>();
			for (PolicyRulesDto policyRulesDto : policyMasterdto.getRules()) {
				PolicyRulesOldDto policyRulesOldDto = new PolicyRulesOldDto();

				policyRulesOldDto.setRuleId(policyRulesDto.getRuleId());
				policyRulesOldDto.setPolicyId(policyRulesDto.getPolicyId());
				policyRulesOldDto.setCategory(policyRulesDto.getCategory());
				policyRulesOldDto.setNormalAgeRetirement(policyRulesDto.getNormalAgeRetirement());
				policyRulesOldDto.setMinAgeRetirement(policyRulesDto.getMinAgeRetirement());
				policyRulesOldDto.setMaxAgeRetirement(policyRulesDto.getMaxAgeRetirement());
				policyRulesOldDto.setMinServicePension(policyRulesDto.getMinServicePension());
				policyRulesOldDto.setMaxServicePension(policyRulesDto.getMaxServicePension());
				policyRulesOldDto.setMinServiceWithdrawal(policyRulesDto.getMinServiceWithdrawal());
				policyRulesOldDto.setMaxServiceWithdrawal(policyRulesDto.getMaxServiceWithdrawal());
				policyRulesOldDto.setContributionType(policyRulesDto.getContributionType());
				policyRulesOldDto.setMinPension(policyRulesDto.getMinPension());
				policyRulesOldDto.setMaxPension(policyRulesDto.getMaxPension());
				policyRulesOldDto.setBenifitPayableTo(policyRulesDto.getBenifitPayableTo());
				policyRulesOldDto.setAnnuityOption(policyRulesDto.getAnnuityOption());
				policyRulesOldDto.setCommutationBy(policyRulesDto.getCommutationBy());
				policyRulesOldDto.setCommutationAmt(policyRulesDto.getCommutationAmt());
				policyRulesOldDto.setModifiedBy(policyRulesDto.getModifiedBy());
				policyRulesOldDto.setModifiedOn(policyRulesDto.getModifiedOn());
				policyRulesOldDto.setCreatedBy(policyRulesDto.getCreatedBy());
				policyRulesOldDto.setCreatedOn(policyRulesDto.getCreatedOn());
				policyRulesOldDto.setIsActive(policyRulesDto.getIsActive());
				policyRulesOldDto.setEffectiveFrom(policyRulesDto.getEffectiveFrom());
				policyRulesOldDto.setPercentageCorpus(policyRulesDto.getPercentageCorpus());

				rulesList.add(policyRulesOldDto);

			}
			response.setRules(rulesList);
			Set<PolicyDepositOldDto> depositList = new HashSet<>();
			Set<PolicyAdjustmentOldDto> adjustmentsList = new HashSet<>();
			for (PolicyDepositDto policyDepositDto : policyMasterdto.getDeposits()) {
				PolicyDepositOldDto oldDto = new PolicyDepositOldDto();
				
				oldDto.setDepositId(policyDepositDto.getDepositId());
				oldDto.setPolicyId(policyDepositDto.getPolicyId());
				
				oldDto.setMasterPolicyId(policyDepositDto.getMasterPolicyId());
				oldDto.setTempPolicyId(policyDepositDto.getTempPolicyId());
				oldDto.setAdjustmentContributionId(policyDepositDto.getAdjustmentContributionId());
				oldDto.setRegularContributionId(policyDepositDto.getRegularContributionId());
				oldDto.setContributionType(policyDepositDto.getContributionType());
				
				oldDto.setCollectionNo(policyDepositDto.getCollectionNo());
				oldDto.setCollectionDate(policyDepositDto.getCollectionDate());
				oldDto.setCollectionStatus(policyDepositDto.getCollectionStatus());
				oldDto.setChallanNo(policyDepositDto.getChallanNo());
				oldDto.setAmount(policyDepositDto.getDepositAmount());
				oldDto.setAdjestmentAmount(policyDepositDto.getAdjustmentAmount());
				oldDto.setAvailableAmount(policyDepositDto.getAvailableAmount());
				oldDto.setTransactionMode(policyDepositDto.getTransactionMode());
				oldDto.setChequeRealisationDate(policyDepositDto.getChequeRealisationDate());
				oldDto.setRemark(policyDepositDto.getRemark());
				oldDto.setStatus(policyDepositDto.getStatus());
				oldDto.setZeroId(policyDepositDto.getZeroId());
				oldDto.setModifiedBy(policyDepositDto.getModifiedBy());
				oldDto.setModifiedOn(policyDepositDto.getModifiedOn());
				oldDto.setCreatedBy(policyDepositDto.getCreatedBy());
				oldDto.setCreatedOn(policyDepositDto.getCreatedOn());
				oldDto.setIsActive(policyDepositDto.getIsActive());

//				oldDto.setDepositId(policyDepositDto.getDepositId());
//				oldDto.setPolicyId(policyDepositDto.getPolicyId());
//				oldDto.setCollectionNo(policyDepositDto.getCollectionNo());
//				oldDto.setCollectionDate(policyDepositDto.getCollectionDate());
//				oldDto.setCollectionStatus(policyDepositDto.getCollectionStatus());
//				oldDto.setChallanNo(policyDepositDto.getChallanNo());
//				oldDto.setAmount(policyDepositDto.getDepositAmount());
//				oldDto.setAdjestmentAmount(policyDepositDto.getAdjustmentAmount());
//				oldDto.setAvailableAmount(policyDepositDto.getAvailableAmount());
//				oldDto.setTransactionMode(policyDepositDto.getTransactionMode());
//				oldDto.setChequeRealisationDate(policyDepositDto.getChequeRealisationDate());
//				oldDto.setRemark(policyDepositDto.getRemark());
//				oldDto.setStatus(policyDepositDto.getStatus());
//				oldDto.setZeroId(policyDepositDto.getZeroId());
//				oldDto.setModifiedBy(policyDepositDto.getModifiedBy());
//				oldDto.setModifiedOn(policyDepositDto.getModifiedOn());
//				oldDto.setCreatedBy(policyDepositDto.getCreatedBy());
//				oldDto.setCreatedOn(policyDepositDto.getCreatedOn());
//				oldDto.setIsActive(policyDepositDto.getIsActive());
				depositList.add(oldDto);
			}
			response.setDeposit(depositList);
			for (PolicyDepositDto policyContributionSummaryDto : policyMasterdto.getDeposits()) {
				PolicyAdjustmentOldDto oldDto = new PolicyAdjustmentOldDto();
				
				
				oldDto.setAdjestmentId(policyContributionSummaryDto.getDepositId());
				oldDto.setDepositId(policyContributionSummaryDto.getDepositId());
				oldDto.setPolicyId(policyContributionSummaryDto.getPolicyId());
				
				oldDto.setMasterPolicyId(policyContributionSummaryDto.getMasterPolicyId());
				oldDto.setTempPolicyId(policyContributionSummaryDto.getTempPolicyId());
				oldDto.setAdjustmentContributionId(policyContributionSummaryDto.getAdjustmentContributionId());
				oldDto.setRegularContributionId(policyContributionSummaryDto.getRegularContributionId());
				oldDto.setContributionType(policyContributionSummaryDto.getContributionType());
				
				oldDto.setCollectionNo(policyContributionSummaryDto.getCollectionNo());
				oldDto.setCollectionDate(policyContributionSummaryDto.getCollectionDate());
				oldDto.setCollectionStatus(policyContributionSummaryDto.getCollectionStatus());
				oldDto.setChallanNo(policyContributionSummaryDto.getChallanNo());
				oldDto.setAmount(policyContributionSummaryDto.getDepositAmount());
				oldDto.setAdjestmentAmount(policyContributionSummaryDto.getAdjustmentAmount());
				oldDto.setAvailableAmount(policyContributionSummaryDto.getAvailableAmount());
				oldDto.setTransactionMode(policyContributionSummaryDto.getTransactionMode());
				oldDto.setChequeRealisationDate(policyContributionSummaryDto.getChequeRealisationDate());
				oldDto.setRemark(policyContributionSummaryDto.getRemark());
				oldDto.setStatus(policyContributionSummaryDto.getStatus());
				oldDto.setZeroId(policyContributionSummaryDto.getZeroId());
				oldDto.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
				oldDto.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
				oldDto.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
				oldDto.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
				oldDto.setIsActive(policyContributionSummaryDto.getIsActive());
				
				
				
				
				
//				oldDto.setAdjestmentId(policyContributionSummaryDto.getDepositId());
//				oldDto.setChallanNo(policyContributionSummaryDto.getChallanNo());
//				oldDto.setDepositId(policyContributionSummaryDto.getDepositId());
//				oldDto.setPolicyId(policyContributionSummaryDto.getPolicyId());
//				oldDto.setCollectionNo(policyContributionSummaryDto.getCollectionNo());
//				oldDto.setCollectionDate(policyContributionSummaryDto.getCollectionDate());
//				oldDto.setAmount(policyContributionSummaryDto.getDepositAmount());
//				oldDto.setAdjestmentAmount(policyContributionSummaryDto.getAdjustmentAmount());
//				oldDto.setAvailableAmount(policyContributionSummaryDto.getAvailableAmount());
//				oldDto.setTransactionMode(policyContributionSummaryDto.getTransactionMode());
//				oldDto.setCollectionStatus(policyContributionSummaryDto.getCollectionStatus());
//				oldDto.setChequeRealisationDate(policyContributionSummaryDto.getChequeRealisationDate());
//				oldDto.setRemark(policyContributionSummaryDto.getRemark());
//				oldDto.setStatus(policyContributionSummaryDto.getStatus());
//				oldDto.setZeroId(policyContributionSummaryDto.getZeroId());
//				oldDto.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
//				oldDto.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
//				oldDto.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
//				oldDto.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
//				oldDto.setIsActive(policyContributionSummaryDto.getIsActive());
				adjustmentsList.add(oldDto);
			}
			response.setAdjustments(adjustmentsList);
			for (PolicyContributionSummaryDto policyContributionSummaryDto : policyMasterdto.getPolicyContributionSummary()) {
				response.setTotalDeposit(policyContributionSummaryDto.getClosingBalance());
				response.setTotalContribution(policyContributionSummaryDto.getTotalContribution());
				response.setEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
				response.setEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
				response.setVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
				response.setStampDuty(policyContributionSummaryDto.getStampDuty());
			}
			Set<PolicyNotesOldDto> notesList = new HashSet<>();
			for (PolicyNotesDto policyNotes : policyMasterdto.getNotes()) {
				PolicyNotesOldDto policyNotesDto = new PolicyNotesOldDto();
				policyNotesDto.setPolicyNoteId(policyNotes.getPolicyNoteId());
				policyNotesDto.setPolicyId(policyNotes.getPolicyId());
				policyNotesDto.setNoteContents(policyNotes.getNoteContents());
				policyNotesDto.setIsActive(policyNotes.getIsActive());
				policyNotesDto.setModifiedBy(policyNotes.getModifiedBy());
				policyNotesDto.setModifiedOn(policyNotes.getModifiedOn());
				policyNotesDto.setCreatedBy(policyNotes.getCreatedBy());
				policyNotesDto.setCreatedOn(policyNotes.getCreatedOn());
				notesList.add(policyNotesDto);
			}
			response.setNotes(notesList);
		}
		return response;
	}


	/***
	 * Adjustment contribution - inprogressload notes Entity to Dto
	 */
	public List<AdjustmentContributionNotesDto> convertNoteEntityToDto(List<AdjustmentContributionNotesTempEntity> adjustmentEntity) {
		List<AdjustmentContributionNotesDto> adjustmentContributionNotesDtoList = new ArrayList<>();
		if (CommonUtils.isNonEmptyArray(adjustmentEntity)) {
			adjustmentEntity.forEach(entity -> {
				AdjustmentContributionNotesDto dto = new AdjustmentContributionNotesDto();
				dto.setNotesId(entity.getNotesId());
				dto.setAdjustmentContributionId(entity.getAdjustmentContributionId());
				dto.setNoteContents(entity.getNoteContents());
				dto.setCreatedOn(entity.getCreatedOn());
				dto.setCreatedBy(entity.getCreatedBy());
				dto.setPolicyId(entity.getPolicyId());
				adjustmentContributionNotesDtoList.add(dto);
			});
		}
		return adjustmentContributionNotesDtoList;
	}

	// TEMP TO DTO

	public AdjustmentContributionDto convertTempEntityToDto(AdjustmentContributionTempEntity adjustmentEntity) {
		// adjustment contribution temp
		AdjustmentContributionDto dto = new AdjustmentContributionDto();
		//PK
		dto.setAdjustmentContributionId(adjustmentEntity.getAdjustmentContributionId());
		
		//FK 
		
		// Reference
		dto.setPolicyId(adjustmentEntity.getPolicyId());
		dto.setTempPolicyId(adjustmentEntity.getTempPolicyId());
		
		dto.setPolicyNumber(adjustmentEntity.getPolicyNumber());
		dto.setMphName(adjustmentEntity.getMphName());
		dto.setMphCode(adjustmentEntity.getMphCode());
		dto.setProposalNumber(adjustmentEntity.getProposalNumber());
		dto.setProduct(adjustmentEntity.getProduct());
		dto.setCustomerCode(adjustmentEntity.getCustomerCode());
		dto.setCustomerName(adjustmentEntity.getCustomerName());
		dto.setVariant(adjustmentEntity.getVariant());
		dto.setTotalContribution(adjustmentEntity.getTotalContribution());
		
		dto.setPolicyStatus(adjustmentEntity.getPolicyStatus());
		dto.setUnitCode(adjustmentEntity.getUnitCode());
		dto.setAdjustmentContributionStatus(adjustmentEntity.getAdjustmentContributionStatus());
		dto.setWorkFlowStatus(adjustmentEntity.getWorkFlowStatus());
		dto.setModifiedBy(adjustmentEntity.getModifiedBy());
		dto.setCreatedBy(adjustmentEntity.getCreatedBy());
		dto.setAmountToBeAdjusted(adjustmentEntity.getAmountToBeAdjusted());
		dto.setAdjustmentForDate(adjustmentEntity.getAdjustmentForDate());
		dto.setRejectionReasonCode(adjustmentEntity.getRejectionReasonCode());
		dto.setRejectionRemarks(adjustmentEntity.getRejectionRemarks());
		dto.setPolicyCommencementDate(adjustmentEntity.getPolicyCommencementDate());
		dto.setCustomerId(adjustmentEntity.getCustomerId());
		dto.setTotalDeposit(adjustmentEntity.getTotalDeposit());
		dto.setFirstPremium(adjustmentEntity.getFirstPremium());
		dto.setSinglePremiumFirstYr(adjustmentEntity.getSinglePremiumFirstYr());
		dto.setRenewalPremium(adjustmentEntity.getRenewalPremium());
		dto.setSubsequentSinglePremium(adjustmentEntity.getSubsequentSinglePremium());
		dto.setIsCommencementdateOneYr(adjustmentEntity.getIsCommencementdateOneYr());
		dto.setAdjustmentContributionNumber(adjustmentEntity.getAdjustmentContributionNumber());
		dto.setModifiedOn(adjustmentEntity.getModifiedOn());
		dto.setCreatedOn(adjustmentEntity.getCreatedOn());
		dto.setIsActive(adjustmentEntity.getIsActive());
		dto.setIsDeposit(adjustmentEntity.getIsDeposit());
		dto.setPolicyType(adjustmentEntity.getPolicyType());
		dto.setAdjustmentDueDate(adjustmentEntity.getAdjustmentDueDate());
		dto.setEffectiveDate(adjustmentEntity.getEffectiveDate());

		// Adjustment Contribution note
		Set<AdjustmentContributionNotesDto> noteList = new HashSet<>();
		for (AdjustmentContributionNotesTempEntity note : adjustmentEntity.getAdjustmentContributionNotes()) {
			AdjustmentContributionNotesDto noteDto = new AdjustmentContributionNotesDto();
			noteDto.setNotesId(note.getNotesId());
			noteDto.setAdjustmentContributionId(note.getAdjustmentContributionId());
			noteDto.setNoteContents(note.getNoteContents());
			noteDto.setCreatedOn(note.getCreatedOn());
			noteDto.setCreatedBy(note.getCreatedBy());
			noteDto.setPolicyId(note.getPolicyId());
			noteList.add(noteDto);
		}
		dto.setAdjustmentContributionNotes(noteList);
		return dto;
	}

	// MASTER TO DTO

	public AdjustmentContributionDto convertMainEntityToDtos(AdjustmentContributionEntity adjustmentEntity) {
		// adjustment contribution temp

		AdjustmentContributionDto dto = new AdjustmentContributionDto();
		dto.setPolicyNumber(adjustmentEntity.getPolicyNumber());
		dto.setMphName(adjustmentEntity.getMphName());
		dto.setMphCode(adjustmentEntity.getMphCode());
		dto.setProposalNumber(adjustmentEntity.getProposalNumber());
		dto.setProduct(adjustmentEntity.getProduct());
		dto.setCustomerCode(adjustmentEntity.getCustomerCode());
		dto.setCustomerName(adjustmentEntity.getCustomerName());
		dto.setVariant(adjustmentEntity.getVariant());
		dto.setTotalContribution(adjustmentEntity.getTotalContribution());
		dto.setPolicyId(adjustmentEntity.getPolicyId());
		dto.setTempPolicyId(adjustmentEntity.getTempPolicyId());
		dto.setPolicyStatus(adjustmentEntity.getPolicyStatus());
		dto.setUnitCode(adjustmentEntity.getUnitCode());
		dto.setAdjustmentContributionId(adjustmentEntity.getAdjustmentContributionId());
		dto.setAdjustmentContributionStatus(adjustmentEntity.getAdjustmentContributionStatus());
		dto.setWorkFlowStatus(adjustmentEntity.getWorkFlowStatus());
		dto.setModifiedBy(adjustmentEntity.getModifiedBy());
		dto.setCreatedBy(adjustmentEntity.getCreatedBy());
		dto.setAmountToBeAdjusted(adjustmentEntity.getAmountToBeAdjusted());
		dto.setAdjustmentForDate(adjustmentEntity.getAdjustmentForDate());
		dto.setRejectionReasonCode(adjustmentEntity.getRejectionReasonCode());
		dto.setRejectionRemarks(adjustmentEntity.getRejectionRemarks());
		dto.setPolicyCommencementDate(adjustmentEntity.getPolicyCommencementDate());
		dto.setCustomerId(adjustmentEntity.getCustomerId());
		dto.setTotalDeposit(adjustmentEntity.getTotalDeposit());
		dto.setFirstPremium(adjustmentEntity.getFirstPremium());
		dto.setSinglePremiumFirstYr(adjustmentEntity.getSinglePremiumFirstYr());
		dto.setRenewalPremium(adjustmentEntity.getRenewalPremium());
		dto.setSubsequentSinglePremium(adjustmentEntity.getSubsequentSinglePremium());
		dto.setIsCommencementdateOneYr(adjustmentEntity.getIsCommencementdateOneYr());
		dto.setAdjustmentContributionNumber(adjustmentEntity.getAdjustmentContributionNumber());
		dto.setModifiedOn(adjustmentEntity.getModifiedOn());
		dto.setCreatedOn(adjustmentEntity.getCreatedOn());
		dto.setIsActive(adjustmentEntity.getIsActive());
		dto.setIsDeposit(adjustmentEntity.getIsDeposit());
		dto.setPolicyType(adjustmentEntity.getPolicyType());
		dto.setAdjustmentDueDate(adjustmentEntity.getAdjustmentDueDate());
		dto.setEffectiveDate(adjustmentEntity.getEffectiveDate());
		
		// Adjustment Contribution note
		Set<AdjustmentContributionNotesDto> noteList = new HashSet<>();
		for (AdjustmentContributionNotesEntity note : adjustmentEntity.getAdjustmentContributionNotes()) {
			AdjustmentContributionNotesDto noteDto = new AdjustmentContributionNotesDto();
			noteDto.setNotesId(note.getNotesId());
			noteDto.setAdjustmentContributionId(note.getAdjustmentContributionId());
			noteDto.setNoteContents(note.getNoteContents());
			noteDto.setCreatedOn(note.getCreatedOn());
			noteDto.setCreatedBy(note.getCreatedBy());
			noteDto.setPolicyId(note.getPolicyId());
			noteList.add(noteDto);
		}
		dto.setAdjustmentContributionNotes(noteList);

		return dto;
	}

	// DTO TO TEMP

	public AdjustmentContributionTempEntity convertDtoToTempEntity(AdjustmentContributionDto dto) {
		AdjustmentContributionTempEntity entity = new AdjustmentContributionTempEntity();

		// adjustment contribution temp
		entity.setAdjustmentContributionId(dto.getAdjustmentContributionId());
		entity.setAdjustmentContributionStatus(dto.getAdjustmentContributionStatus());
		entity.setWorkFlowStatus(dto.getWorkFlowStatus());
		entity.setUnitCode(dto.getUnitCode());
		entity.setModifiedBy(dto.getModifiedBy());
		entity.setModifiedOn(dto.getModifiedOn());
		entity.setCreatedBy(dto.getCreatedBy());
		entity.setCreatedOn(dto.getCreatedOn());
		entity.setIsActive(dto.getIsActive());
		entity.setRejectionReasonCode(dto.getRejectionReasonCode());
		entity.setRejectionRemarks(dto.getRejectionRemarks());
		entity.setPolicyId(dto.getPolicyId());
		entity.setTempPolicyId(dto.getTempPolicyId());
		entity.setPolicyNumber(dto.getPolicyNumber());
		entity.setPolicyStatus(dto.getPolicyStatus());
		entity.setPolicyCommencementDate(dto.getPolicyCommencementDate());
		entity.setCustomerId(dto.getCustomerId());
		entity.setCustomerCode(dto.getCustomerCode());
		entity.setCustomerName(dto.getCustomerName());
		entity.setMphName(dto.getMphName());
		entity.setMphCode(dto.getMphCode());
		entity.setProposalNumber(dto.getProposalNumber());
		entity.setProduct(dto.getProduct());
		entity.setVariant(dto.getVariant());
		entity.setTotalContribution(dto.getTotalContribution());
		entity.setTotalDeposit(dto.getTotalDeposit());
		entity.setAmountToBeAdjusted(dto.getAmountToBeAdjusted());
		entity.setFirstPremium(dto.getFirstPremium());
		entity.setSinglePremiumFirstYr(dto.getSinglePremiumFirstYr());
		entity.setRenewalPremium(dto.getRenewalPremium());
		entity.setSubsequentSinglePremium(dto.getSubsequentSinglePremium());
		entity.setIsCommencementdateOneYr(dto.getIsCommencementdateOneYr());
		entity.setAdjustmentForDate(dto.getAdjustmentForDate());
		entity.setAdjustmentContributionNumber(dto.getAdjustmentContributionNumber());
		entity.setModifiedOn(dto.getModifiedOn());
		entity.setCreatedOn(dto.getCreatedOn());
		entity.setIsActive(dto.getIsActive());
		entity.setIsDeposit(dto.getIsDeposit());
		entity.setPolicyType(dto.getPolicyType());

		// Adjustment Contribution notetemp
		Set<AdjustmentContributionNotesTempEntity> noteList = new HashSet<>();
		for (AdjustmentContributionNotesDto noteDto : dto.getAdjustmentContributionNotes()) {
			AdjustmentContributionNotesTempEntity note = new AdjustmentContributionNotesTempEntity();
			note.setNotesId(noteDto.getNotesId());
			note.setAdjustmentContributionId(noteDto.getAdjustmentContributionId());
			note.setNoteContents(noteDto.getNoteContents());
			note.setCreatedOn(noteDto.getCreatedOn());
			note.setCreatedBy(noteDto.getCreatedBy());
			note.setPolicyId(noteDto.getPolicyId());
			noteList.add(note);
		}
		entity.setAdjustmentContributionNotes(noteList);

		return entity;
	}

	// DTO TO MASTER

	public AdjustmentContributionEntity convertDtoToMainEntitys(AdjustmentContributionDto dto) {
		AdjustmentContributionEntity entity = new AdjustmentContributionEntity();

		// adjustment contribution master
		entity.setAdjustmentContributionId(dto.getAdjustmentContributionId());
		entity.setAdjustmentContributionStatus(dto.getAdjustmentContributionStatus());
		entity.setWorkFlowStatus(dto.getWorkFlowStatus());
		entity.setUnitCode(dto.getUnitCode());
		entity.setModifiedBy(dto.getModifiedBy());
		entity.setModifiedOn(dto.getModifiedOn());
		entity.setCreatedBy(dto.getCreatedBy());
		entity.setCreatedOn(dto.getCreatedOn());
		entity.setIsActive(dto.getIsActive());
		entity.setRejectionReasonCode(dto.getRejectionReasonCode());
		entity.setRejectionRemarks(dto.getRejectionRemarks());
		entity.setPolicyId(dto.getPolicyId());
		entity.setTempPolicyId(dto.getTempPolicyId());
		entity.setPolicyNumber(dto.getPolicyNumber());
		entity.setPolicyStatus(dto.getPolicyStatus());
		entity.setPolicyCommencementDate(dto.getPolicyCommencementDate());
		entity.setCustomerId(dto.getCustomerId());
		entity.setCustomerCode(dto.getCustomerCode());
		entity.setCustomerName(dto.getCustomerName());
		entity.setMphName(dto.getMphName());
		entity.setMphCode(dto.getMphCode());
		entity.setProposalNumber(dto.getProposalNumber());
		entity.setProduct(dto.getProduct());
		entity.setVariant(dto.getVariant());
		entity.setTotalContribution(dto.getTotalContribution());
		entity.setTotalDeposit(dto.getTotalDeposit());
		entity.setAmountToBeAdjusted(dto.getAmountToBeAdjusted());
		entity.setFirstPremium(dto.getFirstPremium());
		entity.setSinglePremiumFirstYr(dto.getSinglePremiumFirstYr());
		entity.setRenewalPremium(dto.getRenewalPremium());
		entity.setSubsequentSinglePremium(dto.getSubsequentSinglePremium());
		entity.setIsCommencementdateOneYr(dto.getIsCommencementdateOneYr());
		entity.setAdjustmentForDate(dto.getAdjustmentForDate());
		entity.setAdjustmentContributionNumber(dto.getAdjustmentContributionNumber());
		entity.setModifiedOn(dto.getModifiedOn());
		entity.setCreatedOn(dto.getCreatedOn());
		entity.setIsActive(dto.getIsActive());
		entity.setIsDeposit(dto.getIsDeposit());
		entity.setPolicyType(dto.getPolicyType());
	

		// Adjustment Contribution note
		Set<AdjustmentContributionNotesEntity> noteList = new HashSet<>();
		for (AdjustmentContributionNotesDto noteDto : dto.getAdjustmentContributionNotes()) {
			AdjustmentContributionNotesEntity note = new AdjustmentContributionNotesEntity();
			note.setNotesId(noteDto.getNotesId());
			note.setAdjustmentContributionId(noteDto.getAdjustmentContributionId());
			note.setNoteContents(noteDto.getNoteContents());
			note.setCreatedOn(noteDto.getCreatedOn());
			note.setCreatedBy(noteDto.getCreatedBy());
			note.setPolicyId(noteDto.getPolicyId());
			noteList.add(note);
		}
		entity.setAdjustmentContributionNotes(noteList);

		return entity;
	}

	// TEMP TO MASTER

	public AdjustmentContributionEntity convertTempToMaster(AdjustmentContributionTempEntity tempEntity) {
		AdjustmentContributionEntity entity = new AdjustmentContributionEntity();

		// adjustment contribution master
		entity.setAdjustmentContributionId(null);
		entity.setAdjustmentContributionStatus(tempEntity.getAdjustmentContributionStatus());
		entity.setWorkFlowStatus(tempEntity.getWorkFlowStatus());
		entity.setUnitCode(tempEntity.getUnitCode());
		entity.setModifiedBy(tempEntity.getModifiedBy());
		entity.setModifiedOn(tempEntity.getModifiedOn());
		entity.setCreatedBy(tempEntity.getCreatedBy());
		entity.setCreatedOn(tempEntity.getCreatedOn());
		entity.setIsActive(tempEntity.getIsActive());
		entity.setRejectionReasonCode(tempEntity.getRejectionReasonCode());
		entity.setRejectionRemarks(tempEntity.getRejectionRemarks());
		entity.setPolicyId(tempEntity.getPolicyId());
		entity.setTempPolicyId(tempEntity.getTempPolicyId());
		entity.setPolicyNumber(tempEntity.getPolicyNumber());
		entity.setPolicyStatus(tempEntity.getPolicyStatus());
		entity.setPolicyCommencementDate(tempEntity.getPolicyCommencementDate());
		entity.setCustomerId(tempEntity.getCustomerId());
		entity.setCustomerCode(tempEntity.getCustomerCode());
		entity.setCustomerName(tempEntity.getCustomerName());
		entity.setMphName(tempEntity.getMphName());
		entity.setMphCode(tempEntity.getMphCode());
		entity.setProposalNumber(tempEntity.getProposalNumber());
		entity.setProduct(tempEntity.getProduct());
		entity.setVariant(tempEntity.getVariant());
		entity.setTotalContribution(tempEntity.getTotalContribution());
		
		entity.setEmployeeContribution(tempEntity.getEmployeeContribution());
		entity.setEmployerContribution(tempEntity.getEmployerContribution());
		entity.setVoluntaryContribution(tempEntity.getVoluntaryContribution());
		
		entity.setTotalDeposit(tempEntity.getTotalDeposit());
		entity.setAmountToBeAdjusted(tempEntity.getAmountToBeAdjusted());
		entity.setFirstPremium(tempEntity.getFirstPremium());
		entity.setSinglePremiumFirstYr(tempEntity.getSinglePremiumFirstYr());
		entity.setRenewalPremium(tempEntity.getRenewalPremium());
		entity.setSubsequentSinglePremium(tempEntity.getSubsequentSinglePremium());
		entity.setIsCommencementdateOneYr(tempEntity.getIsCommencementdateOneYr());
		entity.setAdjustmentForDate(tempEntity.getAdjustmentForDate());
		entity.setAdjustmentContributionNumber(tempEntity.getAdjustmentContributionNumber());
		entity.setIsDeposit(tempEntity.getIsDeposit());
		entity.setPolicyType(tempEntity.getPolicyType());
		entity.setAdjustmentDueDate(tempEntity.getAdjustmentDueDate());
		entity.setEffectiveDate(tempEntity.getEffectiveDate());		

		// Adjustment Contribution note
		Set<AdjustmentContributionNotesEntity> noteList = new HashSet<>();
		for (AdjustmentContributionNotesTempEntity noteTemp : tempEntity.getAdjustmentContributionNotes()) {
			AdjustmentContributionNotesEntity note = new AdjustmentContributionNotesEntity();
			note.setNotesId(null);
			note.setAdjustmentContributionId(entity.getAdjustmentContributionId());
			note.setNoteContents(noteTemp.getNoteContents());
			note.setCreatedOn(noteTemp.getCreatedOn());
			note.setCreatedBy(noteTemp.getCreatedBy());
			note.setPolicyId(noteTemp.getPolicyId());
			noteList.add(note);
		}
		entity.setAdjustmentContributionNotes(noteList);

		return entity;
	}

	public AdjustmentContributionEntity convertRejectTempToMaster(AdjustmentContributionTempEntity tempEntity) {
		AdjustmentContributionEntity entity = new AdjustmentContributionEntity();

		// adjustment contribution master
		entity.setAdjustmentContributionId(null);
		entity.setAdjustmentContributionStatus(tempEntity.getAdjustmentContributionStatus());
		entity.setWorkFlowStatus(tempEntity.getWorkFlowStatus());
		entity.setUnitCode(tempEntity.getUnitCode());
		entity.setModifiedBy(tempEntity.getModifiedBy());
		entity.setModifiedOn(new Date());
		entity.setIsActive(Boolean.FALSE);
		entity.setRejectionReasonCode(tempEntity.getRejectionReasonCode());
		entity.setRejectionRemarks(tempEntity.getRejectionRemarks());
		entity.setPolicyId(tempEntity.getPolicyId());
		entity.setTempPolicyId(tempEntity.getTempPolicyId());
		entity.setPolicyNumber(tempEntity.getPolicyNumber());
		entity.setPolicyStatus(tempEntity.getPolicyStatus());
		entity.setPolicyCommencementDate(tempEntity.getPolicyCommencementDate());
		entity.setCustomerId(tempEntity.getCustomerId());
		entity.setCustomerCode(tempEntity.getCustomerCode());
		entity.setCustomerName(tempEntity.getCustomerName());
		entity.setMphName(tempEntity.getMphName());
		entity.setMphCode(tempEntity.getMphCode());
		entity.setProposalNumber(tempEntity.getProposalNumber());
		entity.setProduct(tempEntity.getProduct());
		entity.setVariant(tempEntity.getVariant());
		entity.setTotalContribution(tempEntity.getTotalContribution());
		
		entity.setEmployeeContribution(tempEntity.getEmployeeContribution());
		entity.setEmployerContribution(tempEntity.getEmployerContribution());
		entity.setVoluntaryContribution(tempEntity.getVoluntaryContribution());
		
		entity.setTotalDeposit(tempEntity.getTotalDeposit());
		entity.setAmountToBeAdjusted(tempEntity.getAmountToBeAdjusted());
		entity.setFirstPremium(tempEntity.getFirstPremium());
		entity.setSinglePremiumFirstYr(tempEntity.getSinglePremiumFirstYr());
		entity.setRenewalPremium(tempEntity.getRenewalPremium());
		entity.setSubsequentSinglePremium(tempEntity.getSubsequentSinglePremium());
		entity.setIsCommencementdateOneYr(tempEntity.getIsCommencementdateOneYr());
		entity.setAdjustmentForDate(tempEntity.getAdjustmentForDate());
		entity.setAdjustmentContributionNumber(tempEntity.getAdjustmentContributionNumber());
		entity.setIsDeposit(tempEntity.getIsDeposit());
		entity.setPolicyType(tempEntity.getPolicyType());
		entity.setAdjustmentDueDate(tempEntity.getAdjustmentDueDate());
		entity.setEffectiveDate(tempEntity.getEffectiveDate());

		// Adjustment Contribution note
		Set<AdjustmentContributionNotesEntity> noteList = new HashSet<>();
		for (AdjustmentContributionNotesTempEntity noteTemp : tempEntity.getAdjustmentContributionNotes()) {
			AdjustmentContributionNotesEntity note = new AdjustmentContributionNotesEntity();
			note.setNotesId(null);
			note.setAdjustmentContributionId(entity.getAdjustmentContributionId());
			note.setNoteContents(noteTemp.getNoteContents());
			note.setCreatedOn(new Date());
			note.setCreatedBy(noteTemp.getCreatedBy());
			note.setPolicyId(noteTemp.getPolicyId());
			noteList.add(note);
		}
		entity.setAdjustmentContributionNotes(noteList);

		return entity;
	}

	private String calculateFinaYr(Date date) {
		return getFinancialYrByDt(date);
	}

	private static final MonthDay FINANCIAL_START = MonthDay.of(4, 1);
	private static final MonthDay FINANCIAL_END = MonthDay.of(3, 31);

	public static LocalDate getStartOfFinancialYear(LocalDate date) {
		/** Try "the same year as the date we've been given" */
		LocalDate candidate = date.with(FINANCIAL_START);
		/**
		 * If we haven't reached that yet, subtract a year. Otherwise, use it.
		 */
		return candidate.isAfter(date) ? candidate.minusYears(1) : candidate;
	}

	public static LocalDate getEndOfFinancialYear(LocalDate date) {
		/** Try "the same year as the date we've been given" */
		LocalDate candidate = date.with(FINANCIAL_END);
		/**
		 * If we haven't reached that yet, subtract a year. Otherwise, use it.
		 */
		return candidate.isBefore(date) ? candidate.plusYears(1) : candidate;
	}

	public static String getFinancialYrByDt(Date date) {
		LocalDate txnDate = dateToLocalDate(date);
		if (txnDate != null) {
			int startYear = getStartOfFinancialYear(txnDate).getYear();
			int endYear = getEndOfFinancialYear(txnDate).getYear();
			return startYear + "-" + endYear;
		}
		return null;
	}

	public static LocalDate dateToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date convertStringToDateDDMMYYYYSlash(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(DDMMYYYY_SLASH);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				/***  */
			}
		}
		return null;
	}

	private static final String DDMMYYYY_SLASH = "dd/MM/yyyy";
	
	public void convertContibutionTempToMain(Long policyId, Long tempPolicyId, Long adjustmentContributionId,Long masterAdjustmentContributionId,String role) {
		
		AdjustmentContributionTempEntity adjustmentContributionTempEntity = adjustmentContributionTempRepository.findByAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionId);

		List<PolicyContributionTempEntity> policyContributionTempEntityList = policyContributionTempRepository.findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionTempEntity.getTempPolicyId(),adjustmentContributionTempEntity.getAdjustmentContributionId());

//		List<PolicyContributionEntity> policyContributionEntityList = policyContributionRepository.findByPolicyIdAndIsActiveTrueOrderByVersionNoDesc(adjustmentContributionTempEntity.getPolicyId());

		
		for (PolicyContributionTempEntity policyContributionTempEntity : policyContributionTempEntityList) {
			PolicyContributionEntity policyContribution = new PolicyContributionEntity();

			policyContribution.setContributionId(null);
			policyContribution.setPolicyId(policyContributionTempEntity.getMasterPolicyId());
			policyContribution.setTempPolicyId(policyContributionTempEntity.getPolicyId());
			policyContribution.setAdjustmentContributionId(masterAdjustmentContributionId);
			policyContribution.setContributionType(policyContributionTempEntity.getContributionType());
			policyContribution.setContributionDate(policyContributionTempEntity.getContributionDate());
			policyContribution.setContReferenceNo(policyContributionTempEntity.getContReferenceNo());
			policyContribution.setVersionNo(policyContributionTempEntity.getVersionNo()+1);
			policyContribution.setFinancialYear(calculateFinaYr(new Date()));
			
			policyContribution.setEmployeeContribution(policyContributionTempEntity.getEmployerContribution());
			policyContribution.setEmployerContribution(policyContributionTempEntity.getEmployeeContribution());
			policyContribution.setVoluntaryContribution(policyContributionTempEntity.getVoluntaryContribution());
			
			policyContribution.setTotalContribution(policyContributionTempEntity.getTotalContribution());
			policyContribution.setOpeningBalance(policyContributionTempEntity.getOpeningBalance());
			policyContribution.setClosingBalance(policyContribution.getOpeningBalance().add(policyContribution.getTotalContribution()));
			
			policyContribution.setIsDeposit(policyContributionTempEntity.getIsDeposit());

			policyContribution.setIsActive(Boolean.TRUE);
			policyContribution.setCreatedBy(role);
			policyContribution.setCreatedOn(new Date());
			policyContribution.setModifiedBy(role);
			policyContribution.setModifiedOn(new Date());
			policyContribution.setTxnEntryStatus(Boolean.FALSE);
			policyContribution.setAdjustmentDueDate(policyContributionTempEntity.getAdjustmentDueDate());
			policyContribution.setEffectiveDate(policyContributionTempEntity.getEffectiveDate());

			Set<MemberContributionEntity> memberContributionList = new HashSet<>();
			if (policyContribution.getPolicyContribution() != null) {
				for (MemberContributionTempEntity memberContributionTemp : policyContributionTempEntity
						.getPolicyContribution()) {
					MemberContributionEntity memberContribution = new MemberContributionEntity();

					memberContribution.setMemberConId(null);
					memberContribution.setPolicyConId(policyContribution.getContributionId());

					memberContribution.setTempPolicyId(memberContributionTemp.getPolicyId());
					memberContribution.setTempMemberId(memberContributionTemp.getMemberId());

					memberContribution.setPolicyId(memberContributionTemp.getMasterPolicyId());
					memberContribution.setMemberId(memberContributionTemp.getMasterMemberId());

					memberContribution.setAdjustmentContributionId(masterAdjustmentContributionId);

					memberContribution.setContributionType(memberContributionTemp.getContributionType());
					memberContribution.setContributionDate(memberContributionTemp.getContributionDate());

					memberContribution.setFinancialYear(calculateFinaYr(new Date()));
					memberContribution.setVersionNo(memberContributionTemp.getVersionNo());

					memberContribution.setLicId(memberContributionTemp.getLicId());

					memberContribution.setEmployeeContribution(memberContributionTemp.getEmployerContribution());
					memberContribution.setEmployerContribution(memberContributionTemp.getEmployeeContribution());
					memberContribution.setVoluntaryContribution(memberContributionTemp.getVoluntaryContribution());
					
					memberContribution.setTotalContribution(memberContributionTemp.getTotalContribution());
					memberContribution.setOpeningBalance(memberContributionTemp.getOpeningBalance());
					memberContribution.setClosingBalance(memberContribution.getOpeningBalance().add(memberContribution.getTotalContribution()));
					
					memberContribution.setTotalInterestedAccured(memberContributionTemp.getTotalInterestedAccured());
					memberContribution.setIsDeposit(memberContributionTemp.getIsDeposit());
					memberContribution.setIsActive(Boolean.TRUE);
					memberContribution.setCreatedBy(role);
					memberContribution.setCreatedOn(new Date());
					memberContribution.setModifiedBy(role);
					memberContribution.setModifiedOn(new Date());
					memberContribution.setTxnEntryStatus(Boolean.FALSE);
					memberContribution.setAdjustmentDueDate(memberContributionTemp.getAdjustmentDueDate());
					memberContribution.setEffectiveDate(memberContributionTemp.getEffectiveDate());
               
					memberContributionList.add(memberContribution);

				}
				policyContribution.setPolicyContribution(memberContributionList);
			}
			policyContributionRepository.save(policyContribution);
		}
		
		
		List<PolicyDepositTempEntity> policyDepositTempEntityList = policyDepositTempRepository.findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionTempEntity.getTempPolicyId(),adjustmentContributionTempEntity.getAdjustmentContributionId());
		
		for (PolicyDepositTempEntity policyDepositTempEntity : policyDepositTempEntityList) {
			PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

			policyDepositEntity.setDepositId(null);
			policyDepositEntity.setPolicyId(policyDepositTempEntity.getMasterPolicyId());

			policyDepositEntity.setTempPolicyId(policyDepositTempEntity.getPolicyId());
			policyDepositEntity.setContributionType(policyDepositTempEntity.getContributionType());
			policyDepositEntity.setAdjustmentContributionId(masterAdjustmentContributionId);

			policyDepositEntity.setCollectionNo(policyDepositTempEntity.getCollectionNo());
			policyDepositEntity.setCollectionDate(policyDepositTempEntity.getCollectionDate());
			policyDepositEntity.setCollectionStatus(policyDepositTempEntity.getCollectionStatus());
			policyDepositEntity.setChallanNo(policyDepositTempEntity.getChallanNo());
			policyDepositEntity.setDepositAmount(policyDepositTempEntity.getDepositAmount());
			policyDepositEntity.setAdjustmentAmount(policyDepositTempEntity.getAdjustmentAmount());
			policyDepositEntity.setAvailableAmount(policyDepositTempEntity.getAvailableAmount());
			policyDepositEntity.setTransactionMode(policyDepositTempEntity.getTransactionMode());
			policyDepositEntity.setChequeRealisationDate(policyDepositTempEntity.getChequeRealisationDate());
			policyDepositEntity.setRemark(policyDepositTempEntity.getRemark());
			policyDepositEntity.setStatus(policyDepositTempEntity.getStatus());
			policyDepositEntity.setZeroId(policyDepositTempEntity.getZeroId());
			policyDepositEntity.setIsDeposit(policyDepositTempEntity.getIsDeposit());
			policyDepositEntity.setModifiedBy(role);
			policyDepositEntity.setModifiedOn(new Date());
			policyDepositEntity.setCreatedBy(role);
			policyDepositEntity.setCreatedOn(new Date());
			policyDepositEntity.setIsActive(Boolean.TRUE);

			policyDepositRepository.save(policyDepositEntity);
		}

	}
	
	public void convertRejectContibutionTempToMain(Long policyId, Long tempPolicyId, Long adjustmentContributionId,Long masterAdjustmentContributionId,String role) {
		AdjustmentContributionTempEntity adjustmentContributionTempEntity = adjustmentContributionTempRepository.findByAdjustmentContributionIdAndIsActiveFalse(adjustmentContributionId);

		List<PolicyContributionTempEntity> policyContributionTempEntityList = policyContributionTempRepository.findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionTempEntity.getTempPolicyId(),adjustmentContributionTempEntity.getAdjustmentContributionId());
		
		List<PolicyDepositTempEntity> policyDepositTempEntityList = policyDepositTempRepository.findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionTempEntity.getTempPolicyId(),adjustmentContributionTempEntity.getAdjustmentContributionId());
		
		for (PolicyDepositTempEntity policyDepositTempEntity : policyDepositTempEntityList) {
			PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

			policyDepositEntity.setDepositId(null);
			policyDepositEntity.setPolicyId(policyDepositTempEntity.getMasterPolicyId());

			policyDepositEntity.setTempPolicyId(policyDepositTempEntity.getPolicyId());
			policyDepositEntity.setContributionType(policyDepositTempEntity.getContributionType());
			policyDepositEntity.setAdjustmentContributionId(masterAdjustmentContributionId);

			policyDepositEntity.setCollectionNo(policyDepositTempEntity.getCollectionNo());
			policyDepositEntity.setCollectionDate(policyDepositTempEntity.getCollectionDate());
			policyDepositEntity.setCollectionStatus(policyDepositTempEntity.getCollectionStatus());
			policyDepositEntity.setChallanNo(policyDepositTempEntity.getChallanNo());
			policyDepositEntity.setDepositAmount(policyDepositTempEntity.getDepositAmount());
			policyDepositEntity.setAdjustmentAmount(policyDepositTempEntity.getAdjustmentAmount());
			policyDepositEntity.setAvailableAmount(policyDepositTempEntity.getAvailableAmount());
			policyDepositEntity.setTransactionMode(policyDepositTempEntity.getTransactionMode());
			policyDepositEntity.setChequeRealisationDate(policyDepositTempEntity.getChequeRealisationDate());
			policyDepositEntity.setRemark(policyDepositTempEntity.getRemark());
			policyDepositEntity.setStatus(policyDepositTempEntity.getStatus());
			policyDepositEntity.setZeroId(policyDepositTempEntity.getZeroId());
			policyDepositEntity.setIsDeposit(policyDepositTempEntity.getIsDeposit());
			policyDepositEntity.setModifiedBy(role);
			policyDepositEntity.setModifiedOn(new Date());
			policyDepositEntity.setIsActive(Boolean.FALSE);

			policyDepositRepository.save(policyDepositEntity);
		}
		

		for (PolicyContributionTempEntity policyContributionTempEntity : policyContributionTempEntityList) {
			PolicyContributionEntity policyContribution = new PolicyContributionEntity();

			policyContribution.setContributionId(null);
			policyContribution.setPolicyId(policyContributionTempEntity.getMasterPolicyId());
			policyContribution.setTempPolicyId(policyContributionTempEntity.getPolicyId());
			policyContribution.setAdjustmentContributionId(masterAdjustmentContributionId);
			policyContribution.setContributionType(policyContributionTempEntity.getContributionType());
			policyContribution.setContributionDate(policyContributionTempEntity.getContributionDate());
			policyContribution.setContReferenceNo(policyContributionTempEntity.getContReferenceNo());
			policyContribution.setVersionNo(policyContributionTempEntity.getVersionNo());
			policyContribution.setFinancialYear(calculateFinaYr(new Date()));
			policyContribution.setOpeningBalance(policyContributionTempEntity.getOpeningBalance());
			policyContribution.setClosingBalance(policyContributionTempEntity.getClosingBalance());
			policyContribution.setEmployerContribution(policyContributionTempEntity.getEmployerContribution());
			policyContribution.setEmployeeContribution(policyContributionTempEntity.getEmployeeContribution());
			policyContribution.setVoluntaryContribution(policyContributionTempEntity.getVoluntaryContribution());
			policyContribution.setTotalContribution(policyContributionTempEntity.getTotalContribution());
			policyContribution.setIsDeposit(policyContributionTempEntity.getIsDeposit());
			policyContribution.setIsActive(Boolean.FALSE);
			policyContribution.setModifiedBy(role);
			policyContribution.setModifiedOn(new Date());
			policyContribution.setAdjustmentDueDate(policyContributionTempEntity.getAdjustmentDueDate());
			policyContribution.setEffectiveDate(policyContributionTempEntity.getEffectiveDate());			

			Set<MemberContributionEntity> memberContributionList = new HashSet<>();
			if (policyContribution.getPolicyContribution() != null) {
				for (MemberContributionTempEntity memberContributionTemp : policyContributionTempEntity.getPolicyContribution()) {
					
					MemberContributionEntity memberContribution = new MemberContributionEntity();

					memberContribution.setMemberConId(null);
					memberContribution.setPolicyConId(policyContribution.getContributionId());

					memberContribution.setTempPolicyId(memberContributionTemp.getPolicyId());
					memberContribution.setTempMemberId(memberContributionTemp.getMemberId());

					memberContribution.setPolicyId(memberContributionTemp.getMasterPolicyId());
					memberContribution.setMemberId(memberContributionTemp.getMasterMemberId());

					memberContribution.setAdjustmentContributionId(masterAdjustmentContributionId);

					memberContribution.setContributionType(memberContributionTemp.getContributionType());
					memberContribution.setContributionDate(memberContributionTemp.getContributionDate());

					memberContribution.setFinancialYear(calculateFinaYr(new Date()));
					memberContribution.setVersionNo(memberContributionTemp.getVersionNo());

					memberContribution.setLicId(memberContributionTemp.getLicId());

					memberContribution.setOpeningBalance(memberContributionTemp.getOpeningBalance());
					memberContribution.setClosingBalance(memberContributionTemp.getClosingBalance());
					memberContribution.setEmployeeContribution(memberContributionTemp.getEmployerContribution());
					memberContribution.setEmployerContribution(memberContributionTemp.getEmployeeContribution());
					memberContribution.setVoluntaryContribution(memberContributionTemp.getVoluntaryContribution());
					memberContribution.setTotalContribution(memberContributionTemp.getTotalContribution());

					memberContribution.setTotalInterestedAccured(memberContributionTemp.getTotalInterestedAccured());
					memberContribution.setIsDeposit(memberContributionTemp.getIsDeposit());
					memberContribution.setIsActive(Boolean.FALSE);
					memberContribution.setModifiedBy(role);
					memberContribution.setModifiedOn(new Date());
					memberContribution.setAdjustmentDueDate(memberContributionTemp.getAdjustmentDueDate());
					memberContribution.setEffectiveDate(memberContributionTemp.getEffectiveDate());;

					memberContributionList.add(memberContribution);

				}
				policyContribution.setPolicyContribution(memberContributionList);
			}
			policyContributionRepository.save(policyContribution);
		}

	}
}
