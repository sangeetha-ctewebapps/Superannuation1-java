package com.lic.epgs.regularadjustmentcontribution.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionNotesTempEntity;

@Repository
public interface RegularAdjustmentContributionNotesTempRepository extends JpaRepository<RegularAdjustmentContributionNotesTempEntity, Long>,
JpaSpecificationExecutor<RegularAdjustmentContributionNotesTempEntity> {

	
	List<RegularAdjustmentContributionNotesTempEntity> findByregularContributionId(Long regularContributionId);


	
	

}