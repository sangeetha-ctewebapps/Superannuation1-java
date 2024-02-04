package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.common.entity.CommonDocsEntity;

public interface CommonDocsRepository extends JpaRepository<CommonDocsEntity, Integer> {

	List<CommonDocsEntity> findAllByQuotationIdAndIsActiveTrue(Integer quotationId);

	List<CommonDocsEntity> findAllBySurrenderIdAndIsActiveTrue(Long surrenderId);


	List<CommonDocsEntity> findAllByMergeIdAndIsActiveTrue(Long mergerId);

}
