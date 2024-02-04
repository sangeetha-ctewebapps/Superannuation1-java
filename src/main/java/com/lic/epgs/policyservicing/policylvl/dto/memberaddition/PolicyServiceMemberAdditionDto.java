/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.dto.memberaddition;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Karthick M
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyServiceMemberAdditionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String role;
	private String pageName;
	
	private Long memberAdditionId;

//	private Set<PolicyServiceMemberTempEntity> policyServiceMemberTemp = new HashSet<>();
//	
//	private Set<PolicyServiceNotesTempEntity> policyServiceNotesTemp = new HashSet<>();
	
	private String memberAdditionStatus;
	
	private Long serviceId;
	private String serviceNumber;
	private String serviceStatus;
	
	private Long policyId;
	private String policyNumber;
	private String policyStatus;
	
	private String mphCode;
	private String mphName;
	private String product;
	private String lineOfBusiness;
	
	private Long employeeContribution;
	private Long employerContribution;
	private Long voluntaryContribution;
	private Long totalContribution;
	
	private String modifiedBy;
	private Date modifiedOn = new Date();
	private String createdBy;
	private Date createdOn = new Date();
	private Boolean isActive;
	
	private String unitCode;
	
	private String rejectionReasonCode;
	private String rejectionRemarks;
	
	private String policyMemberNo;

}
