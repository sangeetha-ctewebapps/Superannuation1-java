package com.lic.epgs.surrender.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lic.epgs.surrender.entity.PolicySurrenderTempEntity;

@Repository
public interface PolicySurrenderTempRepository  extends JpaRepository<PolicySurrenderTempEntity, Long>{
	
//	PolicySurrenderTempEntity findBySurrenderID(Long mergeId);

	Optional<PolicySurrenderTempEntity> findBySurrenderIdAndIsActiveTrue(Long mergeId);
	
	List<?> findAllBySurrenderStatusInAndIsActiveTrueOrderBySurrenderId(List<String> inprogressMaker);

	Optional<PolicySurrenderTempEntity> findAllBySurrenderIdAndIsActiveTrue(Long surrenderId);

	
	@Query(value = "SELECT * FROM POLICY_SURRENDER_TMP WHERE POLICY_ID=:policyId ", nativeQuery = true)
	List<PolicySurrenderTempEntity> findByPolicyIdAndIsActiveTrue(Long policyId);


}
