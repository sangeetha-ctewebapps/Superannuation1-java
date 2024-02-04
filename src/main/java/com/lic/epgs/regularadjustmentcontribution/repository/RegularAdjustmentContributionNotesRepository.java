package com.lic.epgs.regularadjustmentcontribution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionNotesEntity;

public interface RegularAdjustmentContributionNotesRepository  extends JpaRepository<RegularAdjustmentContributionNotesEntity, Long>,
JpaSpecificationExecutor<RegularAdjustmentContributionNotesEntity>  {

	List<RegularAdjustmentContributionNotesEntity> findByregularContributionId(Long regularContributionId);


}
