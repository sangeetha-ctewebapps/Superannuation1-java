/**
 * 
 */
package com.lic.epgs.quotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.quotation.entity.QuotationHistoryEntity;

/**
 * @author Karthick M
 *
 */


@Repository
public interface QuotationHistoryRepository extends JpaRepository<QuotationHistoryEntity, Integer>{

}
