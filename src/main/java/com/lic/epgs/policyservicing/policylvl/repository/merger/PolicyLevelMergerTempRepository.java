package com.lic.epgs.policyservicing.policylvl.repository.merger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.policylvl.entity.merger.PolicyLevelMergerTempEntity;

@Repository
public interface PolicyLevelMergerTempRepository  extends JpaRepository<PolicyLevelMergerTempEntity, Long>{

}
