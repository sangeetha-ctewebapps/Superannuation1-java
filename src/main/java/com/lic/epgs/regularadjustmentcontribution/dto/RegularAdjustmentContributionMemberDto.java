package com.lic.epgs.regularadjustmentcontribution.dto;
/**
 * @author dhanush
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
public class RegularAdjustmentContributionMemberDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long memberId;
	private Long adjustmentContributionId;
	private String memberShipId;
	private String membershipNumber;
	private String memberStatus;
	private String licId;
	private BigDecimal employerContribution;
	private BigDecimal employeeContribution;
	private BigDecimal voluntaryContribution;
	private BigDecimal totalContribution;
	private BigDecimal totalInterestedAccured;
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
	private BigDecimal zeroAvailableAmount;
	private BigDecimal zeroUsedAmount;
}