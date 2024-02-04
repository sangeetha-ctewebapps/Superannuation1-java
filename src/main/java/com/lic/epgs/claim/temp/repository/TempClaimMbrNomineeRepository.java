package com.lic.epgs.claim.temp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.TempClaimMbrNomineeEntity;

@Repository
public interface TempClaimMbrNomineeRepository
		extends JpaRepository<TempClaimMbrNomineeEntity, Long>, JpaSpecificationExecutor<TempClaimMbrNomineeEntity> {
	
	 
	  @Query(value = "SELECT FIRST_NAME From TEMP_CLAIM_MBR_NOMINEE where CLAIM_NO = ?1 AND CLAIMANT_TYPE = ?2", nativeQuery = true)
     List<String> getAppointeeNameList(@Param("claimNo") String cliamNo,@Param("claimType") String claimType);

	TempClaimMbrNomineeEntity findByNomineeCode(String nomineeCode);

	TempClaimMbrNomineeEntity findByNomineeId(Long nomineeId);

}
