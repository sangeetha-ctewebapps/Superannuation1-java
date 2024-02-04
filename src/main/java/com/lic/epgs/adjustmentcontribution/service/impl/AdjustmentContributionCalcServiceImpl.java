package com.lic.epgs.adjustmentcontribution.service.impl;

/**
 * @author pradeepramesh
 *
 */

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.adjustmentcontribution.dto.ACSaveAdjustmentRequestDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionAllDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionEntity;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionTempEntity;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionRepository;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionTempRepository;
import com.lic.epgs.adjustmentcontribution.service.AdjustmentContributionCalcService;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.policy.constants.PolicyConstants;
import com.lic.epgs.policy.entity.MemberContributionEntity;
import com.lic.epgs.policy.entity.MemberContributionSummaryEntity;
import com.lic.epgs.policy.entity.MemberContributionTempEntity;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MemberMasterTempEntity;
import com.lic.epgs.policy.entity.PolicyContributionEntity;
import com.lic.epgs.policy.entity.PolicyContributionSummaryEntity;
import com.lic.epgs.policy.entity.PolicyContributionTempEntity;
import com.lic.epgs.policy.entity.PolicyDepositEntity;
import com.lic.epgs.policy.entity.PolicyDepositTempEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesEntity;
import com.lic.epgs.policy.entity.ZeroAccountEntriesTempEntity;
import com.lic.epgs.policy.entity.ZeroAccountTempEntity;
import com.lic.epgs.policy.repository.MemberContributionRepository;
import com.lic.epgs.policy.repository.MemberContributionSummaryRepository;
import com.lic.epgs.policy.repository.MemberContributionTempRepository;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.MemberMasterTempRepository;
import com.lic.epgs.policy.repository.PolicyContributionRepository;
import com.lic.epgs.policy.repository.PolicyContributionSummaryRepository;
import com.lic.epgs.policy.repository.PolicyContributionTempRepository;
import com.lic.epgs.policy.repository.PolicyDepositRepository;
import com.lic.epgs.policy.repository.PolicyDepositTempRepository;
import com.lic.epgs.policy.repository.ZeroAccountEntriesRepository;
import com.lic.epgs.policy.repository.ZeroAccountEntriesTempRepository;
import com.lic.epgs.policy.repository.ZeroAccountRepository;
import com.lic.epgs.policy.repository.ZeroAccountTempRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
public class AdjustmentContributionCalcServiceImpl implements AdjustmentContributionCalcService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	AdjustmentContributionServiceImpl adjustmentContributionServiceImpl;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	CommonService commonSequenceService;
	@Autowired
	AdjustmentContributionCommonServiceImpl adjustmentContributionCommonServiceImpl;
	@Autowired
	AdjustmentContributionRepository adjRepo;
	@Autowired
	PolicyDepositTempRepository policyDepositTempRepository;
	@Autowired
	PolicyDepositRepository policyDepositRepository;
	@Autowired
	PolicyContributionTempRepository policyContributionTempRepository;
	@Autowired
	MemberContributionTempRepository memberContributionTempRepository;
	@Autowired
	MemberContributionRepository memberContributionRepository;
	@Autowired
	AdjustmentContributionTempRepository adjustmentContributionTempRepository;
	@Autowired
	PolicyContributionRepository policyContributionRepository;
	@Autowired
	PolicyContributionSummaryRepository policyContributionSummaryRepository;
	@Autowired
	MemberContributionSummaryRepository memberContributionSummaryRepository;
	@Autowired
	ZeroAccountEntriesRepository zeroAccountEntriesRepository;
	@Autowired
	ZeroAccountRepository zeroAccountRepository;
	@Autowired
	ZeroAccountEntriesTempRepository zeroAccountEntriesTempRepository;
	@Autowired
	ZeroAccountTempRepository zeroAccountTempRepository;
	@Autowired
	MemberMasterRepository memberMasterRepository;
	@Autowired
	MemberMasterTempRepository memberMasterTempRepository;
	@Autowired
	CommonService commonService;
	
	

	@Override
	public AdjustmentContributionResponseDto saveAdjustment(ACSaveAdjustmentRequestDto adjustmentDto) {
		AdjustmentContributionResponseDto response = new AdjustmentContributionResponseDto();
		try {
			logger.info("AdjustmentContributionCalcServiceImpl :: saveAdjustment :: Start");
			String contributionType = PolicyConstants.SUBSQEUENTADJUSTMENTNEW;

// Adjustment Temp Details
			Optional<AdjustmentContributionTempEntity> adjusOptional = adjustmentContributionTempRepository
					.findById(adjustmentDto.getAdjustmentContributionId());
			if (!adjusOptional.isPresent()) {
				response.setTransactionMessage(PolicyConstants.POLICY_INVALID);
				response.setTransactionStatus(PolicyConstants.FAIL);
				return response;
			}
			AdjustmentContributionTempEntity adjustementTempEntity = adjusOptional.get();
// Policy Contribution Details

			List<PolicyContributionTempEntity> policyContributionOpt = checkPolicycontributionPreviouse(
					adjustementTempEntity.getTempPolicyId(), adjustementTempEntity.getPolicyId(),
					adjustementTempEntity.getAdjustmentContributionId(), contributionType, adjustmentDto.getRole(),
					null);

			Optional<PolicyDepositTempEntity> policyDepositOpt = policyDepositTempRepository
					.findByStatusAndDepositIdAndZeroIdAndPolicyIdAndAdjustmentContributionId(
							PolicyConstants.DEPOSITSTATUSNEW, adjustmentDto.getAdjustmentContributiondepositId(), false,
							adjustementTempEntity.getTempPolicyId(), adjustmentDto.getAdjustmentContributionId());
			if (!policyDepositOpt.isPresent()) {
				response.setTransactionMessage(PolicyConstants.DEPOSIT_INVALID);
				response.setTransactionStatus(PolicyConstants.FAIL);
				return response;
			}
			PolicyDepositTempEntity depositEntity = policyDepositOpt.get();
// Set Ammount Details Respectively

			
			String mphName = adjustementTempEntity.getMphName();
			BigDecimal amountToBeAdjusted = adjustementTempEntity.getAmountToBeAdjusted();
			BigDecimal newDepositAmount = depositEntity.getAvailableAmount();
			BigDecimal totalContribution = adjustementTempEntity.getTotalContribution();
			BigDecimal availableDepositAmt = adjustementTempEntity.getTotalDeposit();
			BigDecimal newTotalDepositAmt = availableDepositAmt.add(newDepositAmount);
			Date adjustmentDueDate = adjustementTempEntity.getAdjustmentDueDate();
			
			Date newContributionDate = null;
			
//			if (depositEntity.getTransactionMode().equalsIgnoreCase("Q")) {
//				newContributionDate = depositEntity.getChequeRealisationDate();
//			}
//			else {
//				newContributionDate = depositEntity.getCollectionDate();
//			}
			
			if(depositEntity.getChequeRealisationDate() != null) {
			newContributionDate = depositEntity.getChequeRealisationDate();
			}
			
			
// Switch Based on Policy Type	-STARTS		
			switch (adjustementTempEntity.getPolicyType()) {
			case "DB":
				logger.info("PolicyId - {}, Contribution depositAmount is less: {}", adjustmentDto.getPolicyId(),
						newTotalDepositAmt);
				calculatePolicyContribution(policyContributionOpt, adjustementTempEntity, contributionType,
						depositEntity, availableDepositAmt, newTotalDepositAmt, totalContribution,
						adjustmentDto.getRole(), amountToBeAdjusted, newDepositAmount, adjustmentDueDate,newContributionDate);

				response = getFinalResponse(
						getResponseAdjustmentContributionAllDto(adjustementTempEntity,
								adjustmentDto.getAdjustmentContributionId(), adjustmentDto.getPolicyId()),
						adjustementTempEntity);

				break;
			case "DC":
//Switch Based on Total Contribution deposit Amount Commpare Condition  STARTS 	
				switch (newTotalDepositAmt.compareTo(totalContribution)) {
				case -1:
					logger.info("PolicyId - {}, Contribution depositAmount is less: {}", adjustmentDto.getPolicyId(),
							newTotalDepositAmt);

					calculatePolicyContribution(policyContributionOpt, adjustementTempEntity, contributionType,
							depositEntity, availableDepositAmt, newTotalDepositAmt, totalContribution,
							adjustmentDto.getRole(), amountToBeAdjusted, newDepositAmount, adjustmentDueDate,newContributionDate);

					response = getFinalResponse(
							getResponseAdjustmentContributionAllDto(adjustementTempEntity,
									adjustmentDto.getAdjustmentContributionId(), adjustmentDto.getPolicyId()),
							adjustementTempEntity);
					break;
				case 0:
					logger.info("PolicyId - {}, Contribution depositAmount is equals: {}", adjustmentDto.getPolicyId(),
							newTotalDepositAmt);
					calculatePolicyContribution(policyContributionOpt, adjustementTempEntity, contributionType,
							depositEntity, availableDepositAmt, newTotalDepositAmt, totalContribution,
							adjustmentDto.getRole(), amountToBeAdjusted, newDepositAmount, adjustmentDueDate,newContributionDate);

					response = getFinalResponse(
							getResponseAdjustmentContributionAllDto(adjustementTempEntity,
									adjustmentDto.getAdjustmentContributionId(), adjustmentDto.getPolicyId()),
							adjustementTempEntity);

					break;
				case 1:
					logger.info("PolicyId - {}, Contribution depositAmount is Excess: {}", adjustmentDto.getPolicyId(),
							newTotalDepositAmt);
					if (!"true".equals(adjustmentDto.getIsZeroId())) {
						calculatePolicyContribution(policyContributionOpt, adjustementTempEntity, contributionType,
								depositEntity, availableDepositAmt, newTotalDepositAmt, totalContribution,
								adjustmentDto.getRole(), amountToBeAdjusted, newDepositAmount, adjustmentDueDate,newContributionDate);
						response = getFinalResponse(
								getResponseAdjustmentContributionAllDto(adjustementTempEntity,
										adjustmentDto.getAdjustmentContributionId(), adjustmentDto.getPolicyId()),
								adjustementTempEntity);
					} else {
						PolicyContributionTempEntity policyContribution2 = new PolicyContributionTempEntity();
						for (PolicyContributionTempEntity policyContribution : policyContributionOpt) {
							policyContribution.setContributionId(null);
							policyContribution.setPolicyId(adjustementTempEntity.getTempPolicyId());
							policyContribution.setMasterPolicyId(adjustementTempEntity.getPolicyId());
							policyContribution
									.setAdjustmentContributionId(adjustementTempEntity.getAdjustmentContributionId());
							policyContribution.setContributionType(contributionType);
							policyContribution.setContributionDate(new Date());

							policyContribution.setContReferenceNo(depositEntity.getChallanNo());
							policyContribution.setVersionNo(policyContribution.getVersionNo() + 1);
							policyContribution
									.setFinancialYear(DateUtils.getFinancialYrByDt( new Date()));
							policyContribution.setOpeningBalance(availableDepositAmt);
							policyContribution.setClosingBalance(newTotalDepositAmt);
							policyContribution.setEmployerContribution(adjustementTempEntity.getEmployerContribution());
							policyContribution.setEmployeeContribution(adjustementTempEntity.getEmployeeContribution());
							policyContribution
									.setVoluntaryContribution(adjustementTempEntity.getVoluntaryContribution());
							policyContribution.setTotalContribution(totalContribution);
							policyContribution.setIsDeposit(Boolean.TRUE);
							policyContribution.setIsActive(Boolean.TRUE);
							policyContribution.setCreatedBy(adjustmentDto.getRole());
							policyContribution.setCreatedOn(new Date());
							policyContribution.setModifiedBy(adjustmentDto.getRole());
							policyContribution.setModifiedOn(new Date());
							policyContribution.setTxnEntryStatus(Boolean.FALSE);
							policyContribution.setPolicyContribution(null);
							policyContribution.setAdjustmentDueDate(adjustmentDueDate);
							policyContribution.setEffectiveDate(newContributionDate);
							policyContribution = policyContributionTempRepository.save(policyContribution);

							Set<MemberContributionTempEntity> memberContributionList = new HashSet<>();
							Set<MemberContributionTempEntity> templist = memberContributionTempRepository
									.findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(
											adjustementTempEntity.getTempPolicyId(),
											adjustementTempEntity.getAdjustmentContributionId());
							for (MemberContributionTempEntity memberContribution : templist) {
								MemberContributionTempEntity memberContribution2 = new MemberContributionTempEntity();
								memberContribution2.setMemberConId(memberContribution.getMemberConId());
								memberContribution2.setPolicyConId(policyContribution.getContributionId());
								memberContribution2.setBatchId(memberContribution.getBatchId());
								memberContribution2.setPolicyId(adjustementTempEntity.getTempPolicyId());
								memberContribution2.setMemberId(memberContribution.getMemberId());
								memberContribution2.setMasterPolicyId(adjustementTempEntity.getPolicyId());
								memberContribution2.setMasterMemberId(memberContribution.getMasterMemberId());
								memberContribution2.setAdjustmentContributionId(
										adjustementTempEntity.getAdjustmentContributionId());
								memberContribution2.setContributionType(contributionType);
								memberContribution2.setContributionDate(new Date());
								memberContribution2.setFinancialYear(
										DateUtils.getFinancialYrByDt( new Date()));
								memberContribution2.setVersionNo(memberContribution.getVersionNo());
								memberContribution2.setLicId(memberContribution.getLicId());
								memberContribution2.setOpeningBalance(memberContribution.getOpeningBalance());
								memberContribution2.setClosingBalance(memberContribution.getClosingBalance());
								memberContribution2
										.setEmployeeContribution(memberContribution.getEmployeeContribution());
								memberContribution2
										.setEmployerContribution(memberContribution.getEmployerContribution());
								memberContribution2
										.setVoluntaryContribution(memberContribution.getVoluntaryContribution());
								memberContribution2.setTotalContribution(memberContribution.getTotalContribution());
								memberContribution2
										.setTotalInterestedAccured(memberContribution.getTotalInterestedAccured());
								memberContribution2.setIsDeposit(Boolean.TRUE);
								memberContribution2.setIsActive(Boolean.TRUE);
								memberContribution2.setCreatedBy(memberContribution.getCreatedBy());
								memberContribution2.setCreatedOn(memberContribution.getCreatedOn());
								memberContribution2.setModifiedBy(adjustmentDto.getRole());
								memberContribution2.setModifiedOn(new Date());
								memberContribution2.setTxnEntryStatus(Boolean.FALSE);
								memberContribution2.setAdjustmentDueDate(adjustmentDueDate);
								memberContribution2.setEffectiveDate(newContributionDate);
								memberContributionList.add(memberContribution2);
							}
							policyContribution.setPolicyContribution(memberContributionList);
							policyContributionTempRepository.save(policyContribution);

							policyContribution2 = policyContributionTempRepository.save(policyContribution);
							break;
						}

						Long policyContributionId = policyContribution2.getContributionId();

						depositEntity.setStatus(PolicyConstants.ADJESTED);
						depositEntity.setAdjustmentAmount(amountToBeAdjusted);
						depositEntity.setAvailableAmount(newDepositAmount.subtract(depositEntity.getAvailableAmount()));
						depositEntity.setIsDeposit(Boolean.TRUE);
						depositEntity.setZeroId(Boolean.TRUE);
						depositEntity.setVoucherNo(adjustmentDto.getVoucherNo());
						depositEntity.setVoucherDate(adjustmentDto.getVoucherDate());	
						policyDepositTempRepository.save(depositEntity);
						adjustementTempEntity
								.setTotalDeposit(availableDepositAmt.add(depositEntity.getAdjustmentAmount()));
						adjustementTempEntity.setAdjustmentForDate(new Date());
						adjustementTempEntity.setAdjustmentDueDate(adjustmentDueDate);
						adjustementTempEntity.setEffectiveDate(newContributionDate);
						adjustmentContributionTempRepository.save(adjustementTempEntity);
						response.setTotalDeposit(adjustementTempEntity.getTotalDeposit());
						if ("DC".equals(adjustementTempEntity.getPolicyType())) {

							BigDecimal zeroAmount = newDepositAmount.subtract(depositEntity.getAdjustmentAmount());
							String role = adjustmentDto.getRole();
							Long memberContibutionId = null;
							String type = contributionType;
							adjustmentContributionServiceImpl.checkZeroAccountTempInMember(
									adjustementTempEntity.getTempPolicyId(), zeroAmount, role, policyContributionId,
									memberContibutionId, type, mphName);
						}

						response = getFinalResponse(
								getResponseAdjustmentContributionAllDto(adjustementTempEntity,
										adjustmentDto.getAdjustmentContributionId(), adjustmentDto.getPolicyId()),
								adjustementTempEntity);
					}
					break;
				default:
					break;
				}
//   Switch Based on Total Contribution deposit Amount Commpare Condition  STARTS				
				break;
			default:
				break;
			}
// Switch Based on Policy Type	-STARTS			
		} catch (IllegalArgumentException e) {
			logger.error("Exception :: AdjustmentContributionCalcServiceImpl :: saveAdjustment", e);
			response.setTransactionMessage(PolicyConstants.FAIL);
			response.setTransactionStatus(PolicyConstants.ERROR);
		} finally {
			logger.info("AdjustmentContributionCalcServiceImpl :: saveAdjustment :: Ends");
		}
		return response;
	}
	
	private AdjustmentContributionResponseDto getFinalResponse(
			AdjustmentContributionAllDto responseAdjustmentContributionAllDto,
			AdjustmentContributionTempEntity adjustementTempEntity) {
		logger.info("AdjustmentContributionCalcServiceImpl :: getFinalResponse :: Start");
		AdjustmentContributionResponseDto response = new AdjustmentContributionResponseDto();
		response.setResponseData(responseAdjustmentContributionAllDto);
		response.setTotalDeposit(adjustementTempEntity.getTotalDeposit());
		response.setIsCommencementdateOneYr(adjustementTempEntity.getIsCommencementdateOneYr());
		response.setBank(adjustmentContributionServiceImpl.getPolicyBankList(adjustementTempEntity.getPolicyId()));
		response.setTransactionMessage(PolicyConstants.ADJUSTEDMESSAGE);
		response.setTransactionStatus(PolicyConstants.SUCCESS);
		response.setZeroRow(responseAdjustmentContributionAllDto.getZeroRow());
		logger.info("AdjustmentContributionCalcServiceImpl :: getFinalResponse :: Ends");
		return response;
	}
	
	private AdjustmentContributionAllDto getResponseAdjustmentContributionAllDto(
			AdjustmentContributionTempEntity adjustementTempEntity, Long adjustmentContributionId, Long policyId) {
		logger.info("AdjustmentContributionCalcServiceImpl :: getResponseAdjustmentContributionAllDto :: Start");
		AdjustmentContributionAllDto adjustmentContributionAllDto = new AdjustmentContributionAllDto();
		adjustmentContributionAllDto
				.setAdjustmentContribution(modelMapper.map(adjustementTempEntity, AdjustmentContributionDto.class));
		adjustmentContributionAllDto.setAdjustmentContributionId(
				adjustmentContributionAllDto.getAdjustmentContribution().getAdjustmentContributionId());
		adjustmentContributionAllDto.setAdjustmentDepositDtos(
				adjustmentContributionServiceImpl.getDepositFromPolicyDeposit(adjustmentContributionId, policyId));
		adjustmentContributionAllDto.setAdjustmentDepositAdjustmentDtos(
				adjustmentContributionServiceImpl.getAdjustementFromPolicyDeposit(adjustmentContributionId, policyId));
		adjustmentContributionAllDto.setZeroRow(
				adjustmentContributionServiceImpl.getZeroAccountFromPolicy(adjustementTempEntity.getPolicyId()));
		adjustmentContributionAllDto
				.setBank(adjustmentContributionServiceImpl.getPolicyBankList(adjustementTempEntity.getPolicyId()));
		logger.info("AdjustmentContributionCalcServiceImpl :: getResponseAdjustmentContributionAllDto :: Ends");
		return adjustmentContributionAllDto;
	}
	
	private void calculatePolicyContribution(List<PolicyContributionTempEntity> policyContributionOpt,
			AdjustmentContributionTempEntity adjustementTempEntity, String contributionType,
			PolicyDepositTempEntity depositEntity, BigDecimal availableDepositAmt, BigDecimal newTotalDepositAmt,
			BigDecimal totalContribution, String role, BigDecimal amountToBeAdjusted, BigDecimal newDepositAmount,
			Date adjustmentDueDate,Date effectiveDate) {

		logger.info("AdjustmentContributionCalcServiceImpl :: calculatePolicyContribution :: Start");

		for (PolicyContributionTempEntity policyContribution : policyContributionOpt) {
			BigDecimal openingBalance = policyContribution.getClosingBalance();

			policyContribution.setContributionId(null);
			policyContribution.setPolicyId(adjustementTempEntity.getTempPolicyId());
			policyContribution.setMasterPolicyId(adjustementTempEntity.getPolicyId());

			policyContribution.setAdjustmentContributionId(adjustementTempEntity.getAdjustmentContributionId());

			policyContribution.setRegularContributionId(null);

			policyContribution.setContributionType(contributionType);
			policyContribution.setContributionDate(new Date());
			policyContribution.setContReferenceNo(depositEntity.getChallanNo());
			policyContribution.setFinancialYear(DateUtils.getFinancialYrByDt( new Date()));
			policyContribution.setVersionNo(policyContribution.getVersionNo() + 1);
			policyContribution.setOpeningBalance(openingBalance);
			policyContribution.setEmployerContribution(adjustementTempEntity.getEmployerContribution());
			policyContribution.setEmployeeContribution(adjustementTempEntity.getEmployeeContribution());
			policyContribution.setVoluntaryContribution(adjustementTempEntity.getVoluntaryContribution());
			policyContribution.setTotalContribution(totalContribution);
			policyContribution.setClosingBalance(openingBalance.add(totalContribution));
			policyContribution.setIsDeposit(Boolean.TRUE);
			policyContribution.setIsActive(Boolean.TRUE);
			policyContribution.setCreatedBy(role);
			policyContribution.setCreatedOn(new Date());
			policyContribution.setModifiedBy(role);
			policyContribution.setModifiedOn(new Date());
			policyContribution.setTxnEntryStatus(Boolean.FALSE);
			policyContribution.setPolicyContribution(null);
			policyContribution.setAdjustmentDueDate(adjustmentDueDate);
			policyContribution.setEffectiveDate(effectiveDate);
			policyContribution = policyContributionTempRepository.save(policyContribution);

			Set<MemberContributionTempEntity> memberContributionList = new HashSet<>();
			Set<MemberContributionTempEntity> templist = memberContributionTempRepository
					.findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(adjustementTempEntity.getTempPolicyId(),
							adjustementTempEntity.getAdjustmentContributionId());

			for (MemberContributionTempEntity memberContribution : templist) {

				MemberContributionTempEntity memberContribution2 = new MemberContributionTempEntity();
				memberContribution2.setMemberConId(memberContribution.getMemberConId());
				memberContribution2.setPolicyConId(policyContribution.getContributionId());
				memberContribution2.setBatchId(memberContribution.getBatchId());
				memberContribution2.setPolicyId(adjustementTempEntity.getTempPolicyId());
				memberContribution2.setMemberId(memberContribution.getMemberId());
				memberContribution2.setMasterPolicyId(adjustementTempEntity.getPolicyId());
				memberContribution2.setMasterMemberId(memberContribution.getMasterMemberId());
				memberContribution2.setAdjustmentContributionId(adjustementTempEntity.getAdjustmentContributionId());
				memberContribution2.setContributionType(contributionType);
				memberContribution2.setContributionDate(new Date());

				memberContribution2.setFinancialYear(DateUtils.getFinancialYrByDt( new Date()));
				memberContribution2.setVersionNo(memberContribution.getVersionNo());
				memberContribution2.setLicId(memberContribution.getLicId());
				memberContribution2.setOpeningBalance(memberContribution.getOpeningBalance());
				memberContribution2.setEmployeeContribution(memberContribution.getEmployeeContribution());
				memberContribution2.setEmployerContribution(memberContribution.getEmployerContribution());
				memberContribution2.setVoluntaryContribution(memberContribution.getVoluntaryContribution());
				memberContribution2.setTotalContribution(memberContribution.getTotalContribution());

				memberContribution2.setClosingBalance(memberContribution.getClosingBalance());

				memberContribution2.setTotalInterestedAccured(memberContribution.getTotalInterestedAccured());
				memberContribution2.setIsDeposit(Boolean.TRUE);
				memberContribution2.setIsActive(Boolean.TRUE);
				memberContribution2.setCreatedBy(memberContribution.getCreatedBy());
				memberContribution2.setCreatedOn(memberContribution.getCreatedOn());
				memberContribution2.setModifiedBy(role);
				memberContribution2.setModifiedOn(new Date());
				memberContribution2.setTxnEntryStatus(Boolean.FALSE);
				memberContribution2.setAdjustmentDueDate(adjustmentDueDate);
				memberContribution2.setEffectiveDate(effectiveDate);
				memberContributionList.add(memberContribution2);
			}
			policyContribution.setPolicyContribution(memberContributionList);

			policyContributionTempRepository.save(policyContribution);
			break;
		}

//Deposit Change 				
		depositEntity.setStatus(PolicyConstants.ADJESTED);
		depositEntity.setAdjustmentAmount(amountToBeAdjusted);
		depositEntity.setAvailableAmount(newDepositAmount.subtract(depositEntity.getAdjustmentAmount()));
		depositEntity.setIsDeposit(Boolean.TRUE);
		policyDepositTempRepository.save(depositEntity);
//update total contribution
		adjustementTempEntity.setTotalDeposit(availableDepositAmt.add(depositEntity.getAdjustmentAmount()));
		adjustementTempEntity.setAdjustmentForDate(new Date());
		adjustementTempEntity.setAdjustmentDueDate(adjustmentDueDate);
		adjustementTempEntity.setEffectiveDate(effectiveDate);
		adjustmentContributionTempRepository.save(adjustementTempEntity);

		logger.info("AdjustmentContributionCalcServiceImpl :: calculatePolicyContribution :: Ends");
	}

	public List<PolicyContributionTempEntity> checkPolicycontributionPreviouse(Long policyId, Long masterPolicyId,
			Long adjustmentConId, String contributionType, String role, Long regularConId) {
		logger.info("AdjustmentContributionCalcServiceImpl :: checkPolicycontributionPreviouse :: Start");

		List<PolicyContributionTempEntity> policyContributionOpt = policyContributionTempRepository
				.findByPolicyIdAndIsActiveTrueOrderByVersionNoDesc(policyId);

		if (policyContributionOpt.isEmpty()) {
			PolicyContributionTempEntity policycontributionNew = new PolicyContributionTempEntity();

			policycontributionNew.setContributionId(null);
			policycontributionNew.setPolicyId(policyId);
			policycontributionNew.setMasterPolicyId(masterPolicyId);
			policycontributionNew.setAdjustmentContributionId(adjustmentConId);
			policycontributionNew.setRegularContributionId(regularConId);
			policycontributionNew.setContributionType(contributionType);
			policycontributionNew.setContributionDate(null);
			policycontributionNew.setContReferenceNo(null);
			policycontributionNew.setFinancialYear(DateUtils.getFinancialYrByDt( new Date()));
			policycontributionNew.setVersionNo(1);
			policycontributionNew.setOpeningBalance(BigDecimal.ZERO);
			policycontributionNew.setEmployerContribution(BigDecimal.ZERO);
			policycontributionNew.setEmployeeContribution(BigDecimal.ZERO);
			policycontributionNew.setVoluntaryContribution(BigDecimal.ZERO);
			policycontributionNew.setTotalContribution(BigDecimal.ZERO);
			policycontributionNew.setClosingBalance(BigDecimal.ZERO);
			policycontributionNew.setIsDeposit(Boolean.TRUE);
			policycontributionNew.setIsActive(Boolean.TRUE);
			policycontributionNew.setCreatedBy(role);
			policycontributionNew.setCreatedOn(new Date());
			policycontributionNew.setModifiedBy(role);
			policycontributionNew.setModifiedOn(new Date());
			policycontributionNew.setTxnEntryStatus(Boolean.FALSE);
			policycontributionNew.setPolicyContribution(null);
			policyContributionTempRepository.save(policycontributionNew);
		}

		policyContributionOpt = policyContributionTempRepository
				.findByPolicyIdAndFinancialYearAndIsActiveTrueOrderByVersionNoDesc(policyId,
						DateUtils.getFinancialYrByDt( new Date()));
		logger.info("AdjustmentContributionCalcServiceImpl :: checkPolicycontributionPreviouse :: Ends");
		return policyContributionOpt;

	}
	
	@Override
	@Transactional
	public AdjustmentContributionResponseDto approve(Long adjustmentContributionId, String adjustmentContributionStatus,
			String role, String variantType) {
		AdjustmentContributionAllDto adjustmentContributionAllDto = new AdjustmentContributionAllDto();
		AdjustmentContributionResponseDto responseDto = new AdjustmentContributionResponseDto();
		try {
			logger.info("AdjustmentContributionCalcServiceImpl :: approve :: Start");

			AdjustmentContributionTempEntity adjContriTempEntity = adjustmentContributionTempRepository
					.findByAdjustmentContributionIdAndIsActiveTrue(adjustmentContributionId);
			if (adjContriTempEntity != null) {
				
				List<AdjustmentContributionEntity> adjustmentContributionEntityList = adjRepo.findAllByAdjustmentContributionNumberAndIsActiveTrue(adjContriTempEntity.getAdjustmentContributionNumber());
				
				if(adjustmentContributionEntityList.isEmpty()) {
				adjContriTempEntity.setModifiedBy(role);
				adjContriTempEntity.setModifiedOn(DateUtils.sysDate());
				adjContriTempEntity.setAdjustmentContributionStatus(adjustmentContributionStatus);
				AdjustmentContributionTempEntity saveEntity = adjustmentContributionTempRepository
						.save(adjContriTempEntity);
				AdjustmentContributionEntity adjConEntity = adjustmentContributionCommonServiceImpl
						.convertTempToMaster(saveEntity);
				AdjustmentContributionEntity saveMain = adjRepo.save(adjConEntity);

				saveContribution(saveEntity, saveMain, role, variantType);

				AdjustmentContributionDto dto = modelMapper.map(saveMain, AdjustmentContributionDto.class);

				adjustmentContributionAllDto.setAdjustmentContribution(dto);
				adjustmentContributionAllDto.setAdjustmentContributionId(dto.getAdjustmentContributionId());
				responseDto.setResponseData(adjustmentContributionAllDto);
				responseDto.setPolicyId(dto.getPolicyId());
				responseDto.setTransactionStatus(CommonConstants.SUCCESS);
				responseDto.setTransactionMessage(CommonConstants.UPDATEMESSAGE);
				} else {
					responseDto.setTransactionStatus(CommonConstants.FAIL);
					responseDto.setTransactionMessage(CommonConstants.ADJ_APPROVE);
					return responseDto;
				}
			} else {
				responseDto.setTransactionStatus(CommonConstants.ERROR);
				responseDto.setTransactionMessage(CommonConstants.FAIL);
			}
		} catch (IllegalArgumentException | ApplicationException e) {
			logger.error("Exception :: AdjustmentContributionCalcServiceImpl :: approve", e);
			responseDto.setTransactionStatus(CommonConstants.ERROR);
			responseDto.setTransactionMessage(CommonConstants.FAIL);
		}
		logger.info("AdjustmentContributionCalcServiceImpl :: approve :: Ends");
		return responseDto;
	}
	
	@Transactional
	public void saveContribution(AdjustmentContributionTempEntity tempAdj, AdjustmentContributionEntity masterAdj,
			String username, String variantType) throws ApplicationException {
		logger.info("saveContribution::Start");
		PolicyMasterEntity policyMasterEntity = commonService.findPolicyDetails(null, masterAdj.getPolicyId());

		String financialYr = DateUtils.getFinancialYrByDt(DateUtils.sysDate());

		PolicyContributionTempEntity policyContributionTempEntity = policyContributionTempRepository
				.findTopByPolicyIdAndAdjustmentContributionIdAndIsActiveTrueOrderByContributionIdDesc(
						tempAdj.getTempPolicyId(), tempAdj.getAdjustmentContributionId());

		PolicyContributionEntity policyContributionEntity = setPolicyContributionEntity(policyContributionTempEntity,
				masterAdj, username, variantType, masterAdj.getPolicyId(), masterAdj.getTempPolicyId());

		policyContributionEntity.setCreatedBy(masterAdj.getCreatedBy());
		policyContributionEntity.setCreatedOn(masterAdj.getCreatedOn());
		policyContributionEntity.setModifiedOn(DateUtils.sysDate());
		policyContributionEntity.setModifiedBy(masterAdj.getModifiedBy());
		
		policyContributionRepository.save(policyContributionEntity);

		Set<MemberContributionEntity> setMemberContributionEntity = setMemberContributionEntity(policyMasterEntity,
				tempAdj, masterAdj, policyContributionEntity, variantType);

		policyContributionEntity = setPolicyContributionBySumOfMemberContribution(policyMasterEntity, masterAdj,
				username, policyContributionEntity, setMemberContributionEntity);

		policyContributionEntity.setPolicyContribution(setMemberContributionEntity);

		policyContributionRepository.save(policyContributionEntity);

		Long policyContributionId = policyContributionEntity.getContributionId();

		printContribution(policyContributionEntity, masterAdj);

		Set<PolicyContributionEntity> policySet = new HashSet<>();

		policySet.add(policyContributionEntity);

		logger.info("savePolicyContributionSummary::Start");
		savePolicyContributionSummary(policySet, username, variantType);
		logger.info("savePolicyContributionSummary::End");

		logger.info("saveMemberContributionSummary::Start");
		saveMemberContributionSummary(setMemberContributionEntity, username);
		logger.info("saveMemberContributionSummary::End");

		logger.info("savePolicyDeposit::Start");
		savePolicyDeposit(policyMasterEntity, tempAdj, masterAdj, financialYr);
		logger.info("savePolicyDeposit::End");

		logger.info("saveZeroAccount::Start");
		saveZeroAcccount(policyMasterEntity, tempAdj, masterAdj, financialYr, policyContributionId);
		logger.info("saveZeroAccount::End");

		logger.info("saveContribution::End");

	}
	
	public PolicyContributionEntity setPolicyContributionEntity(
			PolicyContributionTempEntity policyContributionTempEntity, AdjustmentContributionEntity masterAdj,
			String username, String variantType, Long masterPolicyId, Long policyId) {

		logger.info("AdjustmentContributionCalcServiceImpl :: setPolicyContributionEntity :: Start");

		PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();
		String financialYear = DateUtils.getFinancialYrByDt(DateUtils.sysDate());

		if (policyContributionTempEntity != null) {
			BeanUtils.copyProperties(policyContributionTempEntity, policyContributionEntity);

			PolicyContributionEntity policyContributionEntityPreviousRecord = policyContributionRepository
					.findTopByPolicyIdAndFinancialYearAndIsActiveTrueOrderByContributionIdDesc(
							policyContributionTempEntity.getMasterPolicyId(), financialYear);

			if (policyContributionEntityPreviousRecord != null) {
				policyContributionEntity.setOpeningBalance(policyContributionEntityPreviousRecord.getClosingBalance());
				policyContributionEntity.setVersionNo(policyContributionEntityPreviousRecord.getVersionNo() + 1);
			} else {
				policyContributionEntity.setOpeningBalance(BigDecimal.ZERO);
				policyContributionEntity.setVersionNo(1);
			}

			policyContributionEntity.setPolicyId(policyContributionTempEntity.getMasterPolicyId());
			policyContributionEntity.setTempPolicyId(policyContributionTempEntity.getPolicyId());
			policyContributionEntity.setContributionId(null);
			policyContributionEntity.setAdjustmentContributionId(masterAdj.getAdjustmentContributionId());
			policyContributionEntity.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
			policyContributionEntity.setTxnEntryStatus(false);
			policyContributionEntity.setZeroAccountEntries(null);

			policyContributionEntity
					.setTotalContribution(NumericUtils.add(policyContributionEntity.getEmployeeContribution(),
							policyContributionEntity.getEmployerContribution(),
							policyContributionEntity.getVoluntaryContribution()));

			policyContributionEntity
					.setTotalContribution(NumericUtils.bigDecimalValid(policyContributionEntity.getTotalContribution())
							.compareTo(BigDecimal.ZERO) > 0 ? policyContributionEntity.getTotalContribution()
									: policyContributionEntity.getTotalContribution());

			policyContributionEntity.setClosingBalance(
					policyContributionEntity.getOpeningBalance().add(policyContributionEntity.getTotalContribution()));

			if (variantType.equalsIgnoreCase("V2")) {
				policyContributionEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
			} else {
				policyContributionEntity.setQuarter("Q0");
			}
			policyContributionEntity.setModifiedBy(username);
		} else {

			policyContributionEntity = checkPolicycontributionMainPreviouse(policyId, masterPolicyId,
					masterAdj.getAdjustmentContributionId(), username, financialYear, null, variantType);

			PolicyContributionEntity policyContributionEntityPreviousRecord = policyContributionRepository
					.findTopByPolicyIdAndFinancialYearAndIsActiveTrueOrderByContributionIdDesc(
							policyContributionEntity.getPolicyId(), financialYear);

			policyContributionEntity.setPolicyId(policyContributionEntity.getPolicyId());
			policyContributionEntity.setTempPolicyId(policyContributionEntity.getTempPolicyId());
			policyContributionEntity.setContributionId(null);
			policyContributionEntity.setAdjustmentContributionId(masterAdj.getAdjustmentContributionId());
			policyContributionEntity.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));

			policyContributionEntity.setTxnEntryStatus(false);
			policyContributionEntity.setZeroAccountEntries(null);

			policyContributionEntity.setOpeningBalance(policyContributionEntityPreviousRecord.getClosingBalance());

			policyContributionEntity
					.setTotalContribution(NumericUtils.add(policyContributionEntity.getEmployeeContribution(),
							policyContributionEntity.getEmployerContribution(),
							policyContributionEntity.getVoluntaryContribution()));

			policyContributionEntity.setTotalContribution(
							NumericUtils.bigDecimalValid(policyContributionEntity.getTotalContribution()).compareTo(BigDecimal.ZERO) > 0 
							? policyContributionEntity.getTotalContribution()
							: policyContributionEntity.getTotalContribution());

			policyContributionEntity.setClosingBalance(
					policyContributionEntity.getOpeningBalance().add(policyContributionEntity.getTotalContribution()));

			if (variantType.equalsIgnoreCase("V2")) {
				policyContributionEntity.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
			} else {
				policyContributionEntity.setQuarter("Q0");
			}
			policyContributionEntity.setModifiedBy(username);
		}
		logger.info("AdjustmentContributionCalcServiceImpl :: setPolicyContributionEntity :: Ends");

		return policyContributionEntity;
	}
	
	public PolicyContributionEntity checkPolicycontributionMainPreviouse(Long policyId, Long masterPolicyId,
			Long adjustmentConId, String contributionType, String role, Long regularConId, String variantType) {

		logger.info("AdjustmentContributionCalcServiceImpl :: checkPolicycontributionMainPreviouse :: Start");

		PolicyContributionEntity policyContributionOpt = policyContributionRepository
				.findByPolicyIdAndIsActiveTrueOrderByVersionNoDesc(masterPolicyId);

		if (policyContributionOpt == null) {
			PolicyContributionEntity policycontributionNew = new PolicyContributionEntity();

			policycontributionNew.setContributionId(null);
			policycontributionNew.setPolicyId(masterPolicyId);
			policycontributionNew.setTempPolicyId(policyId);
			policycontributionNew.setAdjustmentContributionId(adjustmentConId);
			policycontributionNew.setRegularContributionId(regularConId);
			policycontributionNew.setContributionType(contributionType);
			policycontributionNew.setContributionDate(null);
			policycontributionNew.setContReferenceNo(null);
			policycontributionNew.setFinancialYear(DateUtils.getFinancialYrByDt( new Date()));
			policycontributionNew.setVersionNo(1);
			policycontributionNew.setOpeningBalance(BigDecimal.ZERO);
			policycontributionNew.setEmployerContribution(BigDecimal.ZERO);
			policycontributionNew.setEmployeeContribution(BigDecimal.ZERO);
			policycontributionNew.setVoluntaryContribution(BigDecimal.ZERO);
			policycontributionNew.setTotalContribution(BigDecimal.ZERO);
			policycontributionNew.setClosingBalance(BigDecimal.ZERO);
			policycontributionNew.setIsDeposit(Boolean.TRUE);
			policycontributionNew.setIsActive(Boolean.TRUE);
			policycontributionNew.setCreatedBy(role);
			policycontributionNew.setCreatedOn(new Date());
			policycontributionNew.setModifiedBy(role);
			policycontributionNew.setModifiedOn(new Date());
			policycontributionNew.setTxnEntryStatus(Boolean.FALSE);
			policycontributionNew.setPolicyContribution(null);
			if (variantType.equalsIgnoreCase("V2")) {
				policycontributionNew.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
			} else {
				policycontributionNew.setQuarter("Q0");
			}
			policyContributionRepository.save(policycontributionNew);
		}

		policyContributionOpt = policyContributionRepository
				.findByPolicyIdAndFinancialYearAndIsActiveTrueOrderByVersionNoDesc(policyId,
						DateUtils.getFinancialYrByDt( new Date()));
		logger.info("AdjustmentContributionCalcServiceImpl :: checkPolicycontributionMainPreviouse :: Ends");
		return policyContributionOpt;

	}

	public Set<MemberContributionEntity> setMemberContributionEntity(PolicyMasterEntity policyMasterEntity,
			AdjustmentContributionTempEntity tempAdj, AdjustmentContributionEntity masterAdj,
			PolicyContributionEntity policyContributionEntity, String variantType) throws ApplicationException {

		logger.info("AdjustmentContributionCalcServiceImpl :: setMemberContributionEntity :: Start");

		if (policyMasterEntity.getPolicyType().equalsIgnoreCase(CommonConstants.DC)) {
			String financialYr = DateUtils.getFinancialYrByDt(DateUtils.sysDate());
			Set<MemberContributionTempEntity> fromMc = memberContributionTempRepository
					.findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(tempAdj.getTempPolicyId(),
							tempAdj.getAdjustmentContributionId());

			if (CommonUtils.isNonEmptyArray(fromMc)) {
				Set<MemberContributionEntity> memberContributionMasterList = new HashSet<>();
				fromMc.stream().forEach(from -> {
					MemberContributionEntity to = new MemberContributionEntity();
					BeanUtils.copyProperties(from, to);
					to.setAdjustmentContributionId(masterAdj.getAdjustmentContributionId());
					to.setClosingBalance(null);
					to.setOpeningBalance(null);
					to.setFinancialYear(financialYr);
					to.setMemberConId(null);
					to.setPolicyId(from.getMasterPolicyId());
					to.setMemberId(from.getMasterMemberId());
					to.setModifiedOn(DateUtils.sysDate());
					if (variantType.equalsIgnoreCase("V2")) {
						to.setQuarter("Q" + DateUtils.getQuarterByDate(DateUtils.sysDate()));
					} else {
						to.setQuarter("Q0");
					}
					to.setTempMemberId(from.getMemberId());
					to.setTempPolicyId(from.getPolicyId());
					to.setTxnEntryStatus(false);
					to.setPolicyConId(policyContributionEntity.getContributionId());
					
					to.setModifiedBy(masterAdj.getModifiedBy());
					to.setCreatedBy(masterAdj.getCreatedBy());
					to.setCreatedOn(masterAdj.getCreatedOn());

					/** to.setZeroAccountEntries(null); */
					memberContributionMasterList.add(to);
				});
				logger.info("AdjustmentContributionCalcServiceImpl :: setMemberContributionEntity :: Ends");
				return memberContributionMasterList;
			}
			throw new ApplicationException(
					"No member contribution found for the given master policy id:" + policyMasterEntity.getPolicyId());
		}
		return Collections.emptySet();
	}

	public PolicyContributionEntity setPolicyContributionBySumOfMemberContribution(
			PolicyMasterEntity policyMasterEntity, AdjustmentContributionEntity masterAdj, String username,
			PolicyContributionEntity policyContributionEntity, Set<MemberContributionEntity> memberContributions) {

		logger.info("AdjustmentContributionCalcServiceImpl :: setPolicyContributionBySumOfMemberContribution :: Start");

		double employee;
		double employer;
		double voluntary;
		BigDecimal totalContribution;

		if (policyMasterEntity.getPolicyType().equalsIgnoreCase(CommonConstants.DC)) {

			employee = memberContributions.stream().filter(p -> p.getEmployeeContribution() != null)
					.mapToDouble(op -> op.getEmployeeContribution().doubleValue()).sum();
			employer = memberContributions.stream().filter(p -> p.getEmployerContribution() != null)
					.mapToDouble(op -> op.getEmployerContribution().doubleValue()).sum();
			voluntary = memberContributions.stream().filter(p -> p.getVoluntaryContribution() != null)
					.mapToDouble(op -> op.getVoluntaryContribution().doubleValue()).sum();
			totalContribution = NumericUtils.doubleToBigDecimal((employee + employer + voluntary));

			policyContributionEntity.setPolicyContribution(memberContributions);
			policyContributionEntity.setEmployeeContribution(NumericUtils.doubleToBigDecimal(employee));
			policyContributionEntity.setEmployerContribution(NumericUtils.doubleToBigDecimal(employer));
			policyContributionEntity.setVoluntaryContribution(NumericUtils.doubleToBigDecimal(voluntary));
			policyContributionEntity.setTotalContribution(totalContribution);

		}
		logger.info("AdjustmentContributionCalcServiceImpl :: setPolicyContributionBySumOfMemberContribution :: Ends");
		return policyContributionEntity;
	}

	public void printContribution(PolicyContributionEntity policyContributionEntity,
			AdjustmentContributionEntity masterAdj) {
		logger.info("*********AdjustmentContributionId::{}, MasterPolicyId::{}*********",
				masterAdj.getAdjustmentContributionId(), masterAdj.getPolicyId());
		logger.info(
				"************************************PolicyContributionEntity**********************************************");
		logger.info("PolicyContributionId::{}, PolicyId::{}, FinancialYr::{}, Quarter::{}, txnEntryStatus::{}",
				policyContributionEntity.getContributionId(), policyContributionEntity.getPolicyId(),
				policyContributionEntity.getFinancialYear(), policyContributionEntity.getQuarter(),
				policyContributionEntity.getTxnEntryStatus());
		Set<MemberContributionEntity> memberSet = policyContributionEntity.getPolicyContribution();
		if (CommonUtils.isNonEmptyArray(memberSet)) {
			memberSet.forEach(mem -> {
				logger.info(
						"************************************MemberContributionEntity**********************************************");
				logger.info(
						"PolicyConId::{}, MemberConId::{}, PolicyId::{}, FinancialYr::{}, Quarter::{}, txnEntryStatus::{}",
						mem.getPolicyConId(), mem.getMemberConId(), mem.getPolicyId(), mem.getFinancialYear(),
						mem.getQuarter(), mem.getTxnEntryStatus());
			});
		}
		logger.info(
				"***********************************PolicyContributionEntity***********************************************");
	}

	public void savePolicyDeposit(PolicyMasterEntity policyMasterEntity, AdjustmentContributionTempEntity tempAdj,
			AdjustmentContributionEntity masterAdj, String role) {
		logger.info("AdjustmentContributionCalcServiceImpl :: savePolicyDeposit :: Start");

		logger.info("{}::", policyMasterEntity.getPolicyId());
		List<PolicyDepositTempEntity> policyDepositTempEntityList = policyDepositTempRepository
				.findByPolicyIdAndAdjustmentContributionIdAndIsActiveTrue(tempAdj.getTempPolicyId(),
						tempAdj.getAdjustmentContributionId());

		for (PolicyDepositTempEntity policyDepositTempEntity : policyDepositTempEntityList) {
			PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

			policyDepositEntity.setDepositId(null);
			policyDepositEntity.setPolicyId(policyDepositTempEntity.getMasterPolicyId());

			policyDepositEntity.setTempPolicyId(policyDepositTempEntity.getPolicyId());
			policyDepositEntity.setContributionType(policyDepositTempEntity.getContributionType());
			policyDepositEntity.setAdjustmentContributionId(masterAdj.getAdjustmentContributionId());

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
			policyDepositEntity.setModifiedOn(DateUtils.sysDate());
			policyDepositEntity.setCreatedBy(role);
			policyDepositEntity.setCreatedOn(DateUtils.sysDate());
			policyDepositEntity.setIsActive(Boolean.TRUE);
			policyDepositRepository.save(policyDepositEntity);
		}
		logger.info("AdjustmentContributionCalcServiceImpl :: savePolicyDeposit :: Ends");
	}

	private void saveZeroAcccount(PolicyMasterEntity policyMasterEntity, AdjustmentContributionTempEntity tempAdj,
			AdjustmentContributionEntity masterAdj, String financialYr, Long policyContributionId) {
		logger.info("saveZeroAccount::Start");
//qwert
//		getTempZeroAccount
		ZeroAccountTempEntity zeroAccountTempEntity = zeroAccountTempRepository.findByPolicyIdAndIsActiveTrue(tempAdj.getTempPolicyId());
		if (zeroAccountTempEntity != null) {
			ZeroAccountEntity zeroAccountEntity = zeroAccountRepository.findByPolicyIdAndIsActiveTrue(tempAdj.getPolicyId());
			if (zeroAccountEntity != null) {
				// need to add old amount with new amount
				zeroAccountEntity.setZeroAccId(zeroAccountEntity.getZeroAccId());
				zeroAccountEntity.setZeroIdAmount(zeroAccountTempEntity.getZeroIdAmount());
				zeroAccountEntity.setPolicyId(zeroAccountEntity.getPolicyId());
				zeroAccountEntity.setModifiedBy(zeroAccountTempEntity.getModifiedBy());
				zeroAccountEntity.setModifiedOn(new Date());
				zeroAccountEntity.setIsActive(Boolean.TRUE);
				zeroAccountRepository.save(zeroAccountEntity);
			} else {
				zeroAccountEntity = new ZeroAccountEntity();
				// need to add old amount with new amount
				zeroAccountEntity.setZeroAccId(null);
				zeroAccountEntity.setZeroIdAmount(zeroAccountTempEntity.getZeroIdAmount());
				zeroAccountEntity.setPolicyId(tempAdj.getPolicyId());
				zeroAccountEntity.setModifiedBy(masterAdj.getModifiedBy());
				zeroAccountEntity.setModifiedOn(new Date());
				zeroAccountEntity.setCreatedBy(masterAdj.getModifiedBy());
				zeroAccountEntity.setCreatedOn(new Date());
				zeroAccountEntity.setIsActive(Boolean.TRUE);
				zeroAccountRepository.save(zeroAccountEntity);

				MemberMasterEntity memberMasterEntity = memberMasterRepository.findByPolicyIdAndLicIdAndMemberStatus(tempAdj.getPolicyId(), "0", CommonConstants.ACTIVE);
				if (memberMasterEntity == null) {
					MemberMasterTempEntity memberMasterTempEntity = memberMasterTempRepository.findByPolicyIdAndLicIdAndMemberStatus(tempAdj.getTempPolicyId(), "0",CommonConstants.ACTIVE);
					if (memberMasterTempEntity != null) {
						MemberMasterEntity masterEntity = new MemberMasterEntity();
						BeanUtils.copyProperties(memberMasterTempEntity, masterEntity);
						masterEntity.setMemberId(null);
						masterEntity.setTempMemberId(memberMasterTempEntity.getMemberId());
						masterEntity.setPolicyId(tempAdj.getPolicyId());
						masterEntity.setCreatedBy(zeroAccountTempEntity.getModifiedBy());
						masterEntity.setCreatedOn(new Date());
						masterEntity.setModifiedBy(zeroAccountTempEntity.getModifiedBy());
						masterEntity.setModifiedOn(new Date());
						memberMasterRepository.save(masterEntity);
					}
				}
			}
		}

//		getZeroAccount
		List<ZeroAccountEntriesTempEntity> zeroAccountEntriesTempEntityList = zeroAccountEntriesTempRepository.findByPolicyIdAndIsMovedFalseAndIsActiveTrue(tempAdj.getTempPolicyId());
		if (zeroAccountEntriesTempEntityList != null) {
			for (ZeroAccountEntriesTempEntity zeroAccountEntriesTempEntity : zeroAccountEntriesTempEntityList) {
				ZeroAccountEntriesEntity zeroAccountEntriesEntity = new ZeroAccountEntriesEntity();
				zeroAccountEntriesEntity.setZeroAccEntId(null);
				zeroAccountEntriesEntity.setPolicyId(tempAdj.getPolicyId());
				zeroAccountEntriesEntity.setPolicyConId(policyContributionId);
				zeroAccountEntriesEntity.setMemberConId(null);
				zeroAccountEntriesEntity.setZeroIdAmount(zeroAccountEntriesTempEntity.getZeroIdAmount());
				zeroAccountEntriesEntity.setTransactionType(zeroAccountEntriesTempEntity.getTransactionType());
				zeroAccountEntriesEntity.setTransactionDate(zeroAccountEntriesTempEntity.getTransactionDate());
				zeroAccountEntriesEntity.setIsActive(Boolean.TRUE);
				zeroAccountEntriesEntity.setCreatedBy(masterAdj.getCreatedBy());
				zeroAccountEntriesEntity.setCreatedOn(masterAdj.getCreatedOn());
				zeroAccountEntriesEntity.setModifiedBy(masterAdj.getModifiedBy());
				zeroAccountEntriesEntity.setModifiedOn(masterAdj.getModifiedOn());
//				zeroAccountEntriesEntity.setTempPolicyId(tempAdj.getTempPolicyId());
				zeroAccountEntriesRepository.save(zeroAccountEntriesEntity);

//				zeroAccountEntriesTempEntity.setIsMoved(Boolean.TRUE);
//				zeroAccountEntriesTempEntity.setMasterPolicyId(tempAdj.getPolicyId());
				zeroAccountEntriesTempRepository.save(zeroAccountEntriesTempEntity);
			}
		}
		logger.info("saveZeroAccount::Ends");
	}

	@Override
	public Set<PolicyContributionSummaryEntity> savePolicyContributionSummary(
			Set<PolicyContributionEntity> contributionEntities, String role, String variantType) {
		String financialYear = DateUtils.getFinancialYrByDt(DateUtils.sysDate());
		Integer quatar = DateUtils.getQuarterByDate(DateUtils.sysDate());
		String quarter = ("Q" + quatar);
		long newTime = System.currentTimeMillis();
		try {
			if (CommonUtils.isNonEmptyArray(contributionEntities)) {
				logger.info("AdjustmentContributionCalcServiceImpl :: savePolicyContributionSummary :: Start");
				Set<PolicyContributionSummaryEntity> sets = new HashSet<>();
				contributionEntities.stream().forEach(master -> {
					long firstTime = System.currentTimeMillis();
					PolicyContributionSummaryEntity topSummaryEntity = policyContributionSummaryRepository
							.findTopByPolicyIdAndFinancialYearAndIsActiveTrueOrderByPolContSummaryIdDesc(
									master.getPolicyId(), financialYear);
					
                    long startTime = System.currentTimeMillis();
                    
					BigDecimal employee = BigDecimal.ZERO;
					BigDecimal employer = BigDecimal.ZERO;
					BigDecimal volun = BigDecimal.ZERO;
					BigDecimal total = BigDecimal.ZERO;

					Object object = new Object();

					if (variantType.equalsIgnoreCase("V2")) {
						object = policyContributionRepository.sumOfPolicyContribution(master.getPolicyId(), quarter,
								financialYear, true);
					} else {
						object = policyContributionRepository.sumOfPolicyContribution(master.getPolicyId(),
								financialYear, true);
					}
					
					if (object != null) {
						Object[] ob = (Object[]) object;
						employee = NumericUtils
								.stringToBigDecimal(NumericUtils.getNotNullStringVal(String.valueOf(ob[0])));
						employer = NumericUtils
								.stringToBigDecimal(NumericUtils.getNotNullStringVal(String.valueOf(ob[1])));
						volun = NumericUtils
								.stringToBigDecimal(NumericUtils.getNotNullStringVal(String.valueOf(ob[2])));
						total = NumericUtils
								.stringToBigDecimal(NumericUtils.getNotNullStringVal(String.valueOf(ob[3])));
					}
					long longTime = System.currentTimeMillis();
					System.out.println("That took " + (longTime - startTime) + " milliseconds");
					if (topSummaryEntity != null) {
						topSummaryEntity.setModifiedOn(DateUtils.sysDate());
						topSummaryEntity.setModifiedBy(role);
						topSummaryEntity.setOpeningBalance(topSummaryEntity.getClosingBalance());
						topSummaryEntity
								.setClosingBalance(NumericUtils.bigDecimalValid(topSummaryEntity.getClosingBalance())
										.add(master.getTotalContribution()));
						topSummaryEntity.setTotalContribution(total);
					} else {
						topSummaryEntity = new PolicyContributionSummaryEntity();
						topSummaryEntity.setCreatedOn(DateUtils.sysDate());
						topSummaryEntity.setCreatedBy(role);
						topSummaryEntity.setModifiedOn(DateUtils.sysDate());
						topSummaryEntity.setModifiedBy(role);
						topSummaryEntity.setOpeningBalance(BigDecimal.ZERO);
						topSummaryEntity.setClosingBalance(total);
						topSummaryEntity.setTotalContribution(total);
						topSummaryEntity.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
						topSummaryEntity.setIsActive(true);
						topSummaryEntity.setPolicyId(master.getPolicyId());
						topSummaryEntity.setStampDuty(BigDecimal.ZERO);
					}
					
					
					topSummaryEntity.setTotalEmployeeContribution(employee);
					topSummaryEntity.setTotalEmployerContribution(employer);
					topSummaryEntity.setTotalVoluntaryContribution(volun);
					sets.add(topSummaryEntity);
					long lastTime = System.currentTimeMillis();
					System.out.println("That took overall" + (lastTime - firstTime) + " milliseconds");
				});
				
				policyContributionSummaryRepository.saveAll(sets);
				logger.info("AdjustmentContributionCalcServiceImpl :: savePolicyContributionSummary :: Ends");
				return sets;
			}
		} catch (Exception e) {
			logger.error("Exception :: AdjustmentContributionCalcServiceImpl :: savePolicyContributionSummary :: ", e);
		}
		return Collections.emptySet();
	}

	@Override
	public Set<MemberContributionSummaryEntity> saveMemberContributionSummary(
			Set<MemberContributionEntity> memberContributionEntiies, String role) {
		String financialYear = DateUtils.getFinancialYrByDt(DateUtils.sysDate());
		try {
			if (CommonUtils.isNonEmptyArray(memberContributionEntiies)) {
				logger.info("AdjustmentContributionCalcServiceImpl :: saveMemberContributionSummary :: Start");
				long longsvsTime = System.currentTimeMillis();
				Set<MemberContributionSummaryEntity> sets = new HashSet<>();
				memberContributionEntiies.stream().forEach(memberContributionEntity -> {
					long longsvsfirstTime = System.currentTimeMillis();
					MemberContributionSummaryEntity topSummaryEntity = memberContributionSummaryRepository
							.findTopByPolicyIdAndMemberIdAndFinancialYearAndIsActiveTrueOrderByMemContSummaryIdDesc(
									memberContributionEntity.getPolicyId(), memberContributionEntity.getMemberId(),
									financialYear);
					long longsvsmethodTime = System.currentTimeMillis();
					BigDecimal employee = BigDecimal.ZERO;
					BigDecimal employer = BigDecimal.ZERO;
					BigDecimal volun = BigDecimal.ZERO;
					BigDecimal total = BigDecimal.ZERO;

					Object object = memberContributionRepository.sumOfMemberContribution(
							memberContributionEntity.getMemberId(), memberContributionEntity.getPolicyId(),
							financialYear, true);
					long longsvsmethodsecondTime = System.currentTimeMillis();
					System.out.println("That took MemberContributionSummaryEntity" + (longsvsmethodsecondTime - longsvsmethodTime) + " milliseconds");
					
					if (object != null) {
						Object[] ob = (Object[]) object;
						employee = NumericUtils
								.stringToBigDecimal(NumericUtils.getNotNullStringVal(String.valueOf(ob[0])));
						employer = NumericUtils
								.stringToBigDecimal(NumericUtils.getNotNullStringVal(String.valueOf(ob[1])));
						volun = NumericUtils
								.stringToBigDecimal(NumericUtils.getNotNullStringVal(String.valueOf(ob[2])));
						total = NumericUtils
								.stringToBigDecimal(NumericUtils.getNotNullStringVal(String.valueOf(ob[3])));
					}

					if (topSummaryEntity != null) {
						topSummaryEntity.setModifiedOn(DateUtils.sysDate());
						topSummaryEntity.setModifiedBy(role);
						topSummaryEntity.setOpeningBalance(topSummaryEntity.getClosingBalance());
						topSummaryEntity
								.setClosingBalance(NumericUtils.bigDecimalValid(topSummaryEntity.getClosingBalance())
										.add(memberContributionEntity.getTotalContribution()));
						topSummaryEntity.setTotalContribution(total);
					} else {
						topSummaryEntity = new MemberContributionSummaryEntity();

						topSummaryEntity.setCreatedOn(DateUtils.sysDate());
						topSummaryEntity.setCreatedBy(role);
						topSummaryEntity.setModifiedOn(DateUtils.sysDate());
						topSummaryEntity.setModifiedBy(role);
						topSummaryEntity.setOpeningBalance(BigDecimal.ZERO);
						topSummaryEntity.setClosingBalance(total);
						topSummaryEntity.setTotalContribution(total);
						topSummaryEntity.setFinancialYear(DateUtils.getFinancialYrByDt(DateUtils.sysDate()));
						topSummaryEntity.setIsActive(true);
						topSummaryEntity.setPolicyId(memberContributionEntity.getPolicyId());
						topSummaryEntity.setLicId(memberContributionEntity.getLicId());
						topSummaryEntity.setMemberId(memberContributionEntity.getMemberId());
					}
					topSummaryEntity.setTotalEmployeeContribution(employee);
					topSummaryEntity.setTotalEmployerContribution(employer);
					topSummaryEntity.setTotalVoluntaryContribution(volun);
					sets.add(topSummaryEntity);
				});
				
				long longsvssecondTime = System.currentTimeMillis();
				System.out.println("That took MemberContributionSummaryEntity" + (longsvssecondTime - longsvsTime) + " milliseconds");
				memberContributionSummaryRepository.saveAll(sets);
				logger.info("AdjustmentContributionCalcServiceImpl :: saveMemberContributionSummary :: Ends");
				return sets;
			}
		} catch (Exception e) {
			logger.error("Exception :: AdjustmentContributionCalcServiceImpl :: saveMemberContributionSummary :: ", e);
		}
		return Collections.emptySet();
	}

}