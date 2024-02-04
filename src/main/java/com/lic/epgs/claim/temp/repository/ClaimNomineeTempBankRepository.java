package com.lic.epgs.claim.temp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.claim.temp.entity.ClaimNomineeTempBankEntity;
@Repository
public interface ClaimNomineeTempBankRepository extends JpaRepository<ClaimNomineeTempBankEntity, Long>{

	List<ClaimNomineeTempBankEntity> findByNomineeId(String nomineeCode);

}
