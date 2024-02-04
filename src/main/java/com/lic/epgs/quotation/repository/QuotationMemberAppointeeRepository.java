package com.lic.epgs.quotation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.quotation.entity.QuotationMemberAppointeeEntity;

@Repository
public interface QuotationMemberAppointeeRepository extends JpaRepository<QuotationMemberAppointeeEntity, Long> {

}
