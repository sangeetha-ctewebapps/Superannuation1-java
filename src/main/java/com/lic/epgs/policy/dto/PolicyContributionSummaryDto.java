package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyContributionSummaryDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long polContSummaryId;
	private Long policyId;
	
	private BigDecimal stampDuty;
	private BigDecimal openingBalance;
	private BigDecimal totalEmployerContribution;
	private BigDecimal totalEmployeeContribution;
	private BigDecimal totalVoluntaryContribution;
	private BigDecimal totalContribution;
	private BigDecimal closingBalance;
	private Boolean isActive;
	private String financialYear;
	private String quarter;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	private String modifiedBy;
}
