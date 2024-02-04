package com.lic.epgs.regularadjustmentcontribution.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lic.epgs.policy.dto.PolicyDepositDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegularAdjustmentContributionDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long batchId;

	private Long regularContributionId;
	private String regularContributionNumber;
	private String regularContributionStatus;
	
	private String workFlowStatus;	
	private String unitCode;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private Boolean isActive = Boolean.TRUE;
	
	private String rejectionReasonCode;
	private String rejectionRemarks;
	
	private Long policyId;
	private Long tempPolicyId;
	private String policyNumber;
	private String policyStatus;
	private String policyType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyCommencementDate;
	
	private Integer customerId;
	private String customerCode;
	private String customerName;
	private String mphName;
	private String mphCode;
	private String proposalNumber;
	private String product;
	private String variant;
	private String variantType;
	private BigDecimal totalContribution= BigDecimal.ZERO;
	private BigDecimal employerContribution= BigDecimal.ZERO;
	private BigDecimal employeeContribution= BigDecimal.ZERO;
	private BigDecimal voluntaryContribution= BigDecimal.ZERO;
	private BigDecimal totalDeposit = BigDecimal.ZERO;
	private Boolean isDeposit;
	
	
	private BigDecimal amountToBeAdjusted;
	private BigDecimal firstPremium;
	private BigDecimal singlePremiumFirstYr;
	private BigDecimal renewalPremium;
	private BigDecimal subsequentSinglePremium;
	private Boolean isCommencementdateOneYr;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date adjustmentForDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date adjustmentDueDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date effectiveDate;
	
	
	private String lastPremiumDueDate;
	private Set<PolicyDepositDto> regAdjustmentContibutionDeposits = new HashSet<>();
	private Set<PolicyDepositDto> regAdjustmentContibutionDepositAdjustments = new HashSet<>();
	private Set<RegularAdjustmentContributionMemberDto> regAdjustmentContibutionMembers = new HashSet<>();
	private Set<RegularAdjustmentContributionNotesDto> regAdjustmentContributionNotes = new HashSet<>();

}