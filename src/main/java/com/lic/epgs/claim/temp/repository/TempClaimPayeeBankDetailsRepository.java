	package com.lic.epgs.claim.temp.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.ClaimPayeeTempBankDetailsEntity;

@Repository
public interface TempClaimPayeeBankDetailsRepository extends JpaRepository<ClaimPayeeTempBankDetailsEntity, Long> {

	ClaimPayeeTempBankDetailsEntity findByBankAccountId(Long bankAccountId);

	List<ClaimPayeeTempBankDetailsEntity> findAll(Specification<ClaimPayeeTempBankDetailsEntity> specification);

	ClaimPayeeTempBankDetailsEntity findByNomineeCode(String nomineeCode);

}