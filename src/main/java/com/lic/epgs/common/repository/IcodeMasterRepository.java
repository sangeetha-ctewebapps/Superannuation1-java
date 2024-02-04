package com.lic.epgs.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.IcodeMasterEntity;

@Repository
public interface IcodeMasterRepository extends JpaRepository<IcodeMasterEntity, Long> {
	
	IcodeMasterEntity findByVariant(String variant);

	IcodeMasterEntity findByIcodeForVariant(String variant);
}
