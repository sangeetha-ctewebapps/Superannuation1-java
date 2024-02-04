package com.lic.epgs.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.common.entity.CommonDocumentMasterEntity;

@Repository
public interface CommonDocumentMasterRepository extends JpaRepository<CommonDocumentMasterEntity,Long> 
{

	List<CommonDocumentMasterEntity> findByProductIdAndVariantIdAndDocumentCategoryAndDocumentSubCategoryAndIsActiveTrue(
			Long productId, Long variantId, String documentCategory, String documentSubCategory);
	

}
