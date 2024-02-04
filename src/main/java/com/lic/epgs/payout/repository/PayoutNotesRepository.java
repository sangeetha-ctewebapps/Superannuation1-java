package com.lic.epgs.payout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.entity.PayoutNotesEntity;

@Repository
public interface PayoutNotesRepository
		extends JpaRepository<PayoutNotesEntity, Long>, JpaSpecificationExecutor<PayoutNotesEntity> {

	List<PayoutNotesEntity> findByPayoutNo(String payoutNo);

}
