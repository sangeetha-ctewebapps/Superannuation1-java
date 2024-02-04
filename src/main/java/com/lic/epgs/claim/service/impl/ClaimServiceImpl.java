package com.lic.epgs.claim.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimEntityConstants;
import com.lic.epgs.claim.constants.ClaimErrorConstants;
import com.lic.epgs.claim.constants.ClaimStatus;
import com.lic.epgs.claim.dto.ClaimCheckerActionRequestDto;
import com.lic.epgs.claim.dto.ClaimDto;
import com.lic.epgs.claim.dto.ClaimFundExportDto;
import com.lic.epgs.claim.dto.ClaimMakerActionRequestDto;
import com.lic.epgs.claim.entity.ClaimEntity;
import com.lic.epgs.claim.repository.ClaimRepository;
import com.lic.epgs.claim.service.ClaimService;
import com.lic.epgs.claim.service.SaveClaimService;
import com.lic.epgs.claim.temp.service.TempClaimService;
import com.lic.epgs.common.dto.ApiResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.dto.ErrorDto;
import com.lic.epgs.fund.repo.MemberFundStatementDetailsRepo;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.NumericUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.lic.epgs.claim.constants.ClaimConstants;	
import org.springframework.web.client.RestTemplate;

import com.lic.epgs.common.entity.CommonStatusEntity;
import com.lic.epgs.common.integration.dto.AnnuityOption;
import com.lic.epgs.common.integration.dto.AnnuityOptionalResponse;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.common.repository.CommonStatusRepository;

import com.lic.epgs.claim.dto.CommonAnnuityDto;
import com.lic.epgs.policy.dto.CustomerBasicDetailsDto;
import com.lic.epgs.policy.dto.MPHContactPersonDetailsDto;
import com.lic.epgs.policy.dto.ProposalAnnuityDto;
import com.lic.epgs.policy.dto.ProposalBasicDetailsDto;
import com.lic.epgs.policy.dto.ProposalChannelDetailsDto;
import com.lic.epgs.policy.dto.ProposalProductDetailsDto;
import com.lic.epgs.policy.entity.MphAddressEntity;
import com.lic.epgs.policy.entity.MphBankEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.repository.MphAddressRepository;
import com.lic.epgs.policy.repository.MphBankRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.policy.repository.PolicyRulesRepository;
import com.lic.epgs.policy.service.PolicyService;

import com.lic.epgs.utils.DateUtils;
import com.fasterxml.jackson.databind.JsonNode;	
import com.lic.epgs.claim.constants.ClaimConstants;

import org.springframework.web.client.RestTemplate;

import com.lic.epgs.claim.dto.CommonAnnuityDto;	
import com.lic.epgs.claim.dto.MphAddressDetailsAnnuityDto;	
import com.lic.epgs.claim.dto.MphBankAnnuityDto;	
import com.lic.epgs.claim.dto.MphContactDetailsAnnuityDto;	
import com.lic.epgs.claim.dto.MphMasterAnnuityDto;	
import com.lic.epgs.claim.dto.PolicyMasterAnnuityDto;

import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.NumericUtils;

import com.lic.epgs.policy.service.impl.PolicyServiceImpl;

import java.math.BigDecimal;
import java.sql.Types;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.StoredProcedureQuery;

import org.aspectj.weaver.NewConstructorTypeMunger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.lic.epgs.claim.dto.AccountingGLcodeDto;

import com.lic.epgs.claim.dto.SaAccountConfigMasterRequestDto;
import com.lic.epgs.claim.dto.SaAccountConfigMasterResponseDto;

import com.lic.epgs.claim.entity.ProcSaStoredProcedureRequestEntity;
import com.lic.epgs.claim.entity.ProcSaStoredProcedureResponseEntity;

import com.lic.epgs.claim.repository.ProcSaStoredProcedureRequestRepository;
import com.lic.epgs.claim.repository.ProcSaStoredProcedureResponseRepository;

import com.lic.epgs.claim.temp.entity.TempClaimAnnuityCalcEntity;
import com.lic.epgs.claim.temp.repository.TempClaimAnnuityCalcRepository;
import com.lic.epgs.claim.temp.repository.TempClaimCommutationCalcRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrRepository;
import com.lic.epgs.claim.temp.repository.TempClaimRepository;
import com.lic.epgs.payout.dto.ReinitiateRequestDto;
import com.lic.epgs.payout.dto.ReinitiateResponse;
import com.lic.epgs.payout.entity.PayoutAnnuityCalcEntity;
import com.lic.epgs.payout.repository.PayoutAnnuityCalcRepository;
import com.lic.epgs.payout.temp.entity.TempPayoutAnnuityCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutCommutationCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.repository.TempPayoutAnnuityCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutCommutationCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.claim.dto.CommonGlCodesResponseDto;
import java.util.Date;

import java.util.stream.Collectors;
import com.lic.epgs.claim.temp.entity.TempClaimCommutationCalcEntity;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrEntity;
import com.lic.epgs.claim.temp.entity.TempClaimAnnuityCalcEntity;

import aj.org.objectweb.asm.Type;
import oracle.jdbc.OracleTypes;




@Service

public class ClaimServiceImpl implements ClaimService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	ClaimRepository claimRepository;

	@Autowired
	TempClaimService tempClaimService;

	@Autowired
	SaveClaimService saveClaimService;
	
	@Autowired
	private MemberFundStatementDetailsRepo memberFundStatementDetailsRepo;
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	IntegrationService integrationService;

	@Autowired
	PolicyService policyService;

	@Autowired
	PolicyMasterRepository policyMasterRepository;
	
	@Autowired
	private CommonStatusRepository commonStatusRepository;
	
	@Autowired
	PolicyRulesRepository policyRulesRepository;

	@Autowired
	MphMasterRepository mphMasterRepository;
	
	@Autowired
	MphAddressRepository mphAddressRepository;
	
	@Autowired
	MphBankRepository mphBankRepository;

	@Autowired
	PolicyServiceImpl policyServiceImpl;
	
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	EntityManager entityManager;

	@Autowired
	TempPayoutRepository tempPayoutRepository;
	
	@Autowired
	TempPayoutMbrRepository tempPayoutMbrRepository;
	
	@Autowired
	TempPayoutCommutationCalcRepository tempPayoutCommutationCalcRepository;
	
	@Autowired
	TempClaimAnnuityCalcRepository tempClaimAnnuityCalcRepository;
	
	@Autowired
	ProcSaStoredProcedureRequestRepository procSaStoredProcedureRequestRepository;
	
	@Autowired
	ProcSaStoredProcedureResponseRepository procSaStoredProcedureResponseRepository;
	
	@Autowired
	Environment environment;
	
	@Autowired
	TempClaimRepository tempClaimRepository;
	
	@Autowired
	TempClaimMbrRepository tempClaimMbrRepository;
	
	@Autowired
	TempClaimCommutationCalcRepository tempClaimCommutationCalcRepository;

	private Specification<ClaimEntity> findByClaimNo(String claimNo) {
		return (root, query, criteriaBuilder) -> {
			criteriaBuilder.and(criteriaBuilder.equal(root.get(ClaimEntityConstants.IS_ACTIVE), Boolean.TRUE));
			return criteriaBuilder.equal(root.get(ClaimEntityConstants.CLAIM_NO), claimNo);
		};
	}

	@Override
	public ApiResponseDto<String> updateMakerAction(ClaimMakerActionRequestDto request) {
		if (request.getClaimNo() != null) {

			Optional<ClaimEntity> optional = claimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
					Boolean.TRUE);
			if (optional.isPresent()) {
				return ApiResponseDto
						.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_STATUS).build());
			} else {
				
				return tempClaimService.updateMakerAction(request);
			}

		} else {
			return ApiResponseDto.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_ONBOARD_NO).build());
		}

	}

	@Override
	public ApiResponseDto<String> updateCheckerAction(ClaimCheckerActionRequestDto request) {
		ApiResponseDto<String> response=new ApiResponseDto<String>();
		logger.info("ClaimServiceImpl{}::updateCheckerAction::{}::start");
		try {
			
		if (request.getClaimNo() != null) {

			Optional<ClaimEntity> optional = claimRepository.findByClaimNoAndIsActive(request.getClaimNo(),
					Boolean.TRUE);
			if (optional.isPresent()) {
				return ApiResponseDto
						.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_STATUS).build());
			} else {
				if (request.getAction().equals(ClaimStatus.APPROVE.val())
						|| request.getAction().equals(ClaimStatus.REJECT.val())) {
					tempClaimService.updateCheckerAction(request);

					String initimation = saveClaimService.insert(request.getClaimNo());
					String status = null;
					if (request.getAction().equals(ClaimStatus.APPROVE.val())) {
						status = ClaimErrorConstants.APPROVED_SUCCUSSFULLY;
					} else if (request.getAction().equals(ClaimStatus.REJECT.val())) {
						status = ClaimErrorConstants.REJECTED_SUCCUSSFULLY;
					}
					return ApiResponseDto.success(null, "Clim IntimationNo : " + initimation + " " + status);
				} else {
					response= tempClaimService.updateCheckerAction(request);
				}
			}

		} else {
			return ApiResponseDto
					.error(ErrorDto.builder().message(ClaimErrorConstants.INVALID_CLAIM_ONBOARD_NO).build());
		}
		}catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		logger.info("ClaimServiceImpl{}::updateCheckerAction::{}::end");
		return response;
	}

	@Override
	public boolean checkClaimStatus(String claimNo) {
		long count = claimRepository.count(findByClaimNo(claimNo));
		return (count > 0);
	}

	@Override
	@Transactional
	public ApiResponseDto<ClaimDto> findClaimDetails(String claimNo) {
		Optional<ClaimEntity> optional = claimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
		if (optional.isPresent()) {
			return convertEntityToDto(optional.get());
		} else {
			return tempClaimService.findClaimDetails(claimNo);
		}

	}

	private ApiResponseDto<ClaimDto> convertEntityToDto(ClaimEntity claimEntity) {
		ClaimDto response = modelMapper.map(claimEntity, ClaimDto.class);
		Double commutatiomSum =0d;
		Double annuitySum =0d;
		response.setTotalShortReserve(NumericUtils.doubleRoundInMath(commutatiomSum+annuitySum, 2));
//		if(!response.getClaimMbr().getClaimCommutationCalc().isEmpty()) {
//		List<Double> commutaionSR=response.getClaimMbr().getClaimCommutationCalc().stream().map(i->i.getShortReserve()).collect(Collectors.toList());
//		commutatiomSum = commutaionSR.stream().collect(Collectors.summingDouble(Double::doubleValue));
//		
//		}
//		if(!response.getClaimMbr().getClaimAnuityCalc().isEmpty()) {
//		List<Double> annuitySR=response.getClaimMbr().getClaimAnuityCalc().stream().map(i->i.getShortReserve()).collect(Collectors.toList());
//		annuitySum = annuitySR.stream().collect(Collectors.summingDouble(Double::doubleValue));
//		}
//		response.setTotalShortReserve(commutatiomSum+annuitySum);
		return ApiResponseDto.success(response);
	}
	
	
	

	@Override
	public ByteArrayInputStream fundSummaryDownload(String memberId, String financialYear,String variant,Integer frequency) throws IOException {
		logger.info("ClaimMbrController -- exportFundCalculation --started");
		
		List<Object[]> fundDetailsList = null;
		
		List<String> headers = null;
		
		List<String> data = null;
		
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

		try (ByteArrayOutputStream out = new ByteArrayOutputStream(); CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
			if(memberId != null && financialYear != null && !memberId.equalsIgnoreCase("") && !financialYear.equalsIgnoreCase("")) {
				
				
				
				if(variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V1) || variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V3)) {
				
				 fundDetailsList = memberFundStatementDetailsRepo.getFund(memberId, financialYear);
				 
				 headers = Arrays.asList("S.No", "Date", "Type",
							"Amount", "Interest Rate", "No of Days","Interest Amount", "Total");
					csvPrinter.printRecord(headers);
				
				}
				else if(variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V2)) {
					
					fundDetailsList = memberFundStatementDetailsRepo.getFundDetails(memberId, financialYear,frequency);
					
					headers = Arrays.asList("S.No", "Date", "Type",
							"Amount", "Interest Rate", "No of Days","Interest Amount","AIR","MFR","FMC","FMC Recon Days","GST","Total");
					csvPrinter.printRecord(headers);
				}
		
				for(Object[] obj :fundDetailsList) {
					
					ClaimFundExportDto dto = new ClaimFundExportDto();
					
					if(obj[0] != null)
						dto.setSNo(obj[0].toString());
					
					if(obj[2] != null)
						dto.setDate(obj[1].toString());
					
					if(obj[2] != null) 
						dto.setType(obj[2].toString());
					
					if(obj[3] != null)
						dto.setAmount(obj[3].toString());
					
					if(obj[4] != null)
						dto.setIntRate(obj[4].toString());
					
					if(obj[5] != null)
						dto.setNoOfDays(obj[5].toString());
					
					if(obj[6] != null)
						dto.setInterestAmount(obj[6].toString());
					
					if(variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V1) || variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V3)) {
						if(obj[7] != null) {
							dto.setTotal(obj[7].toString());
						}
						}
						else if(variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V2)) {
							
							if(obj[7] != null) {
							dto.setAIR(obj[7].toString());
							}
							if(obj[8] != null) {
								dto.setMFR(obj[8].toString());
							}
							if(obj[9] != null) {
								dto.setFMC(obj[9].toString());
							}
							if(obj[10] != null) {
								dto.setFMCReconDays(obj[10].toString());
							}
							if(obj[11] != null) {
								dto.setGST(obj[11].toString());
							}
							if(obj[12] != null) {
								dto.setTotal(obj[12].toString());
							}
						}
					
					if(variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V1) || variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V3)) {
					
					 data = Arrays.asList(dto.getSNo(),dto.getDate() ,dto.getType(),dto.getAmount(),
	
							dto.getIntRate(),dto.getNoOfDays(),dto.getInterestAmount(),dto.getTotal());		
					
					csvPrinter.printRecord(data);
					
					}
					else if(variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V2)) {
						
						 data = Arrays.asList(dto.getSNo(),dto.getDate() ,dto.getType(),dto.getAmount(),
									dto.getIntRate(),dto.getNoOfDays(),dto.getInterestAmount(),
									dto.getAIR(),dto.getMFR(),dto.getFMC(),dto.getFMCReconDays(),dto.getGST(),dto.getTotal());		
							
							csvPrinter.printRecord(data);
					}
				}
		
		}
			csvPrinter.flush();
			logger.info("ClaimMbrController -- exportFundCalculation --started");
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			logger.info("ClaimMbrController -- exportFundCalculation --Exception ",e);
			throw new IOException(e);
		}
		
	}

	@Override
	public CommonResponseDto fundSummaryGet(String memberId, String financialYear,String variant,Integer frequency) {
		CommonResponseDto commonDto = new CommonResponseDto();
		logger.info("ClaimMbrController -- fundSummaryGet --started");
		List<Object[]> fundDetailsList = null;
		try {
			
			if(!StringUtils.isBlank(memberId) && !StringUtils.isBlank(financialYear)) {
				
				if(variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V1) || variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V3)) {
				
				 fundDetailsList = memberFundStatementDetailsRepo.getFund(memberId, financialYear);
	
				}
				
				else if(variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V2)) {
					
					fundDetailsList = memberFundStatementDetailsRepo.getFundDetails(memberId, financialYear,frequency);
				}
				
//				List<Object[]> fundDetailsList = memberFundStatementDetailsRepo.getData();
				List<ClaimFundExportDto> dtoList = new ArrayList<>();
				if(!fundDetailsList.isEmpty()) {
					
					for(Object[] obj :fundDetailsList) {
						
						ClaimFundExportDto dto = new ClaimFundExportDto();
						
						if(obj[0] != null)
							dto.setSNo(obj[0].toString());
						
						if(obj[2] != null)
							dto.setDate(obj[1].toString());
						
						if(obj[2] != null) 
							dto.setType(obj[2].toString());
						
						if(obj[3] != null)
							dto.setAmount(obj[3].toString());
						
						if(obj[4] != null)
							dto.setIntRate(obj[4].toString());
						
						if(obj[5] != null)
							dto.setNoOfDays(obj[5].toString());
						
						if(obj[6] != null)
							dto.setInterestAmount(obj[6].toString());
						
						if(variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V1) || variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V3)) {
						if(obj[7] != null) {
							dto.setTotal(obj[7].toString());
						}
						}
						else if(variant.equalsIgnoreCase(ClaimEntityConstants.VARIANT_V2)) {
							
							if(obj[7] != null) {
							dto.setAIR(obj[7].toString());
							}
							if(obj[8] != null) {
								dto.setMFR(obj[8].toString());
							}
							if(obj[9] != null) {
								dto.setFMC(obj[9].toString());
							}
							if(obj[10] != null) {
								dto.setFMCReconDays(obj[10].toString());
							}
							if(obj[11] != null) {
								dto.setGST(obj[11].toString());
							}
							if(obj[12] != null) {
								dto.setTotal(obj[12].toString());
							}
						}
						
						dtoList.add(dto);
					}
					
				}
				
				


				commonDto.setResponseData(dtoList);
				commonDto.setTransactionStatus(CommonConstants.SUCCESS);
				commonDto.setTransactionMessage(CommonConstants.FETCH);
	
			}else {
				commonDto.setTransactionStatus(CommonConstants.FAIL);
				commonDto.setTransactionMessage("MemberId Or Financial Year is Null");
			}
	
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("ClaimMbrController -- fundSummaryGet --Exception ",e);
		}
		
		logger.info("ClaimMbrController -- fundSummaryGet --started");
		
		return commonDto;
	}
	
	@Override
	public CommonAnnuityDto getAnnuityDataBypolicyNo(String policyNumber) {
		logger.error("Exception:ClaimServiceImpl:getAnnuityDataBypolicyNo - Started");
		CommonAnnuityDto commonAnnuityDto = new CommonAnnuityDto();
		PolicyMasterAnnuityDto policyMasterAnnuityDto = new PolicyMasterAnnuityDto();

		try {

			List<Object[]> policyMasterObj = policyMasterRepository.getPolicyMasterData(policyNumber);

			if (policyMasterObj != null && policyMasterObj.size() > 0) {
				Object[] policyMasterEntity = policyMasterObj.get(0);

				Optional<CommonStatusEntity> commonStatus = commonStatusRepository
						.findById(String.valueOf(policyMasterEntity[4]));

				String policyStatus = commonStatus.isPresent() ? commonStatus.get().getDescription1() : "";

				if (policyMasterEntity != null) {
					policyMasterAnnuityDto.setPolicyNumber(String.valueOf(policyMasterEntity[0]));
					policyMasterAnnuityDto.setModifiedBy(String.valueOf(policyMasterEntity[8]));
					policyMasterAnnuityDto.setPolicyType(String.valueOf(policyMasterEntity[4]));
					policyMasterAnnuityDto.setCreatedBy(String.valueOf(policyMasterEntity[6]));
					policyMasterAnnuityDto.setPolicyStatus(policyStatus);
//				policyMasterAnnuityDto.setGstBorneBy(NumericUtils.convertLongToString(policyMasterEntity.getMphId()));
//				policyMasterAnnuityDto.setRateApplicable("GSCA?");
					policyMasterAnnuityDto.setRateApplicable("GSCA");

					logger.info("proposalDate :" + String.valueOf(policyMasterEntity[1]));
					policyMasterAnnuityDto.setProposalDate(String.valueOf(policyMasterEntity[1]) != null
							? DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(String.valueOf(policyMasterEntity[1]))
							: null);

					policyMasterAnnuityDto.setPolicyCommencementDt(String.valueOf(policyMasterEntity[2]) != null
							? DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(String.valueOf(policyMasterEntity[2]))
							: null);

					policyMasterAnnuityDto.setArd(String.valueOf(policyMasterEntity[5]) != null
							? DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(String.valueOf(policyMasterEntity[5]))
							: null);

					policyMasterAnnuityDto.setCreatedOn(String.valueOf(policyMasterEntity[7]) != null
							? DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(String.valueOf(policyMasterEntity[7]))
							: null);

					policyMasterAnnuityDto.setModifiedOn(String.valueOf(policyMasterEntity[1]) != null
							? DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(String.valueOf(policyMasterEntity[1]))
							: null);

					policyMasterAnnuityDto.setPolicyCreationDate(String.valueOf(policyMasterEntity[1]) != null
							? DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(String.valueOf(policyMasterEntity[1]))
							: null);

					policyMasterAnnuityDto.setPolicyIssueDate(String.valueOf(policyMasterEntity[2]) != null
							? DateUtils.stringToDateYYYYMMDDHHMMSSHyphen(String.valueOf(policyMasterEntity[2]))
							: null);

					commonAnnuityDto.setPolicyDetailsSupAnnuRequest(policyMasterAnnuityDto);

					String annuityOption = policyRulesRepository
							.findByPolicyId(NumericUtils.convertStringToLong(String.valueOf(policyMasterEntity[10])));

					if (annuityOption != null) {
						policyMasterAnnuityDto.setAnnuityOption(getAnnuityoptional(annuityOption));
						commonAnnuityDto.setPolicyDetailsSupAnnuRequest(policyMasterAnnuityDto);
					}

					List<Object[]> mphMasterObj = mphMasterRepository
							.getMphDetails(NumericUtils.convertStringToLong(String.valueOf(policyMasterEntity[9])));

					if (mphMasterObj != null && mphMasterObj.size() > 0) {
						Object[] mphMasterEntity = mphMasterObj.get(0);

						if (mphMasterEntity == null) {
							commonAnnuityDto.setStatus(ClaimConstants.FAIL);
							commonAnnuityDto.setMessage("MPH Details " + ClaimConstants.NO_RECORD_FOUND);
							return commonAnnuityDto;
						}
						MphMasterAnnuityDto mphMasterAnnuityDto = new MphMasterAnnuityDto();

						MphBankEntity mphBankEntity = mphBankRepository.findByMphIdAndIsActiveTrue(
								NumericUtils.convertStringToLong(String.valueOf(mphMasterEntity[0])));
						MphBankAnnuityDto mphBankAnnuityDto = new MphBankAnnuityDto();

						if (mphBankEntity != null) {
							mphBankAnnuityDto.setAccountNumber(mphBankEntity.getAccountNumber());

							mphBankAnnuityDto.setAccountType(mphBankEntity.getAccountType() != null 
									? (mphBankEntity.getAccountType().equalsIgnoreCase("SAVINGS") ? "SAVING"
											: mphBankEntity.getAccountType().toUpperCase())
									: null);
							
							mphBankAnnuityDto.setIfscCode(mphBankEntity.getIfscCode());
							mphBankAnnuityDto.setBankBranch(mphBankEntity.getBankBranch());
							mphBankAnnuityDto.setBankName(mphBankEntity.getBankName());
							mphBankAnnuityDto.setCountryCode(mphBankEntity.getCountryCode());
							mphBankAnnuityDto.setStdCode(mphBankEntity.getStdCode());
							mphBankAnnuityDto.setLandlineNumber(mphBankEntity.getLandlineNumber());
							mphBankAnnuityDto.setEmailId(mphBankEntity.getEmailId());
							mphBankAnnuityDto.setModifiedBy(mphBankEntity.getModifiedBy());
							mphBankAnnuityDto.setModifiedOn(mphBankEntity.getModifiedOn());
							commonAnnuityDto.setMphBankDetailRequest(mphBankAnnuityDto);
						}

						MphAddressEntity mphAddressEntity = mphAddressRepository.findByMphIdAndIsActiveTrue(
								NumericUtils.convertStringToLong(String.valueOf(mphMasterEntity[0])));

						List<MphAddressDetailsAnnuityDto> listMphAddressDetailsAnnuityDto = new ArrayList<MphAddressDetailsAnnuityDto>();
						if (mphAddressEntity != null) {
							MphAddressDetailsAnnuityDto mphAddressDetailsAnnuityDto = new MphAddressDetailsAnnuityDto();

							mphAddressDetailsAnnuityDto.setAddressLine1(mphAddressEntity.getAddressLine1());
							mphAddressDetailsAnnuityDto.setAddressLine2(mphAddressEntity.getAddressLine2());
							mphAddressDetailsAnnuityDto.setAddressLine3(mphAddressEntity.getAddressLine3());
							mphAddressDetailsAnnuityDto.setPinCode((mphAddressEntity.getPincode()));
							mphAddressDetailsAnnuityDto.setDistrict(mphAddressEntity.getDistrict());
							mphAddressDetailsAnnuityDto.setState(mphAddressEntity.getStateName());
							mphAddressDetailsAnnuityDto.setCountry(mphAddressEntity.getCountryId());
							mphAddressDetailsAnnuityDto.setCity(mphAddressEntity.getCityLocality());
							mphAddressDetailsAnnuityDto.setModifiedBy(mphAddressEntity.getModifiedBy());
							mphAddressDetailsAnnuityDto.setModifiedOn(mphAddressEntity.getModifiedOn());

							listMphAddressDetailsAnnuityDto.add(mphAddressDetailsAnnuityDto);
						}
						commonAnnuityDto.setMphaddressDetailsRequestList(listMphAddressDetailsAnnuityDto);

						ProposalAnnuityDto proposalAnnuityDto = policyServiceImpl
								.getProposalDetails(String.valueOf(mphMasterEntity[1]));

						CustomerBasicDetailsDto customerBasicDetailsDto = proposalAnnuityDto.getCustomerBasicDetails();

						ProposalBasicDetailsDto proposalBasicDetailsDto = proposalAnnuityDto.getProposalBasicDetails();

						ProposalProductDetailsDto proposalProductDetailsDto = proposalAnnuityDto
								.getProposalProductDetailsDto();

						ProposalChannelDetailsDto proposalChannelDetailsDto = proposalAnnuityDto
								.getProposalChannelDetails();

						JsonNode jsonNode = integrationService
								.getProposalDetailsByProposalNumber(String.valueOf(mphMasterEntity[1]));

						if (jsonNode != null) {
							JsonNode proposeDetails = jsonNode.path("responseData").path("customerDetails")
									.path("groupCustomerDetails");
							String groupName = proposeDetails.path("groupName").textValue();
							if (groupName != null) {
								mphMasterAnnuityDto.setCustomerGroupName(groupName);
							}
						}

						if (mphMasterEntity != null) {
							mphMasterAnnuityDto.setMphName(String.valueOf(mphMasterEntity[2]));
							mphMasterAnnuityDto.setMphCode(String.valueOf(mphMasterEntity[3]));
							mphMasterAnnuityDto.setCreatedBy(String.valueOf(mphMasterEntity[4]));
							mphMasterAnnuityDto.setCin(String.valueOf(mphMasterEntity[5]));
							mphMasterAnnuityDto.setPan(String.valueOf(mphMasterEntity[6]));
							mphMasterAnnuityDto.setCustomerCode(String.valueOf(mphMasterEntity[3]));
							mphMasterAnnuityDto.setCustomerName(String.valueOf(mphMasterEntity[2]));

							if (customerBasicDetailsDto != null) {
								mphMasterAnnuityDto.setCustomerType(
										NumericUtils.convertLongToString(customerBasicDetailsDto.getCustomerType()));
								mphMasterAnnuityDto.setApan(customerBasicDetailsDto.getApan());
							}
							if (proposalProductDetailsDto != null) {
								mphMasterAnnuityDto.setProductCode(proposalProductDetailsDto.getProductCode());
								
							Long numberOfLives = (proposalProductDetailsDto.getNumberOfLives() !=null) ? 
									Long.valueOf(proposalProductDetailsDto.getNumberOfLives()): null;
								
								mphMasterAnnuityDto.setNumberOfLives(numberOfLives);
							}
							if (proposalChannelDetailsDto != null) {
								mphMasterAnnuityDto
										.setMarketingOfficercode(proposalChannelDetailsDto.getMarketingOfficerCode());
								mphMasterAnnuityDto
										.setMarketingOfficername(proposalChannelDetailsDto.getMarketingOfficerName());
								mphMasterAnnuityDto
										.setIntermediaryName(proposalChannelDetailsDto.getIntermediaryName());
								mphMasterAnnuityDto
										.setIntermediaryCode(proposalChannelDetailsDto.getIntermediaryCode());
							}
							if (proposalBasicDetailsDto != null) {
								mphMasterAnnuityDto
										.setMphSource(proposalBasicDetailsDto.getProposalSourse() == null ? ""
												: proposalBasicDetailsDto.getProposalSourse());
								mphMasterAnnuityDto.setGstin(proposalBasicDetailsDto.getGstin());
								mphMasterAnnuityDto.setGstinApplicable(proposalBasicDetailsDto.getGstinApplicable());
								
								Long gstIn = (proposalBasicDetailsDto.getGstin() !=null) ? 
										Long.valueOf(proposalBasicDetailsDto.getGstin()): null;
								
								mphMasterAnnuityDto.setGstinRate(gstIn);
								mphMasterAnnuityDto.setUrbanOrRural(proposalBasicDetailsDto.getUrbanOrRural());
							}
							commonAnnuityDto.setMphbasicDetailRequest(mphMasterAnnuityDto);
						}

						MphContactDetailsAnnuityDto mphContactDetailsAnnuityDto = new MphContactDetailsAnnuityDto();
						List<MPHContactPersonDetailsDto> mphContactPersonDetailsDto = proposalAnnuityDto
								.getMphContactDetails();

						if (mphContactPersonDetailsDto != null) {

							for (MPHContactPersonDetailsDto contactPersonDetailsDto : mphContactPersonDetailsDto) {

								if (mphContactPersonDetailsDto != null) {
									mphContactDetailsAnnuityDto.setFirstName(contactPersonDetailsDto.getFirstName());
									mphContactDetailsAnnuityDto.setMiddleName(contactPersonDetailsDto.getMiddleName());
									mphContactDetailsAnnuityDto.setLastName(contactPersonDetailsDto.getLastName());
									mphContactDetailsAnnuityDto
											.setDesignation(contactPersonDetailsDto.getDesignation());
									mphContactDetailsAnnuityDto.setCountry(contactPersonDetailsDto.getCountryCode());
									mphContactDetailsAnnuityDto
											.setMobileNumber(contactPersonDetailsDto.getMobileNumber());
									mphContactDetailsAnnuityDto.setStdCode(contactPersonDetailsDto.getStdCode());
									mphContactDetailsAnnuityDto
											.setLandlineNumber(contactPersonDetailsDto.getLandlineNumber());
									mphContactDetailsAnnuityDto.setEmailId(contactPersonDetailsDto.getEmailID());
									mphContactDetailsAnnuityDto
											.setContactName(contactPersonDetailsDto.getContactName());
//				mphContactDetailsAnnuityDto.setContactStatus(contactPersonDetailsDto.getContactStatus());
									mphContactDetailsAnnuityDto.setModifiedBy(contactPersonDetailsDto.getModifiedBy());
								
									Date modifiedOn = (StringUtils.isNotBlank(contactPersonDetailsDto.getModifiedOn()))?
											DateUtils.convertStringToDateDDMMYYYYSlash(contactPersonDetailsDto.getModifiedOn()):null;
									
									mphContactDetailsAnnuityDto.setModifiedDate(modifiedOn);

									commonAnnuityDto.setMphcontactDetailsRequest(mphContactDetailsAnnuityDto);

								}
							}
						}
						commonAnnuityDto.setStatus(ClaimConstants.SUCCESS);
						commonAnnuityDto.setMessage(ClaimConstants.RETRIVE);
					}
				}
			} else {
				commonAnnuityDto.setStatus(ClaimConstants.FAIL);
				commonAnnuityDto.setMessage(ClaimConstants.NO_RECORD_FOUND);
				return commonAnnuityDto;
			}

		} catch (Exception e) {
			logger.error("Exception:ClaimServiceImpl:getAnnuityDataBypolicyNo", e);
			commonAnnuityDto.setStatus(ClaimConstants.FAIL);
			commonAnnuityDto.setMessage(e.getMessage());
		}
		logger.info("ClaimServiceImpl -- getAnnuityDataBypolicyNo --Ended");
		return commonAnnuityDto;
	}

	public String getAnnuityoptional(String input) {
		logger.info("PayoutServiceImpl -------getAnnuityoptional-------- Start");
		String response = null;
		AnnuityOptionalResponse optional = integrationService.getAnnuityOptionReponse();
		if (optional != null) {
			for (AnnuityOption annuityOptOne : optional.getAnnuityOption()) {
				if (NumericUtils.convertIntegerToString(annuityOptOne.getAnnuityOptionId()).equals(input)) {
					response = annuityOptOne.getAnnuityOptionDesc();
					logger.info("PayoutServiceImpl -------getAnnuityoptional-------- ended");
					return response;
				}
			}
		}
		return response;

	}
	
	@Override
	@Transactional
	public SaAccountConfigMasterResponseDto saAccountConfigMasterStoredProcedure(Long claimId) {
		logger.info("claimServiceImpl:saAccountConfigMasterStoredProcedure Method:Start");
		SaAccountConfigMasterResponseDto accountConfigMasterResponse = new SaAccountConfigMasterResponseDto();

		try {	
			SaAccountConfigMasterRequestDto accountConfigMasterRequest = new SaAccountConfigMasterRequestDto();
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery(environment.getProperty("accounting.schema.name") + "."
							+ environment.getProperty("saAccountConfigMaster"));

			setSaAccountConfigMasterInputParameters(storedProcedureQuery);

			Optional<TempClaimEntity> tempClaimEntityOpt = tempClaimRepository.findByClaimIdAndIsActiveTrue(claimId);

			TempClaimEntity tempClaimEntity=tempClaimEntityOpt.get();
			

			if (tempClaimEntity != null) {
				
				TempClaimMbrEntity tempclaimMbrEntity = tempClaimMbrRepository
						.findByMemberId(tempClaimEntity.getClaimMbr().getMemberId());

				if (tempClaimEntity.getModeOfExit() == 1) {
					storedProcedureQuery.setParameter("P_S_ACCOUNT_CONTEXT", ClaimConstants.DEATH_ACCOUNT_CONTEXT);
					accountConfigMasterRequest.setPsAccountContext("Death Claim Payment Approval SA");
				} else if (tempClaimEntity.getModeOfExit() == 2 || tempClaimEntity.getModeOfExit() == 3) {
					storedProcedureQuery.setParameter("P_S_ACCOUNT_CONTEXT", ClaimConstants.MATURITY_ACCOUNT_CONTEXT);
					accountConfigMasterRequest.setPsAccountContext("Maturity OR Resignation Claim Payment Approval SA");
				} else {
					storedProcedureQuery.setParameter("P_S_ACCOUNT_CONTEXT", ClaimConstants.WITHDRAWAL_ACCOUNT_CONTEXT);
					accountConfigMasterRequest.setPsAccountContext("Withdrawal Claim Payment Approval SA");
				}

				List<Object[]> tempClaimCommutationCalcObj = tempClaimCommutationCalcRepository
						.getCommutatinDetailsConfigMaster(tempclaimMbrEntity.getMemberId());

				Double commutationAmount=0d;
				Double tdsAmount=0d;
				if (tempClaimCommutationCalcObj != null && tempClaimCommutationCalcObj.size() > 0) {
					Object[] tPCCalcEntity = tempClaimCommutationCalcObj.get(0);

					 commutationAmount = NumericUtils
							.convertStringToDouble((tPCCalcEntity[0] != null) ? tPCCalcEntity[0].toString() : null);
					 tdsAmount = NumericUtils
							.convertStringToDouble((tPCCalcEntity[1] != null) ? tPCCalcEntity[1].toString() : null);



						storedProcedureQuery.setParameter("P_N_TOTAL_AMOUNT",
								commutationAmount>0d ? commutationAmount : 0d);
						accountConfigMasterRequest
								.setPnTotalAmmount(commutationAmount>0d ? commutationAmount : 0d);

						storedProcedureQuery.setParameter("P_N_PAYMENT_AMOUNT",
								commutationAmount>0d ? commutationAmount : 0d);
						accountConfigMasterRequest
								.setPnTotalAmmount(commutationAmount>0d ? commutationAmount : 0d);

						storedProcedureQuery.setParameter("P_N_TDS_AMOUNT",
								commutationAmount>0d ? commutationAmount : 0d);
						accountConfigMasterRequest
								.setPnTotalAmmount(tdsAmount>0d ? tdsAmount : 0d);

					}

					List<Object[]> payoutAnnuityCalcObj = tempClaimAnnuityCalcRepository
							.getPayOutAnnuityDetails(tempclaimMbrEntity.getMemberId());
					Double purchasePrice=0d;
					Integer gstBorneBy=0;
					Double gstAmount =0d;

					if (!payoutAnnuityCalcObj.isEmpty()) {

						Object[] obj = payoutAnnuityCalcObj.get(0);

						 purchasePrice = NumericUtils
								.convertStringToDouble((obj[0] != null) ? obj[0].toString() : null);
						 gstBorneBy = NumericUtils
								.convertStringToInteger((obj[1] != null) ? obj[1].toString() : null);
						 gstAmount = NumericUtils
								.convertStringToDouble((obj[2] != null) ? obj[2].toString() : null);
					}
					
						storedProcedureQuery.setParameter("P_N_INTERFUND_AMOUNT", purchasePrice);
						accountConfigMasterRequest.setPnInterFundAmount((purchasePrice));

						if (gstBorneBy == 2 || gstBorneBy == 3) {
							storedProcedureQuery.setParameter("P_N_GST_EXPENSE_AMOUNT", purchasePrice);
							accountConfigMasterRequest.setPnGstExpenseAmount(purchasePrice);
						} else {
							storedProcedureQuery.setParameter("P_N_GST_EXPENSE_AMOUNT", 0.0);
							accountConfigMasterRequest.setPnGstExpenseAmount(0.0);
						}

						if (gstAmount == 2 || gstAmount == 3) {
							storedProcedureQuery.setParameter("P_N_ROC_ANNUITY_AMOUNT", purchasePrice);
							accountConfigMasterRequest.setPnRocAnnuityAmount(purchasePrice);
						} else if (purchasePrice > gstAmount) {
							storedProcedureQuery.setParameter("P_N_ROC_ANNUITY_AMOUNT", purchasePrice - gstAmount);
							accountConfigMasterRequest.setPnRocAnnuityAmount(purchasePrice - gstAmount);
						} else {
							storedProcedureQuery.setParameter("P_N_ROC_ANNUITY_AMOUNT", gstAmount - purchasePrice);
							accountConfigMasterRequest.setPnRocAnnuityAmount(gstAmount - purchasePrice);
						}

						storedProcedureQuery.setParameter("P_N_GST_LIABILITY_AMOUNT", gstAmount);
						accountConfigMasterRequest.setPnGstLiabilityAmount(gstAmount);

						storedProcedureQuery.setParameter("P_N_MVA_CHARGE", 0.0);
						accountConfigMasterRequest.setPnMvaCharge(0.0);
						storedProcedureQuery.setParameter("P_N_EXIT_CHARGE", 0.0);
						accountConfigMasterRequest.setPnExitCharge(0.0);
						storedProcedureQuery.setParameter("P_N_DR_SHORT_REMITTANCE", 0.0);
						accountConfigMasterRequest.setPnDrShortRemittance(0.0);
						storedProcedureQuery.setParameter("P_N_CR_SHORT_REMITTANCE", 0.0);
						accountConfigMasterRequest.setPnCrShortRemittance(0.0);

						if (purchasePrice != null && commutationAmount != null) {
							storedProcedureQuery.setParameter("P_S_ISPRICEAPPLICABLE", "B");
							accountConfigMasterRequest.setPsIsPriceApplicable("B");
						} else if (purchasePrice != null && commutationAmount == null) {
							storedProcedureQuery.setParameter("P_S_ISPRICEAPPLICABLE", "P");
							accountConfigMasterRequest.setPsIsPriceApplicable("P");
						} else if (purchasePrice == null && commutationAmount != null) {
							storedProcedureQuery.setParameter("P_S_ISPRICEAPPLICABLE", "C");
							accountConfigMasterRequest.setPsIsPriceApplicable("C");
						}

					}
					String claimOnboardNo = tempClaimEntity.getClaimOnboarding().getClaimOnBoardNo();


					if (claimOnboardNo != null) {

						saveProcSaStoredProcedureRequest(accountConfigMasterRequest, claimId, claimOnboardNo);

					}

				List<AccountingGLcodeDto> accountingGLCodeModelList = new ArrayList<>();

				storedProcedureQuery.execute();

				List<Object[]> glCodes = storedProcedureQuery.getResultList();
				for (Object[] obj : glCodes) {
					AccountingGLcodeDto model = new AccountingGLcodeDto();

					logger.info("getGlCodeDesc:{}", obj[0]);
					model.setGlCodeDesc(String.valueOf(obj[0]));

					logger.info("getAmount:{}", obj[1]);
					model.setAmount(new BigDecimal(String.valueOf(obj[1] != null ? obj[1] : 0)));

					logger.info("getAccountRuleCode:{}", obj[2]);
					model.setAccountRuleCode(String.valueOf(obj[2]));

					logger.info("getAccountType:{}", obj[3]);
					model.setAccountType(String.valueOf(obj[3]));

					logger.info("getGlCode:{}", obj[4]);
					model.setGlCode(String.valueOf(obj[4]));

					logger.info("getAccountRuleCodeDesc:{}", obj[5]);
					model.setAccountRuleCodeDesc(String.valueOf(obj[5]));

					accountingGLCodeModelList.add(model);

				}
				accountConfigMasterResponse.setAccountingGLCodeModel(accountingGLCodeModelList);

				List<AccountingGLcodeDto> accountingGLCodeModelLists = accountConfigMasterResponse
						.getAccountingGLCodeModel();
				for (AccountingGLcodeDto accountingGLCodeModel : accountingGLCodeModelLists) {
					saveProcSaStoredProcedureResponse(claimId, accountingGLCodeModel,tempClaimEntity.getClaimNo(),claimOnboardNo);
				}
				accountConfigMasterResponse.setStatus(ClaimConstants.SUCCESS);
				accountConfigMasterResponse.setMessage(ClaimConstants.RETRIVE);
				logger.info("Exit into ClaimServiceImpl : saAccountConfigMasterStoredProcedure ");
				return accountConfigMasterResponse;
			
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("claimServiceImpl:saAccountConfigMasterStoredProcedure Method -Error:", e);
		}

		logger.info("claimServiceImpl:saAccountConfigMasterStoredProcedure Method - Ended:");
		return accountConfigMasterResponse;

	}



	private void setSaAccountConfigMasterInputParameters(StoredProcedureQuery storedProcedureQuery) {
		storedProcedureQuery.registerStoredProcedureParameter("P_S_ACCOUNT_CONTEXT", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_TOTAL_AMOUNT", Double.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_PAYMENT_AMOUNT", Double.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_TDS_AMOUNT", Double.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_INTERFUND_AMOUNT", Double.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_GST_EXPENSE_AMOUNT", Double.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_ROC_ANNUITY_AMOUNT", Double.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_GST_LIABILITY_AMOUNT", Double.class,
				ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_MVA_CHARGE", Double.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_EXIT_CHARGE", Double.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_DR_SHORT_REMITTANCE", Double.class,
				ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_N_CR_SHORT_REMITTANCE", Double.class,
				ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_S_ISPRICEAPPLICABLE", String.class, ParameterMode.IN);
		storedProcedureQuery.registerStoredProcedureParameter("P_D_OUTDATA", Class.class, ParameterMode.REF_CURSOR);
	}

	public void saveProcSaStoredProcedureRequest(SaAccountConfigMasterRequestDto accountConfigMasterRequest ,
			Long claimId, String claimOnboardNo) {
		
	 ProcSaStoredProcedureRequestEntity existRequest=procSaStoredProcedureRequestRepository.findByClaimOnBoardNoAndIsActiveTrue(claimOnboardNo);
	       
	        
		if (accountConfigMasterRequest != null) {

			ProcSaStoredProcedureRequestEntity procSaStoredProcedureRequestEntity = new ProcSaStoredProcedureRequestEntity();
			procSaStoredProcedureRequestEntity.setClaimId(claimId);
			procSaStoredProcedureRequestEntity.setClaimOnBoardNo(claimOnboardNo);
			procSaStoredProcedureRequestEntity.setPsAccountContext(accountConfigMasterRequest.getPsAccountContext());
			procSaStoredProcedureRequestEntity.setPnTotalAmount(accountConfigMasterRequest.getPnTotalAmmount());
			procSaStoredProcedureRequestEntity.setPnPaymentAmount(accountConfigMasterRequest.getPsPaymentAmount());
			procSaStoredProcedureRequestEntity.setPnTdsAmount(accountConfigMasterRequest.getPnTdsAmmount());
			procSaStoredProcedureRequestEntity.setPnInterfundAmount(accountConfigMasterRequest.getPnInterFundAmount());
			procSaStoredProcedureRequestEntity
					.setPnGstExpenseAmount(accountConfigMasterRequest.getPnGstExpenseAmount());
			procSaStoredProcedureRequestEntity
					.setPnRocAnnuityAmount(accountConfigMasterRequest.getPnRocAnnuityAmount());
			procSaStoredProcedureRequestEntity
					.setPnGstLiabilityAmount(accountConfigMasterRequest.getPnGstLiabilityAmount());
			procSaStoredProcedureRequestEntity.setPnMvaCharge(accountConfigMasterRequest.getPnMvaCharge());
			procSaStoredProcedureRequestEntity.setPnExtiCharge(accountConfigMasterRequest.getPnExitCharge());
			procSaStoredProcedureRequestEntity
					.setPnDrShortRemittance(accountConfigMasterRequest.getPnDrShortRemittance());
			procSaStoredProcedureRequestEntity
					.setPnCrShortRemittance(accountConfigMasterRequest.getPnCrShortRemittance());
			procSaStoredProcedureRequestEntity
					.setPsIspriceApplicable(accountConfigMasterRequest.getPsIsPriceApplicable());
			procSaStoredProcedureRequestEntity.setCreatedOn(DateUtils.sysDate());
			procSaStoredProcedureRequestEntity.setModifiedOn(DateUtils.sysDate());
			procSaStoredProcedureRequestEntity.setIsActive(Boolean.TRUE);
			procSaStoredProcedureRequestRepository.save(procSaStoredProcedureRequestEntity);

		}
		 if(existRequest!=null) {
			 existRequest.setIsActive(false);
	        	procSaStoredProcedureRequestRepository.save(existRequest);
	        }
	}

	private ProcSaStoredProcedureResponseEntity saveProcSaStoredProcedureResponse(Long claimId,
			AccountingGLcodeDto accountConfigMasterResponse, String claimNo, String claimOnboardNo) {
		ProcSaStoredProcedureResponseEntity procSaStoredProcedureResponseEntity = new ProcSaStoredProcedureResponseEntity();

		if (accountConfigMasterResponse != null) {
			procSaStoredProcedureResponseEntity.setGlCode(accountConfigMasterResponse.getGlCode());
			procSaStoredProcedureResponseEntity.setClaimId(claimId);
			procSaStoredProcedureResponseEntity.setClaimNo(claimNo);
			procSaStoredProcedureResponseEntity.setClaimOnBoardNo(claimOnboardNo);
			procSaStoredProcedureResponseEntity.setAmount(NumericUtils.convertBigDecimalToDouble(accountConfigMasterResponse.getAmount()));
			procSaStoredProcedureResponseEntity.setAccountRuleCode(accountConfigMasterResponse.getAccountRuleCode());
			procSaStoredProcedureResponseEntity.setGlCodeDesc(accountConfigMasterResponse.getGlCodeDesc());
			procSaStoredProcedureResponseEntity.setAccountType(accountConfigMasterResponse.getAccountType());
			procSaStoredProcedureResponseEntity
					.setAccountRuleCodeDesc(accountConfigMasterResponse.getAccountRuleCodeDesc());
			procSaStoredProcedureResponseEntity.setIsActive(Boolean.TRUE);
			procSaStoredProcedureResponseEntity = procSaStoredProcedureResponseRepository
					.save(procSaStoredProcedureResponseEntity);
		}
		
		return procSaStoredProcedureResponseEntity;

	}
	
	
	
	
}
