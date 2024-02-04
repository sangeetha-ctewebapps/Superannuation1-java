package com.lic.epgs.payout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.entity.PayoutMbrAddressEntity;

@Repository
public interface PayoutMbrAddressRepository extends JpaRepository<PayoutMbrAddressEntity, Long>,
		JpaSpecificationExecutor<PayoutMbrAddressEntity> {

}
