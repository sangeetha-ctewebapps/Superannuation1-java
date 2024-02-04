package com.lic.epgs.policy.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProposalAnnuityDto {
	
	private String proposalNumber;
	private CustomerBasicDetailsDto customerBasicDetails;
	private List<MPHAddressDetailsDto> mphAddressDetails;
	private List<MPHContactPersonDetailsDto> mphContactDetails;
	private List<MPHBankAccountDetailsDto> mphBankAccountDetails;
	private ProposalBasicDetailsDto proposalBasicDetails;
	private ProposalChannelDetailsDto proposalChannelDetails;
	private ProposalProductDetailsDto proposalProductDetailsDto;

}
