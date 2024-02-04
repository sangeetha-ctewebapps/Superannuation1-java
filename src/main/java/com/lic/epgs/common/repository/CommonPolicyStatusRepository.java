package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonPolicyStatusEntity;

@Repository
public interface CommonPolicyStatusRepository extends JpaRepository<CommonPolicyStatusEntity, Long> {

	List<CommonPolicyStatusEntity> findAllByStatusTrue();

}
