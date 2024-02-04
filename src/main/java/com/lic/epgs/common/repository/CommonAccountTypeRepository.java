package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonAccountTypeEntity;
@Repository
public interface CommonAccountTypeRepository extends JpaRepository<CommonAccountTypeEntity, Long> {

	List<CommonAccountTypeEntity> findAllByStatusTrue();

}
