package com.lic.epgs.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.entity.ClaimPayeeBankDetailsEntity;

@Repository
public interface ClaimPayeeBankDetailsRepository extends JpaRepository<ClaimPayeeBankDetailsEntity, Long>{

}
