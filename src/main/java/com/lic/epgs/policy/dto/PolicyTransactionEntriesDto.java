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
public class PolicyTransactionEntriesDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long polTranId;
	private Long policyId;
	private String transationType;
	private String entryType;
	private BigDecimal openingBalance;
	private BigDecimal employerContribution;
	private BigDecimal employeeContribution;
	private BigDecimal voluntaryContribution;
	private BigDecimal totalContribution;
	private Boolean isActive;
	private BigDecimal closingBalance;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date transactionDate;
	private String financialYear;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	private String modifiedBy;
}
