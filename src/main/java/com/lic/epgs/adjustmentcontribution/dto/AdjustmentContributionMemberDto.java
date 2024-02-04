package com.lic.epgs.adjustmentcontribution.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdjustmentContributionMemberDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long memberId;
	private Long adjustmentContributionId;
	
	private String memberShipId;
	private String membershipNumber;
	private String memberStatus;
	private String licId;
	
	private BigDecimal employerContribution= BigDecimal.ZERO;
	private BigDecimal employeeContribution= BigDecimal.ZERO;
	private BigDecimal voluntaryContribution= BigDecimal.ZERO;
	private BigDecimal totalContribution= BigDecimal.ZERO;
	private BigDecimal totalInterestedAccured= BigDecimal.ZERO;
	
	private String unitCode;
	private String createdBy;
	private Date createdOn = new Date();
	private String modifiedBy;
	private Date modifiedOn = new Date();
	private Boolean isActive = Boolean.TRUE;
	
	private Long policyId;
	private String policyNumber;
	private String policyStatus;
	 
	private String proposalNumber;
	
	private Boolean isZeroId;
	private String zeroId;
	private String zeroIdStatus;
	private BigDecimal zeroAvailableAmount= BigDecimal.ZERO;
	private BigDecimal zeroUsedAmount= BigDecimal.ZERO;
}