/**
 * 
 */
package com.lic.epgs.policyservicing.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceDocumentTempEntity;

/**
 * @author Karthick M
 *
 */

@Repository
public interface PolicyServiceDocumentTempRepository extends JpaRepository<PolicyServiceDocumentTempEntity, Long>{

	PolicyServiceDocumentTempEntity findByDocumentIdAndConversionIdAndIsActiveTrue(Long docId, Long conversionId);

	List<PolicyServiceDocumentTempEntity> findAllByConversionIdAndIsActiveTrue(Long conversionId);

	PolicyServiceDocumentTempEntity findByDocumentIdAndFreeLookId(Long documentId, Long freeLookId);

	List<PolicyServiceDocumentTempEntity> findByFreeLookIdAndIsActiveTrue(Long freeLookcancellationId);


}
