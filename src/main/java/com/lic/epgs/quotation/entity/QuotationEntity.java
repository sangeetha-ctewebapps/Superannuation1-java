package com.lic.epgs.quotation.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lic.epgs.common.entity.CommonDocsEntity;
import com.lic.epgs.common.entity.CommonNoteEntity;
import com.lic.epgs.common.entity.CommonRuleEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author KrunalGothiwala
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "QUOTATION")
public class QuotationEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUOTATION_SEQ")
	@SequenceGenerator(name = "QUOTATION_SEQ", sequenceName = "QUOTATION_SEQ", allocationSize = 1)
	@Column(name = "QUOTATION_ID", length = 10)
	private Integer quotationId;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "QUOTATION_ID", referencedColumnName = "QUOTATION_ID")
	private Set<CommonRuleEntity> rules = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "QUOTATION_ID", referencedColumnName = "QUOTATION_ID")
	private List<CommonDocsEntity> docs = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "QUOTATION_ID", referencedColumnName = "QUOTATION_ID")
	private List<CommonNoteEntity> notes = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "QUOTATION_ID", referencedColumnName = "QUOTATION_ID")
	private Set<QuotationLiabilityEntity> liabilities = new HashSet<>();
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "QUOTATION_ID", referencedColumnName = "QUOTATION_ID")
	private Set<QuotationHistoryEntity> history = new HashSet<>();
	
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "QUOTATION_ID", referencedColumnName = "QUOTATION_ID")
	private Set<QuotationMemberEntity> members = new HashSet<>();

	@Column(name = "TYPE")
	private String quotationType;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "QUOTATION_DATE")
	private Date quotationDate;

	@Column(name = "QUOTATION_NO")
	private String quotationNo;

	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;

	@Column(name = "PROPOSAL_NAME")
	private String proposalName;

	@Column(name = "MPH_CODE")
	private String mphCode;

	@Column(name = "MPH_NAME")
	private String mphName;

	@Column(name = "FREQUENCY")
	private Integer frequency;

	@Column(name = "NO_OF_LIVES")
	private Integer noOfLives;

	@Column(name = "LINE_OF_BUSINESS")
	private String lineOfBusiness;

	@Column(name = "PRODUCT")
	private String product;
	
	@Column(name = "PRODUCT_TYPE")
	private String productType;

	@Column(name = "VARIANT")
	private String variant;

	@Column(name = "NO_OF_CATEGORY")
	private Integer noOfCategory;

	@Column(name = "STAMP_DUTY")
	private Integer stampDuty;

	@Column(name = "VALUATION_TYPE")
	private String valuationType;
	
	@Column(name = "REPORT_REVEIVED")
	private Boolean reportReceived;

	@Column(name = "CON_TYPE")
	private String contributionType;
	
	@Column(name = "ADVANCEOTARREARS")
	private String advanceOrArrears;
	
	@Column(name = "ATTRITION_RATE")
	private Integer attritionRate;

	@Column(name = "SALARY_ESCALATION")
	private Integer salaryEscalation;

	@Column(name = "DEATH_RATE")
	private Integer deathRate;

	@Column(name = "DIS_RATE_SALARY_ESC")
	private Integer disRateSalaryEsc;

	@Column(name = "ANNUITY_OPTION")
	private String annuityOption;

	@Column(name = "ACCURAL_RATE_FACTOR")
	private Integer accuralRateFactor;

	@Column(name = "MIN_PENSION")
	private Integer minPension;

	@Column(name = "MAX_PENSION")
	private Integer maxPension;

	@Column(name = "WITHDRAWAL_RATE")
	private Integer withdrawalRate;

	@Column(name = "EMPLOYER_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employerContribution= BigDecimal.ZERO;

	@Column(name = "EMPLOYEE_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employeeContribution= BigDecimal.ZERO;

	@Column(name = "VOLUNTARY_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal voluntaryContribution= BigDecimal.ZERO;

	@Column(name = "TOTAL_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal totalContribution= BigDecimal.ZERO;

	@Column(name = "TOTAL_MEMBER")
	private Integer totalMember;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "POLICY_COMMENCEMENT")
	private Date policyCommencementDate;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	@Column(name = "UNIT_CODE")
	private String unitCode;

	@Column(name = "REJECTION_REASON_CODE")
	private String rejectionReasonCode;
	@Column(name = "REJECTION_REMARKS")
	private String rejectionRemarks;
	
	@Column(name = "LEAD_NUMBER")
	private String leadNumber;
	
	
}
