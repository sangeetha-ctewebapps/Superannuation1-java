package com.lic.epgs.payout.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.temp.entity.TempPayoutMbrAppointeeEntity;

@Repository
public interface TempPayoutMbrAppointeeRepository extends JpaRepository<TempPayoutMbrAppointeeEntity, Long>,
		JpaSpecificationExecutor<TempPayoutMbrAppointeeEntity> {

	TempPayoutMbrAppointeeEntity findByAppointeeId(Long appointeeId);

}
