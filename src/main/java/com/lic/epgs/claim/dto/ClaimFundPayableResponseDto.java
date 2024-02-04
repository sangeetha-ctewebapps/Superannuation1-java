package com.lic.epgs.claim.dto;

import java.io.Serializable;

import com.lic.epgs.claim.temp.entity.TempClaimMbrNomineeEntity;
import com.lic.epgs.policy.dto.MphBankDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:02-01-2023
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClaimFundPayableResponseDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private MphBankDto mphBank;
	private ClaimMbrBankDetailDto claimMbrBankDetail;
	private ClaimMbrNomineeDto claimNomineeBankDetails;

} 
