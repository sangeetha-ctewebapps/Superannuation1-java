package com.lic.epgs.quotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.quotation.entity.QuotationMemberAddressTempEntity;

@Repository
public interface QuotationMemberAddressTempRepository extends JpaRepository<QuotationMemberAddressTempEntity, Long> {

	QuotationMemberAddressTempEntity findByMemberIdAndAddressIdAndIsActiveTrue(Long memberId, Long addressId);

}
