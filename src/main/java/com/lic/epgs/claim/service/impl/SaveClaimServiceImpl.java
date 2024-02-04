package com.lic.epgs.claim.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.constants.ClaimConstants;
import com.lic.epgs.claim.constants.ClaimErrorConstants;
import com.lic.epgs.claim.constants.ClaimStatus;
import com.lic.epgs.claim.entity.ClaimAnnuityCalcEntity;
import com.lic.epgs.claim.entity.ClaimCommutationCalcEntity;
import com.lic.epgs.claim.entity.ClaimDocumentDetail;
import com.lic.epgs.claim.entity.ClaimEntity;
import com.lic.epgs.claim.entity.ClaimFundValueEntity;
import com.lic.epgs.claim.entity.ClaimMbrAddressEntity;
import com.lic.epgs.claim.entity.ClaimMbrAppointeeEntity;
import com.lic.epgs.claim.entity.ClaimMbrBankDetailEntity;
import com.lic.epgs.claim.entity.ClaimMbrEntity;
import com.lic.epgs.claim.entity.ClaimMbrFundValueEntity;
import com.lic.epgs.claim.entity.ClaimMbrNomineeEntity;
import com.lic.epgs.claim.entity.ClaimNotesEntity;
import com.lic.epgs.claim.entity.ClaimOnboardingEntity;
import com.lic.epgs.claim.entity.ClaimPayeeBankDetailsEntity;
import com.lic.epgs.claim.repository.ClaimAnnuityCalcRepository;
import com.lic.epgs.claim.repository.ClaimCommutationCalcRepository;
import com.lic.epgs.claim.repository.ClaimDocumentDetailRepository;
import com.lic.epgs.claim.repository.ClaimFundValueRepository;
import com.lic.epgs.claim.repository.ClaimMbrAddressRepository;
import com.lic.epgs.claim.repository.ClaimMbrAppointeeRepository;
import com.lic.epgs.claim.repository.ClaimMbrBankDtlsRepository;
import com.lic.epgs.claim.repository.ClaimMbrFundValueRepository;
import com.lic.epgs.claim.repository.ClaimMbrNomineeRepository;
import com.lic.epgs.claim.repository.ClaimMbrRepository;
import com.lic.epgs.claim.repository.ClaimNotesRepository;
import com.lic.epgs.claim.repository.ClaimOnboardingRepository;
import com.lic.epgs.claim.repository.ClaimPayeeBankDetailsRepository;
import com.lic.epgs.claim.repository.ClaimRepository;
import com.lic.epgs.claim.service.SaveClaimService;
import com.lic.epgs.claim.temp.entity.ClaimPayeeTempBankDetailsEntity;
import com.lic.epgs.claim.temp.entity.TempClaimAnnuityCalcEntity;
import com.lic.epgs.claim.temp.entity.TempClaimCommutationCalcEntity;
import com.lic.epgs.claim.temp.entity.TempClaimDocumentDetail;
import com.lic.epgs.claim.temp.entity.TempClaimEntity;
import com.lic.epgs.claim.temp.entity.TempClaimFundValueEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrAddressEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrAppointeeEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrBankDetailEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrFundValueEntity;
import com.lic.epgs.claim.temp.entity.TempClaimMbrNomineeEntity;
import com.lic.epgs.claim.temp.entity.TempClaimNotesEntity;
import com.lic.epgs.claim.temp.entity.TempClaimOnboardingEntity;
import com.lic.epgs.claim.temp.repository.TempClaimAnnuityCalcRepository;
import com.lic.epgs.claim.temp.repository.TempClaimCommutationCalcRepository;
import com.lic.epgs.claim.temp.repository.TempClaimDocumentDetailRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrAddressRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrAppointeeRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrBankDtlsRepository;
import com.lic.epgs.claim.temp.repository.TempClaimMbrNomineeRepository;
import com.lic.epgs.claim.temp.repository.TempClaimNotesRepository;
import com.lic.epgs.claim.temp.repository.TempClaimRepository;
import com.lic.epgs.common.service.CommonService;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.repository.MemberMasterRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.utils.CommonConstants;

@Service
@Transactional
public class SaveClaimServiceImpl implements SaveClaimService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	TempClaimRepository tempClaimRepository;

	@Autowired
	TempClaimNotesRepository tempClaimNotesRepository;

	@Autowired
	TempClaimDocumentDetailRepository tempClaimDocumentDetailRepository;

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
	ClaimRepository claimRepository;

	@Autowired
	ClaimMbrRepository claimMbrRepository;

	@Autowired
	ClaimOnboardingRepository claimOnboardingRepository;

	@Autowired
	ClaimMbrNomineeRepository claimMbrNomineeRepository;

	@Autowired
	ClaimMbrBankDtlsRepository claimMbrBankDtlsRepository;

	@Autowired
	ClaimMbrAddressRepository claimMbrAddressRepository;

	@Autowired
	ClaimMbrAppointeeRepository claimMbrAppointeeRepository;

	@Autowired
	ClaimAnnuityCalcRepository claimAnnuityCalcRepository;

	@Autowired
	ClaimCommutationCalcRepository claimCommutationCalcRepository;

	@Autowired
	ClaimNotesRepository claimNotesRepository;

	@Autowired
	ClaimDocumentDetailRepository claimDocumentDetailRepository;

	@Autowired
	ClaimFundValueRepository claimFundValueRepository;

	@Autowired
	ClaimMbrFundValueRepository claimMbrFundValueRepository;

	@Autowired
	PolicyMasterRepository policyMasterRepository;

	@Autowired
	MemberMasterRepository memberMasterRepository;

	@Autowired
	ClaimPayeeBankDetailsRepository claimPayeeBankDetailsRepository;
	@Autowired
	CommonService commonSequenceService;

	private synchronized String getClaimIntiMationSequence() {
		return commonSequenceService.getSequence(CommonConstants.CLAIM_INTIMATION_SEQ);
	}

	@Override
	public String insert(String claimNo) {
		String claimIntimationNo = null;
		logger.info("SaveClaimServiceImpl{} :: {} insert {} :: {} start");
		try {
			Optional<TempClaimEntity> optional = tempClaimRepository.findByClaimNoAndIsActive(claimNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempClaimEntity tempClaimEntity = optional.get();

				claimIntimationNo = getClaimIntiMationSequence();

				TempClaimOnboardingEntity tempClaimOnboardingEntity = tempClaimEntity.getClaimOnboarding();

				tempClaimOnboardingEntity.setInitiMationNo(claimIntimationNo);

				TempClaimMbrEntity tempClaimMbrEntity = tempClaimEntity.getClaimMbr();

				ClaimOnboardingEntity claimOnboardingEntity = insertClaimOnBoardEntity(tempClaimOnboardingEntity);

				ClaimEntity claimEntity = insertClaimEntity(tempClaimEntity, claimOnboardingEntity);

				insertClaimNotes(claimNo, claimEntity);

				insertClaimDocuments(claimNo, claimEntity);

				if (tempClaimMbrEntity != null) {

					ClaimMbrEntity claimMbrEntity = insertClaimMbr(tempClaimMbrEntity, claimEntity);

					insertClaimMbrAddress(tempClaimMbrEntity.getClaimMbrAddresses(), claimMbrEntity);

					insertClaimMbrAppointee(tempClaimMbrEntity.getClaimMbrAppointeeDtls(), claimMbrEntity);

					insertClaimMbrBank(tempClaimMbrEntity.getClaimMbrBankDetails(), claimMbrEntity);

					insertClaimMbrNominee(tempClaimMbrEntity.getClaimMbrNomineeDtls(), claimMbrEntity);

					insertClaimMbrAnnuity(tempClaimMbrEntity.getClaimAnuityCalc(), claimMbrEntity);

					insertClaimMbrCommutation(tempClaimMbrEntity.getClaimCommutationCalc(), claimMbrEntity);

					insertClaimFundValue(tempClaimMbrEntity.getClaimFundValue(), claimMbrEntity);

					insertClaimMbrFundValue(tempClaimMbrEntity.getClaimMbrFundValue(), claimMbrEntity);

					insertClaimBankPayee(tempClaimMbrEntity.getClaimPayeeTempBank(), claimMbrEntity);
					
					if(claimEntity.getStatus().equals(ClaimStatus.REJECT.val())) {
//					memberStatusUpdated(claimEntity);
					}
						

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("SaveClaimServiceImpl{} :: {} insert {} :: {} error is "+e.getMessage());
		}
		logger.info("SaveClaimServiceImpl{} :: {} insert {} :: {} ended");
		return claimIntimationNo;

	}

//	/****** @PolicyMember Updated *****/
//	private void memberStatusUpdated(ClaimEntity claimEntity) {
//		PolicyMasterEntity policyMasterEntity = policyMasterRepository
//				.findByPolicyNumberAndIsActiveTrue(claimEntity.getMasterPolicyNo());
//		if(policyMasterEntity!=null) {
//		MemberMasterEntity masterEntity=memberMasterRepository.findByLicIdAndPolicyIdAndIsZeroidFalse(claimEntity.getClaimMbr().getLicId(), policyMasterEntity.getPolicyId());
//		masterEntity.setMemberStatus("Active");
//		memberMasterRepository.save(masterEntity);
//		}
//	
//	}

	private void insertClaimMbrFundValue(List<TempClaimMbrFundValueEntity> result, ClaimMbrEntity claimMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<ClaimMbrFundValueEntity> docs = result.stream().map(x -> convertToClaimMbrFundValue(x, claimMbrEntity))
					.collect(Collectors.toList());
			claimMbrFundValueRepository.saveAll(docs);
		}

	}

	private void insertClaimBankPayee(List<ClaimPayeeTempBankDetailsEntity> result, ClaimMbrEntity claimMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<ClaimPayeeBankDetailsEntity> docs = result.stream()
					.map(x -> convertToClaimPayeeBankDetail(x, claimMbrEntity)).collect(Collectors.toList());
			claimPayeeBankDetailsRepository.saveAll(docs);
		}

	}

	private ClaimMbrFundValueEntity convertToClaimMbrFundValue(TempClaimMbrFundValueEntity tempEntity,
			ClaimMbrEntity claimMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimMbrFundValueEntity, ClaimMbrFundValueEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaimMbrEntity());
			}
		});
		ClaimMbrFundValueEntity entity = modelMapper.map(tempEntity, ClaimMbrFundValueEntity.class);
		entity.setClaimMbrEntity(claimMbrEntity);
		entity.setFundValueId(null);
		return entity;
	}

	private ClaimPayeeBankDetailsEntity convertToClaimPayeeBankDetail(ClaimPayeeTempBankDetailsEntity tempEntity,
			ClaimMbrEntity claimMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ClaimPayeeTempBankDetailsEntity, ClaimPayeeBankDetailsEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaimMbrEntity());
			}
		});
		ClaimPayeeBankDetailsEntity entity = modelMapper.map(tempEntity, ClaimPayeeBankDetailsEntity.class);
		entity.setClaimMbrEntity(claimMbrEntity);
		entity.setBankAccountId(null);
		return entity;
	}

	private void insertClaimFundValue(List<TempClaimFundValueEntity> result, ClaimMbrEntity claimMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<ClaimFundValueEntity> docs = result.stream().map(x -> convertToClaimFundValue(x, claimMbrEntity))
					.collect(Collectors.toList());
			claimFundValueRepository.saveAll(docs);
		}

	}

	private ClaimFundValueEntity convertToClaimFundValue(TempClaimFundValueEntity tempEntity,
			ClaimMbrEntity claimMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimFundValueEntity, ClaimFundValueEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaimMbrEntity());
			}
		});
		ClaimFundValueEntity entity = modelMapper.map(tempEntity, ClaimFundValueEntity.class);
		entity.setClaimMbrEntity(claimMbrEntity);
		entity.setFundValueId(null);
		return entity;
	}

	private void insertClaimMbrCommutation(List<TempClaimCommutationCalcEntity> result, ClaimMbrEntity claimMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<ClaimCommutationCalcEntity> docs = result.stream().map(x -> convertToCommutation(x, claimMbrEntity))
					.collect(Collectors.toList());
			claimCommutationCalcRepository.saveAll(docs);
		}

	}

	private ClaimCommutationCalcEntity convertToCommutation(TempClaimCommutationCalcEntity tempEntity,
			ClaimMbrEntity claimMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimCommutationCalcEntity, ClaimCommutationCalcEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaimMbrEntity());
			}
		});
		ClaimCommutationCalcEntity entity = modelMapper.map(tempEntity, ClaimCommutationCalcEntity.class);
		entity.setClaimMbrEntity(claimMbrEntity);
		entity.setCommunityId(null);
		return entity;
	}

	private void insertClaimMbrAnnuity(List<TempClaimAnnuityCalcEntity> result, ClaimMbrEntity claimMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<ClaimAnnuityCalcEntity> docs = result.stream().map(x -> convertToAnnuity(x, claimMbrEntity))
					.collect(Collectors.toList());
			claimAnnuityCalcRepository.saveAll(docs);
		}

	}

	private ClaimAnnuityCalcEntity convertToAnnuity(TempClaimAnnuityCalcEntity tempEntity,
			ClaimMbrEntity claimMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimAnnuityCalcEntity, ClaimAnnuityCalcEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaimMbrEntity());
			}
		});
		ClaimAnnuityCalcEntity entity = modelMapper.map(tempEntity, ClaimAnnuityCalcEntity.class);
		entity.setClaimMbrEntity(claimMbrEntity);
		entity.setAnnuityId(null);
		return entity;
	}

	private void insertClaimMbrNominee(List<TempClaimMbrNomineeEntity> result, ClaimMbrEntity claimMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<ClaimMbrNomineeEntity> docs = result.stream().map(x -> convertToNominee(x, claimMbrEntity))
					.collect(Collectors.toList());
			claimMbrNomineeRepository.saveAll(docs);
		}

	}

	private ClaimMbrNomineeEntity convertToNominee(TempClaimMbrNomineeEntity tempEntity,
			ClaimMbrEntity claimMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimMbrNomineeEntity, ClaimMbrNomineeEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaimMbrEntity());
			}
		});
		ClaimMbrNomineeEntity entity = modelMapper.map(tempEntity, ClaimMbrNomineeEntity.class);
		entity.setClaimMbrEntity(claimMbrEntity);
		entity.setNomineeId(null);
		return entity;
	}

	private void insertClaimMbrBank(List<TempClaimMbrBankDetailEntity> result, ClaimMbrEntity claimMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<ClaimMbrBankDetailEntity> docs = result.stream().map(x -> convertToBank(x, claimMbrEntity))
					.collect(Collectors.toList());
			claimMbrBankDtlsRepository.saveAll(docs);
		}

	}

	private ClaimMbrBankDetailEntity convertToBank(TempClaimMbrBankDetailEntity tempEntity,
			ClaimMbrEntity claimMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimMbrBankDetailEntity, ClaimMbrBankDetailEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaimMbrEntity());
			}
		});
		ClaimMbrBankDetailEntity entity = modelMapper.map(tempEntity, ClaimMbrBankDetailEntity.class);
		entity.setClaimMbrEntity(claimMbrEntity);
		entity.setBankId(null);
		return entity;
	}

	private void insertClaimMbrAppointee(List<TempClaimMbrAppointeeEntity> result, ClaimMbrEntity claimMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<ClaimMbrAppointeeEntity> docs = result.stream().map(x -> convertToAppointee(x, claimMbrEntity))
					.collect(Collectors.toList());
			claimMbrAppointeeRepository.saveAll(docs);
		}
	}

	private ClaimMbrAppointeeEntity convertToAppointee(TempClaimMbrAppointeeEntity tempEntity,
			ClaimMbrEntity claimMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimMbrAppointeeEntity, ClaimMbrAddressEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaimMbrEntity());
			}
		});
		ClaimMbrAppointeeEntity entity = modelMapper.map(tempEntity, ClaimMbrAppointeeEntity.class);
		entity.setClaimMbrEntity(claimMbrEntity);
		entity.setAppointeeId(null);
		return entity;
	}

	private void insertClaimMbrAddress(List<TempClaimMbrAddressEntity> result, ClaimMbrEntity claimMbrEntity) {
		if (result != null && !result.isEmpty()) {
			List<ClaimMbrAddressEntity> docs = result.stream().map(x -> convertToAddress(x, claimMbrEntity))
					.collect(Collectors.toList());
			claimMbrAddressRepository.saveAll(docs);
		}
	}

	private ClaimMbrAddressEntity convertToAddress(TempClaimMbrAddressEntity tempEntity,
			ClaimMbrEntity claimMbrEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimMbrAddressEntity, ClaimMbrAddressEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaimMbrEntity());
			}
		});
		ClaimMbrAddressEntity entity = modelMapper.map(tempEntity, ClaimMbrAddressEntity.class);
		entity.setClaimMbrEntity(claimMbrEntity);
		entity.setAddressId(null);
		return entity;
	}

	private ClaimMbrEntity insertClaimMbr(TempClaimMbrEntity tempClaimMbrEntity, ClaimEntity claimEntity) {
		ClaimMbrEntity entity = new ClaimMbrEntity();
		try {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.addMappings(new PropertyMap<TempClaimMbrEntity, ClaimMbrEntity>() {
				@Override
				protected void configure() {
					skip(destination.getClaim());
					skip(destination.getClaimAnuityCalc());
					skip(destination.getClaimCommutationCalc());
					skip(destination.getClaimMbrAppointeeDtls());
					skip(destination.getClaimMbrNomineeDtls());
					skip(destination.getClaimMbrAddresses());
					skip(destination.getClaimMbrBankDetails());
					skip(destination.getClaimFundValue());
					skip(destination.getClaimMbrFundValue());
					skip(destination.getClaimPayeeTempBank());
				}
			});

			entity = modelMapper.map(tempClaimMbrEntity, ClaimMbrEntity.class);

			entity.setClaim(claimEntity);
			entity.setMemberId(null);
			entity = claimMbrRepository.save(entity);
		} catch (IllegalArgumentException e) {
			//
		}
		return entity;
	}

	private void insertClaimDocuments(String claimNo, ClaimEntity claimEntity) {
		List<TempClaimDocumentDetail> result = tempClaimDocumentDetailRepository.findByClaimNoAndIsDeleted(claimNo,
				Boolean.FALSE);
		if (result != null && !result.isEmpty()) {
			List<ClaimDocumentDetail> docs = result.stream().map(x -> convertToDoc(x, claimEntity))
					.collect(Collectors.toList());
			claimDocumentDetailRepository.saveAll(docs);
		}
	}

	private ClaimDocumentDetail convertToDoc(TempClaimDocumentDetail tempClaimDocumentDetail, ClaimEntity claimEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimDocumentDetail, ClaimDocumentDetail>() {
			@Override
			protected void configure() {
				skip(destination.getClaim());
			}
		});
		ClaimDocumentDetail entity = modelMapper.map(tempClaimDocumentDetail, ClaimDocumentDetail.class);
		entity.setClaim(claimEntity);
		entity.setDocumentId(null);
		return entity;
	}

	private void insertClaimNotes(String claimNo, ClaimEntity claimEntity) {
		List<TempClaimNotesEntity> result = tempClaimNotesRepository.findByClaimNo(claimNo);
		if (result != null && !result.isEmpty()) {
			List<ClaimNotesEntity> notes = result.stream().map(x -> convertToNotes(x, claimEntity))
					.collect(Collectors.toList());
			claimNotesRepository.saveAll(notes);
		}
	}

	private ClaimNotesEntity convertToNotes(TempClaimNotesEntity tempclaimnotesentity, ClaimEntity claimEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimNotesEntity, ClaimNotesEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaim());
			}
		});
		ClaimNotesEntity entity = modelMapper.map(tempclaimnotesentity, ClaimNotesEntity.class);
		entity.setId(null);
		entity.setClaim(claimEntity);
		return entity;
	}

	private ClaimOnboardingEntity insertClaimOnBoardEntity(TempClaimOnboardingEntity tempClaimOnboardingEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimOnboardingEntity, ClaimOnboardingEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaim());
			}
		});
		ClaimOnboardingEntity entity = modelMapper.map(tempClaimOnboardingEntity, ClaimOnboardingEntity.class);
		entity.setClaimOnBoardId(null);
		entity = claimOnboardingRepository.save(entity);
		return entity;
	}

	private ClaimEntity insertClaimEntity(TempClaimEntity tempClaimEntity, ClaimOnboardingEntity onBoardingEntity) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<TempClaimEntity, ClaimEntity>() {
			@Override
			protected void configure() {
				skip(destination.getClaimDocDetails());
				skip(destination.getClaimMbr());
				skip(destination.getClaimNotes());
				skip(destination.getClaimOnboarding());
			}
		});
		ClaimEntity entity = modelMapper.map(tempClaimEntity, ClaimEntity.class);
		entity.setClaimId(null);
		entity.setPolicyType(tempClaimEntity.getPolicyType());
		entity.setClaimOnboarding(onBoardingEntity);
		entity = claimRepository.save(entity);
		return entity;
	}

}
