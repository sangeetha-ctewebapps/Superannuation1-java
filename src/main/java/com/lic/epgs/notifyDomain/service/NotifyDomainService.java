package com.lic.epgs.notifyDomain.service;

import com.lic.epgs.notifyDomain.dto.CommonResponseDto;
import com.lic.epgs.notifyDomain.dto.NotifyDomainDto;

public interface NotifyDomainService {
	
	CommonResponseDto notifyDomain(NotifyDomainDto notifyDomainDto);

}
