package com.lic.epgs.integration.service;

import com.lic.epgs.integration.dto.AccountingIntegrationRequestDto;
import com.lic.epgs.integration.dto.ResponseDto;
import com.lic.epgs.integration.dto.SuperAnnuationResponseModel;

public interface AccountingIntegrationService {
	
	SuperAnnuationResponseModel getMphAndIcodeDetail(AccountingIntegrationRequestDto accountingIntegrationRequestDto);
	
	ResponseDto commonmasterserviceUnitByCode(String unitCode);

}
