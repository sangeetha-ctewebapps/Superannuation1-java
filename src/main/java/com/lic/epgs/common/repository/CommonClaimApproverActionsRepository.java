package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonClaimApproverActionsEntity;
@Repository
public interface CommonClaimApproverActionsRepository extends JpaRepository<CommonClaimApproverActionsEntity,Long> {

	List<CommonClaimApproverActionsEntity> findAllByStatusTrue();

}
