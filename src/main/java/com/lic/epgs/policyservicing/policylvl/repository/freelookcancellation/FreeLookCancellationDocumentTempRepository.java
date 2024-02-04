package com.lic.epgs.policyservicing.policylvl.repository.freelookcancellation;

import java.util.List;

/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceDocumentTempEntity;

@Repository
public interface FreeLookCancellationDocumentTempRepository extends JpaRepository<PolicyServiceDocumentTempEntity, Long> {

	PolicyServiceDocumentTempEntity findByDocumentIdAndFreeLookId(Long documentId, Long freeLookId);

	List<PolicyServiceDocumentTempEntity> findByFreeLookIdAndIsActiveTrue(Long freeLookcancellationId);


}
