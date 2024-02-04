package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonClaimReasonforexitEntity;
@Repository
public interface CommonClaimReasonforexitRepository extends JpaRepository<CommonClaimReasonforexitEntity,Long> {

	List<CommonClaimReasonforexitEntity> findAllByStatusTrue();

}
