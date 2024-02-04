package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommoclaimMemberUploadFileTypeEntity;

@Repository
public interface CommonclaimMemberUploadFileTypeRepository extends JpaRepository<CommoclaimMemberUploadFileTypeEntity,Long> {

	List<CommoclaimMemberUploadFileTypeEntity> findAllByStatusTrue();


}
