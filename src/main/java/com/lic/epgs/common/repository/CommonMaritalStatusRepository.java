package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonMaritalStatusEntity;

@Repository
public interface CommonMaritalStatusRepository extends JpaRepository<CommonMaritalStatusEntity, Long> {

	List<CommonMaritalStatusEntity> findAllByStatusTrue();

}
