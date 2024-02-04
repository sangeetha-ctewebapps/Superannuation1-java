package com.lic.epgs.claim.temp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.TempClaimOnboardingEntity;

@Repository
public interface TempClaimOnboardingRepository extends JpaRepository<TempClaimOnboardingEntity, Long> {

	TempClaimOnboardingEntity findByClaimOnBoardNo(String claimOnBoardNo);

}
