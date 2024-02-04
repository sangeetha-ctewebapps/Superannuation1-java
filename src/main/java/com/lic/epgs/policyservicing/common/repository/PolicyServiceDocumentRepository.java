/**
 * 
 */
package com.lic.epgs.policyservicing.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceDocumentEntity;

/**
 * @author Karthick M
 *
 */

@Repository
public interface PolicyServiceDocumentRepository extends JpaRepository<PolicyServiceDocumentEntity, Long>{
	
	List<PolicyServiceDocumentEntity> findAllByConversionId(Long conversionId);

}
