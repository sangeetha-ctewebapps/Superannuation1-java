package com.lic.epgs.quotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.quotation.entity.QuotationMemberNomineeTempEntity;

@Repository
public interface QuotationMemberNomineeTempRepo extends JpaRepository<QuotationMemberNomineeTempEntity, Long>{

	QuotationMemberNomineeTempEntity findByMemberIdAndNomineeIdAndIsActiveTrue(Long nomineeId, Long nomineeId2);

}
