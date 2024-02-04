package com.lic.epgs.policyservicing.policylvl.repository.freelookcancellation;

/**
 * @author pradeepramesh
 *
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.policylvl.entity.freelookcancellation.FreeLookCancellationEntity;

@Repository
public interface FreeLookCancellationRepository extends JpaRepository<FreeLookCancellationEntity, Long> {

}
