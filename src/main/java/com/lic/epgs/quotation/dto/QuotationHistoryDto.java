/**
 * 
 */
package com.lic.epgs.quotation.dto;

import java.math.BigDecimal;
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
public class QuotationHistoryDto {
	
	
	private Long historyId;
	private Integer quotationId;
	private Boolean active = Boolean.TRUE;
	private String quotationType;
	private Integer status;
	private Date quotationDate;
	private String quotationNo;
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
	private BigDecimal employerContribution= BigDecimal.ZERO;
	private BigDecimal employeeContribution= BigDecimal.ZERO;
	private BigDecimal voluntaryContribution= BigDecimal.ZERO;
	private BigDecimal totalContribution= BigDecimal.ZERO;
	private Integer totalMember;
	private String modifiedBy;
	private Date modifiedOn;
	private String createdBy;
	private Date createdOn;
	private Date policyCommencementDate;
	private Boolean isActive;
	private String unitCode;
	private String rejectionReasonCode;
	private String rejectionRemarks;

}
