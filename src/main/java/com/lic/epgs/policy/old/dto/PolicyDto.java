package com.lic.epgs.policy.old.dto;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long policyId;
	private Long tempPolicyId;
	private Long masterPolicyId;
	private Long mphId;
	private Long tempMphId;
	private Long masterMphId;
	private Long valuationId;
	
	
	private Set<PolicyRulesOldDto> rules = new HashSet<>();
	private Set<PolicyAddressOldDto> addresses = new HashSet<>();
	private Set<PolicyBankOldDto> bankDetails = new HashSet<>();
	private Set<PolicyNotesOldDto> notes = new HashSet<>();
	private Set<PolicyDepositOldDto> deposit = new HashSet<>();
	private Set<PolicyAdjustmentOldDto> adjustments = new HashSet<>();
	private Set<PolicyMbrOldDto> members = new HashSet<>();
	
	
	private Integer proposalId;
	private String proposalName;	
	private String proposalNumber;
	
	private Integer leadId;
	private Integer leadNumber;
	
	private Integer customerId;
	private String customerName;
	private String customerCode;
	
	private String mphCode;
	private String mphName;
	
	private String product;
	private String variant;
	private String lineOfBusiness;
	
	private String variantType;
	
	private String marketingOfficerName;
	private String marketingOfficerCode;
	
	private String intermediaryOfficerName;
	private String intermediaryOfficerCode;
	
	private String unitCode;
	
	private String policyNumber;
	private String policyStatus;
	private String workFlowStatus;
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date policyCommencementDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyEndDt;
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date policyAdjustmentDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyDepositDate;
	
	private BigDecimal amountToBeAdjusted= BigDecimal.ZERO;
	private BigDecimal firstPremium= BigDecimal.ZERO;
	private BigDecimal singlePremiumFirstYr= BigDecimal.ZERO;
	private BigDecimal renewalPremium= BigDecimal.ZERO;
	private BigDecimal subsequentSinglePremium= BigDecimal.ZERO;
	private Boolean isCommencementdateOneYr;
	
	private String quotationNo;
	private Integer quotationId;
	private String quotationType;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date quotationDate;
	
	private String valuationType;
	private Integer attritionRate;
	private Integer salaryEscalation;
	private Integer deathRate;
	private Integer disRateIntrest;
	private String annuityOption;
	private Integer accuralRateFactor;
	private Integer minPension;
	private Integer maxPension;
	private Integer withdrawalRate;
	private Integer disRateSalaryEsc;
	private Integer numberOfLives;
	private BigDecimal quotationPremiumAmount= BigDecimal.ZERO;
	
	private Integer totalMember;
	
	private BigDecimal totalContribution= BigDecimal.ZERO;
	private BigDecimal employerContribution= BigDecimal.ZERO;
	private BigDecimal employeeContribution= BigDecimal.ZERO;
	private BigDecimal voluntaryContribution= BigDecimal.ZERO;
	
	private String pan;
	private String apan;
	
	private Integer cin;
	private Integer fax;
	
	private String contactNo;
	private String emailId;
	
	private Integer frequency;
	private Integer category;
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date ard;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyDispatchDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyRecievedDate;
	
	private String type;
	
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private Boolean isActive = Boolean.TRUE;
	
	private String rejectionReasonCode;
	private String rejectionRemarks;
	
	private Long serviceId;
	private String serviceNo;
	private Integer serviceStatus;
	
	private BigDecimal totalDeposit = BigDecimal.ZERO;
	private BigDecimal stampDuty= BigDecimal.ZERO;
		
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date cancelRequestDate;
	private String contributionType;
	private String advanceOrArrears;
	private String unitOffice;
	
	private String lastPremiumDueDate;
	



}