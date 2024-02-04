package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.FundedCommonMasterEntity;

@Repository
public interface FundedCommonMasterRepository extends JpaRepository<FundedCommonMasterEntity, Long> {
	
	List<FundedCommonMasterEntity> findAllByCodeIdAndIsActiveTrue(Long codeId);

}
