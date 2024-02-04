package com.lic.epgs.claim.temp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.TempClaimEntity;

@Repository
public interface TempClaimRepository
		extends JpaRepository<TempClaimEntity, Long>, JpaSpecificationExecutor<TempClaimEntity> {

	Optional<TempClaimEntity> findByClaimNoAndIsActive(String claimNo, Boolean isActive);

	Optional<TempClaimEntity> findByClaimNoAndIsActiveTrue(String claimNo);

	TempClaimEntity findAllByClaimNoAndIsActiveTrue(String claimNo);

	TempClaimEntity findTopByClaimNoOrderByClaimIdDesc(String claimNo);

	Optional<TempClaimEntity> findByClaimIdAndIsActiveTrue(Long claimId);
	
	@Query(value="select cbd.customer_lei from policy_master pm \r\n"
			+ "join mph_master mm on mm.mph_id =pm.mph_id\r\n"
			+ "join LICCUSTOMERCOMMON.proposal_basic_detail pbd on pbd.proposal_id=mm.proposal_id\r\n"
			+ "join LICCUSTOMERCOMMON.customer_basic_detail cbd on cbd.customer_id = pbd.customer_id \r\n"
			+ "where pm.mph_id=:mphId and pm.is_active=1  and pbd.is_active=1  and cbd.is_active=1  and  order by pm.modified_on FETCH FIRST 1 ROWS only",nativeQuery=true)
	String fetchLEInumberByMphId(Long mphId);

//	@Query(value="SELECT  * FROM TEMP_CLAIM_MBR tcm   JOIN TEMP_CLAIM tc  ON (tc.CLAIM_ID  =tcm.CLAIM_ID) WHERE pm.POLICY_NUMBER =:policyNo and pm.LIC_ID =:unitCode and pm.IS_ACTIVE=:isActive",nativeQuery=true)
//	TempClaimMbrEntity findOutTempClaim(String licId, String policyNo, Boolean true1);

}
