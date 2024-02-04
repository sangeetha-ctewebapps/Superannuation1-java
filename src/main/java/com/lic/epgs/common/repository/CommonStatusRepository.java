package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonStatusEntity;

@Repository
public interface CommonStatusRepository extends JpaRepository<CommonStatusEntity, String> {

	List<CommonStatusEntity> findAllByIsActiveTrue();


	
}
