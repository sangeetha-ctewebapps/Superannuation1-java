package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.common.entity.CommonDocsTempEntity;

public interface CommonDocsTempRepository extends JpaRepository<CommonDocsTempEntity,Long>{

	CommonDocsTempEntity findAllByQuotationId(Integer quotationId);
	
	List<CommonDocsTempEntity> findAllBySurrenderId(Long surrenderId);

//	CommonDocsTempEntity findByQuotationIdAndIsActiveTrue(Integer quotationId);

	CommonDocsTempEntity findByDocIdAndQuotationIdAndIsActiveTrue(Integer stringToInteger, Integer stringToInteger2);

	List<CommonDocsTempEntity> findAllByQuotationIdAndIsActiveTrue(Integer quotationId);

	CommonDocsTempEntity findByDocIdAndIsActiveTrue(Integer docId);

	CommonDocsTempEntity findByDocIdAndMergeIdAndIsActiveTrue(Integer docId, Long mergerId);
	
	CommonDocsTempEntity findByDocIdAndSurrenderIdAndIsActiveTrue(Integer docId,Long surrenderId);
	
	List<CommonDocsTempEntity> findAllBySurrenderIdAndIsActiveTrue(Long surrenderId);

	List<CommonDocsTempEntity> findAllByConversionIdAndIsActiveTrue(Long conversionId);

	CommonDocsTempEntity findByDocIdAndConversionIdAndIsActiveTrue(Integer docId, Long conversionId);
	
	List<CommonDocsTempEntity> findAllByConversionId(Long conversionId);
	
	
	

	List<CommonDocsTempEntity> findAllByQuotationIdAndIsActiveTrueOrderByQuotationIdDesc(Integer quotationId);

}
