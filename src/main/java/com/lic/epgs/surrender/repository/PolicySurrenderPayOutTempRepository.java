package com.lic.epgs.surrender.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.surrender.entity.PolicySurrenderPayOutTempEntity;
import com.lic.epgs.surrender.entity.PolicySurrenderTempEntity;

@Repository
public interface PolicySurrenderPayOutTempRepository  extends JpaRepository<PolicySurrenderPayOutTempEntity, Long>{
	

	Optional<PolicySurrenderTempEntity> findBySurrenderId(Long mergeId);

    List<PolicySurrenderPayOutTempEntity> findAllBySurrenderId(Long surrenderId);
    
    PolicySurrenderPayOutTempEntity  findBySurrenderIdAndIsActiveTrue(Long surrenderId);
    

}
