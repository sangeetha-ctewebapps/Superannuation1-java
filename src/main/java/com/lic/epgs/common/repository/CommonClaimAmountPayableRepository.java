package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonClaimAmountPayableEntity;

@Repository
public interface CommonClaimAmountPayableRepository extends JpaRepository<CommonClaimAmountPayableEntity, Long> {

	List<CommonClaimAmountPayableEntity> findAllByStatusTrue();

}
