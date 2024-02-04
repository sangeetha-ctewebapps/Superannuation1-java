package com.lic.epgs.policy.dto;

import java.io.Serializable;


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
public class ProposalChannelDetailsDto implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private Integer proposalChannelId;
	
    private String proposalId;
	
	private String unitOfficeId;
	
	
	
	private String marketingOfficerCode;
	
	
	private String marketingOfficerName ;
	
	
	
	private String leadSourseType;
	
	
	private String leadSourseName;
	
	
	private String createdBy;
	
	
	private String createdOn;
	
	
	private String modifiedBy;
	
	
	private String modifiedOn;
	
	private String intermediaryCode;
	private String intermediaryName;
	private String intermediaryId;
	private String intermediaryContactNo;
	private String intermediaryEmailAddress;
	
	
	


	
	
	

	
}
