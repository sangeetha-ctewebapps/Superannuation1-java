
package com.lic.epgs.policyservicing.policylvl.repository.freelookcancellation;

import java.util.List;

/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.common.entity.PolicyServiceDocumentEntity;

@Repository
public interface FreeLookCancellationDocumentRepository extends JpaRepository< PolicyServiceDocumentEntity, Long> {

	PolicyServiceDocumentEntity findByDocumentIdAndFreeLookId(Long documentId, Long freeLookId);

	List<PolicyServiceDocumentEntity> findByFreeLookIdAndIsActiveTrue(Long freeLookcancellationId);


}
