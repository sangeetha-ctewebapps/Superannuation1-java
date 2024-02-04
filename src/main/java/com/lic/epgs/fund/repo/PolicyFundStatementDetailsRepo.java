/**
 * 
 */
package com.lic.epgs.fund.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.fund.entity.PolicyFundStatementDetailsEntity;

/**
 * @author Muruganandam
 *
 */
public interface PolicyFundStatementDetailsRepo extends JpaRepository<PolicyFundStatementDetailsEntity, Long> {

}
