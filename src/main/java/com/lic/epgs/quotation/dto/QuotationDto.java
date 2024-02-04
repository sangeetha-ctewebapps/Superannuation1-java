package com.lic.epgs.quotation.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;
import com.lic.epgs.common.dto.CommonRuleDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer quotationId;
	private Set<CommonRuleDto> rules = new HashSet<>();
	private List<CommonDocsDto> docs = new ArrayList<>();
	private List<CommonNotesDto> notes = new ArrayList<>();
	private Set<LiabilityDto> liabilities = new HashSet<>();
	private Set<QuotationMemberBatchDto> batches = new HashSet<>();
	
	private Set<QuotationHistoryDto> history = new HashSet<>();
	private Set<QuotationMemberDto> members = new HashSet<>();
	
	private String quotationType;
	private Integer status =1;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date quotationDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date policyCommencementDate;
	private String proposalNumber;
	private String proposalName;
	private String mphCode;
	private String mphName;
	private Integer frequency;
	private Integer noOfLives;
	private String lineOfBusiness;
	private String product;
	private String variant;
	private Integer noOfCategory;
	private Integer stampDuty;
	private String valuationType;
	private Boolean reportReceived;
	private Integer attritionRate;
	private Integer salaryEscalation;
	private Integer deathRate;
	private Integer disRateSalaryEsc;
	private String annuityOption;
	private Integer accuralRateFactor;
	private Integer minPension;
	private Integer maxPension;
	private Integer withdrawalRate;
	private BigDecimal employerContribution;
	private BigDecimal employeeContribution;
	private BigDecimal voluntaryContribution;
	private BigDecimal totalContribution;
	private Integer totalMember;
	private String unitCode;
	private String quotationNo;
	private String rejectionRemarks;
	private String rejectionReasonCode;
	private String contributionType;
	private String advanceOrArrears;
	private String modifiedBy;
	private String createdBy;
	private Date modifiedOn;
	private Date createdOn;
	private Boolean isActive;
	private String productType;
	private String leadNumber;
	private String componentLabel;
}
