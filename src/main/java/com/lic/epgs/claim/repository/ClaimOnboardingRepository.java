package com.lic.epgs.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.entity.ClaimOnboardingEntity;

@Repository
public interface ClaimOnboardingRepository extends JpaRepository<ClaimOnboardingEntity, Long> {
	
	ClaimOnboardingEntity findByClaimOnBoardNo(String claimOnBoardNo);

	@Query(value="select ob.CLAIM_ONBOARD_NO from CLAIM_ONBORADING ob JOIN CLAIM cl ON cl.CLAIM_ONBOARD_ID  = ob.CLAIM_ONBOARD_ID where cl.CLAIM_NO = ?1 AND cl.IS_ACTIVE = '1' ",nativeQuery=true)
	String getClaimOnboardNo(String claimNo);

	
}
