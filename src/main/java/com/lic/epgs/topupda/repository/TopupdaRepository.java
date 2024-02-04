package com.lic.epgs.topupda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.topupda.entity.TopupdaEntity;

/**
*
* @author Dhanush
*
*/

public interface TopupdaRepository  extends JpaRepository<TopupdaEntity, Long>{

	TopupdaEntity findByQuotationNumberAndIsActiveTrue(String quotationNumber);

}
