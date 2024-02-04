package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonClaimCommutationCalcEntity;

@Repository
public interface CommonClaimCommutationCalcRepository extends JpaRepository<CommonClaimCommutationCalcEntity, Long> {

	List<CommonClaimCommutationCalcEntity> findAllByStatusTrue();

}
