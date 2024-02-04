package com.lic.epgs.payout.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.payout.temp.entity.TempPayoutMbrAddressEntity;

@Repository
public interface TempPayoutMbrAddressRepository extends JpaRepository<TempPayoutMbrAddressEntity, Long>,
		JpaSpecificationExecutor<TempPayoutMbrAddressEntity> {

}
