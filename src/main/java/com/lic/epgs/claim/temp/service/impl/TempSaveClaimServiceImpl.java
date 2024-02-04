package com.lic.epgs.claim.temp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimConstants;
import com.lic.epgs.claim.constants.ClaimStatus;
import com.lic.epgs.claim.dto.ClaimAnnuityCalcDto;
import com.lic.epgs.claim.temp.entity.ClaimPayeeTempBankDetailsEntity;
import com.lic.epgs.claim.temp.entity.TempClaimAnnuityCalcEntity;
import com.lic.epgs.claim.temp.entity.TempClaimCommutationCalcEntity;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.entity.TempClaimFundValueEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrAddressEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrAppointeeEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrBankDetailEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrFundValueEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrNomineeEntity;
import com.lic.epgs.claim.temp.repository.TempClaimAnnuityCalcRepository;
import com.lic.epgs.claim.temp.repository.TempClaimCommutationCalcRepository;
import com.lic.epgs.claim.temp.repository.TempClaimFundValueRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrAddressRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrAppointeeRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrBankDtlsRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrFundValueRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrNomineeRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrRepository;
import com.lic.epgs.claim.temp.repository.TempClaimOnboardingRepository;
import com.lic.epgs.claim.temp.repository.TempClaimPayeeBankDetailsRepository;
import com.lic.epgs.claim.temp.repository.TempClaimRepository;
import com.lic.epgs.claim.temp.service.TempSaveClaimService;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.utils.NumericUtils;

@Service
@Transactional
public class TempSaveClaimServiceImpl implements TempSaveClaimService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	TempClaimRepository tempClaimRepository;

	@Autowired
	TempClaimMbrRepository tempClaimDtlsRepository;

	@Autowired
	TempClaimOnboardingRepository tempClaimOnboardingRepository;

	@Autowired
	TempClaimMbrNomineeRepository tempClaimMbrNomineeRepository;

	@Autowired
	TempClaimMbrBankDtlsRepository tempClaimMbrBankDtlsRepository;

	@Autowired
	TempClaimMbrAddressRepository tempClaimMbrAddressRepository;

	@Autowired
	TempClaimMbrAppointeeRepository tempClaimMbrAppointeeRepository;

	@Autowired
	TempClaimAnnuityCalcRepository tempClaimAnnuityCalcRepository;

	@Autowired
	TempClaimCommutationCalcRepository tempClaimCommutationCalcRepository;
	
	@Autowired
	TempClaimMbrFundValueRepository tempClaimMbrFundValueRepository;
	
	@Autowired
	TempClaimFundValueRepository tempClaimFundValueRepository;
	
	@Autowired
	TempClaimPayeeBankDetailsRepository tempClaimPayeeBankDetailsRepository;
	
	@Autowired
	PolicyMasterRepository policyMasterRepository;
	
	@Autowired
	MemberMasterRepository memberMasterRepository;
	
//	@Autowired
//	PolicyRulesRepository policyRulesRepository;

	private TempClaimEntity makeDublicateClaimEntity(TempClaimEntity tempClaimEntity) {
		TempClaimEntity newClaimEntity = new TempClaimEntity();
		BeanUtils.copyProperties(tempClaimEntity, newClaimEntity);
		newClaimEntity.setClaimId(null);
		newClaimEntity.setClaimMbr(null);
		newClaimEntity.setIsActive(true);
		return newClaimEntity;
	}

	private TempClaimMbrEntity makeDublicateClaimMbrDetails(TempClaimMbrEntity tempClaimMbrEntity,TempClaimEntity newClaimEntity) {

		TempClaimMbrEntity newClaimMbrEntity = new TempClaimMbrEntity();
		BeanUtils.copyProperties(tempClaimMbrEntity, newClaimMbrEntity);
		newClaimMbrEntity.setClaim(newClaimEntity);
		newClaimMbrEntity.setClaimAnuityCalc(null);
		newClaimMbrEntity.setClaimCommutationCalc(null);
		newClaimMbrEntity.setClaimMbrAppointeeDtls(null);
		newClaimMbrEntity.setClaimMbrNomineeDtls(null);
		newClaimMbrEntity.setClaimMbrAddresses(null);
		newClaimMbrEntity.setClaimMbrBankDetails(null);
		newClaimMbrEntity.setMemberId(null);
		newClaimMbrEntity.setClaimFundValue(null);
		newClaimMbrEntity.setClaimMbrFundValue(null);
		newClaimMbrEntity.setClaimPayeeTempBank(null);
		return newClaimMbrEntity;

	}

	@Override
	public TempClaimAnnuityCalcEntity insertClaimAnnuityCalc(String claimNo,
			TempClaimAnnuityCalcEntity tempClaimAnnuityCalcEntity,ClaimAnnuityCalcDto request) {
		try {
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimAnnuityCalc {}:: start");
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
					newClaimEntity = tempClaimRepository.save(newClaimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
								oldTempClaimEntity.getClaimMbr(), newClaimEntity);
						newClaimMbrEntity.setPan(request.getPan());
						newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);

						tempClaimAnnuityCalcEntity = insertClaimAnuityCalc(oldClaimMbrEntity, tempClaimAnnuityCalcEntity,
								newClaimMbrEntity);

						insertClaimCommutationCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimAppointeeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrAddressEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrBankEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrBankPayeeEntity(oldClaimMbrEntity, newClaimMbrEntity, null);

					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					tempClaimRepository.save(oldTempClaimEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Exception-- TempSaveClaimServiceImpl{}::--{}:: insertClaimAnnuityCalc{}:: --", e.getMessage());
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimAnnuityCalc {}:: ended");
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimAnnuityCalcEntity insertClaimAnuityCalc(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimAnnuityCalcEntity tempClaimAnnuityCalcEntity, TempClaimMbrEntity newClaimMbrEntity) {
		if (tempClaimAnnuityCalcEntity != null && tempClaimAnnuityCalcEntity.getAnnuityId() != null
				&& tempClaimAnnuityCalcEntity.getAnnuityId() > 0) {
			tempClaimAnnuityCalcEntity = updateClaimAnuityCalc(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimAnnuityCalcEntity);
		} else {
			tempClaimAnnuityCalcEntity = insertClaimAnuityCalc(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimAnnuityCalcEntity);
		}
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimAnnuityCalcEntity updateClaimAnuityCalc(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimAnnuityCalcEntity tempClaimAnnuityCalcEntity) {
		List<TempClaimAnnuityCalcEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimAnuityCalc();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimAnnuityCalcEntity oldEntity : tempClaimAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getAnnuityId().equals(tempClaimAnnuityCalcEntity.getAnnuityId())) {
					TempClaimAnnuityCalcEntity newEntity = makeDuplicateClaimAnunityEntity(tempClaimAnnuityCalcEntity,
							newClaimMbrEntity);
					tempClaimAnnuityCalcEntity = tempClaimAnnuityCalcRepository.save(newEntity);
				} else {
					TempClaimAnnuityCalcEntity newEntity = makeDuplicateClaimAnunityEntity(oldEntity,
							newClaimMbrEntity);
					tempClaimAnnuityCalcRepository.save(newEntity);
				}
			}
		}
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimAnnuityCalcEntity insertClaimAnuityCalc(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimAnnuityCalcEntity tempClaimAnnuityCalcEntity) {
		List<TempClaimAnnuityCalcEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimAnuityCalc();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimAnnuityCalcEntity oldEntity : tempClaimAnnuityCalcEntities) {
				TempClaimAnnuityCalcEntity newEntity = makeDuplicateClaimAnunityEntity(oldEntity, newClaimMbrEntity);
				tempClaimAnnuityCalcRepository.save(newEntity);
			}
		}
		if (tempClaimAnnuityCalcEntity != null) {
			TempClaimAnnuityCalcEntity newEntity = makeDuplicateClaimAnunityEntity(tempClaimAnnuityCalcEntity,
					newClaimMbrEntity);
			tempClaimAnnuityCalcEntity = tempClaimAnnuityCalcRepository.save(newEntity);
		}
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimAnnuityCalcEntity makeDuplicateClaimAnunityEntity(TempClaimAnnuityCalcEntity oldEntity,
			TempClaimMbrEntity newClaimMbrEntity) {
		TempClaimAnnuityCalcEntity newEntity = new TempClaimAnnuityCalcEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setAnnuityId(null);
		newEntity.setDateOfBirth(oldEntity.getDateOfBirth());
		newEntity.setCaptureCertainNumber(oldEntity.getCaptureCertainNumber());
		newEntity.setClaimMbrEntity(newClaimMbrEntity);
		return newEntity;
	}

	@Override
	public TempClaimCommutationCalcEntity insertClaimCommutationCalc(String claimNo,
			TempClaimCommutationCalcEntity tempClaimCommutationCalcEntity) {
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimCommutationCalc {}:: start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
					newClaimEntity = tempClaimRepository.save(newClaimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
								oldTempClaimEntity.getClaimMbr(), newClaimEntity);

						newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);

						insertClaimAnuityCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						tempClaimCommutationCalcEntity = insertClaimCommutationCalc(oldClaimMbrEntity,
								tempClaimCommutationCalcEntity, newClaimMbrEntity);

						insertClaimAppointeeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrAddressEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrBankEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrBankPayeeEntity(oldClaimMbrEntity, newClaimMbrEntity, null);
						

					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					tempClaimRepository.save(oldTempClaimEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimCommutationCalc {}:: error is "+e.getMessage());
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimCommutationCalc {}:: ended");
		return tempClaimCommutationCalcEntity;
	}

	private TempClaimCommutationCalcEntity insertClaimCommutationCalc(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimCommutationCalcEntity tempClaimCommutationCalcEntity, TempClaimMbrEntity newClaimMbrEntity) {
		if (tempClaimCommutationCalcEntity != null && tempClaimCommutationCalcEntity.getCommunityId() != null
				&& tempClaimCommutationCalcEntity.getCommunityId() > 0) {

			tempClaimCommutationCalcEntity = updateClaimCommutationCalc(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimCommutationCalcEntity);

		} else {
			tempClaimCommutationCalcEntity = insertClaimCommutationCalc(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimCommutationCalcEntity);
		}
		return tempClaimCommutationCalcEntity;
	}

	private TempClaimCommutationCalcEntity updateClaimCommutationCalc(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimCommutationCalcEntity tempClaimAnnuityCalcEntity) {
		List<TempClaimCommutationCalcEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimCommutationCalc();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimCommutationCalcEntity oldEntity : tempClaimAnnuityCalcEntities) {
				if (oldEntity != null
						&& oldEntity.getCommunityId().equals(tempClaimAnnuityCalcEntity.getCommunityId())) {
					TempClaimCommutationCalcEntity newEntity = makeDuplicateClaimAnunityEntity(
							tempClaimAnnuityCalcEntity, newClaimMbrEntity);
					tempClaimAnnuityCalcEntity = tempClaimCommutationCalcRepository.save(newEntity);
				} else {
					TempClaimCommutationCalcEntity newEntity = makeDuplicateClaimAnunityEntity(oldEntity,
							newClaimMbrEntity);
					tempClaimCommutationCalcRepository.save(newEntity);
				}
			}
		}
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimCommutationCalcEntity insertClaimCommutationCalc(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimCommutationCalcEntity tempClaimAnnuityCalcEntity) {
		List<TempClaimCommutationCalcEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimCommutationCalc();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimCommutationCalcEntity oldEntity : tempClaimAnnuityCalcEntities) {
				TempClaimCommutationCalcEntity newEntity = makeDuplicateClaimAnunityEntity(oldEntity,
						newClaimMbrEntity);
				tempClaimCommutationCalcRepository.save(newEntity);
			}
		}
		if (tempClaimAnnuityCalcEntity != null) {
			TempClaimCommutationCalcEntity newEntity = makeDuplicateClaimAnunityEntity(tempClaimAnnuityCalcEntity,
					newClaimMbrEntity);
			tempClaimAnnuityCalcEntity = tempClaimCommutationCalcRepository.save(newEntity);
		}
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimCommutationCalcEntity makeDuplicateClaimAnunityEntity(TempClaimCommutationCalcEntity oldEntity,
			TempClaimMbrEntity newClaimMbrEntity) {
		TempClaimCommutationCalcEntity newEntity = new TempClaimCommutationCalcEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setCommunityId(null);
		newEntity.setClaimMbrEntity(newClaimMbrEntity);
		return newEntity;
	}

	@Override
	public TempClaimMbrEntity insertClaimMbr(String claimNo, TempClaimMbrEntity tempClaimMbrEntity) {

		Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
		if (optional.isPresent()) {
			TempClaimEntity oldTempClaimEntity = optional.get();
			if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
				TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
				newClaimEntity = tempClaimRepository.save(newClaimEntity);
				if (oldTempClaimEntity.getClaimMbr() != null) {
					TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

					tempClaimMbrEntity = makeDublicateClaimMbrDetails(tempClaimMbrEntity, newClaimEntity);

					tempClaimMbrEntity = tempClaimDtlsRepository.save(tempClaimMbrEntity);

					insertClaimAnuityCalc(oldClaimMbrEntity, null, tempClaimMbrEntity);

					insertClaimCommutationCalc(oldClaimMbrEntity, null, tempClaimMbrEntity);

					insertClaimAppointeeEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);

					insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);

					insertClaimMbrAddressEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);

					insertClaimMbrBankEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);
					
					insertClaimFundValueEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);
					
					insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);

				}

				oldTempClaimEntity.setIsActive(Boolean.FALSE);
				tempClaimRepository.save(oldTempClaimEntity);
			}

		}

		return tempClaimMbrEntity;

	}

	@Override
	public TempClaimEntity insertClaim(String claimNo, TempClaimEntity claimEntity) {
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaim {}:: start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					claimEntity = makeDublicateClaimEntity(claimEntity);
					claimEntity = tempClaimRepository.save(claimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity tempClaimMbrEntity = makeDublicateClaimMbrDetails(oldClaimMbrEntity,
								claimEntity);

						tempClaimMbrEntity = tempClaimDtlsRepository.save(tempClaimMbrEntity);

						insertClaimAnuityCalc(oldClaimMbrEntity, null, tempClaimMbrEntity);

						insertClaimCommutationCalc(oldClaimMbrEntity, null, tempClaimMbrEntity);

						insertClaimAppointeeEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);

						insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);

						insertClaimMbrAddressEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);

						insertClaimMbrBankEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);
						
						insertClaimFundValueEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);
						
						insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, tempClaimMbrEntity);
						
						insertClaimMbrPayeeTempBankDetailsEntity(oldClaimMbrEntity,null, tempClaimMbrEntity );

					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					tempClaimRepository.save(oldTempClaimEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaim {}:: error is"+e.getMessage());
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaim {}:: ended");
		return claimEntity;
	}

	@Override
	public TempClaimMbrAppointeeEntity insertClaimMbrAppointee(String claimNo,
			TempClaimMbrAppointeeEntity claimAppointeeEntity) {
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrAppointee {}:: start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
					newClaimEntity = tempClaimRepository.save(newClaimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
								oldTempClaimEntity.getClaimMbr(), newClaimEntity);

						newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);

						insertClaimAnuityCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimCommutationCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						claimAppointeeEntity = insertClaimAppointeeEntity(oldClaimMbrEntity, claimAppointeeEntity,
								newClaimMbrEntity);

						insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrAddressEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrBankEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					tempClaimRepository.save(oldTempClaimEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrAppointee {}:: error is "+e.getMessage());
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrAppointee {}:: ended");
		return claimAppointeeEntity;
	}

	private TempClaimMbrAppointeeEntity insertClaimAppointeeEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrAppointeeEntity tempClaimMbrAppointeeEntity, TempClaimMbrEntity newClaimMbrEntity) {
		if (tempClaimMbrAppointeeEntity != null && tempClaimMbrAppointeeEntity.getAppointeeId() != null
				&& tempClaimMbrAppointeeEntity.getAppointeeId() > 0) {
			tempClaimMbrAppointeeEntity = updateClaimMbrAppointee(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimMbrAppointeeEntity);

		} else {
			tempClaimMbrAppointeeEntity = insertClaimMbrAppointee(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimMbrAppointeeEntity);
		}
		return tempClaimMbrAppointeeEntity;
	}

	private TempClaimMbrAppointeeEntity updateClaimMbrAppointee(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimMbrAppointeeEntity tempClaimAnnuityCalcEntity) {
		List<TempClaimMbrAppointeeEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrAppointeeDtls();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimMbrAppointeeEntity oldEntity : tempClaimAnnuityCalcEntities) {
				if (oldEntity != null
						&& oldEntity.getAppointeeId().equals(tempClaimAnnuityCalcEntity.getAppointeeId())) {
					TempClaimMbrAppointeeEntity newEntity = makeDuplicateClaimMbrAppointeeEntity(
							tempClaimAnnuityCalcEntity, newClaimMbrEntity);
					tempClaimAnnuityCalcEntity = tempClaimMbrAppointeeRepository.save(newEntity);
				} else {
					TempClaimMbrAppointeeEntity newEntity = makeDuplicateClaimMbrAppointeeEntity(oldEntity,
							newClaimMbrEntity);
					tempClaimMbrAppointeeRepository.save(newEntity);
				}
			}
		}
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimMbrAppointeeEntity insertClaimMbrAppointee(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimMbrAppointeeEntity tempClaimMbrAppointeeEntity) {
		List<TempClaimMbrAppointeeEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrAppointeeDtls();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimMbrAppointeeEntity oldEntity : tempClaimAnnuityCalcEntities) {
				TempClaimMbrAppointeeEntity newEntity = makeDuplicateClaimMbrAppointeeEntity(oldEntity,
						newClaimMbrEntity);
				tempClaimMbrAppointeeRepository.save(newEntity);
			}
		}
		if (tempClaimMbrAppointeeEntity != null) {
			TempClaimMbrAppointeeEntity newEntity = makeDuplicateClaimMbrAppointeeEntity(tempClaimMbrAppointeeEntity,
					newClaimMbrEntity);
			tempClaimMbrAppointeeEntity = tempClaimMbrAppointeeRepository.save(newEntity);
		}
		return tempClaimMbrAppointeeEntity;
	}

	private TempClaimMbrAppointeeEntity makeDuplicateClaimMbrAppointeeEntity(TempClaimMbrAppointeeEntity oldEntity,
			TempClaimMbrEntity newClaimMbrEntity) {
		TempClaimMbrAppointeeEntity newEntity = new TempClaimMbrAppointeeEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setAppointeeId(null);
		newEntity.setClaimMbrEntity(newClaimMbrEntity);
		return newEntity;
	}

	@Override
	public TempClaimMbrNomineeEntity insertClaimMbrNominee(String claimNo,
			TempClaimMbrNomineeEntity claimMbrNomineeEntity) {
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrNominee {}:: start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
					newClaimEntity = tempClaimRepository.save(newClaimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(oldTempClaimEntity.getClaimMbr(), newClaimEntity);
						newClaimMbrEntity.setMemberStatus(ClaimConstants.MEMBER_INTIMATION);
						newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);

						insertClaimAnuityCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimCommutationCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimAppointeeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						claimMbrNomineeEntity = insertClaimMbrNomineeEntity(oldClaimMbrEntity, claimMbrNomineeEntity,
								newClaimMbrEntity);

						insertClaimMbrAddressEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrBankEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrPayeeTempBankDetailsEntity(oldClaimMbrEntity,null,newClaimMbrEntity);
						
						memberStatusUpdated(newClaimEntity,newClaimMbrEntity);
					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					
					tempClaimRepository.save(oldTempClaimEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrNominee {}:: eror is "+e.getLocalizedMessage());
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrNominee {}:: ended");
		return claimMbrNomineeEntity;
	}
	
	/****** @PolicyMember Updated *****/
	private void memberStatusUpdated(TempClaimEntity newClaimEntity,TempClaimMbrEntity newClaimMbrEntity) {
//		PolicyMasterEntity policyMasterEntity = policyMasterRepository
//				.findByPolicyNumberAndIsActiveTrue(newClaimEntity.getMasterPolicyNo());
//		if(policyMasterEntity!=null) {
//		MemberMasterEntity masterEntity=memberMasterRepository.findByLicIdAndPolicyIdAndIsZeroidFalse(newClaimMbrEntity.getLicId(), policyMasterEntity.getPolicyId());
//		masterEntity.setMemberStatus(ClaimConstants.MEMBER_INTIMATION);
//		memberMasterRepository.save(masterEntity);
		memberMasterRepository.updateMemberStatusByMemberId(ClaimConstants.MEMBER_INTIMATION, newClaimMbrEntity.getPolicyMemberId());
		}
//	}

	private TempClaimMbrNomineeEntity insertClaimMbrNomineeEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrNomineeEntity tempClaimMbrNomineeEntity, TempClaimMbrEntity newClaimMbrEntity) {
		if (tempClaimMbrNomineeEntity != null && tempClaimMbrNomineeEntity.getNomineeId() != null
				&& tempClaimMbrNomineeEntity.getNomineeId() > 0) {

			tempClaimMbrNomineeEntity = updateClaimMbrNominee(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimMbrNomineeEntity);

		} else {
			tempClaimMbrNomineeEntity = insertClaimMbrNominee(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimMbrNomineeEntity);
		}
		return tempClaimMbrNomineeEntity;
	}

	private TempClaimMbrNomineeEntity updateClaimMbrNominee(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimMbrNomineeEntity tempClaimAnnuityCalcEntity) {
		List<TempClaimMbrNomineeEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrNomineeDtls();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimMbrNomineeEntity oldEntity : tempClaimAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getNomineeId().equals(tempClaimAnnuityCalcEntity.getNomineeId())) {
					TempClaimMbrNomineeEntity newEntity = makeDuplicateClaimMbrNomineeEntity(tempClaimAnnuityCalcEntity,
							newClaimMbrEntity);
					tempClaimAnnuityCalcEntity = tempClaimMbrNomineeRepository.save(newEntity);
				} else {
					TempClaimMbrNomineeEntity newEntity = makeDuplicateClaimMbrNomineeEntity(oldEntity,
							newClaimMbrEntity);
					tempClaimMbrNomineeRepository.save(newEntity);
				}
			}
		}
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimMbrNomineeEntity insertClaimMbrNominee(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimMbrNomineeEntity tempClaimMbrNomineeEntity) {
		List<TempClaimMbrNomineeEntity> newNominee=new ArrayList<>();
		List<TempClaimMbrNomineeEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrNomineeDtls();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimMbrNomineeEntity oldEntity : tempClaimAnnuityCalcEntities) {
				TempClaimMbrNomineeEntity newEntity = makeDuplicateClaimMbrNomineeEntity(oldEntity, newClaimMbrEntity);
				tempClaimMbrNomineeRepository.save(newEntity);
				newNominee.add(newEntity);
				newClaimMbrEntity.setClaimMbrNomineeDtls(newNominee);
			}
		}
		if (tempClaimMbrNomineeEntity != null) {
			TempClaimMbrNomineeEntity newEntity = makeDuplicateClaimMbrNomineeEntity(tempClaimMbrNomineeEntity,
					newClaimMbrEntity);
			tempClaimMbrNomineeEntity = tempClaimMbrNomineeRepository.save(newEntity);
		}
		return tempClaimMbrNomineeEntity;
	}

	private TempClaimMbrNomineeEntity makeDuplicateClaimMbrNomineeEntity(TempClaimMbrNomineeEntity oldEntity,
			TempClaimMbrEntity newClaimMbrEntity) {
		TempClaimMbrNomineeEntity newEntity = new TempClaimMbrNomineeEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setNomineeId(null);
		newEntity.setClaimMbrEntity(newClaimMbrEntity);
		return newEntity;
	}

	@Override
	public TempClaimMbrAddressEntity insertClaimMbrAddressEntity(String claimNo,
			TempClaimMbrAddressEntity tempClaimMbrAddressEntity) {
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrAddressEntity {}:: start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
					newClaimEntity = tempClaimRepository.save(newClaimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
								oldTempClaimEntity.getClaimMbr(), newClaimEntity);

						newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);

						insertClaimAnuityCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimCommutationCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimAppointeeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						tempClaimMbrAddressEntity = insertClaimMbrAddressEntity(oldClaimMbrEntity,
								tempClaimMbrAddressEntity, newClaimMbrEntity);

						insertClaimMbrBankEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					tempClaimRepository.save(oldTempClaimEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrAddressEntity {}:: error is "+e.getMessage());
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrAddressEntity {}:: ended");
		return tempClaimMbrAddressEntity;
	}

	private TempClaimMbrAddressEntity insertClaimMbrAddressEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrAddressEntity tempClaimMbrAddressEntity, TempClaimMbrEntity newClaimMbrEntity) {
		if (tempClaimMbrAddressEntity != null && tempClaimMbrAddressEntity.getAddressId() != null
				&& tempClaimMbrAddressEntity.getAddressId() > 0) {

			tempClaimMbrAddressEntity = updateClaimMbrAddress(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimMbrAddressEntity);

		} else {
			tempClaimMbrAddressEntity = insertClaimMbrAddress(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimMbrAddressEntity);
		}
		return tempClaimMbrAddressEntity;
	}

	private TempClaimMbrAddressEntity updateClaimMbrAddress(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimMbrAddressEntity tempClaimAnnuityCalcEntity) {
		List<TempClaimMbrAddressEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrAddresses();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimMbrAddressEntity oldEntity : tempClaimAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getAddressId().equals(tempClaimAnnuityCalcEntity.getAddressId())) {
					TempClaimMbrAddressEntity newEntity = makeDuplicateClaimMbrAddressEntity(tempClaimAnnuityCalcEntity,
							newClaimMbrEntity);
					tempClaimAnnuityCalcEntity = tempClaimMbrAddressRepository.save(newEntity);
				} else {
					TempClaimMbrAddressEntity newEntity = makeDuplicateClaimMbrAddressEntity(oldEntity,
							newClaimMbrEntity);
					tempClaimMbrAddressRepository.save(newEntity);
				}
			}
		}
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimMbrAddressEntity insertClaimMbrAddress(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimMbrAddressEntity tempClaimMbrAddressEntity) {
		List<TempClaimMbrAddressEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrAddresses();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimMbrAddressEntity oldEntity : tempClaimAnnuityCalcEntities) {
				TempClaimMbrAddressEntity newEntity = makeDuplicateClaimMbrAddressEntity(oldEntity, newClaimMbrEntity);
				tempClaimMbrAddressRepository.save(newEntity);
			}
		}
		if (tempClaimMbrAddressEntity != null) {
			TempClaimMbrAddressEntity newEntity = makeDuplicateClaimMbrAddressEntity(tempClaimMbrAddressEntity,
					newClaimMbrEntity);
			tempClaimMbrAddressEntity = tempClaimMbrAddressRepository.save(newEntity);
		}
		return tempClaimMbrAddressEntity;
	}

	private TempClaimMbrAddressEntity makeDuplicateClaimMbrAddressEntity(TempClaimMbrAddressEntity oldEntity,
			TempClaimMbrEntity newClaimMbrEntity) {
		TempClaimMbrAddressEntity newEntity = new TempClaimMbrAddressEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setAddressId(null);
		newEntity.setClaimMbrEntity(newClaimMbrEntity);
		return newEntity;
	}

	@Override
	public TempClaimMbrBankDetailEntity insertClaimMbrBankEntity(String claimNo,
			TempClaimMbrBankDetailEntity tempClaimMbrBankDetailEntity) {
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrBankEntity {}:: start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
					newClaimEntity = tempClaimRepository.save(newClaimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
								oldTempClaimEntity.getClaimMbr(), newClaimEntity);

						newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);

						insertClaimAnuityCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimCommutationCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimAppointeeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrAddressEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						tempClaimMbrBankDetailEntity = insertClaimMbrBankEntity(oldClaimMbrEntity,
								tempClaimMbrBankDetailEntity, newClaimMbrEntity);

					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					tempClaimRepository.save(oldTempClaimEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrBankEntity {}:: error is "+e.getMessage());
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrBankEntity {}:: ended");
		return tempClaimMbrBankDetailEntity;
	}

	private TempClaimMbrBankDetailEntity insertClaimMbrBankEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrBankDetailEntity tempClaimMbrBankEntity, TempClaimMbrEntity newClaimMbrEntity) {
		if (tempClaimMbrBankEntity != null && tempClaimMbrBankEntity.getBankId() != null
				&& tempClaimMbrBankEntity.getBankId() > 0) {

			tempClaimMbrBankEntity = updateClaimMbrBank(oldClaimMbrEntity, newClaimMbrEntity, tempClaimMbrBankEntity);

		} else {
			tempClaimMbrBankEntity = insertClaimMbrBank(oldClaimMbrEntity, newClaimMbrEntity, tempClaimMbrBankEntity);
		}
		return tempClaimMbrBankEntity;
	}

	private TempClaimMbrBankDetailEntity updateClaimMbrBank(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimMbrBankDetailEntity tempClaimAnnuityCalcEntity) {
		List<TempClaimMbrBankDetailEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrBankDetails();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimMbrBankDetailEntity oldEntity : tempClaimAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getBankId().equals(tempClaimAnnuityCalcEntity.getBankId())) {
					TempClaimMbrBankDetailEntity newEntity = makeDuplicateClaimMbrBankEntity(tempClaimAnnuityCalcEntity,
							newClaimMbrEntity);
					tempClaimAnnuityCalcEntity = tempClaimMbrBankDtlsRepository.save(newEntity);
				} else {
					TempClaimMbrBankDetailEntity newEntity = makeDuplicateClaimMbrBankEntity(oldEntity,
							newClaimMbrEntity);
					tempClaimMbrBankDtlsRepository.save(newEntity);
				}
			}
		}
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimMbrBankDetailEntity insertClaimMbrBank(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimMbrBankDetailEntity tempClaimMbrBankEntity) {
		List<TempClaimMbrBankDetailEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrBankDetails();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimMbrBankDetailEntity oldEntity : tempClaimAnnuityCalcEntities) {
				TempClaimMbrBankDetailEntity newEntity = makeDuplicateClaimMbrBankEntity(oldEntity, newClaimMbrEntity);
				tempClaimMbrBankDtlsRepository.save(newEntity);
			}
		}
		if (tempClaimMbrBankEntity != null) {
			TempClaimMbrBankDetailEntity newEntity = makeDuplicateClaimMbrBankEntity(tempClaimMbrBankEntity,
					newClaimMbrEntity);
			tempClaimMbrBankEntity = tempClaimMbrBankDtlsRepository.save(newEntity);
		}
		return tempClaimMbrBankEntity;
	}

	private TempClaimMbrBankDetailEntity makeDuplicateClaimMbrBankEntity(TempClaimMbrBankDetailEntity oldEntity,
			TempClaimMbrEntity newClaimMbrEntity) {
		TempClaimMbrBankDetailEntity newEntity = new TempClaimMbrBankDetailEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setBankId(null);
		newEntity.setClaimMbrEntity(newClaimMbrEntity);
		return newEntity;
	}

	@Override
	public TempClaimFundValueEntity insertClaimFundValue(String claimNo, TempClaimFundValueEntity entity) {
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimFundValue {}:: start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
					newClaimEntity.setStatus(ClaimStatus.PRELIMINARY_REQUIREMENT_REVIEW.val());
					newClaimEntity = tempClaimRepository.save(newClaimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
								oldTempClaimEntity.getClaimMbr(), newClaimEntity);

						newClaimMbrEntity.setCorpusPercentage(entity.getCorpusPercentage());
						newClaimMbrEntity.setMemberStatus(ClaimConstants.MEMBER_INTIMATION);
						newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);
						
						entity = insertClaimFundValueEntity(oldClaimMbrEntity, entity, newClaimMbrEntity);
					

//					insertClaimAnuityCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

//					insertClaimCommutationCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimAppointeeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrAddressEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrBankEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
//					insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
//					insertClaimMbrBankPayeeEntity(oldClaimMbrEntity, newClaimMbrEntity, null);
						
						
						if(newClaimMbrEntity.getClaimMbrNomineeDtls()!=null && newClaimEntity.getModeOfExit()== ClaimConstants.DEATH) {
						splitTotalFundvalueforNominee(entity,newClaimMbrEntity.getClaimMbrNomineeDtls());
						}
						memberStatusUpdated(newClaimEntity,newClaimMbrEntity);
					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					
					tempClaimRepository.save(oldTempClaimEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimFundValue {}:: error is "+e.getMessage());
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimFundValue {}:: ended");
		return entity;
	}
	
	
	private void splitTotalFundvalueforNominee(TempClaimFundValueEntity tempClaimFundValueEntity,List<TempClaimMbrNomineeEntity> tempClaimMbrNomineeEntityList) {
	if(tempClaimFundValueEntity!=null && !tempClaimMbrNomineeEntityList.isEmpty()&&tempClaimFundValueEntity.getTotalFundValue()>0.0) {
		for(TempClaimMbrNomineeEntity tempClaimMbrNomineeEntity:tempClaimMbrNomineeEntityList) {
			
			if(tempClaimMbrNomineeEntity.getClaimantType().equals("Nominee")&&tempClaimFundValueEntity.getTotalFundValue()>0.0&&tempClaimMbrNomineeEntity.getSharedPercentage()>0.0) {
			Double totalFundValue=(tempClaimFundValueEntity.getTotalFundValue()*tempClaimMbrNomineeEntity.getSharedPercentage())/100;
			tempClaimMbrNomineeEntity.setTotalFundValue(NumericUtils.doubleRoundInMath(totalFundValue,0));
			tempClaimMbrNomineeRepository.save(tempClaimMbrNomineeEntity);
			}
			else {
				continue;
			}
			
		}
	}
	else {
		for(TempClaimMbrNomineeEntity tempClaimMbrNomineeEntity:tempClaimMbrNomineeEntityList) {
			
			if(tempClaimMbrNomineeEntity.getClaimantType().equals("Nominee")&&tempClaimFundValueEntity.getPurchasePrice()>0.0&&tempClaimMbrNomineeEntity.getSharedPercentage()>0.0) {
			Double totalFundValue=(tempClaimFundValueEntity.getPurchasePrice()*tempClaimMbrNomineeEntity.getSharedPercentage())/100;
			tempClaimMbrNomineeEntity.setTotalFundValue(totalFundValue);
			tempClaimMbrNomineeRepository.save(tempClaimMbrNomineeEntity);
			}else if(tempClaimMbrNomineeEntity.getClaimantType().equals("Nominee")&&tempClaimFundValueEntity.getPension()>0.0&&tempClaimMbrNomineeEntity.getSharedPercentage()>0.0){
				Double totalFundValue=(tempClaimFundValueEntity.getPension()*tempClaimMbrNomineeEntity.getSharedPercentage())/100;
				tempClaimMbrNomineeEntity.setTotalFundValue(totalFundValue);
				tempClaimMbrNomineeRepository.save(tempClaimMbrNomineeEntity);
			}
			
			else {
				continue;
			}
			
		}
	}
	}
	private TempClaimFundValueEntity insertClaimFundValueEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimFundValueEntity tempClaimFundValueEntity, TempClaimMbrEntity newClaimMbrEntity) {
		if (tempClaimFundValueEntity != null && tempClaimFundValueEntity.getFundValueId() != null
				&& tempClaimFundValueEntity.getFundValueId() > 0) {

			tempClaimFundValueEntity = updateClaimFundValueEntity(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimFundValueEntity);

		} else {
			tempClaimFundValueEntity = insertClaimFundValueEntity(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimFundValueEntity);
		}
		return tempClaimFundValueEntity;
	}

	private TempClaimFundValueEntity updateClaimFundValueEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimFundValueEntity tempClaimAnnuityCalcEntity) {
		List<TempClaimFundValueEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimFundValue();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimFundValueEntity oldEntity : tempClaimAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getFundValueId().equals(tempClaimAnnuityCalcEntity.getFundValueId())) {
					TempClaimFundValueEntity newEntity = makeDuplicateClaimFundValueEntity(tempClaimAnnuityCalcEntity,
							newClaimMbrEntity);
					tempClaimAnnuityCalcEntity = tempClaimFundValueRepository.save(newEntity);
				} else {
					TempClaimFundValueEntity newEntity = makeDuplicateClaimFundValueEntity(oldEntity,
							newClaimMbrEntity);
					tempClaimFundValueRepository.save(newEntity);
				}
			}
		}
		return tempClaimAnnuityCalcEntity;
	}

	private TempClaimFundValueEntity insertClaimFundValueEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimFundValueEntity tempClaimFundValueEntity) {
		List<TempClaimFundValueEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimFundValue();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimFundValueEntity oldEntity : tempClaimAnnuityCalcEntities) {
				TempClaimFundValueEntity newEntity = makeDuplicateClaimFundValueEntity(oldEntity, newClaimMbrEntity);
				tempClaimFundValueRepository.save(newEntity);
			}
		}
		if (tempClaimFundValueEntity != null) {
			TempClaimFundValueEntity newEntity = makeDuplicateClaimFundValueEntity(tempClaimFundValueEntity,
					newClaimMbrEntity);
			tempClaimFundValueEntity = tempClaimFundValueRepository.save(newEntity);
		}
		return tempClaimFundValueEntity;
	}

	private TempClaimFundValueEntity makeDuplicateClaimFundValueEntity(TempClaimFundValueEntity oldEntity,
			TempClaimMbrEntity newClaimMbrEntity) {
		TempClaimFundValueEntity newEntity = new TempClaimFundValueEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setFundValueId(null);
		newEntity.setClaimMbrEntity(newClaimMbrEntity);
		return newEntity;
	}

	@Override
	public TempClaimMbrFundValueEntity insertClaimMbrFundValue(String claimNo, TempClaimMbrFundValueEntity entity) {
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrFundValue {}:: start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
					newClaimEntity = tempClaimRepository.save(newClaimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
								oldTempClaimEntity.getClaimMbr(), newClaimEntity);

						newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);
						
						entity = insertClaimMbrFundValueEntity(oldClaimMbrEntity, entity, newClaimMbrEntity);

						insertClaimAnuityCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimCommutationCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimAppointeeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrAddressEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrBankEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrBankPayeeEntity(oldClaimMbrEntity, newClaimMbrEntity, null);
						 
						

					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					tempClaimRepository.save(oldTempClaimEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrFundValue {}:: error is"+e.getMessage());
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrFundValue {}:: ended");
		return entity;
	}
	
	@Override
	public TempClaimMbrFundValueEntity insertClaimMbrFundValueRefesh(String claimNo, TempClaimMbrFundValueEntity entity,
			String nomineeCode) {
		Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
		if (optional.isPresent()) {
			TempClaimEntity oldTempClaimEntity = optional.get();
			if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
				TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
				newClaimEntity = tempClaimRepository.save(newClaimEntity);
				if (oldTempClaimEntity.getClaimMbr() != null) {
					TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

					TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
							oldTempClaimEntity.getClaimMbr(), newClaimEntity);

					newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);
					
					entity = insertClaimMbrFundValueEntity(oldClaimMbrEntity, entity, newClaimMbrEntity);
					List<TempClaimCommutationCalcEntity> commuList=new ArrayList<>();
					
					oldClaimMbrEntity.getClaimCommutationCalc().forEach(commutation -> {
						if(!nomineeCode.equalsIgnoreCase(commutation.getNomineeCode())) {
							commuList.add(commutation);
						}
					});
					oldClaimMbrEntity.setClaimCommutationCalc(commuList);
					List<TempClaimAnnuityCalcEntity> anuityList=new ArrayList<>();
					oldClaimMbrEntity.getClaimAnuityCalc().forEach(anuity -> {
						if(!nomineeCode.equalsIgnoreCase(anuity.getNomineeCode())) {
							anuityList.add(anuity);
						}
						
					});
					oldClaimMbrEntity.setClaimAnuityCalc(anuityList);
					List<ClaimPayeeTempBankDetailsEntity> bankList=new ArrayList<>();
					oldClaimMbrEntity.getClaimPayeeTempBank().forEach(bank -> {
						if(!nomineeCode.equalsIgnoreCase(bank.getNomineeCode())) {
							bankList.add(bank);
						}
					});
					oldClaimMbrEntity.setClaimPayeeTempBank(bankList);
					insertClaimAnuityCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

					insertClaimCommutationCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

					insertClaimAppointeeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

					insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

					insertClaimMbrAddressEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

					insertClaimMbrBankEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
					
					insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
					
					insertClaimMbrBankPayeeEntity(oldClaimMbrEntity, newClaimMbrEntity, null);
					 
					

				}

				oldTempClaimEntity.setIsActive(Boolean.FALSE);
				tempClaimRepository.save(oldTempClaimEntity);
			}

		}

		return entity;
	}
	
	/*** BankPayee ***/
	
	@Override
	public ClaimPayeeTempBankDetailsEntity insertClaimMbrBankPayee(String claimNo,ClaimPayeeTempBankDetailsEntity entity) {
		
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrBankPayee {}:: start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
					newClaimEntity = tempClaimRepository.save(newClaimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
								oldTempClaimEntity.getClaimMbr(), newClaimEntity);

						newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);

						insertClaimAnuityCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimCommutationCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimAppointeeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrAddressEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrBankEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						 
						entity = insertClaimMbrPayeeTempBankDetailsEntity(oldClaimMbrEntity, entity, newClaimMbrEntity);

					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					tempClaimRepository.save(oldTempClaimEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrBankPayee {}:: error is "+e.getMessage());
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrBankPayee {}:: ended");
		return entity;
	}
	
	/** Change to withdrawal **/
	
@Override
public String insertDataForChangetoWithdrawalGSAGNPolicy(String claimNo, String type) {
	logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrBankPayee {}:: start");
	try {
		Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
		if (optional.isPresent()) {
			TempClaimEntity oldTempClaimEntity = optional.get();
			if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
				TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
				newClaimEntity.setModeOfExit(type.equalsIgnoreCase("WITHDRAWAL") ? ClaimConstants.WITHDRAWAL
						: oldTempClaimEntity.getModeOfExit());

				newClaimEntity = tempClaimRepository.save(newClaimEntity);
				if (oldTempClaimEntity.getClaimMbr() != null) {
					TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

					TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
							oldTempClaimEntity.getClaimMbr(), newClaimEntity);

					newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);
					insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
					insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

				}
				oldTempClaimEntity.setIsActive(Boolean.FALSE);
				tempClaimRepository.save(oldTempClaimEntity);
				if (type.equalsIgnoreCase("WITHDRAWAL")
						&& newClaimEntity.getModeOfExit() == ClaimConstants.WITHDRAWAL) {
					return "This claim changed to withdrawal successfully";
				} else {
					return "Commutation percentage changed successfully";
				}

			}

		}
		else {
			logger.error(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrBankPayee {}:: Invalid Claim");
			return ClaimConstants.NO_RECORD_FOUND;
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrBankPayee {}:: error is " + e.getMessage());
	}
	logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimMbrBankPayee {}:: end");
	return ClaimConstants.SUCCESS;
}
	
	private TempClaimMbrFundValueEntity insertClaimMbrFundValueEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrFundValueEntity tempClaimMbrFundValueEntity, TempClaimMbrEntity newClaimMbrEntity) {
		if (tempClaimMbrFundValueEntity != null && tempClaimMbrFundValueEntity.getFundValueId() != null
				&& tempClaimMbrFundValueEntity.getFundValueId() > 0) {

			tempClaimMbrFundValueEntity = updateClaimMbrFundValueEntity(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimMbrFundValueEntity);

		} else {
			tempClaimMbrFundValueEntity = insertClaimMbrFundValueEntity(oldClaimMbrEntity, newClaimMbrEntity,
					tempClaimMbrFundValueEntity);
		}
		return tempClaimMbrFundValueEntity;
	}
	
	
	
	private ClaimPayeeTempBankDetailsEntity insertClaimMbrPayeeTempBankDetailsEntity(TempClaimMbrEntity oldClaimMbrEntity,
			ClaimPayeeTempBankDetailsEntity claimPayeeTempBankDetailsEntity, TempClaimMbrEntity newClaimMbrEntity) {
		if (claimPayeeTempBankDetailsEntity != null && claimPayeeTempBankDetailsEntity.getBankAccountId() != null
				&& claimPayeeTempBankDetailsEntity.getBankAccountId() > 0) {

			claimPayeeTempBankDetailsEntity = updateClaimMbrBankPayeeeEntity(oldClaimMbrEntity, newClaimMbrEntity,
					claimPayeeTempBankDetailsEntity);

		} else {
			claimPayeeTempBankDetailsEntity = insertClaimMbrBankPayeeEntity(oldClaimMbrEntity, newClaimMbrEntity,
					claimPayeeTempBankDetailsEntity);
		}
		return claimPayeeTempBankDetailsEntity;
	}

	private TempClaimMbrFundValueEntity updateClaimMbrFundValueEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimMbrFundValueEntity tempClaimAnnuityCalcEntity) {
		List<TempClaimMbrFundValueEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrFundValue();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimMbrFundValueEntity oldEntity : tempClaimAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getFundValueId().equals(tempClaimAnnuityCalcEntity.getFundValueId())) {
					TempClaimMbrFundValueEntity newEntity = makeDuplicateClaimMbrFundValueEntity(tempClaimAnnuityCalcEntity,
							newClaimMbrEntity);
					tempClaimAnnuityCalcEntity = tempClaimMbrFundValueRepository.save(newEntity);
				} else {
					TempClaimMbrFundValueEntity newEntity = makeDuplicateClaimMbrFundValueEntity(oldEntity,
							newClaimMbrEntity);
					tempClaimMbrFundValueRepository.save(newEntity);
				}
			}
		}
		return tempClaimAnnuityCalcEntity;
	}
	
	
//	private ClaimPayeeTempBankDetailsEntity updateClaimMbrBankPayeeeEntity1(TempClaimMbrEntity oldClaimMbrEntity,
//			TempClaimMbrEntity newClaimMbrEntity, ClaimPayeeTempBankDetailsEntity claimPayeeTempBankDetailsEntity) {
//		List<TempClaimMbrFundValueEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrFundValue();
//		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
//			for (TempClaimMbrFundValueEntity oldEntity : tempClaimAnnuityCalcEntities) {
//				if (oldEntity != null && oldEntity.getFundValueId().equals(claimPayeeTempBankDetailsEntity.getBankAccountId())) {
//					ClaimPayeeTempBankDetailsEntity newEntity = makeDuplicateClaimMbrBankPayeeEntity(claimPayeeTempBankDetailsEntity,
//							newClaimMbrEntity);
//					claimPayeeTempBankDetailsEntity = tempClaimPayeeBankDetailsRepository.save(newEntity);
//				} else {
//					TempClaimMbrFundValueEntity newEntity = makeDuplicateClaimMbrFundValueEntity(oldEntity,
//							newClaimMbrEntity);
//					tempClaimMbrFundValueRepository.save(newEntity);
//				}
//			}
//		}
//		return claimPayeeTempBankDetailsEntity;
//	}
	
	
	private ClaimPayeeTempBankDetailsEntity updateClaimMbrBankPayeeeEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, ClaimPayeeTempBankDetailsEntity claimPayeeTempBankDetailsEntity) {
		List<ClaimPayeeTempBankDetailsEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimPayeeTempBank();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (ClaimPayeeTempBankDetailsEntity oldEntity : tempClaimAnnuityCalcEntities) {
				if (oldEntity != null
						&& oldEntity.getBankAccountId().equals(claimPayeeTempBankDetailsEntity.getBankAccountId())) {
					ClaimPayeeTempBankDetailsEntity newEntity = makeDuplicateClaimMbrBankPayeeEntity(
							claimPayeeTempBankDetailsEntity, newClaimMbrEntity);
					claimPayeeTempBankDetailsEntity = tempClaimPayeeBankDetailsRepository.save(newEntity);
				} else {
					ClaimPayeeTempBankDetailsEntity newEntity = makeDuplicateClaimMbrBankPayeeEntity(oldEntity,
							newClaimMbrEntity);
					tempClaimPayeeBankDetailsRepository.save(newEntity);
				}
			}
		}
		return claimPayeeTempBankDetailsEntity;
	}


	private TempClaimMbrFundValueEntity insertClaimMbrFundValueEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, TempClaimMbrFundValueEntity tempClaimMbrFundValueEntity) {
		List<TempClaimMbrFundValueEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimMbrFundValue();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (TempClaimMbrFundValueEntity oldEntity : tempClaimAnnuityCalcEntities) {
				TempClaimMbrFundValueEntity newEntity = makeDuplicateClaimMbrFundValueEntity(oldEntity, newClaimMbrEntity);
				tempClaimMbrFundValueRepository.save(newEntity);
			}
		}
		if (tempClaimMbrFundValueEntity != null) {
			TempClaimMbrFundValueEntity newEntity = makeDuplicateClaimMbrFundValueEntity(tempClaimMbrFundValueEntity,
					newClaimMbrEntity);
			tempClaimMbrFundValueEntity = tempClaimMbrFundValueRepository.save(newEntity);
		}
		return tempClaimMbrFundValueEntity;
	}
	
	private ClaimPayeeTempBankDetailsEntity insertClaimMbrBankPayeeEntity(TempClaimMbrEntity oldClaimMbrEntity,
			TempClaimMbrEntity newClaimMbrEntity, ClaimPayeeTempBankDetailsEntity claimPayeeTempBankDetailsEntity) {
		List<ClaimPayeeTempBankDetailsEntity> tempClaimAnnuityCalcEntities = oldClaimMbrEntity.getClaimPayeeTempBank();
		if (tempClaimAnnuityCalcEntities != null && !tempClaimAnnuityCalcEntities.isEmpty()) {
			for (ClaimPayeeTempBankDetailsEntity oldEntity : tempClaimAnnuityCalcEntities) {
				ClaimPayeeTempBankDetailsEntity newEntity = makeDuplicateClaimMbrBankPayeeEntity(oldEntity, newClaimMbrEntity);
				tempClaimPayeeBankDetailsRepository.save(newEntity);
			}
		}
		if (claimPayeeTempBankDetailsEntity != null) {
			ClaimPayeeTempBankDetailsEntity newEntity = makeDuplicateClaimMbrBankPayeeEntity(claimPayeeTempBankDetailsEntity,
					newClaimMbrEntity);
			claimPayeeTempBankDetailsEntity = tempClaimPayeeBankDetailsRepository.save(newEntity);
		}
		return claimPayeeTempBankDetailsEntity;
	}

	private TempClaimMbrFundValueEntity makeDuplicateClaimMbrFundValueEntity(TempClaimMbrFundValueEntity oldEntity,
			TempClaimMbrEntity newClaimMbrEntity) {
		TempClaimMbrFundValueEntity newEntity = new TempClaimMbrFundValueEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setFundValueId(null);
		newEntity.setClaimMbrEntity(newClaimMbrEntity);
		return newEntity;
	}
	
	private ClaimPayeeTempBankDetailsEntity makeDuplicateClaimMbrBankPayeeEntity(ClaimPayeeTempBankDetailsEntity oldEntity,
			TempClaimMbrEntity newClaimMbrEntity) {
		ClaimPayeeTempBankDetailsEntity newEntity = new ClaimPayeeTempBankDetailsEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setBankAccountId(null);
		newEntity.setClaimMbrEntity(newClaimMbrEntity);
		return newEntity;
	}

	@Override
	public TempClaimCommutationCalcEntity insertClaimCommutationCalcRefesh(String claimNo,
			TempClaimCommutationCalcEntity tempClaimCommutationCalcEntity, String nomineeCode) {
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimCommutationCalcRefesh {}:: start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity oldTempClaimEntity = optional.get();
				if (oldTempClaimEntity != null && oldTempClaimEntity.getClaimOnboarding() != null) {
					TempClaimEntity newClaimEntity = makeDublicateClaimEntity(oldTempClaimEntity);
					newClaimEntity = tempClaimRepository.save(newClaimEntity);
					if (oldTempClaimEntity.getClaimMbr() != null) {
						TempClaimMbrEntity oldClaimMbrEntity = oldTempClaimEntity.getClaimMbr();

						TempClaimMbrEntity newClaimMbrEntity = makeDublicateClaimMbrDetails(
								oldTempClaimEntity.getClaimMbr(), newClaimEntity);

						newClaimMbrEntity = tempClaimDtlsRepository.save(newClaimMbrEntity);

					

						tempClaimCommutationCalcEntity = insertClaimCommutationCalc(oldClaimMbrEntity,
								tempClaimCommutationCalcEntity, newClaimMbrEntity);
						/***AnnuityCalc And PayeeBank Details Refresh ***/
						
						if(oldTempClaimEntity.getModeOfExit()!=4) {
							List<TempClaimAnnuityCalcEntity> anuityList=new ArrayList<>();
							oldClaimMbrEntity.getClaimAnuityCalc().forEach(anuity -> {
								if(!claimNo.equalsIgnoreCase(anuity.getClaimMbrEntity().getClaimNo())) {
									anuityList.add(anuity);
								}
								
							});
							oldClaimMbrEntity.setClaimAnuityCalc(anuityList);
							List<ClaimPayeeTempBankDetailsEntity> bankList=new ArrayList<>();
							oldClaimMbrEntity.getClaimPayeeTempBank().forEach(bank -> {
								if(!claimNo.equalsIgnoreCase(bank.getClaimMbrEntity().getClaimNo())) {
									bankList.add(bank);
								}
							});
							oldClaimMbrEntity.setClaimPayeeTempBank(bankList);
						}
						else {
						List<TempClaimAnnuityCalcEntity> anuityList=new ArrayList<>();
						oldClaimMbrEntity.getClaimAnuityCalc().forEach(anuity -> {
							if(!nomineeCode.equalsIgnoreCase(anuity.getNomineeCode())) {
								anuityList.add(anuity);
							}
							
						});
						oldClaimMbrEntity.setClaimAnuityCalc(anuityList);
						List<ClaimPayeeTempBankDetailsEntity> bankList=new ArrayList<>();
						oldClaimMbrEntity.getClaimPayeeTempBank().forEach(bank -> {
							if(!nomineeCode.equalsIgnoreCase(bank.getNomineeCode())) {
								bankList.add(bank);
							}
						});
						oldClaimMbrEntity.setClaimPayeeTempBank(bankList);
						}
						/***AnnuityCalc And PayeeBank Details Refresh End***/
						insertClaimAnuityCalc(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimAppointeeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrNomineeEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrAddressEntity(oldClaimMbrEntity, null, newClaimMbrEntity);

						insertClaimMbrBankEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrFundValueEntity(oldClaimMbrEntity, null, newClaimMbrEntity);
						
						insertClaimMbrBankPayeeEntity(oldClaimMbrEntity, newClaimMbrEntity, null);
						

					}

					oldTempClaimEntity.setIsActive(Boolean.FALSE);
					tempClaimRepository.save(oldTempClaimEntity);
				}

			} 

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimCommutationCalcRefesh {}:: error is "+e.getMessage());
			
		}
		logger.info(" TempSaveClaimServiceImpl{}::--{}:: insertClaimCommutationCalcRefesh {}:: ended");
		return tempClaimCommutationCalcEntity;
	}

}
