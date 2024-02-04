package com.lic.epgs.adjustmentcontribution.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
public class AdjustmentContributionDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long batchId;

	private Long adjustmentContributionId;
	private String adjustmentContributionNumber;
	private String adjustmentContributionStatus;
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
	private Boolean isCommencementdateOneYr;
	
	
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
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date adjustmentForDate;
	
	private BigDecimal amountToBeAdjusted= BigDecimal.ZERO;
	private BigDecimal firstPremium= BigDecimal.ZERO;
	private BigDecimal singlePremiumFirstYr= BigDecimal.ZERO;
	private BigDecimal renewalPremium= BigDecimal.ZERO;
	private BigDecimal subsequentSinglePremium= BigDecimal.ZERO;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date adjustmentDueDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date effectiveDate;
	
	private String lastPremiumDueDate;
		
	private Set<PolicyDepositDto> adjustmentContibutionDeposits = new HashSet<>();
	private Set<PolicyDepositDto> adjustmentContibutionDepositAdjustments = new HashSet<>();	
	private Set<AdjustmentContributionMemberDto> adjustmentContibutionMembers = new HashSet<>();
	private Set<AdjustmentContributionNotesDto> adjustmentContributionNotes = new HashSet<>();

}