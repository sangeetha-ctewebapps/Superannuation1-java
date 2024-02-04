package com.lic.epgs.policy.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pradeepramesh
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyMasterDto implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private Long policyId;
	private Long masterPolicyId;
	private Long tempPolicyId;
	
	private Long mphId;
	private Long masterMphId;
	private Long tempMphId;
	
	private Integer proposalId;
	private Integer quotationId;
	private Integer leadId;
	
	private String policyNumber;
	private String policyStatus;
	private String policyType;
	
	private String unitId;
	private String unitOffice;
	
	private BigDecimal stampDuty;
	private Integer noOfCategory;
	private Integer contributionFrequency;
	
	private String intermediaryOfficerCode;
	private String intermediaryOfficerName;
	private String marketingOfficerCode;
	private String marketingOfficerName;
	private String lineOfBusiness;
	
	private Long productId;
	private String variant;
	
	private Integer totalMember;
	
	private String conType;
	private String advanceotarrears;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyDispatchDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyRecievedDate;
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date ard;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date policyCommencementDt;
	private Boolean isCommencementdateOneYr;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date adjustmentDt;
	
	private BigDecimal amountToBeAdjusted;
	
	private BigDecimal firstPremium;
	private BigDecimal singlePremiumFirstYr;
	
	private BigDecimal renewalPremium;
	private BigDecimal subsequentSinglePremium;
	
	private Boolean isActive;
	
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	
	private String workflowStatus;
	
	private String rejectionReasonCode;
	private String rejectionRemarks;

	private Set<PolicyValuationDto> valuations = new HashSet<>();
	private Set<PolicyRulesDto> rules = new HashSet<>();
	private Set<PolicyDepositDto> deposits = new HashSet<>();
	private Set<PolicyContributionDto> policyContributions = new HashSet<>();
	private Set<PolicyContributionSummaryDto> policyContributionSummary = new HashSet<>();
	private Set<PolicyTransactionEntriesDto> policyTransactionEntries = new HashSet<>();
	private Set<PolicyTransactionSummaryDto> policyTransactionSummary = new HashSet<>();
	private Set<ZeroAccountdto> zeroAccount = new HashSet<>();
	private Set<ZeroAccountEntriesDto> zeroAccountEntries = new HashSet<>();
	private Set<PolicyNotesDto> notes = new HashSet<>();
	private Set<MemberMasterDto> memberMaster = new HashSet<>();

}