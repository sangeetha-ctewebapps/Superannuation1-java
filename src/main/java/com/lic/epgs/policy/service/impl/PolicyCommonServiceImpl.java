package com.lic.epgs.policy.service.impl;

/**
 * @author pradeepramesh
 *
 */
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.dto.MemberAddressDto;
import com.lic.epgs.policy.dto.MemberAppointeeDto;
import com.lic.epgs.policy.dto.MemberBankDto;
import com.lic.epgs.policy.dto.MemberContributionDto;
import com.lic.epgs.policy.dto.MemberContributionSummaryDto;
import com.lic.epgs.policy.dto.MemberMasterDto;
import com.lic.epgs.policy.dto.MemberNomineeDto;
import com.lic.epgs.policy.dto.MphAddressDto;
import com.lic.epgs.policy.dto.MphBankDto;
import com.lic.epgs.policy.dto.MphMasterDto;
import com.lic.epgs.policy.dto.MphRepresentativesDto;
import com.lic.epgs.policy.dto.PolicyContributionDto;
import com.lic.epgs.policy.dto.PolicyContributionSummaryDto;
import com.lic.epgs.policy.dto.PolicyDepositDto;
import com.lic.epgs.policy.dto.PolicyMasterDto;
import com.lic.epgs.policy.dto.PolicyNotesDto;
import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policy.dto.PolicyTransactionEntriesDto;
import com.lic.epgs.policy.dto.PolicyTransactionSummaryDto;
import com.lic.epgs.policy.dto.PolicyValuationDto;
import com.lic.epgs.policy.dto.ZeroAccountEntriesDto;
import com.lic.epgs.policy.dto.ZeroAccountdto;
import com.lic.epgs.policy.entity.MemberAddressEntity;
import com.lic.epgs.policy.entity.MemberAddressTempEntity;
import com.lic.epgs.policy.entity.MemberAppointeeEntity;
import com.lic.epgs.policy.entity.MemberAppointeeTempEntity;
import com.lic.epgs.policy.entity.MemberBankEntity;
import com.lic.epgs.policy.entity.MemberBankTempEntity;
import com.lic.epgs.policy.entity.MemberContributionEntity;
import com.lic.epgs.policy.entity.MemberContributionSummaryEntity;
import com.lic.epgs.policy.entity.MemberContributionSummaryTempEntity;
import com.lic.epgs.policy.entity.MemberContributionTempEntity;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MemberMasterTempEntity;
import com.lic.epgs.policy.entity.MemberNomineeEntity;
import com.lic.epgs.policy.entity.MemberNomineeTempEntity;
import com.lic.epgs.policy.entity.MphAddressEntity;
import com.lic.epgs.policy.entity.MphAddressTempEntity;
import com.lic.epgs.policy.entity.MphBankEntity;
import com.lic.epgs.policy.entity.MphBankTempEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.MphMasterTempEntity;
import com.lic.epgs.policy.entity.MphRepresentativesEntity;
import com.lic.epgs.policy.entity.MphRepresentativesTempEntity;
import com.lic.epgs.policy.entity.PolicyContributionEntity;
import com.lic.epgs.policy.entity.PolicyContributionSummaryEntity;
import com.lic.epgs.policy.entity.PolicyContributionSummaryTempEntity;
import com.lic.epgs.policy.entity.PolicyContributionTempEntity;
import com.lic.epgs.policy.entity.PolicyDepositEntity;
import com.lic.epgs.policy.entity.PolicyDepositTempEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterTempEntity;
import com.lic.epgs.policy.entity.PolicyNotesEntity;
import com.lic.epgs.policy.entity.PolicyNotesTempEntity;
import com.lic.epgs.policy.entity.PolicyRulesEntity;
import com.lic.epgs.policy.entity.PolicyRulesTempEntity;
import com.lic.epgs.policy.entity.PolicyTransactionEntriesEntity;
import com.lic.epgs.policy.entity.PolicyTransactionEntriesTempEntity;
import com.lic.epgs.policy.entity.PolicyTransactionSummaryEntity;
import com.lic.epgs.policy.entity.PolicyTransactionSummaryTempEntity;
import com.lic.epgs.policy.entity.PolicyValuationEntity;
import com.lic.epgs.policy.entity.PolicyValuationTempEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesTempEntity;
import com.lic.epgs.policy.entity.ZeroAccountTempEntity;
import com.lic.epgs.policy.old.dto.PolicyAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyAdjustmentOldDto;
import com.lic.epgs.policy.old.dto.PolicyBankOldDto;
import com.lic.epgs.policy.old.dto.PolicyDepositOldDto;
import com.lic.epgs.policy.old.dto.PolicyDto;
import com.lic.epgs.policy.old.dto.PolicyMbrAddressOldDto;
import com.lic.epgs.policy.old.dto.PolicyMbrApponinteeOldDto;
import com.lic.epgs.policy.old.dto.PolicyMbrBankOldDto;
import com.lic.epgs.policy.old.dto.PolicyMbrNomineeOldDto;
import com.lic.epgs.policy.old.dto.PolicyMbrOldDto;
import com.lic.epgs.policy.old.dto.PolicyNotesOldDto;
import com.lic.epgs.policy.old.dto.PolicyRulesOldDto;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.quotation.entity.QuotationMemberAddressEntity;
import com.lic.epgs.quotation.entity.QuotationMemberAppointeeEntity;
import com.lic.epgs.quotation.entity.QuotationMemberBankDetailEntity;
import com.lic.epgs.quotation.entity.QuotationMemberEntity;
import com.lic.epgs.quotation.entity.QuotationMemberNomineeEntity;
import com.lic.epgs.quotation.repository.QuotationRepository;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
public class PolicyCommonServiceImpl {

	@Autowired
	PolicyServiceImpl policyServiceImpl;

	@Autowired
	QuotationRepository quotationRepository;

	@Autowired
	private PolicyMasterRepository policyMasterRepository;

	@Autowired
	private MemberMasterRepository memberMasterRepository;

	/**
	 * Set<MphAddressTempEntity> mphAddress = new HashSet<>();
	 * Set<MphBankTempEntity> mphBank = new HashSet<>();
	 * Set<MphRepresentativesTempEntity> mphRepresentative = new HashSet<>();
	 * Set<PolicyMasterTempEntity> policyMaster = new HashSet<>();
	 */

//	MPH MASTER	
//	DTO to TempEntity
	public MphMasterTempEntity mphMasterSetDtoToTempEntity(MphMasterDto request) {
		MphMasterTempEntity response = new MphMasterTempEntity();

		response.setMphId(request.getMphId());
		response.setMasterMphId(request.getMasterMphId());
		response.setMphCode(request.getMphCode());
		response.setMphName(request.getMphName());
		response.setMphType(request.getMphType());
		response.setProposalNumber(request.getProposalNumber());
		response.setProposalId(request.getProposalId());
		response.setCin(request.getCin());
		response.setPan(request.getPan());
		response.setAlternatePan(request.getAlternatePan());
		response.setLandlineNo(request.getLandlineNo());
		response.setCountryCode(request.getCountryCode());
		response.setMobileNo(request.getMobileNo());
		response.setEmailId(request.getEmailId());
		response.setFax(request.getFax());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	DTO to Entity
	public MphMasterEntity mphMasterSetDtoToEntity(MphMasterDto request) {
		MphMasterEntity response = new MphMasterEntity();

		response.setMphId(request.getMphId());
		response.setTempMphId(request.getMasterMphId());
		response.setMphCode(request.getMphCode());
		response.setMphName(request.getMphName());
		response.setMphType(request.getMphType());
		response.setProposalNumber(request.getProposalNumber());
		response.setProposalId(request.getProposalId());
		response.setCin(request.getCin());
		response.setPan(request.getPan());
		response.setAlternatePan(request.getAlternatePan());
		response.setLandlineNo(request.getLandlineNo());
		response.setCountryCode(request.getCountryCode());
		response.setMobileNo(request.getMobileNo());
		response.setEmailId(request.getEmailId());
		response.setFax(request.getFax());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	TempEntity to Dto
	public MphMasterDto mphMasterSetTempEntityToDto(MphMasterTempEntity request) {
		MphMasterDto response = new MphMasterDto();

		response.setMphId(request.getMphId());
		response.setMasterMphId(request.getMasterMphId());
		response.setMphCode(request.getMphCode());
		response.setMphName(request.getMphName());
		response.setMphType(request.getMphType());
		response.setProposalNumber(request.getProposalNumber());
		response.setProposalId(request.getProposalId());
		response.setCin(request.getCin());
		response.setPan(request.getPan());
		response.setAlternatePan(request.getAlternatePan());
		response.setLandlineNo(request.getLandlineNo());
		response.setCountryCode(request.getCountryCode());
		response.setMobileNo(request.getMobileNo());
		response.setEmailId(request.getEmailId());
		response.setFax(request.getFax());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	Entity to Dto
	public MphMasterDto mphMasterSetEntityToDto(MphMasterEntity request) {
		MphMasterDto response = new MphMasterDto();

		response.setMphId(request.getMphId());
		response.setMasterMphId(request.getMphId());
		response.setMphCode(request.getMphCode());
		response.setMphName(request.getMphName());
		response.setMphType(request.getMphType());
		response.setProposalNumber(request.getProposalNumber());
		response.setProposalId(request.getProposalId());
		response.setCin(request.getCin());
		response.setPan(request.getPan());
		response.setAlternatePan(request.getAlternatePan());
		response.setLandlineNo(request.getLandlineNo());
		response.setCountryCode(request.getCountryCode());
		response.setMobileNo(request.getMobileNo());
		response.setEmailId(request.getEmailId());
		response.setFax(request.getFax());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	TempEntity to Entity
	public MphMasterEntity mphMasterSetTempEntityToEntity(MphMasterTempEntity request) {
		MphMasterEntity response = new MphMasterEntity();

		response.setMphId(request.getMasterMphId());
		response.setTempMphId(request.getMphId());
		response.setMphCode(request.getMphCode());
		response.setMphName(request.getMphName());
		response.setMphType(request.getMphType());
		response.setProposalNumber(request.getProposalNumber());
		response.setProposalId(request.getProposalId());
		response.setCin(request.getCin());
		response.setPan(request.getPan());
		response.setAlternatePan(request.getAlternatePan());
		response.setLandlineNo(request.getLandlineNo());
		response.setCountryCode(request.getCountryCode());
		response.setMobileNo(request.getMobileNo());
		response.setEmailId(request.getEmailId());
		response.setFax(request.getFax());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	Entity to TempEntity
	public MphMasterTempEntity mphMasterSetEntityToTempEntity(MphMasterEntity request) {
		MphMasterTempEntity response = new MphMasterTempEntity();

		response.setMphId(request.getTempMphId());
		response.setMasterMphId(request.getMphId());
		response.setMphCode(request.getMphCode());
		response.setMphName(request.getMphName());
		response.setMphType(request.getMphType());
		response.setProposalNumber(request.getProposalNumber());
		response.setProposalId(request.getProposalId());
		response.setCin(request.getCin());
		response.setPan(request.getPan());
		response.setAlternatePan(request.getAlternatePan());
		response.setLandlineNo(request.getLandlineNo());
		response.setCountryCode(request.getCountryCode());
		response.setMobileNo(request.getMobileNo());
		response.setEmailId(request.getEmailId());
		response.setFax(request.getFax());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	MPH Address	
//	DTO to TempEntity
	public MphAddressTempEntity mphAddressSetDtoToTempEntity(MphAddressDto request) {
		MphAddressTempEntity response = new MphAddressTempEntity();

		response.setAddressId(response.getAddressId());
		response.setMphId(response.getMphId());
		response.setIsDefault(response.getIsDefault());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryId(response.getCountryId());
		response.setStateId(response.getStateId());
		response.setDistrictId(response.getDistrictId());
		response.setCityId(response.getCityId());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	DTO to Entity
	public MphAddressEntity mphAddressSetDtoToEntity(MphAddressDto request) {
		MphAddressEntity response = new MphAddressEntity();

		response.setAddressId(response.getAddressId());
		response.setMphId(response.getMphId());
		response.setIsDefault(response.getIsDefault());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryId(response.getCountryId());
		response.setStateId(response.getStateId());
		response.setDistrictId(response.getDistrictId());
		response.setCityId(response.getCityId());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	TempEntity to Dto
	public MphAddressDto mphAddressSetTempEntityToDto(MphAddressTempEntity request) {
		MphAddressDto response = new MphAddressDto();

		response.setAddressId(response.getAddressId());
		response.setMphId(response.getMphId());
		response.setIsDefault(response.getIsDefault());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryId(response.getCountryId());
		response.setStateId(response.getStateId());
		response.setDistrictId(response.getDistrictId());
		response.setCityId(response.getCityId());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	Entity to Dto
	public MphAddressDto mphAddressSetEntityToDto(MphAddressEntity request) {
		MphAddressDto response = new MphAddressDto();

		response.setAddressId(response.getAddressId());
		response.setMphId(response.getMphId());
		response.setIsDefault(response.getIsDefault());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryId(response.getCountryId());
		response.setStateId(response.getStateId());
		response.setDistrictId(response.getDistrictId());
		response.setCityId(response.getCityId());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	TempEntity to Entity
	public MphAddressEntity mphAddressSetTempEntityToEntity(MphAddressTempEntity request) {
		MphAddressEntity response = new MphAddressEntity();

		response.setAddressId(response.getAddressId());
		response.setMphId(response.getMphId());
		response.setIsDefault(response.getIsDefault());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryId(response.getCountryId());
		response.setStateId(response.getStateId());
		response.setDistrictId(response.getDistrictId());
		response.setCityId(response.getCityId());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	Entity to TempEntity
	public MphAddressTempEntity mphAddressSetEntityToTempEntity(MphAddressEntity request) {
		MphAddressTempEntity response = new MphAddressTempEntity();

		response.setAddressId(response.getAddressId());
		response.setMphId(response.getMphId());
		response.setIsDefault(response.getIsDefault());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryId(response.getCountryId());
		response.setStateId(response.getStateId());
		response.setDistrictId(response.getDistrictId());
		response.setCityId(response.getCityId());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	MPH Bank	
//	DTO to TempEntity
	public MphBankTempEntity mphBankSetDtoToTempEntity(MphBankDto request) {
		MphBankTempEntity response = new MphBankTempEntity();

		response.setMphBankId(request.getMphBankId());
		response.setIsDefault(request.getIsDefault());
		response.setAccountNumber(request.getAccountNumber());
		response.setAccountType(request.getAccountType());
		response.setBankAddress(request.getBankAddress());
		response.setBankBranch(request.getBankBranch());
		response.setBankName(request.getBankName());
		response.setConfirmAccountNumber(request.getConfirmAccountNumber());
		response.setCountryCode(request.getCountryCode());
		response.setEmailId(request.getEmailId());
		response.setIfscCode(request.getIfscCode());
		response.setIfscCodeAvailable(request.getIfscCodeAvailable());
		response.setLandlineNumber(request.getLandlineNumber());
		response.setStdCode(request.getStdCode());
		response.setMphId(request.getMphId());
		response.setCountryId(request.getCountryId());
		response.setStateId(request.getStateId());
		response.setDistrictId(request.getDistrictId());
		response.setCityId(request.getCityId());
		response.setTownLocality(request.getTownLocality());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	DTO to Entity
	public MphBankEntity mphBankSetDtoToEntity(MphBankDto request) {
		MphBankEntity response = new MphBankEntity();

		response.setMphBankId(request.getMphBankId());
		response.setIsDefault(request.getIsDefault());
		response.setAccountNumber(request.getAccountNumber());
		response.setAccountType(request.getAccountType());
		response.setBankAddress(request.getBankAddress());
		response.setBankBranch(request.getBankBranch());
		response.setBankName(request.getBankName());
		response.setConfirmAccountNumber(request.getConfirmAccountNumber());
		response.setCountryCode(request.getCountryCode());
		response.setEmailId(request.getEmailId());
		response.setIfscCode(request.getIfscCode());
		response.setIfscCodeAvailable(request.getIfscCodeAvailable());
		response.setLandlineNumber(request.getLandlineNumber());
		response.setStdCode(request.getStdCode());
		response.setMphId(request.getMphId());
		response.setCountryId(request.getCountryId());
		response.setStateId(request.getStateId());
		response.setDistrictId(request.getDistrictId());
		response.setCityId(request.getCityId());
		response.setTownLocality(request.getTownLocality());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	TempEntity to Dto
	public MphBankDto mphBankSetTempEntityToDto(MphBankTempEntity request) {
		MphBankDto response = new MphBankDto();

		response.setMphBankId(request.getMphBankId());
		response.setIsDefault(request.getIsDefault());
		response.setAccountNumber(request.getAccountNumber());
		response.setAccountType(request.getAccountType());
		response.setBankAddress(request.getBankAddress());
		response.setBankBranch(request.getBankBranch());
		response.setBankName(request.getBankName());
		response.setConfirmAccountNumber(request.getConfirmAccountNumber());
		response.setCountryCode(request.getCountryCode());
		response.setEmailId(request.getEmailId());
		response.setIfscCode(request.getIfscCode());
		response.setIfscCodeAvailable(request.getIfscCodeAvailable());
		response.setLandlineNumber(request.getLandlineNumber());
		response.setStdCode(request.getStdCode());
		response.setMphId(request.getMphId());
		response.setCountryId(request.getCountryId());
		response.setStateId(request.getStateId());
		response.setDistrictId(request.getDistrictId());
		response.setCityId(request.getCityId());
		response.setTownLocality(request.getTownLocality());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	Entity to Dto
	public MphBankDto mphBankSetEntityToDto(MphBankEntity request) {
		MphBankDto response = new MphBankDto();

		response.setMphBankId(request.getMphBankId());
		response.setIsDefault(request.getIsDefault());
		response.setAccountNumber(request.getAccountNumber());
		response.setAccountType(request.getAccountType());
		response.setBankAddress(request.getBankAddress());
		response.setBankBranch(request.getBankBranch());
		response.setBankName(request.getBankName());
		response.setConfirmAccountNumber(request.getConfirmAccountNumber());
		response.setCountryCode(request.getCountryCode());
		response.setEmailId(request.getEmailId());
		response.setIfscCode(request.getIfscCode());
		response.setIfscCodeAvailable(request.getIfscCodeAvailable());
		response.setLandlineNumber(request.getLandlineNumber());
		response.setStdCode(request.getStdCode());
		response.setMphId(request.getMphId());
		response.setCountryId(request.getCountryId());
		response.setStateId(request.getStateId());
		response.setDistrictId(request.getDistrictId());
		response.setCityId(request.getCityId());
		response.setTownLocality(request.getTownLocality());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	TempEntity to Entity
	public MphBankEntity mphBankSetTempEntityToEntity(MphBankTempEntity request) {
		MphBankEntity response = new MphBankEntity();

		response.setMphBankId(request.getMphBankId());
		response.setIsDefault(request.getIsDefault());
		response.setAccountNumber(request.getAccountNumber());
		response.setAccountType(request.getAccountType());
		response.setBankAddress(request.getBankAddress());
		response.setBankBranch(request.getBankBranch());
		response.setBankName(request.getBankName());
		response.setConfirmAccountNumber(request.getConfirmAccountNumber());
		response.setCountryCode(request.getCountryCode());
		response.setEmailId(request.getEmailId());
		response.setIfscCode(request.getIfscCode());
		response.setIfscCodeAvailable(request.getIfscCodeAvailable());
		response.setLandlineNumber(request.getLandlineNumber());
		response.setStdCode(request.getStdCode());
		response.setMphId(request.getMphId());
		response.setCountryId(request.getCountryId());
		response.setStateId(request.getStateId());
		response.setDistrictId(request.getDistrictId());
		response.setCityId(request.getCityId());
		response.setTownLocality(request.getTownLocality());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	Entity to TempEntity
	public MphBankTempEntity mphBankSetEntityToTempEntity(MphBankEntity request) {
		MphBankTempEntity response = new MphBankTempEntity();

		response.setMphBankId(request.getMphBankId());
		response.setIsDefault(request.getIsDefault());
		response.setAccountNumber(request.getAccountNumber());
		response.setAccountType(request.getAccountType());
		response.setBankAddress(request.getBankAddress());
		response.setBankBranch(request.getBankBranch());
		response.setBankName(request.getBankName());
		response.setConfirmAccountNumber(request.getConfirmAccountNumber());
		response.setCountryCode(request.getCountryCode());
		response.setEmailId(request.getEmailId());
		response.setIfscCode(request.getIfscCode());
		response.setIfscCodeAvailable(request.getIfscCodeAvailable());
		response.setLandlineNumber(request.getLandlineNumber());
		response.setStdCode(request.getStdCode());
		response.setMphId(request.getMphId());
		response.setCountryId(request.getCountryId());
		response.setStateId(request.getStateId());
		response.setDistrictId(request.getDistrictId());
		response.setCityId(request.getCityId());
		response.setTownLocality(request.getTownLocality());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	MPH Representatives	
//	DTO to TempEntity
	public MphRepresentativesTempEntity mphRepresentativesTSetDtoToTempEntity(MphRepresentativesDto request) {
		MphRepresentativesTempEntity response = new MphRepresentativesTempEntity();

		response.setRepId(response.getRepId());
		response.setMphId(response.getMphId());
		response.setEmailId(response.getEmailId());
		response.setRepresentativeCode(response.getRepresentativeCode());
		response.setRepresentativeName(response.getRepresentativeName());
		response.setLandlineNo(response.getLandlineNo());
		response.setMobileNo(response.getMobileNo());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryCode(response.getCountryCode());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	DTO to Entity
	public MphRepresentativesEntity mphRepresentativesTSetDtoToEntity(MphRepresentativesDto request) {
		MphRepresentativesEntity response = new MphRepresentativesEntity();

		response.setRepId(response.getRepId());
		response.setMphId(response.getMphId());
		response.setEmailId(response.getEmailId());
		response.setRepresentativeCode(response.getRepresentativeCode());
		response.setRepresentativeName(response.getRepresentativeName());
		response.setLandlineNo(response.getLandlineNo());
		response.setMobileNo(response.getMobileNo());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryCode(response.getCountryCode());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	TempEntity to Dto
	public MphRepresentativesDto mphRepresentativesSetTempEntityToDto(MphRepresentativesTempEntity request) {
		MphRepresentativesDto response = new MphRepresentativesDto();

		response.setRepId(response.getRepId());
		response.setMphId(response.getMphId());
		response.setEmailId(response.getEmailId());
		response.setRepresentativeCode(response.getRepresentativeCode());
		response.setRepresentativeName(response.getRepresentativeName());
		response.setLandlineNo(response.getLandlineNo());
		response.setMobileNo(response.getMobileNo());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryCode(response.getCountryCode());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	Entity to Dto
	public MphRepresentativesDto mphRepresentativesSetEntityToDto(MphRepresentativesEntity request) {
		MphRepresentativesDto response = new MphRepresentativesDto();

		response.setRepId(response.getRepId());
		response.setMphId(response.getMphId());
		response.setEmailId(response.getEmailId());
		response.setRepresentativeCode(response.getRepresentativeCode());
		response.setRepresentativeName(response.getRepresentativeName());
		response.setLandlineNo(response.getLandlineNo());
		response.setMobileNo(response.getMobileNo());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryCode(response.getCountryCode());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	TempEntity to Entity
	public MphRepresentativesEntity mphRepresentativesSetTempEntityToEntity(MphRepresentativesTempEntity request) {
		MphRepresentativesEntity response = new MphRepresentativesEntity();

		response.setRepId(response.getRepId());
		response.setMphId(response.getMphId());
		response.setEmailId(response.getEmailId());
		response.setRepresentativeCode(response.getRepresentativeCode());
		response.setRepresentativeName(response.getRepresentativeName());
		response.setLandlineNo(response.getLandlineNo());
		response.setMobileNo(response.getMobileNo());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryCode(response.getCountryCode());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	Entity to TempEntity
	public MphRepresentativesTempEntity mphRepresentativesSetEntityToTempEntity(MphRepresentativesEntity request) {
		MphRepresentativesTempEntity response = new MphRepresentativesTempEntity();

		response.setRepId(response.getRepId());
		response.setMphId(response.getMphId());
		response.setEmailId(response.getEmailId());
		response.setRepresentativeCode(response.getRepresentativeCode());
		response.setRepresentativeName(response.getRepresentativeName());
		response.setLandlineNo(response.getLandlineNo());
		response.setMobileNo(response.getMobileNo());
		response.setAddressType(response.getAddressType());
		response.setAddressLine1(response.getAddressLine1());
		response.setAddressLine2(response.getAddressLine2());
		response.setAddressLine3(response.getAddressLine3());
		response.setCityLocality(response.getCityLocality());
		response.setDistrict(response.getDistrict());
		response.setStateName(response.getStateName());
		response.setPincode(response.getPincode());
		response.setCountryCode(response.getCountryCode());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

	/**
	 * Set<PolicyDepositTempEntity> deposits = new HashSet<>();
	 * Set<PolicyValuationTempEntity> valuations = new HashSet<>();
	 * Set<PolicyRulesTempEntity> rules = new HashSet<>();
	 * Set<PolicyContributionTempEntity> policyContributions = new HashSet<>();
	 * Set<PolicyContributionSummaryTempEntity> policyContributionSummary = new
	 * HashSet<>(); Set<PolicyTransactionEntriesTempEntity> policyTransactionEntries
	 * = new HashSet<>(); Set<PolicyTransactionSummaryTempEntity>
	 * policyTransactionSummary = new HashSet<>(); Set<ZeroAccountTempEntity>
	 * zeroAccount = new HashSet<>(); Set<ZeroAccountEntriesTempEntity>
	 * zeroAccountEntries = new HashSet<>(); Set<PolicyNotesTempEntity> notes = new
	 * HashSet<>(); Set<MemberMasterTempEntity> memberMaster = new HashSet<>();
	 */

//  POLICY MASTER	
//	DTO to TempEntity
	public PolicyMasterTempEntity policyMasterSetDtoToTempEntity(PolicyMasterDto request) {
		PolicyMasterTempEntity response = new PolicyMasterTempEntity();

		response.setPolicyId(request.getPolicyId());
		response.setMasterPolicyId(request.getMasterPolicyId());
		response.setMphId(request.getMphId());
		response.setProposalId(request.getProposalId());
		response.setQuotationId(request.getQuotationId());
		response.setLeadId(request.getLeadId());
		response.setPolicyNumber(request.getPolicyNumber());
		response.setPolicyStatus(request.getPolicyStatus());
		response.setPolicyType(request.getPolicyType());
		response.setUnitId(request.getUnitId());
		response.setUnitOffice(request.getUnitOffice());
		response.setStampDuty(request.getStampDuty());
		response.setNoOfCategory(request.getNoOfCategory());
		response.setContributionFrequency(request.getContributionFrequency());
		response.setIntermediaryOfficerCode(request.getIntermediaryOfficerCode());
		response.setIntermediaryOfficerName(request.getIntermediaryOfficerName());
		response.setMarketingOfficerCode(request.getMarketingOfficerCode());
		response.setMarketingOfficerName(request.getMarketingOfficerName());
		response.setLineOfBusiness(request.getLineOfBusiness());
		response.setProductId(request.getProductId());
		response.setVariant(request.getVariant());
		response.setTotalMember(request.getTotalMember());
		response.setConType(request.getConType());
		response.setAdvanceotarrears(request.getAdvanceotarrears());
		response.setPolicyDispatchDate(request.getPolicyDispatchDate());
		response.setPolicyRecievedDate(request.getPolicyRecievedDate());
		response.setArd(request.getArd());
		response.setPolicyCommencementDt(request.getPolicyCommencementDt());
		response.setIsCommencementdateOneYr(request.getIsCommencementdateOneYr());
		response.setAdjustmentDt(request.getAdjustmentDt());
		response.setAmountToBeAdjusted(request.getAmountToBeAdjusted());
		response.setFirstPremium(request.getFirstPremium());
		response.setSinglePremiumFirstYr(request.getSinglePremiumFirstYr());
		response.setRenewalPremium(request.getRenewalPremium());
		response.setSubsequentSinglePremium(request.getSubsequentSinglePremium());
		response.setWorkflowStatus(request.getWorkflowStatus());
		response.setRejectionReasonCode(request.getRejectionReasonCode());
		response.setRejectionRemarks(request.getRejectionRemarks());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	DTO to Entity	
	public PolicyMasterEntity policyMasterSetDtoToEntity(PolicyMasterDto request) {
		PolicyMasterEntity response = new PolicyMasterEntity();

		response.setPolicyId(request.getPolicyId());
		response.setTempPolicyId(request.getTempPolicyId());
		response.setMphId(request.getMphId());
		response.setProposalId(request.getProposalId());
		response.setQuotationId(request.getQuotationId());
		response.setLeadId(request.getLeadId());
		response.setPolicyNumber(request.getPolicyNumber());
		response.setPolicyStatus(request.getPolicyStatus());
		response.setPolicyType(request.getPolicyType());
		response.setUnitId(request.getUnitId());
		response.setUnitOffice(request.getUnitOffice());
		response.setStampDuty(request.getStampDuty());
		response.setNoOfCategory(request.getNoOfCategory());
		response.setContributionFrequency(request.getContributionFrequency());
		response.setIntermediaryOfficerCode(request.getIntermediaryOfficerCode());
		response.setIntermediaryOfficerName(request.getIntermediaryOfficerName());
		response.setMarketingOfficerCode(request.getMarketingOfficerCode());
		response.setMarketingOfficerName(request.getMarketingOfficerName());
		response.setLineOfBusiness(request.getLineOfBusiness());
		response.setProductId(request.getProductId());
		response.setVariant(request.getVariant());
		response.setTotalMember(request.getTotalMember());
		response.setConType(request.getConType());
		response.setAdvanceotarrears(request.getAdvanceotarrears());
		response.setPolicyDispatchDate(request.getPolicyDispatchDate());
		response.setPolicyRecievedDate(request.getPolicyRecievedDate());
		response.setArd(request.getArd());
		response.setPolicyCommencementDt(request.getPolicyCommencementDt());
		response.setIsCommencementdateOneYr(request.getIsCommencementdateOneYr());
		response.setAdjustmentDt(request.getAdjustmentDt());
		response.setAmountToBeAdjusted(request.getAmountToBeAdjusted());
		response.setFirstPremium(request.getFirstPremium());
		response.setSinglePremiumFirstYr(request.getSinglePremiumFirstYr());
		response.setRenewalPremium(request.getRenewalPremium());
		response.setSubsequentSinglePremium(request.getSubsequentSinglePremium());
		response.setWorkflowStatus(request.getWorkflowStatus());
		response.setRejectionReasonCode(request.getRejectionReasonCode());
		response.setRejectionRemarks(request.getRejectionRemarks());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	TempEntity to Dto	
	public PolicyMasterDto policyMasterSetTempEntityToDto(PolicyMasterTempEntity request) {
		PolicyMasterDto response = new PolicyMasterDto();

		response.setPolicyId(request.getPolicyId());
		response.setMasterPolicyId(request.getMasterPolicyId());
		response.setMphId(request.getMphId());
		response.setProposalId(request.getProposalId());
		response.setQuotationId(request.getQuotationId());
		response.setLeadId(request.getLeadId());
		response.setPolicyNumber(request.getPolicyNumber());
		response.setPolicyStatus(request.getPolicyStatus());
		response.setPolicyType(request.getPolicyType());
		response.setUnitId(request.getUnitId());
		response.setUnitOffice(request.getUnitOffice());
		response.setStampDuty(request.getStampDuty());
		response.setNoOfCategory(request.getNoOfCategory());
		response.setContributionFrequency(request.getContributionFrequency());
		response.setIntermediaryOfficerCode(request.getIntermediaryOfficerCode());
		response.setIntermediaryOfficerName(request.getIntermediaryOfficerName());
		response.setMarketingOfficerCode(request.getMarketingOfficerCode());
		response.setMarketingOfficerName(request.getMarketingOfficerName());
		response.setLineOfBusiness(request.getLineOfBusiness());
		response.setProductId(request.getProductId());
		response.setVariant(request.getVariant());
		response.setTotalMember(request.getTotalMember());
		response.setConType(request.getConType());
		response.setAdvanceotarrears(request.getAdvanceotarrears());
		response.setPolicyDispatchDate(request.getPolicyDispatchDate());
		response.setPolicyRecievedDate(request.getPolicyRecievedDate());
		response.setArd(request.getArd());
		response.setPolicyCommencementDt(request.getPolicyCommencementDt());
		response.setIsCommencementdateOneYr(request.getIsCommencementdateOneYr());
		response.setAdjustmentDt(request.getAdjustmentDt());
		response.setAmountToBeAdjusted(request.getAmountToBeAdjusted());
		response.setFirstPremium(request.getFirstPremium());
		response.setSinglePremiumFirstYr(request.getSinglePremiumFirstYr());
		response.setRenewalPremium(request.getRenewalPremium());
		response.setSubsequentSinglePremium(request.getSubsequentSinglePremium());
		response.setWorkflowStatus(request.getWorkflowStatus());
		response.setRejectionReasonCode(request.getRejectionReasonCode());
		response.setRejectionRemarks(request.getRejectionRemarks());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	Entity to Dto	
	public PolicyMasterDto policyMasterSetEntityToDto(PolicyMasterEntity request) {
		PolicyMasterDto response = new PolicyMasterDto();

		response.setPolicyId(request.getPolicyId());
		response.setTempPolicyId(request.getTempPolicyId());
		response.setMphId(request.getMphId());
		response.setProposalId(request.getProposalId());
		response.setQuotationId(request.getQuotationId());
		response.setLeadId(request.getLeadId());
		response.setPolicyNumber(request.getPolicyNumber());
		response.setPolicyStatus(request.getPolicyStatus());
		response.setPolicyType(request.getPolicyType());
		response.setUnitId(request.getUnitId());
		response.setUnitOffice(request.getUnitOffice());
		response.setStampDuty(request.getStampDuty());
		response.setNoOfCategory(request.getNoOfCategory());
		response.setContributionFrequency(request.getContributionFrequency());
		response.setIntermediaryOfficerCode(request.getIntermediaryOfficerCode());
		response.setIntermediaryOfficerName(request.getIntermediaryOfficerName());
		response.setMarketingOfficerCode(request.getMarketingOfficerCode());
		response.setMarketingOfficerName(request.getMarketingOfficerName());
		response.setLineOfBusiness(request.getLineOfBusiness());
		response.setProductId(request.getProductId());
		response.setVariant(request.getVariant());
		response.setTotalMember(request.getTotalMember());
		response.setConType(request.getConType());
		response.setAdvanceotarrears(request.getAdvanceotarrears());
		response.setPolicyDispatchDate(request.getPolicyDispatchDate());
		response.setPolicyRecievedDate(request.getPolicyRecievedDate());
		response.setArd(request.getArd());
		response.setPolicyCommencementDt(request.getPolicyCommencementDt());
		response.setIsCommencementdateOneYr(request.getIsCommencementdateOneYr());
		response.setAdjustmentDt(request.getAdjustmentDt());
		response.setAmountToBeAdjusted(request.getAmountToBeAdjusted());
		response.setFirstPremium(request.getFirstPremium());
		response.setSinglePremiumFirstYr(request.getSinglePremiumFirstYr());
		response.setRenewalPremium(request.getRenewalPremium());
		response.setSubsequentSinglePremium(request.getSubsequentSinglePremium());
		response.setWorkflowStatus(request.getWorkflowStatus());
		response.setRejectionReasonCode(request.getRejectionReasonCode());
		response.setRejectionRemarks(request.getRejectionRemarks());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	TempEntity to Entity
	public PolicyMasterEntity policyMasterSetTempEntityToEntity(PolicyMasterTempEntity request) {
		PolicyMasterEntity response = new PolicyMasterEntity();

		response.setPolicyId(request.getMasterPolicyId());
		response.setTempPolicyId(request.getPolicyId());
		response.setMphId(request.getMphId());
		response.setProposalId(request.getProposalId());
		response.setQuotationId(request.getQuotationId());
		response.setLeadId(request.getLeadId());
		response.setPolicyNumber(request.getPolicyNumber());
		response.setPolicyStatus(request.getPolicyStatus());
		response.setPolicyType(request.getPolicyType());
		response.setUnitId(request.getUnitId());
		response.setUnitOffice(request.getUnitOffice());
		response.setStampDuty(request.getStampDuty());
		response.setNoOfCategory(request.getNoOfCategory());
		response.setContributionFrequency(request.getContributionFrequency());
		response.setIntermediaryOfficerCode(request.getIntermediaryOfficerCode());
		response.setIntermediaryOfficerName(request.getIntermediaryOfficerName());
		response.setMarketingOfficerCode(request.getMarketingOfficerCode());
		response.setMarketingOfficerName(request.getMarketingOfficerName());
		response.setLineOfBusiness(request.getLineOfBusiness());
		response.setProductId(request.getProductId());
		response.setVariant(request.getVariant());
		response.setTotalMember(request.getTotalMember());
		response.setConType(request.getConType());
		response.setAdvanceotarrears(request.getAdvanceotarrears());
		response.setPolicyDispatchDate(request.getPolicyDispatchDate());
		response.setPolicyRecievedDate(request.getPolicyRecievedDate());
		response.setArd(request.getArd());
		response.setPolicyCommencementDt(request.getPolicyCommencementDt());
		response.setIsCommencementdateOneYr(request.getIsCommencementdateOneYr());
		response.setAdjustmentDt(request.getAdjustmentDt());
		response.setAmountToBeAdjusted(request.getAmountToBeAdjusted());
		response.setFirstPremium(request.getFirstPremium());
		response.setSinglePremiumFirstYr(request.getSinglePremiumFirstYr());
		response.setRenewalPremium(request.getRenewalPremium());
		response.setSubsequentSinglePremium(request.getSubsequentSinglePremium());
		response.setWorkflowStatus(request.getWorkflowStatus());
		response.setRejectionReasonCode(request.getRejectionReasonCode());
		response.setRejectionRemarks(request.getRejectionRemarks());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	Entity to TempEntity
	public PolicyMasterTempEntity policyMasterSetEntityToTempEntity(PolicyMasterTempEntity request) {
		PolicyMasterTempEntity response = new PolicyMasterTempEntity();

		response.setPolicyId(request.getPolicyId());
		response.setMasterPolicyId(request.getMasterPolicyId());
		response.setMphId(request.getMphId());
		response.setProposalId(request.getProposalId());
		response.setQuotationId(request.getQuotationId());
		response.setLeadId(request.getLeadId());
		response.setPolicyNumber(request.getPolicyNumber());
		response.setPolicyStatus(request.getPolicyStatus());
		response.setPolicyType(request.getPolicyType());
		response.setUnitId(request.getUnitId());
		response.setUnitOffice(request.getUnitOffice());
		response.setStampDuty(request.getStampDuty());
		response.setNoOfCategory(request.getNoOfCategory());
		response.setContributionFrequency(request.getContributionFrequency());
		response.setIntermediaryOfficerCode(request.getIntermediaryOfficerCode());
		response.setIntermediaryOfficerName(request.getIntermediaryOfficerName());
		response.setMarketingOfficerCode(request.getMarketingOfficerCode());
		response.setMarketingOfficerName(request.getMarketingOfficerName());
		response.setLineOfBusiness(request.getLineOfBusiness());
		response.setProductId(request.getProductId());
		response.setVariant(request.getVariant());
		response.setTotalMember(request.getTotalMember());
		response.setConType(request.getConType());
		response.setAdvanceotarrears(request.getAdvanceotarrears());
		response.setPolicyDispatchDate(request.getPolicyDispatchDate());
		response.setPolicyRecievedDate(request.getPolicyRecievedDate());
		response.setArd(request.getArd());
		response.setPolicyCommencementDt(request.getPolicyCommencementDt());
		response.setIsCommencementdateOneYr(request.getIsCommencementdateOneYr());
		response.setAdjustmentDt(request.getAdjustmentDt());
		response.setAmountToBeAdjusted(request.getAmountToBeAdjusted());
		response.setFirstPremium(request.getFirstPremium());
		response.setSinglePremiumFirstYr(request.getSinglePremiumFirstYr());
		response.setRenewalPremium(request.getRenewalPremium());
		response.setSubsequentSinglePremium(request.getSubsequentSinglePremium());
		response.setWorkflowStatus(request.getWorkflowStatus());
		response.setRejectionReasonCode(request.getRejectionReasonCode());
		response.setRejectionRemarks(request.getRejectionRemarks());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//  POLICY Deposit	
//	DTO to TempEntity
	public PolicyDepositTempEntity policyDepositSetDtoToTempEntity(PolicyDepositDto request) {
		PolicyDepositTempEntity response = new PolicyDepositTempEntity();

		response.setDepositId(request.getDepositId());
		response.setPolicyId(request.getPolicyId());
		response.setMasterPolicyId(request.getMasterPolicyId());
		response.setAdjustmentContributionId(request.getAdjustmentContributionId());
		response.setRegularContributionId(request.getRegularContributionId());
		response.setContributionType(request.getContributionType());
		response.setCollectionNo(request.getCollectionNo());
		response.setCollectionDate(request.getCollectionDate());
		response.setCollectionStatus(request.getCollectionStatus());
		response.setChallanNo(request.getChallanNo());
		response.setDepositAmount(request.getDepositAmount());
		response.setAdjustmentAmount(request.getAdjustmentAmount());
		response.setAvailableAmount(request.getAvailableAmount());
		response.setTransactionMode(request.getTransactionMode());
		response.setChequeRealisationDate(request.getChequeRealisationDate());
		response.setRemark(request.getRemark());
		response.setStatus(request.getStatus());
		response.setZeroId(request.getZeroId());
		response.setIsDeposit(request.getIsDeposit());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	DTO to Entity	
	public PolicyDepositEntity policyDepositSetDtoToEntity(PolicyDepositDto request) {
		PolicyDepositEntity response = new PolicyDepositEntity();
		response.setDepositId(request.getDepositId());
		response.setPolicyId(request.getPolicyId());
		response.setTempPolicyId(request.getTempPolicyId());
		response.setAdjustmentContributionId(request.getAdjustmentContributionId());
		response.setRegularContributionId(request.getRegularContributionId());
		response.setContributionType(request.getContributionType());
		response.setCollectionNo(request.getCollectionNo());
		response.setCollectionDate(request.getCollectionDate());
		response.setCollectionStatus(request.getCollectionStatus());
		response.setChallanNo(request.getChallanNo());
		response.setDepositAmount(request.getDepositAmount());
		response.setAdjustmentAmount(request.getAdjustmentAmount());
		response.setAvailableAmount(request.getAvailableAmount());
		response.setTransactionMode(request.getTransactionMode());
		response.setChequeRealisationDate(request.getChequeRealisationDate());
		response.setRemark(request.getRemark());
		response.setStatus(request.getStatus());
		response.setZeroId(request.getZeroId());
		response.setIsDeposit(request.getIsDeposit());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		return response;
	}

//	TempEntity to Dto	
	public PolicyDepositDto policyDepositSetTempEntityToDto(PolicyDepositTempEntity request) {
		PolicyDepositDto response = new PolicyDepositDto();
		response.setDepositId(request.getDepositId());
		response.setPolicyId(request.getPolicyId());
		response.setMasterPolicyId(request.getMasterPolicyId());
		response.setAdjustmentContributionId(request.getAdjustmentContributionId());
		response.setRegularContributionId(request.getRegularContributionId());
		response.setContributionType(request.getContributionType());
		response.setCollectionNo(request.getCollectionNo());
		response.setCollectionDate(request.getCollectionDate());
		response.setCollectionStatus(request.getCollectionStatus());
		response.setChallanNo(request.getChallanNo());
		response.setDepositAmount(request.getDepositAmount());
		response.setAdjustmentAmount(request.getAdjustmentAmount());
		response.setAvailableAmount(request.getAvailableAmount());
		response.setTransactionMode(request.getTransactionMode());
		response.setChequeRealisationDate(request.getChequeRealisationDate());
		response.setRemark(request.getRemark());
		response.setStatus(request.getStatus());
		response.setZeroId(request.getZeroId());
		response.setIsDeposit(request.getIsDeposit());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		return response;
	}

//	Entity to Dto	
	public PolicyDepositDto policyDepositSetEntityToDto(PolicyDepositEntity request) {
		PolicyDepositDto response = new PolicyDepositDto();
		response.setDepositId(request.getDepositId());
		response.setPolicyId(request.getPolicyId());
		response.setTempPolicyId(request.getTempPolicyId());
		response.setAdjustmentContributionId(request.getAdjustmentContributionId());
		response.setRegularContributionId(request.getRegularContributionId());
		response.setContributionType(request.getContributionType());
		response.setCollectionNo(request.getCollectionNo());
		response.setCollectionDate(request.getCollectionDate());
		response.setCollectionStatus(request.getCollectionStatus());
		response.setChallanNo(request.getChallanNo());
		response.setDepositAmount(request.getDepositAmount());
		response.setAdjustmentAmount(request.getAdjustmentAmount());
		response.setAvailableAmount(request.getAvailableAmount());
		response.setTransactionMode(request.getTransactionMode());
		response.setChequeRealisationDate(request.getChequeRealisationDate());
		response.setRemark(request.getRemark());
		response.setStatus(request.getStatus());
		response.setZeroId(request.getZeroId());
		response.setIsDeposit(request.getIsDeposit());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		return response;
	}

//	TempEntity to Entity
	public PolicyDepositEntity policyDepositSetTempEntityToEntity(PolicyDepositTempEntity request) {
		PolicyDepositEntity response = new PolicyDepositEntity();
		response.setDepositId(request.getDepositId());
		response.setPolicyId(request.getMasterPolicyId());
		response.setTempPolicyId(request.getPolicyId());
		response.setAdjustmentContributionId(request.getAdjustmentContributionId());
		response.setRegularContributionId(request.getRegularContributionId());
		response.setContributionType(request.getContributionType());
		response.setCollectionNo(request.getCollectionNo());
		response.setCollectionDate(request.getCollectionDate());
		response.setCollectionStatus(request.getCollectionStatus());
		response.setChallanNo(request.getChallanNo());
		response.setDepositAmount(request.getDepositAmount());
		response.setAdjustmentAmount(request.getAdjustmentAmount());
		response.setAvailableAmount(request.getAvailableAmount());
		response.setTransactionMode(request.getTransactionMode());
		response.setChequeRealisationDate(request.getChequeRealisationDate());
		response.setRemark(request.getRemark());
		response.setStatus(request.getStatus());
		response.setZeroId(request.getZeroId());
		response.setIsDeposit(request.getIsDeposit());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		return response;
	}

//	Entity to TempEntity
	public PolicyDepositTempEntity policyDepositSetEntityToTempEntity(PolicyDepositTempEntity request) {
		PolicyDepositTempEntity response = new PolicyDepositTempEntity();
		response.setDepositId(request.getDepositId());
		response.setPolicyId(request.getPolicyId());
		response.setMasterPolicyId(request.getMasterPolicyId());
		response.setAdjustmentContributionId(request.getAdjustmentContributionId());
		response.setRegularContributionId(request.getRegularContributionId());
		response.setContributionType(request.getContributionType());
		response.setCollectionNo(request.getCollectionNo());
		response.setCollectionDate(request.getCollectionDate());
		response.setCollectionStatus(request.getCollectionStatus());
		response.setChallanNo(request.getChallanNo());
		response.setDepositAmount(request.getDepositAmount());
		response.setAdjustmentAmount(request.getAdjustmentAmount());
		response.setAvailableAmount(request.getAvailableAmount());
		response.setTransactionMode(request.getTransactionMode());
		response.setChequeRealisationDate(request.getChequeRealisationDate());
		response.setRemark(request.getRemark());
		response.setStatus(request.getStatus());
		response.setZeroId(request.getZeroId());
		response.setIsDeposit(request.getIsDeposit());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		return response;
	}

//  POLICY Valuation
//	DTO to TempEntity
	public PolicyValuationTempEntity policyValuationSetDtoToTempEntity(PolicyValuationDto request) {
		PolicyValuationTempEntity response = new PolicyValuationTempEntity();

		response.setValuationId(request.getValuationId());
		response.setPolicyId(request.getPolicyId());
		response.setValuationType(request.getValuationType());
		response.setAttritionRate(request.getAttritionRate());
		response.setSalaryEscalation(request.getSalaryEscalation());
		response.setDeathRate(request.getDeathRate());
		response.setDisRateIntrest(request.getDisRateIntrest());
		response.setDisRateSalaryEsc(request.getDisRateSalaryEsc());
		response.setAnnuityOptId(request.getAnnuityOptId());
		response.setAnnuityOptionCode(request.getAnnuityOptionCode());
		response.setAnnuityOption(request.getAnnuityOption());
		response.setAccuralRateFactor(request.getAccuralRateFactor());
		response.setMinPension(request.getMinPension());
		response.setMaxPension(request.getMaxPension());
		response.setWithdrawalRate(request.getWithdrawalRate());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		return response;
	}

//	DTO to Entity	
	public PolicyValuationEntity policyValuationSetDtoToEntity(PolicyValuationDto request) {
		PolicyValuationEntity response = new PolicyValuationEntity();
		response.setValuationId(request.getValuationId());
		response.setPolicyId(request.getPolicyId());
		response.setValuationType(request.getValuationType());
		response.setAttritionRate(request.getAttritionRate());
		response.setSalaryEscalation(request.getSalaryEscalation());
		response.setDeathRate(request.getDeathRate());
		response.setDisRateIntrest(request.getDisRateIntrest());
		response.setDisRateSalaryEsc(request.getDisRateSalaryEsc());
		response.setAnnuityOptId(request.getAnnuityOptId());
		response.setAnnuityOptionCode(request.getAnnuityOptionCode());
		response.setAnnuityOption(request.getAnnuityOption());
		response.setAccuralRateFactor(request.getAccuralRateFactor());
		response.setMinPension(request.getMinPension());
		response.setMaxPension(request.getMaxPension());
		response.setWithdrawalRate(request.getWithdrawalRate());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		return response;
	}

//	TempEntity to Dto	
	public PolicyValuationDto policyValuationSetTempEntityToDto(PolicyValuationTempEntity request) {
		PolicyValuationDto response = new PolicyValuationDto();
		response.setValuationId(request.getValuationId());
		response.setPolicyId(request.getPolicyId());
		response.setValuationType(request.getValuationType());
		response.setAttritionRate(request.getAttritionRate());
		response.setSalaryEscalation(request.getSalaryEscalation());
		response.setDeathRate(request.getDeathRate());
		response.setDisRateIntrest(request.getDisRateIntrest());
		response.setDisRateSalaryEsc(request.getDisRateSalaryEsc());
		response.setAnnuityOptId(request.getAnnuityOptId());
		response.setAnnuityOptionCode(request.getAnnuityOptionCode());
		response.setAnnuityOption(request.getAnnuityOption());
		response.setAccuralRateFactor(request.getAccuralRateFactor());
		response.setMinPension(request.getMinPension());
		response.setMaxPension(request.getMaxPension());
		response.setWithdrawalRate(request.getWithdrawalRate());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		return response;
	}

//	Entity to Dto	
	public PolicyValuationDto policyValuationSetEntityToDto(PolicyValuationEntity request) {
		PolicyValuationDto response = new PolicyValuationDto();
		response.setValuationId(request.getValuationId());
		response.setPolicyId(request.getPolicyId());
		response.setValuationType(request.getValuationType());
		response.setAttritionRate(request.getAttritionRate());
		response.setSalaryEscalation(request.getSalaryEscalation());
		response.setDeathRate(request.getDeathRate());
		response.setDisRateIntrest(request.getDisRateIntrest());
		response.setDisRateSalaryEsc(request.getDisRateSalaryEsc());
		response.setAnnuityOptId(request.getAnnuityOptId());
		response.setAnnuityOptionCode(request.getAnnuityOptionCode());
		response.setAnnuityOption(request.getAnnuityOption());
		response.setAccuralRateFactor(request.getAccuralRateFactor());
		response.setMinPension(request.getMinPension());
		response.setMaxPension(request.getMaxPension());
		response.setWithdrawalRate(request.getWithdrawalRate());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		return response;
	}

//	TempEntity to Entity
	public PolicyValuationEntity policyValuationSetTempEntityToEntity(PolicyValuationTempEntity request) {
		PolicyValuationEntity response = new PolicyValuationEntity();
		response.setValuationId(request.getValuationId());
		response.setPolicyId(request.getPolicyId());
		response.setValuationType(request.getValuationType());
		response.setAttritionRate(request.getAttritionRate());
		response.setSalaryEscalation(request.getSalaryEscalation());
		response.setDeathRate(request.getDeathRate());
		response.setDisRateIntrest(request.getDisRateIntrest());
		response.setDisRateSalaryEsc(request.getDisRateSalaryEsc());
		response.setAnnuityOptId(request.getAnnuityOptId());
		response.setAnnuityOptionCode(request.getAnnuityOptionCode());
		response.setAnnuityOption(request.getAnnuityOption());
		response.setAccuralRateFactor(request.getAccuralRateFactor());
		response.setMinPension(request.getMinPension());
		response.setMaxPension(request.getMaxPension());
		response.setWithdrawalRate(request.getWithdrawalRate());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		return response;
	}

//	Entity to TempEntity
	public PolicyValuationTempEntity policyValuationSetEntityToTempEntity(PolicyValuationTempEntity request) {
		PolicyValuationTempEntity response = new PolicyValuationTempEntity();
		response.setValuationId(request.getValuationId());
		response.setPolicyId(request.getPolicyId());
		response.setValuationType(request.getValuationType());
		response.setAttritionRate(request.getAttritionRate());
		response.setSalaryEscalation(request.getSalaryEscalation());
		response.setDeathRate(request.getDeathRate());
		response.setDisRateIntrest(request.getDisRateIntrest());
		response.setDisRateSalaryEsc(request.getDisRateSalaryEsc());
		response.setAnnuityOptId(request.getAnnuityOptId());
		response.setAnnuityOptionCode(request.getAnnuityOptionCode());
		response.setAnnuityOption(request.getAnnuityOption());
		response.setAccuralRateFactor(request.getAccuralRateFactor());
		response.setMinPension(request.getMinPension());
		response.setMaxPension(request.getMaxPension());
		response.setWithdrawalRate(request.getWithdrawalRate());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		return response;
	}

//  POLICY Rules
//	DTO to TempEntity
	public PolicyRulesTempEntity policyRulesSetDtoToTempEntity(PolicyRulesDto request) {
		PolicyRulesTempEntity response = new PolicyRulesTempEntity();

		return response;
	}

//	DTO to Entity	
	public PolicyRulesEntity policyRulesSetDtoToEntity(PolicyRulesDto request) {
		PolicyRulesEntity response = new PolicyRulesEntity();

		return response;
	}

//	TempEntity to Dto	
	public PolicyRulesDto policyRulesSetTempEntityToDto(PolicyRulesTempEntity request) {
		PolicyRulesDto response = new PolicyRulesDto();

		return response;
	}

//	Entity to Dto	
	public PolicyRulesDto policyRulesSetEntityToDto(PolicyRulesEntity request) {
		PolicyRulesDto response = new PolicyRulesDto();

		return response;
	}

//	TempEntity to Entity
	public PolicyRulesEntity policyRulesSetTempEntityToEntity(PolicyRulesTempEntity request) {
		PolicyRulesEntity response = new PolicyRulesEntity();

		return response;
	}

//	Entity to TempEntity
	public PolicyRulesTempEntity policyRulesSetEntityToTempEntity(PolicyRulesTempEntity request) {
		PolicyRulesTempEntity response = new PolicyRulesTempEntity();

		return response;
	}

//  POLICY Contribution
//	DTO to TempEntity
	public PolicyContributionTempEntity policyContributionSetDtoToTempEntity(PolicyContributionDto request) {
		PolicyContributionTempEntity response = new PolicyContributionTempEntity();

		return response;
	}

//	DTO to Entity	
	public PolicyContributionEntity policyContributionSetDtoToEntity(PolicyContributionDto request) {
		PolicyContributionEntity response = new PolicyContributionEntity();

		return response;
	}

//	TempEntity to Dto	
	public PolicyContributionDto policyContributionSetTempEntityToDto(PolicyContributionTempEntity request) {
		PolicyContributionDto response = new PolicyContributionDto();

		return response;
	}

//	Entity to Dto	
	public PolicyContributionDto policyContributionSetEntityToDto(PolicyContributionEntity request) {
		PolicyContributionDto response = new PolicyContributionDto();

		return response;
	}

//	TempEntity to Entity
	public PolicyContributionEntity policyContributionSetTempEntityToEntity(PolicyContributionTempEntity request) {
		PolicyContributionEntity response = new PolicyContributionEntity();

		return response;
	}

//	Entity to TempEntity
	public PolicyContributionTempEntity policyContributionSetEntityToTempEntity(PolicyContributionTempEntity request) {
		PolicyContributionTempEntity response = new PolicyContributionTempEntity();

		return response;
	}

//  POLICY ContributionSummary
//	DTO to TempEntity
	public PolicyContributionSummaryTempEntity policyContributionSummarySetDtoToTempEntity(
			PolicyContributionSummaryDto request) {
		PolicyContributionSummaryTempEntity response = new PolicyContributionSummaryTempEntity();

		return response;
	}

//	DTO to Entity	
	public PolicyContributionSummaryEntity policyContributionSummarySetDtoToEntity(
			PolicyContributionSummaryDto request) {
		PolicyContributionSummaryEntity response = new PolicyContributionSummaryEntity();

		return response;
	}

//	TempEntity to Dto	
	public PolicyContributionSummaryDto policyContributionSummarySetTempEntityToDto(
			PolicyContributionSummaryTempEntity request) {
		PolicyContributionSummaryDto response = new PolicyContributionSummaryDto();

		return response;
	}

//	Entity to Dto	
	public PolicyContributionSummaryDto policyContributionSummarySetEntityToDto(
			PolicyContributionSummaryEntity request) {
		PolicyContributionSummaryDto response = new PolicyContributionSummaryDto();

		return response;
	}

//	TempEntity to Entity
	public PolicyContributionSummaryEntity policyContributionSummarySetTempEntityToEntity(
			PolicyContributionSummaryTempEntity request) {
		PolicyContributionSummaryEntity response = new PolicyContributionSummaryEntity();

		return response;
	}

//	Entity to TempEntity
	public PolicyContributionSummaryTempEntity policyContributionSummarySetEntityToTempEntity(
			PolicyContributionSummaryTempEntity request) {
		PolicyContributionSummaryTempEntity response = new PolicyContributionSummaryTempEntity();

		return response;
	}

//  POLICY TransactionEntries
//	DTO to TempEntity
	public PolicyTransactionEntriesTempEntity policyTransactionEntriesSetDtoToTempEntity(
			PolicyTransactionEntriesDto request) {
		PolicyTransactionEntriesTempEntity response = new PolicyTransactionEntriesTempEntity();

		return response;
	}

//	DTO to Entity	
	public PolicyTransactionEntriesEntity policyTransactionEntriesSetDtoToEntity(PolicyTransactionEntriesDto request) {
		PolicyTransactionEntriesEntity response = new PolicyTransactionEntriesEntity();

		return response;
	}

//	TempEntity to Dto	
	public PolicyTransactionEntriesDto policyTransactionEntriesSetTempEntityToDto(
			PolicyTransactionEntriesTempEntity request) {
		PolicyTransactionEntriesDto response = new PolicyTransactionEntriesDto();

		return response;
	}

//	Entity to Dto	
	public PolicyTransactionEntriesDto policyTransactionEntriesSetEntityToDto(PolicyTransactionEntriesEntity request) {
		PolicyTransactionEntriesDto response = new PolicyTransactionEntriesDto();

		return response;
	}

//	TempEntity to Entity
	public PolicyTransactionEntriesEntity policyTransactionEntriesSetTempEntityToEntity(
			PolicyTransactionEntriesTempEntity request) {
		PolicyTransactionEntriesEntity response = new PolicyTransactionEntriesEntity();

		return response;
	}

//	Entity to TempEntity
	public PolicyTransactionEntriesTempEntity policyTransactionEntriesSetEntityToTempEntity(
			PolicyTransactionEntriesTempEntity request) {
		PolicyTransactionEntriesTempEntity response = new PolicyTransactionEntriesTempEntity();

		return response;
	}

//  POLICY TransactionSummary
//	DTO to TempEntity
	public PolicyTransactionSummaryTempEntity policyTransactionSummarySetDtoToTempEntity(
			PolicyTransactionSummaryDto request) {
		PolicyTransactionSummaryTempEntity response = new PolicyTransactionSummaryTempEntity();

		return response;
	}

//	DTO to Entity	
	public PolicyTransactionSummaryEntity policyTransactionSummarySetDtoToEntity(PolicyTransactionSummaryDto request) {
		PolicyTransactionSummaryEntity response = new PolicyTransactionSummaryEntity();

		return response;
	}

//	TempEntity to Dto	
	public PolicyTransactionSummaryDto policyTransactionSummarySetTempEntityToDto(
			PolicyTransactionSummaryTempEntity request) {
		PolicyTransactionSummaryDto response = new PolicyTransactionSummaryDto();

		return response;
	}

//	Entity to Dto	
	public PolicyTransactionSummaryDto policyTransactionSummarySetEntityToDto(PolicyTransactionSummaryEntity request) {
		PolicyTransactionSummaryDto response = new PolicyTransactionSummaryDto();

		return response;
	}

//	TempEntity to Entity
	public PolicyTransactionSummaryEntity policyTransactionSummarySetTempEntityToEntity(
			PolicyTransactionSummaryTempEntity request) {
		PolicyTransactionSummaryEntity response = new PolicyTransactionSummaryEntity();

		return response;
	}

//	Entity to TempEntity
	public PolicyTransactionSummaryTempEntity policyTransactionSummarySetEntityToTempEntity(
			PolicyTransactionSummaryTempEntity request) {
		PolicyTransactionSummaryTempEntity response = new PolicyTransactionSummaryTempEntity();

		return response;
	}

//  Zero Account	
//	DTO to TempEntity
	public ZeroAccountTempEntity zeroAccountSetDtoToTempEntity(ZeroAccountdto request) {
		ZeroAccountTempEntity response = new ZeroAccountTempEntity();

		return response;
	}

//	DTO to Entity	
	public ZeroAccountEntity zeroAccountSetDtoToEntity(ZeroAccountdto request) {
		ZeroAccountEntity response = new ZeroAccountEntity();

		return response;
	}

//	TempEntity to Dto	
	public ZeroAccountdto zeroAccountSetTempEntityToDto(ZeroAccountTempEntity request) {
		ZeroAccountdto response = new ZeroAccountdto();

		return response;
	}

//	Entity to Dto	
	public ZeroAccountdto zeroAccountSetEntityToDto(ZeroAccountEntity request) {
		ZeroAccountdto response = new ZeroAccountdto();

		return response;
	}

//	TempEntity to Entity
	public ZeroAccountEntity zeroAccountSetTempEntityToEntity(ZeroAccountTempEntity request) {
		ZeroAccountEntity response = new ZeroAccountEntity();

		return response;
	}

//	Entity to TempEntity
	public ZeroAccountTempEntity zeroAccountSetEntityToTempEntity(ZeroAccountTempEntity request) {
		ZeroAccountTempEntity response = new ZeroAccountTempEntity();

		return response;
	}

//  ZeroAccountEntries
//	DTO to TempEntity
	public ZeroAccountEntriesTempEntity zeroAccountEntriesSetDtoToTempEntity(ZeroAccountEntriesDto request) {
		ZeroAccountEntriesTempEntity response = new ZeroAccountEntriesTempEntity();

		return response;
	}

//	DTO to Entity	
	public ZeroAccountEntriesEntity zeroAccountEntriesSetDtoToEntity(ZeroAccountEntriesDto request) {
		ZeroAccountEntriesEntity response = new ZeroAccountEntriesEntity();

		return response;
	}

//	TempEntity to Dto	
	public ZeroAccountEntriesDto zeroAccountEntriesSetTempEntityToDto(ZeroAccountEntriesTempEntity request) {
		ZeroAccountEntriesDto response = new ZeroAccountEntriesDto();

		return response;
	}

//	Entity to Dto	
	public ZeroAccountEntriesDto zeroAccountEntriesSetEntityToDto(ZeroAccountEntriesEntity request) {
		ZeroAccountEntriesDto response = new ZeroAccountEntriesDto();

		return response;
	}

//	TempEntity to Entity
	public ZeroAccountEntriesEntity zeroAccountEntriesSetTempEntityToEntity(ZeroAccountEntriesTempEntity request) {
		ZeroAccountEntriesEntity response = new ZeroAccountEntriesEntity();

		return response;
	}

//	Entity to TempEntity
	public ZeroAccountEntriesTempEntity zeroAccountEntriesSetEntityToTempEntity(ZeroAccountEntriesTempEntity request) {
		ZeroAccountEntriesTempEntity response = new ZeroAccountEntriesTempEntity();

		return response;
	}

//  POLICY Notes
//	DTO to TempEntity
	public PolicyNotesTempEntity policyNotesSetDtoToTempEntity(PolicyNotesDto request) {
		PolicyNotesTempEntity response = new PolicyNotesTempEntity();

		return response;
	}

//	DTO to Entity	
	public PolicyNotesEntity policyNotesSetDtoToEntity(PolicyNotesDto request) {
		PolicyNotesEntity response = new PolicyNotesEntity();

		return response;
	}

//	TempEntity to Dto	
	public PolicyNotesDto policyNotesSetTempEntityToDto(PolicyNotesTempEntity request) {
		PolicyNotesDto response = new PolicyNotesDto();

		return response;
	}

//	Entity to Dto	
	public PolicyNotesDto policyNotesSetEntityToDto(PolicyNotesEntity request) {
		PolicyNotesDto response = new PolicyNotesDto();

		return response;
	}

//	TempEntity to Entity
	public PolicyNotesEntity policyNotesSetTempEntityToEntity(PolicyNotesTempEntity request) {
		PolicyNotesEntity response = new PolicyNotesEntity();

		return response;
	}

//	Entity to TempEntity
	public PolicyNotesTempEntity policyNotesSetEntityToTempEntity(PolicyNotesTempEntity request) {
		PolicyNotesTempEntity response = new PolicyNotesTempEntity();

		return response;
	}

//  MemberMaster	
//	DTO to TempEntity
	public MemberMasterTempEntity memberMasterSetDtoToTempEntity(MemberMasterDto request) {
		MemberMasterTempEntity response = new MemberMasterTempEntity();

		return response;
	}

//	DTO to Entity	
	public MemberMasterEntity memberMasterSetDtoToEntity(MemberMasterDto request) {
		MemberMasterEntity response = new MemberMasterEntity();

		return response;
	}

//	TempEntity to Dto	
	public MemberMasterDto memberMasterSetTempEntityToDto(MemberMasterTempEntity request) {
		MemberMasterDto response = new MemberMasterDto();

		return response;
	}

//	Entity to Dto	
	public MemberMasterDto memberMasterSetEntityToDto(MemberMasterEntity request) {
		MemberMasterDto response = new MemberMasterDto();

		return response;
	}

//	TempEntity to Entity
	public MemberMasterEntity memberMasterSetTempEntityToEntity(MemberMasterTempEntity request) {
		MemberMasterEntity response = new MemberMasterEntity();

		return response;
	}

//	Entity to TempEntity
	public MemberMasterTempEntity memberMasterSetEntityToTempEntity(MemberMasterTempEntity request) {
		MemberMasterTempEntity response = new MemberMasterTempEntity();

		return response;
	}

////OLD GETTER AND SETTER STARTS
// DTO TO TEMP ENTITY	
	public MphMasterTempEntity convertMphMasterDtoToMphMasterTempEntity(MphMasterDto masterDto) {
		MphMasterTempEntity response = new MphMasterTempEntity();

		response.setMphId(masterDto.getMphId());
		response.setMasterMphId(masterDto.getMasterMphId());
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
		Set<MphAddressTempEntity> mphAddress = new HashSet<>();
		for (MphAddressDto mphAddressDto : masterDto.getMphAddress()) {
			MphAddressTempEntity mphAddressTempEntity = new MphAddressTempEntity();
			mphAddressTempEntity.setAddressId(mphAddressDto.getAddressId());
			mphAddressTempEntity.setMphId(mphAddressDto.getMphId());
			mphAddressTempEntity.setAddressType(mphAddressDto.getAddressType());
			mphAddressTempEntity.setAddressLine1(mphAddressDto.getAddressLine1());
			mphAddressTempEntity.setAddressLine2(mphAddressDto.getAddressLine2());
			mphAddressTempEntity.setAddressLine3(mphAddressDto.getAddressLine3());
			mphAddressTempEntity.setPincode(mphAddressDto.getPincode());
			mphAddressTempEntity.setCityLocality(mphAddressDto.getCityLocality());
			mphAddressTempEntity.setCityId(mphAddressDto.getCityId());
			mphAddressTempEntity.setDistrict(mphAddressDto.getDistrict());
			mphAddressTempEntity.setDistrictId(mphAddressDto.getDistrictId());
			mphAddressTempEntity.setCountryId(mphAddressDto.getCountryId());
			mphAddressTempEntity.setStateId(mphAddressDto.getStateId());
			mphAddressTempEntity.setStateName(mphAddressDto.getStateName());
			mphAddressTempEntity.setIsActive(mphAddressDto.getIsActive());
			mphAddressTempEntity.setIsDefault(Boolean.TRUE);
			mphAddressTempEntity.setCreatedOn(mphAddressDto.getCreatedOn());
			mphAddressTempEntity.setCreatedBy(mphAddressDto.getCreatedBy());
			mphAddressTempEntity.setModifiedOn(mphAddressDto.getModifiedOn());
			mphAddressTempEntity.setModifiedBy(mphAddressDto.getModifiedBy());
			mphAddress.add(mphAddressTempEntity);
		}
		response.setMphAddress(mphAddress);
		Set<MphBankTempEntity> mphBank = new HashSet<>();
		for (MphBankDto mphBankDto : masterDto.getMphBank()) {
			MphBankTempEntity mphBankTempEntity = new MphBankTempEntity();
			mphBankTempEntity.setMphBankId(mphBankDto.getMphBankId());
			mphBankTempEntity.setMphId(mphBankDto.getMphId());
			mphBankTempEntity.setAccountNumber(mphBankDto.getAccountNumber());
			mphBankTempEntity.setConfirmAccountNumber(mphBankDto.getConfirmAccountNumber());
			mphBankTempEntity.setAccountType(mphBankDto.getAccountType());
			mphBankTempEntity.setIfscCodeAvailable(mphBankDto.getIfscCodeAvailable());
			mphBankTempEntity.setIfscCode(mphBankDto.getIfscCode());
			mphBankTempEntity.setBankName(mphBankDto.getBankName());
			mphBankTempEntity.setBankBranch(mphBankDto.getBankBranch());
			mphBankTempEntity.setBankAddress(mphBankDto.getBankAddress());
			mphBankTempEntity.setStdCode(mphBankDto.getStdCode());
			mphBankTempEntity.setLandlineNumber(mphBankDto.getLandlineNumber());
			mphBankTempEntity.setEmailId(mphBankDto.getEmailId());
			mphBankTempEntity.setCountryCode(mphBankDto.getCountryCode());
			mphBankTempEntity.setCountryId(mphBankDto.getCountryId());
			mphBankTempEntity.setStateId(mphBankDto.getStateId());
			mphBankTempEntity.setDistrictId(mphBankDto.getDistrictId());
			mphBankTempEntity.setCityId(mphBankDto.getCityId());
			mphBankTempEntity.setTownLocality(mphBankDto.getTownLocality());
			mphBankTempEntity.setIsActive(mphBankDto.getIsActive());
			mphBankTempEntity.setIsDefault(Boolean.TRUE);
			mphBankTempEntity.setCreatedBy(mphBankDto.getCreatedBy());
			mphBankTempEntity.setCreatedOn(mphBankDto.getCreatedOn());
			mphBankTempEntity.setModifiedOn(mphBankDto.getModifiedOn());
			mphBankTempEntity.setModifiedBy(mphBankDto.getModifiedBy());
			mphBank.add(mphBankTempEntity);
		}
		response.setMphBank(mphBank);
		Set<MphRepresentativesTempEntity> mphRepresentative = new HashSet<>();
		for (MphRepresentativesDto mphRepresentativeDto : masterDto.getMphRepresentative()) {
			MphRepresentativesTempEntity mphRepresentativesTempEntity = new MphRepresentativesTempEntity();
			mphRepresentativesTempEntity.setRepId(mphRepresentativeDto.getRepId());
			mphRepresentativesTempEntity.setMphId(mphRepresentativeDto.getMphId());
			mphRepresentativesTempEntity.setRepresentativeCode(mphRepresentativeDto.getRepresentativeCode());
			mphRepresentativesTempEntity.setRepresentativeName(mphRepresentativeDto.getRepresentativeName());
			mphRepresentativesTempEntity.setLandlineNo(mphRepresentativeDto.getLandlineNo());
			mphRepresentativesTempEntity.setCountryCode(mphRepresentativeDto.getCountryCode());
			mphRepresentativesTempEntity.setMobileNo(mphRepresentativeDto.getMobileNo());
			mphRepresentativesTempEntity.setEmailId(mphRepresentativeDto.getEmailId());
			mphRepresentativesTempEntity.setAddressType(mphRepresentativeDto.getAddressType());
			mphRepresentativesTempEntity.setAddressLine1(mphRepresentativeDto.getAddressLine1());
			mphRepresentativesTempEntity.setAddressLine2(mphRepresentativeDto.getAddressLine2());
			mphRepresentativesTempEntity.setAddressLine3(mphRepresentativeDto.getAddressLine3());
			mphRepresentativesTempEntity.setCityLocality(mphRepresentativeDto.getCityLocality());
			mphRepresentativesTempEntity.setDistrict(mphRepresentativeDto.getDistrict());
			mphRepresentativesTempEntity.setStateName(mphRepresentativeDto.getStateName());
			mphRepresentativesTempEntity.setPincode(mphRepresentativeDto.getPincode());
			mphRepresentativesTempEntity.setIsActive(mphRepresentativeDto.getIsActive());
			mphRepresentativesTempEntity.setCreatedOn(mphRepresentativeDto.getCreatedOn());
			mphRepresentativesTempEntity.setCreatedBy(mphRepresentativeDto.getCreatedBy());
			mphRepresentativesTempEntity.setModifiedOn(mphRepresentativeDto.getModifiedOn());
			mphRepresentativesTempEntity.setModifiedBy(mphRepresentativeDto.getModifiedBy());
			mphRepresentative.add(mphRepresentativesTempEntity);
		}
		response.setMphRepresentative(mphRepresentative);
		Set<PolicyMasterTempEntity> policyMaster = new HashSet<>();
		for (PolicyMasterDto policyDto : masterDto.getPolicyMaster()) {
			PolicyMasterTempEntity policyMasterTempEntity = convertPolicyMasterDtoToPolicyMasterTempEntity(policyDto);
			policyMaster.add(policyMasterTempEntity);
		}
		response.setPolicyMaster(policyMaster);
		return response;
	}

	public PolicyMasterTempEntity convertPolicyMasterDtoToPolicyMasterTempEntity(PolicyMasterDto policyMasterDto) {
		PolicyMasterTempEntity response = new PolicyMasterTempEntity();
		response.setPolicyId(policyMasterDto.getPolicyId());
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
		response.setStampDuty(policyMasterDto.getStampDuty());
		Set<PolicyValuationTempEntity> valuations = new HashSet<>();
		for (PolicyValuationDto policyValuationDto : policyMasterDto.getValuations()) {
			PolicyValuationTempEntity policyValuationTempEntity = new PolicyValuationTempEntity();
			policyValuationTempEntity.setValuationId(policyValuationDto.getValuationId());
			policyValuationTempEntity.setPolicyId(policyValuationDto.getPolicyId());
			policyValuationTempEntity.setValuationType(policyValuationDto.getValuationType());
			policyValuationTempEntity.setAttritionRate(policyValuationDto.getAttritionRate());
			policyValuationTempEntity.setSalaryEscalation(policyValuationDto.getSalaryEscalation());
			policyValuationTempEntity.setDeathRate(policyValuationDto.getDeathRate());
			policyValuationTempEntity.setDisRateIntrest(policyValuationDto.getDisRateIntrest());
			policyValuationTempEntity.setDisRateSalaryEsc(policyValuationDto.getDisRateSalaryEsc());
			policyValuationTempEntity.setAnnuityOption(policyValuationDto.getAnnuityOption());
			policyValuationTempEntity.setAccuralRateFactor(policyValuationDto.getAccuralRateFactor());
			policyValuationTempEntity.setMinPension(policyValuationDto.getMinPension());
			policyValuationTempEntity.setMaxPension(policyValuationDto.getMaxPension());
			policyValuationTempEntity.setWithdrawalRate(policyValuationDto.getWithdrawalRate());
			policyValuationTempEntity.setIsActive(policyValuationDto.getIsActive());
			policyValuationTempEntity.setCreatedOn(policyValuationDto.getCreatedOn());
			policyValuationTempEntity.setCreatedBy(policyValuationDto.getCreatedBy());
			policyValuationTempEntity.setModifiedOn(policyValuationDto.getModifiedOn());
			policyValuationTempEntity.setModifiedBy(policyValuationDto.getModifiedBy());
			valuations.add(policyValuationTempEntity);
		}
		response.setValuations(valuations);
		Set<PolicyRulesTempEntity> rules = new HashSet<>();
		for (PolicyRulesDto policyRulesDto : policyMasterDto.getRules()) {
			PolicyRulesTempEntity policyAnnuityTempEntity = new PolicyRulesTempEntity();
			policyAnnuityTempEntity.setRuleId(policyRulesDto.getRuleId());
			policyAnnuityTempEntity.setPolicyId(policyRulesDto.getPolicyId());
			policyAnnuityTempEntity.setEffectiveFrom(policyRulesDto.getEffectiveFrom());
			policyAnnuityTempEntity.setPercentageCorpus(policyRulesDto.getPercentageCorpus());
			policyAnnuityTempEntity.setCategory(policyRulesDto.getCategory());
			policyAnnuityTempEntity.setContributionType(policyRulesDto.getContributionType());
			policyAnnuityTempEntity.setBenifitPayableTo(policyRulesDto.getBenifitPayableTo());
			policyAnnuityTempEntity.setAnnuityOption(policyRulesDto.getAnnuityOption());
			policyAnnuityTempEntity.setCommutationBy(policyRulesDto.getCommutationBy());
			policyAnnuityTempEntity.setCommutationAmt(policyRulesDto.getCommutationAmt());
			policyAnnuityTempEntity.setNormalAgeRetirement(policyRulesDto.getNormalAgeRetirement());
			policyAnnuityTempEntity.setMinAgeRetirement(policyRulesDto.getMinAgeRetirement());
			policyAnnuityTempEntity.setMaxAgeRetirement(policyRulesDto.getMaxAgeRetirement());
			policyAnnuityTempEntity.setMinServicePension(policyRulesDto.getMinServicePension());
			policyAnnuityTempEntity.setMaxServicePension(policyRulesDto.getMaxServicePension());
			policyAnnuityTempEntity.setMinServiceWithdrawal(policyRulesDto.getMinServiceWithdrawal());
			policyAnnuityTempEntity.setMaxServiceWithdrawal(policyRulesDto.getMaxServiceWithdrawal());
			policyAnnuityTempEntity.setMinPension(policyRulesDto.getMinPension());
			policyAnnuityTempEntity.setMaxPension(policyRulesDto.getMaxPension());
			policyAnnuityTempEntity.setModifiedBy(policyRulesDto.getModifiedBy());
			policyAnnuityTempEntity.setModifiedOn(policyRulesDto.getModifiedOn());
			policyAnnuityTempEntity.setCreatedBy(policyRulesDto.getCreatedBy());
			policyAnnuityTempEntity.setCreatedOn(policyRulesDto.getCreatedOn());
			policyAnnuityTempEntity.setIsActive(policyRulesDto.getIsActive());
//			policyAnnuityTempEntity.setIsPolicy(Boolean.TRUE);
			rules.add(policyAnnuityTempEntity);
		}
		response.setRules(rules);
		Set<PolicyDepositTempEntity> deposits = new HashSet<>();
		for (PolicyDepositDto policyDepositDto : policyMasterDto.getDeposits()) {
			PolicyDepositTempEntity policyDepositTempEntity = new PolicyDepositTempEntity();
			policyDepositTempEntity.setDepositId(policyDepositDto.getDepositId());
			policyDepositTempEntity.setPolicyId(policyDepositDto.getPolicyId());
			policyDepositTempEntity.setCollectionNo(policyDepositDto.getCollectionNo());
			policyDepositTempEntity.setCollectionDate(policyDepositDto.getCollectionDate());
			policyDepositTempEntity.setCollectionStatus(policyDepositDto.getCollectionStatus());
			policyDepositTempEntity.setChallanNo(policyDepositDto.getChallanNo());
			policyDepositTempEntity.setDepositAmount(policyDepositDto.getDepositAmount());
			policyDepositTempEntity.setAdjustmentAmount(policyDepositDto.getAdjustmentAmount());
			policyDepositTempEntity.setAvailableAmount(policyDepositDto.getAvailableAmount());
			policyDepositTempEntity.setTransactionMode(policyDepositDto.getTransactionMode());
			policyDepositTempEntity.setChequeRealisationDate(policyDepositDto.getChequeRealisationDate());
			policyDepositTempEntity.setRemark(policyDepositDto.getRemark());
			policyDepositTempEntity.setStatus(policyDepositDto.getStatus());
			policyDepositTempEntity.setZeroId(policyDepositDto.getZeroId());
			policyDepositTempEntity.setModifiedBy(policyDepositDto.getModifiedBy());
			policyDepositTempEntity.setModifiedOn(policyDepositDto.getModifiedOn());
			policyDepositTempEntity.setCreatedBy(policyDepositDto.getCreatedBy());
			policyDepositTempEntity.setCreatedOn(policyDepositDto.getCreatedOn());
			policyDepositTempEntity.setIsActive(policyDepositDto.getIsActive());
			deposits.add(policyDepositTempEntity);
		}
		response.setDeposits(deposits);
		Set<PolicyContributionTempEntity> policyContributions = new HashSet<>();
		for (PolicyContributionDto policyContributionDto : policyMasterDto.getPolicyContributions()) {
			PolicyContributionTempEntity policyContributionTempEntity = new PolicyContributionTempEntity();
			policyContributionTempEntity.setContributionId(policyContributionDto.getContributionId());
			policyContributionTempEntity.setPolicyId(policyContributionDto.getPolicyId());
			policyContributionTempEntity.setContReferenceNo(policyContributionDto.getContReferenceNo());
			policyContributionTempEntity.setContributionType(policyContributionDto.getContributionType());
			policyContributionTempEntity.setContributionDate(policyContributionDto.getContributionDate());
			policyContributionTempEntity.setOpeningBalance(policyContributionDto.getOpeningBalance());
			policyContributionTempEntity.setClosingBalance(policyContributionDto.getClosingBalance());
			policyContributionTempEntity.setEmployerContribution(policyContributionDto.getEmployerContribution());
			policyContributionTempEntity.setEmployeeContribution(policyContributionDto.getEmployeeContribution());
			policyContributionTempEntity.setVoluntaryContribution(policyContributionDto.getVoluntaryContribution());
			policyContributionTempEntity.setTotalContribution(policyContributionDto.getTotalContribution());
			policyContributionTempEntity.setIsDeposit(policyContributionDto.getIsDeposit());
			policyContributionTempEntity.setFinancialYear(policyContributionDto.getFinancialYear());
			policyContributionTempEntity.setQuarter(policyContributionDto.getQuarter());
			policyContributionTempEntity.setVersionNo(policyContributionDto.getVersionNo());
			policyContributionTempEntity.setCreatedOn(policyContributionDto.getCreatedOn());
			policyContributionTempEntity.setCreatedBy(policyContributionDto.getCreatedBy());
			policyContributionTempEntity.setModifiedOn(policyContributionDto.getModifiedOn());
			policyContributionTempEntity.setModifiedBy(policyContributionDto.getModifiedBy());
			policyContributionTempEntity.setIsActive(policyContributionDto.getIsActive());
			policyContributions.add(policyContributionTempEntity);
		}
		response.setPolicyContributions(policyContributions);
		Set<PolicyContributionSummaryTempEntity> policyContributionSummary = new HashSet<>();
		for (PolicyContributionSummaryDto policyContributionSummaryDto : policyMasterDto
				.getPolicyContributionSummary()) {
			PolicyContributionSummaryTempEntity policyContributionSummaryTempEntity = new PolicyContributionSummaryTempEntity();
			policyContributionSummaryTempEntity.setPolContSummaryId(policyContributionSummaryDto.getPolContSummaryId());
			policyContributionSummaryTempEntity.setPolicyId(policyContributionSummaryDto.getPolicyId());
			policyContributionSummaryTempEntity.setStampDuty(policyContributionSummaryDto.getStampDuty());
			policyContributionSummaryTempEntity.setOpeningBalance(policyContributionSummaryDto.getOpeningBalance());
			policyContributionSummaryTempEntity
					.setTotalEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
			policyContributionSummaryTempEntity
					.setTotalEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
			policyContributionSummaryTempEntity
					.setTotalVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
			policyContributionSummaryTempEntity
					.setTotalContribution(policyContributionSummaryDto.getTotalContribution());
			policyContributionSummaryTempEntity.setClosingBalance(policyContributionSummaryDto.getClosingBalance());
			policyContributionSummaryTempEntity.setFinancialYear(policyContributionSummaryDto.getFinancialYear());
			policyContributionSummaryTempEntity.setQuarter(policyContributionSummaryDto.getQuarter());
			policyContributionSummaryTempEntity.setIsActive(policyContributionSummaryDto.getIsActive());
			policyContributionSummaryTempEntity.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
			policyContributionSummaryTempEntity.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
			policyContributionSummaryTempEntity.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
			policyContributionSummaryTempEntity.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
			policyContributionSummary.add(policyContributionSummaryTempEntity);
		}
		response.setPolicyContributionSummary(policyContributionSummary);
		Set<ZeroAccountTempEntity> zeroAccount = new HashSet<>();
		for (ZeroAccountdto zeroAccountdto : policyMasterDto.getZeroAccount()) {
			ZeroAccountTempEntity zeroAccountTempEntity = new ZeroAccountTempEntity();
			zeroAccountTempEntity.setZeroAccId(zeroAccountdto.getZeroAccId());
			zeroAccountTempEntity.setPolicyId(zeroAccountdto.getPolicyId());
			zeroAccountTempEntity.setZeroIdAmount(zeroAccountdto.getZeroIdAmount());
			zeroAccountTempEntity.setIsActive(zeroAccountdto.getIsActive());
			zeroAccountTempEntity.setCreatedOn(zeroAccountdto.getCreatedOn());
			zeroAccountTempEntity.setCreatedBy(zeroAccountdto.getCreatedBy());
			zeroAccountTempEntity.setModifiedOn(zeroAccountdto.getModifiedOn());
			zeroAccountTempEntity.setModifiedBy(zeroAccountdto.getModifiedBy());
			zeroAccount.add(zeroAccountTempEntity);
		}
		response.setZeroAccount(zeroAccount);
		Set<ZeroAccountEntriesTempEntity> zeroAccountEntries = new HashSet<>();
		for (ZeroAccountEntriesDto zeroAccountEntriesDto : policyMasterDto.getZeroAccountEntries()) {
			ZeroAccountEntriesTempEntity zeroAccountEntriesTempEntity = new ZeroAccountEntriesTempEntity();
			zeroAccountEntriesTempEntity.setZeroAccEntId(zeroAccountEntriesDto.getZeroAccEntId());
			zeroAccountEntriesTempEntity.setPolicyId(zeroAccountEntriesDto.getPolicyId());
			zeroAccountEntriesTempEntity.setPolicyConId(zeroAccountEntriesDto.getPolicyConId());
			zeroAccountEntriesTempEntity.setMemberConId(zeroAccountEntriesDto.getMemberConId());
			zeroAccountEntriesTempEntity.setTransactionType(zeroAccountEntriesDto.getTransactionType());
			zeroAccountEntriesTempEntity.setTransactionDate(zeroAccountEntriesDto.getTransactionDate());
			zeroAccountEntriesTempEntity.setZeroIdAmount(zeroAccountEntriesDto.getZeroIdAmount());
			zeroAccountEntriesTempEntity.setCreatedOn(zeroAccountEntriesDto.getCreatedOn());
			zeroAccountEntriesTempEntity.setCreatedBy(zeroAccountEntriesDto.getCreatedBy());
			zeroAccountEntriesTempEntity.setModifiedOn(zeroAccountEntriesDto.getModifiedOn());
			zeroAccountEntriesTempEntity.setModifiedBy(zeroAccountEntriesDto.getModifiedBy());
			zeroAccountEntriesTempEntity.setIsActive(zeroAccountEntriesDto.getIsActive());
			zeroAccountEntries.add(zeroAccountEntriesTempEntity);
		}
		response.setZeroAccountEntries(zeroAccountEntries);
		Set<PolicyNotesTempEntity> notes = new HashSet<>();
		for (PolicyNotesDto policyNotesDto : policyMasterDto.getNotes()) {
			PolicyNotesTempEntity policyNotesTempEntity = new PolicyNotesTempEntity();
			policyNotesTempEntity.setPolicyNoteId(policyNotesDto.getPolicyNoteId());
			policyNotesTempEntity.setPolicyId(policyNotesDto.getPolicyId());
			policyNotesTempEntity.setNoteContents(policyNotesDto.getNoteContents());
			policyNotesTempEntity.setModifiedBy(policyNotesDto.getModifiedBy());
			policyNotesTempEntity.setModifiedOn(policyNotesDto.getModifiedOn());
			policyNotesTempEntity.setCreatedBy(policyNotesDto.getCreatedBy());
			policyNotesTempEntity.setCreatedOn(policyNotesDto.getCreatedOn());
			policyNotesTempEntity.setIsActive(policyNotesDto.getIsActive());
			notes.add(policyNotesTempEntity);
		}
		response.setNotes(notes);
		Set<MemberMasterTempEntity> memberMasterTempEntityList = new HashSet<>();
		for (MemberMasterDto policyValuationDto : policyMasterDto.getMemberMaster()) {
			MemberMasterTempEntity memberMasterTempEntity = convertMemberDtoToMemberTempEntity(policyValuationDto);
			memberMasterTempEntityList.add(memberMasterTempEntity);
		}
		response.setMemberMaster(memberMasterTempEntityList);
		return response;
	}

	public MemberMasterTempEntity convertMemberDtoToMemberTempEntity(MemberMasterDto request) {
		MemberMasterTempEntity response = new MemberMasterTempEntity();
		response.setMemberId(request.getMemberId());
		response.setPolicyId(request.getPolicyId());
		response.setMembershipNumber(request.getMembershipNumber());
		response.setMemberStatus(request.getMemberStatus());
		response.setLicId(request.getLicId());
		response.setTypeOfMemebership(request.getTypeOfMemebership());
		response.setAadharNumber(request.getAadharNumber());
		response.setCategoryNo(request.getCategoryNo());
		response.setCommunicationPreference(request.getCommunicationPreference());
		response.setLanguagePreference(request.getLanguagePreference());
		response.setDateOfBirth(request.getDateOfBirth());
		response.setDateOfJoining(request.getDateOfJoining());
		response.setDateOfJoiningScheme(request.getDateOfJoiningScheme());
		response.setDateOfRetrirement(request.getDateOfRetrirement());
		response.setDesignation(request.getDesignation());
		response.setEmailid(request.getEmailid());
		response.setGender(request.getGender());
		response.setFatherName(request.getFatherName());
		response.setFirstName(request.getFirstName());
		response.setMiddleName(request.getMiddleName());
		response.setLastName(request.getLastName());
		response.setSpouseName(request.getSpouseName());
		response.setMaritalStatus(request.getMaritalStatus());
		response.setMemberPan(request.getMemberPan());
		response.setLandlineNo(request.getLandlineNo());
		response.setMobileNo(request.getMobileNo());
		response.setIsZeroid(request.getIsZeroid());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		Set<MemberAddressTempEntity> memberAddressList = new HashSet<>();
		for (MemberAddressDto dto : request.getMemberAddress()) {
			MemberAddressTempEntity entity = new MemberAddressTempEntity();
			entity.setAddressId(dto.getAddressId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setAddress1(dto.getAddress1());
			entity.setAddress2(dto.getAddress2());
			entity.setAddress3(dto.getAddress3());
			entity.setAddressType(dto.getAddressType());
			entity.setCity(dto.getCity());
			entity.setDistrict(dto.getDistrict());
			entity.setStateName(dto.getStateName());
			entity.setCountry(dto.getCountry());
			entity.setPincode(dto.getPincode());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberAddressList.add(entity);
		}
		response.setMemberAddress(memberAddressList);
		Set<MemberBankTempEntity> memberBankList = new HashSet<>();
		for (MemberBankDto dto : request.getMemberBank()) {
			MemberBankTempEntity entity = new MemberBankTempEntity();
			entity.setMemberBankId(dto.getMemberBankId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setConfirmAccountNumber(dto.getConfirmAccountNumber());
			entity.setAccountType(dto.getAccountType());
			entity.setIfscCodeAvailable(dto.getIfscCodeAvailable());
			entity.setIfscCode(dto.getIfscCode());
			entity.setBankName(dto.getBankName());
			entity.setBankBranch(dto.getBankBranch());
			entity.setBankAddress(dto.getBankAddress());
			entity.setCountryCode(dto.getCountryCode());
			entity.setStdCode(dto.getStdCode());
			entity.setLandlineNumber(dto.getLandlineNumber());
			entity.setEmailId(dto.getEmailId());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberBankList.add(entity);
		}
		response.setMemberBank(memberBankList);
		Set<MemberNomineeTempEntity> memberNomineeList = new HashSet<>();
		for (MemberNomineeDto dto : request.getMemberNominee()) {
			MemberNomineeTempEntity entity = new MemberNomineeTempEntity();
			entity.setNomineeId(dto.getNomineeId());
			entity.setMemberId(dto.getMemberId());
			entity.setNomineeType(dto.getNomineeType());
			entity.setNomineeName(dto.getNomineeName());
			entity.setRelationShip(dto.getRelationShip());
			entity.setAadharNumber(dto.getAadharNumber());
			entity.setPhone(dto.getPhone());
			entity.setEmailId(dto.getEmailId());
			entity.setAddressOne(dto.getAddressOne());
			entity.setAddressTwo(dto.getAddressTwo());
			entity.setAddressThree(dto.getAddressThree());
			entity.setCountry(dto.getCountry());
			entity.setPinCode(dto.getPinCode());
			entity.setDistrict(dto.getDistrict());
			entity.setState(dto.getState());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setPercentage(dto.getPercentage());
			entity.setAge(dto.getAge());
			entity.setDateOfBirth(dto.getDateOfBirth());
			memberNomineeList.add(entity);
		}
		response.setMemberNominee(memberNomineeList);
		Set<MemberAppointeeTempEntity> memberAppointeeList = new HashSet<>();
		for (MemberAppointeeDto dto : request.getMemberAppointee()) {
			MemberAppointeeTempEntity entity = new MemberAppointeeTempEntity();
			entity.setAppointeeId(dto.getAppointeeId());
			entity.setMemberId(dto.getMemberId());
			entity.setAppointeeName(dto.getAppointeeName());
			entity.setAppointeeCode(dto.getAppointeeCode());
			entity.setRelationship(dto.getRelationship());
			entity.setContactNumber(dto.getContactNumber());
			entity.setDateOfBirth(dto.getDateOfBirth());
			entity.setPan(dto.getPan());
			entity.setAadharNumber(dto.getAadharNumber());
			entity.setIfscCode(dto.getIfscCode());
			entity.setBankName(dto.getBankName());
			entity.setAccountType(dto.getAccountType());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberAppointeeList.add(entity);
		}
		response.setMemberAppointee(memberAppointeeList);
		Set<MemberContributionTempEntity> memberContributionList = new HashSet<>();
		for (MemberContributionDto dto : request.getMemberContribution()) {
			MemberContributionTempEntity entity = new MemberContributionTempEntity();
			entity.setMemberConId(dto.getMemberConId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyConId(dto.getPolicyConId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setContributionType(dto.getContributionType());
			entity.setContributionDate(dto.getContributionDate());
			entity.setLicId(dto.getLicId());
			entity.setOpeningBalance(dto.getOpeningBalance());
			entity.setEmployeeContribution(dto.getEmployeeContribution());
			entity.setEmployerContribution(dto.getEmployerContribution());
			entity.setVoluntaryContribution(dto.getVoluntaryContribution());
			entity.setTotalContribution(dto.getTotalContribution());
			entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
			entity.setClosingBalance(dto.getClosingBalance());
//			entity.setIsZeroAccount(dto.getIsZeroAccount());
			entity.setIsDeposit(dto.getIsDeposit());
			entity.setFinancialYear(dto.getFinancialYear());
			entity.setQuarter(dto.getQuarter());
			entity.setVersionNo(dto.getVersionNo());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setIsActive(dto.getIsActive());
			memberContributionList.add(entity);
		}
		response.setMemberContribution(memberContributionList);

		Set<MemberContributionSummaryTempEntity> memberContributionSummaryList = new HashSet<>();
		for (MemberContributionSummaryDto dto : request.getMemberContributionSummary()) {
			MemberContributionSummaryTempEntity entity = new MemberContributionSummaryTempEntity();
			entity.setMemContSummaryId(dto.getMemContSummaryId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setLicId(dto.getLicId());
			entity.setOpeningBalance(dto.getOpeningBalance());
			entity.setTotalEmployeeContribution(dto.getTotalEmployeeContribution());
			entity.setClosingBalance(dto.getClosingBalance());
			entity.setTotalEmployerContribution(dto.getTotalEmployerContribution());
			entity.setTotalVoluntaryContribution(dto.getTotalVoluntaryContribution());
			entity.setTotalContribution(dto.getTotalContribution());
			entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
			entity.setFinancialYear(dto.getFinancialYear());
			entity.setQuarter(dto.getQuarter());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setIsActive(dto.getIsActive());
			memberContributionSummaryList.add(entity);
		}
		response.setMemberContributionSummary(memberContributionSummaryList);
		return response;
	}

// TEMP ENTITY TO DTO
	public MphMasterDto convertMphMasterTempEntityToMphMasterDto(MphMasterTempEntity masterDto) {

		MphMasterDto response = new MphMasterDto();

		response.setMphId(masterDto.getMphId());
		response.setMasterMphId(masterDto.getMasterMphId());
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
		Set<MphAddressDto> mphAddress = new HashSet<>();
		for (MphAddressTempEntity mphAddressDto : masterDto.getMphAddress()) {
			MphAddressDto mphAddressTempEntity = new MphAddressDto();
			mphAddressTempEntity.setAddressId(mphAddressDto.getAddressId());
			mphAddressTempEntity.setMphId(mphAddressDto.getMphId());
			mphAddressTempEntity.setAddressType(mphAddressDto.getAddressType());
			mphAddressTempEntity.setAddressLine1(mphAddressDto.getAddressLine1());
			mphAddressTempEntity.setAddressLine2(mphAddressDto.getAddressLine2());
			mphAddressTempEntity.setAddressLine3(mphAddressDto.getAddressLine3());
			mphAddressTempEntity.setPincode(mphAddressDto.getPincode());
			mphAddressTempEntity.setCityLocality(mphAddressDto.getCityLocality());
			mphAddressTempEntity.setCityId(mphAddressDto.getCityId());
			mphAddressTempEntity.setDistrict(mphAddressDto.getDistrict());
			mphAddressTempEntity.setDistrictId(mphAddressDto.getDistrictId());
			mphAddressTempEntity.setCountryId(mphAddressDto.getCountryId());
			mphAddressTempEntity.setStateId(mphAddressDto.getStateId());
			mphAddressTempEntity.setStateName(mphAddressDto.getStateName());
			mphAddressTempEntity.setIsActive(mphAddressDto.getIsActive());
			mphAddressTempEntity.setCreatedOn(mphAddressDto.getCreatedOn());
			mphAddressTempEntity.setCreatedBy(mphAddressDto.getCreatedBy());
			mphAddressTempEntity.setModifiedOn(mphAddressDto.getModifiedOn());
			mphAddressTempEntity.setModifiedBy(mphAddressDto.getModifiedBy());
			mphAddress.add(mphAddressTempEntity);
		}
		response.setMphAddress(mphAddress);
		Set<MphBankDto> mphBank = new HashSet<>();
		for (MphBankTempEntity mphBankDto : masterDto.getMphBank()) {
			MphBankDto mphBankTempEntity = new MphBankDto();
			mphBankTempEntity.setMphBankId(mphBankDto.getMphBankId());
			mphBankTempEntity.setMphId(mphBankDto.getMphId());
			mphBankTempEntity.setAccountNumber(mphBankDto.getAccountNumber());
			mphBankTempEntity.setConfirmAccountNumber(mphBankDto.getConfirmAccountNumber());
			mphBankTempEntity.setAccountType(mphBankDto.getAccountType());
			mphBankTempEntity.setIfscCodeAvailable(mphBankDto.getIfscCodeAvailable());
			mphBankTempEntity.setIfscCode(mphBankDto.getIfscCode());
			mphBankTempEntity.setBankName(mphBankDto.getBankName());
			mphBankTempEntity.setBankBranch(mphBankDto.getBankBranch());
			mphBankTempEntity.setBankAddress(mphBankDto.getBankAddress());
			mphBankTempEntity.setStdCode(mphBankDto.getStdCode());
			mphBankTempEntity.setLandlineNumber(mphBankDto.getLandlineNumber());
			mphBankTempEntity.setEmailId(mphBankDto.getEmailId());
			mphBankTempEntity.setCountryCode(mphBankDto.getCountryCode());
			mphBankTempEntity.setCountryId(mphBankDto.getCountryId());
			mphBankTempEntity.setStateId(mphBankDto.getStateId());
			mphBankTempEntity.setDistrictId(mphBankDto.getDistrictId());
			mphBankTempEntity.setCityId(mphBankDto.getCityId());
			mphBankTempEntity.setTownLocality(mphBankDto.getTownLocality());
			mphBankTempEntity.setIsActive(mphBankDto.getIsActive());
			mphBankTempEntity.setCreatedBy(mphBankDto.getCreatedBy());
			mphBankTempEntity.setCreatedOn(mphBankDto.getCreatedOn());
			mphBankTempEntity.setModifiedOn(mphBankDto.getModifiedOn());
			mphBankTempEntity.setModifiedBy(mphBankDto.getModifiedBy());
			mphBank.add(mphBankTempEntity);
		}
		response.setMphBank(mphBank);
		Set<MphRepresentativesDto> mphRepresentative = new HashSet<>();
		for (MphRepresentativesTempEntity mphRepresentativeDto : masterDto.getMphRepresentative()) {
			MphRepresentativesDto mphRepresentativesTempEntity = new MphRepresentativesDto();
			mphRepresentativesTempEntity.setRepId(mphRepresentativeDto.getRepId());
			mphRepresentativesTempEntity.setMphId(mphRepresentativeDto.getMphId());
			mphRepresentativesTempEntity.setRepresentativeCode(mphRepresentativeDto.getRepresentativeCode());
			mphRepresentativesTempEntity.setRepresentativeName(mphRepresentativeDto.getRepresentativeName());
			mphRepresentativesTempEntity.setLandlineNo(mphRepresentativeDto.getLandlineNo());
			mphRepresentativesTempEntity.setCountryCode(mphRepresentativeDto.getCountryCode());
			mphRepresentativesTempEntity.setMobileNo(mphRepresentativeDto.getMobileNo());
			mphRepresentativesTempEntity.setEmailId(mphRepresentativeDto.getEmailId());
			mphRepresentativesTempEntity.setAddressType(mphRepresentativeDto.getAddressType());
			mphRepresentativesTempEntity.setAddressLine1(mphRepresentativeDto.getAddressLine1());
			mphRepresentativesTempEntity.setAddressLine2(mphRepresentativeDto.getAddressLine2());
			mphRepresentativesTempEntity.setAddressLine3(mphRepresentativeDto.getAddressLine3());
			mphRepresentativesTempEntity.setCityLocality(mphRepresentativeDto.getCityLocality());
			mphRepresentativesTempEntity.setDistrict(mphRepresentativeDto.getDistrict());
			mphRepresentativesTempEntity.setStateName(mphRepresentativeDto.getStateName());
			mphRepresentativesTempEntity.setPincode(mphRepresentativeDto.getPincode());
			mphRepresentativesTempEntity.setIsActive(mphRepresentativeDto.getIsActive());
			mphRepresentativesTempEntity.setCreatedOn(mphRepresentativeDto.getCreatedOn());
			mphRepresentativesTempEntity.setCreatedBy(mphRepresentativeDto.getCreatedBy());
			mphRepresentativesTempEntity.setModifiedOn(mphRepresentativeDto.getModifiedOn());
			mphRepresentativesTempEntity.setModifiedBy(mphRepresentativeDto.getModifiedBy());
			mphRepresentative.add(mphRepresentativesTempEntity);
		}
		response.setMphRepresentative(mphRepresentative);

		Set<PolicyMasterDto> policyMaster = new HashSet<>();
		for (PolicyMasterTempEntity policyDto : masterDto.getPolicyMaster()) {
			PolicyMasterDto policyMasterTempEntity = convertPolicyMasterTempEntityToPolicyMasterDto(policyDto);
			policyMaster.add(policyMasterTempEntity);
		}
		response.setPolicyMaster(policyMaster);
		return response;
	}

	public PolicyMasterDto convertPolicyMasterTempEntityToPolicyMasterDto(PolicyMasterTempEntity policyMasterDto) {
		PolicyMasterDto response = new PolicyMasterDto();
		response.setPolicyId(policyMasterDto.getPolicyId());
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
		response.setStampDuty(policyMasterDto.getStampDuty());
		Set<PolicyValuationDto> valuations = new HashSet<>();
		for (PolicyValuationTempEntity policyValuationDto : policyMasterDto.getValuations()) {
			PolicyValuationDto policyValuationTempEntity = new PolicyValuationDto();
			policyValuationTempEntity.setValuationId(policyValuationDto.getValuationId());
			policyValuationTempEntity.setPolicyId(policyValuationDto.getPolicyId());
			policyValuationTempEntity.setValuationType(policyValuationDto.getValuationType());
			policyValuationTempEntity.setAttritionRate(policyValuationDto.getAttritionRate());
			policyValuationTempEntity.setSalaryEscalation(policyValuationDto.getSalaryEscalation());
			policyValuationTempEntity.setDeathRate(policyValuationDto.getDeathRate());
			policyValuationTempEntity.setDisRateIntrest(policyValuationDto.getDisRateIntrest());
			policyValuationTempEntity.setDisRateSalaryEsc(policyValuationDto.getDisRateSalaryEsc());
			policyValuationTempEntity.setAnnuityOption(policyValuationDto.getAnnuityOption());
			policyValuationTempEntity.setAccuralRateFactor(policyValuationDto.getAccuralRateFactor());
			policyValuationTempEntity.setMinPension(policyValuationDto.getMinPension());
			policyValuationTempEntity.setMaxPension(policyValuationDto.getMaxPension());
			policyValuationTempEntity.setWithdrawalRate(policyValuationDto.getWithdrawalRate());
			policyValuationTempEntity.setIsActive(policyValuationDto.getIsActive());
			policyValuationTempEntity.setCreatedOn(policyValuationDto.getCreatedOn());
			policyValuationTempEntity.setCreatedBy(policyValuationDto.getCreatedBy());
			policyValuationTempEntity.setModifiedOn(policyValuationDto.getModifiedOn());
			policyValuationTempEntity.setModifiedBy(policyValuationDto.getModifiedBy());
			valuations.add(policyValuationTempEntity);
		}
		response.setValuations(valuations);
		Set<PolicyRulesDto> rules = new HashSet<>();
		for (PolicyRulesTempEntity policyRulesDto : policyMasterDto.getRules()) {
			PolicyRulesDto policyAnnuityTempEntity = new PolicyRulesDto();
			policyAnnuityTempEntity.setRuleId(policyRulesDto.getRuleId());
			policyAnnuityTempEntity.setPolicyId(policyRulesDto.getPolicyId());
			policyAnnuityTempEntity.setEffectiveFrom(policyRulesDto.getEffectiveFrom());
			policyAnnuityTempEntity.setPercentageCorpus(policyRulesDto.getPercentageCorpus());
			policyAnnuityTempEntity.setCategory(policyRulesDto.getCategory());
			policyAnnuityTempEntity.setContributionType(policyRulesDto.getContributionType());
			policyAnnuityTempEntity.setBenifitPayableTo(policyRulesDto.getBenifitPayableTo());
			policyAnnuityTempEntity.setAnnuityOption(policyRulesDto.getAnnuityOption());
			policyAnnuityTempEntity.setCommutationBy(policyRulesDto.getCommutationBy());
			policyAnnuityTempEntity.setCommutationAmt(policyRulesDto.getCommutationAmt());
			policyAnnuityTempEntity.setNormalAgeRetirement(policyRulesDto.getNormalAgeRetirement());
			policyAnnuityTempEntity.setMinAgeRetirement(policyRulesDto.getMinAgeRetirement());
			policyAnnuityTempEntity.setMaxAgeRetirement(policyRulesDto.getMaxAgeRetirement());
			policyAnnuityTempEntity.setMinServicePension(policyRulesDto.getMinServicePension());
			policyAnnuityTempEntity.setMaxServicePension(policyRulesDto.getMaxServicePension());
			policyAnnuityTempEntity.setMinServiceWithdrawal(policyRulesDto.getMinServiceWithdrawal());
			policyAnnuityTempEntity.setMaxServiceWithdrawal(policyRulesDto.getMaxServiceWithdrawal());
			policyAnnuityTempEntity.setMinPension(policyRulesDto.getMinPension());
			policyAnnuityTempEntity.setMaxPension(policyRulesDto.getMaxPension());
			policyAnnuityTempEntity.setModifiedBy(policyRulesDto.getModifiedBy());
			policyAnnuityTempEntity.setModifiedOn(policyRulesDto.getModifiedOn());
			policyAnnuityTempEntity.setCreatedBy(policyRulesDto.getCreatedBy());
			policyAnnuityTempEntity.setCreatedOn(policyRulesDto.getCreatedOn());
			policyAnnuityTempEntity.setIsActive(policyRulesDto.getIsActive());
			rules.add(policyAnnuityTempEntity);
		}
		response.setRules(rules);
		Set<PolicyDepositDto> deposits = new HashSet<>();
		for (PolicyDepositTempEntity policyDepositDto : policyMasterDto.getDeposits()) {
			PolicyDepositDto policyDepositTempEntity = new PolicyDepositDto();
			policyDepositTempEntity.setDepositId(policyDepositDto.getDepositId());
			policyDepositTempEntity.setPolicyId(policyDepositDto.getPolicyId());
			policyDepositTempEntity.setCollectionNo(policyDepositDto.getCollectionNo());
			policyDepositTempEntity.setCollectionDate(policyDepositDto.getCollectionDate());
			policyDepositTempEntity.setCollectionStatus(policyDepositDto.getCollectionStatus());
			policyDepositTempEntity.setChallanNo(policyDepositDto.getChallanNo());
			policyDepositTempEntity.setDepositAmount(policyDepositDto.getDepositAmount());
			policyDepositTempEntity.setAdjustmentAmount(policyDepositDto.getAdjustmentAmount());
			policyDepositTempEntity.setAvailableAmount(policyDepositDto.getAvailableAmount());
			policyDepositTempEntity.setTransactionMode(policyDepositDto.getTransactionMode());
			policyDepositTempEntity.setChequeRealisationDate(policyDepositDto.getChequeRealisationDate());
			policyDepositTempEntity.setRemark(policyDepositDto.getRemark());
			policyDepositTempEntity.setStatus(policyDepositDto.getStatus());
			policyDepositTempEntity.setZeroId(policyDepositDto.getZeroId());
			policyDepositTempEntity.setModifiedBy(policyDepositDto.getModifiedBy());
			policyDepositTempEntity.setModifiedOn(policyDepositDto.getModifiedOn());
			policyDepositTempEntity.setCreatedBy(policyDepositDto.getCreatedBy());
			policyDepositTempEntity.setCreatedOn(policyDepositDto.getCreatedOn());
			policyDepositTempEntity.setIsActive(policyDepositDto.getIsActive());
			deposits.add(policyDepositTempEntity);
		}
		response.setDeposits(deposits);
		Set<PolicyContributionDto> policyContributions = new HashSet<>();
		for (PolicyContributionTempEntity policyContributionDto : policyMasterDto.getPolicyContributions()) {
			PolicyContributionDto policyContributionTempEntity = new PolicyContributionDto();
			policyContributionTempEntity.setContributionId(policyContributionDto.getContributionId());
			policyContributionTempEntity.setPolicyId(policyContributionDto.getPolicyId());
			policyContributionTempEntity.setContReferenceNo(policyContributionDto.getContReferenceNo());
			policyContributionTempEntity.setContributionType(policyContributionDto.getContributionType());
			policyContributionTempEntity.setContributionDate(policyContributionDto.getContributionDate());
			policyContributionTempEntity.setOpeningBalance(policyContributionDto.getOpeningBalance());
			policyContributionTempEntity.setClosingBalance(policyContributionDto.getClosingBalance());
			policyContributionTempEntity.setEmployerContribution(policyContributionDto.getEmployerContribution());
			policyContributionTempEntity.setEmployeeContribution(policyContributionDto.getEmployeeContribution());
			policyContributionTempEntity.setVoluntaryContribution(policyContributionDto.getVoluntaryContribution());
			policyContributionTempEntity.setTotalContribution(policyContributionDto.getTotalContribution());
			policyContributionTempEntity.setIsDeposit(policyContributionDto.getIsDeposit());
			policyContributionTempEntity.setFinancialYear(policyContributionDto.getFinancialYear());
			policyContributionTempEntity.setQuarter(policyContributionDto.getQuarter());
			policyContributionTempEntity.setVersionNo(policyContributionDto.getVersionNo());
			policyContributionTempEntity.setCreatedOn(policyContributionDto.getCreatedOn());
			policyContributionTempEntity.setCreatedBy(policyContributionDto.getCreatedBy());
			policyContributionTempEntity.setModifiedOn(policyContributionDto.getModifiedOn());
			policyContributionTempEntity.setModifiedBy(policyContributionDto.getModifiedBy());
			policyContributionTempEntity.setIsActive(policyContributionDto.getIsActive());
			policyContributions.add(policyContributionTempEntity);
		}
		response.setPolicyContributions(policyContributions);
		Set<PolicyContributionSummaryDto> policyContributionSummary = new HashSet<>();
		for (PolicyContributionSummaryTempEntity policyContributionSummaryDto : policyMasterDto
				.getPolicyContributionSummary()) {
			PolicyContributionSummaryDto policyContributionSummaryTempEntity = new PolicyContributionSummaryDto();
			policyContributionSummaryTempEntity.setPolContSummaryId(policyContributionSummaryDto.getPolContSummaryId());
			policyContributionSummaryTempEntity.setPolicyId(policyContributionSummaryDto.getPolicyId());
			policyContributionSummaryTempEntity.setStampDuty(policyContributionSummaryDto.getStampDuty());
			policyContributionSummaryTempEntity.setOpeningBalance(policyContributionSummaryDto.getOpeningBalance());
			policyContributionSummaryTempEntity
					.setTotalEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
			policyContributionSummaryTempEntity
					.setTotalEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
			policyContributionSummaryTempEntity
					.setTotalVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
			policyContributionSummaryTempEntity
					.setTotalContribution(policyContributionSummaryDto.getTotalContribution());
			policyContributionSummaryTempEntity.setClosingBalance(policyContributionSummaryDto.getClosingBalance());
			policyContributionSummaryTempEntity.setFinancialYear(policyContributionSummaryDto.getFinancialYear());
			policyContributionSummaryTempEntity.setQuarter(policyContributionSummaryDto.getQuarter());
			policyContributionSummaryTempEntity.setIsActive(policyContributionSummaryDto.getIsActive());
			policyContributionSummaryTempEntity.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
			policyContributionSummaryTempEntity.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
			policyContributionSummaryTempEntity.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
			policyContributionSummaryTempEntity.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
			policyContributionSummary.add(policyContributionSummaryTempEntity);
		}
		response.setPolicyContributionSummary(policyContributionSummary);
		Set<ZeroAccountdto> zeroAccount = new HashSet<>();
		for (ZeroAccountTempEntity zeroAccountdto : policyMasterDto.getZeroAccount()) {
			ZeroAccountdto zeroAccountTempEntity = new ZeroAccountdto();
			zeroAccountTempEntity.setZeroAccId(zeroAccountdto.getZeroAccId());
			zeroAccountTempEntity.setPolicyId(zeroAccountdto.getPolicyId());
			zeroAccountTempEntity.setZeroIdAmount(zeroAccountdto.getZeroIdAmount());
			zeroAccountTempEntity.setIsActive(zeroAccountdto.getIsActive());
			zeroAccountTempEntity.setCreatedOn(zeroAccountdto.getCreatedOn());
			zeroAccountTempEntity.setCreatedBy(zeroAccountdto.getCreatedBy());
			zeroAccountTempEntity.setModifiedOn(zeroAccountdto.getModifiedOn());
			zeroAccountTempEntity.setModifiedBy(zeroAccountdto.getModifiedBy());
			zeroAccount.add(zeroAccountTempEntity);
		}
		response.setZeroAccount(zeroAccount);
		Set<ZeroAccountEntriesDto> zeroAccountEntries = new HashSet<>();
		for (ZeroAccountEntriesTempEntity zeroAccountEntriesDto : policyMasterDto.getZeroAccountEntries()) {
			ZeroAccountEntriesDto zeroAccountEntriesTempEntity = new ZeroAccountEntriesDto();
			zeroAccountEntriesTempEntity.setZeroAccEntId(zeroAccountEntriesDto.getZeroAccEntId());
			zeroAccountEntriesTempEntity.setPolicyId(zeroAccountEntriesDto.getPolicyId());
			zeroAccountEntriesTempEntity.setPolicyConId(zeroAccountEntriesDto.getPolicyConId());
			zeroAccountEntriesTempEntity.setMemberConId(zeroAccountEntriesDto.getMemberConId());
			zeroAccountEntriesTempEntity.setTransactionType(zeroAccountEntriesDto.getTransactionType());
			zeroAccountEntriesTempEntity.setTransactionDate(zeroAccountEntriesDto.getTransactionDate());
			zeroAccountEntriesTempEntity.setZeroIdAmount(zeroAccountEntriesDto.getZeroIdAmount());
			zeroAccountEntriesTempEntity.setCreatedOn(zeroAccountEntriesDto.getCreatedOn());
			zeroAccountEntriesTempEntity.setCreatedBy(zeroAccountEntriesDto.getCreatedBy());
			zeroAccountEntriesTempEntity.setModifiedOn(zeroAccountEntriesDto.getModifiedOn());
			zeroAccountEntriesTempEntity.setModifiedBy(zeroAccountEntriesDto.getModifiedBy());
			zeroAccountEntriesTempEntity.setIsActive(zeroAccountEntriesDto.getIsActive());
			zeroAccountEntries.add(zeroAccountEntriesTempEntity);
		}
		response.setZeroAccountEntries(zeroAccountEntries);
		Set<PolicyNotesDto> notes = new HashSet<>();
		for (PolicyNotesTempEntity policyNotesDto : policyMasterDto.getNotes()) {
			PolicyNotesDto policyNotesTempEntity = new PolicyNotesDto();
			policyNotesTempEntity.setPolicyNoteId(policyNotesDto.getPolicyNoteId());
			policyNotesTempEntity.setPolicyId(policyNotesDto.getPolicyId());
			policyNotesTempEntity.setNoteContents(policyNotesDto.getNoteContents());
			policyNotesTempEntity.setModifiedBy(policyNotesDto.getModifiedBy());
			policyNotesTempEntity.setModifiedOn(policyNotesDto.getModifiedOn());
			policyNotesTempEntity.setCreatedBy(policyNotesDto.getCreatedBy());
			policyNotesTempEntity.setCreatedOn(policyNotesDto.getCreatedOn());
			policyNotesTempEntity.setIsActive(policyNotesDto.getIsActive());
			notes.add(policyNotesTempEntity);
		}
		response.setNotes(notes);
		Set<MemberMasterDto> memberMasterTempEntityList = new HashSet<>();
		for (MemberMasterTempEntity policyValuationDto : policyMasterDto.getMemberMaster()) {
			MemberMasterDto memberMasterTempEntity = convertMemberTempEntityToMemberDto(policyValuationDto);
			memberMasterTempEntityList.add(memberMasterTempEntity);
		}
		response.setMemberMaster(memberMasterTempEntityList);
		return response;
	}

	public MemberMasterDto convertMemberTempEntityToMemberDto(MemberMasterTempEntity request) {
		MemberMasterDto response = new MemberMasterDto();
		response.setMemberId(request.getMemberId());
		response.setPolicyId(request.getPolicyId());
		response.setMembershipNumber(request.getMembershipNumber());
		response.setMemberStatus(request.getMemberStatus());
		response.setLicId(request.getLicId());
		response.setTypeOfMemebership(request.getTypeOfMemebership());
		response.setAadharNumber(request.getAadharNumber());
		response.setCategoryNo(request.getCategoryNo());
		response.setCommunicationPreference(request.getCommunicationPreference());
		response.setLanguagePreference(request.getLanguagePreference());
		response.setDateOfBirth(request.getDateOfBirth());
		response.setDateOfJoining(request.getDateOfJoining());
		response.setDateOfJoiningScheme(request.getDateOfJoiningScheme());
		response.setDateOfRetrirement(request.getDateOfRetrirement());
		response.setDesignation(request.getDesignation());
		response.setEmailid(request.getEmailid());
		response.setGender(request.getGender());
		response.setFatherName(request.getFatherName());
		response.setFirstName(request.getFirstName());
		response.setMiddleName(request.getMiddleName());
		response.setLastName(request.getLastName());
		response.setSpouseName(request.getSpouseName());
		response.setMaritalStatus(request.getMaritalStatus());
		response.setMemberPan(request.getMemberPan());
		response.setLandlineNo(request.getLandlineNo());
		response.setMobileNo(request.getMobileNo());
		response.setIsZeroid(request.getIsZeroid());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		Set<MemberAddressDto> memberAddressList = new HashSet<>();
		for (MemberAddressTempEntity dto : request.getMemberAddress()) {
			MemberAddressDto entity = new MemberAddressDto();
			entity.setAddressId(dto.getAddressId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setAddress1(dto.getAddress1());
			entity.setAddress2(dto.getAddress2());
			entity.setAddress3(dto.getAddress3());
			entity.setAddressType(dto.getAddressType());
			entity.setCity(dto.getCity());
			entity.setDistrict(dto.getDistrict());
			entity.setStateName(dto.getStateName());
			entity.setCountry(dto.getCountry());
			entity.setPincode(dto.getPincode());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberAddressList.add(entity);
		}
		response.setMemberAddress(memberAddressList);
		Set<MemberBankDto> memberBankList = new HashSet<>();
		for (MemberBankTempEntity dto : request.getMemberBank()) {
			MemberBankDto entity = new MemberBankDto();
			entity.setMemberBankId(dto.getMemberBankId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setConfirmAccountNumber(dto.getConfirmAccountNumber());
			entity.setAccountType(dto.getAccountType());
			entity.setIfscCodeAvailable(dto.getIfscCodeAvailable());
			entity.setIfscCode(dto.getIfscCode());
			entity.setBankName(dto.getBankName());
			entity.setBankBranch(dto.getBankBranch());
			entity.setBankAddress(dto.getBankAddress());
			entity.setCountryCode(dto.getCountryCode());
			entity.setStdCode(dto.getStdCode());
			entity.setLandlineNumber(dto.getLandlineNumber());
			entity.setEmailId(dto.getEmailId());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberBankList.add(entity);
		}
		response.setMemberBank(memberBankList);
		Set<MemberNomineeDto> memberNomineeList = new HashSet<>();
		for (MemberNomineeTempEntity dto : request.getMemberNominee()) {
			MemberNomineeDto entity = new MemberNomineeDto();
			entity.setNomineeId(dto.getNomineeId());
			entity.setMemberId(dto.getMemberId());
			entity.setNomineeType(dto.getNomineeType());
			entity.setNomineeName(dto.getNomineeName());
			entity.setRelationShip(dto.getRelationShip());
			entity.setAadharNumber(dto.getAadharNumber());
			entity.setPhone(dto.getPhone());
			entity.setEmailId(dto.getEmailId());
			entity.setAddressOne(dto.getAddressOne());
			entity.setAddressTwo(dto.getAddressTwo());
			entity.setAddressThree(dto.getAddressThree());
			entity.setCountry(dto.getCountry());
			entity.setPinCode(dto.getPinCode());
			entity.setDistrict(dto.getDistrict());
			entity.setState(dto.getState());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setPercentage(dto.getPercentage());
			entity.setAge(dto.getAge());
			entity.setDateOfBirth(dto.getDateOfBirth());
			memberNomineeList.add(entity);
		}
		response.setMemberNominee(memberNomineeList);
		Set<MemberAppointeeDto> memberAppointeeList = new HashSet<>();
		for (MemberAppointeeTempEntity dto : request.getMemberAppointee()) {
			MemberAppointeeDto entity = new MemberAppointeeDto();
			entity.setAppointeeId(dto.getAppointeeId());
			entity.setMemberId(dto.getMemberId());
			entity.setAppointeeName(dto.getAppointeeName());
			entity.setAppointeeCode(dto.getAppointeeCode());
			entity.setRelationship(dto.getRelationship());
			entity.setContactNumber(dto.getContactNumber());
			entity.setDateOfBirth(dto.getDateOfBirth());
			entity.setPan(dto.getPan());
			entity.setAadharNumber(dto.getAadharNumber());
			entity.setIfscCode(dto.getIfscCode());
			entity.setBankName(dto.getBankName());
			entity.setAccountType(dto.getAccountType());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberAppointeeList.add(entity);
		}
		response.setMemberAppointee(memberAppointeeList);
		Set<MemberContributionDto> memberContributionList = new HashSet<>();
		for (MemberContributionTempEntity dto : request.getMemberContribution()) {
			MemberContributionDto entity = new MemberContributionDto();
			entity.setMemberConId(dto.getMemberConId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyConId(dto.getPolicyConId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setContributionType(dto.getContributionType());
			entity.setContributionDate(dto.getContributionDate());
			entity.setLicId(dto.getLicId());
			entity.setOpeningBalance(dto.getOpeningBalance());
			entity.setEmployeeContribution(dto.getEmployeeContribution());
			entity.setEmployerContribution(dto.getEmployerContribution());
			entity.setVoluntaryContribution(dto.getVoluntaryContribution());
			entity.setTotalContribution(dto.getTotalContribution());
			entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
			entity.setClosingBalance(dto.getClosingBalance());
//			entity.setIsZeroAccount(dto.getIsZeroAccount());
			entity.setIsDeposit(dto.getIsDeposit());
			entity.setFinancialYear(dto.getFinancialYear());
			entity.setQuarter(dto.getQuarter());
			entity.setVersionNo(dto.getVersionNo());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setIsActive(dto.getIsActive());
			memberContributionList.add(entity);
		}
		response.setMemberContribution(memberContributionList);
		Set<MemberContributionSummaryDto> memberContributionSummaryList = new HashSet<>();
		for (MemberContributionSummaryTempEntity dto : request.getMemberContributionSummary()) {
			MemberContributionSummaryDto entity = new MemberContributionSummaryDto();
			entity.setMemContSummaryId(dto.getMemContSummaryId());

			entity.setMemberId(dto.getMemberId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setLicId(dto.getLicId());
			entity.setTotalEmployeeContribution(dto.getTotalEmployeeContribution());
			entity.setTotalEmployerContribution(dto.getTotalEmployerContribution());
			entity.setTotalVoluntaryContribution(dto.getTotalVoluntaryContribution());
			entity.setOpeningBalance(dto.getOpeningBalance());
			entity.setTotalContribution(dto.getTotalContribution());
			entity.setClosingBalance(dto.getClosingBalance());

			entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());

			entity.setFinancialYear(dto.getFinancialYear());
			entity.setQuarter(dto.getQuarter());

			entity.setCreatedOn(dto.getCreatedOn());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setIsActive(dto.getIsActive());
			memberContributionSummaryList.add(entity);
		}
		response.setMemberContributionSummary(memberContributionSummaryList);
		return response;
	}

//  ENTITY TO DTO
	public MphMasterEntity convertMphMasterDtoToMphMasterEntity(MphMasterDto masterDto) {
		MphMasterEntity response = new MphMasterEntity();

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
		Set<MphAddressEntity> mphAddress = new HashSet<>();
		for (MphAddressDto mphAddressDto : masterDto.getMphAddress()) {
			MphAddressEntity mphAddressEntity = new MphAddressEntity();
			mphAddressEntity.setAddressId(mphAddressDto.getAddressId());
			mphAddressEntity.setMphId(mphAddressDto.getMphId());
			mphAddressEntity.setAddressType(mphAddressDto.getAddressType());
			mphAddressEntity.setAddressLine1(mphAddressDto.getAddressLine1());
			mphAddressEntity.setAddressLine2(mphAddressDto.getAddressLine2());
			mphAddressEntity.setAddressLine3(mphAddressDto.getAddressLine3());
			mphAddressEntity.setPincode(mphAddressDto.getPincode());
			mphAddressEntity.setCityLocality(mphAddressDto.getCityLocality());
			mphAddressEntity.setCityId(mphAddressDto.getCityId());
			mphAddressEntity.setDistrict(mphAddressDto.getDistrict());
			mphAddressEntity.setDistrictId(mphAddressDto.getDistrictId());
			mphAddressEntity.setCountryId(mphAddressDto.getCountryId());
			mphAddressEntity.setStateId(mphAddressDto.getStateId());
			mphAddressEntity.setStateName(mphAddressDto.getStateName());
			mphAddressEntity.setIsActive(mphAddressDto.getIsActive());
			mphAddressEntity.setCreatedOn(mphAddressDto.getCreatedOn());
			mphAddressEntity.setCreatedBy(mphAddressDto.getCreatedBy());
			mphAddressEntity.setModifiedOn(mphAddressDto.getModifiedOn());
			mphAddressEntity.setModifiedBy(mphAddressDto.getModifiedBy());
			mphAddress.add(mphAddressEntity);
		}
		response.setMphAddress(mphAddress);
		Set<MphBankEntity> mphBank = new HashSet<>();
		for (MphBankDto mphBankDto : masterDto.getMphBank()) {
			MphBankEntity mphBankEntity = new MphBankEntity();
			mphBankEntity.setMphBankId(mphBankDto.getMphBankId());
			mphBankEntity.setMphId(mphBankDto.getMphId());
			mphBankEntity.setAccountNumber(mphBankDto.getAccountNumber());
			mphBankEntity.setConfirmAccountNumber(mphBankDto.getConfirmAccountNumber());
			mphBankEntity.setAccountType(mphBankDto.getAccountType());
			mphBankEntity.setIfscCodeAvailable(mphBankDto.getIfscCodeAvailable());
			mphBankEntity.setIfscCode(mphBankDto.getIfscCode());
			mphBankEntity.setBankName(mphBankDto.getBankName());
			mphBankEntity.setBankBranch(mphBankDto.getBankBranch());
			mphBankEntity.setBankAddress(mphBankDto.getBankAddress());
			mphBankEntity.setStdCode(mphBankDto.getStdCode());
			mphBankEntity.setLandlineNumber(mphBankDto.getLandlineNumber());
			mphBankEntity.setEmailId(mphBankDto.getEmailId());
			mphBankEntity.setCountryCode(mphBankDto.getCountryCode());
			mphBankEntity.setCountryId(mphBankDto.getCountryId());
			mphBankEntity.setStateId(mphBankDto.getStateId());
			mphBankEntity.setDistrictId(mphBankDto.getDistrictId());
			mphBankEntity.setCityId(mphBankDto.getCityId());
			mphBankEntity.setTownLocality(mphBankDto.getTownLocality());
			mphBankEntity.setIsActive(mphBankDto.getIsActive());
			mphBankEntity.setCreatedBy(mphBankDto.getCreatedBy());
			mphBankEntity.setCreatedOn(mphBankDto.getCreatedOn());
			mphBankEntity.setModifiedOn(mphBankDto.getModifiedOn());
			mphBankEntity.setModifiedBy(mphBankDto.getModifiedBy());
			mphBank.add(mphBankEntity);
		}
		response.setMphBank(mphBank);
		Set<MphRepresentativesEntity> mphRepresentative = new HashSet<>();
		for (MphRepresentativesDto mphRepresentativeDto : masterDto.getMphRepresentative()) {
			MphRepresentativesEntity mphRepresentativesEntity = new MphRepresentativesEntity();
			mphRepresentativesEntity.setRepId(mphRepresentativeDto.getRepId());
			mphRepresentativesEntity.setMphId(mphRepresentativeDto.getMphId());
			mphRepresentativesEntity.setRepresentativeCode(mphRepresentativeDto.getRepresentativeCode());
			mphRepresentativesEntity.setRepresentativeName(mphRepresentativeDto.getRepresentativeName());
			mphRepresentativesEntity.setLandlineNo(mphRepresentativeDto.getLandlineNo());
			mphRepresentativesEntity.setCountryCode(mphRepresentativeDto.getCountryCode());
			mphRepresentativesEntity.setMobileNo(mphRepresentativeDto.getMobileNo());
			mphRepresentativesEntity.setEmailId(mphRepresentativeDto.getEmailId());
			mphRepresentativesEntity.setAddressType(mphRepresentativeDto.getAddressType());
			mphRepresentativesEntity.setAddressLine1(mphRepresentativeDto.getAddressLine1());
			mphRepresentativesEntity.setAddressLine2(mphRepresentativeDto.getAddressLine2());
			mphRepresentativesEntity.setAddressLine3(mphRepresentativeDto.getAddressLine3());
			mphRepresentativesEntity.setCityLocality(mphRepresentativeDto.getCityLocality());
			mphRepresentativesEntity.setDistrict(mphRepresentativeDto.getDistrict());
			mphRepresentativesEntity.setStateName(mphRepresentativeDto.getStateName());
			mphRepresentativesEntity.setPincode(mphRepresentativeDto.getPincode());
			mphRepresentativesEntity.setIsActive(mphRepresentativeDto.getIsActive());
			mphRepresentativesEntity.setCreatedOn(mphRepresentativeDto.getCreatedOn());
			mphRepresentativesEntity.setCreatedBy(mphRepresentativeDto.getCreatedBy());
			mphRepresentativesEntity.setModifiedOn(mphRepresentativeDto.getModifiedOn());
			mphRepresentativesEntity.setModifiedBy(mphRepresentativeDto.getModifiedBy());
			mphRepresentative.add(mphRepresentativesEntity);
		}
		response.setMphRepresentative(mphRepresentative);
		Set<PolicyMasterEntity> policyMaster = new HashSet<>();
		for (PolicyMasterDto policyDto : masterDto.getPolicyMaster()) {
			PolicyMasterEntity policyMasterEntity = convertPolicyMasterDtoToPolicyMasterEntity(policyDto);
			policyMaster.add(policyMasterEntity);
		}
		response.setPolicyMaster(policyMaster);
		return response;
	}

	public PolicyMasterEntity convertPolicyMasterDtoToPolicyMasterEntity(PolicyMasterDto policyMasterDto) {
		PolicyMasterEntity response = new PolicyMasterEntity();
		response.setPolicyId(policyMasterDto.getPolicyId());
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
		Set<PolicyValuationEntity> valuations = new HashSet<>();
		for (PolicyValuationDto policyValuationDto : policyMasterDto.getValuations()) {
			PolicyValuationEntity policyValuationEntity = new PolicyValuationEntity();
			policyValuationEntity.setValuationId(policyValuationDto.getValuationId());
			policyValuationEntity.setPolicyId(policyValuationDto.getPolicyId());
			policyValuationEntity.setValuationType(policyValuationDto.getValuationType());
			policyValuationEntity.setAttritionRate(policyValuationDto.getAttritionRate());
			policyValuationEntity.setSalaryEscalation(policyValuationDto.getSalaryEscalation());
			policyValuationEntity.setDeathRate(policyValuationDto.getDeathRate());
			policyValuationEntity.setDisRateIntrest(policyValuationDto.getDisRateIntrest());
			policyValuationEntity.setDisRateSalaryEsc(policyValuationDto.getDisRateSalaryEsc());
			policyValuationEntity.setAnnuityOption(policyValuationDto.getAnnuityOption());
			policyValuationEntity.setAccuralRateFactor(policyValuationDto.getAccuralRateFactor());
			policyValuationEntity.setMinPension(policyValuationDto.getMinPension());
			policyValuationEntity.setMaxPension(policyValuationDto.getMaxPension());
			policyValuationEntity.setWithdrawalRate(policyValuationDto.getWithdrawalRate());
			policyValuationEntity.setIsActive(policyValuationDto.getIsActive());
			policyValuationEntity.setCreatedOn(policyValuationDto.getCreatedOn());
			policyValuationEntity.setCreatedBy(policyValuationDto.getCreatedBy());
			policyValuationEntity.setModifiedOn(policyValuationDto.getModifiedOn());
			policyValuationEntity.setModifiedBy(policyValuationDto.getModifiedBy());
			valuations.add(policyValuationEntity);
		}
		response.setValuations(valuations);
		Set<PolicyRulesEntity> rules = new HashSet<>();
		for (PolicyRulesDto policyRulesDto : policyMasterDto.getRules()) {
			PolicyRulesEntity policyAnnuityEntity = new PolicyRulesEntity();
			policyAnnuityEntity.setRuleId(policyRulesDto.getRuleId());
			policyAnnuityEntity.setPolicyId(policyRulesDto.getPolicyId());
			policyAnnuityEntity.setEffectiveFrom(policyRulesDto.getEffectiveFrom());
			policyAnnuityEntity.setPercentageCorpus(policyRulesDto.getPercentageCorpus());
			policyAnnuityEntity.setCategory(policyRulesDto.getCategory());
			policyAnnuityEntity.setContributionType(policyRulesDto.getContributionType());
			policyAnnuityEntity.setBenifitPayableTo(policyRulesDto.getBenifitPayableTo());
			policyAnnuityEntity.setAnnuityOption(policyRulesDto.getAnnuityOption());
			policyAnnuityEntity.setCommutationBy(policyRulesDto.getCommutationBy());
			policyAnnuityEntity.setCommutationAmt(policyRulesDto.getCommutationAmt());
			policyAnnuityEntity.setNormalAgeRetirement(policyRulesDto.getNormalAgeRetirement());
			policyAnnuityEntity.setMinAgeRetirement(policyRulesDto.getMinAgeRetirement());
			policyAnnuityEntity.setMaxAgeRetirement(policyRulesDto.getMaxAgeRetirement());
			policyAnnuityEntity.setMinServicePension(policyRulesDto.getMinServicePension());
			policyAnnuityEntity.setMaxServicePension(policyRulesDto.getMaxServicePension());
			policyAnnuityEntity.setMinServiceWithdrawal(policyRulesDto.getMinServiceWithdrawal());
			policyAnnuityEntity.setMaxServiceWithdrawal(policyRulesDto.getMaxServiceWithdrawal());
			policyAnnuityEntity.setMinPension(policyRulesDto.getMinPension());
			policyAnnuityEntity.setMaxPension(policyRulesDto.getMaxPension());
			policyAnnuityEntity.setModifiedBy(policyRulesDto.getModifiedBy());
			policyAnnuityEntity.setModifiedOn(policyRulesDto.getModifiedOn());
			policyAnnuityEntity.setCreatedBy(policyRulesDto.getCreatedBy());
			policyAnnuityEntity.setCreatedOn(policyRulesDto.getCreatedOn());
			policyAnnuityEntity.setIsActive(policyRulesDto.getIsActive());
			rules.add(policyAnnuityEntity);
		}
		response.setRules(rules);
		Set<PolicyDepositEntity> deposits = new HashSet<>();
		for (PolicyDepositDto policyDepositDto : policyMasterDto.getDeposits()) {
			PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();
			policyDepositEntity.setDepositId(policyDepositDto.getDepositId());
			policyDepositEntity.setPolicyId(policyDepositDto.getPolicyId());
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
		Set<PolicyContributionEntity> policyContributions = new HashSet<>();
		for (PolicyContributionDto policyContributionDto : policyMasterDto.getPolicyContributions()) {
			PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();
			policyContributionEntity.setContributionId(policyContributionDto.getContributionId());
			policyContributionEntity.setPolicyId(policyContributionDto.getPolicyId());
			policyContributionEntity.setContReferenceNo(policyContributionDto.getContReferenceNo());
			policyContributionEntity.setContributionType(policyContributionDto.getContributionType());
			policyContributionEntity.setContributionDate(policyContributionDto.getContributionDate());
			policyContributionEntity.setOpeningBalance(policyContributionDto.getOpeningBalance());
			policyContributionEntity.setClosingBalance(policyContributionDto.getClosingBalance());
			policyContributionEntity.setEmployerContribution(policyContributionDto.getEmployerContribution());
			policyContributionEntity.setEmployeeContribution(policyContributionDto.getEmployeeContribution());
			policyContributionEntity.setVoluntaryContribution(policyContributionDto.getVoluntaryContribution());
			policyContributionEntity.setTotalContribution(policyContributionDto.getTotalContribution());
			policyContributionEntity.setIsDeposit(policyContributionDto.getIsDeposit());
			policyContributionEntity.setFinancialYear(policyContributionDto.getFinancialYear());
			policyContributionEntity.setQuarter(policyContributionDto.getQuarter());
			policyContributionEntity.setVersionNo(policyContributionDto.getVersionNo());
			policyContributionEntity.setCreatedOn(policyContributionDto.getCreatedOn());
			policyContributionEntity.setCreatedBy(policyContributionDto.getCreatedBy());
			policyContributionEntity.setModifiedOn(policyContributionDto.getModifiedOn());
			policyContributionEntity.setModifiedBy(policyContributionDto.getModifiedBy());
			policyContributionEntity.setIsActive(policyContributionDto.getIsActive());
			policyContributions.add(policyContributionEntity);
		}
		response.setPolicyContributions(policyContributions);
		Set<PolicyContributionSummaryEntity> policyContributionSummary = new HashSet<>();
		for (PolicyContributionSummaryDto policyContributionSummaryDto : policyMasterDto
				.getPolicyContributionSummary()) {
			PolicyContributionSummaryEntity policyContributionSummaryEntity = new PolicyContributionSummaryEntity();
			policyContributionSummaryEntity.setPolContSummaryId(policyContributionSummaryDto.getPolContSummaryId());
			policyContributionSummaryEntity.setPolicyId(policyContributionSummaryDto.getPolicyId());
			policyContributionSummaryEntity.setStampDuty(policyContributionSummaryDto.getStampDuty());
			policyContributionSummaryEntity.setOpeningBalance(policyContributionSummaryDto.getOpeningBalance());
			policyContributionSummaryEntity
					.setTotalEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
			policyContributionSummaryEntity
					.setTotalEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
			policyContributionSummaryEntity
					.setTotalVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
			policyContributionSummaryEntity.setTotalContribution(policyContributionSummaryDto.getTotalContribution());
			policyContributionSummaryEntity.setClosingBalance(policyContributionSummaryDto.getClosingBalance());
			policyContributionSummaryEntity.setFinancialYear(policyContributionSummaryDto.getFinancialYear());
			policyContributionSummaryEntity.setQuarter(policyContributionSummaryDto.getQuarter());
			policyContributionSummaryEntity.setIsActive(policyContributionSummaryDto.getIsActive());
			policyContributionSummaryEntity.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
			policyContributionSummaryEntity.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
			policyContributionSummaryEntity.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
			policyContributionSummaryEntity.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
			policyContributionSummary.add(policyContributionSummaryEntity);
		}
		response.setPolicyContributionSummary(policyContributionSummary);
		Set<ZeroAccountEntity> zeroAccount = new HashSet<>();
		for (ZeroAccountdto zeroAccountdto : policyMasterDto.getZeroAccount()) {
			ZeroAccountEntity zeroAccountEntity = new ZeroAccountEntity();
			zeroAccountEntity.setZeroAccId(zeroAccountdto.getZeroAccId());
			zeroAccountEntity.setPolicyId(zeroAccountdto.getPolicyId());
			zeroAccountEntity.setZeroIdAmount(zeroAccountdto.getZeroIdAmount());
			zeroAccountEntity.setIsActive(zeroAccountdto.getIsActive());
			zeroAccountEntity.setCreatedOn(zeroAccountdto.getCreatedOn());
			zeroAccountEntity.setCreatedBy(zeroAccountdto.getCreatedBy());
			zeroAccountEntity.setModifiedOn(zeroAccountdto.getModifiedOn());
			zeroAccountEntity.setModifiedBy(zeroAccountdto.getModifiedBy());
			zeroAccount.add(zeroAccountEntity);
		}
		response.setZeroAccount(zeroAccount);
		Set<ZeroAccountEntriesEntity> zeroAccountEntries = new HashSet<>();
		for (ZeroAccountEntriesDto zeroAccountEntriesDto : policyMasterDto.getZeroAccountEntries()) {
			ZeroAccountEntriesEntity zeroAccountEntriesEntity = new ZeroAccountEntriesEntity();
			zeroAccountEntriesEntity.setZeroAccEntId(zeroAccountEntriesDto.getZeroAccEntId());
			zeroAccountEntriesEntity.setPolicyId(zeroAccountEntriesDto.getPolicyId());
			zeroAccountEntriesEntity.setPolicyConId(zeroAccountEntriesDto.getPolicyConId());
			zeroAccountEntriesEntity.setMemberConId(zeroAccountEntriesDto.getMemberConId());
			zeroAccountEntriesEntity.setTransactionType(zeroAccountEntriesDto.getTransactionType());
			zeroAccountEntriesEntity.setTransactionDate(zeroAccountEntriesDto.getTransactionDate());
			zeroAccountEntriesEntity.setZeroIdAmount(zeroAccountEntriesDto.getZeroIdAmount());
			zeroAccountEntriesEntity.setCreatedOn(zeroAccountEntriesDto.getCreatedOn());
			zeroAccountEntriesEntity.setCreatedBy(zeroAccountEntriesDto.getCreatedBy());
			zeroAccountEntriesEntity.setModifiedOn(zeroAccountEntriesDto.getModifiedOn());
			zeroAccountEntriesEntity.setModifiedBy(zeroAccountEntriesDto.getModifiedBy());
			zeroAccountEntriesEntity.setIsActive(zeroAccountEntriesDto.getIsActive());
			zeroAccountEntries.add(zeroAccountEntriesEntity);
		}
		response.setZeroAccountEntries(zeroAccountEntries);
		Set<PolicyNotesEntity> notes = new HashSet<>();
		for (PolicyNotesDto policyNotesDto : policyMasterDto.getNotes()) {
			PolicyNotesEntity policyNotesEntity = new PolicyNotesEntity();
			policyNotesEntity.setPolicyNoteId(policyNotesDto.getPolicyNoteId());
			policyNotesEntity.setPolicyId(policyNotesDto.getPolicyId());
			policyNotesEntity.setNoteContents(policyNotesDto.getNoteContents());
			policyNotesEntity.setModifiedBy(policyNotesDto.getModifiedBy());
			policyNotesEntity.setModifiedOn(policyNotesDto.getModifiedOn());
			policyNotesEntity.setCreatedBy(policyNotesDto.getCreatedBy());
			policyNotesEntity.setCreatedOn(policyNotesDto.getCreatedOn());
			policyNotesEntity.setIsActive(policyNotesDto.getIsActive());
			notes.add(policyNotesEntity);
		}
		response.setNotes(notes);
		Set<MemberMasterEntity> memberMasterEntityList = new HashSet<>();
		for (MemberMasterDto policyValuationDto : policyMasterDto.getMemberMaster()) {
			MemberMasterEntity memberMasterEntity = convertMemberDtoToMemberEntity(policyValuationDto);
			memberMasterEntityList.add(memberMasterEntity);
		}
		response.setMemberMaster(memberMasterEntityList);
		return response;
	}

	public MemberMasterEntity convertMemberDtoToMemberEntity(MemberMasterDto request) {
		MemberMasterEntity response = new MemberMasterEntity();
		response.setMemberId(request.getMemberId());
		response.setPolicyId(request.getPolicyId());
		response.setMembershipNumber(request.getMembershipNumber());
		response.setMemberStatus(request.getMemberStatus());
		response.setLicId(request.getLicId());
		response.setTypeOfMemebership(request.getTypeOfMemebership());
		response.setAadharNumber(request.getAadharNumber());
		response.setCategoryNo(request.getCategoryNo());
		response.setCommunicationPreference(request.getCommunicationPreference());
		response.setLanguagePreference(request.getLanguagePreference());
		response.setDateOfBirth(request.getDateOfBirth());
		response.setDateOfJoining(request.getDateOfJoining());
		response.setDateOfJoiningScheme(request.getDateOfJoiningScheme());
		response.setDateOfRetrirement(request.getDateOfRetrirement());
		response.setDesignation(request.getDesignation());
		response.setEmailid(request.getEmailid());
		response.setGender(request.getGender());
		response.setFatherName(request.getFatherName());
		response.setFirstName(request.getFirstName());
		response.setMiddleName(request.getMiddleName());
		response.setLastName(request.getLastName());
		response.setSpouseName(request.getSpouseName());
		response.setMaritalStatus(request.getMaritalStatus());
		response.setMemberPan(request.getMemberPan());
		response.setLandlineNo(request.getLandlineNo());
		response.setMobileNo(request.getMobileNo());
		response.setIsZeroid(request.getIsZeroid());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		Set<MemberAddressEntity> memberAddressList = new HashSet<>();
		for (MemberAddressDto dto : request.getMemberAddress()) {
			MemberAddressEntity entity = new MemberAddressEntity();
			entity.setAddressId(dto.getAddressId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setAddress1(dto.getAddress1());
			entity.setAddress2(dto.getAddress2());
			entity.setAddress3(dto.getAddress3());
			entity.setAddressType(dto.getAddressType());
			entity.setCity(dto.getCity());
			entity.setDistrict(dto.getDistrict());
			entity.setStateName(dto.getStateName());
			entity.setCountry(dto.getCountry());
			entity.setPincode(dto.getPincode());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberAddressList.add(entity);
		}
		response.setMemberAddress(memberAddressList);
		Set<MemberBankEntity> memberBankList = new HashSet<>();
		for (MemberBankDto dto : request.getMemberBank()) {
			MemberBankEntity entity = new MemberBankEntity();
			entity.setMemberBankId(dto.getMemberBankId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setConfirmAccountNumber(dto.getConfirmAccountNumber());
			entity.setAccountType(dto.getAccountType());
			entity.setIfscCodeAvailable(dto.getIfscCodeAvailable());
			entity.setIfscCode(dto.getIfscCode());
			entity.setBankName(dto.getBankName());
			entity.setBankBranch(dto.getBankBranch());
			entity.setBankAddress(dto.getBankAddress());
			entity.setCountryCode(dto.getCountryCode());
			entity.setStdCode(dto.getStdCode());
			entity.setLandlineNumber(dto.getLandlineNumber());
			entity.setEmailId(dto.getEmailId());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberBankList.add(entity);
		}
		response.setMemberBank(memberBankList);
		Set<MemberNomineeEntity> memberNomineeList = new HashSet<>();
		for (MemberNomineeDto dto : request.getMemberNominee()) {
			MemberNomineeEntity entity = new MemberNomineeEntity();
			entity.setNomineeId(dto.getNomineeId());
			entity.setMemberId(dto.getMemberId());
			entity.setNomineeType(dto.getNomineeType());
			entity.setNomineeName(dto.getNomineeName());
			entity.setRelationShip(dto.getRelationShip());
			entity.setAadharNumber(dto.getAadharNumber());
			entity.setPhone(dto.getPhone());
			entity.setEmailId(dto.getEmailId());
			entity.setAddressOne(dto.getAddressOne());
			entity.setAddressTwo(dto.getAddressTwo());
			entity.setAddressThree(dto.getAddressThree());
			entity.setCountry(dto.getCountry());
			entity.setPinCode(dto.getPinCode());
			entity.setDistrict(dto.getDistrict());
			entity.setState(dto.getState());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setPercentage(dto.getPercentage());
			entity.setAge(dto.getAge());
			entity.setDateOfBirth(dto.getDateOfBirth());
			memberNomineeList.add(entity);
		}
		response.setMemberNominee(memberNomineeList);
		Set<MemberAppointeeEntity> memberAppointeeList = new HashSet<>();
		for (MemberAppointeeDto dto : request.getMemberAppointee()) {
			MemberAppointeeEntity entity = new MemberAppointeeEntity();
			entity.setAppointeeId(dto.getAppointeeId());
			entity.setMemberId(dto.getMemberId());
			entity.setAppointeeName(dto.getAppointeeName());
			entity.setAppointeeCode(dto.getAppointeeCode());
			entity.setRelationship(dto.getRelationship());
			entity.setContactNumber(dto.getContactNumber());
			entity.setDateOfBirth(dto.getDateOfBirth());
			entity.setPan(dto.getPan());
			entity.setAadharNumber(dto.getAadharNumber());
			entity.setIfscCode(dto.getIfscCode());
			entity.setBankName(dto.getBankName());
			entity.setAccountType(dto.getAccountType());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberAppointeeList.add(entity);
		}
		response.setMemberAppointee(memberAppointeeList);
		Set<MemberContributionEntity> memberContributionList = new HashSet<>();
		for (MemberContributionDto dto : request.getMemberContribution()) {
			MemberContributionEntity entity = new MemberContributionEntity();
			entity.setMemberConId(dto.getMemberConId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyConId(dto.getPolicyConId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setContributionType(dto.getContributionType());
			entity.setContributionDate(dto.getContributionDate());
			entity.setLicId(dto.getLicId());
			entity.setOpeningBalance(dto.getOpeningBalance());
			entity.setEmployeeContribution(dto.getEmployeeContribution());
			entity.setEmployerContribution(dto.getEmployerContribution());
			entity.setVoluntaryContribution(dto.getVoluntaryContribution());
			entity.setTotalContribution(dto.getTotalContribution());
			entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
			entity.setClosingBalance(dto.getClosingBalance());
//			entity.setIsZeroAccount(dto.getIsZeroAccount());
			entity.setIsDeposit(dto.getIsDeposit());
			entity.setFinancialYear(dto.getFinancialYear());
			entity.setQuarter(dto.getQuarter());
			entity.setVersionNo(dto.getVersionNo());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setIsActive(dto.getIsActive());
			memberContributionList.add(entity);
		}
		response.setMemberContribution(memberContributionList);
		Set<MemberContributionSummaryEntity> memberContributionSummaryList = new HashSet<>();
		for (MemberContributionSummaryDto dto : request.getMemberContributionSummary()) {
			MemberContributionSummaryEntity entity = new MemberContributionSummaryEntity();
			entity.setMemContSummaryId(dto.getMemContSummaryId());
			entity.setMemberId(dto.getMemberId());
			entity.setPolicyId(dto.getPolicyId());
			entity.setLicId(dto.getLicId());
			entity.setOpeningBalance(dto.getOpeningBalance());
			entity.setTotalEmployeeContribution(dto.getTotalEmployeeContribution());
			entity.setClosingBalance(dto.getClosingBalance());
			entity.setTotalEmployerContribution(dto.getTotalEmployerContribution());
			entity.setTotalVoluntaryContribution(dto.getTotalVoluntaryContribution());
			entity.setTotalContribution(dto.getTotalContribution());
			entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
			entity.setFinancialYear(dto.getFinancialYear());
			entity.setQuarter(dto.getQuarter());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setIsActive(dto.getIsActive());
			memberContributionSummaryList.add(entity);
		}
		response.setMemberContributionSummary(memberContributionSummaryList);
		return response;
	}

//  DTO TO ENTITY
	public MphMasterDto convertMphMasterEntityToMphMasterDto(MphMasterEntity masterDto) {

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
		Set<MphAddressDto> mphAddress = new HashSet<>();
		for (MphAddressEntity mphAddressDto : masterDto.getMphAddress()) {
			if (mphAddressDto != null) {
				MphAddressDto mphAddressEntity = new MphAddressDto();
				mphAddressEntity.setAddressId(mphAddressDto.getAddressId());
				mphAddressEntity.setMphId(mphAddressDto.getMphId());
				mphAddressEntity.setAddressType(mphAddressDto.getAddressType());
				mphAddressEntity.setAddressLine1(mphAddressDto.getAddressLine1());
				mphAddressEntity.setAddressLine2(mphAddressDto.getAddressLine2());
				mphAddressEntity.setAddressLine3(mphAddressDto.getAddressLine3());
				mphAddressEntity.setPincode(mphAddressDto.getPincode());
				mphAddressEntity.setCityLocality(mphAddressDto.getCityLocality());
				mphAddressEntity.setCityId(mphAddressDto.getCityId());
				mphAddressEntity.setDistrict(mphAddressDto.getDistrict());
				mphAddressEntity.setDistrictId(mphAddressDto.getDistrictId());
				mphAddressEntity.setCountryId(mphAddressDto.getCountryId());
				mphAddressEntity.setStateId(mphAddressDto.getStateId());
				mphAddressEntity.setStateName(mphAddressDto.getStateName());
				mphAddressEntity.setIsActive(mphAddressDto.getIsActive());
				mphAddressEntity.setCreatedOn(mphAddressDto.getCreatedOn());
				mphAddressEntity.setCreatedBy(mphAddressDto.getCreatedBy());
				mphAddressEntity.setModifiedOn(mphAddressDto.getModifiedOn());
				mphAddressEntity.setModifiedBy(mphAddressDto.getModifiedBy());
				mphAddress.add(mphAddressEntity);
			}
		}
		response.setMphAddress(mphAddress);
		Set<MphBankDto> mphBank = new HashSet<>();
		for (MphBankEntity mphBankDto : masterDto.getMphBank()) {
			if (mphBankDto != null) {
				MphBankDto mphBankEntity = new MphBankDto();
				mphBankEntity.setMphBankId(mphBankDto.getMphBankId());
				mphBankEntity.setMphId(mphBankDto.getMphId());
				mphBankEntity.setAccountNumber(mphBankDto.getAccountNumber());
				mphBankEntity.setConfirmAccountNumber(mphBankDto.getConfirmAccountNumber());
				mphBankEntity.setAccountType(mphBankDto.getAccountType());
				mphBankEntity.setIfscCodeAvailable(mphBankDto.getIfscCodeAvailable());
				mphBankEntity.setIfscCode(mphBankDto.getIfscCode());
				mphBankEntity.setBankName(mphBankDto.getBankName());
				mphBankEntity.setBankBranch(mphBankDto.getBankBranch());
				mphBankEntity.setBankAddress(mphBankDto.getBankAddress());
				mphBankEntity.setStdCode(mphBankDto.getStdCode());
				mphBankEntity.setLandlineNumber(mphBankDto.getLandlineNumber());
				mphBankEntity.setEmailId(mphBankDto.getEmailId());
				mphBankEntity.setCountryCode(mphBankDto.getCountryCode());
				mphBankEntity.setCountryId(mphBankDto.getCountryId());
				mphBankEntity.setStateId(mphBankDto.getStateId());
				mphBankEntity.setDistrictId(mphBankDto.getDistrictId());
				mphBankEntity.setCityId(mphBankDto.getCityId());
				mphBankEntity.setTownLocality(mphBankDto.getTownLocality());
				mphBankEntity.setIsActive(mphBankDto.getIsActive());
				mphBankEntity.setCreatedBy(mphBankDto.getCreatedBy());
				mphBankEntity.setCreatedOn(mphBankDto.getCreatedOn());
				mphBankEntity.setModifiedOn(mphBankDto.getModifiedOn());
				mphBankEntity.setModifiedBy(mphBankDto.getModifiedBy());
				mphBank.add(mphBankEntity);
			}
		}
		response.setMphBank(mphBank);
		Set<MphRepresentativesDto> mphRepresentative = new HashSet<>();
		for (MphRepresentativesEntity mphRepresentativeDto : masterDto.getMphRepresentative()) {
			if (mphRepresentativeDto != null) {
				MphRepresentativesDto mphRepresentativesEntity = new MphRepresentativesDto();
				mphRepresentativesEntity.setRepId(mphRepresentativeDto.getRepId());
				mphRepresentativesEntity.setMphId(mphRepresentativeDto.getMphId());
				mphRepresentativesEntity.setRepresentativeCode(mphRepresentativeDto.getRepresentativeCode());
				mphRepresentativesEntity.setRepresentativeName(mphRepresentativeDto.getRepresentativeName());
				mphRepresentativesEntity.setLandlineNo(mphRepresentativeDto.getLandlineNo());
				mphRepresentativesEntity.setCountryCode(mphRepresentativeDto.getCountryCode());
				mphRepresentativesEntity.setMobileNo(mphRepresentativeDto.getMobileNo());
				mphRepresentativesEntity.setEmailId(mphRepresentativeDto.getEmailId());
				mphRepresentativesEntity.setAddressType(mphRepresentativeDto.getAddressType());
				mphRepresentativesEntity.setAddressLine1(mphRepresentativeDto.getAddressLine1());
				mphRepresentativesEntity.setAddressLine2(mphRepresentativeDto.getAddressLine2());
				mphRepresentativesEntity.setAddressLine3(mphRepresentativeDto.getAddressLine3());
				mphRepresentativesEntity.setCityLocality(mphRepresentativeDto.getCityLocality());
				mphRepresentativesEntity.setDistrict(mphRepresentativeDto.getDistrict());
				mphRepresentativesEntity.setStateName(mphRepresentativeDto.getStateName());
				mphRepresentativesEntity.setPincode(mphRepresentativeDto.getPincode());
				mphRepresentativesEntity.setIsActive(mphRepresentativeDto.getIsActive());
				mphRepresentativesEntity.setCreatedOn(mphRepresentativeDto.getCreatedOn());
				mphRepresentativesEntity.setCreatedBy(mphRepresentativeDto.getCreatedBy());
				mphRepresentativesEntity.setModifiedOn(mphRepresentativeDto.getModifiedOn());
				mphRepresentativesEntity.setModifiedBy(mphRepresentativeDto.getModifiedBy());
				mphRepresentative.add(mphRepresentativesEntity);
			}
		}
		response.setMphRepresentative(mphRepresentative);

		Set<PolicyMasterDto> policyMaster = new HashSet<>();
		for (PolicyMasterEntity policyDto : masterDto.getPolicyMaster()) {
			if (policyDto != null) {
				PolicyMasterDto policyMasterEntity = convertPolicyMasterEntityToPolicyMasterDto(policyDto);
				policyMaster.add(policyMasterEntity);
			}
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
		response.setStampDuty(policyMasterDto.getStampDuty());
		Set<PolicyValuationDto> valuations = new HashSet<>();
		for (PolicyValuationEntity policyValuationDto : policyMasterDto.getValuations()) {
			if (policyValuationDto != null) {
				PolicyValuationDto policyValuationEntity = new PolicyValuationDto();
				policyValuationEntity.setValuationId(policyValuationDto.getValuationId());
				policyValuationEntity.setPolicyId(policyValuationDto.getPolicyId());
				policyValuationEntity.setValuationType(policyValuationDto.getValuationType());
				policyValuationEntity.setAttritionRate(policyValuationDto.getAttritionRate());
				policyValuationEntity.setSalaryEscalation(policyValuationDto.getSalaryEscalation());
				policyValuationEntity.setDeathRate(policyValuationDto.getDeathRate());
				policyValuationEntity.setDisRateIntrest(policyValuationDto.getDisRateIntrest());
				policyValuationEntity.setDisRateSalaryEsc(policyValuationDto.getDisRateSalaryEsc());
				policyValuationEntity.setAnnuityOption(policyValuationDto.getAnnuityOption());
				policyValuationEntity.setAccuralRateFactor(policyValuationDto.getAccuralRateFactor());
				policyValuationEntity.setMinPension(policyValuationDto.getMinPension());
				policyValuationEntity.setMaxPension(policyValuationDto.getMaxPension());
				policyValuationEntity.setWithdrawalRate(policyValuationDto.getWithdrawalRate());
				policyValuationEntity.setIsActive(policyValuationDto.getIsActive());
				policyValuationEntity.setCreatedOn(policyValuationDto.getCreatedOn());
				policyValuationEntity.setCreatedBy(policyValuationDto.getCreatedBy());
				policyValuationEntity.setModifiedOn(policyValuationDto.getModifiedOn());
				policyValuationEntity.setModifiedBy(policyValuationDto.getModifiedBy());
				valuations.add(policyValuationEntity);
			}
		}
		response.setValuations(valuations);
		Set<PolicyRulesDto> rules = new HashSet<>();
		for (PolicyRulesEntity policyRulesDto : policyMasterDto.getRules()) {
			if (policyRulesDto != null) {
				PolicyRulesDto policyAnnuityEntity = new PolicyRulesDto();
				policyAnnuityEntity.setRuleId(policyRulesDto.getRuleId());
				policyAnnuityEntity.setPolicyId(policyRulesDto.getPolicyId());
				policyAnnuityEntity.setEffectiveFrom(policyRulesDto.getEffectiveFrom());
				policyAnnuityEntity.setPercentageCorpus(policyRulesDto.getPercentageCorpus());
				policyAnnuityEntity.setCategory(policyRulesDto.getCategory());
				policyAnnuityEntity.setContributionType(policyRulesDto.getContributionType());
				policyAnnuityEntity.setBenifitPayableTo(policyRulesDto.getBenifitPayableTo());
				policyAnnuityEntity.setAnnuityOption(policyRulesDto.getAnnuityOption());
				policyAnnuityEntity.setCommutationBy(policyRulesDto.getCommutationBy());
				policyAnnuityEntity.setCommutationAmt(policyRulesDto.getCommutationAmt());
				policyAnnuityEntity.setNormalAgeRetirement(policyRulesDto.getNormalAgeRetirement());
				policyAnnuityEntity.setMinAgeRetirement(policyRulesDto.getMinAgeRetirement());
				policyAnnuityEntity.setMaxAgeRetirement(policyRulesDto.getMaxAgeRetirement());
				policyAnnuityEntity.setMinServicePension(policyRulesDto.getMinServicePension());
				policyAnnuityEntity.setMaxServicePension(policyRulesDto.getMaxServicePension());
				policyAnnuityEntity.setMinServiceWithdrawal(policyRulesDto.getMinServiceWithdrawal());
				policyAnnuityEntity.setMaxServiceWithdrawal(policyRulesDto.getMaxServiceWithdrawal());
				policyAnnuityEntity.setMinPension(policyRulesDto.getMinPension());
				policyAnnuityEntity.setMaxPension(policyRulesDto.getMaxPension());
				policyAnnuityEntity.setModifiedBy(policyRulesDto.getModifiedBy());
				policyAnnuityEntity.setModifiedOn(policyRulesDto.getModifiedOn());
				policyAnnuityEntity.setCreatedBy(policyRulesDto.getCreatedBy());
				policyAnnuityEntity.setCreatedOn(policyRulesDto.getCreatedOn());
				policyAnnuityEntity.setIsActive(policyRulesDto.getIsActive());
				rules.add(policyAnnuityEntity);
			}
		}
		response.setRules(rules);
		Set<PolicyDepositDto> deposits = new HashSet<>();
		for (PolicyDepositEntity policyDepositDto : policyMasterDto.getDeposits()) {
			if (policyDepositDto != null) {
				PolicyDepositDto policyDepositEntity = new PolicyDepositDto();
				policyDepositEntity.setDepositId(policyDepositDto.getDepositId());
				policyDepositEntity.setPolicyId(policyDepositDto.getPolicyId());
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
		}
		response.setDeposits(deposits);
		Set<PolicyContributionDto> policyContributions = new HashSet<>();
		for (PolicyContributionEntity policyContributionDto : policyMasterDto.getPolicyContributions()) {
			if (policyContributionDto != null) {
				PolicyContributionDto policyContributionEntity = new PolicyContributionDto();
				policyContributionEntity.setContributionId(policyContributionDto.getContributionId());
				policyContributionEntity.setPolicyId(policyContributionDto.getPolicyId());
				policyContributionEntity.setContReferenceNo(policyContributionDto.getContReferenceNo());
				policyContributionEntity.setContributionType(policyContributionDto.getContributionType());
				policyContributionEntity.setContributionDate(policyContributionDto.getContributionDate());
				policyContributionEntity.setOpeningBalance(policyContributionDto.getOpeningBalance());
				policyContributionEntity.setClosingBalance(policyContributionDto.getClosingBalance());
				policyContributionEntity.setEmployerContribution(policyContributionDto.getEmployerContribution());
				policyContributionEntity.setEmployeeContribution(policyContributionDto.getEmployeeContribution());
				policyContributionEntity.setVoluntaryContribution(policyContributionDto.getVoluntaryContribution());
				policyContributionEntity.setTotalContribution(policyContributionDto.getTotalContribution());
				policyContributionEntity.setIsDeposit(policyContributionDto.getIsDeposit());
				policyContributionEntity.setFinancialYear(policyContributionDto.getFinancialYear());
				policyContributionEntity.setQuarter(policyContributionDto.getQuarter());
				policyContributionEntity.setVersionNo(policyContributionDto.getVersionNo());
				policyContributionEntity.setCreatedOn(policyContributionDto.getCreatedOn());
				policyContributionEntity.setCreatedBy(policyContributionDto.getCreatedBy());
				policyContributionEntity.setModifiedOn(policyContributionDto.getModifiedOn());
				policyContributionEntity.setModifiedBy(policyContributionDto.getModifiedBy());
				policyContributionEntity.setIsActive(policyContributionDto.getIsActive());
				policyContributions.add(policyContributionEntity);
			}
		}
		response.setPolicyContributions(policyContributions);
		Set<PolicyContributionSummaryDto> policyContributionSummary = new HashSet<>();
		for (PolicyContributionSummaryEntity policyContributionSummaryDto : policyMasterDto
				.getPolicyContributionSummary()) {
			if (policyContributionSummaryDto != null) {
				PolicyContributionSummaryDto policyContributionSummaryEntity = new PolicyContributionSummaryDto();
				policyContributionSummaryEntity.setPolContSummaryId(policyContributionSummaryDto.getPolContSummaryId());
				policyContributionSummaryEntity.setPolicyId(policyContributionSummaryDto.getPolicyId());
				policyContributionSummaryEntity.setStampDuty(policyContributionSummaryDto.getStampDuty());
				policyContributionSummaryEntity.setOpeningBalance(policyContributionSummaryDto.getOpeningBalance());
				policyContributionSummaryEntity
						.setTotalEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
				policyContributionSummaryEntity
						.setTotalEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
				policyContributionSummaryEntity
						.setTotalVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
				policyContributionSummaryEntity
						.setTotalContribution(policyContributionSummaryDto.getTotalContribution());
				policyContributionSummaryEntity.setClosingBalance(policyContributionSummaryDto.getClosingBalance());
				policyContributionSummaryEntity.setFinancialYear(policyContributionSummaryDto.getFinancialYear());
				policyContributionSummaryEntity.setQuarter(policyContributionSummaryDto.getQuarter());
				policyContributionSummaryEntity.setIsActive(policyContributionSummaryDto.getIsActive());
				policyContributionSummaryEntity.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
				policyContributionSummaryEntity.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
				policyContributionSummaryEntity.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
				policyContributionSummaryEntity.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
				policyContributionSummary.add(policyContributionSummaryEntity);
			}
		}
		response.setPolicyContributionSummary(policyContributionSummary);
		Set<ZeroAccountdto> zeroAccount = new HashSet<>();
		for (ZeroAccountEntity zeroAccountdto : policyMasterDto.getZeroAccount()) {
			if (zeroAccountdto != null) {
				ZeroAccountdto zeroAccountEntity = new ZeroAccountdto();
				zeroAccountEntity.setZeroAccId(zeroAccountdto.getZeroAccId());
				zeroAccountEntity.setPolicyId(zeroAccountdto.getPolicyId());
				zeroAccountEntity.setZeroIdAmount(zeroAccountdto.getZeroIdAmount());
				zeroAccountEntity.setIsActive(zeroAccountdto.getIsActive());
				zeroAccountEntity.setCreatedOn(zeroAccountdto.getCreatedOn());
				zeroAccountEntity.setCreatedBy(zeroAccountdto.getCreatedBy());
				zeroAccountEntity.setModifiedOn(zeroAccountdto.getModifiedOn());
				zeroAccountEntity.setModifiedBy(zeroAccountdto.getModifiedBy());
				zeroAccount.add(zeroAccountEntity);
			}
		}
		response.setZeroAccount(zeroAccount);
		Set<ZeroAccountEntriesDto> zeroAccountEntries = new HashSet<>();
		for (ZeroAccountEntriesEntity zeroAccountEntriesDto : policyMasterDto.getZeroAccountEntries()) {
			if (zeroAccountEntriesDto != null) {
				ZeroAccountEntriesDto zeroAccountEntriesEntity = new ZeroAccountEntriesDto();
				zeroAccountEntriesEntity.setZeroAccEntId(zeroAccountEntriesDto.getZeroAccEntId());
				zeroAccountEntriesEntity.setPolicyId(zeroAccountEntriesDto.getPolicyId());
				zeroAccountEntriesEntity.setPolicyConId(zeroAccountEntriesDto.getPolicyConId());
				zeroAccountEntriesEntity.setMemberConId(zeroAccountEntriesDto.getMemberConId());
				zeroAccountEntriesEntity.setTransactionType(zeroAccountEntriesDto.getTransactionType());
				zeroAccountEntriesEntity.setTransactionDate(zeroAccountEntriesDto.getTransactionDate());
				zeroAccountEntriesEntity.setZeroIdAmount(zeroAccountEntriesDto.getZeroIdAmount());
				zeroAccountEntriesEntity.setCreatedOn(zeroAccountEntriesDto.getCreatedOn());
				zeroAccountEntriesEntity.setCreatedBy(zeroAccountEntriesDto.getCreatedBy());
				zeroAccountEntriesEntity.setModifiedOn(zeroAccountEntriesDto.getModifiedOn());
				zeroAccountEntriesEntity.setModifiedBy(zeroAccountEntriesDto.getModifiedBy());
				zeroAccountEntriesEntity.setIsActive(zeroAccountEntriesDto.getIsActive());
				zeroAccountEntries.add(zeroAccountEntriesEntity);
			}
		}
		response.setZeroAccountEntries(zeroAccountEntries);
		Set<PolicyNotesDto> notes = new HashSet<>();
		for (PolicyNotesEntity policyNotesDto : policyMasterDto.getNotes()) {
			if (policyNotesDto != null) {
				PolicyNotesDto policyNotesEntity = new PolicyNotesDto();
				policyNotesEntity.setPolicyNoteId(policyNotesDto.getPolicyNoteId());
				policyNotesEntity.setPolicyId(policyNotesDto.getPolicyId());
				policyNotesEntity.setNoteContents(policyNotesDto.getNoteContents());
				policyNotesEntity.setModifiedBy(policyNotesDto.getModifiedBy());
				policyNotesEntity.setModifiedOn(policyNotesDto.getModifiedOn());
				policyNotesEntity.setCreatedBy(policyNotesDto.getCreatedBy());
				policyNotesEntity.setCreatedOn(policyNotesDto.getCreatedOn());
				policyNotesEntity.setIsActive(policyNotesDto.getIsActive());
				notes.add(policyNotesEntity);
			}
		}
		response.setNotes(notes);
		Set<MemberMasterDto> memberMasterEntityList = new HashSet<>();
		for (MemberMasterEntity policyValuationDto : policyMasterDto.getMemberMaster()) {
			if (policyValuationDto != null) {
				MemberMasterDto memberMasterEntity = convertMemberEntityToMemberDto(policyValuationDto);
				memberMasterEntityList.add(memberMasterEntity);
			}
		}
		response.setMemberMaster(memberMasterEntityList);
		return response;
	}

	public MemberMasterDto convertMemberEntityToMemberDto(MemberMasterEntity request) {
		MemberMasterDto response = new MemberMasterDto();
		response.setMemberId(request.getMemberId());
		response.setTempMemberId(request.getTempMemberId());
		response.setPolicyId(request.getPolicyId());
		response.setMembershipNumber(request.getMembershipNumber());
		response.setMemberStatus(request.getMemberStatus());
		response.setLicId(request.getLicId());
		response.setTypeOfMemebership(request.getTypeOfMemebership());
		response.setAadharNumber(request.getAadharNumber());
		response.setCategoryNo(request.getCategoryNo());
		response.setCommunicationPreference(request.getCommunicationPreference());
		response.setLanguagePreference(request.getLanguagePreference());
		response.setDateOfBirth(request.getDateOfBirth());
		response.setDateOfJoining(request.getDateOfJoining());
		response.setDateOfJoiningScheme(request.getDateOfJoiningScheme());
		response.setDateOfRetrirement(request.getDateOfRetrirement());
		response.setDesignation(request.getDesignation());
		response.setEmailid(request.getEmailid());
		response.setGender(request.getGender());
		response.setFatherName(request.getFatherName());
		response.setFirstName(request.getFirstName());
		response.setMiddleName(request.getMiddleName());
		response.setLastName(request.getLastName());
		response.setSpouseName(request.getSpouseName());
		response.setMaritalStatus(request.getMaritalStatus());
		response.setMemberPan(request.getMemberPan());
		response.setLandlineNo(request.getLandlineNo());
		response.setMobileNo(request.getMobileNo());
		response.setIsZeroid(request.getIsZeroid());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());
		Set<MemberAddressDto> memberAddressList = new HashSet<>();
		for (MemberAddressEntity dto : request.getMemberAddress()) {
			if (dto != null) {
				MemberAddressDto entity = new MemberAddressDto();
				entity.setAddressId(dto.getAddressId());
				entity.setMemberId(dto.getMemberId());
				entity.setPolicyId(dto.getPolicyId());
				entity.setAddress1(dto.getAddress1());
				entity.setAddress2(dto.getAddress2());
				entity.setAddress3(dto.getAddress3());
				entity.setAddressType(dto.getAddressType());
				entity.setCity(dto.getCity());
				entity.setDistrict(dto.getDistrict());
				entity.setStateName(dto.getStateName());
				entity.setCountry(dto.getCountry());
				entity.setPincode(dto.getPincode());
				entity.setIsActive(dto.getIsActive());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				memberAddressList.add(entity);
			}
		}
		response.setMemberAddress(memberAddressList);
		Set<MemberBankDto> memberBankList = new HashSet<>();
		for (MemberBankEntity dto : request.getMemberBank()) {
			if (dto != null) {
				MemberBankDto entity = new MemberBankDto();
				entity.setMemberBankId(dto.getMemberBankId());
				entity.setMemberId(dto.getMemberId());
				entity.setPolicyId(dto.getPolicyId());
				entity.setAccountNumber(dto.getAccountNumber());
				entity.setConfirmAccountNumber(dto.getConfirmAccountNumber());
				entity.setAccountType(dto.getAccountType());
				entity.setIfscCodeAvailable(dto.getIfscCodeAvailable());
				entity.setIfscCode(dto.getIfscCode());
				entity.setBankName(dto.getBankName());
				entity.setBankBranch(dto.getBankBranch());
				entity.setBankAddress(dto.getBankAddress());
				entity.setCountryCode(dto.getCountryCode());
				entity.setStdCode(dto.getStdCode());
				entity.setLandlineNumber(dto.getLandlineNumber());
				entity.setEmailId(dto.getEmailId());
				entity.setIsActive(dto.getIsActive());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				memberBankList.add(entity);
			}
		}
		response.setMemberBank(memberBankList);
		Set<MemberNomineeDto> memberNomineeList = new HashSet<>();
		for (MemberNomineeEntity dto : request.getMemberNominee()) {
			if (dto != null) {
				MemberNomineeDto entity = new MemberNomineeDto();
				entity.setNomineeId(dto.getNomineeId());
				entity.setMemberId(dto.getMemberId());
				entity.setNomineeType(dto.getNomineeType());
				entity.setNomineeName(dto.getNomineeName());
				entity.setRelationShip(dto.getRelationShip());
				entity.setAadharNumber(dto.getAadharNumber());
				entity.setPhone(dto.getPhone());
				entity.setEmailId(dto.getEmailId());
				entity.setAddressOne(dto.getAddressOne());
				entity.setAddressTwo(dto.getAddressTwo());
				entity.setAddressThree(dto.getAddressThree());
				entity.setCountry(dto.getCountry());
				entity.setPinCode(dto.getPinCode());
				entity.setDistrict(dto.getDistrict());
				entity.setState(dto.getState());
				entity.setIsActive(dto.getIsActive());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				entity.setPercentage(dto.getPercentage());
				entity.setAge(dto.getAge());
				entity.setDateOfBirth(dto.getDateOfBirth());
				memberNomineeList.add(entity);
			}
		}
		response.setMemberNominee(memberNomineeList);
		Set<MemberAppointeeDto> memberAppointeeList = new HashSet<>();
		for (MemberAppointeeEntity dto : request.getMemberAppointee()) {
			if (dto != null) {
				MemberAppointeeDto entity = new MemberAppointeeDto();
				entity.setAppointeeId(dto.getAppointeeId());
				entity.setMemberId(dto.getMemberId());
				entity.setAppointeeName(dto.getAppointeeName());
				entity.setAppointeeCode(dto.getAppointeeCode());
				entity.setRelationship(dto.getRelationship());
				entity.setContactNumber(dto.getContactNumber());
				entity.setDateOfBirth(dto.getDateOfBirth());
				entity.setPan(dto.getPan());
				entity.setAadharNumber(dto.getAadharNumber());
				entity.setIfscCode(dto.getIfscCode());
				entity.setBankName(dto.getBankName());
				entity.setAccountType(dto.getAccountType());
				entity.setAccountNumber(dto.getAccountNumber());
				entity.setIsActive(dto.getIsActive());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				memberAppointeeList.add(entity);
			}
		}
		response.setMemberAppointee(memberAppointeeList);
		Set<MemberContributionDto> memberContributionList = new HashSet<>();
		for (MemberContributionEntity dto : request.getMemberContribution()) {
			if (dto != null) {
				MemberContributionDto entity = new MemberContributionDto();
				entity.setMemberConId(dto.getMemberConId());
				entity.setMemberId(dto.getMemberId());
				entity.setPolicyConId(dto.getPolicyConId());
				entity.setPolicyId(dto.getPolicyId());
				entity.setContributionType(dto.getContributionType());
				entity.setContributionDate(dto.getContributionDate());
				entity.setLicId(dto.getLicId());
				entity.setOpeningBalance(dto.getOpeningBalance());
				entity.setEmployeeContribution(dto.getEmployeeContribution());
				entity.setEmployerContribution(dto.getEmployerContribution());
				entity.setVoluntaryContribution(dto.getVoluntaryContribution());
				entity.setTotalContribution(dto.getTotalContribution());
				entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
				entity.setClosingBalance(dto.getClosingBalance());
//			entity.setIsZeroAccount(dto.getIsZeroAccount());
				entity.setIsDeposit(dto.getIsDeposit());
				entity.setFinancialYear(dto.getFinancialYear());
				entity.setQuarter(dto.getQuarter());
				entity.setVersionNo(dto.getVersionNo());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setIsActive(dto.getIsActive());
				memberContributionList.add(entity);
			}
		}
		response.setMemberContribution(memberContributionList);
		Set<MemberContributionSummaryDto> memberContributionSummaryList = new HashSet<>();
		for (MemberContributionSummaryEntity dto : request.getMemberContributionSummary()) {
			if (dto != null) {
				MemberContributionSummaryDto entity = new MemberContributionSummaryDto();
				entity.setMemContSummaryId(dto.getMemContSummaryId());
				entity.setMemberId(dto.getMemberId());
				entity.setPolicyId(dto.getPolicyId());
				entity.setLicId(dto.getLicId());
				entity.setOpeningBalance(dto.getOpeningBalance());
				entity.setTotalEmployeeContribution(dto.getTotalEmployeeContribution());
				entity.setClosingBalance(dto.getClosingBalance());
				entity.setTotalEmployerContribution(dto.getTotalEmployerContribution());
				entity.setTotalVoluntaryContribution(dto.getTotalVoluntaryContribution());
				entity.setTotalContribution(dto.getTotalContribution());
				entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
				entity.setFinancialYear(dto.getFinancialYear());
				entity.setQuarter(dto.getQuarter());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setIsActive(dto.getIsActive());
				memberContributionSummaryList.add(entity);
			}
		}
		response.setMemberContributionSummary(memberContributionSummaryList);
		return response;
	}

////OLD GETTER AND SETTER ENDS

//POLICY DTO TO MPH DTO
	public MphMasterDto convertOldRequestToNewRequest(PolicyDto masterDto) {

		MphMasterDto response = new MphMasterDto();

		response.setMphId(masterDto.getMphId());
		response.setTempMphId(masterDto.getTempMphId());
		response.setMphCode(masterDto.getMphCode());
		response.setMphName(masterDto.getMphName());
//		response.setMphType(masterDto.getMphType());
		response.setProposalNumber(masterDto.getProposalNumber());
		response.setProposalId(NumericUtils.convertIntegerToString(masterDto.getProposalId()));
		response.setCin(NumericUtils.convertIntegerToString(masterDto.getCin()));
		response.setPan(masterDto.getPan());
		response.setAlternatePan(masterDto.getPan());
//		response.setLandlineNo(masterDto.getLandlineNo());
//		response.setCountryCode(masterDto.getCountryCode());
		response.setMobileNo(NumericUtils.convertStringToLong(masterDto.getContactNo()));
		response.setEmailId(masterDto.getEmailId());
		response.setFax(masterDto.getFax());

		response.setIsActive(Boolean.TRUE);
		response.setCreatedOn(new Date());
		response.setCreatedBy(masterDto.getCreatedBy());
		response.setModifiedOn(new Date());
		response.setModifiedBy(masterDto.getModifiedBy());

		if (!masterDto.getAddresses().isEmpty()) {
			Set<MphAddressDto> mphAddress = new HashSet<>();
			for (PolicyAddressOldDto mphAddressDto : masterDto.getAddresses()) {
				MphAddressDto mphAddressTempEntity = new MphAddressDto();
				mphAddressTempEntity.setAddressId(mphAddressDto.getAddressId());
				mphAddressTempEntity.setMphId(mphAddressDto.getMphId());
				mphAddressTempEntity.setAddressType(mphAddressDto.getAddressType());
				mphAddressTempEntity.setAddressLine1(mphAddressDto.getAddressLine1());
				mphAddressTempEntity.setAddressLine2(mphAddressDto.getAddressLine2());
				mphAddressTempEntity.setAddressLine3(mphAddressDto.getAddressLine3());
				mphAddressTempEntity.setPincode(NumericUtils.convertStringToInteger(mphAddressDto.getPinCode()));
				mphAddressTempEntity.setCityLocality(mphAddressDto.getCityId());
				mphAddressTempEntity.setCityId(mphAddressDto.getCityId());
				mphAddressTempEntity.setDistrict(NumericUtils.convertIntegerToString(mphAddressDto.getDistrictId()));
				mphAddressTempEntity.setDistrictId(mphAddressDto.getDistrictId());
				mphAddressTempEntity.setCountryId(mphAddressDto.getCountryId());
				mphAddressTempEntity.setStateId(mphAddressDto.getStateId());
				mphAddressTempEntity.setStateName(NumericUtils.convertIntegerToString(mphAddressDto.getStateId()));
				mphAddressTempEntity.setIsActive(Boolean.TRUE);
				mphAddressTempEntity.setCreatedOn(new Date());
				mphAddressTempEntity.setCreatedBy(mphAddressDto.getCreatedBy());
				mphAddressTempEntity.setModifiedOn(new Date());
				mphAddressTempEntity.setModifiedBy(mphAddressDto.getModifiedBy());
				mphAddress.add(mphAddressTempEntity);
			}
			response.setMphAddress(mphAddress);

		}
		Set<MphBankDto> mphBank = new HashSet<>();
		for (PolicyBankOldDto mphBankDto : masterDto.getBankDetails()) {
			MphBankDto mphBankTempEntity = new MphBankDto();
			mphBankTempEntity.setMphBankId(mphBankDto.getBankAccountId());
			mphBankTempEntity.setMphId(mphBankDto.getMphId());
			mphBankTempEntity.setAccountNumber(mphBankDto.getAccountNumber());
			mphBankTempEntity.setConfirmAccountNumber(mphBankDto.getAccountNumber());
			mphBankTempEntity.setAccountType(mphBankDto.getAccountType());
			mphBankTempEntity.setIfscCodeAvailable(mphBankDto.getIfscCodeAvailable());
			mphBankTempEntity.setIfscCode(mphBankDto.getIfscCode());
			mphBankTempEntity.setBankName(mphBankDto.getBankName());
			mphBankTempEntity.setBankBranch(mphBankDto.getBankBranch());
			mphBankTempEntity.setBankAddress(mphBankDto.getBankAddress());
			mphBankTempEntity.setStdCode(NumericUtils.convertStringToInteger(mphBankDto.getStdCode()));
//			mphBankTempEntity.setLandlineNumber(mphBankDto.getLandlineNumber());
			mphBankTempEntity.setEmailId(mphBankDto.getEmailId());
			mphBankTempEntity.setCountryCode(NumericUtils.convertStringToInteger(mphBankDto.getCountryId()));
			mphBankTempEntity.setCountryId(mphBankDto.getCountryId());
			mphBankTempEntity.setStateId(mphBankDto.getStateId());
			mphBankTempEntity.setDistrictId(mphBankDto.getDistrictId());
			mphBankTempEntity.setCityId(mphBankDto.getCityId());
			mphBankTempEntity.setTownLocality(mphBankDto.getTownLocality());
			mphBankTempEntity.setIsActive(Boolean.TRUE);
			mphBankTempEntity.setCreatedBy(mphBankDto.getCreatedBy());
			mphBankTempEntity.setCreatedOn(new Date());
			mphBankTempEntity.setModifiedOn(new Date());
			mphBankTempEntity.setModifiedBy(mphBankDto.getModifiedBy());
			mphBank.add(mphBankTempEntity);
		}
		response.setMphBank(mphBank);

//		Set<MphRepresentativesDto> mphRepresentative = new HashSet<>();
//		for (MphRepresentativesTempEntity mphRepresentativeDto : masterDto.getMphRepresentative()) {
//			MphRepresentativesDto mphRepresentativesTempEntity = new MphRepresentativesDto();
//			mphRepresentativesTempEntity.setRepId(mphRepresentativeDto.getRepId());
//			mphRepresentativesTempEntity.setMphId(mphRepresentativeDto.getMphId());
//			mphRepresentativesTempEntity.setRepresentativeCode(mphRepresentativeDto.getRepresentativeCode());
//			mphRepresentativesTempEntity.setRepresentativeName(mphRepresentativeDto.getRepresentativeName());
//			mphRepresentativesTempEntity.setLandlineNo(mphRepresentativeDto.getLandlineNo());
//			mphRepresentativesTempEntity.setCountryCode(mphRepresentativeDto.getCountryCode());
//			mphRepresentativesTempEntity.setMobileNo(mphRepresentativeDto.getMobileNo());
//			mphRepresentativesTempEntity.setEmailId(mphRepresentativeDto.getEmailId());
//			mphRepresentativesTempEntity.setAddressType(mphRepresentativeDto.getAddressType());
//			mphRepresentativesTempEntity.setAddressLine1(mphRepresentativeDto.getAddressLine1());
//			mphRepresentativesTempEntity.setAddressLine2(mphRepresentativeDto.getAddressLine2());
//			mphRepresentativesTempEntity.setAddressLine3(mphRepresentativeDto.getAddressLine3());
//			mphRepresentativesTempEntity.setCityLocality(mphRepresentativeDto.getCityLocality());
//			mphRepresentativesTempEntity.setDistrict(mphRepresentativeDto.getDistrict());
//			mphRepresentativesTempEntity.setStateName(mphRepresentativeDto.getStateName());
//			mphRepresentativesTempEntity.setPincode(mphRepresentativeDto.getPincode());
//			mphRepresentativesTempEntity.setIsActive(Boolean.TRUE);
//			mphRepresentativesTempEntity.setCreatedOn(new Date());
//			mphRepresentativesTempEntity.setCreatedBy(mphRepresentativeDto.getCreatedBy());
//			mphRepresentativesTempEntity.setModifiedOn(new Date());
//			mphRepresentativesTempEntity.setModifiedBy(mphRepresentativeDto.getModifiedBy());
//			mphRepresentative.add(mphRepresentativesTempEntity);
//		}
//		response.setMphRepresentative(mphRepresentative);

		Set<PolicyMasterDto> policyMaster = new HashSet<>();

		PolicyMasterDto responsee = new PolicyMasterDto();

		responsee.setPolicyId(masterDto.getPolicyId());
		responsee.setMphId(masterDto.getMphId());
		responsee.setPolicyNumber(masterDto.getPolicyNumber());
		responsee.setPolicyStatus(masterDto.getPolicyStatus());
		responsee.setPolicyType(masterDto.getQuotationType());
		responsee.setNoOfCategory(masterDto.getCategory());
		responsee.setContributionFrequency(masterDto.getFrequency());
		responsee.setIntermediaryOfficerCode(masterDto.getIntermediaryOfficerCode());
		responsee.setIntermediaryOfficerName(masterDto.getIntermediaryOfficerName());
		responsee.setMarketingOfficerCode(masterDto.getMarketingOfficerCode());
		responsee.setMarketingOfficerName(masterDto.getMarketingOfficerName());
		responsee.setProposalId(masterDto.getProposalId());
		responsee.setQuotationId(masterDto.getQuotationId());
		responsee.setLeadId(masterDto.getLeadId());
		responsee.setLineOfBusiness(masterDto.getLineOfBusiness());
		responsee.setVariant(masterDto.getVariant());
		responsee.setProductId(NumericUtils.convertStringToLong(masterDto.getProduct()));
		responsee.setTotalMember(masterDto.getTotalMember());
		responsee.setUnitId(masterDto.getUnitCode());
		responsee.setUnitOffice(masterDto.getUnitOffice());
		responsee.setAdjustmentDt(masterDto.getPolicyAdjustmentDate());
		responsee.setRejectionReasonCode(masterDto.getRejectionReasonCode());
		responsee.setRejectionRemarks(masterDto.getRejectionRemarks());
		responsee.setWorkflowStatus(masterDto.getWorkFlowStatus());

		responsee.setIsCommencementdateOneYr(masterDto.getIsCommencementdateOneYr());
		responsee.setPolicyCommencementDt(masterDto.getPolicyCommencementDate());
		responsee.setArd(
				masterDto.getArd() != null ? masterDto.getArd() : calculateARDa(masterDto.getPolicyCommencementDate()));
		responsee.setPolicyDispatchDate(masterDto.getPolicyDispatchDate());
		responsee.setPolicyRecievedDate(masterDto.getPolicyRecievedDate());
		responsee.setConType(masterDto.getContributionType());
		responsee.setAdvanceotarrears(masterDto.getAdvanceOrArrears());

		responsee.setIsActive(Boolean.TRUE);
		responsee.setCreatedBy(masterDto.getCreatedBy());
		responsee.setCreatedOn(new Date());
		responsee.setModifiedBy(masterDto.getModifiedBy());
		responsee.setModifiedOn(new Date());

		responsee.setAmountToBeAdjusted(
				masterDto.getAmountToBeAdjusted() != null ? masterDto.getAmountToBeAdjusted() : BigDecimal.ZERO);
		responsee.setFirstPremium(masterDto.getFirstPremium() != null ? masterDto.getFirstPremium() : BigDecimal.ZERO);
		responsee.setSinglePremiumFirstYr(
				masterDto.getSinglePremiumFirstYr() != null ? masterDto.getSinglePremiumFirstYr() : BigDecimal.ZERO);
		responsee.setRenewalPremium(
				masterDto.getRenewalPremium() != null ? masterDto.getRenewalPremium() : BigDecimal.ZERO);
		responsee.setSubsequentSinglePremium(
				masterDto.getSubsequentSinglePremium() != null ? masterDto.getSubsequentSinglePremium()
						: BigDecimal.ZERO);

		Set<PolicyValuationDto> valuations = new HashSet<>();

		PolicyValuationDto policyValuationTempEntity = new PolicyValuationDto();
		policyValuationTempEntity.setValuationId(masterDto.getValuationId());
		policyValuationTempEntity.setPolicyId(masterDto.getPolicyId());
		policyValuationTempEntity.setValuationType(masterDto.getValuationType());
		policyValuationTempEntity.setAttritionRate(masterDto.getAttritionRate());
		policyValuationTempEntity.setSalaryEscalation(masterDto.getSalaryEscalation());
		policyValuationTempEntity.setDeathRate(masterDto.getDeathRate());
		policyValuationTempEntity.setDisRateIntrest(masterDto.getDisRateIntrest());
		policyValuationTempEntity.setDisRateSalaryEsc(masterDto.getDisRateSalaryEsc());
		policyValuationTempEntity.setAnnuityOption(masterDto.getAnnuityOption());
		policyValuationTempEntity.setAccuralRateFactor(masterDto.getAccuralRateFactor());
		policyValuationTempEntity.setMinPension(masterDto.getMinPension());
		policyValuationTempEntity.setMaxPension(masterDto.getMaxPension());
		policyValuationTempEntity.setWithdrawalRate(masterDto.getWithdrawalRate());
		policyValuationTempEntity.setIsActive(Boolean.TRUE);
		policyValuationTempEntity.setCreatedOn(new Date());
		policyValuationTempEntity.setCreatedBy(masterDto.getCreatedBy());
		policyValuationTempEntity.setModifiedOn(new Date());
		policyValuationTempEntity.setModifiedBy(masterDto.getModifiedBy());
		valuations.add(policyValuationTempEntity);
		responsee.setValuations(valuations);

		Set<PolicyRulesDto> rules = new HashSet<>();
		for (PolicyRulesOldDto policyRulesDto : masterDto.getRules()) {
			PolicyRulesDto policyAnnuityTempEntity = new PolicyRulesDto();
			policyAnnuityTempEntity.setRuleId(policyRulesDto.getRuleId());
			policyAnnuityTempEntity.setPolicyId(policyRulesDto.getPolicyId());
			policyAnnuityTempEntity.setEffectiveFrom(policyRulesDto.getEffectiveFrom());
			policyAnnuityTempEntity.setPercentageCorpus(policyRulesDto.getPercentageCorpus());
			policyAnnuityTempEntity.setCategory(policyRulesDto.getCategory());
			policyAnnuityTempEntity.setContributionType(policyRulesDto.getContributionType());
			policyAnnuityTempEntity.setBenifitPayableTo(policyRulesDto.getBenifitPayableTo());
			policyAnnuityTempEntity.setAnnuityOption(policyRulesDto.getAnnuityOption());
			policyAnnuityTempEntity.setCommutationBy(policyRulesDto.getCommutationBy());
			policyAnnuityTempEntity.setCommutationAmt(policyRulesDto.getCommutationAmt());
			policyAnnuityTempEntity.setNormalAgeRetirement(policyRulesDto.getNormalAgeRetirement());
			policyAnnuityTempEntity.setMinAgeRetirement(policyRulesDto.getMinAgeRetirement());
			policyAnnuityTempEntity.setMaxAgeRetirement(policyRulesDto.getMaxAgeRetirement());
			policyAnnuityTempEntity.setMinServicePension(policyRulesDto.getMinServicePension());
			policyAnnuityTempEntity.setMaxServicePension(policyRulesDto.getMaxServicePension());
			policyAnnuityTempEntity.setMinServiceWithdrawal(policyRulesDto.getMinServiceWithdrawal());
			policyAnnuityTempEntity.setMaxServiceWithdrawal(policyRulesDto.getMaxServiceWithdrawal());
			policyAnnuityTempEntity.setMinPension(policyRulesDto.getMinPension());
			policyAnnuityTempEntity.setMaxPension(policyRulesDto.getMaxPension());
			policyAnnuityTempEntity.setModifiedBy(policyRulesDto.getModifiedBy());
			policyAnnuityTempEntity.setModifiedOn(new Date());
			policyAnnuityTempEntity.setCreatedBy(policyRulesDto.getCreatedBy());
			policyAnnuityTempEntity.setCreatedOn(new Date());
			policyAnnuityTempEntity.setIsActive(Boolean.TRUE);
			rules.add(policyAnnuityTempEntity);
		}
		responsee.setRules(rules);
		Set<PolicyDepositDto> deposits = new HashSet<>();
		for (PolicyDepositOldDto policyDepositDto : masterDto.getDeposit()) {
			PolicyDepositDto policyDepositTempEntity = new PolicyDepositDto();
			policyDepositTempEntity.setDepositId(policyDepositDto.getDepositId());
			policyDepositTempEntity.setPolicyId(policyDepositDto.getPolicyId());
			policyDepositTempEntity.setCollectionNo(policyDepositDto.getCollectionNo());
			policyDepositTempEntity.setCollectionDate(policyDepositDto.getCollectionDate());
			policyDepositTempEntity.setCollectionStatus(policyDepositDto.getCollectionStatus());
//			policyDepositTempEntity.setChallanNo(policyDepositDto.getChallanNo());qwety
			policyDepositTempEntity.setChallanNo(
					policyDepositDto.getChallanNo() != null && !"0".equals(policyDepositDto.getChallanNo())
							? policyDepositDto.getChallanNo()
							: policyServiceImpl.getChallanSeq());
			policyDepositTempEntity.setDepositAmount(policyDepositDto.getAmount());
			policyDepositTempEntity.setAdjustmentAmount(policyDepositDto.getAdjestmentAmount());
			policyDepositTempEntity.setAvailableAmount(policyDepositDto.getAvailableAmount());
			policyDepositTempEntity.setTransactionMode(policyDepositDto.getTransactionMode());
			policyDepositTempEntity.setChequeRealisationDate(policyDepositDto.getChequeRealisationDate());
			policyDepositTempEntity.setRemark(policyDepositDto.getRemark());
			policyDepositTempEntity.setStatus(PolicyConstants.DEPOSITSTATUSNEW);
			policyDepositTempEntity.setZeroId(policyDepositDto.getZeroId());
			policyDepositTempEntity.setModifiedBy(policyDepositDto.getModifiedBy());
			policyDepositTempEntity.setModifiedOn(new Date());
			policyDepositTempEntity.setCreatedBy(policyDepositDto.getCreatedBy());
			policyDepositTempEntity.setCreatedOn(new Date());
			policyDepositTempEntity.setIsActive(Boolean.TRUE);
			deposits.add(policyDepositTempEntity);
		}
		responsee.setDeposits(deposits);

		Set<PolicyContributionSummaryDto> policyContributionSummary = new HashSet<>();
		PolicyContributionSummaryDto policyContributionSummaryTempEntity = new PolicyContributionSummaryDto();
		policyContributionSummaryTempEntity.setPolContSummaryId(null);
		policyContributionSummaryTempEntity.setPolicyId(masterDto.getPolicyId());
		policyContributionSummaryTempEntity.setStampDuty(BigDecimal.ZERO);
		policyContributionSummaryTempEntity.setOpeningBalance(BigDecimal.ZERO);
		policyContributionSummaryTempEntity.setTotalEmployerContribution(masterDto.getEmployerContribution());
		policyContributionSummaryTempEntity.setTotalEmployeeContribution(masterDto.getEmployeeContribution());
		policyContributionSummaryTempEntity.setTotalVoluntaryContribution(masterDto.getVoluntaryContribution());
		policyContributionSummaryTempEntity.setTotalContribution(masterDto.getTotalContribution());
		policyContributionSummaryTempEntity.setClosingBalance(masterDto.getTotalContribution());
//		policyContributionSummaryTempEntity.setClosingBalance(BigDecimal.ZERO);
		policyContributionSummaryTempEntity.setFinancialYear(DateUtils.getFinancialYrByDt(new Date()));
//		if (variantType.equalsIgnoreCase("V2")) {
//			policyContributionSummaryTempEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
//		} else {
//			policyContributionSummaryTempEntity.setQuarter("Q0");
//		}
		policyContributionSummaryTempEntity.setIsActive(Boolean.TRUE);
		policyContributionSummaryTempEntity.setCreatedOn(new Date());
		policyContributionSummaryTempEntity.setCreatedBy(masterDto.getCreatedBy());
		policyContributionSummaryTempEntity.setModifiedOn(new Date());
		policyContributionSummaryTempEntity.setModifiedBy(masterDto.getModifiedBy());
		policyContributionSummary.add(policyContributionSummaryTempEntity);
		responsee.setPolicyContributionSummary(policyContributionSummary);

		Set<PolicyNotesDto> notes = new HashSet<>();
		for (PolicyNotesOldDto policyNotesDto : masterDto.getNotes()) {
			PolicyNotesDto policyNotesTempEntity = new PolicyNotesDto();
			policyNotesTempEntity.setPolicyNoteId(policyNotesDto.getPolicyNoteId());
			policyNotesTempEntity.setPolicyId(policyNotesDto.getPolicyId());
			policyNotesTempEntity.setNoteContents(policyNotesDto.getNoteContents());
			policyNotesTempEntity.setModifiedBy(policyNotesDto.getModifiedBy());
			policyNotesTempEntity.setModifiedOn(new Date());
			policyNotesTempEntity.setCreatedBy(policyNotesDto.getCreatedBy());
			policyNotesTempEntity.setCreatedOn(new Date());
			policyNotesTempEntity.setIsActive(Boolean.TRUE);
			notes.add(policyNotesTempEntity);
		}
		responsee.setNotes(notes);

		policyMaster.add(responsee);
		response.setPolicyMaster(policyMaster);
		return response;
	}

	public MphMasterDto convertEditRequestToNewRequest(PolicyDto masterDto,
			List<PolicyContributionTempEntity> policyContributionTempEntity,
			List<PolicyContributionSummaryTempEntity> policyContributionSummaryTempEntities,
			List<MemberMasterTempEntity> memberMaster) {

		MphMasterDto response = new MphMasterDto();

		response.setMphId(masterDto.getMphId());
//		response.setTempMphId(masterDto.getTempMphId());
		response.setMphCode(masterDto.getMphCode());
		response.setMphName(masterDto.getMphName());
//		response.setMphType(masterDto.getMphType());
		response.setProposalNumber(masterDto.getProposalNumber());
		response.setProposalId(NumericUtils.convertIntegerToString(masterDto.getProposalId()));
		response.setCin(NumericUtils.convertIntegerToString(masterDto.getCin()));
		response.setPan(masterDto.getPan());
		response.setAlternatePan(masterDto.getPan());
//		response.setLandlineNo(masterDto.getLandlineNo());
//		response.setCountryCode(masterDto.getCountryCode());
		response.setMobileNo(NumericUtils.convertStringToLong(masterDto.getContactNo()));
		response.setEmailId(masterDto.getEmailId());
		response.setFax(masterDto.getFax());

		response.setIsActive(masterDto.getIsActive());
		response.setCreatedOn(masterDto.getCreatedOn());
		response.setCreatedBy(masterDto.getCreatedBy());
		response.setModifiedOn(masterDto.getModifiedOn());
		response.setModifiedBy(masterDto.getModifiedBy());

		Set<MphAddressDto> mphAddress = new HashSet<>();
		for (PolicyAddressOldDto mphAddressDto : masterDto.getAddresses()) {
			MphAddressDto mphAddressTempEntity = new MphAddressDto();
			mphAddressTempEntity.setAddressId(mphAddressDto.getAddressId());
			mphAddressTempEntity.setMphId(mphAddressDto.getMphId());
			mphAddressTempEntity.setAddressType(mphAddressDto.getAddressType());
			mphAddressTempEntity.setAddressLine1(mphAddressDto.getAddressLine1());
			mphAddressTempEntity.setAddressLine2(mphAddressDto.getAddressLine2());
			mphAddressTempEntity.setAddressLine3(mphAddressDto.getAddressLine3());
			mphAddressTempEntity.setPincode(NumericUtils.convertStringToInteger(mphAddressDto.getPinCode()));
			mphAddressTempEntity.setCityLocality(mphAddressDto.getCityId());
			mphAddressTempEntity.setCityId(mphAddressDto.getCityId());
			mphAddressTempEntity.setDistrict(NumericUtils.convertIntegerToString(mphAddressDto.getDistrictId()));
			mphAddressTempEntity.setDistrictId(mphAddressDto.getDistrictId());
			mphAddressTempEntity.setCountryId(mphAddressDto.getCountryId());
			mphAddressTempEntity.setStateId(mphAddressDto.getStateId());
			mphAddressTempEntity.setStateName(NumericUtils.convertIntegerToString(mphAddressDto.getStateId()));
			mphAddressTempEntity.setIsActive(mphAddressDto.getIsActive());
			mphAddressTempEntity.setIsDefault(Boolean.TRUE);
			mphAddressTempEntity.setCreatedOn(mphAddressDto.getCreatedOn());
			mphAddressTempEntity.setCreatedBy(mphAddressDto.getCreatedBy());
			mphAddressTempEntity.setModifiedOn(mphAddressDto.getModifiedOn());
			mphAddressTempEntity.setModifiedBy(mphAddressDto.getModifiedBy());
			mphAddress.add(mphAddressTempEntity);
		}
		response.setMphAddress(mphAddress);
		Set<MphBankDto> mphBank = new HashSet<>();
		for (PolicyBankOldDto mphBankDto : masterDto.getBankDetails()) {
			MphBankDto mphBankTempEntity = new MphBankDto();
			mphBankTempEntity.setMphBankId(mphBankDto.getBankAccountId());
			mphBankTempEntity.setMphId(mphBankDto.getMphId());
			mphBankTempEntity.setAccountNumber(mphBankDto.getAccountNumber());
			mphBankTempEntity.setConfirmAccountNumber(mphBankDto.getAccountNumber());
			mphBankTempEntity.setAccountType(mphBankDto.getAccountType());
			mphBankTempEntity.setIfscCodeAvailable(mphBankDto.getIfscCodeAvailable());
			mphBankTempEntity.setIfscCode(mphBankDto.getIfscCode());
			mphBankTempEntity.setBankName(mphBankDto.getBankName());
			mphBankTempEntity.setBankBranch(mphBankDto.getBankBranch());
			mphBankTempEntity.setBankAddress(mphBankDto.getBankAddress());
			mphBankTempEntity.setStdCode(NumericUtils.convertStringToInteger(mphBankDto.getStdCode()));
//			mphBankTempEntity.setLandlineNumber(mphBankDto.getLandlineNumber());
			mphBankTempEntity.setEmailId(mphBankDto.getEmailId());
			mphBankTempEntity.setCountryCode(NumericUtils.convertStringToInteger(mphBankDto.getCountryId()));
			mphBankTempEntity.setCountryId(mphBankDto.getCountryId());
			mphBankTempEntity.setStateId(mphBankDto.getStateId());
			mphBankTempEntity.setDistrictId(mphBankDto.getDistrictId());
			mphBankTempEntity.setCityId(mphBankDto.getCityId());
			mphBankTempEntity.setTownLocality(mphBankDto.getTownLocality());
			mphBankTempEntity.setIsActive(mphBankDto.getIsActive());
			mphBankTempEntity.setIsDefault(Boolean.TRUE);
			mphBankTempEntity.setCreatedBy(mphBankDto.getCreatedBy());
			mphBankTempEntity.setCreatedOn(mphBankDto.getCreatedOn());
			mphBankTempEntity.setModifiedOn(mphBankDto.getModifiedOn());
			mphBankTempEntity.setModifiedBy(mphBankDto.getModifiedBy());
			mphBank.add(mphBankTempEntity);
		}
		response.setMphBank(mphBank);

//		Set<MphRepresentativesDto> mphRepresentative = new HashSet<>();
//		for (MphRepresentativesTempEntity mphRepresentativeDto : masterDto.getMphRepresentative()) {
//			MphRepresentativesDto mphRepresentativesTempEntity = new MphRepresentativesDto();
//			mphRepresentativesTempEntity.setRepId(mphRepresentativeDto.getRepId());
//			mphRepresentativesTempEntity.setMphId(mphRepresentativeDto.getMphId());
//			mphRepresentativesTempEntity.setRepresentativeCode(mphRepresentativeDto.getRepresentativeCode());
//			mphRepresentativesTempEntity.setRepresentativeName(mphRepresentativeDto.getRepresentativeName());
//			mphRepresentativesTempEntity.setLandlineNo(mphRepresentativeDto.getLandlineNo());
//			mphRepresentativesTempEntity.setCountryCode(mphRepresentativeDto.getCountryCode());
//			mphRepresentativesTempEntity.setMobileNo(mphRepresentativeDto.getMobileNo());
//			mphRepresentativesTempEntity.setEmailId(mphRepresentativeDto.getEmailId());
//			mphRepresentativesTempEntity.setAddressType(mphRepresentativeDto.getAddressType());
//			mphRepresentativesTempEntity.setAddressLine1(mphRepresentativeDto.getAddressLine1());
//			mphRepresentativesTempEntity.setAddressLine2(mphRepresentativeDto.getAddressLine2());
//			mphRepresentativesTempEntity.setAddressLine3(mphRepresentativeDto.getAddressLine3());
//			mphRepresentativesTempEntity.setCityLocality(mphRepresentativeDto.getCityLocality());
//			mphRepresentativesTempEntity.setDistrict(mphRepresentativeDto.getDistrict());
//			mphRepresentativesTempEntity.setStateName(mphRepresentativeDto.getStateName());
//			mphRepresentativesTempEntity.setPincode(mphRepresentativeDto.getPincode());
//			mphRepresentativesTempEntity.setIsActive(mphRepresentativeDto.getIsActive());
//			mphRepresentativesTempEntity.setCreatedOn(mphRepresentativeDto.getCreatedOn());
//			mphRepresentativesTempEntity.setCreatedBy(mphRepresentativeDto.getCreatedBy());
//			mphRepresentativesTempEntity.setModifiedOn(mphRepresentativeDto.getModifiedOn());
//			mphRepresentativesTempEntity.setModifiedBy(mphRepresentativeDto.getModifiedBy());
//			mphRepresentative.add(mphRepresentativesTempEntity);
//		}
//		response.setMphRepresentative(mphRepresentative);

		Set<PolicyMasterDto> policyMaster = new HashSet<>();

		PolicyMasterDto responsee = new PolicyMasterDto();

		responsee.setPolicyId(masterDto.getPolicyId());
		responsee.setMphId(masterDto.getMphId());
		responsee.setPolicyNumber(masterDto.getPolicyNumber());
		responsee.setPolicyStatus(masterDto.getPolicyStatus());
		responsee.setPolicyType(masterDto.getQuotationType());
		responsee.setNoOfCategory(masterDto.getCategory());
		responsee.setContributionFrequency(masterDto.getFrequency());
		responsee.setIntermediaryOfficerCode(masterDto.getIntermediaryOfficerCode());
		responsee.setIntermediaryOfficerName(masterDto.getIntermediaryOfficerName());
		responsee.setMarketingOfficerCode(masterDto.getMarketingOfficerCode());
		responsee.setMarketingOfficerName(masterDto.getMarketingOfficerName());
		responsee.setProposalId(masterDto.getProposalId());
		responsee.setQuotationId(masterDto.getQuotationId());
		responsee.setLeadId(masterDto.getLeadId());
		responsee.setLineOfBusiness(masterDto.getLineOfBusiness());
		responsee.setVariant(masterDto.getVariant());
		responsee.setProductId(NumericUtils.convertStringToLong(masterDto.getProduct()));
		responsee.setTotalMember(masterDto.getTotalMember());
		responsee.setUnitId(masterDto.getUnitCode());
		responsee.setUnitOffice(masterDto.getUnitOffice());
		responsee.setAdjustmentDt(masterDto.getPolicyAdjustmentDate());
		responsee.setRejectionReasonCode(masterDto.getRejectionReasonCode());
		responsee.setRejectionRemarks(masterDto.getRejectionRemarks());
		responsee.setWorkflowStatus(masterDto.getWorkFlowStatus());
		responsee.setArd(masterDto.getArd());
		responsee.setIsCommencementdateOneYr(masterDto.getIsCommencementdateOneYr());
		responsee.setPolicyCommencementDt(masterDto.getPolicyCommencementDate());
		responsee.setPolicyDispatchDate(masterDto.getPolicyDispatchDate());
		responsee.setPolicyRecievedDate(masterDto.getPolicyRecievedDate());
		responsee.setConType(masterDto.getContributionType());
		responsee.setAdvanceotarrears(masterDto.getAdvanceOrArrears());

		responsee.setIsActive(masterDto.getIsActive());
		responsee.setCreatedBy(masterDto.getCreatedBy());
		responsee.setCreatedOn(masterDto.getCreatedOn());
		responsee.setModifiedBy(masterDto.getModifiedBy());
		responsee.setModifiedOn(masterDto.getModifiedOn());

		responsee.setAmountToBeAdjusted(masterDto.getAmountToBeAdjusted());
		responsee.setFirstPremium(masterDto.getFirstPremium());
		responsee.setSinglePremiumFirstYr(masterDto.getSinglePremiumFirstYr());
		responsee.setRenewalPremium(masterDto.getRenewalPremium());
		responsee.setSubsequentSinglePremium(masterDto.getSubsequentSinglePremium());
		responsee.setStampDuty(policyServiceImpl.calculateStamps(responsee.getAmountToBeAdjusted()));
		Set<PolicyValuationDto> valuations = new HashSet<>();

//		for (PolicyValuationTempEntity policyValuationDto : masterDto.getValuations()) {

		PolicyValuationDto policyValuationTempEntity = new PolicyValuationDto();
		policyValuationTempEntity.setValuationId(masterDto.getValuationId());
		policyValuationTempEntity.setPolicyId(masterDto.getPolicyId());
		policyValuationTempEntity.setValuationType(masterDto.getValuationType());
		policyValuationTempEntity.setAttritionRate(masterDto.getAttritionRate());
		policyValuationTempEntity.setSalaryEscalation(masterDto.getSalaryEscalation());
		policyValuationTempEntity.setDeathRate(masterDto.getDeathRate());
		policyValuationTempEntity.setDisRateIntrest(masterDto.getDisRateIntrest());
		policyValuationTempEntity.setDisRateSalaryEsc(masterDto.getDisRateSalaryEsc());
		policyValuationTempEntity.setAnnuityOption(masterDto.getAnnuityOption());
		policyValuationTempEntity.setAccuralRateFactor(masterDto.getAccuralRateFactor());
		policyValuationTempEntity.setMinPension(masterDto.getMinPension());
		policyValuationTempEntity.setMaxPension(masterDto.getMaxPension());
		policyValuationTempEntity.setWithdrawalRate(masterDto.getWithdrawalRate());
		policyValuationTempEntity.setIsActive(masterDto.getIsActive());
		policyValuationTempEntity.setCreatedOn(masterDto.getCreatedOn());
		policyValuationTempEntity.setCreatedBy(masterDto.getCreatedBy());
		policyValuationTempEntity.setModifiedOn(masterDto.getModifiedOn());
		policyValuationTempEntity.setModifiedBy(masterDto.getModifiedBy());

		valuations.add(policyValuationTempEntity);
//		}
		responsee.setValuations(valuations);

		Set<PolicyRulesDto> rules = new HashSet<>();
		for (PolicyRulesOldDto policyRulesDto : masterDto.getRules()) {
			PolicyRulesDto policyAnnuityTempEntity = new PolicyRulesDto();
			policyAnnuityTempEntity.setRuleId(policyRulesDto.getRuleId());
			policyAnnuityTempEntity.setPolicyId(policyRulesDto.getPolicyId());
			policyAnnuityTempEntity.setEffectiveFrom(policyRulesDto.getEffectiveFrom());
			policyAnnuityTempEntity.setPercentageCorpus(policyRulesDto.getPercentageCorpus());
			policyAnnuityTempEntity.setCategory(policyRulesDto.getCategory());
			policyAnnuityTempEntity.setContributionType(policyRulesDto.getContributionType());
			policyAnnuityTempEntity.setBenifitPayableTo(policyRulesDto.getBenifitPayableTo());
			policyAnnuityTempEntity.setAnnuityOption(policyRulesDto.getAnnuityOption());
			policyAnnuityTempEntity.setCommutationBy(policyRulesDto.getCommutationBy());
			policyAnnuityTempEntity.setCommutationAmt(policyRulesDto.getCommutationAmt());
			policyAnnuityTempEntity.setNormalAgeRetirement(policyRulesDto.getNormalAgeRetirement());
			policyAnnuityTempEntity.setMinAgeRetirement(policyRulesDto.getMinAgeRetirement());
			policyAnnuityTempEntity.setMaxAgeRetirement(policyRulesDto.getMaxAgeRetirement());
			policyAnnuityTempEntity.setMinServicePension(policyRulesDto.getMinServicePension());
			policyAnnuityTempEntity.setMaxServicePension(policyRulesDto.getMaxServicePension());
			policyAnnuityTempEntity.setMinServiceWithdrawal(policyRulesDto.getMinServiceWithdrawal());
			policyAnnuityTempEntity.setMaxServiceWithdrawal(policyRulesDto.getMaxServiceWithdrawal());
			policyAnnuityTempEntity.setMinPension(policyRulesDto.getMinPension());
			policyAnnuityTempEntity.setMaxPension(policyRulesDto.getMaxPension());
			policyAnnuityTempEntity.setModifiedBy(policyRulesDto.getModifiedBy());
			policyAnnuityTempEntity.setModifiedOn(policyRulesDto.getModifiedOn());
			policyAnnuityTempEntity.setCreatedBy(policyRulesDto.getCreatedBy());
			policyAnnuityTempEntity.setCreatedOn(policyRulesDto.getCreatedOn());
			policyAnnuityTempEntity.setIsActive(policyRulesDto.getIsActive());
			rules.add(policyAnnuityTempEntity);
		}
		responsee.setRules(rules);
		Set<PolicyDepositDto> deposits = new HashSet<>();
		for (PolicyDepositOldDto policyDepositDto : masterDto.getDeposit()) {
			PolicyDepositDto policyDepositTempEntity = new PolicyDepositDto();
			policyDepositTempEntity.setDepositId(policyDepositDto.getDepositId());
			policyDepositTempEntity.setPolicyId(policyDepositDto.getPolicyId());
			policyDepositTempEntity.setCollectionNo(policyDepositDto.getCollectionNo());
			policyDepositTempEntity.setCollectionDate(policyDepositDto.getCollectionDate());
			policyDepositTempEntity.setCollectionStatus(policyDepositDto.getCollectionStatus());
//			policyDepositTempEntity.setChallanNo(policyDepositDto.getChallanNo());
			policyDepositTempEntity.setChallanNo(
					policyDepositDto.getChallanNo() != null && !"0".equals(policyDepositDto.getChallanNo())
							? policyDepositDto.getChallanNo()
							: policyServiceImpl.getChallanSeq());
			policyDepositTempEntity.setDepositAmount(policyDepositDto.getAmount());
			policyDepositTempEntity.setAdjustmentAmount(policyDepositDto.getAdjestmentAmount());
			policyDepositTempEntity.setAvailableAmount(policyDepositDto.getAvailableAmount());
			policyDepositTempEntity.setTransactionMode(policyDepositDto.getTransactionMode());
			policyDepositTempEntity.setChequeRealisationDate(policyDepositDto.getChequeRealisationDate());
			policyDepositTempEntity.setRemark(policyDepositDto.getRemark());
			policyDepositTempEntity.setStatus(policyDepositDto.getStatus());
			policyDepositTempEntity.setZeroId(policyDepositDto.getZeroId());
			policyDepositTempEntity.setModifiedBy(policyDepositDto.getModifiedBy());
			policyDepositTempEntity.setModifiedOn(policyDepositDto.getModifiedOn());
			policyDepositTempEntity.setCreatedBy(policyDepositDto.getCreatedBy());
			policyDepositTempEntity.setCreatedOn(policyDepositDto.getCreatedOn());
			policyDepositTempEntity.setIsActive(policyDepositDto.getIsActive());
			deposits.add(policyDepositTempEntity);
		}
		responsee.setDeposits(deposits);

		Set<PolicyContributionDto> policyContributions = new HashSet<>();
		for (PolicyContributionTempEntity policyContributionDto : policyContributionTempEntity) {

			PolicyContributionDto policyContributionDtos = new PolicyContributionDto();

			policyContributionDtos.setContributionId(policyContributionDto.getContributionId());
			policyContributionDtos.setPolicyId(policyContributionDto.getPolicyId());
			policyContributionDtos.setContReferenceNo(policyContributionDto.getContReferenceNo());
			policyContributionDtos.setContributionType(policyContributionDto.getContributionType());
			policyContributionDtos.setContributionDate(policyContributionDto.getContributionDate());
			policyContributionDtos.setOpeningBalance(policyContributionDto.getOpeningBalance());
			policyContributionDtos.setClosingBalance(policyContributionDto.getClosingBalance());
			policyContributionDtos.setEmployerContribution(policyContributionDto.getEmployerContribution());
			policyContributionDtos.setEmployeeContribution(policyContributionDto.getEmployeeContribution());
			policyContributionDtos.setVoluntaryContribution(policyContributionDto.getVoluntaryContribution());
			policyContributionDtos.setTotalContribution(policyContributionDto.getTotalContribution());
			policyContributionDtos.setIsDeposit(policyContributionDto.getIsDeposit());
			policyContributionDtos.setFinancialYear(policyContributionDto.getFinancialYear());
			policyContributionDtos.setQuarter(policyContributionDto.getQuarter());
			policyContributionDtos.setVersionNo(policyContributionDto.getVersionNo());
			policyContributionDtos.setCreatedOn(policyContributionDto.getCreatedOn());
			policyContributionDtos.setCreatedBy(policyContributionDto.getCreatedBy());
			policyContributionDtos.setModifiedOn(policyContributionDto.getModifiedOn());
			policyContributionDtos.setModifiedBy(policyContributionDto.getModifiedBy());
			policyContributionDtos.setIsActive(policyContributionDto.getIsActive());

			policyContributions.add(policyContributionDtos);
		}
		responsee.setPolicyContributions(policyContributions);

		Set<PolicyContributionSummaryDto> policyContributionSummary = new HashSet<>();

		for (PolicyContributionSummaryTempEntity policyContributionSummaryDto : policyContributionSummaryTempEntities) {

			PolicyContributionSummaryDto policyContributionSummaryTempEntity = new PolicyContributionSummaryDto();
			policyContributionSummaryTempEntity.setPolContSummaryId(policyContributionSummaryDto.getPolContSummaryId());
			policyContributionSummaryTempEntity.setPolicyId(policyContributionSummaryDto.getPolicyId());
			policyContributionSummaryTempEntity.setStampDuty(policyContributionSummaryDto.getStampDuty());
			policyContributionSummaryTempEntity.setOpeningBalance(policyContributionSummaryDto.getOpeningBalance());
			policyContributionSummaryTempEntity
					.setTotalEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
			policyContributionSummaryTempEntity
					.setTotalEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
			policyContributionSummaryTempEntity
					.setTotalVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
			policyContributionSummaryTempEntity
					.setTotalContribution(policyContributionSummaryDto.getTotalContribution());
			policyContributionSummaryTempEntity.setClosingBalance(policyContributionSummaryDto.getClosingBalance());
			policyContributionSummaryTempEntity.setFinancialYear(policyContributionSummaryDto.getFinancialYear());
			policyContributionSummaryTempEntity.setQuarter(policyContributionSummaryDto.getQuarter());
			policyContributionSummaryTempEntity.setIsActive(policyContributionSummaryDto.getIsActive());
			policyContributionSummaryTempEntity.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
			policyContributionSummaryTempEntity.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
			policyContributionSummaryTempEntity.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
			policyContributionSummaryTempEntity.setModifiedBy(policyContributionSummaryDto.getModifiedBy());

			policyContributionSummary.add(policyContributionSummaryTempEntity);
		}
		responsee.setPolicyContributionSummary(policyContributionSummary);

		Set<MemberMasterDto> memberMasterList = new HashSet<>();

		for (MemberMasterTempEntity request : memberMaster) {

			MemberMasterDto responseMember = new MemberMasterDto();

			responseMember.setMemberId(request.getMemberId());
			responseMember.setPolicyId(request.getPolicyId());
			responseMember.setMembershipNumber(request.getMembershipNumber());
			responseMember.setMemberStatus(request.getMemberStatus());
			responseMember.setLicId(request.getLicId());
			responseMember.setTypeOfMemebership(request.getTypeOfMemebership());
			responseMember.setAadharNumber(request.getAadharNumber());
			responseMember.setCategoryNo(request.getCategoryNo());
			responseMember.setCommunicationPreference(request.getCommunicationPreference());
			responseMember.setLanguagePreference(request.getLanguagePreference());
			responseMember.setDateOfBirth(request.getDateOfBirth());
			responseMember.setDateOfJoining(request.getDateOfJoining());
			responseMember.setDateOfJoiningScheme(request.getDateOfJoiningScheme());
			responseMember.setDateOfRetrirement(request.getDateOfRetrirement());
			responseMember.setDesignation(request.getDesignation());
			responseMember.setEmailid(request.getEmailid());
			responseMember.setGender(request.getGender());
			responseMember.setFatherName(request.getFatherName());
			responseMember.setFirstName(request.getFirstName());
			responseMember.setMiddleName(request.getMiddleName());
			responseMember.setLastName(request.getLastName());
			responseMember.setSpouseName(request.getSpouseName());
			responseMember.setMaritalStatus(request.getMaritalStatus());
			responseMember.setMemberPan(request.getMemberPan());
			responseMember.setLandlineNo(request.getLandlineNo());
			responseMember.setMobileNo(request.getMobileNo());
			responseMember.setIsZeroid(request.getIsZeroid());
			responseMember.setIsActive(request.getIsActive());
			responseMember.setCreatedBy(request.getCreatedBy());
			responseMember.setCreatedOn(request.getCreatedOn());
			responseMember.setModifiedBy(request.getModifiedBy());
			responseMember.setModifiedOn(request.getModifiedOn());
			Set<MemberAddressDto> memberAddressList = new HashSet<>();
			for (MemberAddressTempEntity dto : request.getMemberAddress()) {
				MemberAddressDto entity = new MemberAddressDto();
				entity.setAddressId(dto.getAddressId());
				entity.setMemberId(dto.getMemberId());
				entity.setPolicyId(dto.getPolicyId());
				entity.setAddress1(dto.getAddress1());
				entity.setAddress2(dto.getAddress2());
				entity.setAddress3(dto.getAddress3());
				entity.setAddressType(dto.getAddressType());
				entity.setCity(dto.getCity());
				entity.setDistrict(dto.getDistrict());
				entity.setStateName(dto.getStateName());
				entity.setCountry(dto.getCountry());
				entity.setPincode(dto.getPincode());
				entity.setIsActive(dto.getIsActive());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				memberAddressList.add(entity);
			}
			responseMember.setMemberAddress(memberAddressList);
			Set<MemberBankDto> memberBankList = new HashSet<>();
			for (MemberBankTempEntity dto : request.getMemberBank()) {
				MemberBankDto entity = new MemberBankDto();
				entity.setMemberBankId(dto.getMemberBankId());
				entity.setMemberId(dto.getMemberId());
				entity.setPolicyId(dto.getPolicyId());
				entity.setAccountNumber(dto.getAccountNumber());
				entity.setConfirmAccountNumber(dto.getConfirmAccountNumber());
				entity.setAccountType(dto.getAccountType());
				entity.setIfscCodeAvailable(dto.getIfscCodeAvailable());
				entity.setIfscCode(dto.getIfscCode());
				entity.setBankName(dto.getBankName());
				entity.setBankBranch(dto.getBankBranch());
				entity.setBankAddress(dto.getBankAddress());
				entity.setCountryCode(dto.getCountryCode());
				entity.setStdCode(dto.getStdCode());
				entity.setLandlineNumber(dto.getLandlineNumber());
				entity.setEmailId(dto.getEmailId());
				entity.setIsActive(dto.getIsActive());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				memberBankList.add(entity);
			}
			responseMember.setMemberBank(memberBankList);
			Set<MemberNomineeDto> memberNomineeList = new HashSet<>();
			for (MemberNomineeTempEntity dto : request.getMemberNominee()) {
				MemberNomineeDto entity = new MemberNomineeDto();
				entity.setNomineeId(dto.getNomineeId());
				entity.setMemberId(dto.getMemberId());
				entity.setNomineeType(dto.getNomineeType());
				entity.setNomineeName(dto.getNomineeName());
				entity.setRelationShip(dto.getRelationShip());
				entity.setAadharNumber(dto.getAadharNumber());
				entity.setPhone(dto.getPhone());
				entity.setEmailId(dto.getEmailId());
				entity.setAddressOne(dto.getAddressOne());
				entity.setAddressTwo(dto.getAddressTwo());
				entity.setAddressThree(dto.getAddressThree());
				entity.setCountry(dto.getCountry());
				entity.setPinCode(dto.getPinCode());
				entity.setDistrict(dto.getDistrict());
				entity.setState(dto.getState());
				entity.setIsActive(dto.getIsActive());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				entity.setPercentage(dto.getPercentage());
				entity.setAge(dto.getAge());
				entity.setDateOfBirth(dto.getDateOfBirth());
				memberNomineeList.add(entity);
			}
			responseMember.setMemberNominee(memberNomineeList);
			Set<MemberAppointeeDto> memberAppointeeList = new HashSet<>();
			for (MemberAppointeeTempEntity dto : request.getMemberAppointee()) {
				MemberAppointeeDto entity = new MemberAppointeeDto();
				entity.setAppointeeId(dto.getAppointeeId());
				entity.setMemberId(dto.getMemberId());
				entity.setAppointeeName(dto.getAppointeeName());
				entity.setAppointeeCode(dto.getAppointeeCode());
				entity.setRelationship(dto.getRelationship());
				entity.setContactNumber(dto.getContactNumber());
				entity.setDateOfBirth(dto.getDateOfBirth());
				entity.setPan(dto.getPan());
				entity.setAadharNumber(dto.getAadharNumber());
				entity.setIfscCode(dto.getIfscCode());
				entity.setBankName(dto.getBankName());
				entity.setAccountType(dto.getAccountType());
				entity.setAccountNumber(dto.getAccountNumber());
				entity.setIsActive(dto.getIsActive());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				memberAppointeeList.add(entity);
			}
			responseMember.setMemberAppointee(memberAppointeeList);
			Set<MemberContributionDto> memberContributionList = new HashSet<>();
			for (MemberContributionTempEntity dto : request.getMemberContribution()) {
				MemberContributionDto entity = new MemberContributionDto();
				entity.setMemberConId(dto.getMemberConId());
				entity.setMemberId(dto.getMemberId());
				entity.setPolicyConId(dto.getPolicyConId());
				entity.setPolicyId(dto.getPolicyId());
				entity.setContributionType(dto.getContributionType());
				entity.setContributionDate(dto.getContributionDate());
				entity.setLicId(dto.getLicId());
				entity.setOpeningBalance(dto.getOpeningBalance());
				entity.setEmployeeContribution(dto.getEmployeeContribution());
				entity.setEmployerContribution(dto.getEmployerContribution());
				entity.setVoluntaryContribution(dto.getVoluntaryContribution());
				entity.setTotalContribution(dto.getTotalContribution());
				entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
				entity.setClosingBalance(dto.getClosingBalance());
//				entity.setIsZeroAccount(dto.getIsZeroAccount());
				entity.setIsDeposit(dto.getIsDeposit());
				entity.setFinancialYear(dto.getFinancialYear());
				entity.setQuarter(dto.getQuarter());
				entity.setVersionNo(dto.getVersionNo());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setIsActive(dto.getIsActive());
				memberContributionList.add(entity);
			}
			responseMember.setMemberContribution(memberContributionList);

			Set<MemberContributionSummaryDto> memberContributionSummaryList = new HashSet<>();
			for (MemberContributionSummaryTempEntity dto : request.getMemberContributionSummary()) {
				MemberContributionSummaryDto entity = new MemberContributionSummaryDto();
				entity.setMemContSummaryId(dto.getMemContSummaryId());
				entity.setMemberId(dto.getMemberId());
				entity.setPolicyId(dto.getPolicyId());
				entity.setLicId(dto.getLicId());
				entity.setOpeningBalance(dto.getOpeningBalance());
				entity.setTotalEmployeeContribution(dto.getTotalEmployeeContribution());
				entity.setClosingBalance(dto.getClosingBalance());
				entity.setTotalEmployerContribution(dto.getTotalEmployerContribution());
				entity.setTotalVoluntaryContribution(dto.getTotalVoluntaryContribution());
				entity.setTotalContribution(dto.getTotalContribution());
				entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
				entity.setFinancialYear(dto.getFinancialYear());
				entity.setQuarter(dto.getQuarter());
				entity.setCreatedOn(dto.getCreatedOn());
				entity.setCreatedBy(dto.getCreatedBy());
				entity.setModifiedOn(dto.getModifiedOn());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setIsActive(dto.getIsActive());
				memberContributionSummaryList.add(entity);
			}
			responseMember.setMemberContributionSummary(memberContributionSummaryList);
			memberMasterList.add(responseMember);
		}
		responsee.setMemberMaster(memberMasterList);

//		Set<ZeroAccountdto> zeroAccount = new HashSet<>();
//		for (ZeroAccountTempEntity zeroAccountdto : masterDto.getZeroAccount()) {
//			ZeroAccountdto zeroAccountTempEntity = new ZeroAccountdto();
//			zeroAccountTempEntity.setZeroAccId(zeroAccountdto.getZeroAccId());
//			zeroAccountTempEntity.setPolicyId(zeroAccountdto.getPolicyId());
//			zeroAccountTempEntity.setZeroIdAmount(zeroAccountdto.getZeroIdAmount());
//			zeroAccountTempEntity.setIsActive(zeroAccountdto.getIsActive());
//			zeroAccountTempEntity.setCreatedOn(zeroAccountdto.getCreatedOn());
//			zeroAccountTempEntity.setCreatedBy(zeroAccountdto.getCreatedBy());
//			zeroAccountTempEntity.setModifiedOn(zeroAccountdto.getModifiedOn());
//			zeroAccountTempEntity.setModifiedBy(zeroAccountdto.getModifiedBy());
//			zeroAccount.add(zeroAccountTempEntity);
//		}
//		responsee.setZeroAccount(zeroAccount);
//		Set<ZeroAccountEntriesDto> zeroAccountEntries = new HashSet<>();
//		for (ZeroAccountEntriesTempEntity zeroAccountEntriesDto : masterDto.getZeroAccountEntries()) {
//			ZeroAccountEntriesDto zeroAccountEntriesTempEntity = new ZeroAccountEntriesDto();
//			zeroAccountEntriesTempEntity.setZeroAccEntId(zeroAccountEntriesDto.getZeroAccEntId());
//			zeroAccountEntriesTempEntity.setPolicyId(zeroAccountEntriesDto.getPolicyId());
//			zeroAccountEntriesTempEntity.setPolicyConId(zeroAccountEntriesDto.getPolicyConId());
//			zeroAccountEntriesTempEntity.setMemberConId(zeroAccountEntriesDto.getMemberConId());
//			zeroAccountEntriesTempEntity.setTransactionType(zeroAccountEntriesDto.getTransactionType());
//			zeroAccountEntriesTempEntity.setTransactionDate(zeroAccountEntriesDto.getTransactionDate());
//			zeroAccountEntriesTempEntity.setZeroIdAmount(zeroAccountEntriesDto.getZeroIdAmount());
//			zeroAccountEntriesTempEntity.setCreatedOn(zeroAccountEntriesDto.getCreatedOn());
//			zeroAccountEntriesTempEntity.setCreatedBy(zeroAccountEntriesDto.getCreatedBy());
//			zeroAccountEntriesTempEntity.setModifiedOn(zeroAccountEntriesDto.getModifiedOn());
//			zeroAccountEntriesTempEntity.setModifiedBy(zeroAccountEntriesDto.getModifiedBy());
//			zeroAccountEntriesTempEntity.setIsActive(zeroAccountEntriesDto.getIsActive());
//			zeroAccountEntries.add(zeroAccountEntriesTempEntity);
//		}
//		responsee.setZeroAccountEntries(zeroAccountEntries);

		Set<PolicyNotesDto> notes = new HashSet<>();
		for (PolicyNotesOldDto policyNotesDto : masterDto.getNotes()) {
			PolicyNotesDto policyNotesTempEntity = new PolicyNotesDto();
			policyNotesTempEntity.setPolicyNoteId(policyNotesDto.getPolicyNoteId());
			policyNotesTempEntity.setPolicyId(policyNotesDto.getPolicyId());
			policyNotesTempEntity.setNoteContents(policyNotesDto.getNoteContents());
			policyNotesTempEntity.setModifiedBy(policyNotesDto.getModifiedBy());
			policyNotesTempEntity.setModifiedOn(policyNotesDto.getModifiedOn());
			policyNotesTempEntity.setCreatedBy(policyNotesDto.getCreatedBy());
			policyNotesTempEntity.setCreatedOn(policyNotesDto.getCreatedOn());
			policyNotesTempEntity.setIsActive(policyNotesDto.getIsActive());
			notes.add(policyNotesTempEntity);
		}
		responsee.setNotes(notes);

		policyMaster.add(responsee);
		response.setPolicyMaster(policyMaster);
		return response;
	}

//MPH DTO TO POLICY DTO	
	public PolicyDto convertNewResponseToOldResponse(MphMasterDto request) {

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
			response.setQuotationNo(getQuotationNumber(response.getQuotationId()));
			response.setStampDuty(policyMasterdto.getStampDuty());

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
				depositList.add(oldDto);
			}
			response.setDeposit(depositList);
			for (PolicyDepositDto policyContributionSummaryDto : policyMasterdto.getDeposits()) {
				PolicyAdjustmentOldDto oldDto = new PolicyAdjustmentOldDto();
				oldDto.setAdjestmentId(policyContributionSummaryDto.getDepositId());
				oldDto.setChallanNo(policyContributionSummaryDto.getChallanNo());
				oldDto.setDepositId(policyContributionSummaryDto.getDepositId());
				oldDto.setPolicyId(policyContributionSummaryDto.getPolicyId());
				oldDto.setCollectionNo(policyContributionSummaryDto.getCollectionNo());
				oldDto.setCollectionDate(policyContributionSummaryDto.getCollectionDate());
				oldDto.setAmount(policyContributionSummaryDto.getDepositAmount());
				oldDto.setAdjestmentAmount(policyContributionSummaryDto.getAdjustmentAmount());
				oldDto.setAvailableAmount(policyContributionSummaryDto.getAvailableAmount());
				oldDto.setTransactionMode(policyContributionSummaryDto.getTransactionMode());
				oldDto.setCollectionStatus(policyContributionSummaryDto.getCollectionStatus());
				oldDto.setChequeRealisationDate(policyContributionSummaryDto.getChequeRealisationDate());
				oldDto.setRemark(policyContributionSummaryDto.getRemark());
				oldDto.setStatus(policyContributionSummaryDto.getStatus());
				oldDto.setZeroId(policyContributionSummaryDto.getZeroId());
				oldDto.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
				oldDto.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
				oldDto.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
				oldDto.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
				oldDto.setIsActive(policyContributionSummaryDto.getIsActive());
				adjustmentsList.add(oldDto);
			}
			response.setAdjustments(adjustmentsList);
//			for (PolicyContributionDto policyContributionDto : policyMasterdto.getPolicyContributions()) {
////				response.setAmountToBeAdjusted(policyContributionDto.getAmountToBeAdjusted());
////				response.setFirstPremium(policyContributionDto.getFirstPremium());
////				response.setSinglePremiumFirstYr(policyContributionDto.getSinglePremiumFirstYr());
////				response.setRenewalPremium(policyContributionDto.getRenewalPremium());
////				response.setSubsequentSinglePremium(policyContributionDto.getSubsequentSinglePremium());
////				response.setStampDuty(policyContributionDto.getStampDuty());
//				response.setPolicyDepositDate(policyContributionDto.getContributionDate());
////				response.setStampDuty(policyContributionDto.getStampDuty);
//			}

//			for (PolicyContributionSummaryDto policyContributionSummaryDto : policyMasterdto
//					.getPolicyContributionSummary()) {
//				response.setTotalDeposit(policyContributionSummaryDto.getClosingBalance());
//				response.setTotalContribution(policyContributionSummaryDto.getTotalContribution());
//				response.setEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
//				response.setEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
//				response.setVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
//				response.setStampDuty(policyContributionSummaryDto.getStampDuty());
//			}

			String financialYear = DateUtils.getFinancialYrByDt(new Date());
			for (PolicyContributionSummaryDto policyContributionSummaryDto : policyMasterdto
					.getPolicyContributionSummary()) {

				if (financialYear.equalsIgnoreCase(policyContributionSummaryDto.getFinancialYear())) {

					response.setTotalDeposit(policyContributionSummaryDto.getClosingBalance());
					response.setTotalContribution(policyContributionSummaryDto.getTotalContribution());
					response.setEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
					response.setEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
					response.setVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
//					response.setStampDuty(policyContributionSummaryDto.getStampDuty());

				}

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

			Set<PolicyMbrOldDto> membersList = new HashSet<>();
			for (MemberMasterDto memberMasterDto : policyMasterdto.getMemberMaster()) {
				PolicyMbrOldDto oldDto = new PolicyMbrOldDto();

				oldDto.setMemberId(memberMasterDto.getMemberId());
				oldDto.setAadharNumber(memberMasterDto.getAadharNumber());
				oldDto.setMemberId(memberMasterDto.getMemberId());
				oldDto.setAadharNumber(memberMasterDto.getAadharNumber());
				oldDto.setCommunicationPreference(memberMasterDto.getCommunicationPreference());
				oldDto.setCreatedBy(memberMasterDto.getCreatedBy());
				oldDto.setCreatedOn(memberMasterDto.getCreatedOn());
				oldDto.setDateOfBirth(memberMasterDto.getDateOfBirth());
				oldDto.setDateOfJoining(memberMasterDto.getDateOfJoining());
				oldDto.setDateOfJoiningScheme(memberMasterDto.getDateOfJoiningScheme());
				oldDto.setDesignation(memberMasterDto.getDesignation());
				oldDto.setFatherName(memberMasterDto.getFatherName());
				oldDto.setFirstName(memberMasterDto.getFirstName());
				oldDto.setGender(memberMasterDto.getGender());
				oldDto.setIsActive(memberMasterDto.getIsActive());
				oldDto.setLanguagePreference(memberMasterDto.getLanguagePreference());
				oldDto.setLastName(memberMasterDto.getLastName());
				oldDto.setMaritalStatus(memberMasterDto.getMaritalStatus());

				oldDto.setLicId(memberMasterDto.getLicId());
				oldDto.setMembershipNumber(memberMasterDto.getMembershipNumber());
				oldDto.setMemberShipId(memberMasterDto.getMembershipNumber());
				oldDto.setCategory(memberMasterDto.getCategoryNo());

				oldDto.setMemberStatus(memberMasterDto.getMemberStatus());
				oldDto.setMiddleName(memberMasterDto.getMiddleName());
				oldDto.setModifiedBy(memberMasterDto.getModifiedBy());
				oldDto.setModifiedOn(memberMasterDto.getModifiedOn());
				oldDto.setPolicyId(memberMasterDto.getPolicyId());
				oldDto.setSpouseName(memberMasterDto.getSpouseName());

				for (MemberContributionSummaryDto MemberContributionDto : memberMasterDto
						.getMemberContributionSummary()) {
					oldDto.setEmployeeContribution(MemberContributionDto.getTotalEmployeeContribution());
					oldDto.setEmployerContribution(MemberContributionDto.getTotalEmployerContribution());
					oldDto.setVoluntaryContribution(MemberContributionDto.getTotalVoluntaryContribution());
					oldDto.setTotalContribution(MemberContributionDto.getTotalContribution());
					oldDto.setTotalInterestedAccured(MemberContributionDto.getTotalInterestedAccured());
				}
				Set<PolicyMbrAddressOldDto> policyMbrAddressOldDtoList = new HashSet<>();
				for (MemberAddressDto memberAddressDto : memberMasterDto.getMemberAddress()) {
					PolicyMbrAddressOldDto oldChildDto = new PolicyMbrAddressOldDto();
					oldChildDto.setAddressId(memberAddressDto.getAddressId());
					oldChildDto.setMemberId(memberAddressDto.getMemberId());
					oldChildDto.setAddressLineOne(memberAddressDto.getAddress1());
					oldChildDto.setAddressLineTwo(memberAddressDto.getAddress2());
					oldChildDto.setAddressLineThree(memberAddressDto.getAddress3());
					oldChildDto.setAddressType(memberAddressDto.getAddressType());
					oldChildDto.setDistrict(memberAddressDto.getDistrict());
					oldChildDto.setState(memberAddressDto.getStateName());
					oldChildDto.setCountry(memberAddressDto.getCountry());
					oldChildDto.setPinCode(NumericUtils.convertIntegerToLong(memberAddressDto.getPincode()));
					oldChildDto.setIsActive(memberAddressDto.getIsActive());
					oldChildDto.setCreatedBy(memberAddressDto.getCreatedBy());
					oldChildDto.setCreatedOn(memberAddressDto.getCreatedOn());
					oldChildDto.setModifiedBy(memberAddressDto.getModifiedBy());
					oldChildDto.setModifiedOn(memberAddressDto.getModifiedOn());
					policyMbrAddressOldDtoList.add(oldChildDto);
				}
				oldDto.setAddress(policyMbrAddressOldDtoList);
				Set<PolicyMbrBankOldDto> PolicyMbrBankOldDtoList = new HashSet<>();
				for (MemberBankDto memberBankDto : memberMasterDto.getMemberBank()) {
					PolicyMbrBankOldDto oldChildDto = new PolicyMbrBankOldDto();
					oldChildDto.setMemberId(memberBankDto.getMemberId());
					oldChildDto.setAccountNumber(memberBankDto.getAccountNumber());
					oldChildDto.setConfirmAccountNumber(memberBankDto.getConfirmAccountNumber());
					oldChildDto.setAccountType(memberBankDto.getAccountType());
					oldChildDto.setIfscCodeAvailable(memberBankDto.getIfscCodeAvailable());
					oldChildDto.setIfscCode(memberBankDto.getIfscCode());
					oldChildDto.setBankName(memberBankDto.getBankName());
					oldChildDto.setBankBranch(memberBankDto.getBankBranch());
					oldChildDto.setBankAddress(memberBankDto.getBankAddress());
					oldChildDto.setCountryCode(NumericUtils.convertIntegerToString(memberBankDto.getCountryCode()));
					oldChildDto.setStdCode(NumericUtils.convertIntegerToLong(memberBankDto.getStdCode()));
					oldChildDto.setLandlineNumber(memberBankDto.getLandlineNumber());
					oldChildDto.setEmailId(memberBankDto.getEmailId());
					oldChildDto.setIsActive(memberBankDto.getIsActive());
					oldChildDto.setCreatedBy(memberBankDto.getCreatedBy());
					oldChildDto.setCreatedOn(memberBankDto.getCreatedOn());
					oldChildDto.setModifiedBy(memberBankDto.getModifiedBy());
					oldChildDto.setModifiedOn(memberBankDto.getModifiedOn());
					PolicyMbrBankOldDtoList.add(oldChildDto);
				}
				oldDto.setBank(PolicyMbrBankOldDtoList);
				Set<PolicyMbrApponinteeOldDto> policyMbrApponinteeOldDtoList = new HashSet<>();
				for (MemberAppointeeDto memberNomineeDto : memberMasterDto.getMemberAppointee()) {
					PolicyMbrApponinteeOldDto oldChildDto = new PolicyMbrApponinteeOldDto();
					oldChildDto.setMemberId(memberNomineeDto.getMemberId());
					oldChildDto.setAppointeeName(memberNomineeDto.getAppointeeName());
					oldChildDto.setAppointeeCode(memberNomineeDto.getAppointeeCode());
					oldChildDto.setRelationship(memberNomineeDto.getRelationship());
					oldChildDto.setContactNumber(memberNomineeDto.getContactNumber());
					oldChildDto.setDateOfBirth(memberNomineeDto.getDateOfBirth());
					oldChildDto.setPan(memberNomineeDto.getPan());
					oldChildDto.setAadharNumber(memberNomineeDto.getAadharNumber());
					oldChildDto.setIfscCode(memberNomineeDto.getIfscCode());
					oldChildDto.setBankName(memberNomineeDto.getBankName());
					oldChildDto.setAccountType(memberNomineeDto.getAccountType());
					oldChildDto.setAccountNumber(NumericUtils.convertLongToString(memberNomineeDto.getAccountNumber()));
					oldChildDto.setIsActive(memberNomineeDto.getIsActive());
					oldChildDto.setCreatedBy(memberNomineeDto.getCreatedBy());
					oldChildDto.setCreatedOn(memberNomineeDto.getCreatedOn());
					oldChildDto.setModifiedBy(memberNomineeDto.getModifiedBy());
					oldChildDto.setModifiedOn(memberNomineeDto.getModifiedOn());
					policyMbrApponinteeOldDtoList.add(oldChildDto);
				}
				oldDto.setAppointee(policyMbrApponinteeOldDtoList);
				Set<PolicyMbrNomineeOldDto> policyMbrNomineeOldDtoList = new HashSet<>();
				for (MemberNomineeDto memberNomineeDto : memberMasterDto.getMemberNominee()) {
					PolicyMbrNomineeOldDto oldChildDto = new PolicyMbrNomineeOldDto();
					oldChildDto.setNomineeId(memberNomineeDto.getNomineeId());
					oldChildDto.setMemberId(memberNomineeDto.getMemberId());
					oldChildDto.setNomineeType(memberNomineeDto.getNomineeType());
					oldChildDto.setNomineeName(memberNomineeDto.getNomineeName());
					oldChildDto.setRelationShip(memberNomineeDto.getRelationShip());
					oldChildDto.setAadharNumber(memberNomineeDto.getAadharNumber());
					oldChildDto.setPhone(memberNomineeDto.getPhone());
					oldChildDto.setEmailId(memberNomineeDto.getEmailId());
					oldChildDto.setAddressOne(memberNomineeDto.getAddressOne());
					oldChildDto.setAddressTwo(memberNomineeDto.getAddressTwo());
					oldChildDto.setAddressThree(memberNomineeDto.getAddressThree());
					oldChildDto.setCountry(memberNomineeDto.getCountry());
					oldChildDto.setPinCode(memberNomineeDto.getPinCode());
					oldChildDto.setDistrict(memberNomineeDto.getDistrict());
					oldChildDto.setState(memberNomineeDto.getState());
					oldChildDto.setIsActive(memberNomineeDto.getIsActive());
					oldChildDto.setCreatedBy(memberNomineeDto.getCreatedBy());
					oldChildDto.setCreatedOn(memberNomineeDto.getCreatedOn());
					oldChildDto.setModifiedBy(memberNomineeDto.getModifiedBy());
					oldChildDto.setModifiedOn(memberNomineeDto.getModifiedOn());
					oldChildDto.setPercentage(NumericUtils.convertStringToInteger(memberNomineeDto.getPercentage()));
					oldChildDto.setAge(memberNomineeDto.getAge());
					oldChildDto.setDateOfBirth(memberNomineeDto.getDateOfBirth());
					policyMbrNomineeOldDtoList.add(oldChildDto);
				}
				oldDto.setNominee(policyMbrNomineeOldDtoList);
				membersList.add(oldDto);
			}
			response.setMembers(membersList);
		}

		return response;
	}

//	TEMP TO MAIN TABLE MOVE STARTS
	public MphMasterEntity convertMphMasterTempEntityToMphMasterMasterEntityNew(MphMasterTempEntity masterDto,
			String status, String policyNumber, String variantType) {
		MphMasterEntity response = new MphMasterEntity();
		response.setMphId(null);
		response.setTempMphId(masterDto.getMphId());
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
//		response.setIsActive(masterDto.getIsActive());
		response.setCreatedOn(masterDto.getCreatedOn());
		response.setCreatedBy(masterDto.getCreatedBy());
		response.setModifiedOn(masterDto.getModifiedOn());
		response.setModifiedBy(masterDto.getModifiedBy());

		if (Objects.equals(status, PolicyConstants.APPROVED)) {
			response.setIsActive(Boolean.TRUE);
		} else if (Objects.equals(status, PolicyConstants.REJECT)) {
			response.setIsActive(Boolean.FALSE);
		}

		Set<MphAddressEntity> mphAddress = new HashSet<>();
		for (MphAddressTempEntity mphAddressDto : masterDto.getMphAddress()) {
			MphAddressEntity mphAddressTempEntity = new MphAddressEntity();
			mphAddressTempEntity.setAddressId(null);
			mphAddressTempEntity.setMphId(response.getMphId());
			mphAddressTempEntity.setAddressType(mphAddressDto.getAddressType());
			mphAddressTempEntity.setAddressLine1(mphAddressDto.getAddressLine1());
			mphAddressTempEntity.setAddressLine2(mphAddressDto.getAddressLine2());
			mphAddressTempEntity.setAddressLine3(mphAddressDto.getAddressLine3());
			mphAddressTempEntity.setPincode(mphAddressDto.getPincode());
			mphAddressTempEntity.setCityLocality(mphAddressDto.getCityLocality());
			mphAddressTempEntity.setCityId(mphAddressDto.getCityId());
			mphAddressTempEntity.setDistrict(mphAddressDto.getDistrict());
			mphAddressTempEntity.setDistrictId(mphAddressDto.getDistrictId());
			mphAddressTempEntity.setCountryId(mphAddressDto.getCountryId());
			mphAddressTempEntity.setStateId(mphAddressDto.getStateId());
			mphAddressTempEntity.setStateName(mphAddressDto.getStateName());
			mphAddressTempEntity.setIsActive(mphAddressDto.getIsActive());
			mphAddressTempEntity.setIsDefault(mphAddressDto.getIsDefault());
			mphAddressTempEntity.setCreatedOn(mphAddressDto.getCreatedOn());
			mphAddressTempEntity.setCreatedBy(mphAddressDto.getCreatedBy());
			mphAddressTempEntity.setModifiedOn(mphAddressDto.getModifiedOn());
			mphAddressTempEntity.setModifiedBy(mphAddressDto.getModifiedBy());
			mphAddress.add(mphAddressTempEntity);
		}
		response.setMphAddress(mphAddress);
		Set<MphBankEntity> mphBank = new HashSet<>();
		for (MphBankTempEntity mphBankDto : masterDto.getMphBank()) {
			MphBankEntity mphBankTempEntity = new MphBankEntity();
			mphBankTempEntity.setMphBankId(null);
			mphBankTempEntity.setMphId(response.getMphId());
			mphBankTempEntity.setAccountNumber(mphBankDto.getAccountNumber());
			mphBankTempEntity.setConfirmAccountNumber(mphBankDto.getConfirmAccountNumber());
			mphBankTempEntity.setAccountType(mphBankDto.getAccountType());
			mphBankTempEntity.setIfscCodeAvailable(mphBankDto.getIfscCodeAvailable());
			mphBankTempEntity.setIfscCode(mphBankDto.getIfscCode());
			mphBankTempEntity.setBankName(mphBankDto.getBankName());
			mphBankTempEntity.setBankBranch(mphBankDto.getBankBranch());
			mphBankTempEntity.setBankAddress(mphBankDto.getBankAddress());
			mphBankTempEntity.setStdCode(mphBankDto.getStdCode());
			mphBankTempEntity.setLandlineNumber(mphBankDto.getLandlineNumber());
			mphBankTempEntity.setEmailId(mphBankDto.getEmailId());
			mphBankTempEntity.setCountryCode(mphBankDto.getCountryCode());
			mphBankTempEntity.setCountryId(mphBankDto.getCountryId());
			mphBankTempEntity.setStateId(mphBankDto.getStateId());
			mphBankTempEntity.setDistrictId(mphBankDto.getDistrictId());
			mphBankTempEntity.setCityId(mphBankDto.getCityId());
			mphBankTempEntity.setTownLocality(mphBankDto.getTownLocality());
			mphBankTempEntity.setIsActive(mphBankDto.getIsActive());
			mphBankTempEntity.setIsDefault(mphBankDto.getIsDefault());
			mphBankTempEntity.setCreatedBy(mphBankDto.getCreatedBy());
			mphBankTempEntity.setCreatedOn(mphBankDto.getCreatedOn());
			mphBankTempEntity.setModifiedOn(mphBankDto.getModifiedOn());
			mphBankTempEntity.setModifiedBy(mphBankDto.getModifiedBy());
			mphBank.add(mphBankTempEntity);
		}
		response.setMphBank(mphBank);
		Set<MphRepresentativesEntity> mphRepresentative = new HashSet<>();
		for (MphRepresentativesTempEntity mphRepresentativeDto : masterDto.getMphRepresentative()) {
			MphRepresentativesEntity mphRepresentativesTempEntity = new MphRepresentativesEntity();
			mphRepresentativesTempEntity.setRepId(null);
			mphRepresentativesTempEntity.setMphId(response.getMphId());
			mphRepresentativesTempEntity.setRepresentativeCode(mphRepresentativeDto.getRepresentativeCode());
			mphRepresentativesTempEntity.setRepresentativeName(mphRepresentativeDto.getRepresentativeName());
			mphRepresentativesTempEntity.setLandlineNo(mphRepresentativeDto.getLandlineNo());
			mphRepresentativesTempEntity.setCountryCode(mphRepresentativeDto.getCountryCode());
			mphRepresentativesTempEntity.setMobileNo(mphRepresentativeDto.getMobileNo());
			mphRepresentativesTempEntity.setEmailId(mphRepresentativeDto.getEmailId());
			mphRepresentativesTempEntity.setAddressType(mphRepresentativeDto.getAddressType());
			mphRepresentativesTempEntity.setAddressLine1(mphRepresentativeDto.getAddressLine1());
			mphRepresentativesTempEntity.setAddressLine2(mphRepresentativeDto.getAddressLine2());
			mphRepresentativesTempEntity.setAddressLine3(mphRepresentativeDto.getAddressLine3());
			mphRepresentativesTempEntity.setCityLocality(mphRepresentativeDto.getCityLocality());
			mphRepresentativesTempEntity.setDistrict(mphRepresentativeDto.getDistrict());
			mphRepresentativesTempEntity.setStateName(mphRepresentativeDto.getStateName());
			mphRepresentativesTempEntity.setPincode(mphRepresentativeDto.getPincode());
			mphRepresentativesTempEntity.setIsActive(mphRepresentativeDto.getIsActive());
			mphRepresentativesTempEntity.setCreatedOn(mphRepresentativeDto.getCreatedOn());
			mphRepresentativesTempEntity.setCreatedBy(mphRepresentativeDto.getCreatedBy());
			mphRepresentativesTempEntity.setModifiedOn(mphRepresentativeDto.getModifiedOn());
			mphRepresentativesTempEntity.setModifiedBy(mphRepresentativeDto.getModifiedBy());
			mphRepresentative.add(mphRepresentativesTempEntity);
		}
		response.setMphRepresentative(mphRepresentative);

		Set<PolicyMasterEntity> policyMaster = new HashSet<>();
		for (PolicyMasterTempEntity policyDto : masterDto.getPolicyMaster()) {
			PolicyMasterEntity policyMasterTempEntity = convertPolicyMasterTempEntityToPolicyMasterEntity(policyDto,
					status, response.getMphId(), policyNumber, variantType);
			policyMaster.add(policyMasterTempEntity);
		}
		response.setPolicyMaster(policyMaster);
		return response;
	}

	public PolicyMasterEntity convertPolicyMasterTempEntityToPolicyMasterEntity(PolicyMasterTempEntity policyMasterDto,
			String status, Long mphId, String policyNumber, String variantType) {
		PolicyMasterEntity response = new PolicyMasterEntity();
		response.setPolicyId(null);
		response.setTempPolicyId(policyMasterDto.getPolicyId());
		response.setMphId(mphId);
//		response.setTempMphId(tempMphId);

		if (Objects.equals(status, PolicyConstants.APPROVED)) {
			response.setPolicyStatus(PolicyConstants.APPROVED_NO);
			response.setPolicyNumber(policyNumber);
			response.setIsActive(Boolean.TRUE);
		} else if (Objects.equals(status, PolicyConstants.REJECT)) {
			response.setPolicyStatus(PolicyConstants.REJECTED_NO);
			response.setIsActive(Boolean.FALSE);
			response.setPolicyNumber(null);
		}
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
//		response.setIsActive(policyMasterDto.getIsActive());
		response.setCreatedBy(policyMasterDto.getCreatedBy());
		response.setCreatedOn(policyMasterDto.getCreatedOn());
		response.setModifiedBy(policyMasterDto.getModifiedBy());
		response.setModifiedOn(policyMasterDto.getModifiedOn());
		response.setAmountToBeAdjusted(policyMasterDto.getAmountToBeAdjusted());
		response.setFirstPremium(policyMasterDto.getFirstPremium());
		response.setSinglePremiumFirstYr(policyMasterDto.getSinglePremiumFirstYr());
		response.setRenewalPremium(policyMasterDto.getRenewalPremium());
		response.setSubsequentSinglePremium(policyMasterDto.getSubsequentSinglePremium());
		response.setStampDuty(policyMasterDto.getStampDuty());
		policyMasterRepository.save(response);

		Set<PolicyValuationEntity> valuations = new HashSet<>();
		for (PolicyValuationTempEntity policyValuationDto : policyMasterDto.getValuations()) {
			PolicyValuationEntity policyValuationTempEntity = new PolicyValuationEntity();
			policyValuationTempEntity.setValuationId(null);
			policyValuationTempEntity.setPolicyId(response.getPolicyId());
			policyValuationTempEntity.setValuationType(policyValuationDto.getValuationType());
			policyValuationTempEntity.setAttritionRate(policyValuationDto.getAttritionRate());
			policyValuationTempEntity.setSalaryEscalation(policyValuationDto.getSalaryEscalation());
			policyValuationTempEntity.setDeathRate(policyValuationDto.getDeathRate());
			policyValuationTempEntity.setDisRateIntrest(policyValuationDto.getDisRateIntrest());
			policyValuationTempEntity.setDisRateSalaryEsc(policyValuationDto.getDisRateSalaryEsc());
			policyValuationTempEntity.setAnnuityOption(policyValuationDto.getAnnuityOption());
			policyValuationTempEntity.setAccuralRateFactor(policyValuationDto.getAccuralRateFactor());
			policyValuationTempEntity.setMinPension(policyValuationDto.getMinPension());
			policyValuationTempEntity.setMaxPension(policyValuationDto.getMaxPension());
			policyValuationTempEntity.setWithdrawalRate(policyValuationDto.getWithdrawalRate());
			policyValuationTempEntity.setIsActive(policyValuationDto.getIsActive());
			policyValuationTempEntity.setCreatedOn(policyValuationDto.getCreatedOn());
			policyValuationTempEntity.setCreatedBy(policyValuationDto.getCreatedBy());
			policyValuationTempEntity.setModifiedOn(policyValuationDto.getModifiedOn());
			policyValuationTempEntity.setModifiedBy(policyValuationDto.getModifiedBy());
			valuations.add(policyValuationTempEntity);
		}
		response.setValuations(valuations);
		Set<PolicyRulesEntity> rules = new HashSet<>();
		for (PolicyRulesTempEntity policyRulesDto : policyMasterDto.getRules()) {
			PolicyRulesEntity policyAnnuityTempEntity = new PolicyRulesEntity();
			policyAnnuityTempEntity.setRuleId(null);
			policyAnnuityTempEntity.setPolicyId(response.getPolicyId());
			policyAnnuityTempEntity.setEffectiveFrom(policyRulesDto.getEffectiveFrom());
			policyAnnuityTempEntity.setPercentageCorpus(policyRulesDto.getPercentageCorpus());
			policyAnnuityTempEntity.setCategory(policyRulesDto.getCategory());
			policyAnnuityTempEntity.setContributionType(policyRulesDto.getContributionType());
			policyAnnuityTempEntity.setBenifitPayableTo(policyRulesDto.getBenifitPayableTo());
			policyAnnuityTempEntity.setAnnuityOption(policyRulesDto.getAnnuityOption());
			policyAnnuityTempEntity.setCommutationBy(policyRulesDto.getCommutationBy());
			policyAnnuityTempEntity.setCommutationAmt(policyRulesDto.getCommutationAmt());
			policyAnnuityTempEntity.setNormalAgeRetirement(policyRulesDto.getNormalAgeRetirement());
			policyAnnuityTempEntity.setMinAgeRetirement(policyRulesDto.getMinAgeRetirement());
			policyAnnuityTempEntity.setMaxAgeRetirement(policyRulesDto.getMaxAgeRetirement());
			policyAnnuityTempEntity.setMinServicePension(policyRulesDto.getMinServicePension());
			policyAnnuityTempEntity.setMaxServicePension(policyRulesDto.getMaxServicePension());
			policyAnnuityTempEntity.setMinServiceWithdrawal(policyRulesDto.getMinServiceWithdrawal());
			policyAnnuityTempEntity.setMaxServiceWithdrawal(policyRulesDto.getMaxServiceWithdrawal());
			policyAnnuityTempEntity.setMinPension(policyRulesDto.getMinPension());
			policyAnnuityTempEntity.setMaxPension(policyRulesDto.getMaxPension());
			policyAnnuityTempEntity.setModifiedBy(policyRulesDto.getModifiedBy());
			policyAnnuityTempEntity.setModifiedOn(policyRulesDto.getModifiedOn());
			policyAnnuityTempEntity.setCreatedBy(policyRulesDto.getCreatedBy());
			policyAnnuityTempEntity.setCreatedOn(policyRulesDto.getCreatedOn());
			policyAnnuityTempEntity.setIsActive(policyRulesDto.getIsActive());
			rules.add(policyAnnuityTempEntity);
		}
		response.setRules(rules);
		Set<PolicyDepositEntity> deposits = new HashSet<>();
		for (PolicyDepositTempEntity policyDepositDto : policyMasterDto.getDeposits()) {
			PolicyDepositEntity policyDepositTempEntity = new PolicyDepositEntity();
			policyDepositTempEntity.setDepositId(null);
			policyDepositTempEntity.setPolicyId(response.getPolicyId());

			policyDepositTempEntity.setTempPolicyId(policyDepositDto.getPolicyId());
			policyDepositTempEntity.setAdjustmentContributionId(policyDepositDto.getAdjustmentContributionId());
			policyDepositTempEntity.setContributionType(policyDepositDto.getContributionType());

			policyDepositTempEntity.setCollectionNo(policyDepositDto.getCollectionNo());
			policyDepositTempEntity.setCollectionDate(policyDepositDto.getCollectionDate());
			policyDepositTempEntity.setCollectionStatus(policyDepositDto.getCollectionStatus());
			policyDepositTempEntity.setChallanNo(policyDepositDto.getChallanNo());
			policyDepositTempEntity.setDepositAmount(policyDepositDto.getDepositAmount());
			policyDepositTempEntity.setAdjustmentAmount(policyDepositDto.getAdjustmentAmount());
			policyDepositTempEntity.setAvailableAmount(policyDepositDto.getAvailableAmount());
			policyDepositTempEntity.setTransactionMode(policyDepositDto.getTransactionMode());
			policyDepositTempEntity.setChequeRealisationDate(policyDepositDto.getChequeRealisationDate());
			policyDepositTempEntity.setRemark(policyDepositDto.getRemark());
			policyDepositTempEntity.setStatus(policyDepositDto.getStatus());
			policyDepositTempEntity.setZeroId(policyDepositDto.getZeroId());
			policyDepositTempEntity.setIsDeposit(policyDepositDto.getIsDeposit());
			policyDepositTempEntity.setModifiedBy(policyDepositDto.getModifiedBy());
			policyDepositTempEntity.setModifiedOn(policyDepositDto.getModifiedOn());
			policyDepositTempEntity.setCreatedBy(policyDepositDto.getCreatedBy());
			policyDepositTempEntity.setCreatedOn(policyDepositDto.getCreatedOn());
			policyDepositTempEntity.setIsActive(policyDepositDto.getIsActive());
			deposits.add(policyDepositTempEntity);
		}
		response.setDeposits(deposits);

		Set<PolicyContributionEntity> policyContributions = new HashSet<>();
		for (PolicyContributionTempEntity policyContributionDto : policyMasterDto.getPolicyContributions()) {
			PolicyContributionEntity policyContributionTempEntity = new PolicyContributionEntity();
			policyContributionTempEntity.setContributionId(null);
			policyContributionTempEntity.setPolicyId(response.getPolicyId());

			policyContributionTempEntity.setTempPolicyId(policyContributionDto.getPolicyId());
			policyContributionTempEntity
					.setAdjustmentContributionId(policyContributionDto.getAdjustmentContributionId());

//			if (Objects.equals(status, PolicyConstants.APPROVED)) {
			if (variantType.equalsIgnoreCase("V2")) {
				policyContributionTempEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
			} else {
				policyContributionTempEntity.setQuarter("Q0");
			}
//			} else if (Objects.equals(status, PolicyConstants.REJECT)) {
//				if (variantType.equalsIgnoreCase("V2")) {
//					policyContributionTempEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
//				} else {
//					policyContributionTempEntity.setQuarter("Q0");
//				}
//			}			
			policyContributionTempEntity.setContReferenceNo(policyContributionDto.getContReferenceNo());
			policyContributionTempEntity.setContributionType(policyContributionDto.getContributionType());
			policyContributionTempEntity.setContributionDate(policyContributionDto.getContributionDate());
			policyContributionTempEntity.setOpeningBalance(policyContributionDto.getOpeningBalance());
			policyContributionTempEntity.setClosingBalance(policyContributionDto.getClosingBalance());
			policyContributionTempEntity.setEmployerContribution(policyContributionDto.getEmployerContribution());
			policyContributionTempEntity.setEmployeeContribution(policyContributionDto.getEmployeeContribution());
			policyContributionTempEntity.setVoluntaryContribution(policyContributionDto.getVoluntaryContribution());
			policyContributionTempEntity.setTotalContribution(policyContributionDto.getTotalContribution());
			policyContributionTempEntity.setIsDeposit(policyContributionDto.getIsDeposit());
			policyContributionTempEntity.setFinancialYear(policyContributionDto.getFinancialYear());
			policyContributionTempEntity.setVersionNo(policyContributionDto.getVersionNo());
			policyContributionTempEntity.setCreatedOn(policyContributionDto.getCreatedOn());
			policyContributionTempEntity.setCreatedBy(policyContributionDto.getCreatedBy());
			policyContributionTempEntity.setModifiedOn(policyContributionDto.getModifiedOn());
			policyContributionTempEntity.setModifiedBy(policyContributionDto.getModifiedBy());
			policyContributionTempEntity.setIsActive(policyContributionDto.getIsActive());
			policyContributions.add(policyContributionTempEntity);
		}
		response.setPolicyContributions(policyContributions);

		Set<PolicyContributionSummaryEntity> policyContributionSummary = new HashSet<>();
		for (PolicyContributionSummaryTempEntity policyContributionSummaryDto : policyMasterDto
				.getPolicyContributionSummary()) {
			PolicyContributionSummaryEntity policyContributionSummaryTempEntity = new PolicyContributionSummaryEntity();
			policyContributionSummaryTempEntity.setPolContSummaryId(null);
			policyContributionSummaryTempEntity.setPolicyId(response.getPolicyId());
			policyContributionSummaryTempEntity.setStampDuty(response.getStampDuty());
			policyContributionSummaryTempEntity.setOpeningBalance(policyContributionSummaryDto.getOpeningBalance());
			policyContributionSummaryTempEntity
					.setTotalEmployerContribution(policyContributionSummaryDto.getTotalEmployerContribution());
			policyContributionSummaryTempEntity
					.setTotalEmployeeContribution(policyContributionSummaryDto.getTotalEmployeeContribution());
			policyContributionSummaryTempEntity
					.setTotalVoluntaryContribution(policyContributionSummaryDto.getTotalVoluntaryContribution());
			policyContributionSummaryTempEntity
					.setTotalContribution(policyContributionSummaryDto.getTotalContribution());

			policyContributionSummaryTempEntity.setClosingBalance(policyContributionSummaryTempEntity
					.getOpeningBalance().add(policyContributionSummaryTempEntity.getTotalContribution()));
			policyContributionSummaryTempEntity.setFinancialYear(policyContributionSummaryDto.getFinancialYear());
			policyContributionSummaryTempEntity.setIsActive(policyContributionSummaryDto.getIsActive());
			policyContributionSummaryTempEntity.setCreatedOn(policyContributionSummaryDto.getCreatedOn());
			policyContributionSummaryTempEntity.setCreatedBy(policyContributionSummaryDto.getCreatedBy());
			policyContributionSummaryTempEntity.setModifiedOn(policyContributionSummaryDto.getModifiedOn());
			policyContributionSummaryTempEntity.setModifiedBy(policyContributionSummaryDto.getModifiedBy());
			if (variantType.equalsIgnoreCase("V2")) {
				policyContributionSummaryTempEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
			} else {
				policyContributionSummaryTempEntity.setQuarter("Q0");
			}
			policyContributionSummary.add(policyContributionSummaryTempEntity);
		}
		response.setPolicyContributionSummary(policyContributionSummary);
		policyMasterRepository.save(response);

		Set<ZeroAccountEntriesEntity> zeroAccountEntries = new HashSet<>();
		for (ZeroAccountEntriesTempEntity zeroAccountEntriesDto : policyMasterDto.getZeroAccountEntries()) {

			Set<PolicyContributionEntity> contributions = response.getPolicyContributions();
			for (PolicyContributionEntity contributionEntity : contributions) {
				Long policyContributionId = contributionEntity.getContributionId();

				ZeroAccountEntriesEntity zeroAccountEntriesTempEntity = new ZeroAccountEntriesEntity();
				zeroAccountEntriesTempEntity.setZeroAccEntId(null);
				zeroAccountEntriesTempEntity.setPolicyId(response.getPolicyId());
				zeroAccountEntriesTempEntity.setTempPolicyId(zeroAccountEntriesDto.getPolicyId());
				zeroAccountEntriesTempEntity.setPolicyConId(policyContributionId);
				zeroAccountEntriesTempEntity.setMemberConId(zeroAccountEntriesDto.getMemberConId());
				zeroAccountEntriesTempEntity.setTransactionType(zeroAccountEntriesDto.getTransactionType());
				zeroAccountEntriesTempEntity.setTransactionDate(zeroAccountEntriesDto.getTransactionDate());
				zeroAccountEntriesTempEntity.setZeroIdAmount(zeroAccountEntriesDto.getZeroIdAmount());
				zeroAccountEntriesTempEntity.setCreatedOn(zeroAccountEntriesDto.getCreatedOn());
				zeroAccountEntriesTempEntity.setCreatedBy(zeroAccountEntriesDto.getCreatedBy());
				zeroAccountEntriesTempEntity.setModifiedOn(zeroAccountEntriesDto.getModifiedOn());
				zeroAccountEntriesTempEntity.setModifiedBy(zeroAccountEntriesDto.getModifiedBy());
				zeroAccountEntriesTempEntity.setIsActive(zeroAccountEntriesDto.getIsActive());

				if (Objects.equals(status, PolicyConstants.APPROVED)) {
					zeroAccountEntriesTempEntity.setIsActive(Boolean.TRUE);
				} else if (Objects.equals(status, PolicyConstants.REJECT)) {
					zeroAccountEntriesTempEntity.setIsActive(Boolean.FALSE);
				}

				zeroAccountEntries.add(zeroAccountEntriesTempEntity);
			}
		}
		response.setZeroAccountEntries(zeroAccountEntries);

		Set<ZeroAccountEntity> zeroAccount = new HashSet<>();
		for (ZeroAccountTempEntity zeroAccountdto : policyMasterDto.getZeroAccount()) {
			ZeroAccountEntity zeroAccountTempEntity = new ZeroAccountEntity();
			zeroAccountTempEntity.setZeroAccId(null);
			zeroAccountTempEntity.setPolicyId(response.getPolicyId());
			zeroAccountTempEntity.setZeroIdAmount(zeroAccountdto.getZeroIdAmount());
			zeroAccountTempEntity.setIsActive(zeroAccountdto.getIsActive());
			zeroAccountTempEntity.setCreatedOn(zeroAccountdto.getCreatedOn());
			zeroAccountTempEntity.setCreatedBy(zeroAccountdto.getCreatedBy());
			zeroAccountTempEntity.setModifiedOn(zeroAccountdto.getModifiedOn());
			zeroAccountTempEntity.setModifiedBy(zeroAccountdto.getModifiedBy());
			zeroAccount.add(zeroAccountTempEntity);
		}
		response.setZeroAccount(zeroAccount);

		Set<PolicyNotesEntity> notes = new HashSet<>();
		for (PolicyNotesTempEntity policyNotesDto : policyMasterDto.getNotes()) {
			PolicyNotesEntity policyNotesTempEntity = new PolicyNotesEntity();
			policyNotesTempEntity.setPolicyNoteId(null);
			policyNotesTempEntity.setPolicyId(response.getPolicyId());
			policyNotesTempEntity.setNoteContents(policyNotesDto.getNoteContents());
			policyNotesTempEntity.setModifiedBy(policyNotesDto.getModifiedBy());
			policyNotesTempEntity.setModifiedOn(policyNotesDto.getModifiedOn());
			policyNotesTempEntity.setCreatedBy(policyNotesDto.getCreatedBy());
			policyNotesTempEntity.setCreatedOn(policyNotesDto.getCreatedOn());
			policyNotesTempEntity.setIsActive(policyNotesDto.getIsActive());
			notes.add(policyNotesTempEntity);
		}
		response.setNotes(notes);

		policyMasterRepository.save(response);

		Set<MemberMasterEntity> memberMasterTempEntityList = new HashSet<>();
		for (MemberMasterTempEntity policyValuationDto : policyMasterDto.getMemberMaster()) {
			Set<PolicyContributionEntity> contribution = response.getPolicyContributions();
			for (PolicyContributionEntity contributionEntity : contribution) {
				Long policyContributionId = contributionEntity.getContributionId();
				Long tempPolicyId = policyMasterDto.getPolicyId();
				MemberMasterEntity memberMasterTempEntity = convertMemberTempEntityToMemberMasterEntity(
						policyValuationDto, response.getPolicyId(), policyContributionId, tempPolicyId, variantType,
						status);
				memberMasterTempEntityList.add(memberMasterTempEntity);
			}
		}
		response.setMemberMaster(memberMasterTempEntityList);
		return response;
	}

	public MemberMasterEntity convertMemberTempEntityToMemberMasterEntity(MemberMasterTempEntity request, Long policyId,
			Long policyContributionId, Long tempPolicyId, String variantType, String status) {
		MemberMasterEntity response = new MemberMasterEntity();
		response.setMemberId(null);
		response.setTempMemberId(request.getMemberId());
		response.setPolicyId(policyId);
		response.setMembershipNumber(request.getMembershipNumber());
		response.setMemberStatus(request.getMemberStatus());
		response.setLicId(request.getLicId());
		response.setTypeOfMemebership(request.getTypeOfMemebership());
		response.setAadharNumber(request.getAadharNumber());
		response.setCategoryNo(request.getCategoryNo());
		response.setCommunicationPreference(request.getCommunicationPreference());
		response.setLanguagePreference(request.getLanguagePreference());
		response.setDateOfBirth(request.getDateOfBirth());
		response.setDateOfJoining(request.getDateOfJoining());
		response.setDateOfJoiningScheme(request.getDateOfJoiningScheme());
		response.setDateOfRetrirement(request.getDateOfRetrirement());
		response.setDesignation(request.getDesignation());
		response.setEmailid(request.getEmailid());
		response.setGender(request.getGender());
		response.setFatherName(request.getFatherName());
		response.setFirstName(request.getFirstName());
		response.setMiddleName(request.getMiddleName());
		response.setLastName(request.getLastName());
		response.setSpouseName(request.getSpouseName());
		response.setMaritalStatus(request.getMaritalStatus());
		response.setMemberPan(request.getMemberPan());
		response.setLandlineNo(request.getLandlineNo());
		response.setMobileNo(request.getMobileNo());
		response.setIsZeroid(request.getIsZeroid());
		response.setIsActive(request.getIsActive());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedOn(request.getCreatedOn());
		response.setModifiedBy(request.getModifiedBy());
		response.setModifiedOn(request.getModifiedOn());

		memberMasterRepository.save(response);

		Set<MemberAddressEntity> memberAddressList = new HashSet<>();
		for (MemberAddressTempEntity dto : request.getMemberAddress()) {
			MemberAddressEntity entity = new MemberAddressEntity();
			entity.setAddressId(null);
			entity.setMemberId(response.getMemberId());
//			entity.setPolicyId(dto.getPolicyId());
			entity.setAddress1(dto.getAddress1());
			entity.setAddress2(dto.getAddress2());
			entity.setAddress3(dto.getAddress3());
			entity.setAddressType(dto.getAddressType());
			entity.setCity(dto.getCity());
			entity.setDistrict(dto.getDistrict());
			entity.setStateName(dto.getStateName());
			entity.setCountry(dto.getCountry());
			entity.setPincode(dto.getPincode());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberAddressList.add(entity);
		}
		response.setMemberAddress(memberAddressList);
		Set<MemberBankEntity> memberBankList = new HashSet<>();
		for (MemberBankTempEntity dto : request.getMemberBank()) {
			MemberBankEntity entity = new MemberBankEntity();
			entity.setMemberBankId(null);
			entity.setMemberId(response.getMemberId());
//			entity.setPolicyId(dto.getPolicyId());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setConfirmAccountNumber(dto.getConfirmAccountNumber());
			entity.setAccountType(dto.getAccountType());
			entity.setIfscCodeAvailable(dto.getIfscCodeAvailable());
			entity.setIfscCode(dto.getIfscCode());
			entity.setBankName(dto.getBankName());
			entity.setBankBranch(dto.getBankBranch());
			entity.setBankAddress(dto.getBankAddress());
			entity.setCountryCode(dto.getCountryCode());
			entity.setStdCode(dto.getStdCode());
			entity.setLandlineNumber(dto.getLandlineNumber());
			entity.setEmailId(dto.getEmailId());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberBankList.add(entity);
		}
		response.setMemberBank(memberBankList);
		Set<MemberNomineeEntity> memberNomineeList = new HashSet<>();
		for (MemberNomineeTempEntity dto : request.getMemberNominee()) {
			MemberNomineeEntity entity = new MemberNomineeEntity();
			entity.setNomineeId(null);
			entity.setMemberId(response.getMemberId());
			entity.setNomineeType(dto.getNomineeType());
			entity.setNomineeName(dto.getNomineeName());
			entity.setRelationShip(dto.getRelationShip());
			entity.setAadharNumber(dto.getAadharNumber());
			entity.setPhone(dto.getPhone());
			entity.setEmailId(dto.getEmailId());
			entity.setAddressOne(dto.getAddressOne());
			entity.setAddressTwo(dto.getAddressTwo());
			entity.setAddressThree(dto.getAddressThree());
			entity.setCountry(dto.getCountry());
			entity.setPinCode(dto.getPinCode());
			entity.setDistrict(dto.getDistrict());
			entity.setState(dto.getState());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setPercentage(dto.getPercentage());
			entity.setAge(dto.getAge());
			entity.setDateOfBirth(dto.getDateOfBirth());
			memberNomineeList.add(entity);
		}
		response.setMemberNominee(memberNomineeList);
		Set<MemberAppointeeEntity> memberAppointeeList = new HashSet<>();
		for (MemberAppointeeTempEntity dto : request.getMemberAppointee()) {
			MemberAppointeeEntity entity = new MemberAppointeeEntity();
			entity.setAppointeeId(null);
			entity.setMemberId(response.getMemberId());
			entity.setAppointeeName(dto.getAppointeeName());
			entity.setAppointeeCode(dto.getAppointeeCode());
			entity.setRelationship(dto.getRelationship());
			entity.setContactNumber(dto.getContactNumber());
			entity.setDateOfBirth(dto.getDateOfBirth());
			entity.setPan(dto.getPan());
			entity.setAadharNumber(dto.getAadharNumber());
			entity.setIfscCode(dto.getIfscCode());
			entity.setBankName(dto.getBankName());
			entity.setAccountType(dto.getAccountType());
			entity.setAccountNumber(dto.getAccountNumber());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			memberAppointeeList.add(entity);
		}
		response.setMemberAppointee(memberAppointeeList);
		Set<MemberContributionEntity> memberContributionList = new HashSet<>();
		for (MemberContributionTempEntity dto : request.getMemberContribution()) {
			MemberContributionEntity entity = new MemberContributionEntity();
			entity.setMemberConId(null);
			entity.setMemberId(response.getMemberId());
			entity.setTempMemberId(dto.getMemberId());
			entity.setPolicyConId(policyContributionId);
			entity.setPolicyId(policyId);
			entity.setTempPolicyId(tempPolicyId);
			entity.setAdjustmentContributionId(dto.getAdjustmentContributionId());

//			if (Objects.equals(status, PolicyConstants.APPROVED)) {
			if (variantType.equalsIgnoreCase("V2")) {
				entity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
			} else {
				entity.setQuarter("Q0");
			}
//			} else if (Objects.equals(status, PolicyConstants.REJECT)) {
//				if (variantType.equalsIgnoreCase("V2")) {
//					entity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
//				} else {
//					entity.setQuarter("Q0");
//				}
//			}
//			if (variantType.equalsIgnoreCase("V2")) {
//				entity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
//			} else {
//				entity.setQuarter("Q0");
//			}
			entity.setContributionType(dto.getContributionType());
			entity.setContributionDate(dto.getContributionDate());
			entity.setLicId(dto.getLicId());
			entity.setOpeningBalance(dto.getOpeningBalance());
			entity.setEmployeeContribution(dto.getEmployeeContribution());
			entity.setEmployerContribution(dto.getEmployerContribution());
			entity.setVoluntaryContribution(dto.getVoluntaryContribution());
			entity.setTotalContribution(dto.getTotalContribution());
			entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
			entity.setClosingBalance(dto.getClosingBalance());
//			entity.setIsZeroAccount(dto.getIsZeroAccount());
			entity.setIsDeposit(dto.getIsDeposit());
			entity.setFinancialYear(dto.getFinancialYear());
			entity.setVersionNo(dto.getVersionNo());
			entity.setCreatedOn(dto.getCreatedOn());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setIsActive(dto.getIsActive());
			memberContributionList.add(entity);
		}
		response.setMemberContribution(memberContributionList);
		Set<MemberContributionSummaryEntity> memberContributionSummaryList = new HashSet<>();
		for (MemberContributionSummaryTempEntity dto : request.getMemberContributionSummary()) {
			MemberContributionSummaryEntity entity = new MemberContributionSummaryEntity();
			entity.setMemContSummaryId(null);
			entity.setMemberId(response.getMemberId());
			entity.setPolicyId(policyId);
			entity.setLicId(dto.getLicId());

			entity.setTotalEmployeeContribution(dto.getTotalEmployeeContribution());
			entity.setTotalEmployerContribution(dto.getTotalEmployerContribution());
			entity.setTotalVoluntaryContribution(dto.getTotalVoluntaryContribution());

			entity.setOpeningBalance(dto.getOpeningBalance());
			entity.setTotalContribution(dto.getTotalContribution());
			entity.setClosingBalance(dto.getClosingBalance());

			entity.setTotalInterestedAccured(dto.getTotalInterestedAccured());
			entity.setFinancialYear(dto.getFinancialYear());
			entity.setQuarter(dto.getQuarter());

			entity.setCreatedOn(dto.getCreatedOn());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setModifiedOn(dto.getModifiedOn());
			entity.setModifiedBy(dto.getModifiedBy());
			entity.setIsActive(dto.getIsActive());
			memberContributionSummaryList.add(entity);
		}
		response.setMemberContributionSummary(memberContributionSummaryList);
		return response;
	}
//	TEMP TO MAIN TABLE MOVE ENDS

	public Set<MemberMasterTempEntity> convertQuoMemEntityToMemMasEntity(Set<QuotationMemberEntity> members,
			Long policyId, Long policyConId, String role, String variantType) {
		Set<MemberMasterTempEntity> memberMasterTempEntityList = new HashSet<>();
		for (QuotationMemberEntity request : members) {
			MemberMasterTempEntity response = new MemberMasterTempEntity();
			response.setMemberId(null);
			response.setPolicyId(policyId);
			response.setMembershipNumber(request.getMemberShipId());
			response.setMemberStatus(request.getMemberStatus());
			response.setTypeOfMemebership(request.getTypeOfMembershipNo());
			response.setLicId(request.getLicId());
			response.setFirstName(request.getFirstName());
			response.setMiddleName(request.getMiddleName());
			response.setLastName(request.getLastName());
			response.setFatherName(request.getFatherName());
			response.setSpouseName(request.getSpouseName());
			response.setMaritalStatus(request.getMaritalStatus());
			response.setDateOfBirth(request.getDateOfBirth());
			response.setGender(request.getGender());
			response.setDateOfJoining(request.getDateOfJoining());
			response.setDateOfJoiningScheme(request.getDateOfJoiningScheme());
			response.setDateOfRetrirement(request.getDateOfRetirement());
			response.setDesignation(request.getDesignation());
			response.setEmailid(request.getEmailId());
			response.setLanguagePreference(request.getLanguagePreference());
			response.setCommunicationPreference(request.getCommunicationPreference());
			response.setAadharNumber(request.getAadharNumber());
//			response.setEmployerContribution(request.getEmployerContribution());
//			response.setEmployeeContribution(request.getEmployeeContribution());
			response.setCreatedBy(request.getCreatedBy() != null ? request.getCreatedBy() : role);
			response.setCreatedOn(new Date());
			response.setModifiedBy(role);
			response.setModifiedOn(new Date());
			response.setIsActive(Boolean.TRUE);
			response.setCategoryNo(request.getCategory());
			response.setMemberPan(request.getPan());

			Set<MemberContributionTempEntity> memberContributionTempList = new HashSet<>();
			MemberContributionTempEntity memberContributionTempEntity = new MemberContributionTempEntity();

			memberContributionTempEntity.setMemberConId(null);

			memberContributionTempEntity.setPolicyId(policyId);
			memberContributionTempEntity.setMemberId(response.getMemberId());
			memberContributionTempEntity.setLicId(response.getLicId());

			memberContributionTempEntity.setPolicyConId(null);
			memberContributionTempEntity.setContributionDate(null);
			memberContributionTempEntity.setFinancialYear(null);
			memberContributionTempEntity.setContributionType(null);
//			memberContributionTempEntity.setIsZeroAccount(Boolean.FALSE);
			memberContributionTempEntity.setIsDeposit(Boolean.FALSE);

			Integer i = 1;
			memberContributionTempEntity.setVersionNo(i);

			memberContributionTempEntity.setEmployeeContribution(request.getEmployeeContribution());
			memberContributionTempEntity.setEmployerContribution(request.getEmployerContribution());
			memberContributionTempEntity.setVoluntaryContribution(request.getVoluntaryContribution());
			memberContributionTempEntity.setOpeningBalance(BigDecimal.ZERO);
			memberContributionTempEntity.setTotalContribution(request.getTotalContribution());
//			memberContributionTempEntity.setClosingBalance(request.getTotalContribution());
			memberContributionTempEntity.setClosingBalance(memberContributionTempEntity.getOpeningBalance()
					.add(memberContributionTempEntity.getTotalContribution()));

			memberContributionTempEntity.setTotalInterestedAccured(request.getTotalInterestedAccured());

//			memberContributionTempEntity.setQuarter("Q"+DateUtils.getQuarterByDate(DateUtils.sysDate()));

			if (variantType.equalsIgnoreCase("V2")) {
				memberContributionTempEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
			} else {
				memberContributionTempEntity.setQuarter("Q0");
			}
			memberContributionTempEntity.setCreatedOn(new Date());
			memberContributionTempEntity.setCreatedBy(role);
			memberContributionTempEntity.setModifiedOn(new Date());
			memberContributionTempEntity.setModifiedBy(role);
			memberContributionTempEntity.setIsActive(Boolean.TRUE);

			memberContributionTempList.add(memberContributionTempEntity);
			response.setMemberContribution(memberContributionTempList);

			Set<MemberContributionSummaryTempEntity> memberContributionSummaryTempList = new HashSet<>();

			MemberContributionSummaryTempEntity memberContributionSummaryTempEntity = new MemberContributionSummaryTempEntity();

			memberContributionSummaryTempEntity.setMemContSummaryId(null);

			memberContributionSummaryTempEntity.setPolicyId(policyId);
			memberContributionSummaryTempEntity.setMemberId(response.getMemberId());
			memberContributionSummaryTempEntity.setLicId(request.getLicId());

			memberContributionSummaryTempEntity.setTotalEmployeeContribution(request.getEmployeeContribution());
			memberContributionSummaryTempEntity.setTotalEmployerContribution(request.getEmployerContribution());
			memberContributionSummaryTempEntity.setTotalVoluntaryContribution(request.getVoluntaryContribution());
			memberContributionSummaryTempEntity.setOpeningBalance(BigDecimal.ZERO);
			memberContributionSummaryTempEntity.setTotalContribution(request.getTotalContribution());
			memberContributionSummaryTempEntity.setClosingBalance(memberContributionSummaryTempEntity
					.getOpeningBalance().add(memberContributionSummaryTempEntity.getTotalContribution()));
			memberContributionSummaryTempEntity.setTotalInterestedAccured(request.getTotalInterestedAccured());

			memberContributionSummaryTempEntity.setFinancialYear(DateUtils.getFinancialYrByDt(new Date()));
//			memberContributionSummaryTempEntity.setQuarter("Q"+DateUtils.getQuarterByDate(DateUtils.sysDate()));

			if (variantType.equalsIgnoreCase("V2")) {
				memberContributionSummaryTempEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
			} else {
				memberContributionSummaryTempEntity.setQuarter("Q0");
			}

			memberContributionSummaryTempEntity.setCreatedOn(new Date());
			memberContributionSummaryTempEntity.setCreatedBy(role);
			memberContributionSummaryTempEntity.setModifiedOn(new Date());
			memberContributionSummaryTempEntity.setModifiedBy(role);
			memberContributionSummaryTempList.add(memberContributionSummaryTempEntity);

			response.setMemberContributionSummary(memberContributionSummaryTempList);

			Set<MemberAddressTempEntity> memberAddressList = new HashSet<>();
			for (QuotationMemberAddressEntity quoEntity : request.getQuotationMemberAddresses()) {
				MemberAddressTempEntity tempEntity = new MemberAddressTempEntity();
				tempEntity.setAddressId(null);
				tempEntity.setMemberId(response.getMemberId());
//				tempEntity.setPolicyId(null);
				tempEntity.setAddress1(quoEntity.getAddressLineOne());
				tempEntity.setAddress2(quoEntity.getAddressLineTwo());
				tempEntity.setAddress3(quoEntity.getAddressLineThree());
				tempEntity.setAddressType(quoEntity.getAddressType());
				tempEntity.setCity(quoEntity.getCity());
				tempEntity.setDistrict(quoEntity.getDistrict());
				tempEntity.setStateName(quoEntity.getState());
				tempEntity.setCountry(quoEntity.getCountry());
				tempEntity.setPincode(quoEntity.getPinCode());
				tempEntity.setIsActive(Boolean.TRUE);
				tempEntity.setCreatedBy(quoEntity.getCreatedBy());
				tempEntity.setCreatedOn(new Date());
				tempEntity.setModifiedBy(quoEntity.getModifiedBy());
				tempEntity.setModifiedOn(new Date());
				memberAddressList.add(tempEntity);
			}
			response.setMemberAddress(memberAddressList);
			Set<MemberBankTempEntity> memberBankList = new HashSet<>();
			for (QuotationMemberBankDetailEntity quoEntity : request.getQuotationMemberBankDetails()) {
				MemberBankTempEntity entity = new MemberBankTempEntity();
				entity.setMemberBankId(null);
				entity.setMemberId(response.getMemberId());
//				entity.setPolicyId(null);
				entity.setAccountNumber(quoEntity.getAccountNumber());
				entity.setConfirmAccountNumber(quoEntity.getConfirmAccountNumber());
				entity.setAccountType(quoEntity.getAccountType());
				entity.setIfscCodeAvailable(quoEntity.getIfscCodeAvailable());
				entity.setIfscCode(quoEntity.getIfscCode());
				entity.setBankName(quoEntity.getBankName());
				entity.setBankBranch(quoEntity.getBankBranch());
				entity.setBankAddress(quoEntity.getBankAddress());
				entity.setCountryCode(quoEntity.getCountryCode());
				entity.setStdCode((StringUtils.isNotBlank(quoEntity.getStdCode()) && quoEntity.getStdCode() != null)
						? Integer.valueOf(quoEntity.getStdCode())
						: null);
				entity.setLandlineNumber(quoEntity.getLandlineNumber());
				entity.setEmailId(quoEntity.getEmailId());
				entity.setIsActive(quoEntity.getIsActive());
				entity.setCreatedBy(quoEntity.getCreatedBy());
				entity.setCreatedOn(quoEntity.getCreatedOn());
				entity.setModifiedBy(quoEntity.getModifiedBy());
				entity.setModifiedOn(quoEntity.getModifiedOn());
				memberBankList.add(entity);
			}
			response.setMemberBank(memberBankList);
			Set<MemberNomineeTempEntity> memberNomineeList = new HashSet<>();
			for (QuotationMemberNomineeEntity quoEntity : request.getQuotationMemberNominees()) {
				MemberNomineeTempEntity entity = new MemberNomineeTempEntity();
				entity.setNomineeId(null);
				entity.setMemberId(response.getMemberId());
				entity.setNomineeType(quoEntity.getNomineeType());
				entity.setNomineeName(quoEntity.getNomineeName());
				entity.setRelationShip(quoEntity.getRelationShip());
				entity.setAadharNumber(quoEntity.getAadharNumber());
				entity.setPhone(quoEntity.getPhone());
				entity.setEmailId(quoEntity.getEmailId());
				entity.setAddressOne(quoEntity.getAddressOne());
				entity.setAddressTwo(quoEntity.getAddressTwo());
				entity.setAddressThree(quoEntity.getAddressThree());
				entity.setCountry(quoEntity.getCountry());
				entity.setPinCode(quoEntity.getPinCode());
				entity.setDistrict(quoEntity.getDistrict());
				entity.setState(quoEntity.getState());
				entity.setIsActive(quoEntity.getIsActive());
				entity.setCreatedBy(quoEntity.getCreatedBy());
				entity.setCreatedOn(quoEntity.getCreatedOn());
				entity.setModifiedBy(quoEntity.getModifiedBy());
				entity.setModifiedOn(quoEntity.getModifiedOn());
				entity.setPercentage(quoEntity.getPercentage());
				entity.setAge(quoEntity.getAge());
				entity.setDateOfBirth(quoEntity.getDateOfBirth());
				memberNomineeList.add(entity);
			}
			response.setMemberNominee(memberNomineeList);
			Set<MemberAppointeeTempEntity> memberAppointeeList = new HashSet<>();
			for (QuotationMemberAppointeeEntity quoEntity : request.getQuotationMemberAppointees()) {
				MemberAppointeeTempEntity entity = new MemberAppointeeTempEntity();
				entity.setAppointeeId(null);
				entity.setMemberId(response.getMemberId());
				entity.setAppointeeName(quoEntity.getAppointeeName());
				entity.setAppointeeCode(quoEntity.getAppointeeCode());
				entity.setRelationship(quoEntity.getRelationship());
				entity.setContactNumber(quoEntity.getContactNumber());
				entity.setDateOfBirth(quoEntity.getDateOfBirth());
				entity.setPan(quoEntity.getPan());
				entity.setAadharNumber(quoEntity.getAadharNumber());
				entity.setIfscCode(quoEntity.getIfscCode());
				entity.setBankName(quoEntity.getBankName());
				entity.setAccountType(quoEntity.getAccountType());
				entity.setAccountNumber(quoEntity.getAccountNumber());
				entity.setIsActive(quoEntity.getIsActive());
				entity.setCreatedBy(quoEntity.getCreatedBy());
				entity.setCreatedOn(quoEntity.getCreatedOn());
				entity.setModifiedBy(quoEntity.getModifiedBy());
				entity.setModifiedOn(quoEntity.getModifiedOn());
				memberAppointeeList.add(entity);
			}
			response.setMemberAppointee(memberAppointeeList);
			memberMasterTempEntityList.add(response);
		}

		return memberMasterTempEntityList;
	}

	public Date calculateARDa(Date policyCommencementDate) {
		Date newDate = null;
		if (policyCommencementDate != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(policyCommencementDate);
			c.add(Calendar.YEAR, 1);
			c.add(Calendar.DAY_OF_MONTH, 0);
			newDate = c.getTime();
		}
		return newDate;
	}

	public String getQuotationNumber(Integer quotationId) {
		String quotationNumber;
		if (quotationId != null) {
			quotationNumber = quotationRepository.getQuotationNumber(quotationId);
		} else {
			quotationNumber = null;
		}
		return quotationNumber;
	}

}