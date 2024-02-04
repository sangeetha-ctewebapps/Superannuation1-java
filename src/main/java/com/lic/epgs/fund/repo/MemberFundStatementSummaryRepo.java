/**
 * 
 */
package com.lic.epgs.fund.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.fund.entity.MemberFundStatementSummaryEntity;

/**
 * @author Muruganandam
 *
 */
public interface MemberFundStatementSummaryRepo extends JpaRepository<MemberFundStatementSummaryEntity, Long> {

}
