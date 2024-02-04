package com.lic.epgs.topupda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lic.epgs.topupda.entity.TopupdaNotesTempEntity;

/**
*
* @author Dhanush
*
*/

public interface TopupdaNotesTempRepository extends JpaRepository<TopupdaNotesTempEntity, Long> {

	List<TopupdaNotesTempEntity> findAllByTopupIdAndIsActiveTrue(Long topupId);

}
