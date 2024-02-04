package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonClaimMakerActionsEntity;
@Repository
public interface CommonClaimMakerActionsRepository extends JpaRepository<CommonClaimMakerActionsEntity,Long> {

	List<CommonClaimMakerActionsEntity> findAllByStatusTrue();

}
