package com.lic.epgs.policy.dto;
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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyContributionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long contributionId;
	private Long policyId;
	private Long masterPolicyId;
	private Long tempPolicyId;
	private Long adjustmentContributionId;
	private Long regularContributionId;
	private String contributionType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date contributionDate;
	private String contReferenceNo;
	private Integer versionNo;
	private String financialYear;
	private String quarter;
	private BigDecimal openingBalance;
	private BigDecimal closingBalance;
	private BigDecimal employerContribution;
	private BigDecimal employeeContribution;
	private BigDecimal voluntaryContribution;
	private BigDecimal totalContribution;
	private Boolean isDeposit;
	private Boolean isActive;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;

	private Boolean txnEntryStatus;

	private Set<MemberContributionDto> memberContribution = new HashSet<>();
	private Set<ZeroAccountEntriesDto> zeroAccountEntries = new HashSet<>();

}
