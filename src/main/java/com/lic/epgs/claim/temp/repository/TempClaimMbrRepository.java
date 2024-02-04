package com.lic.epgs.claim.temp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.TempClaimMbrEntity;

@Repository
public interface TempClaimMbrRepository
		extends JpaRepository<TempClaimMbrEntity, Long>, JpaSpecificationExecutor<TempClaimMbrEntity> {
   
	List<TempClaimMbrEntity> findAllByClaimNoAndIsActiveTrue(String claimNo);

	TempClaimMbrEntity findByClaimNoAndIsActiveTrue(String claimNo);

	TempClaimMbrEntity findByMemberIdAndIsActiveTrue(Long memberId);
	@Query(value="SELECT *  FROM   TEMP_CLAIM_MBR TCM  JOIN  TEMP_CLAIM TC ON TC.CLAIM_ID =TCM.CLAIM_ID \r\n"
			+ "WHERE TCM .LIC_ID =?1 \r\n"
			+ "AND TC.MASTER_POLICY_NO =?2  AND TC.CLAIM_STATUS=?3 AND TC.IS_ACTIVE =1",nativeQuery=true)
	TempClaimMbrEntity checkInitimationMemberExit(String  licId,String policyNumber,Integer claimStatus);

	
	@Query(value = "SELECT tcm.LIC_ID  FROM TEMP_CLAIM_MBR tcm JOIN TEMP_CLAIM tc ON TCM .CLAIM_ID =tc.CLAIM_ID  WHERE\r\n"
			+ "	tcm.CLAIM_ID =?1 AND tc.IS_ACTIVE =1",nativeQuery=true)
	String getLicId(Long claimId);

	TempClaimMbrEntity findByMemberId(Long memberId);
	
	
}
