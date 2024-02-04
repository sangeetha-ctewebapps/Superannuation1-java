package com.lic.epgs.claim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.entity.ClaimEntity;

@Repository
public interface ClaimRepository extends JpaRepository<ClaimEntity, Long>, JpaSpecificationExecutor<ClaimEntity> {

	Optional<ClaimEntity> findByClaimNoAndIsActive(String claimNo, Boolean isActive);

	Optional<ClaimEntity> findByClaimOnboardingAndIsActive(String claimBoardingNo, Boolean true1);

	@Query(value="select co.claim_onboard_no from claim c join claim_onborading co on c.claim_onboard_id=co.claim_onboard_id where c.claim_no=?1 and c.is_active=?2",nativeQuery=true)
	String fetchByClaimNoAndIsActive(@Param ("claimNo") String claimNo,@Param ("true1") Boolean true1);
	
	@Query(value="select lmu.description  from liccommon.master_unit lmu where lmu.unit_code=?1",nativeQuery = true)
	String  getUnitName(String unitCod);

	@Query(value = "SELECT c.mode_of_exit FROM claim c "
			+ "join claim_onborading co "
			+ "on c.claim_onboard_id= co.claim_onboard_id "
			+ "WHERE "
			+ "co.claim_onboard_no = ?1 AND c.is_active = 1" ,nativeQuery=true)
	List<Object[]> getModeOfExistFromClaim(String claimOnBoardNo);

	@Query(value = "SELECT * FROM claim c "
			+ "join claim_onborading co "
			+ "on c.claim_onboard_id= co.claim_onboard_id "
			+ "WHERE "
			+ "co.claim_onboard_no = ?1 AND c.is_active = 1" ,nativeQuery=true)
	ClaimEntity getClaimDetails(String claimOnBoardNo);


}
