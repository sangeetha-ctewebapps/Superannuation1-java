package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonClaimRequiredDocumentEntity;

@Repository
public interface CommonClaimRequiredDocumentRepository extends JpaRepository<CommonClaimRequiredDocumentEntity,Long> {

	List<CommonClaimRequiredDocumentEntity> findAllByStatusTrue();


}
