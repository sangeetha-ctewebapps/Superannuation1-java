package com.lic.epgs.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.common.entity.AppLogErrorEntity;

public interface AppLogErrorRepository extends JpaRepository<AppLogErrorEntity, Integer> {

}
