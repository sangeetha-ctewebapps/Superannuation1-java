package com.lic.epgs.policyservicing.memberlvl.transferout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policyservicing.memberlvl.transferout.entity.TransferInAndOutMasterTempEntity;

@Repository
public interface  TransferInAndOutMasterTempRepository extends JpaRepository<TransferInAndOutMasterTempEntity , Long> {

	TransferInAndOutMasterTempEntity findByTrnsfrIdAndIsActiveTrue(Long trnsfrId);
	TransferInAndOutMasterTempEntity findByTrnsfrIdAndUnitCodeAndIsActiveTrue(Long trnsfrId, String unitCode);
	
	List<TransferInAndOutMasterTempEntity> findAllByTrnsfrStatusInAndUnitCodeAndIsActiveTrueOrderByModifiedOnDesc(
			List<String> inprogressChecker, String unitCode);

}
