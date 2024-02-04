/**
 * 
 */
package com.lic.epgs.fund.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.fund.entity.PolicyFundStatementSummaryEntity;

/**
 * @author Muruganandam
 *
 */
public interface PolicyFundStatementSummaryRepo extends JpaRepository<PolicyFundStatementSummaryEntity, Long> {

}
