package com.lic.epgs.topupda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.topupda.entity.TopupdaTempEntity;
/**
*
* @author Dhanush
*
*/

@Repository
public interface TopupdaTempRepository  extends JpaRepository<TopupdaTempEntity, Long>{

	TopupdaTempEntity findByTopupIdAndIsActiveTrue(Long topupId);

}
	