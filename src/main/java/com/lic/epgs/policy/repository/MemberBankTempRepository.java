package com.lic.epgs.policy.repository;
/**
 * @author pradeepramesh
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.entity.MemberBankTempEntity;
@Repository
public interface MemberBankTempRepository extends JpaRepository<MemberBankTempEntity, Long> {

}
