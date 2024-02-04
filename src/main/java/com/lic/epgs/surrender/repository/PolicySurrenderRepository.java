package com.lic.epgs.surrender.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.surrender.entity.PolicySurrenderEntity;

@Repository
public interface  PolicySurrenderRepository extends JpaRepository<PolicySurrenderEntity , Long> {

	PolicySurrenderEntity findBySurrenderIdAndIsActiveTrue(Long mergeId);

	List<PolicySurrenderEntity> findAllBySurrenderStatusInAndIsActiveTrueOrderBySurrenderId(
			List<String> existingChecker);

}
