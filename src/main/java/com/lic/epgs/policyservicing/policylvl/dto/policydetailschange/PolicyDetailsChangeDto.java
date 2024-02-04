package com.lic.epgs.policyservicing.policylvl.dto.policydetailschange;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.policy.dto.MphAddressDto;
import com.lic.epgs.policy.dto.MphBankDto;
import com.lic.epgs.policy.dto.PolicyDepositDto;
import com.lic.epgs.policy.dto.PolicyNotesDto;
import com.lic.epgs.policy.dto.PolicyRulesDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDocumentDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.policylvl.dto.memberaddition.PolicyServiceMbrDto;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelServiceDto;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDetailsChangeDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long policyDtlsId;
	private Long policyId;
	
	
	private Set<PolicyRulesDto> rules = new HashSet<>();
	private Set<MphAddressDto> addresses = new HashSet<>();
	private Set<MphBankDto> bankDetails = new HashSet<>();
	private Set<PolicyServiceDocumentDto> docs = new HashSet<>();
	private Set<PolicyNotesDto> notes = new HashSet<>();
	private Set<PolicyDepositDto> deposit = new HashSet<>();
//	private Set<PolicyAdjustmentDto> adjustments = new HashSet<>();
	
	private Set<PolicyServiceNotesDto> serviceNotes = new HashSet<>();
	private Set<PolicyLevelServiceDto> service = new HashSet<>();
	private Set<PolicyServiceMbrDto> members = new HashSet<>();

	private String policyStatus;

	private Integer quotationId;

	private String proposalNumber;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyCommencementDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyAdjustmentDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyEndDt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyDepositDate;

	private String mphCode;

	private String mphName;
	
	private Long mphId;

	private String customerName;

	private String customerCode;

	private String pan;

	private String apan;

	private Integer cin;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date ard;

	private Integer tax;

	private String contactNo;

	private String emailId;

	private Integer numberOfLives;

	private String product;

	private String variant;

	private String lineOfBusiness;

//	private String unit;
//
//	private String unitCode;

	private String marketingOfficerName;

	private String marketingOfficerCode;

	private String intermediaryOfficerName;

	private String intermediaryOfficerCode;

	private Integer attritionRate;

	private Integer salaryEscalation;

	private Integer deathRate;

	private Integer disRateIntrest;

	private String annuityOption;

	private Integer accuralRateFactor;

	private Integer minPension;

	private Integer maxPension;

	private Integer withdrawalRate;

	private BigDecimal quotationPremiumAmount;

	private Integer employerContribution;

	private Integer employeeContribution;

	private Integer voluntaryContribution;

	private Integer totalContribution;

	private Integer totalMember;

	private String modifiedBy;

	private Date modifiedOn = new Date();

	private String createdBy;

	private Date createdOn = new Date();

	private Integer disRateSalaryEsc;

	private Integer frequency;

	private Integer category;

	private String proposalName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date quotationDate;

	private String type;

	private BigDecimal stampDuty;

	private String valuationType;

	private Integer proposalId;

	private String quotationType;

	private Integer leadId;

	private BigDecimal totalDeposit;

	private Integer customerId;

	private String policyNumber;

	private String workFlowStatus;

	private String status;

	private String quotationNo;

	private Boolean isActive;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyDispatchDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyRecievedDate;
	

	private String rejectionReasonCode;
	
	private String rejectionRemarks;
	
	private Long serviceId;
	
	private String serviceNo;
	
	private Integer serviceStatus;
	
	private String unitOffice;
	
	private String unitId;

}
