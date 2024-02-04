package com.lic.epgs.notifyDomain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lic.epgs.notifyDomain.entity.NotifyDomainEntity;


@Repository
public interface NotifyDomainRepository extends JpaRepository<NotifyDomainEntity,Long> {

}
