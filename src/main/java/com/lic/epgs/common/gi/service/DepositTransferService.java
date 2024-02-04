package com.lic.epgs.common.gi.service;

import com.lic.epgs.common.gi.dto.DepositTransferDto;
import com.lic.epgs.common.gi.dto.GiServiceRequestDto;

import io.swagger.v3.oas.annotations.servers.Servers;

/**
 * @author Ramprasad
 *
 */
@Servers
public interface DepositTransferService {

	DepositTransferDto giPolicySearch(GiServiceRequestDto dto);
}
