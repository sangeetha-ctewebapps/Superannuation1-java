package com.lic.epgs.claim.temp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.TempClaimCommutationCalcEntity;

@Repository
public interface TempClaimCommutationCalcRepository extends JpaRepository<TempClaimCommutationCalcEntity, Long>,
		JpaSpecificationExecutor<TempClaimCommutationCalcEntity> {

	TempClaimCommutationCalcEntity findByNomineeCode(String nomineeCode);

	@Query(value ="SELECT commutation_amt, tds_amt FROM temp_claim_commutation tpc where claim_member_id =:claimMemberId",nativeQuery=true)
	List<Object[]> getCommutatinDetailsConfigMaster(@Param("claimMemberId") Long memberId);

}
