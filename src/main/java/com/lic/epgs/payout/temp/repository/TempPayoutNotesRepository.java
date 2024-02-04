package com.lic.epgs.payout.temp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.temp.entity.TempPayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutNotesEntity;

@Repository
public interface TempPayoutNotesRepository
		extends JpaRepository<TempPayoutNotesEntity, Long>, JpaSpecificationExecutor<TempPayoutNotesEntity> {

	List<TempPayoutNotesEntity> findByPayoutNo(String payoutNo);

	List<TempPayoutNotesEntity> findByPayout(TempPayoutEntity tempPayoutEntity);
}
