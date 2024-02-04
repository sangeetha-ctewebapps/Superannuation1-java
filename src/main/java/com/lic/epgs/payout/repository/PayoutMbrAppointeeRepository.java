package com.lic.epgs.payout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.entity.PayoutMbrAppointeeEntity;

@Repository
public interface PayoutMbrAppointeeRepository extends JpaRepository<PayoutMbrAppointeeEntity, Long>,
		JpaSpecificationExecutor<PayoutMbrAppointeeEntity> {

	PayoutMbrAppointeeEntity findByAppointeeId(Long appointeeId);

}
