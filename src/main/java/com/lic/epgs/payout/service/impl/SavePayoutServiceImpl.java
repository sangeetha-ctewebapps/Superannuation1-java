package com.lic.epgs.payout.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lic.epgs.common.utils.CommonDateUtils;
import com.lic.epgs.payout.entity.PayoutAnnuityCalcEntity;
import com.lic.epgs.payout.entity.PayoutCommutationCalcEntity;
import com.lic.epgs.payout.entity.PayoutDocumentDetail;
import com.lic.epgs.payout.entity.PayoutEntity;
import com.lic.epgs.payout.entity.PayoutFundValueEntity;
import com.lic.epgs.payout.entity.PayoutMbrAddressEntity;
import com.lic.epgs.payout.entity.PayoutMbrAppointeeEntity;
import com.lic.epgs.payout.entity.PayoutMbrBankDetailEntity;
import com.lic.epgs.payout.entity.PayoutMbrEntity;
import com.lic.epgs.payout.entity.PayoutMbrFundValueEntity;
import com.lic.epgs.payout.entity.PayoutMbrNomineeEntity;
import com.lic.epgs.payout.entity.PayoutNotesEntity;
import com.lic.epgs.payout.entity.PayoutPayeeBankDetailsEntity;
import com.lic.epgs.payout.repository.PayoutAnnuityCalcRepository;
import com.lic.epgs.payout.repository.PayoutCommutationCalcRepository;
import com.lic.epgs.payout.repository.PayoutDocumentDetailRepository;
import com.lic.epgs.payout.repository.PayoutFundValueRepository;
import com.lic.epgs.payout.repository.PayoutMbrAddressRepository;
import com.lic.epgs.payout.repository.PayoutMbrAppointeeRepository;
import com.lic.epgs.payout.repository.PayoutMbrBankDtlsRepository;
import com.lic.epgs.payout.repository.PayoutMbrFundValueRepository;
import com.lic.epgs.payout.repository.PayoutMbrNomineeRepository;
import com.lic.epgs.payout.repository.PayoutMbrRepository;
import com.lic.epgs.payout.repository.PayoutNotesRepository;
import com.lic.epgs.payout.repository.PayoutPayeeBankDetailsRepository;
import com.lic.epgs.payout.repository.PayoutRepository;
import com.lic.epgs.payout.restapi.dto.PayoutAddressRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutAnnuityRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutAnnuityRestApiRes;
import com.lic.epgs.payout.restapi.dto.PayoutBankRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutFinancialDetailRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutJointDetailRestApiRequest;
import com.lic.epgs.payout.restapi.dto.PayoutMbrPrslDtRestApiRequest;
import com.lic.epgs.payout.service.SavePayoutService;
import com.lic.epgs.payout.temp.entity.PayoutPayeeBankDetailsTempEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutAnnuityCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutCommutationCalcEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutDocumentDetail;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutFundValueEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrAddressEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrAppointeeEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrBankDetailEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrFundValueEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutMbrNomineeEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutNotesEntity;
import com.lic.epgs.payout.temp.repository.TempPayoutAnnuityCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutCommutationCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutDocumentDetailRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrAddressRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrAppointeeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrBankDtlsRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrNomineeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutNotesRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.NumericUtils;

@Service
@Transactional
public class SavePayoutServiceImpl implements SavePayoutService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempPayoutRepository tempPayoutRepository;

	@Autowired
	TempPayoutNotesRepository tempPayoutNotesRepository;

	@Autowired
	TempPayoutDocumentDetailRepository tempPayoutDocumentDetailRepository;

	@Autowired
	TempPayoutMbrNomineeRepository tempPayoutMbrNomineeRepository;

	@Autowired
	TempPayoutMbrBankDtlsRepository tempPayoutMbrBankDtlsRepository;

	@Autowired
	TempPayoutMbrAddressRepository tempPayoutMbrAddressRepository;

	@Autowired
	TempPayoutMbrAppointeeRepository tempPayoutMbrAppointeeRepository;

	@Autowired
	TempPayoutAnnuityCalcRepository tempPayoutAnnuityCalcRepository;

	@Autowired
	TempPayoutCommutationCalcRepository tempPayoutCommutationCalcRepository;

	@Autowired
	PayoutRepository payoutRepository;

	@Autowired
	PayoutMbrRepository payoutMbrRepository;

	@Autowired
	PayoutMbrNomineeRepository payoutMbrNomineeRepository;

	@Autowired
	PayoutMbrBankDtlsRepository payoutMbrBankDtlsRepository;

	@Autowired
	PayoutMbrAddressRepository payoutMbrAddressRepository;

	@Autowired
	PayoutMbrAppointeeRepository payoutMbrAppointeeRepository;

	@Autowired
	PayoutAnnuityCalcRepository payoutAnnuityCalcRepository;

	@Autowired
	PayoutCommutationCalcRepository payoutCommutationCalcRepository;

	@Autowired
	PayoutNotesRepository payoutNotesRepository;

	@Autowired
	PayoutDocumentDetailRepository payoutDocumentDetailRepository;

	@Autowired
	PayoutFundValueRepository payoutFundValueRepository;

	@Autowired
	PayoutMbrFundValueRepository payoutMbrFundValueRepository;

	@Autowired
	PayoutPayeeBankDetailsRepository payoutPayeeBankDetailsRepository;

	@Autowired
	private Environment environment;

	@Autowired
	RestTemplate restTemplate;
	@Override
	public PayoutEntity insert(String initiMationNo) {
		PayoutEntity payoutEntity = new PayoutEntity();
		logger.info("SavePayoutServiceImpl :: insert :: start ");
		try {
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByInitiMationNoAndIsActive(initiMationNo,
					Boolean.TRUE);

			if (optional.isPresent()) {
				TempPayoutEntity tempPayoutEntity = optional.get();

				TempPayoutMbrEntity tempPayoutMbrEntity = tempPayoutEntity.getPayoutMbr();

				payoutEntity = insertPayoutEntity(tempPayoutEntity);

				insertPayoutNotes(tempPayoutEntity, payoutEntity);

				insertPayoutDocuments(tempPayoutEntity, payoutEntity);

				PayoutMbrEntity payoutMbrEntity = insertPayoutMbr(tempPayoutMbrEntity, payoutEntity);

				insertPayoutMbrAddress(tempPayoutMbrEntity.getPayoutMbrAddresses(), payoutMbrEntity);

				insertPayoutMbrAppointee(tempPayoutMbrEntity.getPayoutMbrAppointeeDtls(), payoutMbrEntity);

				insertPayoutMbrBank(tempPayoutMbrEntity.getPayoutMbrBankDetails(), payoutMbrEntity);

				insertPayoutMbrNominee(tempPayoutMbrEntity.getPayoutMbrNomineeDtls(), payoutMbrEntity);

				insertPayoutMbrAnnuity(tempPayoutMbrEntity.getPayoutAnuityCalc(), payoutMbrEntity);

				insertPayoutMbrCommutation(tempPayoutMbrEntity.getPayoutCommutationCalc(), payoutMbrEntity);

				insertPayoutFundValue(tempPayoutMbrEntity.getPayoutFundValue(), payoutMbrEntity);

				insertPayoutMbrFundValue(tempPayoutMbrEntity.getPayoutMbrFundValue(), payoutMbrEntity);

				insertPayoutPayeeBankDetails(tempPayoutMbrEntity.getPayoutPayeeBank(), payoutMbrEntity);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("SavePayoutServiceImpl :: insert :: error is  "+e.getMessage());
		}
		logger.info("SavePayoutServiceImpl :: insert :: ended ");
		return payoutEntity;
	}

	
	@Override
	public PayoutEntity insertNew(TempPayoutEntity tempPayoutEntityUpdated) {
		logger.info("insertNew -------------Start");
		tempPayoutRepository.save(tempPayoutEntityUpdated);
		PayoutEntity payoutEntity = null;
		try {
			payoutEntity = new PayoutEntity();
			if (tempPayoutEntityUpdated!=null) {
				TempPayoutEntity tempPayoutEntity = tempPayoutEntityUpdated;

				TempPayoutMbrEntity tempPayoutMbrEntity = tempPayoutEntity.getPayoutMbr();

				payoutEntity = insertPayoutEntity(tempPayoutEntity);

				insertPayoutNotes(tempPayoutEntity, payoutEntity);

				insertPayoutDocuments(tempPayoutEntity, payoutEntity);

				PayoutMbrEntity payoutMbrEntity = insertPayoutMbr(tempPayoutMbrEntity, payoutEntity);

				insertPayoutMbrAddress(tempPayoutMbrEntity.getPayoutMbrAddresses(), payoutMbrEntity);

				insertPayoutMbrAppointee(tempPayoutMbrEntity.getPayoutMbrAppointeeDtls(), payoutMbrEntity);

				insertPayoutMbrBank(tempPayoutMbrEntity.getPayoutMbrBankDetails(), payoutMbrEntity);

				insertPayoutMbrNominee(tempPayoutMbrEntity.getPayoutMbrNomineeDtls(), payoutMbrEntity);

				insertPayoutMbrAnnuity(tempPayoutMbrEntity.getPayoutAnuityCalc(), payoutMbrEntity);

				insertPayoutMbrCommutation(tempPayoutMbrEntity.getPayoutCommutationCalc(), payoutMbrEntity);

				insertPayoutFundValue(tempPayoutMbrEntity.getPayoutFundValue(), payoutMbrEntity);

				insertPayoutMbrFundValue(tempPayoutMbrEntity.getPayoutMbrFundValue(), payoutMbrEntity);

				insertPayoutPayeeBankDetails(tempPayoutMbrEntity.getPayoutPayeeBank(), payoutMbrEntity);
				
				logger.info("insertNew -------------Endedd............");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("insertNew -------------error is "+e.getMessage());
		}

		return payoutEntity;
	}
	
	

	
	
	
	private PayoutAnnuityRestApiRes annutityRestApi(PayoutEntity payoutEntity) {
		PayoutAnnuityRestApiRes payoutAnnuityRestApiRes = new PayoutAnnuityRestApiRes();
		try {
			PayoutAnnuityRestApiRequest payoutAnnuityRestApiReq = new PayoutAnnuityRestApiRequest();
			payoutAnnuityRestApiReq.setPolicyNumber(payoutEntity.getMasterPolicyNo());
			payoutAnnuityRestApiReq.setSourceCode(null);
			payoutAnnuityRestApiReq.setUploadedBy(payoutEntity.getCreatedBy());
			/* payoutAnnuityRestApiReq.setUploadedOn(payoutEntity.getCreatedOn()); */
			payoutAnnuityRestApiReq.setAnAddressDtlReqList(annuityApiAddressRestApi(payoutEntity));
			payoutAnnuityRestApiReq.setAnBankDetailRequest(annuityApiBankDetailsRestApi(payoutEntity));
			payoutAnnuityRestApiReq.setAnFinancialDetailRequest(annuityApiFinancialDetailRestApi(payoutEntity));
			payoutAnnuityRestApiReq.setAnJointDetailRequest(annuityApiJointDetailRestApi(payoutEntity));
//			payoutAnnuityRestApiReq.setAnNomineeDtlRequestList(anNomineeDtlRestApiRequestList(payoutEntity));
			payoutAnnuityRestApiReq.setAntPrslDtlRequest(anPayoutMbrPrslDtRestApiRequest(payoutEntity));

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			String annuityUrl = environment.getProperty("SA_ANNUITY_CAL_API_FOR_PAYOUT");
			HttpEntity<PayoutAnnuityRestApiRequest> requestEntity = new HttpEntity<>(payoutAnnuityRestApiReq, headers);
			payoutAnnuityRestApiRes.setMessage("Skip Annuity API call");
			if (payoutAnnuityRestApiReq!=null) {

				ResponseEntity<PayoutAnnuityRestApiRes> responseEntity = restTemplate.postForEntity(annuityUrl,
						requestEntity, PayoutAnnuityRestApiRes.class);

				if (responseEntity.getBody() != null) {
					logger.info("AnnuityCal-PayoutAnnuityRestApiRes-End");
					payoutAnnuityRestApiRes = responseEntity.getBody();
					return payoutAnnuityRestApiRes;
				}
			}

		} catch (IllegalArgumentException e) {
			logger.info("AnnuityCal-PayoutAnnuityRestApiRes-Error:", e);
		}
		return payoutAnnuityRestApiRes;
	}

	private Set<PayoutAddressRestApiRequest> annuityApiAddressRestApi(PayoutEntity payoutEntity) {
		Set<PayoutAddressRestApiRequest> addressReqList = new HashSet<>();
		PayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
		if (payoutMbr != null) {
			List<PayoutMbrAddressEntity> payoutMbrAddresses = payoutMbr.getPayoutMbrAddresses();
			if (CommonUtils.isNonEmptyArray(payoutMbrAddresses)) {
				payoutMbrAddresses.forEach(address -> {
					PayoutAddressRestApiRequest payoutAddressReq = new PayoutAddressRestApiRequest();
					payoutAddressReq.setAddress1(address.getAddressLineOne());
					payoutAddressReq.setAddress2(address.getAddressLineTwo());
					payoutAddressReq.setAddress3(address.getAddressLineThree());
					payoutAddressReq.setCity(address.getCity());
					payoutAddressReq.setCountry(address.getCountry());
					payoutAddressReq.setDistrict(address.getDistrict());
					payoutAddressReq.setPinCode(address.getPinCode());
					payoutAddressReq.setState(address.getState());
					payoutAddressReq.setTypeOfAddress(address.getAddressType());
					addressReqList.add(payoutAddressReq);
				});
			}
		}
		return addressReqList;
	}

	private PayoutBankRestApiRequest annuityApiBankDetailsRestApi(PayoutEntity payoutEntity) {
		PayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
		if (payoutMbr != null) {
			List<PayoutMbrBankDetailEntity> payoutMbrBankDetails = payoutMbr.getPayoutMbrBankDetails();
			if (CommonUtils.isNonEmptyArray(payoutMbrBankDetails)) {
				PayoutMbrBankDetailEntity bank = payoutMbrBankDetails.get(0);
				PayoutBankRestApiRequest payoutBankRestApiReq = new PayoutBankRestApiRequest();
				payoutBankRestApiReq.setAccountNumber(bank.getAccountNumber());
				payoutBankRestApiReq.setAccountType(bank.getAccountType());
				payoutBankRestApiReq.setBankAddress(bank.getBankAddress());
				payoutBankRestApiReq.setBankBranch(bank.getBankBranch());
				payoutBankRestApiReq.setBankEmailId(bank.getEmailId());
				payoutBankRestApiReq.setConfirmAccountNumber(bank.getConfirmAccountNumber());
				payoutBankRestApiReq.setCountryCode(NumericUtils.convertIntegerToString(bank.getCountryCode()));
				payoutBankRestApiReq.setIfcsCode(bank.getIfscCode());
				payoutBankRestApiReq.setLandlineNumber(NumericUtils.convertIntegerToString(bank.getLandlineNumber()));
				payoutBankRestApiReq.setStdCode(NumericUtils.convertIntegerToString(bank.getStdCode()));

				return payoutBankRestApiReq;
			}
		}
		return null;

	}

	private PayoutFinancialDetailRestApiRequest annuityApiFinancialDetailRestApi(PayoutEntity payoutEntity) {
		PayoutFinancialDetailRestApiRequest payoutFinancialDetailRestApiRequest = new PayoutFinancialDetailRestApiRequest();

		PayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
		if (payoutMbr != null) {
			List<PayoutAnnuityCalcEntity> payoutAnuityCalc = payoutMbr.getPayoutAnuityCalc();
			if (CommonUtils.isNonEmptyArray(payoutAnuityCalc)) {
				payoutAnuityCalc.forEach(annuity -> {
					payoutFinancialDetailRestApiRequest.setAnnuityAmount(annuity.getAmtPaidTo().doubleValue());
					payoutFinancialDetailRestApiRequest.setAnnuityDueDate(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(annuity.getCreatedOn()));
					payoutFinancialDetailRestApiRequest.setAnnuityMode(annuity.getAnuityMode());
					payoutFinancialDetailRestApiRequest.setAnnuityOption(annuity.getAnnuityOption());
					payoutFinancialDetailRestApiRequest.setAnOrigin(null);
					payoutFinancialDetailRestApiRequest.setAnPayoutMethod(null);
					payoutFinancialDetailRestApiRequest.setArrears(annuity.getArrears());
					payoutFinancialDetailRestApiRequest.setBasicPension(annuity.getPension().intValue());
					payoutFinancialDetailRestApiRequest.setCertailLifePeriod(null);
					payoutFinancialDetailRestApiRequest.setCertainPeriod(null);
					payoutFinancialDetailRestApiRequest.setDaDueDate(null);
					payoutFinancialDetailRestApiRequest.setDateOfExit(null);
					payoutFinancialDetailRestApiRequest.setDateOfVesting(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutEntity.getDtOfExit()));
					payoutFinancialDetailRestApiRequest.setGstAmnt(annuity.getGstAmount());
					payoutFinancialDetailRestApiRequest.setIncomeTaxDeducted(null);
					payoutFinancialDetailRestApiRequest.setIncomeTaxPending(null);
					payoutFinancialDetailRestApiRequest.setIncomeTaxProjected(null);
					payoutFinancialDetailRestApiRequest
							.setModeOfExit(NumericUtils.convertIntegerToString(payoutEntity.getModeOfExit()));
					payoutFinancialDetailRestApiRequest
							.setPensionAmtPaidTo(NumericUtils.convertLongToString(annuity.getAmtPaidTo()));
					payoutFinancialDetailRestApiRequest.setPurchasePrice(annuity.getPurchasePrice());
					payoutFinancialDetailRestApiRequest.setRecovery(null);
				});
				return payoutFinancialDetailRestApiRequest;
			}
		}
		return null;

	}

	private PayoutJointDetailRestApiRequest annuityApiJointDetailRestApi(PayoutEntity payoutEntity) {
		List<String> annuityList = new ArrayList<>();
		annuityList.add("3");
		annuityList.add("4");
		annuityList.add("5");

		PayoutJointDetailRestApiRequest payoutJointDetailRestApiRequest = new PayoutJointDetailRestApiRequest();

		PayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
		if (payoutMbr != null) {
			List<PayoutAnnuityCalcEntity> payoutAnuityCalc = payoutMbr.getPayoutAnuityCalc();
			if (CommonUtils.isNonEmptyArray(payoutAnuityCalc)) {
				payoutAnuityCalc.forEach(anuity -> {
					if (annuityList.contains(anuity.getAnnuityOption())) {
						payoutJointDetailRestApiRequest.setJointLifeDOB(anuity.getDateOfBirth());
						payoutJointDetailRestApiRequest.setJointLifeFirstName(null);
						payoutJointDetailRestApiRequest.setJointLifeGender(null);
						payoutJointDetailRestApiRequest.setJointLifeLastName(null);
						payoutJointDetailRestApiRequest.setJointLifeMiddleName(null);
						payoutJointDetailRestApiRequest.setJointLifePercent(null);
					}
				});
				return payoutJointDetailRestApiRequest;
			}
		}
		return payoutJointDetailRestApiRequest;

	}

//	private Set<PayoutNomineeDtlRestApiRequest> anNomineeDtlRestApiRequestList(PayoutEntity payoutEntity) {
//
//		PayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
//		if (payoutMbr != null) {
//			if (CommonUtils.isNonEmptyArray(payoutMbrNomineeDtls)) {
//				Set<PayoutNomineeDtlRestApiRequest> payoutNomineeDtlRestApiRequestList = new HashSet<>();
//				payoutMbrNomineeDtls.forEach(nominee -> {
//					PayoutNomineeDtlRestApiRequest payoutNomineeDtlRestApiRequest = new PayoutNomineeDtlRestApiRequest();
//					payoutNomineeDtlRestApiRequest
//							.setAadharNo(NumericUtils.convertLongToString(nominee.getAadharNumber()));
//					payoutNomineeDtlRestApiRequest.setAccountNumber(nominee.getAccountNumber());
//					payoutNomineeDtlRestApiRequest.setAccountType(nominee.getAccountType());
//					payoutNomineeDtlRestApiRequest.setAccountTypeId(null);
//					payoutNomineeDtlRestApiRequest
//							.setAmtPercentage(NumericUtils.convertDoubleToString(nominee.getSharedAmount()));
//					payoutNomineeDtlRestApiRequest.setAnnuitantName(null);
//					payoutNomineeDtlRestApiRequest.setAnnuityNumber(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeAadharNumber(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeAccountNumber(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeAccountType(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeAccountTypeId(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeAddressId(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeAge(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeBankAddress(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeBankBranch(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeBankId(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeBankName(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeCode(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeContactNumber(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeCountryCode(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeDOB(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeFirstName(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeIfscCode(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeLandlineNo(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeLastName(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeMiddleName(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeName(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeOthers(null);
//					payoutNomineeDtlRestApiRequest.setAppointeepan(null);
//					payoutNomineeDtlRestApiRequest.setAppointeeStdCode(null);
//					payoutNomineeDtlRestApiRequest.setBankAddress(nominee.getAddressOne());
//					payoutNomineeDtlRestApiRequest.setBankBranch(nominee.getBankBranch());
//					payoutNomineeDtlRestApiRequest.setBankName(nominee.getBankName());
//					payoutNomineeDtlRestApiRequest
//							.setContactNumber(NumericUtils.convertLongToString(nominee.getMobileNo()));
//					payoutNomineeDtlRestApiRequest.setCountry(nominee.getCountry());
//					payoutNomineeDtlRestApiRequest.setDateOfBirth(nominee.getDob());
//					payoutNomineeDtlRestApiRequest.setEmailId(nominee.getEmailId());
//					payoutNomineeDtlRestApiRequest.setIfscCode(nominee.getIfscCode());
//					payoutNomineeDtlRestApiRequest
//							.setNmeStatus(NumericUtils.convertLongToString(nominee.getMaritalStatus()));
//					payoutNomineeDtlRestApiRequest.setNmeType(nominee.getClaimantType());
//					payoutNomineeDtlRestApiRequest.setNomineeAddress1(nominee.getAddressOne());
//					payoutNomineeDtlRestApiRequest.setNomineeAddress2(nominee.getAddressTwo());
//					payoutNomineeDtlRestApiRequest.setNomineeAddress3(nominee.getAddressThree());
//					payoutNomineeDtlRestApiRequest.setNomineeAge(nominee.getAge());
//					payoutNomineeDtlRestApiRequest.setNomineeCity(null);
//					payoutNomineeDtlRestApiRequest.setNomineeCode(nominee.getNomineeCode());
//					payoutNomineeDtlRestApiRequest.setNomineeCountry(nominee.getCountry());
//					payoutNomineeDtlRestApiRequest.setNomineeCountryCode(null);
//					payoutNomineeDtlRestApiRequest.setNomineeDistrict(nominee.getDistrict());
//					payoutNomineeDtlRestApiRequest.setNomineeFirstName(nominee.getFirstName());
//					payoutNomineeDtlRestApiRequest.setNomineeId(nominee.getNomineeId());
//					payoutNomineeDtlRestApiRequest.setNomineeLastName(nominee.getLastName());
//					payoutNomineeDtlRestApiRequest.setNomineeMiddleName(nominee.getMiddleName());
//					payoutNomineeDtlRestApiRequest.setNomineeName(nominee.getFirstName());
//					payoutNomineeDtlRestApiRequest.setNomineePinCode(nominee.getPincode().intValue());
//					payoutNomineeDtlRestApiRequest.setNomineeState(nominee.getState());
//					payoutNomineeDtlRestApiRequest.setPan(null);
//					payoutNomineeDtlRestApiRequest
//							.setRelationship(NumericUtils.convertLongToString(nominee.getRelationShip()));
//					payoutNomineeDtlRestApiRequest
//							.setRelationShipWithNominee(NumericUtils.convertLongToString(nominee.getRelationShip()));
//					payoutNomineeDtlRestApiRequest
//							.setStatus(NumericUtils.convertLongToString(nominee.getMaritalStatus()));
//					payoutNomineeDtlRestApiRequest.setStdCode(null);
//					payoutNomineeDtlRestApiRequestList.add(payoutNomineeDtlRestApiRequest);
//				});
//				return payoutNomineeDtlRestApiRequestList;
//			}
//		}
//		return Collections.emptyList();
//	}

	private PayoutMbrPrslDtRestApiRequest anPayoutMbrPrslDtRestApiRequest(PayoutEntity payoutEntity) {
		PayoutMbrPrslDtRestApiRequest payoutMbrPrslDtRestApiRequest = new PayoutMbrPrslDtRestApiRequest();

		if (payoutEntity != null) {
			PayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
			if (payoutMbr != null) {
				payoutMbrPrslDtRestApiRequest
						.setAddharNumber(NumericUtils.convertLongToString(payoutMbr.getAadharNumber()));
				payoutMbrPrslDtRestApiRequest.setCommunicationPreference(payoutMbr.getCommunicationPreference());
				payoutMbrPrslDtRestApiRequest.setDateOfJoiningScheme(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutMbr.getDateOfJoining()));
				payoutMbrPrslDtRestApiRequest.setEmailId(payoutMbr.getEmailId());
				payoutMbrPrslDtRestApiRequest.setFatherName(payoutMbr.getFatherName());
				payoutMbrPrslDtRestApiRequest.setFirstName(payoutMbr.getFirstName());
				payoutMbrPrslDtRestApiRequest.setGender(payoutMbr.getGender());
				payoutMbrPrslDtRestApiRequest.setLanguagePreference(payoutMbr.getLanguagePreference());
				payoutMbrPrslDtRestApiRequest.setLastEcSubmissionDate(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutMbr.getDateOfRetirement()));
				payoutMbrPrslDtRestApiRequest.setLastName(payoutMbr.getLastName());
				payoutMbrPrslDtRestApiRequest.setMiddleName(payoutMbr.getMiddleName());
				payoutMbrPrslDtRestApiRequest.setMobileNo(null);
				payoutMbrPrslDtRestApiRequest.setPan(payoutMbr.getPan());
				payoutMbrPrslDtRestApiRequest.setPersonalDateOfBirth(CommonDateUtils.dateToStringFormatYyyyMmDdSlash(payoutMbr.getDateOfBirth()));
				payoutMbrPrslDtRestApiRequest.setSpouseName(payoutMbr.getSpouseName());
			}
			payoutMbrPrslDtRestApiRequest.setUnit(payoutEntity.getUnitCode());
			payoutMbrPrslDtRestApiRequest.setVariant(payoutEntity.getVariant());
			payoutMbrPrslDtRestApiRequest.setProduct(payoutEntity.getProduct());
		}
		return payoutMbrPrslDtRestApiRequest;
	}
	private void insertPayoutPayeeBankDetails(List<PayoutPayeeBankDetailsTempEntity> result,
			PayoutMbrEntity payoutMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<PayoutPayeeBankDetailsEntity> docs = result.stream()
					.map(x -> convertToPayoutPayeeBankDetails(x, payoutMbrEntity)).collect(Collectors.toList());
			payoutPayeeBankDetailsRepository.saveAll(docs);
		}

	}

	private PayoutPayeeBankDetailsEntity convertToPayoutPayeeBankDetails(PayoutPayeeBankDetailsTempEntity tempEntity,
			PayoutMbrEntity payoutMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<PayoutPayeeBankDetailsTempEntity, PayoutPayeeBankDetailsEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		PayoutPayeeBankDetailsEntity entity = modelMapper.map(tempEntity, PayoutPayeeBankDetailsEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setBankAccountId(null);
		entity.setTempBankAccountId(NumericUtils.convertLongToInteger(tempEntity.getBankAccountId()));
		entity.setVersionNo("1");
		return entity;
	}

	private void insertPayoutMbrFundValue(List<TempPayoutMbrFundValueEntity> result, PayoutMbrEntity payoutMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<PayoutMbrFundValueEntity> docs = result.stream()
					.map(x -> convertToPayoutMbrFundValue(x, payoutMbrEntity)).collect(Collectors.toList());
			payoutMbrFundValueRepository.saveAll(docs);
		}

	}

	private PayoutMbrFundValueEntity convertToPayoutMbrFundValue(TempPayoutMbrFundValueEntity tempEntity,
			PayoutMbrEntity payoutMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutMbrFundValueEntity, PayoutMbrFundValueEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		PayoutMbrFundValueEntity entity = modelMapper.map(tempEntity, PayoutMbrFundValueEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setFundValueId(null);
		return entity;
	}

	private void insertPayoutFundValue(List<TempPayoutFundValueEntity> result, PayoutMbrEntity payoutMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<PayoutFundValueEntity> docs = result.stream().map(x -> convertToPayoutFundValue(x, payoutMbrEntity))
					.collect(Collectors.toList());
			payoutFundValueRepository.saveAll(docs);
		}

	}

	private PayoutFundValueEntity convertToPayoutFundValue(TempPayoutFundValueEntity tempEntity,
			PayoutMbrEntity payoutMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutFundValueEntity, PayoutFundValueEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		PayoutFundValueEntity entity = modelMapper.map(tempEntity, PayoutFundValueEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setFundValueId(null);
		return entity;
	}

	private void insertPayoutMbrCommutation(List<TempPayoutCommutationCalcEntity> result,
			PayoutMbrEntity payoutMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<PayoutCommutationCalcEntity> docs = result.stream().map(x -> convertToCommutation(x, payoutMbrEntity))
					.collect(Collectors.toList());
			payoutCommutationCalcRepository.saveAll(docs);
		}

	}

	private PayoutCommutationCalcEntity convertToCommutation(TempPayoutCommutationCalcEntity tempEntity,
			PayoutMbrEntity payoutMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutCommutationCalcEntity, PayoutCommutationCalcEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		PayoutCommutationCalcEntity entity = modelMapper.map(tempEntity, PayoutCommutationCalcEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setCommunityId(null);
		return entity;
	}

	private void insertPayoutMbrAnnuity(List<TempPayoutAnnuityCalcEntity> result, PayoutMbrEntity payoutMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<PayoutAnnuityCalcEntity> docs = result.stream().map(x -> convertToAnnuity(x, payoutMbrEntity))
					.collect(Collectors.toList());
			payoutAnnuityCalcRepository.saveAll(docs);
		}

	}

	private PayoutAnnuityCalcEntity convertToAnnuity(TempPayoutAnnuityCalcEntity tempEntity,
			PayoutMbrEntity payoutMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutAnnuityCalcEntity, PayoutAnnuityCalcEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		PayoutAnnuityCalcEntity entity = modelMapper.map(tempEntity, PayoutAnnuityCalcEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setAnnuityId(null);
		return entity;
	}

	private void insertPayoutMbrNominee(List<TempPayoutMbrNomineeEntity> result, PayoutMbrEntity payoutMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<PayoutMbrNomineeEntity> docs = result.stream().map(x -> convertToNominee(x, payoutMbrEntity))
					.collect(Collectors.toList());
			payoutMbrNomineeRepository.saveAll(docs);
		}

	}

	private PayoutMbrNomineeEntity convertToNominee(TempPayoutMbrNomineeEntity tempEntity,
			PayoutMbrEntity payoutMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutMbrNomineeEntity, PayoutMbrNomineeEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		PayoutMbrNomineeEntity entity = modelMapper.map(tempEntity, PayoutMbrNomineeEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setNomineeId(null);
		return entity;
	}

	private void insertPayoutMbrBank(List<TempPayoutMbrBankDetailEntity> result, PayoutMbrEntity payoutMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<PayoutMbrBankDetailEntity> docs = result.stream().map(x -> convertToBank(x, payoutMbrEntity))
					.collect(Collectors.toList());
			payoutMbrBankDtlsRepository.saveAll(docs);
		}

	}

	private PayoutMbrBankDetailEntity convertToBank(TempPayoutMbrBankDetailEntity tempEntity,
			PayoutMbrEntity payoutMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutMbrBankDetailEntity, PayoutMbrBankDetailEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		PayoutMbrBankDetailEntity entity = modelMapper.map(tempEntity, PayoutMbrBankDetailEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setBankId(null);
		return entity;
	}

	private void insertPayoutMbrAppointee(List<TempPayoutMbrAppointeeEntity> result, PayoutMbrEntity payoutMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<PayoutMbrAppointeeEntity> docs = result.stream().map(x -> convertToAppointee(x, payoutMbrEntity))
					.collect(Collectors.toList());
			payoutMbrAppointeeRepository.saveAll(docs);
		}
	}

	private PayoutMbrAppointeeEntity convertToAppointee(TempPayoutMbrAppointeeEntity tempEntity,
			PayoutMbrEntity payoutMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutMbrAppointeeEntity, PayoutMbrAddressEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		PayoutMbrAppointeeEntity entity = modelMapper.map(tempEntity, PayoutMbrAppointeeEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setAppointeeId(null);
		return entity;
	}

	private void insertPayoutMbrAddress(List<TempPayoutMbrAddressEntity> result, PayoutMbrEntity payoutMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<PayoutMbrAddressEntity> docs = result.stream().map(x -> convertToAddress(x, payoutMbrEntity))
					.collect(Collectors.toList());
			payoutMbrAddressRepository.saveAll(docs);
		}
	}

	private PayoutMbrAddressEntity convertToAddress(TempPayoutMbrAddressEntity tempEntity,
			PayoutMbrEntity payoutMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutMbrAddressEntity, PayoutMbrAddressEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutMbrEntity());
			}
		});
		PayoutMbrAddressEntity entity = modelMapper.map(tempEntity, PayoutMbrAddressEntity.class);
		entity.setPayoutMbrEntity(payoutMbrEntity);
		entity.setAddressId(null);
		return entity;
	}

	private PayoutMbrEntity insertPayoutMbr(TempPayoutMbrEntity tempPayoutMbrEntity, PayoutEntity payoutEntity) {
		logger.info("insertPayoutMbr -------------Start");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutMbrEntity, PayoutMbrEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayout());
				skip(destination.getPayoutAnuityCalc());
				skip(destination.getPayoutCommutationCalc());
				skip(destination.getPayoutMbrAppointeeDtls());
				skip(destination.getPayoutMbrNomineeDtls());
				skip(destination.getPayoutMbrAddresses());
				skip(destination.getPayoutMbrBankDetails());
				skip(destination.getPayoutFundValue());
				skip(destination.getPayoutMbrFundValue());
				skip(destination.getPayoutPayeeBank());

			}
		});
		PayoutMbrEntity entity = modelMapper.map(tempPayoutMbrEntity, PayoutMbrEntity.class);
		entity.setPayout(payoutEntity);
		entity.setMemberId(null);
		entity = payoutMbrRepository.save(entity);
		logger.info("insertPayoutMbr -------------end");
		return entity;
	}

	private void insertPayoutDocuments(TempPayoutEntity tempPayoutEntity, PayoutEntity payoutEntity) {
		List<TempPayoutDocumentDetail> result = tempPayoutDocumentDetailRepository
				.findByPayoutAndIsDeleted(tempPayoutEntity, Boolean.FALSE);

		if (result != null && !result.isEmpty()) {
			List<PayoutDocumentDetail> docs = result.stream().map(x -> convertToDoc(x, payoutEntity))
					.collect(Collectors.toList());
			payoutDocumentDetailRepository.saveAll(docs);
		}
	}

	private PayoutDocumentDetail convertToDoc(TempPayoutDocumentDetail tempPayoutDocumentDetail,
			PayoutEntity payoutEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutDocumentDetail, PayoutDocumentDetail>() {
			@Override
			protected void configure() {
				skip(destination.getPayout());
			}
		});
		PayoutDocumentDetail entity = modelMapper.map(tempPayoutDocumentDetail, PayoutDocumentDetail.class);
		entity.setPayout(payoutEntity);
		entity.setDocumentId(null);
		return entity;
	}

	private void insertPayoutNotes(TempPayoutEntity tempPayoutEntity, PayoutEntity payoutEntity) {
		List<TempPayoutNotesEntity> result = tempPayoutNotesRepository.findByPayout(tempPayoutEntity);
		if (result != null && !result.isEmpty()) {
			List<PayoutNotesEntity> notes = result.stream().map(x -> convertToNotes(x, payoutEntity))
					.collect(Collectors.toList());
			payoutNotesRepository.saveAll(notes);
		}
	}

	private PayoutNotesEntity convertToNotes(TempPayoutNotesEntity temppayoutnotesentity, PayoutEntity payoutEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutNotesEntity, PayoutNotesEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayout());
			}
		});
		PayoutNotesEntity entity = modelMapper.map(temppayoutnotesentity, PayoutNotesEntity.class);
		entity.setId(null);
		entity.setPayout(payoutEntity);
		return entity;
	}

	private PayoutEntity insertPayoutEntity(TempPayoutEntity tempPayoutEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempPayoutEntity, PayoutEntity>() {
			@Override
			protected void configure() {
				skip(destination.getPayoutDocDetails());
				skip(destination.getPayoutMbr());
				skip(destination.getPayoutNotes());
			}
		});
		PayoutEntity entity = modelMapper.map(tempPayoutEntity, PayoutEntity.class);
		entity.setPayoutId(null);
		entity = payoutRepository.save(entity);
		return entity;
	}

}
