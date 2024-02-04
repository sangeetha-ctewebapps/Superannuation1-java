package com.lic.epgs.payout.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.temp.entity.TempPayoutMbrNomineeEntity;

@Repository
public interface TempPayoutMbrNomineeRepository
		extends JpaRepository<TempPayoutMbrNomineeEntity, Long>, JpaSpecificationExecutor<TempPayoutMbrNomineeEntity> {

	TempPayoutMbrNomineeEntity findByParentNomineeId(Long nomineeId);

}
