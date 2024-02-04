package com.lic.epgs.payout.temp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.claim.temp.repository.TempClaimPayeeBankDetailsRepository;
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
import com.lic.epgs.payout.temp.repository.PayoutPayeeBankDetailsTempRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutAnnuityCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutCommutationCalcRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutDocumentDetailRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutFundValueRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrAddressRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrAppointeeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrBankDtlsRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrFundValueRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrNomineeRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutMbrRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutNotesRepository;
import com.lic.epgs.payout.temp.repository.TempPayoutRepository;
import com.lic.epgs.payout.temp.service.TempSavePayoutService;
import com.lic.epgs.utils.NumericUtils;

@Service
//@Transactional
public class TempSavePayoutServiceImpl implements TempSavePayoutService {

	protected final Logger logger = LogManager.getLogger(getClass());

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	TempPayoutRepository tempPayoutRepository;

	@Autowired
	TempPayoutMbrRepository tempPayoutDtlsRepository;

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
	TempPayoutMbrFundValueRepository tempPayoutMbrFundValueRepository;

	@Autowired
	TempPayoutFundValueRepository tempPayoutFundValueRepository;
	@Autowired
	PayoutPayeeBankDetailsTempRepository payoutPayeeBankDetailsTempRepository;
	@Autowired
	TempPayoutDocumentDetailRepository tempPayoutDocumentDetailRepository;
	
	@Autowired
	TempPayoutNotesRepository tempPayoutNotesRepository;
	
	

//	private TempPayoutEntity makeDublicatePayoutEntity(TempPayoutEntity tempPayoutEntity) {
//		return newPayoutTemp(tempPayoutEntity);
//	}

	private TempPayoutEntity makeDublicatePayoutEntity(TempPayoutEntity tempPayoutEntity) {
		TempPayoutEntity newPayoutEntity = new TempPayoutEntity();
		BeanUtils.copyProperties(tempPayoutEntity, newPayoutEntity);
		newPayoutEntity.setPayoutId(null);
		newPayoutEntity.setPayoutMbr(null);

		newPayoutEntity.setPayoutNotes(null);

		newPayoutEntity.setPayoutDocDetails(null);
		
		return newPayoutEntity;
	}


	/****
	 * @author Muruganandam
	 * @implNote Start Temp to new Temp
	 */

	public void newPayoutTemp(TempPayoutEntity from, TempPayoutEntity to) {
		to.setPayoutId(null);
		/** tempPayoutRepository.save(to); */
		to.setPayoutDocDetails(payoutDocDetails(to, from));
		to.setPayoutNotes(payoutNotes(to, from));
		to.setPayoutMbr(payoutMbr(to, from));
	}

	public TempPayoutEntity newPayoutTemp(TempPayoutEntity from) {

		TempPayoutEntity to = new TempPayoutEntity();
		BeanUtils.copyProperties(from, to);
		to.setPayoutId(null);
		to.setPayoutDocDetails(payoutDocDetails(to, from));
		to.setPayoutNotes(payoutNotes(to, from));

		tempPayoutRepository.save(to);

		to.setPayoutMbr(payoutMbr(to, from));
		return to;
	}

	public List<TempPayoutDocumentDetail> payoutDocDetails(TempPayoutEntity payoutEntity,
			TempPayoutEntity fromPayoutEntity) {
		List<TempPayoutDocumentDetail> tos = new ArrayList<>();
		fromPayoutEntity.getPayoutDocDetails().forEach(from -> {
			TempPayoutDocumentDetail to = new TempPayoutDocumentDetail();
			BeanUtils.copyProperties(from, to);
			to.setDocumentId(null);
			to.setPayout(payoutEntity);
			tos.add(to);
		});
		return tos;
	}

	@Autowired
	TempPayoutMbrRepository tempPayoutMbrRepository;

	public TempPayoutMbrEntity payoutMbr(TempPayoutEntity payoutEntity, TempPayoutEntity fromPayoutEntity) {
		TempPayoutMbrEntity from = fromPayoutEntity.getPayoutMbr();
		TempPayoutMbrEntity to = new TempPayoutMbrEntity();
		if (from != null) {
			BeanUtils.copyProperties(from, to);
			to.setMemberId(null);
			to.setPayout(payoutEntity);
			to.setPayoutAnuityCalc(payoutAnuityCalc(to, from.getPayoutAnuityCalc()));
			to.setPayoutCommutationCalc(payoutCommutationCalc(to, from.getPayoutCommutationCalc()));
			to.setPayoutFundValue(payoutFundValue(to, from.getPayoutFundValue()));
			to.setPayoutMbrAddresses(payoutMbrAddresses(to, from.getPayoutMbrAddresses()));
			to.setPayoutMbrAppointeeDtls(payoutMbrAppointeeDtls(to, from.getPayoutMbrAppointeeDtls()));
			to.setPayoutMbrBankDetails(payoutMbrBankDetails(to, from.getPayoutMbrBankDetails()));
			to.setPayoutMbrFundValue(payoutMbrFundValue(to, from.getPayoutMbrFundValue()));
			to.setPayoutMbrNomineeDtls(payoutMbrNomineeDtls(to, from.getPayoutMbrNomineeDtls()));
			to.setPayoutPayeeBank(payoutPayeeBank(to, from.getPayoutPayeeBank()));
		}
		/* tempPayoutMbrRepository.save(to); */
		return to;
	}

	public List<TempPayoutAnnuityCalcEntity> payoutAnuityCalc(TempPayoutMbrEntity mbr,
			List<TempPayoutAnnuityCalcEntity> froms) {
		List<TempPayoutAnnuityCalcEntity> tos = new ArrayList<>();
		froms.forEach(from -> {
			TempPayoutAnnuityCalcEntity to = new TempPayoutAnnuityCalcEntity();
			BeanUtils.copyProperties(from, to);
			to.setAnnuityId(null);
			to.setPayoutMbrEntity(mbr);
			tos.add(to);
		});
		return tos;
	}

	public List<TempPayoutCommutationCalcEntity> payoutCommutationCalc(TempPayoutMbrEntity mbr,
			List<TempPayoutCommutationCalcEntity> froms) {
		List<TempPayoutCommutationCalcEntity> tos = new ArrayList<>();
		froms.forEach(from -> {
			TempPayoutCommutationCalcEntity to = new TempPayoutCommutationCalcEntity();
			BeanUtils.copyProperties(from, to);
			to.setPayoutMbrEntity(mbr);
			to.setCommunityId(null);
			tos.add(to);
		});
		return tos;
	}

	public List<TempPayoutFundValueEntity> payoutFundValue(TempPayoutMbrEntity mbr,
			List<TempPayoutFundValueEntity> froms) {
		List<TempPayoutFundValueEntity> tos = new ArrayList<>();
		froms.forEach(from -> {
			TempPayoutFundValueEntity to = new TempPayoutFundValueEntity();
			BeanUtils.copyProperties(from, to);
			to.setPayoutMbrEntity(mbr);
			to.setFundValueId(null);
			tos.add(to);
		});
		return tos;
	}

	public List<TempPayoutMbrAddressEntity> payoutMbrAddresses(TempPayoutMbrEntity mbr,
			List<TempPayoutMbrAddressEntity> froms) {
		List<TempPayoutMbrAddressEntity> tos = new ArrayList<>();
		froms.forEach(from -> {
			TempPayoutMbrAddressEntity to = new TempPayoutMbrAddressEntity();
			BeanUtils.copyProperties(from, to);
			to.setPayoutMbrEntity(mbr);
			to.setAddressId(null);
			tos.add(to);
		});
		return tos;
	}

	public List<TempPayoutMbrAppointeeEntity> payoutMbrAppointeeDtls(TempPayoutMbrEntity mbr,
			List<TempPayoutMbrAppointeeEntity> froms) {
		List<TempPayoutMbrAppointeeEntity> tos = new ArrayList<>();
		froms.forEach(from -> {
			TempPayoutMbrAppointeeEntity to = new TempPayoutMbrAppointeeEntity();
			BeanUtils.copyProperties(from, to);
			to.setPayoutMbrEntity(mbr);
			to.setAppointeeId(null);
			tos.add(to);
		});
		return tos;
	}

	public List<TempPayoutMbrBankDetailEntity> payoutMbrBankDetails(TempPayoutMbrEntity mbr,
			List<TempPayoutMbrBankDetailEntity> froms) {
		List<TempPayoutMbrBankDetailEntity> tos = new ArrayList<>();
		froms.forEach(from -> {
			TempPayoutMbrBankDetailEntity to = new TempPayoutMbrBankDetailEntity();
			BeanUtils.copyProperties(from, to);
			to.setPayoutMbrEntity(mbr);
			to.setBankId(null);
			tos.add(to);
		});
		return tos;
	}

	public List<TempPayoutMbrFundValueEntity> payoutMbrFundValue(TempPayoutMbrEntity mbr,
			List<TempPayoutMbrFundValueEntity> froms) {
		List<TempPayoutMbrFundValueEntity> tos = new ArrayList<>();
		froms.forEach(from -> {
			TempPayoutMbrFundValueEntity to = new TempPayoutMbrFundValueEntity();
			BeanUtils.copyProperties(from, to);
			to.setPayoutMbrEntity(mbr);
			to.setFundValueId(null);
			tos.add(to);
		});
		return tos;
	}

	public List<TempPayoutMbrNomineeEntity> payoutMbrNomineeDtls(TempPayoutMbrEntity mbr,
			List<TempPayoutMbrNomineeEntity> froms) {
		List<TempPayoutMbrNomineeEntity> tos = new ArrayList<>();
		froms.forEach(from -> {
			TempPayoutMbrNomineeEntity to = new TempPayoutMbrNomineeEntity();
			BeanUtils.copyProperties(from, to);
			to.setPayoutMbrEntity(mbr);
			to.setNomineeId(null);
			tos.add(to);
		});
		return tos;
	}

	public List<PayoutPayeeBankDetailsTempEntity> payoutPayeeBank(TempPayoutMbrEntity mbr,
			List<PayoutPayeeBankDetailsTempEntity> froms) {
		List<PayoutPayeeBankDetailsTempEntity> tos = new ArrayList<>();
		froms.forEach(from -> {
			PayoutPayeeBankDetailsTempEntity to = new PayoutPayeeBankDetailsTempEntity();
			BeanUtils.copyProperties(from, to);
			to.setPayoutMbrEntity(mbr);
			to.setBankAccountId(null);
			tos.add(to);
		});
		return tos;
	}

	public List<TempPayoutNotesEntity> payoutNotes(TempPayoutEntity payoutEntity, TempPayoutEntity fromPayoutEntity) {
		List<TempPayoutNotesEntity> froms = fromPayoutEntity.getPayoutNotes();
		List<TempPayoutNotesEntity> tos = new ArrayList<>();
		froms.forEach(from -> {
			TempPayoutNotesEntity to = new TempPayoutNotesEntity();
			BeanUtils.copyProperties(from, to);
			to.setPayout(payoutEntity);
			to.setId(null);
			tos.add(to);
		});
		return tos;

	}

	/****
	 * @End Temp to new Temp
	 * @param tempPayoutMbrEntity
	 * @param newPayoutEntity
	 * @return
	 */

	private TempPayoutMbrEntity makeDublicatePayoutMbrDetails(TempPayoutMbrEntity tempPayoutMbrEntity,
			TempPayoutEntity newPayoutEntity) {

		TempPayoutMbrEntity newPayoutMbrEntity = new TempPayoutMbrEntity();
		BeanUtils.copyProperties(tempPayoutMbrEntity, newPayoutMbrEntity);
		newPayoutMbrEntity.setPayout(newPayoutEntity);
		newPayoutMbrEntity.setPayoutAnuityCalc(null);
		newPayoutMbrEntity.setPayoutCommutationCalc(null);
		newPayoutMbrEntity.setPayoutMbrAppointeeDtls(null);
		newPayoutMbrEntity.setPayoutMbrNomineeDtls(null);
		newPayoutMbrEntity.setPayoutMbrAddresses(null);
		newPayoutMbrEntity.setPayoutMbrBankDetails(null);
		newPayoutMbrEntity.setMemberId(null);
		newPayoutMbrEntity.setPayoutFundValue(null);
		newPayoutMbrEntity.setPayoutMbrFundValue(null);
		newPayoutMbrEntity.setPayoutPayeeBank(null);
		newPayoutMbrEntity.setPayout(newPayoutEntity);
		return newPayoutMbrEntity;

	}

	@Override
	public TempPayoutAnnuityCalcEntity insertPayoutAnnuityCalc(String payoutNo,
			TempPayoutAnnuityCalcEntity tempPayoutAnnuityCalcEntity) {
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutAnnuityCalc  {} ::{} start");
		try {
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempPayoutEntity oldTempPayoutEntity = optional.get();
				TempPayoutEntity newPayoutEntity = makeDublicatePayoutEntity(oldTempPayoutEntity);
				newPayoutEntity = tempPayoutRepository.save(newPayoutEntity);
				if (oldTempPayoutEntity.getPayoutMbr() != null) {
					TempPayoutMbrEntity oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();

					TempPayoutMbrEntity newPayoutMbrEntity = makeDublicatePayoutMbrDetails(
							oldTempPayoutEntity.getPayoutMbr(), newPayoutEntity);

					newPayoutMbrEntity = tempPayoutDtlsRepository.save(newPayoutMbrEntity);

					tempPayoutAnnuityCalcEntity = insertPayoutAnuityCalc(oldPayoutMbrEntity, tempPayoutAnnuityCalcEntity,
							newPayoutMbrEntity);

					insertPayoutCommutationCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

					insertPayoutAppointeeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

					insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

					insertPayoutMbrAddressEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

					insertPayoutMbrBankEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

					insertPayoutFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

					insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

				}

				oldTempPayoutEntity.setIsActive(Boolean.FALSE);
				tempPayoutRepository.save(oldTempPayoutEntity);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutAnnuityCalc  {} ::{} error is "+e.getMessage());
		}
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutAnnuityCalc  {} ::{} ended");
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutAnnuityCalcEntity insertPayoutAnuityCalc(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutAnnuityCalcEntity tempPayoutAnnuityCalcEntity, TempPayoutMbrEntity newPayoutMbrEntity) {
		if (tempPayoutAnnuityCalcEntity != null && tempPayoutAnnuityCalcEntity.getAnnuityId() != null
				&& tempPayoutAnnuityCalcEntity.getAnnuityId() > 0) {
			tempPayoutAnnuityCalcEntity = updatePayoutAnuityCalc(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutAnnuityCalcEntity);
		} else {
			tempPayoutAnnuityCalcEntity = insertPayoutAnuityCalc(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutAnnuityCalcEntity);
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutAnnuityCalcEntity updatePayoutAnuityCalc(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutAnnuityCalcEntity tempPayoutAnnuityCalcEntity) {
		List<TempPayoutAnnuityCalcEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutAnuityCalc();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutAnnuityCalcEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getAnnuityId().equals(tempPayoutAnnuityCalcEntity.getAnnuityId())) {
					TempPayoutAnnuityCalcEntity newEntity = makeDuplicatePayoutAnunityEntity(
							tempPayoutAnnuityCalcEntity, newPayoutMbrEntity);
					tempPayoutAnnuityCalcEntity = tempPayoutAnnuityCalcRepository.save(newEntity);
				} else {
					TempPayoutAnnuityCalcEntity newEntity = makeDuplicatePayoutAnunityEntity(oldEntity,
							newPayoutMbrEntity);
					tempPayoutAnnuityCalcRepository.save(newEntity);
				}
			}
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutAnnuityCalcEntity insertPayoutAnuityCalc(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutAnnuityCalcEntity tempPayoutAnnuityCalcEntity) {
		List<TempPayoutAnnuityCalcEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutAnuityCalc();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutAnnuityCalcEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				TempPayoutAnnuityCalcEntity newEntity = makeDuplicatePayoutAnunityEntity(oldEntity, newPayoutMbrEntity);
				tempPayoutAnnuityCalcRepository.save(newEntity);
			}
		}
		if (tempPayoutAnnuityCalcEntity != null) {
			TempPayoutAnnuityCalcEntity newEntity = makeDuplicatePayoutAnunityEntity(tempPayoutAnnuityCalcEntity,
					newPayoutMbrEntity);
			tempPayoutAnnuityCalcEntity = tempPayoutAnnuityCalcRepository.save(newEntity);
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutAnnuityCalcEntity makeDuplicatePayoutAnunityEntity(TempPayoutAnnuityCalcEntity oldEntity,
			TempPayoutMbrEntity newPayoutMbrEntity) {
		TempPayoutAnnuityCalcEntity newEntity = new TempPayoutAnnuityCalcEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setAnnuityId(null);
		newEntity.setPayoutMbrEntity(newPayoutMbrEntity);
		return newEntity;
	}

	@Override
	public TempPayoutCommutationCalcEntity insertPayoutCommutationCalc(String payoutNo,
			TempPayoutCommutationCalcEntity tempPayoutCommutationCalcEntity) {
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutCommutationCalc  {} ::{} start");
		try {
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempPayoutEntity oldTempPayoutEntity = optional.get();
				if (oldTempPayoutEntity != null) {
					TempPayoutEntity newPayoutEntity = makeDublicatePayoutEntity(oldTempPayoutEntity);
					newPayoutEntity = tempPayoutRepository.save(newPayoutEntity);
					if (oldTempPayoutEntity.getPayoutMbr() != null) {
						TempPayoutMbrEntity oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();

						TempPayoutMbrEntity newPayoutMbrEntity = makeDublicatePayoutMbrDetails(
								oldTempPayoutEntity.getPayoutMbr(), newPayoutEntity);

						newPayoutMbrEntity = tempPayoutDtlsRepository.save(newPayoutMbrEntity);

						insertPayoutAnuityCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						tempPayoutCommutationCalcEntity = insertPayoutCommutationCalc(oldPayoutMbrEntity,
								tempPayoutCommutationCalcEntity, newPayoutMbrEntity);

						insertPayoutAppointeeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrAddressEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrBankEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

					}

					oldTempPayoutEntity.setIsActive(Boolean.FALSE);
					tempPayoutRepository.save(oldTempPayoutEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutCommutationCalc  {} ::{} error is ");
		}
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutCommutationCalc  {} ::{} ended");
		return tempPayoutCommutationCalcEntity;
	}

	private TempPayoutCommutationCalcEntity insertPayoutCommutationCalc(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutCommutationCalcEntity tempPayoutCommutationCalcEntity, TempPayoutMbrEntity newPayoutMbrEntity) {
		if (tempPayoutCommutationCalcEntity != null && tempPayoutCommutationCalcEntity.getCommunityId() != null
				&& tempPayoutCommutationCalcEntity.getCommunityId() > 0) {

			tempPayoutCommutationCalcEntity = updatePayoutCommutationCalc(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutCommutationCalcEntity);

		} else {
			tempPayoutCommutationCalcEntity = insertPayoutCommutationCalc(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutCommutationCalcEntity);
		}
		return tempPayoutCommutationCalcEntity;
	}

	private TempPayoutCommutationCalcEntity updatePayoutCommutationCalc(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutCommutationCalcEntity tempPayoutAnnuityCalcEntity) {
		List<TempPayoutCommutationCalcEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity
				.getPayoutCommutationCalc();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutCommutationCalcEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				if (oldEntity != null
						&& oldEntity.getCommunityId().equals(tempPayoutAnnuityCalcEntity.getCommunityId())) {
					TempPayoutCommutationCalcEntity newEntity = makeDuplicatePayoutAnunityEntity(
							tempPayoutAnnuityCalcEntity, newPayoutMbrEntity);
					tempPayoutAnnuityCalcEntity = tempPayoutCommutationCalcRepository.save(newEntity);
				} else {
					TempPayoutCommutationCalcEntity newEntity = makeDuplicatePayoutAnunityEntity(oldEntity,
							newPayoutMbrEntity);
					tempPayoutCommutationCalcRepository.save(newEntity);
				}
			}
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutCommutationCalcEntity insertPayoutCommutationCalc(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutCommutationCalcEntity tempPayoutAnnuityCalcEntity) {
		List<TempPayoutCommutationCalcEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity
				.getPayoutCommutationCalc();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutCommutationCalcEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				TempPayoutCommutationCalcEntity newEntity = makeDuplicatePayoutAnunityEntity(oldEntity,
						newPayoutMbrEntity);
				tempPayoutCommutationCalcRepository.save(newEntity);
			}
		}
		if (tempPayoutAnnuityCalcEntity != null) {
			TempPayoutCommutationCalcEntity newEntity = makeDuplicatePayoutAnunityEntity(tempPayoutAnnuityCalcEntity,
					newPayoutMbrEntity);
			tempPayoutAnnuityCalcEntity = tempPayoutCommutationCalcRepository.save(newEntity);
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutCommutationCalcEntity makeDuplicatePayoutAnunityEntity(TempPayoutCommutationCalcEntity oldEntity,
			TempPayoutMbrEntity newPayoutMbrEntity) {
		TempPayoutCommutationCalcEntity newEntity = new TempPayoutCommutationCalcEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setCommunityId(null);
		newEntity.setPayoutMbrEntity(newPayoutMbrEntity);
		return newEntity;
	}

	@Override
	public TempPayoutMbrEntity insertPayoutMbr(String payoutNo, TempPayoutMbrEntity tempPayoutMbrEntity) {
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbr  {} ::{} start");
		try {
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempPayoutEntity oldTempPayoutEntity = optional.get();
				if (oldTempPayoutEntity != null) {
					TempPayoutEntity newPayoutEntity = makeDublicatePayoutEntity(oldTempPayoutEntity);
					newPayoutEntity = tempPayoutRepository.save(newPayoutEntity);
					if (oldTempPayoutEntity.getPayoutMbr() != null) {
						TempPayoutMbrEntity oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();

						tempPayoutMbrEntity = makeDublicatePayoutMbrDetails(tempPayoutMbrEntity, newPayoutEntity);

						tempPayoutMbrEntity = tempPayoutDtlsRepository.save(tempPayoutMbrEntity);

						insertPayoutAnuityCalc(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutCommutationCalc(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutAppointeeEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutMbrAddressEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutMbrBankEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutFundValueEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

					}

					oldTempPayoutEntity.setIsActive(Boolean.FALSE);
					tempPayoutRepository.save(oldTempPayoutEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbr  {} ::{} error is "+e.getMessage());
		}
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbr  {} ::{} ended");
		return tempPayoutMbrEntity;

	}
	
	@Override
	public TempPayoutEntity insertPayout(String initiMationNo, TempPayoutEntity payoutEntity) {
		try {
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayout  {} ::{} start");
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByInitiMationNoAndIsActive(initiMationNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempPayoutEntity oldTempPayoutEntity = optional.get();
				if (oldTempPayoutEntity != null) {
					payoutEntity = makeDublicatePayoutEntity(payoutEntity);
					
//				if(!oldTempPayoutEntity.getPayoutNotes().isEmpty()) {
//					List<TempPayoutNotesEntity> noteList = new ArrayList<>();
//					for(TempPayoutNotesEntity note:oldTempPayoutEntity.getPayoutNotes()) {
//						TempPayoutNotesEntity noteEntity = new TempPayoutNotesEntity();
//						noteEntity.setPayout(payoutEntity);
//						noteEntity.setCreatedBy(note.getCreatedBy());
//						noteEntity.setCreatedOn(new Date());
//						noteEntity.setNoteContents(note.getNoteContents());
//						
//						noteList.add(noteEntity);	
//					}
//					
//					payoutEntity.setPayoutNotes(noteList);
//				}
//				
//				if(!oldTempPayoutEntity.getPayoutDocDetails().isEmpty()) {
//					
//					List<TempPayoutDocumentDetail> docList = new ArrayList<>();
//					
//					for(TempPayoutDocumentDetail doc:oldTempPayoutEntity.getPayoutDocDetails()) {
//						TempPayoutDocumentDetail docEntity = new TempPayoutDocumentDetail();
//						BeanUtils.copyProperties(doc, docEntity);
//						docEntity.setDocumentId(null);
//						docEntity.setPayout(payoutEntity);
//
//						docList.add(docEntity);
//					}
//					
//					payoutEntity.setPayoutDocDetails(docList);
//
//				}
					
					
					payoutEntity = tempPayoutRepository.save(payoutEntity);
					
					if(!oldTempPayoutEntity.getPayoutNotes().isEmpty()) {
						List<TempPayoutNotesEntity> noteList = new ArrayList<>();
						for(TempPayoutNotesEntity note:oldTempPayoutEntity.getPayoutNotes()) {
							TempPayoutNotesEntity noteEntity = new TempPayoutNotesEntity();
							noteEntity.setPayout(payoutEntity);
							noteEntity.setCreatedBy(note.getCreatedBy());
							noteEntity.setCreatedOn(new Date());
							noteEntity.setNoteContents(note.getNoteContents());
							tempPayoutNotesRepository.save(noteEntity);
							noteList.add(noteEntity);	
						}
						
						oldTempPayoutEntity.setPayoutNotes(noteList);
					}
					
					if(!oldTempPayoutEntity.getPayoutDocDetails().isEmpty()) {
						
						List<TempPayoutDocumentDetail> docList = new ArrayList<>();
						
						for(TempPayoutDocumentDetail doc:oldTempPayoutEntity.getPayoutDocDetails()) {
							TempPayoutDocumentDetail docEntity = new TempPayoutDocumentDetail();
							BeanUtils.copyProperties(doc, docEntity);
							docEntity.setDocumentId(null);
							docEntity.setPayout(payoutEntity);
							tempPayoutDocumentDetailRepository.save(docEntity);
							docList.add(docEntity);
						}
						
						oldTempPayoutEntity.setPayoutDocDetails(docList);

					}
					
					if (oldTempPayoutEntity.getPayoutMbr() != null) {
						TempPayoutMbrEntity oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();

						TempPayoutMbrEntity tempPayoutMbrEntity = makeDublicatePayoutMbrDetails(oldPayoutMbrEntity,
								payoutEntity);
//					tempPayoutMbrEntity.getPayout().setPayoutMbr(tempPayoutMbrEntity);
						tempPayoutMbrEntity = tempPayoutDtlsRepository.save(tempPayoutMbrEntity);

						insertPayoutAnuityCalc(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutCommutationCalc(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutAppointeeEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutMbrAddressEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutMbrBankEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutFundValueEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);

						insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);
						
						insertPayoutPayeeBankValueDetails(oldPayoutMbrEntity, null, tempPayoutMbrEntity);


					}

					oldTempPayoutEntity.setIsActive(Boolean.FALSE);
					tempPayoutRepository.save(oldTempPayoutEntity);
				}

			}
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayout  {} ::{} error is "+e.getMessage());
		}
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayout  {} ::{} ended");
		return payoutEntity;
	}


	private PayoutPayeeBankDetailsTempEntity insertPayoutPayeeBankValueDetails(TempPayoutMbrEntity oldPayoutMbrEntity,
			PayoutPayeeBankDetailsTempEntity tempPayoutPayeeBankDetailsEntity, TempPayoutMbrEntity newPayoutMbrEntity) {
		if (tempPayoutPayeeBankDetailsEntity != null && tempPayoutPayeeBankDetailsEntity.getBankAccountId() != null
				&& tempPayoutPayeeBankDetailsEntity.getBankAccountId() > 0) {
			tempPayoutPayeeBankDetailsEntity = updatePayoutPayeeBankEntity(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutPayeeBankDetailsEntity);

		} else {
			tempPayoutPayeeBankDetailsEntity = insertPayoutPayeeBankEntity(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutPayeeBankDetailsEntity);
		}
		return tempPayoutPayeeBankDetailsEntity;
	}

	private PayoutPayeeBankDetailsTempEntity updatePayoutPayeeBankEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, PayoutPayeeBankDetailsTempEntity tempPayoutPayeeBankDetailsEntity) {
		List<PayoutPayeeBankDetailsTempEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutPayeeBank();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (PayoutPayeeBankDetailsTempEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				if (oldEntity != null
						&& oldEntity.getBankAccountId().equals(tempPayoutPayeeBankDetailsEntity.getBankAccountId())) {
					PayoutPayeeBankDetailsTempEntity newEntity = makeDuplicatePayoutPayeeBankDetailsTempEntity(
							tempPayoutPayeeBankDetailsEntity, newPayoutMbrEntity);
					tempPayoutPayeeBankDetailsEntity = payoutPayeeBankDetailsTempRepository.save(newEntity);
				} else {
					PayoutPayeeBankDetailsTempEntity newEntity = makeDuplicatePayoutPayeeBankDetailsTempEntity(oldEntity,
							newPayoutMbrEntity);
					payoutPayeeBankDetailsTempRepository.save(newEntity);
				}
			}
		}
		return tempPayoutPayeeBankDetailsEntity;
	}
	
	private PayoutPayeeBankDetailsTempEntity insertPayoutPayeeBankEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, PayoutPayeeBankDetailsTempEntity tempPayoutMbrFundValueEntity) {
		List<PayoutPayeeBankDetailsTempEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutPayeeBank();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (PayoutPayeeBankDetailsTempEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				PayoutPayeeBankDetailsTempEntity newEntity = makeDuplicatePayoutPayeeBankDetailsTempEntity(oldEntity,
						newPayoutMbrEntity);
				payoutPayeeBankDetailsTempRepository.save(newEntity);
			}
		}
		if (tempPayoutMbrFundValueEntity != null) {
			PayoutPayeeBankDetailsTempEntity newEntity = makeDuplicatePayoutPayeeBankDetailsTempEntity(tempPayoutMbrFundValueEntity,
					newPayoutMbrEntity);
			tempPayoutMbrFundValueEntity = payoutPayeeBankDetailsTempRepository.save(newEntity);
		}
		return tempPayoutMbrFundValueEntity;
	}
	
	private PayoutPayeeBankDetailsTempEntity makeDuplicatePayoutPayeeBankDetailsTempEntity(PayoutPayeeBankDetailsTempEntity oldEntity,
			TempPayoutMbrEntity newPayoutMbrEntity) {
		PayoutPayeeBankDetailsTempEntity newEntity = new PayoutPayeeBankDetailsTempEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		oldEntity.setIsActive(Boolean.FALSE);
		payoutPayeeBankDetailsTempRepository.save(oldEntity);
		newEntity.setBankAccountId(null);
		newEntity.setPayoutMbrEntity(newPayoutMbrEntity);
		Integer versionNo = NumericUtils.convertStringToInteger(oldEntity.getVersionNo());
		newEntity.setVersionNo(NumericUtils.convertIntegerToString(versionNo != null && versionNo >= 1 ? versionNo + 1 : 1));
		return newEntity;
	}

//	@Override
//	public TempPayoutEntity insertPayout(String initiMationNo, TempPayoutEntity payoutEntity) {
//		Optional<TempPayoutEntity> optional = tempPayoutRepository.findByInitiMationNoAndIsActive(initiMationNo,
//				Boolean.TRUE);
//		if (optional.isPresent()) {
//			TempPayoutEntity oldTempPayoutEntity = optional.get();
//			if (oldTempPayoutEntity != null) {
//				newPayoutTemp(oldTempPayoutEntity, payoutEntity);
//
////				if(!oldTempPayoutEntity.getPayoutNotes().isEmpty()) {
////					List<TempPayoutNotesEntity> noteList = new ArrayList<>();
////					for(TempPayoutNotesEntity note:oldTempPayoutEntity.getPayoutNotes()) {
////						TempPayoutNotesEntity noteEntity = new TempPayoutNotesEntity();
////						noteEntity.setPayout(payoutEntity);
////						noteEntity.setCreatedBy(note.getCreatedBy());
////						noteEntity.setCreatedOn(new Date());
////						noteEntity.setNoteContents(note.getNoteContents());
////						
////						noteList.add(noteEntity);	
////					}
////					
////					payoutEntity.setPayoutNotes(noteList);
////				}
////				
////				if(!oldTempPayoutEntity.getPayoutDocDetails().isEmpty()) {
////					
////					List<TempPayoutDocumentDetail> docList = new ArrayList<>();
////					
////					for(TempPayoutDocumentDetail doc:oldTempPayoutEntity.getPayoutDocDetails()) {
////						TempPayoutDocumentDetail docEntity = new TempPayoutDocumentDetail();
////						BeanUtils.copyProperties(doc, docEntity);
////						docEntity.setDocumentId(null);
////						docEntity.setPayout(payoutEntity);
////
////						docList.add(docEntity);
////					}
////					
////					payoutEntity.setPayoutDocDetails(docList);
////
////				}
//
//				tempPayoutRepository.save(payoutEntity);
//
//				/*
//				 * if (!oldTempPayoutEntity.getPayoutNotes().isEmpty()) {
//				 * List<TempPayoutNotesEntity> noteList = new ArrayList<>(); for
//				 * (TempPayoutNotesEntity note : oldTempPayoutEntity.getPayoutNotes()) {
//				 * TempPayoutNotesEntity noteEntity = new TempPayoutNotesEntity();
//				 * noteEntity.setPayout(payoutEntity);
//				 * noteEntity.setCreatedBy(note.getCreatedBy()); noteEntity.setCreatedOn(new
//				 * Date()); noteEntity.setNoteContents(note.getNoteContents());
//				 * 
//				 * noteList.add(noteEntity); }
//				 * 
//				 * oldTempPayoutEntity.setPayoutNotes(noteList); }
//				 * 
//				 * if (!oldTempPayoutEntity.getPayoutDocDetails().isEmpty()) {
//				 * 
//				 * List<TempPayoutDocumentDetail> docList = new ArrayList<>();
//				 * 
//				 * for (TempPayoutDocumentDetail doc :
//				 * oldTempPayoutEntity.getPayoutDocDetails()) { TempPayoutDocumentDetail
//				 * docEntity = new TempPayoutDocumentDetail(); BeanUtils.copyProperties(doc,
//				 * docEntity); docEntity.setDocumentId(null); docEntity.setPayout(payoutEntity);
//				 * 
//				 * docList.add(docEntity); }
//				 * 
//				 * oldTempPayoutEntity.setPayoutDocDetails(docList);
//				 * 
//				 * }
//				 * 
//				 * if (oldTempPayoutEntity.getPayoutMbr() != null) { TempPayoutMbrEntity
//				 * oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();
//				 * 
//				 * TempPayoutMbrEntity tempPayoutMbrEntity =
//				 * makeDublicatePayoutMbrDetails(oldPayoutMbrEntity, payoutEntity);
//				 * 
//				 * tempPayoutMbrEntity = tempPayoutDtlsRepository.save(tempPayoutMbrEntity);
//				 * 
//				 * insertPayoutAnuityCalc(oldPayoutMbrEntity, null, tempPayoutMbrEntity);
//				 * 
//				 * insertPayoutCommutationCalc(oldPayoutMbrEntity, null, tempPayoutMbrEntity);
//				 * 
//				 * insertPayoutAppointeeEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);
//				 * 
//				 * insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);
//				 * 
//				 * insertPayoutMbrAddressEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);
//				 * 
//				 * insertPayoutMbrBankEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);
//				 * 
//				 * insertPayoutFundValueEntity(oldPayoutMbrEntity, null, tempPayoutMbrEntity);
//				 * 
//				 * insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, null,
//				 * tempPayoutMbrEntity);
//				 * 
//				 * }
//				 */
//				oldTempPayoutEntity.setIsActive(Boolean.FALSE);
//				tempPayoutRepository.save(oldTempPayoutEntity);
//			}
//
//		}
//
//		return payoutEntity;
//	}

	@Override
	public TempPayoutMbrAppointeeEntity insertPayoutMbrAppointee(String payoutNo,
			TempPayoutMbrAppointeeEntity payoutAppointeeEntity) {
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrAppointee  {} ::{} start");
		try {
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempPayoutEntity oldTempPayoutEntity = optional.get();
				if (oldTempPayoutEntity != null) {
					TempPayoutEntity newPayoutEntity = makeDublicatePayoutEntity(oldTempPayoutEntity);
					newPayoutEntity = tempPayoutRepository.save(newPayoutEntity);
					if (oldTempPayoutEntity.getPayoutMbr() != null) {
						TempPayoutMbrEntity oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();

						TempPayoutMbrEntity newPayoutMbrEntity = makeDublicatePayoutMbrDetails(
								oldTempPayoutEntity.getPayoutMbr(), newPayoutEntity);

						newPayoutMbrEntity = tempPayoutDtlsRepository.save(newPayoutMbrEntity);

						insertPayoutAnuityCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutCommutationCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						payoutAppointeeEntity = insertPayoutAppointeeEntity(oldPayoutMbrEntity, payoutAppointeeEntity,
								newPayoutMbrEntity);

						insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrAddressEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrBankEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

					}

					oldTempPayoutEntity.setIsActive(Boolean.FALSE);
					tempPayoutRepository.save(oldTempPayoutEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrAppointee  {} ::{} error is "+e.getMessage());
		}
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrAppointee  {} ::{} ended");
		return payoutAppointeeEntity;
	}

	private TempPayoutMbrAppointeeEntity insertPayoutAppointeeEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrAppointeeEntity tempPayoutMbrAppointeeEntity, TempPayoutMbrEntity newPayoutMbrEntity) {
		if (tempPayoutMbrAppointeeEntity != null && tempPayoutMbrAppointeeEntity.getAppointeeId() != null
				&& tempPayoutMbrAppointeeEntity.getAppointeeId() > 0) {
			tempPayoutMbrAppointeeEntity = updatePayoutMbrAppointee(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutMbrAppointeeEntity);

		} else {
			tempPayoutMbrAppointeeEntity = insertPayoutMbrAppointee(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutMbrAppointeeEntity);
		}
		return tempPayoutMbrAppointeeEntity;
	}

	private TempPayoutMbrAppointeeEntity updatePayoutMbrAppointee(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutMbrAppointeeEntity tempPayoutAnnuityCalcEntity) {
		List<TempPayoutMbrAppointeeEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity
				.getPayoutMbrAppointeeDtls();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutMbrAppointeeEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				if (oldEntity != null
						&& oldEntity.getAppointeeId().equals(tempPayoutAnnuityCalcEntity.getAppointeeId())) {
					TempPayoutMbrAppointeeEntity newEntity = makeDuplicatePayoutMbrAppointeeEntity(
							tempPayoutAnnuityCalcEntity, newPayoutMbrEntity);
					tempPayoutAnnuityCalcEntity = tempPayoutMbrAppointeeRepository.save(newEntity);
				} else {
					TempPayoutMbrAppointeeEntity newEntity = makeDuplicatePayoutMbrAppointeeEntity(oldEntity,
							newPayoutMbrEntity);
					tempPayoutMbrAppointeeRepository.save(newEntity);
				}
			}
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutMbrAppointeeEntity insertPayoutMbrAppointee(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutMbrAppointeeEntity tempPayoutMbrAppointeeEntity) {
		List<TempPayoutMbrAppointeeEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity
				.getPayoutMbrAppointeeDtls();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutMbrAppointeeEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				TempPayoutMbrAppointeeEntity newEntity = makeDuplicatePayoutMbrAppointeeEntity(oldEntity,
						newPayoutMbrEntity);
				tempPayoutMbrAppointeeRepository.save(newEntity);
			}
		}
		if (tempPayoutMbrAppointeeEntity != null) {
			TempPayoutMbrAppointeeEntity newEntity = makeDuplicatePayoutMbrAppointeeEntity(tempPayoutMbrAppointeeEntity,
					newPayoutMbrEntity);
			tempPayoutMbrAppointeeEntity = tempPayoutMbrAppointeeRepository.save(newEntity);
		}
		return tempPayoutMbrAppointeeEntity;
	}

	private TempPayoutMbrAppointeeEntity makeDuplicatePayoutMbrAppointeeEntity(TempPayoutMbrAppointeeEntity oldEntity,
			TempPayoutMbrEntity newPayoutMbrEntity) {
		TempPayoutMbrAppointeeEntity newEntity = new TempPayoutMbrAppointeeEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setAppointeeId(null);
		newEntity.setPayoutMbrEntity(newPayoutMbrEntity);
		return newEntity;
	}

	@Override
	public TempPayoutMbrNomineeEntity insertPayoutMbrNominee(String payoutNo,
			TempPayoutMbrNomineeEntity payoutMbrNomineeEntity) {
		try {
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrNominee  {} ::{} start");	
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempPayoutEntity oldTempPayoutEntity = optional.get();
				if (oldTempPayoutEntity != null) {
					TempPayoutEntity newPayoutEntity = makeDublicatePayoutEntity(oldTempPayoutEntity);
					newPayoutEntity = tempPayoutRepository.save(newPayoutEntity);
					if (oldTempPayoutEntity.getPayoutMbr() != null) {
						TempPayoutMbrEntity oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();

						TempPayoutMbrEntity newPayoutMbrEntity = makeDublicatePayoutMbrDetails(
								oldTempPayoutEntity.getPayoutMbr(), newPayoutEntity);

						newPayoutMbrEntity = tempPayoutDtlsRepository.save(newPayoutMbrEntity);

						insertPayoutAnuityCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutCommutationCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutAppointeeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						payoutMbrNomineeEntity = insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, payoutMbrNomineeEntity,
								newPayoutMbrEntity);

						insertPayoutMbrAddressEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrBankEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

					}

					oldTempPayoutEntity.setIsActive(Boolean.FALSE);
					tempPayoutRepository.save(oldTempPayoutEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrNominee  {} ::{} error is"+e.getMessage());
		}
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrNominee  {} ::{} ended");
		return payoutMbrNomineeEntity;
	}

	private TempPayoutMbrNomineeEntity insertPayoutMbrNomineeEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrNomineeEntity tempPayoutMbrNomineeEntity, TempPayoutMbrEntity newPayoutMbrEntity) {
		if (tempPayoutMbrNomineeEntity != null && tempPayoutMbrNomineeEntity.getNomineeId() != null
				&& tempPayoutMbrNomineeEntity.getNomineeId() > 0) {

			tempPayoutMbrNomineeEntity = updatePayoutMbrNominee(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutMbrNomineeEntity);

		} else {
			tempPayoutMbrNomineeEntity = insertPayoutMbrNominee(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutMbrNomineeEntity);
		}
		return tempPayoutMbrNomineeEntity;
	}

	private TempPayoutMbrNomineeEntity updatePayoutMbrNominee(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutMbrNomineeEntity tempPayoutAnnuityCalcEntity) {
		List<TempPayoutMbrNomineeEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutMbrNomineeDtls();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutMbrNomineeEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getNomineeId().equals(tempPayoutAnnuityCalcEntity.getNomineeId())) {
					TempPayoutMbrNomineeEntity newEntity = makeDuplicatePayoutMbrNomineeEntity(
							tempPayoutAnnuityCalcEntity, newPayoutMbrEntity);
					tempPayoutAnnuityCalcEntity = tempPayoutMbrNomineeRepository.save(newEntity);
				} else {
					TempPayoutMbrNomineeEntity newEntity = makeDuplicatePayoutMbrNomineeEntity(oldEntity,
							newPayoutMbrEntity);
					tempPayoutMbrNomineeRepository.save(newEntity);
				}
			}
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutMbrNomineeEntity insertPayoutMbrNominee(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutMbrNomineeEntity tempPayoutMbrNomineeEntity) {
		List<TempPayoutMbrNomineeEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutMbrNomineeDtls();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutMbrNomineeEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				TempPayoutMbrNomineeEntity newEntity = makeDuplicatePayoutMbrNomineeEntity(oldEntity,
						newPayoutMbrEntity);
				tempPayoutMbrNomineeRepository.save(newEntity);
			}
		}
		if (tempPayoutMbrNomineeEntity != null) {
			TempPayoutMbrNomineeEntity newEntity = makeDuplicatePayoutMbrNomineeEntity(tempPayoutMbrNomineeEntity,
					newPayoutMbrEntity);
			tempPayoutMbrNomineeEntity = tempPayoutMbrNomineeRepository.save(newEntity);
		}
		return tempPayoutMbrNomineeEntity;
	}

	private TempPayoutMbrNomineeEntity makeDuplicatePayoutMbrNomineeEntity(TempPayoutMbrNomineeEntity oldEntity,
			TempPayoutMbrEntity newPayoutMbrEntity) {
		TempPayoutMbrNomineeEntity newEntity = new TempPayoutMbrNomineeEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setNomineeId(null);
		newEntity.setPayoutMbrEntity(newPayoutMbrEntity);
		return newEntity;
	}

	@Override
	public TempPayoutMbrAddressEntity insertPayoutMbrAddressEntity(String payoutNo,
			TempPayoutMbrAddressEntity tempPayoutMbrAddressEntity) {
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrAddressEntity  {} ::{} start");	
		try {
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempPayoutEntity oldTempPayoutEntity = optional.get();
				if (oldTempPayoutEntity != null) {
					TempPayoutEntity newPayoutEntity = makeDublicatePayoutEntity(oldTempPayoutEntity);
					newPayoutEntity = tempPayoutRepository.save(newPayoutEntity);
					if (oldTempPayoutEntity.getPayoutMbr() != null) {
						TempPayoutMbrEntity oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();

						TempPayoutMbrEntity newPayoutMbrEntity = makeDublicatePayoutMbrDetails(
								oldTempPayoutEntity.getPayoutMbr(), newPayoutEntity);

						newPayoutMbrEntity = tempPayoutDtlsRepository.save(newPayoutMbrEntity);

						insertPayoutAnuityCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutCommutationCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutAppointeeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						tempPayoutMbrAddressEntity = insertPayoutMbrAddressEntity(oldPayoutMbrEntity,
								tempPayoutMbrAddressEntity, newPayoutMbrEntity);

						insertPayoutMbrBankEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

					}

					oldTempPayoutEntity.setIsActive(Boolean.FALSE);
					tempPayoutRepository.save(oldTempPayoutEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrAddressEntity  {} ::{} error is "+e.getMessage());
		}
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrAddressEntity  {} ::{} ended");
		return tempPayoutMbrAddressEntity;
	}

	private TempPayoutMbrAddressEntity insertPayoutMbrAddressEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrAddressEntity tempPayoutMbrAddressEntity, TempPayoutMbrEntity newPayoutMbrEntity) {
		if (tempPayoutMbrAddressEntity != null && tempPayoutMbrAddressEntity.getAddressId() != null
				&& tempPayoutMbrAddressEntity.getAddressId() > 0) {

			tempPayoutMbrAddressEntity = updatePayoutMbrAddress(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutMbrAddressEntity);

		} else {
			tempPayoutMbrAddressEntity = insertPayoutMbrAddress(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutMbrAddressEntity);
		}
		return tempPayoutMbrAddressEntity;
	}

	private TempPayoutMbrAddressEntity updatePayoutMbrAddress(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutMbrAddressEntity tempPayoutAnnuityCalcEntity) {
		List<TempPayoutMbrAddressEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutMbrAddresses();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutMbrAddressEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getAddressId().equals(tempPayoutAnnuityCalcEntity.getAddressId())) {
					TempPayoutMbrAddressEntity newEntity = makeDuplicatePayoutMbrAddressEntity(
							tempPayoutAnnuityCalcEntity, newPayoutMbrEntity);
					tempPayoutAnnuityCalcEntity = tempPayoutMbrAddressRepository.save(newEntity);
				} else {
					TempPayoutMbrAddressEntity newEntity = makeDuplicatePayoutMbrAddressEntity(oldEntity,
							newPayoutMbrEntity);
					tempPayoutMbrAddressRepository.save(newEntity);
				}
			}
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutMbrAddressEntity insertPayoutMbrAddress(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutMbrAddressEntity tempPayoutMbrAddressEntity) {
		List<TempPayoutMbrAddressEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutMbrAddresses();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutMbrAddressEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				TempPayoutMbrAddressEntity newEntity = makeDuplicatePayoutMbrAddressEntity(oldEntity,
						newPayoutMbrEntity);
				tempPayoutMbrAddressRepository.save(newEntity);
			}
		}
		if (tempPayoutMbrAddressEntity != null) {
			TempPayoutMbrAddressEntity newEntity = makeDuplicatePayoutMbrAddressEntity(tempPayoutMbrAddressEntity,
					newPayoutMbrEntity);
			tempPayoutMbrAddressEntity = tempPayoutMbrAddressRepository.save(newEntity);
		}
		return tempPayoutMbrAddressEntity;
	}

	private TempPayoutMbrAddressEntity makeDuplicatePayoutMbrAddressEntity(TempPayoutMbrAddressEntity oldEntity,
			TempPayoutMbrEntity newPayoutMbrEntity) {
		TempPayoutMbrAddressEntity newEntity = new TempPayoutMbrAddressEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setAddressId(null);
		newEntity.setPayoutMbrEntity(newPayoutMbrEntity);
		return newEntity;
	}

	@Override
	public TempPayoutMbrBankDetailEntity insertPayoutMbrBankEntity(String payoutNo,
			TempPayoutMbrBankDetailEntity tempPayoutMbrBankDetailEntity) {
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrBankEntity  {} ::{} start");
		try {
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempPayoutEntity oldTempPayoutEntity = optional.get();
				if (oldTempPayoutEntity != null) {
					TempPayoutEntity newPayoutEntity = makeDublicatePayoutEntity(oldTempPayoutEntity);
					newPayoutEntity = tempPayoutRepository.save(newPayoutEntity);
					if (oldTempPayoutEntity.getPayoutMbr() != null) {
						TempPayoutMbrEntity oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();

						TempPayoutMbrEntity newPayoutMbrEntity = makeDublicatePayoutMbrDetails(
								oldTempPayoutEntity.getPayoutMbr(), newPayoutEntity);

						newPayoutMbrEntity = tempPayoutDtlsRepository.save(newPayoutMbrEntity);

						insertPayoutAnuityCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutCommutationCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutAppointeeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrAddressEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						tempPayoutMbrBankDetailEntity = insertPayoutMbrBankEntity(oldPayoutMbrEntity,
								tempPayoutMbrBankDetailEntity, newPayoutMbrEntity);

					}

					oldTempPayoutEntity.setIsActive(Boolean.FALSE);
					tempPayoutRepository.save(oldTempPayoutEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrBankEntity  {} ::{} error is "+e.getMessage());
		}
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrBankEntity  {} ::{} ended");
		return tempPayoutMbrBankDetailEntity;
	}

	private TempPayoutMbrBankDetailEntity insertPayoutMbrBankEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrBankDetailEntity tempPayoutMbrBankEntity, TempPayoutMbrEntity newPayoutMbrEntity) {
		if (tempPayoutMbrBankEntity != null && tempPayoutMbrBankEntity.getBankId() != null
				&& tempPayoutMbrBankEntity.getBankId() > 0) {

			tempPayoutMbrBankEntity = updatePayoutMbrBank(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutMbrBankEntity);

		} else {
			tempPayoutMbrBankEntity = insertPayoutMbrBank(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutMbrBankEntity);
		}
		return tempPayoutMbrBankEntity;
	}

	private TempPayoutMbrBankDetailEntity updatePayoutMbrBank(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutMbrBankDetailEntity tempPayoutAnnuityCalcEntity) {
		List<TempPayoutMbrBankDetailEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity
				.getPayoutMbrBankDetails();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutMbrBankDetailEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				if (oldEntity != null && oldEntity.getBankId().equals(tempPayoutAnnuityCalcEntity.getBankId())) {
					TempPayoutMbrBankDetailEntity newEntity = makeDuplicatePayoutMbrBankEntity(
							tempPayoutAnnuityCalcEntity, newPayoutMbrEntity);
					tempPayoutAnnuityCalcEntity = tempPayoutMbrBankDtlsRepository.save(newEntity);
				} else {
					TempPayoutMbrBankDetailEntity newEntity = makeDuplicatePayoutMbrBankEntity(oldEntity,
							newPayoutMbrEntity);
					tempPayoutMbrBankDtlsRepository.save(newEntity);
				}
			}
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutMbrBankDetailEntity insertPayoutMbrBank(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutMbrBankDetailEntity tempPayoutMbrBankEntity) {
		List<TempPayoutMbrBankDetailEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity
				.getPayoutMbrBankDetails();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutMbrBankDetailEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				TempPayoutMbrBankDetailEntity newEntity = makeDuplicatePayoutMbrBankEntity(oldEntity,
						newPayoutMbrEntity);
				tempPayoutMbrBankDtlsRepository.save(newEntity);
			}
		}
		if (tempPayoutMbrBankEntity != null) {
			TempPayoutMbrBankDetailEntity newEntity = makeDuplicatePayoutMbrBankEntity(tempPayoutMbrBankEntity,
					newPayoutMbrEntity);
			tempPayoutMbrBankEntity = tempPayoutMbrBankDtlsRepository.save(newEntity);
		}
		return tempPayoutMbrBankEntity;
	}

	private TempPayoutMbrBankDetailEntity makeDuplicatePayoutMbrBankEntity(TempPayoutMbrBankDetailEntity oldEntity,
			TempPayoutMbrEntity newPayoutMbrEntity) {
		TempPayoutMbrBankDetailEntity newEntity = new TempPayoutMbrBankDetailEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setBankId(null);
		newEntity.setPayoutMbrEntity(newPayoutMbrEntity);
		return newEntity;
	}

	@Override
	public TempPayoutFundValueEntity insertPayoutFundValue(String payoutNo, TempPayoutFundValueEntity entity) {
		try {
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutFundValue  {} ::{} start");
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempPayoutEntity oldTempPayoutEntity = optional.get();
				if (oldTempPayoutEntity != null) {
					TempPayoutEntity newPayoutEntity = makeDublicatePayoutEntity(oldTempPayoutEntity);
					newPayoutEntity = tempPayoutRepository.save(newPayoutEntity);
					if (oldTempPayoutEntity.getPayoutMbr() != null) {
						TempPayoutMbrEntity oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();

						TempPayoutMbrEntity newPayoutMbrEntity = makeDublicatePayoutMbrDetails(
								oldTempPayoutEntity.getPayoutMbr(), newPayoutEntity);

						newPayoutMbrEntity = tempPayoutDtlsRepository.save(newPayoutMbrEntity);

						insertPayoutAnuityCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutCommutationCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutAppointeeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrAddressEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrBankEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						entity = insertPayoutFundValueEntity(oldPayoutMbrEntity, entity, newPayoutMbrEntity);

					}

					oldTempPayoutEntity.setIsActive(Boolean.FALSE);
					tempPayoutRepository.save(oldTempPayoutEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutFundValue  {} ::{} error is "+e.getMessage());	
		}
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutFundValue  {} ::{} ended");
		return entity;
	}

	private TempPayoutFundValueEntity insertPayoutFundValueEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutFundValueEntity tempPayoutFundValueEntity, TempPayoutMbrEntity newPayoutMbrEntity) {
		if (tempPayoutFundValueEntity != null && tempPayoutFundValueEntity.getFundValueId() != null
				&& tempPayoutFundValueEntity.getFundValueId() > 0) {

			tempPayoutFundValueEntity = updatePayoutFundValueEntity(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutFundValueEntity);

		} else {
			tempPayoutFundValueEntity = insertPayoutFundValueEntity(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutFundValueEntity);
		}
		return tempPayoutFundValueEntity;
	}

	private TempPayoutFundValueEntity updatePayoutFundValueEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutFundValueEntity tempPayoutAnnuityCalcEntity) {
		List<TempPayoutFundValueEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutFundValue();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutFundValueEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				if (oldEntity != null
						&& oldEntity.getFundValueId().equals(tempPayoutAnnuityCalcEntity.getFundValueId())) {
					TempPayoutFundValueEntity newEntity = makeDuplicatePayoutFundValueEntity(
							tempPayoutAnnuityCalcEntity, newPayoutMbrEntity);
					tempPayoutAnnuityCalcEntity = tempPayoutFundValueRepository.save(newEntity);
				} else {
					TempPayoutFundValueEntity newEntity = makeDuplicatePayoutFundValueEntity(oldEntity,
							newPayoutMbrEntity);
					tempPayoutFundValueRepository.save(newEntity);
				}
			}
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutFundValueEntity insertPayoutFundValueEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutFundValueEntity tempPayoutFundValueEntity) {
		List<TempPayoutFundValueEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutFundValue();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutFundValueEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				TempPayoutFundValueEntity newEntity = makeDuplicatePayoutFundValueEntity(oldEntity, newPayoutMbrEntity);
				tempPayoutFundValueRepository.save(newEntity);
			}
		}
		if (tempPayoutFundValueEntity != null) {
			TempPayoutFundValueEntity newEntity = makeDuplicatePayoutFundValueEntity(tempPayoutFundValueEntity,
					newPayoutMbrEntity);
			tempPayoutFundValueEntity = tempPayoutFundValueRepository.save(newEntity);
		}
		return tempPayoutFundValueEntity;
	}

	private TempPayoutFundValueEntity makeDuplicatePayoutFundValueEntity(TempPayoutFundValueEntity oldEntity,
			TempPayoutMbrEntity newPayoutMbrEntity) {
		TempPayoutFundValueEntity newEntity = new TempPayoutFundValueEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setFundValueId(null);
		newEntity.setPayoutMbrEntity(newPayoutMbrEntity);
		return newEntity;
	}

	@Override
	public TempPayoutMbrFundValueEntity insertPayoutMbrFundValue(String payoutNo, TempPayoutMbrFundValueEntity entity) {
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrFundValue  {} ::{} start");
		try {
			Optional<TempPayoutEntity> optional = tempPayoutRepository.findByPayoutNoAndIsActive(payoutNo, Boolean.TRUE);
			if (optional.isPresent()) {
				TempPayoutEntity oldTempPayoutEntity = optional.get();
				if (oldTempPayoutEntity != null) {
					TempPayoutEntity newPayoutEntity = makeDublicatePayoutEntity(oldTempPayoutEntity);
					newPayoutEntity = tempPayoutRepository.save(newPayoutEntity);
					if (oldTempPayoutEntity.getPayoutMbr() != null) {
						TempPayoutMbrEntity oldPayoutMbrEntity = oldTempPayoutEntity.getPayoutMbr();

						TempPayoutMbrEntity newPayoutMbrEntity = makeDublicatePayoutMbrDetails(
								oldTempPayoutEntity.getPayoutMbr(), newPayoutEntity);

						newPayoutMbrEntity = tempPayoutDtlsRepository.save(newPayoutMbrEntity);

						insertPayoutAnuityCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutCommutationCalc(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutAppointeeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrNomineeEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrAddressEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutMbrBankEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						insertPayoutFundValueEntity(oldPayoutMbrEntity, null, newPayoutMbrEntity);

						entity = insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, entity, newPayoutMbrEntity);

					}

					oldTempPayoutEntity.setIsActive(Boolean.FALSE);
					tempPayoutRepository.save(oldTempPayoutEntity);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrFundValue  {} ::{} error is "+e.getMessage());	
		}
		logger.info("TempSavePayoutServiceImpl {}::{} insertPayoutMbrFundValue  {} ::{} ended");
		return entity;
	}

	private TempPayoutMbrFundValueEntity insertPayoutMbrFundValueEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrFundValueEntity tempPayoutMbrFundValueEntity, TempPayoutMbrEntity newPayoutMbrEntity) {
		if (tempPayoutMbrFundValueEntity != null && tempPayoutMbrFundValueEntity.getFundValueId() != null
				&& tempPayoutMbrFundValueEntity.getFundValueId() > 0) {

			tempPayoutMbrFundValueEntity = updatePayoutMbrFundValueEntity(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutMbrFundValueEntity);

		} else {
			tempPayoutMbrFundValueEntity = insertPayoutMbrFundValueEntity(oldPayoutMbrEntity, newPayoutMbrEntity,
					tempPayoutMbrFundValueEntity);
		}
		return tempPayoutMbrFundValueEntity;
	}

	private TempPayoutMbrFundValueEntity updatePayoutMbrFundValueEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutMbrFundValueEntity tempPayoutAnnuityCalcEntity) {
		List<TempPayoutMbrFundValueEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutMbrFundValue();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutMbrFundValueEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				if (oldEntity != null
						&& oldEntity.getFundValueId().equals(tempPayoutAnnuityCalcEntity.getFundValueId())) {
					TempPayoutMbrFundValueEntity newEntity = makeDuplicatePayoutMbrFundValueEntity(
							tempPayoutAnnuityCalcEntity, newPayoutMbrEntity);
					tempPayoutAnnuityCalcEntity = tempPayoutMbrFundValueRepository.save(newEntity);
				} else {
					TempPayoutMbrFundValueEntity newEntity = makeDuplicatePayoutMbrFundValueEntity(oldEntity,
							newPayoutMbrEntity);
					tempPayoutMbrFundValueRepository.save(newEntity);
				}
			}
		}
		return tempPayoutAnnuityCalcEntity;
	}

	private TempPayoutMbrFundValueEntity insertPayoutMbrFundValueEntity(TempPayoutMbrEntity oldPayoutMbrEntity,
			TempPayoutMbrEntity newPayoutMbrEntity, TempPayoutMbrFundValueEntity tempPayoutMbrFundValueEntity) {
		List<TempPayoutMbrFundValueEntity> tempPayoutAnnuityCalcEntities = oldPayoutMbrEntity.getPayoutMbrFundValue();
		if (tempPayoutAnnuityCalcEntities != null && !tempPayoutAnnuityCalcEntities.isEmpty()) {
			for (TempPayoutMbrFundValueEntity oldEntity : tempPayoutAnnuityCalcEntities) {
				TempPayoutMbrFundValueEntity newEntity = makeDuplicatePayoutMbrFundValueEntity(oldEntity,
						newPayoutMbrEntity);
				tempPayoutMbrFundValueRepository.save(newEntity);
			}
		}
		if (tempPayoutMbrFundValueEntity != null) {
			TempPayoutMbrFundValueEntity newEntity = makeDuplicatePayoutMbrFundValueEntity(tempPayoutMbrFundValueEntity,
					newPayoutMbrEntity);
			tempPayoutMbrFundValueEntity = tempPayoutMbrFundValueRepository.save(newEntity);
		}
		return tempPayoutMbrFundValueEntity;
	}

	private TempPayoutMbrFundValueEntity makeDuplicatePayoutMbrFundValueEntity(TempPayoutMbrFundValueEntity oldEntity,
			TempPayoutMbrEntity newPayoutMbrEntity) {
		TempPayoutMbrFundValueEntity newEntity = new TempPayoutMbrFundValueEntity();
		BeanUtils.copyProperties(oldEntity, newEntity);
		newEntity.setFundValueId(null);
		newEntity.setPayoutMbrEntity(newPayoutMbrEntity);
		return newEntity;
	}

}
